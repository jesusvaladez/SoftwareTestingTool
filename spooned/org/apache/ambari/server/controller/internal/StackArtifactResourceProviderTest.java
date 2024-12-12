package org.apache.ambari.server.controller.internal;
import static org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class StackArtifactResourceProviderTest {
    private org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        metaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private org.apache.ambari.server.controller.internal.StackArtifactResourceProvider getStackArtifactResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact;
        return ((org.apache.ambari.server.controller.internal.StackArtifactResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController)));
    }

    @org.junit.Test
    public void testGetMetricsDescriptorForService() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.internal.StackArtifactResourceProvider resourceProvider = getStackArtifactResourceProvider(managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID).equals("metrics_descriptor").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID).equals("OTHER").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).equals("1.0").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).equals("HDFS").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = resourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertyMap = resource.getPropertiesMap();
        java.util.Map<java.lang.String, java.lang.Object> descriptor = propertyMap.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID + "/HDFS/DATANODE");
        org.junit.Assert.assertNotNull(descriptor);
        org.junit.Assert.assertEquals(1, ((java.util.ArrayList) (descriptor.get("Component"))).size());
        org.apache.ambari.server.state.stack.MetricDefinition md = ((org.apache.ambari.server.state.stack.MetricDefinition) (((java.util.ArrayList) (descriptor.get("Component"))).iterator().next()));
        org.apache.ambari.server.state.stack.Metric m1 = md.getMetrics().get("metrics/dfs/datanode/heartBeats_avg_time");
        org.apache.ambari.server.state.stack.Metric m2 = md.getMetrics().get("metrics/rpc/closeRegion_num_ops");
        org.junit.Assert.assertEquals(326, md.getMetrics().size());
        org.junit.Assert.assertTrue(m1.isAmsHostMetric());
        org.junit.Assert.assertEquals("unitless", m1.getUnit());
        org.junit.Assert.assertFalse(m2.isAmsHostMetric());
        EasyMock.verify(managementController);
    }

    @org.junit.Test
    public void testGetMetricsDescriptorRpcForNamenode() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.internal.StackArtifactResourceProvider resourceProvider = getStackArtifactResourceProvider(managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID).equals("metrics_descriptor").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID).equals("OTHER").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).equals("1.0").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).equals("HDFS").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = resourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertyMap = resource.getPropertiesMap();
        java.util.Map<java.lang.String, java.lang.Object> descriptor = propertyMap.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID + "/HDFS/NAMENODE");
        org.junit.Assert.assertNotNull(descriptor);
        org.junit.Assert.assertEquals(2, ((java.util.ArrayList) (descriptor.get("Component"))).size());
        org.apache.ambari.server.state.stack.MetricDefinition md = ((org.apache.ambari.server.state.stack.MetricDefinition) (((java.util.ArrayList) (descriptor.get("Component"))).iterator().next()));
        org.junit.Assert.assertEquals("rpcdetailed.rpcdetailed.client.BlockReceivedAndDeletedAvgTime", md.getMetrics().get("metrics/rpcdetailed/client/blockReceived_avg_time").getName());
        org.junit.Assert.assertEquals("rpc.rpc.healthcheck.CallQueueLength", md.getMetrics().get("metrics/rpc/healthcheck/callQueueLen").getName());
        org.junit.Assert.assertEquals("rpcdetailed.rpcdetailed.datanode.DeleteNumOps", md.getMetrics().get("metrics/rpcdetailed/datanode/delete_num_ops").getName());
        EasyMock.verify(managementController);
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testGetWidgetDescriptorForService() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(managementController.getAmbariMetaInfo()).andReturn(metaInfo).anyTimes();
        EasyMock.replay(managementController);
        org.apache.ambari.server.controller.internal.StackArtifactResourceProvider resourceProvider = getStackArtifactResourceProvider(managementController);
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_NAME_PROPERTY_ID).equals("widgets_descriptor").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_NAME_PROPERTY_ID).equals("OTHER").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_VERSION_PROPERTY_ID).equals("2.0").and().property(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.STACK_SERVICE_NAME_PROPERTY_ID).equals("HBASE").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = resourceProvider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertyMap = resource.getPropertiesMap();
        java.util.Map<java.lang.String, java.lang.Object> descriptor = propertyMap.get(org.apache.ambari.server.controller.internal.StackArtifactResourceProvider.ARTIFACT_DATA_PROPERTY_ID);
        org.junit.Assert.assertNotNull(descriptor);
        org.junit.Assert.assertEquals(1, ((java.util.List<java.lang.Object>) (descriptor.get("layouts"))).size());
        org.apache.ambari.server.state.stack.WidgetLayout layout = ((java.util.List<org.apache.ambari.server.state.stack.WidgetLayout>) (descriptor.get("layouts"))).iterator().next();
        org.junit.Assert.assertEquals("default_hbase_layout", layout.getLayoutName());
        org.junit.Assert.assertEquals("HBASE_SUMMARY", layout.getSectionName());
        org.junit.Assert.assertEquals(5, layout.getWidgetLayoutInfoList().size());
        EasyMock.verify(managementController);
    }
}