package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.User;

import javax.persistence.*;

/**
 * A JPA service that'll handle interacting with the database for all User interactions.
 */
public class UserJPAService {

    //A private factory for making entity managers.
	private EntityManagerFactory emFactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );

    /**
     * A method to begin the transaction.
     * @return Entity Manager which will manage our entity.
     */
	private EntityManager beginTransaction() {
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    /**
     * A private method that'll end the transaction.
     * @param entityManager the manager we are ending the transaction for.
     */
    private void endTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * Creating a new User instance we are creating in the database.
     * @param user being created in the database.
     * @return the id of the user in the database.
     */
    public int createUser(User user) {
        String queryString = "SELECT * FROM user u WHERE u.username =" + user.getUsername() +"";
        EntityManager entityManager = beginTransaction();
        try {
            Query query = entityManager.createQuery(queryString);
            query.getSingleResult();
        } catch (javax.persistence.NoResultException nr) {
            entityManager.persist(user);
            entityManager.flush();
            int id= user.getId();
            endTransaction(entityManager);
            return id;
        }
        return 0;
    }

    /**
     * A method made to delete a user in the database.
     * @param user being deleted in the database.
     */
    public void deleteUser(User user) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(user);
        endTransaction(entityManager);
    }

    /**
     * Updating the User in the database with the given user credentials.
     * @param user being updated in the database.
     */
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

    /**
     * A method made to search for a user in the database.
     * @param username name of the user we are searching for.
     * @return User instance we are looking for.
     */
    public User search(String username) {
        String thisString = "SELECT u" + "FROM User u,Profile p WHERE u.id = p.userId AND u.username =" + username;
        EntityManager entityManager = beginTransaction();
        Query query = entityManager.createQuery(thisString);
        return (User)query.getSingleResult();
    }

    /**
     * A method made to get the user by it's Id in the database.
     * @param id of the user we are looking to get.
     * @return a User instance when we are trying to get a User.
     */
    public User getUser(int id) {
        StringBuilder stringBuilder = new StringBuilder("SELECT u FROM User u WHERE u.id =");
        stringBuilder.append(id);
        String queryString = stringBuilder.toString();
        EntityManager entityManager = beginTransaction();
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
        EntityManager entityManager = beginTransaction();
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            User newUser = query.getSingleResult();
            if(!user.getUsername().equals(newUser.getUsername()) || !user.getPassword().equals(newUser.getPassword())) {
                return null;
            } else {
                return newUser;
            }
        } catch (Exception e) {
            return null;
        }
    }
}