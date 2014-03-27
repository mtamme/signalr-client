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
     * Returns a resolved {@link Promise}.
     * 
     * @return A resolved {@link Promise}.
     */
    public static Promise<Void> resolved() {
        return resolved(null);
    }

    /**
     * Returns a resolved {@link Promise}.
     * 
     * @param value The value.
     * @return A resolved {@link Promise}.
     */
    public static <V> Promise<V> resolved(final V value) {
        return new Deferred<V>(value);
    }

    /**
     * Returns a rejected {@link Promise}.
     * 
     * @param throwable The {@link Throwable}.
     * @return A rejected {@link Promise}.
     */
    public static <V> Promise<V> rejected(final Throwable throwable) {
        return new Deferred<V>(throwable);
    }

    /**
     * Returns a {@link Future} for the specified {@link Promise}.
     * 
     * @param promise The promise.
     * @return The future.
     */
    public static <V> Future<V> toFuture(final Promise<V> promise) {
        if (promise == null) {
            throw new IllegalArgumentException("Promise must not be null");
        }

        final Awaiter<V> awaiter = new Awaiter<V>();

        promise.addCallback(awaiter);

        return new Future<V>() {
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
                return awaiter.isCompleted();
            }

            @Override
            public V get() throws InterruptedException, ExecutionException {
                return awaiter.get();
            }

            @Override
            public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return awaiter.get(timeout, unit);
            }
        };
    }
}
