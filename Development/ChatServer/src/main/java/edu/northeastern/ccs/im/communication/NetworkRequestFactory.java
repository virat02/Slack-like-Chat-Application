package edu.northeastern.ccs.im.communication;

import edu.northeastern.ccs.jpa.Message;
import edu.northeastern.ccs.jpa.Profile;
import edu.northeastern.ccs.jpa.User;

public class NetworkRequestFactory {
    public NetworkRequest createUserRequest(String userName, String password, String emailAddress) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_USER,
                () -> {
                    User user = new User();
                    user.setId(0);

                    Profile profile = new Profile();
                    profile.setId(0);
                    profile.setName(userName);
                    profile.setEmail(emailAddress);
                    profile.setImageUrl("");
//                    profile.setUser(user);
                    profile.setPassword(password);

                    user.setProfile(profile);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createLoginRequest(String userName, String password) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.LOGIN_USER,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createForgotPasswordRequest(String emailID) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.FORGOT_PASSWORD,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createSearchUserRequest(String searchString) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SEARCH_USER,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createSearchGroupRequest(String searchString) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SEARCH_GROUP,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createGroupRequest(String groupName) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_GROUP,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createSelectChatRequest(String chatId) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SELECT_CHAT,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createMessageRequest(String messageBody) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SEND_MESSAGE,
                () -> {
                    Message message = new Message();
                    message.setMessage(messageBody);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(message);
                });
    }

    public NetworkRequest createJoinGroup() {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.JOIN_GROUP,
                () -> "");
    }
}
