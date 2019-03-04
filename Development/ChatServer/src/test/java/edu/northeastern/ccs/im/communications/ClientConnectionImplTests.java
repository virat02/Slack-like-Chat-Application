package edu.northeastern.ccs.im.communications;

import edu.northeastern.ccs.im.communication.ClientConnection;
import edu.northeastern.ccs.im.communication.ClientConnectionImpl;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.SocketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientConnectionImplTests {
    private SocketChannel mockSocketChannel;
    private ClientConnection clientConnection;
    private NetworkRequest networkRequest;
    private SocketFactory socketFactory;
    private static final String HOST = "localhost";
    private static Integer PORT = 4545;

    @Before
    public void beforeTest() throws IOException {
        mockSocketChannel = mock(SocketChannel.class);
        socketFactory = mock(SocketFactory.class);
        mockSocketChannel = mock(SocketChannel.class);
        try {
            when(socketFactory.createSocket()).thenReturn(mockSocketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        networkRequest = mock(NetworkRequest.class);
        clientConnection = new ClientConnectionImpl(HOST, PORT, socketFactory);
        clientConnection.connect();
    }

    @Test
    public void whenConnectCalledNoExceptionShouldBeThrown() throws IOException {
        verify(mockSocketChannel, times(1)).connect(any());
    }

    @Test
    public void whenSendRequestIsCalledChannelWriteMethodIsCalled() throws IOException {
        clientConnection.sendRequest(mock(NetworkRequest.class));
        verify(mockSocketChannel, times(1)).write(any(ByteBuffer.class));
    }

    @Test
    public void whenCloseIsCalledChannelChannelCloseMethodShouldBeCalled() throws IOException {
        clientConnection.close();
        verify(mockSocketChannel, times(1)).close();
    }
}
