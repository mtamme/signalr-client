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

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.ChannelHandler;
import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;

/**
 * Represents a web socket listener.
 */
final class WebSocketTextListenerAdapter implements WebSocketTextListener {

    /**
     * The channel handler.
     */
    private final ChannelHandler _handler;

    /**
     * The channel.
     */
    private final Deferred<Channel> _channel;

    /**
     * Initializes a new instance of the {@link WebSocketTextListenerAdapter} class.
     * 
     * @param handler The channel handler.
     */
    public WebSocketTextListenerAdapter(final ChannelHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }

        _handler = handler;
        _channel = Promises.newDeferred();
    }

    /**
     * Returns the channel.
     * 
     * @return The channel.
     */
    public Promise<Channel> getChannel() {
        return _channel;
    }

    @Override
    public void onOpen(final WebSocket webSocket) {
        final Channel channel = new WebSocketChannel(_handler, webSocket);

        if (_channel.trySuccess(channel)) {
            _handler.handleChannelOpened();
        }
    }

    @Override
    public void onClose(final WebSocket webSocket) {
        _handler.handleChannelClosed();
    }

    @Override
    public void onFragment(final String fragment, final boolean last) {
    }

    @Override
    public void onMessage(final String message) {
        _handler.handleMessageReceived(message);
    }

    @Override
    public void onError(final Throwable cause) {
        if (!_channel.tryFailure(cause)) {
            _handler.handleError(cause);
        }
    }
}
