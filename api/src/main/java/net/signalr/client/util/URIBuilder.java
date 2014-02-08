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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Represents an URI builder.
 */
public final class URIBuilder {

    /**
     * The schema.
     */
    private String _schema;

    /**
     * The user information.
     */
    private String _userInfo;

    /**
     * The host.
     */
    private String _host;

    /**
     * The port.
     */
    private int _port;

    /**
     * The path.
     */
    private String _path;

    /**
     * The query.
     */
    private String _query;

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
        if (uri == null)
            throw new IllegalArgumentException("URI must not be null");

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
        if (uri == null)
            throw new IllegalArgumentException("URI must not be null");

        init(uri.resolve(path));
    }

    /**
     * Returns an {@link URI} for the specified URI.
     * 
     * @param uri The URI.
     * @return The URI.
     */
    private static URI toURI(final String uri) {
        if (uri == null)
            throw new IllegalArgumentException("URI must not be null");

        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new URIException(e);
        }
    }

    /**
     * Initializes the {@link URIBuilder}.
     * 
     * @param uri The URI.
     */
    private void init(final URI uri) {
        _schema = uri.getScheme();
        _userInfo = uri.getUserInfo();
        _host = uri.getHost();
        _port = uri.getPort();
        _path = uri.getPath();
        _query = uri.getQuery();
        _fragment = uri.getFragment();
    }

    /**
     * Returns the schema.
     * 
     * @return The schema.
     */
    public String getSchema() {
        return _schema;
    }

    /**
     * Sets the schema.
     * 
     * @param schema The schema.
     */
    public void setSchema(final String schema) {
        _schema = schema;
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
     */
    public void setHost(final String host) {
        _host = host;
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
     */
    public void setPort(final int port) {
        _port = port;
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
     */
    public void setPath(final String path) {
        _path = path;
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
     */
    public void setQuery(final String query) {
        _query = query;
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
     */
    public void setFragment(final String fragment) {
        _fragment = fragment;
    }

    /**
     * Converts to an {@link URI}.
     * 
     * @return The URI.
     */
    public URI toURI() {
        try {
            return new URI(_schema, _userInfo, _host, _port, _path, _query, _fragment);
        } catch (URISyntaxException e) {
            throw new URIException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        URI uri = toURI();

        return uri.toString();
    }
}
