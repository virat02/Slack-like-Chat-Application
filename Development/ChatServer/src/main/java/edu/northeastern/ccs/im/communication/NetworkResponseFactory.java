package edu.northeastern.ccs.im.communication;

/***
 * A factory classes serving implementations of NetworkResponse.
 */
public class NetworkResponseFactory {

    private static NetworkResponse successful = new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL, () -> "{\"id\": 1}");
    private static NetworkResponse failed = new NetworkResponseImpl(NetworkResponse.STATUS.FAILED, () -> "");

    /***
     * Creates a successful Response with empty payload.
     * @return
     */
    public NetworkResponse createSuccessfulResponse() {
        return successful;
    }

    /***
     * Creates a success response with non-empty payload.
     * @param payload
     * @return NetworkResponse
     */
    public NetworkResponse createSuccessfulResponseWithPayload(Payload payload) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.SUCCESSFUL, payload);
    }

    /***
     * Creates a failed response with empty payload
     * @return NetworkResponse
     */
    public NetworkResponse createFailedResponse() {
        return failed;
    }

    /***
     * Creates a failed response with non-empty payload.
     * @param payload
     * @return NetworkResponse
     */
    public NetworkResponse createFailedResponseWithPayload(Payload payload) {
        return new NetworkResponseImpl(NetworkResponse.STATUS.FAILED, payload);
    }
}
