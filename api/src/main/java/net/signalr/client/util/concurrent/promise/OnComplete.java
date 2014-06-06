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

package net.signalr.client.util.concurrent.promise;

/**
 * Represents a complete continuation.
 * 
 * @param <T> The value type.
 */
public abstract class OnComplete<T> implements Continuation<T, T> {

    /**
     * Handles the success continuation.
     * 
     * @param value The value.
     * @throws Exception
     */
    protected void onSuccess(final T value) throws Exception {
        onComplete(value, null);
    }

    /**
     * Handles the failure continuation.
     * 
     * @param cause The cause.
     * @throws Exception
     */
    protected void onFailure(final Throwable cause) throws Exception {
        onComplete(null, cause);
    }

    /**
     * Handles the complete continuation.
     * 
     * @param cause The cause.
     * @throws Exception
     */
    protected void onComplete(final T value, final Throwable cause) throws Exception {
    }

    @Override
    public final void onSuccess(final T value, final Completable<? super T> result) throws Exception {
        onSuccess(value);
        result.setSuccess(value);
    }

    @Override
    public final void onFailure(final Throwable cause, final Completable<? super T> result) throws Exception {
        onFailure(cause);
        result.setFailure(cause);
    }
}
