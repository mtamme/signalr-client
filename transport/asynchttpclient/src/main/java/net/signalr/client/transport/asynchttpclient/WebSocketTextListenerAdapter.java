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

package net.signalr.client.transport.asynchttpclient;

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.ChannelHandler;
import net.signalr.client.util.concurrent.promise.Deferred;
import net.signalr.client.util.concurrent.promise.Promise;

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
