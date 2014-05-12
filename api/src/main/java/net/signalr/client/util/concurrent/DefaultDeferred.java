/*
 * Copyright © Martin Tamme
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.signalr.client.util.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a default deferred.
 * 
 * @param <T> The value type.
 */
final class DefaultDeferred<T> implements Deferred<T> {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultDeferred.class);

    /**
     * The state of the deferred.
     */
    private final AtomicReference<State<T>> _state;

    /**
     * Initializes a new instance of the {@link DefaultDeferred} class.
     */
    public DefaultDeferred() {
        final State<T> initialState = new PendingState();

        _state = new AtomicReference<State<T>>(initialState);
    }

    /**
     * Initializes a new instance of the {@link DefaultDeferred} class.
     * 
     * @param value The value.
     */
    public DefaultDeferred(final T value) {
        final State<T> initialState = new SuccessState<T>(value);

        _state = new AtomicReference<State<T>>(initialState);
    }

    /**
     * Initializes a new instance of the {@link DefaultDeferred} class.
     * 
     * @param cause The cause.
     */
    public DefaultDeferred(final Throwable cause) {
        if (cause == null) {
            throw new IllegalArgumentException("Cause must not be null");
        }

        final State<T> initialState = new FailureState<T>(cause);

        _state = new AtomicReference<State<T>>(initialState);
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
    public final boolean trySuccess(final T value) {
        return _state.get().trySuccess(value);
    }

    @Override
    public final boolean tryFailure(final Throwable cause) {
        if (cause == null) {
            throw new IllegalArgumentException("Cause must not be null");
        }

        return _state.get().tryFailure(cause);
    }

    @Override
    public final boolean isComplete() {
        return _state.get().isComplete();
    }

    @Override
    public void then(final Completion<? super T> completion) {
        if (completion == null) {
            throw new IllegalArgumentException("Completion must not be null");
        }

        _state.get().then(completion);
    }

    @Override
    public final <R> Promise<R> then(final Continuation<? super T, ? extends R> continuation) {
        if (continuation == null) {
            throw new IllegalArgumentException("Continuation must not be null");
        }

        final Deferred<R> deferred = new DefaultDeferred<R>();

        _state.get().then(new Completion<T>() {
            @Override
            public void setSuccess(final T value) {
                try {
                    continuation.setSuccess(value, deferred);
                } catch (final Throwable t) {
                    deferred.setFailure(t);
                }
            }

            @Override
            public void setFailure(final Throwable cause) {
                try {
                    continuation.setFailure(cause, deferred);
                } catch (final Throwable t) {
                    t.addSuppressed(cause);
                    deferred.setFailure(t);
                }
            }
        });

        return deferred;
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
        boolean tryFailure(Throwable throwable);

        /**
         * Adds the specified completion.
         * 
         * @param completion The completion.
         */
        void then(Completion<? super T> completion);
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
        private final ConcurrentLinkedQueue<Stage<T>> _stages;

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

            if (state != this) {
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
                    logger.warn("Failed to complete stage", t);
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
        public void then(final Completion<? super T> completion) {
            final Stage<T> stage = new Stage<T>(completion);

            addStage(stage);
        }
    }

    /**
     * Represents a complete state.
     * 
     * @param <T> The value type.
     */
    private static abstract class CompleteState<T> implements State<T> {

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
        public void then(final Completion<? super T> completion) {
            completion.setSuccess(_value);
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
        public void then(final Completion<? super T> completion) {
            completion.setFailure(_cause);
        }
    }

    /**
     * Represents a stage.
     * 
     * @param <T> The value type.
     */
    private static final class Stage<T> {

        /**
         * The completion.
         */
        private final Completion<? super T> _completion;

        /**
         * A value indicating whether the stage completed.
         */
        private final AtomicBoolean _completed;

        /**
         * Initializes a new instance of the {@link Stage} class.
         * 
         * @param completion The completion.
         */
        public Stage(final Completion<? super T> completion) {
            _completion = completion;

            _completed = new AtomicBoolean(false);
        }

        /**
         * Completes the stage with the specified state.
         * 
         * @param state The state.
         */
        public void complete(final State<T> state) {
            if (_completed.compareAndSet(false, true)) {
                state.then(_completion);
            }
        }
    }
}
