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
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import net.signalr.client.concurrent.Callback;
import net.signalr.client.concurrent.Deferred;
import net.signalr.client.concurrent.Promise;
import net.signalr.client.concurrent.Promises;

/**
 * Represents an auto transport.
 */
public final class AutoTransport implements Transport {

    /**
     * The transport name.
     */
    private static final String NAME = "auto";

    /**
     * The transports.
     */
    private final List<Transport> _transports;

    /**
     * Initializes a new instance of the {@link AutoTransport} class.
     * 
     * @param transports The transports.
     */
    public AutoTransport(final Transport... transports) {
        if (transports == null) {
            throw new IllegalArgumentException("Transports must not be null");
        }
        if (transports.length <= 0) {
            throw new IllegalArgumentException("Transports must not be empty");
        }

        _transports = new ArrayList<Transport>();
        for (final Transport transport : transports) {
            _transports.add(transport);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Promise<NegotiationResponse> negotiate(final TransportContext context) {
        final Transport transport = _transports.get(0);

        return transport.negotiate(context);
    }

    @Override
    public Promise<TransportChannel> connect(final TransportContext context, final TransportChannelHandler handler, final boolean reconnect) {
        final Deferred<TransportChannel> deferred = new Deferred<TransportChannel>();
        final Iterator<Transport> transports = _transports.iterator();
        final Callback<TransportChannel> callback = new Callback<TransportChannel>() {
            @Override
            public void onResolved(final TransportChannel channel) {
                deferred.resolve(channel);
            }

            @Override
            public void onRejected(final Throwable throwable) {
                if (transports.hasNext()) {
                    final Transport transport = transports.next();

                    transport.connect(context, handler, reconnect).thenCall(this);
                } else {
                    deferred.reject(throwable);
                }
            }
        };

        if (transports.hasNext()) {
            final Transport transport = transports.next();

            transport.connect(context, handler, reconnect).thenCall(callback);

            return deferred;
        }

        return Promises.rejected(new OperationNotSupportedException());
    }

    @Override
    public Promise<PingResponse> ping(final TransportContext context) {
        final Transport transport = _transports.get(0);

        return transport.ping(context);
    }

    @Override
    public Promise<Void> abort(final TransportContext context) {
        return null;
    }

}
