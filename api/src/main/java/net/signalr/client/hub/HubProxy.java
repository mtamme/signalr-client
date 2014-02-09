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
*
*/
public interface HubProxy {

    /**
     * Invokes a method on the server side hub asynchronously.
     * 
     * @param methodName The name of the method.
     * @param args The arguments.
     * @return A promise that represents when return value.
     */
    <T> Promise<T> invoke(String methodName, Object... args);

    /**
     * Registers an event for the hub.
     * 
     * @param eventName The name of the event.
     * @param listener The hub listener.
     */
    void subscribe(String eventName, HubEventListener listener);
}
