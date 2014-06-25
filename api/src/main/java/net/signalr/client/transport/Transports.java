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

package net.signalr.client.transport;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import net.signalr.client.util.URIBuilder;

/**
 * Provides {@link Transport} extension methods.
 */
public final class Transports {

    /**
     * The long polling transport name.
     */
    public static final String LONG_POLLING = "longPolling";

    /**
     * The server sent events transport name.
     */
    public static final String SERVER_SENT_EVENTS = "serverSentEvents";

    /**
     * The web sockets transport name.
     */
    public static final String WEB_SOCKETS = "webSockets";

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
     * The negotiate URI.
     */
    private static final String NEGOTIATE_URI = "negotiate";

    /**
     * The ping URI.
     */
    private static final String PING_URI = "ping";

    /**
     * The abort URI.
     */
    private static final String ABORT_URI = "abort";

    /**
     * The connect URI.
     */
    private static final String CONNECT_URI = "connect";

    /**
     * The reconnect URI.
     */
    private static final String RECONNECT_URI = "reconnect";

    /**
     * The connection data parameter name.
     */
    private static final String CONNECTION_DATA_PARAMETER = "connectionData";

    /**
     * The connection token parameter name.
     */
    private static final String CONNECTION_TOKEN_PARAMETER = "connectionToken";

    /**
     * The protocol version parameter name.
     */
    private static final String PROTOCOL_VERSION_PARAMETER = "clientProtocol";

    /**
     * The transport parameter name.
     */
    private static final String TRANSPORT_PARAMETER = "transport";

    /**
     * Initializes a new instance of the {@link Transports} class.
     */
    private Transports() {
    }

    /**
     * Builds the negotiate URI.
     * 
     * @param context The transport context.
     * @return The negotiate URI.
     */
    public static URI buildNegotiateUri(final TransportContext context) {
        final URIBuilder uriBuilder = URIBuilder.resolve(context.getUrl(), NEGOTIATE_URI);
        final Map<String, Collection<String>> parameters = context.getParameters();

        uriBuilder.addParameters(parameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        uriBuilder.addParameter(PROTOCOL_VERSION_PARAMETER, context.getProtocolVersion());

        return uriBuilder.build();
    }

    /**
     * Builds the ping URI.
     * 
     * @param context The transport context.
     * @return The ping URI.
     */
    public static URI buildPingUri(final TransportContext context) {
        final URIBuilder uriBuilder = URIBuilder.resolve(context.getUrl(), PING_URI);
        final Map<String, Collection<String>> parameters = context.getParameters();

        uriBuilder.addParameters(parameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());

        return uriBuilder.build();
    }

    /**
     * Builds the connect URI.
     * 
     * @param context The transport context.
     * @param transport The transport.
     * @param reconnect A value indicating whether to reconnect.
     * @return The connect URI.
     */
    public static URI buildConnectUri(final TransportContext context, final Transport transport, final boolean reconnect) {
        final URIBuilder uriBuilder = URIBuilder.resolve(context.getUrl(), reconnect ? RECONNECT_URI : CONNECT_URI);
        final String transportName = transport.getName();

        if (WEB_SOCKETS.equals(transportName)) {
            final String scheme = uriBuilder.getScheme().equals(HTTPS_SCHEME) ? WSS_SCHEME : WS_SCHEME;

            uriBuilder.setScheme(scheme);
        }
        final Map<String, Collection<String>> parameters = context.getParameters();

        uriBuilder.addParameters(parameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        uriBuilder.addParameter(CONNECTION_TOKEN_PARAMETER, context.getTransportOptions().getConnectionToken());
        uriBuilder.addParameter(TRANSPORT_PARAMETER, transport.getName());

        return uriBuilder.build();
    }

    /**
     * Builds the abort URI.
     * 
     * @param context The transport context.
     * @param transport The transport.
     * @return The abort URI.
     */
    public static URI buildAbortUri(final TransportContext context, final Transport transport) {
        final URIBuilder uriBuilder = URIBuilder.resolve(context.getUrl(), ABORT_URI);
        final Map<String, Collection<String>> parameters = context.getParameters();

        uriBuilder.addParameters(parameters);
        uriBuilder.addParameter(CONNECTION_DATA_PARAMETER, context.getConnectionData());
        final TransportOptions options = context.getTransportOptions();

        uriBuilder.addParameter(CONNECTION_TOKEN_PARAMETER, options.getConnectionToken());
        uriBuilder.addParameter(TRANSPORT_PARAMETER, transport.getName());

        return uriBuilder.build();
    }
}
