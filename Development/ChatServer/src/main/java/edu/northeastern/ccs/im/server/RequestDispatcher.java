package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.IController;
import edu.northeastern.ccs.im.controller.UserController;
import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.jpa.Message;
import edu.northeastern.ccs.jpa.User;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;

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
    private GroupService groupService = new GroupService();

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
        NetworkRequest.NetworkRequestType networkRequestType = networkRequest.networkRequestType();
        if (networkRequestType == NetworkRequestType.CREATE_USER) {
            return handleCreateUserRequest(networkRequest);
        } else if (networkRequestType == NetworkRequestType.LOGIN_USER) {
            return handleLoginRequest(networkRequest);
        } else if (networkRequestType == NetworkRequestType.SEARCH_USER) {
            return searchQueryResults(networkRequest);
        } else if (networkRequestType == NetworkRequestType.JOIN_GROUP) {
            groupService.addConnection(socketChannel);
        } else if (networkRequestType == NetworkRequestType.SEND_MESSAGE) {
            try {
                Message message = parseMessage(networkRequest);
                groupService.receiveMessage(message);
            } catch (IOException e) {
                ChatLogger.error(e.getMessage());
            }
        }


        return networkResponseFactory.createFailedResponse();
    }

    private Message parseMessage(NetworkRequest networkRequest) throws IOException {
        Message message = objectMapper.readValue(networkRequest.payload().jsonString(), Message.class);
        return message;
    }

    private NetworkResponse searchQueryResults(NetworkRequest networkRequest) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL,
                () -> CommunicationUtils.getObjectMapper().writeValueAsString(new ArrayList<>(Arrays.asList("sibendu", "tarun", "sangeetha", "jerry", "virat"))));
    }

    private NetworkResponse handleLoginRequest(NetworkRequest networkRequest) {
        return handleCreateUserRequest(networkRequest);
    }

    private NetworkResponse handleCreateUserRequest(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
//            userController.addIUserGroup(user);
        } catch (IOException e) {
            return networkResponseFactory.createFailedResponse();
        }
        return networkResponseFactory.createSuccessfulResponse();
    }
}
