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
 * Represents a accept continuation.
 */
public abstract class Accept<T> implements Continuation<T, Void> {

    /**
     * Handles the accept continuation.
     * 
     * @param value The value.
     * @return The result.
     * @throws Exception
     */
    protected abstract void doAccept(T value) throws Exception;

    /**
     * Handles the failure continuation.
     * 
     * @param cause The cause.
     * @throws Exception
     */
    protected void onFailure(final Throwable cause) throws Exception {
    }

    @Override
    public void onSuccess(final T value, final Completable<? super Void> result) throws Exception {
        doAccept(value);
        result.setSuccess(null);
    }

    @Override
    public void onFailure(final Throwable cause, final Completable<? super Void> result) throws Exception {
        onFailure(cause);
        result.setFailure(cause);
    }
}
