package edu.northeastern.ccs.im.communication;

public class NetworkResponseImpl implements NetworkResponse {
    private STATUS status;
    private Payload payload;

    /***
     * Default constructor. Used by jackson for serialization/deserialization.
     */
    public NetworkResponseImpl()    {
    }
    public NetworkResponseImpl(STATUS status, Payload payload)   {
        this.status = status;
        this.payload = payload;
    }
    @Override
    public STATUS status() {
        return status;
    }

    @Override
    public Payload payload() {
        return payload;
    }
}
