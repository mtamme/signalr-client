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

package net.signalr.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.TransportManager;
import net.signalr.client.transport.TransportOptions;
import net.signalr.client.util.concurrent.Scheduler;

/**
 * Represents a persistent connection context.
 */
final class PersistentConnectionContext implements ConnectionContext {

    /**
     * The protocol version.
     */
    private static final String PROTOCOL_VERSION = "1.3";

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(PersistentConnectionContext.class);

    /**
     * The connection URL.
     */
    private final String _url;

    /**
     * The transport manager.
     */
    private final TransportManager _manager;

    /**
     * The executor.
     */
    private final Executor _executor;

    /**
     * The scheduler.
     */
    private final Scheduler _scheduler;

    /**
     * The mapper.
     */
    private final JsonMapper _mapper;

    /**
     * The current connection state.
     */
    private final AtomicReference<ConnectionState> _connectionState;

    /**
     * The headers.
     */
    private final Map<String, Collection<String>> _headers;

    /**
     * The parameters.
     */
    private final Map<String, Collection<String>> _parameters;

    /**
     * The connection listeners.
     */
    private final CopyOnWriteArraySet<ConnectionListener> _listeners;

    /**
     * The connection data.
     */
    private String _connectionData;

    /**
     * The transport options.
     */
    private TransportOptions _transportOptions;

    /**
     * Initializes a new instance of the {@link PersistentConnectionContext} class.
     * 
     * @param url The connection URL.
     * @param manager The transport manager.
     * @param executor The executor.
     * @param scheduler The scheduler.
     * @param mapper The mapper.
     */
    protected PersistentConnectionContext(final String url, final TransportManager manager, final Executor executor, final Scheduler scheduler, final JsonMapper mapper) {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        if (manager == null) {
            throw new IllegalArgumentException("Manager must not be null");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Executor must not be null");
        }
        if (scheduler == null) {
            throw new IllegalArgumentException("Scheduler must not be null");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper must not be null");
        }

        _url = url;
        _manager = manager;
        _executor = executor;
        _scheduler = scheduler;
        _mapper = mapper;

        final ConnectionState initialState = new DisconnectedConnectionState(null);

        _connectionState = new AtomicReference<ConnectionState>(initialState);
        _headers = new HashMap<String, Collection<String>>();
        _parameters = new HashMap<String, Collection<String>>();
        _listeners = new CopyOnWriteArraySet<ConnectionListener>();

        _connectionData = null;
        _transportOptions = null;
    }

    @Override
    public TransportManager getTransportManager() {
        return _manager;
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
    public void addParameter(final String name, final String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }

        Collection<String> values = _parameters.get(name);

        if (values == null) {
            values = new ArrayList<String>();
            _parameters.put(name, values);
        }

        values.add(value);
    }

    @Override
    public void addConnectionListener(final ConnectionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.add(listener);
    }

    @Override
    public void removeConnectionListener(final ConnectionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.add(listener);
    }

    @Override
    public void setConnectionData(final String connectionData) {
        _connectionData = connectionData;
    }

    @Override
    public void setTransportOptions(final TransportOptions transportOptions) {
        _transportOptions = transportOptions;
    }

    @Override
    public void handleError(final Throwable cause) {
        for (final ConnectionListener listener : _listeners) {
            listener.onError(cause);
        }
    }

    @Override
    public ConnectionState getConnectionState() {
        return _connectionState.get();
    }

    @Override
    public void changeConnectionState(final ConnectionState connectionState, final ConnectionState newConnectionState) {
        if (!tryChangeConnectionState(connectionState, newConnectionState)) {
            throw new IllegalStateException("Failed to change connection state");
        }
    }

    @Override
    public boolean tryChangeConnectionState(final ConnectionState connectionState, final ConnectionState newConnectionState) {
        if (!_connectionState.compareAndSet(connectionState, newConnectionState)) {
            return false;
        }

        logger.debug("Changed connection state to '{}'", newConnectionState);

        for (final ConnectionListener listener : _listeners) {
            try {
                newConnectionState.notifyConnectionListener(listener);
            } catch (final Throwable t) {
                logger.warn("Failed to notify connection listener", t);
            }
        }

        return true;
    }

    @Override
    public void onChannelOpened() {
    }

    @Override
    public void onChannelClosed() {
    }

    @Override
    public void onConnectionSlow() {
        for (final ConnectionListener listener : _listeners) {
            listener.onConnectionSlow();
        }
    }

    @Override
    public void onConnectionLost() {
        _connectionState.get().reconnect(this);
    }

    @Override
    public void onError(final Throwable cause) {
        handleError(cause);
    }

    @Override
    public void onSending(final String message) {
        for (final ConnectionListener listener : _listeners) {
            listener.onSending(message);
        }
    }

    @Override
    public void onReceived(final String message) {
        for (final ConnectionListener listener : _listeners) {
            listener.onReceived(message);
        }
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
    public Executor getExecutor() {
        return _executor;
    }

    @Override
    public Scheduler getScheduler() {
        return _scheduler;
    }

    @Override
    public JsonMapper getMapper() {
        return _mapper;
    }

    @Override
    public Map<String, Collection<String>> getHeaders() {
        return Collections.unmodifiableMap(_headers);
    }

    @Override
    public Map<String, Collection<String>> getParameters() {
        return Collections.unmodifiableMap(_parameters);
    }

    @Override
    public String getConnectionData() {
        return _connectionData;
    }

    @Override
    public TransportOptions getTransportOptions() {
        if (_transportOptions == null) {
            throw new IllegalStateException("Transport options have not been set");
        }

        return _transportOptions;
    }
}
