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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.signalr.client.Connection;
import net.signalr.client.ConnectionHandler;
import net.signalr.client.PersistentConnection;
import net.signalr.client.concurrent.Function;
import net.signalr.client.concurrent.Promise;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.transport.Transport;

/**
 * Represents a hub connection.
 */
public final class HubConnection {

    protected final Connection _connection;

    private final Map<String, HubProxy> _hubProxies;

    public HubConnection(final String url, final Transport transport, JsonSerializer serializer) {
        this(new PersistentConnection(url, transport, serializer));
    }

    HubConnection(final Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }

        _connection = connection;
        _hubProxies = new HashMap<String, HubProxy>();
    }

    private void updateConnectionData(final Iterable<String> names, String newName) {
        final List<HubName> hubNames = new ArrayList<HubName>();

        for (final String name : names) {
            hubNames.add(new HubName(name));
        }

        hubNames.add(new HubName(newName));
        final JsonSerializer serializer = _connection.getSerializer();
        final String connectionData = serializer.serialize(hubNames);

        _connection.setConnectionData(connectionData);
    }

    public final void addHeader(final String name, final String value) {
        _connection.addHeader(name, value);
    }

    public final void addQueryParameter(final String name, final String value) {
        _connection.addQueryParameter(name, value);
    }

    public HubProxy createHubProxy(final String hubName) {
        final String lowerCaseHubName = hubName.toLowerCase();
        HubProxy hubProxy = _hubProxies.get(lowerCaseHubName);

        if (hubProxy == null) {
            updateConnectionData(_hubProxies.keySet(), hubName);
            hubProxy = new HubProxyImpl(_connection, hubName);
            _hubProxies.put(lowerCaseHubName, hubProxy);
        }

        return hubProxy;
    }

    public String registerCallback(final Function<String, Void> callback) {
        return null;
    }

    public void removeCallback(final String callbackId) {
    }

    public final Promise<Void> start(final ConnectionHandler handler) {
        return _connection.start(handler);
    }

    public final Promise<Void> stop() {
        return _connection.stop();
    }
}
