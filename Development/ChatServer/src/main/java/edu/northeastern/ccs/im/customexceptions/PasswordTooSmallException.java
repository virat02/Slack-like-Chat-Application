package edu.northeastern.ccs.im.customexceptions;

public class PasswordTooSmallException extends Exception {
    // Constructor that accepts a message
    public PasswordTooSmallException(String message)
    {
        super(message);
    }
}
