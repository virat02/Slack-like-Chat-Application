package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class CircleJPAService {

    private static final Logger LOGGER = Logger.getLogger(CircleJPAService.class.getName());
    EntityManagerFactory emFactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );

    /**
     * Begin the transaction
     * @return An entity manager
     */
    private EntityManager beginTransaction() {
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    /**
     * Ends a transaction
     * @param entityManager
     */
    private void endTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * @param user
     * @return  A list of followers of the given user's followers
     */
    public List<List<User>> getFollowersOfFollowers(User user) {
        String queryString =
                "SELECT u.followingID FROM user_user u WHERE u.userID ='" + user.getId() + "'";

        EntityManager entityManager = beginTransaction();
        try {
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            List<User> followingList =  query.getResultList();

            List<List<User>> resultList = new ArrayList<>();

            for(User u: followingList) {
                String queryString2 = "SELECT u.followingID FROM user_user u WHERE u.userID ='"+u.getId()+"'";
                try {
                    TypedQuery<User> query1 = entityManager.createQuery(queryString2, User.class);
                    resultList.add(query1.getResultList());
                }
                catch (Exception e) {
                    LOGGER.info(e.getMessage());
                    return Collections.emptyList();
                }
            }

            return resultList;
        }
        catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

        return Collections.emptyList();
    }
}
