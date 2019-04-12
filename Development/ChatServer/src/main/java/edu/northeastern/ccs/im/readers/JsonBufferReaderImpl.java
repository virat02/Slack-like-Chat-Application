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

    /**
     * Reading the bytes
     * @return long byte representation.
     */
    @Override
    public long bytesRead() {
        return bytesRead;
    }

    /**
     * The message list.
     * @param buffer a ByteBuffer
     * @return the list of messages
     * @throws JsonProcessingException if there is a problem processing
     */
    @Override
    public List<Message> messageList(ByteBuffer buffer) throws JsonProcessingException {
        List<Message> messages = new ArrayList<>();
        ByteBuffer byteBuffer = charDecoder(buffer);
        try (JsonParser jsonParser = CommunicationUtils.getObjectMapper().getFactory().createParser(byteBuffer.array())) {
            readFromJsonParser(jsonParser, messages);
        } catch (IOException e) {
            ChatLogger.info(e.getMessage());
        }

        for (Message m : messages)
            bytesRead += CommunicationUtils.getObjectMapper().writeValueAsBytes(m).length;

        return Collections.unmodifiableList(messages);
    }

    /***
     * Reads the messages from json string using json parser on per token basis.
     * @param jsonParser the jsonParser
     * @param messages the messages
     * @throws IOException if there are not messages.
     */
    private void readFromJsonParser(JsonParser jsonParser, List<Message> messages) throws IOException {
        String srcName = null;
        String text = null;
        String groupCode = null;
        String msgHandle = null;
        while (!jsonParser.isClosed()) {
            JsonToken jsonToken = jsonParser.nextToken();
            if (jsonToken == null)
                continue;
            if (jsonToken.equals(JsonToken.START_OBJECT)) {
                srcName = text = groupCode = msgHandle = "";
            } else if (jsonToken.equals(JsonToken.FIELD_NAME)) {
                String fieldName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                switch (fieldName) {
                    case "name":
                        srcName = jsonParser.getText();
                        break;
                    case "text":
                        text = jsonParser.getText();
                        break;
                    case "msgType":
                        msgHandle = MessageType.valueOf(jsonParser.getText()).toString();
                        break;
                    case "groupCode":
                        groupCode = jsonParser.getText();
                        break;
                    default:
                        ChatLogger.warning("Illegal property name found");
                }
            } else if (jsonToken.equals(JsonToken.END_OBJECT)) {
                Message message = Message.makeMessage(msgHandle, srcName, text, groupCode);
                messages.add(message);
            }
        }
    }

    /***
     * Returns an ascii encoded buffer representation of the characters.
     * @param byteBuffer Stores the byte representation of messages.
     * @return Returns an ascii encoded buffer representation of the characters.
     */
    private ByteBuffer charDecoder(ByteBuffer byteBuffer) {
        Charset charset = Charset.forName(CHARSET_NAME);
        CharsetDecoder decoder = charset.newDecoder();
        // Convert the buffer to a format that we can actually use.
        CharBuffer charBuffer;
        try {
            charBuffer = decoder.decode(byteBuffer);
        } catch (CharacterCodingException e) {
            ChatLogger.error(e.getMessage());
            ChatLogger.info("Returning empty Buffer!!!!");
            return ByteBuffer.wrap(new byte[]{});
        }
        return charset.encode(charBuffer.toString().trim());
    }
}