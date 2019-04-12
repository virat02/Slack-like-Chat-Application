package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.GroupController;
import edu.northeastern.ccs.im.controller.ProfileController;
import edu.northeastern.ccs.im.controller.UserController;

import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.GroupNotPersistedException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotPresentInTheGroup;
import edu.northeastern.ccs.im.service.BroadCastService;
import edu.northeastern.ccs.im.user_group.*;
import edu.northeastern.ccs.im.service.MessageManagerService;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.northeastern.ccs.im.communication.NetworkRequest.NetworkRequestType;

/**
 * A Singleton class taking care of dispatching the requests
 * to appropriate controllers and manager services.
 */
public class RequestDispatcher {

    private static RequestDispatcher requestDispatcher;
    private final Map<NetworkRequestType, RequestStrategy> map;

    static {
        requestDispatcher = new RequestDispatcher();
    }

    /***
     * Sets up the initialization of map which stores the request strategies
     * corresponding to the type of network request.
     * @return
     */
    private Map<NetworkRequestType, RequestStrategy> initializeRequestStrategies() {
        return Collections.unmodifiableMap(
                Stream.of(new AbstractMap.SimpleEntry<>(NetworkRequestType.UPDATE_INVITE, handleUpdateInviteRequest()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.CREATE_USER, handleCreateUserRequest()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.LOGIN_USER, handleLoginRequest()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.SEARCH_USER, searchQueryResults()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.GET_GROUP, getGroupQueryResults()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.UPDATE_GROUP, updateGroupQueryResults()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.CREATE_PROFILE, handleCreateUserProfile()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.UPDATE_PROFILE, handleUpdateProfileObj()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.DELETE_PROFILE, handleDeleteProfileObj()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.UPDATE_USERPROFILE, handleUpdateUserProfileObj()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.UPDATE_PASSWORD, handleUpdatePasswordChange()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.CREATE_GROUP, handleCreateGroup()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.DELETE_GROUP, handleDeleteGroup()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.GET_FOLLOWERS, handleGetFollowers()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.GET_FOLLOWEES, handleGetFollowees()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.SET_FOLLOWERS, handleSetFollowers()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.SET_UNFOLLOWERS, handleSetUnFollowers()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.INVITE_USER, handleInvitationRequest()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.FETCH_INVITE, handleFetchInvitationsRequest()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.EXIT_CHATROOM, handleExitChatRoomRequest()),
                        new AbstractMap.SimpleEntry<>(NetworkRequestType.DELETE_MESSAGE, handleDeleteMessageRequest())
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private static final String USERNAME = "userName";
    private static final String GROUPCODE = "groupCode";

    /***
     * Private constructor to enforce singleton pattern
     */
    private RequestDispatcher() {
        map = initializeRequestStrategies();
    }

    /***
     * Provides a way of injecting message manager service
     * to this dispatcher service.
     *
     * @param messageManagerService the message manager service
     */
    public void setMessageManagerService(MessageManagerService messageManagerService) {
        this.messageManagerService = messageManagerService;
    }

    /**
     * Gets the single instance of this class.
     *
     * @return the instance
     */
    public static RequestDispatcher getInstance() {
        return requestDispatcher;
    }

    private ObjectMapper objectMapper = new ObjectMapper();
    private UserController userController = UserController.getInstance();
    private NetworkResponseFactory networkResponseFactory = new NetworkResponseFactory();
    private MessageManagerService messageManagerService = MessageManagerService.getInstance();

    /**
     * Provides a way of injecting group controller
     * to this dispatcher service.
     *
     * @param groupController the group controller
     */
    public void setGroupController(GroupController groupController) {
        this.groupController = groupController;
    }

    private GroupController groupController = GroupController.getInstance();
    private ProfileController profileController = ProfileController.getInstance();

    /***
     * Provides a way of injecting user controller
     * to this dispatcher service.
     * @param controller the controller
     */
    public void setUserController(UserController controller) {
        this.userController = controller;
    }

    /**
     * Provides a way of injecting user controller
     * to this dispatcher service.
     *
     * @param profileController the profile controller
     */
    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    /**
     * Handle the networks requests coming from clients.
     * <p>
     * Network Requests has different types. Hence, once the
     * network request type is being found, it will be parsed accordingly,
     * and the appropriate controller and service is being called.
     *
     * @param networkRequest the network request
     * @param socketChannel  the socket channel -> The client connection, basically used for
     *                       chat communications, apart from that every request/ response is stateless
     *                       and we don't need to keep the connection persistent.
     * @return the network response
     */
    public NetworkResponse handleNetworkRequest(NetworkRequest networkRequest, SocketChannel socketChannel) {
        ChatLogger.info("Request dispatcher called");
        NetworkRequest.NetworkRequestType networkRequestType = networkRequest.networkRequestType();
        if (networkRequestType == NetworkRequestType.JOIN_GROUP) {
            return handleJoinGroupRequest(networkRequest, socketChannel);
        } else {
            return map.get(networkRequestType).execute(networkRequest);
        }
    }

    /***
     * Handle the update invitation request
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleUpdateInviteRequest() {
        return networkRequest -> {
            try {
                Invite invite = CommunicationUtils.getObjectMapper().readValue(networkRequest.payload().jsonString(), Invite.class);
                return userController.updateInvite(invite);
            } catch (IOException e) {
                ChatLogger.error("Error processing group invitation request");
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handle the fetching of list of invitations request.
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleFetchInvitationsRequest() {
        return networkRequest -> {
            try {
                JsonNode jsonNode = CommunicationUtils.getObjectMapper().readTree(networkRequest.payload().jsonString());
                String userName = jsonNode.get(USERNAME).asText();
                String groupCode = jsonNode.get(GROUPCODE).asText();
                return userController.searchInviteByGroupCode(groupCode, userName);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handle the invitation request when someone invites a person to
     * a group
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleInvitationRequest() {
        return networkRequest -> {
            try {
                Invite invite = CommunicationUtils.getObjectMapper().readValue(networkRequest.payload().jsonString(), Invite.class);
                return userController.sendInvite(invite);
            } catch (IOException e) {
                ChatLogger.error("Error processing group invitation request");
            }

            return networkResponseFactory.createFailedResponse();
        };
    }

    /***
     * Handle the join chatroom request by a user.
     * @param networkRequest -> The network request representing the transaction
     * @param socketChannel -> The channel over which transaction is being performed.
     * @return NetworkResponse response from the server.
     */
    private NetworkResponse handleJoinGroupRequest(NetworkRequest networkRequest, SocketChannel socketChannel) {
        final String IOError = "{\"message\" : \"Some error happened while processing the request. Please try later.\"}";
        final String userNotPresentInGroup = "{\"message\" : \"You are not a participant of the group\"}";
        final String groupNotFound = "{\"message\" : \"The group doesn't exist. Please create a group\"}";
        final String userNotFound = "{\"message\" : \"The user doesn't exist in the system.\"}";
        // Get the message service
        try {
            JsonNode jsonNode = CommunicationUtils
                    .getObjectMapper().readTree(networkRequest.payload().jsonString());
            String groupCode = jsonNode.get(GROUPCODE).asText();
            String userName = jsonNode.get(USERNAME).asText();
            boolean flag = jsonNode.get("isPrivate").asBoolean();
            BroadCastService messageService = messageManagerService.getService(groupCode, userName, flag);
            List<Message> messages = messageService.getUnreadMessages(userName);
            messageService.addConnection(socketChannel);
            return networkResponseFactory.createSuccessfulResponseWithPayload(() -> CommunicationUtils.toJson(messages));
        } catch (IOException | GroupNotPersistedException e) {
            return networkResponseFactory.createFailedResponseWithPayload(() -> IOError);
        } catch (UserNotPresentInTheGroup userNotPresentInTheGroup) {
            return networkResponseFactory.createFailedResponseWithPayload(() -> userNotPresentInGroup);
        } catch (GroupNotFoundException e) {
            return networkResponseFactory.createFailedResponseWithPayload(() -> groupNotFound);
        } catch (UserNotFoundException e) {
            return networkResponseFactory.createFailedResponseWithPayload(() -> userNotFound);
        }
    }

    /***
     * Returns username based on the search query.
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy searchQueryResults() {
        return networkRequest -> {
            try {
                User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
                return userController.searchEntity(user.getUsername());
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handles the login request from client
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleLoginRequest() {
        return networkRequest -> {
            try {
                User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
                return userController.loginUser(user);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handles the signup request from client
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleCreateUserRequest() {
        return networkRequest -> {
            try {
                User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
                return userController.addEntity(user);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handles create user profile request from client.
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleCreateUserProfile() {
        return networkRequest -> {
            try {
                Profile profile = objectMapper.readValue(networkRequest.payload().jsonString(), Profile.class);
                return profileController.addEntity(profile);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handle the update user profile request from client.
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleUpdateProfileObj() {
        return networkRequest -> {
            try {
                Profile profile = objectMapper.readValue(networkRequest.payload().jsonString(), Profile.class);
                return profileController.updateEntity(profile);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handle the delete user profile request from client.
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleDeleteProfileObj() {
        return networkRequest -> {
            try {
                Profile profile = objectMapper.readValue(networkRequest.payload().jsonString(), Profile.class);
                return profileController.deleteEntity(profile);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handle the update user profile
     * @return RequestStrategy handling the request.
     */
    private RequestStrategy handleUpdateUserProfileObj() {
        return networkRequest -> {
            try {
                User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
                return userController.updateEntity(user);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Handle the update password change request
     * @return RequestStrategy handling the request
     */
    private RequestStrategy handleUpdatePasswordChange() {
        return networkRequest -> {
            try {
                User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
                return userController.updateEntity(user);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Deletes a group upon request from moderator
     * @return RequestStrategy handling the request
     */
    private RequestStrategy handleDeleteGroup() {
        return networkRequest -> {
            try {
                Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
                return groupController.deleteEntity(group);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Creates a group
     * @return RequestStrategy handling the request
     */
    private RequestStrategy handleCreateGroup() {
        return networkRequest -> {
            try {
                Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
                List<User> moderators = group.getModerators();
                group.setModerators(Collections.emptyList());
                NetworkResponse response = groupController.addEntity(group);
                if (response.status() == NetworkResponse.STATUS.SUCCESSFUL) {
                    group.setModerators(moderators);
                    return groupController.updateEntity(group);
                }
                return response;
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Fetches the list of followers
     * @return RequestStrategy handling the request
     */
    private RequestStrategy handleGetFollowers() {
        return networkRequest -> {
            try {
                User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
                return userController.viewFollowers(user.getUsername());
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Fetches list of followees
     * @return RequestStrategy handling the request
     */
    private RequestStrategy handleGetFollowees() {
        return networkRequest -> {
            try {
                User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
                return userController.viewFollowees(user.getUsername());
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Set followers for an user
     * @return RequestStrategy handling the request
     */
    private RequestStrategy handleSetFollowers() {
        return networkRequest -> {
            try {
                String payloadString = networkRequest.payload().jsonString();
                List<String> parsedString = Arrays.asList(payloadString.split("\n"));
                if (parsedString.size() != 2) {
                    return networkResponseFactory.createFailedResponse();
                }
                User currentUser = objectMapper.readValue(parsedString.get(0), User.class);
                String userToFollow = parsedString.get(1);
                return userController.followUser(userToFollow, currentUser);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Set unfollowers for an user
     * @return RequestStrategy handling the request
     */
    private RequestStrategy handleSetUnFollowers() {
        return networkRequest -> {
            try {
                String payloadString = networkRequest.payload().jsonString();
                List<String> parsedString = Arrays.asList(payloadString.split("\n"));
                if (parsedString.size() != 2) {
                    return networkResponseFactory.createFailedResponse();
                }
                User currentUser = objectMapper.readValue(parsedString.get(0), User.class);
                String userToFollow = parsedString.get(1);
                return userController.unfollowUser(userToFollow, currentUser);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Fetches a list of groups.
     * @return RequestStrategy handling the request
     */
    private RequestStrategy getGroupQueryResults() {
        return networkRequest -> {
            try {
                Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
                return groupController.getEntity(group.getGroupCode());
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * Updates a group
     * @return RequestStrategy handling the request
     */
    private RequestStrategy updateGroupQueryResults() {
        return networkRequest -> {
            try {
                Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
                return groupController.updateEntity(group);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * A Request to exit out the chatroom. Logs in the user log out time in the db.
     * @return
     */
    private RequestStrategy handleExitChatRoomRequest() {
        return networkRequest -> {
            try {
                JsonNode jsonNode = CommunicationUtils.getObjectMapper().readTree(networkRequest.payload().jsonString());
                String userName = jsonNode.get(USERNAME).asText();
                String groupCode = jsonNode.get(GROUPCODE).asText();
                return userController.exitChatRoom(groupCode, userName);
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /**
     * Delete a message from the group.
     * @return RequestStrategy handling the request
     */
    private RequestStrategy handleDeleteMessageRequest() {
        return networkRequest -> {
            try {
                JsonNode jsonNode = CommunicationUtils
                        .getObjectMapper().readTree(networkRequest.payload().jsonString());
                String groupCode = jsonNode.get(GROUPCODE).asText();
                String userName = jsonNode.get(USERNAME).asText();
                boolean flag = jsonNode.get("isPrivate").asBoolean();
                int messageIndex = jsonNode.get("messageIndex").asInt();

                BroadCastService messageService = messageManagerService.getService(groupCode,
                        userName, flag);
                messageService.deleteMessage(groupCode, messageIndex);

                return networkResponseFactory.createSuccessfulResponse();
            } catch (IOException | GroupNotFoundException | UserNotFoundException |
                    UserNotPresentInTheGroup | GroupNotPersistedException e) {
                return networkResponseFactory.createFailedResponse();
            }
        };
    }

    /***
     * An functional object which handles the dispatching of
     * request for a particular network request
     */
    @FunctionalInterface
    private interface RequestStrategy {
        NetworkResponse execute(NetworkRequest networkRequest);
    }
}