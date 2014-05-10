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

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an executor completion.
 * 
 * @param <T> The value type.
 */
abstract class ExecutorCompletion<T> implements Completion<T> {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ExecutorCompletion.class);

    /**
     * The executor.
     */
    private final Executor _executor;

    /**
     * Initializes a new instance of the {@link ExecutorCompletion} class.
     * 
     * @param executor The executor.
     */
    protected ExecutorCompletion(final Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor must not e null");
        }

        _executor = executor;
    }

    /**
     * Handles the success completion.
     * 
     * @param value The value.
     */
    protected abstract void doSuccess(T value);

    /**
     * Handles the failure completion.
     * 
     * @param cause The cause.
     */
    protected abstract void doFailure(Throwable cause);

    @Override
    public final void setSuccess(final T value) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    doSuccess(value);
                } catch (final Throwable t) {
                    logger.warn("Failed to execute completion", t);
                }

            }
        });
    }

    @Override
    public final void setFailure(final Throwable cause) {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    doFailure(cause);
                } catch (final Throwable t) {
                    logger.warn("Failed to execute completion", t);
                }
            }
        });
    }
}
