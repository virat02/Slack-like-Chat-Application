package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.service.jpa_service.GroupJPAService;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	/**
	 * Initializing all the objects necessary for testing
	 */
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

	/**
	 * Setting up the mock for testing groupService methods
	 */
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


	/**
	 * Testing the create group method
	 */
	@Test
	public void testCreateGroup() {
		when(groupJPAService.createGroup(any())).thenReturn(1);
        when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
        groupService.setJPAService(groupJPAService);
        Group newGroup = groupService.create(groupOne);
        
        assert newGroup != null;
        assertEquals(groupOne,newGroup);
        verify(groupJPAService).createGroup(any());

	}

	/**
	 * Testing the update group method
	 */
	@Test
	public void testUpdateGroup() {
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.update(groupOne);

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the delete group method
	 */
	@Test
	public void testDeleteGroup(){
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.delete(groupOne);

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the get group method
	 */
	@Test
	public void testGetGroup(){
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.get(groupOne.getId());

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the searching using groupCode
	 */
	@Test
	public void testSearchUsingCode(){
		when(groupJPAService.searchUsingCode(any())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.searchUsingCode(groupOne.getGroupCode());

		verify(groupJPAService).searchUsingCode(any());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the remove user from group
	 */
	@Test
	public void testremoveUserFromGroup(){
		when(groupJPAService.searchUsingCode(any())).thenReturn(groupOne);
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		when(groupJPAService.removeUserFromGroup(any(),anyInt())).thenReturn(1);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.removeUserFromGroup(groupOne.getGroupCode(),userOne.getId());
		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * testing join Group method
	 */
	@Test
	public void testJoinGroup(){
		GroupService gs = mock(GroupService.class);
		GroupJPAService groupJPAService = mock(GroupJPAService.class);
		UserJPAService userJPAService = mock(UserJPAService.class);
		when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
		when(userJPAService.getUser(anyInt())).thenReturn(userOne);
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		groupService.setUserService(userJPAService);
		Group g = groupService.joinGroup(groupOne);
		assertEquals(g, groupOne);
	}

	/**
	 * testing the search using name method
	 */
	@Test
	public void testSearchUsingName(){
		when(groupJPAService.searchUsingName(any())).thenReturn(groupList);
		groupService.setJPAService(groupJPAService);
		List<Group> newGroup = groupService.searchUsingName(groupOne.getName());
		verify(groupJPAService).searchUsingName(any());
		assertEquals(groupList,newGroup);

	}

	/**
	 * Test create if not present method
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateIfNotPresent() {
		GroupJPAService gpa = mock(GroupJPAService.class);
		groupService.setJPAService(gpa);

		when(gpa.searchUsingCode(anyString())).thenThrow(new NullPointerException());
		when(gpa.createGroup(any(Group.class))).thenReturn(1);

		assertEquals(1, groupService.createIfNotPresent("ABC"));

	}
}
