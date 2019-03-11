package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.Profile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;

public class ProfileJPAService {

    private EntityManager beginTransaction() {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
        EntityManager entityManager = emFactory.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager;
    }

    private void endTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void createProfile(Profile p) {
        EntityManager entityManager = beginTransaction();
        entityManager.persist(p);
        endTransaction(entityManager);

    }

    public void deleteProfile(Profile p) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(p);
        endTransaction(entityManager);
    }

    public void updateProfile(Profile p) {
        EntityManager entityManager = beginTransaction();
        Profile thisProfile = entityManager.find(Profile.class, p.getId());
        if (thisProfile == null) {
            throw new EntityNotFoundException("Can't find Profile for ID "
                    + p.getId());
        }

        thisProfile.setUser(p.getUser());
        thisProfile.setProfileAccess(p.getProfileAccess());
        thisProfile.setImageUrl(p.getImageUrl());
        thisProfile.setPassword(p.getPassword());
        thisProfile.setEmail(p.getEmail());
        thisProfile.setUsername(p.getUsername());

        endTransaction(entityManager);
    }

}
