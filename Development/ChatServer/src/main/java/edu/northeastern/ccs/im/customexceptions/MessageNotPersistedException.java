package edu.northeastern.ccs.im.customexceptions;

/**
 * Exception thrown when JPA is unable to persist a message
 */
public class MessageNotPersistedException extends Exception {

    /**
     * Constructor for MessageNotPersistedException
     * @param message
     */
    public MessageNotPersistedException(String message)
    {
        super(message);
    }
}
