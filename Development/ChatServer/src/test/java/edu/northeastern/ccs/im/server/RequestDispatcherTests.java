package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseFactory;
import edu.northeastern.ccs.im.communication.Payload;
import edu.northeastern.ccs.im.controller.IController;
import edu.northeastern.ccs.im.controller.UserController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestDispatcherTests {
    private NetworkRequest networkRequest;
    private Payload payload;
    private IController userController;
    private NetworkResponseFactory networkResponseFactory = new NetworkResponseFactory();

    @Before
    public void setup() throws JsonProcessingException {
        networkRequest = mock(NetworkRequest.class);
        payload = mock(Payload.class);
        userController = mock(UserController.class);
        when(networkRequest.payload()).thenReturn(payload);
        when(networkRequest.networkRequestType()).thenReturn(NetworkRequest.NetworkRequestType.CREATE_USER);
        when(payload.jsonString()).thenReturn("{\"id\":0,\"name\":\"tarun\",\"email\":\"tarungmailcom\",\"messages\":[],\"groups\":[],\"profile\":null}");
    }

    @Test
    public void whenHandleNetworkRequestIfOperationSucceedResponseSuccessful() {
        RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
        requestDispatcher.setUserController(userController);
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createSuccessfulResponse().status());
    }

    @Test
    public void whenHandleNetworkRequestIfOperationNotSuccessfulReturnFailedResponse() {
        RequestDispatcher requestDispatcher = RequestDispatcher.getInstance();
        requestDispatcher.setUserController(userController);
        doThrow(IOException.class).when(userController).addIUserGroup(any());
        NetworkResponse networkResponse = requestDispatcher.handleNetworkRequest(networkRequest);
        Assert.assertEquals(networkResponse.status(), networkResponseFactory.createFailedResponse().status());
    }
}
