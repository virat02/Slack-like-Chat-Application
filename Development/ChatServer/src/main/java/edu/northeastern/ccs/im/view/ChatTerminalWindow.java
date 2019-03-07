package edu.northeastern.ccs.im.view;

import java.util.HashMap;

public class ChatTerminalWindow extends AbstractTerminalWindow {

  public ChatTerminalWindow(TerminalWindow caller) {
    super(caller, new HashMap<Integer, String>() {{

    }});
  }

  @Override
  void inputFetchedFromUser(String inputString) {

  }

  @Override
  public void runWindow() {
    
  }
}
