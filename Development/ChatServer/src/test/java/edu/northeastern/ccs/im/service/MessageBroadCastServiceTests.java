package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.user_group.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Messagebroadcast service tests.
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
     * Should broad cast message if message is broadcast and message is persistent
     * @throws IOException                  the io exception
     * @throws UserNotFoundException        the user not found exception
     * @throws MessageNotPersistedException the message not persisted exception
     * @throws GroupNotFoundException       the group not found exception
     */
    @Test
    public void shouldBroadCastMessageIfMessageIsBroadcastAndMessageIsPersistent() throws IOException, UserNotFoundException, MessageNotPersistedException, GroupNotFoundException {
        broadCastService.addConnection(socketChannel);
        when(message.isBroadcastMessage()).thenReturn(true);
        when(messageService.createMessage(anyString(), anyString(), anyString())).thenReturn(true);
        broadCastService.broadcastMessage(message);
    }

    /**
     * Should not broad cast message if message is not broadcast and dont call message service.
     * @throws IOException                  the io exception
     * @throws UserNotFoundException        the user not found exception
     * @throws MessageNotPersistedException the message not persisted exception
     * @throws GroupNotFoundException       the group not found exception
     */
    @Test
    public void shouldNotBroadCastMessageIfMessageIsNotBroadcastAndDontCallMessageService() throws IOException, UserNotFoundException, MessageNotPersistedException, GroupNotFoundException {
        broadCastService.addConnection(socketChannel);
        when(message.isBroadcastMessage()).thenReturn(false);
        broadCastService.broadcastMessage(message);
        verify(messageService, times(0)).createMessage(anyString(), anyString(), anyString());
    }

    /**
     * Should broad cast message if message is broadcast and call message service.
     *
     * @throws IOException                  the io exception
     * @throws UserNotFoundException        the user not found exception
     * @throws MessageNotPersistedException the message not persisted exception
     * @throws GroupNotFoundException       the group not found exception
     */
    @Test
    public void shouldBroadCastMessageIfMessageIsBroadcastAndCallMessageService() throws IOException, UserNotFoundException, MessageNotPersistedException, GroupNotFoundException {
        broadCastService.addConnection(socketChannel);
        when(message.isBroadcastMessage()).thenReturn(true);
        when(messageService.createMessage(any(), any(), any())).thenReturn(true);
        broadCastService.broadcastMessage(message);
        verify(messageService, times(1)).createMessage(any(), any(), any());
    }

    /**
     * Should not broad cast message if message is not broadcast and throws message not persisted exception.
     *
     * @throws IOException                  the io exception
     * @throws UserNotFoundException        the user not found exception
     * @throws MessageNotPersistedException the message not persisted exception
     * @throws GroupNotFoundException       the group not found exception
     */
    @Test
    public void shouldNotBroadCastMessageIfMessageIsNotBroadcastAndThrowsMessageNotPersistedException() throws IOException, UserNotFoundException, MessageNotPersistedException, GroupNotFoundException {
        broadCastService.addConnection(socketChannel);
        when(message.isBroadcastMessage()).thenReturn(true);
        doThrow(MessageNotPersistedException.class).when(messageService).createMessage(any(), any(), any());
        broadCastService.broadcastMessage(message);
    }

    /**
     * Should not broad cast message if message is not broadcast and throws user not found exception.
     *
     * @throws UserNotFoundException        the user not found exception
     * @throws MessageNotPersistedException the message not persisted exception
     * @throws GroupNotFoundException       the group not found exception
     */
    @Test
    public void shouldNotBroadCastMessageIfMessageIsNotBroadcastAndThrowsUserNotFoundException() throws UserNotFoundException, MessageNotPersistedException, GroupNotFoundException {
        when(message.isBroadcastMessage()).thenReturn(true);
        doThrow(UserNotFoundException.class).when(messageService).createMessage(any(), any(), any());
        broadCastService.broadcastMessage(message);
    }

    /**
     * Should not broad cast message if message is not broadcast and throws group not found exception.
     *
     * @throws UserNotFoundException        the user not found exception
     * @throws MessageNotPersistedException the message not persisted exception
     * @throws GroupNotFoundException       the group not found exception
     */
    @Test
    public void shouldNotBroadCastMessageIfMessageIsNotBroadcastAndThrowsGroupNotFoundException() throws UserNotFoundException, MessageNotPersistedException, GroupNotFoundException {
        when(message.isBroadcastMessage()).thenReturn(true);
        doThrow(GroupNotFoundException.class).when(messageService).createMessage(any(), any(), any());
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
     * @throws GroupNotFoundException the group not found exception
     */
    @Test
    public void shouldReturnASingleMessage() throws GroupNotFoundException {
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
     *
     * @throws GroupNotFoundException the group not found exception
     */
    @Test
    public void shouldReturnZeroMessageIfNoResultExceptionIsThrown() throws GroupNotFoundException {
        doThrow(NoResultException.class).when(messageService).getTop15Messages(any());
        List<Message> messages = broadCastService.getRecentMessages();
        Assert.assertEquals(0, messages.size());
    }

    /**
     * Should return zero messages if null pointer exception is thrown
     * by message service getTop15Messages.
     *
     * @throws GroupNotFoundException the group not found exception
     */
    @Test
    public void shouldReturnZeroMessageIfGroupNotFoundExceptionIsThrown() throws GroupNotFoundException {
        doThrow(GroupNotFoundException.class).when(messageService).getTop15Messages(any());
        List<Message> messages = broadCastService.getRecentMessages();
        Assert.assertEquals(0, messages.size());
    }
}
