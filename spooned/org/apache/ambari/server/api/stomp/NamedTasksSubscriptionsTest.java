package org.apache.ambari.server.api.stomp;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class NamedTasksSubscriptionsTest {
    private static final java.lang.String SESSION_ID_1 = "fdsg3";

    private static final java.lang.String SESSION_ID_2 = "idfg6";

    private org.apache.ambari.server.api.stomp.NamedTasksSubscriptions tasksSubscriptions;

    private com.google.inject.Provider<org.apache.ambari.server.events.listeners.tasks.TaskStatusListener> taskStatusListenerProvider;

    private org.apache.ambari.server.events.listeners.tasks.TaskStatusListener taskStatusListener;

    @org.junit.Before
    public void setupTest() {
        taskStatusListenerProvider = EasyMock.createMock(com.google.inject.Provider.class);
        taskStatusListener = EasyMock.createMock(org.apache.ambari.server.events.listeners.tasks.TaskStatusListener.class);
        java.util.Map<java.lang.Long, org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand1 = EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand4 = EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand5 = EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        EasyMock.expect(hostRoleCommand1.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS).anyTimes();
        EasyMock.expect(hostRoleCommand4.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS).anyTimes();
        EasyMock.expect(hostRoleCommand5.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS).anyTimes();
        hostRoleCommands.put(1L, hostRoleCommand1);
        hostRoleCommands.put(4L, hostRoleCommand4);
        hostRoleCommands.put(5L, hostRoleCommand5);
        EasyMock.expect(taskStatusListener.getActiveTasksMap()).andReturn(hostRoleCommands).anyTimes();
        EasyMock.expect(taskStatusListenerProvider.get()).andReturn(taskStatusListener).anyTimes();
        EasyMock.replay(taskStatusListenerProvider, taskStatusListener, hostRoleCommand1, hostRoleCommand4, hostRoleCommand5);
        tasksSubscriptions = new org.apache.ambari.server.api.stomp.NamedTasksSubscriptions(taskStatusListenerProvider);
        tasksSubscriptions.addTaskId(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_1, 1L, "sub-1");
        tasksSubscriptions.addTaskId(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_1, 5L, "sub-5");
        tasksSubscriptions.addTaskId(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_2, 1L, "sub-1");
        tasksSubscriptions.addTaskId(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_2, 4L, "sub-4");
    }

    @org.junit.Test
    public void testMatching() {
        java.util.Optional<java.lang.Long> taskIdOpt = tasksSubscriptions.matchDestination("/events/tasks/1");
        org.junit.Assert.assertTrue(taskIdOpt.isPresent());
        org.junit.Assert.assertEquals(1L, taskIdOpt.get().longValue());
        org.junit.Assert.assertFalse(tasksSubscriptions.matchDestination("/events/topologies").isPresent());
    }

    @org.junit.Test
    public void testCheckId() {
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(5L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(2L));
    }

    @org.junit.Test
    public void testRemoveBySessionId() {
        tasksSubscriptions.removeSession(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_1);
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(5L));
        tasksSubscriptions.removeSession(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_2);
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(5L));
    }

    @org.junit.Test
    public void testRemoveById() {
        tasksSubscriptions.removeId(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_1, "sub-1");
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(5L));
        tasksSubscriptions.removeId(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_1, "sub-5");
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(5L));
        tasksSubscriptions.removeId(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_2, "sub-1");
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(5L));
        tasksSubscriptions.removeId(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_2, "sub-4");
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(5L));
    }

    @org.junit.Test
    public void testAddDestination() {
        tasksSubscriptions = new org.apache.ambari.server.api.stomp.NamedTasksSubscriptions(taskStatusListenerProvider);
        tasksSubscriptions.addDestination(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_1, "/events/tasks/1", "sub-1");
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(5L));
        tasksSubscriptions.addDestination(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_1, "/events/tasks/5", "sub-5");
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(5L));
        tasksSubscriptions.addDestination(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_2, "/events/tasks/1", "sub-1");
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertFalse(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(5L));
        tasksSubscriptions.addDestination(org.apache.ambari.server.api.stomp.NamedTasksSubscriptionsTest.SESSION_ID_2, "/events/tasks/4", "sub-4");
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(1L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(4L));
        org.junit.Assert.assertTrue(tasksSubscriptions.checkTaskId(5L));
    }
}