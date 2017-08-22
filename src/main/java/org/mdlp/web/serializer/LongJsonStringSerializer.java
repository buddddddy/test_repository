package org.mdlp.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by SSuvorov on 10.04.2017.
 */
public class LongJsonStringSerializer extends JsonSerializer<String> {
    private static final int ABBREVIATION_LENGTH = 3;
    @Value("${json.string.length}")
    private int length;

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        StringWriter sw = null;
        try {
            Gson gson = new GsonBuilder().create();
            JsonElement element = gson.fromJson(value, JsonElement.class);
            sw = new StringWriter();
            new GsonBuilder().create().toJson(element, new CustomJsonWriter(sw));
            jsonGenerator.writeString( sw.getBuffer().toString() );
        } finally {
            IOUtils.closeQuietly(sw);
        }
    }

    private class CustomJsonWriter extends JsonWriter {
        public CustomJsonWriter(final StringWriter out) {
            super(out);
        }

        @Override
        public JsonWriter value(final String value) throws IOException {
            return super.value(StringUtils.abbreviate(value, length + ABBREVIATION_LENGTH));
        }
    }
}
