package edu.northeastern.ccs.im.communication;

import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.Profile;
import edu.northeastern.ccs.im.user_group.User;

import java.util.ArrayList;
import java.util.List;

/***
 * A NetworkRequestFactory which returns instance of Network Request depending upon
 * the Enum NetworkRequestType
 */
public class NetworkRequestFactory {

    /***
     * Creates a NetworkRequest for creating a user.
     * @param userName The username set by the user
     * @param password The password set by the user
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createUserRequest(String userName, String password) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_USER,
                () -> {
                    User user = new User();
                    user.setUsername(userName);
                    user.setPassword(password);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a login request for the user.
     * @param userName the user name required to login
     * @param password the password required to login
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createLoginRequest(String userName, String password) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.LOGIN_USER,
                () -> {
                    User user = new User();
                    user.setUsername(userName);
                    user.setPassword(password);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /**
     * Update user profile with the required credentials.
     * @param profile the profile object to update
     * @param user the user to which the profile must be updated
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createUpdateUserProfile(Profile profile, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_USERPROFILE,
                () -> {
                    user.setProfile(profile);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /**
     * Creates a user with the required credentials
     * @param password the password for logging in
     * @param user the user object
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createUpdateUserCredentials(String password, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_PASSWORD,
                () -> {
                    user.setPassword(password);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a forgot password request for the user.
     * @param emailID the email address for recovery
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createForgotPasswordRequest(String emailID) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.FORGOT_PASSWORD,
                () -> {
                    User user = new User();
                    user.setProfile(new Profile());
                    user.getProfile().setEmail(emailID);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a search user request for the user.
     * @param searchString the search query containing the user if
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createSearchUserRequest(String searchString) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SEARCH_USER,
                () -> {
                    User user = new User();
                    user.setUsername(searchString);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a network request for searching the group
     * @param searchString the search query
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createSearchGroupRequest(String searchString) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SEARCH_GROUP,
                () -> {
                    Group group = new Group();
                    group.setName(searchString);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(group);
                });
    }

    /***
     * Creates a request for creation of a new group.
     * @param groupName the group name to be updated
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createGroupRequest(String groupName, String groupCode, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_GROUP,
                () -> {
                    Group group = new Group();
                    group.setName(groupName);
                    group.setGroupCode(groupCode);
                    List<User> moderators = new ArrayList<>();
                    moderators.add(user);
                    group.setModerators(moderators);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(group);
                });
    }

    /***
     * Creates a request for selecting a chat
     * @param chatId the chat if of the chat
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createSelectChatRequest(String chatId) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SELECT_CHAT,
                () -> {
                    User user = new User();
                    user.setUsername(chatId);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a message request for sending a message.
     * @param messageBody the message content of the message
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createMessageRequest(String messageBody) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SEND_MESSAGE,
                () -> {
                    Message message = new Message();
                    message.setMessage(messageBody);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(message);
                });
    }

    /***
     * Creates a request for joining a group.
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createJoinGroup(String groupCode) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.JOIN_GROUP,
                () -> "{\"groupCode\":\"" + groupCode + "\"}");
    }

    /**
     * Creates a request for deleting a group
     * @param groupName group name of the group
     * @param groupCode group code of the group
     * @param user the user who is updating
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createDeleteGroupRequest(String groupName, String groupCode, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.DELETE_GROUP,
                () -> {
                    Group group = new Group();
                    group.setName(groupName);
                    group.setGroupCode(groupCode);
                    List<User> moderators = new ArrayList<>();
                    moderators.add(user);
                    group.setModerators(moderators);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(group);
                });
    }

    /**
     * Creates a user profile
     * @param userEmail the email of user
     * @param imageUrl the image url of user
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createUserProfile(String userEmail, String imageUrl) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_PROFILE,
                () -> {
                    Profile profile = new Profile();
                    profile.setImageUrl(imageUrl);
                    profile.setEmail(userEmail);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(profile);
                });
    }

    /**
     * Updates the user profile
     * @param userEmail the email of the user
     * @param imageUrl the image url of the user
     * @param user the user object
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createUpdateUserProfile(String userEmail, String imageUrl, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_PROFILE,
                () -> {
                    Profile profile = user.getProfile();
                    profile.setEmail(userEmail);
                    profile.setImageUrl(imageUrl);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(profile);
                });
    }

    /**
     * Get followers list for the user
     * @param userName the given username
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createGetUserFollowersList(String userName) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.GET_FOLLOWERS,
                () -> {
                    User user = new User();
                    user.setUsername(userName);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /**
     * Gets the followees of the user
     * @param userName the given user
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createGetUserFolloweesList(String userName) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.GET_FOLLOWEES,
                () -> {
                    User user = new User();
                    user.setUsername(userName);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /**
     * Used to set followers
     * @param userName user to follow
     * @param user the followee
     * @return NetworkRequest containing the required request
     */
    public NetworkRequest createSetUserFolloweresList(String userName, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SET_FOLLOWERS,
                () -> CommunicationUtils.getObjectMapper().writeValueAsString(user) +
                        "\n" + userName);
    }
}