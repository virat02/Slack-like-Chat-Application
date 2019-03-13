package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.GroupController;
import edu.northeastern.ccs.im.controller.IController;
import edu.northeastern.ccs.im.controller.UserController;

import edu.northeastern.ccs.im.service.MessageBroadCastService;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Message;
import edu.northeastern.ccs.im.userGroup.User;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.northeastern.ccs.im.communication.NetworkRequest.NetworkRequestType;

public class RequestDispatcher {

  private static RequestDispatcher requestDispatcher = new RequestDispatcher();

  private RequestDispatcher() {

  }

  public static RequestDispatcher getInstance() {
    return requestDispatcher;
  }

  private ObjectMapper objectMapper = new ObjectMapper();
  private IController userController = new UserController();
  private NetworkResponseFactory networkResponseFactory = new NetworkResponseFactory();
  private MessageBroadCastService messageBroadCastService = new MessageBroadCastService();

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
      try {
        messageBroadCastService.addConnection(socketChannel);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (networkRequestType == NetworkRequestType.SEND_MESSAGE) {
      try {
        Message message = parseMessage(networkRequest);
//                groupService.receiveMessage(message);
      } catch (IOException e) {
        return networkResponseFactory.createFailedResponse();
      }
    } else if (networkRequestType == NetworkRequestType.UPDATE_USERNAME) {
      return handleUpdateUserName(networkRequest);
    } else if (networkRequestType == NetworkRequestType.UPDATE_USERSTATUS) {
      return handleUpdateUserStatus(networkRequest);
    } else if (networkRequestType == NetworkRequestType.CREATE_GROUP) {
      return handleCreateGroup(networkRequest);
    } else if (networkRequestType == NetworkRequestType.DELETE_GROUP) {
      return handleDeleteGroup(networkRequest);
    }

    return networkResponseFactory.createFailedResponse();
  }

  private Message parseMessage(NetworkRequest networkRequest) throws IOException {
    Message message = objectMapper.readValue(networkRequest.payload().jsonString(), Message.class);
    return message;
  }

  private NetworkResponse searchQueryResults(NetworkRequest networkRequest) {
    try {
      User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
      NetworkResponse response = (new UserController()).searchEntity(user.getUsername());
      return response;
    } catch (IOException e) {
      return networkResponseFactory.createFailedResponse();
    }
//    return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
//            () -> CommunicationUtils.getObjectMapper().writeValueAsString(new ArrayList<>(Arrays.asList("sibendu", "tarun", "sangeetha", "jerry", "virat"))));
  }

  private NetworkResponse handleLoginRequest(NetworkRequest networkRequest) {
    try {
      User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
      NetworkResponse response = (new UserController()).addEntity(user);
      return response;
    } catch (IOException e) {
      return networkResponseFactory.createFailedResponse();
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

  private NetworkResponse handleUpdateUserName(NetworkRequest networkRequest) {
    try {
      User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
      NetworkResponse response = (new UserController()).updateEntity(user);
      return response;
    } catch (IOException e) {
      return networkResponseFactory.createFailedResponse();
    }
  }

  private NetworkResponse handleUpdateUserStatus(NetworkRequest networkRequest) {
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
      User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
    } catch (IOException e) {
      return networkResponseFactory.createFailedResponse();
    }
    return networkResponseFactory.createSuccessfulResponse();
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
}
