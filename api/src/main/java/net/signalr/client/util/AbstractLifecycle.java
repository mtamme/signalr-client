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

            logger.info("Failed to start '{}'", this, t);
            return;
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
            logger.info("Failed to stop '{}'", this, t);
        }

        changeState(State.STOPPING, State.STOPPED);
    }

    /**
     * Defines all lifecycle states.
     */
    private enum State {

        /**
         * Lifecycle has been started.
         */
        STARTED,

        /**
         * Lifecycle is going to be started.
         */
        STARTING,

        /**
         * Lifecycle is going to be stopped.
         */
        STOPPING,

        /**
         * Lifecycle has been stopped.
         */
        STOPPED
    }
}
