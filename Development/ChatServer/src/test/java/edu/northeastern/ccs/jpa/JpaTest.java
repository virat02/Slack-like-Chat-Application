package edu.northeastern.ccs.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;


public class JpaTest {
	public static final String TEST_GROUP = "Test Group";

	@Test
	public void test1() {
		Group group1 = new Group();
		group1.setName(TEST_GROUP + " 1");
		assertEquals(group1.getName(), TEST_GROUP + " 1");
	}
	
	@Test
	public void test2() {
		Group group2 = new Group();
		group2.setId(3);
		assertEquals(group2.getId(), 3);
	}
	
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
		assertEquals(userList, group2.getUsers());
	}
	
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
		assertEquals(userList, group3.getUsers());
	}
	
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
		assertEquals(msgList, group3.getMsgs());
	}
	
	@Test
	public void test6() {
		Message msg1 = new Message();
		msg1.setId(66);
		assertEquals(msg1.getId(), 66);
	}
	
	@Test
	public void test7() {
		String str = TEST_GROUP + " 7";
		Message msg2 = new Message();
		msg2.setId(77);
		msg2.setMessage(str);
		assertEquals(msg2.getMessage(), str);
	}
	
	@Test
	public void test8() {
		String str = TEST_GROUP + " 8";
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		msg2.setDeleted(true);
		assertEquals(msg2.isDeleted(), true);
	}
	
	@Test
	public void test9() {
		String str = TEST_GROUP + " 8";
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		msg2.setDeleted(true);
		User user = new User();
		msg2.setSender(user);
		assertEquals(msg2.getSender(), user);
	}
	
	@Test
	public void test10() {
		String str = TEST_GROUP + " 8";
		Message msg2 = new Message();
		msg2.setId(88);
		msg2.setMessage(str);
		msg2.setDeleted(true);
		Group group = new Group();
		msg2.setGroup(group);
		assertEquals(msg2.getGroup(), group);
	}
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
		assertEquals(msg2.getTimestamp(), timestamp);
	}
	
	@Test
	public void test12() {
		User usr = new User();
		usr.setId(123);
		assertEquals(usr.getId(), 123);
	}
	
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
		assertEquals(usr.getMessages(), msgList);
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
		assertEquals(usr.getGroups(), groupList);
	}
	
	@Test
	public void test15() {
		User usr = new User();
		usr.setId(123);
		Profile p1 = new Profile();

		usr.setProfile(p1);
		assertEquals(usr.getProfile(), p1);
	}
	
	@Test
	public void test16() {
		Profile prof = new Profile(3, "hello Profile", "p@gmail.com","profile","http://profile.com/profile.jpg");
		assertEquals(prof.getId(), 3);
		assertEquals(prof.getName(), "hello Profile");
		assertEquals(prof.getEmail(), "p@gmail.com");
		assertEquals(prof.getImageUrl(),"http://profile.com/profile.jpg");
		assertEquals(prof.getPassword(), "profile");
	}
	
	@Test
	public void test17() {
		Profile prof = new Profile();
		prof.setId(4);
		prof.setEmail("p@gmail.com");
		prof.setName("hello Profile");
		prof.setPassword("profile");
		prof.setImageUrl("http://profile.com/profile.jpg");
		assertEquals(prof.getId(), 4);
		assertEquals(prof.getName(), "hello Profile");
		assertEquals(prof.getEmail(), "p@gmail.com");
		assertEquals(prof.getImageUrl(),"http://profile.com/profile.jpg");
		assertEquals(prof.getPassword(), "profile");
	}
	
}
