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
 * Represents a system time provider.
 */
public final class SystemTimeProvider implements TimeProvider {

    /**
     * Returns the singleton instance.
     */
    public static final SystemTimeProvider INSTANCE = new SystemTimeProvider();

    /**
     * Initializes a new instance of the {@link SystemTimeProvider} class.
     */
    private SystemTimeProvider() {
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}