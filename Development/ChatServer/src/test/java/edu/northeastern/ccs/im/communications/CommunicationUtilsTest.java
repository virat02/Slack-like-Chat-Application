package edu.northeastern.ccs.im.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkRequest;
import edu.northeastern.ccs.im.communication.NetworkRequestFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.*;

public class CommunicationUtilsTest {

    private SocketChannel socketChannel;
    private Message message;

    @Before
    public void setup() {
        socketChannel = mock(SocketChannel.class);
        message = mock(Message.class);
    }

    @Test
    public void testMultipleCallsShouldReturnSameObjectMapperInstance() {
        ObjectMapper objectMapper1 = CommunicationUtils.getObjectMapper();
        ObjectMapper objectMapper2 = CommunicationUtils.getObjectMapper();
        ObjectMapper objectMapper3 = CommunicationUtils.getObjectMapper();
        Assert.assertTrue(objectMapper1 == objectMapper2 && objectMapper2 == objectMapper3);
    }

    /**
     * Tests number of retries should be 100 when message is failing to be written to socket.
     *
     * @throws IOException as required by SocketChannel::write method
     */
    @Test
    public void testWriteToChanneleNumberOfRetriesShouldBe100WhenMessageIsFailingToBeWrittenToSocket() throws IOException {
        CommunicationUtils.writeToChannel(socketChannel, message);
        verify(socketChannel, times(100)).write(any(ByteBuffer.class));
    }

    /**
     * Test writeToChannel() should return false when message is failed to be written to socket.
     */
    @Test
    public void testWriteToChannelMessageShouldReturnFalseWhenMessageIsFailedToBeWrittenToSocket() {
        Assert.assertFalse(CommunicationUtils.writeToChannel(socketChannel, message));
    }

    /**
     * Test writeToChannel() should return false channel::write throws IOException
     */
    @Test
    public void testWriteToChannelShouldReturnFalseWhenSocketChannelWriteThrowsIOException() throws IOException {
        doThrow(IOException.class).when(socketChannel).write(any(ByteBuffer.class));
        Assert.assertFalse(CommunicationUtils.writeToChannel(socketChannel, message));
    }

    /**
     * Test writeToChannel() should return true when message is delivered to socket.
     */
    @Test
    public void testMessageSendMessageShouldReturnTrueWhenMessageIsDeliveredToSocket() throws IOException {
        message = Message.makeBroadcastMessage("", "", "");
        doAnswer(invocationOnMock -> {
            ByteBuffer byteBuffer = (ByteBuffer) invocationOnMock.getArguments()[0];
            byteBuffer.position(43);
            return 43;
        }).when(socketChannel).write(any(ByteBuffer.class));
        Assert.assertTrue(CommunicationUtils.writeToChannel(socketChannel, message));
    }

    @Test
    public void testNetworkRequestReadFromSocketShouldReturnCreateUserRequest() throws IOException {
        NetworkRequestFactory networkRequestFactory = new NetworkRequestFactory();
        doAnswer(invocationOnMock -> {
            ByteBuffer byteBuffer = (ByteBuffer) invocationOnMock.getArguments()[0];
            byteBuffer.put(CommunicationUtils.getObjectMapper().writeValueAsBytes(networkRequestFactory.createUserRequest("", "")));
            return 100;
        }).when(socketChannel).read(any(ByteBuffer.class));
        NetworkRequest networkRequest  = CommunicationUtils.networkRequestReadFromSocket(socketChannel);
        Assert.assertEquals(NetworkRequest.NetworkRequestType.CREATE_USER, networkRequest.networkRequestType());
    }
}
