package edu.northeastern.ccs.im.readers;

import edu.northeastern.ccs.im.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/***
 * A Reader class to read the Message transmitted by socket.
 */
public interface JsonBufferReader {
    /***
     * Returns the number of bytes from which message has been successfully
     * extracted from the buffer.
     * Returns 0 by default if this method is invoked
     * before messageList()
     * @return the bytes read
     */
    long bytesRead();

    /***
     * Returns a list of Messages read from the buffer
     * @param buffer
     * @return A list returning the list of messages.
     * @throws IOException
     */
    List<Message> messageList(ByteBuffer buffer) throws IOException;
}
