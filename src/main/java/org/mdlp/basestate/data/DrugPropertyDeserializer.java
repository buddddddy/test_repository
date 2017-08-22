package org.mdlp.basestate.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Created by SSuvorov on 04.05.2017.
 */
public class DrugPropertyDeserializer extends StdDeserializer<DrugProperty> {
    DrugPropertyDeserializer()
    {
        super(DrugProperty.class);
    }


    @Override
    public DrugProperty deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return null;
    }
}
