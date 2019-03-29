package edu.northeastern.ccs.im.customexceptions;

public class UsernameDoesNotContainNumberException extends Exception {
    // Constructor that accepts a message
    public UsernameDoesNotContainNumberException(String message)
    {
        super(message);
    }
}
