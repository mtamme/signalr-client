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

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

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
     * The WebSocket client.
     */
    private final WebSocketClient _webSocketClient;

    /**
     * Initializes a new instance of the {@link WebSocketTransport} class.
     */
    public WebSocketTransport() {
        this(new SslContextFactory());
    }

    /**
     * Initializes a new instance of the {@link WebSocketTransport} class.
     * 
     * @param sslContextFactory The SSL context factory.
     */
    public WebSocketTransport(final SslContextFactory sslContextFactory) {
        this(new HttpClient(sslContextFactory), new WebSocketClient(sslContextFactory));
    }

    /**
     * Initializes a new instance of the {@link WebSocketTransport} class.
     * 
     * @param httpClient The HTTP client.
     * @param webSocketClient The WebSocket client.
     */
    public WebSocketTransport(final HttpClient httpClient, final WebSocketClient webSocketClient) {
        super(httpClient);

        if (webSocketClient == null) {
            throw new IllegalArgumentException("WebSocket client must not be null");
        }

        _webSocketClient = webSocketClient;
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
        final ClientUpgradeRequest request = new ClientUpgradeRequest();
        final Map<String, Collection<String>> headers = context.getHeaders();

        for (final Map.Entry<String, Collection<String>> header : headers.entrySet()) {
            final String name = header.getKey();

            for (final String value : header.getValue()) {
                request.setHeader(name, value);
            }
        }
        request.setHeader("User-Agent", USER_AGENT);

        // Send request.
        final WebSocketListenerAdapter listener = new WebSocketListenerAdapter(handler);

        try {
            _webSocketClient.connect(listener, uri, request);
        } catch (final Exception e) {
            return Promises.newFailure(e);
        }

        return listener.getChannel();
    }

    @Override
    protected void doStart(final TransportContext context) throws Exception {
        try {
            _webSocketClient.start();
        } finally {
            super.doStart(context);
        }
    }

    @Override
    protected void doStop(final TransportContext context) throws Exception {
        try {
            _webSocketClient.stop();
        } finally {
            super.doStop(context);
        }
    }
}
