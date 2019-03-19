package edu.northeastern.ccs.im.user_group;

import java.util.Date;
import java.util.List;

/***
 *
 */
public interface IGroup extends IUserGroup {
    /**
     * Returns the name of the entity.
     * @return
     */
    String getName();

    /***
     * Set the name of the entity.
     * @param name
     */
    void setName(String name);

    /***
     * Returns the id of the entity.
     * @return
     */
    int getId();

    /***
     * Timestamp when the entity has been created
     * @return
     */
    Date getCreatedOn();

    /***
     * Return the list of Messages.
     * @return the list of messages associated with this group.
     */
    List<Message> getMsgs();
}
