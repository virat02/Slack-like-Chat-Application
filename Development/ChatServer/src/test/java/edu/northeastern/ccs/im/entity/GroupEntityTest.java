package edu.northeastern.ccs.im.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.User;

public class GroupEntityTest {
	
	public static final String TEST_GROUP = "Test Group";

	/**
	 * Test 1.
	 */
	@Test
	public void test1() {
		Group group1 = new Group();
		group1.setName(TEST_GROUP + " 1");
		assertEquals(TEST_GROUP + " 1",group1.getName());
	}

	/**
	 * Test 2.
	 */
	@Test
	public void test2() {
		Group group2 = new Group();
		group2.setId(3);
		assertEquals(3,group2.getId());
	}

	/**
	 * Test 3.
	 */
	@Test
	public void test3() {
		String str = TEST_GROUP + " 2";
		Group group2 = new Group(3 , str);
		User user1 = new User();
		User user2 = new User();
		user1.setUsername("virat");
		user2.setUsername("sangeetha");
		group2.addUser(user1);
		group2.addUser(user2);
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		assertEquals(group2.getUsers(),userList);
	}

	/**
	 * Test 4.
	 */
	@Test
	public void test4() {
		String str = TEST_GROUP + " 3";
		Group group3 = new Group(3 , str);
		User user1 = new User();
		User user2 = new User();
		List<User> userList = new ArrayList<>();
		userList.add(user1);
		userList.add(user2);
		group3.setUsers(userList);
		assertEquals(group3.getUsers(),userList);
	}

	/**
	 * Test 5.
	 */
	@Test
	public void test5() {
		String str = TEST_GROUP + " 4";
		Group group3 = new Group(3 , str);
		Message msg1 = new Message();
		Message msg2 = new Message();
		List<Message> msgList = new ArrayList<>();
		msgList.add(msg1);
		msgList.add(msg2);
		group3.setMsgs(msgList);
		assertEquals(group3.getMsgs(),msgList);
	}
	
	@Test
	public void test10() {
		String str = TEST_GROUP + " 8";
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		msg2.setDeleted(true);
		Group group = new Group();
//		msg2.setGroup(group);
//		assertEquals(group,msg2.getGroup());
	}
	
	@Test
	public void test14() {
		String str = TEST_GROUP + " 14";
		User usr = new User();
		usr.setId(123);
		Group group2 = new Group(377 , str);
		Group group1 = new Group();
		List<Group> groupList = new ArrayList<>();
		groupList.add(group2);
		groupList.add(group1);
		usr.setGroups(groupList);
		Group group3 = new Group();
		groupList.add(group3);
		usr.addGroup(group3);
		assertEquals(groupList,usr.getGroups());
	}



}
