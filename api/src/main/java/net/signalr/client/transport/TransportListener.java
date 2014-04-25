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

package net.signalr.client.transport;

/**
 * Defines a transport listener.
 */
public interface TransportListener {

    /**
     * Invoked when the channel has been opened.
     */
    void onChannelOpened();

    /**
     * Invoked when the channel has been closed.
     */
    void onChannelClosed();

    /**
     * Invoked when the connection is slow.
     */
    void onConnectionSlow();

    /**
     * Invoked when the connection is lost.
     */
    void onConnectionLost();

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
