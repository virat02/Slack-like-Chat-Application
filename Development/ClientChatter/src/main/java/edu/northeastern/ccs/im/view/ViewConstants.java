package edu.northeastern.ccs.im.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ViewConstants {

  private static BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
  private static PrintStream outputStream = new PrintStream(System.out);

  public static BufferedReader getInputStream() {
    return inputStream;
  }

  public static PrintStream getOutputStream() {
    return outputStream;
  }

  public static void setInputStream(BufferedReader reader) {
    inputStream = reader;
  }

  public static void setOutputStream(PrintStream printStream) {
    outputStream = printStream;
  }
}
