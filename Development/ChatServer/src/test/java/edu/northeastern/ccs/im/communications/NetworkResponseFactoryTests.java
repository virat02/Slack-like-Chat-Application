package edu.northeastern.ccs.im.communications;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.NetworkResponseFactory;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Assert;
import org.junit.Test;

public class NetworkResponseFactoryTests {
    private NetworkResponseFactory networkResponseFactory = new NetworkResponseFactory();
    @Test
    public void testSuccessfulStatusWhenSuccessfulResponseIsCreated()   {
        NetworkResponse networkResponse = networkResponseFactory.createSuccessfulResponse();
        Assert.assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
    }

    @Test
    public void testFailedStatusWhenFailedResponseIsCreated()   {
        NetworkResponse networkResponse = networkResponseFactory.createFailedResponse();
        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }

    @Test
    public void testCreateFailedResponseWithPayload()   {
        NetworkResponse networkResponse =
                networkResponseFactory.createFailedResponseWithPayload(() -> {
                    User user = new User();
                    user.setUsername("test");
                    return CommunicationUtils.getObjectMapper().writeValueAsString(user);
                });
        Assert.assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
    }
}
