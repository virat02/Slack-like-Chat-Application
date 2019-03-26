package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.customexceptions.GroupNotDeletedException;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.GroupNotPersistedException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupJPAServiceTest {

    /**
     * Initializing all the objects necessary for testing
     */

    private GroupJPAService groupJPAService;
    private User userOne;
    private Group groupOne;
    private Group groupTwo;
    private List<Group> groupList = new ArrayList<>();

    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Setting up the mock for testing groupService methods
     */
    @Before
    public void setUp(){
        userOne = new User();
        userOne.setUsername("Jalannin");
        userOne.setPassword("jjj");
        groupOne = new Group();
        groupTwo = new Group();
        groupOne.setName("GroupOneTest");
        groupOne.setId(1234);
        groupOne.setGroupCode("One23");
        groupTwo.setName("GroupTwoTest");
        groupTwo.setId(2345);

        groupList.add(groupOne);

        entityManager = mock(EntityManager.class);
        groupJPAService = new GroupJPAService();
        entityTransaction = mock(EntityTransaction.class);
    }

    /**
     * Testing the create group method
     */
    @Test
    public void testCreateGroup() throws GroupNotPersistedException {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.createGroup(groupOne);
    }

    /**
     * Testing the create group method for throwing a custom exception
     */
    @Test(expected = GroupNotPersistedException.class)
    public void testCreateGroupForException() throws GroupNotPersistedException{
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        doThrow(new EntityNotFoundException()).when(entityManager).persist(any(Group.class));
        groupJPAService.createGroup(groupOne);
    }

    /**
     * Testing the get group method
     */
    @Test
    public void testGetGroup() throws GroupNotFoundException {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.getGroup(groupOne.getId());
    }

    /**
     * Testing the get group method for throwing custom exception
     */
    @Test(expected = GroupNotFoundException.class)
    public void testGetGroupForException() throws GroupNotFoundException {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenThrow(new EntityNotFoundException());
        groupJPAService.getGroup(groupOne.getId());
    }

    /**
     * Testing the update group method
     */
    @Test
    public void testUpdateGroup() throws GroupNotFoundException{
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        assertTrue(groupJPAService.updateGroup(groupOne));
    }

    /**
     * Testing the update group method for throwing custom exception
     */
    @Test(expected = GroupNotFoundException.class)
    public void testUpdateGroupForException() throws GroupNotFoundException{
        when(entityManager.getTransaction()).thenReturn(entityTransaction);

        //When the group is not found, null is returned, so we mock that behavior
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.updateGroup(groupOne);
    }

    /**
     * Testing the update group method for throwing custom exception
     */
    @Test
    public void testUpdateGroupForFalse() throws GroupNotFoundException{
        when(entityManager.getTransaction()).thenReturn(entityTransaction);

        //We mock the behavior of a falsely updated group object returned by JPA
        when(entityManager.find(any(), anyInt())).thenReturn(groupTwo);
        groupJPAService.setEntityManager(entityManager);
        assertFalse(groupJPAService.updateGroup(groupOne));
    }

    /**
     * Testing the delete group method
     */
    @Test
    public void testDeleteGroup() throws GroupNotFoundException, GroupNotDeletedException {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        when(entityManager.find(any(), anyInt())).thenReturn(groupOne);
        Group newGroupList = groupJPAService.searchUsingCode(groupOne.getGroupCode());
        assertEquals(newGroupList,groupOne);
        groupJPAService.deleteGroup(groupOne);
    }

    /**
     * Testing the delete group method for throwing GroupNotFound custom exception
     */
    @Test(expected = GroupNotFoundException.class)
    public void testDeleteGroupForGroupNotFoundException() throws GroupNotFoundException, GroupNotDeletedException {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(new NoResultException());
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.deleteGroup(groupOne);
    }

    /**
     * Testing the delete group method for throwing GroupNotFound custom exception
     */
    @Test(expected = GroupNotDeletedException.class)
    public void testDeleteGroupForGroupNotDeletedException() throws GroupNotFoundException, GroupNotDeletedException {
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupTwo);
        when(entityManager.find(any(), anyInt())).thenThrow(new EntityNotFoundException());
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.deleteGroup(groupOne);
    }

    /**
     * Testing the add user to a group method
     */
    @Test
    public void testAddUserToGroup() throws GroupNotFoundException {

        User u = mock(User.class);
        Group g = mock(Group.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(Group.class, g.getId())).thenReturn(g);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.addUserToGroup(g.getId(),u);
    }

    /**
     * Testing the add user to a group method for a GroupNotFoundException
     */
    @Test(expected = GroupNotFoundException.class)
    public void testAddUserToGroupForGroupNotFoundException() throws GroupNotFoundException {

        User u = mock(User.class);
        Group g = mock(Group.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(Group.class, g.getId())).thenReturn(null);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.addUserToGroup(g.getId(),u);
    }

    /**
     * Testing the search group using name method
     */
    @Test
    public void testSearchUsingName() throws GroupNotFoundException{
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(groupList);
        groupJPAService.setEntityManager(entityManager);
        List<Group> newGroupList = groupJPAService.searchUsingName(groupOne.getName());
        assertEquals(newGroupList.size(),groupList.size());
    }

    /**
     * Testing the search group using name method for GroupNotFoundException
     */
    @Test(expected = GroupNotFoundException.class)
    public void testSearchUsingNameForGroupNotFoundException() throws GroupNotFoundException{
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenThrow(new IllegalArgumentException());
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.searchUsingName(groupOne.getName());
    }

    /**
     * Testing the search group using code method
     */
    @Test
    public void testSearchUsingCode() throws GroupNotFoundException{
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        Group newGroup = groupJPAService.searchUsingCode(groupOne.getGroupCode());
        assertEquals(newGroup,groupOne);
    }

    /**
     * Testing the search group using code method for GroupNotFoundException
     */
    @Test(expected = GroupNotFoundException.class)
    public void testSearchUsingCodeForGroupNotFoundException() throws GroupNotFoundException{
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(new IllegalArgumentException());
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.searchUsingCode(groupOne.getGroupCode());
    }

    /**
     * Testing the remove user from group method
     */
    @Test
    public void testRemoveUserFromGroup() throws UserNotFoundException{
        Query mockedQuery = mock(Query.class);
        UserJPAService userJPA = mock(UserJPAService.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.executeUpdate()).thenReturn(1);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.setUserJPAService(userJPA);
        when(userJPA.search(anyString())).thenReturn(userOne);
        int res = groupJPAService.removeUserFromGroup(groupOne, userOne.getUsername());
        assertEquals(1,res);
    }

    /**
     * Testing the remove user from group method for UserNotFoundException
     */
    @Test(expected = UserNotFoundException.class)
    public void testRemoveUserFromGroupForUserNotFoundException() throws UserNotFoundException{
        UserJPAService userJPA = mock(UserJPAService.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.setUserJPAService(userJPA);
        when(userJPA.search(anyString())).thenThrow(new UserNotFoundException("Could not find user!"));
        groupJPAService.removeUserFromGroup(groupOne, userOne.getUsername());
    }

    /**
     * Testing the remove user from group method for not being able to remove a user
     */
    @Test
    public void testRemoveUserFromGroupForFalse() throws UserNotFoundException{
        Query mockedQuery = mock(Query.class);
        UserJPAService userJPA = mock(UserJPAService.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.executeUpdate()).thenReturn(0);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.setUserJPAService(userJPA);
        when(userJPA.search(anyString())).thenReturn(userOne);
        int res = groupJPAService.removeUserFromGroup(groupOne, userOne.getUsername());
        assertEquals(0,res);
    }
}
