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

package net.signalr.client.json;

import java.io.Closeable;
import java.io.Flushable;

public interface JsonWriter extends Closeable, Flushable {

    void writeBeginArray();

    void writeEndArray();

    void writeBeginObject();

    void writeEndObject();

    void writeName(String name);

    void writeValue(JsonValue element);

    <V> void writeObject(V value);

    void writeNull();

    void writeString(String value);

    void writeBoolean(boolean value);

    void writeDouble(double value);

    void writeLong(long value);

    void writeInt(int value);

    void flush();

    void close();
}