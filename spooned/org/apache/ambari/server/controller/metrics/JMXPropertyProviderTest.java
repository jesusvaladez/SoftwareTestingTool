package org.apache.ambari.server.controller.metrics;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class JMXPropertyProviderTest {
    protected static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name");

    protected static final java.lang.String HOST_COMPONENT_HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name");

    protected static final java.lang.String HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name");

    protected static final java.lang.String HOST_COMPONENT_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state");

    public static final int NUMBER_OF_RESOURCES = 400;

    private static final int METRICS_SERVICE_TIMEOUT = 10;

    public static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> jmxPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);

    public static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> jmxPropertyIdsWithHAState;

    static {
        jmxPropertyIdsWithHAState = new java.util.HashMap<>(jmxPropertyIds);
        jmxPropertyIdsWithHAState.get("NAMENODE").put("metrics/dfs/FSNamesystem/HAState", new org.apache.ambari.server.controller.internal.PropertyInfo("Hadoop:service=NameNode,name=FSNamesystem.tag#HAState", false, true));
    }

    private static org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory metricPropertyProviderFactory;

    private static org.apache.ambari.server.state.services.MetricsRetrievalService metricsRetrievalService;

    @org.junit.BeforeClass
    public static void setupClass() throws java.util.concurrent.TimeoutException {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_REQUEST_TTL_ENABLED.getKey(), "false");
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider.init(configuration);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory = injector.getInstance(org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory.class);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricsRetrievalService = injector.getInstance(org.apache.ambari.server.state.services.MetricsRetrievalService.class);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricsRetrievalService.startAsync();
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricsRetrievalService.awaitRunning(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricsRetrievalService.setThreadPoolExecutor(new org.apache.ambari.server.utils.SynchronousThreadPoolExecutor());
    }

    @org.junit.AfterClass
    public static void stopService() throws java.util.concurrent.TimeoutException {
        if ((org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricsRetrievalService != null) && org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricsRetrievalService.isRunning()) {
            org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricsRetrievalService.stopAsync();
            org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricsRetrievalService.awaitTerminated(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    @org.junit.Before
    public void setUpCommonMocks() throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.lang.reflect.Field field = org.apache.ambari.server.controller.AmbariServer.class.getDeclaredField("clusterController");
        field.setAccessible(true);
        field.set(null, amc);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(2L).anyTimes();
        try {
            EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster).anyTimes();
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
        }
        EasyMock.replay(amc, clusters, cluster);
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testJMXPropertyProviderAsClusterAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("ClusterAdmin", 2L));
        testPopulateResources();
        testPopulateResources_singleProperty();
        testPopulateResources_category();
        testPopulateResourcesWithUnknownPort();
        testPopulateResourcesUnhealthyResource();
        testPopulateResourcesMany();
        testPopulateResourcesTimeout();
        testPopulateResources_HAState_request();
    }

    @org.junit.Test
    public void testJMXPropertyProviderAsAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("Admin"));
        testPopulateResources();
        testPopulateResources_singleProperty();
        testPopulateResources_category();
        testPopulateResourcesWithUnknownPort();
        testPopulateResourcesUnhealthyResource();
        testPopulateResourcesMany();
        testPopulateResourcesTimeout();
        testPopulateResources_HAState_request();
    }

    @org.junit.Test
    public void testJMXPropertyProviderAsServiceAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator("ServiceAdmin", 2L));
        testPopulateResources();
        testPopulateResources_singleProperty();
        testPopulateResources_category();
        testPopulateResourcesWithUnknownPort();
        testPopulateResourcesUnhealthyResource();
        testPopulateResourcesMany();
        testPopulateResourcesTimeout();
        testPopulateResources_HAState_request();
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testJMXPropertyProviderAsViewUser() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser("ViewUser", 2L));
        testPopulateResources();
        testPopulateResources_singleProperty();
        testPopulateResources_category();
        testPopulateResourcesWithUnknownPort();
        testPopulateResourcesUnhealthyResource();
        testPopulateResourcesMany();
        testPopulateResourcesTimeout();
        testPopulateResources_HAState_request();
    }

    public void testPopulateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.jmxPropertyIdsWithHAState, streamProvider, hostProvider, metricsHostProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"));
        propertyProvider.setPopulateTimeout(5000);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> expectedSpecs = new java.util.ArrayList<>();
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx"));
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx?get=Hadoop:service=NameNode,name=FSNamesystem::tag.HAState"));
        org.junit.Assert.assertEquals(expectedSpecs, streamProvider.getSpecs());
        org.junit.Assert.assertEquals(13670605, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "ReceivedBytes")));
        org.junit.Assert.assertEquals(28, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/namenode", "CreateFileOps")));
        org.junit.Assert.assertEquals(1006632960, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryMax")));
        org.junit.Assert.assertEquals(473433016, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryUsed")));
        org.junit.Assert.assertEquals(136314880, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryMax")));
        org.junit.Assert.assertEquals(23634400, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryUsed")));
        org.junit.Assert.assertEquals(887717691390L, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/FSNamesystem", "CapacityTotal")));
        org.junit.Assert.assertEquals(184320, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/FSNamesystem", "CapacityUsed")));
        org.junit.Assert.assertEquals(842207944704L, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/FSNamesystem", "CapacityRemaining")));
        org.junit.Assert.assertEquals(45509562366L, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/FSNamesystem", "CapacityNonDFSUsed")));
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-14-ee-b3.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(propertyProvider.getSpec("http", "domu-12-31-39-14-ee-b3.compute-1.internal", "50075", "/jmx"), streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(856, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "ReceivedBytes")));
        org.junit.Assert.assertEquals(954466304, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryMax")));
        org.junit.Assert.assertEquals(9772616, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryUsed")));
        org.junit.Assert.assertEquals(136314880, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryMax")));
        org.junit.Assert.assertEquals(21933376, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryUsed")));
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-14-ee-b3.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "HBASE_MASTER");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        java.util.Set<java.lang.String> properties = new java.util.HashSet<>();
        properties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryMax"));
        properties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryUsed"));
        properties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryMax"));
        properties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryUsed"));
        properties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/load", "AverageLoad"));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(properties);
        propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(propertyProvider.getSpec("http", "domu-12-31-39-14-ee-b3.compute-1.internal", "60010", "/jmx"), streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(9, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertEquals(1069416448, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryMax")));
        org.junit.Assert.assertEquals(4806976, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryUsed")));
        org.junit.Assert.assertEquals(136314880, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryMax")));
        org.junit.Assert.assertEquals(28971240, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryUsed")));
        org.junit.Assert.assertEquals(3.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/load", "AverageLoad")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "gcCount")));
    }

    public void testPopulateResources_singleProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), streamProvider, hostProvider, metricsHostProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"));
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/rpc/ReceivedBytes"), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx"), streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(13670605, resource.getPropertyValue("metrics/rpc/ReceivedBytes"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/dfs/namenode/CreateFileOps"));
    }

    public void testPopulateResources_category() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), streamProvider, hostProvider, metricsHostProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"));
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/dfs"), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> expectedSpecs = new java.util.ArrayList<>();
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx"));
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx?get=Hadoop:service=NameNode,name=FSNamesystem::tag.HAState"));
        org.junit.Assert.assertEquals(expectedSpecs, streamProvider.getSpecs());
        org.junit.Assert.assertEquals(184320, resource.getPropertyValue("metrics/dfs/FSNamesystem/CapacityUsed"));
        org.junit.Assert.assertEquals(21, resource.getPropertyValue("metrics/dfs/FSNamesystem/UnderReplicatedBlocks"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/rpc/ReceivedBytes"));
    }

    public void testPopulateResources_HAState_request() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.jmxPropertyIdsWithHAState, streamProvider, hostProvider, metricsHostProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"));
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/dfs/FSNamesystem"), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> expectedSpecs = new java.util.ArrayList<>();
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx"));
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx?get=Hadoop:service=NameNode,name=FSNamesystem::tag.HAState"));
        org.junit.Assert.assertEquals(expectedSpecs, streamProvider.getSpecs());
        org.junit.Assert.assertEquals(184320, resource.getPropertyValue("metrics/dfs/FSNamesystem/CapacityUsed"));
        org.junit.Assert.assertEquals(21, resource.getPropertyValue("metrics/dfs/FSNamesystem/UnderReplicatedBlocks"));
        org.junit.Assert.assertEquals("customState", resource.getPropertyValue("metrics/dfs/FSNamesystem/HAState"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/rpc/ReceivedBytes"));
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        streamProvider.getSpecs().clear();
        temporalInfoMap = new java.util.HashMap<>();
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/dfs/FSNamesystem/CapacityUsed"), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        expectedSpecs.clear();
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx"));
        org.junit.Assert.assertEquals(expectedSpecs, streamProvider.getSpecs());
        org.junit.Assert.assertEquals(184320, resource.getPropertyValue("metrics/dfs/FSNamesystem/CapacityUsed"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/dfs/FSNamesystem/HAState"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/rpc/ReceivedBytes"));
    }

    public void testPopulateResourcesWithUnknownPort() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider(true);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), streamProvider, hostProvider, metricsHostProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"));
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> expectedSpecs = new java.util.ArrayList<>();
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx"));
        expectedSpecs.add(propertyProvider.getSpec("http", "domu-12-31-39-0e-34-e1.compute-1.internal", "50070", "/jmx?get=Hadoop:service=NameNode,name=FSNamesystem::tag.HAState"));
        org.junit.Assert.assertEquals(expectedSpecs, streamProvider.getSpecs());
        org.junit.Assert.assertEquals(13670605, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "ReceivedBytes")));
        org.junit.Assert.assertEquals(28, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/namenode", "CreateFileOps")));
        org.junit.Assert.assertEquals(1006632960, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryMax")));
        org.junit.Assert.assertEquals(473433016, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryUsed")));
        org.junit.Assert.assertEquals(136314880, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryMax")));
        org.junit.Assert.assertEquals(23634400, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryUsed")));
    }

    public void testPopulateResourcesUnhealthyResource() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider(true);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), streamProvider, hostProvider, metricsHostProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"));
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "INSTALLED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertNull(streamProvider.getLastSpec());
    }

    public void testPopulateResourcesMany() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider(50L);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider(true);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), streamProvider, hostProvider, metricsHostProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"));
        propertyProvider.setPopulateTimeout(5000);
        for (int i = 0; i < org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.NUMBER_OF_RESOURCES; ++i) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
            resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
            resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-14-ee-b3.compute-1.internal");
            resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "DATANODE");
            resource.setProperty("unique_id", i);
            resources.add(resource);
        }
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = propertyProvider.populateResources(resources, request, null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.NUMBER_OF_RESOURCES, resourceSet.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resourceSet) {
            org.junit.Assert.assertEquals(856, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "ReceivedBytes")));
            org.junit.Assert.assertEquals(954466304, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryMax")));
            org.junit.Assert.assertEquals(9772616, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryUsed")));
            org.junit.Assert.assertEquals(136314880, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryMax")));
            org.junit.Assert.assertEquals(21933376, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryUsed")));
        }
    }

    public void testPopulateResourcesTimeout() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider(100L);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider(true);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider propertyProvider = org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), streamProvider, hostProvider, metricsHostProvider, "HostRoles/cluster_name", "HostRoles/host_name", "HostRoles/component_name", "HostRoles/state");
        propertyProvider.setPopulateTimeout(50L);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-14-ee-b3.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        resources.add(resource);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = propertyProvider.populateResources(resources, request, null);
        java.lang.Thread.sleep(150L);
        org.junit.Assert.assertEquals(0, resourceSet.size());
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/rpc/ReceivedBytes"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/jvm/HeapMemoryMax"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/jvm/HeapMemoryUsed"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/jvm/NonHeapMemoryMax"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/jvm/NonHeapMemoryUsed"));
    }

    public static class TestJMXHostProvider implements org.apache.ambari.server.controller.jmx.JMXHostProvider {
        private final boolean unknownPort;

        public TestJMXHostProvider(boolean unknownPort) {
            this.unknownPort = unknownPort;
        }

        @java.lang.Override
        public java.lang.String getPublicHostName(final java.lang.String clusterName, final java.lang.String hostName) {
            return null;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> getHostNames(java.lang.String clusterName, java.lang.String componentName) {
            return null;
        }

        @java.lang.Override
        public java.lang.String getPort(java.lang.String clusterName, java.lang.String componentName, java.lang.String hostName, boolean httpsEnabled) {
            return getPort(clusterName, componentName, hostName);
        }

        public java.lang.String getPort(java.lang.String clusterName, java.lang.String componentName, java.lang.String hostName) {
            if (unknownPort) {
                return null;
            }
            if (componentName.equals("NAMENODE")) {
                return "50070";
            } else if (componentName.equals("DATANODE")) {
                return "50075";
            } else if (componentName.equals("HBASE_MASTER")) {
                if (clusterName.equals("c2")) {
                    return "60011";
                } else {
                    return "60010";
                }
            } else if (componentName.equals("JOURNALNODE")) {
                return "8480";
            } else if (componentName.equals("STORM_REST_API")) {
                return "8745";
            } else {
                return null;
            }
        }

        @java.lang.Override
        public java.lang.String getJMXProtocol(java.lang.String clusterName, java.lang.String componentName) {
            return "http";
        }

        @java.lang.Override
        public java.lang.String getJMXRpcMetricTag(java.lang.String clusterName, java.lang.String componentName, java.lang.String port) {
            return null;
        }
    }

    public static class TestMetricHostProvider implements org.apache.ambari.server.controller.metrics.MetricHostProvider {
        @java.lang.Override
        public java.lang.String getCollectorHostName(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return null;
        }

        @java.lang.Override
        public java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) {
            return null;
        }

        @java.lang.Override
        public java.lang.String getCollectorPort(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return null;
        }

        @java.lang.Override
        public boolean isCollectorHostLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return false;
        }

        @java.lang.Override
        public boolean isCollectorComponentLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return false;
        }

        @java.lang.Override
        public boolean isCollectorHostExternal(java.lang.String clusterName) {
            return false;
        }
    }
}