package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.User;

import java.util.Arrays;
import java.util.List;

public class JPAMain {

	public static void main(String[] args) {
		
		GroupJPAService groupJPA = new GroupJPAService();
		Group group = new Group();
//		group.setName("groupOne");
//		group.setGroupCode("g123");
//		groupJPA.deleteGroup(group);
	
		UserJPAService userJPA = new UserJPAService();
//
//		User user1 = new User();
//		User user2 = new User();
//		User user3 = new User();
//
//		List<User> user1FollowingList = Arrays.asList(user2, user3);
//		user1.setFollowing(user1FollowingList);
//
//		List<User> user1FolloweeList = Arrays.asList(user2, user3);
//		user1.setFollowing(user1FolloweeList);
//
//		int id1 = userJPA.createUser(user1);
//		int id2 = userJPA.createUser(user2);
//		int id3 = userJPA.createUser(user3);
//
//		user1.setId(id1);
//		user2.setId(id2);
//		user3.setId(id3);
		
		User user = new User();
		user.setUsername("JohnHello1");
		user.setPassword("password");
//		int id= userJPA.createUser(user);
		user.setId(351);
		
		group = new Group();
		group.setName("testgroupHello1");
//		int id4 = groupJPA.createGroup(group);
		group.setId(352);
//		group.addUser(user);
//		groupJPA.updateGroup(group);
//		user.addGroup(group);
//		userJPA.updateUser(user);
		
		groupJPA.removeUserFromGroup(group, user.getId());
	}

}
