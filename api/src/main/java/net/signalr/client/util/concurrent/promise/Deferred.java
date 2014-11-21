/*
 * Copyright © Martin Tamme
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.signalr.client.util.concurrent.promise;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a deferred.
 * 
 * @param <T> The value type.
 */
public final class Deferred<T> implements Promise<T>, Completable<T> {

    /**
     * The private logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Deferred.class);

    /**
     * The state of the deferred.
     */
    private final AtomicReference<State<T>> _state;

    /**
     * Initializes a new instance of the {@link Deferred} class.
     */
    public Deferred() {
        final State<T> initialState = new PendingState();

        _state = new AtomicReference<State<T>>(initialState);
    }

    /**
     * Initializes a new instance of the {@link Deferred} class.
     * 
     * @param value The value.
     */
    public Deferred(final T value) {
        final State<T> initialState = new SuccessState<T>(value);

        _state = new AtomicReference<State<T>>(initialState);
    }

    /**
     * Initializes a new instance of the {@link Deferred} class.
     * 
     * @param cause The cause.
     */
    public Deferred(final Throwable cause) {
        if (cause == null) {
            throw new IllegalArgumentException("Cause must not be null");
        }

        final State<T> initialState = new FailureState<T>(cause);

        _state = new AtomicReference<State<T>>(initialState);
    }

    /**
     * Tries to complete the deferred with the specified value.
     * 
     * @param value The value.
     * @return A value indicating whether the deferred has been completed.
     */
    public final boolean trySuccess(final T value) {
        return _state.get().trySuccess(value);
    }

    /**
     * Tries to complete the deferred with the specified cause.
     * 
     * @param cause The cause.
     * @return A value indicating whether the deferred has been completed.
     */
    public final boolean tryFailure(final Throwable cause) {
        if (cause == null) {
            throw new IllegalArgumentException("Cause must not be null");
        }

        return _state.get().tryFailure(cause);
    }

    @Override
    public void setSuccess(final T value) {
        if (!trySuccess(value)) {
            throw new IllegalStateException("Deferred is already complete");
        }
    }

    @Override
    public void setFailure(final Throwable cause) {
        if (!tryFailure(cause)) {
            throw new IllegalStateException("Deferred is already complete");
        }
    }

    @Override
    public final boolean isComplete() {
        return _state.get().isComplete();
    }

    @Override
    public void then(final Completable<? super T> completable) {
        if (completable == null) {
            throw new IllegalArgumentException("Completable must not be null");
        }

        _state.get().then(completable);
    }

    @Override
    public final <R> Promise<R> then(final Continuation<? super T, ? extends R> continuation) {
        if (continuation == null) {
            throw new IllegalArgumentException("Continuation must not be null");
        }

        final Deferred<R> result = new Deferred<R>();

        _state.get().then(new Completable<T>() {
            @Override
            public void setSuccess(final T value) {
                try {
                    continuation.onSuccess(value, result);
                } catch (final Throwable t) {
                    result.setFailure(t);
                }
            }

            @Override
            public void setFailure(final Throwable cause) {
                try {
                    continuation.onFailure(cause, result);
                } catch (final Throwable t) {
                    t.addSuppressed(cause);
                    result.setFailure(t);
                }
            }
        });

        return result;
    }

    /**
     * Defines a state.
     * 
     * @param <T> The value type.
     */
    private static interface State<T> {

        /**
         * Returns a value indicating whether the deferred is complete.
         * 
         * @return a value indicating whether the deferred is complete.
         */
        boolean isComplete();

        /**
         * Tries to complete the deferred with the specified value.
         * 
         * @param value The value.
         * @return A value indicating whether the deferred has been completed.
         */
        boolean trySuccess(T value);

        /**
         * Tries to complete the deferred with the specified cause.
         * 
         * @param cause The cause.
         * @return A value indicating whether the deferred has been completed.
         */
        boolean tryFailure(Throwable cause);

        /**
         * Adds the specified completable.
         * 
         * @param completable The completable.
         */
        void then(Completable<? super T> completable);
    }

    /**
     * Represents a pending state.
     * 
     * @param <V> The value type.
     */
    private final class PendingState implements State<T> {

        /**
         * The stage queue.
         */
        private final Queue<Stage<T>> _stages;

        /**
         * Initializes a new instance of the {@link PendingState} class.
         */
        public PendingState() {
            _stages = new ConcurrentLinkedQueue<Stage<T>>();
        }

        /**
         * Tries to change the state to the specified state.
         * 
         * @param state The state.
         * @return A value indicating whether the state has been changed.
         */
        private boolean tryChangeState(final State<T> state) {
            if (!_state.compareAndSet(this, state)) {
                return false;
            }
            completeStages(state);

            return true;
        }

        /**
         * Adds the specified stage.
         * 
         * @param stage The stage.
         */
        private void addStage(final Stage<T> stage) {
            // As the queue is unbounded, this method will never return false.
            _stages.offer(stage);
            // Trigger completion when the promise has been completed in the meantime.
            final State<T> state = _state.get();

            if (state.isComplete()) {
                completeStages(state);
            }
        }

        /**
         * Completes the stages with the specified state.
         * 
         * @param state The state.
         */
        private void completeStages(final State<T> state) {
            Stage<T> stage;

            while ((stage = _stages.poll()) != null) {
                try {
                    stage.complete(state);
                } catch (final Throwable t) {
                    LOGGER.warn("Failed to complete stage", t);
                }
            }
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean trySuccess(final T value) {
            final State<T> state = new SuccessState<T>(value);

            return tryChangeState(state);
        }

        @Override
        public boolean tryFailure(final Throwable cause) {
            final State<T> state = new FailureState<T>(cause);

            return tryChangeState(state);
        }

        @Override
        public void then(final Completable<? super T> completable) {
            final Stage<T> stage = new Stage<T>(completable);

            addStage(stage);
        }
    }

    /**
     * Represents a complete state.
     * 
     * @param <T> The value type.
     */
    private abstract static class CompleteState<T> implements State<T> {

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public final boolean trySuccess(final T value) {
            return false;
        }

        @Override
        public final boolean tryFailure(final Throwable cause) {
            return false;
        }
    }

    /**
     * Represents a success state.
     * 
     * @param <T> The value type.
     */
    private static final class SuccessState<T> extends CompleteState<T> {

        /**
         * The value.
         */
        private final T _value;

        /**
         * Initializes a new instance of the {@link SuccessState} class.
         * 
         * @param value The value.
         */
        public SuccessState(final T value) {
            _value = value;
        }

        @Override
        public void then(final Completable<? super T> completable) {
            completable.setSuccess(_value);
        }
    }

    /**
     * Represents a failure state.
     * 
     * @param <T> The value type.
     */
    private static final class FailureState<T> extends CompleteState<T> {

        /**
         * The cause.
         */
        private final Throwable _cause;

        /**
         * Initializes a new instance of the {@link FailureState} class.
         * 
         * @param cause The cause.
         */
        public FailureState(final Throwable cause) {
            _cause = cause;
        }

        @Override
        public void then(final Completable<? super T> completable) {
            completable.setFailure(_cause);
        }
    }

    /**
     * Represents a stage.
     * 
     * @param <T> The value type.
     */
    private static final class Stage<T> {

        /**
         * The completable.
         */
        private final Completable<? super T> _completable;

        /**
         * A value indicating whether the stage completed.
         */
        private final AtomicBoolean _completed;

        /**
         * Initializes a new instance of the {@link Stage} class.
         * 
         * @param completable The completable.
         */
        public Stage(final Completable<? super T> completable) {
            _completable = completable;

            _completed = new AtomicBoolean(false);
        }

        /**
         * Completes the stage with the specified state.
         * 
         * @param state The state.
         */
        public void complete(final State<T> state) {
            if (_completed.compareAndSet(false, true)) {
                state.then(_completable);
            }
        }
    }
}
