package edu.northeastern.ccs.im;

import edu.northeastern.ccs.im.communications.*;
import edu.northeastern.ccs.im.readers.ReadersTest;
import edu.northeastern.ccs.im.server.RequestDispatcherTests;
import edu.northeastern.ccs.im.server.RequestHandlerTests;
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
        NetworkConnectionTests.class,
        ClientTimerTest.class,
        MessageTest.class,
        ClientRunnableTest.class,
        edu.northeastern.ccs.im.server.PrattleTests.class,
        ClientConnectionImplTests.class,
        CommunicationUtilsTest.class,
        NetworkRequestFactoryTests.class,
        NetworkResponseFactoryTests.class,
        NetworkRequestImplTests.class,
        RequestDispatcherTests.class,
        PayloadImplTests.class,
        RequestHandlerTests.class,
        ReadersTest.class,
})
public class PrattleTests {

}
