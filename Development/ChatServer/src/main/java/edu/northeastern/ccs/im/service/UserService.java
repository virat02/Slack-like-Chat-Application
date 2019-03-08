package edu.northeastern.ccs.im.service;

import edu.northeastern.ccs.im.controller.UserController;
import edu.northeastern.ccs.im.userGroup.IGroup;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;
import edu.northeastern.ccs.jpa.Group;
import edu.northeastern.ccs.jpa.Message;
import edu.northeastern.ccs.jpa.User;
import org.hibernate.sql.Select;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class UserService {
    private UserJPAService userJPAService;
    private UserService() {
        userJPAService = new UserJPAService();
    }


    public void addUser(Object user) {
        userJPAService.createUser((IUser)user);
    }

    /**
     * Searches for a particular user.
     * @param username the name of the user being searched
     * @return the users with the name searched for
     */
    public IUser search(String username) {
        userJPAService.search(username);
        return null;
    }

    /**
     * Follow a particular user given their username.
     * @param username of the user we want to follow.
     */
    public void follow(String username, IUser currentUser) {
        currentUser.addFollowee(search(username));
    }

    public void update(Object user) {
        userJPAService.updateUser((IUser) user);
    }

    public void delete(Object user) {

    }

//    /**
//     * Creates a new Group instance and adds the Group to the list of groups.
//     * @param iGroupId the id of the group we are adding
//     */
//    public void createIGroup(int iGroupId) {
//        Group newGroup = new Group();
//        newGroup.setId(iGroupId);
//        this.groups.add(newGroup);
//    }

    private class UserJPAService {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
        EntityManager entityManager = emFactory.createEntityManager();

        public void createUser(IUser user) {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            entityManager.close();
        }

        public void deleteUser(IUser user) {
            entityManager.getTransaction().begin();
            entityManager.remove(user);
            entityManager.getTransaction().commit();
            entityManager.close();
        }

        public void updateUser(IUser user) {
            entityManager.getTransaction().begin();
            User thisUser = entityManager.find(User.class, user.getId());
            if (thisUser == null) {
                throw new EntityNotFoundException("Can't find Artist for ID "
                        + user.getId());
            }

            thisUser.setFollowing(user.getFollowing());
            thisUser.setProfile(user.getProfile());

            entityManager.getTransaction().begin();
            entityManager.merge(thisUser);
            entityManager.getTransaction().commit();
        }

        public IUser search(String username) {
            String thisString = "SELECT u FROM User u," +
                    " Profile p WHERE u.id = p.userId AND u.username = ";
            thisString = thisString.concat(username);
            Query query = entityManager.createQuery(thisString);
            return (IUser) query.getSingleResult();
        }
    }
}
