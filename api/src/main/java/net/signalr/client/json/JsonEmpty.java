/*
 * Copyright 2014 Martin Tamme
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.signalr.client.json;

/**
 * Represents an empty JSON element.
 */
public final class JsonEmpty implements JsonElement {

    /**
     * The immutable singleton instance.
     */
    public static final JsonEmpty INSTANCE = new JsonEmpty();

    /**
     * Initializes a new instance of the {@link JsonEmpty} class.
     */
    private JsonEmpty() {
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isValue() {
        return false;
    }

    @Override
    public JsonElement get(final int index) {
        return this;
    }

    @Override
    public JsonElement get(final String name) {
        return this;
    }

    @Override
    public boolean getBoolean(final boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public double getDouble(final double defaultValue) {
        return defaultValue;
    }

    @Override
    public int getInt(final int defaultValue) {
        return defaultValue;
    }

    @Override
    public long getLong(final long defaultValue) {
        return defaultValue;
    }

    @Override
    public String getString(final String defaultValue) {
        return defaultValue;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public <T> T unwrap(final Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T toObject(final Class<T> type, final T defaultValue) {
        return defaultValue;
    }

    @Override
    public int hashCode() {
        return JsonEmpty.class.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (other instanceof JsonEmpty);
    }

    @Override
    public String toString() {
        return "";
    }
}
