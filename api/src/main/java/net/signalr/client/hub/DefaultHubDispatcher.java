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

package net.signalr.client.hub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.Connection;
import net.signalr.client.concurrent.Deferred;
import net.signalr.client.concurrent.Function;
import net.signalr.client.concurrent.OnRejected;
import net.signalr.client.concurrent.Promise;
import net.signalr.client.json.JsonSerializer;

/**
 * Represents the default hub dispatcher.
 */
final class DefaultHubDispatcher implements HubDispatcher {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultHubDispatcher.class);

    /**
     * The connection.
     */
    private final Connection _connection;

    /**
     * The next callback ID.
     */
    private final AtomicLong _nextCallbackId;

    /**
     * The deferred responses.
     */
    private final Map<String, Deferred<HubResponse>> _responses;

    /**
     * Initializes a new instance of the {@link DefaultHubDispatcher} class.
     * 
     * @param connection The connection.
     */
    public DefaultHubDispatcher(final Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }

        _connection = connection;

        _nextCallbackId = new AtomicLong(0);
        _responses = new ConcurrentHashMap<String, Deferred<HubResponse>>();
    }

    /**
     * Returns the next callback ID.
     * 
     * @return The next callback ID.
     */
    private String nextCallbackId() {
        final long callbackId = _nextCallbackId.incrementAndGet();

        return String.valueOf(callbackId);
    }

    @Override
    public void onReceived(final String message) {
        final JsonSerializer serializer = _connection.getSerializer();
        final HubResponse response = serializer.deserialize(message, HubResponse.class);
        final String callbackId = response.getCallbackId();
        final Deferred<HubResponse> deferred = _responses.remove(callbackId);

        if (deferred == null) {
            logger.warn("Received response for unknown callback ID {}", callbackId);
            return;
        }

        deferred.resolve(response);
    }

    @Override
    public Promise<HubResponse> invoke(final HubRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }

        final String callbackId = nextCallbackId();

        request.setCallbackId(callbackId);
        final JsonSerializer serializer = _connection.getSerializer();
        final String message = serializer.serialize(request);
        final Deferred<HubResponse> deferred = new Deferred<HubResponse>();

        _responses.put(callbackId, deferred);

        return _connection.send(message).thenCall(new OnRejected<Void>() {
            @Override
            public void onRejected(final Throwable throwable) {
                _responses.remove(callbackId);
            }
        }).thenCompose(new Function<Void, Promise<HubResponse>>() {
            @Override
            public Promise<HubResponse> apply(final Void value) throws Exception {
                return deferred;
            }
        });
    }
}
