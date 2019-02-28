package edu.northeastern.ccs.im.communication;

public class NetworkResponseFactory {

    private static NetworkResponse successful = () -> NetworkResponse.STATUS.SUCCESSFUL;
    private static NetworkResponse failed = () -> NetworkResponse.STATUS.FAILED;

    public NetworkResponse createSuccessfulResponse() {
        return successful;
    }

    public NetworkResponse createFailedResponse() {
        return failed;
    }
}
