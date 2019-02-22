package edu.northeastern.ccs.im;

import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import edu.northeastern.ccs.im.server.ClientTimer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Adding test class for {@code ClientTimer} class
 */
public class ClientTimerTest {

  private ClientTimer clientTimer;
  private boolean shouldWait;

  /**
   * Initializing the client timer every time the test is going to run
   */
  @Before
  public void initializeClientTimer() {
    clientTimer = new ClientTimer();
    shouldWait = true;
  }

  /**
   * Testing the isBehind for normal use cases.
   */
  @Test
  public void testTimerIsBehind() {
    clientTimer.isBehind();
    assertFalse(clientTimer.isBehind());
  }

  /**
   * Testing the isBehind of the client timer after initialization.
   */
  @Test
  public void testTimerForUpdateAfterInitialization() {
    clientTimer.updateAfterInitialization();
    assertFalse(clientTimer.isBehind());
  }

  /**
   * Testing the isBehind of the client timer after an activity is performed.
   */
  @Test
  public void testTimerForUpdateAfterActivity() {
    long currentTime = clientTimer.getTimeInMilliSeconds();
    clientTimer.updateAfterActivity();
    assertTrue((clientTimer.getTimeInMilliSeconds() - currentTime > 16000000) &&
            (clientTimer.getTimeInMilliSeconds() - currentTime < 18000000));
    assertFalse(clientTimer.isBehind());
  }

  /**
   * Testing the isBehind of the client timer after multiple activities is performed.
   */
  @Test
  public void testTimerForUpdateAfterMultipleActivity() {
    long currentTime = clientTimer.getTimeInMilliSeconds();
    clientTimer.updateAfterActivity();
    assertTrue((clientTimer.getTimeInMilliSeconds() - currentTime > 16000000) &&
            (clientTimer.getTimeInMilliSeconds() - currentTime < 18000000));
    assertFalse(clientTimer.isBehind());

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        timer.cancel();
        shouldWait = false;
      }
    };
    timer.schedule(task, 10000, 1);

    while (shouldWait) {
      System.out.print("");
    }

    clientTimer.updateAfterActivity();
    assertTrue((clientTimer.getTimeInMilliSeconds() - currentTime > 16000000) &&
            (clientTimer.getTimeInMilliSeconds() - currentTime < 18000000));
  }
}