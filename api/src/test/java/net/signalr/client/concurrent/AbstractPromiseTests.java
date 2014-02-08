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

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class AbstractPromiseTests {

    @Test
    public void addCallbackWithResolvedAndThenCallTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        callback.onResolved(1);
        replay(callback);
        final Promise<Integer> promise = Promises.resolved(1);
        final Promise<Integer> result = promise.thenCall(new Callback<Integer>() {
            @Override
            public void onResolved(final Integer value) {
            }

            @Override
            public void onRejected(final Throwable throwable) {
            }
        });

        // Act
        result.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithRejectedAndThenCallTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);
        final Throwable throwable = new Throwable();

        callback.onRejected(throwable);
        replay(callback);
        final Promise<Integer> promise = Promises.rejected(throwable);
        final Promise<Integer> result = promise.thenCall(new Callback<Integer>() {
            @Override
            public void onResolved(final Integer value) {
            }

            @Override
            public void onRejected(final Throwable throwable) {
            }
        });

        // Act
        result.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithResolvedAndThenApplyTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<String> callback = (Callback<String>) createStrictMock(Callback.class);

        callback.onResolved("1");
        replay(callback);
        final Promise<Integer> promise = Promises.resolved(1);
        final Promise<String> result = promise.thenApply(new Function<Integer, String>() {
            @Override
            public String apply(final Integer value) throws Exception {
                return value.toString();
            }
        });

        // Act
        result.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithRejectedAndThenApplyTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<String> callback = (Callback<String>) createStrictMock(Callback.class);
        final Throwable throwable = new Throwable();

        callback.onRejected(throwable);
        replay(callback);
        final Promise<Integer> promise = Promises.rejected(throwable);
        final Promise<String> result = promise.thenApply(new Function<Integer, String>() {
            @Override
            public String apply(final Integer value) throws Exception {
                return value.toString();
            }
        });

        // Act
        result.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithResolvedAndThenComposeTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<String> callback = (Callback<String>) createStrictMock(Callback.class);

        callback.onResolved("1");
        replay(callback);
        final Promise<Integer> promise = Promises.resolved(1);
        final Promise<String> result = promise.thenCompose(new Function<Integer, Promise<String>>() {
            @Override
            public Promise<String> apply(final Integer value) throws Exception {
                return Promises.resolved(value.toString());
            }
        });

        // Act
        result.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithRejectedAndThenComposeTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<String> callback = (Callback<String>) createStrictMock(Callback.class);
        final Throwable throwable = new Throwable();

        callback.onRejected(throwable);
        replay(callback);
        final Promise<Integer> promise = Promises.rejected(throwable);
        final Promise<String> result = promise.thenCompose(new Function<Integer, Promise<String>>() {
            @Override
            public Promise<String> apply(final Integer value) throws Exception {
                return Promises.resolved(value.toString());
            }
        });

        // Act
        result.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void thenCallWithResolvedTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        callback.onResolved(1);
        replay(callback);
        final Promise<Integer> promise = Promises.resolved(1);

        // Act
        final Promise<Integer> result = promise.thenCall(callback);

        // Assert
        verify(callback);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenCallWithRejectedTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);
        final Throwable throwable = new Throwable();

        callback.onRejected(throwable);
        replay(callback);
        final Promise<Integer> promise = Promises.rejected(throwable);

        // Act
        final Promise<Integer> result = promise.thenCall(callback);

        // Assert
        verify(callback);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenCallWithResolvedAndRuntimeExceptionTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        callback.onResolved(anyInt());
        expectLastCall().andThrow(new RuntimeException());
        replay(callback);
        final Promise<Integer> promise = Promises.resolved(1);

        // Act
        final Promise<Integer> result = promise.thenCall(callback);

        // Assert
        verify(callback);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenCallWithRejectedAndRuntimeExceptionTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);
        final Throwable throwable = new Throwable();

        callback.onRejected(throwable);
        expectLastCall().andThrow(new RuntimeException());
        replay(callback);
        final Promise<Integer> promise = Promises.rejected(throwable);

        // Act
        final Promise<Integer> result = promise.thenCall(callback);

        // Assert
        verify(callback);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenApplyWithResolvedTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Function<Integer, String> function = (Function<Integer, String>) createStrictMock(Function.class);

        expect(function.apply(1)).andReturn("1");
        replay(function);
        final Promise<Integer> promise = Promises.resolved(1);

        // Act
        final Promise<String> result = promise.thenApply(function);

        // Assert
        verify(function);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenApplyWithRejectedTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Function<Integer, String> function = (Function<Integer, String>) createStrictMock(Function.class);

        replay(function);
        final Throwable throwable = new Throwable();
        final Promise<Integer> promise = Promises.rejected(throwable);

        // Act
        final Promise<String> result = promise.thenApply(function);

        // Assert
        verify(function);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenApplyWithResolvedAndExceptionTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Function<Integer, String> function = (Function<Integer, String>) createStrictMock(Function.class);

        expect(function.apply(anyInt())).andThrow(new Exception());
        replay(function);
        final Promise<Integer> promise = Promises.resolved(1);

        // Act
        final Promise<String> result = promise.thenApply(function);

        // Assert
        verify(function);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenApplyWithRejectedAndExceptionTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Function<Integer, String> function = (Function<Integer, String>) createStrictMock(Function.class);

        replay(function);
        final Throwable throwable = new Throwable();
        final Promise<Integer> promise = Promises.rejected(throwable);

        // Act
        final Promise<String> result = promise.thenApply(function);

        // Assert
        verify(function);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenComposeWithResolvedTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Function<Integer, Promise<String>> function = (Function<Integer, Promise<String>>) createStrictMock(Function.class);

        expect(function.apply(1)).andReturn(Promises.resolved("1"));
        replay(function);
        final Promise<Integer> promise = Promises.resolved(1);

        // Act
        final Promise<String> result = promise.thenCompose(function);

        // Assert
        verify(function);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenComposeWithRejectedTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Function<Integer, Promise<String>> function = (Function<Integer, Promise<String>>) createStrictMock(Function.class);

        replay(function);
        final Throwable throwable = new Throwable();
        final Promise<Integer> promise = Promises.rejected(throwable);

        // Act
        final Promise<String> result = promise.thenCompose(function);

        // Assert
        verify(function);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenComposeWithResolvedAndExceptionTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Function<Integer, Promise<String>> function = (Function<Integer, Promise<String>>) createStrictMock(Function.class);

        expect(function.apply(1)).andThrow(new Exception());
        replay(function);
        final Promise<Integer> promise = Promises.resolved(1);

        // Act
        final Promise<String> result = promise.thenCompose(function);

        // Assert
        verify(function);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }

    @Test
    public void thenComposeWithRejectedAndExceptionTest() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        final Function<Integer, Promise<String>> function = (Function<Integer, Promise<String>>) createStrictMock(Function.class);

        replay(function);
        final Throwable throwable = new Throwable();
        final Promise<Integer> promise = Promises.rejected(throwable);

        // Act
        final Promise<String> result = promise.thenCompose(function);

        // Assert
        verify(function);
        assertNotNull(result);
        assertTrue(result.isCompleted());
    }
}
