package edu.northeastern.ccs.im.communication;

//import edu.northeastern.ccs.jpa.Message;
//import edu.northeastern.ccs.jpa.Profile;
//import edu.northeastern.ccs.jpa.User;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Message;
import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.userGroup.User;

/***
 * A NetworkRequestFactory which returns instance of Network Request depending upon
 * the Enum NetworkRequestType
 */
public class NetworkRequestFactory {
    /***
     * Creates a NetworkRequest for creating a user.
     * @param userName The username set by the user
     * @param password The password set by the user
     * @return NetworkRequest
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
     * @param userName
     * @param password
     * @return NetworkRequest
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

    public NetworkRequest createUpdateUserProfile(Profile profile, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_USERPROFILE,
                () -> {
                    user.setProfile(profile);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createUpdateUserCredentials(String password, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_PASSWORD,
                () -> {
                    user.setPassword(password);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a forgot password request for the user.
     * @param emailID
     * @return
     */
    public NetworkRequest createForgotPasswordRequest(String emailID) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.FORGOT_PASSWORD,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a search user request for the user.
     * @param searchString
     * @return NetworkRequest
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
     * @param code
     * @return NetworkRequest
     */
    public NetworkRequest createGetGroupRequest(String code) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.GET_GROUP,
                () -> {
                    Group group = new Group();
                    group.setGroupCode(code);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(group);
                });
    }

    /***
     * Creates a network request for searching the group
     * @param group
     * @return NetworkRequest
     */
    public NetworkRequest createUpdateGroupRequest(Group group) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_GROUP,
                () -> CommunicationUtils.getObjectMapper().writeValueAsString(group));
    }

    /***
     * Creates a network request for searching the group
     * @param searchString
     * @return NetworkRequest
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
     * @param groupName
     * @return NetworkRequest
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
     * @param chatId
     * @return NetworkRequest
     */
    public NetworkRequest createSelectChatRequest(String chatId) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SELECT_CHAT,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a message request for sending a message.
     * @param messageBody
     * @return
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

    public NetworkRequest createUpdateGroupRequest(String groupName, String groupCode, User user) {
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

    public NetworkRequest createRemoveGroupUserRequest(String groupName, String groupCode, User user) {
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

    public NetworkRequest createUpdateProfileStatus(boolean viewStatus, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_USERPROFILE,
                () -> {
                    user.setProfileAccess(viewStatus);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createUserProfile(String userEmail, String imageUrl) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_PROFILE,
                () -> {
                    Profile profile = new Profile();
                    profile.setImageUrl(imageUrl);
                    profile.setEmail(userEmail);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(profile);
                });
    }

    public NetworkRequest createUpdateUserProfile(String userEmail, String imageUrl, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_PROFILE,
                () -> {
                    Profile profile = user.getProfile();
                    profile.setEmail(userEmail);
                    profile.setImageUrl(imageUrl);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(profile);
                });
    }

    public NetworkRequest createGetUserFollowersList(String userName) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.GET_FOLLOWERS,
                () -> {
                    User user = new User();
                    user.setUsername(userName);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createGetUserFolloweesList(String userName) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.GET_FOLLOWEES,
                () -> {
                    User user = new User();
                    user.setUsername(userName);
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createSetUserFolloweresList(String userName, User user) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SET_FOLLOWERS,
                () -> {

                    return CommunicationUtils.getObjectMapper().writeValueAsString(user) +
                            "\n" + userName;
                });
    }

    /***
     * Creates a group invite request
     * @param userName
     * @param groupCode
     * @return
     */
    public NetworkRequest createGroupInviteRequest(String invitee, String groupCode) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.INVITE_USER,
                () -> "");
    }
}