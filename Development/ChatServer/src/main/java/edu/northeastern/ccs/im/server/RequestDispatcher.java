package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.GroupController;
import edu.northeastern.ccs.im.controller.IController;
import edu.northeastern.ccs.im.controller.ProfileController;
import edu.northeastern.ccs.im.controller.UserController;

import edu.northeastern.ccs.im.service.BroadCastService;
import edu.northeastern.ccs.im.service.MessageBroadCastService;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.service.MessageManagerService;
import edu.northeastern.ccs.im.userGroup.Message;
import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.userGroup.User;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.List;

import static edu.northeastern.ccs.im.communication.NetworkRequest.NetworkRequestType;

public class RequestDispatcher {

    private static RequestDispatcher requestDispatcher = new RequestDispatcher();

    private RequestDispatcher() {

    }

    public void setMessageManagerService(MessageManagerService messageManagerService) {
        this.messageManagerService = messageManagerService;
    }

    public static RequestDispatcher getInstance() {
        return requestDispatcher;
    }

    private ObjectMapper objectMapper = new ObjectMapper();
    private IController userController = new UserController();
    private NetworkResponseFactory networkResponseFactory = new NetworkResponseFactory();
    private MessageManagerService messageManagerService = MessageManagerService.getInstance();

    /***
     * The purpose of this method is mocking the IController.
     * Though there can be dependency injection done for user controller
     * in this application, but I think it is not required
     * for this application, hence just having a setter in
     * place to mock the controller
     * @param controller
     */
    public void setUserController(IController controller) {
        this.userController = controller;
    }

    public NetworkResponse handleNetworkRequest(NetworkRequest networkRequest, SocketChannel socketChannel) {
        ChatLogger.info("Request dispatcher called");
        NetworkRequest.NetworkRequestType networkRequestType = networkRequest.networkRequestType();
        if (networkRequestType == NetworkRequestType.CREATE_USER) {
            return handleCreateUserRequest(networkRequest);
        } else if (networkRequestType == NetworkRequestType.LOGIN_USER) {
            return handleLoginRequest(networkRequest);
        } else if (networkRequestType == NetworkRequestType.SEARCH_USER) {
            return searchQueryResults(networkRequest);
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
            messageService.addConnection(socketChannel);
        } catch (IllegalAccessException e) {
            ChatLogger.error(e.getMessage());
            return networkResponseFactory.createFailedResponse();
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }

        return networkResponseFactory.createSuccessfulResponse();
    }

    private NetworkResponse searchQueryResults(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            NetworkResponse response = userController.searchEntity(user.getUsername());
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleLoginRequest(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            NetworkResponse response = (new UserController()).loginUser(user);
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }

        private NetworkResponse handleCreateUserRequest (NetworkRequest networkRequest){
            try {
                User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
                NetworkResponse response = userController.addEntity(user);
                return response;
            } catch (IOException e) {
                return networkResponseFactory.createFailedResponse();
            }
        }
    }

    private NetworkResponse handleCreateUserRequest(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            NetworkResponse response = (new UserController()).addEntity(user);
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleCreateUserProfile(NetworkRequest networkRequest) {
        try {
            Profile profile = objectMapper.readValue(networkRequest.payload().jsonString(), Profile.class);
            NetworkResponse response = (new ProfileController()).addEntity(profile);
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleUpdateProfileObj(NetworkRequest networkRequest) {
        try {
            Profile profile = objectMapper.readValue(networkRequest.payload().jsonString(), Profile.class);
            NetworkResponse response = (new ProfileController()).updateEntity(profile);
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleUpdateUserProfileObj(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            NetworkResponse response = (new UserController()).updateEntity(user);
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleUpdatePasswordChange(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            NetworkResponse response = (new UserController()).updateEntity(user);
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleDeleteGroup(NetworkRequest networkRequest) {
        try {
            Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
            NetworkResponse response = (new GroupController()).deleteEntity(group);
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleJoinGroup(NetworkRequest networkRequest) {
        try {
            Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
            NetworkResponse response = (new GroupController()).joinGroup(group);
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }

    private NetworkResponse handleCreateGroup(NetworkRequest networkRequest) {
        try {
            Group group = objectMapper.readValue(networkRequest.payload().jsonString(), Group.class);
            List<User> moderators = group.getModerators();
            group.setModerators(null);
            NetworkResponse response = (new GroupController()).addEntity(group);
            if (response.status() == NetworkResponse.STATUS.SUCCESSFUL) {
                group.setModerators(moderators);
                NetworkResponse moderatorResponse = (new GroupController()).updateEntity(group);
                return moderatorResponse;
            }
            return response;
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
    }
