package edu.northeastern.ccs.im.communication;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/***
 * Returns socket instances.
 */
public class SocketFactory {
    /***
     *
     * @return an instance of socket channel, used by the client side code.
     * @throws IOException Thrown by SocketChannel.open()
     */
    public SocketChannel createSocket() throws IOException {
        return SocketChannel.open();
    }
}
