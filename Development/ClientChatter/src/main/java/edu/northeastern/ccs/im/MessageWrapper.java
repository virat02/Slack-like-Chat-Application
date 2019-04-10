package edu.northeastern.ccs.im;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.view.UserConstants;

public class MessageWrapper {

  private List<Message> messageList;

  public MessageWrapper() {
    messageList = new ArrayList<>();
  }

  public void addMessageToList(List<Message> newMessages) {
    messageList.addAll(newMessages);
  }

  public void clearMessages() {
    messageList = new ArrayList<>();
  }

  public int getIndexOfObject(Message message) {
    return messageList.indexOf(message);
  }

  public int length() {
    return messageList.size();
  }

  public boolean isMessageByCurrentUser(int messageIndex) {
    if (messageIndex <= messageList.size() && messageIndex > 0) {
      Message messageObj = messageList.get(messageIndex - 1);
      return messageObj.getName().equals(UserConstants.getUserName());
    }
    return false;
  }
}
