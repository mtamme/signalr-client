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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents a {@link Callback} which can be awaited.
 * 
 * @param <V> The value type.
 */
final class Awaiter<V> implements Callback<V> {

    /**
     * Defines a result.
     * 
     * @param <V> The value type.
     */
    private interface Result<V> {
        /**
         * Returns the value.
         * 
         * @return The value.
         * @throws ExecutionException
         */
        V get() throws ExecutionException;
    }

    /**
     * The completion count down.
     */
    private final CountDownLatch _completion;

    /**
     * The result.
     */
    private Result<V> _result;

    /**
     * Initializes a new instance of the {@link Awaiter} class.
     */
    public Awaiter() {
        _completion = new CountDownLatch(1);

        _result = null;
    }

    /**
     * Returns a value indicating whether a {@link Promise} completed.
     * 
     * @return A value indicating whether a {@link Promise} completed.
     */
    public boolean isCompleted() {
        final long count = _completion.getCount();

        return (count == 0);
    }

    /**
     * Causes the current thread to wait until a {@link Promise} completed, unless the thread is interrupted.
     * 
     * @return The value.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public V get() throws InterruptedException, ExecutionException {
        _completion.await();

        return _result.get();
    }

    /**
     * Causes the current thread to wait until a {@link Promise} completed, unless the thread is interrupted.
     * 
     * @param timeout The timeout.
     * @param unit The time unit.
     * @return The value.
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!_completion.await(timeout, unit)) {
            throw new TimeoutException("A timeout occured while waiting for completion");
        }

        return _result.get();
    }

    @Override
    public void onResolved(final V value) {
        _result = new Result<V>() {
            @Override
            public V get() throws ExecutionException {
                return value;
            }
        };

        _completion.countDown();
    }

    @Override
    public void onRejected(final Throwable cause) {
        _result = new Result<V>() {
            @Override
            public V get() throws ExecutionException {
                throw new ExecutionException(cause);
            }
        };

        _completion.countDown();
    }
}
