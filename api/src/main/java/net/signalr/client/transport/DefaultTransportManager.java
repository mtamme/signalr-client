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

package net.signalr.client.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import net.signalr.client.util.SystemTimeProvider;
import net.signalr.client.util.concurrent.Job;
import net.signalr.client.util.concurrent.Scheduler;

/**
 * Represents the default transport manager.
 */
public final class DefaultTransportManager implements TransportManager {

    /**
     * The transport ping interval in seconds.
     */
    private static final long PING_INTERVAL = 5;

    /**
     * The transport.
     */
    private final Transport _transport;

    /**
     * The transport listeners.
     */
    private final CopyOnWriteArraySet<TransportListener> _listeners;

    /**
     * The jobs.
     */
    private final List<Job> _jobs;

    /**
     * Initializes a new instance of the {@link DefaultTransportManager} class.
     * 
     * @param transport The transport.
     */
    public DefaultTransportManager(final Transport transport) {
        if (transport == null) {
            throw new IllegalArgumentException("Transport must not be null");
        }

        _transport = transport;

        _listeners = new CopyOnWriteArraySet<TransportListener>();
        _jobs = new ArrayList<Job>();
    }

    @Override
    public Transport getTransport() {
        return _transport;
    }

    @Override
    public void addListener(final TransportListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.add(listener);
    }

    @Override
    public void removeListener(final TransportListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.remove(listener);
    }

    @Override
    public void handleConnectionLost() {
    }

    @Override
    public void handleConnectionSlow() {
        for (final TransportListener listener : _listeners) {
            listener.onConnectionSlow();
        }
    }

    @Override
    public void start(final TransportContext context) {
        final Scheduler scheduler = context.getScheduler();
        final TransportOptions options = context.getTransportOptions();
        final long keepAliveTimeout = options.getKeepAliveTimeout();

        if (keepAliveTimeout > 0) {
            final TransportMonitor monitor = new TransportMonitor(this, SystemTimeProvider.INSTANCE, keepAliveTimeout, TimeUnit.MILLISECONDS);
            final long monitorInterval = monitor.getInterval();
            final Job job = scheduler.scheduleJob(monitor, monitorInterval, TimeUnit.MILLISECONDS);

            _jobs.add(job);
        }
        final TransportPing ping = new TransportPing(this, context);
        final Job job = scheduler.scheduleJob(ping, PING_INTERVAL, TimeUnit.MINUTES);

        _jobs.add(job);
    }

    @Override
    public void stop(final TransportContext context) {
        for (final Job job : _jobs) {
            job.cancel();
        }
        _jobs.clear();
    }

    @Override
    public void handleChannelOpened() {
        for (final TransportListener listener : _listeners) {
            listener.onChannelOpened();
        }
    }

    @Override
    public void handleChannelClosed() {
        for (final TransportListener listener : _listeners) {
            listener.onChannelClosed();
        }
    }

    @Override
    public void handleError(final Throwable cause) {
        for (final TransportListener listener : _listeners) {
            listener.onError(cause);
        }
    }

    @Override
    public void handleMessageSending(final String message) {
        for (final TransportListener listener : _listeners) {
            listener.onSending(message);
        }
    }

    @Override
    public void handleMessageReceived(final String message) {
        for (final TransportListener listener : _listeners) {
            listener.onReceived(message);
        }
    }
}
