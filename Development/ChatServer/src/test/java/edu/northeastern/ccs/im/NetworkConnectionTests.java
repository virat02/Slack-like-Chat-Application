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

    private String messageString = "7 sibendu 17 Hello How Are You";

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
     * Tests number of retries should be 100 when message is failing to be written to socket.
     *
     * @throws IOException as required by SocketChannel::write method
     */
    @Test
    public void testMessageNumberOfRetriesShouldBe100WhenMessageIsFailingToBeWrittenToSocket() throws IOException {
        networkConnection.sendMessage(message);
        verify(socketChannel, times(100)).write(ByteBuffer.wrap(message.toString().getBytes()));
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
     * Test sendMessage() should return true when message is delivered to socket.
     */
    @Test
    public void testMessageSendMessageShouldReturnTrueWhenMessageIsDeliveredToSocket() {
        when(message.toString()).thenReturn("");
        Assert.assertTrue(networkConnection.sendMessage(message));
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
        setUpIteratorTests(invocation -> {
            return null;
        });

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
        StringBuilder messages = new StringBuilder()
                .append(MessageType.BROADCAST.toString()).append(" ")
                .append(messageString);
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

    /**
     * Iterator::hasNext should return true when two messages are broad cast.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testIteratorHasNextShouldReturnTrueWhenTwoMessagesAreBroadCast() throws IOException {
        StringBuilder messages = new StringBuilder()
                .append(MessageType.BROADCAST.toString()).append(" ")
                .append(messageString);

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

    /**
     * Iterator::next should return messages with proper username, message, and broadcast type.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testIteratorHasNextShouldReturnMessageWithProperType() throws IOException {
        StringBuilder messages = new StringBuilder()
                .append(MessageType.BROADCAST.toString()).append(" ")
                .append(messageString);

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
        Assert.assertTrue(iterator.hasNext());
        Message message1 = iterator.next();
        Assert.assertEquals("sibendu", message1.getName());
        Assert.assertEquals("Hello How Are You", message1.getText());
        Assert.assertTrue(message1.isBroadcastMessage());
        Message message2 = iterator.next();
        Assert.assertEquals("dey", message2.getName());
        Assert.assertEquals("I am good", message2.getText());
        Assert.assertTrue(message2.isBroadcastMessage());
    }
}
