package edu.northeastern.ccs.jpa;

import edu.northeastern.ccs.jpa.services.*;
public class DemoJpa {

	public static void main(String[] args) {
		ProfileServices ps = new ProfileServices();
		ps.createProfile(1, "JaneDoe", "test", "jd@jd.com", "imageUrljd");
	}

}
