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

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.AsyncHttpClientConfig.Builder;

import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.NegotiationResponse;
import net.signalr.client.transport.PingResponse;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportContext;
import net.signalr.client.transport.TransportOptions;
import net.signalr.client.util.AbstractLifecycle;
import net.signalr.client.util.URIBuilder;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;
import net.signalr.client.util.concurrent.Apply;

/**
 * Represents an abstract transport.
 */
public abstract class AbstractTransport extends AbstractLifecycle<TransportContext> implements Transport {

    /**
     * The user agent.
     */
    private static final String USER_AGENT = "SignalR-Client/0.1 (Java; AsyncHttpClient)";

    /**
     * The content length header name.
     */
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";

    /**
     * The content type header name.
     */
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    /**
     * The HTTP client.
     */
    private AsyncHttpClient _httpClient;

    /**
     * Initializes a new instance of the {@link AbstractTransport} class.
     */
    public AbstractTransport() {
        _httpClient = null;
    }

    /**
     * Creates a new HTTP client.
     * 
     * @return The new HTTP client.
     */
    protected AsyncHttpClient newHttpClient() {
        final Builder builder = new Builder();

        builder.setAllowPoolingConnection(true);
        builder.setAllowSslConnectionPool(true);
        builder.setCompressionEnabled(true);
        builder.setUserAgent(USER_AGENT);

        return new AsyncHttpClient(builder.build());
    }

    /**
     * Prepares a GET request.
     * 
     * @param uri The request URI.
     * @return The request builder.
     */
    protected final BoundRequestBuilder prepareGet(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }

        if (_httpClient == null) {
            throw new IllegalStateException("Transport has not been started");
        }

        return _httpClient.prepareGet(uri.toString());
    }

    /**
     * Prepares a POST request.
     * 
     * @param uri The request URI.
     * @return The request builder.
     */
    protected final BoundRequestBuilder preparePost(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }

        if (_httpClient == null) {
            throw new IllegalStateException("Transport has not been started");
        }

        return _httpClient.preparePost(uri.toString());
    }

    @Override
    public final Promise<NegotiationResponse> negotiate(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        // Build request URI.
        final URIBuilder uriBuilder = URIBuilder.resolve(context.getUrl(), NEGOTIATE_URI);
        final Map<String, Collection<String>> parameters = context.getParameters();

        uriBuilder.addParameters(parameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        uriBuilder.addParameter(PROTOCOL_VERSION_PARAMETER, context.getProtocolVersion());
        final URI uri = uriBuilder.build();

        // Setup request.
        final BoundRequestBuilder boundRequestBuilder = prepareGet(uri);
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);

        // Send request.
        final ResponseHandler handler = new ResponseHandler();

        try {
            boundRequestBuilder.execute(handler);
        } catch (final Throwable t) {
            return Promises.newFailure(t);
        }

        return handler.getResponse().then(new Apply<Response, NegotiationResponse>() {
            @Override
            protected NegotiationResponse doApply(final Response response) throws Exception {
                final JsonMapper mapper = context.getMapper();
                final String body = response.getResponseBody();

                return mapper.toObject(body, NegotiationResponse.class);
            }
        });
    }

    @Override
    public final Promise<PingResponse> ping(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        // Build request URI.
        final URIBuilder uriBuilder = URIBuilder.resolve(context.getUrl(), PING_URI);
        final Map<String, Collection<String>> parameters = context.getParameters();

        uriBuilder.addParameters(parameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        final URI uri = uriBuilder.build();

        // Setup request.
        final BoundRequestBuilder boundRequestBuilder = prepareGet(uri);
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);

        // Send request.
        final ResponseHandler handler = new ResponseHandler();

        try {
            boundRequestBuilder.execute(handler);
        } catch (final Throwable t) {
            return Promises.newFailure(t);
        }

        return handler.getResponse().then(new Apply<Response, PingResponse>() {
            @Override
            protected PingResponse doApply(final Response response) throws Exception {
                final JsonMapper mapper = context.getMapper();
                final String body = response.getResponseBody();

                return mapper.toObject(body, PingResponse.class);
            }
        });
    }

    @Override
    public final Promise<Void> abort(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        // Build request URI.
        final URIBuilder uriBuilder = URIBuilder.resolve(context.getUrl(), ABORT_URI);
        final Map<String, Collection<String>> parameters = context.getParameters();

        uriBuilder.addParameters(parameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        final TransportOptions options = context.getTransportOptions();

        uriBuilder.addParameter(CONNECTION_TOKEN_PARAMETER, options.getConnectionToken());
        uriBuilder.addParameter(TRANSPORT_PARAMETER, getName());
        final URI uri = uriBuilder.build();

        // Setup request.
        final BoundRequestBuilder boundRequestBuilder = preparePost(uri);
        final Map<String, Collection<String>> headers = context.getHeaders();

        boundRequestBuilder.setHeaders(headers);
        boundRequestBuilder.addHeader(CONTENT_LENGTH_HEADER, "0");
        boundRequestBuilder.addHeader(CONTENT_TYPE_HEADER, "text/plain");

        // Send request.
        final ResponseHandler handler = new ResponseHandler();

        try {
            boundRequestBuilder.execute(handler);
        } catch (final Throwable t) {
            return Promises.newFailure(t);
        }

        return handler.getResponse().then(new Apply<Response, Void>() {
            @Override
            protected Void doApply(final Response response) throws Exception {
                return null;
            }
        });
    }

    @Override
    protected final void doStart(final TransportContext context) {
        _httpClient = newHttpClient();
    }

    @Override
    protected final void doStop(final TransportContext context) {
        _httpClient.close();
    }
}
