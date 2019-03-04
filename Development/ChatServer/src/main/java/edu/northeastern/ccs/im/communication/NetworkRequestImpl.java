package edu.northeastern.ccs.im.communication;

public class NetworkRequestImpl implements NetworkRequest {
    private Payload payload;
    private NetworkRequestType networkRequestType;

    public NetworkRequestImpl(NetworkRequestType networkRequestType, Payload payload) {
        this.networkRequestType = networkRequestType;
        this.payload = payload;
    }

    @Override
    public NetworkRequestType networkRequestType() {
        return networkRequestType;
    }

    @Override
    public Payload payload() {
        return payload;
    }
}
