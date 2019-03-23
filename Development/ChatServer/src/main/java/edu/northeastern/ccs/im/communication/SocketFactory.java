package edu.northeastern.ccs.im.communication;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/***
 * Returns socket instances.
 */
public class SocketFactory {
    /***
     *
     * @return an instance of socket channel
     * @throws IOException
     */
    public SocketChannel createSocket() throws IOException {
        return SocketChannel.open();
    }
}
