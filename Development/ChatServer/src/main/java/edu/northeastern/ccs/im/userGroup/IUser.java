package edu.northeastern.ccs.im.userGroup;

import java.util.List;

public interface IUser extends IUserGroup {
   Profile getProfile();

   void addFollowee(User user);

   int getId();

   List<User> getFollowing();

}
