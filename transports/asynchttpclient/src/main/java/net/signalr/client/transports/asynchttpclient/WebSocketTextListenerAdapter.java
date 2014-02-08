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

package net.signalr.client.transports.asynchttpclient;

import net.signalr.client.concurrent.Deferred;
import net.signalr.client.concurrent.Promise;
import net.signalr.client.transports.TransportChannel;
import net.signalr.client.transports.TransportChannelHandler;

import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;

/**
 * Represents a web socket listener.
 */
final class WebSocketTextListenerAdapter implements WebSocketTextListener {

    /**
     * The transport channel handler.
     */
    private final TransportChannelHandler _handler;

    /**
     * The transport channel.
     */
    private final Deferred<TransportChannel> _channel;

    /**
     * Initializes a new instance of the {@link WebSocketTextListenerAdapter} class.
     * 
     * @param handler The transport channel handler.
     */
    public WebSocketTextListenerAdapter(final TransportChannelHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }

        _handler = handler;
        _channel = new Deferred<TransportChannel>();
    }

    /**
     * Returns the transport channel.
     * 
     * @return The transport channel.
     */
    public Promise<TransportChannel> getChannel() {
        return _channel;
    }

    @Override
    public void onOpen(final WebSocket webSocket) {
        final TransportChannel channel = new WebSocketTransportChannel(_handler, webSocket);

        if (_channel.resolve(channel)) {
            _handler.onOpen();
        }
    }

    @Override
    public void onClose(final WebSocket webSocket) {
        _handler.onClose();
    }

    @Override
    public void onFragment(final String fragment, final boolean last) {
    }

    @Override
    public void onMessage(final String message) {
        _handler.onReceived(message);
    }

    @Override
    public void onError(final Throwable throwable) {
        if (!_channel.reject(throwable)) {
            _handler.onError(throwable);
        }
    }
}
