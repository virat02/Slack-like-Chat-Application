package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.userGroup.Group;
import edu.northeastern.ccs.im.userGroup.Invite;
import edu.northeastern.ccs.im.userGroup.Profile;
import edu.northeastern.ccs.im.userGroup.User;

public class ResponseParser {

  private final static String networkErrorMessage = "Unable to fetch data";
  private final static String payloadEmptyErrorMessage = "Payload empty";
  private final static String payloadParsingErrorMessage = "Error in parsing data";

  public static void throwErrorIfResponseFailed(NetworkResponse networkResponse) throws IOException
          , NetworkResponseFailureException {
    if (networkResponse.status().equals(NetworkResponse.STATUS.FAILED)) {
      HashMap<String,String> errorMessages =
              CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString(),
                      new TypeReference<HashMap<String,String>>() {});
      throw new NetworkResponseFailureException(errorMessages.getOrDefault("message",
              networkErrorMessage));
    }
  }

  static User parseLoginNetworkResponse(NetworkResponse networkResponse) throws NetworkResponseFailureException {
    try {
      throwErrorIfResponseFailed(networkResponse);
      if (networkResponse.payload() == null || networkResponse.payload().jsonString().equals("")) {
        throw new NetworkResponseFailureException(payloadEmptyErrorMessage);
      }
      User parsedUserObj = CommunicationUtils
              .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
      if (parsedUserObj == null) {
        throw new NetworkResponseFailureException(payloadEmptyErrorMessage);
      }
      UserConstants.setUserObj(parsedUserObj);
      return parsedUserObj;
    }
    catch (IOException e) {
      throw new NetworkResponseFailureException(payloadParsingErrorMessage);
    }
  }

  static boolean parseForgotPasswordResponse(NetworkResponse networkResponse) {
    return networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL);
  }

  static User parseSearchUserNetworkResponse(NetworkResponse networkResponse) throws
          NetworkResponseFailureException {
    try {
      throwErrorIfResponseFailed(networkResponse);
      User parsedUserObj = CommunicationUtils
              .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
      return parsedUserObj;
    }
    catch (IOException exception) {
      throw new NetworkResponseFailureException(payloadParsingErrorMessage);
    }
  }

  static Group parseGroupNetworkResponse(NetworkResponse networkResponse) throws
          NetworkResponseFailureException {
    try {
      throwErrorIfResponseFailed(networkResponse);
      Group parsedGroupObj = CommunicationUtils
              .getObjectMapper().readValue(networkResponse.payload().jsonString(), Group.class);
      return parsedGroupObj;
    } catch (IOException e) {
      throw new NetworkResponseFailureException(payloadParsingErrorMessage);
    }
  }

  static List<Group> parseSearchGroupNetworkResponse(NetworkResponse networkResponse) throws
          NetworkResponseFailureException {
    try {
      throwErrorIfResponseFailed(networkResponse);
      List<Group> parsedObjects = CommunicationUtils
              .getObjectMapper().readValue(networkResponse.payload().jsonString(), List.class);
      return parsedObjects;
    }
    catch (IOException exception) {
      throw new NetworkResponseFailureException(payloadParsingErrorMessage);
    }
  }

  static Group parseAddGroupResponse(NetworkResponse networkResponse) throws
          NetworkResponseFailureException {
    try {
      throwErrorIfResponseFailed(networkResponse);
      Group parsedUserObj = CommunicationUtils
              .getObjectMapper().readValue(networkResponse.payload().jsonString(), Group.class);
      return parsedUserObj;
    }
    catch (IOException exception) {
      throw new NetworkResponseFailureException(payloadParsingErrorMessage);
    }
  }

  static boolean parseDeleteGroupResponse(NetworkResponse networkResponse) {
    return networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL);
  }

  static boolean parseUpdateUserObj(NetworkResponse networkResponse) {
    try {
      if (networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL)) {
        User parsedUserObj = CommunicationUtils
                .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
        UserConstants.setUserObj(parsedUserObj);
        return true;
      }
    }
    catch (IOException exception) {
      return false;
    }
    return false;
  }

  static Profile parseUpdateUserProfile(NetworkResponse networkResponse) throws
          NetworkResponseFailureException {
    try {
      throwErrorIfResponseFailed(networkResponse);
      Profile parsedProfileObj = CommunicationUtils
              .getObjectMapper().readValue(networkResponse.payload().jsonString(), Profile.class);
      return parsedProfileObj;
    }
    catch (IOException exception) {
      throw new NetworkResponseFailureException(payloadParsingErrorMessage);
    }
  }

  static List<User> parseFollowersList(NetworkResponse networkResponse) throws
          NetworkResponseFailureException {
    try {
      throwErrorIfResponseFailed(networkResponse);
      List<User> parsedUsers =
              CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString()
                      , ArrayList.class);

      return parsedUsers;
    }
    catch (IOException exception) {
      throw new NetworkResponseFailureException(payloadParsingErrorMessage);
    }
  }

  static boolean parseSetFollowersList(NetworkResponse networkResponse) {
    try {
      if (networkResponse.status().equals(NetworkResponse.STATUS.SUCCESSFUL)) {
        User parsedUserObj = CommunicationUtils
                .getObjectMapper().readValue(networkResponse.payload().jsonString(), User.class);
        UserConstants.setUserObj(parsedUserObj);
        return true;
      }
    }
    catch (Exception exception) {
      return false;
    }
    return false;
  }

  public static List<Message> readRecentMessagesAndPrintInScreen(NetworkResponse networkResponse) throws NetworkResponseFailureException {
    try {
      throwErrorIfResponseFailed(networkResponse);
      List<Message> messages = CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString(),new TypeReference<ArrayList<Message>>() {});
      messages.stream().map(m -> MessageSocketListener.messageFormatter().formatMessage(m)).filter(m -> !m.equals(""))
              .forEach(ViewConstants.getOutputStream()::println);

      return messages;
    }
    catch (IOException exception) {
      throw new NetworkResponseFailureException(payloadParsingErrorMessage);
    }
  }

  public static void parseNetworkResponse(NetworkResponse networkResponse) throws IOException, NetworkResponseFailureException {
    if (networkResponse.status() == NetworkResponse.STATUS.FAILED)
      throwErrorIfResponseFailed(networkResponse);

    JsonNode jsonNode = CommunicationUtils.getObjectMapper().readTree(networkResponse.payload().jsonString());
    if (jsonNode.has("message")) {
      ViewConstants.getOutputStream().println(jsonNode.get("message").asText());
    }
  }

  public static List<Invite> parseInvitationsList(NetworkResponse networkResponse) throws IOException, NetworkResponseFailureException {
    if (networkResponse.status() == NetworkResponse.STATUS.FAILED)
      throwErrorIfResponseFailed(networkResponse);

    return CommunicationUtils.getObjectMapper().readValue(networkResponse.payload().jsonString(), new TypeReference<ArrayList<Invite>>() {
    });
  }
}
