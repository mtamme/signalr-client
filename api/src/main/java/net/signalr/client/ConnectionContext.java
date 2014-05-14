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

import net.signalr.client.transport.TransportContext;
import net.signalr.client.transport.TransportManager;
import net.signalr.client.transport.TransportOptions;

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
     * Returns the connection notifier.
     * 
     * @return The connection notifier.
     */
    ConnectionNotifier getConnectionNotifier();

    /**
     * Sets the connection data.
     * 
     * @param connectionData The connection data.
     */
    void setConnectionData(String connectionData);

    /**
     * Sets the transport options.
     * 
     * @param options The transport options.
     */
    void setTransportOptions(TransportOptions options);

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
