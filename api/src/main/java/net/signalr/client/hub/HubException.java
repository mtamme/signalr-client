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
