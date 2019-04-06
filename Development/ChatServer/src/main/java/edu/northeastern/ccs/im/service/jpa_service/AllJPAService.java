package edu.northeastern.ccs.im.service.jpa_service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

public class AllJPAService {

    private static final Logger LOGGER = Logger.getLogger(AllJPAService.class.getName());

    //The entity manager for this class.
    private EntityManager entityManager;

    /**
     * Set an entity manager
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        if(entityManager == null) {
            EntityManagerFactory emFactory;
            emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
            this.entityManager = emFactory.createEntityManager();
        }
        else{
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
     * Generic method to persist the given object in the DB
     * @param obj
     * @return
     */
    public boolean createEntity(Object obj) {
        beginTransaction();
        entityManager.persist(obj);
        entityManager.flush();
        endTransaction();
        return true;
    }

    /**
     * Generic method to delete an object from DB
     * @param obj
     * @return
     */
    public boolean deleteEntity(Object obj) {
        beginTransaction();
        entityManager.remove(obj);
        endTransaction();

        //If reached here, all went well
        return true;
    }
}
