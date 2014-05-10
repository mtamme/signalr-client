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

import net.signalr.client.json.JsonMapper;
import net.signalr.client.json.JsonElement;
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
        final JsonMapper mapper = _context.getMapper();
        final JsonElement element = mapper.toElement(message);
        final String callbackId = element.get("I").getString(null);

        if (callbackId != null) {
            _context.getListeners().notifyOnReceived(message);
        } else {
            final JsonElement messages = element.get("M");

            for (int i = 0; i < messages.size(); i++) {
                _context.getListeners().notifyOnReceived(messages.get(i).toString());
            }
        }
    }
}
