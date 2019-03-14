package edu.northeastern.ccs.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.northeastern.ccs.im.service.JPAService.UserJPAService;
import edu.northeastern.ccs.im.userGroup.User;


/**
 * The Class JpaTest.
 */
public class JpaTest {

	//
	public void test1() {
		UserJPAService userJPA = new UserJPAService();
		User user = new User();
		user.setUsername("John");
		user.setPassword("password");
		userJPA.createUser(user);
//		assertEquals();
	}
	
	public void test2() {
		
	}

}
