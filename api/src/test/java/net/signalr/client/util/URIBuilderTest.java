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

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class URIBuilderTest {

    @Test
    public void resolveTest() {
        // Arrange
        // Act
        final URIBuilder uriBuilder = URIBuilder.resolve("scheme://user@host:8000/path?a=1#fragment", "new-path?b=2#new-fragment");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals("//user@host:8000/new-path?b=2", uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/new-path", uriBuilder.getRawPath());
        assertEquals("b=2", uriBuilder.getRawQuery());
        assertEquals("new-fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void resolveWithPathSegmentTest() {
        // Arrange
        // Act
        final URIBuilder uriBuilder = URIBuilder.resolve("scheme://user@host:8000/segment/?a=1#fragment", "new-segment?b=2#new-fragment");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals("//user@host:8000/segment/new-segment?b=2", uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/segment/new-segment", uriBuilder.getRawPath());
        assertEquals("b=2", uriBuilder.getRawQuery());
        assertEquals("new-fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void constructorWithUriTest() {
        // Arrange
        // Act
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals("//user@host:8000/path?a=1", uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setSchemeTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setScheme("new-scheme");

        // Assert
        assertEquals("new-scheme", uriBuilder.getScheme());
        assertEquals("//user@host:8000/path?a=1", uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setSchemeWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setScheme("new-scheme");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("new-scheme://user@host:8000/path?a=1#fragment", uri.toString());
    }

    @Test
    public void setRawSchemeSpecificPartTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawSchemeSpecificPart("//new-user@new-host:8001/new-path?b=2");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals("//new-user@new-host:8001/new-path?b=2", uriBuilder.getRawSchemeSpecificPart());
        assertEquals(null, uriBuilder.getRawAuthority());
        assertEquals(null, uriBuilder.getRawUserInfo());
        assertEquals(null, uriBuilder.getHost());
        assertEquals(-1, uriBuilder.getPort());
        assertEquals(null, uriBuilder.getRawPath());
        assertEquals(null, uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setRawSchemeSpecificPartWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawSchemeSpecificPart("//new-user@new-host:8001/new-path?b=2");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://new-user@new-host:8001/new-path?b=2#fragment", uri.toString());
    }

    @Test
    public void setRawAuthorityTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawAuthority("new-user@new-host:8001");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals("new-user@new-host:8001", uriBuilder.getRawAuthority());
        assertEquals(null, uriBuilder.getRawUserInfo());
        assertEquals(null, uriBuilder.getHost());
        assertEquals(-1, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setRawAuthorityWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawAuthority("new-user@new-host:8001");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://new-user@new-host:8001/path?a=1#fragment", uri.toString());
    }

    @Test
    public void setHostTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setHost("new-host");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals(null, uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("new-host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setHostWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setHost("new-host");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@new-host:8000/path?a=1#fragment", uri.toString());
    }

    @Test
    public void setPortTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setPort(8001);

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals(null, uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8001, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setPortWithZeroValueTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setPort(0);

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals(null, uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(0, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setPortWithNegativeValueTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setPort(-1);

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals(null, uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(-1, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setPortWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setPort(8001);
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:8001/path?a=1#fragment", uri.toString());
    }

    @Test
    public void setPortWithZeroValueAndBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setPort(0);
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:0/path?a=1#fragment", uri.toString());
    }

    @Test
    public void setPortWithNegativeValueAndBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setPort(-1);
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host/path?a=1#fragment", uri.toString());
    }

    @Test
    public void setRawUserInfoTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawUserInfo("new-user");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals(null, uriBuilder.getRawAuthority());
        assertEquals("new-user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setRawUserInfoWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawUserInfo("new-user");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://new-user@host:8000/path?a=1#fragment", uri.toString());
    }

    @Test
    public void setRawPathTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawPath("/new-path");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/new-path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setRawPathWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawPath("/new-path");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:8000/new-path?a=1#fragment", uri.toString());
    }

    @Test
    public void setRawQueryTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawQuery("b=%2B2");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("b=%2B2", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setRawQueryWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawQuery("b=%2B2");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:8000/path?b=%2B2#fragment", uri.toString());
    }

    @Test
    public void addParameterTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.addParameter("b", "+2");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1&b=%2B2", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void addParameterWithNullValueTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.addParameter("b", null);

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1&b", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void addParameterWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.addParameter("b", "+2");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:8000/path?a=1&b=%2B2#fragment", uri.toString());
    }

    @Test
    public void addParameterWithNullValueAndBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.addParameter("b", null);
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:8000/path?a=1&b#fragment", uri.toString());
    }

    @Test
    public void addParametersTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");
        final Map<String, Collection<String>> parameters = new HashMap<>();

        parameters.put("b", Arrays.asList("2", "+2"));
        parameters.put("c", Arrays.asList("3", "+3"));

        // Act
        uriBuilder.addParameters(parameters);

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1&b=2&b=%2B2&c=3&c=%2B3", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void addParametersWithNullValuesTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");
        final Map<String, Collection<String>> parameters = new HashMap<>();

        parameters.put("b", Arrays.asList("2", null));
        parameters.put("c", Arrays.asList("3", null));

        // Act
        uriBuilder.addParameters(parameters);

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals(null, uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1&b=2&b&c=3&c", uriBuilder.getRawQuery());
        assertEquals("fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void addParametersWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");
        final Map<String, Collection<String>> parameters = new HashMap<>();

        parameters.put("b", Arrays.asList("2", "+2"));
        parameters.put("c", Arrays.asList("3", "+3"));

        // Act
        uriBuilder.addParameters(parameters);
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:8000/path?a=1&b=2&b=%2B2&c=3&c=%2B3#fragment", uri.toString());
    }

    @Test
    public void addParametersWithNullValuesAndBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");
        final Map<String, Collection<String>> parameters = new HashMap<>();

        parameters.put("b", Arrays.asList("2", null));
        parameters.put("c", Arrays.asList("3", null));

        // Act
        uriBuilder.addParameters(parameters);
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:8000/path?a=1&b=2&b&c=3&c#fragment", uri.toString());
    }

    @Test
    public void setRawFragmentTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawFragment("new-fragment");

        // Assert
        assertEquals("scheme", uriBuilder.getScheme());
        assertEquals("//user@host:8000/path?a=1", uriBuilder.getRawSchemeSpecificPart());
        assertEquals("user@host:8000", uriBuilder.getRawAuthority());
        assertEquals("user", uriBuilder.getRawUserInfo());
        assertEquals("host", uriBuilder.getHost());
        assertEquals(8000, uriBuilder.getPort());
        assertEquals("/path", uriBuilder.getRawPath());
        assertEquals("a=1", uriBuilder.getRawQuery());
        assertEquals("new-fragment", uriBuilder.getRawFragment());
    }

    @Test
    public void setRawFragmentWithBuildTest() {
        // Arrange
        final URIBuilder uriBuilder = new URIBuilder("scheme://user@host:8000/path?a=1#fragment");

        // Act
        uriBuilder.setRawFragment("new-fragment");
        final URI uri = uriBuilder.build();

        // Assert
        assertEquals("scheme://user@host:8000/path?a=1#new-fragment", uri.toString());
    }
}
