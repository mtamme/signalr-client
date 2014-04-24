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

import net.signalr.client.json.JsonElement;
import net.signalr.client.util.concurrent.Function;
import net.signalr.client.util.concurrent.Promise;

/**
 * Represents the default hub proxy.
 */
final class DefaultHubProxy implements HubProxy {

    /**
     * The hub name.
     */
    private final String _hubName;

    /**
     * The hub dispatcher.
     */
    private final HubDispatcher _dispatcher;

    /**
     * Initializes a new instance of the {@link DefaultHubProxy} class.
     * 
     * @param hubName The hub name.
     * @param dispatcher hub dispatcher.
     */
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
    public <R> Promise<R> invoke(final String methodName, final Class<R> returnType, final Object... arguments) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method name must not e null");
        }
        if (returnType == null) {
            throw new IllegalArgumentException("Return type must not e null");
        }
        if (arguments == null) {
            throw new IllegalArgumentException("Arguments must not e null");
        }

        final HubRequest request = new HubRequest();

        request.setHubName(_hubName);
        request.setMethodName(methodName);
        request.setArguments(arguments);

        return _dispatcher.invoke(request).thenApply(new Function<HubResponse, R>() {
            public R apply(final HubResponse response) throws Exception {
                final String errorMessage = response.getErrorMessage();

                if (errorMessage != null) {
                    final String errorData = response.getErrorData();
                    final String stackTrace = response.getStackTrace();

                    throw new HubException(errorMessage, errorData, stackTrace);
                }
                final JsonElement data = response.getData();

                return data.toObject(returnType);
            }
        });
    }

    @Override
    public <T> void subscribe(final String eventName, final HubEventListener<T> listener) {
    }
}
