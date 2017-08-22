package org.mdlp.utils;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.io.IOException;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 27.07.2017
 * @package org.mdlp.utils
 */
public class JsonUtils {

    private JsonUtils() {
    }

    private static ObjectMapper createObjectMapperInstance() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return mapper;
    }


    /**
     * Convert json string to object
     *
     * @param content   - json string
     * @param valueType - object class
     * @param <T>       - object class paramter
     * @return instance of T class
     */
    public static <T> T fromJson(String content, Class<T> valueType) {
        try {
            if (content == null || content.trim().isEmpty()) {
                return null;
            } else {
                return createObjectMapperInstance().readValue(content, valueType);
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(getMessage(e), e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to deserialize object from JSON", e);
        }
    }

    /**
     * Convert object to json string
     *
     * @param value - object value
     * @return json string
     */
    public static String toJson(Object value) {
        try {
            return createObjectMapperInstance().writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize object to JSON", e);
        }
    }

    /**
     * Convert json string to JsonNode
     *
     * @param content - json string
     * @return json node object
     */
    public static JsonNode toJsonNode(String content) {
        try {
            return createObjectMapperInstance().readTree(content);
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize object to JSON", e);
        }
    }

    /**
     * Convert json node object to object
     *
     * @param node      - json node
     * @param valueType - object class
     * @param <T>       - object class parameter
     * @return instance of T class
     */
    public static <T> T treeToValue(JsonNode node, Class<T> valueType) {
        try {
            return createObjectMapperInstance().treeToValue(node, valueType);
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize object to JSON", e);
        }
    }

    private static String getMessage(JsonMappingException e) {
        String prefix = "message mapping exception";
        if (e instanceof UnrecognizedPropertyException) {
            String pName = ((UnrecognizedPropertyException) e).getPropertyName();
            return prefix + " - unrecognized property: '" + pName + "'";
        }
        return prefix;
    }
}
