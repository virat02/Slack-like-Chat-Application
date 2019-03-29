package edu.northeastern.ccs.im.service.jpa_service;


import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.customexceptions.*;
import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Invite;
import edu.northeastern.ccs.im.user_group.User;

import javax.persistence.*;
import java.util.List;

public class InviteJPAService {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(UserJPAService.class.getName());
    private GroupJPAService groupJPA = new GroupJPAService();
    private static final String NO_INVITE = "Could not get the invite!";

    /**
     * The emfactory.
     */
    //The entity manager for this class.
    private EntityManager entityManager;

    /**
     * A function made to setup the entity manager for this class to make the class more testable.
     *
     * @param entityManager The entity manager for this class.
     */
    public void setEntityManager(EntityManager entityManager) {
        if (entityManager == null) {
            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("PrattlePersistance");
            this.entityManager = emFactory.createEntityManager();
        } else {
            this.entityManager = entityManager;
        }
    }

    /**
     * A setter for GroupJPAService
     * @param groupJPA the groupjpa service we are setting.
     */
    public void setGroupJPA(GroupJPAService groupJPA) {
        this.groupJPA = groupJPA;
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
     *
     * @param invite that is being created
     * @return id of the invite that is persisted in the db
     */
    public int createInvite(Invite invite) throws InviteNotAddedException {
        try {
            beginTransaction();
            if (!isUserInGroup(invite)) {
                if (!isUserInvitedToGroup(invite)) {
                    entityManager.persist(invite);
                    entityManager.flush();
                    int id = invite.getId();
                    endTransaction();
                    return id;
                } else {
                    throw new InviteNotAddedException("User has already been invited to group!");
                }
            } else {
                throw new InviteNotAddedException("User is already in group!");
            }
        } catch (Exception e) {
            LOGGER.info("Could not create the invite!");
            throw new InviteNotAddedException("Could not create the invite!");
        }
    }

    /**
     * getInvite method returns the invite from the database fetched using id
     *
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
            LOGGER.info(NO_INVITE);
            throw new InviteNotFoundException(NO_INVITE);
        }
    }

    /**
     * updateInvite method updates all the data related to a persisted invite object
     *
     * @param currentInvite that we are looking to update
     */
    public void updateInvite(Invite currentInvite) throws InviteNotUpdatedException {
        try {
            beginTransaction();
            Invite invite = entityManager.find(Invite.class, currentInvite.getId());

            if (invite == null) {
                throw new EntityNotFoundException("Can't find Invite for the given id = " + currentInvite.getId());
            }

            invite.setStatus(currentInvite.getStatus());
            if (invite.getStatus() == Status.ACCEPTED) {
                groupJPA.setEntityManager(null);
                groupJPA.addUserToGroup(invite.getGroup().getId(), invite.getReceiver());
            }
            endTransaction();
        } catch (Exception e) {
            LOGGER.info("Could not update the invite!");
            throw new InviteNotUpdatedException("Could not update the invite!");
        }
    }

    /**
     * delete Invite removes a persisted invite object from db
     *
     * @param currentInvite we are looking to mark as delete
     */
    public Invite deleteInvite(Invite currentInvite) throws InviteNotDeletedException {
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
     * @param groupCode of the group
     * @return list of invites found
     * @throws GroupNotFoundException if the group is not found
     * @throws InviteNotFoundException if the invite is not found
     */
    public List<Invite> searchInviteByGroupCode(String groupCode, User moderator) throws GroupNotFoundException, InviteNotFoundException {
        try {
            groupJPA.setEntityManager(null);
            Group group = groupJPA.searchUsingCode(groupCode);
            List<User> moderators = group.getModerators();
            boolean isModerator = false;
            for (User u : moderators) {
                if (u.getId() == moderator.getId()) {
                    isModerator = true;
                    break;
                }
            }

            if (isModerator) {
                String queryString =
                        "SELECT i FROM Invite i WHERE i.group.id ='" + group.getId() + "'";
                TypedQuery<Invite> query = entityManager.createQuery(queryString, Invite.class);
                return query.getResultList();
            } else {
                ChatLogger.info("User is not the moderator of the group with code: " + groupCode);
                throw new IllegalAccessException("User is not the moderator of the group with code: " + groupCode);
            }
        } catch (GroupNotFoundException e) {
            LOGGER.info("Can't find Group with code: " + groupCode);
            throw new GroupNotFoundException("Can't find Group with code: " + groupCode);
        } catch (Exception e) {
            LOGGER.info(NO_INVITE);
            throw new InviteNotFoundException(NO_INVITE);
        }
    }

    private boolean isUserInGroup(Invite invite) {
        Group group = invite.getGroup();
        User receiver = invite.getReceiver();
        List<User> usersOfGroup = group.getUsers();
        for (User u : usersOfGroup) {
            if (u.getId() == receiver.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean isUserInvitedToGroup(Invite invite) {
        Group group = invite.getGroup();
        User receiver = invite.getReceiver();
        String queryString =
                "SELECT i FROM Invite i WHERE i.group.id =" + group.getId() + " AND i.receiver.id = " + receiver.getId()
                        + " AND NOT i.status = edu.northeastern.ccs.im.service.jpa_service.Status.REJECTED";
        TypedQuery<Invite> query = entityManager.createQuery(queryString, Invite.class);
        List<Invite> inviteList = query.getResultList();
        return !inviteList.isEmpty();
    }
}