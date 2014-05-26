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

package net.signalr.client.hub;

import java.util.Map;

import net.signalr.client.json.JsonElement;

/**
 * Represents a hub response.
 */
final class HubResponse {

    /**
     * The element.
     */
    private final JsonElement _element;

    /**
     * Initializes a new instance of the {@link HubResponse} class.
     * 
     * @param element The element.
     */
    public HubResponse(final JsonElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }

        _element = element;
    }

    /**
     * Returns the round tripped state.
     * 
     * @return The round tripped state.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getState() {
        return _element.get("S").toObject(Map.class, null);
    }

    /**
     * Returns the data of the invocation.
     * 
     * @return The data of the invocation.
     */
    public JsonElement getData() {
        return _element.get("R");
    }

    /**
     * Returns the ID of the operation.
     * 
     * @return The ID of the operation.
     */
    public String getCallbackId() {
        return _element.get("I").getString(null);
    }

    /**
     * Returns a value indicating whether the error is a {@link HubException}.
     * 
     * @return A value indicating whether the error is a {@link HubException}.
     */
    public boolean isHubException() {
        return _element.get("H").getBoolean(false);
    }

    /**
     * Returns the exception that occurred as a result of a hub method invocation.
     * 
     * @return The exception that occurred as a result of a hub method invocation.
     */
    public String getErrorMessage() {
        return _element.get("E").getString(null);
    }

    /**
     * Returns the stack trace of the exception that occurred as a result of a hub method invocation.
     * 
     * @return The stack trace of the exception that occurred as a result of a hub method invocation.
     */
    public String getStackTrace() {
        return _element.get("T").getString(null);
    }

    /**
     * Returns the extra error data contained in the {@link HubException}.
     * 
     * @return The extra error data contained in the {@link HubException}.
     */
    public String getErrorData() {
        return _element.get("D").getString(null);
    }

    /**
     * Returns the message ID.
     * 
     * @return The message ID.
     */
    public String getMessageId() {
        return _element.get("C").getString(null);
    }

    /**
     * Returns the hub messages.
     * 
     * @return The hub messages.
     */
    public HubMessage[] getMessages() {
        final JsonElement array = _element.get("M");
        final HubMessage[] messages = new HubMessage[array.size()];

        for (int i = 0; i < messages.length; i++) {
            final JsonElement element = array.get(i);

            messages[i] = new HubMessage(element);
        }

        return messages;
    }
}
