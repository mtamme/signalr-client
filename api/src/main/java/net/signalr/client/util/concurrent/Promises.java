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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Provides {@link Promise} extension methods.
 */
public final class Promises {

    /**
     * Initializes a new instance of the {@link Promises} class.
     */
    private Promises() {
    }

    /**
     * Returns a new success promise.
     * 
     * @return The promise.
     */
    public static Promise<Void> newSuccess() {
        return newSuccess(null);
    }

    /**
     * Returns a new success promise.
     * 
     * @param value The value.
     * @return The promise.
     */
    public static <T> Promise<T> newSuccess(final T value) {
        return new DefaultDeferred<T>(value);
    }

    /**
     * Returns a new failure promise.
     * 
     * @param cause The cause.
     * @return The promise.
     */
    public static <T> Promise<T> newFailure(final Throwable cause) {
        return new DefaultDeferred<T>(cause);
    }

    /**
     * Returns a new promise based on the execution of the specified runnable.
     * 
     * @param runnable The runnable.
     * @return The promise.
     */
    public static Promise<Void> newPromise(final Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable must not be null");
        }

        try {
            runnable.run();
        } catch (final Throwable t) {
            return newFailure(t);
        }

        return newSuccess();
    }

    /**
     * Returns a new deferred.
     * 
     * @return The deferred.
     */
    public static <T> Deferred<T> newDeferred() {
        return new DefaultDeferred<T>();
    }

    /**
     * Awaits the specified promise.
     * 
     * @param promise The promise.
     * @return The value.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static <T> T await(final Promise<T> promise) throws InterruptedException, ExecutionException {
        if (promise == null) {
            throw new IllegalArgumentException("Promise must not be null");
        }

        final Awaiter<T> awaiter = new Awaiter<T>();

        promise.then(awaiter);

        return awaiter.get();
    }

    /**
     * Returns a future for the specified promise.
     * 
     * @param promise The promise.
     * @return The future.
     */
    public static <T> Future<T> toFuture(final Promise<T> promise) {
        if (promise == null) {
            throw new IllegalArgumentException("Promise must not be null");
        }

        final Awaiter<T> awaiter = new Awaiter<T>();

        promise.then(awaiter);

        return new Future<T>() {
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return awaiter.isComplete();
            }

            @Override
            public T get() throws InterruptedException, ExecutionException {
                return awaiter.get();
            }

            @Override
            public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return awaiter.get(timeout, unit);
            }
        };
    }
}
