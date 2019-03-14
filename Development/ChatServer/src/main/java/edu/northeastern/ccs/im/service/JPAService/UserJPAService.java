package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.User;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class UserJPAService {

    private static final Logger LOGGER = Logger.getLogger(UserJPAService.class.getName());
	EntityManagerFactory emFactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );

	private EntityManager beginTransaction() {
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    private void endTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public int createUser(User user) {
        EntityManager entityManager = beginTransaction();
        entityManager.persist(user);
        entityManager.flush();
        int id= user.getId();
        endTransaction(entityManager);
        return id;
    }

    public void deleteUser(User user) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(user);
        endTransaction(entityManager);
    }

    public void updateUser(User user) {
        EntityManager entityManager = beginTransaction();
        User thisUser = entityManager.find(User.class, user.getId());
        if (thisUser == null) {
            throw new EntityNotFoundException("Can't find User for ID "
                    + user.getId());
        }
        thisUser.setPassword(user.getPassword());
        thisUser.setFollowing(user.getFollowing());
        thisUser.setProfile(user.getProfile());
        thisUser.setUsername(user.getUsername());
        thisUser.setMessages(user.getMessages());
        thisUser.setGroups(user.getGroups());
        entityManager.merge(thisUser);
        endTransaction(entityManager);
    }

    public User search(String username) {
        String thisString = "SELECT u " + "FROM User u,Profile p WHERE u.id = p.id AND u" +
                ".username = '" + username + "'";
        EntityManager entityManager = beginTransaction();
        TypedQuery<User> query = entityManager.createQuery(thisString, User.class);
        return query.getSingleResult();
    }

    public User getUser(int id) {
        String queryString = "SELECT u " + "FROM User u WHERE u.id =" + id;
        EntityManager entityManager = beginTransaction();
        Query query = entityManager.createQuery(queryString);
        return (User) query.getSingleResult();
    }

    public User loginUser(User user) {
        String queryString =
                "SELECT u FROM User u WHERE u.username ='" + user.getUsername() + "'";
        EntityManager entityManager = beginTransaction();
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            User newUser = query.getSingleResult();
            if(!user.getUsername().equals(newUser.getUsername()) || !user.getPassword().equals(newUser.getPassword())) {
                return null;
            } else {
                return newUser;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * @param user
     * @return The list of follower's of the given user
     */
    public List<User> getFollowers(User user) {
        String queryString =
                "SELECT u FROM user_follower u WHERE u.userID ='" + user.getId() + "'";
        EntityManager entityManager = beginTransaction();
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            return query.getResultList();
        }
        catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * @param user
     * @return The list of followee's of the given user
     */
    public List<User> getFollowees(User user) {
        String queryString =
                "SELECT u FROM user_followee u WHERE u.userID ='" + user.getId() + "'";
        EntityManager entityManager = beginTransaction();
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            return query.getResultList();
        }
        catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Collections.emptyList();
    }
}