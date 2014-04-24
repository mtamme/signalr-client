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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

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

        // Build request URI.
        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), reconnect ? RECONNECT_PATH : CONNECT_PATH);
        final String scheme = uriBuilder.getScheme().equals(HTTPS_SCHEME) ? WSS_SCHEME : WS_SCHEME;

        uriBuilder.setScheme(scheme);
        final String transportName = getName();

        uriBuilder.addParameter(TRANSPORT_PARAMETER, transportName);
        uriBuilder.addParameter(CONNECTION_TOKEN_PARAMETER, context.getConnectionToken());
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        uriBuilder.addParameters(queryParameters);

        // Setup request.
        final BoundRequestBuilder boundRequestBuilder = _client.prepareGet(uriBuilder.toString());
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);

        // Execute request.
        final WebSocketUpgradeHandler.Builder builder = new WebSocketUpgradeHandler.Builder();
        final WebSocketTextListenerAdapter listener = new WebSocketTextListenerAdapter(handler);

        builder.addWebSocketListener(listener);

        try {
            boundRequestBuilder.execute(builder.build());
        } catch (final IOException e) {
            return Promises.rejected(e);
        }

        return listener.getChannel();
    }
}
