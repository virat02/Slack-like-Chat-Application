package edu.northeastern.ccs.im;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NetworkConnectionTests {

    @Mock
    private SocketChannel socketChannel;
    @Mock
    private Selector selector;
    private NetworkConnection networkConnection;
    @Mock
    private Message message;
    private SelectionKey selectionKey;

    @Before
    public void setUpTest() throws IOException {
        when(socketChannel.configureBlocking(anyBoolean())).thenReturn(socketChannel);
        networkConnection = new NetworkConnection(socketChannel);
    }

    @BeforeClass
    public static void disableLogging() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testMessageNumberOfRetriesShouldBe100WhenMessageIsFailingToBeWrittenToSocket() throws IOException {
        networkConnection.sendMessage(message);
        verify(socketChannel, times(100)).write(ByteBuffer.wrap(message.toString().getBytes()));
    }

    @Test
    public void testMessageSendMessageShouldReturnFalseWhenMessageIsFailedToBeWrittenToSocket() throws IOException {
        Assert.assertFalse(networkConnection.sendMessage(message));
    }

    @Test
    public void testMessageSendMessageShouldReturnTrueWhenMessageIsDeliveredToSocket() throws IOException {
        when(message.toString()).thenReturn("");
        Assert.assertTrue(networkConnection.sendMessage(message));
    }

    @Test(expected = AssertionError.class)
    public void testCloseAssertionFalse() throws IOException {
        doThrow(IOException.class)
                .when(socketChannel)
                .close();

        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.close();
    }

    @Test(expected = AssertionError.class)
    public void testNetworkConnectionInstantiationWhenChannelBlockingConfigurationFails() throws IOException {
        doThrow(IOException.class)
                .when(socketChannel).
                configureBlocking(anyBoolean());

        networkConnection = new NetworkConnection(socketChannel);
    }

    private void setUpIteratorTests(Answer<Void> answer) throws IOException {
        selectionKey = mock(SelectionKey.class);
        when(socketChannel.register(any(), anyInt())).thenReturn(selectionKey);
        when(selectionKey.isReadable()).thenReturn(true);
        when(selector.selectNow()).thenReturn(1);
        doAnswer(answer).when(socketChannel).read(any(ByteBuffer.class));
    }

    @Test
    public void testIteratorHasNextShouldReturnTrueWhenASingleMessageIsBroadCast() throws IOException {
        StringBuilder messages = new StringBuilder()
                .append(MessageType.BROADCAST.toString()).append(" ")
                .append("7 sibendu 17 Hello How Are You");
        setUpIteratorTests(invocation -> {
            ByteBuffer buffer = (ByteBuffer) invocation.getArguments()[0];
            buffer.put(messages.toString().getBytes());
            return null;
        });
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testIteratorHasNextShouldReturnTrueWhenTwoMessagesAreBroadCast() throws IOException {
        StringBuilder messages = new StringBuilder()
                .append(MessageType.BROADCAST.toString()).append(" ")
                .append("7 sibendu 17 Hello How Are You");

        messages.append(messages);
        setUpIteratorTests(invocation -> {
            ByteBuffer buffer = (ByteBuffer) invocation.getArguments()[0];
            buffer.put(messages.toString().getBytes());
            return null;
        });
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testIteratorHasNextShouldReturnMessageWithProperType() throws IOException {
        StringBuilder messages = new StringBuilder()
                .append(MessageType.BROADCAST.toString()).append(" ")
                .append("7 sibendu 17 Hello How Are You");

        messages.append(MessageType.BROADCAST.toString()).append(" ")
        .append("3 dey 9 I am good");
        setUpIteratorTests(invocation -> {
            ByteBuffer buffer = (ByteBuffer) invocation.getArguments()[0];
            buffer.put(messages.toString().getBytes());
            return null;
        });
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        iterator.hasNext();
        Message message1 = iterator.next();
        Assert.assertEquals("sibendu", message1.getName());
        Assert.assertEquals( "Hello How Are You", message1.getText());
        Assert.assertTrue(message1.isBroadcastMessage());
        Message message2 = iterator.next();
        Assert.assertEquals("dey", message2.getName());
        Assert.assertEquals("I am good", message2.getText());
        Assert.assertTrue(message2.isBroadcastMessage());
    }
}
