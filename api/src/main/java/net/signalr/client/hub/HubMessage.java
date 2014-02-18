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

import net.signalr.client.json.JsonArray;
import net.signalr.client.json.JsonName;

/**
 * Represents a hub message.
 */
final class HubMessage {

    /**
     * The hub name.
     */
    @JsonName("H")
    private String _hubName;

    /**
     * The method name.
     */
    @JsonName("M")
    private String _methodName;

    /**
     * The arguments.
     */
    @JsonName("A")
    private JsonArray _arguments;

    public String getHubName() {
        return _hubName;
    }

    public String getMethodName() {
        return _methodName;
    }

    public JsonArray getArguments() {
        return _arguments;
    }
}
