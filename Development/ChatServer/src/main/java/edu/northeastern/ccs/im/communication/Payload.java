package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@FunctionalInterface
@JsonDeserialize(as = PayloadImpl.class)
public interface Payload {
    @JsonProperty("jsonString")
    String jsonString() throws JsonProcessingException;
}
