package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonCreator;

public class PayloadImpl implements Payload {
    private String jsonString;
    public PayloadImpl()    {

    }
    @JsonCreator
    public PayloadImpl(String jsonString)    {
        this.jsonString = jsonString;
    }
    @Override
    public String jsonString() {
        return jsonString;
    }
}
