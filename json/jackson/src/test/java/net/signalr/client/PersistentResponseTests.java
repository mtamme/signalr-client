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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.signalr.client.json.DefaultJsonMapper;
import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonMapper;
import net.signalr.client.json.jackson.JacksonFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class PersistentResponseTests {

    private JsonMapper _mapper;

    @Before
    public void setUp() {
        final JsonFactory factory = new JacksonFactory();

        _mapper = new DefaultJsonMapper(factory);
    }

    @Test
    public void deserializeInitializationResponseTest() {
        // Arrange
        final String data = "{\"C\":\"s-0,298F386\",\"S\":1,\"M\":[]}";

        // Act
        final PersistentResponse response = _mapper.toObject(data, PersistentResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getMessageId(), is("s-0,298F386"));
        assertThat(response.isInitialize(), is(true));
    }

    @Test
    public void deserializeGroupTokenResponseTest() {
        // Arrange
        final String data = "{\"C\":\"s-0,298F388\",\"G\":\"jFN2mJ5rvg9vPfwkBxM1YlE6xggh6C+h+RfCKioW0uJpH0vg3bL40vD2e4p8Ncr4vsrTxzqDKN7zBqCUclpqEgzuJRwG/mKifZrTcxdLez2DMF8ZmGTi0/N6vBju1XQVGnMj3HpOKDieWe8ifbFTL89lIFg=\",\"M\":[]}";

        // Act
        final PersistentResponse response = _mapper.toObject(data, PersistentResponse.class);

        // Assert
        assertNotNull(response);
        assertThat(response.getMessageId(), is("s-0,298F388"));
        assertThat(response.isInitialize(), is(false));
        assertThat(response.getGroupsToken(),
                is("jFN2mJ5rvg9vPfwkBxM1YlE6xggh6C+h+RfCKioW0uJpH0vg3bL40vD2e4p8Ncr4vsrTxzqDKN7zBqCUclpqEgzuJRwG/mKifZrTcxdLez2DMF8ZmGTi0/N6vBju1XQVGnMj3HpOKDieWe8ifbFTL89lIFg="));
    }
}
