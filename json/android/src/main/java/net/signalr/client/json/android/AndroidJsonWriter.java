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

package net.signalr.client.json.android;

import net.signalr.client.json.JsonElement;
import net.signalr.client.json.JsonWriter;

/**
 * Represents an Android JSON writer.
 */
final class AndroidJsonWriter implements JsonWriter {

    @Override
    public void writeBeginArray() {
    }

    @Override
    public void writeEndArray() {
    }

    @Override
    public void writeBeginObject() {
    }

    @Override
    public void writeEndObject() {
    }

    @Override
    public void writeName(final String name) {
    }

    @Override
    public void writeElement(final JsonElement element) {
    }

    @Override
    public <T> void writeObject(final T object) {
    }

    @Override
    public void writeNull() {
    }

    @Override
    public void writeString(final String value) {
    }

    @Override
    public void writeBoolean(final boolean value) {
    }

    @Override
    public void writeDouble(final double value) {
    }

    @Override
    public void writeLong(final long value) {
    }

    @Override
    public void writeInt(final int value) {
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
