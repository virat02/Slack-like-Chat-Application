package edu.northeastern.ccs.im.communication;

public interface NetworkResponse {
    STATUS status();

    enum STATUS {
        SUCCESSFUL, FAILED;
    }
}
