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

import net.signalr.client.json.JsonWriteable;
import net.signalr.client.json.JsonWriter;

/**
 * Represents a hub request.
 */
final class HubRequest implements JsonWriteable {

    private String _callbackId;

    private String _hubName;

    private String _methodName;

    private Object[] _arguments;

    private Map<String, Object> _state;

    public void setCallbackId(final String callbackId) {
        _callbackId = callbackId;
    }

    public void setHubName(final String hubName) {
        _hubName = hubName;
    }

    public void setMethodName(final String methodName) {
        _methodName = methodName;
    }

    public void setArguments(final Object[] arguments) {
        _arguments = arguments;
    }

    public void setState(final Map<String, Object> state) {
        _state = state;
    }

    @Override
    public void writeJson(final JsonWriter writer) {
        writer.beginObject();

        writer.name("I");
        writer.stringValue(_callbackId);
        if (_hubName != null) {
            writer.name("H");
            writer.stringValue(_hubName);
        }
        if (_methodName != null) {
            writer.name("M");
            writer.stringValue(_methodName);
        }
        if (_arguments != null) {
            writer.name("A");
            writer.value(_arguments);
        }
        if (_state != null) {
            writer.name("S");
            writer.value(_state);
        }

        writer.endObject();
    }
}
