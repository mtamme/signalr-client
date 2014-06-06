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

package net.signalr.client.transport.jetty;

import java.io.IOException;

import net.signalr.client.util.concurrent.promise.Deferred;
import net.signalr.client.util.concurrent.promise.Promise;

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

            _response.setFailure(cause);
        } else {
            final String content = getContentAsString();

            _response.setSuccess(content);
        }
    }

    @Override
    public void onComplete(final Result result) {
        final Throwable failure = result.getFailure();

        if (failure != null) {
            _response.setFailure(failure);
        } else {
            final Response response = result.getResponse();

            handleResponse(response);
        }
    }
}
