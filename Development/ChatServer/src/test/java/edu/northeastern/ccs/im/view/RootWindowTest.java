package edu.northeastern.ccs.im.view;

import edu.northeastern.ccs.im.communication.ClientConnectionFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RootWindowTest extends AbstractWindowTest {
  @Test
  public void testInitialDisplayView() {
    initializeInputStreamWithInput("");
    RootWindow rootWindow = new RootWindow(null, new ClientConnectionFactory());
    rootWindow.runWindow();
    assertEquals("1 - Login\n2 - Sign Up\n* - QUIT\n", getOutputStreamContent());
  }
}