package edu.northeastern.ccs.im.communication;

public class NetworkResponseFactory {

    private static NetworkResponse successful = new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL, null);
    private static NetworkResponse failed = new NetworkResponseImpl(NetworkResponse.STATUS.FAILED, null);

    public NetworkResponse createSuccessfulResponse() {
        return successful;
    }

    public NetworkResponse createFailedResponse() {
        return failed;
    }
}
