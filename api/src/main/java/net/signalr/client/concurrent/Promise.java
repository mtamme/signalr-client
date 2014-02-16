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

package net.signalr.client.concurrent;

/**
 * Defines a promise.
 * 
 * @param <V> The value type.
 */
public interface Promise<V> {

    /**
     * Returns a value indicating whether the {@link Promise} completed.
     * 
     * @return A value indicating whether the {@link Promise} completed.
     */
    boolean isCompleted();

    /**
     * Adds the specified {@link Callback}.
     * 
     * @param callback The callback.
     */
    void addCallback(Callback<? super V> callback);

    /**
     * Completes the specified {@link Deferred} when this {@link Promise} completes.
     * 
     * @param deferred The deferred.
     */
    void thenCopy(Deferred<V> deferred);

    /**
     * Completes the specified {@link Deferred} when this {@link Promise} completes.
     * 
     * @param deferred The deferred.
     */
    void thenPropagate(Deferred<Void> deferred);

    /**
     * Returns a new {@link Promise} that, when this {@link Promise} completes, is executed with this {@link Promise}'s result as the argument to the supplied callback.
     * 
     * @param callback The callback.
     * @return The new {@link Promise}.
     */
    Promise<V> thenCall(Callback<? super V> callback);

    /**
     * Returns a new {@link Promise} that, when this {@link Promise} completes normally, is executed with this {@link Promise}'s result as the argument to the supplied function.
     * 
     * @param function The function.
     * @return The new {@link Promise}.
     */
    <R> Promise<R> thenApply(Function<? super V, ? extends R> function);

    /**
     * Returns a new {@link Promise} that, when this {@link Promise} completes normally, is executed with this {@link Promise}' result as the argument to the supplied function.
     * 
     * @param function The function.
     * @return The new {@link Promise}.
     */
    <R> Promise<R> thenCompose(Function<? super V, ? extends Promise<R>> function);
}
