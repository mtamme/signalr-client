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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents a completion which can be awaited.
 * 
 * @param <T> The value type.
 */
final class Awaiter<T> implements Completion<T> {

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
