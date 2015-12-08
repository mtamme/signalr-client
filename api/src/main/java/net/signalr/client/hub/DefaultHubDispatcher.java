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
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.Connection;
import net.signalr.client.ConnectionAdapter;
import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonMapper;
import net.signalr.client.util.concurrent.promise.Compose;
import net.signalr.client.util.concurrent.promise.Deferred;
import net.signalr.client.util.concurrent.promise.Promise;

/**
 * Represents the default hub dispatcher.
 */
final class DefaultHubDispatcher extends ConnectionAdapter implements HubDispatcher {

    /**
     * The private logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHubDispatcher.class);

    /**
     * The connection.
     */
    private final Connection _connection;

    /**
     * The next callback ID.
     */
    private final AtomicLong _nextCallbackId;

    /**
     * The proxies.
     */
    private final Map<String, DefaultHubProxy> _proxies;

    /**
     * The deferred responses.
     */
    private final Map<String, Deferred<HubResponse>> _responses;

    /**
     * Initializes a new instance of the {@link DefaultHubDispatcher} class.
     * 
     * @param connection The connection.
     */
    public DefaultHubDispatcher(final Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection must not be null");
        }

        _connection = connection;

        _nextCallbackId = new AtomicLong(0);
        _proxies = new ConcurrentHashMap<>();
        _responses = new ConcurrentHashMap<>();
    }

    /**
     * Updates the connection data.
     * 
     * @param newHubName The new hub name.
     */
    private void updateConnectionData(final String newHubName) {
        final HubNames hubNames = new HubNames();

        hubNames.addAll(_proxies.keySet());
        hubNames.add(newHubName);
        final JsonMapper mapper = _connection.getMapper();
        final String connectionData = mapper.toJson(hubNames);

        _connection.setConnectionData(connectionData);
    }

    /**
     * Returns the next callback ID.
     * 
     * @return The next callback ID.
     */
    private String nextCallbackId() {
        final long callbackId = _nextCallbackId.incrementAndGet();

        return String.valueOf(callbackId);
    }

    /**
     * Handles a hub response.
     * 
     * @param callbackId The callback ID.
     * @param response The hub response.
     */
    private void handleResponse(final String callbackId, final HubResponse response) {
        final Deferred<HubResponse> deferred = _responses.remove(callbackId);

        if (deferred == null) {
            LOGGER.warn("Received response for unknown callback ID {}", callbackId);
            return;
        }
        deferred.setSuccess(response);
    }

    /**
     * Handles hub messages.
     * 
     * @param messages The hub messages.
     */
    private void handleMessages(final HubMessage[] messages) {
        for (final HubMessage message : messages) {
            final String hubName = message.getHubName();
            final String lowerCaseHubName = hubName.toLowerCase();
            final DefaultHubProxy proxy = _proxies.get(lowerCaseHubName);

            if (proxy == null) {
                continue;
            }
            proxy.onInvoke(message);
        }
    }

    @Override
    public void onReceived(final String message) {
        final JsonMapper mapper = _connection.getMapper();
        final JsonElement element = mapper.toElement(message);
        final HubResponse response = new HubResponse(element);
        final String callbackId = response.getCallbackId();

        if (callbackId != null) {
            handleResponse(callbackId, response);
        } else {
            final HubMessage[] messages = response.getMessages();

            handleMessages(messages);
        }
    }

    @Override
    public HubProxy newHubProxy(final String hubName) {
        if (hubName == null) {
            throw new IllegalArgumentException("Hub name must not be null");
        }

        final String lowerCaseHubName = hubName.toLowerCase();
        DefaultHubProxy proxy = _proxies.get(lowerCaseHubName);

        if (proxy == null) {
            // Update the connection data before adding the new hub proxy
            // since it could fail when the underlying connection is not disconnected.
            updateConnectionData(hubName);
            proxy = new DefaultHubProxy(hubName, this);
            _proxies.put(lowerCaseHubName, proxy);
        }

        return proxy;
    }

    @Override
    public Promise<HubResponse> invoke(final HubRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }

        final String callbackId = nextCallbackId();

        request.setCallbackId(callbackId);
        final JsonMapper mapper = _connection.getMapper();
        final String message = mapper.toJson(request);
        final Deferred<HubResponse> deferred = new Deferred<>();

        _responses.put(callbackId, deferred);

        return _connection.send(message).then(new Compose<Void, HubResponse>() {
            @Override
            protected Promise<HubResponse> doCompose(final Void value) throws Exception {
                return deferred;
            }

            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                _responses.remove(callbackId);
            }
        });
    }
}
