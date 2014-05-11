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

import net.signalr.client.Connection;
import net.signalr.client.ConnectionListener;
import net.signalr.client.PersistentConnection;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.transport.Transport;
import net.signalr.client.util.concurrent.Compose;
import net.signalr.client.util.concurrent.OnComplete;
import net.signalr.client.util.concurrent.OnFailure;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

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
     * Initializes a new instance of the {@link HubConnection} class.
     * 
     * @param url The URL.
     * @param transport The transport.
     * @param factory The factory.
     */
    public HubConnection(final String url, final Transport transport, final JsonFactory factory) {
        this(new PersistentConnection(url, transport, factory));
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
    }

    /**
     * Adds the specified header name and value.
     * 
     * @param name The header name.
     * @param value The header value.
     */
    public void addHeader(final String name, final String value) {
        _connection.addHeader(name, value);
    }

    /**
     * Adds the specified parameter name and value.
     * 
     * @param name The parameter name.
     * @param value The parameter value.
     */
    public void addParameter(final String name, final String value) {
        _connection.addParameter(name, value);
    }

    /**
     * Adds the specified connection listener.
     * 
     * @param listener The connection listener.
     */
    public void addListener(final ConnectionListener listener) {
        _connection.addListener(listener);
    }

    /**
     * Adds the specified connection listener.
     * 
     * @param listener The connection listener.
     */
    public void removeListener(final ConnectionListener listener) {
        _connection.removeListener(listener);
    }

    /**
     * Returns a hub proxy for the specified hub name.
     * 
     * @param hubName The hub name.
     * @return The hub proxy.
     */
    public HubProxy getProxy(final String hubName) {
        return _dispatcher.getProxy(hubName);
    }

    /**
     * Starts the connection.
     * 
     * @return The start result.
     */
    public Promise<Void> start() {
        return Promises.newPromise(new Runnable() {
            @Override
            public void run() {
                _connection.addListener(_dispatcher);
            }
        }).then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                return _connection.start();
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                _connection.removeListener(_dispatcher);
            }
        });
    }

    /**
     * Stops the connection.
     * 
     * @return The stop result.
     */
    public Promise<Void> stop() {
        return _connection.stop().then(new OnComplete<Void>() {
            @Override
            protected void onComplete(final Void value, final Throwable cause) throws Exception {
                _connection.removeListener(_dispatcher);
            }
        });
    }
}
