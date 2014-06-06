/*
 * Copyright © Martin Tamme
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.signalr.client;

import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.Transport;
import net.signalr.client.util.concurrent.promise.Promise;

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
     * Adds the specified connection listener.
     * 
     * @param listener The connection listener.
     */
    void addConnectionListener(ConnectionListener listener);

    /**
     * Removes the specified connection listener.
     * 
     * @param listener The connection listener.
     */
    void removeConnectionListener(ConnectionListener listener);

    /**
     * Sets the connection data.
     * 
     * @param connectionData The connection data.
     */
    void setConnectionData(String connectionData);

    /**
     * Starts the connection.
     * 
     * @return The start result.
     */
    Promise<Void> start();

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