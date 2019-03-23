package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.service.UserService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupJPAServiceTest {

<<<<<<< HEAD
    /**
     * Initializing all the objects necessary for testing
     */

=======
    private UserJPAService userJPAService;
    private UserService userService;
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
    private GroupJPAService groupJPAService;
    private GroupService groupService;
    private User userOne;
    private User userTwo;
    private Group groupOne;
    private Group groupTwo;
    private List<Group> groupList = new ArrayList<>();

    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

<<<<<<< HEAD
    /**
     * Setting up the mock for testing groupService methods
     */
=======
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
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

<<<<<<< HEAD
    /**
     * Testing the create group method
     */
    @Test
    public void testCreateGroup(){
=======
    @Test
    public void test1(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.createGroup(groupOne);
    }

<<<<<<< HEAD
    /**
     * Testing the get group method
     */
    @Test
    public void testGetGroup(){
=======
    @Test
    public void test2(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.getGroup(groupOne.getId());
    }

<<<<<<< HEAD
    /**
     * Testing the update group method
     */
    @Test
    public void testUpdateGroup(){
=======
    @Test
    public void test3(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.updateGroup(groupOne);
    }

<<<<<<< HEAD
    /**
     * Testing the update group method with exception thrown
     */
    @Test (expected = EntityNotFoundException.class)
    public void testUpdateGroupWithExcception() {
=======
    @Test (expected = EntityNotFoundException.class)
    public void test4() {
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.updateGroup(groupOne);
    }

<<<<<<< HEAD
    /**
     * Testing the delete group method
     */
    @Test
    public void testDeleteGroup() {
=======
    @Test
    public void test5() {
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        Group newGroupList = groupJPAService.searchUsingCode(groupOne.getGroupCode());
        assertEquals(newGroupList,groupOne);
        groupJPAService.deleteGroup(groupOne);
    }

<<<<<<< HEAD
    /**
     * Testing the add user to a group method
     */
    @Test
    public void testAddUserToGroup(){
=======
    @Test
    public void test6(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.addUserToGroup(groupOne.getId(),userOne);
    }

<<<<<<< HEAD
    /**
     * Testing the search group using name  method
     */
    @Test
    public void testSearchUsingName(){
=======
    @Test
    public void test7(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(groupList);
        groupJPAService.setEntityManager(entityManager);
        List<Group> newGroupList = groupJPAService.searchUsingName(groupOne.getName());
        assertEquals(1,groupList.size());
    }

<<<<<<< HEAD
    /**
     * Testing the search group using code  method
     */
    @Test
    public void testSearchUsingCode(){
=======
    @Test
    public void test8(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        Group newGroupList = groupJPAService.searchUsingCode(groupOne.getGroupCode());
        assertEquals(newGroupList,groupOne);
    }

<<<<<<< HEAD
    /**
     * Testing the remove user from group method
     */
    @Test
    public void testRemoveUserFromGroup(){
=======
    @Test
    public void test9(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.executeUpdate()).thenReturn(1);
        groupJPAService.setEntityManager(entityManager);
        int res = groupJPAService.removeUserFromGroup(groupOne, userOne.getId());
        assertEquals(1,res);

    }
<<<<<<< HEAD

    /**
     * Testing the search group using name  method
     */
=======
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
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
