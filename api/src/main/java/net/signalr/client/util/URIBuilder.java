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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
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
    private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    /**
     * The URI.
     */
    private URI _uri;

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
     * The path.
     */
    private String _path;

    /**
     * The raw query.
     */
    private String _rawQuery;

    /**
     * The query.
     */
    private String _query;

    /**
     * The raw fragment.
     */
    private String _rawFragment;

    /**
     * The fragment.
     */
    private String _fragment;

    /**
     * Initializes a new instance of the {@link URIBuilder} class.
     * 
     * @param uri The URI.
     */
    public URIBuilder(final String uri) {
        this(toURI(uri));
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
     * Initializes a new instance of the {@link URIBuilder} class.
     * 
     * @param uri The URI.
     * @param path The path.
     */
    public URIBuilder(final String uri, final String path) {
        this(toURI(uri), path);
    }

    /**
     * Initializes a new instance of the {@link URIBuilder} class.
     * 
     * @param uri The URI.
     * @param path The path.
     */
    public URIBuilder(final URI uri, final String path) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }

        init(uri.resolve(path));
    }

    /**
     * Returns an {@link URI} for the specified URI.
     * 
     * @param uri The URI.
     * @return The URI.
     */
    private static URI toURI(final String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null");
        }

        try {
            return new URI(uri);
        } catch (final URISyntaxException e) {
            throw new URIException(e);
        }
    }

    /**
     * Initializes the {@link URIBuilder}.
     * 
     * @param uri The URI.
     */
    private void init(final URI uri) {
        _uri = uri;
        _scheme = uri.getScheme();
        _rawSchemeSpecificPart = uri.getRawSchemeSpecificPart();
        _rawAuthority = uri.getRawAuthority();
        _host = uri.getHost();
        _port = uri.getPort();
        _rawUserInfo = uri.getRawUserInfo();
        _rawPath = uri.getRawPath();
        _path = uri.getPath();
        _rawQuery = uri.getRawQuery();
        _query = uri.getQuery();
        _rawFragment = uri.getRawFragment();
        _fragment = uri.getFragment();
    }

    /**
     * Builds the raw URI.
     * 
     * @return The raw URI.
     */
    private String buildRawUri() {
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

        return uri.toString();
    }

    /**
     * Builds the raw query including the specified parameters.
     * 
     * @param newParameters The parameters.
     * @return The raw query.
     */
    private String buildRawQuery(final Map<String, Collection<String>> newParameters) {
        final StringBuilder rawQuery = new StringBuilder();

        if (_rawQuery != null) {
            rawQuery.append(_rawQuery);
        }
        if (rawQuery.length() > 0) {
            rawQuery.append('&');
        }

        for (final String name : newParameters.keySet()) {
            final Collection<String> values = newParameters.get(name);
            final String rawName = encode(name);

            for (final String value : values) {
                appendRawParameter(rawName, value, rawQuery);
            }
        }

        return rawQuery.toString();
    }

    /**
     * Builds the raw query including the specified parameter name and parameter value.
     * 
     * @param name The parameter name.
     * @param value The parameter value.
     * @return The raw query.
     */
    private String buildRawQuery(final String name, final String value) {
        final StringBuilder rawQuery = new StringBuilder();

        if (_rawQuery != null) {
            rawQuery.append(_rawQuery);
        }
        final String rawName = encode(name);

        appendRawParameter(rawName, value, rawQuery);

        return rawQuery.toString();
    }

    /**
     * Appends the specified raw parameter name and parameter value.
     * 
     * @param rawName The raw parameter name.
     * @param value The parameter value.
     * @param rawQuery The raw query.
     */
    private void appendRawParameter(final String rawName, final String value, final StringBuilder rawQuery) {
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
     * Decodes the specified value.
     * 
     * @param value The value.
     * @return The decoded value.
     */
    private static String decode(final String value) {
        if (value == null) {
            return null;
        }

        try {
            return URLDecoder.decode(value, DEFAULT_CHARACTER_ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new URIException(e);
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
            return URLEncoder.encode(value, DEFAULT_CHARACTER_ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new URIException(e);
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
     * @return The {@link URIBuilder}.
     */
    public URIBuilder setScheme(final String scheme) {
        _uri = null;
        _scheme = scheme;

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
     * @return The {@link URIBuilder}.
     */
    public URIBuilder setHost(final String host) {
        _uri = null;
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
     * @return The {@link URIBuilder}.
     */
    public URIBuilder setPort(final int port) {
        _uri = null;
        _rawSchemeSpecificPart = null;
        _rawAuthority = null;
        _port = (port < 0) ? -1 : port;

        return this;
    }

    /**
     * Returns the path.
     * 
     * @return The path.
     */
    public String getPath() {
        return _path;
    }

    /**
     * Sets the path.
     * 
     * @param path The path.
     * @return The {@link URIBuilder}.
     */
    public URIBuilder setPath(final String path) {
        _uri = null;
        _rawSchemeSpecificPart = null;
        _rawPath = encode(path);
        _path = path;

        return this;
    }

    /**
     * Returns the query.
     * 
     * @return The query.
     */
    public String getQuery() {
        return _query;
    }

    /**
     * Sets the query.
     * 
     * @param query The query.
     * @return The {@link URIBuilder}.
     */
    public URIBuilder setQuery(final String query) {
        _uri = null;
        _rawSchemeSpecificPart = null;
        _rawQuery = encode(query);
        _query = query;

        return this;
    }

    /**
     * Adds the specified parameters.
     * 
     * @param parameters The parameters.
     * @return The {@link URIBuilder}.
     */
    public URIBuilder addParameters(final Map<String, Collection<String>> parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters must not be null");
        }

        _rawSchemeSpecificPart = null;
        _rawQuery = buildRawQuery(parameters);
        _query = decode(_rawQuery);

        return this;
    }

    /**
     * Adds the specified parameter.
     * 
     * @param name The parameter name.
     * @param value The parameter value.
     * @return The {@link URIBuilder}.
     */
    public URIBuilder addParameter(final String name, final String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }

        _rawSchemeSpecificPart = null;
        _rawQuery = buildRawQuery(name, value);
        _query = decode(_rawQuery);

        return this;
    }

    /**
     * Returns the fragment.
     * 
     * @return The fragment.
     */
    public String getFragment() {
        return _fragment;
    }

    /**
     * Sets the fragment.
     * 
     * @param fragment The fragment.
     * @return The {@link URIBuilder}.
     */
    public URIBuilder setFragment(final String fragment) {
        _uri = null;
        _rawFragment = encode(fragment);
        _fragment = fragment;

        return this;
    }

    /**
     * Converts to an {@link URI}.
     * 
     * @return The URI.
     */
    public URI toURI() {
        if (_uri != null) {
            return _uri;
        }

        try {
            final String uri = buildRawUri();

            _uri = new URI(uri);
        } catch (final URISyntaxException e) {
            throw new URIException(e);
        }

        return _uri;
    }

    @Override
    public String toString() {
        final URI uri = toURI();

        return uri.toString();
    }
}
