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

import net.signalr.client.util.concurrent.Promise;

/**
 * Represents the reconnecting connection state.
 */
final class ReconnectingConnectionState implements ConnectionState {

    /**
     * The reconnect result.
     */
    private final Promise<Void> _promise;

    /**
     * Initializes a new instance of the {@link ReconnectingConnectionState} class.
     * 
     * @param promise The reconnect result.
     */
    public ReconnectingConnectionState(final Promise<Void> promise) {
        if (promise == null) {
            throw new IllegalArgumentException("Promise must not be null");
        }

        _promise = promise;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void addHeader(final ConnectionContext context, final String name, final String value) {
        throw new IllegalStateException("Connection is reconnecting");
    }

    @Override
    public void addParameter(final ConnectionContext context, final String name, final String value) {
        throw new IllegalStateException("Connection is reconnecting");
    }

    @Override
    public void setConnectionData(final ConnectionContext context, final String connectionData) {
        throw new IllegalStateException("Connection is reconnecting");
    }

    @Override
    public void notifyConnectionListener(final ConnectionListener listener) {
        listener.onReconnecting();
    }

    @Override
    public Promise<Void> start(final ConnectionContext context) {
        throw new IllegalStateException("Connection is reconnecting");
    }

    @Override
    public Promise<Void> stop(final ConnectionContext context) {
        throw new IllegalStateException("Connection is reconnecting");
    }

    @Override
    public Promise<Void> reconnect(final ConnectionContext context) {
        return _promise;
    }

    @Override
    public Promise<Void> send(final ConnectionContext context, final String message) {
        // TODO Queue messages and return promise immediately.
        throw new IllegalStateException("Connection is reconnecting");
    }
}
