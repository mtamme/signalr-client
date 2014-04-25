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
     * The keep-alive timeout in seconds.
     */
    private Double _keepAliveTimeout;

    /**
     * The disconnect timeout in seconds.
     */
    private Double _disconnectTimeout;

    /**
     * The transport connect timeout in seconds.
     */
    private Double _connectTimeout;

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
        return (_disconnectTimeout != null) ? _disconnectTimeout.longValue() : -1L;
    }

    @Override
    public long getKeepAliveTimeout() {
        return (_keepAliveTimeout != null) ? _keepAliveTimeout.longValue() : -1L;
    }

    @Override
    public long getConnectTimeout() {
        return (_connectTimeout != null) ? _connectTimeout.longValue() : -1L;
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
                _keepAliveTimeout = reader.readDouble();
            } else if (name.equalsIgnoreCase("DisconnectTimeout")) {
                _disconnectTimeout = reader.readDouble();
            } else if (name.equalsIgnoreCase("TransportConnectTimeout")) {
                _connectTimeout = reader.readDouble();
            }
        }

        reader.readEndObject();
    }
}
