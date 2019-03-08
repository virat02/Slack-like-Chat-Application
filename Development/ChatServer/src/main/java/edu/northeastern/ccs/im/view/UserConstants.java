package edu.northeastern.ccs.im.view;

public class UserConstants {

  private int userId;
  private String userName;
  private String emailAddress;

  private static UserConstants userConstantsInstance;

  private UserConstants() {}

  private static UserConstants getInstance() {
    if (userConstantsInstance == null) {
      userConstantsInstance = new UserConstants();
    }
    return userConstantsInstance;
  }

  public static int getUserId() {
    return getInstance().userId;
  }

  public static void setUserId(int userId) {
    getInstance().userId = userId;
  }

  public static String getUserName() {
    return getInstance().userName;
  }

  public static void setUserName(String userName) {
    getInstance().userName = userName;
  }

  public static String getEmailAddress() {
    return getInstance().emailAddress;
  }

  public static void setEmailAddress(String emailAddress) {
    getInstance().emailAddress = emailAddress;
  }
}
