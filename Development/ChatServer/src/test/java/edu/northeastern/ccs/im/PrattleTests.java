package edu.northeastern.ccs.im;

import edu.northeastern.ccs.im.communications.*;
import edu.northeastern.ccs.im.controller.GroupControllerTest;
import edu.northeastern.ccs.im.controller.ProfileControllerTest;
import edu.northeastern.ccs.im.controller.UserControllerTest;
import edu.northeastern.ccs.im.entity.GroupEntityTest;
import edu.northeastern.ccs.im.entity.MessageEntityTest;
import edu.northeastern.ccs.im.entity.ProfileEntityTest;
import edu.northeastern.ccs.im.entity.UserEntityTest;
import edu.northeastern.ccs.im.readers.ReadersTest;
import edu.northeastern.ccs.im.server.MessageManagerServiceTest;
import edu.northeastern.ccs.im.server.RequestDispatcherTests;
import edu.northeastern.ccs.im.server.RequestHandlerTests;
import edu.northeastern.ccs.im.service.*;
import edu.northeastern.ccs.im.service.jpa_service.GroupJPAServiceTest;
import edu.northeastern.ccs.im.service.jpa_service.MessageJPAServiceTest;
import edu.northeastern.ccs.im.service.jpa_service.ProfileJPAServiceTest;
import edu.northeastern.ccs.im.service.jpa_service.UserJPAServiceTest;
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
        MessageBroadCastServiceTests.class,
        MessageManagerServiceTest.class,
        CommunicationUtilsTest.class,
        NetworkRequestFactoryTests.class,
        NetworkResponseFactoryTests.class,
        NetworkRequestImplTests.class,
        RequestDispatcherTests.class,
        PayloadImplTests.class,
        RequestHandlerTests.class,
        ReadersTest.class,

        //Services
        GroupServiceTest.class,
        MessageServiceTest.class,
        ProfileServiceTest.class,
        UserServiceTest.class,

        //JPA Services
        GroupJPAServiceTest.class,
        MessageJPAServiceTest.class,
        ProfileJPAServiceTest.class,
        UserJPAServiceTest.class,

        //Entities
        GroupEntityTest.class,
        MessageEntityTest.class,
        ProfileEntityTest.class,
        UserEntityTest.class,

        //Controllers
        UserControllerTest.class,
        ProfileControllerTest.class,
        GroupControllerTest.class
})
public class PrattleTests {

}
