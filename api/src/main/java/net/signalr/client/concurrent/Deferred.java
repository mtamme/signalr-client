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

package net.signalr.client.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a deferred.
 * 
 * @param <V> The value type.
 */
public final class Deferred<V> extends AbstractPromise<V> {

    /**
     * The current state of the {@link Deferred}.
     */
    private final AtomicReference<State<V>> _state;

    /**
     * Initializes a new instance of the {@link Deferred} class.
     */
    public Deferred() {
        final State<V> initialState = new PendingState();

        _state = new AtomicReference<State<V>>(initialState);
    }

    /**
     * Tries to resolves the {@link Deferred} with the specified value.
     * 
     * @param value The value.
     * @return A value indicating whether the {@link Deferred} was resolved.
     */
    public boolean resolve(final V value) {
        return _state.get().resolve(value);
    }

    /**
     * Tries to rejects the {@link Deferred} with the specified throwable.
     * 
     * @param throwable The throwable.
     * @return A value indicating whether the {@link Deferred} was rejected.
     */
    public boolean reject(final Throwable throwable) {
        if (throwable == null) {
            throw new IllegalArgumentException("Throwable must not be null");
        }

        return _state.get().reject(throwable);
    }

    @Override
    public boolean isCompleted() {
        return _state.get().isCompleted();
    }

    @Override
    public void addCallback(final Callback<? super V> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }

        _state.get().addCallback(callback);
    }

    /**
     * Defines a state of a {@link Deferred}.
     * 
     * @param <V> The value type.
     */
    private static interface State<V> {

        /**
         * Returns a value indicating whether the {@link Deferred} completed.
         * 
         * @return A value indicating whether the {@link Deferred} completed.
         */
        boolean isCompleted();

        /**
         * Tries to resolves the {@link Deferred} with the specified value.
         * 
         * @param value The value.
         * @return A value indicating whether the {@link Deferred} was resolved.
         */
        boolean resolve(V value);

        /**
         * Tries to rejects the {@link Deferred} with the specified throwable.
         * 
         * @param throwable The throwable.
         * @return A value indicating whether the {@link Deferred} was rejected.
         */
        boolean reject(Throwable throwable);

        /**
         * Adds the specified {@link Callback}.
         * 
         * @param callback The callback.
         */
        void addCallback(Callback<? super V> callback);
    }

    /**
     * Represents the pending state of a {@link Deferred}.
     * 
     * @param <V> The value type.
     */
    private final class PendingState implements State<V> {

        /**
         * The completion queue.
         */
        private final ConcurrentLinkedQueue<Completion<V>> _completions;

        /**
         * Initializes a new instance of the {@link PendingState} class.
         */
        public PendingState() {
            _completions = new ConcurrentLinkedQueue<Completion<V>>();
        }

        /**
         * Triggers completion with the specified state.
         * 
         * @param state The state.
         */
        private void complete(final State<V> state) {
            Completion<V> completion;

            while ((completion = _completions.poll()) != null) {
                completion.complete(state);
            }
        }

        @Override
        public boolean isCompleted() {
            return false;
        }

        @Override
        public boolean resolve(final V value) {
            final ResolvedState<V> state = new ResolvedState<V>(value);

            if (!_state.compareAndSet(this, state)) {
                return false;
            }
            complete(state);

            return true;
        }

        @Override
        public boolean reject(final Throwable throwable) {
            final RejectedState<V> state = new RejectedState<V>(throwable);

            if (!_state.compareAndSet(this, state)) {
                return false;
            }
            complete(state);

            return true;
        }

        @Override
        public void addCallback(final Callback<? super V> callback) {
            final Completion<V> completion = new Completion<V>(callback);

            _completions.offer(completion);
            // Trigger completion when the promise has been completed in the meantime.
            final State<V> state = _state.get();

            if (state.isCompleted()) {
                complete(state);
            }
        }
    }

    /**
     * Represents the completed state of a {@link Deferred}.
     * 
     * @param <V> The value type.
     */
    private static abstract class CompletedState<V> implements State<V> {

        @Override
        public final boolean isCompleted() {
            return true;
        }

        @Override
        public final boolean resolve(final V value) {
            return false;
        }

        @Override
        public final boolean reject(final Throwable throwable) {
            return false;
        }
    }

    /**
     * Represents the resolved state of a {@link Deferred}.
     * 
     * @param <V> The value type.
     */
    private static final class ResolvedState<V> extends CompletedState<V> {

        /**
         * The value.
         */
        private final V _value;

        /**
         * Initializes a new instance of the {@link ResolvedState} class.
         * 
         * @param value The value.
         */
        public ResolvedState(final V value) {
            _value = value;
        }

        @Override
        public void addCallback(final Callback<? super V> callback) {
            callback.onResolved(_value);
        }
    }

    /**
     * Represents the rejected state of a {@link Deferred}.
     * 
     * @param <V> The value type.
     */
    private static final class RejectedState<V> extends CompletedState<V> {

        /**
         * The throwable.
         */
        private final Throwable _throwable;

        /**
         * Initializes a new instance of the {@link RejectedState} class.
         * 
         * @param throwable The throwable.
         */
        public RejectedState(final Throwable throwable) {
            _throwable = throwable;
        }

        @Override
        public void addCallback(final Callback<? super V> callback) {
            callback.onRejected(_throwable);
        }
    }

    /**
     * Represents a completion.
     * 
     * @param <V> The value type.
     */
    private static final class Completion<V> {

        /**
         * The callback.
         */
        private final Callback<? super V> _callback;

        /**
         * The completed flag.
         */
        private final AtomicBoolean _completed;

        /**
         * Initializes a new instance of the {@link Completion} class.
         * 
         * @param callback The callback.
         */
        public Completion(final Callback<? super V> callback) {
            _callback = callback;

            _completed = new AtomicBoolean(false);
        }

        /**
         * Triggers completion with the specified state.
         * 
         * @param state The state.
         */
        public void complete(final State<V> state) {
            if (_completed.compareAndSet(false, true)) {
                state.addCallback(_callback);
            }
        }
    }
}
