    package edu.northeastern.ccs.im.user_group;

    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    import javax.persistence.Column;
    import javax.persistence.JoinColumn;
    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;
    import javax.persistence.JoinTable;
    import javax.persistence.OneToMany;
    import javax.persistence.Table;
    import javax.persistence.Temporal;
    import javax.persistence.TemporalType;

    /**
    * The Class Group.
    */
    @Entity
    @Table(name="basegroup")
    public class Group implements IGroup {

    /** The id. */
    @Id
    @GeneratedValue( strategy=GenerationType.AUTO )
    private int id;

    /** The name. */
    private String name;

    /** The users. */
    @OneToMany(targetEntity=User.class)
    @JoinTable
    (
        name="basegroup_user",
        joinColumns={ @JoinColumn(name="GROUP_ID", referencedColumnName="ID") },
        inverseJoinColumns={ @JoinColumn(name="USER_ID", referencedColumnName="ID") }
    )
    private List<User> users = new ArrayList<>();

    @OneToMany(targetEntity=User.class)
    @JoinTable
    (
        name="basegroup_moderator",
        joinColumns={ @JoinColumn(name="GROUP_ID", referencedColumnName="ID") },
        inverseJoinColumns={ @JoinColumn(name="MODERATOR_ID", referencedColumnName="ID") }
    )
    private List<User> moderators= new ArrayList<>();

    /** The msgs. */
    @OneToMany(targetEntity=Message.class)
    @JoinTable
    (
        name="basegroup_message",
        joinColumns={ @JoinColumn(name="GROUP_ID", referencedColumnName="ID") },
        inverseJoinColumns={ @JoinColumn(name="MESSAGE_ID", referencedColumnName="ID") }
    )
    private List<Message> msgs = new ArrayList<>();

    /** The groups. */
    @OneToMany(targetEntity=Group.class)
    @JoinTable
    (
        name="basegroup_subgroup",
        joinColumns={ @JoinColumn(name="GROUP_ID", referencedColumnName="ID") },
        inverseJoinColumns={ @JoinColumn(name="SUBGROUP_ID", referencedColumnName="ID") }
    )
    private List<Group> groups = new ArrayList<>();

    @OneToMany(targetEntity=User.class)
    @JoinTable
    (
        name="basegroup_follower",
        joinColumns={ @JoinColumn(name="GROUP_ID", referencedColumnName="ID") },
        inverseJoinColumns={ @JoinColumn(name="FOLLOWER_ID", referencedColumnName="ID") }
    )
    private List<User> followers = new ArrayList<>();

    @JoinTable
    (
        name="basegroup_followee",
        joinColumns={ @JoinColumn(name="GROUP_ID", referencedColumnName="ID") },
        inverseJoinColumns={ @JoinColumn(name="FOLLOWEE_ID", referencedColumnName="ID") }
    )
    private List<User> followees = new ArrayList<>();

    @Column(unique=true)
    private String groupCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    private String groupPassword;


    /**
     * Instantiates a new user_group.
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
     * Instantiates a new user_group.
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
    public List<User> getUsers() {
        return users;
    }

    /**
     * Sets the users.
     *
     * @param users the new users
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Adds the user.
     *
     * @param user the user
     */
    public void addUser(User user) {
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

    /**
     * Gets the groups
     * @return
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Sets the groups
     * @param groups
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    /**
     * Add a group
     * @param group
     */
    public void addGroup(Group group) {
        this.groups.add(group);
    }

    /**
     * Get the list of followers
     * @return
     */
    public List<User> getFollowers() {
        return followers;
    }

    /**
     * Set the list of followers
     * @return
     */
    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    /**
     * Add a follower to the followers list
     * @param follower
     */
    public void addFollower(User follower) {
        this.followers.add(follower);
    }

    /**
     * Get the moderators of the group
     * @return
     */
    public List<User> getModerators() {
        return moderators;
    }

    /**
     * Set the moderators of the group
     * @return
     */
    public void setModerators(List<User> moderators) {
        this.moderators = moderators;
        for(User u : moderators){
            if(!this.users.contains(u))
                this.addUser(u);
        }
    }

    /**
     * Add a moderator to the current list of moderators
     * @param moderator
     */
    public void addModerator(User moderator) {
            this.moderators.add(moderator);
            if(!this.users.contains(moderator))
                this.addUser(moderator);
    }

    /**
     * Get the Group code
     * @return
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * Set the Group code
     * @return
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * Get the created on Date
     * @return
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Set the created on Date
     * @param createdOn
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Get the list of followees
     * @return
     */
    public List<User> getFollowees() {
        return followees;
    }

    /**
     * Set the list of followees
     * @return
     */
    public void setFollowees(List<User> followees) {
        this.followees = followees;
    }

    /**
     * Add a followee
     * @param followee
     */
    public void addFollowee(User followee) {
        this.followees.add(followee);
    }

    /**
     * Get the group password
     * @return
     */
    public String getGroupPassword() {
        return groupPassword;
    }

    /**
     * Set the group password
     * @return
     */
    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    /**
     * toString method for Group
     * @return
     */
    @Override
    public String toString(){
        return "group id : "+this.id+" group name : "+this.name+" group code : "+ this.groupCode + " number of moderators : "+this.moderators.size()
                +" number of users : "+this.users.size()+" number of followers : "+this.followees.size();
    }
}

