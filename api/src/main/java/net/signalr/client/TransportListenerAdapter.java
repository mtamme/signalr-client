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

import net.signalr.client.transport.TransportListener;

/**
 * Represents a transport listener adapter.
 */
final class TransportListenerAdapter implements TransportListener {

    /**
     * The connection context.
     */
    private final ConnectionContext _context;

    /**
     * Initializes a new instance of the {@link TransportListenerAdapter} class.
     * 
     * @param context The connection context.
     */
    public TransportListenerAdapter(final ConnectionContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        _context = context;
    }

    @Override
    public void onChannelOpened() {
    }

    @Override
    public void onChannelClosed() {
    }

    @Override
    public void onConnectionSlow() {
        _context.getListeners().notifyOnConnectionSlow();
    }

    @Override
    public void onConnectionLost() {
        _context.getState().reconnect(_context);
    }

    @Override
    public void onError(final Throwable cause) {
        _context.getListeners().notifyOnError(cause);
    }

    @Override
    public void onSending(final String message) {
        _context.getListeners().notifyOnSending(message);
    }

    @Override
    public void onReceived(final String message) {
        _context.getListeners().notifyOnReceived(message);
    }
}
