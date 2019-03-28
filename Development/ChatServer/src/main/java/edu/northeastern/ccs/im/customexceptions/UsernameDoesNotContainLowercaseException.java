package edu.northeastern.ccs.im.customexceptions;

public class UsernameDoesNotContainLowercaseException extends Exception {
    // Constructor that accepts a message
    public UsernameDoesNotContainLowercaseException(String message)
    {
        super(message);
    }
}
