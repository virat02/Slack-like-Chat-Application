package edu.northeastern.ccs.im.userGroup;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.*;

/**
 * The Class Message.
 */
@Entity
@Table(name="message")
public class Message {

    /** The id. */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    /** The msg. */
    private String msg;

    /** The timestamp. */
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    /** The sender. */
    @ManyToOne
    private IUserGroup receiver;

    /** The userGroup. */
    @ManyToOne
    private IUserGroup group;

    /** The sender. */
    private IUserGroup sender;

    /**
     * Instantiates a new message.
     */
    public Message(int id, String message, Date timestamp, IUserGroup sender, IUserGroup receiver, boolean deleted) {
        super();
        this.id = id;
        this.msg = message;
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
        this.deleted = deleted;
    }

    /**
     * Empty constructor for message
     */
    public Message() {
        super();
    }

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
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return msg;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        msg = message;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the new timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the sender.
     *
     * @return the sender
     */
    public IUserGroup getSender() {
        return sender;
    }

    /**
     * Sets the receiver.
     *
     * @param receiver the new sender
     */
    public void setReceiver(IUserGroup receiver) {
        this.sender = receiver;
    }

    /**
     * Gets the receiver.
     *
     * @return the receiver
     */
    public IUserGroup getReceiver() {
        return receiver;
    }

    /**
     * Sets the sender.
     *
     * @param sender the new sender
     */
    public void setSender(IUserGroup sender) {
        this.sender = sender;
    }

    /**
     * Gets the userGroup.
     *
     * @return the userGroup
     */
    public IUserGroup getGroup() {
        return group;
    }

    /**
     * Sets the userGroup.
     *
     * @param group the new userGroup
     */
    public void setGroup(IUserGroup group) {
        this.group = group;
    }

    /**
     * Checks if is deleted.
     *
     * @return true, if is deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted.
     *
     * @param deleted the new deleted
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /** The deleted. */
    private boolean deleted = false;

    /**
     * Create a message object
     * @param message
     * @param timestamp
     */
    public Message createMessage(String message, Date timestamp) {
        setMessage(message);
        setTimestamp(timestamp);

        return new Message(id, msg, timestamp, sender, receiver, deleted);
    }

    /**
     * Updates the message
     * @param message
     * @param timestamp
     */
    public void updateMessage(String message, Date timestamp) {
        if(message != null) {
            setMessage(message);
        }

        if(timestamp != null) {
            setTimestamp(timestamp);
        }
    }

    /**
     * Deletes a message
     */
    public void deleteMessage() {
        this.deleted = true;
    }

}