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

import net.signalr.client.transport.TransportListener;

/**
 * Defines a connection manager.
 */
interface ConnectionManager extends TransportListener {

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
     * Notifies all connection listeners that the connection is connecting.
     */
    void notifyOnConnecting();

    /**
     * Notifies all connection listeners that the connection is connected.
     */
    void notifyOnConnected();

    /**
     * Notifies all connection listeners that the connection is reconnecting.
     */
    void notifyOnReconnecting();

    /**
     * Notifies all connection listeners that the connection is reconnected.
     */
    void notifyOnReconnected();

    /**
     * Notifies all connection listeners that the connection is disconnecting.
     */
    void notifyOnDisconnecting();

    /**
     * Notifies all connection listeners that the connection is disconnected.
     */
    void notifyOnDisconnected();

    /**
     * Notifies all connection listeners that a connection error occurred.
     */
    void notifyOnError(Throwable cause);
}
