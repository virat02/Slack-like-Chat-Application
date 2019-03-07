package edu.northeastern.ccs.im.view;

import org.junit.After;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;

public abstract class AbstractWindowTest {

  private PrintStream printStream;
  private ByteArrayOutputStream outputContent;

  private BufferedReader bufferedReader;
  private Reader stringReader;

  @Before
  public void initializeOutputStream() {
    outputContent = new ByteArrayOutputStream();
    printStream = new PrintStream(outputContent);
    ViewConstants.setOutputStream(printStream);
  }

  protected void initializeInputStreamWithInput(String inputString) {
    stringReader = new StringReader(inputString);
    bufferedReader = new BufferedReader(stringReader);
    ViewConstants.setInputStream(bufferedReader);
  }

  protected String getOutputStreamContent() {
    return outputContent.toString();
  }

  @After
  public void restoreStreams() {
    ViewConstants.setInputStream(new BufferedReader(new InputStreamReader(System.in)));
    ViewConstants.setOutputStream(System.out);
  }
}
