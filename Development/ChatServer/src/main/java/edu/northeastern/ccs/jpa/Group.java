package edu.northeastern.ccs.jpa;

import edu.northeastern.ccs.im.userGroup.IGroup;
import edu.northeastern.ccs.im.userGroup.IUser;
import edu.northeastern.ccs.im.userGroup.IUserGroup;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The Class Group.
 */
@Entity
@Table(name="groupcomposite")
public class Group implements IGroup {
	
	/** The id. */
	@Id
	@GeneratedValue( strategy=GenerationType.AUTO )
	private int id;
	
	/** The name. */
	private String name;
	
	/** The users. */
	@OneToMany(targetEntity=User.class)
	private List<IUserGroup> users = new ArrayList<>();
	
	/** The msgs. */
	@OneToMany(targetEntity=Message.class)
	private List<Message> msgs = new ArrayList<>();
	
	/**
	 * Instantiates a new userGroup.
	 *
	 * @param id the id
	 * @param name the name
	 */
	public Group(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
		
	/**
	 * Instantiates a new userGroup.
	 */
	public Group() {
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	public List<IUserGroup> getUsers() {
		return users;
	}

	/**
	 * Sets the users.
	 *
	 * @param users the new users
	 */
	public void setUsers(List<IUserGroup> users) {
		this.users = users;
	}
	
	/**
	 * Adds the user.
	 *
	 * @param user the user
	 */
	public void addUser(IUserGroup user) {
		this.users.add(user);
	}
	
	/**
	 * Gets the msgs.
	 *
	 * @return the msgs
	 */
	@OneToMany(targetEntity=Message.class)
	public List<Message> getMsgs() {
		return msgs;
	}

	/**
	 * Sets the msgs.
	 *
	 * @param msgs the new msgs
	 */
	public void setMsgs(List<Message> msgs) {
		this.msgs = msgs;
	}

	

}
