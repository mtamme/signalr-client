package net.signalr.client.json;

/**
 * Defines all JSON token types.
 */
public enum JsonToken {

    /**
     * An unknown JSON token.
     */
    UNKNOWN,

    /**
     * The opening of a JSON array.
     */
    BEGIN_ARRAY,

    /**
     * The opening of a JSON object.
     */
    BEGIN_OBJECT,

    /**
     * A JSON true or false.
     */
    BOOLEAN,

    /**
     * The closing of a JSON array.
     */
    END_ARRAY,

    /**
     * The end of the JSON stream.
     */
    END_DOCUMENT,

    /**
     * The closing of a JSON object.
     */
    END_OBJECT,

    /**
     * A JSON property name.
     */
    NAME,

    /**
     * A JSON null.
     */
    NULL,

    /**
     * A JSON number represented in this API by a Java double, long, or int.
     */
    NUMBER,

    /**
     * A JSON string.
     */
    STRING
}
