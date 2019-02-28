package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.jpa.User;

public class NetworkRequestFactory {
    ObjectMapper objectMapper = new ObjectMapper();

    public NetworkRequest createUserRequest(String name, String emailId) throws JsonProcessingException {
        User user = new User(name, emailId);
        String jsonString = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Payload payload = new PayloadImpl(jsonString);
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_USER, payload);
    }
}
