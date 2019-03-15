package edu.northeastern.ccs.im.communications;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.im.userGroup.User;
//import edu.northeastern.ccs.jpa.User;
import org.junit.Assert;
import org.junit.Test;

public class NetworkRequestFactoryTests {
    private NetworkRequestFactory networkRequestFactory = new NetworkRequestFactory();
    @Test
    public void whenCreateUserRequestIsCalledJsonPayloadShouldBeSame() throws JsonProcessingException {
        String name = "Sibendu";
        String password = "password";
        NetworkRequest networkRequest = networkRequestFactory.createUserRequest(name, password);
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }
}
