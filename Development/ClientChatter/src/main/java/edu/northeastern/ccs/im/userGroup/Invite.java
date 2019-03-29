package edu.northeastern.ccs.im.userGroup;

import javax.persistence.*;

/**
 * The Class Invite for monitoring all group invites.
 */
@Entity
@Table(name="invite")
public class Invite {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /** The group. */
    @OneToOne
    private Group group;

    /** The sender. */
    @OneToOne
    private User sender;

    /** The receiver. */
    @OneToOne
    private User receiver;

    /** The invitation message. */
    private String invitationMessage;

    /** The status. */
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the group.
     *
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets the group.
     *
     * @param group the new group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Gets the sender.
     *
     * @return the sender
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the sender.
     *
     * @param sender the new sender
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Gets the receiver.
     *
     * @return the receiver
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver.
     *
     * @param receiver the new receiver
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * Gets the invitation message.
     *
     * @return the invitation message
     */
    public String getInvitationMessage() {
        return invitationMessage;
    }

    /**
     * Sets the invitation message.
     *
     * @param invitationMessage the new invitation message
     */
    public void setInvitationMessage(String invitationMessage) {
        this.invitationMessage = invitationMessage;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("ID: ").append(getId())
                .append(" ")
                .append("Sender username: " + sender.getUsername())
                .append(" ")
                .append("Receiver username: " + receiver.getUsername())
                .append(" ")
                .append("Status: ").append(status)
                .toString();
    }
}

