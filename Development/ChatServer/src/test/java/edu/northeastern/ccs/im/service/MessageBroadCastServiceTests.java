package edu.northeastern.ccs.im.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.*;

public class MessageBroadCastServiceTests {
    private SocketChannel socketChannel;
    private BroadCastService broadCastService;
    @Before
    public void setup() {
        socketChannel = mock(SocketChannel.class);
        broadCastService = new MessageBroadCastService();
    }
    @Test
    public void shouldAddClientToActiveList() throws IOException, InterruptedException {
        broadCastService.addConnection(socketChannel);
        Assert.assertEquals(true, broadCastService.isClientActive());
    }
}
