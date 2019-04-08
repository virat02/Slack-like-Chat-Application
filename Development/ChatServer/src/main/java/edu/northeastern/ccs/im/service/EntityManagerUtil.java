package edu.northeastern.ccs.im.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/***
 * A utility class to hold instance of entitymanagerfactory.
 * As the factory is thread-safe, it can be reused through out the
 * application to create thread local entitymanager instances.
 */
public class EntityManagerUtil {
    private static EntityManagerFactory entityManagerFactory;
    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
    }

    /**
     * Enforce singleton pattern, private constructor
     */
    private EntityManagerUtil()    {}

    /***
     * Returns the entitymanagerfactory
     * @return an instance of EntityManagerFactory.
     */
    public static EntityManagerFactory getEntityManagerFactory()    {
        return entityManagerFactory;
    }
}
