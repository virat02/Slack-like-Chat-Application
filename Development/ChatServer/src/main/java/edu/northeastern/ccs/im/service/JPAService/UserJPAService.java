package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.User;

import javax.persistence.*;

/**
 * A JPA service that'll handle interacting with the database for all User interactions.
 */
public class UserJPAService {
    //The entity manager for this class.
    private EntityManager entityManager;

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
     * @param user being created in the database.
     * @return the id of the user in the database.
     */
    public int createUser(User user) {
        beginTransaction();
        entityManager.persist(user);
        entityManager.flush();
        int id= user.getId();
        endTransaction();
        return id;
    }

    /**
     * A method made to delete a user in the database.
     * @param user being deleted in the database.
     */
    public void deleteUser(User user) {
        beginTransaction();
        entityManager.remove(user);
        endTransaction();
    }

    /**
     * Updating the User in the database with the given user credentials.
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
        thisUser.setGroups(user.getGroups());
        entityManager.merge(thisUser);
        endTransaction();
    }

    /**
     * A method made to search for a user in the database.
     * @param username name of the user we are searching for.
     * @return User instance we are looking for.
     */
    public User search(String username) {
        String thisString = "SELECT u" + "FROM User u,Profile p WHERE u.id = p.userId AND u.username =" + username;
        beginTransaction();
        Query query = entityManager.createQuery(thisString);
        return (User)query.getSingleResult();
    }

    /**
     * A method made to get the user by it's Id in the database.
     * @param id of the user we are looking to get.
     * @return a User instance when we are trying to get a User.
     */
    public User getUser(int id) {
        String queryString = "SELECT u " + "FROM User u WHERE u.id =" + id;
        beginTransaction();
        Query query = entityManager.createQuery(queryString);
        return (User) query.getSingleResult();
    }

    /**
     * A Method made to login a particular user given credentials of the user instance.
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
}