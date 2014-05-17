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

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Represents the default connection manager.
 */
final class DefaultConnectionManager implements ConnectionManager {

    /**
     * The connection context.
     */
    private final ConnectionContext _context;

    /**
     * The connection listeners.
     */
    private final CopyOnWriteArraySet<ConnectionListener> _listeners;

    /**
     * Initializes a new instance of the {@link DefaultConnectionManager} class.
     * 
     * @param context The connection context.
     */
    public DefaultConnectionManager(final ConnectionContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        _context = context;

        _listeners = new CopyOnWriteArraySet<ConnectionListener>();
    }

    @Override
    public void addConnectionListener(final ConnectionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.add(listener);
    }

    @Override
    public void removeConnectionListener(final ConnectionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.add(listener);
    }

    @Override
    public void notifyOnConnecting() {
        for (final ConnectionListener listener : _listeners) {
            listener.onConnecting();
        }
    }

    @Override
    public void notifyOnConnected() {
        for (final ConnectionListener listener : _listeners) {
            listener.onConnected();
        }
    }

    @Override
    public void notifyOnReconnecting() {
        for (final ConnectionListener listener : _listeners) {
            listener.onReconnecting();
        }
    }

    @Override
    public void notifyOnReconnected() {
        for (final ConnectionListener listener : _listeners) {
            listener.onReconnected();
        }
    }

    @Override
    public void notifyOnDisconnecting() {
        for (final ConnectionListener listener : _listeners) {
            listener.onDisconnecting();
        }
    }

    @Override
    public void notifyOnDisconnected() {
        for (final ConnectionListener listener : _listeners) {
            listener.onDisconnected();
        }
    }

    @Override
    public void notifyOnError(final Throwable cause) {
        for (final ConnectionListener listener : _listeners) {
            listener.onError(cause);
        }
    }

    @Override
    public void onChannelOpened() {
    }

    @Override
    public void onChannelClosed() {
    }

    @Override
    public void onConnectionSlow() {
        for (final ConnectionListener listener : _listeners) {
            listener.onConnectionSlow();
        }
    }

    @Override
    public void onConnectionLost() {
        _context.getConnectionState().reconnect(_context);
    }

    @Override
    public void onError(final Throwable cause) {
        notifyOnError(cause);
    }

    @Override
    public void onSending(final String message) {
        for (final ConnectionListener listener : _listeners) {
            listener.onSending(message);
        }
    }

    @Override
    public void onReceived(final String message) {
        for (final ConnectionListener listener : _listeners) {
            listener.onReceived(message);
        }
    }
}
