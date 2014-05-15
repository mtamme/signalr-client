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

import net.signalr.client.util.Lifecycle;

/**
 * Defines a transport manager.
 */
public interface TransportManager extends Lifecycle<TransportContext>, ChannelHandler {

    /**
     * Returns the transport.
     * 
     * @return The transport.
     */
    Transport getTransport();

    /**
     * Adds the specified transport listener.
     * 
     * @param listener The transport listener.
     */
    void addTransportListener(TransportListener listener);

    /**
     * Removes the specified transport listener.
     * 
     * @param listener The transport listener.
     */
    void removeTransportListener(TransportListener listener);

    /**
     * Invoked when the connection has been lost.
     */
    void handleConnectionLost();

    /**
     * Invoked when the connection is slow.
     */
    void handleConnectionSlow();
}