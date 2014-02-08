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

import net.signalr.client.Connection;
import net.signalr.client.concurrent.Deferred;
import net.signalr.client.concurrent.Promise;
import net.signalr.client.json.JsonSerializer;

/**
 * Represents the default hub proxy.
 */
final class DefaultHubProxy implements HubProxy {

    private final Connection _connection;

    private final String _hubName;

    private final AtomicLong _callbackId;

    private final Map<Long, Promise<?>> _promises;

    public DefaultHubProxy(final Connection connection, final String hubName) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }
        if (hubName == null) {
            throw new IllegalArgumentException("Hub name must not be null");
        }

        _connection = connection;
        _hubName = hubName;

        _callbackId = new AtomicLong(0);
        _promises = new ConcurrentHashMap<Long, Promise<?>>();
    }

    @Override
    public <T> Promise<T> invoke(final String method, final Object... args) {
        final long callbackId = _callbackId.incrementAndGet();
        final Deferred<T> deferred = new Deferred<T>();

        _promises.put(callbackId, deferred);
        final HubRequest request = new HubRequest();

        request.setHubName(_hubName);
        request.setMethodName(method);
        request.setCallbackId(Long.toString(callbackId));
        request.setArguments(args);
        final JsonSerializer serializer = _connection.getSerializer();
        final String message = serializer.serialize(request);

        _connection.send(message);

        return deferred;
    }

    @Override
    public void subscribe(final String eventName, final HubListener listener) {
    }
}
