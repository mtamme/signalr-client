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

package net.signalr.client.transport.jetty;

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.ChannelHandler;
import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.Promise;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;

/**
 * Represents a web socket listener adapter.
 */
final class WebSocketListenerAdapter implements UpgradeListener, WebSocketListener {

    /**
     * The channel handler.
     */
    private final ChannelHandler _handler;

    /**
     * The channel.
     */
    private final Deferred<Channel> _channel;

    /**
     * Initializes a new instance of the {@link WebSocketListenerAdapter} class.
     * 
     * @param handler The channel handler.
     */
    public WebSocketListenerAdapter(final ChannelHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }

        _handler = handler;

        _channel = new Deferred<Channel>();
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
    public void onHandshakeRequest(final UpgradeRequest request) {
    }

    @Override
    public void onHandshakeResponse(final UpgradeResponse response) {
    }

    @Override
    public void onWebSocketBinary(final byte[] payload, final int offset, final int len) {
    }

    @Override
    public void onWebSocketClose(final int statusCode, final String reason) {
        _handler.handleChannelClosed();
    }

    @Override
    public void onWebSocketConnect(final Session session) {
        final WebSocketChannel channel = new WebSocketChannel(_handler, session);

        _channel.resolve(channel);
        _handler.handleChannelOpened();
    }

    @Override
    public void onWebSocketError(final Throwable cause) {
        _handler.handleError(cause);
    }

    @Override
    public void onWebSocketText(final String message) {
        _handler.handleMessageReceived(message);
    }
}
