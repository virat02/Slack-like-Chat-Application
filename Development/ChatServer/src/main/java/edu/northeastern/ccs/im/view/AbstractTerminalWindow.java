package edu.northeastern.ccs.im.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;

public abstract class AbstractTerminalWindow implements TerminalWindow {

  private final BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
  private final PrintStream outputStream = new PrintStream(System.out);

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
    outputStream.println(message);
  }

  protected void printInConsoleForCurrentProcess() {
    printInConsoleForProcess(currentProcess);
  }

  protected void printInConsoleForNextProcess() {
    printInConsoleForProcess(currentProcess + 1);
  }

  protected void printInConsoleForProcess(int process) {
    currentProcess = process;
    outputStream.println(processMap.get(currentProcess));
    getInputFromUser();
  }

  //INPUT METHODS
  protected void getInputFromUser() {
    String input = "";
    try {
      while (!input.equalsIgnoreCase("stop")) {
        input = inputStream.readLine();
        inputFetchedFromUser(input);
      }
    } catch (IOException e) {
      getInputFromUser();
    }
  }

  protected void invalidInputPassed() {
    outputStream.println(ConstantStrings.kInvalidInputString);
    printInConsoleForCurrentProcess();
  }

  //EXIT APP METHODS
  private void exitApp() {
    System.exit(0);
  }

  @Override
  public void exitWindow() {
    outputStream.println(ConstantStrings.kConfirmExitMessage);
    String input = "";
    try {
      while (!input.equalsIgnoreCase("stop")) {
        input = inputStream.readLine();
        if (input.toUpperCase().equals("Y")) {
          exitApp();
        }
        else if (input.toUpperCase().equals("N")) {
          runWindow();
        }
        else {
          outputStream.println(ConstantStrings.kInvalidInputString);
          exitWindow();
        }
      }
    } catch (IOException e) {
      outputStream.println(ConstantStrings.kInvalidInputString);
      exitWindow();
    }
  }
}
