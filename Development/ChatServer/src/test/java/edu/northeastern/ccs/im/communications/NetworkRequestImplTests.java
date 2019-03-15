package edu.northeastern.ccs.im.communications;

import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkRequestImpl;
import edu.northeastern.ccs.im.communication.Payload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;


/**
 * The type Network request impl tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class NetworkRequestImplTests {
    private Payload payload;
    private NetworkRequest networkRequest;

    /**
     * Before test.
     */
    @Before
    public void beforeTest()    {
        NetworkRequest.NetworkRequestType networkRequestType = NetworkRequest.NetworkRequestType.JOIN_GROUP;
        payload = mock(Payload.class);
        networkRequest = new NetworkRequestImpl(networkRequestType, payload);
    }

    /**
     * Test when network request instantiated should return correct network request type.
     */
    @Test
    public void testWhenNetworkRequestInstantiatedShouldReturnNetworkRequestType()  {
        Assert.assertEquals(NetworkRequest.NetworkRequestType.JOIN_GROUP, networkRequest.networkRequestType());
    }

    /**
     * Test when network request instantiated should return the set payload.
     */
    @Test
    public void testWhenNetworkRequestInstantiatedShouldReturnTheSetPayload()  {
        Assert.assertEquals(payload, networkRequest.payload());
    }
}
