/*
 * Copyright 2014 Martin Tamme
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
 * Defines a deferred value.
 * 
 * @param <T> The value type.
 */
public interface Deferred<T> extends Promise<T>, Completion<T> {

    /**
     * Tries to complete the deferred with the specified value.
     * 
     * @param value The value.
     * @return A value indicating whether the deferred has been completed.
     */
    boolean trySuccess(T value);

    /**
     * Tries to complete the deferred with the specified cause.
     * 
     * @param cause The cause.
     * @return A value indicating whether the deferred has been completed.
     */
    boolean tryFailure(Throwable cause);
}
