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

package net.signalr.client.util;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an abstract lifecycle.
 * 
 * @param <T> The lifecycle context type.
 */
public abstract class AbstractLifecycle<T> implements Lifecycle<T> {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractLifecycle.class);

    /**
     * The lifecycle state.
     */
    private final AtomicReference<State> _state;

    /**
     * Initializes a new instance of the {@link AbstractLifecycle} class
     */
    protected AbstractLifecycle() {
        _state = new AtomicReference<State>(State.STOPPED);
    }

    /**
     * Changes the lifecycle state to the specified new lifecycle state.
     * 
     * @param oldState The old lifecycle state.
     * @param newState The new lifecycle state.
     */
    private void changeState(final State oldState, final State newState) {
        if (!tryChangeState(oldState, newState)) {
            throw new IllegalStateException("Failed to change state");
        }
    }

    /**
     * Tries to change the lifecycle state to the specified new lifecycle state.
     * 
     * @param oldState The old lifecycle state.
     * @param newState The new lifecycle state.
     * @return A value indicating whether the lifecycle state changed.
     */
    private boolean tryChangeState(final State oldState, final State newState) {
        if (!_state.compareAndSet(oldState, newState)) {
            return false;
        }

        logger.info("{} '{}'", newState, this);

        return true;
    }

    /**
     * Starts the lifecycle.
     * 
     * @param context The lifecycle context.
     */
    protected abstract void doStart(T context);

    /**
     * Stops the lifecycle.
     * 
     * @param context The lifecycle context.
     */
    protected abstract void doStop(T context);

    @Override
    public final boolean isRunning() {
        final State state = _state.get();

        return ((state == State.STARTED) || (state == State.STARTING));
    }

    @Override
    public final void start(final T context) {
        if (!tryChangeState(State.STOPPED, State.STARTING)) {
            return;
        }

        try {
            doStart(context);
        } catch (final Throwable t) {
            changeState(State.STARTING, State.STOPPED);

            logger.warn("Failed to start '{}'", this, t);

            throw new LifecycleException("Failed to start lifecycle", t);
        }

        changeState(State.STARTING, State.STARTED);
    }

    @Override
    public final void stop(final T context) {
        if (!tryChangeState(State.STARTED, State.STOPPING)) {
            return;
        }

        try {
            doStop(context);
        } catch (final Throwable t) {
            changeState(State.STOPPING, State.STOPPED);

            logger.warn("Failed to stop '{}'", this, t);

            throw new LifecycleException("Failed to stop lifecycle", t);
        }

        changeState(State.STOPPING, State.STOPPED);
    }

    /**
     * Defines all lifecycle states.
     */
    private static enum State {

        /**
         * Lifecycle has been started.
         */
        STARTED("Started"),

        /**
         * Lifecycle is going to be started.
         */
        STARTING("Starting"),

        /**
         * Lifecycle is going to be stopped.
         */
        STOPPING("Stopping"),

        /**
         * Lifecycle has been stopped.
         */
        STOPPED("Stopped");

        /**
         * The state name.
         */
        private final String _name;

        /**
         * Initializes a new instance of the {@link State} class.
         * 
         * @param name The state name.
         */
        State(final String name) {
            if (name == null) {
                throw new IllegalArgumentException("Name must not be null");
            }

            _name = name;
        }

        @Override
        public String toString() {
            return _name;
        }
    }
}
