package edu.northeastern.ccs.im.server;

import org.junit.Assert;
import org.junit.Test;

public class RequestHandlerTests {
    @Test
    public void requestHandlerIsSingleton(){
        RequestHandler requestHandler1 = RequestHandler.getInstance();
        RequestHandler requestHandler2 = RequestHandler.getInstance();
        Assert.assertEquals(requestHandler1, requestHandler2);
    }
}