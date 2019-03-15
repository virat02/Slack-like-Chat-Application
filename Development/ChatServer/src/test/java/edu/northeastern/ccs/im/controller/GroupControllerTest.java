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
    private GroupController groupController;
    private GroupService groupService;
    private Group groupOne;
    private User userOne;
    private List<Group> groupList;

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

    @Test
    public void test(){
        when(groupService.create(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).create(any());
    }

    @Test
    public void test1(){
        when(groupService.searchUsingCode(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.getEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    @Test
    public void test2(){
        when(groupService.update(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.updateEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).update(any());
    }

    @Test
    public void test3(){
        when(groupService.delete(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).delete(any());
    }

    @Test
    public void test4(){
        when(groupService.searchUsingCode(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    @Test
    public void test5(){
        when(groupService.searchUsingName(any())).thenReturn(groupList);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchAllGroup(groupOne.getName());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).searchUsingName(any());
    }

    @Test
    public void test6(){
        when(groupService.joinGroup(any())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

    @Test
    public void test7(){
        when(groupService.removeUserFromGroup(any(), anyInt())).thenReturn(groupOne);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getId());
        assertEquals(NetworkResponse.STATUS.SUCCESSFUL, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyInt());
    }

    @Test
    public void test8(){
        when(groupService.removeUserFromGroup(any(), anyInt())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.removeUserFromGroup(groupOne.getGroupCode(),userOne.getId());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).removeUserFromGroup(any(), anyInt());
    }

    @Test
    public void test9(){
        when(groupService.joinGroup(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.joinGroup(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).joinGroup(any());
    }

    @Test
    public void test10(){
        when(groupService.searchUsingCode(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.getEntity(groupOne.getGroupCode());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingCode(any());
    }

    @Test
    public void test11(){
        when(groupService.update(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.updateEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).update(any());
    }

    @Test
    public void test12(){
        when(groupService.delete(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.deleteEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).delete(any());
    }

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
        when(groupService.searchUsingName(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.searchAllGroup(groupOne.getName());
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).searchUsingName(any());
    }

    @Test
    public void test15(){
        when(groupService.create(any())).thenThrow(IllegalArgumentException.class);
        groupController.setGroupService(groupService);
        NetworkResponse networkResponse = groupController.addEntity(groupOne);
        assertEquals(NetworkResponse.STATUS.FAILED, networkResponse.status());
        verify(groupService).create(any());
    }


}
