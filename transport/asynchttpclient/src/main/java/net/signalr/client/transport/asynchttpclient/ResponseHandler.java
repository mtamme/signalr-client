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

package net.signalr.client.transport.asynchttpclient;

import java.io.IOException;

import net.signalr.client.util.concurrent.promise.Deferred;
import net.signalr.client.util.concurrent.promise.Promise;

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
        _response = new Deferred<>();
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
