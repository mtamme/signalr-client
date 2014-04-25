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
import java.util.concurrent.atomic.AtomicLong;

import net.signalr.client.util.TimeProvider;
import net.signalr.client.util.concurrent.Schedulable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the transport monitor.
 */
final class TransportMonitor implements Schedulable, TransportListener {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(TransportMonitor.class);

    /**
     * The transport manager.
     */
    private final TransportManager _manager;

    /**
     * The time provider.
     */
    private final TimeProvider _timeProvider;

    /**
     * The lost timeout in milliseconds.
     */
    private final long _lostTimeout;

    /**
     * The slow timeout in milliseconds.
     */
    private final long _slowTimeout;

    /**
     * The last heart beat time in milliseconds.
     */
    private final AtomicLong _lastHeartbeatTime;

    /**
     * The transport status.
     */
    private TransportStatus _status;

    /**
     * Initializes a new instance of the {@link TransportMonitor} class.
     * 
     * @param timeProvider The time provider.
     * @param manager The transport manager.
     * @param timeout The timeout.
     * @param timeUnit The time unit.
     */
    public TransportMonitor(final TransportManager manager, final TimeProvider timeProvider, final long timeout, final TimeUnit timeUnit) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager must not be null");
        }
        if (timeProvider == null) {
            throw new IllegalArgumentException("Time provider must not be null");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("Timeout must be greater than 0");
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit must not be null");
        }

        _manager = manager;
        _timeProvider = timeProvider;
        _lostTimeout = timeUnit.toMillis(timeout);
        _slowTimeout = (_lostTimeout * 2) / 3;
        final long currentTime = _timeProvider.currentTimeMillis();

        _lastHeartbeatTime = new AtomicLong(currentTime);
        _status = TransportStatus.VITAL;
    }

    /**
     * Returns the transport status.
     * 
     * @return The transport status.
     */
    private TransportStatus getStatus() {
        final long lastHeartbeatTime = _lastHeartbeatTime.get();
        final long currentTime = _timeProvider.currentTimeMillis();
        final long elapsedTime = currentTime - lastHeartbeatTime;

        if (elapsedTime >= _lostTimeout) {
            return TransportStatus.LOST;
        }
        if (elapsedTime >= _slowTimeout) {
            return TransportStatus.SLOW;
        }

        return TransportStatus.VITAL;
    }

    /**
     * Returns the monitor interval.
     * 
     * @return The monitor interval.
     */
    public long getInterval() {
        return _lostTimeout - _slowTimeout;
    }

    /**
     * Updates the heart beat time.
     */
    private void updateHeartbeatTime() {
        logger.info("Received transport heart beat...");

        final long currentTime = _timeProvider.currentTimeMillis();

        _lastHeartbeatTime.set(currentTime);
    }

    @Override
    public void onChannelOpened() {
        updateHeartbeatTime();
    }

    @Override
    public void onChannelClosed() {
    }

    @Override
    public void onConnectionSlow() {
    }

    @Override
    public void onError(final Throwable cause) {
    }

    @Override
    public void onSending(final String message) {
    }

    @Override
    public void onReceived(final String message) {
        updateHeartbeatTime();
    }

    @Override
    public void onScheduled() {
        _manager.addListener(this);
    }

    @Override
    public void onCancelled() {
        _manager.removeListener(this);
    }

    @Override
    public void run() {
        final TransportStatus status = getStatus();

        if (status == _status) {
            return;
        }
        if (status == TransportStatus.LOST) {
            logger.error("Heart beat timed out, connection has been lost.");

            _manager.handleConnectionLost();
        } else if (status == TransportStatus.SLOW) {
            logger.warn("Heart beat has been missed, connection may be dead/slow.");

            _manager.handleConnectionSlow();
        }
        _status = status;
    }
}