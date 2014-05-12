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

package net.signalr.client.util.concurrent;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DefaultDeferredTests {

    @Test
    public void constructorTest() {
        // Arrange
        // Act
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Assert
        assertFalse(deferred.isComplete());
    }

    @Test
    public void trySuccessTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        final boolean success = deferred.trySuccess(1);

        // Assert
        assertTrue(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void trySuccessWithSetFailureTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.setFailure(new Throwable());
        final boolean success = deferred.trySuccess(1);

        // Assert
        assertFalse(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void trySuccessWithThenTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completion<Integer> completion = createStrictMock(Completion.class);

        completion.setSuccess(1);
        replay(completion);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.then(completion);

        // Act
        final boolean success = deferred.trySuccess(1);

        // Assert
        verify(completion);
        assertTrue(success);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        final boolean failure = deferred.tryFailure(new Throwable());

        // Assert
        assertTrue(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureWithSetSuccessTest() {
        // Arrange
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.setSuccess(1);
        final boolean failure = deferred.tryFailure(new Throwable());

        // Assert
        assertFalse(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void setFailureWithThenTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completion<Integer> completion = createStrictMock(Completion.class);
        final Throwable cause = new Throwable();

        completion.setFailure(cause);
        replay(completion);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.then(completion);

        // Act
        final boolean failure = deferred.tryFailure(cause);

        // Assert
        verify(completion);
        assertTrue(failure);
        assertTrue(deferred.isComplete());
    }

    @Test
    public void thenTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completion<Integer> completion = createStrictMock(Completion.class);

        replay(completion);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        // Act
        deferred.then(completion);

        // Assert
        verify(completion);
    }

    @Test
    public void thenWithSetSuccessTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completion<Integer> completion = createStrictMock(Completion.class);

        completion.setSuccess(1);
        replay(completion);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.setSuccess(1);

        // Act
        deferred.then(completion);

        // Assert
        verify(completion);
    }

    @Test
    public void thenWithSetFailureTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completion<Integer> completion = createStrictMock(Completion.class);
        final Throwable cause = new Throwable();

        completion.setFailure(cause);
        replay(completion);
        final Deferred<Integer> deferred = new DefaultDeferred<Integer>();

        deferred.setFailure(cause);

        // Act
        deferred.then(completion);

        // Assert
        verify(completion);
    }
}
