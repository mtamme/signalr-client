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

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class URIBuilderTests {

    @Test
    public void constructorTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("http://signalr.net");

        // Act
        // Assert
        assertEquals("http", uriBuilder.getScheme());
        assertEquals("signalr.net", uriBuilder.getHost());
        assertEquals(-1, uriBuilder.getPort());
        assertEquals("", uriBuilder.getPath());
        assertEquals(null, uriBuilder.getQuery());
        assertEquals(null, uriBuilder.getFragment());
    }
}
