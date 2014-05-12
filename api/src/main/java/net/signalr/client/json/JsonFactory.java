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
