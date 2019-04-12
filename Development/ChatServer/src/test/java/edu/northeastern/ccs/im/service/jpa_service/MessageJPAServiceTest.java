package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.MessageNotFoundException;
import edu.northeastern.ccs.im.service.EntityManagerUtil;
import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.user_group.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.*;
import javax.persistence.criteria.*;
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
    private EntityTransaction entityTransaction;

    private AllJPAService allJPAService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityManagerUtil entityManagerUtil;

    @Mock
    private GroupService groupService;

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
        entityManagerUtil = mock(EntityManagerUtil.class);
        entityManager = mock(EntityManager.class);
        entityTransaction = mock(EntityTransaction.class);

        Date d = new Date();

        group = mock(Group.class);
        group.setGroupCode("XYZ");

        ///Set user id's
        u1.setId(1);
        u2.setId(2);
        u3.setId(3);

        u1.setUsername("u1");
        u2.setUsername("u2");
        u3.setUsername("u3");

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

        messageJPAService = MessageJPAService.getInstance();
        allJPAService = AllJPAService.getInstance();
    }

    /**
     * Test the create message for Message JPA Service
     */
    @Test
    public void testCreateMessageForMessageJPAService() {

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        allJPAService.setEntityManagerUtil(entityManagerUtil);
        assertTrue(allJPAService.createEntity(m1));
    }

    /**
     * Test the create message for Message JPA Service for throwing the MessageNotPersistedException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMessageForIllegalArgumentException() {

        allJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        doThrow(new IllegalArgumentException()).when(entityManager).persist(any(Message.class));
        allJPAService.createEntity(m1);
    }

    /**
     * Test the update message method of MessageJPAService for successful update
     */
    @Test
    public void testUpdateMessageForTrue() throws MessageNotFoundException {

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManager.find(any(), anyInt())).thenReturn(m1);
        assertTrue(messageJPAService.updateMessage(m2));
    }

    /**
     * Test the update message method of MessageJPAService for a false update
     */
    @Test(expected = MessageNotFoundException.class)
    public void testUpdateMessageForMessageNotFoundException()
            throws MessageNotFoundException{

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        messageJPAService.updateMessage(m3);
    }

    /**
     * Test the update message method of MessageJPAService for unable to update
     */
    @Test
    public void testUpdateMessageForFalse() throws MessageNotFoundException {
        Message m = mock(Message.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManagerUtil(entityManagerUtil);
        when(entityManager.find(any(), anyInt())).thenReturn(m);
        assertFalse(messageJPAService.updateMessage(m2));
    }

    /**
     * Test the delete message method
     */
    @Test
    public void testDeleteMessage() throws MessageNotFoundException {

        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManagerUtil(entityManagerUtil);

        m2.setDeleted(true);
        when(entityManager.find(any(), anyInt())).thenReturn(m2);

        messageJPAService.updateMessage(m2);
        assertTrue(m2.isDeleted());

    }

    /**
     * Test the delete message method for non existing message
     */
    @Test(expected = MessageNotFoundException.class)
    public void testDeleteNonExistingMessage() throws MessageNotFoundException {
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManagerUtil(entityManagerUtil);

        m2.setDeleted(true);
        when(entityManager.find(any(), anyInt())).thenReturn(null);

        messageJPAService.updateMessage(m2);
    }

    /**
     * Test get top 15 messages for throwing an exception
     */
    @Test
    public void testGetTop15Messages() throws GroupNotFoundException {
        List<Message> msgList = new ArrayList<>(Arrays.asList(m1,m2,m3));

        when(groupService.searchUsingCode(anyString())).thenReturn(group);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManagerUtil(entityManagerUtil);
        messageJPAService.setGroupService(groupService);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(group.getGroupCode()).thenReturn("ABC");
        when(group.getId()).thenReturn(1);
        when(mockedQuery.setMaxResults(anyInt())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(msgList);
        assertEquals(msgList,messageJPAService.getTop15Messages(group.getGroupCode()));
    }

    /**
     * Test get top 15 messages for throwing an exception
     */
    @Test(expected = GroupNotFoundException.class)
    public void testGetTop15MessagesForGroupNotFoundException() throws GroupNotFoundException {
        when(groupService.searchUsingCode(anyString())).thenThrow(new GroupNotFoundException("group not found"));
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        messageJPAService.setEntityManagerUtil(entityManagerUtil);
        messageJPAService.setGroupService(groupService);
        messageJPAService.getTop15Messages("XYZ");
    }

    /**
     * Test for getting a message which does exist
     */
    @Test
    public void testGetMessage() {

        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(m3);
        allJPAService.setEntityManagerUtil(entityManagerUtil);

        assertEquals(m3, allJPAService.getEntity("Message", m3.getId()));
    }

    /**
     * Test for getting a message which does exist
     */
    @Test(expected = NoResultException.class)
    public void testGetMessageForNoResultException() {

        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(new NoResultException());
        allJPAService.setEntityManagerUtil(entityManagerUtil);

        assertEquals(m3, allJPAService.getEntity("Message", m3.getId()));
    }

    /**
     * A test to get all the messages.
     * @throws GroupNotFoundException if the group is not found.
     */
    @Test
    public void testGetAllMessages() throws GroupNotFoundException {
        List<Message> messages = new ArrayList<>();
        messages.add(m1);
        messages.add(m2);
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(groupService.searchUsingCode(anyString())).thenReturn(group);
        when(entityManagerUtil.getEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(messages);
        messageJPAService.setEntityManagerUtil(entityManagerUtil);
        messageJPAService.setGroupService(groupService);
        assertEquals(messages, messageJPAService.getAllMessages("123"));
    }

}
