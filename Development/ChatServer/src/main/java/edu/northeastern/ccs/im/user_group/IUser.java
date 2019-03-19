package edu.northeastern.ccs.im.user_group;

import java.util.List;

public interface IUser extends IUserGroup {
    Profile getProfile();

    int getId();

    List<User> getFollowing();

}
