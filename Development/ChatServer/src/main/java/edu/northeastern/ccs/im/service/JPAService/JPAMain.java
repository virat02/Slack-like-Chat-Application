package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.User;

import java.util.Arrays;
import java.util.List;

public class JPAMain {

	public static void main(String[] args) {

		UserJPAService userJPA = new UserJPAService();

		User user1 = new User();
		User user2 = new User();
		User user3 = new User();

		List<User> user1FollowingList = Arrays.asList(user2, user3);
		user1.setFollowing(user1FollowingList);

		List<User> user1FolloweeList = Arrays.asList(user2, user3);
		user1.setFollowing(user1FolloweeList);

		int id1 = userJPA.createUser(user1);
		int id2 = userJPA.createUser(user2);
		int id3 = userJPA.createUser(user3);

		user1.setId(id1);
		user2.setId(id2);
		user3.setId(id3);
		
//		UserJPAService userJPA = new UserJPAService();
//		User user = new User();
//		user.setUsername("John");
//		user.setPassword("password");
//		int id= userJPA.createUser(user);
//		user.setId(id);
		
//		GroupJPAService groupJPA = new GroupJPAService();
//		Group group = new Group();
//		group.setName("testgroup");
//		groupJPA.createGroup(group);
//		group.addUser(user);
//		group.setName("testing");
//		groupJPA.updateGroup(group);
//		user.addGroup(group);
//		userJPA.updateUser(user);

	}

}
