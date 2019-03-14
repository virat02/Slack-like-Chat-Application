package edu.northeastern.ccs.im.server;

import edu.northeastern.ccs.im.service.MessageBroadCastService;
import edu.northeastern.ccs.im.service.MessageManagerService;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class MessageManagerServiceTest {

    @Mock
    private MessageBroadCastService messageBroadcastService;

    private MessageManagerService messageManagerService;
    /**
     * Test for singleton MessageManagerService class
     */
    @Test
    public void testSingletonClass() {
        MessageManagerService messageManagerService1 = MessageManagerService.getInstance();
        MessageManagerService messageManagerService2 = MessageManagerService.getInstance();

        assertEquals(messageManagerService1.hashCode(), messageManagerService2.hashCode());
    }

    /**
     * Test for create service when isClientActive is true
     */
//    @Test
//    public void testCreateService() {
//        //when(messageBroadcastService.isClientActive()).thenReturn(true);
//        messageBroadcastService = new MessageBroadCastService();
//        messageManagerService.checkForInactivity(messageBroadcastService);
//
//    }


}
