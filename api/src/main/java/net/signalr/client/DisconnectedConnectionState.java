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

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.NegotiationResponse;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportManager;
import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.ExecuteOn;
import net.signalr.client.util.concurrent.OnFailure;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;
import net.signalr.client.util.concurrent.Apply;
import net.signalr.client.util.concurrent.Compose;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void addParameter(final ConnectionContext context, final String name, final String value) {
        context.addParameter(name, value);
    }

    @Override
    public void setConnectionData(final ConnectionContext context, final String connectionData) {
        context.setConnectionData(connectionData);
    }

    @Override
    public Promise<Void> start(final ConnectionContext context) {
        final Deferred<Void> deferred = Promises.newDeferred();
        final ConnectingConnectionState connecting = new ConnectingConnectionState(deferred);

        if (!context.tryChangeConnectionState(this, connecting)) {
            return context.getConnectionState().start(context);
        }
        final TransportManager manager = context.getTransportManager();
        final Transport transport = manager.getTransport();

        Promises.newPromise(new Runnable() {
            @Override
            public void run() {
                context.getConnectionNotifier().notifyOnConnecting();
                manager.addTransportListener(context.getConnectionNotifier());
                transport.start(context);
            }
        }).then(new Compose<Void, NegotiationResponse>() {
            @Override
            protected Promise<NegotiationResponse> doCompose(final Void value) throws Exception {
                logger.debug("Negotiating transport...");

                return transport.negotiate(context);
            }
        }).then(new Compose<NegotiationResponse, Channel>() {
            @Override
            protected Promise<Channel> doCompose(final NegotiationResponse response) throws Exception {
                final String protocolVersion = response.getProtocolVersion();

                if (!protocolVersion.equals(context.getProtocolVersion())) {
                    throw new IllegalStateException("Server returned unsupported protocol version: '" + protocolVersion + "'");
                }

                context.setTransportOptions(response);

                logger.debug("Connecting transport...");

                return transport.connect(context, manager, false);
            }
        }).then(new ExecuteOn<Channel>(context.getExecutor())).then(new Apply<Channel, Void>() {
            @Override
            protected Void doApply(final Channel channel) throws Exception {
                final ConnectedConnectionState connected = new ConnectedConnectionState(channel);

                context.changeConnectionState(connecting, connected);
                context.getConnectionNotifier().notifyOnConnected();
                manager.start(context);

                return null;
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                context.changeConnectionState(connecting, DisconnectedConnectionState.this);
                context.getConnectionNotifier().notifyOnDisconnected();
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                transport.stop(context);
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                manager.stop(context);
                manager.removeTransportListener(context.getConnectionNotifier());
            }
        }).then(deferred);

        return deferred;
    }

    @Override
    public Promise<Void> stop(final ConnectionContext context) {
        return Promises.newSuccess();
    }

    @Override
    public Promise<Void> reconnect(final ConnectionContext context) {
        throw new IllegalStateException("Connection is disconnected");
    }

    @Override
    public Promise<Void> send(final ConnectionContext context, final String message) {
        throw new IllegalStateException("Connection is disconnected");
    }
}