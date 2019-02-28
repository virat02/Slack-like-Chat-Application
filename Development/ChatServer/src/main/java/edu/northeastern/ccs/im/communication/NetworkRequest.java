package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface NetworkRequest {
    @JsonProperty("networkRequestType")
    NetworkRequestType networkRequestType();

    @JsonProperty("payload")
    Payload payload();

    enum NetworkRequestType {
        CREATE_USER;
    }
}