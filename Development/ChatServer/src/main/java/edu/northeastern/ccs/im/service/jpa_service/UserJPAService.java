package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.customexceptions.FirstTimeUserLoggedInException;
import edu.northeastern.ccs.im.customexceptions.ListOfUsersNotFound;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotPersistedException;
import edu.northeastern.ccs.im.service.EntityManagerUtil;
import edu.northeastern.ccs.im.user_group.UserChatRoomLogOffEvent;
import edu.northeastern.ccs.im.user_group.User;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * A JPA service that'll handle interacting with the database for all User interactions.
 */
public class UserJPAService {

    private static final Logger LOGGER = Logger.getLogger(UserJPAService.class.getName());

    //The entity manager for this class.
    private EntityManager entityManager;


    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PrattlePersistance");

    public UserJPAService() {}

    /**
     * A function made to setup the entity manager for this class to make the class more testable.
     * @param entityManager The entity manager for this class.
     */
    public void setEntityManager(EntityManager entityManager) {
        if(entityManager == null) {
            EntityManagerFactory emFactory;
            emFactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );
            this.entityManager = emFactory.createEntityManager();
        } else {
            this.entityManager = entityManager;
        }
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
     * @param user being created in the database.
     * @return the id of the user in the database.
     */
    public int createUser(User user) throws UserNotPersistedException {
        try {
            beginTransaction();
            entityManager.persist(user);
            int id= user.getId();
            endTransaction();
            return id;
        }
        catch (Exception e) {
            LOGGER.info("JPA could not persist the user!");
            throw new UserNotPersistedException("JPA could not persist the user!");
        }

    }

    /**
     * A method made to delete a user in the database.
     * @param user being deleted in the database.
     */
    public void deleteUser(User user) throws UserNotFoundException {
        if(user == search(user.getUsername())) {
            beginTransaction();
            entityManager.remove(user);
            endTransaction();
        }
    }

    /**
     * Updating the User in the database with the given user credentials.
     * @param user being updated in the database.
     */
    public void updateUser(User user) throws UserNotFoundException {
        beginTransaction();
        User thisUser = entityManager.find(User.class, user.getId());
        if (thisUser == null) {
            throw new UserNotFoundException("Can't find User for ID " + user.getId());
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
     * @param username name of the user we are searching for.
     * @return User instance we are looking for.
     */
    public User search(String username) throws UserNotFoundException {
        try {
            String thisString = "SELECT u " + "FROM User u WHERE u.username ='" + username + "'";
            beginTransaction();
            TypedQuery<User> query = entityManager.createQuery(thisString, User.class);
            User result = query.getSingleResult();
            endTransaction();
            return result;
        }
        catch (Exception e) {
            ChatLogger.error(e.getMessage());
            throw new UserNotFoundException("User with username: "+username+ " not found! ");
        }

    }

    /**
     * A method made to get the user by it's Id in the database.
     * @param id of the user we are looking to get.
     * @return a User instance when we are trying to get a User.
     */
    public User getUser(int id) throws UserNotFoundException {
        try {
            StringBuilder queryString = new StringBuilder("SELECT u FROM User u WHERE u.id = ");
            queryString.append(id);
            beginTransaction();
            TypedQuery<User> query = entityManager.createQuery(queryString.toString(), User.class);
            return query.getSingleResult();
        }
        catch (Exception e) {
            LOGGER.info("Could not find User with user id: "+id);
            throw new UserNotFoundException("Could not find User with user id: "+id);
        }

    }

    /**
     * A Method made to login a particular user given credentials of the user instance.
     * @param user trying to login to the database.
     * @return User instance after login.
     */
    public User loginUser(User user) throws UserNotFoundException {
        String queryString =
                "SELECT u FROM User u WHERE u.username ='" + user.getUsername() + "'";
        beginTransaction();
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            User newUser = query.getSingleResult();
            if(!user.getUsername().equals(newUser.getUsername()) || !user.getPassword().equals(newUser.getPassword())) {
                throw new UserNotFoundException("Wrong password");
            } else {
                return newUser;
            }
        }
        catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new UserNotFoundException("Could not find the user who wants to login!");
        }
    }

    /**
     * Gets the followers from a particular user.
     * @param user we are getting the followers of.
     * @return The list of follower's of the given user
     */
    public List<User> getFollowers(User user) throws ListOfUsersNotFound {
        String queryString =
                "SELECT f FROM User u LEFT JOIN u.following f WHERE u.id = " + user.getId();
        beginTransaction();
        return getUsers(queryString);
    }

    /**
     * Gets the users who have a profile access
     * @param queryString the query we have made for the getUser.
     * @return
     */
    private List<User> getUsers(String queryString) throws ListOfUsersNotFound {
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            List<User> followerList = query.getResultList();
            if (followerList.isEmpty() || followerList.contains(null)) {
                throw new ListOfUsersNotFound("No users following!");
            }
            return followerList;
        }
        catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new ListOfUsersNotFound("Could not fetch list of users!");
        }
    }

    /**
     * @param user
     * @return The list of followee's of the given user
     */
    public List<User> getFollowees(User user) throws UserNotFoundException, ListOfUsersNotFound {
        if(user == search(user.getUsername())) {
            String queryString =
                    "SELECT u FROM user_follower u WHERE u.followerID ='" + user.getId() + "'";
            beginTransaction();
            return getUsers(queryString);
        }

        return Collections.emptyList();
    }

    /**
     * A method to begin the transaction.
     */
    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    /***
     * Creates or updates a join group event whenever a user logs in / logs off
     * from the chatroom.
     */
    public void saveOrUpdateJoinGroupEvent(UserChatRoomLogOffEvent userChatRoomLogOffEvent)  {
        EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.merge(userChatRoomLogOffEvent);
        em.getTransaction().commit();
        em.close();
    }

    public UserChatRoomLogOffEvent getLogOffEvent(int userId, int groupId) throws FirstTimeUserLoggedInException {
        EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserChatRoomLogOffEvent> cq = cb.createQuery(UserChatRoomLogOffEvent.class);
        Root<UserChatRoomLogOffEvent> root = cq.from(UserChatRoomLogOffEvent.class);
        cq.where(cb.and(cb.equal(root.get("compositeObject").get("userId"), userId), cb.equal(root.get("compositeObject").get("groupId"), groupId)));
        TypedQuery<UserChatRoomLogOffEvent> query = em.createQuery(cq);
        try {
            UserChatRoomLogOffEvent userChatRoomLogOffEvent = query.getSingleResult();
            em.close();
            return userChatRoomLogOffEvent;
        } catch (NoResultException e)  {
            throw new FirstTimeUserLoggedInException("User has logged in the first time, no log off event found for this group");
        }
    }
}