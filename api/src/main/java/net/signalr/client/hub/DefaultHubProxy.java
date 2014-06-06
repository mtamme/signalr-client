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

package net.signalr.client.hub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.json.JsonElement;
import net.signalr.client.util.concurrent.promise.Apply;
import net.signalr.client.util.concurrent.promise.Promise;

/**
 * Represents the default hub proxy.
 */
final class DefaultHubProxy implements HubProxy, HubCallback<HubMessage> {

    /**
     * The private logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHubProxy.class);

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
    private final Map<String, HubCallback<HubMessage>> _callbacks;

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
    public void register(final String methodName, final HubCallback<JsonElement> callback) {
        if (methodName == null) {
            throw new IllegalArgumentException("Method name must not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }

        _callbacks.put(methodName, new HubCallback<HubMessage>() {
            @Override
            public void onInvoke(final HubMessage message) {
                final JsonElement arguments = message.getArguments();

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
                final JsonElement arguments = message.getArguments();
                final int count = arguments.size();

                if (count != 1) {
                    LOGGER.warn("Received message with wrong number of arguments: {}", count);
                    return;
                }
                final JsonElement argument = arguments.get(0);
                final T object = argument.toObject(argumentType, null);

                callback.onInvoke(object);
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
