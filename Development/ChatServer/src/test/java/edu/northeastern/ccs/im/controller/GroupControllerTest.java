package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.user_group.Group;
import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupControllerTest {
<<<<<<< HEAD

    /**
     * Initializing all the objects necessary for testing
     */
=======
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
    private GroupController groupController;
    private GroupService groupService;
    private Group groupOne;
    private User userOne;
    private List<Group> groupList;

<<<<<<< HEAD
    /**
     * Setting up the mock for testing groupService methods
     */
=======
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
    @Before
    public void setUp() throws IOException {
        groupController = new GroupController();
        groupService = mock(GroupService.class);
        groupOne = new Group();
        groupOne.setName("groupOneTest");
        groupOne.setGroupCode("123group");
        groupList = new ArrayList<>();
        groupList.add(groupOne);
        userOne = new User();
        userOne.setPassword("hello123");
        userOne.setUsername("Hello");
        userOne.setId(12);


    }

<<<<<<< HEAD
    /**
     * Testing the create group method
     */
    @Test
    public void testCreateGroup(){
=======
    @Test
    public void test(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.create(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).create(any());
    }

<<<<<<< HEAD
    /**
     * Testing the searching using groupCode
     */
    @Test
    public void testSearchUsingCode(){
=======
    @Test
    public void test1(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.searchUsingCode(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.getEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

<<<<<<< HEAD
    /**
     * Testing the update group method
     */
    @Test
    public void testUpdateGroup(){
=======
    @Test
    public void test2(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.update(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.updateEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).update(any());
    }

<<<<<<< HEAD
    /**
     * Testing the delete group method
     */
    @Test
    public void testDeleteGroup(){
=======
    @Test
    public void test3(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.delete(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).delete(any());
    }

<<<<<<< HEAD
    /**
     * Testing the searching using groupCode
     */
    @Test
    public void testSearchUsingCodeEntity(){
=======
    @Test
    public void test4(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.searchUsingCode(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

<<<<<<< HEAD
    /**
     * Testing the searching using name
     */
    @Test
    public void testSearchUsingName(){
=======
    @Test
    public void test5(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.searchUsingName(any())).thenReturn(groupList);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchAllGroup(groupOne.getName());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingName(any());
    }

<<<<<<< HEAD
    /**
     * Testing the join Group
     */
    @Test
    public void testJoinGroup(){
=======
    @Test
    public void test6(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.joinGroup(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

<<<<<<< HEAD
    /**
     * Testing the remove user from group
     */
    @Test
    public void testRemoveUSerFromGroup(){
=======
    @Test
    public void test7(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.removeUserFromGroup(any(), anyInt())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getId());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyInt());
    }

<<<<<<< HEAD
    /**
     * Testing the remove user from group with exception thrown
     */
    @Test
    public void testRemoveUserException(){
=======
    @Test
    public void test8(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.removeUserFromGroup(any(), anyInt())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getId());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyInt());
    }

<<<<<<< HEAD
    /**
     * Testing the join Group with exception
     */
    @Test
    public void testJoinGroupException(){
=======
    @Test
    public void test9(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.joinGroup(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

<<<<<<< HEAD
    /**
     * Testing the searching using groupCode with exception
     */
    @Test
    public void testSearchUsingCodeException(){
=======
    @Test
    public void test10(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.searchUsingCode(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.getEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

<<<<<<< HEAD
    /**
     * Testing the update group with exception
     */
    @Test
    public void testUpdateException(){
=======
    @Test
    public void test11(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.update(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.updateEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).update(any());
    }

<<<<<<< HEAD
    /**
     * Testing the delete group method with exception
     */
    @Test
    public void testDeleteException(){
=======
    @Test
    public void test12(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.delete(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).delete(any());
    }

<<<<<<< HEAD
    /**
     * testing the search using name method with exception
     */
    @Test
    public void testSearchUsingNameException(){
=======
    @Test
    public void test13(){
        when(groupService.searchUsingCode(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    @Test
    public void test14(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.searchUsingName(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchAllGroup(groupOne.getName());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingName(any());
    }
<<<<<<< HEAD
    /**
     * testing the create group method with exception
     */
    @Test
    public void testCreateException(){
=======

    @Test
    public void test15(){
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
        when(groupService.create(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).create(any());
    }


}
