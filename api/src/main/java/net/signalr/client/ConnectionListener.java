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
public interface ConnectionListener {

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
