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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.signalr.client.Connection;
import net.signalr.client.ConnectionAdapter;
import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonMapper;
import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.OnComplete;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;
import net.signalr.client.util.concurrent.Compose;

/**
 * Represents the default hub dispatcher.
 */
final class DefaultHubDispatcher extends ConnectionAdapter implements HubDispatcher {

    /**
     * The private logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultHubDispatcher.class);

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
    private final ConcurrentHashMap<String, Deferred<HubResponse>> _responses;

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
        _proxies = new ConcurrentHashMap<String, DefaultHubProxy>();
        _responses = new ConcurrentHashMap<String, Deferred<HubResponse>>();
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
            logger.warn("Received response for unknown callback ID {}", callbackId);
            return;
        }
        deferred.setSuccess(response);
    }

    /**
     * Handles hub messages.
     * 
     * @param message The hub messages.
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
    public HubProxy getProxy(final String hubName) {
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
        final Deferred<HubResponse> deferred = Promises.newDeferred();

        _responses.put(callbackId, deferred);

        return _connection.send(message).then(new OnComplete<Void>() {
            @Override
            protected void onFailure(final Throwable cause) throws Exception {
                _responses.remove(callbackId);
            }
        }).then(new Compose<Void, HubResponse>() {
            @Override
            protected Promise<HubResponse> doCompose(final Void value) throws Exception {
                return deferred;
            }
        });
    }
}
