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

import java.util.Collection;
import java.util.Map;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.AsyncHttpClientConfig.Builder;

import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.NegotiationResponse;
import net.signalr.client.transport.PingResponse;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportContext;
import net.signalr.client.util.URIBuilder;
import net.signalr.client.util.concurrent.Function;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

/**
 * Represents an abstract transport.
 */
public abstract class AbstractTransport implements Transport {

    /**
     * The user agent.
     */
    private static final String USER_AGENT = "SignalR-Client/0.1 (Java)";

    /**
     * The empty body.
     */
    private static final byte[] EMPTY_BODY = new byte[0];

    /**
     * The asynchronous HTTP client.
     */
    protected final AsyncHttpClient _client;

    /**
     * Initializes a new instance of the {@link AbstractTransport} class.
     */
    public AbstractTransport() {
        final Builder builder = new Builder();

        builder.setAllowPoolingConnection(true);
        builder.setAllowSslConnectionPool(true);
        builder.setCompressionEnabled(true);
        builder.setUserAgent(USER_AGENT);

        _client = new AsyncHttpClient(builder.build());
    }

    @Override
    public Promise<NegotiationResponse> negotiate(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), NEGOTIATE_PATH);
        final BoundRequestBuilder boundRequestBuilder = _client.prepareGet(uriBuilder.toString());

        // Set query parameters.
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        boundRequestBuilder.setQueryParameters(new FluentStringsMap(queryParameters));
        boundRequestBuilder.addQueryParameter(PROTOCOL_VERSION_PARAMETER, context.getProtocolVersion());
        boundRequestBuilder.addQueryParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());

        // Set headers.
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);

        // Execute request.
        final AsyncResponseHandler handler = new AsyncResponseHandler();

        try {
            boundRequestBuilder.execute(handler);
        } catch (final Throwable t) {
            return Promises.rejected(t);
        }

        return handler.getResponse().thenApply(new Function<Response, NegotiationResponse>() {
            @Override
            public NegotiationResponse apply(final Response response) throws Exception {
                final JsonMapper mapper = context.getMapper();
                final String body = response.getResponseBody();

                return mapper.fromJson(body, NegotiationResponse.class);
            }
        });
    }

    @Override
    public Promise<PingResponse> ping(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), PING_PATH);
        final BoundRequestBuilder boundRequestBuilder = _client.prepareGet(uriBuilder.toString());

        // Set query parameters.
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        boundRequestBuilder.setQueryParameters(new FluentStringsMap(queryParameters));
        boundRequestBuilder.addQueryParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());

        // Set headers.
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);

        // Execute request.
        final AsyncResponseHandler handler = new AsyncResponseHandler();

        try {
            boundRequestBuilder.execute(handler);
        } catch (final Throwable t) {
            return Promises.rejected(t);
        }

        return handler.getResponse().thenApply(new Function<Response, PingResponse>() {
            @Override
            public PingResponse apply(final Response response) throws Exception {
                final JsonMapper mapper = context.getMapper();
                final String body = response.getResponseBody();

                return mapper.fromJson(body, PingResponse.class);
            }
        });
    }

    @Override
    public Promise<Void> abort(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), ABORT_PATH);
        final BoundRequestBuilder boundRequestBuilder = _client.preparePost(uriBuilder.toString());

        // Set query parameters.
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        boundRequestBuilder.setQueryParameters(new FluentStringsMap(queryParameters));
        boundRequestBuilder.addQueryParameter(CONNECTION_TOKEN_PARAMETER, context.getConnectionToken());
        boundRequestBuilder.addQueryParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        final String transportName = getName();

        boundRequestBuilder.addQueryParameter(TRANSPORT_PARAMETER, transportName);

        // Set headers.
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);

        // Set body.
        boundRequestBuilder.setBody(EMPTY_BODY);

        // Execute request.
        final AsyncResponseHandler handler = new AsyncResponseHandler();

        try {
            boundRequestBuilder.execute(handler);
        } catch (final Throwable t) {
            return Promises.rejected(t);
        }

        return handler.getResponse().thenApply(new Function<Response, Void>() {
            @Override
            public Void apply(final Response response) throws Exception {
                return null;
            }
        });
    }
}
