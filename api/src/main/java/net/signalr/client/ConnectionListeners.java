/*
 * Copyright 2014 Martin Tamme
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
