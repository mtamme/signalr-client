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

package net.signalr.client;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import net.signalr.client.util.concurrent.promise.Promise;
import net.signalr.client.util.concurrent.promise.Promises;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public final class PersistentConnectionTest {

    @Mock
    private ConnectionContext _connectionContext;

    @Mock
    private ConnectionState _connectionState;

    @Test
    public void startTest() {
        // Arrange
        final PersistentConnection connection = new PersistentConnection(_connectionContext);

        expect(_connectionContext.getConnectionState()).andReturn(_connectionState);
        expect(_connectionState.connect(_connectionContext)).andReturn(Promises.newSuccess());
        replay(_connectionContext, _connectionState);

        // Act
        final Promise<Void> start = connection.start();

        // Assert
        verify(_connectionContext, _connectionState);
        assertTrue(start.isComplete());
    }

    @Test
    public void stopTest() {
        // Arrange
        final PersistentConnection connection = new PersistentConnection(_connectionContext);

        expect(_connectionContext.getConnectionState()).andReturn(_connectionState);
        expect(_connectionState.disconnect(_connectionContext)).andReturn(Promises.newSuccess());
        replay(_connectionContext, _connectionState);

        // Act
        final Promise<Void> stop = connection.stop();

        // Assert
        verify(_connectionContext, _connectionState);
        assertTrue(stop.isComplete());
    }

    @Test
    public void sendTest() {
        // Arrange
        final PersistentConnection connection = new PersistentConnection(_connectionContext);

        expect(_connectionContext.getConnectionState()).andReturn(_connectionState);
        expect(_connectionState.send(_connectionContext, "Message")).andReturn(Promises.newSuccess());
        replay(_connectionContext, _connectionState);

        // Act
        final Promise<Void> send = connection.send("Message");

        // Assert
        verify(_connectionContext, _connectionState);
        assertTrue(send.isComplete());
    }
}
