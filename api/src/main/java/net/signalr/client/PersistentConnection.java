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

import net.signalr.client.json.DefaultJsonMapper;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.DefaultTransportManager;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportManager;
import net.signalr.client.util.concurrent.ScheduledExecutorServiceScheduler;
import net.signalr.client.util.concurrent.Promise;

/**
 * Represents a persistent connection.
 */
public final class PersistentConnection implements Connection {

    /**
     * The connection context.
     */
    private final ConnectionContext _context;

    /**
     * Initializes a new instance of the {@link PersistentConnection} class.
     * 
     * @param url The connection URL.
     * @param transport The transport.
     * @param factory The factory.
     */
    public PersistentConnection(final String url, final Transport transport, final JsonFactory factory) {
        this(new PersistentConnectionContext(url, new DefaultTransportManager(transport), new ScheduledExecutorServiceScheduler(), new DefaultJsonMapper(factory)));
    }

    /**
     * Initializes a new instance of the {@link PersistentConnection} class.
     * 
     * @param context The connection context.
     */
    PersistentConnection(final ConnectionContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        _context = context;
    }

    @Override
    public String getProtocolVersion() {
        return _context.getProtocolVersion();
    }

    @Override
    public String getUrl() {
        return _context.getUrl();
    }

    @Override
    public Transport getTransport() {
        final TransportManager manager = _context.getTransportManager();

        return manager.getTransport();
    }

    @Override
    public JsonMapper getMapper() {
        return _context.getMapper();
    }

    @Override
    public boolean isConnected() {
        return _context.getState().isConnected();
    }

    @Override
    public final void addHeader(final String name, final String value) {
        _context.getState().addHeader(_context, name, value);
    }

    @Override
    public final void addParameter(final String name, final String value) {
        _context.getState().addParameter(_context, name, value);
    }

    @Override
    public void setConnectionData(final String connectionData) {
        _context.getState().setConnectionData(_context, connectionData);
    }

    @Override
    public final Promise<Void> start(final ConnectionHandler handler) {
        return _context.getState().start(_context, handler);
    }

    @Override
    public final Promise<Void> stop() {
        return _context.getState().stop(_context);
    }

    @Override
    public final Promise<Void> send(final String message) {
        return _context.getState().send(_context, message);
    }
}
