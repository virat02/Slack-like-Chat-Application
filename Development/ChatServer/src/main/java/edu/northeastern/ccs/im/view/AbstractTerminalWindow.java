package edu.northeastern.ccs.im.view;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractTerminalWindow implements TerminalWindow {

  private final TerminalWindow callerWindow;

  private int currentProcess;
  private final Map<Integer,String> processMap;

  abstract void inputFetchedFromUser(String inputString);

  protected AbstractTerminalWindow(TerminalWindow callerWindow, Map<Integer,String> processMap) {
    this.callerWindow = callerWindow;
    this.currentProcess = 0;
    this.processMap = processMap;
  }

  public int getCurrentProcess() {
    return currentProcess;
  }

  @Override
  public void runWindow() {
    currentProcess = 0;
    printInConsoleForCurrentProcess();
  }

  @Override
  public void goBack() {
    callerWindow.runWindow();
  }

  //OUTPUT METHODS
  protected void printMessageInConsole(String message) {
    ViewConstants.getOutputStream().println(message);
  }

  protected void printInConsoleForCurrentProcess() {
    printInConsoleForProcess(currentProcess);
  }

  protected void printInConsoleForNextProcess() {
    printInConsoleForProcess(currentProcess + 1);
  }

  protected void printInConsoleForProcess(int process) {
    currentProcess = process;
    ViewConstants.getOutputStream().println(processMap.get(currentProcess));
    getInputFromUser();
  }

  //INPUT METHODS
  protected void getInputFromUser() {
    String input = "";
    try {
      while((input = ViewConstants.getInputStream().readLine()) != null){
        inputFetchedFromUser(input);
      }
    } catch (IOException e) {
      getInputFromUser();
    }
  }

  protected void invalidInputPassed() {
    ViewConstants.getOutputStream().println(ConstantStrings.kInvalidInputString);
    printInConsoleForCurrentProcess();
  }

  //EXIT APP METHODS
  private void exitApp() {
    System.exit(0);
  }

  @Override
  public void exitWindow() {
    ViewConstants.getOutputStream().println(ConstantStrings.kConfirmExitMessage);
    String input = "";
    try {
      while (!input.equalsIgnoreCase("stop")) {
        input = ViewConstants.getInputStream().readLine();
        if (input.toUpperCase().equals("Y")) {
          exitApp();
        }
        else if (input.toUpperCase().equals("N")) {
          runWindow();
        }
        else {
          ViewConstants.getOutputStream().println(ConstantStrings.kInvalidInputString);
          exitWindow();
        }
      }
    } catch (IOException e) {
      ViewConstants.getOutputStream().println(ConstantStrings.kInvalidInputString);
      exitWindow();
    }
  }
}
