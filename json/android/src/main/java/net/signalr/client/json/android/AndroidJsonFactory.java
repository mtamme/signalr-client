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

import java.io.Reader;
import java.io.Writer;

import net.signalr.client.json.JsonFactory;
import net.signalr.client.json.JsonReader;
import net.signalr.client.json.JsonWriter;

/**
 * Represents an Android JSON factory.
 */
public final class AndroidJsonFactory implements JsonFactory {

    @Override
    public JsonReader createReader(final Reader input) {
        return null;
    }

    @Override
    public JsonWriter createWriter(final Writer output) {
        return null;
    }
}
