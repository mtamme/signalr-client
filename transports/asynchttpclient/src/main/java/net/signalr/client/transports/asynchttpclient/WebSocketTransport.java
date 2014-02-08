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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

import net.signalr.client.concurrent.Promise;
import net.signalr.client.concurrent.Promises;
import net.signalr.client.transports.TransportChannel;
import net.signalr.client.transports.TransportContext;
import net.signalr.client.transports.TransportChannelHandler;
import net.signalr.client.util.URIBuilder;

/**
 * Represents the web socket transport.
 */
public final class WebSocketTransport extends AbstractTransport {

    @Override
    public String getName() {
        return "webSockets";
    }

    @Override
    public Promise<TransportChannel> connect(final TransportContext context, final TransportChannelHandler handler, boolean reconnect) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }
        if (!context.getTryWebSockets()) {
            throw new IllegalStateException("WebSockets are not supported by the server");
        }

        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), reconnect ? "reconnect" : "connect");
        final String schema = uriBuilder.getSchema().equals("https") ? "wss" : "ws";

        uriBuilder.setSchema(schema);
        final BoundRequestBuilder boundRequestBuilder = _client.prepareGet(uriBuilder.toString());

        // Set query parameters.
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        boundRequestBuilder.setQueryParameters(new FluentStringsMap(queryParameters));
        boundRequestBuilder.addQueryParameter("connectionToken", context.getConnectionToken());
        boundRequestBuilder.addQueryParameter("connectionData", context.getConnectionData());
        final String transportName = getName();

        boundRequestBuilder.addQueryParameter("transport", transportName);

        // Set headers.
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);

        // Setup WebSocket upgrade handler.
        final WebSocketUpgradeHandler.Builder builder = new WebSocketUpgradeHandler.Builder();
        final WebSocketTextListenerAdapter listener = new WebSocketTextListenerAdapter(handler);

        builder.addWebSocketListener(listener);

        // Execute request.
        try {
            boundRequestBuilder.execute(builder.build());
        } catch (final IOException e) {
            return Promises.rejected(e);
        }

        return listener.getChannel();
    }
}
