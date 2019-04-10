package edu.northeastern.ccs.im.customexceptions;

public class FirstTimeUserLoggedInException extends Exception {
    public FirstTimeUserLoggedInException(String message)   {
        super(message);
    }
}
