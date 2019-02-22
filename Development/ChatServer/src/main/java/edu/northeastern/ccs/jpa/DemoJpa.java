package edu.northeastern.ccs.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.northeastern.ccs.jpa.services.*;
public class DemoJpa {

	public static void main(String[] args) {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );
		EntityManager entitymanager = emfactory.createEntityManager();
		ProfileServices ps = new ProfileServices();
		ps.createProfile(entitymanager,1, "JaneDoe", "test", "jd@jd.com", "imageUrljd");
	}

}
