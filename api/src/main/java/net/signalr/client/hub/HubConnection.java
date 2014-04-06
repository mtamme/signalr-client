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
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.transport.Transport;
import net.signalr.client.util.concurrent.Promise;

/**
 * Represents a hub connection.
 */
public final class HubConnection {

    /**
     * The hub dispatcher.
     */
    private final HubDispatcher _dispatcher;

    /**
     * The underlying connection.
     */
    private final Connection _connection;

    /**
     * The hub proxies.
     */
    private final Map<String, HubProxy> _hubProxies;

    /**
     * Initializes a new instance of the {@link HubConnection} class.
     * 
     * @param url The URL.
     * @param transport The transport.
     * @param serializer The serializer.
     */
    public HubConnection(final String url, final Transport transport, JsonSerializer serializer) {
        this(new PersistentConnection(url, transport, serializer));
    }

    /**
     * Initializes a new instance of the {@link HubConnection} class.
     * 
     * @param connection The underlying connection.
     */
    public HubConnection(final Connection connection) {
        this(new DefaultHubDispatcher(connection), connection);
    }

    /**
     * Initializes a new instance of the {@link HubConnection} class.
     * 
     * @param dispatcher The hub dispatcher.
     * @param connection The underlying connection.
     */
    HubConnection(final HubDispatcher dispatcher, final Connection connection) {
        if (dispatcher == null) {
            throw new IllegalArgumentException("Dispatcher must not be null");
        }
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }

        _dispatcher = dispatcher;
        _connection = connection;

        _hubProxies = new HashMap<String, HubProxy>();
    }

    /**
     * Updates the connection data.
     * 
     * @param newHubName The new hub name.
     */
    private void updateConnectionData(final String newHubName) {
        final HubNames hubNames = new HubNames();

        hubNames.addAll(_hubProxies.keySet());
        hubNames.add(newHubName);
        final JsonSerializer serializer = _connection.getSerializer();
        final String connectionData = serializer.toJson(hubNames);

        _connection.setConnectionData(connectionData);
    }

    /**
     * Adds a header.
     * 
     * @param name The header name.
     * @param value The header value.
     */
    public final void addHeader(final String name, final String value) {
        _connection.addHeader(name, value);
    }

    /**
     * Adds a query parameter.
     * 
     * @param name The query parameter name.
     * @param value The query parameter value.
     */
    public final void addQueryParameter(final String name, final String value) {
        _connection.addQueryParameter(name, value);
    }

    /**
     * Returns a hub proxy for the specified hub name.
     * 
     * @param hubName The hub name.
     * @return The hub proxy.
     */
    public HubProxy getProxy(final String hubName) {
        if (hubName == null) {
            throw new IllegalArgumentException("Hub name must not be null");
        }

        final String lowerCaseHubName = hubName.toLowerCase();
        HubProxy hubProxy = _hubProxies.get(lowerCaseHubName);

        if (hubProxy == null) {
            // Update the connection data before adding the new hub proxy
            // since it could fail when the underlying connection is not disconnected.
            updateConnectionData(hubName);
            hubProxy = new DefaultHubProxy(hubName, _dispatcher);
            _hubProxies.put(lowerCaseHubName, hubProxy);
        }

        return hubProxy;
    }

    /**
     * Starts the connection.
     * 
     * @param handler The connection handler.
     * @return The start result.
     */
    public final Promise<Void> start(final ConnectionHandler handler) {
        return _connection.start(handler);
    }

    /**
     * Stops the connection.
     * 
     * @return The stop result.
     */
    public final Promise<Void> stop() {
        return _connection.stop();
    }
}
