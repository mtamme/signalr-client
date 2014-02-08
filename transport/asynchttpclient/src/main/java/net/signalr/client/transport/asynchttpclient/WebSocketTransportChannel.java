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

package net.signalr.client.transport.asynchttpclient;

import com.ning.http.client.websocket.WebSocket;

import net.signalr.client.concurrent.Promise;
import net.signalr.client.concurrent.Promises;
import net.signalr.client.transport.TransportChannel;
import net.signalr.client.transport.TransportChannelHandler;

/**
 * Represents a web socket transport channel.
 */
final class WebSocketTransportChannel implements TransportChannel {

    /**
     * The transport channel handler.
     */
    private final TransportChannelHandler _handler;

    /**
     * The web socket.
     */
    private final WebSocket _webSocket;

    /**
     * Initializes a new instance of the {@link WebSocketTransportChannel} class.
     * 
     * @param handlerThe transport channel handler.
     * @param webSocket The web socket.
     */
    public WebSocketTransportChannel(final TransportChannelHandler handler, final WebSocket webSocket) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }
        if (webSocket == null) {
            throw new IllegalArgumentException("WebSocket must not be null");
        }

        _handler = handler;
        _webSocket = webSocket;
    }

    @Override
    public Promise<Void> send(final String message) {
        _handler.onSending(message);

        try {
            _webSocket.sendTextMessage(message);
        } catch (Throwable t) {
            return Promises.rejected(t);
        }

        return Promises.resolved();
    }

    @Override
    public Promise<Void> close() {
        try {
            _webSocket.close();
            return Promises.resolved();
        } catch (Throwable t) {
            return Promises.rejected(t);
        }
    }
}
