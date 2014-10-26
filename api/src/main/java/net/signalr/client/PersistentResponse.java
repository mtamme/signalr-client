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

package net.signalr.client;

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
 * Represents a persistent response.
 */
final class PersistentResponse implements JsonReadable {

    private String _messageId;

    private Integer _initialize;

    private Integer _disconnect;

    private Integer _reconnect;

    private String _groupsToken;

    private Long _longPollDelay;

    private JsonElement _messages;

    public String getMessageId() {
        return _messageId;
    }

    public boolean isInitialize() {
        return (_initialize != null) && (_initialize == 1);
    }

    public boolean isDisconnect() {
        return (_disconnect != null) && (_disconnect == 1);
    }

    public boolean isReconnect() {
        return (_reconnect != null) && (_reconnect == 1);
    }

    public String getGroupsToken() {
        return _groupsToken;
    }

    public Long getLongPollingDelay() {
        return _longPollDelay;
    }

    public JsonElement getMessage() {
        return _messages;
    }

    @Override
    public void readJson(final JsonReader reader) {
        reader.readBeginObject();

        while (reader.read()) {
            final String name = reader.getName();

            if (name.equalsIgnoreCase("C")) {
                _messageId = reader.readString();
            } else if (name.equalsIgnoreCase("S")) {
                _initialize = reader.readInt();
            } else if (name.equalsIgnoreCase("D")) {
                _disconnect = reader.readInt();
            } else if (name.equalsIgnoreCase("T")) {
                _reconnect = reader.readInt();
            } else if (name.equalsIgnoreCase("G")) {
                _groupsToken = reader.readString();
            } else if (name.equalsIgnoreCase("L")) {
                _longPollDelay = reader.readLong();
            } else if (name.equalsIgnoreCase("M")) {
                _messages = reader.readElement();
            } else {
                reader.skipValue();
            }
        }

        reader.readEndObject();
    }
}
