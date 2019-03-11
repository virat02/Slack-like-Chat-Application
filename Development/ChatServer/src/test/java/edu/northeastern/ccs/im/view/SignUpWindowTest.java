package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import edu.northeastern.ccs.im.communication.ClientConnection;
import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.Payload;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SignUpWindowTest extends AbstractWindowTest  {

  private AbstractTerminalWindow signUpWindow;
  private AbstractTerminalWindow callerWindow;
  private ClientConnectionFactory clientConnectionFactory;
  private ClientConnection clientConnection;
  private NetworkResponse networkResponse;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    callerWindow = mock(AbstractTerminalWindow.class);
    clientConnectionFactory = mock(ClientConnectionFactory.class);
    clientConnection = mock(ClientConnection.class);
    networkResponse = mock(NetworkResponse.class);
    when(clientConnectionFactory.createClientConnection(anyString(), anyInt())).thenReturn(clientConnection);
    try {
      when(clientConnection.readResponse()).thenReturn(networkResponse);
    } catch (IOException e) {
      e.printStackTrace();
    }
    signUpWindow = new SignUpWindow(callerWindow, clientConnectionFactory);
  }

  @Test
  public void shouldDisplayEnterEmailAddressAsFirstOutput() {
    final String input = ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING +
            System.lineSeparator() +
            ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
  }

  @Test
  public void shouldDisplayEnterUserName() {
    final String input = "deys@deys.com" + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
            + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
            + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayEnterPassword() {
    final String input =
            "deys@deys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
                    + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayEnterReEnterPassword() {
    final String input = "deys@deys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
                    + "password" + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayReEnterPasswordWrong() {
    final String input = "deys@deys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
            + "password" + System.lineSeparator() + "password1" + System.lineSeparator()
            + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORDS_DO_NOT_MATCH
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplaySignUpSuccessfulOnSuccessfulNetworkConnection() throws JsonProcessingException {
    final String input = "deys@deys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
            + "password" + System.lineSeparator() + "password" + System.lineSeparator()
            + ConstantStrings.EXIT;
    ViewConstants.setInputStream(new BufferedReader(new StringReader(input)));
    when(networkResponse.payload()).thenReturn(mock(Payload.class));
    when(networkResponse.payload().jsonString()).thenReturn("{\"id\": 1}");
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.SIGN_UP_SUCCESSFUL
                    + System.lineSeparator() + ConstantStrings.CHAT_MAIN_COMMAND
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayLoginFailedForInvalidEmailAddress() {
    final String input = "deysdeys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
            + "password" + System.lineSeparator() + "password" + System.lineSeparator()
            + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
            + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
            + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
            + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
            + System.lineSeparator() + ConstantStrings.SIGN_UP_FAILED
            + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayLoginFailedAndRetry() {
    final String input = "deysdeys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
            + "password" + System.lineSeparator() + "password" + System.lineSeparator() + "1"
            + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.SIGN_UP_FAILED
                    + System.lineSeparator() + ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayLoginFailedAndGoBack() {
    final String input = "deysdeys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
            + "password" + System.lineSeparator() + "password" + System.lineSeparator() + "0"
            + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.SIGN_UP_FAILED
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayLoginFailedAndExit() {
    final String input = "deysdeys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
            + "password" + System.lineSeparator() + "password" + System.lineSeparator() + "*"
            + System.lineSeparator() + "Y";
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.SIGN_UP_FAILED
                    + System.lineSeparator() + ConstantStrings.CONFIRM_EXIT_MESSAGE
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayLoginFailedAndDoNotExit() {
    final String input = "deysdeys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
            + "password" + System.lineSeparator() + "password" + System.lineSeparator() + "*"
            + System.lineSeparator() + "N" + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.SIGN_UP_FAILED
                    + System.lineSeparator() + ConstantStrings.CONFIRM_EXIT_MESSAGE
                    + System.lineSeparator() + ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }

  @Test
  public void shouldDisplayLoginForInvalidCharacterInputs() {
    final String input = "deysdeys.com" + System.lineSeparator() + "deys" + System.lineSeparator()
            + "password" + System.lineSeparator() + "password" + System.lineSeparator() + "3"
            + System.lineSeparator() + ConstantStrings.EXIT;
    initializeInputStreamWithInput(input);
    signUpWindow.runWindow();
    Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING
                    + System.lineSeparator() + ConstantStrings.USER_NAME_STRING
                    + System.lineSeparator() + ConstantStrings.PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.RE_ENTER_PASSWORD_STRING
                    + System.lineSeparator() + ConstantStrings.SIGN_UP_FAILED
                    + System.lineSeparator() + ConstantStrings.INVALID_INPUT_STRING
                    + System.lineSeparator() + ConstantStrings.SIGN_UP_FAILED
                    + System.lineSeparator() + ConstantStrings.THANK_YOU + System.lineSeparator(),
            getOutputStreamContent());
  }
}