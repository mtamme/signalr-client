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

import java.util.EventListener;

/**
 * Defines a connection listener.
 */
public interface ConnectionListener extends EventListener {

    /**
     * Invoked when the connection is going to be connected.
     */
    void onConnecting();

    /**
     * Invoked when the connection has been connected.
     */
    void onConnected();

    /**
     * Invoked when the connection is going to be reconnected.
     */
    void onReconnecting();

    /**
     * Invoked when the connection has been reconnected.
     */
    void onReconnected();

    /**
     * Invoked when the connection is going to be disconnected.
     */
    void onDisconnecting();

    /**
     * Invoked when the connection has been disconnected.
     */
    void onDisconnected();

    /**
     * Invoked when the connection is slow.
     */
    void onConnectionSlow();

    /**
     * Invoked when an error occurred.
     * 
     * @param cause The cause.
     */
    void onError(Throwable cause);

    /**
     * Invoked when a message is going to be sent.
     * 
     * @param message The message.
     */
    void onSending(String message);

    /**
     * Invoked when a message has been received.
     * 
     * @param message The message.
     */
    void onReceived(String message);
}
