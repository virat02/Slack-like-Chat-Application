package edu.northeastern.ccs.im.user_group;

import edu.northeastern.ccs.im.service.jpa_service.Status;

import javax.persistence.*;

@Entity
@Table(name="invite")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private Group group;

    @OneToOne
    private User sender;

    @OneToOne
    private User receiver;

    private String invitationMessage;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getInvitationMessage() {
        return invitationMessage;
    }

    public void setInvitationMessage(String invitationMessage) {
        this.invitationMessage = invitationMessage;
    }

}

