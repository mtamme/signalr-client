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

import java.util.HashMap;
import java.util.Map;

import net.signalr.client.Connection;
import net.signalr.client.ConnectionHandler;
import net.signalr.client.PersistentConnection;
import net.signalr.client.concurrent.Promise;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.transport.Transport;

/**
 * Represents a hub connection.
 */
public final class HubConnection {

    private final HubDispatcher _dispatcher;

    private final Connection _connection;

    private final HubNames _hubNames;

    private final Map<String, HubProxy> _hubProxies;

    public HubConnection(final String url, final Transport transport, JsonSerializer serializer) {
        this(new PersistentConnection(url, transport, serializer));
    }

    public HubConnection(final Connection connection) {
        this(new DefaultHubDispatcher(connection), connection);
    }

    HubConnection(final HubDispatcher dispatcher, final Connection connection) {
        if (dispatcher == null) {
            throw new IllegalArgumentException("Dispatcher must not be null");
        }
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }

        _dispatcher = dispatcher;
        _connection = connection;

        _hubNames = new HubNames();
        _hubProxies = new HashMap<String, HubProxy>();
    }

    private void updateConnectionData(String newHubName) {
        _hubNames.add(newHubName);
        final JsonSerializer serializer = _connection.getSerializer();
        final String connectionData = serializer.toJson(_hubNames);

        _connection.setConnectionData(connectionData);
    }

    public final void addHeader(final String name, final String value) {
        _connection.addHeader(name, value);
    }

    public final void addQueryParameter(final String name, final String value) {
        _connection.addQueryParameter(name, value);
    }

    public HubProxy getProxy(final String hubName) {
        final String lowerCaseHubName = hubName.toLowerCase();
        HubProxy hubProxy = _hubProxies.get(lowerCaseHubName);

        if (hubProxy == null) {
            hubProxy = new DefaultHubProxy(hubName, _dispatcher);
            _hubProxies.put(lowerCaseHubName, hubProxy);
            updateConnectionData(hubName);
        }

        return hubProxy;
    }

    public final Promise<Void> start(final ConnectionHandler handler) {
        return _connection.start(handler);
    }

    public final Promise<Void> stop() {
        return _connection.stop();
    }
}
