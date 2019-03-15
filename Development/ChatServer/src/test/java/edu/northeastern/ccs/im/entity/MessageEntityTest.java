package edu.northeastern.ccs.im.entity;

import edu.northeastern.ccs.im.user_group.Group;
import edu.northeastern.ccs.im.user_group.Message;
import edu.northeastern.ccs.im.user_group.User;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

/**
 * Test cases for the Message Class
 */
public class MessageEntityTest {

    /**
     * Test a constructor of Message class
     */
    @Test
    public void testConstructor1() {
        Message m = new Message(1,"hi", new Date(), 10, new User(), new Group(), false);

        String expected = "Message: hi\n" +
                "Message Timestamp: " + m.getTimestamp() +
                "\nExpiry time: 10\n" +
                "Sender: " + m.getSender()+
                "\nReceiver: " + m.getReceiver()+
                "\nMessage deleted? : false";

        assertEquals(expected, m.toString());
    }

    /**
     * Test a constructor of Message class
     */
    @Test
    public void testConstructor2() {
        Message m = new Message(2, "hellooo", new Date(), 20);

        String expected = "Message: hellooo\n" +
                "Message Timestamp: " + m.getTimestamp()+
                "\nExpiry time: 20\n" +
                "Sender: " + m.getSender()+
                "\nReceiver: " + m.getReceiver()+
                "\nMessage deleted? : false";

        assertEquals(expected, m.toString());
    }
}
