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

import net.signalr.client.concurrent.Promise;

/**
 * Defines a hub proxy.
 */
public interface HubProxy {

    /**
     * Invokes a method on the server side hub asynchronously.
     * 
     * @param returnClass The class of the return value.
     * @param methodName The name of the method.
     * @param args The arguments.
     * @return A promise representing the return value.
     */
    <T> Promise<T> invoke(Class<T> returnClass, String methodName, Object... args);

    /**
     * Subscribes to a hub event.
     * 
     * @param eventName The event name.
     * @param listener The hub event listener.
     */
    void subscribe(String eventName, HubEventListener listener);
}
