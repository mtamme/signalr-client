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

/**
 * Represents a hub message.
 */
final class HubMessage {

    /**
     * The element.
     */
    private JsonElement _element;

    /**
     * Initializes a new instance of the {@link HubMessage} class.
     * 
     * @param element The element.
     */
    public HubMessage(final JsonElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }

        _element = element;
    }

    /**
     * Returns the hub name.
     * 
     * @return The hub name.
     */
    public String getHubName() {
        return _element.get("H").getString(null);
    }

    /**
     * Returns the method name.
     * 
     * @return The method name.
     */
    public String getMethodName() {
        return _element.get("M").getString(null);
    }

    /**
     * Returns the arguments.
     * 
     * @return The arguments.
     */
    public JsonElement[] getArguments() {
        final JsonElement array = _element.get("A");
        final JsonElement[] arguments = new JsonElement[array.size()];

        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = array.get(i);
        }

        return arguments;
    }
}
