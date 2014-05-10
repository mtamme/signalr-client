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

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a connection listener collection.
 */
final class ConnectionListeners {

    /**
     * The connection listeners.
     */
    private final CopyOnWriteArrayList<ConnectionListener> _listeners;

    /**
     * Initializes a new instance of the {@link ConnectionListeners} class.
     */
    public ConnectionListeners() {
        _listeners = new CopyOnWriteArrayList<ConnectionListener>();
    }

    /**
     * Adds the specified connection listener.
     * 
     * @param listener The connection listener.
     */
    public void addListener(final ConnectionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.add(listener);
    }

    /**
     * Removes the specified connection listener.
     * 
     * @param listener The connection listener.
     */
    public void removeListener(final ConnectionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.add(listener);
    }

    public void notifyOnConnecting() {
        for (final ConnectionListener listener : _listeners) {
            listener.onConnecting();
        }
    }

    public void notifyOnConnected() {
        for (final ConnectionListener listener : _listeners) {
            listener.onConnected();
        }
    }

    public void notifyOnReconnecting() {
        for (final ConnectionListener listener : _listeners) {
            listener.onReconnecting();
        }
    }

    public void notifyOnReconnected() {
        for (final ConnectionListener listener : _listeners) {
            listener.onReconnected();
        }
    }

    public void notifyOnDisconnecting() {
        for (final ConnectionListener listener : _listeners) {
            listener.onDisconnecting();
        }
    }

    public void notifyOnDisconnected() {
        for (final ConnectionListener listener : _listeners) {
            listener.onDisconnected();
        }
    }

    public void notifyOnConnectionSlow() {
        for (final ConnectionListener listener : _listeners) {
            listener.onConnectionSlow();
        }
    }

    public void notifyOnError(final Throwable cause) {
        for (final ConnectionListener listener : _listeners) {
            listener.onError(cause);
        }
    }

    public void notifyOnSending(final String message) {
        for (final ConnectionListener listener : _listeners) {
            listener.onSending(message);
        }
    }

    public void notifyOnReceived(final String message) {
        for (final ConnectionListener listener : _listeners) {
            listener.onReceived(message);
        }
    }
}
