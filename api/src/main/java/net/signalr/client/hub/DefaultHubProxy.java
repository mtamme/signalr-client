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

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.json.JsonElement;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Apply;

/**
 * Represents the default hub proxy.
 */
final class DefaultHubProxy implements HubProxy, HubCallback<HubMessage> {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultHubProxy.class);

    /**
     * The hub name.
     */
    private final String _hubName;

    /**
     * The hub dispatcher.
     */
    private final HubDispatcher _dispatcher;

    /**
     * The hub callbacks.
     */
    private final ConcurrentHashMap<String, HubCallback<HubMessage>> _callbacks;

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

        _callbacks = new ConcurrentHashMap<String, HubCallback<HubMessage>>();
    }

    @Override
    public void onInvoke(final HubMessage message) {
        final String methodName = message.getMethodName();
        final HubCallback<HubMessage> callback = _callbacks.get(methodName);

        if (callback != null) {
            callback.onInvoke(message);
        }
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

        return _dispatcher.invoke(request).then(new Apply<HubResponse, R>() {
            @Override
            protected R doApply(final HubResponse response) throws Exception {
                final String errorMessage = response.getErrorMessage();

                if (errorMessage != null) {
                    final String errorData = response.getErrorData();
                    final String stackTrace = response.getStackTrace();

                    throw new HubException(errorMessage, errorData, stackTrace);
                }
                final JsonElement data = response.getData();

                return data.toObject(returnType, null);
            }
        });
    }

    @Override
    public void register(final String methodName, final HubCallback<JsonElement[]> callback) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method name must not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }

        _callbacks.put(methodName, new HubCallback<HubMessage>() {
            @Override
            public void onInvoke(final HubMessage message) {
                final JsonElement[] arguments = message.getArguments();

                callback.onInvoke(arguments);
            }
        });
    }

    @Override
    public <T> void register(final String methodName, final Class<T> argumentType, final HubCallback<T> callback) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method name must not be null");
        }
        if (argumentType == null) {
            throw new IllegalArgumentException("Argument type must not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }

        _callbacks.put(methodName, new HubCallback<HubMessage>() {
            @Override
            public void onInvoke(final HubMessage message) {
                final JsonElement[] arguments = message.getArguments();

                if (arguments.length != 1) {
                    logger.warn("Received message with invalid number of arguments");
                    return;
                }
                final T argument = arguments[0].toObject(argumentType, null);

                callback.onInvoke(argument);
            }
        });
    }

    @Override
    public void unregister(final String methodName) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method name must not be null");
        }

        _callbacks.remove(methodName);
    }
}
