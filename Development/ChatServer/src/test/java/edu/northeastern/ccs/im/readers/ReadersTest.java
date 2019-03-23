package edu.northeastern.ccs.im.readers;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class ReadersTest {
    private JsonBufferReader jsonBufferReader;
    private Message message1;
    private Message message2;

    @Before
    public void setup() {
        jsonBufferReader = new JsonBufferReaderImpl();
    }

    @Test
    public void testBytesReadWhenMessageListHasNotBeenCalled() {
        Assert.assertEquals(0, jsonBufferReader.bytesRead());
    }

    @Test
    public void whenASingleMessageIsWrittenToBuffer() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        message1 = Message.makeBroadcastMessage("sibendu", "test", "group1");
        byteBuffer.put(CommunicationUtils.getObjectMapper().writeValueAsBytes(message1));
        byteBuffer.flip();
        List<Message> messageList = jsonBufferReader.messageList(byteBuffer);
        Assert.assertEquals(1, messageList.size());
        Message message = messageList.get(0);
        Assert.assertEquals(message1, message);
    }

    @Test
    public void whenTwoMessagesAreWrittenToBuffer() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        message1 = Message.makeBroadcastMessage("sibendu", "test", "group1");
        byteBuffer.put(CommunicationUtils.getObjectMapper().writeValueAsBytes(message1));
        byteBuffer.put(CommunicationUtils.getObjectMapper().writeValueAsBytes(message1));
        byteBuffer.flip();
        List<Message> messageList = jsonBufferReader.messageList(byteBuffer);
        Assert.assertEquals(2, messageList.size());
        Message actualMessage1 = messageList.get(0);
        Message actualMessage2 = messageList.get(1);
        Assert.assertEquals(actualMessage1, message1);
        Assert.assertEquals(actualMessage2, message1);
    }

    @Test
    public void whenTwoDifferentMessagesAreWrittenToBuffer() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        message1 = Message.makeBroadcastMessage("sibendu", "test", "group1");
        message2 = Message.makeBroadcastMessage("sibendu2", "test2", "group2");
        byteBuffer.put(CommunicationUtils.getObjectMapper().writeValueAsBytes(message1));
        byteBuffer.put(CommunicationUtils.getObjectMapper().writeValueAsBytes(message2));
        byteBuffer.flip();
        List<Message> messageList = jsonBufferReader.messageList(byteBuffer);
        Assert.assertEquals(2, messageList.size());
        Message actualMessage1 = messageList.get(0);
        Message actualMessage2 = messageList.get(1);
        Assert.assertEquals(actualMessage1, message1);
        Assert.assertEquals(actualMessage2, message2);
    }

    @Test
    public void whenTwoAndAPartialMessageAreWrittenToBuffer() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        message1 = Message.makeBroadcastMessage("sibendu", "test", "group1");
        message2 = Message.makeBroadcastMessage("sibendu2", "test2", "group2");
        byteBuffer.put(CommunicationUtils.getObjectMapper().writeValueAsBytes(message1));
        byteBuffer.put(CommunicationUtils.getObjectMapper().writeValueAsBytes(message2));
        byteBuffer.put(Arrays.copyOfRange(CommunicationUtils.getObjectMapper().writeValueAsBytes(message1), 0, 9));
        byteBuffer.flip();
        List<Message> messageList = jsonBufferReader.messageList(byteBuffer);
        Assert.assertEquals(2, messageList.size());
        Message actualMessage1 = messageList.get(0);
        Message actualMessage2 = messageList.get(1);
        Assert.assertEquals(actualMessage1, message1);
        Assert.assertEquals(actualMessage2, message2);
    }
}
