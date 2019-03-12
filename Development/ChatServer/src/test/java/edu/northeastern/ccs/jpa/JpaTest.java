package edu.northeastern.ccs.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;


/**
 * The Class JpaTest.
 */
public class JpaTest {
	
	/** The Constant TEST_GROUP. */
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
//		group3.setUsers(userList);
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
	
	/**
	 * Test 6.
	 */
	@Test
	public void test6() {
		Message msg1 = new Message();
		msg1.setId(66);
		assertEquals(66,msg1.getId());
	}
	
	/**
	 * Test 7.
	 */
	@Test
	public void test7() {
		String str = TEST_GROUP + " 7";
		Message msg2 = new Message();
		msg2.setId(77);
		msg2.setMessage(str);
		assertEquals(str,msg2.getMessage());
	}
	
	/**
	 * Test 8.
	 */
	@Test
	public void test8() {
		String str = TEST_GROUP + " 8";
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		msg2.setDeleted(true);
		assertEquals(true,msg2.isDeleted());
	}
	
	/**
	 * Test 9.
	 */
	@Test
	public void test9() {
		String str = TEST_GROUP + " 8";
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		msg2.setDeleted(true);
		User user = new User();
		msg2.setSender(user);
		assertEquals(user,msg2.getSender());
	}
	
	/**
	 * Test 10.
	 */
	@Test
	public void test10() {
		String str = TEST_GROUP + " 8";
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		msg2.setDeleted(true);
		Group group = new Group();
		msg2.setGroup(group);
		assertEquals(group,msg2.getGroup());
	}
	
	/**
	 * Test 11.
	 */
	@Test
	public void test11() {
		String str = TEST_GROUP + " 8";
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		msg2.setDeleted(true);
		Group group = new Group();
		msg2.setGroup(group);
		Date timestamp = new Date();
		msg2.setTimestamp(timestamp);
		assertEquals(timestamp,msg2.getTimestamp());
	}
	
	/**
	 * Test 12.
	 */
	@Test
	public void test12() {
		User usr = new User();
		usr.setId(123);
		assertEquals(123,usr.getId());
	}
	
	/**
	 * Test 13.
	 */
	@Test
	public void test13() {
		String str = TEST_GROUP + " 13";
		User usr = new User();
		usr.setId(123);
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		Message msg1 = new Message();
		msg2.setId(98);
		msg2.setMessage(str);
		Message msg3 = new Message();
		msg3.setId(28);
		msg3.setMessage(str);
		List<Message> msgList = new ArrayList<>();
		msgList.add(msg1);
		msgList.add(msg2);
		usr.setMessages(msgList);
		usr.addMessages(msg3);
		msgList.add(msg3);
		assertEquals(msgList,usr.getMessages());
	}
	
	/**
	 * Test 14.
	 */
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
//		usr.setGroups(groupList);
		Group group3 = new Group();
		groupList.add(group3);
//		usr.addGroup(group3);
		assertEquals(groupList,usr.getGroups());
	}
	
	/**
	 * Test 15.
	 */
	@Test
	public void test15() {
		User usr = new User();
		usr.setId(123);
		Profile p1 = new Profile();

		usr.setProfile(p1);
		assertEquals(p1,usr.getProfile());
	}
	
	/**
	 * Test 16.
	 */
	@Test
	public void test16() {
		Profile prof = new Profile(3, "hello Profile", "p@gmail.com","profile","http://profile.com/profile.jpg");
		assertEquals(3,prof.getId());
		assertEquals("hello Profile",prof.getName());
		assertEquals("p@gmail.com",prof.getEmail());
		assertEquals("http://profile.com/profile.jpg",prof.getImageUrl());
		assertEquals("profile",prof.getPassword());
	}
	
	/**
	 * Test 17.
	 */
	@Test
	public void test17() {
		Profile prof = new Profile();
		prof.setId(4);
		prof.setEmail("p2@gmail.com");
		prof.setName("Profile");
		prof.setPassword("profilepass");
		prof.setImageUrl("http://profile.com/prof.jpg");
		assertEquals(4,prof.getId());
		assertEquals("Profile",prof.getName());
		assertEquals("p2@gmail.com",prof.getEmail());
		assertEquals("http://profile.com/prof.jpg",prof.getImageUrl());
		assertEquals("profilepass",prof.getPassword());
	}
	
	/**
	 * Test 18.
	 */
	@Test
	public void test18() {
		Profile prof = new Profile();
		prof.setId(4);
		User usr = new User();
		prof.setUser(usr);
		assertEquals(usr,prof.getUser());
	}
	
	
	
	

	
	
}
