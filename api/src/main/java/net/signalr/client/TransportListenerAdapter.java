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
     * The connection handler.
     */
    private final ConnectionHandler _handler;

    /**
     * Initializes a new instance of the {@link TransportListenerAdapter} class.
     * 
     * @param context The connection context.
     * @param handler The connection handler.
     */
    public TransportListenerAdapter(final ConnectionContext context, final ConnectionHandler handler) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }

        _context = context;
        _handler = handler;
    }

    @Override
    public void onChannelOpened() {
    }

    @Override
    public void onChannelClosed() {
    }

    @Override
    public void onConnectionSlow() {
        _handler.onConnectionSlow();
    }

    @Override
    public void onConnectionLost() {
        final ConnectionState state = _context.getState();

        if (!state.isConnected()) {
            return;
        }
    }

    @Override
    public void onError(final Throwable cause) {
        _handler.onError(cause);
    }

    @Override
    public void onSending(final String message) {
        _handler.onSending(message);
    }

    @Override
    public void onReceived(final String message) {
        final JsonMapper mapper = _context.getMapper();
        final JsonElement element = mapper.toElement(message);
        final String callbackId = element.get("I").getString(null);

        if (callbackId != null) {
            _handler.onReceived(message);
        } else {
            final JsonElement messages = element.get("M");

            for (int i = 0; i < messages.size(); i++) {
                _handler.onReceived(messages.get(i).toString());
            }
        }
    }
}
