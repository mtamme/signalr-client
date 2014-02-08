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

import net.signalr.client.transports.TransportChannelHandler;

/**
 * Represents a connection handler adapter.
 */
final class ConnectionHandlerAdapter implements TransportChannelHandler {

    private final ConnectionHandler _handler;

    public ConnectionHandlerAdapter(final ConnectionHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }

        _handler = handler;
    }

    @Override
    public void onOpen() {
        // _handler.onConnected();
    }

    @Override
    public void onClose() {
        // _handler.onDisconnected();
    }

    @Override
    public void onSending(final String message) {
        _handler.onSending(message);
    }

    @Override
    public void onReceived(final String message) {
        _handler.onReceived(message);
    }

    @Override
    public void onError(final Throwable throwable) {
        _handler.onError(throwable);
    }
}
