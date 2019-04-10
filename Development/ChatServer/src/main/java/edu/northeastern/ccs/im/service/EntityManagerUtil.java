package edu.northeastern.ccs.im.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/***
 * A utility class to hold instance of entitymanagerfactory.
 * As the factory is thread-safe, it can be reused through out the
 * application to create thread local entitymanager instances.
 */
public class EntityManagerUtil {
    private EntityManagerFactory entityManagerFactory;

    /***
     * Creates an instance of this.
     */
    public EntityManagerUtil()  {
        entityManagerFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
    }
    /***
     * Returns a single instance of EntityManager.
     * EntityManager instances are not thread-safe,
     * hence it is advisable to use the entitymanager in a thread local
     * environment instead of being used as an instance.
     * @return an instance of EntityManager.
     */
    public EntityManager getEntityManager()    {
        return entityManagerFactory.createEntityManager();
    }
}
