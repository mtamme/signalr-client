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

import net.signalr.client.concurrent.Callback;
import net.signalr.client.concurrent.Deferred;
import net.signalr.client.concurrent.Function;
import net.signalr.client.concurrent.Promise;
import net.signalr.client.concurrent.Promises;
import net.signalr.client.transport.DefaultTransportManager;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportChannel;
import net.signalr.client.transport.TransportManager;

/**
 * Represents the connected connection state.
 */
final class ConnectedConnectionState implements ConnectionState {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ConnectedConnectionState.class);

    private final ConnectionHandler _handler;

    private final ConnectionContext _context;

    private final TransportChannel _channel;

    private final TransportManager _manager;

    public ConnectedConnectionState(final ConnectionHandler handler, final ConnectionContext context, final TransportChannel channel) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        if (channel == null) {
            throw new IllegalArgumentException("Channel must not be null");
        }

        _handler = handler;
        _context = context;
        _channel = channel;

        _manager = new DefaultTransportManager(context.getTransport());
    }

    @Override
    public void onEnterState() {
        _manager.start(_context);
    }

    @Override
    public void onLeaveState() {
        _manager.stop(_context);
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void addHeader(final ConnectionContext context, final String name, final String value) {
        throw new IllegalStateException("Connection is connected");
    }

    @Override
    public void addQueryParameter(final ConnectionContext context, final String name, final String value) {
        throw new IllegalStateException("Connection is connected");
    }

    @Override
    public void setConnectionData(final ConnectionContext context, final String connectionData) {
        throw new IllegalStateException("Connection is connected");
    }

    @Override
    public Promise<Void> start(final ConnectionContext context, final ConnectionHandler handler) {
        return Promises.resolved();
    }

    @Override
    public Promise<Void> stop(final ConnectionContext context) {
        final Deferred<Void> deferred = new Deferred<Void>();
        final DisconnectingConnectionState disconnecting = new DisconnectingConnectionState(deferred);

        if (!context.tryChangeState(this, disconnecting)) {
            return context.getState().stop(context);
        }

        _handler.onDisconnecting();

        logger.info("Closing transport channel...");

        _channel.close().thenCompose(new Function<Void, Promise<Void>>() {
            @Override
            public Promise<Void> apply(final Void source) throws Exception {
                logger.info("Aborting transport...");

                final Transport transport = context.getTransport();

                return transport.abort(context);
            }
        }).thenCall(new Callback<Void>() {
            @Override
            public void onResolved(final Void value) {
                final DisconnectedConnectionState disconnected = new DisconnectedConnectionState();

                context.setTryWebSockets(false);
                context.setConnectionId(null);
                context.setConnectionToken(null);
                context.setDisconnectTimeout(-1);
                context.setKeepAliveTimeout(-1);

                context.changeState(disconnecting, disconnected);
                _handler.onDisconnected();
            }

            @Override
            public void onRejected(final Throwable throwable) {
                context.changeState(disconnecting, ConnectedConnectionState.this);
                _handler.onConnected();
            }
        }).thenPropagate(deferred);

        return deferred;
    }

    @Override
    public Promise<Void> send(final ConnectionContext context, final String message) {
        return _channel.send(message);
    }

    @Override
    public String toString() {
        return "CONNECTED";
    }
}
