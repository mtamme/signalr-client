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

package net.signalr.client;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public final class PersistentConnectionTests {

    @Mock
    private ConnectionContext _connectionContext;

    @Mock
    private ConnectionState _connectionState;

    @Mock
    private ConnectionHandler _connectionHandler;

    @Test
    public void startTest() {
        // Arrange
        final PersistentConnection connection = new PersistentConnection(_connectionContext);

        expect(_connectionContext.getState()).andReturn(_connectionState);
        expect(_connectionState.start(_connectionContext, _connectionHandler)).andReturn(Promises.newSuccess());
        replay(_connectionContext, _connectionState, _connectionHandler);

        // Act
        final Promise<Void> start = connection.start(_connectionHandler);

        // Assert
        verify(_connectionContext, _connectionState, _connectionHandler);
        assertTrue(start.isComplete());
    }

    @Test
    public void stopTest() {
        // Arrange
        final PersistentConnection connection = new PersistentConnection(_connectionContext);

        expect(_connectionContext.getState()).andReturn(_connectionState);
        expect(_connectionState.stop(_connectionContext)).andReturn(Promises.newSuccess());
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

        expect(_connectionContext.getState()).andReturn(_connectionState);
        expect(_connectionState.send(_connectionContext, "Message")).andReturn(Promises.newSuccess());
        replay(_connectionContext, _connectionState);

        // Act
        final Promise<Void> send = connection.send("Message");

        // Assert
        verify(_connectionContext, _connectionState);
        assertTrue(send.isComplete());
    }
}
