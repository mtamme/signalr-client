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

package net.signalr.client.transports;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.util.TimeProvider;

/**
 * Represents the transport monitor.
 */
final class TransportMonitor implements Runnable, TransportChannelHandler {

    /**
     * The private logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportMonitor.class);

    /**
     * Defines heart beat states.
     */
    private enum HeartbeatState {
        NORMAL, WARNING_TIMEOUT, TIMEOUT
    }

    /**
     * The transport manager.
     */
    private final TransportManager _manager;

    /**
     * The time provider.
     */
    private final TimeProvider _timeProvider;

    /**
     * The timeout.
     */
    private final long _keepAliveTimeout;

    /**
     * The warning timeout.
     */
    private final long _keepAliveWarningTimeout;

    /**
     * The last heart beat time.
     */
    private final AtomicLong _lastHeartbeatTime;

    /**
     * The last heart beat state.
     */
    private HeartbeatState _lastHeartbeatState;

    /**
     * Initializes a new instance of the {@link TransportMonitor} class.
     * 
     * @param timeProvider The time provider.
     * @param manager The transport manager.
     * @param keepAliveTimeout The keep-alive timeout.
     */
    public TransportMonitor(final TransportManager manager, final TimeProvider timeProvider, final long keepAliveTimeout) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager must not be null");
        }
        if (timeProvider == null) {
            throw new IllegalArgumentException("Time provider must not be null");
        }
        if (keepAliveTimeout <= 0) {
            throw new IllegalArgumentException("Keep alive timeout must be greater than 0");
        }

        _manager = manager;
        _timeProvider = timeProvider;
        _keepAliveTimeout = keepAliveTimeout;
        _keepAliveWarningTimeout = (keepAliveTimeout * 2) / 3;
        final long currentTime = _timeProvider.currentTimeMillis();

        _lastHeartbeatTime = new AtomicLong(currentTime);
        _lastHeartbeatState = HeartbeatState.NORMAL;
    }

    /**
     * Returns the current heart beat state.
     * 
     * @return The current heart beat state.
     */
    private HeartbeatState getCurrentHeartbeatState() {
        final long lastHeartbeatTime = _lastHeartbeatTime.get();
        final long currentTime = _timeProvider.currentTimeMillis();
        final long elapsedTime = currentTime - lastHeartbeatTime;

        if (elapsedTime >= _keepAliveTimeout) {
            return HeartbeatState.TIMEOUT;
        }
        if (elapsedTime >= _keepAliveWarningTimeout) {
            return HeartbeatState.WARNING_TIMEOUT;
        }

        return HeartbeatState.NORMAL;
    }

    /**
     * Updates the last hear beat time.
     */
    private void updateLastHeartbeatTime() {
        final long currentTime = _timeProvider.currentTimeMillis();

        _lastHeartbeatTime.set(currentTime);
    }

    public long getInterval() {
        return _keepAliveTimeout - _keepAliveWarningTimeout;
    }

    @Override
    public void run() {
        final HeartbeatState currentHeartbeatState = getCurrentHeartbeatState();

        if (currentHeartbeatState == _lastHeartbeatState) {
            return;
        }
        if (currentHeartbeatState == HeartbeatState.TIMEOUT) {
            LOGGER.error("Keep alive timed out, connection has been lost.");

            _manager.handleConnectionLost();
        } else if (currentHeartbeatState == HeartbeatState.WARNING_TIMEOUT) {
            LOGGER.warn("Keep alive has been missed, connection may be dead/slow.");

            _manager.handleConnectionSlow();
        }
        _lastHeartbeatState = currentHeartbeatState;
    }

    @Override
    public void onOpen() {
        updateLastHeartbeatTime();
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onSending(final String message) {
    }

    @Override
    public void onReceived(final String message) {
        updateLastHeartbeatTime();
    }

    @Override
    public void onError(final Throwable throwable) {
    }
}