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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.signalr.client.json.DefaultJsonMapper;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonMapper;
import net.signalr.client.transport.DefaultTransportManager;
import net.signalr.client.transport.Transport;
import net.signalr.client.transport.TransportManager;
import net.signalr.client.util.SystemTimeProvider;
import net.signalr.client.util.TimeProvider;
import net.signalr.client.util.concurrent.ScheduledExecutorServiceScheduler;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Scheduler;

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
        this(url, transport, SystemTimeProvider.INSTANCE, factory);
    }

    /**
     * Initializes a new instance of the {@link PersistentConnection} class.
     * 
     * @param url The connection URL.
     * @param transport The transport.
     * @param timeProvider The time provider.
     * @param factory The factory.
     */
    public PersistentConnection(final String url, final Transport transport, final TimeProvider timeProvider, final JsonFactory factory) {
        this(url, new DefaultTransportManager(transport, timeProvider), Executors.newCachedThreadPool(), new ScheduledExecutorServiceScheduler(), new DefaultJsonMapper(factory));
    }

    /**
     * Initializes a new instance of the {@link PersistentConnection} class.
     * 
     * @param url The connection URL.
     * @param manager The transport manager.
     * @param executor The executor.
     * @param scheduler The scheduler.
     * @param mapper The mapper.
     */
    public PersistentConnection(final String url, final TransportManager manager, final Executor executor, final Scheduler scheduler, final JsonMapper mapper) {
        this(new PersistentConnectionContext(url, manager, executor, scheduler, mapper));
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
        return _context.getConnectionState().isConnected();
    }

    @Override
    public final void addHeader(final String name, final String value) {
        _context.getConnectionState().addHeader(_context, name, value);
    }

    @Override
    public final void addParameter(final String name, final String value) {
        _context.getConnectionState().addParameter(_context, name, value);
    }

    @Override
    public void addConnectionListener(final ConnectionListener listener) {
        _context.getConnectionNotifier().addConnectionListener(listener);
    }

    @Override
    public void removeConnectionListener(final ConnectionListener listener) {
        _context.getConnectionNotifier().removeConnectionListener(listener);
    }

    @Override
    public void setConnectionData(final String connectionData) {
        _context.getConnectionState().setConnectionData(_context, connectionData);
    }

    @Override
    public final Promise<Void> start() {
        return _context.getConnectionState().start(_context);
    }

    @Override
    public final Promise<Void> stop() {
        return _context.getConnectionState().stop(_context);
    }

    @Override
    public final Promise<Void> send(final String message) {
        return _context.getConnectionState().send(_context, message);
    }
}
