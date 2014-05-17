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
public final class PersistentResponseTest {

    private JsonMapper _mapper;

    @Before
    public void before() {
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
