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
    public Map<String, Object> getState() {
        @SuppressWarnings("unchecked")
        final Map<String, Object> state = _element.get("S").toObject(Map.class, null);

        return state;
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
