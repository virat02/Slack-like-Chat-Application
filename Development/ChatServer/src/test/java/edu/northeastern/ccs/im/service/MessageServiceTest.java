package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.jpa_service.MessageJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MessageServiceTest {

    private MessageService messageService = new MessageService();
    private MessageJPAService messageJPAService;
    private UserService userService;
    private GroupService groupService;

    @Mock
    private Message m = new Message();

    @Mock
    private Message m1 = new Message();

    private Message m2 = new Message();

    private User u = new User();
    private Group g = new Group();

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
    }

    /**
     *Test for able to create message
     */
    @Test
    public void testCreateMessage() {

        when(messageJPAService.createMessage(any(Message.class))).thenReturn(1);
        when(m.getId()).thenReturn(1);
        when(messageJPAService.getMessage(anyInt())).thenReturn(m);

        messageService.setMessageJPAService(messageJPAService);

        assertTrue(messageService.createMessage(m));
    }

    /**
     *Test for unable to create message
     */
    @Test
    public void testCreateMessageFalse() {

        when(messageJPAService.createMessage(any(Message.class))).thenReturn(-1);
        when(m.getId()).thenReturn(1);
        when(messageJPAService.getMessage(anyInt())).thenReturn(m);

        messageService.setMessageJPAService(messageJPAService);

        assertFalse(messageService.createMessage(m));
    }

    /**
     * Test the create message method from client
     */
    @Test
    public void testCreateMessageBody() {

        when(userService.search(anyString())).thenReturn(u);
        when(groupService.searchUsingCode(anyString())).thenReturn(g);
        when(messageJPAService.createMessage(any(Message.class))).thenReturn(1);

        messageService.setMessageJPAService(messageJPAService);
        messageService.setJPAServices(userService, groupService);

        assertTrue(messageService.createMessage(MESSAGE_BODY, USERNAME, GROUPCODE));
    }

    /**
     * Test successful update message method
     */
    @Test
    public void testUpdateMessageService() {

        when(messageJPAService.updateMessage(any(Message.class))).thenReturn(true);
        messageService.setMessageJPAService(messageJPAService);
        assertTrue(messageService.updateMessage(m1));
    }

    /**
     * Test unsuccessful update message method
     */
    @Test
    public void testUpdateMessageServiceUnsuccessful() {

        when(messageJPAService.updateMessage(any(Message.class))).thenReturn(false);
        messageService.setMessageJPAService(messageJPAService);
        assertFalse(messageService.updateMessage(m1));
    }

    /**
     * Test get message method
     */
    @Test
    public void testGetMessage() {

        when(messageJPAService.getMessage(anyInt())).thenReturn(m);
        messageService.setMessageJPAService(messageJPAService);
        assertEquals(m, messageService.get(2));
    }

    /**
     * Test get message method to return null
     */
    @Test
    public void testGetMessageNullMessage() {

        when(messageJPAService.getMessage(anyInt())).thenReturn(null);
        messageService.setMessageJPAService(messageJPAService);
        assertNull(messageService.get(1));
    }

    /**
     * Test the delete message method when message is deleted
     */
    @Test
    public void testDeleteMessageForDeletedMessage() {
        when(messageJPAService.updateMessage(any(Message.class))).thenReturn(true);
        messageService.setMessageJPAService(messageJPAService);
        assertTrue(messageService.deleteMessage(m2));
        assertTrue(m2.isDeleted());
    }

    /**
     * Test the delete message method when message is not deleted
     */
    @Test
    public void testDeleteMessageForNotDeletedMessage() {
        when(messageJPAService.updateMessage(any(Message.class))).thenReturn(false);
        messageService.setMessageJPAService(messageJPAService);
        assertFalse(messageService.deleteMessage(m));
    }

    /**
     * Test the getTop15Messages method
     */
    @Test
    public void testGetTop15Messages() {
        List<Message> msgList = new ArrayList<>();
        when(messageJPAService.getTop15Messages(anyString())).thenReturn(msgList);
        messageService.setMessageJPAService(messageJPAService);
        assertEquals(msgList, messageService.getTop15Messages("ABCD"));
    }
}