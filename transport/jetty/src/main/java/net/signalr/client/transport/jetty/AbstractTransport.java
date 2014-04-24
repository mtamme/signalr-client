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

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;

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
    protected static final String USER_AGENT = "SignalR-Client/0.1 (Java)";

    /**
     * The SSL context factory.
     */
    protected final SslContextFactory _sslContextFactory;

    /**
     * The HTTP client.
     */
    protected final HttpClient _client;

    /**
     * Initializes a new instance of the {@link AbstractTransport} class.
     */
    public AbstractTransport() {
        _sslContextFactory = new SslContextFactory();
        _client = new HttpClient(_sslContextFactory);
        _client.setUserAgentField(new HttpField(HttpHeader.USER_AGENT, USER_AGENT));

        // FIXME
        try {
            _client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Promise<NegotiationResponse> negotiate(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        // Build request URI.
        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), NEGOTIATE_PATH);
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        uriBuilder.addParameters(queryParameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        uriBuilder.addParameter(PROTOCOL_VERSION_PARAMETER, context.getProtocolVersion());
        final URI uri = uriBuilder.build();

        // Setup request.
        final Request request = _client.newRequest(uri);

        request.method(HttpMethod.GET);
        final Map<String, Collection<String>> headers = context.getHeaders();

        for (final Map.Entry<String, Collection<String>> header : headers.entrySet()) {
            final String name = header.getKey();

            for (final String value : header.getValue()) {
                request.header(name, value);
            }
        }

        // Send request.
        final ResponseListener listener = new ResponseListener();

        try {
            request.send(listener);
        } catch (final Throwable t) {
            return Promises.rejected(t);
        }

        return listener.getResponse().thenApply(new Function<String, NegotiationResponse>() {
            @Override
            public NegotiationResponse apply(final String response) throws Exception {
                final JsonMapper mapper = context.getMapper();

                return mapper.toObject(response, NegotiationResponse.class);
            }
        });
    }

    @Override
    public Promise<PingResponse> ping(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        // Build request URI.
        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), PING_PATH);
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        uriBuilder.addParameters(queryParameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        final URI uri = uriBuilder.build();

        // Setup request.
        final Request request = _client.newRequest(uri);

        request.method(HttpMethod.GET);
        final Map<String, Collection<String>> headers = context.getHeaders();

        for (final Map.Entry<String, Collection<String>> header : headers.entrySet()) {
            final String name = header.getKey();

            for (final String value : header.getValue()) {
                request.header(name, value);
            }
        }

        // Send request.
        final ResponseListener listener = new ResponseListener();

        try {
            request.send(listener);
        } catch (final Throwable t) {
            return Promises.rejected(t);
        }

        return listener.getResponse().thenApply(new Function<String, PingResponse>() {
            @Override
            public PingResponse apply(final String response) throws Exception {
                final JsonMapper mapper = context.getMapper();

                return mapper.toObject(response, PingResponse.class);
            }
        });
    }

    @Override
    public Promise<Void> abort(final TransportContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        // Build request URI.
        final URIBuilder uriBuilder = new URIBuilder(context.getUrl(), ABORT_PATH);
        final Map<String, Collection<String>> queryParameters = context.getQueryParameters();

        uriBuilder.addParameters(queryParameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        uriBuilder.addParameter(CONNECTION_TOKEN_PARAMETER, context.getConnectionToken());
        final String transportName = getName();

        uriBuilder.addParameter(TRANSPORT_PARAMETER, transportName);
        final URI uri = uriBuilder.build();

        // Setup request.
        final Request request = _client.newRequest(uri);

        request.method(HttpMethod.POST);
        final Map<String, Collection<String>> headers = context.getHeaders();

        for (final Map.Entry<String, Collection<String>> header : headers.entrySet()) {
            final String name = header.getKey();

            for (final String value : header.getValue()) {
                request.header(name, value);
            }
        }

        // Set content.
        request.content(new BytesContentProvider());

        // Send request.
        final ResponseListener listener = new ResponseListener();

        try {
            request.send(listener);
        } catch (final Throwable t) {
            return Promises.rejected(t);
        }

        return listener.getResponse().thenApply(new Function<String, Void>() {
            @Override
            public Void apply(final String response) throws Exception {
                return null;
            }
        });
    }
}
