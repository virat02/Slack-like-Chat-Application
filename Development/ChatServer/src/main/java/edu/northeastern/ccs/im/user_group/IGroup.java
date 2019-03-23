package edu.northeastern.ccs.im.user_group;

import java.util.Date;
import java.util.List;

<<<<<<< HEAD
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
=======
public interface IGroup extends IUserGroup {

    String getName();
    void setName(String name);
    int getId();
    Date getCreatedOn();
>>>>>>> cd5e8a874b3c49e1ecd457c6c34f396f4d01e570
    List<Message> getMsgs();
}
