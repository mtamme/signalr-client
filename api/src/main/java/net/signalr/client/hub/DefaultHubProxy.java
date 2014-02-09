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

package net.signalr.client.hub;

import net.signalr.client.concurrent.Function;
import net.signalr.client.concurrent.Promise;

/**
 * Represents the default hub proxy.
 */
final class DefaultHubProxy implements HubProxy {

    private final String _hubName;

    private final HubDispatcher _dispatcher;

    public DefaultHubProxy(final String hubName, final HubDispatcher dispatcher) {
        if (hubName == null) {
            throw new IllegalArgumentException("Hub name must not be null");
        }
        if (dispatcher == null) {
            throw new IllegalArgumentException("Dispatcher must not be null");
        }

        _hubName = hubName;
        _dispatcher = dispatcher;
    }

    @Override
    public <T> Promise<T> invoke(final String methodName, final Object... args) {
        final HubRequest request = new HubRequest();

        request.setHubName(_hubName);
        request.setMethodName(methodName);
        request.setArguments(args);

        return _dispatcher.invoke(request).thenApply(new Function<HubResponse, T>() {
            @Override
            public T apply(final HubResponse value) throws Exception {
                return null;
            }
        });
    }

    @Override
    public void subscribe(final String eventName, final HubListener listener) {
    }
}