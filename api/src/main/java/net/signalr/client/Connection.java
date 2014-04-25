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

import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.Transport;
import net.signalr.client.util.concurrent.Promise;

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
     * Returns the mapper.
     * 
     * @return The mapper.
     */
    JsonMapper getMapper();

    /**
     * Returns a value indicating whether the connection is connected.
     * 
     * @return A value indicating whether the connection is connected.
     */
    boolean isConnected();

    /**
     * Adds the specified header name and value.
     * 
     * @param name The header name.
     * @param value The header value.
     */
    void addHeader(String name, String value);

    /**
     * Adds the specified parameter name and value.
     * 
     * @param name The parameter name.
     * @param value The parameter value.
     */
    void addParameter(String name, String value);

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
     * @return The start result.
     */
    Promise<Void> start(ConnectionHandler handler);

    /**
     * Stops the connection.
     * 
     * @return The stop result.
     */
    Promise<Void> stop();

    /**
     * Sends a message.
     * 
     * @param message The message.
     * @return The send result.
     */
    Promise<Void> send(String message);

}