package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PayloadImpl implements Payload {
    private String jsonString;
    public PayloadImpl()    {

    }
    @JsonCreator
    public PayloadImpl(String jsonString)    {
        this.jsonString = jsonString;
    }
    @Override
    public String jsonString() throws JsonProcessingException {
        return jsonString;
    }
}
