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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents a completable which can be awaited.
 * 
 * @param <T> The value type.
 */
final class Awaiter<T> implements Completable<T> {

    /**
     * Defines a value.
     * 
     * @param <V> The value type.
     */
    private static interface Value<V> {
        /**
         * Returns the value.
         * 
         * @return The value.
         * @throws ExecutionException
         */
        V get() throws ExecutionException;
    }

    /**
     * The count down latch.
     */
    private final CountDownLatch _latch;

    /**
     * The value.
     */
    private Value<T> _value;

    /**
     * Initializes a new instance of the {@link Awaiter} class.
     */
    public Awaiter() {
        _latch = new CountDownLatch(1);

        _value = null;
    }

    /**
     * Returns a value indicating whether the awaiter is complete.
     * 
     * @return A value indicating whether the awaiter is complete.
     */
    public boolean isComplete() {
        final long count = _latch.getCount();

        return (count == 0);
    }

    /**
     * Causes the current thread to wait until a {@link Promise} completed, unless the thread is interrupted.
     * 
     * @return The value.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public T get() throws InterruptedException, ExecutionException {
        _latch.await();

        return _value.get();
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
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!_latch.await(timeout, unit)) {
            throw new TimeoutException("A timeout occured while waiting for completion");
        }

        return _value.get();
    }

    @Override
    public void setSuccess(final T value) {
        _value = new Value<T>() {
            @Override
            public T get() throws ExecutionException {
                return value;
            }
        };
        _latch.countDown();
    }

    @Override
    public void setFailure(final Throwable cause) {
        _value = new Value<T>() {
            @Override
            public T get() throws ExecutionException {
                throw new ExecutionException(cause);
            }
        };
        _latch.countDown();
    }
}
