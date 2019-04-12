package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Invite;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A lass for testing invite JPA Service class.
 */
public class InviteJPAServiceTest {
     private InviteJPAService mockedInviteJPAService;
     private EntityManager mockedEntityManager;
     private InviteJPAService inviteJPAService;
     private Invite invite;
     private Group group;
     private User sender;
     private User receiver;
     private User userOne;
     private User userTwo;
     private List<User> userList;
     private EntityTransaction mockedEntityTransaction;
     private TypedQuery typedQuery;
     private List<Invite> inviteList;

    /**
     * Sets up the inviteJPAService before the tests.
     */
    @Before
     public void setUp() {
         mockedInviteJPAService = mock(InviteJPAService.class);
         typedQuery = mock(TypedQuery.class);
         mockedEntityTransaction = mock(EntityTransaction.class);
         mockedEntityManager = mock(EntityManager.class);
         inviteList = new ArrayList<>();
         inviteJPAService =  InviteJPAService.getInstance();
         userOne = new User();
         userOne.setUsername("User1");
         userOne.setPassword("User1");
         userOne.setId(1);
         userTwo = new User();
         userTwo.setUsername("User2");
         userTwo.setPassword("User2");
         userTwo.setId(2);
         sender = new User();
         sender.setId(4);
         sender.setUsername("Puff");
         receiver = new User();
         receiver.setUsername("Jiggly");
         receiver.setPassword("Ghb1");
         receiver.setId(3);
         receiver.setUsername("Kazoo");
         group = new Group();
         group.setGroupCode("Banjo");
         invite = new Invite();
         userList = new ArrayList<>();
         userList.add(userOne);
         userList.add(userTwo);
         userList.add(sender);
         group.setUsers(userList);
         List<User> moderators = new ArrayList<>();
         moderators.add(userOne);
         group.setModerators(moderators);
         invite.setReceiver(receiver);
         invite.setId(123);
         invite.setSender(sender);
         invite.setGroup(group);
         invite.setStatus(Status.NOUPDATE);

     }

    /**
     * Tests to ensure we can create an invite.
     * @throws InviteNotAddedException when an invite can't be added to the DB.
     */
    @Test
     public void testCreateInvite() throws InviteNotAddedException {
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.createQuery(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(inviteList);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        assertEquals(invite.getId(), inviteJPAService.createInvite(invite));
     }

    /**
     * Tests when a invite cannot be added to the DB.
     * @throws InviteNotAddedException when an invite isn't added to the DB.
     */
    @Test (expected = InviteNotAddedException.class)
    public void testCreateInviteFail() throws InviteNotAddedException {
        group.addUser(receiver);
        invite.setGroup(group);
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        inviteJPAService.createInvite(invite);
    }

    /**
     * Tests when a invite cannot be added to the DB.
     * @throws InviteNotAddedException when an invite isn't added to the DB.
     */
    @Test (expected = InviteNotAddedException.class)
    public void testCreateInviteFailInvitedAlready() throws InviteNotAddedException {
        inviteList.add(invite);
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.createQuery(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(inviteList);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        inviteJPAService.createInvite(invite);
    }

    /**
     * Tests when a successful return of an invite.
     * @throws InviteNotAddedException when an invite isn't added to the DB.
     */
    @Test
    public void testGetInvite() throws InviteNotFoundException {
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        assertEquals(invite, inviteJPAService.getInvite(invite.getId()));
    }

    /**
     * Tests when an unsuccessful return of an invite.
     * @throws InviteNotAddedException when an invite isn't added to the DB.
     */
    @Test (expected = InviteNotFoundException.class)
    public void testGetInviteFail() throws InviteNotFoundException {
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenThrow(IllegalArgumentException.class);
        inviteJPAService.setEntityManager(mockedEntityManager);
        assertEquals(invite, inviteJPAService.getInvite(invite.getId()));
    }

    /**
     * Tests when an unsuccessful return of an invite.
     * @throws InviteNotAddedException when an invite isn't added to the DB.
     */
    @Test
    public void testUpdateInvite() throws InviteNotUpdatedException {
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        inviteJPAService.updateInvite(invite);
        assertEquals(Status.NOUPDATE, invite.getStatus());
    }

    /**
     * Tests when an unsuccessful return of an invite.
     * @throws InviteNotAddedException when an invite isn't added to the DB.
     */
    @Test (expected = InviteNotFoundException.class)
    public void testUpdateInviteFail() throws InviteNotFoundException {
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenThrow(IllegalArgumentException.class);
        inviteJPAService.setEntityManager(mockedEntityManager);
        assertEquals(invite, inviteJPAService.getInvite(invite.getId()));
    }

//    /**
//     * Tests when an unsuccessful return of an invite.
//     * @throws InviteNotAddedException when an invite isn't added to the DB.
//     */
//    @Test (expected = EntityNotFoundException.class)
//    public void testUpdateInviteNull() throws InviteNotUpdatedException {
//        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
//        when(mockedEntityManager.find(any(), anyInt())).thenReturn(null);
//        inviteJPAService.setEntityManager(mockedEntityManager);
//        inviteJPAService.updateInvite(invite);
//        assertEquals(Status.NOUPDATE, invite.getStatus());
//    }


    /**
     * Tests when an unsuccessful return of an invite.
     * @throws InviteNotAddedException when an invite isn't added to the DB.
     */
    @Test
    public void testUpdateInviteGroup() throws InviteNotUpdatedException, GroupNotFoundException {
        GroupJPAService groupJPAService = mock(GroupJPAService.class);
        invite.setStatus(Status.ACCEPTED);
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        inviteJPAService.setGroupJPA(groupJPAService);
        inviteJPAService.updateInvite(invite);
        assertEquals(Status.ACCEPTED, invite.getStatus());
    }

    /**
     * Tests to ensure we can delete an invite.
     * @throws InviteNotDeletedException if an invite wasn't deleted.
     */
    @Test
    public void testDeleteInvite() throws InviteNotDeletedException {
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        assertEquals(invite, inviteJPAService.deleteInvite(invite));
    }

    /**
     * Tests to ensure we can throw an invite Not Deleted Exception.
     * @throws InviteNotDeletedException if an invite wasn't deleted.
     */
    @Test (expected = InviteNotDeletedException.class)
    public void testDeleteInviteException() throws InviteNotDeletedException {
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenThrow(IllegalArgumentException.class);
        inviteJPAService.setEntityManager(mockedEntityManager);
        assertEquals(invite, inviteJPAService.deleteInvite(invite));
    }

    /**
     * Tests to ensure we can search for a list of invites.
     * @throws GroupNotFoundException if the group isn't found in the DB.
     * @throws InviteNotFoundException if the invite is not found in the DB.
     */
    @Test
    public void testSearchInvite() throws GroupNotFoundException, InviteNotFoundException {
        inviteList.add(invite);
        GroupJPAService groupJPAService = mock(GroupJPAService.class);
        when(groupJPAService.searchUsingCode(anyString())).thenReturn(group);
        when(mockedEntityManager.createQuery(anyString(),any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(inviteList);
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        inviteJPAService.setGroupJPA(groupJPAService);
        inviteJPAService.searchInviteByGroupCode("Banjo", userOne);
        assertEquals(1, inviteList.size());
    }

    /**
     * Tests to ensure someone who isn't the moderator throws an exception.
     * @throws GroupNotFoundException if the group isn't found in the DB.
     * @throws InviteNotFoundException if the invite is not found in the DB.
     */
    @Test (expected = InviteNotFoundException.class)
    public void testSearchInviteNotModeratorFail() throws GroupNotFoundException, InviteNotFoundException {
        inviteList.add(invite);
        GroupJPAService groupJPAService = mock(GroupJPAService.class);
        when(groupJPAService.searchUsingCode(anyString())).thenReturn(group);
        when(mockedEntityManager.createQuery(anyString(),any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(inviteList);
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        inviteJPAService.setGroupJPA(groupJPAService);
        inviteJPAService.searchInviteByGroupCode("Banjo", userTwo);
    }

    /**
     * Tests to ensure group not found exception can be thrown.
     * @throws GroupNotFoundException if the group isn't found in the DB.
     * @throws InviteNotFoundException if the invite is not found in the DB.
     */
    @Test (expected = GroupNotFoundException.class)
    public void testSearchInviteGroupNotFound() throws GroupNotFoundException, InviteNotFoundException {
        inviteList.add(invite);
        GroupJPAService groupJPAService = mock(GroupJPAService.class);
        when(groupJPAService.searchUsingCode(anyString())).thenThrow(GroupNotFoundException.class);
        when(mockedEntityManager.createQuery(anyString(),any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(inviteList);
        when(mockedEntityManager.getTransaction()).thenReturn(mockedEntityTransaction);
        when(mockedEntityManager.find(any(), anyInt())).thenReturn(invite);
        inviteJPAService.setEntityManager(mockedEntityManager);
        inviteJPAService.setGroupJPA(groupJPAService);
        inviteJPAService.searchInviteByGroupCode("Banjo", userTwo);
    }

}







































