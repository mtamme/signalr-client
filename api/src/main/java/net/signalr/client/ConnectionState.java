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

import net.signalr.client.util.concurrent.Promise;

/**
 * Defines a connection state.
 */
interface ConnectionState {

    /**
     * Returns a value indicating whether the connection is connected.
     * 
     * @return A value indicating whether the connection is connected.
     */
    boolean isConnected();

    /**
     * Adds the specified header name and value.
     * 
     * @param context The connection context.
     * @param name The header name.
     * @param value The header value.
     */
    void addHeader(ConnectionContext context, String name, String value);

    /**
     * Adds the specified parameter name and value.
     * 
     * @param context The connection context.
     * @param name The parameter name.
     * @param value The parameter value.
     */
    void addParameter(ConnectionContext context, String name, String value);

    /**
     * Sets the connection data.
     * 
     * @param context The connection context.
     * @param connectionData The connection data.
     */
    void setConnectionData(ConnectionContext context, String connectionData);

    /**
     * Starts the connection.
     * 
     * @param context The connection context.
     * @param handler The connection handler.
     * @return The start result.
     */
    Promise<Void> start(ConnectionContext context, ConnectionHandler handler);

    /**
     * Stops the connection.
     * 
     * @param context The connection context.
     * @return The stop result.
     */
    Promise<Void> stop(ConnectionContext context);

    /**
     * Sends a message.
     * 
     * @param context The connection context.
     * @param message The message.
     * @return The send result.
     */
    Promise<Void> send(ConnectionContext context, String message);
}