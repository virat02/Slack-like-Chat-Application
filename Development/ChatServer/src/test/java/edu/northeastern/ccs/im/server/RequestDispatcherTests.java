package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.*;
import edu.northeastern.ccs.im.controller.GroupController;
import edu.northeastern.ccs.im.controller.ProfileController;
import edu.northeastern.ccs.im.controller.UserController;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotPresentInTheGroup;
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

/**
 * Request dispatcher tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestDispatcherTests {
    private RequestDispatcher requestDispatcher;
    private NetworkRequest networkRequest;
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
     * @throws JsonProcessingException    the json processing exception
     * @throws IllegalAccessException     the illegal access exception
     * @throws UserNotFoundException      the user not found exception
     * @throws UserNotPresentInTheGroup   the user not present in the group
     * @throws GroupNotFoundException     the group not found exception
     */
    @Before
    public void setup() throws UserNotFoundException,
            UserNotPresentInTheGroup,
            GroupNotFoundException {
        broadCastService = mock(MessageBroadCastService.class);
        messageManagerService = mock(MessageManagerService.class);
        when(messageManagerService.getService(anyString(), anyString(), anyBoolean())).thenReturn(broadCastService);
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
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestIfCreateUserResponseSuccessful() throws JsonProcessingException {
        initializeForUserEntityRequests();
        RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
        requestDispatcher.setUserController(userController);
        when(userController.addEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_USER);
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

    /**
     * When handle network request if create user throws io exception check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
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

    /**
     * When handle network request if login throws io exception check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
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

    /**
     * When handle network request if search query results throw exception check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
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
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"group1\", \"userName\": \"sibendu\", \"isPrivate\" : \"true\"}");
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
    public void whenHandleNetworkRequestIfJoinGroupIsUnSuccessfulDueToIOExceptionCheckNetworkResponse() throws IOException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"group1\", \"userName\": \"sibendu\", \"isPrivate\" : \"true\"}");
        requestDispatcher.setMessageManagerService(messageManagerService);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.JOIN_GROUP);
        doThrow(IOException.class).when(broadCastService).addConnection(mockSocketChannel);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }


    /**
     * When handle network request if join group is un successful due to user not found exception check network response.
     *
     * @throws IOException                the io exception
     * @throws UserNotFoundException      the user not found exception
     * @throws UserNotPresentInTheGroup   the user not present in the group
     * @throws GroupNotFoundException     the group not found exception
     */
    @Test
    public void whenHandleNetworkRequestIfJoinGroupIsUnSuccessfulDueToUserNotFoundExceptionCheckNetworkResponse()
            throws IOException, UserNotFoundException, UserNotPresentInTheGroup, GroupNotFoundException {
        final String userNotFound = "{\"message\" : \"The user doesn't exist in the system.\"}";
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"group1\", \"userName\": \"sibendu\", \"isPrivate\" : \"true\"}");
        requestDispatcher.setMessageManagerService(messageManagerService);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.JOIN_GROUP);
        doThrow(UserNotFoundException.class).when(messageManagerService).getService(anyString(), anyString(),anyBoolean());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
        Assert.assertEquals(networkResponse.payload().jsonString(), userNotFound);
    }

    /**
     * When handle network request if join group is un successful due to user not present in the group exception check network response.
     *
     * @throws IOException                the io exception
     * @throws UserNotFoundException      the user not found exception
     * @throws UserNotPresentInTheGroup   the user not present in the group
     * @throws GroupNotFoundException     the group not found exception
     */
    @Test
    public void whenHandleNetworkRequestIfJoinGroupIsUnSuccessfulDueToUserNotPresentInTheGroupExceptionCheckNetworkResponse()
            throws IOException, UserNotFoundException, UserNotPresentInTheGroup, GroupNotFoundException {
        final String userNotPresentInGroup = "{\"message\" : \"You are not a participant of the " +
                "group\"}";
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"group1\", \"userName\": \"sibendu\", \"isPrivate\" : \"true\"}");
        requestDispatcher.setMessageManagerService(messageManagerService);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.JOIN_GROUP);
        doThrow(UserNotPresentInTheGroup.class).when(messageManagerService).getService(anyString(), anyString(),anyBoolean());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
        Assert.assertEquals(networkResponse.payload().jsonString(), userNotPresentInGroup);
    }

    /**
     * When handle network request if join group is un successful due to group not found exception check network response.
     *
     * @throws IOException                the io exception
     * @throws UserNotFoundException      the user not found exception
     * @throws UserNotPresentInTheGroup   the user not present in the group
     * @throws GroupNotFoundException     the group not found exception
     */
    @Test
    public void whenHandleNetworkRequestIfJoinGroupIsUnSuccessfulDueToGroupNotFoundExceptionCheckNetworkResponse()
            throws IOException, UserNotFoundException, UserNotPresentInTheGroup, GroupNotFoundException {
        final String groupNotFound = "{\"message\" : \"The group doesn't exist. Please create a " +
                "group\"}";
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"group1\", \"userName\": \"sibendu\", \"isPrivate\" : \"true\"}");
        requestDispatcher.setMessageManagerService(messageManagerService);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.JOIN_GROUP);
        doThrow(GroupNotFoundException.class).when(messageManagerService).getService(anyString(), anyString(),anyBoolean());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
        Assert.assertEquals(networkResponse.payload().jsonString(), groupNotFound);
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

    /**
     * When handle network request get group query results is successful check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestGetGroupQueryResultsIsSuccessfulCheckNetworkResponse() throws JsonProcessingException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setGroupController(groupController);
        when(groupController.getEntity(anyString())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createSuccessfulResponse().status());
    }

    /**
     * When handle network request get group query results is failure check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestGetGroupQueryResultsIsFailureCheckNetworkResponse() throws JsonProcessingException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setGroupController(groupController);
        when(groupController.getEntity(anyString())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request get group query results throws io exception network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestGetGroupQueryResultsThrowsIOExceptionNetworkResponse() throws JsonProcessingException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setGroupController(groupController);
        doThrow(IOException.class).when(groupController).getEntity(anyString());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request update group query results is successful check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestUpdateGroupQueryResultsIsSuccessfulCheckNetworkResponse() throws JsonProcessingException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setGroupController(groupController);
        when(groupController.updateEntity(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createSuccessfulResponse().status());
    }

    /**
     * When handle network request update group query results is failure check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestUpdateGroupQueryResultsIsFailureCheckNetworkResponse() throws JsonProcessingException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setGroupController(groupController);
        when(groupController.updateEntity(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request update group query results throws io exception network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestUpdateGroupQueryResultsThrowsIOExceptionNetworkResponse() throws JsonProcessingException {
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"xyz\"}");
        requestDispatcher.setGroupController(groupController);
        doThrow(IOException.class).when(groupController).updateEntity(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_GROUP);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle update user profile obj is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleGetFollowersIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}");
        when(userController.viewFollowers(anyString())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_FOLLOWERS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request handle update user profile obj is failed check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleGetFollowersIsFailedCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}");
        when(userController.viewFollowers(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_FOLLOWERS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle update user profile obj throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleGetFollowersThrowsExceptionCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}");
        doThrow(IOException.class).when(userController).viewFollowers(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_FOLLOWERS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }


    /**
     * When handle network request handle get followees is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleGetFolloweesIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}");
        when(userController.viewFollowees(anyString())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_FOLLOWEES);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request handle get followees is failed check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleGetFolloweesIsFailedCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}");
        when(userController.viewFollowees(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_FOLLOWEES);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle get followees throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleGetFolloweesThrowsExceptionCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}");
        doThrow(IOException.class).when(userController).viewFollowees(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.GET_FOLLOWEES);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle set followers is successful check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestHandleSetFollowersIsSuccessfulCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}" + "\n" + "{\"id\": 1, \"username\": \"sibendu\"}");
        when(userController.followUser(anyString(), any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SET_FOLLOWERS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createSuccessfulResponse().status());
    }

    /**
     * When handle network request handle set followers parsed size is one check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestHandleSetFollowersParsedSizeIsOneCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}");
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SET_FOLLOWERS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createFailedResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request handle set followers throws exception network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestHandleSetFollowersThrowsExceptionNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}" + "\n" + "{\"id\": 1, \"username\": \"sibendu\"}");
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SET_FOLLOWERS);
        doThrow(IOException.class).when(userController).followUser(anyString(), any());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createFailedResponse().status(), networkResponse.status());
    }


    /**
     * When handle network request handle set un followers is successful check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestHandleSetUnFollowersIsSuccessfulCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}" + "\n" + "{\"id\": 1, \"username\": \"sibendu\"}");
        when(userController.unfollowUser(anyString(), any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SET_UNFOLLOWERS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createSuccessfulResponse().status());
    }

    /**
     * When handle network request handle set un followers parsed size is one check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestHandleSetUnFollowersParsedSizeIsOneCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}");
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SET_UNFOLLOWERS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createFailedResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request handle set un followers throws io exception check network response.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void whenHandleNetworkRequestHandleSetUnFollowersThrowsIOExceptionCheckNetworkResponse() throws JsonProcessingException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"id\": 1, \"username\": \"sibendu\"}" + "\n" + "{\"id\": 1, \"username\": \"sibendu\"}");
        doThrow(IOException.class).when(userController).unfollowUser(anyString(), any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.SET_UNFOLLOWERS);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle update invite request is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleUpdateInviteRequestIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.updateInvite(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_INVITE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request handle update invite request is failed check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleUpdateInviteRequestIsFailedCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.updateInvite(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_INVITE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle update invite request throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleUpdateInviteRequestThrowsExceptionCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        doThrow(IOException.class).when(userController).updateInvite(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.UPDATE_INVITE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle fetch inviations request is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleFetchInviationsRequestIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"group1\", \"userName\": \"sibendu\"}");
        when(userController.searchInviteByGroupCode(anyString(), anyString())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.FETCH_INVITE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request handle fetch inviations request is failed check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleFetchInviationsRequestIsFailedCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"group1\", \"userName\": \"sibendu\"}");
        when(userController.searchInviteByGroupCode(anyString(), anyString())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.FETCH_INVITE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle fetch inviations request throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleFetchInviationsRequestThrowsExceptionCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(payload.jsonString()).thenReturn("{\"groupCode\": \"group1\", \"userName\": \"sibendu\"}");
        doThrow(IOException.class).when(userController).searchInviteByGroupCode(anyString(), anyString());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.FETCH_INVITE);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle invitation request is successful check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleInvitationRequestIsSuccessfulCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.sendInvite(any())).thenReturn(networkResponseFactory.createSuccessfulResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.INVITE_USER);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponseFactory.createSuccessfulResponse().status(), networkResponse.status());
    }

    /**
     * When handle network request handle invitation request is failed check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleInvitationRequestIsFailedCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        when(userController.sendInvite(any())).thenReturn(networkResponseFactory.createFailedResponse());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.INVITE_USER);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

    /**
     * When handle network request handle invitation request throws exception check network response.
     *
     * @throws IOException the io exception
     */
    @Test
    public void whenHandleNetworkRequestHandleInvitationRequestThrowsExceptionCheckNetworkResponse() throws IOException {
        initializeForUserEntityRequests();
        doThrow(IOException.class).when(userController).sendInvite(any());
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.INVITE_USER);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest, mockSocketChannel);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }

}
