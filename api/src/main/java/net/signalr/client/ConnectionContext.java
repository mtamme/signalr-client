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

package net.signalr.client;

import net.signalr.client.transport.TransportContext;
import net.signalr.client.transport.TransportManager;

/**
 * Defines a connection context.
 */
interface ConnectionContext extends TransportContext {

    /**
     * Returns the transport manager.
     * 
     * @return The transport manager.
     */
    TransportManager getTransportManager();

    /**
     * Returns the connection ID.
     * 
     * @return The connection ID.
     */
    String getConnectionId();

    /**
     * Sets the connection ID.
     * 
     * @param connectionId The connection ID.
     */
    void setConnectionId(String connectionId);

    /**
     * Sets a value indicating whether web sockets should be tried.
     * 
     * @param tryWebSockets A value indicating whether web sockets should be tried.
     */
    void setTryWebSockets(boolean tryWebSockets);

    /**
     * Sets the connection data.
     * 
     * @param connectionData The connection data.
     */
    void setConnectionData(String connectionData);

    /**
     * Sets the connection token.
     * 
     * @param connectionToken The connection token.
     */
    void setConnectionToken(String connectionToken);

    /**
     * Returns the disconnect timeout.
     * 
     * @return The disconnect timeout.
     */
    double getDisconnectTimeout();

    /**
     * Sets the disconnect timeout.
     * 
     * @param disconnectTimeout The disconnect timeout.
     */
    void setDisconnectTimeout(long disconnectTimeout);

    /**
     * Sets the keep-alive timeout.
     * 
     * @param keepAliveTimeout The keep-alive timeout.
     */
    void setKeepAliveTimeout(long keepAliveTimeout);

    /**
     * Adds a header.
     * 
     * @param name The header name.
     * @param value The header value.
     */
    void addHeader(String name, String value);

    /**
     * Adds a query parameter.
     * 
     * @param name The query parameter name.
     * @param value The query parameter value.
     */
    void addQueryParameter(String name, String value);

    /**
     * Returns the current connection state.
     * 
     * @return The current connection state.
     */
    ConnectionState getState();

    /**
     * Changes the connection state to the specified new connection state.
     * 
     * @param oldState The old connection state.
     * @param newState The new connection state.
     */
    void changeState(ConnectionState oldState, ConnectionState newState);

    /**
     * Tries to change the connection state to the specified new connection state.
     * 
     * @param oldState The old connection state.
     * @param newState The new connection state.
     * @return A value indicating whether the connection state changed.
     */
    boolean tryChangeState(ConnectionState oldState, ConnectionState newState);
}
