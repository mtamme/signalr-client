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

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.ChannelHandler;
import net.signalr.client.transport.TransportContext;
import net.signalr.client.transport.TransportOptions;
import net.signalr.client.transport.Transports;
import net.signalr.client.util.concurrent.promise.Promise;
import net.signalr.client.util.concurrent.promise.Promises;

/**
 * Represents the WebSocket transport.
 */
public final class WebSocketTransport extends AbstractTransport {

    /**
     * Initializes a new instance of the {@link WebSocketTransport} class.
     */
    public WebSocketTransport() {
        this(null);
    }

    /**
     * Initializes a new instance of the {@link WebSocketTransport} class.
     * 
     * @param httpProviderClass The HTTP provider class.
     */
    public WebSocketTransport(final String httpProviderClass) {
        super(httpProviderClass);
    }

    @Override
    public String getName() {
        return Transports.WEB_SOCKETS;
    }

    @Override
    public Promise<Channel> connect(final TransportContext context, final ChannelHandler handler, boolean reconnect) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }

        final TransportOptions options = context.getTransportOptions();

        if (!options.getTryWebSockets()) {
            throw new IllegalStateException("WebSockets are not supported by the server");
        }

        // Setup request.
        final URI uri = Transports.buildConnectUri(context, this, reconnect);
        final BoundRequestBuilder boundRequestBuilder = prepareGet(uri);
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);

        // Send request.
        final WebSocketUpgradeHandler.Builder builder = new WebSocketUpgradeHandler.Builder();
        final WebSocketTextListenerAdapter listener = new WebSocketTextListenerAdapter(handler);

        builder.addWebSocketListener(listener);

        try {
            boundRequestBuilder.execute(builder.build());
        } catch (final IOException e) {
            return Promises.newFailure(e);
        }

        return listener.getChannel();
    }
}
