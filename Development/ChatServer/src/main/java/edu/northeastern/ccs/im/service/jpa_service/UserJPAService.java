package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.customexceptions.FirstTimeUserLoggedInException;
import edu.northeastern.ccs.im.customexceptions.ListOfUsersNotFound;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.service.EntityManagerUtil;
import edu.northeastern.ccs.im.user_group.UserChatRoomLogOffEvent;
import edu.northeastern.ccs.im.user_group.User;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * A JPA service that'll handle interacting with the database for all User interactions.
 */
public class UserJPAService {

    private static final Logger LOGGER = Logger.getLogger(UserJPAService.class.getName());
    private static final UserJPAService userJpaServiceInstance = new UserJPAService();
    private EntityManagerUtil entityManagerUtil;

    /**
     * Constructor for UserJPAService to setup the EntityManagerUtil
     */
    private UserJPAService() {
        entityManagerUtil = new EntityManagerUtil();
    }

    /**
     * Singleton for user jpa service
     * @return a UserJPAService instance.
     */
    public static UserJPAService getInstance(){
        return userJpaServiceInstance;
    }

    /**
     * A function made to setup the entity manager util for this class to make the class more testable.
     * @param entityManagerUtil The entity manager for this class.
     */
    public void setEntityManagerUtil(EntityManagerUtil entityManagerUtil) {
        this.entityManagerUtil = entityManagerUtil;
    }


    /**
     * Updating the User in the database with the given user credentials.
     * @param user being updated in the database.
     */
    public void updateUser(User user) throws UserNotFoundException {
        try {
            EntityManager em = entityManagerUtil.getEntityManager();
            beginTransaction(em);
            User thisUser = em.find(User.class, user.getId());
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
            em.merge(thisUser);
            endTransaction(em);
        } catch (Exception e) {
            ChatLogger.error(e.getMessage());
            throw new UserNotFoundException("Profile Not Updated!");
        }
    }

    /**
     * A method made to search for a user in the database.
     * @param username name of the user we are searching for.
     * @return User instance we are looking for.
     */
    public User search(String username) throws UserNotFoundException {
        try {
            String thisString = "SELECT u " + "FROM User u WHERE u.username ='" + username + "'";
            EntityManager em = entityManagerUtil.getEntityManager();
            beginTransaction(em);
            TypedQuery<User> query = em.createQuery(thisString, User.class);
            User result = query.getSingleResult();
            em.getTransaction().commit();
            em.close();
            return result;
        }
        catch (Exception e) {
            ChatLogger.error(e.getMessage());
            throw new UserNotFoundException("User with username: "+username+ " not found! ");
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
        EntityManager em = entityManagerUtil.getEntityManager();
        beginTransaction(em);
        try {
            TypedQuery<User> query = em.createQuery(queryString, User.class);
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
        EntityManager em = entityManagerUtil.getEntityManager();
        beginTransaction(em);
        return getUsers(queryString, em);
    }

    /**
     * Gets the users who have a profile access
     * @param queryString the query we have made for the getUser.
     * @return
     */
    private List<User> getUsers(String queryString, EntityManager em) throws ListOfUsersNotFound {
        try {
            TypedQuery<User> query = em.createQuery(queryString, User.class);
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
     * Gets the followees for this user.
     * @param user looking to get their list of followees.
     * @return The list of followee's of the given user
     */
    public List<User> getFollowees(User user) throws UserNotFoundException, ListOfUsersNotFound {
        if(user == search(user.getUsername())) {
            String queryString =
                    "SELECT u FROM user_follower u WHERE u.followerID ='" + user.getId() + "'";
            EntityManager em = entityManagerUtil.getEntityManager();
            beginTransaction(em);
            return getUsers(queryString, em);
        }

        return Collections.emptyList();
    }

    /**
     * A method to begin the transaction.
     */
    private void beginTransaction(EntityManager entityManager) {
        entityManager.getTransaction().begin();
    }

    /**
     * A private method that'll end the transaction.
     */
    private void endTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /***
     * Creates or updates a join group event whenever a user logs in / logs off
     * from the chatroom.
     * @param userChatRoomLogOffEvent a log off event.
     */
    public void saveOrUpdateJoinGroupEvent(UserChatRoomLogOffEvent userChatRoomLogOffEvent)  {
        EntityManager em = entityManagerUtil.getEntityManager();
        beginTransaction(em);
        em.merge(userChatRoomLogOffEvent);
        endTransaction(em);
    }

    /**
     * Gets a log off event for when a user logs out of a chat.
     * @param userId of the user logging out
     * @param groupId of the group they are logging out from
     * @return UserChatRoomLogOffEvent which represents the event of a logoff.
     * @throws FirstTimeUserLoggedInException exception when a user logs in for first time.
     */
    public UserChatRoomLogOffEvent getLogOffEvent(int userId, int groupId) throws FirstTimeUserLoggedInException {
        EntityManager em = entityManagerUtil.getEntityManager();
        beginTransaction(em);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserChatRoomLogOffEvent> cq = cb.createQuery(UserChatRoomLogOffEvent.class);
        Root<UserChatRoomLogOffEvent> root = cq.from(UserChatRoomLogOffEvent.class);
        Predicate predicate1 = cb.equal(root.get("compositeObject").get("userId"), userId);
        Predicate predicate2 = cb.equal(root.get("compositeObject").get("groupId"), groupId);
        Predicate predicate = cb.and(predicate1, predicate2);
        cq.where(predicate);
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