package edu.northeastern.ccs.im.view;

import org.junit.Before;
import org.junit.Test;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;

import static org.junit.Assert.assertEquals;

public class ForgotPasswordWindowTest extends AbstractWindowTest {

  private AbstractTerminalWindow forgotPasswordWindow;

  @Before
  public void initializeVariables() {
    forgotPasswordWindow = new ForgotPasswordWindow(new LoginWindow(null, null), null);
  }

  @Test
  public void testInitialDisplayView() {
    final String input = ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    forgotPasswordWindow.runWindow();
    assertEquals(ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForSuccessfulRecovery() {
    final String input = "deys@deys.com" + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    forgotPasswordWindow.runWindow();
    assertEquals(ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
            + ConstantStrings.RECOVERY_INITIATED + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForFailedRecovery() {
    final String input = "deysdeys.com" + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    forgotPasswordWindow.runWindow();
    assertEquals(ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
            + ConstantStrings.RECOVERY_FAILED + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayRetryForFailedRecovery() {
    final String input = "deysdeys.com" + System.lineSeparator() + "1" + System.lineSeparator()
                    + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    forgotPasswordWindow.runWindow();
    assertEquals(ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
            + ConstantStrings.RECOVERY_FAILED + System.lineSeparator()
            + ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForGoBack() {
    final String input = "deys@deys.com" + System.lineSeparator() + "0" + System.lineSeparator()
                    + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    forgotPasswordWindow.runWindow();
    assertEquals(ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
            + ConstantStrings.RECOVERY_INITIATED + System.lineSeparator()
            + ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForExit() {
    final String input = "deys@deys.com" + System.lineSeparator() + "*" + System.lineSeparator()
            + "Y";
    initializeInputStreamWithInput(input);
    forgotPasswordWindow.runWindow();
    assertEquals(ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
            + ConstantStrings.RECOVERY_INITIATED + System.lineSeparator()
            + ConstantStrings.CONFIRM_EXIT_MESSAGE + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void testInitialDisplayForInvalidInput() {
    final String input = "deys@deys.com" + System.lineSeparator() + "3" + System.lineSeparator()
            + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    forgotPasswordWindow.runWindow();
    assertEquals(ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
            + ConstantStrings.RECOVERY_INITIATED + System.lineSeparator()
            + ConstantStrings.INVALID_INPUT_STRING + System.lineSeparator()
            + ConstantStrings.RECOVERY_INITIATED + System.lineSeparator()
            + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }
}