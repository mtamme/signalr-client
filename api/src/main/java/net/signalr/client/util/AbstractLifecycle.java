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

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an abstract lifecycle.
 */
public abstract class AbstractLifecycle<T> implements Lifecycle<T> {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractLifecycle.class);

    /**
     * A value indicating whether the lifecycle has already been started.
     */
    private final AtomicBoolean _started;

    /**
     * Initializes a new instance of the {@link AbstractLifecycle} class
     */
    protected AbstractLifecycle() {
        _started = new AtomicBoolean();
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
    public final boolean isStarted() {
        return _started.get();
    }

    @Override
    public final void start(final T context) {
        if (!_started.compareAndSet(false, true)) {
            throw new IllegalStateException("Lifecycle already started");
        }

        logger.info("Starting '{}'...", this);

        doStart(context);
    }

    @Override
    public final void stop(final T context) {
        if (!_started.compareAndSet(true, false)) {
            throw new IllegalStateException("Lifecycle already stopped");
        }

        logger.info("Stopping '{}'...", this);

        doStop(context);
    }
}
