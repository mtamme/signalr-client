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
 * Represents an abstract {@link Promise}.
 * 
 * @param <V> The value type.
 */
public abstract class AbstractPromise<V> implements Promise<V> {

    @Override
    public final void thenCopy(final Deferred<V> deferred) {
        addCallback(new Callback<V>() {
            @Override
            public void onResolved(final V value) {
                deferred.resolve(value);
            }

            @Override
            public void onRejected(final Throwable cause) {
                deferred.reject(cause);
            }
        });
    }

    @Override
    public final void thenPropagate(final Deferred<Void> deferred) {
        addCallback(new Callback<V>() {
            @Override
            public void onResolved(final V value) {
                deferred.resolve(null);
            }

            @Override
            public void onRejected(final Throwable cause) {
                deferred.reject(cause);
            }
        });
    }

    @Override
    public final Promise<V> thenCall(final Callback<? super V> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback must not be null");
        }

        final Deferred<V> deferred = new Deferred<V>();

        addCallback(new Callback<V>() {
            @Override
            public void onResolved(final V value) {
                try {
                    callback.onResolved(value);
                    deferred.resolve(value);
                } catch (final Throwable t) {
                    deferred.reject(t);
                }
            }

            @Override
            public void onRejected(final Throwable cause) {
                try {
                    callback.onRejected(cause);
                    deferred.reject(cause);
                } catch (final Throwable t) {
                    t.addSuppressed(cause);
                    deferred.reject(t);
                }
            }
        });

        return deferred;
    }

    @Override
    public final <R> Promise<R> thenApply(final Function<? super V, ? extends R> function) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }

        final Deferred<R> deferred = new Deferred<R>();

        addCallback(new Callback<V>() {
            @Override
            public void onResolved(final V value) {
                try {
                    final R result = function.apply(value);

                    deferred.resolve(result);
                } catch (final Throwable t) {
                    deferred.reject(t);
                }
            }

            @Override
            public void onRejected(final Throwable cause) {
                deferred.reject(cause);
            }
        });

        return deferred;
    }

    @Override
    public final <R> Promise<R> thenCompose(final Function<? super V, ? extends Promise<R>> function) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }

        final Deferred<R> deferred = new Deferred<R>();

        addCallback(new Callback<V>() {
            @Override
            public void onResolved(final V value) {
                try {
                    final Promise<R> result = function.apply(value);

                    result.thenCopy(deferred);
                } catch (final Throwable t) {
                    deferred.reject(t);
                }
            }

            @Override
            public void onRejected(final Throwable cause) {
                deferred.reject(cause);
            }
        });

        return deferred;
    }
}
