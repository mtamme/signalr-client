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

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.ChannelHandler;
import net.signalr.client.transport.TransportContext;
import net.signalr.client.transport.TransportOptions;
import net.signalr.client.util.URIBuilder;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

/**
 * Represents the web socket transport.
 */
public final class WebSocketTransport extends AbstractTransport {

    /**
     * The HTTPS URI scheme.
     */
    private static final String HTTPS_SCHEME = "https";

    /**
     * The WS URI scheme.
     */
    private static final String WS_SCHEME = "ws";

    /**
     * The WSS URI scheme.
     */
    private static final String WSS_SCHEME = "wss";

    /**
     * The transport name.
     */
    private static final String NAME = "webSockets";

    /**
     * The web socket client.
     */
    private final WebSocketClient _client;

    /**
     * Initializes a new instance of the {@link WebSocketTransport} class
     */
    public WebSocketTransport() {
        _client = new WebSocketClient(_sslContextFactory);

        // FIXME
        try {
            _client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return NAME;
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

        // Build request URI.
        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), reconnect ? RECONNECT_PATH : CONNECT_PATH);
        final String scheme = uriBuilder.getScheme().equals(HTTPS_SCHEME) ? WSS_SCHEME : WS_SCHEME;

        uriBuilder.setScheme(scheme);
        final Map<String, Collection<String>> parameters = context.getParameters();

        uriBuilder.addParameters(parameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        uriBuilder.addParameter(CONNECTION_TOKEN_PARAMETER, options.getConnectionToken());
        final String transportName = getName();

        uriBuilder.addParameter(TRANSPORT_PARAMETER, transportName);
        final URI uri = uriBuilder.build();

        // Setup request.
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
            _client.connect(listener, uri, request);
        } catch (final Exception e) {
            return Promises.rejected(e);
        }

        return listener.getChannel();
    }
}
