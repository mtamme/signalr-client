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

import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.NegotiationResponse;
import net.signalr.client.transport.PingResponse;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportContext;
import net.signalr.client.transport.Transports;
import net.signalr.client.util.AbstractLifecycle;
import net.signalr.client.util.concurrent.promise.Apply;
import net.signalr.client.util.concurrent.promise.Promise;
import net.signalr.client.util.concurrent.promise.Promises;

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

        // Setup request.
        final URI uri = Transports.buildNegotiateUri(context);
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

        // Setup request.
        final URI uri = Transports.buildPingUri(context);
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

        // Setup request.
        final URI uri = Transports.buildAbortUri(context, this);
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
    protected void doStart(final TransportContext context) throws Exception {
        _httpClient.start();
    }

    @Override
    protected void doStop(final TransportContext context) throws Exception {
        _httpClient.stop();
    }
}
