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

package net.signalr.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.transport.Transport;
import net.signalr.client.transport.Channel;
import net.signalr.client.transport.TransportManager;
import net.signalr.client.util.concurrent.Accept;
import net.signalr.client.util.concurrent.Catch;
import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.ExecuteOn;
import net.signalr.client.util.concurrent.OnComplete;
import net.signalr.client.util.concurrent.OnFailure;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;
import net.signalr.client.util.concurrent.Compose;

/**
 * Represents the connected connection state.
 */
final class ConnectedConnectionState implements ConnectionState {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ConnectedConnectionState.class);

    /**
     * The channel.
     */
    private final Channel _channel;

    /**
     * A value indicating whether a reconnect occurred.
     */
    private final boolean _reconnect;

    /**
     * Initializes a new instance of the {@link ConnectedConnectionState} class.
     * 
     * @param channel The channel.
     * @param reconnect A value indicating whether a reconnect occurred.
     */
    public ConnectedConnectionState(final Channel channel, final boolean reconnect) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel must not be null");
        }

        _channel = channel;
        _reconnect = reconnect;
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
    public void notifyConnectionListener(final ConnectionListener listener) {
        if (_reconnect) {
            listener.onReconnected();
        } else {
            listener.onConnected();
        }
    }

    @Override
    public Promise<Void> connect(final ConnectionContext context) {
        return Promises.newSuccess();
    }

    @Override
    public Promise<Void> reconnect(final ConnectionContext context) {
        final Deferred<Void> deferred = Promises.newDeferred();
        final ReconnectingConnectionState reconnecting = new ReconnectingConnectionState(deferred);

        if (!context.tryChangeConnectionState(this, reconnecting)) {
            return context.getConnectionState().reconnect(context);
        }
        final TransportManager manager = context.getTransportManager();
        final Transport transport = manager.getTransport();

        _channel.close().then(new Catch<Void>() {
            @Override
            protected Void doCatch(final Throwable cause) throws Exception {
                context.handleError(cause);

                return null;
            }
        }).then(new Compose<Void, Channel>() {
            @Override
            protected Promise<Channel> doCompose(final Void value) throws Exception {
                logger.debug("Reconnecting transport...");

                return transport.connect(context, manager, true);
            }
        }).then(new ExecuteOn<Channel>(context.getExecutor())).then(new Accept<Channel>() {
            @Override
            protected void doAccept(final Channel channel) throws Exception {
                final ConnectedConnectionState connected = new ConnectedConnectionState(channel, true);

                context.changeConnectionState(reconnecting, connected);
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                manager.stop(context);
                manager.removeTransportListener(context);
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                transport.stop(context);
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                final DisconnectedConnectionState disconnected = new DisconnectedConnectionState(cause);

                context.changeConnectionState(reconnecting, disconnected);
            }
        }).then(deferred);

        return deferred;
    }

    @Override
    public Promise<Void> disconnect(final ConnectionContext context) {
        final Deferred<Void> deferred = Promises.newDeferred();
        final DisconnectingConnectionState disconnecting = new DisconnectingConnectionState(deferred);

        if (!context.tryChangeConnectionState(this, disconnecting)) {
            return context.getConnectionState().disconnect(context);
        }
        final TransportManager manager = context.getTransportManager();
        final Transport transport = manager.getTransport();

        Promises.newPromise(new Runnable() {
            @Override
            public void run() {
                manager.stop(context);
                manager.removeTransportListener(context);
            }
        }).then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                logger.debug("Closing channel...");

                return _channel.close();
            }
        }).then(new Catch<Void>() {
            @Override
            protected Void doCatch(final Throwable cause) throws Exception {
                context.handleError(cause);
                return null;
            }
        }).then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                logger.debug("Aborting transport...");

                return transport.abort(context);
            }
        }).then(new ExecuteOn<Void>(context.getExecutor())).then(new OnComplete<Void>() {
            @Override
            protected void onComplete(final Void value, final Throwable cause) throws Exception {
                transport.stop(context);
            }
        }).then(new OnComplete<Void>() {
            @Override
            protected void onComplete(final Void value, final Throwable cause) throws Exception {
                final DisconnectedConnectionState disconnected = new DisconnectedConnectionState(cause);

                context.setTransportOptions(null);
                context.changeConnectionState(disconnecting, disconnected);
            }
        }).then(deferred);

        return deferred;
    }

    @Override
    public Promise<Void> send(final ConnectionContext context, final String message) {
        return _channel.send(message);
    }
}
