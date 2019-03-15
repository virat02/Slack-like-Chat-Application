package edu.northeastern.ccs.im;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Each instance of this class represents a single transmission by our IM
 * clients.
 * <p>
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public class Message {

    /**
     * The string sent when a field is null.
     */
    private static final String NULL_OUTPUT = "--";

    /**
     * The handle of the message.
     */
    private MessageType msgType;

    /**
     * The first argument used in the message. This will be the sender's identifier.
     */
    @JsonProperty("name")
    private String msgSender;

    /**
     * The second argument used in the message.
     */
    @JsonProperty("text")
    private String msgText;

    @JsonProperty("groupCode")
    private String groupCode;

    /**
     * Create a new message that contains actual IM text. The type of distribution
     * is defined by the handle and we must also set the name of the message sender,
     * message recipient, and the text to send.
     *
     * @param handle  Handle for the type of message being created.
     * @param srcName Name of the individual sending this message
     * @param text    Text of the instant message
     */
    private Message(MessageType handle, String srcName, String text, String groupCode) {
        msgType = handle;
        // Save the properly formatted identifier for the user sending the
        // message.
        msgSender = srcName;
        // Save the text of the message.
        msgText = text;
        // Saves the group to which this message belongs.
        this.groupCode = groupCode;
    }

    /**
     * Instantiates a new Message.
     */
    public Message() {

    }

    /**
     * Create a new message that contains a command sent the server that requires a
     * single argument. This message contains the given handle and the single
     * argument.
     *
     * @param handle  Handle for the type of message being created.
     * @param srcName Argument for the message; at present this is the name used to
     *                log-in to the IM server.
     */
    private Message(MessageType handle, String srcName, String groupCode) {
        this(handle, srcName, null, groupCode);
    }

    /**
     * Create a new message to continue the logout process.
     *
     * @param myName    The name of the client that sent the quit message.
     * @param groupCode the group code
     * @return Instance of Message that specifies the process is logging out.
     */
    public static Message makeQuitMessage(String myName, String groupCode) {
        return new Message(MessageType.QUIT, myName, groupCode);
    }

    /**
     * Create a new message broadcasting an announcement to the world.
     *
     * @param myName    Name of the sender of this very important missive.
     * @param text      Text of the message that will be sent to all users
     * @param groupCode the group code
     * @return Instance of Message that transmits text to all logged in users.
     */
    public static Message makeBroadcastMessage(String myName, String text, String groupCode) {
        return new Message(MessageType.BROADCAST, myName, text, groupCode);
    }

    /**
     * Gets msg type.
     *
     * @return the msg type
     */
    public MessageType getMsgType() {
        return msgType;
    }

    /**
     * Create a new message stating the name with which the user would like to
     * login.
     *
     * @param msgSender Name the user wishes to use as their screen name.
     * @param groupCode The group under which this message has been sent
     * @return Instance of Message that can be sent to the server to try and login.
     */
    protected static Message makeHelloMessage(String msgSender, String groupCode) {
        return new Message(MessageType.HELLO, msgSender, groupCode);
    }

    /**
     * Group code string.
     *
     * @return the string
     */
    public String groupCode() {
        return groupCode;
    }

    /**
     * Given a handle, name and text, return the appropriate message instance or an
     * instance from a subclass of message.
     *
     * @param handle    Handle of the message to be generated.
     * @param srcName   Name of the originator of the message (may be null)
     * @param text      Text sent in this message (may be null)
     * @param groupCode the group code
     * @return Instance of Message (or its subclasses) representing the handle, name, & text.
     */
    public static Message makeMessage(String handle, String srcName, String text, String groupCode) {
        Message result = null;
        if (handle.compareTo(MessageType.QUIT.toString()) == 0) {
            result = makeQuitMessage(srcName, groupCode);
        } else if (handle.compareTo(MessageType.HELLO.toString()) == 0) {
            result = makeSimpleLoginMessage(srcName, groupCode);
        } else if (handle.compareTo(MessageType.BROADCAST.toString()) == 0) {
            result = makeBroadcastMessage(srcName, text, groupCode);
        }
        return result;
    }

    /**
     * Create a new message for the early stages when the user logs in without all
     * the special stuff.
     *
     * @param myName    Name of the user who has just logged in.
     * @param groupCode the group code
     * @return Instance of Message specifying a new friend has just logged in.
     */
    public static Message makeSimpleLoginMessage(String myName, String groupCode) {
        return new Message(MessageType.HELLO, myName, groupCode);
    }

    /**
     * Return the name of the sender of this message.
     *
     * @return String specifying the name of the message originator.
     */
    public String getName() {
        return msgSender;
    }

    /**
     * Return the text of this message.
     *
     * @return String equal to the text sent by this message.
     */
    public String getText() {
        return msgText;
    }

    /**
     * Determine if this message is broadcasting text to everyone.
     *
     * @return True if the message is a broadcast message; false otherwise.
     */
    @JsonIgnore
    public boolean isBroadcastMessage() {
        return (msgType == MessageType.BROADCAST);
    }

    /**
     * Determine if this message is sent by a new client to log-in to the server.
     *
     * @return True if the message is an initialization message; false otherwise
     */
    @JsonIgnore
    public boolean isInitialization() {
        return (msgType == MessageType.HELLO);
    }

    /**
     * Determine if this message is a message signing off from the IM server.
     *
     * @return True if the message is sent when signing off; false otherwise
     */
    @JsonIgnore
    public boolean terminate() {
        return (msgType == MessageType.QUIT);
    }

    /**
     * Representation of this message as a String. This begins with the message
     * handle and then contains the length (as an integer) and the value of the next
     * two arguments.
     *
     * @return Representation of this message as a String.
     */
    @Override
    public String toString() {
        String result = msgType.toString();
        if (msgSender != null) {
            result += " " + msgSender.length() + " " + msgSender;
        } else {
            result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
        }
        if (msgText != null) {
            result += " " + msgText.length() + " " + msgText;
        } else {
            result += " " + NULL_OUTPUT.length() + " " + NULL_OUTPUT;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message))
            return false;

        Message compareObj = (Message) obj;
        return (compareObj.msgSender.equals(this.msgSender))
                && compareObj.groupCode.equals(this.groupCode)
                && compareObj.msgType.equals(this.msgType)
                && compareObj.msgText.equals(this.msgText);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
