package edu.northeastern.ccs.im.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.communication.CommunicationUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class JsonBufferReader {
    private long bytesRead;

    public long bytesRead() {
        return bytesRead;
    }

    public List<Message> messageList(ByteBuffer buffer) throws IOException {
        List<Message> messages = new ArrayList<>();
        JsonParser jsonParser = new JsonFactory().createParser(buffer.array());
        String srcName = null;
        String text = null;
        while (!jsonParser.isClosed()) {
            JsonToken jsonToken = jsonParser.nextToken();
            if (jsonToken == null)
                continue;

            if (jsonToken.equals(JsonToken.START_OBJECT)) {
                srcName = "";
                text = "";
            } else if (jsonToken.equals(JsonToken.FIELD_NAME)) {
                String fieldName = jsonParser.getCurrentName();
                if (fieldName.equals("name")) {
                    jsonParser.nextToken();
                    srcName = jsonParser.getText();
                } else if (fieldName.equals("text")) {
                    jsonParser.nextToken();
                    text = jsonParser.getText();
                } else if (fieldName.equals("msgType")) {
                    jsonParser.nextToken();
                }
            } else if (jsonToken.equals(JsonToken.END_OBJECT)) {
                Message message = Message.makeBroadcastMessage(srcName, text);
                messages.add(message);
            }
        }

        for (Message m : messages)
            bytesRead += CommunicationUtils.getObjectMapper().writeValueAsBytes(m).length;
        return messages;
    }
}
