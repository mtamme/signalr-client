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

import java.util.concurrent.Executor;

/**
 * Defines a promised value.
 * 
 * @param <T> The value type.
 */
public interface Promise<T> {

    /**
     * Returns a value indicating whether the promise is complete.
     * 
     * @return A value indicating whether the promise is complete.
     */
    boolean isComplete();

    /**
     * Adds the specified completion.
     * 
     * @param completion The completion.
     */
    void then(Completion<? super T> completion);

    /**
     * Adds the specified completion.
     * 
     * @param completion The completion.
     * @param executor The executor.
     */
    void then(Completion<? super T> completion, Executor executor);

    /**
     * Adds the specified continuation.
     * 
     * @param continuation The continuation.
     * @return The result.
     */
    <R> Promise<R> then(Continuation<? super T, ? extends R> continuation);

    /**
     * Adds the specified continuation.
     * 
     * @param continuation The continuation.
     * @param executor The executor.
     * @return The result.
     */
    <R> Promise<R> then(Continuation<? super T, ? extends R> continuation, Executor executor);
}
