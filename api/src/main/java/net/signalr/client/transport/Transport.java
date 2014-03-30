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

package net.signalr.client.transport;

import net.signalr.client.concurrent.Promise;

/**
 * Defines a transport.
 */
public interface Transport {

    /**
     * The negotiate URI path.
     */
    public static final String NEGOTIATE_PATH = "negotiate";

    /**
     * The ping URI path.
     */
    public static final String PING_PATH = "ping";

    /**
     * The abort URI path.
     */
    public static final String ABORT_PATH = "abort";

    /**
     * The connect URI path.
     */
    public static final String CONNECT_PATH = "connect";

    /**
     * The reconnect URI path.
     */
    public static final String RECONNECT_PATH = "reconnect";

    /**
     * The protocol version query parameter name.
     */
    public static final String PROTOCOL_VERSION_PARAMETER = "clientProtocol";

    /**
     * The connection data query parameter name.
     */
    public static final String CONNECTION_DATA_PARAMETER = "connectionData";

    /**
     * The connection token query parameter name.
     */
    public static final String CONNECTION_TOKEN_PARAMETER = "connectionToken";

    /**
     * The transport query parameter name.
     */
    public static final String TRANSPORT_PARAMETER = "transport";

    /**
     * Returns the transport name.
     * 
     * @return the transport name.
     */
    String getName();

    /**
     * Negotiates the transport.
     * 
     * @param context The transport context.
     * @return The negotiation response.
     */
    Promise<NegotiationResponse> negotiate(TransportContext context);

    /**
     * Connects the transport.
     * 
     * @param context The transport context.
     * @param handler The transport channel handler.
     * @param reconnect A value indicating whether to reconnect.
     * @return The transport channel.
     */
    Promise<TransportChannel> connect(TransportContext context, TransportChannelHandler handler, boolean reconnect);

    /**
     * Performs a transport ping.
     * 
     * @param context The transport context.
     * @return The ping response.
     */
    Promise<PingResponse> ping(TransportContext context);

    /**
     * Aborts the transport.
     * 
     * @param context The transport context.
     * @return The abort response.
     */
    Promise<Void> abort(TransportContext context);
}
