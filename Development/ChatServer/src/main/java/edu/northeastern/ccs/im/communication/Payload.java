package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

@FunctionalInterface
@JsonDeserialize(using = JSONDeserializer.class)
public interface Payload {
    @JsonProperty("jsonString")
    String jsonString() throws JsonProcessingException;
}

class JSONDeserializer extends StdDeserializer<Payload> {

    public JSONDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Payload deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String jsonString = node.get("jsonString").asText();
        return new PayloadImpl(jsonString);
    }
}
