package org.apache.ambari.server.events.listeners.tasks;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
public class TaskStatusListenerTest extends org.easymock.EasyMockSupport {
    private org.apache.ambari.server.events.publishers.TaskEventPublisher publisher = new org.apache.ambari.server.events.publishers.TaskEventPublisher();

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory;

    @org.junit.Test
    public void testOnTaskUpdateEvent() throws org.apache.ambari.server.ClusterNotFoundException {
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = new java.util.ArrayList<>();
        org.apache.ambari.server.state.ServiceComponentHostEvent serviceComponentHostEvent = createNiceMock(org.apache.ambari.server.state.ServiceComponentHostEvent.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.easymock.EasyMock.replay(hostDAO);
        org.easymock.EasyMock.replay(serviceComponentHostEvent);
        int hostRoleCommandSize = 3;
        int hrcCounter = 1;
        for (int stageCounter = 0; stageCounter < 2; stageCounter++) {
            for (int i = 1; i <= hostRoleCommandSize; i++ , hrcCounter++) {
                java.lang.String hostname = "hostname-" + hrcCounter;
                org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand(hostname, org.apache.ambari.server.Role.DATANODE, serviceComponentHostEvent, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
                hostRoleCommand.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
                hostRoleCommand.setRequestId(1L);
                hostRoleCommand.setStageId(stageCounter);
                hostRoleCommand.setTaskId(hrcCounter);
                hostRoleCommands.add(hostRoleCommand);
            }
        }
        org.apache.ambari.server.actionmanager.HostRoleStatus hostRoleStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;
        org.apache.ambari.server.orm.dao.StageDAO stageDAO = createNiceMock(org.apache.ambari.server.orm.dao.StageDAO.class);
        org.apache.ambari.server.orm.dao.RequestDAO requestDAO = createNiceMock(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = createNiceMock(org.apache.ambari.server.orm.entities.StageEntity.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntity = createNiceMock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        org.apache.ambari.server.events.publishers.STOMPUpdatePublisher statePublisher = createNiceMock(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class);
        org.apache.ambari.server.api.stomp.NamedTasksSubscriptions namedTasksSubscriptions = createNiceMock(org.apache.ambari.server.api.stomp.NamedTasksSubscriptions.class);
        org.easymock.EasyMock.expect(stageEntity.getStatus()).andReturn(hostRoleStatus).anyTimes();
        org.easymock.EasyMock.expect(stageEntity.getDisplayStatus()).andReturn(hostRoleStatus).anyTimes();
        org.easymock.EasyMock.expect(stageEntity.isSkippable()).andReturn(java.lang.Boolean.FALSE).anyTimes();
        org.easymock.EasyMock.expect(stageEntity.getRoleSuccessCriterias()).andReturn(java.util.Collections.emptyList()).anyTimes();
        org.easymock.EasyMock.expect(stageDAO.findByPK(EasyMock.anyObject(org.apache.ambari.server.orm.entities.StageEntityPK.class))).andReturn(stageEntity).anyTimes();
        org.easymock.EasyMock.expect(requestEntity.getStatus()).andReturn(hostRoleStatus).anyTimes();
        org.easymock.EasyMock.expect(requestEntity.getDisplayStatus()).andReturn(hostRoleStatus).anyTimes();
        org.easymock.EasyMock.expect(requestDAO.findByPK(EasyMock.anyLong())).andReturn(requestEntity).anyTimes();
        org.easymock.EasyMock.expect(requestDAO.updateStatus(EasyMock.eq(1L), EasyMock.eq(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED), EasyMock.eq(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED))).andReturn(new org.apache.ambari.server.orm.entities.RequestEntity()).times(1);
        org.easymock.EasyMock.replay(stageEntity);
        org.easymock.EasyMock.replay(requestEntity);
        org.easymock.EasyMock.replay(stageDAO);
        org.easymock.EasyMock.replay(requestDAO);
        org.easymock.EasyMock.replay(statePublisher);
        org.easymock.EasyMock.replay(namedTasksSubscriptions);
        org.apache.ambari.server.events.TaskCreateEvent event = new org.apache.ambari.server.events.TaskCreateEvent(hostRoleCommands);
        org.apache.ambari.server.events.listeners.tasks.TaskStatusListener listener = new org.apache.ambari.server.events.listeners.tasks.TaskStatusListener(publisher, stageDAO, requestDAO, statePublisher, namedTasksSubscriptions);
        org.junit.Assert.assertTrue(listener.getActiveTasksMap().isEmpty());
        org.junit.Assert.assertTrue(listener.getActiveStageMap().isEmpty());
        org.junit.Assert.assertTrue(listener.getActiveRequestMap().isEmpty());
        listener.onTaskCreateEvent(event);
        org.junit.Assert.assertEquals(listener.getActiveTasksMap().size(), 6);
        org.junit.Assert.assertEquals(listener.getActiveStageMap().size(), 2);
        org.junit.Assert.assertEquals(listener.getActiveRequestMap().size(), 1);
        org.junit.Assert.assertEquals(listener.getActiveRequestMap().get(1L).getStatus(), hostRoleStatus);
        java.lang.String hostname = "hostname-1";
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand(hostname, org.apache.ambari.server.Role.DATANODE, serviceComponentHostEvent, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
        hostRoleCommand.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        hostRoleCommand.setRequestId(1L);
        hostRoleCommand.setStageId(0);
        hostRoleCommand.setTaskId(1L);
        listener.onTaskUpdateEvent(new org.apache.ambari.server.events.TaskUpdateEvent(java.util.Collections.singletonList(hostRoleCommand)));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, listener.getActiveRequestMap().get(1L).getStatus());
        hrcCounter = 1;
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> finalHostRoleCommands = new java.util.ArrayList<>();
        org.apache.ambari.server.actionmanager.HostRoleStatus finalHostRoleStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED;
        for (int stageCounter = 0; stageCounter < 2; stageCounter++) {
            for (int i = 1; i <= hostRoleCommandSize; i++ , hrcCounter++) {
                java.lang.String finalHostname = "hostname-" + hrcCounter;
                org.apache.ambari.server.actionmanager.HostRoleCommand finalHostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand(finalHostname, org.apache.ambari.server.Role.DATANODE, serviceComponentHostEvent, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
                finalHostRoleCommand.setStatus(finalHostRoleStatus);
                finalHostRoleCommand.setRequestId(1L);
                finalHostRoleCommand.setStageId(stageCounter);
                finalHostRoleCommand.setTaskId(hrcCounter);
                finalHostRoleCommands.add(finalHostRoleCommand);
            }
            finalHostRoleStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED;
        }
        listener.onTaskUpdateEvent(new org.apache.ambari.server.events.TaskUpdateEvent(finalHostRoleCommands));
        org.junit.Assert.assertNull(listener.getActiveRequestMap().get(1L));
        verifyAll();
    }

    @org.junit.Test
    public void testNamedTasksEnabled() {
        final java.lang.Long taskId = 1L;
        final java.lang.Long requestId = 2L;
        final org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED;
        final java.lang.String stderr = "gW$%SGFbhzsdfHBzdffdfd";
        final java.lang.String stdout = "gW$%gTESJ KHBjzdkfjbgv";
        final java.lang.String errorLog = " wTHT J YHKtjgsjgbvklfj";
        final java.lang.String outputLog = "546ky3kt%V$WYk4tgs5xzs";
        com.google.inject.Provider<org.apache.ambari.server.events.listeners.tasks.TaskStatusListener> taskStatusListenerProvider = createMock(com.google.inject.Provider.class);
        org.apache.ambari.server.api.stomp.NamedTasksSubscriptions namedTasksSubscriptions = new org.apache.ambari.server.api.stomp.NamedTasksSubscriptions(taskStatusListenerProvider);
        org.easymock.Capture<org.apache.ambari.server.events.NamedTaskUpdateEvent> namedTaskUpdateEventCapture = org.easymock.Capture.newInstance();
        org.apache.ambari.server.events.publishers.STOMPUpdatePublisher stompUpdatePublisher = createStrictMock(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class);
        stompUpdatePublisher.publish(EasyMock.capture(namedTaskUpdateEventCapture));
        EasyMock.expectLastCall();
        org.apache.ambari.server.state.ServiceComponentHostEvent serviceComponentHostEvent = createNiceMock(org.apache.ambari.server.state.ServiceComponentHostEvent.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = createNiceMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.easymock.EasyMock.replay(hostDAO);
        org.easymock.EasyMock.replay(serviceComponentHostEvent);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> updateHostRolesCommands = new java.util.ArrayList<>();
        org.apache.ambari.server.actionmanager.HostRoleCommand updateHostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand("hostName", org.apache.ambari.server.Role.DATANODE, serviceComponentHostEvent, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
        updateHostRoleCommand.setStatus(status);
        updateHostRoleCommand.setRequestId(requestId);
        updateHostRoleCommand.setStageId(3L);
        updateHostRoleCommand.setTaskId(taskId);
        updateHostRoleCommand.setStderr(stderr);
        updateHostRoleCommand.setStdout(stdout);
        updateHostRoleCommand.setErrorLog(errorLog);
        updateHostRoleCommand.setOutputLog(outputLog);
        updateHostRolesCommands.add(updateHostRoleCommand);
        org.apache.ambari.server.orm.dao.StageDAO stageDAO = createNiceMock(org.apache.ambari.server.orm.dao.StageDAO.class);
        org.apache.ambari.server.orm.dao.RequestDAO requestDAO = createNiceMock(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.easymock.EasyMock.replay(stageDAO);
        org.easymock.EasyMock.replay(requestDAO);
        org.easymock.EasyMock.replay(stompUpdatePublisher);
        org.apache.ambari.server.events.listeners.tasks.TaskStatusListener listener = new org.apache.ambari.server.events.listeners.tasks.TaskStatusListener(publisher, stageDAO, requestDAO, stompUpdatePublisher, namedTasksSubscriptions);
        EasyMock.expect(taskStatusListenerProvider.get()).andReturn(listener);
        org.easymock.EasyMock.replay(taskStatusListenerProvider);
        namedTasksSubscriptions.addTaskId("", taskId, "sub-1");
        org.apache.ambari.server.actionmanager.HostRoleCommand activeHostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand("hostName", org.apache.ambari.server.Role.DATANODE, serviceComponentHostEvent, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
        activeHostRoleCommand.setStatus(status);
        listener.getActiveTasksMap().put(taskId, activeHostRoleCommand);
        listener.onTaskUpdateEvent(new org.apache.ambari.server.events.TaskUpdateEvent(updateHostRolesCommands));
        org.junit.Assert.assertNotNull(namedTaskUpdateEventCapture.getValues());
        org.junit.Assert.assertEquals(1L, namedTaskUpdateEventCapture.getValues().size());
        org.apache.ambari.server.events.NamedTaskUpdateEvent capturedEvent = namedTaskUpdateEventCapture.getValue();
        org.junit.Assert.assertEquals(taskId, capturedEvent.getId());
        org.junit.Assert.assertEquals(requestId, capturedEvent.getRequestId());
        org.junit.Assert.assertEquals(status, capturedEvent.getStatus());
        org.junit.Assert.assertEquals(stderr, capturedEvent.getStderr());
        org.junit.Assert.assertEquals(stdout, capturedEvent.getStdout());
        org.junit.Assert.assertEquals(errorLog, capturedEvent.getErrorLog());
        org.junit.Assert.assertEquals(outputLog, capturedEvent.getOutLog());
        verifyAll();
    }
}