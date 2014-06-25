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
import net.signalr.client.util.concurrent.promise.Promise;

/**
 * Defines a transport.
 */
public interface Transport extends Lifecycle<TransportContext> {

    /**
     * Returns the transport name.
     * 
     * @return the transport name.
     */
    String getName();

    /**
     * Negotiates the transport.
     * 
     * @param context The transport context.
     * @return The negotiation response.
     */
    Promise<NegotiationResponse> negotiate(TransportContext context);

    /**
     * Connects the transport.
     * 
     * @param context The transport context.
     * @param handler The channel handler.
     * @param reconnect A value indicating whether to reconnect.
     * @return The channel.
     */
    Promise<Channel> connect(TransportContext context, ChannelHandler handler, boolean reconnect);

    /**
     * Performs a transport ping.
     * 
     * @param context The transport context.
     * @return The ping response.
     */
    Promise<PingResponse> ping(TransportContext context);

    /**
     * Aborts the transport.
     * 
     * @param context The transport context.
     * @return The abort result.
     */
    Promise<Void> abort(TransportContext context);
}
