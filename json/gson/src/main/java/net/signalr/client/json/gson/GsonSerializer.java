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

package net.signalr.client.json.gson;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Modifier;

import net.signalr.client.json.JsonReadable;
import net.signalr.client.json.JsonSerializer;
import net.signalr.client.json.JsonWriteable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public final class GsonSerializer implements JsonSerializer {

    private final Gson _gson;

    public GsonSerializer() {
        _gson = build();
    }

    private static Gson build() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.excludeFieldsWithModifiers(Modifier.STATIC);
        // gsonBuilder.setFieldNamingStrategy(new ReflectiveFieldNamingStrategy());

        return gsonBuilder.create();
    }

    @Override
    public <T extends JsonReadable> T fromJson(String json, T object) {
        final GsonReader reader = new GsonReader(_gson, new JsonReader(new StringReader(json)));

        object.readJson(reader);

        return object;
    }

    @Override
    public String toJson(JsonWriteable object) {
        final StringWriter json = new StringWriter();
        final GsonWriter writer = new GsonWriter(_gson, new JsonWriter(json));

        object.writeJson(writer);

        return json.toString();
    }
}