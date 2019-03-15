package edu.northeastern.ccs.im.service.JPAService;

import edu.northeastern.ccs.im.userGroup.Profile;

import javax.persistence.*;

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

        thisProfile.setImageUrl(p.getImageUrl());
        thisProfile.setEmail(p.getEmail());

        endTransaction(entityManager);
    }

    public Profile getProfile(int id) {
        String queryString = "SELECT p " + "FROM Profile p WHERE p.id =" + id;
        EntityManager entityManager = beginTransaction();
        Query query = entityManager.createQuery(queryString);
        return (Profile) query.getSingleResult();
    }

}
