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

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonReader;

/**
 * Represents a hub message.
 */
final class HubMessage implements JsonReadable {

    /**
     * The hub name.
     */
    private String _hubName;

    /**
     * The method name.
     */
    private String _methodName;

    /**
     * The arguments.
     */
    private JsonElement _arguments;

    public String getHubName() {
        return _hubName;
    }

    public String getMethodName() {
        return _methodName;
    }

    public JsonElement getArguments() {
        return _arguments;
    }

    @Override
    public void readJson(final JsonReader reader) {
        while (reader.hasNext()) {
            final String name = reader.nextName();

            if (name.equalsIgnoreCase("H")) {
                _hubName = reader.nextString();
            } else if (name.equalsIgnoreCase("M")) {
                _methodName = reader.nextString();
            } else if (name.equalsIgnoreCase("A")) {
                _arguments = reader.nextElement();
            }
        }
    }
}
