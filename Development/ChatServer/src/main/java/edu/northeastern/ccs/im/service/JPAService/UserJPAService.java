package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.User;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * A JPA service that'll handle interacting with the database for all User interactions.
 */
public class UserJPAService {
    private static final Logger LOGGER = Logger.getLogger(UserJPAService.class.getName());
    EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");

    //The entity manager for this class.
    private EntityManager entityManager;

    /**
     * A function made to setup the entity manager for this class to make the class more testable.
     *
     * @param entityManager The entity manager for this class.
     */
    public void setEntityManager(EntityManager entityManager) {
        if (entityManager == null) {
            EntityManagerFactory emFactory;
            emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
            this.entityManager = emFactory.createEntityManager();
        } else {
            this.entityManager = entityManager;
        }
    }

    /**
     * A method to begin the transaction.
     */
    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    /**
     * A private method that'll end the transaction.
     */
    private void endTransaction() {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * Creating a new User instance we are creating in the database.
     *
     * @param user being created in the database.
     * @return the id of the user in the database.
     */
    public int createUser(User user) {
        beginTransaction();
        entityManager.persist(user);
        entityManager.flush();
        int id = user.getId();
        endTransaction();
        return id;
    }

    /**
     * A method made to delete a user in the database.
     *
     * @param user being deleted in the database.
     */
    public void deleteUser(User user) {
        beginTransaction();
        entityManager.remove(user);
        endTransaction();
    }

    /**
     * Updating the User in the database with the given user credentials.
     *
     * @param user being updated in the database.
     */
    public void updateUser(User user) {
        beginTransaction();
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
        thisUser.setProfileAccess(user.getProfileAccess());
        thisUser.setGroups(user.getGroups());
        entityManager.merge(thisUser);
        endTransaction();
    }

    /**
     * A method made to search for a user in the database.
     *
     * @param username name of the user we are searching for.
     * @return User instance we are looking for.
     */
    public User search(String username) {
        String thisString = "SELECT u " + "FROM User u WHERE u.username ='" + username + "'";
        beginTransaction();
        TypedQuery<User> query = entityManager.createQuery(thisString, User.class);
        return (User) query.getSingleResult();
    }

    /**
     * A method made to get the user by it's Id in the database.
     *
     * @param id of the user we are looking to get.
     * @return a User instance when we are trying to get a User.
     */
    public User getUser(int id) {
        String queryString = "SELECT u " + "FROM User u WHERE u.id =" + id;
        beginTransaction();
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        return (User) query.getSingleResult();
    }

    /**
     * A Method made to login a particular user given credentials of the user instance.
     *
     * @param user trying to login to the database.
     * @return User instance after login.
     */
    public User loginUser(User user) {
        String queryString =
                "SELECT u FROM User u WHERE u.username ='" + user.getUsername() + "'";
        beginTransaction();
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            User newUser = query.getSingleResult();
            if (!user.getUsername().equals(newUser.getUsername()) || !user.getPassword().equals(newUser.getPassword())) {
                return null;
            } else {
                return newUser;
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    /**
     * Gets the followers from a particular user.
     *
     * @param user we are getting the followers of.
     * @return The list of follower's of the given user
     */
    public List<User> getFollowers(User user) {
        String queryString = "SELECT u FROM user_follower u WHERE u.userID ='" + user.getId() + "'";
        beginTransaction();
        return getUsers(queryString);
    }

    /**
     * Gets the users who have a profile access
     *
     * @param queryString the query we have made for the getUser.
     * @return
     */
    private List<User> getUsers(String queryString) {
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            List<User> followerList = query.getResultList();

            //Filter out users with no profile access
            for (User u : followerList) {
                if (!u.getProfileAccess()) {
                    followerList.remove(u);
                }
            }

            return followerList;
        } catch (Exception e) {
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
                "SELECT u FROM user_follower u WHERE u.followerID ='" + user.getId() + "'";
        beginTransaction();
        return getUsers(queryString);
    }
}