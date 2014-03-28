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

import java.io.Reader;
import java.io.Writer;

public interface JsonSerializer {

    /**
     * 
     * @param buffer
     * @return
     */
    JsonReader createReader(Reader buffer);

    /**
     * 
     * @param buffer
     * @return
     */
    JsonWriter createWriter(Writer buffer);

    /**
     * 
     * @param json
     * @param object
     * @return
     */
    <T extends JsonReadable> T fromJson(String json, T object);

    /**
     * 
     * @param object
     * @return
     */
    String toJson(JsonWriteable object);
}
