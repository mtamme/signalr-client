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

    void beginArray();

    void endArray();

    void beginObject();

    void endObject();

    void name(String name);

    void elementValue(JsonElement element);

    <V> void value(V value);

    void nullValue();

    void stringValue(String value);

    void booleanValue(boolean value);

    void doubleValue(double value);

    void longValue(long value);

    void intValue(int value);

    void flush();

    void close();
}