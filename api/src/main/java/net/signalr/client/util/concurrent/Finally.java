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
 * Represents a finally continuation.
 * 
 * @param <T> The value type.
 * @param <U> The result type.
 */
public abstract class Finally<T, U> implements Continuation<T, U> {

    /**
     * Handles the finally continuation.
     * 
     * @param value The value.
     * @param cause The cause.
     * @return The result.
     * @throws Exception
     */
    protected abstract U doFinally(T value, Throwable cause) throws Exception;

    @Override
    public final void setSuccess(final T value, final Deferred<? super U> result) throws Exception {
        final U newValue = doFinally(value, null);

        result.setSuccess(newValue);
    }

    @Override
    public final void setFailure(final Throwable cause, final Deferred<? super U> result) throws Exception {
        final U newValue = doFinally(null, cause);

        result.setSuccess(newValue);
    }
}
