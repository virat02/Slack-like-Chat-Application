package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.User;

import javax.persistence.*;

public class UserJPAService {
    EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");

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
        int id = user.getId();
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
        String thisString = "SELECT u FROM User u where u.username ='" + username + "'";
        EntityManager entityManager = beginTransaction();
        //Query query = entityManager.createQuery(thisString);
        TypedQuery<User> query = entityManager.createQuery(thisString, User.class);
        //return (User) query.getSingleResult();
        return query.getSingleResult();
    }

    public User getUser(int id) {
        String queryString = "SELECT u " + "FROM User u WHERE u.id =" + id;
        EntityManager entityManager = beginTransaction();
        //Query query = entityManager.createQuery(queryString);
        TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
        //return (User) query.getSingleResult();
        return query.getSingleResult();
    }

    public User loginUser(User user) {
        String queryString =
                "SELECT u FROM User u WHERE u.username ='" + user.getUsername() + "'";
        EntityManager entityManager = beginTransaction();
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            User newUser = query.getSingleResult();
            if (!user.getUsername().equals(newUser.getUsername()) || !user.getPassword().equals(newUser.getPassword())) {
                return null;
            } else {
                return newUser;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}