package edu.northeastern.ccs.jpa;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {

	@Id
	private int id;
	
	@OneToMany(targetEntity=Message.class)
	private List<Message> messages;
	
	@OneToMany(targetEntity=Group.class)
	private List<Group> groups;
	
	@OneToOne
	private Profile profile;

	public User() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public void addMessages(Message msg) {
		this.messages.add(msg);
	}
	
	public void addGroup(Group group) {
		this.groups.add(group);
	}
	
	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
}
