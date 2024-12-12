package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class RequestStageContainerTest {
    @org.junit.Test
    public void testGetId() {
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(500L, null, null, null);
        org.junit.Assert.assertEquals(500, requestStages.getId().longValue());
    }

    @org.junit.Test
    public void testGetAddStages() {
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(500L, null, null, null);
        org.junit.Assert.assertTrue(requestStages.getStages().isEmpty());
        org.apache.ambari.server.actionmanager.Stage stage = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        requestStages.addStages(java.util.Collections.singletonList(stage));
        org.junit.Assert.assertEquals(1, requestStages.getStages().size());
        org.junit.Assert.assertTrue(requestStages.getStages().contains(stage));
        org.apache.ambari.server.actionmanager.Stage stage2 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage stage3 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> listStages = new java.util.ArrayList<>();
        listStages.add(stage2);
        listStages.add(stage3);
        requestStages.addStages(listStages);
        org.junit.Assert.assertEquals(3, requestStages.getStages().size());
        listStages = requestStages.getStages();
        org.junit.Assert.assertEquals(stage, listStages.get(0));
        org.junit.Assert.assertEquals(stage2, listStages.get(1));
        org.junit.Assert.assertEquals(stage3, listStages.get(2));
    }

    @org.junit.Test
    public void testGetLastStageId() {
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(1L, null, null, null);
        org.junit.Assert.assertEquals(-1, requestStages.getLastStageId());
        org.apache.ambari.server.actionmanager.Stage stage1 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage stage2 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> listStages = new java.util.ArrayList<>();
        listStages.add(stage1);
        listStages.add(stage2);
        EasyMock.expect(stage2.getStageId()).andReturn(22L);
        EasyMock.replay(stage1, stage2);
        requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(1L, listStages, null, null);
        org.junit.Assert.assertEquals(22, requestStages.getLastStageId());
    }

    @org.junit.Test
    public void testGetProjectedState() {
        java.lang.String hostname = "host";
        java.lang.String componentName = "component";
        org.apache.ambari.server.actionmanager.Stage stage1 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage stage2 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage stage3 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage stage4 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand command1 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand command2 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand command3 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(stage1);
        stages.add(stage2);
        stages.add(stage3);
        stages.add(stage4);
        EasyMock.expect(stage1.getHostRoleCommands()).andReturn(java.util.Collections.singletonMap(hostname, java.util.Collections.singletonMap(componentName, command1))).anyTimes();
        EasyMock.expect(stage2.getHostRoleCommands()).andReturn(java.util.Collections.singletonMap(hostname, java.util.Collections.singletonMap(componentName, command2))).anyTimes();
        EasyMock.expect(stage3.getHostRoleCommands()).andReturn(java.util.Collections.singletonMap(hostname, java.util.Collections.singletonMap(componentName, command3))).anyTimes();
        EasyMock.expect(stage4.getHostRoleCommands()).andReturn(java.util.Collections.emptyMap()).anyTimes();
        EasyMock.expect(command3.getRoleCommand()).andReturn(org.apache.ambari.server.RoleCommand.SERVICE_CHECK).anyTimes();
        EasyMock.expect(command2.getRoleCommand()).andReturn(org.apache.ambari.server.RoleCommand.INSTALL).anyTimes();
        EasyMock.replay(stage1, stage2, stage3, stage4, command1, command2, command3);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(1L, stages, null, null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, requestStages.getProjectedState(hostname, componentName));
        EasyMock.verify(stage1, stage2, stage3, stage4, command1, command2, command3);
    }

    @org.junit.Test
    public void testPersist() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.ActionManager actionManager = EasyMock.createStrictMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.actionmanager.RequestFactory requestFactory = EasyMock.createStrictMock(org.apache.ambari.server.actionmanager.RequestFactory.class);
        org.apache.ambari.server.actionmanager.Request request = EasyMock.createStrictMock(org.apache.ambari.server.actionmanager.Request.class);
        org.apache.ambari.server.actionmanager.Stage stage1 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage stage2 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(stage1);
        stages.add(stage2);
        EasyMock.expect(requestFactory.createNewFromStages(stages, "{}")).andReturn(request);
        request.setUserName(null);
        EasyMock.expectLastCall().once();
        EasyMock.expect(request.getStages()).andReturn(stages).anyTimes();
        actionManager.sendActions(request, null);
        EasyMock.replay(actionManager, requestFactory, request, stage1, stage2);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(1L, stages, requestFactory, actionManager);
        requestStages.persist();
        EasyMock.verify(actionManager, requestFactory, request, stage1, stage2);
    }

    @org.junit.Test
    public void testPersist_noStages() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.ActionManager actionManager = EasyMock.createStrictMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.actionmanager.RequestFactory requestFactory = EasyMock.createStrictMock(org.apache.ambari.server.actionmanager.RequestFactory.class);
        EasyMock.replay(actionManager, requestFactory);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(1L, null, requestFactory, actionManager);
        requestStages.persist();
        EasyMock.verify(actionManager, requestFactory);
    }

    @org.junit.Test
    public void testGetRequestStatusResponse() {
        org.apache.ambari.server.actionmanager.ActionManager actionManager = EasyMock.createStrictMock(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.apache.ambari.server.actionmanager.Stage stage1 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage stage2 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand command1 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        org.apache.ambari.server.Role role = EasyMock.createNiceMock(org.apache.ambari.server.Role.class);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        org.apache.ambari.server.RoleCommand roleCommand = org.apache.ambari.server.RoleCommand.INSTALL;
        org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS;
        stages.add(stage1);
        stages.add(stage2);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = new java.util.ArrayList<>();
        hostRoleCommands.add(command1);
        EasyMock.expect(actionManager.getRequestTasks(100)).andReturn(hostRoleCommands);
        EasyMock.expect(actionManager.getRequestContext(100)).andReturn("test");
        EasyMock.expect(command1.getTaskId()).andReturn(1L);
        EasyMock.expect(command1.getRoleCommand()).andReturn(roleCommand);
        EasyMock.expect(command1.getRole()).andReturn(role);
        EasyMock.expect(command1.getStatus()).andReturn(status);
        EasyMock.replay(actionManager, stage1, stage2, command1, role);
        org.apache.ambari.server.controller.internal.RequestStageContainer requestStages = new org.apache.ambari.server.controller.internal.RequestStageContainer(100L, stages, null, actionManager);
        org.apache.ambari.server.controller.RequestStatusResponse response = requestStages.getRequestStatusResponse();
        org.junit.Assert.assertEquals(100, response.getRequestId());
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> tasks = response.getTasks();
        org.junit.Assert.assertEquals(1, tasks.size());
        org.apache.ambari.server.controller.ShortTaskStatus task = tasks.get(0);
        org.junit.Assert.assertEquals(1, task.getTaskId());
        org.junit.Assert.assertEquals(roleCommand.toString(), task.getCommand());
        org.junit.Assert.assertEquals(status.toString(), task.getStatus());
        org.junit.Assert.assertEquals("test", response.getRequestContext());
        EasyMock.verify(actionManager, stage1, stage2, command1, role);
    }
}