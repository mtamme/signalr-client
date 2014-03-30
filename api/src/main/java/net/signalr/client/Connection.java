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

import net.signalr.client.concurrent.Promise;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.transport.Transport;

/**
 * Defines a connection.
 */
public interface Connection {

    /**
     * Returns the protocol version.
     * 
     * @return The protocol version.
     */
    String getProtocolVersion();

    /**
     * Returns the connection URL.
     * 
     * @return The connection URL.
     */
    String getUrl();

    /**
     * Returns the transport.
     * 
     * @return The transport.
     */
    Transport getTransport();

    /**
     * Returns the serializer.
     * 
     * @return The serializer.
     */
    JsonSerializer getSerializer();

    /**
     * Returns a value indicating whether the connection is connected.
     * 
     * @return A value indicating whether the connection is connected.
     */
    boolean isConnected();

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
     * Sets the connection data.
     * 
     * @param connectionData The connection data.
     */
    void setConnectionData(String connectionData);

    /**
     * Starts the connection.
     * 
     * @param handler The connection handler.
     * @return The start promise.
     */
    Promise<Void> start(ConnectionHandler handler);

    /**
     * Stops the connection.
     * 
     * @return The stop promise.
     */
    Promise<Void> stop();

    /**
     * Sends a message.
     * 
     * @param message The message.
     * @return The send promise.
     */
    Promise<Void> send(String message);

}