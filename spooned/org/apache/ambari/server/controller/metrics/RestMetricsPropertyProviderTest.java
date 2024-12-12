package org.apache.ambari.server.controller.metrics;
import org.easymock.EasyMock;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class RestMetricsPropertyProviderTest {
    public static final java.lang.String WRAPPED_METRICS_KEY = "WRAPPED_METRICS_KEY";

    protected static final java.lang.String HOST_COMPONENT_HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name");

    protected static final java.lang.String HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name");

    protected static final java.lang.String HOST_COMPONENT_STATE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state");

    protected static final java.util.Map<java.lang.String, java.lang.String> metricsProperties = new java.util.HashMap<>();

    protected static final java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> componentMetrics = new java.util.HashMap<>();

    private static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name");

    private static final java.lang.String DEFAULT_STORM_UI_PORT = "8745";

    public static final int NUMBER_OF_RESOURCES = 400;

    private static final int METRICS_SERVICE_TIMEOUT = 10;

    private static com.google.inject.Injector injector;

    private static org.apache.ambari.server.state.Clusters clusters;

    private static org.apache.ambari.server.state.Cluster c1;

    private static org.apache.ambari.server.controller.AmbariManagementController amc;

    private static org.apache.ambari.server.state.services.MetricsRetrievalService metricsRetrievalService;

    {
        metricsProperties.put("default_port", DEFAULT_STORM_UI_PORT);
        metricsProperties.put("port_config_type", "storm-site");
        metricsProperties.put("port_property_name", "ui.port");
        metricsProperties.put("protocol", "http");
        componentMetrics.put("metrics/api/cluster/summary/tasks.total", new org.apache.ambari.server.state.stack.Metric("/api/cluster/summary##tasks.total", false, false, false, "unitless"));
        componentMetrics.put("metrics/api/cluster/summary/slots.total", new org.apache.ambari.server.state.stack.Metric("/api/cluster/summary##slots.total", false, false, false, "unitless"));
        componentMetrics.put("metrics/api/cluster/summary/slots.free", new org.apache.ambari.server.state.stack.Metric("/api/cluster/summary##slots.free", false, false, false, "unitless"));
        componentMetrics.put("metrics/api/cluster/summary/supervisors", new org.apache.ambari.server.state.stack.Metric("/api/cluster/summary##supervisors", false, false, false, "unitless"));
        componentMetrics.put("metrics/api/cluster/summary/executors.total", new org.apache.ambari.server.state.stack.Metric("/api/cluster/summary##executors.total", false, false, false, "unitless"));
        componentMetrics.put("metrics/api/cluster/summary/slots.used", new org.apache.ambari.server.state.stack.Metric("/api/cluster/summary##slots.used", false, false, false, "unitless"));
        componentMetrics.put("metrics/api/cluster/summary/topologies", new org.apache.ambari.server.state.stack.Metric("/api/cluster/summary##topologies", false, false, false, "unitless"));
        componentMetrics.put("metrics/api/cluster/summary/nimbus.uptime", new org.apache.ambari.server.state.stack.Metric("/api/cluster/summary##nimbus.uptime", false, false, false, "unitless"));
        componentMetrics.put("metrics/api/cluster/summary/wrong.metric", new org.apache.ambari.server.state.stack.Metric(null, false, false, false, "unitless"));
    }

    @org.junit.BeforeClass
    public static void setup() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.clusters = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName("HDP");
        stackEntity.setStackVersion("2.1.1");
        stackDAO.create(stackEntity);
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.clusters.addCluster("c1", new org.apache.ambari.server.state.StackId("HDP-2.1.1"));
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.c1 = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.clusters.getCluster("c1");
        org.apache.ambari.server.configuration.Configuration configuration = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_REQUEST_TTL_ENABLED.getKey(), "false");
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider.init(configuration);
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsRetrievalService = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector.getInstance(org.apache.ambari.server.state.services.MetricsRetrievalService.class);
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsRetrievalService.startAsync();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsRetrievalService.awaitRunning(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsRetrievalService.setThreadPoolExecutor(new org.apache.ambari.server.utils.SynchronousThreadPoolExecutor());
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.amc = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.lang.reflect.Field field = org.apache.ambari.server.controller.AmbariServer.class.getDeclaredField("clusterController");
        field.setAccessible(true);
        field.set(null, org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.amc);
        org.apache.ambari.server.state.ConfigHelper configHelperMock = EasyMock.createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.amc.getClusters()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.clusters).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.amc.getAmbariEventPublisher()).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class)).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.amc.findConfigurationTagsWithOverrides(EasyMock.eq(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.c1), EasyMock.anyString(), EasyMock.anyObject())).andReturn(java.util.Collections.singletonMap("storm-site", java.util.Collections.singletonMap("tag", "version1"))).anyTimes();
        EasyMock.expect(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.amc.getConfigHelper()).andReturn(configHelperMock).anyTimes();
        EasyMock.expect(configHelperMock.getEffectiveConfigProperties(EasyMock.eq(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.c1), org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.singletonMap("storm-site", java.util.Collections.singletonMap("ui.port", org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.DEFAULT_STORM_UI_PORT))).anyTimes();
        EasyMock.replay(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.amc, configHelperMock);
    }

    @org.junit.AfterClass
    public static void after() throws org.apache.ambari.server.AmbariException, java.sql.SQLException, java.util.concurrent.TimeoutException {
        if ((org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsRetrievalService != null) && org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsRetrievalService.isRunning()) {
            org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsRetrievalService.stopAsync();
            org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsRetrievalService.awaitTerminated(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        }
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector);
    }

    private org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider createRestMetricsPropertyProvider(org.apache.ambari.server.state.stack.MetricDefinition metricDefinition, java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider metricsHostProvider) throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory factory = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector.getInstance(org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory.class);
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider restMetricsPropertyProvider = factory.createRESTMetricsPropertyProvider(metricDefinition.getProperties(), componentMetrics, streamProvider, metricsHostProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), "STORM_REST_API");
        java.lang.reflect.Field field = org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider.class.getDeclaredField("amc");
        field.setAccessible(true);
        field.set(restMetricsPropertyProvider, org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.amc);
        return restMetricsPropertyProvider;
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testRestMetricsPropertyProviderAsClusterAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("ClusterAdmin", 2L));
        testPopulateResources();
        testPopulateResources_singleProperty();
        testPopulateResources_category();
        testPopulateResourcesUnhealthyResource();
        testPopulateResourcesMany();
        testPopulateResourcesTimeout();
    }

    @org.junit.Test
    public void testRestMetricsPropertyProviderAsAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("Admin"));
        testPopulateResources();
        testPopulateResources_singleProperty();
        testPopulateResources_category();
        testPopulateResourcesUnhealthyResource();
        testPopulateResourcesMany();
        testPopulateResourcesTimeout();
    }

    @org.junit.Test
    public void testRestMetricsPropertyProviderAsServiceAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator("ServiceAdmin", 2L));
        testPopulateResources();
        testPopulateResources_singleProperty();
        testPopulateResources_category();
        testPopulateResourcesUnhealthyResource();
        testPopulateResourcesMany();
        testPopulateResourcesTimeout();
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testRestMetricsPropertyProviderAsViewUser() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser("ViewUser", 2L));
        testPopulateResources();
        testPopulateResources_singleProperty();
        testPopulateResources_category();
        testPopulateResourcesUnhealthyResource();
        testPopulateResourcesMany();
        testPopulateResourcesTimeout();
    }

    @org.junit.Test
    public void testResolvePort() throws java.lang.Exception {
        org.apache.ambari.server.state.stack.MetricDefinition metricDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.MetricDefinition.class);
        EasyMock.expect(metricDefinition.getMetrics()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.componentMetrics);
        EasyMock.expect(metricDefinition.getType()).andReturn("org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider");
        EasyMock.expect(metricDefinition.getProperties()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        EasyMock.replay(metricDefinition);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(metricDefinition);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
        componentMetrics.put(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.WRAPPED_METRICS_KEY, metrics);
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider restMetricsPropertyProvider = createRestMetricsPropertyProvider(metricDefinition, componentMetrics, streamProvider, metricsHostProvider);
        java.util.Map<java.lang.String, java.lang.String> customMetricsProperties = new java.util.HashMap<>(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        customMetricsProperties.put("port_property_name", "wrong_property");
        java.lang.String resolvedPort = restMetricsPropertyProvider.resolvePort(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.c1, "domu-12-31-39-0e-34-e1.compute-1.internal", "STORM_REST_API", customMetricsProperties, "http");
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.DEFAULT_STORM_UI_PORT, resolvedPort);
        customMetricsProperties = new java.util.HashMap<>(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        customMetricsProperties.put("default_port", "8746");
        resolvedPort = restMetricsPropertyProvider.resolvePort(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.c1, "domu-12-31-39-0e-34-e1.compute-1.internal", "STORM_REST_API", customMetricsProperties, "http");
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.DEFAULT_STORM_UI_PORT, resolvedPort);
    }

    public void testPopulateResources() throws java.lang.Exception {
        org.apache.ambari.server.state.stack.MetricDefinition metricDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.MetricDefinition.class);
        EasyMock.expect(metricDefinition.getMetrics()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.componentMetrics);
        EasyMock.expect(metricDefinition.getType()).andReturn("org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider");
        EasyMock.expect(metricDefinition.getProperties()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        EasyMock.replay(metricDefinition);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(metricDefinition);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
        componentMetrics.put(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.WRAPPED_METRICS_KEY, metrics);
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider restMetricsPropertyProvider = createRestMetricsPropertyProvider(metricDefinition, componentMetrics, streamProvider, metricsHostProvider);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "STORM_REST_API");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, restMetricsPropertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "wrong.metric")));
        org.junit.Assert.assertEquals(28.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "tasks.total")));
        org.junit.Assert.assertEquals(8.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "slots.total")));
        org.junit.Assert.assertEquals(5.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "slots.free")));
        org.junit.Assert.assertEquals(2.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "supervisors")));
        org.junit.Assert.assertEquals(28.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "executors.total")));
        org.junit.Assert.assertEquals(3.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "slots.used")));
        org.junit.Assert.assertEquals(1.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "topologies")));
        org.junit.Assert.assertEquals(4637.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "nimbus.uptime")));
    }

    public void testPopulateResources_singleProperty() throws java.lang.Exception {
        org.apache.ambari.server.state.stack.MetricDefinition metricDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.MetricDefinition.class);
        EasyMock.expect(metricDefinition.getMetrics()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.componentMetrics);
        EasyMock.expect(metricDefinition.getType()).andReturn("org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider");
        EasyMock.expect(metricDefinition.getProperties()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        EasyMock.replay(metricDefinition);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(metricDefinition);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
        componentMetrics.put(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.WRAPPED_METRICS_KEY, metrics);
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider restMetricsPropertyProvider = createRestMetricsPropertyProvider(metricDefinition, componentMetrics, streamProvider, metricsHostProvider);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "STORM_REST_API");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, restMetricsPropertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(28.0, resource.getPropertyValue("metrics/api/cluster/summary/tasks.total"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/api/cluster/summary/taskstotal"));
    }

    public void testPopulateResources_category() throws java.lang.Exception {
        org.apache.ambari.server.state.stack.MetricDefinition metricDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.MetricDefinition.class);
        EasyMock.expect(metricDefinition.getMetrics()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.componentMetrics);
        EasyMock.expect(metricDefinition.getType()).andReturn("org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider");
        EasyMock.expect(metricDefinition.getProperties()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        EasyMock.replay(metricDefinition);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(metricDefinition);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
        componentMetrics.put(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.WRAPPED_METRICS_KEY, metrics);
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider restMetricsPropertyProvider = createRestMetricsPropertyProvider(metricDefinition, componentMetrics, streamProvider, metricsHostProvider);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "STORM_REST_API");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/api/cluster"), temporalInfoMap);
        org.junit.Assert.assertEquals(1, restMetricsPropertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(28.0, resource.getPropertyValue("metrics/api/cluster/summary/tasks.total"));
        org.junit.Assert.assertEquals(2.0, resource.getPropertyValue("metrics/api/cluster/summary/supervisors"));
        org.junit.Assert.assertNull(resource.getPropertyValue("metrics/api/cluster/summary/taskstotal"));
    }

    public void testPopulateResourcesUnhealthyResource() throws java.lang.Exception {
        org.apache.ambari.server.state.stack.MetricDefinition metricDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.MetricDefinition.class);
        EasyMock.expect(metricDefinition.getMetrics()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.componentMetrics);
        EasyMock.expect(metricDefinition.getType()).andReturn("org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider");
        EasyMock.expect(metricDefinition.getProperties()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        EasyMock.replay(metricDefinition);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(metricDefinition);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
        componentMetrics.put(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.WRAPPED_METRICS_KEY, metrics);
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider restMetricsPropertyProvider = createRestMetricsPropertyProvider(metricDefinition, componentMetrics, streamProvider, metricsHostProvider);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "STORM_REST_API");
        resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "INSTALLED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, restMetricsPropertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertNull(streamProvider.getLastSpec());
    }

    public void testPopulateResourcesMany() throws java.lang.Exception {
        org.apache.ambari.server.state.stack.MetricDefinition metricDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.MetricDefinition.class);
        EasyMock.expect(metricDefinition.getMetrics()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.componentMetrics);
        EasyMock.expect(metricDefinition.getType()).andReturn("org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider");
        EasyMock.expect(metricDefinition.getProperties()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        EasyMock.replay(metricDefinition);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(metricDefinition);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
        componentMetrics.put(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.WRAPPED_METRICS_KEY, metrics);
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider restMetricsPropertyProvider = createRestMetricsPropertyProvider(metricDefinition, componentMetrics, streamProvider, metricsHostProvider);
        for (int i = 0; i < org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.NUMBER_OF_RESOURCES; ++i) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
            resource.setProperty("HostRoles/cluster_name", "c1");
            resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
            resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "STORM_REST_API");
            resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
            resource.setProperty("unique_id", i);
            resources.add(resource);
        }
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = restMetricsPropertyProvider.populateResources(resources, request, null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.NUMBER_OF_RESOURCES, resourceSet.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resourceSet) {
            org.junit.Assert.assertEquals(28.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "tasks.total")));
            org.junit.Assert.assertEquals(8.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "slots.total")));
            org.junit.Assert.assertEquals(5.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "slots.free")));
            org.junit.Assert.assertEquals(2.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "supervisors")));
        }
    }

    public void testPopulateResourcesTimeout() throws java.lang.Exception {
        org.apache.ambari.server.state.stack.MetricDefinition metricDefinition = EasyMock.createNiceMock(org.apache.ambari.server.state.stack.MetricDefinition.class);
        EasyMock.expect(metricDefinition.getMetrics()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.componentMetrics);
        EasyMock.expect(metricDefinition.getType()).andReturn("org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider");
        EasyMock.expect(metricDefinition.getProperties()).andReturn(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.metricsProperties);
        EasyMock.replay(metricDefinition);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.getPropertyInfo(metricDefinition);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<>();
        componentMetrics.put(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.WRAPPED_METRICS_KEY, metrics);
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider(100L);
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.TestMetricsHostProvider();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider restMetricsPropertyProvider = createRestMetricsPropertyProvider(metricDefinition, componentMetrics, streamProvider, metricsHostProvider);
        restMetricsPropertyProvider.setPopulateTimeout(-1L);
        try {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
            resource.setProperty("HostRoles/cluster_name", "c1");
            resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
            resource.setProperty(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "STORM_REST_API");
            resources.add(resource);
            org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = restMetricsPropertyProvider.populateResources(resources, request, null);
            java.lang.Thread.sleep(150L);
            org.junit.Assert.assertEquals(0, resourceSet.size());
            org.junit.Assert.assertNull(resource.getPropertyValue("metrics/api/cluster/summary/tasks.total"));
            org.junit.Assert.assertNull(resource.getPropertyValue("metrics/api/cluster/summary/supervisors"));
        } finally {
            restMetricsPropertyProvider.setPopulateTimeout(org.apache.ambari.server.controller.metrics.RestMetricsPropertyProviderTest.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class).getPropertyProvidersCompletionServiceTimeout());
        }
    }

    public static class TestMetricsHostProvider implements org.apache.ambari.server.controller.metrics.MetricHostProvider {
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