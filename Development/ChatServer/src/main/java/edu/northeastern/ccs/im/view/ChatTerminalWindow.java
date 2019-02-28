package edu.northeastern.ccs.im.view;

import java.util.HashMap;

public class ChatTerminalWindow extends AbstractTerminalWindow {

  public ChatTerminalWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kRecoveryEmail);
      put(1, ConstantStrings.kRecoveryInitiated);
    }});
  }

  @Override
  void inputFetchedFromUser(String inputString) {

  }

  @Override
  public void runWindow() {

  }
}
