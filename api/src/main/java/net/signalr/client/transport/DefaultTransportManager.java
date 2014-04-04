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

import java.util.concurrent.TimeUnit;

import net.signalr.client.util.SystemTimeProvider;
import net.signalr.client.util.concurrent.Scheduler;

/**
 * Represents the default transport manager.
 */
public final class DefaultTransportManager implements TransportManager {

    /**
     * The transport monitor job name.
     */
    private static final String MONITOR_NAME = "Transport-Monitor";

    /**
     * The transport ping job name.
     */
    private static final String PING_NAME = "Transport-Ping";

    /**
     * The transport ping interval in seconds.
     */
    private static final long PING_INTERVAL = 5;

    /**
     * The transport.
     */
    private final Transport _transport;

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
    }

    @Override
    public Transport getTransport() {
        return _transport;
    }

    @Override
    public void handleConnectionLost() {
    }

    @Override
    public void handleConnectionSlow() {
    }

    @Override
    public void start(final TransportContext context) {
        final Scheduler scheduler = context.getScheduler();
        final long keepAliveTimeout = context.getKeepAliveTimeout();

        if (keepAliveTimeout > 0) {
            final TransportMonitor monitor = new TransportMonitor(this, SystemTimeProvider.INSTANCE, keepAliveTimeout);
            final long monitorInterval = monitor.getInterval();

            scheduler.scheduleJob(MONITOR_NAME, monitor, monitorInterval, monitorInterval, TimeUnit.MILLISECONDS);
        }
        final TransportPing ping = new TransportPing(this, context);

        scheduler.scheduleJob(PING_NAME, ping, PING_INTERVAL, PING_INTERVAL, TimeUnit.MINUTES);
    }

    @Override
    public void stop(final TransportContext context) {
        final Scheduler scheduler = context.getScheduler();

        scheduler.unscheduleJob(PING_NAME);
        final long keepAliveTimeout = context.getKeepAliveTimeout();

        if (keepAliveTimeout > 0) {
            scheduler.unscheduleJob(MONITOR_NAME);
        }
    }

    @Override
    public void handleChannelOpened() {
    }

    @Override
    public void handleChannelClosed() {
    }

    @Override
    public void handleError(final Throwable cause) {
    }

    @Override
    public void handleMessageSending(final String message) {
    }

    @Override
    public void handleMessageReceived(final String message) {
    }
}
