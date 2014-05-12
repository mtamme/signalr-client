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
 * Represents a JSON exception.
 */
public final class JsonException extends RuntimeException {

    /**
     * The serial version unique identifier.
     */
    private static final long serialVersionUID = -4454146458602222646L;

    /**
     * Initializes a new instance of the {@link JsonException} class.
     * 
     * @param message The message.
     */
    public JsonException(final String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@link JsonException} class.
     * 
     * @param cause The cause.
     */
    public JsonException(final Throwable cause) {
        super(cause);
    }
}
