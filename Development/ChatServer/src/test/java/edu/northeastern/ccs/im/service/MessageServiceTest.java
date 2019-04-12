package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.customexceptions.FirstTimeUserLoggedInException;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.MessageNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.service.jpa_service.AllJPAService;
import edu.northeastern.ccs.im.service.jpa_service.GroupJPAService;
import edu.northeastern.ccs.im.service.jpa_service.MessageJPAService;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.User;
import edu.northeastern.ccs.im.user_group.UserChatRoomLogOffEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MessageServiceTest {

    private MessageService messageService = new MessageService();
    private MessageJPAService messageJPAService;
    private UserService userService;
    private GroupService groupService;
    private AllJPAService jpaService;

    @Mock
    private Message m = new Message();

    @Mock
    private Message m1 = new Message();

    private Message m2 = new Message();

    private User u = new User();
    private Group g = new Group();
    List<Message> messages;

    private static final String MESSAGE_BODY = "Hello!";
    private static final String USERNAME = "virat";
    private static final String GROUPCODE = "ABCD";

    /**
     * Sets up the required information
     */
    @Before
    public void setUp() {

        messageJPAService = mock(MessageJPAService.class);
        userService = mock(UserService.class);
        groupService = mock(GroupService.class);
        jpaService = mock(AllJPAService.class);

        m = mock(Message.class);
        m1 = mock(Message.class);

        //Setup a user
        u.setId(1);
        u.setPassword("password");
        u.setUsername("virat");

        //Setup a group
        g.setId(1);
        g.setGroupCode(GROUPCODE);
        g.setCreatedOn(new Date());
        g.setName("group1");

        //Setup a message
        m1.setId(1);
        m1.setDeleted(true);
        m1.setExpiration(20);
        m1.setTimestamp(new Date());
        m1.setSender(u);
        m1.setReceiver(g);

        messages = new ArrayList<>();
        messages.add(m1);
        messages.add(m2);
    }

    /**
     *Test for able to create message
     */
    @Test
    public void testCreateMessage() {

        messageService.setAllJPAService(jpaService);
        when(jpaService.createEntity(any())).thenReturn(true);
        assertTrue(messageService.createMessage(m));
    }

    /**
     *Test for able to create message for false
     */
    @Test
    public void testCreateMessageForFalse() {

        messageService.setAllJPAService(jpaService);
        when(jpaService.createEntity(any())).thenThrow(new NoResultException());
        assertFalse(messageService.createMessage(m));
    }

    /**
     * Test the create message method from client
     */
    @Test
    public void testCreateMessageBody()
            throws UserNotFoundException, GroupNotFoundException {

        when(userService.search(anyString())).thenReturn(u);
        when(groupService.searchUsingCode(anyString())).thenReturn(g);
        when(jpaService.createEntity(any(Message.class))).thenReturn(true);
        when(jpaService.createEntity(any())).thenReturn(true);
        messageService.setAllJPAService(jpaService);
        messageService.setMessageJPAService(messageJPAService);
        messageService.setServices(userService, groupService);

        assertTrue(messageService.createMessage(MESSAGE_BODY, USERNAME, GROUPCODE));
    }

    /**
     * Test the create message method from client for throwing UserNotFound Exception
     */
    @Test(expected = UserNotFoundException.class)
    public void testCreateMessageBodyForUserNotFoundException()
            throws UserNotFoundException, GroupNotFoundException {

        when(userService.search(anyString())).thenThrow(new UserNotFoundException("Could not find user!"));
        messageService.setServices(userService, groupService);

        messageService.createMessage(MESSAGE_BODY, USERNAME, GROUPCODE);
    }

    /**
     * Test the create message method from client for throwing GroupNotFound Exception
     */
    @Test(expected = GroupNotFoundException.class)
    public void testCreateMessageBodyForGroupNotFoundException()
            throws UserNotFoundException, GroupNotFoundException {

        when(groupService.searchUsingCode(anyString())).thenThrow(new GroupNotFoundException("Could not find group!"));
        messageService.setServices(userService, groupService);

        messageService.createMessage(MESSAGE_BODY, USERNAME, GROUPCODE);
    }

    /**
     * Test successful update message method
     */
    @Test
    public void testUpdateMessageOfMessageService() throws MessageNotFoundException{

        when(messageJPAService.updateMessage(any(Message.class))).thenReturn(true);
        messageService.setMessageJPAService(messageJPAService);
        assertTrue(messageService.updateMessage(m1));
    }

    /**
     * Test unsuccessful update message method
     */
    @Test
    public void testUpdateMessageServiceUnsuccessful() throws MessageNotFoundException {

        when(messageJPAService.updateMessage(any(Message.class))).thenReturn(false);
        messageService.setMessageJPAService(messageJPAService);
        assertFalse(messageService.updateMessage(m1));
    }

    /**
     * Test get message method
     */
    @Test
    public void testGetMessage() throws MessageNotFoundException{

        when(jpaService.getEntity(anyString(), anyInt())).thenReturn(m);
        messageService.setAllJPAService(jpaService);
        assertEquals(m, messageService.get(2));
    }

    /**
     * Test get message method to return null
     */
    @Test
    public void testGetMessageForNullMessage() throws MessageNotFoundException{

        when(jpaService.getEntity(anyString(), anyInt())).thenReturn(null);
        messageService.setAllJPAService(jpaService);
        assertNull(messageService.get(1));
    }

    @Test(expected = MessageNotFoundException.class)
    public void testGetMessageForMessageNotFoundException()
            throws MessageNotFoundException {
        when(jpaService.getEntity(anyString(), anyInt())).thenThrow(new NoResultException());
        messageService.setAllJPAService(jpaService);
        messageService.get(1);
    }

    /**
     * Test the delete message method when message is deleted
     */
    @Test
    public void testDeleteMessageForAlreadyDeletedMessage()
            throws MessageNotFoundException {
        when(messageJPAService.updateMessage(any(Message.class))).thenReturn(true);
        messageService.setMessageJPAService(messageJPAService);
        assertTrue(messageService.deleteMessage(m2));
        assertTrue(m2.isDeleted());
    }

    /**
     * Test the delete message method for an unsuccessful update fron JPA
     */
    @Test
    public void testDeleteMessageForUnsuccessfulUpdate() throws MessageNotFoundException{
        when(messageJPAService.updateMessage(any(Message.class))).thenReturn(false);
        messageService.setMessageJPAService(messageJPAService);
        assertFalse(messageService.deleteMessage(m));
    }

    /**
     * Test the getTop15Messages method
     */
    @Test
    public void testGetTop15Messages() throws GroupNotFoundException {
        List<Message> msgList = new ArrayList<>();
        when(messageJPAService.getTop15Messages(anyString())).thenReturn(msgList);
        messageService.setMessageJPAService(messageJPAService);
        assertEquals(msgList, messageService.getTop15Messages("ABCD"));
    }

    /**
     * Test the getTop15Messages method for GroupNotFoundException
     */
    @Test(expected = GroupNotFoundException.class)
    public void testGetTop15MessagesForGroupNotFoundException()
            throws GroupNotFoundException {
        when(messageJPAService.getTop15Messages(anyString()))
                .thenThrow(new GroupNotFoundException("No Group found!"));
        messageService.setMessageJPAService(messageJPAService);
        messageService.getTop15Messages("ABCD");
    }

    /**
     * Tests the get Unread Messages method.
     * @throws UserNotFoundException if the user isn't found.
     * @throws GroupNotFoundException if the group isn't found.
     * @throws FirstTimeUserLoggedInException if this if the first time the user logged in to a chat.
     */
    @Test
    public void testGetUnreadMessages() throws UserNotFoundException, GroupNotFoundException, FirstTimeUserLoggedInException {
        UserJPAService userJPAService = mock(UserJPAService.class);
        User userOne = new User();
        userOne.setId(123);
        userOne.setUsername("Hello");
        Group groupOne = new Group();
        groupOne.setId(345);
        groupOne.setName("Hello");
        UserChatRoomLogOffEvent userChatRoomLogOffEvent = new UserChatRoomLogOffEvent();
        when(userJPAService.search(any())).thenReturn(userOne);
        when(groupService.searchUsingCode(anyString())).thenReturn(groupOne);
        when(userJPAService.getLogOffEvent(anyInt(), anyInt())).thenReturn(userChatRoomLogOffEvent);
        when(messageJPAService.getMessagesAfterThisTimestamp(any(), anyInt())).thenReturn(messages);
        messageService.setUserJPAService(userJPAService);
        messageService.setGroupService(groupService);
        messageService.setMessageJPAService(messageJPAService);
        assertEquals(messages, messageService.getUnreadMessages("Bob", "321"));

    }
    /**
     * Tests the get Unread Messages method.
     * @throws UserNotFoundException if the user isn't found.
     * @throws GroupNotFoundException if the group isn't found.
     * @throws FirstTimeUserLoggedInException if this if the first time the user logged in to a chat.
     */
    @Test
    public void testGetUnreadMessagesFail() throws UserNotFoundException, GroupNotFoundException, FirstTimeUserLoggedInException {
        UserJPAService userJPAService = mock(UserJPAService.class);
        User userOne = new User();
        userOne.setId(123);
        userOne.setUsername("Hello");
        Group groupOne = new Group();
        groupOne.setId(345);
        groupOne.setName("Hello");
        UserChatRoomLogOffEvent userChatRoomLogOffEvent = new UserChatRoomLogOffEvent();
        when(userJPAService.search(any())).thenReturn(userOne);
        when(groupService.searchUsingCode(anyString())).thenReturn(groupOne);
        when(userJPAService.getLogOffEvent(anyInt(), anyInt())).thenThrow(FirstTimeUserLoggedInException.class);
        when(messageJPAService.getTop15Messages(anyString())).thenReturn(messages);
        messageService.setUserJPAService(userJPAService);
        messageService.setGroupService(groupService);
        messageService.setMessageJPAService(messageJPAService);
        assertEquals(messages, messageService.getUnreadMessages("Bob", "321"));

    }

    /**
     * Tests the get all messages method for the UserService class.
     * @throws GroupNotFoundException if the group is not found.
     */
    @Test
    public void testGetAllMessages() throws GroupNotFoundException {
        when(messageJPAService.getAllMessages(anyString())).thenReturn(messages);
        messageService.setMessageJPAService(messageJPAService);
        assertEquals(messages, messageService.getAllMessages("123"));
    }

}
