package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RootWindowTest extends AbstractWindowTest {

  private AbstractTerminalWindow rootWindow;

  @Before
  public void initializeVariables() {
    rootWindow = new RootWindow(null, new ClientConnectionFactory());
  }

  @Test
  public void testInitialDisplayView() {
    final String input = ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    rootWindow.runWindow();
    assertEquals(ConstantStrings.INITIAL_LAUNCH + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForLogin() {
    final String input = "1" + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    rootWindow.runWindow();
    assertEquals(ConstantStrings.INITIAL_LAUNCH + System.lineSeparator()
            + ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForSignUp() {
    final String input = "2" + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    rootWindow.runWindow();
    assertEquals(ConstantStrings.INITIAL_LAUNCH + System.lineSeparator()
            + ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForExit() {
    final String input = "*" + System.lineSeparator() + "Y";
    initializeInputStreamWithInput(input);
    rootWindow.runWindow();
    assertEquals(ConstantStrings.INITIAL_LAUNCH + System.lineSeparator()
            + ConstantStrings.CONFIRM_EXIT_MESSAGE + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForInvalidInput() {
    final String input = "3" + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    rootWindow.runWindow();
    assertEquals(ConstantStrings.INITIAL_LAUNCH + System.lineSeparator()
            + ConstantStrings.INVALID_INPUT_STRING + System.lineSeparator()
            + ConstantStrings.INITIAL_LAUNCH + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }
}