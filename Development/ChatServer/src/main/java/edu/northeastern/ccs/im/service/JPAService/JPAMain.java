package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.User;

public class JPAMain {

	public static void main(String[] args) {
		
		GroupJPAService groupJPA = new GroupJPAService();
		Group group = new Group();
		group.setName("groupOne");
		group.setGroupCode("g123");
		groupJPA.deleteGroup(group);


	}

}
