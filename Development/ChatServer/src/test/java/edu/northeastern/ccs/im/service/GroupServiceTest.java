package edu.northeastern.ccs.im.service;

import static org.junit.Assert.*;

import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.northeastern.ccs.im.service.jpa_service.GroupJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	private UserJPAService userJPAService;
	private UserService userService;
	private GroupJPAService groupJPAService;
	private GroupService groupService;
	private User userOne;
	private User userTwo;
	private Group groupOne;
	private Group groupTwo;
	private List<Group> groupList = new ArrayList<>();
	private List<User> userList = new ArrayList<>();
	
	@Before
	public void setUp() {
		groupOne = new Group();
		groupTwo = new Group();
		groupOne.setName("GroupOneTest");
		groupOne.setId(1234);
		groupOne.setGroupCode("One23");
		groupTwo.setName("GroupTwoTest");
		groupTwo.setId(2345);
		
		userOne = new User();
        userTwo = new User();
        userOne.setUsername("Jerry");
        userOne.setPassword("Banjo");
        userOne.setId(123);

        userTwo.setUsername("Danny");
        userTwo.setPassword("Dragons");
        userTwo.setId(2);

        groupList.add(groupOne);
        groupList.add(groupTwo);
        groupOne.addUser(userOne);
        userList.add(userOne);
        
        groupService = new GroupService();
        groupJPAService = mock(GroupJPAService.class);

		userService = new UserService();
		userJPAService = mock(UserJPAService.class);
	}
	
	
	
	@Test
	public void test() {
		when(groupJPAService.createGroup(any())).thenReturn(1);
        when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
        groupService.setJPAService(groupJPAService);
        Group newGroup = groupService.create(groupOne);
        
        assert newGroup != null;
        assertEquals(groupOne,newGroup);
        verify(groupJPAService).createGroup(any());

	}

	@Test
	public void test2() {
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.update(groupOne);

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	@Test
	public void test3(){
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.delete(groupOne);

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	@Test
	public void test4(){
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.get(groupOne.getId());

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	@Test
	public void test5(){

		when(groupJPAService.searchUsingCode(any())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.searchUsingCode(groupOne.getGroupCode());

		verify(groupJPAService).searchUsingCode(any());
		assertEquals(groupOne,newGroup);
	}

	@Test
	public void test6(){
		when(groupJPAService.searchUsingCode(any())).thenReturn(groupOne);
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		when(groupJPAService.removeUserFromGroup(any(),anyInt())).thenReturn(1);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.removeUserFromGroup(groupOne.getGroupCode(),userOne.getId());
		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

//	@Test
//	public void test7(){
//		when(groupJPAService.searchUsingCode(any())).thenReturn(groupOne);
//		when(groupOne.getUsers()).thenReturn(userList);
//		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
//		when(userJPAService.getUser(anyInt())).thenReturn(userOne);
//		groupService.setJPAService(groupJPAService);
//		userService.setJPAService(userJPAService);
//		Group newGroup = groupService.joinGroup(groupOne);
//		verify(groupJPAService).searchUsingCode(any());
//		assertEquals(groupOne,newGroup);
//		verify(groupJPAService).getGroup(anyInt());
//	}

	@Test
	public void test8(){
		when(groupJPAService.searchUsingName(any())).thenReturn(groupList);
		groupService.setJPAService(groupJPAService);
		List<Group> newGroup = groupService.searchUsingName(groupOne.getName());
		verify(groupJPAService).searchUsingName(any());
		assertEquals(groupList,newGroup);

	}

}
