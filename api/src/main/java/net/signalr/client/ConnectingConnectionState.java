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

import net.signalr.client.util.concurrent.promise.Promise;

/**
 * Represents the connecting connection state.
 */
final class ConnectingConnectionState implements ConnectionState {

    /**
     * The start result.
     */
    private final Promise<Void> _promise;

    /**
     * Initializes a new instance of the {@link ConnectingConnectionState} class.
     * 
     * @param promise The start result.
     */
    public ConnectingConnectionState(final Promise<Void> promise) {
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
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public void addParameter(final ConnectionContext context, final String name, final String value) {
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public void setConnectionData(final ConnectionContext context, final String connectionData) {
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public void notifyConnectionListener(final ConnectionListener listener) {
        listener.onConnecting();
    }

    @Override
    public Promise<Void> connect(final ConnectionContext context) {
        return _promise;
    }

    @Override
    public Promise<Void> reconnect(final ConnectionContext context) {
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public Promise<Void> disconnect(final ConnectionContext context) {
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public Promise<Void> send(final ConnectionContext context, final String message) {
        throw new IllegalStateException("Connection is connecting");
    }
}
