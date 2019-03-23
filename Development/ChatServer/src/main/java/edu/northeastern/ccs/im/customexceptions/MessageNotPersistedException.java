package edu.northeastern.ccs.im.customexceptions;

public class MessageNotPersistedException extends Exception {

    // Constructor that accepts a message
    public MessageNotPersistedException(String message)
    {
        super(message);
    }
}
