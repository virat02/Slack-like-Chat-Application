package edu.northeastern.ccs.im.communication;

public class NetworkResponseFactory {

    private static NetworkResponse successful = new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL, () -> "{\"id\": 1}");
    private static NetworkResponse failed = new NetworkResponseImpl(NetworkResponse.STATUS.FAILED, () -> "");

    public NetworkResponse createSuccessfulResponse() {
        return successful;
    }

    public NetworkResponse createSuccessfulResponseWithPayload(Payload payload) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL, payload);
    }

    public NetworkResponse createFailedResponse() {
        return failed;
    }

    public NetworkResponse createFailedResponseWithPayload(Payload payload) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED, payload);
    }
}
