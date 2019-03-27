package edu.northeastern.ccs.im.customexceptions;

public class UserNotPresentInTheGroup extends Exception {

    // Constructor that accepts a message
    public UserNotPresentInTheGroup(String message)
    {
        super(message);
    }
}
