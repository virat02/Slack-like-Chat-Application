package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.user_group.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * The type Message broad cast service tests.
 */
public class MessageBroadCastServiceTests {
    private SocketChannel socketChannel;
    private BroadCastService broadCastService;
    private MessageService messageService;
    private Message message;


    private ClientRunnable clientRunnable;

    /**
     * Sets .
     */
    @Before
    public void setup() {
        clientRunnable = mock(ClientRunnable.class);
        messageService = mock(MessageService.class);
        message = mock(Message.class);
        socketChannel = mock(SocketChannel.class);
        broadCastService = new MessageBroadCastService();
        ((MessageBroadCastService) broadCastService).setMessageService(messageService);
    }

    /**
     * Should add client to active list and assert true that atleast
     * one client is added
     *
     * @throws IOException the io exception
     */
    @Test
    public void shouldAddClientToActiveList() throws IOException {
        broadCastService.addConnection(socketChannel);
        Assert.assertTrue(broadCastService.isClientActive());
    }

    /**
     * Should broad cast message if message is broadcast and message is persistent.
     *
     * @throws IOException the io exception
     */
    @Test
    public void shouldBroadCastMessageIfMessageIsBroadcastAndMessageIsPersistent() throws IOException {
        broadCastService.addConnection(socketChannel);
        when(message.isBroadcastMessage()).thenReturn(true);
        when(messageService.createMessage(anyString(), anyString(), anyString())).thenReturn(true);
        broadCastService.broadcastMessage(message);
    }

    /**
     * Should not broad cast message if message is not broadcast and dont call message service.
     *
     * @throws IOException the io exception
     */
    @Test
    public void shouldNotBroadCastMessageIfMessageIsNotBroadcastAndDontCallMessageService() throws IOException {
        broadCastService.addConnection(socketChannel);
        when(message.isBroadcastMessage()).thenReturn(false);
        verify(messageService, times(0)).createMessage(anyString(), anyString(), anyString());
        broadCastService.broadcastMessage(message);
    }

    /**
     * Remove client should proceed successfully without exceptions.
     */
    @Test
    public void removeClientShouldProceedSuccessfullyWithoutExceptions() {
        broadCastService.removeClient(clientRunnable);
    }

    /**
     * Should return a single message, when broadCastService returns a single message.
     */
    @Test
    public void shouldReturnASingleMessage() {
        String name1 = "sender name";
        String messageBody = "message body";
        String group1 = "group1";
        edu.northeastern.ccs.im.user_group.Message message1 = mock(edu.northeastern.ccs.im.user_group.Message.class);
        User user = mock(User.class);
        when(message1.getSender()).thenReturn(user);
        when(user.getUsername()).thenReturn(name1);
        when(message1.getMessage()).thenReturn(messageBody);
        Group group = mock(Group.class);
        when(message1.getReceiver()).thenReturn(group);
        when(group.getGroupCode()).thenReturn(group1);
        when(messageService.getTop15Messages(any())).thenReturn(Collections.singletonList(message1));
        List<Message> messageList = broadCastService.getRecentMessages();
        Assert.assertEquals(1, messageList.size());
        Message receivedMessage = messageList.get(0);
        Assert.assertTrue(receivedMessage.isBroadcastMessage());
        Assert.assertEquals(receivedMessage.getName(), name1);
        Assert.assertEquals(receivedMessage.getText(), messageBody);
    }

    /**
     * Should return zero messages if null pointer exception is thrown
     * by message service getTop15Messages.
     */
    @Test
    public void shouldReturnZeroMessageIfNullPointerExceptionIsThrown() {
        doThrow(NullPointerException.class).when(messageService).getTop15Messages(any());
        List<Message> messages = broadCastService.getRecentMessages();
        Assert.assertEquals(0, messages.size());
    }
}
