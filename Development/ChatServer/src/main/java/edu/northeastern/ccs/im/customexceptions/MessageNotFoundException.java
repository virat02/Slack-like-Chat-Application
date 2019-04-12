package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when Message is not found
 */
public class MessageNotFoundException extends Exception {

    /**
     * Constructor for MessageNotFoundException
     * @param message the message attached to the exception.
     */
    public MessageNotFoundException(String message)
    {
        super(message);
    }
}
