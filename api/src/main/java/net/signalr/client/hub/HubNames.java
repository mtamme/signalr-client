/*
 * Copyright 2014 Martin Tamme
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.signalr.client.json.JsonWriteable;
import net.signalr.client.json.JsonWriter;

/**
 * Represents a set of hub names.
 */
final class HubNames implements JsonWriteable {

    /**
     * The hub names.
     */
    private final Set<String> _names;

    /**
     * Initializes a new instance of the {@link HubNames} class.
     */
    public HubNames() {
        _names = new HashSet<String>();
    }

    /**
     * Adds the specified hub name.
     * 
     * @param name The hub name.
     */
    public void add(final String name) {
        _names.add(name);
    }

    /**
     * Adds the specified hub names.
     * 
     * @param names The hub names.
     */
    public void addAll(final Collection<String> names) {
        _names.addAll(names);
    }

    @Override
    public void writeJson(final JsonWriter writer) {
        writer.writeBeginArray();

        for (final String name : _names) {
            writer.writeBeginObject();
            writer.writeName("name");
            writer.writeString(name);
            writer.writeEndObject();
        }

        writer.writeEndArray();
    }
}
