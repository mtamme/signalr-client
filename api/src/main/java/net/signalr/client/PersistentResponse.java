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
        reader.beginObject();

        while (reader.hasNext()) {
            final String name = reader.nextName();

            if (name.equalsIgnoreCase("C")) {
                _messageId = reader.nextString();
            } else if (name.equalsIgnoreCase("S")) {
                _initialize = reader.nextInt();
            } else if (name.equalsIgnoreCase("D")) {
                _disconnect = reader.nextInt();
            } else if (name.equalsIgnoreCase("T")) {
                _reconnect = reader.nextInt();
            } else if (name.equalsIgnoreCase("G")) {
                _groupsToken = reader.nextString();
            } else if (name.equalsIgnoreCase("L")) {
                _longPollDelay = reader.nextLong();
            } else if (name.equalsIgnoreCase("M")) {
                _messages = reader.nextElement();
            }
        }

        reader.endObject();
    }
}
