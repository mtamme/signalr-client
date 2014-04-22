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

package net.signalr.client.transport.jetty;

import java.io.IOException;

import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.Promise;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;

/**
 * Represents a response listener.
 */
final class ResponseListener extends BufferingResponseListener {

    /**
     * The response.
     */
    private final Deferred<String> _response;

    /**
     * Initializes a new instance of the {@link ResponseListener} class.
     */
    public ResponseListener() {
        _response = new Deferred<String>();
    }

    /**
     * Returns the response.
     * 
     * @return The response.
     */
    public Promise<String> getResponse() {
        return _response;
    }

    /**
     * Handles a response.
     * 
     * @param response The response.
     */
    private void handleResponse(final Response response) {
        final int statusCode = response.getStatus();

        if (statusCode != 200) {
            final Throwable cause = new IOException("The request failed with HTTP status " + statusCode + ": " + response.getReason());

            _response.reject(cause);
        } else {
            final String content = getContentAsString();

            _response.resolve(content);
        }
    }

    @Override
    public void onComplete(final Result result) {
        final Throwable failure = result.getFailure();

        if (failure != null) {
            _response.reject(failure);
        } else {
            final Response response = result.getResponse();

            handleResponse(response);
        }
    }
}
