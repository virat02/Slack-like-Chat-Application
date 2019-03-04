package edu.northeastern.ccs.im.communications;

import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkRequestImpl;
import edu.northeastern.ccs.im.communication.Payload;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class NetworkRequestImplTests {
    NetworkRequest.NetworkRequestType networkRequestType;
    Payload payload;
    NetworkRequest networkRequest;
    @Before
    public void beforeTest()    {
        networkRequestType = mock(NetworkRequest.NetworkRequestType.class);
        payload = mock(Payload.class);
        networkRequest = new NetworkRequestImpl(networkRequestType, payload);
    }
    public void testWhenNetworkRequestInstantiatedShouldReturnNetworkRequestType()  {

    }
}
