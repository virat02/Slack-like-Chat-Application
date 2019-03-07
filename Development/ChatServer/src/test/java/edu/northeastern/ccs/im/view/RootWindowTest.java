package edu.northeastern.ccs.im.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RootWindowTest extends AbstractWindowTest {

  @Test
  public void testInitialDisplayView() {
    initializeInputStreamWithInput("* Y");
    RootWindow rootWindow = new RootWindow(null);
    rootWindow.runWindow();
    System.out.println(getOutputStreamContent());
  }
}