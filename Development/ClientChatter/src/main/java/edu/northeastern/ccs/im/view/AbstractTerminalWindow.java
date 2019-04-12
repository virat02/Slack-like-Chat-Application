package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.*;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractTerminalWindow implements TerminalWindow {

  protected ClientConnectionFactory clientConnectionFactory;
  private final TerminalWindow callerWindow;
  private int currentProcess;
  private final Map<Integer,String> processMap;

  private ClientConnection clientConnection;
  protected NetworkRequestFactory networkRequestFactory = new NetworkRequestFactory();

  abstract void inputFetchedFromUser(String inputString);

  private AbstractTerminalWindow(TerminalWindow callerWindow, Map<Integer,String> processMap) {
    this.callerWindow = callerWindow;
    this.currentProcess = 0;
    this.processMap = processMap;
  }

  protected AbstractTerminalWindow(TerminalWindow callerWindow, Map<Integer,String> processMap, ClientConnectionFactory clientConnectionFactory) {
    this(callerWindow, processMap);
    this.clientConnectionFactory = clientConnectionFactory;
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

  protected void printInConsoleForNextProcess(String input) {
    if (input.equals("QUIT")) {
      exitApp();
      return;
    }

    printInConsoleForProcess(currentProcess + 1);
  }

  protected void printInConsoleForProcess(int process) {
    currentProcess = process;
    ViewConstants.getOutputStream().println(processMap.get(currentProcess));
    getInputFromUser();
  }

  protected abstract String helpCommand();

  //INPUT METHODS
  protected void getInputFromUser() {
    String input;
    try {
      while((input = ViewConstants.getInputStream().readLine()) != null) {
        if (input.trim().equals("")) {
          printMessageInConsole(ConstantStrings.INVALID_INPUT_STRING);
          printInConsoleForCurrentProcess();
          continue;
        }
        else if (input.equals("exit")) {
          exitApp();
          return;
        }
        else if (input.equals("/..")) {
          goBack();
        }
        else if (input.equals("??")) {
          try {
            String url = "https://github.ccs.neu.edu/cs5500/team-108-SP19";
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
          }
          catch (java.io.IOException e) {
            printMessageInConsole("Unable to open help document");
          }
          printInConsoleForCurrentProcess();
        }
        else if (input.equals("?")) {
          printMessageInConsole("*********HELP************");
          printMessageInConsole(helpCommand());
          printMessageInConsole("*************************");
          printInConsoleForCurrentProcess();
        }
        else
          inputFetchedFromUser(input);
      }
    } catch (IOException e) {
      getInputFromUser();
    }
  }

  protected void invalidInputPassed() {
    ViewConstants.getOutputStream().println(ConstantStrings.INVALID_INPUT_STRING);
    printInConsoleForCurrentProcess();
  }

  //EXIT APP METHODS
  private void exitApp() {
    printMessageInConsole(ConstantStrings.THANK_YOU);
    System.exit(0);
  }

  public void signOutApp() {
    if (!(this instanceof RootWindow)) {
      callerWindow.signOutApp();
    }
    else {
      runWindow();
    }
  }

  @Override
  public void exitWindow() {
    ViewConstants.getOutputStream().println(ConstantStrings.CONFIRM_EXIT_MESSAGE);
    String input = "";
    try {
      while((input = ViewConstants.getInputStream().readLine()) != null) {
        if (input.toUpperCase().equals("Y")) {
          exitApp();
        }
        else if (input.toUpperCase().equals("N")) {
          runWindow();
        }
        else {
          ViewConstants.getOutputStream().println(ConstantStrings.INVALID_INPUT_STRING);
          exitWindow();
        }
      }
    } catch (IOException e) {
      ViewConstants.getOutputStream().println(ConstantStrings.INVALID_INPUT_STRING);
      exitWindow();
    }
  }

  private void createNetworkConnection() throws IOException {
    if (clientConnection == null) {
      clientConnection = clientConnectionFactory
              .createClientConnection(NetworkConstants.getHostName(),
                      NetworkConstants.getPortNumber());
    }
    clientConnection.connect();
  }

  protected NetworkResponse sendNetworkConnection(NetworkRequest networkRequest)
          throws NetworkResponseFailureException {
    try {
      createNetworkConnection();
      clientConnection.sendRequest(networkRequest);
      NetworkResponse networkResponse = clientConnection.readResponse();
      clientConnection.close();
      return networkResponse;
    } catch (IOException e) {
      throw new NetworkResponseFailureException("Unable to connect to network");
    }
  }
}
