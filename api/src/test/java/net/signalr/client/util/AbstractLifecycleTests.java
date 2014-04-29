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

package net.signalr.client.util;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class AbstractLifecycleTests {

    @Test
    public void startTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final AbstractLifecycle<Integer> lifecycle = createMockBuilder(AbstractLifecycle.class).withConstructor().createStrictMock();

        lifecycle.doStart(1);
        replay(lifecycle);

        // Act
        lifecycle.start(1);

        // Assert
        verify(lifecycle);
        assertTrue(lifecycle.isRunning());
    }

    @Test
    public void startWithRuntimeExceptionTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final AbstractLifecycle<Integer> lifecycle = createMockBuilder(AbstractLifecycle.class).withConstructor().createStrictMock();

        lifecycle.doStart(1);
        expectLastCall().andThrow(new RuntimeException());
        replay(lifecycle);

        // Act
        lifecycle.start(1);

        // Assert
        verify(lifecycle);
        assertFalse(lifecycle.isRunning());
    }

    @Test
    public void startWithStartedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final AbstractLifecycle<Integer> lifecycle = createMockBuilder(AbstractLifecycle.class).withConstructor().createStrictMock();

        lifecycle.doStart(1);
        replay(lifecycle);
        lifecycle.start(1);

        // Act
        lifecycle.start(2);

        // Assert
        verify(lifecycle);
        assertTrue(lifecycle.isRunning());
    }

    @Test
    public void startWithStoppedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final AbstractLifecycle<Integer> lifecycle = createMockBuilder(AbstractLifecycle.class).withConstructor().createStrictMock();

        lifecycle.doStart(1);
        lifecycle.doStop(2);
        lifecycle.doStart(3);
        replay(lifecycle);
        lifecycle.start(1);
        lifecycle.stop(2);

        // Act
        lifecycle.start(3);

        // Assert
        verify(lifecycle);
        assertTrue(lifecycle.isRunning());
    }

    @Test
    public void stopTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final AbstractLifecycle<Integer> lifecycle = createMockBuilder(AbstractLifecycle.class).withConstructor().createStrictMock();

        replay(lifecycle);

        // Act
        lifecycle.stop(1);

        // Assert
        verify(lifecycle);
        assertFalse(lifecycle.isRunning());
    }

    @Test
    public void stopWithRuntimeExceptionTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final AbstractLifecycle<Integer> lifecycle = createMockBuilder(AbstractLifecycle.class).withConstructor().createStrictMock();

        lifecycle.doStart(1);
        lifecycle.doStop(2);
        expectLastCall().andThrow(new RuntimeException());
        replay(lifecycle);
        lifecycle.start(1);

        // Act
        lifecycle.stop(2);

        // Assert
        verify(lifecycle);
        assertFalse(lifecycle.isRunning());
    }

    @Test
    public void stopWithStartedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final AbstractLifecycle<Integer> lifecycle = createMockBuilder(AbstractLifecycle.class).withConstructor().createStrictMock();

        lifecycle.doStart(1);
        lifecycle.doStop(2);
        replay(lifecycle);
        lifecycle.start(1);

        // Act
        lifecycle.stop(2);

        // Assert
        verify(lifecycle);
        assertFalse(lifecycle.isRunning());
    }

    @Test
    public void stopWithStoppedTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final AbstractLifecycle<Integer> lifecycle = createMockBuilder(AbstractLifecycle.class).withConstructor().createStrictMock();

        replay(lifecycle);

        // Act
        lifecycle.stop(1);

        // Assert
        verify(lifecycle);
        assertFalse(lifecycle.isRunning());
    }
}
