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
     * The last heartbeat time in milliseconds.
     */
    private final AtomicLong _lastHeartbeatTime;

    /**
     * The transport status.
     */
    private Status _status;

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
        _status = Status.VITAL;
    }

    /**
     * Returns the transport status.
     * 
     * @return The transport status.
     */
    private Status getStatus() {
        final long lastHeartbeatTime = _lastHeartbeatTime.get();
        final long currentTime = _timeProvider.currentTimeMillis();
        final long elapsedTime = currentTime - lastHeartbeatTime;

        if (elapsedTime >= _lostTimeout) {
            return Status.LOST;
        }
        if (elapsedTime >= _slowTimeout) {
            return Status.SLOW;
        }

        return Status.VITAL;
    }

    /**
     * Returns the monitor period.
     * 
     * @return The monitor period.
     */
    public long getPeriod() {
        return _lostTimeout - _slowTimeout;
    }

    /**
     * Updates the heartbeat time.
     */
    private void updateHeartbeatTime() {
        logger.debug("Received transport heartbeat");

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
    public void onConnectionLost() {
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
        _manager.addTransportListener(this);
    }

    @Override
    public void onCancelled() {
        _manager.removeTransportListener(this);
    }

    @Override
    public void run() {
        final Status status = getStatus();

        if (status == Status.VITAL) {
            logger.debug("Heartbeat is recent, connection seems to be vital");
        }
        if (status == _status) {
            return;
        }
        _status = status;
        if (status == Status.SLOW) {
            logger.warn("Heartbeat has been missed, connection may be dead/slow");

            _manager.handleConnectionSlow();
        } else if (status == Status.LOST) {
            logger.error("Heartbeat timed out, connection has been lost");

            _manager.handleConnectionLost();
        }
    }

    /**
     * Defines all transport statuses.
     */
    private static enum Status {
        /**
         * Transport is vital.
         */
        VITAL,

        /**
         * Transport may be dead/slow.
         */
        SLOW,

        /**
         * Transport has been lost.
         */
        LOST
    }
}