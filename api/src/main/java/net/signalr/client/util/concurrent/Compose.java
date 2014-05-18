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
 * Represents a compose continuation.
 * 
 * @param <T> The value type.
 * @param <R> The result type.
 */
public abstract class Compose<T, R> implements Continuation<T, R> {

    /**
     * Handles the compose continuation.
     * 
     * @param value The value.
     * @return The result.
     * @throws Exception
     */
    protected abstract Promise<R> doCompose(T value) throws Exception;

    @Override
    public final void setSuccess(final T value, final Deferred<? super R> result) throws Exception {
        final Promise<R> promise = doCompose(value);

        promise.then(result);
    }

    @Override
    public final void setFailure(final Throwable cause, final Deferred<? super R> result) throws Exception {
        result.setFailure(cause);
    }
}
