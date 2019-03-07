package edu.northeastern.ccs.im.communications;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import edu.northeastern.ccs.jpa.User;
import org.junit.Assert;
import org.junit.Test;

public class NetworkRequestFactoryTests {
    private NetworkRequestFactory networkRequestFactory = new NetworkRequestFactory();
    @Test
    public void whenCreateUserRequestIsCalledJsonPayloadShouldBeSame() throws JsonProcessingException {
        String name = "Sibendu";
        String email = "sibendu.dey@gmail.com";
        NetworkRequest networkRequest = networkRequestFactory.createUserRequest(name, email);
        User user = new User();
        //The below line gave an error
        //User user = new User(name, email);
        String expectedJSON = CommunicationUtils.getObjectMapper().writeValueAsString(user);
        Assert.assertEquals(expectedJSON, networkRequest.payload().jsonString());
    }
}
