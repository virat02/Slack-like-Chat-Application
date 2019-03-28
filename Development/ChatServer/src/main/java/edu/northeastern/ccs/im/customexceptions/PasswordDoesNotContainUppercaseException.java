package edu.northeastern.ccs.im.customexceptions;

public class PasswordDoesNotContainUppercaseException extends Exception {
    // Constructor that accepts a message
    public PasswordDoesNotContainUppercaseException(String message)
    {
        super(message);
    }
}
