package edu.northeastern.ccs.im.service.jpa_service;

import edu.northeastern.ccs.im.user_group.Invite;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;

public class InviteJPAService {

    /** The emfactory. */
    //The entity manager for this class.
    private EntityManager entityManager;

    /**
     * A function made to setup the entity manager for this class to make the class more testable.
     * @param entityManager The entity manager for this class.
     */
    public void setEntityManager(EntityManager entityManager) {
        if(entityManager == null) {
            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory( "PrattlePersistance" );
            this.entityManager = emFactory.createEntityManager();
        } else {
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
     * Create an invite and persists invite
     * @param invite
     * @return id of the invite that is persisted in the db
     */
    public int createInvite(Invite invite){
        beginTransaction();
        entityManager.persist(invite);
        entityManager.flush();
        int id = invite.getId();
        endTransaction();
        return id;
    }

    /**
     * getInvite method returns the invite from the database fetched using id
     * @param id
     * @return Invite
     */
    public Invite getInvite(int id){
        beginTransaction();
        return entityManager.find(Invite.class, id);
    }

    /**
     *  updateInvite method updates all the data related to a persisted invite object
     * @param currentInvite
     */
    public void updateInvite(Invite currentInvite){
        beginTransaction();
        Invite invite = getInvite(currentInvite.getId());

        if(invite == null){
            throw new EntityNotFoundException("Can't find Invite for the given id = " + currentInvite.getId());
        }
        invite.setReceiver(currentInvite.getReceiver());
        invite.setSender(currentInvite.getSender());
        invite.setGroup(currentInvite.getGroup());
        invite.setInvitationMessage(currentInvite.getInvitationMessage());
        invite.setStatus(currentInvite.getStatus());
        endTransaction();
    }

    /**
     * delete Invite removes a persisted invite object from db
     * @param currentInvite
     */
    public void deleteInvite(Invite currentInvite){
        Invite invite = getInvite(currentInvite.getId());
        beginTransaction();
        entityManager.remove(invite);
        endTransaction();
    }

}
