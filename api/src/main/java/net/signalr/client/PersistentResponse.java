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

import net.signalr.client.json.JsonName;
import net.signalr.client.json.JsonToken;
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
    private JsonToken[] _messages;

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
    public void readJson(JsonReader reader) {
        _messageId = reader.getString("C");
        _initialize = reader.getInteger("S");
        _disconnect = reader.getInteger("D");
        _reconnect = reader.getInteger("T");
        _groupsToken = reader.getString("G");
        _longPollDelay = reader.getLong("L");
        _messages = reader.getArray("M");
    }
}
