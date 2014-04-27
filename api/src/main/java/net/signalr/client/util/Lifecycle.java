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
 * Defines a lifecycle.
 */
public interface Lifecycle<T> {

    /**
     * Returns a value indicating whether the lifecycle has been started.
     * 
     * @return A value indicating whether the lifecycle has been started.
     */
    boolean isStarted();

    /**
     * Starts the lifecycle.
     * 
     * @param context The lifecycle context.
     */
    void start(T context);

    /**
     * Stops the lifecycle.
     * 
     * @param context The lifecycle context.
     */
    void stop(T context);
}
