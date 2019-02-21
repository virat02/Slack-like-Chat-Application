package edu.northeastern.ccs.im;

import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import edu.northeastern.ccs.im.server.ClientTimer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClientTimerTest {

  private ClientTimer clientTimer;
  private boolean shouldWait;

  @Before
  public void initializeClientTimer() {
    clientTimer = new ClientTimer();
    shouldWait = true;
  }

  @Test
  public void testTimerIsBehind() {
    clientTimer.isBehind();
    assertFalse(clientTimer.isBehind());
  }

  @Test
  public void testTimerForUpdateAfterInitialization() {
    clientTimer.updateAfterInitialization();
  }

  @Test
  public void testTimerForUpdateAfterActivity() {
    long currentTime = clientTimer.getTimeInMilliSeconds();
    clientTimer.updateAfterActivity();
    assertTrue((clientTimer.getTimeInMilliSeconds() - currentTime > 16000000) &&
            (clientTimer.getTimeInMilliSeconds() - currentTime < 18000000));
    assertFalse(clientTimer.isBehind());
  }

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