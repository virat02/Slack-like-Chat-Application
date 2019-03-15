package edu.northeastern.ccs.im.communications;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.northeastern.ccs.im.communication.Payload;
import edu.northeastern.ccs.im.communication.PayloadImpl;
import org.junit.Test;

public class PayloadImplTests {
    @Test
    public void shouldSetThePayload() throws JsonProcessingException {
        final String json = "id: 1";
        Payload payload = new PayloadImpl(json);
        assert json.equals(payload.jsonString());
    }
}
