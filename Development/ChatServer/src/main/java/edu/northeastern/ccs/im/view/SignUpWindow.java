package edu.northeastern.ccs.im.view;

import java.util.HashMap;

public class SignUpWindow extends AbstractTerminalWindow {

  SignUpWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{
      put(0, ConstantStrings.kRecoveryEmail);
      put(1, ConstantStrings.kRecoveryInitiated);
    }});
  }

  @Override
  public void runWindow() {

  }

  @Override
  public void exitWindow() {

  }

  @Override
  void inputFetchedFromUser(String inputString) {

  }
}
