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
public final class DeferredTests {

    @Test
    public void constructorTest() {
        // Arrange
        // Act
        final Deferred<Integer> deferred = new Deferred<Integer>();

        // Assert
        assertFalse(deferred.isCompleted());
    }

    @Test
    public void resolveTest() {
        // Arrange
        final Deferred<Integer> deferred = new Deferred<Integer>();

        // Act
        final boolean resolved = deferred.resolve(1);

        // Assert
        assertTrue(resolved);
        assertTrue(deferred.isCompleted());
    }

    @Test
    public void resolveWithRejectedTest() {
        // Arrange
        final Deferred<Integer> deferred = new Deferred<Integer>();

        // Act
        deferred.reject(new Throwable());
        final boolean resolved = deferred.resolve(1);

        // Assert
        assertFalse(resolved);
        assertTrue(deferred.isCompleted());
    }

    @Test
    public void resolveWithAddCallbackTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        callback.onResolved(1);
        replay(callback);
        final Deferred<Integer> deferred = new Deferred<Integer>();

        deferred.addCallback(callback);

        // Act
        final boolean resolved = deferred.resolve(1);

        // Assert
        verify(callback);
        assertTrue(resolved);
        assertTrue(deferred.isCompleted());
    }

    @Test
    public void rejectTest() {
        // Arrange
        final Deferred<Integer> deferred = new Deferred<Integer>();

        // Act
        final boolean rejected = deferred.reject(new Throwable());

        // Assert
        assertTrue(rejected);
        assertTrue(deferred.isCompleted());
    }

    @Test
    public void rejectWithResolvedTest() {
        // Arrange
        final Deferred<Integer> deferred = new Deferred<Integer>();

        // Act
        deferred.resolve(1);
        final boolean rejected = deferred.reject(new Throwable());

        // Assert
        assertFalse(rejected);
        assertTrue(deferred.isCompleted());
    }

    @Test
    public void rejectWithAddCallbackTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);
        final Throwable cause = new Throwable();

        callback.onRejected(cause);
        replay(callback);
        final Deferred<Integer> deferred = new Deferred<Integer>();

        deferred.addCallback(callback);

        // Act
        final boolean rejected = deferred.reject(cause);

        // Assert
        verify(callback);
        assertTrue(rejected);
        assertTrue(deferred.isCompleted());
    }

    @Test
    public void addCallbackWithPendingTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        replay(callback);
        final Deferred<Integer> deferred = new Deferred<Integer>();

        // Act
        deferred.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithResolvedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);

        callback.onResolved(1);
        replay(callback);
        final Deferred<Integer> deferred = new Deferred<Integer>();

        deferred.resolve(1);

        // Act
        deferred.addCallback(callback);

        // Assert
        verify(callback);
    }

    @Test
    public void addCallbackWithRejectedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Callback<Integer> callback = (Callback<Integer>) createStrictMock(Callback.class);
        final Throwable cause = new Throwable();

        callback.onRejected(cause);
        replay(callback);
        final Deferred<Integer> deferred = new Deferred<Integer>();

        deferred.reject(cause);

        // Act
        deferred.addCallback(callback);

        // Assert
        verify(callback);
    }
}
