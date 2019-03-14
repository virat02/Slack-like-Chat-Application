package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.IController;
import edu.northeastern.ccs.im.controller.UserController;
import edu.northeastern.ccs.im.service.BroadCastService;
import edu.northeastern.ccs.im.service.MessageBroadCastService;
import edu.northeastern.ccs.im.service.MessageManagerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestDispatcherTests {
    private RequestDispatcher requestDispatcher;
    private NetworkRequest networkRequest;
    private NetworkRequestFactory networkRequestFactory = new NetworkRequestFactory();
    private Payload payload;
    private IController userController;
    private SocketChannel mockSocketChannel;
    private NetworkResponseFactory networkResponseFactory = new NetworkResponseFactory();
    private MessageManagerService messageManagerService;
    private BroadCastService broadCastService;

    @Before
    public void setup() throws JsonProcessingException, IllegalAccessException {
        broadCastService = mock(MessageBroadCastService.class);
        messageManagerService = mock(MessageManagerService.class);
        when(messageManagerService.getService(anyString())).thenReturn(broadCastService);
        requestDispatcher = RequestDispatcher.getInstance();
        networkRequest = mock(NetworkRequest.class);
        payload = mock(Payload.class);
        userController = mock(UserController.class);
        mockSocketChannel = mock(SocketChannel.class);
        when(networkRequest.payload()).thenReturn(payload);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_USER);
    }

    @Test
    public void whenHandleNetworkRequestIfCreateUserResponseSuccessful() {
        networkRequest = networkRequestFactory.createUserRequest("123", "password", "emailaddress");
        RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
        requestDispatcher.setUserController(userController);
        when(userController.addEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    @Test
    public void whenHandleNetworkRequestIfCreateUserReturnFailedResponse() {
        RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_USER);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createFailedResponse().status(), networkResponse.status());
    }

    private void initializeForUserEntityRequests() throws JsonProcessingException {
        requestDispatcher.setUserController(userController);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.LOGIN_USER);
        when(payload.jsonString()).thenReturn("{\"id\": 1}");
    }

    @Test
    public void whenHandleNetworkRequestIfLoginIsSuccessfulCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(userController.addEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    @Test
    public void whenHandleNetworkRequestIfLoginFailedCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(userController.addEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    @Test
    public void whenHandleNetworkRequestIfSearchUserIsSuccessfulCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SEARCH_USER);
        when(userController.searchEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    @Test
    public void whenHandleNetworkRequestIfSearchUserFailedCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SEARCH_USER);
        when(userController.searchEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    @Test
    public void whenHandleNetworkRequestIfJoinGroupIsSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setMessageManagerService(messageManagerService);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.JOIN_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    @Test
    public void whenHandleNetworkRequestIfJoinGroupIsUnSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setMessageManagerService(messageManagerService);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.JOIN_GROUP);
        doThrow(IOException.class).when(broadCastService).addConnection(mockSocketChannel);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    @Test
    public void whenHandleNetworkRequestIfUpdateUserNameIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.updateEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_USERNAME);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    @Test
    public void whenHandleNetworkRequestIfUpdateUserNameIsUnSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        when(userController.updateEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_USERNAME);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    @Test
    public void whenHandleNetworkRequestIfUpdateUserStatusIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.updateEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_USERSTATUS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    @Test
    public void whenHandleNetworkRequestIfUpdateUserStatusIsUnSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        when(userController.updateEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_USERSTATUS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }
}
