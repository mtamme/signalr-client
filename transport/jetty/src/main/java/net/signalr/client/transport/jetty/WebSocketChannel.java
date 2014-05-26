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
                deferred.setSuccess(null);
            }

            @Override
            public void writeFailed(final Throwable cause) {
                deferred.setFailure(cause);
            }
        });

        return deferred;
    }

    @Override
    public Promise<Void> close() {
        try {
            _session.close();
        } catch (final Throwable t) {
            return Promises.newFailure(t);
        }

        return Promises.newSuccess();
    }
}
