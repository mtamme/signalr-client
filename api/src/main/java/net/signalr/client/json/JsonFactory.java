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

/**
 * Defines JSON factory.
 */
public interface JsonFactory {

    /**
     * Creates a new reader.
     * 
     * @param input The reader.
     * @return The new reader.
     */
    JsonReader createReader(Reader input);

    /**
     * Creates a new writer.
     * 
     * @param output The writer.
     * @return The new writer.
     */
    JsonWriter createWriter(Writer output);
}
