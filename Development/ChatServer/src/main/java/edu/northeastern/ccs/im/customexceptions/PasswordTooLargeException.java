package edu.northeastern.ccs.im.customexceptions;

public class PasswordTooLargeException extends Exception{
    // Constructor that accepts a message
    public PasswordTooLargeException(String message)
    {
        super(message);
    }
}
