package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.GroupController;
import edu.northeastern.ccs.im.controller.IController;
import edu.northeastern.ccs.im.controller.ProfileController;
import edu.northeastern.ccs.im.controller.UserController;
import edu.northeastern.ccs.im.service.BroadCastService;
import edu.northeastern.ccs.im.service.MessageBroadCastService;
import edu.northeastern.ccs.im.service.MessageManagerService;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Profile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The type Request dispatcher tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestDispatcherTests {
    private RequestDispatcher requestDispatcher;
    private NetworkRequest networkRequest;
    private NetworkRequestFactory networkRequestFactory = new NetworkRequestFactory();
    private Payload payload;
    private UserController userController;
    private SocketChannel mockSocketChannel;
    private NetworkResponseFactory networkResponseFactory = new NetworkResponseFactory();
    private MessageManagerService messageManagerService;
    private BroadCastService broadCastService;
    private ProfileController profileController;
    private GroupController groupController;

    /**
     * Sets up the tests.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws IllegalAccessException  the illegal access exception
     */
    @Before
    public void setup() throws JsonProcessingException, IllegalAccessException {
        broadCastService = mock(MessageBroadCastService.class);
        messageManagerService = mock(MessageManagerService.class);
        when(messageManagerService.getService(anyString())).thenReturn(broadCastService);
        requestDispatcher = RequestDispatcher.getInstance();
        networkRequest = mock(NetworkRequest.class);
        payload = mock(Payload.class);
        userController = mock(UserController.class);
        profileController = mock(ProfileController.class);
        groupController = mock(GroupController.class);
        mockSocketChannel = mock(SocketChannel.class);
        when(networkRequest.payload()).thenReturn(payload);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_USER);
    }

    /**
     * When handle network request if create user response successful.
     */
    @Test
    public void whenHandleNetworkRequestIfCreateUserResponseSuccessful() {
        networkRequest = networkRequestFactory.createUserRequest("123", "password", "emailaddress");
        RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
        requestDispatcher.setUserController(userController);
        when(userController.addEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request if create user return failed response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestIfCreateUserReturnFailedResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_USER);
        when(userController.addEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createFailedResponse().status(), networkResponse.status());
    }

    @Test
    public void whenHandleNetworkRequestIfCreateUserThrowsIOExceptionCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_USER);
        doThrow(IOException.class).when(userController).addEntity(any());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createFailedResponse().status(), networkResponse.status());
    }

    private void initializeForUserEntityRequests() throws JsonProcessingException {
        requestDispatcher.setUserController(userController);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.LOGIN_USER);
        when(payload.jsonString()).thenReturn("{\"id\": 1}");
    }

    /**
     * When handle network request if login is successful check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestIfLoginIsSuccessfulCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(userController.loginUser(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request if login failed check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestIfLoginFailedCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(userController.loginUser(any())).thenReturn(networkResponseFactory.createFailedResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    @Test
    public void whenHandleNetworkRequestIfLoginThrowsIOExceptionCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        doThrow(IOException.class).when(userController).loginUser(any());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request if search user is successful check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestIfSearchUserIsSuccessfulCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SEARCH_USER);
        when(userController.searchEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request if search user failed check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestIfSearchUserFailedCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SEARCH_USER);
        when(userController.searchEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    @Test
    public void whenHandleNetworkRequestIfSearchQueryResultsThrowExceptionCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"userName\": \"sibendu\"}");
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SEARCH_USER);
        doThrow(IOException.class).when(userController).searchEntity(anyString());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request if join group is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestIfJoinGroupIsSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setMessageManagerService(messageManagerService);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.JOIN_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request if join group is un successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestIfJoinGroupIsUnSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setMessageManagerService(messageManagerService);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.JOIN_GROUP);
        doThrow(IOException.class).when(broadCastService).addConnection(mockSocketChannel);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request if create user profile is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestIfCreateUserProfileIsSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"email\": \"s@s.com\"}");
        requestDispatcher.setProfileController(profileController);
        when(profileController.addEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_PROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request if create user profile is failed check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestIfCreateUserProfileIsFailedCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"email\": \"s@s.com\"}");
        requestDispatcher.setProfileController(profileController);
        when(profileController.addEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_PROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request if create user profile throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestIfCreateUserProfileThrowsExceptionCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"email\": \"s@s.com\"}");
        requestDispatcher.setProfileController(profileController);
        doThrow(IOException.class).when(profileController).addEntity(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_PROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle update user profile obj is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleUpdateUserProfileObjIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.updateEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_USERPROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request handle update user profile obj is failed check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleUpdateUserProfileObjIsFailedCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.updateEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_USERPROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle update user profile obj throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleUpdateUserProfileObjThrowsExceptionCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        doThrow(IOException.class).when(userController).updateEntity(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_USERPROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request if update user profile is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestIfUpdateUserProfileIsSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"email\": \"s@s.com\"}");
        requestDispatcher.setProfileController(profileController);
        when(profileController.updateEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_PROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request if update user profile is failed check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestIfUpdateUserProfileIsFailedCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"email\": \"s@s.com\"}");
        requestDispatcher.setProfileController(profileController);
        when(profileController.updateEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_PROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request if update user profile throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestIfUpdateUserProfileThrowsExceptionCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"email\": \"s@s.com\"}");
        requestDispatcher.setProfileController(profileController);
        doThrow(IOException.class).when(profileController).updateEntity(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_PROFILE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle update password change is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleUpdatePasswordChangeIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.updateEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_PASSWORD);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle update password change throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleUpdatePasswordChangeThrowsExceptionCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        doThrow(IOException.class).when(userController).updateEntity(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_PASSWORD);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle delete group is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleDeleteGroupIsSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"id\": 1}");
        requestDispatcher.setGroupController(groupController);
        when(groupController.deleteEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.DELETE_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createSuccessfulResponse().status());
    }

    /**
     * When handle network request handle delete group throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleDeleteGroupThrowsExceptionCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"id\": 1}");
        requestDispatcher.setGroupController(groupController);
        doThrow(IOException.class).when(groupController).deleteEntity(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.DELETE_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }


    /**
     * When handle network request handle create group is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleCreateGroupIsSuccessfulCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"id\": 1}");
        requestDispatcher.setGroupController(groupController);
        when(groupController.addEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(groupController.updateEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createSuccessfulResponse().status());
    }

    /**
     * When handle network request handle create group throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleCreateGroupThrowsExceptionCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"id\": 1}");
        requestDispatcher.setGroupController(groupController);
        doThrow(IOException.class).when(groupController).addEntity(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

}
