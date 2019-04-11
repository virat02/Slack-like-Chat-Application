package edu.northeastern.ccs.im.controller;

import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.customexceptions.GroupNotDeletedException;
import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.GroupNotPersistedException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
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
    public void testCreateGroup() throws GroupNotPersistedException, GroupNotFoundException {
        when(groupService.create(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).create(any());
    }

    /**
     * Testing the create group method for GroupNotPersistedException
     */
    @Test
    public void testCreateGroupForGroupNotPersistedException() throws GroupNotPersistedException, GroupNotFoundException {
        when(groupService.create(any())).thenThrow(new GroupNotPersistedException("Could not persist group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).create(any());
    }

    /**
     * Testing the create group method for GroupNotFoundException
     */
    @Test
    public void testCreateGroupForGroupNotFoundException() throws GroupNotPersistedException, GroupNotFoundException {
        when(groupService.create(any())).thenThrow(new GroupNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).create(any());
    }

    /**
     * Testing the searching using groupCode
     */
    @Test
    public void testSearchUsingCode() throws GroupNotFoundException{
        when(groupService.searchUsingCode(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.getEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    /**
     * Testing the searching using groupCode for throwing GroupNotFoundException
     */
    @Test
    public void testSearchUsingCodeForGroupNotFoundException() throws GroupNotFoundException{
        when(groupService.searchUsingCode(any())).thenThrow(new GroupNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.getEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    /**
     * Testing the update group method
     */
    @Test
    public void testUpdateGroup() throws GroupNotFoundException {
        when(groupService.update(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.updateEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).update(any());
    }

    /**
     * Testing the update group method for GroupNotFoundException
     */
    @Test
    public void testUpdateGroupForGroupNotFoundException() throws GroupNotFoundException {
        when(groupService.update(any())).thenThrow(new GroupNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.updateEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).update(any());
    }

    /**
     * Testing the delete group method
     */
    @Test
    public void testDeleteGroup() throws GroupNotFoundException, GroupNotDeletedException {
        when(groupService.delete(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).delete(any());
    }

    /**
     * Testing the delete group method for throwing GroupNotFoundException
     */
    @Test
    public void testDeleteGroupForGroupNotFoundException() throws GroupNotFoundException, GroupNotDeletedException {
        when(groupService.delete(any())).thenThrow(new GroupNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).delete(any());
    }

    /**
     * Testing the delete group method for throwing GroupNotDeletedException
     */
    @Test
    public void testDeleteGroupForGroupNotDeletedException() throws GroupNotFoundException, GroupNotDeletedException {
        when(groupService.delete(any())).thenThrow(new GroupNotDeletedException("Could not delete group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).delete(any());
    }

    /**
     * Testing the searching using groupCode
     */
    @Test
    public void testSearchUsingCodeEntity() throws GroupNotFoundException {
        when(groupService.searchUsingCode(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    /**
     * Testing the searching using groupCode for throwing GroupNotFoundException
     */
    @Test
    public void testSearchUsingCodeEntityForGroupNotFoundException() throws GroupNotFoundException {
        when(groupService.searchUsingCode(any())).thenThrow(new GroupNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    /**
     * Testing the searching using name
     */
    @Test
    public void testSearchUsingName() throws GroupNotFoundException {
        when(groupService.searchUsingName(any())).thenReturn(groupList);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchAllGroup(groupOne.getName());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingName(any());
    }

    /**
     * Testing the searching using name for throwing GroupNotFoundException
     */
    @Test
    public void testSearchUsingNameForGroupNotFoundException() throws GroupNotFoundException {
        when(groupService.searchUsingName(any())).thenThrow(new GroupNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchAllGroup(groupOne.getName());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingName(any());
    }

    /**
     * Testing the join Group
     */
    @Test
    public void testJoinGroup() throws GroupNotFoundException, UserNotFoundException {
        when(groupService.joinGroup(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

    /**
     * Testing the join Group for throwing GroupNotFoundException
     */
    @Test
    public void testJoinGroupForGroupNotFoundException() throws GroupNotFoundException, UserNotFoundException {
        when(groupService.joinGroup(any())).thenThrow(new GroupNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

    /**
     * Testing the join Group for throwing UserNotFoundException
     */
    @Test
    public void testJoinGroupForUserNotFoundException() throws GroupNotFoundException, UserNotFoundException {
        when(groupService.joinGroup(any())).thenThrow(new UserNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

    /**
     * Testing the remove user from group
     */
    @Test
    public void testRemoveUserFromGroup() throws GroupNotFoundException, UserNotFoundException {
        when(groupService.removeUserFromGroup(any(), anyString())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyString());
    }

    /**
     * Testing the remove user from group for throwing GroupNotFoundException
     */
    @Test
    public void testRemoveUserFromGroupForGroupNotFoundException() throws GroupNotFoundException, UserNotFoundException {
        when(groupService.removeUserFromGroup(any(), anyString())).thenThrow(new GroupNotFoundException("Could not find group!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyString());
    }

    /**
     * Testing the remove user from group for throwing UserNotFoundException
     */
    @Test
    public void testRemoveUserFromGroupForUserNotFoundException() throws GroupNotFoundException, UserNotFoundException {
        when(groupService.removeUserFromGroup(any(), anyString())).thenThrow(new UserNotFoundException("Could not find user!"));
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyString());
    }

    /**
     * Testing the remove user from group for throwing IllegalArgumentException
     */
    @Test
    public void testRemoveUserFromGroupForIllegalArgumentException() throws GroupNotFoundException, UserNotFoundException {
        when(groupService.removeUserFromGroup(any(), anyString())).thenThrow(new IllegalArgumentException());
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyString());
    }
}
