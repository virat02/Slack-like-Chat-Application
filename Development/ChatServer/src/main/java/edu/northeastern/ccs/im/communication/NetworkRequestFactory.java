package edu.northeastern.ccs.im.communication;

import edu.northeastern.ccs.jpa.User;

public class NetworkRequestFactory {
    public NetworkRequest createUserRequest(String name, String emailId) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_USER,
                () -> {
                    User user = new User(name, emailId);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }
}
