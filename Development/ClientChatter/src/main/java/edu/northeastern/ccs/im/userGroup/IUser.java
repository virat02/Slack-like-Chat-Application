package edu.northeastern.ccs.im.userGroup;

import java.util.List;

public interface IUser extends IUserGroup {
   Profile getProfile();

   int getId();

   List<User> getFollowing();

}
