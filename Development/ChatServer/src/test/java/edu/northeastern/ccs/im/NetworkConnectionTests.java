package edu.northeastern.ccs.im;

import edu.northeastern.ccs.im.communication.CommunicationUtils;
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
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

/**
 * Tests the class NetworkConnection
 */
@RunWith(MockitoJUnitRunner.class)
public class NetworkConnectionTests {

    /***
     * Mocks the socketChannel and its behaviors required by
     * NetworkConnection
     */
    @Mock
    private SocketChannel socketChannel;
    /***
     * Mock the selector and its behaviors required by
     * Selector
     */
    @Mock
    private Selector selector;
    private NetworkConnection networkConnection;
    /***
     * Mocks a message
     */
    @Mock
    private Message message;

    private final static String senderName = "sibendu";
    private final static String groupName = "group1";

    /**
     * Sets up every test, with a mock blocking configuration
     *
     * @throws IOException the io exception
     */
    @Before
    public void setUpTest() throws IOException {
        when(socketChannel.configureBlocking(anyBoolean())).thenReturn(socketChannel);
        networkConnection = new NetworkConnection(socketChannel);
    }

    /**
     * Disable logging.
     */
    @BeforeClass
    public static void disableLogging() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Test sendMessage() should return false when message is failed to be written to socket.
     */
    @Test
    public void testMessageSendMessageShouldReturnFalseWhenMessageIsFailedToBeWrittenToSocket() {
        Assert.assertFalse(networkConnection.sendMessage(message));
    }

    /**
     * Test sendMessage() should return false channel::write throws IOException
     */
    @Test
    public void testMessageSendMessageShouldReturnFalseWhenSocketChannelWriteThrowsIOException() throws IOException {
        doThrow(IOException.class).when(socketChannel).write(any(ByteBuffer.class));
        Assert.assertFalse(networkConnection.sendMessage(message));
    }


    /**
     * If the socket or channel refuses to close,
     * an assertionError should be thrown
     *
     * @throws IOException the io exception
     */
    @Test(expected = AssertionError.class)
    public void testCloseAssertionFalse() throws IOException {
        doThrow(IOException.class)
                .when(socketChannel)
                .close();

        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.close();
    }

    /**
     * If the socket or channel closes successfully
     * no assertionError should be thrown
     *
     * @throws IOException the io exception
     */
    @Test
    public void testCloseAssertionIsNotFalse() throws IOException {
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.close();
    }

    /**
     * Test network connection instantiation when channel blocking configuration fails.
     *
     * @throws IOException the io exception
     */
    @Test(expected = AssertionError.class)
    public void testNetworkConnectionInstantiationWhenChannelBlockingConfigurationFails() throws IOException {
        doThrow(IOException.class).when(socketChannel).configureBlocking(anyBoolean());
        networkConnection = new NetworkConnection(socketChannel);
    }

    /***
     * Sets up mocking behavior for writing to a ByteBuffer
     * @param answer The mocking behavior
     * @throws IOException IOException is required by register() method
     */
    private void setUpIteratorTests(Answer<Void> answer) throws IOException {
        SelectionKey selectionKey = mock(SelectionKey.class);
        when(socketChannel.register(any(), anyInt())).thenReturn(selectionKey);
        when(selectionKey.isReadable()).thenReturn(true);
        when(selector.selectNow()).thenReturn(1);
        doAnswer(answer).when(socketChannel).read(any(ByteBuffer.class));
    }


    /**
     * Iterator::hasNext should return false when a single message is broad cast.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testIteratorHasNextShouldReturnFalseWhenAEmptyMessageIsBroadCast() throws IOException {
        setUpIteratorTests(invocation -> {
            return null;
        });
        when(selector.selectNow()).thenReturn(0);
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * selector::selectNow throwing IOException should be caught
     *
     * @throws IOException the io exception
     */
    @Test(expected = AssertionError.class)
    public void testIteratorWhenSelectorSelectNowThrowsIOException() throws IOException {
        setUpIteratorTests(invocation -> null);

        doThrow(IOException.class).when(selector).selectNow();
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * If there are no messages, NoSuchElementException should be thrown
     */
    @Test(expected = NoSuchElementException.class)
    public void testIteratorWhenNoMessagesArePresentInTheQueue() throws IOException {
        setUpIteratorTests(invocation -> {
            return null;
        });

        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        iterator.next();
    }

    /**
     * Iterator::hasNext should return true when a single message is broad cast.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testIteratorHasNextShouldReturnTrueWhenASingleMessageIsBroadCast() throws IOException {
        byte[] bytes = CommunicationUtils.toJson(Message.makeBroadcastMessage(senderName, "hello", groupName)).getBytes();
        setUpIteratorTests(invocation -> {
            ByteBuffer buffer = (ByteBuffer) invocation.getArguments()[0];
            buffer.put(bytes);
            return null;
        });
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        Assert.assertTrue(iterator.hasNext());
    }

    /**
     * Iterator::hasNext should return true when two messages are broad cast.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testIteratorHasNextShouldReturnTrueWhenTwoMessagesAreBroadCast() throws IOException {
        byte[] bytes = CommunicationUtils.toJson(Message.makeBroadcastMessage(senderName, "hello", groupName)).getBytes();
        setUpIteratorTests(invocation -> {
            ByteBuffer buffer = (ByteBuffer) invocation.getArguments()[0];
            buffer.put(bytes);
            buffer.put(bytes);
            return null;
        });
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
    }

    /**
     * Iterator::next should return messages with proper username, message, and broadcast type.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testIteratorHasNextShouldReturnMessageWithProperType() throws IOException {

        Message message1 = Message.makeBroadcastMessage(senderName, "Hello How Are You", groupName);
        Message message2 = Message.makeBroadcastMessage("dey", "I am good", groupName);

        byte[] bytes1 = CommunicationUtils.toJson(message1).getBytes();
        byte[] bytes2 = CommunicationUtils.toJson(message2).getBytes();
        setUpIteratorTests(invocation -> {
            ByteBuffer buffer = (ByteBuffer) invocation.getArguments()[0];
            buffer.put(bytes1);
            buffer.put(bytes2);
            return null;
        });
        networkConnection = new NetworkConnection(socketChannel);
        networkConnection.setSelector(selector);
        Iterator<Message> iterator = networkConnection.iterator();
        Assert.assertTrue(iterator.hasNext());
        Message message1Read = iterator.next();
        Assert.assertEquals(senderName, message1Read.getName());
        Assert.assertEquals("Hello How Are You", message1Read.getText());
        Assert.assertTrue(message1Read.isBroadcastMessage());
        Message message2Read = iterator.next();
        Assert.assertEquals("dey", message2Read.getName());
        Assert.assertEquals("I am good", message2Read.getText());
        Assert.assertTrue(message2Read.isBroadcastMessage());
    }
}
