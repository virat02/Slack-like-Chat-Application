package edu.northeastern.ccs.im.customexceptions;

public class MessageNotFoundException extends Exception {

    // Constructor that accepts a message
    public MessageNotFoundException(String message)
    {
        super(message);
    }
}
