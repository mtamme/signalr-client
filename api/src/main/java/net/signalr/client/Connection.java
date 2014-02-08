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

package net.signalr.client;

import net.signalr.client.concurrent.Promise;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.transports.Transport;

/**
 * Defines a connection.
 */
public interface Connection {

    String getProtocolVersion();

    String getUrl();

    Transport getTransport();

    JsonSerializer getSerializer();

    boolean isConnected();

    void addHeader(String name, String value);

    void addQueryParameter(String name, String value);

    void setConnectionData(String connectionData);

    Promise<Void> start(ConnectionHandler handler);

    Promise<Void> stop();

    Promise<Void> send(String message);

}