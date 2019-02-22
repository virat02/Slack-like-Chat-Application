package edu.northeastern.ccs.im;

import edu.northeastern.ccs.im.server.ClientRunnable;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Junit test suite for prattle test classes.
 *
 * @author sibendudey
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // Add the junit classes under here provided by the
        // fully qualified name of the classes
        NetworkConnectionTests.class, ClientTimerTest.class, MessageTest.class, ClientRunnableTest.class
})
public class PrattleTests {

}
