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
 * Defines a continuation.
 * 
 * @param <T> The value type.
 * @param <U> The result type.
 */
public interface Continuation<T, U> {

    /**
     * Completes the continuation with the specified value.
     * 
     * @param value The value.
     * @param result The result.
     * @throws Exception
     */
    void setSuccess(T value, Deferred<? super U> result) throws Exception;

    /**
     * Completes the continuation with the specified cause.
     * 
     * @param cause The cause.
     * @param result The result.
     * @throws Exception
     */
    void setFailure(Throwable cause, Deferred<? super U> result) throws Exception;
}
