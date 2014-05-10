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
 * Represents a complete continuation.
 * 
 * @param <T> The value type.
 */
public abstract class OnComplete<T> implements Continuation<T, T> {

    /**
     * Handles the success continuation.
     * 
     * @param value The value.
     * @throws Exception
     */
    protected void onSuccess(final T value) throws Exception {
        onComplete(value, null);
    }

    /**
     * Handles the failure continuation.
     * 
     * @param cause The cause.
     * @throws Exception
     */
    protected void onFailure(final Throwable cause) throws Exception {
        onComplete(null, cause);
    }

    /**
     * Handles the complete continuation.
     * 
     * @param cause The cause.
     * @throws Exception
     */
    protected void onComplete(final T value, final Throwable cause) throws Exception {
    }

    @Override
    public final void setSuccess(final T value, final Deferred<? super T> result) throws Exception {
        onSuccess(value);
        result.setSuccess(value);
    }

    @Override
    public final void setFailure(final Throwable cause, final Deferred<? super T> result) throws Exception {
        onFailure(cause);
        result.setFailure(cause);
    }
}
