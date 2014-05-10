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
 * Defines a completion.
 * 
 * @param <T> The value type.
 */
public interface Completion<T> {

    /**
     * Completes the completion with the specified value.
     * 
     * @param value The value.
     * @throws IllegalStateException
     */
    void setSuccess(T value);

    /**
     * Completes the completion with the specified cause.
     * 
     * @param cause The cause.
     * @throws IllegalStateException
     */
    void setFailure(Throwable cause);
}
