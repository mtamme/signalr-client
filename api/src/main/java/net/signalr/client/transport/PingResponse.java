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

package net.signalr.client.transport;

import net.signalr.client.json.JsonName;
import net.signalr.client.json.JsonObject;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
 * Represents a ping response.
 */
public final class PingResponse implements JsonReadable {

    @JsonName("Response")
    private String _value;

    public String getValue() {
        return _value;
    }

    @Override
    public void readJson(final JsonReader reader) {
        final JsonObject object = reader.readObject();

        _value = object.getString("Response");
    }
}
