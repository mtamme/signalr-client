/*
 * Copyright 2014 Martin Tamme
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