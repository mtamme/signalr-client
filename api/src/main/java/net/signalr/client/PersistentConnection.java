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

import net.signalr.client.concurrent.Promise;
import net.signalr.client.concurrent.TimerScheduler;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.transport.DefaultTransportManager;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportManager;

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
     * @param serializer The serializer.
     */
    public PersistentConnection(final String url, final Transport transport, JsonSerializer serializer) {
        this(new PersistentConnectionContext(url, new DefaultTransportManager(transport), new TimerScheduler(), serializer));
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
        final TransportManager transportManager = _context.getTransportManager();

        return transportManager.getTransport();
    }

    @Override
    public JsonSerializer getSerializer() {
        return _context.getSerializer();
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
    public final void addQueryParameter(final String name, final String value) {
        _context.getState().addQueryParameter(_context, name, value);
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
