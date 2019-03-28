package edu.northeastern.ccs.im.server;

import edu.northeastern.ccs.im.customexceptions.GroupNotFoundException;
import edu.northeastern.ccs.im.customexceptions.GroupNotPersistedException;
import edu.northeastern.ccs.im.customexceptions.UserNotFoundException;
import edu.northeastern.ccs.im.customexceptions.UserNotPresentInTheGroup;
import edu.northeastern.ccs.im.service.BroadCastService;
import edu.northeastern.ccs.im.service.GroupService;
import edu.northeastern.ccs.im.service.MessageManagerService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageManagerServiceTest {
    private GroupService groupService;
    private MessageManagerService messageManagerService;

    @Before
    public void setUp() {
        groupService = mock(GroupService.class);
        messageManagerService = MessageManagerService.getInstance();
        messageManagerService.setGroupService(groupService);
    }
    /**
     * Test for singleton MessageManagerService class
     */
    @Test
    public void testSingletonClass() {
        MessageManagerService messageManagerService1 = MessageManagerService.getInstance();
        MessageManagerService messageManagerService2 = MessageManagerService.getInstance();
        assertEquals(messageManagerService1.hashCode(), messageManagerService2.hashCode());
    }

    @Test
    public void shouldReturnSameMessageBroadCastServiceForRepeatedCallsToSameGroup()
            throws UserNotFoundException, GroupNotPersistedException, UserNotPresentInTheGroup, GroupNotFoundException {
        when(groupService.createIfNotPresent(anyString(), anyString(), anyBoolean())).thenReturn(true);
        BroadCastService broadCastService1 = messageManagerService.getService("group1", "user1", false);
        BroadCastService broadCastService2 = messageManagerService.getService("group1", "user2", false);
        assertEquals(broadCastService1, broadCastService2);
    }
}
