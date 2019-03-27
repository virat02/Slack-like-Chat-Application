package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.GroupController;
import edu.northeastern.ccs.im.controller.ProfileController;
import edu.northeastern.ccs.im.controller.UserController;

import edu.northeastern.ccs.im.service.BroadCastService;
import edu.northeastern.ccs.im.user_group.*;
import edu.northeastern.ccs.im.service.MessageManagerService;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;

import static edu.northeastern.ccs.im.communication.NetworkRequest.NetworkRequestType;

/**
 * A Singleton class taking care of dispatching the requests
 * to appropriate controllers and manager services.
 */
public class RequestDispatcher {

    private static RequestDispatcher requestDispatcher = new RequestDispatcher();

    private RequestDispatcher() {

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
    private UserController userController = new UserController();
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

    private GroupController groupController = new GroupController();
    private ProfileController profileController = new ProfileController();

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
     *
     * Network Requests has different types. Hence, once the
     * network request type is being found, it will be parsed accordingly,
     * and the appropriate controller and service is being called.
     * @param networkRequest the network request
     * @param socketChannel  the socket channel -> The client connection, basically used for
     *                       chat communications, apart from that every request/ response is stateless
     *                       and we don't need to keep the connection persistent.
     * @return the network response
     */
    public NetworkResponse handleNetworkRequest(NetworkRequest networkRequest, SocketChannel socketChannel) {
        ChatLogger.info("Request dispatcher called");
        NetworkRequest.NetworkRequestType networkRequestType = networkRequest.networkRequestType();
        if (networkRequestType == NetworkRequestType.CREATE_USER) {
            return handleCreateUserRequest(networkRequest);
        } else if (networkRequestType == NetworkRequestType.LOGIN_USER) {
            return handleLoginRequest(networkRequest);
        } else if (networkRequestType == NetworkRequestType.SEARCH_USER) {
            return searchQueryResults(networkRequest);
        } else if (networkRequestType == NetworkRequestType.GET_GROUP) {
            return getGroupQueryResults(networkRequest);
        } else if (networkRequestType == NetworkRequestType.UPDATE_GROUP) {
            return updateGroupQueryResults(networkRequest);
        } else if (networkRequestType == NetworkRequestType.JOIN_GROUP) {
            return handleJoinGroupRequest(networkRequest, socketChannel);
        } else if (networkRequestType == NetworkRequestType.CREATE_PROFILE) {
            return handleCreateUserProfile(networkRequest);
        } else if (networkRequestType == NetworkRequestType.UPDATE_PROFILE) {
            return handleUpdateProfileObj(networkRequest);
        } else if (networkRequestType == NetworkRequestType.UPDATE_USERPROFILE) {
            return handleUpdateUserProfileObj(networkRequest);
        } else if (networkRequestType == NetworkRequestType.UPDATE_PASSWORD) {
            return handleUpdatePasswordChange(networkRequest);
        } else if (networkRequestType == NetworkRequestType.CREATE_GROUP) {
            return handleCreateGroup(networkRequest);
        } else if (networkRequestType == NetworkRequestType.DELETE_GROUP) {
            return handleDeleteGroup(networkRequest);
        } else if (networkRequestType == NetworkRequestType.GET_FOLLOWERS) {
            return handleGetFollowers(networkRequest);
        } else if (networkRequestType == NetworkRequestType.GET_FOLLOWEES) {
            return handleGetFollowees(networkRequest);
        } else if (networkRequestType == NetworkRequestType.SET_FOLLOWERS) {
            return handleSetFollowers(networkRequest);
        } else if (networkRequestType == NetworkRequestType.SET_UNFOLLOWERS) {
            return handleSetUnFollowers(networkRequest);
        } else if (networkRequestType == NetworkRequestType.INVITE_USER)    {
            return handleInvitationRequest(networkRequest);
        } else if (networkRequestType == NetworkRequestType.FETCH_INVITE)   {
            return handleFetchInvitationsRequest(networkRequest);
        } else if (networkRequestType == NetworkRequestType.UPDATE_INVITE)  {
            return handleUpdateInviteRequest(networkRequest);
        }

        return networkResponseFactory.createFailedResponse();
    }

    private NetworkResponse handleUpdateInviteRequest(NetworkRequest networkRequest) {
        try {
            Invite invite = CommunicationUtils.getObjectMapper().readValue(networkRequest.payload().jsonString(), Invite.class);
            return userController.updateInvite(invite);
        } catch (IOException e) {
            ChatLogger.error("Error processing group invitation request");
        }

        return networkResponseFactory.createFailedResponse();
    }

    private NetworkResponse handleFetchInvitationsRequest(NetworkRequest networkRequest) {
        try {
            JsonNode jsonNode = CommunicationUtils.getObjectMapper().readTree(networkRequest.payload().jsonString());
            String userName = jsonNode.get("userName").asText();
            String groupCode = jsonNode.get("groupCode").asText();
            return userController.searchInviteByGroupCode(groupCode, userName);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleInvitationRequest(NetworkRequest networkRequest) {
        try {
            Invite invite = CommunicationUtils.getObjectMapper().readValue(networkRequest.payload().jsonString(), Invite.class);
            return userController.sendInvite(invite);
        } catch (IOException e) {
            ChatLogger.error("Error processing group invitation request");
        }

        return networkResponseFactory.createFailedResponse();
    }

    private NetworkResponse handleJoinGroupRequest(NetworkRequest networkRequest, SocketChannel socketChannel) {
        // Get the message service
        try {
            JsonNode jsonNode = CommunicationUtils
                    .getObjectMapper().readTree(networkRequest.payload().jsonString());
            String groupCode = jsonNode.get("groupCode").asText();
            BroadCastService messageService = messageManagerService.getService(groupCode);
            List<Message> messages = messageService.getRecentMessages();
            messageService.addConnection(socketChannel);
            return networkResponseFactory.createSuccessfulResponseWithPayload(() -> CommunicationUtils.toJson(messages));
        } catch (IllegalAccessException | IOException e ) {
            ChatLogger.error(e.getMessage());
        }
        return networkResponseFactory.createFailedResponse();
    }

    private NetworkResponse searchQueryResults(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            return userController.searchEntity(user.getUsername());
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleLoginRequest(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            return userController.loginUser(user);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleCreateUserRequest(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            return userController.addEntity(user);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleCreateUserProfile(NetworkRequest networkRequest) {
        try {
            Profile profile = objectMapper.readValue(networkRequest.payload().jsonString(), Profile.class);
            return profileController.addEntity(profile);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleUpdateProfileObj(NetworkRequest networkRequest) {
        try {
            Profile profile = objectMapper.readValue(networkRequest.payload().jsonString(), Profile.class);
            return profileController.updateEntity(profile);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleUpdateUserProfileObj(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            return userController.updateEntity(user);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleUpdatePasswordChange(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            return userController.updateEntity(user);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleDeleteGroup(NetworkRequest networkRequest) {
        try {
            Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
            return groupController.deleteEntity(group);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleCreateGroup(NetworkRequest networkRequest) {
        try {
            Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
            List<User> moderators = group.getModerators();
            group.setModerators(null);
            NetworkResponse response = groupController.addEntity(group);
            if (response.status() == NetworkResponse.STATUS.SUCCESSFUL) {
                group.setModerators(moderators);
                for(User u : moderators){
                    group.addUser(u);
                }
                return groupController.updateEntity(group);
            }
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleGetFollowers(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            return userController.viewFollowers(user.getUsername());
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleGetFollowees(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            return userController.viewFollowees(user.getUsername());
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleSetFollowers(NetworkRequest networkRequest) {
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
    }

    private NetworkResponse handleSetUnFollowers(NetworkRequest networkRequest) {
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
    }

    private NetworkResponse getGroupQueryResults(NetworkRequest networkRequest) {
        try {
            Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
            return groupController.getEntity(group.getGroupCode());
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse updateGroupQueryResults(NetworkRequest networkRequest) {
        try {
            Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
            return groupController.updateEntity(group);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }
}