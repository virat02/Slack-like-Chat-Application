package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.IController;
import edu.northeastern.ccs.im.controller.UserController;
import edu.northeastern.ccs.jpa.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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

    public NetworkResponse handleNetworkRequest(NetworkRequest networkRequest) {
        NetworkRequest.NetworkRequestType networkRequestType = networkRequest.networkRequestType();
        if (networkRequestType == NetworkRequest.NetworkRequestType.CREATE_USER) {
            return handleCreateUserRequest(networkRequest);
        } else if (networkRequestType == NetworkRequest.NetworkRequestType.LOGIN_USER) {
            return handleLoginRequest(networkRequest);
        } else if (networkRequestType == NetworkRequest.NetworkRequestType.SEARCH_USER) {
            return searchQueryResults(networkRequest);
        }

        return networkResponseFactory.createFailedResponse();
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
