package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.user_group.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Class for testing the Message JPA services
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class MessageJPAServiceTest {

    @Mock
    private MessageJPAService messageJPAService;

    @Mock
    private GroupService groupService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction entityTransaction;

    @Mock
    private Message m1;

    @Mock
    private Message m2;

    @Mock
    private Message m3;

    private User u1 = new User();
    private User u2 = new User();
    private User u3 = new User();

    private Group privateGroup = new Group();
    private Group group;

    private static final String U1_MESSAGE = "Hi, I'm Virat!";
    private static final String U2_MESSAGE = "Hi, I'm Sangeetha!";
    private static final String U3_MESSAGE = "Hi, I'm Jerry!";

    /**
     * Sets up the users information
     */
    @Before
    public void setUp() {
        Date d = new Date();

        group = mock(Group.class);

        ///Set user id's
        u1.setId(1);
        u2.setId(2);
        u3.setId(3);

        //Create a message m1
        m1 = new Message();
        m1.setId(1);
        m1.setMessage(U1_MESSAGE);
        m1.setTimestamp(d);
        m1.setExpiration(30);
        m1.setSender(u1);
        m1.setReceiver(privateGroup);
        m1.setDeleted(false);

        //Create a message m2
        m2 = new Message();
        m2.setId(2);
        m2.setMessage(U2_MESSAGE);
        m2.setTimestamp(d);
        m2.setExpiration(20);
        m2.setSender(u2);
        m2.setReceiver(group);
        m2.setDeleted(false);

        //Create a message m3
        m3 = new Message();
        m3.setId(1);
        m3.setMessage(U3_MESSAGE);
        m3.setTimestamp(d);
        m3.setExpiration(15);
        m3.setSender(u3);
        m3.setReceiver(group);
        m3.setDeleted(false);

        messageJPAService = new MessageJPAService();
    }

    /**
     * Test the create message and send message method for Message JPA Service
     */
    @Test
    public void testCreateAndSendMessageForMessageJPAService() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);
        messageJPAService.createMessage(m1);
    }

    /**
     * Test the create message and send message method for Message JPA Service for an exception
     */
    @Test
    public void testCreateAndSendMessageForMessageJPAServiceForException() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);
        doThrow(new IllegalArgumentException()).when(entityManager).persist(any(Message.class));
        assertEquals(-1, messageJPAService.createMessage(m1));
    }

    /**
     * Test the update message method of MessageJPAService
     */
    @Test
    public void testUpdateMessageOfMessageJPA() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(m2);
        messageJPAService.updateMessage(m3);

        assertEquals(m2.toString(), m3.toString());
    }

    /**
     * Test the update message method of MessageJPAService for a false update
     */
    @Test(expected = EntityNotFoundException.class)
    public void testFalseUpdateMessageOfMessageJPA() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        assertFalse(messageJPAService.updateMessage(m3));
    }

    /**
     * Test the update message method for non-existing message
     */
    @Test(expected = EntityNotFoundException.class)
    public void testUpdateNonExistingMessage() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        messageJPAService.updateMessage(m3);
    }

    /**
     * Test the delete message method
     */
    @Test
    public void testDeleteMessage() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);

        m2.setDeleted(true);
        when(entityManager.find(any(), anyInt())).thenReturn(m2);

        messageJPAService.updateMessage(m2);
        assertTrue(m2.isDeleted());

    }

    /**
     * Test the delete message method for non existing message
     */
    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNonExistingMessage() {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);

        m2.setDeleted(true);
        when(entityManager.find(any(), anyInt())).thenReturn(null);

        messageJPAService.updateMessage(m2);
    }

    /**
     * Test get top 15 messages for throwing an exception
     */
    @Test
    public void testGetTop15Messages() {
        List<Message> msgList = new ArrayList<>(Arrays.asList(m1,m2,m3));

        when(groupService.searchUsingCode(anyString())).thenReturn(group);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(group.getGroupCode()).thenReturn("ABC");
        when(group.getId()).thenReturn(1);
        when(mockedQuery.setMaxResults(anyInt())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(msgList);
        messageJPAService.setGroupService(groupService);
        messageJPAService.getTop15Messages(group.getGroupCode());
    }

    /**
     * Test get top 15 messages for throwing an exception
     */
    @Test(expected = NullPointerException.class)
    public void testGetTop15MessagesForException1() {
        when(groupService.searchUsingCode(anyString())).thenReturn(null);
        messageJPAService.setEntityManager(entityManager);
        when(group.getGroupCode()).thenReturn("ABC");
        when(group.getId()).thenReturn(1);
        messageJPAService.setGroupService(groupService);
        messageJPAService.getTop15Messages(group.getGroupCode());
    }

    /**
     * Test get top 15 messages for throwing an exception
     */
    @Test(expected = NullPointerException.class)
    public void testGetTop15MessagesForException() {

        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);

        messageJPAService.getTop15Messages(group.getGroupCode());
    }

    /**
     * Test for getting a valid message
     */
    @Test
    public void testGetMessageForMessageJPA() {

        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);

        messageJPAService.getMessage(group.getId());
    }

    /**
     * Test for getting a message which doesn't exist
     */
    @Test(expected = NullPointerException.class)
    public void testGetMessageForMessageJPAForException() {

        mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManager(entityManager);

        messageJPAService.getMessage(group.getId());
    }
}
