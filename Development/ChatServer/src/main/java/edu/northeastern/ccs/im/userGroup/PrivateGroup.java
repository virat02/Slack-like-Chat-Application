package edu.northeastern.ccs.im.userGroup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="privategroup")
public class PrivateGroup extends Group {
	
	 /** The id. */
    @Id
    @GeneratedValue( strategy=GenerationType.AUTO )
    private int id;
    
    /** The users. */
    @OneToMany(targetEntity=User.class)
    private List<IUserGroup> users = new ArrayList<>();
    
	@OneToMany(targetEntity=User.class)
	private List<User> moderators= new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<IUserGroup> getUsers() {
		return users;
	}

	public void setUsers(List<IUserGroup> users) {
		this.users = users;
	}

	public List<User> getModerators() {
		return moderators;
	}

	public void setModerators(List<User> moderators) {
		this.moderators = moderators;
	}
	
	
}
