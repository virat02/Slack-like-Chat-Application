package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/***
 * A contract serving network responses sent by the server to client.
 */
@JsonDeserialize(as = NetworkResponseImpl.class)
public interface NetworkResponse {
    /***
     * Represents the status of this response
     * @return STATUS
     */
    @JsonProperty("status")
    STATUS status();

    /***
     * Represents the payload of this response.
     * @return Payload
     */
    @JsonProperty("payload")
    Payload payload();

    /***
     * Represents the status of this network response.
     */
    enum STATUS {
        SUCCESSFUL, FAILED;
    }
}

/***
 * Used by jackson library for deserializartion purposes.
 */
class NetworkResponseDeserializer extends StdDeserializer<Payload> {

    public NetworkResponseDeserializer() {
        this(null);
    }

    public NetworkResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * A deserializer for the NetworkResponse.
     * @param jsonParser the parser for json.
     * @param deserializationContext the context for a deserializer.
     * @return Payload that was created.
     * @throws IOException if there aren't any deserializers.
     */
    @Override
    public Payload deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String jsonString = node.get("jsonString").asText();
        return new PayloadImpl(jsonString);
    }
}

