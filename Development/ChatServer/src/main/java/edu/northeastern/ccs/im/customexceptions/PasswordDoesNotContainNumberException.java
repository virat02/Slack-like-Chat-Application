package edu.northeastern.ccs.im.customexceptions;

public class PasswordDoesNotContainNumberException extends Exception {
    // Constructor that accepts a message
    public PasswordDoesNotContainNumberException(String message)
    {
        super(message);
    }
}
