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

import net.signalr.client.json.JsonArray;
import net.signalr.client.json.JsonName;
import net.signalr.client.json.JsonObject;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
*
*/
final class PersistentResponse implements JsonReadable {

    @JsonName("C")
    private String _messageId;

    @JsonName("S")
    private Integer _initialize;

    @JsonName("D")
    private Integer _disconnect;

    @JsonName("T")
    private Integer _reconnect;

    @JsonName("G")
    private String _groupsToken;

    @JsonName("L")
    private Long _longPollDelay;

    @JsonName("M")
    private JsonArray _messages;

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

    @Override
    public void readJson(final JsonReader reader) {
        final JsonObject object = reader.readObject();

        _messageId = object.getString("C");
        _initialize = object.getInt("S", 0);
        _disconnect = object.getInt("D", 0);
        _reconnect = object.getInt("T", 0);
        _groupsToken = object.getString("G");
        _longPollDelay = object.getLong("L", 0L);
        _messages = object.getArray("M");
    }
}
