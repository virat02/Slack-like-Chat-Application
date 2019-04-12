package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.service.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class AllJPAService {

    private static final AllJPAService jpaServiceInstance = new AllJPAService();

    //The entityManagerUtil for this class.
    private EntityManagerUtil entityManagerUtil;


    private AllJPAService(){
        this.entityManagerUtil = new EntityManagerUtil();
    }

    public static AllJPAService getInstance(){
        return jpaServiceInstance;
    }

    /**
     * Set the entityManagerUtil
     * @param entityManagerUtil
     */
    public void setEntityManagerUtil(EntityManagerUtil entityManagerUtil) {
        this.entityManagerUtil = entityManagerUtil;
    }

    /**
     * Generic method to persist the given object in the DB
     * @param obj
     * @return
     */
    public boolean createEntity(Object obj) {

        //Begin Transaction
        EntityManager em = entityManagerUtil.getEntityManager();
        em.getTransaction().begin();

        em.persist(obj);
        em.flush();

        //End Transaction
        em.getTransaction().commit();
        em.close();
        return true;
    }

    /**
     * Generic method to delete an object from DB
     * @param obj
     * @return
     */
    public boolean deleteEntity(Object obj) {

        //Begin Transaction
        EntityManager em = entityManagerUtil.getEntityManager();
        em.getTransaction().begin();

        //Re-attach the entity if not attached
        if(!em.contains(obj)) {
            obj = em.merge(obj);
        }
        em.remove(obj);

        //End Transaction
        em.getTransaction().commit();
        em.close();

        //If reached here, all went well
        return true;
    }

    /**
     * Generic method to get a particular entity from the DB
     * @param object String to identify which entity we need to get
     * @param id
     * @return
     */
    public Object getEntity(String object, int id) {
        StringBuilder queryString = new StringBuilder("SELECT o FROM ");
        queryString.append(object);
        queryString.append(" o WHERE o.id = ");
        queryString.append(id);

        //Begin Transaction
        EntityManager em = entityManagerUtil.getEntityManager();
        em.getTransaction().begin();

        TypedQuery<Object> query = em.createQuery(queryString.toString(), Object.class);
        return query.getSingleResult();
    }
}
