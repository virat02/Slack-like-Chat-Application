package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.userGroup.User;

public class UserConstants {

  private User userObj;

  private static UserConstants userConstantsInstance;

  private UserConstants() {}

  private static UserConstants getInstance() {
    if (userConstantsInstance == null) {
      userConstantsInstance = new UserConstants();
    }
    return userConstantsInstance;
  }

  public static void setUserObj(User userObj) {
    getInstance().userObj = userObj;
  }

  public static int getUserId() {
    return getInstance().userObj.getId();
  }

  public static String getUserName() {
    return getInstance().userObj.getUsername();
  }

  public static User getUserObj() {
    return getInstance().userObj;
  }
}
