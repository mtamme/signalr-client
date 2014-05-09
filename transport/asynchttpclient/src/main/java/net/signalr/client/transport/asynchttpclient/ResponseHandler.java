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

package net.signalr.client.transport.asynchttpclient;

import java.io.IOException;

import net.signalr.client.util.concurrent.Deferred;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

/**
 * Represents an response handler.
 */
final class ResponseHandler extends AsyncCompletionHandler<Response> {

    /**
     * The response.
     */
    private final Deferred<Response> _response;

    /**
     * Initializes a new instance of the {@link ResponseHandler} class.
     */
    public ResponseHandler() {
        _response = Promises.newDeferred();
    }

    /**
     * Returns the response.
     * 
     * @return The response.
     */
    public Promise<Response> getResponse() {
        return _response;
    }

    @Override
    public void onThrowable(final Throwable cause) {
        _response.setFailure(cause);
    }

    @Override
    public Response onCompleted(final Response response) throws Exception {
        final int statusCode = response.getStatusCode();

        if (statusCode != 200) {
            final Throwable cause = new IOException("The request failed with HTTP status " + statusCode + ": " + response.getStatusText());

            _response.setFailure(cause);
        } else {
            _response.setSuccess(response);
        }

        return response;
    }
}
