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
import edu.northeastern.ccs.im.view.UserConstants;

/***
 * A NetworkRequestFactory which returns instance of Network Request depending upon
 * the Enum NetworkRequestType
 */
public class NetworkRequestFactory {
    /***
     * Creates a NetworkRequest for creating a user.
     * @param userName The username set by the user
     * @param password The password set by the user
     * @param emailAddress The emailaddress set by the user
     * @return NetworkRequest
     */
    public NetworkRequest createUserRequest(String userName, String password, String emailAddress) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_USER,
                () -> {
                    User user = new User();
                    user.setUsername(userName);
                    user.setPassword(password);

//                    Profile profile = new Profile();
//                    profile.setEmail(emailAddress);
//                    profile.setImageUrl("");
//
//                    user.setProfile(profile);
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
     * @param searchString
     * @return NetworkRequest
     */
    public NetworkRequest createSearchGroupRequest(String searchString) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.SEARCH_GROUP,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    /***
     * Creates a request for creation of a new group.
     * @param groupName
     * @return NetworkRequest
     */
    public NetworkRequest createGroupRequest(String groupName) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.CREATE_GROUP,
                () -> {
                  Group group = new Group();
                  group.setName(groupName);
                  List<User> moderators = new ArrayList<>();
                  moderators.add(UserConstants.getUserObj());
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
     * @return NetworkRequest
     */
    public NetworkRequest createJoinGroup() {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.JOIN_GROUP,
                () -> "");
    }

    public NetworkRequest createDeleteGroupRequest(String groupName) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.DELETE_GROUP,
                () -> {
                  Group group = new Group();
                  group.setName(groupName);
                  List<User> moderators = new ArrayList<>();
                  moderators.add(UserConstants.getUserObj());
                  group.setModerators(moderators);
                  return CommunicationUtils.getObjectMapper().writeValueAsString(group);
                });
    }

    public NetworkRequest createUpdateUserName(String userName) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_USERNAME,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }

    public NetworkRequest createUpdateUserStatus(String userStatus) {
        return new NetworkRequestImpl(NetworkRequest.NetworkRequestType.UPDATE_USERSTATUS,
                () -> {
                    User user = new User();
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
    }
}
