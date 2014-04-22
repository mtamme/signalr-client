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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.ChannelHandler;
import net.signalr.client.transport.TransportContext;
import net.signalr.client.util.URIBuilder;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

/**
 * Represents the web socket transport.
 */
public final class WebSocketTransport extends AbstractTransport {

    /**
     * The HTTPS URI schema.
     */
    private static final String HTTPS_SCHEMA = "https";

    /**
     * The WS URI schema.
     */
    private static final String WS_SCHEMA = "ws";

    /**
     * The WSS URI schema.
     */
    private static final String WSS_SCHEMA = "wss";

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
        if (!context.getTryWebSockets()) {
            throw new IllegalStateException("WebSockets are not supported by the server");
        }

        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), reconnect ? RECONNECT_PATH : CONNECT_PATH);
        final String schema = uriBuilder.getSchema().equals(HTTPS_SCHEMA) ? WSS_SCHEMA : WS_SCHEMA;

        uriBuilder.setSchema(schema);
        final ClientUpgradeRequest request = new ClientUpgradeRequest();

        // Set query parameters.
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        for (final Map.Entry<String, Collection<String>> queryParameter : queryParameters.entrySet()) {
            final String name = queryParameter.getKey();

            for (final String value : queryParameter.getValue()) {
                // FIXME Set query parameter not header.
                request.setHeader(name, value);
            }
        }
        // FIXME Set query parameter not header.
        request.setHeader(CONNECTION_TOKEN_PARAMETER, context.getConnectionToken());
        // FIXME Set query parameter not header.
        request.setHeader(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        // FIXME Set query parameter not header.
        final String transportName = getName();

        request.setHeader(TRANSPORT_PARAMETER, transportName);

        // Set headers.
        final Map<String, Collection<String>> headers = context.getHeaders();

        for (final Map.Entry<String, Collection<String>> header : headers.entrySet()) {
            final String name = header.getKey();

            for (final String value : header.getValue()) {
                request.setHeader(name, value);
            }
        }

        // Send request.
        final WebSocketListenerAdapter listener = new WebSocketListenerAdapter(handler);

        try {
            _client.connect(listener, uriBuilder.toURI(), request);
        } catch (final IOException e) {
            return Promises.rejected(e);
        }

        return listener.getChannel();
    }
}
