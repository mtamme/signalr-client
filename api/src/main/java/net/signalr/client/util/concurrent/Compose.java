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
 * Represents a compose continuation.
 * 
 * @param <T> The value type.
 * @param <U> The result type.
 */
public abstract class Compose<T, U> implements Continuation<T, U> {

    /**
     * Handles the compose continuation.
     * 
     * @param value The value.
     * @return The result.
     * @throws Exception
     */
    protected abstract Promise<U> doCompose(T value) throws Exception;

    @Override
    public final void setSuccess(final T value, final Deferred<? super U> result) throws Exception {
        final Promise<U> promise = doCompose(value);

        promise.then(result);
    }

    @Override
    public final void setFailure(final Throwable cause, final Deferred<? super U> result) throws Exception {
        result.setFailure(cause);
    }
}
