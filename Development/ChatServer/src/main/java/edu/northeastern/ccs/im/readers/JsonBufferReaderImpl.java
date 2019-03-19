package edu.northeastern.ccs.im.readers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.communication.CommunicationUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonBufferReaderImpl implements JsonBufferReader {
    private static final String CHARSET_NAME = "us-ascii";
    private long bytesRead;

    @Override
    public long bytesRead() {
        return bytesRead;
    }

    @Override
    public List<Message> messageList(ByteBuffer buffer) throws JsonProcessingException {
        List<Message> messages = new ArrayList<>();
        ByteBuffer byteBuffer = charDecoder(buffer);
        try (JsonParser jsonParser = CommunicationUtils.getObjectMapper().getFactory().createParser(byteBuffer.array())) {
            String srcName = null;
            String text = null;
            String groupCode = null;
            String msgHandle = null;
            while (!jsonParser.isClosed()) {
                JsonToken jsonToken = jsonParser.nextToken();
                if (jsonToken == null)
                    continue;
                if (jsonToken.equals(JsonToken.START_OBJECT)) {
                    srcName = "";
                    text = "";
                    groupCode = "";
                    msgHandle = "";
                } else if (jsonToken.equals(JsonToken.FIELD_NAME)) {
                    String fieldName = jsonParser.getCurrentName();
                    switch (fieldName) {
                        case "name":
                            jsonParser.nextToken();
                            srcName = jsonParser.getText();
                            break;
                        case "text":
                            jsonParser.nextToken();
                            text = jsonParser.getText();
                            break;
                        case "msgType":
                            jsonParser.nextToken();
                            msgHandle = MessageType.valueOf(jsonParser.getText()).toString();
                            break;
                        case "groupCode":
                            jsonParser.nextToken();
                            groupCode = jsonParser.getText();
                            break;
                        default:
                            jsonParser.nextToken();
                    }
                } else if (jsonToken.equals(JsonToken.END_OBJECT)) {
                    Message message = Message.makeMessage(msgHandle, srcName, text, groupCode);
                    messages.add(message);
                }
            }
        } catch (IOException e) {
            ChatLogger.info(e.getMessage());
        }

        for (Message m : messages)
            bytesRead += CommunicationUtils.getObjectMapper().writeValueAsBytes(m).length;

        return Collections.unmodifiableList(messages);
    }

    private ByteBuffer charDecoder(ByteBuffer byteBuffer) {
        Charset charset = Charset.forName(CHARSET_NAME);
        CharsetDecoder decoder = charset.newDecoder();
        // Convert the buffer to a format that we can actually use.
        CharBuffer charBuffer = null;
        try {
            charBuffer = decoder.decode(byteBuffer);
        } catch (CharacterCodingException e) {
            ChatLogger.error(e.getMessage());
            ChatLogger.info("Returning empty Buffer!!!!");
            return ByteBuffer.wrap(new byte[]{});
        }
        return charset.encode(charBuffer);
    }
}