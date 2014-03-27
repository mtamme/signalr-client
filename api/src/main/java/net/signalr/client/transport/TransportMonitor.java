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
     * The lost timeout.
     */
    private final long _lostTimeout;

    /**
     * The slow timeout.
     */
    private final long _slowTimeout;

    /**
     * The heart beat time.
     */
    private final AtomicLong _heartbeatTime;

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
     */
    public TransportMonitor(final TransportManager manager, final TimeProvider timeProvider, final long timeout) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager must not be null");
        }
        if (timeProvider == null) {
            throw new IllegalArgumentException("Time provider must not be null");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("Timeout must be greater than 0");
        }

        _manager = manager;
        _timeProvider = timeProvider;
        _lostTimeout = timeout;
        _slowTimeout = (timeout * 2) / 3;
        final long currentTime = _timeProvider.currentTimeMillis();

        _heartbeatTime = new AtomicLong(currentTime);
        _status = TransportStatus.VITAL;
    }

    /**
     * Returns the transport status.
     * 
     * @return The transport status.
     */
    private TransportStatus getStatus() {
        final long heartbeatTime = _heartbeatTime.get();
        final long currentTime = _timeProvider.currentTimeMillis();
        final long elapsedTime = currentTime - heartbeatTime;

        if (elapsedTime >= _lostTimeout) {
            return TransportStatus.LOST;
        }
        if (elapsedTime >= _slowTimeout) {
            return TransportStatus.SLOW;
        }

        return TransportStatus.VITAL;
    }

    /**
     * Updates the hear beat time.
     */
    private void updateHeartbeatTime() {
        final long currentTime = _timeProvider.currentTimeMillis();

        _heartbeatTime.set(currentTime);
    }

    /**
     * Returns the monitor interval.
     * 
     * @return The monitor interval.
     */
    public long getInterval() {
        return _lostTimeout - _slowTimeout;
    }

    @Override
    public void run() {
        final TransportStatus status = getStatus();

        if (status == _status) {
            return;
        }
        if (status == TransportStatus.LOST) {
            logger.error("Keep alive timed out, connection has been lost.");

            _manager.handleConnectionLost();
        } else if (status == TransportStatus.SLOW) {
            logger.warn("Keep alive has been missed, connection may be dead/slow.");

            _manager.handleConnectionSlow();
        }
        _status = status;
    }

    @Override
    public void onOpen() {
        updateHeartbeatTime();
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onSending(final String message) {
    }

    @Override
    public void onReceived(final String message) {
        updateHeartbeatTime();
    }

    @Override
    public void onError(final Throwable throwable) {
    }
}