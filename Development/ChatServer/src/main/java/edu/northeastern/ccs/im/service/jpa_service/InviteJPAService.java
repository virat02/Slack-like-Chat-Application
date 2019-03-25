package edu.northeastern.ccs.im.service.jpa_service;

import com.sun.javaws.exceptions.InvalidArgumentException;
import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Invite;
import edu.northeastern.ccs.im.user_group.User;
import org.eclipse.persistence.internal.helper.InvalidObject;

import javax.persistence.*;
import java.util.List;

public class InviteJPAService {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(UserJPAService.class.getName());
    private GroupJPAService groupJPA = new GroupJPAService();

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
     * @param invite that is being created
     * @return id of the invite that is persisted in the db
     */
    public int createInvite(Invite invite) throws InviteNotAddedException{
        try {
            beginTransaction();
            entityManager.persist(invite);
            entityManager.flush();
            int id = invite.getId();
            endTransaction();
            return id;
        } catch (Exception e) {
            LOGGER.info("Could not create the invite!");
            throw new InviteNotAddedException("Could not create the invite!");
        }
    }

    /**
     * getInvite method returns the invite from the database fetched using id
     * @param id of the invite
     * @return Invite that has been retrieved by the JPA service
     */
    public Invite getInvite(int id) throws InviteNotFoundException {
        try {
            beginTransaction();
            Invite invite = entityManager.find(Invite.class, id);
            endTransaction();
            return invite;
        } catch (Exception e) {
            LOGGER.info("Could not get the invite!");
            throw new InviteNotFoundException("Could not get the invite!");
        }
    }

    /**
     *  updateInvite method updates all the data related to a persisted invite object
     * @param currentInvite that we are looking to update
     */
    public void updateInvite(Invite currentInvite) throws InviteNotUpdatedException{
        try {
            beginTransaction();
            Invite invite = entityManager.find(Invite.class, currentInvite.getId());

            if (invite == null) {
                throw new EntityNotFoundException("Can't find Invite for the given id = " + currentInvite.getId());
            }
            invite.setReceiver(currentInvite.getReceiver());
            invite.setSender(currentInvite.getSender());
            invite.setGroup(currentInvite.getGroup());
            invite.setInvitationMessage(currentInvite.getInvitationMessage());
            invite.setStatus(currentInvite.getStatus());
            endTransaction();
        } catch (Exception e) {
            LOGGER.info("Could not update the invite!");
            throw new InviteNotUpdatedException("Could not update the invite!");
        }
    }

    /**
     * delete Invite removes a persisted invite object from db
     * @param currentInvite we are looking to mark as delete
     */
    public Invite deleteInvite(Invite currentInvite) throws InviteNotDeletedException{
        try {
            Invite invite = getInvite(currentInvite.getId());
            beginTransaction();
            entityManager.remove(invite);
            endTransaction();
            return invite;
        } catch (Exception e) {
            LOGGER.info("Could not delete the invite!");
            throw new InviteNotDeletedException("Could not delete the invite!");
        }
    }

    /**
     *  search a list of invites based on groupCode
     * @param groupCode
     * @return
     * @throws GroupNotFoundException
     * @throws InviteNotFoundException
     */
    public List<Invite> searchInviteByGroupCode(String groupCode , User moderator) throws GroupNotFoundException, InviteNotFoundException {
        try {
            groupJPA.setEntityManager(null);
            Group group = groupJPA.searchUsingCode(groupCode);
            List<User> moderators = group.getModerators();
            boolean isModerator = false;
            for(User u : moderators){
                if(u.getId() == moderator.getId()) {
                    isModerator = true;
                    break;
                }
            }

            if(isModerator) {
                String queryString =
                        "SELECT i FROM Invite i WHERE i.group.id ='" + group.getId() + "'";
                TypedQuery<Invite> query = entityManager.createQuery(queryString, Invite.class);
                List<Invite> inviteList = query.getResultList();
                return inviteList;
            }
            else {
                LOGGER.info("User is not the moderator of the group with code: " + groupCode);
                throw new IllegalAccessException("User is not the moderator of the group with code: " + groupCode);
            }
        }
        catch (GroupNotFoundException e) {
            LOGGER.info("Can't find Group with code: " + groupCode);
            throw new GroupNotFoundException("Can't find Group with code: " + groupCode);
        }
        catch (Exception e) {
            LOGGER.info("Could not get the invite!");
            throw new InviteNotFoundException("Could not get the invite!");
        }
    }

}
