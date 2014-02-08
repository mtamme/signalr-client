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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.concurrent.Scheduler;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.transports.Transport;

/**
 * Represents a persistent connection context.
 */
final class PersistentConnectionContext implements ConnectionContext {

    /**
     * The private logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistentConnectionContext.class);

    private static final String PROTOCOL_VERSION = "1.3";

    private final String _url;

    private final Transport _transport;

    private final Scheduler _scheduler;

    private final JsonSerializer _serializer;

    private final AtomicReference<ConnectionState> _state;

    private final Map<String, Collection<String>> _headers;

    private final Map<String, Collection<String>> _queryParameters;

    private boolean _tryWebSockets;

    private String _connectionData;

    private String _connectionId;

    private String _connectionToken;

    private long _disconnectTimeout;

    private long _keepAliveTimeout;

    protected PersistentConnectionContext(final String url, final Transport transport, final Scheduler scheduler, final JsonSerializer serializer) {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        if (transport == null) {
            throw new IllegalArgumentException("Transport must not be null");
        }
        if (scheduler == null) {
            throw new IllegalArgumentException("Scheduler must not be null");
        }
        if (serializer == null) {
            throw new IllegalArgumentException("Serializer must not be null");
        }

        _url = url;
        _transport = transport;
        _scheduler = scheduler;
        _serializer = serializer;
        final ConnectionState initialState = new DisconnectedConnectionState();

        _state = new AtomicReference<ConnectionState>(initialState);
        _headers = new HashMap<String, Collection<String>>();
        _queryParameters = new HashMap<String, Collection<String>>();

        _tryWebSockets = false;
        _connectionData = null;
        _connectionId = null;
        _disconnectTimeout = -1;
        _keepAliveTimeout = -1;
    }

    @Override
    public String getProtocolVersion() {
        return PROTOCOL_VERSION;
    }

    @Override
    public String getUrl() {
        return _url;
    }

    @Override
    public Scheduler getScheduler() {
        return _scheduler;
    }

    @Override
    public JsonSerializer getSerializer() {
        return _serializer;
    }

    @Override
    public boolean getTryWebSockets() {
        return _tryWebSockets;
    }

    @Override
    public String getConnectionData() {
        return _connectionData;
    }

    @Override
    public String getConnectionToken() {
        return _connectionToken;
    }

    @Override
    public long getKeepAliveTimeout() {
        return _keepAliveTimeout;
    }

    @Override
    public Map<String, Collection<String>> getHeaders() {
        return _headers;
    }

    @Override
    public Map<String, Collection<String>> getQueryParameters() {
        return _queryParameters;
    }

    @Override
    public Transport getTransport() {
        return _transport;
    }

    @Override
    public String getConnectionId() {
        return _connectionId;
    }

    @Override
    public void setConnectionId(final String connectionId) {
        _connectionId = connectionId;
    }

    @Override
    public void setTryWebSockets(boolean tryWebSockets) {
        _tryWebSockets = tryWebSockets;
    }

    @Override
    public void setConnectionData(final String connectionData) {
        _connectionData = connectionData;
    }

    @Override
    public void setConnectionToken(final String connectionToken) {
        _connectionToken = connectionToken;
    }

    @Override
    public double getDisconnectTimeout() {
        return _disconnectTimeout;
    }

    @Override
    public void setDisconnectTimeout(final long disconnectTimeout) {
        _disconnectTimeout = disconnectTimeout;
    }

    @Override
    public void setKeepAliveTimeout(final long keepAliveTimeout) {
        _keepAliveTimeout = keepAliveTimeout;
    }

    @Override
    public void addHeader(final String name, final String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }

        Collection<String> values = _headers.get(name);

        if (values == null) {
            values = new ArrayList<String>();
            _headers.put(name, values);
        }

        values.add(value);
    }

    @Override
    public void addQueryParameter(final String name, final String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }

        Collection<String> values = _queryParameters.get(name);

        if (values == null) {
            values = new ArrayList<String>();
            _queryParameters.put(name, values);
        }

        values.add(value);
    }

    @Override
    public ConnectionState getState() {
        return _state.get();
    }

    @Override
    public void changeState(final ConnectionState oldState, final ConnectionState newState) {
        if (!tryChangeState(oldState, newState)) {
            throw new IllegalStateException("Failed to change connection state");
        }
    }

    @Override
    public boolean tryChangeState(final ConnectionState oldState, final ConnectionState newState) {
        if (!_state.compareAndSet(oldState, newState)) {
            LOGGER.warn("Failed to change connection state from '{}' to '{}'", oldState, newState);

            return false;
        }

        LOGGER.info("Leaving connection state '{}'", oldState);

        oldState.onLeaveState();

        LOGGER.info("Entering connection state '{}'", newState);

        newState.onEnterState();

        return true;
    }
}
