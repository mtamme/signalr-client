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

/**
 * Represents a finally continuation.
 * 
 * @param <T> The value type.
 * @param <R> The result type.
 */
public abstract class Finally<T, R> implements Continuation<T, R> {

    /**
     * Handles the finally continuation.
     * 
     * @param value The value.
     * @param cause The cause.
     * @return The result.
     * @throws Exception
     */
    protected abstract R doFinally(T value, Throwable cause) throws Exception;

    @Override
    public final void onSuccess(final T value, final Completable<? super R> result) throws Exception {
        final R newValue = doFinally(value, null);

        result.setSuccess(newValue);
    }

    @Override
    public final void onFailure(final Throwable cause, final Completable<? super R> result) throws Exception {
        final R newValue = doFinally(null, cause);

        result.setSuccess(newValue);
    }
}
