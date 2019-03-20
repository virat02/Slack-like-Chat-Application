package edu.northeastern.ccs.im.communication;

public final class NetworkConstants {

  private static String HOST_NAME = "";
  private static int PORT_NUMBER = 0;

  public static String getHostName() {
    return HOST_NAME;
  }

  public static void setHostName(String hostName) {
    NetworkConstants.HOST_NAME = hostName;
  }

  public static int getPortNumber() {
    return PORT_NUMBER;
  }

  public static void setPortNumber(int portNumber) {
    NetworkConstants.PORT_NUMBER = portNumber;
  }
}
