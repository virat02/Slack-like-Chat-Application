package edu.northeastern.ccs.im.user_group;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/***
 * Represents an entity to store the user logs
 * mapping to user logs in / out to the chatroom.
 */
@Entity
@Table(name = "userchatroomlogoffevent")
public class UserChatRoomLogOffEvent {

    @EmbeddedId
    private CompositeObject compositeObject;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "loggedouttime")
    private Date loggedOutTime;

    public Date getLoggedOutTime() {
        return loggedOutTime;
    }

    /***
     * Sets the loggedouttime.
     * @param loggedOutTime
     */
    public void setLoggedOutTime(Date loggedOutTime) {
        this.loggedOutTime = loggedOutTime;
    }

    /***
     * Constructor to instantiate this class.
     * @param userId -> The id of the user
     * @param groupId -> The id of the group
     * @param loggedOutTime -> The timestamp at which user left the group.
     */
    public UserChatRoomLogOffEvent(long userId, long groupId, Date loggedOutTime) {
        this.compositeObject = new CompositeObject(userId, groupId);
        this.loggedOutTime = loggedOutTime;
    }

    /***
     * A public constructor for JPA purposes.
     */
    public UserChatRoomLogOffEvent() {
    }
}

/***
 * Defines a composite key class for UserChatRoomLogOffEvent
 * Basically the user id and group id forms a composite key for the
 * join group event.
 *
 */
@Embeddable
class CompositeObject implements Serializable {
    @Column(name = "user_id")
    private long userId;

    @Column(name = "group_id")
    private long groupId;
    /***
     * A public constructor for JPA purposes
     */
    public CompositeObject() {
    }

    /***
     *
     * @param userId The user id which wants to exit from chatroom
     * @param groupId The code of the group chatroom
     */
    public CompositeObject(long userId, long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CompositeObject))
            return false;

        CompositeObject compositeObject = (CompositeObject) obj;
        return compositeObject.groupId == groupId && compositeObject.userId == userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }
}
