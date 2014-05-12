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

import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
 * Represents a negotiation response.
 */
public final class NegotiationResponse implements JsonReadable, TransportOptions {

    /**
     * The relative URL.
     */
    private String _relativeUrl;

    /**
     * The connection token.
     */
    private String _connectionToken;

    /**
     * The connection ID.
     */
    private String _connectionId;

    /**
     * The protocol version.
     */
    private String _protocolVersion;

    /**
     * A value indicating whether web sockets should be tried.
     */
    private boolean _tryWebSockets;

    /**
     * The keep-alive timeout in milliseconds.
     */
    private long _keepAliveTimeout;

    /**
     * The disconnect timeout in milliseconds.
     */
    private long _disconnectTimeout;

    /**
     * The transport connect timeout in milliseconds.
     */
    private long _connectTimeout;

    /**
     * Initializes a new instance of the {@link NegotiationResponse} class.
     */
    public NegotiationResponse() {
        _keepAliveTimeout = -1;
        _disconnectTimeout = -1;
        _connectTimeout = -1;
    }

    @Override
    public String getRelativeUrl() {
        return _relativeUrl;
    }

    @Override
    public String getConnectionToken() {
        return _connectionToken;
    }

    @Override
    public String getConnectionId() {
        return _connectionId;
    }

    @Override
    public String getProtocolVersion() {
        return _protocolVersion;
    }

    @Override
    public boolean getTryWebSockets() {
        return _tryWebSockets;
    }

    @Override
    public long getDisconnectTimeout() {
        return _disconnectTimeout;
    }

    @Override
    public long getKeepAliveTimeout() {
        return _keepAliveTimeout;
    }

    @Override
    public long getConnectTimeout() {
        return _connectTimeout;
    }

    @Override
    public void readJson(final JsonReader reader) {
        reader.readBeginObject();

        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("Url")) {
                _relativeUrl = reader.readString();
            } else if (name.equalsIgnoreCase("ConnectionToken")) {
                _connectionToken = reader.readString();
            } else if (name.equalsIgnoreCase("ConnectionId")) {
                _connectionId = reader.readString();
            } else if (name.equalsIgnoreCase("ProtocolVersion")) {
                _protocolVersion = reader.readString();
            } else if (name.equalsIgnoreCase("TryWebSockets")) {
                _tryWebSockets = reader.readBoolean();
            } else if (name.equalsIgnoreCase("KeepAliveTimeout")) {
                _keepAliveTimeout = (long) (reader.readDouble() * 1000L);
            } else if (name.equalsIgnoreCase("DisconnectTimeout")) {
                _disconnectTimeout = (long) (reader.readDouble() * 1000L);
            } else if (name.equalsIgnoreCase("TransportConnectTimeout")) {
                _connectTimeout = (long) (reader.readDouble() * 1000L);
            }
        }

        reader.readEndObject();
    }
}
