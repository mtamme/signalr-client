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

package net.signalr.client.hub;

import net.signalr.client.json.JsonElement;
import net.signalr.client.util.concurrent.Promise;

/**
 * Defines a hub proxy.
 */
public interface HubProxy {

    /**
     * Invokes a server side hub method asynchronously.
     * 
     * @param methodName The method name.
     * @param returnType The return type.
     * @param arguments The arguments.
     * @return The invocation result.
     */
    <R> Promise<R> invoke(String methodName, Class<R> returnType, Object... arguments);

    /**
     * Registers a client side hub callback.
     * 
     * @param methodName The method name.
     * @param callback The hub callback.
     */
    void register(String methodName, HubCallback<JsonElement> callback);

    /**
     * Registers a client side hub callback.
     * 
     * @param methodName The method name.
     * @param argumentType The argument type.
     * @param callback The hub callback.
     */
    <T> void register(String methodName, Class<T> argumentType, HubCallback<T> callback);

    /**
     * Unregisters a client side hub callback.
     * 
     * @param methodName The method name.
     */
    void unregister(String methodName);
}
