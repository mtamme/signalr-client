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
 * Defines transport options.
 */
public interface TransportOptions {

    /**
     * Returns the relative URL.
     * 
     * @return The relative URL.
     */
    String getRelativeUrl();

    /**
     * Returns the connection token.
     * 
     * @return The connection token.
     */
    String getConnectionToken();

    /**
     * Returns the connection ID.
     * 
     * @return The connection ID.
     */
    String getConnectionId();

    /**
     * Returns the protocol version.
     * 
     * @return The protocol version.
     */
    String getProtocolVersion();

    /**
     * Returns a value indicating whether web sockets should be tried.
     * 
     * @return A value indicating whether web sockets should be tried.
     */
    boolean getTryWebSockets();

    /**
     * Returns the disconnect timeout in milliseconds.
     * 
     * @return The disconnect timeout in milliseconds.
     */
    long getDisconnectTimeout();

    /**
     * Returns the keep-alive timeout in milliseconds.
     * 
     * @return The keep-alive timeout in milliseconds.
     */
    long getKeepAliveTimeout();

    /**
     * Returns the connect timeout in milliseconds.
     * 
     * @return The connect timeout in milliseconds.
     */
    long getConnectTimeout();
}