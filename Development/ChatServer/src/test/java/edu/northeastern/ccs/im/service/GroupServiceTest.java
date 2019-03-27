package edu.northeastern.ccs.im.service;

import static org.junit.Assert.*;

import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import edu.northeastern.ccs.im.service.jpa_service.GroupJPAService;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.User;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

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
		groupOne.setUsers(userList);
		groupOne.setGroupCode("One23");
		groupTwo.setName("GroupTwoTest");
		groupTwo.setId(2345);

		userList.add(userOne);

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
	public void testCreateGroup() throws GroupNotPersistedException, GroupNotFoundException {
		when(groupJPAService.createGroup(any())).thenReturn(1);
        when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
        groupService.setJPAService(groupJPAService);
        Group newGroup = groupService.create(groupOne);

        assert newGroup != null;
        assertEquals(groupOne,newGroup);
        verify(groupJPAService).createGroup(any());
	}

//    /**
//     * Testing the createIfNotPresent group method
//     */
//    @Test(expected = UserAlreadyPresentInGroupException.class)
//    public void testCreateIfNotPresentForPublicGroupForUserAlreadyExistsException()
//			throws GroupNotPersistedException, GroupNotFoundException, UserAlreadyPresentInGroupException, UserNotFoundException {
//
////        when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
////        groupService.setJPAService(groupJPAService);
////        assertTrue(groupService.createIfNotPresent(groupOne.getGroupCode()));
//		groupService.setUserService(userJPAService);
//		when(userJPAService.search(anyString())).thenReturn(userOne);
//		when(groupJPAService.searchUsingCode(anyString())).thenReturn(groupOne);
//		assertFalse(groupService.createIfNotPresent(groupOne.getGroupCode(), userOne.getUsername(), false));
//
//    }
//
//    /**
//     * Testing the createIfNotPresent group method for GroupNotPersistedException
//     */
//    @Test(expected = GroupNotPersistedException.class)
//    public void testCreateIfNotPresentForGroupNotPersistedException() throws GroupNotPersistedException, GroupNotFoundException {
////        when(groupJPAService.searchUsingCode(anyString())).thenThrow(new GroupNotFoundException("JPA could not find group!"));
////        when(groupJPAService.createGroup(any())).thenThrow(new GroupNotPersistedException("JPA Could not persist group!"));
////        groupService.setJPAService(groupJPAService);
////        groupService.createIfNotPresent(groupOne.getGroupCode());
//
//
//
//    }

	/**
	 * Testing the update group method
	 */
	@Test
	public void testUpdateGroup() throws GroupNotFoundException {
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		when(groupJPAService.updateGroup(any())).thenReturn(true);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.update(groupOne);

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the update group method
	 */
	@Test
	public void testUpdateGroupForFalse() throws GroupNotFoundException {
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupTwo);
		when(groupJPAService.updateGroup(any())).thenReturn(false);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.update(groupOne);

		verify(groupJPAService).getGroup(anyInt());
		assertNotEquals(groupOne,newGroup);
	}

	/**
	 * Testing the update group method
	 */
	@Test(expected = GroupNotFoundException.class)
	public void testUpdateGroupForGroupNotFoundException() throws GroupNotFoundException {
		when(groupJPAService.getGroup(anyInt())).thenThrow(new GroupNotFoundException("Could not find group"));
		when(groupJPAService.updateGroup(any())).thenReturn(true);
		groupService.setJPAService(groupJPAService);
		groupService.update(groupOne);
	}

	/**
	 * Testing the delete group method
	 */
	@Test
	public void testDeleteGroup() throws GroupNotFoundException, GroupNotDeletedException {
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.delete(groupOne);

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the delete group method for throwing GroupNotFoundException
	 */
	@Test(expected = GroupNotFoundException.class)
	public void testDeleteGroupForGroupNotFoundException() throws GroupNotFoundException, GroupNotDeletedException {
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		doThrow(new GroupNotFoundException("Group not found")).when(groupJPAService).deleteGroup(any());
		groupService.setJPAService(groupJPAService);
		groupService.delete(groupOne);
	}

	/**
	 * Testing the delete group method for throwing GroupNotDeletedException
	 */
	@Test(expected = GroupNotDeletedException.class)
	public void testDeleteGroupForGroupNotDeletedException() throws GroupNotFoundException, GroupNotDeletedException {
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		doThrow(new GroupNotDeletedException("Group not deleted")).when(groupJPAService).deleteGroup(any());
		groupService.setJPAService(groupJPAService);
		groupService.delete(groupOne);
	}

	/**
	 * Testing the get group method
	 */
	@Test
	public void testGetGroup() throws GroupNotFoundException{
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.get(groupOne.getId());

		verify(groupJPAService).getGroup(anyInt());
		assertEquals(groupOne,newGroup);
	}

	/**
	 * Testing the get group method for throwing GroupNotFoundException
	 */
	@Test(expected = GroupNotFoundException.class)
	public void testGetGroupForGroupNotFoundException() throws GroupNotFoundException{
		when(groupJPAService.getGroup(anyInt())).thenThrow(new GroupNotFoundException("Could not find group!"));
		groupService.setJPAService(groupJPAService);
		groupService.get(groupOne.getId());
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
		when(groupJPAService.getGroup(anyInt())).thenReturn(groupOne);
		when(groupJPAService.removeUserFromGroup(any(),anyString())).thenReturn(1);
		groupService.setJPAService(groupJPAService);
		Group newGroup = groupService.removeUserFromGroup(groupOne.getGroupCode(),userOne.getUsername());
		verify(groupJPAService).getGroup(anyInt());
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
	 * testing join Group method
	 */
	@Test
	public void testJoinGroup() throws GroupNotFoundException, UserNotFoundException{
		mock(GroupService.class);
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
	public void testSearchUsingName() throws GroupNotFoundException{
		when(groupJPAService.searchUsingName(any())).thenReturn(groupList);
		groupService.setJPAService(groupJPAService);
		List<Group> newGroup = groupService.searchUsingName(groupOne.getName());
		verify(groupJPAService).searchUsingName(any());
		assertEquals(groupList,newGroup);

	}
}
