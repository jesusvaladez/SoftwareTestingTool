package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class TaskResourceProviderTest {
    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Task;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.replay(managementController, response);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID, 100);
        properties.put(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID, 100);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        try {
            provider.createResources(request);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Task;
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        com.google.inject.Injector m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.controller.internal.TaskResourceProvider provider = ((org.apache.ambari.server.controller.internal.TaskResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, amc)));
        m_injector.injectMembers(provider);
        org.apache.ambari.server.controller.internal.TaskResourceProvider.s_dao = hostRoleCommandDAO;
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        hostRoleCommandEntity.setRequestId(100L);
        hostRoleCommandEntity.setTaskId(100L);
        hostRoleCommandEntity.setStageId(100L);
        hostRoleCommandEntity.setRole(org.apache.ambari.server.Role.DATANODE);
        hostRoleCommandEntity.setCustomCommandName("customCommandName");
        hostRoleCommandEntity.setCommandDetail("commandDetail");
        hostRoleCommandEntity.setOpsDisplayName("opsDisplayName");
        entities.add(hostRoleCommandEntity);
        EasyMock.expect(hostRoleCommandDAO.findAll(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.spi.Predicate.class))).andReturn(entities).once();
        EasyMock.replay(hostRoleCommandDAO);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_DET_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_OPS_DISPLAY_NAME);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).equals("100").and().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            long taskId = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals(100L, taskId);
            org.junit.Assert.assertEquals(null, resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CUST_CMD_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("commandDetail", resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_DET_PROPERTY_ID));
            org.junit.Assert.assertEquals("opsDisplayName", resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_OPS_DISPLAY_NAME));
        }
        EasyMock.verify(hostRoleCommandDAO);
    }

    @org.junit.Test
    public void testGetResourcesForTopology() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Task;
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.apache.ambari.server.topology.TopologyManager topologyManager = EasyMock.createMock(org.apache.ambari.server.topology.TopologyManager.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO = EasyMock.createMock(org.apache.ambari.server.orm.dao.ExecutionCommandDAO.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory = EasyMock.createMock(org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory.class);
        com.google.inject.Injector m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.controller.internal.TaskResourceProvider provider = ((org.apache.ambari.server.controller.internal.TaskResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, amc)));
        m_injector.injectMembers(provider);
        org.apache.ambari.server.controller.internal.TaskResourceProvider.s_dao = hostRoleCommandDAO;
        org.apache.ambari.server.controller.internal.TaskResourceProvider.s_topologyManager = topologyManager;
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        hostRoleCommandEntity.setRequestId(100L);
        hostRoleCommandEntity.setTaskId(100L);
        hostRoleCommandEntity.setStageId(100L);
        hostRoleCommandEntity.setRole(org.apache.ambari.server.Role.DATANODE);
        hostRoleCommandEntity.setCustomCommandName("customCommandName");
        hostRoleCommandEntity.setCommandDetail("commandDetail");
        hostRoleCommandEntity.setOpsDisplayName("opsDisplayName");
        commands.add(new org.apache.ambari.server.actionmanager.HostRoleCommand(hostRoleCommandEntity, hostDAO, executionCommandDAO, ecwFactory));
        EasyMock.expect(hostRoleCommandDAO.findAll(org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.spi.Request.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.spi.Predicate.class))).andReturn(entities).once();
        EasyMock.expect(topologyManager.getTasks(org.easymock.EasyMock.anyLong())).andReturn(commands).once();
        EasyMock.replay(hostRoleCommandDAO, topologyManager);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_DET_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_OPS_DISPLAY_NAME);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).equals("100").and().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals("100").toPredicate();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            long taskId = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals(100L, taskId);
            org.junit.Assert.assertEquals(null, resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CUST_CMD_NAME_PROPERTY_ID));
            org.junit.Assert.assertEquals("commandDetail", resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_DET_PROPERTY_ID));
            org.junit.Assert.assertEquals("opsDisplayName", resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_COMMAND_OPS_DISPLAY_NAME));
        }
        EasyMock.verify(hostRoleCommandDAO, topologyManager);
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Task;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        EasyMock.replay(managementController, response);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).equals("Task100").toPredicate();
        try {
            provider.updateResources(request, predicate);
            org.junit.Assert.fail("Expected an UnsupportedOperationException");
        } catch (java.lang.UnsupportedOperationException e) {
        }
        EasyMock.verify(managementController, response);
    }

    @org.junit.Test(expected = java.lang.UnsupportedOperationException.class)
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Task;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).equals("Task100").toPredicate();
        provider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        EasyMock.verify(managementController);
    }

    @org.junit.Test
    public void testParseStructuredOutput() {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Task;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.TaskResourceProvider taskResourceProvider = new org.apache.ambari.server.controller.internal.TaskResourceProvider(managementController);
        EasyMock.replay(managementController);
        java.util.Map<?, ?> result = taskResourceProvider.parseStructuredOutput("{\"a\":\"b\", \"c\": {\"d\":\"e\",\"f\": [\"g\",\"h\"],\"i\": {\"k\":\"l\"}}}");
        org.junit.Assert.assertEquals(result.size(), 2);
        java.util.Map<?, ?> submap = ((java.util.Map<?, ?>) (result.get("c")));
        org.junit.Assert.assertEquals(submap.size(), 3);
        java.util.List sublist = ((java.util.List) (submap.get("f")));
        org.junit.Assert.assertEquals(sublist.size(), 2);
        java.util.Map<?, ?> subsubmap = ((java.util.Map<?, ?>) (submap.get("i")));
        org.junit.Assert.assertEquals(subsubmap.size(), 1);
        org.junit.Assert.assertEquals(subsubmap.get("k"), "l");
        result = taskResourceProvider.parseStructuredOutput("{\"a\": invalid JSON}");
        org.junit.Assert.assertNull(result);
        result = taskResourceProvider.parseStructuredOutput("{\"a\": 5}");
        org.junit.Assert.assertEquals(result.get("a"), 5);
        EasyMock.verify(managementController);
    }

    @org.junit.Test
    public void testParseStructuredOutputForHostCheck() {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Task;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.TaskResourceProvider taskResourceProvider = new org.apache.ambari.server.controller.internal.TaskResourceProvider(managementController);
        EasyMock.replay(managementController);
        java.util.Map<?, ?> result = taskResourceProvider.parseStructuredOutput("{\"host_resolution_check\": {\"failures\": [{\"cause\": [-2, \"Name or service not known\"], \"host\": \"foobar\", \"type\": \"FORWARD_LOOKUP\"}], \"message\": \"There were 1 host(s) that could not resolve to an IP address.\", \"failed_count\": 1, \"success_count\": 3, \"exit_code\": 0}}");
        org.junit.Assert.assertNotNull(result);
        java.util.Map<?, ?> host_resolution_check = ((java.util.Map<?, ?>) (result.get("host_resolution_check")));
        org.junit.Assert.assertEquals(host_resolution_check.get("success_count"), 3);
        org.junit.Assert.assertEquals(host_resolution_check.get("failed_count"), 1);
        EasyMock.verify(managementController);
    }

    @org.junit.Test
    public void testInvalidStructuredOutput() {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Task;
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.TaskResourceProvider taskResourceProvider = new org.apache.ambari.server.controller.internal.TaskResourceProvider(managementController);
        EasyMock.replay(managementController);
        java.util.Map<?, ?> result = taskResourceProvider.parseStructuredOutput(null);
        org.junit.Assert.assertNull(result);
        result = taskResourceProvider.parseStructuredOutput("This is some bad JSON");
        org.junit.Assert.assertNull(result);
        EasyMock.verify(managementController);
    }
}