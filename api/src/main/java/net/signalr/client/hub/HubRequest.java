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
        writer.writeBeginObject();

        if (_callbackId != null) {
            writer.writeName("I");
            writer.writeString(_callbackId);
        }
        if (_hubName != null) {
            writer.writeName("H");
            writer.writeString(_hubName);
        }
        if (_methodName != null) {
            writer.writeName("M");
            writer.writeString(_methodName);
        }
        if (_arguments != null) {
            writer.writeName("A");
            writer.writeObject(_arguments);
        }
        if (_state != null) {
            writer.writeName("S");
            writer.writeObject(_state);
        }

        writer.writeEndObject();
    }
}
