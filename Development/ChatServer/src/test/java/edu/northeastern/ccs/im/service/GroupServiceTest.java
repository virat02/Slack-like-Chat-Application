package edu.northeastern.ccs.im.service;

import static org.junit.Assert.*;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.AllJPAService;
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

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	/**
	 * Initializing all the objects necessary for testing
	 */
	private UserJPAService userJPAService;
	private GroupJPAService groupJPAService;
	private GroupService groupService;
	private AllJPAService jpaService;
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
		groupOne.setUsers(userList);
		groupOne.setGroupCode("One23");
		groupTwo.setName("GroupTwoTest");
		groupTwo.setId(2345);
		groupTwo.setGroupCode("Hello");

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

		groupService = GroupService.getInstance();
		groupJPAService = mock(GroupJPAService.class);

		userJPAService = mock(UserJPAService.class);
		jpaService = mock(AllJPAService.class);
	}


	/**
	 * Testing the create group method
	 */
	@Test
	public void testCreateGroup() {
		when(jpaService.createEntity(any())).thenReturn(true);
		when(jpaService.getEntity(anyString(), anyInt())).thenReturn(groupOne);
		groupService.setAllService(jpaService);
		assertTrue(groupService.create(groupOne));
	}

	/**
	 * Testing the createIfNotPresent group method for UserNotPresentException
	 */
	@Test(expected = UserNotPresentInTheGroup.class)
	public void testCreateIfNotPresentForPublicGroupForUserAlreadyExistsException()
			throws GroupNotFoundException, UserNotPresentInTheGroup, UserNotFoundException {

		Group g = mock(Group.class);
		groupService.setUserService(userJPAService);
		groupService.setJPAService(groupJPAService);
		when(g.getUsers()).thenReturn(Collections.emptyList());
		when(userJPAService.search(anyString())).thenReturn(userOne);
		when(groupJPAService.searchUsingCode(anyString())).thenReturn(g);
		groupService.createIfNotPresent("abc", "Jerry", false);

	}

	/**
	 * Testing the createIfNotPresent group method for successful
	 */
	@Test
	public void testCreateIfNotPresentForPublicGroup()
			throws GroupNotFoundException, UserNotPresentInTheGroup, UserNotFoundException {

		Group g = mock(Group.class);
		groupService.setUserService(userJPAService);
		groupService.setJPAService(groupJPAService);
		when(g.getUsers()).thenReturn(userList);
		when(userJPAService.search(anyString())).thenReturn(userOne);
		when(groupJPAService.searchUsingCode(anyString())).thenReturn(g);
		assertTrue(groupService.createIfNotPresent("abc", "Jerry", false));

	}

	/**
	 * Testing the createIfNotPresent group method for already existing private group
	 */
	@Test
	public void testCreateIfNotPresentForPrivateGroupAlreadyExists()
			throws GroupNotFoundException, UserNotPresentInTheGroup, UserNotFoundException {

		Group g = mock(Group.class);
		groupService.setJPAService(groupJPAService);
		when(groupJPAService.searchUsingCode(anyString())).thenReturn(g);
		assertTrue(groupService.createIfNotPresent("Jerry_Virat", "Jerry", true));

	}

	/**
	 * Testing the createIfNotPresent group method for already creating a new private group
	 */
	@Test
	public void testCreateIfNotPresentForPrivateGroup()
			throws GroupNotFoundException, UserNotPresentInTheGroup, UserNotFoundException {

		groupService.setUserService(userJPAService);
		groupService.setJPAService(groupJPAService);
		groupService.setAllService(jpaService);
		when(userJPAService.search("Jerry")).thenReturn(userOne);
		when(userJPAService.search("Danny")).thenReturn(userTwo);
		when(groupJPAService.searchUsingCode(anyString()))
				.thenThrow(new GroupNotFoundException("Could not find group!"));
		when(jpaService.createEntity(any(Group.class))).thenReturn(true);

		assertTrue(groupService.createIfNotPresent("Danny_Jerry", "Danny", true));
	}

	/**
	 * Testing the update group method
	 */
	@Test
	public void testUpdateGroup() throws GroupNotFoundException {
		when(jpaService.getEntity(anyString(), anyInt())).thenReturn(groupOne);
		when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
		when(groupJPAService.updateGroup(any())).thenReturn(true);
		groupService.setJPAService(groupJPAService);
		groupService.setAllService(jpaService);
		Group newGroup = groupService.update(groupOne);

		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the update group method
	 */
	@Test
	public void testUpdateGroupForFalse() throws GroupNotFoundException {
		when(jpaService.getEntity(anyString(), anyInt())).thenReturn(groupTwo);
		when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
		when(groupJPAService.updateGroup(any())).thenReturn(false);
		groupService.setJPAService(groupJPAService);
		assertNotEquals(groupOne,groupService.update(groupOne));
	}

	/**
	 * Testing the update group method
	 */
	@Test(expected = GroupNotFoundException.class)
	public void testUpdateGroupForGroupNotFoundException() throws GroupNotFoundException {
		when(groupJPAService.searchUsingCode(anyString()))
				.thenThrow(new GroupNotFoundException("Could not find group!"));
		groupService.setJPAService(groupJPAService);
		groupService.update(groupOne);
	}

	/**
	 * Testing the delete group method
	 */
	@Test
	public void testDeleteGroup() throws GroupNotFoundException {
		when(jpaService.deleteEntity(any())).thenReturn(true);
		when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
		groupService.setAllService(jpaService);
		groupService.setJPAService(groupJPAService);
		assertTrue(groupService.delete(groupOne));
	}


	/**
	 * Testing the delete group method for false
	 */
	@Test
	public void testDeleteGroupForFalse() throws GroupNotFoundException {
		when(jpaService.deleteEntity(any())).thenReturn(false);
		when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		groupService.setAllService(jpaService);
		assertFalse(groupService.delete(groupOne));
	}

	/**
	 * Testing the get group method
	 */
	@Test
	public void testGetGroup(){
		when(jpaService.getEntity(anyString(), anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = (Group) jpaService.getEntity("Group", groupOne.getId());

		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the searching using groupCode
	 */
	@Test
	public void testSearchUsingCode() throws GroupNotFoundException{

		when(groupJPAService.searchUsingCode(any())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.searchUsingCode(groupOne.getGroupCode());

		verify(groupJPAService).searchUsingCode(any());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the searching using groupCode for throwing GroupNotFoundException
	 */
	@Test(expected = GroupNotFoundException.class)
	public void testSearchUsingCodeForGroupNotFoundException() throws GroupNotFoundException{

		when(groupJPAService.searchUsingCode(any())).thenThrow(new GroupNotFoundException("Could not find group!"));
		groupService.setJPAService(groupJPAService);
		groupService.searchUsingCode(groupOne.getGroupCode());
	}

	/**
	 * Testing the remove user from group
	 */
	@Test
	public void testRemoveUserFromGroup() throws GroupNotFoundException, UserNotFoundException {
		when(groupJPAService.searchUsingCode(any())).thenReturn(groupOne);
		when(jpaService.getEntity(anyString(), anyInt())).thenReturn(groupOne);
		when(groupJPAService.removeUserFromGroup(any(),anyString())).thenReturn(1);
		groupService.setJPAService(groupJPAService);
		groupService.setAllService(jpaService);
		Group newGroup = groupService.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the remove user from group for throwing GroupNotFoundException
	 */
	@Test(expected = GroupNotFoundException.class)
	public void testRemoveUserFromGroupForGroupNotFoundException() throws GroupNotFoundException, UserNotFoundException {

		when(groupJPAService.searchUsingCode(anyString())).thenThrow(new GroupNotFoundException("Could not find group!"));
		groupService.setJPAService(groupJPAService);
		groupService.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
	}

	/**
	 * Testing the remove user from group for throwing GroupNotFoundException
	 */
	@Test(expected = UserNotFoundException.class)
	public void testRemoveUserFromGroupForUserNotFoundException() throws GroupNotFoundException, UserNotFoundException {

		when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
		when(groupJPAService.removeUserFromGroup(any(),anyString())).thenThrow(new UserNotFoundException("Could not find user to be removed!"));
		groupService.setJPAService(groupJPAService);
		groupService.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
	}

	/**
	 * Testing the remove user from group for throwing GroupNotFoundException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveUserFromGroupForIllegalArgumentException() throws GroupNotFoundException, UserNotFoundException {

		when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
		when(groupJPAService.removeUserFromGroup(any(),anyString())).thenThrow(new IllegalArgumentException("Could not find user to be removed!"));
		groupService.setJPAService(groupJPAService);
		groupService.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
	}

	/**
	 * testing the search using name method
	 */
	@Test
	public void testSearchUsingName() throws GroupNotFoundException{
		when(groupJPAService.searchUsingName(any())).thenReturn(groupList);
		groupService.setJPAService(groupJPAService);
		List<Group> newGroup = groupService.searchUsingName(groupOne.getName());
		verify(groupJPAService).searchUsingName(any());
		assertEquals(groupList,newGroup);

	}
}