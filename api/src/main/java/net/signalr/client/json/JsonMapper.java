/*
 * Copyright © Martin Tamme
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
 * Defines a JSON mapper.
 */
public interface JsonMapper {

    /**
     * Converts the specified text into an element.
     * 
     * @param text The text.
     * @return The element.
     */
    JsonElement toElement(String text);

    /**
     * Converts the specified text into an object.
     * 
     * @param text The text.
     * @param type The object type.
     * @return The object.
     */
    <T extends JsonReadable> T toObject(String text, Class<T> type);

    /**
     * Converts the specified object into a text.
     * 
     * @param object The object.
     * @return The text.
     */
    String toJson(JsonWriteable object);
}
