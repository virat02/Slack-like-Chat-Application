package edu.northeastern.ccs.im;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import edu.northeastern.ccs.jpa.JpaTest;

/**
 * Junit test suite for prattle test classes.
 *
 * @author sibendudey
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        // Add the junit classes under here provided by the
        // fully qualified name of the classes
        NetworkConnectionTests.class, ClientTimerTest.class, MessageTest.class, JpaTest.class, ClientRunnableTest.class

})
public class PrattleTests {

}
