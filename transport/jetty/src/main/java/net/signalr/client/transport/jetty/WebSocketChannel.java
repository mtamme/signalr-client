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

package net.signalr.client.transport.jetty;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WriteCallback;

import net.signalr.client.transport.Channel;
import net.signalr.client.transport.ChannelHandler;
import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

/**
 * Represents a web socket channel.
 */
final class WebSocketChannel implements Channel {

    /**
     * The channel handler.
     */
    private final ChannelHandler _handler;

    /**
     * The session.
     */
    private final Session _session;

    /**
     * Initializes a new instance of the {@link WebSocketChannel} class.
     * 
     * @param handler The channel handler.
     * @param session The session.
     */
    public WebSocketChannel(final ChannelHandler handler, final Session session) {
        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }
        if (session == null) {
            throw new IllegalArgumentException("Session must not be null");
        }

        _handler = handler;
        _session = session;
    }

    @Override
    public Promise<Void> send(final String message) {
        _handler.handleMessageSending(message);
        final Deferred<Void> deferred = new Deferred<Void>();

        _session.getRemote().sendString(message, new WriteCallback() {
            @Override
            public void writeSuccess() {
                deferred.resolve(null);
            }

            @Override
            public void writeFailed(final Throwable cause) {
                deferred.reject(cause);
            }
        });

        return deferred;
    }

    @Override
    public Promise<Void> close() {
        try {
            _session.close();
        } catch (final Throwable t) {
            return Promises.rejected(t);
        }

        return Promises.resolved();
    }
}
