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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class PromisesTest {

    @Test
    public void newSuccessTest() {
        // Arrange
        // Act
        final Promise<Integer> promise = Promises.newSuccess(1);

        // Assert
        assertNotNull(promise);
        assertTrue(promise.isComplete());
    }

    @Test
    public void newFailureTest() {
        // Arrange
        // Act
        final Promise<Integer> promise = Promises.newFailure(new Throwable());

        // Assert
        assertNotNull(promise);
        assertTrue(promise.isComplete());
    }

    @Test
    public void thenWithNewSuccessTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completion<Integer> completion = createStrictMock(Completion.class);

        completion.setSuccess(1);
        replay(completion);
        final Promise<Integer> promise = Promises.newSuccess(1);

        // Act
        promise.then(completion);

        // Assert
        verify(completion);
    }

    @Test
    public void thenWithNewFailureTest() {
        // Arrange
        @SuppressWarnings("unchecked")
        final Completion<Integer> completion = createStrictMock(Completion.class);
        final Throwable cause = new Throwable();

        completion.setFailure(cause);
        replay(completion);
        final Promise<Integer> promise = Promises.newFailure(cause);

        // Act
        promise.then(completion);

        // Assert
        verify(completion);
    }

    @Test
    public void getWithNewSuccessAndToFutureTest() throws InterruptedException, ExecutionException {
        // Arrange
        final Promise<Integer> promise = Promises.newSuccess(1);
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        assertThat(future.get(), is(1));
    }

    @Test(expected = ExecutionException.class)
    public void getWithNewFailureAndToFutureTest() throws InterruptedException, ExecutionException {
        // Arrange
        final Promise<Integer> promise = Promises.newFailure(new Throwable());
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        future.get();
    }

    @Test
    public void getWithNewSuccessAndToFutureAndTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        final Promise<Integer> promise = Promises.newSuccess(1);
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        assertThat(future.get(0, TimeUnit.MICROSECONDS), is(1));
    }

    @Test(expected = ExecutionException.class)
    public void getWithNewFailureAndToFutureAndTimeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        final Promise<Integer> promise = Promises.newFailure(new Throwable());
        final Future<Integer> future = Promises.toFuture(promise);

        // Act
        // Assert
        assertTrue(future.isDone());
        assertFalse(future.isCancelled());
        future.get(0, TimeUnit.MICROSECONDS);
    }
}
