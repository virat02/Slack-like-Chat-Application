package edu.northeastern.ccs.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="groupcomposite")
public class Group {
	
	@Id
	@GeneratedValue( strategy=GenerationType.AUTO )
	private int id;
	private String name;
	
	@OneToMany(targetEntity=User.class)
	private List<User> users = new ArrayList<>();
	
	@OneToMany(targetEntity=Message.class)
	private List<Message> msgs = new ArrayList<>();
	
	public Group(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
		
	public Group() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public void addUser(User user) {
		this.users.add(user);
	}
	
	@OneToMany(targetEntity=Message.class)
	public List<Message> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<Message> msgs) {
		this.msgs = msgs;
	}

	

}
