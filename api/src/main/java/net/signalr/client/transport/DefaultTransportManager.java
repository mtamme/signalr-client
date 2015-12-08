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

package net.signalr.client.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import net.signalr.client.util.AbstractLifecycle;
import net.signalr.client.util.TimeProvider;
import net.signalr.client.util.concurrent.Job;
import net.signalr.client.util.concurrent.Scheduler;

/**
 * Represents the default transport manager.
 */
public final class DefaultTransportManager extends AbstractLifecycle<TransportContext> implements TransportManager {

    /**
     * The transport ping period in seconds.
     */
    private static final long PING_PERIOD = 5;

    /**
     * The transport.
     */
    private final Transport _transport;

    /**
     * The time provider.
     */
    private final TimeProvider _timeProvider;

    /**
     * The transport listeners.
     */
    private final Set<TransportListener> _listeners;

    /**
     * The jobs.
     */
    private final List<Job> _jobs;

    /**
     * Initializes a new instance of the {@link DefaultTransportManager} class.
     * 
     * @param transport The transport.
     */
    public DefaultTransportManager(final Transport transport, final TimeProvider timeProvider) {
        if (transport == null) {
            throw new IllegalArgumentException("Transport must not be null");
        }
        if (timeProvider == null) {
            throw new IllegalArgumentException("Time provider must not be null");
        }

        _transport = transport;
        _timeProvider = timeProvider;

        _listeners = new CopyOnWriteArraySet<>();
        _jobs = new ArrayList<>();
    }

    @Override
    public Transport getTransport() {
        return _transport;
    }

    @Override
    public void addTransportListener(final TransportListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.add(listener);
    }

    @Override
    public void removeTransportListener(final TransportListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }

        _listeners.remove(listener);
    }

    @Override
    public void handleConnectionLost() {
        for (final TransportListener listener : _listeners) {
            listener.onConnectionLost();
        }
    }

    @Override
    public void handleConnectionSlow() {
        for (final TransportListener listener : _listeners) {
            listener.onConnectionSlow();
        }
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

    @Override
    protected void doStart(final TransportContext context) {
        final TransportOptions options = context.getTransportOptions();
        final long keepAliveTimeout = options.getKeepAliveTimeout();
        final Scheduler scheduler = context.getScheduler();

        if (keepAliveTimeout > 0) {
            final TransportMonitor monitor = new TransportMonitor(this, _timeProvider, keepAliveTimeout, TimeUnit.MILLISECONDS);
            final long monitorPeriod = monitor.getPeriod();
            final Job job = scheduler.scheduleJob(monitor, monitorPeriod, TimeUnit.MILLISECONDS);

            _jobs.add(job);
        }
        final TransportPing ping = new TransportPing(this, context);
        final Job job = scheduler.scheduleJob(ping, PING_PERIOD, TimeUnit.MINUTES);

        _jobs.add(job);
    }

    @Override
    protected void doStop(final TransportContext context) {
        for (final Job job : _jobs) {
            job.cancel();
        }
        _jobs.clear();
    }
}
