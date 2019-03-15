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

    private UserJPAService userJPAService;
    private UserService userService;
    private GroupJPAService groupJPAService;
    private GroupService groupService;
    private User userOne;
    private User userTwo;
    private Group groupOne;
    private Group groupTwo;
    private List<Group> groupList = new ArrayList<>();

    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

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

    @Test
    public void test1(){
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.createGroup(groupOne);
    }

    @Test
    public void test2(){
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.getGroup(groupOne.getId());
    }

    @Test
    public void test3(){
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.updateGroup(groupOne);
    }

    @Test (expected = EntityNotFoundException.class)
    public void test4() {
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(null);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.updateGroup(groupOne);
    }

    @Test
    public void test5() {
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        Group newGroupList = groupJPAService.searchUsingCode(groupOne.getGroupCode());
        assertEquals(newGroupList,groupOne);
        groupJPAService.deleteGroup(groupOne);
    }

    @Test
    public void test6(){
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(any(), anyInt())).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        groupJPAService.addUserToGroup(groupOne.getId(),userOne);
    }

    @Test
    public void test7(){
        TypedQuery mockedQuery = mock(TypedQuery.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString(), any())).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(groupList);
        groupJPAService.setEntityManager(entityManager);
        List<Group> newGroupList = groupJPAService.searchUsingName(groupOne.getName());
        assertEquals(1,groupList.size());
    }

    @Test
    public void test8(){
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(groupOne);
        groupJPAService.setEntityManager(entityManager);
        Group newGroupList = groupJPAService.searchUsingCode(groupOne.getGroupCode());
        assertEquals(newGroupList,groupOne);
    }

    @Test
    public void test9(){
        Query mockedQuery = mock(Query.class);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockedQuery);
        when(mockedQuery.executeUpdate()).thenReturn(1);
        groupJPAService.setEntityManager(entityManager);
        int res = groupJPAService.removeUserFromGroup(groupOne, userOne.getId());
        assertEquals(1,res);

    }
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
