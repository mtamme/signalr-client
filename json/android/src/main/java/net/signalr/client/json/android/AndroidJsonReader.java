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
import net.signalr.client.json.JsonReader;

/**
 * Represents an Android JSON reader.
 */
final class AndroidJsonReader implements JsonReader {

    @Override
    public void readBeginArray() {
    }

    @Override
    public void readEndArray() {
    }

    @Override
    public void readBeginObject() {
    }

    @Override
    public void readEndObject() {
    }

    @Override
    public boolean read() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public JsonElement readElement() {
        return null;
    }

    @Override
    public <T> T readObject(final Class<T> type) {
        return null;
    }

    @Override
    public void readNull() {
    }

    @Override
    public String readString() {
        return null;
    }

    @Override
    public boolean readBoolean() {
        return false;
    }

    @Override
    public double readDouble() {
        return 0;
    }

    @Override
    public long readLong() {
        return 0;
    }

    @Override
    public int readInt() {
        return 0;
    }

    @Override
    public void close() {
    }
}
