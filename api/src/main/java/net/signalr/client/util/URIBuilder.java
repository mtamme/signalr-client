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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

/**
 * Represents an URI builder.
 */
public final class URIBuilder {

    /**
     * The default character encoding.
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Resolves the specified URI against the base URI and returns a new URI builder.
     * 
     * @param baseUri The base URI.
     * @param uri The URI.
     * @return The new URI builder.
     */
    public static URIBuilder resolve(final String baseUri, final String uri) {
        return resolve(toUri(baseUri), uri);
    }

    /**
     * Resolves the specified URI against the base URI and returns a new URI builder.
     * 
     * @param baseUri The base URI.
     * @param uri The URI.
     * @return The new URI builder.
     */
    public static URIBuilder resolve(final URI baseUri, final String uri) {
        if (baseUri == null) {
            throw new IllegalArgumentException("Base URI must not be null");
        }

        return new URIBuilder(baseUri.resolve(toUri(uri)));
    }

    /**
     * The scheme.
     */
    private String _scheme;

    /**
     * The raw scheme specific part.
     */
    private String _rawSchemeSpecificPart;

    /**
     * The raw authority.
     */
    private String _rawAuthority;

    /**
     * The raw user information.
     */
    private String _rawUserInfo;

    /**
     * The host.
     */
    private String _host;

    /**
     * The port.
     */
    private int _port;

    /**
     * The raw path.
     */
    private String _rawPath;

    /**
     * The raw query.
     */
    private String _rawQuery;

    /**
     * The raw fragment.
     */
    private String _rawFragment;

    /**
     * Initializes a new instance of the {@link URIBuilder} class.
     */
    public URIBuilder() {
        _scheme = null;
        _rawSchemeSpecificPart = null;
        _rawAuthority = null;
        _host = null;
        _port = -1;
        _rawUserInfo = null;
        _rawPath = null;
        _rawQuery = null;
        _rawFragment = null;
    }

    /**
     * Initializes a new instance of the {@link URIBuilder} class.
     * 
     * @param uri The URI.
     */
    public URIBuilder(final String uri) {
        this(toUri(uri));
    }

    /**
     * Initializes a new instance of the {@link URIBuilder} class.
     * 
     * @param uri The URI.
     */
    public URIBuilder(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }

        init(uri);
    }

    /**
     * Converts the specified URI to an {@link URI}.
     * 
     * @param uri The URI.
     * @return The URI.
     */
    private static URI toUri(final String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }

        try {
            return new URI(uri);
        } catch (final URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Initializes the {@link URIBuilder}.
     * 
     * @param uri The URI.
     */
    private void init(final URI uri) {
        _scheme = uri.getScheme();
        _rawSchemeSpecificPart = uri.getRawSchemeSpecificPart();
        _rawAuthority = uri.getRawAuthority();
        _host = uri.getHost();
        _port = uri.getPort();
        _rawUserInfo = uri.getRawUserInfo();
        _rawPath = uri.getRawPath();
        _rawQuery = uri.getRawQuery();
        _rawFragment = uri.getRawFragment();
    }

    /**
     * Appends the specified parameter name and parameter value.
     * 
     * @param name The parameter name.
     * @param value The parameter value.
     * @return The raw query.
     */
    private String appendParameter(final String name, final String value) {
        final StringBuilder rawQuery = new StringBuilder();

        if (_rawQuery != null) {
            rawQuery.append(_rawQuery);
        }
        final String rawName = encode(name);

        appendParameter(rawName, value, rawQuery);

        return rawQuery.toString();
    }

    /**
     * Appends the specified parameters.
     * 
     * @param parameters The parameters.
     * @return The raw query.
     */
    private String appendParameters(final Map<String, Collection<String>> parameters) {
        final StringBuilder rawQuery = new StringBuilder();

        if (_rawQuery != null) {
            rawQuery.append(_rawQuery);
        }
        for (final String name : parameters.keySet()) {
            final Collection<String> values = parameters.get(name);
            final String rawName = encode(name);

            for (final String value : values) {
                appendParameter(rawName, value, rawQuery);
            }
        }

        return rawQuery.toString();
    }

    /**
     * Appends the specified raw parameter name and parameter value.
     * 
     * @param rawName The raw parameter name.
     * @param value The parameter value.
     * @param rawQuery The raw query.
     */
    private static void appendParameter(final String rawName, final String value, final StringBuilder rawQuery) {
        if (rawQuery.length() > 0) {
            rawQuery.append('&');
        }
        rawQuery.append(rawName);
        if (value != null) {
            final String rawValue = encode(value);

            rawQuery.append('=').append(rawValue);
        }
    }

    /**
     * Encodes the specified value.
     * 
     * @param value The value.
     * @return The encoded value.
     */
    private static String encode(final String value) {
        if (value == null) {
            return null;
        }

        try {
            return URLEncoder.encode(value, DEFAULT_ENCODING);
        } catch (final UnsupportedEncodingException e) {
            // Unlikely since the default encoding should be always supported.
            return null;
        }
    }

    /**
     * Returns the scheme.
     * 
     * @return The scheme.
     */
    public String getScheme() {
        return _scheme;
    }

    /**
     * Sets the scheme.
     * 
     * @param scheme The scheme.
     * @return The URI builder.
     */
    public URIBuilder setScheme(final String scheme) {
        _scheme = scheme;

        return this;
    }

    /**
     * Returns the raw scheme specific part.
     * 
     * @return The raw scheme specific part.
     */
    public String getRawSchemeSpecificPart() {
        return _rawSchemeSpecificPart;
    }

    /**
     * Sets the raw scheme specific part.
     * 
     * @param rawSchemeSpecificPart The raw scheme specific part.
     * @return The URI builder.
     */
    public URIBuilder setRawSchemeSpecificPart(final String rawSchemeSpecificPart) {
        _rawSchemeSpecificPart = rawSchemeSpecificPart;
        _rawAuthority = null;
        _rawUserInfo = null;
        _host = null;
        _port = -1;
        _rawPath = null;
        _rawQuery = null;

        return this;
    }

    /**
     * Returns the raw authority.
     * 
     * @return The raw authority.
     */
    public String getRawAuthority() {
        return _rawAuthority;
    }

    /**
     * Sets the raw authority.
     * 
     * @param rawAuthority The raw authority.
     * @return The URI builder.
     */
    public URIBuilder setRawAuthority(final String rawAuthority) {
        _rawSchemeSpecificPart = null;
        _rawAuthority = rawAuthority;
        _rawUserInfo = null;
        _host = null;
        _port = -1;

        return this;
    }

    /**
     * Returns the host.
     * 
     * @return The host.
     */
    public String getHost() {
        return _host;
    }

    /**
     * Sets the host.
     * 
     * @param host The host.
     * @return The URI builder.
     */
    public URIBuilder setHost(final String host) {
        _rawSchemeSpecificPart = null;
        _rawAuthority = null;
        _host = host;

        return this;
    }

    /**
     * Returns the port.
     * 
     * @return The port.
     */
    public int getPort() {
        return _port;
    }

    /**
     * Sets the port.
     * 
     * @param port The port.
     * @return The URI builder.
     */
    public URIBuilder setPort(final int port) {
        _rawSchemeSpecificPart = null;
        _rawAuthority = null;
        _port = (port < 0) ? -1 : port;

        return this;
    }

    /**
     * Returns the raw user info.
     * 
     * @return The raw user info.
     */
    public String getRawUserInfo() {
        return _rawUserInfo;
    }

    /**
     * Sets the raw user info.
     * 
     * @param path The raw user info.
     * @return The URI builder.
     */
    public URIBuilder setRawUserInfo(final String rawUserInfo) {
        _rawSchemeSpecificPart = null;
        _rawAuthority = null;
        _rawUserInfo = rawUserInfo;

        return this;
    }

    /**
     * Returns the raw path.
     * 
     * @return The raw path.
     */
    public String getRawPath() {
        return _rawPath;
    }

    /**
     * Sets the raw path.
     * 
     * @param path The raw path.
     * @return The URI builder.
     */
    public URIBuilder setRawPath(final String rawPath) {
        _rawSchemeSpecificPart = null;
        _rawPath = rawPath;

        return this;
    }

    /**
     * Returns the raw query.
     * 
     * @return The raw query.
     */
    public String getRawQuery() {
        return _rawQuery;
    }

    /**
     * Sets the raw query.
     * 
     * @param query The raw query.
     * @return The URI builder.
     */
    public URIBuilder setRawQuery(final String rawQuery) {
        _rawSchemeSpecificPart = null;
        _rawQuery = rawQuery;

        return this;
    }

    /**
     * Adds the specified parameter.
     * 
     * @param name The parameter name.
     * @param value The parameter value.
     * @return The URI builder.
     */
    public URIBuilder addParameter(final String name, final String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }

        _rawSchemeSpecificPart = null;
        _rawQuery = appendParameter(name, value);

        return this;
    }

    /**
     * Adds the specified parameters.
     * 
     * @param parameters The parameters.
     * @return The URI builder.
     */
    public URIBuilder addParameters(final Map<String, Collection<String>> parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters must not be null");
        }

        _rawSchemeSpecificPart = null;
        _rawQuery = appendParameters(parameters);

        return this;
    }

    /**
     * Returns the raw fragment.
     * 
     * @return The raw fragment.
     */
    public String getRawFragment() {
        return _rawFragment;
    }

    /**
     * Sets the raw fragment.
     * 
     * @param fragment The raw fragment.
     * @return The URI builder.
     */
    public URIBuilder setRawFragment(final String rawFragment) {
        _rawFragment = rawFragment;

        return this;
    }

    /**
     * Builds a new {@link URI}.
     * 
     * @return The URI.
     */
    public URI build() {
        final StringBuilder uri = new StringBuilder();

        if (_scheme != null) {
            uri.append(_scheme).append(':');
        }
        if (_rawSchemeSpecificPart != null) {
            uri.append(_rawSchemeSpecificPart);
        } else {
            if (_rawAuthority != null) {
                uri.append("//").append(_rawAuthority);
            } else if (_host != null) {
                uri.append("//");
                if (_rawUserInfo != null) {
                    uri.append(_rawUserInfo).append("@");
                }
                uri.append(_host);
                if (_port >= 0) {
                    uri.append(":").append(_port);
                }
            }
            if (_rawPath != null) {
                uri.append(_rawPath);
            }
            if (_rawQuery != null) {
                uri.append("?").append(_rawQuery);
            }
        }
        if (_rawFragment != null) {
            uri.append("#").append(_rawFragment);
        }

        return toUri(uri.toString());
    }
}
