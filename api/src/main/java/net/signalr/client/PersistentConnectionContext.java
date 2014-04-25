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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
     * The scheduler.
     */
    private final Scheduler _scheduler;

    /**
     * The mapper.
     */
    private final JsonMapper _mapper;

    /**
     * The current state.
     */
    private final AtomicReference<ConnectionState> _state;

    /**
     * The headers.
     */
    private final Map<String, Collection<String>> _headers;

    /**
     * The parameters.
     */
    private final Map<String, Collection<String>> _parameters;

    /**
     * The connection data.
     */
    private String _connectionData;

    /**
     * The transport options.
     */
    private TransportOptions _options;

    /**
     * Initializes a new instance of the {@link PersistentConnectionContext} class.
     * 
     * @param url The connection URL.
     * @param manager The transport manager.
     * @param scheduler The scheduler.
     * @param mapper The mapper.
     */
    protected PersistentConnectionContext(final String url, final TransportManager manager, final Scheduler scheduler, final JsonMapper mapper) {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null");
        }
        if (manager == null) {
            throw new IllegalArgumentException("Manager must not be null");
        }
        if (scheduler == null) {
            throw new IllegalArgumentException("Scheduler must not be null");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper must not be null");
        }

        _url = url;
        _manager = manager;
        _scheduler = scheduler;
        _mapper = mapper;
        final ConnectionState initialState = new DisconnectedConnectionState();

        _state = new AtomicReference<ConnectionState>(initialState);
        _headers = new HashMap<String, Collection<String>>();
        _parameters = new HashMap<String, Collection<String>>();

        _connectionData = null;
        _options = null;
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
        return _options;
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
    public void setConnectionData(final String connectionData) {
        _connectionData = connectionData;
    }

    @Override
    public void setTransportOptions(final TransportOptions options) {
        _options = options;
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
            logger.warn("Failed to change connection state from '{}' to '{}'", oldState, newState);

            return false;
        }

        logger.info("Changed connection state from '{}' to '{}'", oldState, newState);

        return true;
    }
}
