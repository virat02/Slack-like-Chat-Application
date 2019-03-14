package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonProperty;

/***
 * An interface for a network request
 */
public interface NetworkRequest {
    /***
     *
     * @return the type of the network Request represented by the enum NetworkRequestType
     */
    @JsonProperty("networkRequestType")
    NetworkRequestType networkRequestType();

    /***
     *
     * @return the payload carried by this network request.
     */
    @JsonProperty("payload")
    Payload payload();

    /***
     * An enum for the type of NetworkRequest
     */
    enum NetworkRequestType {
        CREATE_USER,
        LOGIN_USER,
        FORGOT_PASSWORD,
        SEARCH_USER,
        SEARCH_GROUP,
        CREATE_GROUP,
        DELETE_GROUP,
        SELECT_CHAT,
        JOIN_GROUP,
        SEND_MESSAGE,
        CREATE_PROFILE,
        UPDATE_PROFILE,
        UPDATE_USERPROFILE,
        UPDATE_PASSWORD,
        GET_FOLLOWEES,
        GET_FOLLOWERS,
        SET_FOLLOWERS,
    }
}