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

    /**
     * Initializing all the objects necessary for testing
     */
    private GroupController groupController;
    private GroupService groupService;
    private Group groupOne;
    private User userOne;
    private List<Group> groupList;

    /**
     * Setting up the mock for testing groupService methods
     */
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

    /**
     * Testing the create group method
     */
    @Test
    public void testCreateGroup(){
        when(groupService.create(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).create(any());
    }

    /**
     * Testing the searching using groupCode
     */
    @Test
    public void testSearchUsingCode(){
        when(groupService.searchUsingCode(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.getEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    /**
     * Testing the update group method
     */
    @Test
    public void testUpdateGroup(){
        when(groupService.update(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.updateEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).update(any());
    }

    /**
     * Testing the delete group method
     */
    @Test
    public void testDeleteGroup(){
        when(groupService.delete(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).delete(any());
    }

    /**
     * Testing the searching using groupCode
     */
    @Test
    public void testSearchUsingCodeEntity(){
        when(groupService.searchUsingCode(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    /**
     * Testing the searching using name
     */
    @Test
    public void testSearchUsingName(){
        when(groupService.searchUsingName(any())).thenReturn(groupList);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchAllGroup(groupOne.getName());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingName(any());
    }

    /**
     * Testing the join Group
     */
    @Test
    public void testJoinGroup(){
        when(groupService.joinGroup(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

    /**
     * Testing the remove user from group
     */
    @Test
    public void testRemoveUSerFromGroup(){
        when(groupService.removeUserFromGroup(any(), anyInt())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getId());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyInt());
    }

    /**
     * Testing the remove user from group with exception thrown
     */
    @Test
    public void testRemoveUserException(){
        when(groupService.removeUserFromGroup(any(), anyInt())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getId());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyInt());
    }

    /**
     * Testing the join Group with exception
     */
    @Test
    public void testJoinGroupException(){
        when(groupService.joinGroup(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

    /**
     * Testing the searching using groupCode with exception
     */
    @Test
    public void testSearchUsingCodeException(){
        when(groupService.searchUsingCode(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.getEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    /**
     * Testing the update group with exception
     */
    @Test
    public void testUpdateException(){
        when(groupService.update(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.updateEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).update(any());
    }

    /**
     * Testing the delete group method with exception
     */
    @Test
    public void testDeleteException(){
        when(groupService.delete(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).delete(any());
    }

    /**
     * testing the search using name method with exception
     */
    @Test
    public void testSearchUsingNameException(){
        when(groupService.searchUsingName(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchAllGroup(groupOne.getName());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingName(any());
    }
    /**
     * testing the create group method with exception
     */
    @Test
    public void testCreateException(){
        when(groupService.create(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).create(any());
    }


}
