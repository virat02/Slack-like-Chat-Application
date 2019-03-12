package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/***
 * An contract for serving payloads over network requests and responses.
 */
@FunctionalInterface
@JsonDeserialize(using = NetworkResponseDeserializer.class)
public interface Payload {
    /***
     * Stores the transferable entities in json format.
     * @return
     * @throws JsonProcessingException
     */
    @JsonProperty("jsonString")
    String jsonString() throws JsonProcessingException;
}

/***
 * A Deserialization class used by Jackson. 
 */
class JSONDeserializer extends StdDeserializer<Payload> {

    public JSONDeserializer()   {
        this(null);
    }
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
