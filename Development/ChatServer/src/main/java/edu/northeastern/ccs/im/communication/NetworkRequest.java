package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface NetworkRequest {
    @JsonProperty("networkRequestType")
    NetworkRequestType networkRequestType();

    @JsonProperty("payload")
    Payload payload();

    enum NetworkRequestType {
        CREATE_USER,
        LOGIN_USER,
        FORGOT_PASSWORD,
        SEARCH_USER,
        SEARCH_GROUP,
        CREATE_GROUP,
        SELECT_CHAT,
        JOIN_GROUP,
        SEND_MESSAGE,
    }
}