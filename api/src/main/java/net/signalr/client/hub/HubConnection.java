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

package net.signalr.client.hub;

import net.signalr.client.Connection;
import net.signalr.client.ConnectionListener;
import net.signalr.client.PersistentConnection;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.transport.Transport;
import net.signalr.client.util.concurrent.promise.Compose;
import net.signalr.client.util.concurrent.promise.OnComplete;
import net.signalr.client.util.concurrent.promise.OnFailure;
import net.signalr.client.util.concurrent.promise.Promise;
import net.signalr.client.util.concurrent.promise.Promises;

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
    public void addConnectionListener(final ConnectionListener listener) {
        _connection.addConnectionListener(listener);
    }

    /**
     * Adds the specified connection listener.
     * 
     * @param listener The connection listener.
     */
    public void removeConnectionListener(final ConnectionListener listener) {
        _connection.removeConnectionListener(listener);
    }

    /**
     * Creates a new hub proxy for the specified hub name.
     * 
     * @param hubName The hub name.
     * @return The new hub proxy.
     */
    public HubProxy newHubProxy(final String hubName) {
        return _dispatcher.newHubProxy(hubName);
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
                _connection.addConnectionListener(_dispatcher);
            }
        }).then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                return _connection.start();
            }
        }).then(new OnFailure<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                _connection.removeConnectionListener(_dispatcher);
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
                _connection.removeConnectionListener(_dispatcher);
            }
        });
    }
}
