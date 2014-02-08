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

package net.signalr.client.transport;

import net.signalr.client.concurrent.Promise;

/**
 * Defines a transport channel.
 */
public interface TransportChannel {

    /**
     * Sends the specified message.
     * 
     * @param message The message.
     * @return A promise.
     */
    Promise<Void> send(String message);

    /**
     * Closes the transport channel.
     * 
     * @return A promise.
     */
    Promise<Void> close();
}
