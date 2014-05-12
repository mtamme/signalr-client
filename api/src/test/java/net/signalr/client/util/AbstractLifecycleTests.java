/*
 * Copyright © Martin Tamme
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
        LifecycleException exception = null;

        try {
            lifecycle.start(1);
        } catch (final LifecycleException e) {
            exception = e;
        }

        // Assert
        verify(lifecycle);
        assertNotNull(exception);
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
        LifecycleException exception = null;

        try {
            lifecycle.stop(2);
        } catch (final LifecycleException e) {
            exception = e;
        }

        // Assert
        verify(lifecycle);
        assertNotNull(exception);
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
