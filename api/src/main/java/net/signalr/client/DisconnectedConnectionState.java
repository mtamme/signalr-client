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

package net.signalr.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.transport.NegotiationResponse;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.Channel;
import net.signalr.client.transport.TransportManager;
import net.signalr.client.util.concurrent.Callback;
import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.Function;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

/**
 * Represents the disconnected connection state.
 */
final class DisconnectedConnectionState implements ConnectionState {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DisconnectedConnectionState.class);

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void addHeader(final ConnectionContext context, final String name, final String value) {
        context.addHeader(name, value);
    }

    @Override
    public void addQueryParameter(final ConnectionContext context, final String name, final String value) {
        context.addQueryParameter(name, value);
    }

    @Override
    public void setConnectionData(final ConnectionContext context, final String connectionData) {
        context.setConnectionData(connectionData);
    }

    @Override
    public Promise<Void> start(final ConnectionContext context, final ConnectionHandler handler) {
        final Deferred<Void> deferred = new Deferred<Void>();
        final ConnectingConnectionState connecting = new ConnectingConnectionState(deferred);

        if (!context.tryChangeState(this, connecting)) {
            return context.getState().start(context, handler);
        }

        handler.onConnecting();

        logger.info("Negotiating transport...");

        final TransportManager manager = context.getTransportManager();
        final Transport transport = manager.getTransport();

        transport.negotiate(context).thenCompose(new Function<NegotiationResponse, Promise<Channel>>() {
            @Override
            public Promise<Channel> apply(final NegotiationResponse response) throws Exception {
                final String protocolVersion = response.getProtocolVersion();

                if (!protocolVersion.equals(context.getProtocolVersion())) {
                    throw new IllegalStateException("Invalid protocol version: '" + protocolVersion + "'");
                }

                context.setTryWebSockets(response.getTryWebSockets());
                context.setConnectionId(response.getConnectionId());
                context.setConnectionToken(response.getConnectionToken());
                context.setDisconnectTimeout(response.getDisconnectTimeout());
                context.setKeepAliveTimeout(response.getKeepAliveTimeout());

                logger.info("Connecting transport...");

                return transport.connect(context, new ChannelHandlerAdapter(context, handler), false);
            }
        }).thenCall(new Callback<Channel>() {
            @Override
            public void onResolved(final Channel channel) {
                final ConnectedConnectionState connected = new ConnectedConnectionState(handler, channel);

                context.changeState(connecting, connected);
                handler.onConnected();
                manager.start(context);
            }

            @Override
            public void onRejected(final Throwable cause) {
                context.changeState(connecting, DisconnectedConnectionState.this);
                handler.onDisconnected();
            }
        }).thenPropagate(deferred);

        return deferred;
    }

    @Override
    public Promise<Void> stop(final ConnectionContext context) {
        return Promises.resolved();
    }

    @Override
    public Promise<Void> send(final ConnectionContext context, final String message) {
        throw new IllegalStateException("Connection is disconnected");
    }

    @Override
    public String toString() {
        return "DISCONNECTED";
    }
}