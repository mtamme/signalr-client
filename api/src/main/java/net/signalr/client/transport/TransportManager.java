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

import net.signalr.client.util.Lifecycle;

/**
 * Defines a transport manager.
 */
public interface TransportManager extends Lifecycle<TransportContext>, ChannelHandler {

    /**
     * Returns the transport.
     * 
     * @return The transport.
     */
    Transport getTransport();

    /**
     * Adds the specified transport listener.
     * 
     * @param listener The transport listener.
     */
    void addListener(TransportListener listener);

    /**
     * Removes the specified transport listener.
     * 
     * @param listener The transport listener.
     */
    void removeListener(TransportListener listener);

    /**
     * Invoked when the connection has been lost.
     */
    void handleConnectionLost();

    /**
     * Invoked when the connection is slow.
     */
    void handleConnectionSlow();
}