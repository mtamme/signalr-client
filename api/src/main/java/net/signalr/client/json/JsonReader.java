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

/**
 * Defines a JSON reader.
 */
public interface JsonReader extends Closeable {

    void beginArray();

    void endArray();

    void beginObject();

    void endObject();

    boolean hasNext();

    JsonToken peek();

    String nextName();

    JsonElement nextElement();

    <V> V nextValue(Class<V> valueClass);

    void nextNull();

    String nextString();

    boolean nextBoolean();

    double nextDouble();

    long nextLong();

    int nextInt();

    void close();
}