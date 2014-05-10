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

import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.NegotiationResponse;
import net.signalr.client.transport.PingResponse;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportContext;
import net.signalr.client.transport.TransportException;
import net.signalr.client.transport.TransportOptions;
import net.signalr.client.util.AbstractLifecycle;
import net.signalr.client.util.URIBuilder;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;
import net.signalr.client.util.concurrent.Apply;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;

/**
 * Represents an abstract transport.
 */
public abstract class AbstractTransport extends AbstractLifecycle<TransportContext> implements Transport {

    /**
     * The user agent.
     */
    protected static final String USER_AGENT = "SignalR-Client/0.1 (Java; Jetty)";

    /**
     * The HTTP client.
     */
    private final HttpClient _httpClient;

    /**
     * Initializes a new instance of the {@link AbstractTransport} class.
     * 
     * @param httpClient The HTTP client.
     */
    protected AbstractTransport(final HttpClient httpClient) {
        if (httpClient == null) {
            throw new IllegalArgumentException("HTTP client must not be null");
        }

        _httpClient = httpClient;
        _httpClient.setFollowRedirects(false);
        _httpClient.setUserAgentField(new HttpField(HttpHeader.USER_AGENT, USER_AGENT));
    }

    /**
     * Returns a new request.
     * 
     * @param uri The request URI.
     * @return The request.
     */
    protected final Request newRequest(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }

        return _httpClient.newRequest(uri);
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
        final Request request = newRequest(uri).method(HttpMethod.GET);
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
            return Promises.newFailure(t);
        }

        return listener.getResponse().then(new Apply<String, NegotiationResponse>() {
            @Override
            protected NegotiationResponse doApply(final String response) throws Exception {
                final JsonMapper mapper = context.getMapper();

                return mapper.toObject(response, NegotiationResponse.class);
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
        final Request request = newRequest(uri).method(HttpMethod.GET);
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
            return Promises.newFailure(t);
        }

        return listener.getResponse().then(new Apply<String, PingResponse>() {
            @Override
            protected PingResponse doApply(final String response) throws Exception {
                final JsonMapper mapper = context.getMapper();

                return mapper.toObject(response, PingResponse.class);
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
        final Request request = newRequest(uri).method(HttpMethod.POST);
        final Map<String, Collection<String>> headers = context.getHeaders();

        for (final Map.Entry<String, Collection<String>> header : headers.entrySet()) {
            final String name = header.getKey();

            for (final String value : header.getValue()) {
                request.header(name, value);
            }
        }
        request.header(HttpHeader.CONTENT_LENGTH, "0");
        request.header(HttpHeader.CONTENT_TYPE, "text/plain");

        // Send request.
        final ResponseListener listener = new ResponseListener();

        try {
            request.send(listener);
        } catch (final Throwable t) {
            return Promises.newFailure(t);
        }

        return listener.getResponse().then(new Apply<String, Void>() {
            @Override
            protected Void doApply(final String response) throws Exception {
                return null;
            }
        });
    }

    @Override
    protected void doStart(final TransportContext context) {
        try {
            _httpClient.start();
        } catch (final Exception e) {
            throw new TransportException("Failed to start transport", e);
        }
    }

    @Override
    protected void doStop(final TransportContext context) {
        try {
            _httpClient.stop();
        } catch (final Exception e) {
            throw new TransportException("Failed to stop transport", e);
        }
    }
}
