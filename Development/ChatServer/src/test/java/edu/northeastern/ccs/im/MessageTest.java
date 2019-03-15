package edu.northeastern.ccs.im;

import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Junit test suite for Message and MessageType class.
 *
 * @author virat02
 */
public class MessageTest {

    private Message quit;
    private Message helloMsg;
    private Message loginMsg;
    private Message broadcastMsg;
    private Message broadcastEmptyMsg;

    private Message simpleLoginMessage;
    private Message quitMessage;
    private Message broadcastMessage;

    private Message invalidHandleMessage;

    /**
     * Creating object instances of Message for testing.
     */
    @Before
    public void setUp() {
        //Make a quit message for a user
        quit = Message.makeQuitMessage("Virat", null);

        //Make a user send a hello message
        helloMsg = Message.makeHelloMessage("virat02", null);

        //Make a simple login message
        loginMsg = Message.makeSimpleLoginMessage("Successfully logged in as Virat", "");

        //Make a broadcast message
        broadcastMsg = Message.makeBroadcastMessage("Sangeetha", "This is Sangeetha's broadcast message.", "");

        //Broadcast a null message
        broadcastEmptyMsg = Message.makeBroadcastMessage("Jerry", null, "");

        // Make Hello, Quit and Broadcast messages using the makeMessage method
        simpleLoginMessage = Message.makeMessage("HLO", "Client1", "Hello Client1", "");
        quitMessage = Message.makeMessage("BYE", "Client2", "Client2 quit.", "");
        broadcastMessage = Message.makeMessage("BCT", "Client3", "Client3 made a broadcast message.", "");

        //Make an Invalid handle message using the makeMessage method
        invalidHandleMessage = Message.makeMessage("ABC", "Client4", "Client4 has an invalid handle", "");
    }

    /**
     * Tests that the getName function returns the correct sender for each message.
     */
    @Test
    public void getName() {
        assertEquals("Virat", quit.getName());
        assertEquals("Sangeetha", broadcastMsg.getName());
        assertEquals("Successfully logged in as Virat", loginMsg.getName());
        assertEquals("virat02", helloMsg.getName());
        assertEquals("Jerry", broadcastEmptyMsg.getName());

        assertEquals("Client1", simpleLoginMessage.getName());
        assertEquals("Client2", quitMessage.getName());
        assertEquals("Client3", broadcastMessage.getName());
    }

    /**
     * Tests that the getText function returns the correct message text for each message.
     */
    @Test
    public void getText() {
        assertNull(quit.getText());
        assertEquals("This is Sangeetha's broadcast message.", broadcastMsg.getText());
        assertEquals("virat02", helloMsg.getName());
        assertNull(broadcastEmptyMsg.getText());

        assertNull(simpleLoginMessage.getText());
        assertNull(quitMessage.getText());
        assertEquals("Client3 made a broadcast message.", broadcastMessage.getText());
    }

    /**
     * Tests that the terminate function returns the correct boolean for each message type.
     * Returns true iff the message is of type "BYE"
     */
    @Test
    public void terminate() {
        assertTrue(quit.terminate());
        assertFalse(loginMsg.terminate());
        assertNotEquals(true, broadcastEmptyMsg.terminate());
        assertNotEquals(true, helloMsg.terminate());
        assertFalse(broadcastMsg.terminate());

        assertFalse(simpleLoginMessage.terminate());
        assertTrue(quitMessage.terminate());
        assertFalse(broadcastMessage.terminate());
    }

    /**
     * Tests that the isBroadcastMessage function returns the correct boolean for each message type.
     * Returns true iff the message is of type "BCT"
     */
    @Test
    public void isBroadcastMessage() {
        assertFalse(quit.isBroadcastMessage());
        assertFalse(loginMsg.isBroadcastMessage());
        assertNotEquals(false, broadcastEmptyMsg.isBroadcastMessage());
        assertNotEquals(true, helloMsg.isBroadcastMessage());
        assertTrue(broadcastMsg.isBroadcastMessage());

        assertFalse(simpleLoginMessage.isBroadcastMessage());
        assertFalse(quitMessage.isBroadcastMessage());
        assertTrue(broadcastMessage.isBroadcastMessage());
    }

    /**
     * Tests that the isInitialization function returns the correct boolean for each message type.
     * Returns true iff the message is of type "HLO"
     */
    @Test
    public void isInitialization() {
        assertFalse(quit.isInitialization());
        assertTrue(loginMsg.isInitialization());
        assertNotEquals(true, broadcastEmptyMsg.isInitialization());
        assertNotEquals(false, helloMsg.isInitialization());
        assertFalse(broadcastMsg.isInitialization());

        assertTrue(simpleLoginMessage.isInitialization());
        assertFalse(quitMessage.isInitialization());
        assertFalse(broadcastMessage.isInitialization());
    }

    /**
     * Tests that the toString function returns the correct representation for each message.
     */
    @Test
    public void toStringFunction() {

        assertEquals("BYE 5 Virat 2 --", quit.toString());
        assertEquals("BCT 9 Sangeetha 38 This is Sangeetha's broadcast message.", broadcastMsg.toString());
        assertEquals("HLO 31 Successfully logged in as Virat 2 --", loginMsg.toString());
        assertEquals("HLO 7 virat02 2 --", helloMsg.toString());
        assertEquals("BCT 5 Jerry 2 --", broadcastEmptyMsg.toString());

        assertEquals("HLO 7 Client1 2 --", simpleLoginMessage.toString());
        assertEquals("BYE 7 Client2 2 --", quitMessage.toString());
        assertEquals("BCT 7 Client3 33 Client3 made a broadcast message.", broadcastMessage.toString());
    }

    /**
     * Test for values that are null
     */
    @Test
    public void testNull() {
        assertNull(invalidHandleMessage);
    }
}