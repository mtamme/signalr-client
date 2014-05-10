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

package net.signalr.client.util;

/**
 * Represents a lifecycle exception.
 */
public final class LifecycleException extends RuntimeException {

    /**
     * The serial version unique identifier.
     */
    private static final long serialVersionUID = -7493776115808074215L;

    /**
     * Initializes a new instance of the {@link LifecycleException} class.
     * 
     * @param message The message.
     */
    public LifecycleException(final String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@link LifecycleException} class.
     * 
     * @param message The message.
     * @param cause The cause.
     */
    public LifecycleException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
