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
 * Represents a complete {@link Promise} callback.
 */
public abstract class OnCompleted<V> implements Callback<V> {

    /**
     * Invoked when the {@link Promise} was completed.
     * 
     * @param value The value.
     * @param cause The cause.
     */
    public abstract void onCompleted(V value, Throwable cause);

    @Override
    public final void onResolved(final V value) {
        onCompleted(value, null);
    }

    @Override
    public final void onRejected(final Throwable cause) {
        onCompleted(null, cause);
    }
}
