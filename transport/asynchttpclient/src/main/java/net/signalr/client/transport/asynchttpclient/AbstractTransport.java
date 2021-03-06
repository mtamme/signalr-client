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
import net.signalr.client.transport.Transports;
import net.signalr.client.transport.TransportContext;
import net.signalr.client.util.AbstractLifecycle;
import net.signalr.client.util.concurrent.promise.Apply;
import net.signalr.client.util.concurrent.promise.Promise;
import net.signalr.client.util.concurrent.promise.Promises;

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
     * The HTTP provider class.
     */
    private final String _httpProviderClass;

    /**
     * The HTTP client.
     */
    private AsyncHttpClient _httpClient;

    /**
     * Initializes a new instance of the {@link AbstractTransport} class.
     * 
     * @param httpProviderClass The HTTP provider class.
     */
    protected AbstractTransport(final String httpProviderClass) {
        _httpProviderClass = httpProviderClass;
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

        if (_httpProviderClass != null) {
            return new AsyncHttpClient(_httpProviderClass, builder.build());
        }

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

        // Setup request.
        final URI uri = Transports.buildNegotiateUri(context);
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

        // Setup request.
        final URI uri = Transports.buildPingUri(context);
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

        // Setup request.
        final URI uri = Transports.buildAbortUri(context, this);
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
    protected final void doStart(final TransportContext context) throws Exception {
        _httpClient = newHttpClient();
    }

    @Override
    protected final void doStop(final TransportContext context) throws Exception {
        _httpClient.close();
    }
}
