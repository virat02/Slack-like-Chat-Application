package edu.northeastern.ccs.im.customexceptions;

public class UsernameDoesNotContainUppercaseException extends Exception {
    // Constructor that accepts a message
    public UsernameDoesNotContainUppercaseException(String message)
    {
        super(message);
    }
}
