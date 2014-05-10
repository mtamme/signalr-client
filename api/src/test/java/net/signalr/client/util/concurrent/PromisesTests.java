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
public final class PromisesTests {

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
