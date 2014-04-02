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

import java.io.IOException;

import net.signalr.client.concurrent.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the transport ping.
 */
final class TransportPing implements Runnable {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(TransportPing.class);

    private static final String PING_RESPONSE_VALUE = "pong";

    /**
     * The transport manager.
     */
    private final TransportManager _manager;

    /**
     * The transport context.
     */
    private final TransportContext _context;

    /**
     * Initializes a new instance of the {@link TransportPing} class.
     * 
     * @param context The transport context.
     * @param manager The transport manager.
     */
    public TransportPing(final TransportManager manager, final TransportContext context) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager must not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }

        _manager = manager;
        _context = context;
    }

    @Override
    public void run() {
        logger.info("Sending transport ping...");

        final Transport transport = _manager.getTransport();

        transport.ping(_context).thenCall(new Callback<PingResponse>() {
            @Override
            public void onResolved(final PingResponse response) {
                final String value = response.getValue();

                logger.info("Received transport ping response: '{}'", value);

                if (!PING_RESPONSE_VALUE.equalsIgnoreCase(value)) {
                    _manager.notifyOnError(new IOException("Received invalid transport ping response: '" + value + "'."));
                }
            }

            @Override
            public void onRejected(final Throwable throwable) {
                logger.warn("Transport ping failed", throwable);

                _manager.notifyOnError(throwable);
            }
        });
    }
}