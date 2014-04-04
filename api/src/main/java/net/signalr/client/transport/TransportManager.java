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

/**
 * Defines a transport manager.
 */
public interface TransportManager extends ChannelHandler {

    /**
     * Returns the transport.
     * 
     * @return The transport.
     */
    Transport getTransport();

    /**
     * Invoked when the connection has been lost.
     */
    void handleConnectionLost();

    /**
     * Invoked when the connection is slow.
     */
    void handleConnectionSlow();

    /**
     * Starts the transport manager.
     * 
     * @param context The transport context.
     */
    void start(TransportContext context);

    /**
     * Stops the transport manager.
     * 
     * @param context The transport context.
     */
    void stop(TransportContext context);
}