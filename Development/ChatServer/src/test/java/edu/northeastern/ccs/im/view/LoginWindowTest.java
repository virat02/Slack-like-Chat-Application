package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.ClientConnection;
import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.Payload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginWindowTest extends AbstractWindowTest {

    private AbstractTerminalWindow loginWindow;
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
        loginWindow = new LoginWindow(callerWindow, clientConnectionFactory);
    }

    @Test
    public void shouldDisplayEnterEmailAddressAsFirstOutput() {
        final String input = ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING +
                System.lineSeparator() +
                ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayEnterEmailAddressAndPassword() {
        final String input = "deys@deys.com" + System.lineSeparator() + ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING +
                System.lineSeparator() +
                ConstantStrings.PASSWORD_STRING +
                System.lineSeparator() +
                ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayLoginSuccessfulOnSuccessfulNetworkConnection() throws JsonProcessingException {
        final String input = "deys@deys.com" + System.lineSeparator() + "password"
                + System.lineSeparator() + ConstantStrings.EXIT;
        ViewConstants.setInputStream(new BufferedReader(new StringReader(input)));
        when(networkResponse.payload()).thenReturn(mock(Payload.class));
        when(networkResponse.payload().jsonString()).thenReturn("{\"id\": 1}");
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING +
                System.lineSeparator() +
                ConstantStrings.PASSWORD_STRING +
                System.lineSeparator() +
                ConstantStrings.LOGIN_SUCCESSFUL +
                System.lineSeparator() +
                ConstantStrings.CHAT_MAIN_COMMAND +
                System.lineSeparator() +
                ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayLoginFailedForInvalidEmailAddress() {
        final String input = "deysdeys.com" + System.lineSeparator() + "password"
                + System.lineSeparator() + ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.PASSWORD_STRING + System.lineSeparator()
                + ConstantStrings.LOGIN_FAILED + System.lineSeparator() + ConstantStrings.THANK_YOU
                + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayLoginFailedAndRetry() {
        final String input = "deysdeys.com" + System.lineSeparator() + "password"
                + System.lineSeparator() + "1" + System.lineSeparator() + ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.PASSWORD_STRING + System.lineSeparator()
                + ConstantStrings.LOGIN_FAILED + System.lineSeparator()
                + ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayLoginFailedAndForgotPassword() {
        final String input = "deysdeys.com" + System.lineSeparator() + "password"
                + System.lineSeparator() + "2" + System.lineSeparator() + ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.PASSWORD_STRING + System.lineSeparator()
                + ConstantStrings.LOGIN_FAILED + System.lineSeparator()
                + ConstantStrings.RECOVERY_EMAIL + System.lineSeparator()
                + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayLoginFailedAndGoBack() {
        final String input = "deysdeys.com" + System.lineSeparator() + "password"
                + System.lineSeparator() + "0" + System.lineSeparator() + ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.PASSWORD_STRING + System.lineSeparator()
                + ConstantStrings.LOGIN_FAILED + System.lineSeparator() + ConstantStrings.THANK_YOU
                + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayLoginFailedAndExit() {
        final String input = "deysdeys.com" + System.lineSeparator() + "password"
                + System.lineSeparator() + "*" + System.lineSeparator() + "Y";
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.PASSWORD_STRING + System.lineSeparator()
                + ConstantStrings.LOGIN_FAILED + System.lineSeparator()
                + ConstantStrings.CONFIRM_EXIT_MESSAGE + System.lineSeparator()
                + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayLoginFailedAndDoNotExit() {
        final String input = "deysdeys.com" + System.lineSeparator() + "password"
                + System.lineSeparator() + "*" + System.lineSeparator() + "N"
                + System.lineSeparator() + ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.PASSWORD_STRING + System.lineSeparator()
                + ConstantStrings.LOGIN_FAILED + System.lineSeparator()
                + ConstantStrings.CONFIRM_EXIT_MESSAGE + System.lineSeparator()
                + ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayLoginForInvalidCharacterInputs() {
        final String input = "deysdeys.com" + System.lineSeparator() + "password"
                + System.lineSeparator() + "7" + System.lineSeparator() + ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.EMAIL_ADDRESS_STRING + System.lineSeparator()
                + ConstantStrings.PASSWORD_STRING + System.lineSeparator()
                + ConstantStrings.LOGIN_FAILED + System.lineSeparator()
                + ConstantStrings.INVALID_INPUT_STRING + System.lineSeparator()
                + ConstantStrings.LOGIN_FAILED + System.lineSeparator()
                + ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }
}