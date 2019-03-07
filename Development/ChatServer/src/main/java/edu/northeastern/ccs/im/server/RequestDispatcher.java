package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseFactory;
import edu.northeastern.ccs.im.controller.IController;
import edu.northeastern.ccs.im.controller.UserController;
import edu.northeastern.ccs.jpa.User;

import java.io.IOException;

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
        }

        return networkResponseFactory.createFailedResponse();
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
