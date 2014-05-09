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

import net.signalr.client.transport.Transport;
import net.signalr.client.transport.Channel;
import net.signalr.client.transport.TransportManager;
import net.signalr.client.util.concurrent.Catch;
import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.OnComplete;
import net.signalr.client.util.concurrent.OnFailure;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;
import net.signalr.client.util.concurrent.Run;
import net.signalr.client.util.concurrent.RunAsync;

/**
 * Represents the connected connection state.
 */
final class ConnectedConnectionState implements ConnectionState {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ConnectedConnectionState.class);

    /**
     * The connection handler.
     */
    private final ConnectionHandler _handler;

    /**
     * The channel.
     */
    private final Channel _channel;

    /**
     * Initializes a new instance of the {@link ConnectedConnectionState} class.
     * 
     * @param handler The connection handler.
     * @param channel The channel.
     */
    public ConnectedConnectionState(final ConnectionHandler handler, final Channel channel) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }
        if (channel == null) {
            throw new IllegalArgumentException("Channel must not be null");
        }

        _handler = handler;
        _channel = channel;
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
    public void addParameter(final ConnectionContext context, final String name, final String value) {
        throw new IllegalStateException("Connection is connected");
    }

    @Override
    public void setConnectionData(final ConnectionContext context, final String connectionData) {
        throw new IllegalStateException("Connection is connected");
    }

    @Override
    public Promise<Void> start(final ConnectionContext context, final ConnectionHandler handler) {
        return Promises.newSuccess();
    }

    @Override
    public Promise<Void> stop(final ConnectionContext context) {
        final Deferred<Void> deferred = Promises.newDeferred();
        final DisconnectingConnectionState disconnecting = new DisconnectingConnectionState(deferred);

        if (!context.tryChangeState(this, disconnecting)) {
            return context.getState().stop(context);
        }

        _handler.onDisconnecting();
        final TransportManager manager = context.getTransportManager();

        manager.stop(context);

        logger.info("Closing channel...");

        _channel.close().then(new RunAsync<Void, Void>() {
            @Override
            protected Promise<Void> doRun(final Void value) throws Exception {
                logger.info("Aborting transport...");

                final Transport transport = manager.getTransport();

                return transport.abort(context);
            }
        }).then(new OnComplete<Void>() {
            @Override
            protected void onComplete(final Void value, final Throwable cause) throws Exception {
                final DisconnectedConnectionState disconnected = new DisconnectedConnectionState();

                context.setTransportOptions(null);

                context.changeState(disconnecting, disconnected);
                _handler.onDisconnected();
            }
        }).then(deferred);

        return deferred;
    }

    @Override
    public Promise<Void> reconnect(final ConnectionContext context) {
        final Deferred<Void> deferred = Promises.newDeferred();
        final ReconnectingConnectionState reconnecting = new ReconnectingConnectionState(deferred);

        if (!context.tryChangeState(this, reconnecting)) {
            return context.getState().reconnect(context);
        }

        _handler.onReconnecting();
        final TransportManager manager = context.getTransportManager();

        logger.info("Closing channel...");

        _channel.close().then(new Catch<Void>() {
            @Override
            protected Void doCatch(final Throwable cause) throws Exception {
                return null;
            }
        }).then(new RunAsync<Void, Channel>() {
            @Override
            protected Promise<Channel> doRun(final Void value) throws Exception {
                logger.info("Reconnecting transport...");

                final Transport transport = manager.getTransport();

                return transport.connect(context, manager, true);
            }
        }).then(new Run<Channel, Void>() {
            @Override
            protected Void doRun(final Channel channel) throws Exception {
                final ConnectedConnectionState connected = new ConnectedConnectionState(_handler, channel);

                context.changeState(reconnecting, connected);
                _handler.onReconnected();

                return null;
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                _handler.onError(cause);
                final DisconnectedConnectionState disconnected = new DisconnectedConnectionState();

                context.changeState(reconnecting, disconnected);
                _handler.onDisconnected();
                manager.stop(context);
            }
        }).then(deferred);

        return deferred;
    }

    @Override
    public Promise<Void> send(final ConnectionContext context, final String message) {
        return _channel.send(message);
    }
}
