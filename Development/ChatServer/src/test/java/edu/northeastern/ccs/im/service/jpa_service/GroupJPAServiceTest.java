package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupJPAServiceTest {

    /**
     * Initializing all the objects necessary for testing
     */
    private GroupJPAService groupJPAService;
    private GroupService groupService;
    private User userOne;
    private User userTwo;
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
    public void testCreateGroup(){
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.createGroup(groupOne);
    }

    /**
     * Testing the get group method
     */
    @Test
    public void testGetGroup(){
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.getGroup(groupOne.getId());
    }

    /**
     * Testing the update group method
     */
    @Test
    public void testUpdateGroup(){
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.updateGroup(groupOne);
    }

    /**
     * Testing the update group method with exception thrown
     */
    @Test (expected = EntityNotFoundException.class)
    public void testUpdateGroupWithExcception() {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.updateGroup(groupOne);
    }

    /**
     * Testing the delete group method
     */
    @Test
    public void testDeleteGroup() {
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        Group newGroupList = groupJPAService.searchUsingCode(groupOne.getGroupCode());
        assertEquals(newGroupList,groupOne);
        groupJPAService.deleteGroup(groupOne);
    }

    /**
     * Testing the add user to a group method
     */
    @Test
    public void testAddUserToGroup(){
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.addUserToGroup(groupOne.getId(),userOne);
    }

    /**
     * Testing the search group using name  method
     */
    @Test
    public void testSearchUsingName(){
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(groupList);
        groupJPAService.setEntityManager(entityManager);
        List<Group> newGroupList = groupJPAService.searchUsingName(groupOne.getName());
        assertEquals(1,groupList.size());
    }

    /**
     * Testing the search group using code  method
     */
    @Test
    public void testSearchUsingCode(){
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        Group newGroupList = groupJPAService.searchUsingCode(groupOne.getGroupCode());
        assertEquals(newGroupList,groupOne);
    }

    /**
     * Testing the remove user from group method
     */
    @Test
    public void testRemoveUserFromGroup(){
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.executeUpdate()).thenReturn(1);
        groupJPAService.setEntityManager(entityManager);
        int res = groupJPAService.removeUserFromGroup(groupOne, userOne.getId());
        assertEquals(1,res);

    }

    /**
     * Testing the search group using name  method
     */
    @Test
    public void test10() {
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.executeUpdate()).thenReturn(0);
        groupJPAService.setEntityManager(entityManager);
        int res = groupJPAService.removeUserFromGroup(groupOne, userOne.getId());
        assertEquals(0, res);
    }

}
