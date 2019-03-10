package edu.northeastern.ccs.im.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.ClientConnection;
import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import edu.northeastern.ccs.im.communication.NetworkResponse;
import edu.northeastern.ccs.im.communication.Payload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.nio.ch.Net;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginWindowTest extends AbstractWindowTest {

    private AbstractTerminalWindow loginWindow;
    private AbstractTerminalWindow callerWindow;
    private ClientConnectionFactory clientConnectionFactory;
    private ClientConnection clientConnection;
    private NetworkResponse networkResponse;
    @Before
    public void setup() {
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
        Assert.assertEquals(ConstantStrings.kEmailAddressString +
                System.lineSeparator() +
                ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }

    @Test
    public void shouldDisplayEnterEmailAddressAndPassword() {
        final String input = "deys@deys.com" + System.lineSeparator() + ConstantStrings.EXIT;
        initializeInputStreamWithInput(input);
        loginWindow.runWindow();
        Assert.assertEquals(ConstantStrings.kEmailAddressString +
                System.lineSeparator() +
                ConstantStrings.kPasswordString +
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
        Assert.assertEquals(ConstantStrings.kEmailAddressString +
                System.lineSeparator() +
                ConstantStrings.kPasswordString +
                System.lineSeparator() +
                ConstantStrings.kLoginSuccessful +
                System.lineSeparator() +
                ConstantStrings.chatMainCommand +
                System.lineSeparator() +
                ConstantStrings.THANK_YOU + System.lineSeparator(), getOutputStreamContent());
    }
}