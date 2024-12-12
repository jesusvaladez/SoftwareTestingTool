package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ActionResourceProviderTest {
    private com.google.inject.Injector injector;

    public static org.apache.ambari.server.controller.internal.ActionResourceProvider getActionDefinitionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Action;
        return ((org.apache.ambari.server.controller.internal.ActionResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController)));
    }

    public static java.util.Set<org.apache.ambari.server.controller.ActionResponse> getActions(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.ActionRequest> requests) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.internal.ActionResourceProvider provider = org.apache.ambari.server.controller.internal.ActionResourceProviderTest.getActionDefinitionResourceProvider(controller);
        return provider.getActionDefinitions(requests);
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Action;
        org.apache.ambari.server.api.services.AmbariMetaInfo am = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(am).anyTimes();
        java.util.List<org.apache.ambari.server.customactions.ActionDefinition> allDefinition = new java.util.ArrayList<>();
        allDefinition.add(new org.apache.ambari.server.customactions.ActionDefinition("a1", org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "fileName", "HDFS", "DATANODE", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("100"), null));
        allDefinition.add(new org.apache.ambari.server.customactions.ActionDefinition("a2", org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "fileName", "HDFS", "DATANODE", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("100"), null));
        allDefinition.add(new org.apache.ambari.server.customactions.ActionDefinition("a3", org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "fileName", "HDFS", "DATANODE", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("100"), null));
        java.util.Set<org.apache.ambari.server.controller.ActionResponse> allResponse = new java.util.HashSet<>();
        for (org.apache.ambari.server.customactions.ActionDefinition definition : allDefinition) {
            allResponse.add(definition.convertToResponse());
        }
        org.apache.ambari.server.customactions.ActionDefinition namedDefinition = new org.apache.ambari.server.customactions.ActionDefinition("a1", org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "fileName", "HDFS", "DATANODE", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("100"), null);
        java.util.Set<org.apache.ambari.server.controller.ActionResponse> nameResponse = new java.util.HashSet<>();
        nameResponse.add(namedDefinition.convertToResponse());
        EasyMock.expect(am.getAllActionDefinition()).andReturn(allDefinition).once();
        EasyMock.expect(am.getActionDefinition("a1")).andReturn(namedDefinition).once();
        EasyMock.replay(managementController, am);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_TYPE_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ActionResourceProvider.DEFAULT_TIMEOUT_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ActionResourceProvider.DESCRIPTION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ActionResourceProvider.INPUTS_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_COMPONENT_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_HOST_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_SERVICE_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, null);
        org.junit.Assert.assertEquals(allResponse.size(), resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String actionName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_NAME_PROPERTY_ID)));
            java.lang.String actionType = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_TYPE_PROPERTY_ID)));
            java.lang.String defaultTimeout = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.DEFAULT_TIMEOUT_PROPERTY_ID)));
            java.lang.String description = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.DESCRIPTION_PROPERTY_ID)));
            java.lang.String inputs = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.INPUTS_PROPERTY_ID)));
            java.lang.String comp = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_COMPONENT_PROPERTY_ID)));
            java.lang.String svc = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_SERVICE_PROPERTY_ID)));
            java.lang.String host = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_HOST_PROPERTY_ID)));
            org.junit.Assert.assertTrue(allResponse.contains(new org.apache.ambari.server.controller.ActionResponse(actionName, actionType, inputs, svc, comp, description, host, defaultTimeout)));
        }
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_NAME_PROPERTY_ID).equals("a1").toPredicate();
        resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.junit.Assert.assertEquals("a1", resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_NAME_PROPERTY_ID));
        EasyMock.verify(managementController);
    }
}