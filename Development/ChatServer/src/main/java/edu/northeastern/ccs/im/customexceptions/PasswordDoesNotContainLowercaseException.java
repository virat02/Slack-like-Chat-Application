package edu.northeastern.ccs.im.customexceptions;

public class PasswordDoesNotContainLowercaseException extends Exception {
    // Constructor that accepts a message
    public PasswordDoesNotContainLowercaseException(String message)
    {
        super(message);
    }
}
