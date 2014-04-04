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
 * Defines a channel handler.
 */
public interface ChannelHandler {

    /**
     * Invoked when the channel has been opened.
     */
    void handleChannelOpened();

    /**
     * Invoked when the channel has been closed.
     */
    void handleChannelClosed();

    /**
     * Invoked when an error occurred.
     * 
     * @param cause The cause.
     */
    void handleError(Throwable cause);

    /**
     * Invoked when a message is going to be sent.
     * 
     * @param message The message.
     */
    void handleMessageSending(String message);

    /**
     * Invoked when a message was received.
     * 
     * @param message The message.
     */
    void handleMessageReceived(String message);
}
