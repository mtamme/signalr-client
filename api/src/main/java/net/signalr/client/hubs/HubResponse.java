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

package net.signalr.client.hubs;

import java.util.Map;

import net.signalr.client.json.JsonName;
import net.signalr.client.json.JsonToken;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
*
*/
final class HubResponse implements JsonReadable {

    /**
     * The changes made the the round tripped state.
     */
    @JsonName("S")
    private Map<String, JsonToken> _state;

    /**
     * The result of the invocation.
     */
    @JsonName("R")
    private Object _result;

    /**
     * The ID of the operation.
     */
    @JsonName("I")
    private String _callbackId;

    /**
     * Indicates whether the error is a <code>HubException</code>.
     */
    @JsonName("H")
    private Boolean _isHubException;

    /**
     * The exception that occurs as a result of invoking the hub method.
     */
    @JsonName("E")
    private String _error;

    /**
     * The stack trace of the exception that occurs as a result of invoking the hub method.
     */
    @JsonName("T")
    private String _stackTrace;

    /**
     * Extra error data contained in the <code>HubException</code>.
     */
    @JsonName("D")
    private String _errorData;

    @JsonName("C")
    private String _messageId;

    public String getCallbackId() {
        return _callbackId;
    }

    public String getMessageId() {
        return _messageId;
    }

    @Override
    public void readJson(final JsonReader reader) {
    }
}
