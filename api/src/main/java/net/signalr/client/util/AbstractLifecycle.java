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
     * @param state The lifecycle state.
     * @param newState The new lifecycle state.
     */
    private void changeState(final State state, final State newState) {
        if (!tryChangeState(state, newState)) {
            throw new IllegalStateException("Failed to change lifecycle state");
        }
    }

    /**
     * Tries to change the lifecycle state to the specified new lifecycle state.
     * 
     * @param state The lifecycle state.
     * @param newState The new lifecycle state.
     * @return A value indicating whether the lifecycle state changed.
     */
    private boolean tryChangeState(final State state, final State newState) {
        if (!_state.compareAndSet(state, newState)) {
            return false;
        }

        logger.debug("{} '{}'", newState, this);

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
        return _state.get().isRunning();
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
         * Lifecycle is going to be started.
         */
        STARTING("Starting", true),

        /**
         * Lifecycle has been started.
         */
        STARTED("Started", true),

        /**
         * Lifecycle is going to be stopped.
         */
        STOPPING("Stopping", false),

        /**
         * Lifecycle has been stopped.
         */
        STOPPED("Stopped", false);

        /**
         * The state name.
         */
        private final String _name;

        /**
         * A value indicating whether this is a running state.
         */
        private final boolean _running;

        /**
         * Initializes a new instance of the {@link State} class.
         * 
         * @param name The state name.
         * @param running A value indicating whether this is a running state.
         */
        private State(final String name, final boolean running) {
            if (name == null) {
                throw new IllegalArgumentException("Name must not be null");
            }

            _name = name;
            _running = running;
        }

        /**
         * Returns a value indicating whether this is a running state.
         * 
         * @return A value indicating whether this is a running state.
         */
        public boolean isRunning() {
            return _running;
        }

        @Override
        public String toString() {
            return _name;
        }
    }
}
