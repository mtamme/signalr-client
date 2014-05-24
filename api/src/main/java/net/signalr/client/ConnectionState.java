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
     * Notifies the specified connection listener.
     * 
     * @param listener The connection listener.
     */
    void notifyConnectionListener(ConnectionListener listener);

    /**
     * Connects the connection.
     * 
     * @param context The connection context.
     * @return The connect result.
     */
    Promise<Void> connect(ConnectionContext context);

    /**
     * Reconnects the connection
     * 
     * @param context The connection context.
     * @return The reconnect result.
     */
    Promise<Void> reconnect(ConnectionContext context);

    /**
     * Disconnects the connection.
     * 
     * @param context The connection context.
     * @return The disconnect result.
     */
    Promise<Void> disconnect(ConnectionContext context);

    /**
     * Sends a message.
     * 
     * @param context The connection context.
     * @param message The message.
     * @return The send result.
     */
    Promise<Void> send(ConnectionContext context, String message);
}