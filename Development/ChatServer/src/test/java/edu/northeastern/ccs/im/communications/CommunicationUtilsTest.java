package edu.northeastern.ccs.im.communications;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.northeastern.ccs.im.communication.CommunicationUtils;
import org.junit.Test;

public class CommunicationUtilsTest {
    @Test
    public void testMultipleCallsShouldReturnSameObjectMapperInstance() {
        ObjectMapper objectMapper1 = CommunicationUtils.getObjectMapper();
        ObjectMapper objectMapper2 = CommunicationUtils.getObjectMapper();
        ObjectMapper objectMapper3 = CommunicationUtils.getObjectMapper();
        assert objectMapper1 == objectMapper2 && objectMapper2 == objectMapper3;
    }
}
