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

/**
 * Represents a transport exception.
 */
public final class TransportException extends RuntimeException {

    /**
     * The serial version unique identifier.
     */
    private static final long serialVersionUID = 296020295810180406L;

    /**
     * Initializes a new instance of the {@link TransportException} class.
     * 
     * @param message The message.
     */
    public TransportException(final String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@link TransportException} class.
     * 
     * @param message The message.
     * @param cause The cause.
     */
    public TransportException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
