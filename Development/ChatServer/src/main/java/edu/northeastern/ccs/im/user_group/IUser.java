package edu.northeastern.ccs.im.user_group;

import java.util.List;

/**
 * The interface for User, represents the User entity
 */
public interface IUser extends IUserGroup {
    /**
     * Get a profile
     * @return
     */
    Profile getProfile();

    /**
     * Get the user id
     * @return
     */
    int getId();

    /**
     * Get the list of users this user is following
     * @return
     */
    List<User> getFollowing();

}
