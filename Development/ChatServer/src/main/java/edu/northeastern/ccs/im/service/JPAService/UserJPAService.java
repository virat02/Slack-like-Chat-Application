package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.*;

public class UserJPAService {

    private EntityManager beginTransaction() {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    private void endTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void createUser(IUser user) {
        EntityManager entityManager = beginTransaction();
        entityManager.persist(user);
        endTransaction(entityManager);
    }

    public void deleteUser(IUser user) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(user);
        endTransaction(entityManager);
    }

    public void updateUser(IUser user) {
        EntityManager entityManager = beginTransaction();
        User thisUser = entityManager.find(User.class, user.getId());
        if (thisUser == null) {
            throw new EntityNotFoundException("Can't find User for ID "
                    + user.getId());
        }

        thisUser.setFollowing(user.getFollowing());
        thisUser.setProfile(user.getProfile());
        entityManager.merge(thisUser);
        endTransaction(entityManager);
    }

    public IUser search(String username) {
        String thisString = "SELECT u" + "FROM User u,Profile p WHERE u.id = p.userId AND u.username =" + username;
        EntityManager entityManager = beginTransaction();
        Query query = entityManager.createQuery(thisString);
        return (IUser) query.getSingleResult();
    }
}