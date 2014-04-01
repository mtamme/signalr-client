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

/**
 * Defines a connection handler.
 */
public interface ConnectionHandler {

    /**
     * Called when the connection is going to be connected.
     */
    void onConnecting();

    /**
     * Called when the connection has been connected.
     */
    void onConnected();

    /**
     * Called when the connection is going to be reconnected.
     */
    void onReconnecting();

    /**
     * Called when the connection has been reconnected.
     */
    void onReconnected();

    /**
     * Called when the connection is going to be disconnected.
     */
    void onDisconnecting();

    /**
     * Called when the connection has been disconnected.
     */
    void onDisconnected();

    /**
     * Called when the connection is slow.
     */
    void onConnectionSlow();

    /**
     * Called when a message is going to be sent.
     * 
     * @param message The message.
     */
    void onSending(String message);

    /**
     * Called when a message has been received.
     * 
     * @param message The message.
     */
    void onReceived(String message);

    /**
     * Called when an error occurred.
     * 
     * @param throwable The error.
     */
    void onError(Throwable throwable);
}
