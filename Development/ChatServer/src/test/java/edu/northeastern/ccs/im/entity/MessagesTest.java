package edu.northeastern.ccs.im.entity;

import edu.northeastern.ccs.im.service.MessageService;
import edu.northeastern.ccs.im.userGroup.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessagesTest {

    private Message m1;
    private Message m2;
    private Message m3;

    private User u1 = new User();
    private User u2 = new User();
    private User u3 = new User();

    private IGroup privateGroup = new PrivateGroup();
    private IGroup group = new Group();

    private static final String U1_MESSAGE = "Hi, I'm Virat!";
    private static final String U2_MESSAGE = "Hi, I'm Sangeetha!";
    private static final String U3_MESSAGE = "Hi, I'm Jerry!";

    private static final String DELETED_MESSAGE_STRING = "Message deleted? : false";
    private static final String ID_2 = "Id: 2\n";
    private static final String ID_3 = "Id: 3\n";
    private static final String SENDER = "Sender: ";
    private static final String RECEIVER = "Receiver: ";
    private static final String MESSAGE_TIMESTAMP_STRING_LITERAL = "Message Timestamp: ";

    /**
     * Sets up the users information
     */
    @Before
    public void setUp() {
        Date d = new Date();

        u1.setId(1);
        u2.setId(2);
        u3.setId(3);

        m1 = new Message();
        m1.setId(1);
        m1.setMessage(U1_MESSAGE);
        m1.setTimestamp(d);
        m1.setExpiration(30);
        m1.setSender(u1);
        m1.setReceiver(privateGroup);
        m1.setDeleted(false);

        m2 = new Message();
        m2.setId(2);
        m2.setMessage(U2_MESSAGE);
        m2.setTimestamp(d);
        m2.setExpiration(20);
        m2.setSender(u2);
        m2.setReceiver(group);
        m2.setDeleted(false);

        m3 = new Message();
        m3.setId(1);
        m3.setMessage(U3_MESSAGE);
        m3.setTimestamp(d);
        m3.setExpiration(15);
        m3.setSender(u3);
        m3.setReceiver(group);
        m3.setDeleted(false);
    }

    /**
     * Test the create message and send message method
     */
    @Test
    public void testCreateAndSendMessage() {
        Date d1 = new Date();

        Message msg1 = new Message();
        msg1.setId(1);
        MessageService service = new MessageService(msg1);

        msg1 = service.createMessage(U1_MESSAGE, d1, 30);
        msg1 = service.sendMessage(msg1, u1, privateGroup, false);

        assertEquals(m1.toString(), msg1.toString());
    }

    /**
     * Test the create message and send message method for invalid input
     */
    @Test(expected = NullPointerException.class)
    public void testCreateAndSendMessageInvalidInput() {
        MessageService service = new MessageService(m1);
        Date d1 = new Date();
        service.createMessage("", d1,30); }

    /**
     * Test the update message method
     */
    @Test
    public void testUpdateMessage() {
        MessageService service = new MessageService(m2);
        Date d = new Date();

        Message updatedMessage = new Message();
        updatedMessage.setId(m2.getId());
        updatedMessage.setTimestamp(m2.getTimestamp());
        updatedMessage.setExpiration(30);
        updatedMessage.setMessage("Updated Sangeetha's message");
        updatedMessage.setDeleted(m2.isDeleted());
        updatedMessage.setSender(m2.getSender());
        updatedMessage.setReceiver(m2.getReceiver());

        service.updateMessage(updatedMessage);

        String expected = ID_2 +
                "Message: Updated Sangeetha's message\n" +
                MESSAGE_TIMESTAMP_STRING_LITERAL +d+
                "\nExpiry time: 30\n" +
                SENDER+ m2.getSender()+"\n"+
                RECEIVER+ m2.getReceiver()+"\n"+
                DELETED_MESSAGE_STRING;

        assertEquals(expected, m2.toString());
    }

    /**
     * Test the update message method with invalid timestamp input
     */
    @Test
    public void testUpdateMessageInvalidTimestampInput() {
        MessageService service = new MessageService(m2);
        Date d = new Date();

        Message updatedMessage = new Message();
        updatedMessage.setId(m2.getId());
        updatedMessage.setTimestamp(m2.getTimestamp());
        updatedMessage.setExpiration(0);
        updatedMessage.setMessage("Updated Sangeetha's message");
        updatedMessage.setDeleted(m2.isDeleted());
        updatedMessage.setSender(m2.getSender());
        updatedMessage.setReceiver(m2.getReceiver());

        service.updateMessage(updatedMessage);

        String expected = ID_2 +
                "Message: Updated Sangeetha's message\n" +
                MESSAGE_TIMESTAMP_STRING_LITERAL +d+
                "\nExpiry time: 20\n" +
                SENDER+ m2.getSender()+"\n"+
                RECEIVER+ m2.getReceiver()+"\n"+
                DELETED_MESSAGE_STRING;

        assertEquals(expected, m2.toString());
    }

    /**
     * Test the update message method with invalid message input
     */
    @Test
    public void testUpdateMessageInvalidMessageInput() {
        Message updatedMessage = new Message();
        updatedMessage.setMessage("");
    }

    /**
     * Test the delete message method
     */
    @Test
    public void testDeleteMessage() {
        MessageService service = new MessageService(m2);
        Date d = new Date();
        service.deleteMessage();

        String expected = ID_2 +
                "Message: Hi, I'm Sangeetha!\n" +
                MESSAGE_TIMESTAMP_STRING_LITERAL +d+
                "\nExpiry time: 20\n" +
                SENDER + m2.getSender()+"\n"+
                RECEIVER + m2.getReceiver()+"\n"+
                "Message deleted? : true";

        assertEquals(expected, m2.toString());
    }

    /**
     * Test the isDeleted method
     */
    @Test
    public void testIsDeleted() {
        MessageService service = new MessageService(m3);
        service.deleteMessage();

        assertTrue(m3.isDeleted());

    }

    /**
     * Test getReceiver method
     */
    @Test
    public void testGetReceiver() {
        IUserGroup u = m2.getReceiver();

        assertEquals(group, u);

    }
}
