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
    private IController iController = new UserController();
    private NetworkResponseFactory networkResponseFactory = new NetworkResponseFactory();

    public NetworkResponse handleNetworkRequest(NetworkRequest networkRequest) {
        NetworkRequest.NetworkRequestType networkRequestType = networkRequest.networkRequestType();
        if (networkRequestType == NetworkRequest.NetworkRequestType.CREATE_USER) {
            return handleCreateUseRequest(networkRequest);
        }

        return networkResponseFactory.createFailedResponse();
    }

    private NetworkResponse handleCreateUseRequest(NetworkRequest networkRequest) {
        try {
            User user = objectMapper.readValue(networkRequest.payload().jsonString(), User.class);
            iController.addIUserGroup(user);
        } catch (IOException e) {
            e.printStackTrace();
            return networkResponseFactory.createFailedResponse();
        }
        return networkResponseFactory.createSuccessfulResponse();
    }
}
