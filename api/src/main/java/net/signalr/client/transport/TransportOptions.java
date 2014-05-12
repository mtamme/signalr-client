/*
 * Copyright 2014 Martin Tamme
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