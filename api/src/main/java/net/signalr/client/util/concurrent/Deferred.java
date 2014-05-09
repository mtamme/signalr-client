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

package net.signalr.client.util.concurrent;

/**
 * Defines a deferred value.
 * 
 * @param <T> The value type.
 */
public interface Deferred<T> extends Promise<T>, Completable<T> {

    /**
     * Tries to complete the deferred with the specified value.
     * 
     * @param value The value.
     * @return A value indicating whether the deferred has been completed.
     */
    boolean trySuccess(T value);

    /**
     * Tries to complete the deferred with the specified cause.
     * 
     * @param cause The cause.
     * @return A value indicating whether the deferred has been completed.
     */
    boolean tryFailure(Throwable cause);
}
