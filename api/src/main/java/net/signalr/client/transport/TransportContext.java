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

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executor;

import net.signalr.client.json.JsonMapper;
import net.signalr.client.util.concurrent.Scheduler;

/**
 * Defines a transport context.
 */
public interface TransportContext {

    /**
     * Returns the protocol version.
     * 
     * @return The protocol version.
     */
    String getProtocolVersion();

    /**
     * Returns the URL.
     * 
     * @return The URL.
     */
    String getUrl();

    /**
     * Returns the executor.
     * 
     * @return The executor.
     */
    Executor getExecutor();

    /**
     * Returns the scheduler.
     * 
     * @return The scheduler.
     */
    Scheduler getScheduler();

    /**
     * Returns the mapper.
     * 
     * @return The mapper.
     */
    JsonMapper getMapper();

    /**
     * Returns the headers.
     * 
     * @return The headers.
     */
    Map<String, Collection<String>> getHeaders();

    /**
     * Returns the parameters.
     * 
     * @return The parameters.
     */
    Map<String, Collection<String>> getParameters();

    /**
     * Returns the connection data.
     * 
     * @return The connection data.
     */
    String getConnectionData();

    /**
     * Returns the transport options.
     * 
     * @return The transport options.
     */
    TransportOptions getTransportOptions();
}