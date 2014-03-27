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

import net.signalr.client.json.JsonValue;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
 * Represents a hub response.
 */
final class HubResponse implements JsonReadable {

    /**
     * The changes made the the round tripped state.
     */
    private Map<String, Object> _state;

    /**
     * The data of the invocation.
     */
    private JsonValue _data;

    /**
     * The ID of the operation.
     */
    private String _callbackId;

    /**
     * Indicates whether the error is a <code>HubException</code>.
     */
    private Boolean _isHubException;

    /**
     * The exception that occurs as a result of invoking the hub method.
     */
    private String _errorMessage;

    /**
     * The stack trace of the exception that occurs as a result of invoking the hub method.
     */
    private String _stackTrace;

    /**
     * Extra error data contained in the <code>HubException</code>.
     */
    private String _errorData;

    /**
     * The message ID.
     */
    private String _messageId;

    /**
     * The hub messages.
     */
    private JsonValue _messages;

    public Map<String, Object> getState() {
        return _state;
    }

    public JsonValue getData() {
        return _data;
    }

    public String getCallbackId() {
        return _callbackId;
    }

    public boolean isHubException() {
        return _isHubException;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public String getStackTrace() {
        return _stackTrace;
    }

    public String getErrorData() {
        return _errorData;
    }

    public String getMessageId() {
        return _messageId;
    }

    public JsonValue getMessages() {
        return _messages;
    }

    @Override
    public void readJson(final JsonReader reader) {
        reader.readBeginObject();

        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("S")) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> state = reader.readObject(Map.class);

                _state = state;
            } else if (name.equalsIgnoreCase("R")) {
                _data = reader.readValue();
            } else if (name.equalsIgnoreCase("I")) {
                _callbackId = reader.readString();
            } else if (name.equalsIgnoreCase("H")) {
                _isHubException = reader.readBoolean();
            } else if (name.equalsIgnoreCase("E")) {
                _errorMessage = reader.readString();
            } else if (name.equalsIgnoreCase("T")) {
                _stackTrace = reader.readString();
            } else if (name.equalsIgnoreCase("D")) {
                _errorData = reader.readString();
            } else if (name.equalsIgnoreCase("C")) {
                _messageId = reader.readString();
            } else if (name.equalsIgnoreCase("M")) {
                _messages = reader.readValue();
            }
        }

        reader.readEndObject();
    }
}
