package edu.northeastern.ccs.im.userGroup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.Date;

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

    /** The message expiration time in seconds. */
    private int expiration;

    /** The sender. */
    private User sender;

    /** The receiver. */
    private Group receiver;

    /**
     * Instantiates a new message.
     */
    public Message(int id, String message, Date timestamp, int expiration, User sender, Group receiver, boolean deleted) {
        super();
        this.id = id;
        this.msg = message;
        this.timestamp = timestamp;
        this.expiration = expiration;
        this.sender = sender;
        this.receiver = receiver;
        this.deleted = deleted;
    }

    /**
     * Instantiates a new message.
     */
    public Message(int id, String message, Date timestamp, int expiration) {
        super();
        this.id = id;
        this.msg = message;
        this.timestamp = timestamp;
        this.expiration = expiration;
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
        if (message.length() != 0) {
            this.msg = message;
        }
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
     * Sets the expiration.
     *
     * @param expiration the new expiration
     */
    public void setExpiration(int expiration) {
        if (expiration != 0) {
            this.expiration = expiration;
        }
    }

    /**
     * Gets the expiration.
     *
     * @return the expiration
     */
    public int getExpiration() {
        return expiration;
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
    public User getSender() {
        return sender;
    }

    /**
     * Sets the sender.
     *
     * @param sender the new sender
     */
    public void setSender(User sender) {
        if (sender != null) {
            this.sender = sender;
        }
    }

    /**
     * Gets the receiver.
     *
     * @return the receiver
     */
    public Group getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver.
     *
     * @param receiver the new receiver
     */
    public void setReceiver(Group receiver) {
        if (receiver != null) {
            this.receiver = receiver;
        }
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
     * toString method for a Message
     */
    @Override
    public String toString() {
        return "Id: "+getId()+"\nMessage: "+getMessage() + "\nMessage Timestamp: "+getTimestamp()+"\nExpiry time: "+getExpiration()+"\nSender: "+getSender()+"\nReceiver: "+getReceiver()+"\nMessage deleted? : "+isDeleted();
    }

}