package edu.northeastern.ccs.im.communication;

import java.io.IOException;

public interface ClientConnection {
    void connect() throws IOException;
    void close() throws IOException;
}
