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

package net.signalr.client.util.concurrent;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an execute on continuation.
 * 
 * @param <T> The value type.
 */
public final class ExecuteOn<T> implements Continuation<T, T> {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ExecuteOn.class);

    /**
     * The executor.
     */
    private final Executor _executor;

    /**
     * Initializes a new instance of the {@link ExecuteOn} class.
     * 
     * @param executor The executor.
     */
    public ExecuteOn(final Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor must not be null");
        }

        _executor = executor;
    }

    @Override
    public void setSuccess(final T value, final Deferred<? super T> result) throws Exception {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    result.setSuccess(value);
                } catch (final Throwable t) {
                    logger.warn("Failed to execute completion", t);
                }
            }
        });
    }

    @Override
    public void setFailure(final Throwable cause, final Deferred<? super T> result) throws Exception {
        _executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    result.setFailure(cause);
                } catch (final Throwable t) {
                    logger.warn("Failed to execute completion", t);
                }
            }
        });
    }
}
