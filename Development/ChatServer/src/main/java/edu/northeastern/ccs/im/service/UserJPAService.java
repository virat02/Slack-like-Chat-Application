package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.userGroup.IUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UserJPAService {

    public void createUser(IUser user) {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void deleteUser(IUser user) {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(user);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void updateUser(IUser user, int id) {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.find(IUser.class, id);

    }


}
