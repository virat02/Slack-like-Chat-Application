package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommunicationUtils {
    private CommunicationUtils() {

    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
