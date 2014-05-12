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

/**
 * Represents a hub exception.
 */
public final class HubException extends RuntimeException {

    /**
     * The serial version unique identifier.
     */
    private static final long serialVersionUID = 2962258989233090645L;

    /**
     * The error data.
     */
    private final String _data;

    /**
     * The remote stack trace.
     */
    private final String _remoteStackTrace;

    /**
     * Initializes a new instance of the {@link HubException} class.
     * 
     * @param message The error message.
     * @param data The error data.
     * @param remoteStackTrace The remote stack trace.
     */
    public HubException(final String message, final String data, final String remoteStackTrace) {
        super(message);

        _data = data;
        _remoteStackTrace = remoteStackTrace;
    }

    /**
     * Returns the error data.
     */
    public String getData() {
        return _data;
    }

    /**
     * Returns the remote stack trace.
     */
    public String getRemoteStackTrace() {
        return _remoteStackTrace;
    }
}
