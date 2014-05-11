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

package net.signalr.client.hub;

import net.signalr.client.json.JsonElement;
import net.signalr.client.util.concurrent.Promise;

/**
 * Defines a hub proxy.
 */
public interface HubProxy {

    /**
     * Invokes a server side hub method.
     * 
     * @param methodName The method name.
     * @param returnType The return type.
     * @param arguments The arguments.
     * @return The invocation result.
     */
    <R> Promise<R> invoke(String methodName, Class<R> returnType, Object... arguments);

    /**
     * Registers a client side hub method.
     * 
     * @param methodName The method name.
     * @param callback The hub callback.
     */
    void register(String methodName, HubCallback<JsonElement[]> callback);

    /**
     * Registers a client side hub method.
     * 
     * @param methodName The method name.
     * @param argumentType The argument type.
     * @param callback The hub callback.
     */
    <T> void register(String methodName, Class<T> argumentType, HubCallback<T> callback);

    /**
     * Unregisters a client side hub method.
     * 
     * @param methodName The method name.
     */
    void unregister(String methodName);
}
