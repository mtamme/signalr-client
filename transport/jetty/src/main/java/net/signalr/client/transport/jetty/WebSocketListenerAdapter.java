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

package net.signalr.client.transport.jetty;

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.ChannelHandler;
import net.signalr.client.util.concurrent.promise.Deferred;
import net.signalr.client.util.concurrent.promise.Promise;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

/**
 * Represents a web socket listener adapter.
 */
final class WebSocketListenerAdapter implements WebSocketListener {

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

        _channel = new Deferred<>();
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
    public void onWebSocketBinary(final byte[] payload, final int offset, final int len) {
    }

    @Override
    public void onWebSocketClose(final int statusCode, final String reason) {
        _handler.handleChannelClosed();
    }

    @Override
    public void onWebSocketConnect(final Session session) {
        final WebSocketChannel channel = new WebSocketChannel(_handler, session);

        if (_channel.trySuccess(channel)) {
            _handler.handleChannelOpened();
        }
    }

    @Override
    public void onWebSocketError(final Throwable cause) {
        if (!_channel.tryFailure(cause)) {
            _handler.handleError(cause);
        }
    }

    @Override
    public void onWebSocketText(final String message) {
        _handler.handleMessageReceived(message);
    }
}
