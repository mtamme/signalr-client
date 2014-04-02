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

import net.signalr.client.concurrent.Promise;

/**
 * Represents the connecting connection state.
 */
final class ConnectingConnectionState implements ConnectionState {

    private final Promise<Void> _promise;

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
    public void addQueryParameter(final ConnectionContext context, final String name, final String value) {
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public void setConnectionData(final ConnectionContext context, final String connectionData) {
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public Promise<Void> start(final ConnectionContext context, final ConnectionHandler handler) {
        return _promise;
    }

    @Override
    public Promise<Void> stop(final ConnectionContext context) {
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public Promise<Void> send(final ConnectionContext context, final String message) {
        throw new IllegalStateException("Connection is connecting");
    }

    @Override
    public String toString() {
        return "CONNECTING";
    }
}
