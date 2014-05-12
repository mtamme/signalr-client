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
