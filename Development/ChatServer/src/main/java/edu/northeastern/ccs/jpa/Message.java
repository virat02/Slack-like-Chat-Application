package edu.northeastern.ccs.jpa;

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
	private User sender;
	
	/** The userGroup. */
	@ManyToOne
	private Group group;
	
	/**
	 * Instantiates a new message.
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
	 * Gets the userGroup.
	 *
	 * @return the userGroup
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Sets the userGroup.
	 *
	 * @param group the new userGroup
	 */
	public void setGroup(Group group) {
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
	
}
