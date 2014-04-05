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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.signalr.client.json.JsonWriteable;
import net.signalr.client.json.JsonWriter;

/**
 * Represents a set of hub names.
 */
final class HubNames implements JsonWriteable {

    private final Set<String> _names;

    public HubNames() {
        _names = new HashSet<String>();
    }

    public void add(final String name) {
        _names.add(name);
    }

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
