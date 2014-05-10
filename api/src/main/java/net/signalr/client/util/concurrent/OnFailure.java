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
 * Represents a failure continuation.
 * 
 * @param <T> The value type.
 */
public abstract class OnFailure<T> implements Continuation<T, T> {

    /**
     * Handles the failure continuation.
     * 
     * @param cause The cause.
     * @throws Exception
     */
    protected abstract void onFailure(Throwable cause) throws Exception;

    @Override
    public final void setSuccess(final T value, final Deferred<? super T> result) throws Exception {
        result.setSuccess(value);
    }

    @Override
    public final void setFailure(final Throwable cause, final Deferred<? super T> result) throws Exception {
        onFailure(cause);
        result.setFailure(cause);
    }
}
