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

import java.io.IOException;

import net.signalr.client.util.concurrent.Completable;
import net.signalr.client.util.concurrent.Schedulable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the transport ping.
 */
final class TransportPing implements Schedulable {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(TransportPing.class);

    /**
     * The transport ping response value.
     */
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
     * @param manager The transport manager.
     * @param context The transport context.
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
    public void onScheduled() {
    }

    @Override
    public void onCancelled() {
    }

    @Override
    public void run() {
        logger.debug("Sending transport ping...");

        final Transport transport = _manager.getTransport();

        transport.ping(_context).then(new Completable<PingResponse>() {
            @Override
            public void setSuccess(final PingResponse response) {
                final String value = response.getValue();

                logger.debug("Received transport ping response: '{}'", value);

                if (!PING_RESPONSE_VALUE.equalsIgnoreCase(value)) {
                    _manager.handleError(new IOException("Received invalid transport ping response: '" + value + "'."));
                }
            }

            @Override
            public void setFailure(final Throwable cause) {
                logger.warn("Transport ping failed", cause);

                _manager.handleError(cause);
            }
        });
    }
}