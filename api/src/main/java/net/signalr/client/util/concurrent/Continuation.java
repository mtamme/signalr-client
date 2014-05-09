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
 * Defines a continuation.
 * 
 * @param <T> The value type.
 * @param <U> The result type.
 */
public interface Continuation<T, U> {

    /**
     * Completes the continuation with the specified value.
     * 
     * @param value The value.
     * @param result The result.
     * @throws Exception
     */
    void setSuccess(T value, Deferred<U> result) throws Exception;

    /**
     * Completes the continuation with the specified cause.
     * 
     * @param cause The cause.
     * @param result The result.
     * @throws Exception
     */
    void setFailure(Throwable cause, Deferred<U> result) throws Exception;
}
