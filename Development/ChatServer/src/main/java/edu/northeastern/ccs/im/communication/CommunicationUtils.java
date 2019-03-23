package edu.northeastern.ccs.im.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectWriter;

import edu.northeastern.ccs.im.ChatLogger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

/***
 * A Utility class for obtaining an instance of jackson object mapper.
 *
 */
public class CommunicationUtils {
    private static final int MAXIMUM_TRIES_SENDING = 100;
    private static final int BUFFER_SIZE = 64 * 1024;

    private CommunicationUtils() {

    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    /***
     * Reuse the instance of objectMapper from jackson. Documentation states that it threadsafe.
     * @return the instance of objectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> String toJsonArray(List<T> t) {
        ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return objWriter.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            ChatLogger.error(e.getMessage());
        }
        return "{}";
    }

    /***
     * This method writes to the channel and returns true or false
     * depending upon the success of message written.
     *
     * The function has been referred from Network Connection.
     *
     * The blocking nature of this operation is depending upon the
     * configuration of socket channel, Consumers should handle the result of
     * this operation accordingly.
     *
     * @param channel The socket channel to which this object has to be written
     * @param object The Object which has to be written to socket
     * @return Boolean value indicating the success of the message.
     */
    public static boolean writeToChannel(SocketChannel channel, Object object) {
        byte[] encoded = new byte[]{};
        boolean result = true;
        try {
            encoded = CommunicationUtils.getObjectMapper().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            ChatLogger.error(e.getMessage());
            result = false;
            return result;
        }
        ByteBuffer wrapper = ByteBuffer.wrap(encoded);
        int bytesWritten = 0;
        int attemptsRemaining = MAXIMUM_TRIES_SENDING;
        while (result && wrapper.hasRemaining() && (attemptsRemaining > 0)) {
            try {
                attemptsRemaining--;
                bytesWritten += channel.write(wrapper);
            } catch (IOException e) {
                // Show that this was unsuccessful
                result = false;
            }
        }
        // Check to see if we were successful in our attempt to write the message
        if (result && wrapper.hasRemaining()) {
            ChatLogger.warning("WARNING: Sent only " + bytesWritten + " out of " + wrapper.limit()
                    + " bytes -- dropping this user.");
            result = false;
        }
        return result;
    }

    /***
     * Parse the network request from socket channel.
     * Assumptions: SocketChannel is blocking.
     * @param socketChannel The channel from which request will be read.
     * @return NetworkRequest read from channel
     */
    public static NetworkRequest networkRequestReadFromSocket(SocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            socketChannel.read(byteBuffer);
            return CommunicationUtils.getObjectMapper().readValue(byteBuffer.array(), NetworkRequestImpl.class);
        } catch (IOException e) {
            ChatLogger.error(e.getMessage());
        }

        return null;
    }

    /**
     * Parses the object and creates a Json string.
     *
     * @param object being parsed into a Json string
     * @return Json String representation of the object.
     */
    public static String toJson(Object object) {
        ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return objWriter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            ChatLogger.error("Error processing object during conversion to json");
            ChatLogger.warning("Returning empty json string");
        }
        return "{}";
    }
}