package edu.northeastern.ccs.im.communication;

/***
 * A class used for deserialization purposes by the jackson library
 */
public class NetworkRequestImpl implements NetworkRequest {
    private Payload payload;
    private NetworkRequestType networkRequestType;

    /***
     * Default constructor
     */
    public NetworkRequestImpl() {

    }

    /***
     * Constructor
     * @param networkRequestType The type of the networkRequest
     * @param payload The Payload
     */
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
