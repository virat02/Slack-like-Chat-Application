package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.User;

public class JPAMain {

	public static void main(String[] args) {
		
		UserJPAService userJPA = new UserJPAService();
		User user = new User();
		user.setUsername("John");
		user.setPassword("password");
		int id= userJPA.createUser(user);
		user.setId(id);
		
		GroupJPAService groupJPA = new GroupJPAService();
		Group group = new Group();
		group.setName("testgroup");
		groupJPA.createGroup(group);
		group.addUser(user);
		group.setName("testing");
		groupJPA.updateGroup(group);
		user.addGroup(group);
		userJPA.updateUser(user);

	}

}
