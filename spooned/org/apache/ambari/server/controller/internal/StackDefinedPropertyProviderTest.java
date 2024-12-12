package org.apache.ambari.server.controller.internal;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class StackDefinedPropertyProviderTest {
    private static final java.lang.String HOST_COMPONENT_HOST_NAME_PROPERTY_ID = "HostRoles/host_name";

    private static final java.lang.String HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID = "HostRoles/component_name";

    private static final java.lang.String HOST_COMPONENT_STATE_PROPERTY_ID = "HostRoles/state";

    private static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name");

    private static final int METRICS_SERVICE_TIMEOUT = 10;

    private org.apache.ambari.server.state.Clusters clusters = null;

    private com.google.inject.Injector injector = null;

    private org.apache.ambari.server.orm.OrmTestHelper helper = null;

    private static org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory;

    private static org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider;

    private static org.apache.ambari.server.state.services.MetricsRetrievalService metricsRetrievalService;

    @org.junit.BeforeClass
    public static void setupCache() {
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.cacheEntryFactory = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory(new org.apache.ambari.server.configuration.Configuration());
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.cacheProvider = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider(new org.apache.ambari.server.configuration.Configuration(), org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.cacheEntryFactory);
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.controller.jmx.JMXPropertyProvider.init(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
    }

    public class TestModuleWithCacheProvider implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class).toInstance(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.cacheProvider);
        }
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(module).with(new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestModuleWithCacheProvider()));
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider.init(injector);
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.metricsRetrievalService = injector.getInstance(org.apache.ambari.server.state.services.MetricsRetrievalService.class);
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.metricsRetrievalService.startAsync();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.metricsRetrievalService.awaitRunning(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.metricsRetrievalService.setThreadPoolExecutor(new org.apache.ambari.server.utils.SynchronousThreadPoolExecutor());
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.5");
        clusters.addCluster("c2", stackId);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c2");
        cluster.setDesiredStackVersion(stackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.state.Service service = cluster.addService("HDFS", repositoryVersion);
        service.addServiceComponent("NAMENODE");
        service.addServiceComponent("DATANODE");
        service.addServiceComponent("JOURNALNODE");
        service = cluster.addService("YARN", repositoryVersion);
        service.addServiceComponent("RESOURCEMANAGER");
        service = cluster.addService("HBASE", repositoryVersion);
        service.addServiceComponent("HBASE_MASTER");
        service.addServiceComponent("HBASE_REGIONSERVER");
        stackId = new org.apache.ambari.server.state.StackId("HDP-2.1.1");
        repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        service = cluster.addService("STORM", repositoryVersion);
        service.addServiceComponent("STORM_REST_API");
        clusters.addHost("h1");
        org.apache.ambari.server.state.Host host = clusters.getHost("h1");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        host.setHostAttributes(hostAttributes);
        clusters.mapHostToCluster("h1", "c2");
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.lang.reflect.Field field = org.apache.ambari.server.controller.AmbariServer.class.getDeclaredField("clusterController");
        field.setAccessible(true);
        field.set(null, amc);
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getAmbariEventPublisher()).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class)).anyTimes();
        EasyMock.expect(amc.getClusters()).andReturn(clustersMock).anyTimes();
        EasyMock.expect(clustersMock.getCluster(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID)).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getResourceId()).andReturn(2L).anyTimes();
        try {
            EasyMock.expect(clustersMock.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(clusterMock).anyTimes();
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
        }
        EasyMock.replay(amc, clustersMock, clusterMock);
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        if ((org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.metricsRetrievalService != null) && org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.metricsRetrievalService.isRunning()) {
            org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.metricsRetrievalService.stopAsync();
            org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.metricsRetrievalService.awaitTerminated(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        }
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testStackDefinedPropertyProviderAsClusterAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("ClusterAdmin", 2L));
        testPopulateHostComponentResources();
        testCustomProviders();
        testPopulateResources_HDP2();
        testPopulateResources_HDP2_params();
        testPopulateResources_HDP2_params_singleProperty();
        testPopulateResources_HDP2_params_category();
        testPopulateResources_HDP2_params_category2();
        testPopulateResources_jmx_JournalNode();
        testPopulateResources_jmx_Storm();
        testPopulateResources_NoRegionServer();
        testPopulateResources_HBaseMaster2();
        testPopulateResources_params_category5();
        testPopulateResources_ganglia_JournalNode();
        testPopulateResources_resourcemanager_clustermetrics();
        testPopulateResourcesWithAggregateFunctionMetrics();
    }

    @org.junit.Test
    public void testStackDefinedPropertyProviderAsAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("Admin"));
        testPopulateHostComponentResources();
        testCustomProviders();
        testPopulateResources_HDP2();
        testPopulateResources_HDP2_params();
        testPopulateResources_HDP2_params_singleProperty();
        testPopulateResources_HDP2_params_category();
        testPopulateResources_HDP2_params_category2();
        testPopulateResources_jmx_JournalNode();
        testPopulateResources_jmx_Storm();
        testPopulateResources_NoRegionServer();
        testPopulateResources_HBaseMaster2();
        testPopulateResources_params_category5();
        testPopulateResources_ganglia_JournalNode();
        testPopulateResources_resourcemanager_clustermetrics();
        testPopulateResourcesWithAggregateFunctionMetrics();
    }

    @org.junit.Test
    public void testStackDefinedPropertyProviderAsServiceAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator("ServiceAdmin", 2L));
        testPopulateHostComponentResources();
        testCustomProviders();
        testPopulateResources_HDP2();
        testPopulateResources_HDP2_params();
        testPopulateResources_HDP2_params_singleProperty();
        testPopulateResources_HDP2_params_category();
        testPopulateResources_HDP2_params_category2();
        testPopulateResources_jmx_JournalNode();
        testPopulateResources_jmx_Storm();
        testPopulateResources_NoRegionServer();
        testPopulateResources_HBaseMaster2();
        testPopulateResources_params_category5();
        testPopulateResources_ganglia_JournalNode();
        testPopulateResources_resourcemanager_clustermetrics();
        testPopulateResourcesWithAggregateFunctionMetrics();
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testStackDefinedPropertyProviderAsViewUser() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser("ViewUser", 2L));
        testPopulateHostComponentResources();
        testCustomProviders();
        testPopulateResources_HDP2();
        testPopulateResources_HDP2_params();
        testPopulateResources_HDP2_params_singleProperty();
        testPopulateResources_HDP2_params_category();
        testPopulateResources_HDP2_params_category2();
        testPopulateResources_jmx_JournalNode();
        testPopulateResources_jmx_Storm();
        testPopulateResources_NoRegionServer();
        testPopulateResources_HBaseMaster2();
        testPopulateResources_params_category5();
        testPopulateResources_ganglia_JournalNode();
        testPopulateResources_resourcemanager_clustermetrics();
        testPopulateResourcesWithAggregateFunctionMetrics();
    }

    public void testPopulateHostComponentResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider tj = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(true);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider tm = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider sdpp = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, tj, tm, serviceProvider, new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CombinedStreamProvider(), "HostRoles/cluster_name", "HostRoles/host_name", "HostRoles/component_name", "HostRoles/state", null, null);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty("HostRoles/host_name", "h1");
        resource.setProperty("HostRoles/component_name", "NAMENODE");
        resource.setProperty("HostRoles/state", "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet(), new java.util.HashMap<>());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> set = sdpp.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, set.size());
        org.apache.ambari.server.controller.spi.Resource res = set.iterator().next();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> values = res.getPropertiesMap();
        org.junit.Assert.assertTrue("Expected JMX metric 'metrics/dfs/FSNamesystem'", values.containsKey("metrics/dfs/FSNamesystem"));
        org.junit.Assert.assertTrue("Expected JMX metric 'metrics/dfs/namenode'", values.containsKey("metrics/dfs/namenode"));
        org.junit.Assert.assertTrue("Expected Ganglia metric 'metrics/rpcdetailed/client'", values.containsKey("metrics/rpcdetailed/client"));
        org.junit.Assert.assertTrue("Expected Ganglia metric 'metrics/rpc/client'", values.containsKey("metrics/rpc/client"));
    }

    public void testCustomProviders() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider sdpp = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, null, null, null, new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CombinedStreamProvider(), "HostRoles/cluster_name", "HostRoles/host_name", "HostRoles/component_name", "HostRoles/state", new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty("HostRoles/host_name", "h1");
        resource.setProperty("HostRoles/component_name", "DATANODE");
        resource.setProperty("HostRoles/state", "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet(), new java.util.HashMap<>());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> set = sdpp.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, set.size());
        org.apache.ambari.server.controller.spi.Resource res = set.iterator().next();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> values = res.getPropertiesMap();
        org.junit.Assert.assertTrue(values.containsKey("foo/type1"));
        org.junit.Assert.assertTrue(values.containsKey("foo/type2"));
        org.junit.Assert.assertTrue(values.containsKey("foo/type3"));
        org.junit.Assert.assertFalse(values.containsKey("foo/type4"));
        org.junit.Assert.assertTrue(values.get("foo/type1").containsKey("name"));
        org.junit.Assert.assertTrue(values.get("foo/type2").containsKey("name"));
        org.junit.Assert.assertTrue(values.get("foo/type3").containsKey("name"));
        org.junit.Assert.assertEquals("value1", values.get("foo/type1").get("name"));
    }

    private static class CombinedStreamProvider extends org.apache.ambari.server.controller.internal.URLStreamProvider {
        public CombinedStreamProvider() {
            super(1000, 1000, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance());
        }

        @java.lang.Override
        public java.io.InputStream readFrom(java.lang.String spec) throws java.io.IOException {
            if (spec.indexOf("jmx") > (-1)) {
                return java.lang.ClassLoader.getSystemResourceAsStream("hdfs_namenode_jmx.json");
            } else {
                return java.lang.ClassLoader.getSystemResourceAsStream("temporal_ganglia_data.txt");
            }
        }

        @java.lang.Override
        public java.io.InputStream readFrom(java.lang.String spec, java.lang.String requestMethod, java.lang.String params) throws java.io.IOException {
            return readFrom(spec);
        }
    }

    private static class EmptyPropertyProvider implements org.apache.ambari.server.controller.spi.PropertyProvider {
        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
            return null;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            return null;
        }
    }

    public static class CustomMetricProvider1 implements org.apache.ambari.server.controller.spi.PropertyProvider {
        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
            for (org.apache.ambari.server.controller.spi.Resource r : resources) {
                r.setProperty("foo/type1/name", "value1");
            }
            return resources;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            return java.util.Collections.emptySet();
        }
    }

    public static class CustomMetricProvider2 implements org.apache.ambari.server.controller.spi.PropertyProvider {
        private java.util.Map<java.lang.String, java.lang.String> providerProperties = null;

        public CustomMetricProvider2(java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> metrics) {
            providerProperties = properties;
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
            for (org.apache.ambari.server.controller.spi.Resource r : resources) {
                r.setProperty("foo/type2/name", providerProperties.get("Type2.Metric.Name"));
            }
            return resources;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            return java.util.Collections.emptySet();
        }
    }

    public static class CustomMetricProvider3 implements org.apache.ambari.server.controller.spi.PropertyProvider {
        private static org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CustomMetricProvider3 instance = null;

        private java.util.Map<java.lang.String, java.lang.String> providerProperties = new java.util.HashMap<>();

        public static org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CustomMetricProvider3 getInstance(java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, org.apache.ambari.server.state.stack.Metric> metrics) {
            if (null == org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CustomMetricProvider3.instance) {
                org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CustomMetricProvider3.instance = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CustomMetricProvider3();
                org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CustomMetricProvider3.instance.providerProperties.putAll(properties);
            }
            return org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.CustomMetricProvider3.instance;
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
            for (org.apache.ambari.server.controller.spi.Resource r : resources) {
                r.setProperty("foo/type3/name", providerProperties.get("Type3.Metric.Name"));
            }
            return resources;
        }

        @java.lang.Override
        public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
            return java.util.Collections.emptySet();
        }
    }

    public void testPopulateResources_HDP2() throws java.lang.Exception {
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(true);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, metricsHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "RESOURCEMANAGER");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(6, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(6, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(8192, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableMB")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableVCores")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AppsSubmitted")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/ClusterMetrics", "NumActiveNMs")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/ClusterMetrics", "NumDecommissionedNMs")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/ClusterMetrics", "NumLostNMs")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/ClusterMetrics", "NumUnhealthyNMs")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/ClusterMetrics", "NumRebootedNMs")));
        org.junit.Assert.assertEquals(932118528, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryMax")));
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
    }

    public void testPopulateResources_HDP2_params() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, metricsHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "RESOURCEMANAGER");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(6, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(6, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(8192, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableMB")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableVCores")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AppsSubmitted")));
        org.junit.Assert.assertEquals(15, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(12, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(8192, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AvailableMB")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AvailableVCores")));
        org.junit.Assert.assertEquals(47, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AppsSubmitted")));
        org.junit.Assert.assertEquals(4, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(4, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(6048, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AvailableMB")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AvailableVCores")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AppsSubmitted")));
    }

    public void testPopulateResources_HDP2_params_singleProperty() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, metricsHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "RESOURCEMANAGER");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/yarn/Queue/root/AvailableMB"), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(8192, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableMB")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableVCores")));
    }

    public void testPopulateResources_HDP2_params_category() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, metricsHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "RESOURCEMANAGER");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/yarn/Queue"), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(6, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(6, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(8192, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableMB")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableVCores")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AppsSubmitted")));
        org.junit.Assert.assertEquals(15, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(12, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(8192, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AvailableMB")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AvailableVCores")));
        org.junit.Assert.assertEquals(47, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AppsSubmitted")));
        org.junit.Assert.assertEquals(4, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(4, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(6048, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AvailableMB")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AvailableVCores")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AppsSubmitted")));
    }

    public void testPopulateResources_HDP2_params_category2() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, metricsHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "RESOURCEMANAGER");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/yarn/Queue/root/default"), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AggregateContainersAllocated")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AggregateContainersReleased")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableMB")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AvailableVCores")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root", "AppsSubmitted")));
        org.junit.Assert.assertEquals(15, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(12, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(8192, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AvailableMB")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AvailableVCores")));
        org.junit.Assert.assertEquals(47, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default", "AppsSubmitted")));
        org.junit.Assert.assertEquals(99, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default/sub_queue", "AggregateContainersAllocated")));
        org.junit.Assert.assertEquals(98, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default/sub_queue", "AggregateContainersReleased")));
        org.junit.Assert.assertEquals(9898, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default/sub_queue", "AvailableMB")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default/sub_queue", "AvailableVCores")));
        org.junit.Assert.assertEquals(97, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/default/sub_queue", "AppsSubmitted")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AggregateContainersAllocated")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AggregateContainersReleased")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AvailableMB")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AvailableVCores")));
        org.junit.Assert.assertNull(resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/yarn/Queue/root/second_queue", "AppsSubmitted")));
    }

    public void testPopulateResources_jmx_JournalNode() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, metricsHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "JOURNALNODE");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(1377795104272L, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "startTime")));
        org.junit.Assert.assertEquals(954466304, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryMax")));
        org.junit.Assert.assertEquals(14569736, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "HeapMemoryUsed")));
        org.junit.Assert.assertEquals(136314880, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryMax")));
        org.junit.Assert.assertEquals(24993392, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "NonHeapMemoryUsed")));
        org.junit.Assert.assertEquals(9100, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "gcCount")));
        org.junit.Assert.assertEquals(31641, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "gcTimeMillis")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "logError")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "logFatal")));
        org.junit.Assert.assertEquals(4163, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "logInfo")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "logWarn")));
        org.junit.Assert.assertEquals(29.8125, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "memHeapCommittedM")));
        org.junit.Assert.assertEquals(13.894783, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "memHeapUsedM")));
        org.junit.Assert.assertEquals(24.9375, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "memNonHeapCommittedM")));
        org.junit.Assert.assertEquals(23.835556, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "memNonHeapUsedM")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "threadsBlocked")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "threadsNew")));
        org.junit.Assert.assertEquals(6, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "threadsRunnable")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "threadsTerminated")));
        org.junit.Assert.assertEquals(3, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "threadsTimedWaiting")));
        org.junit.Assert.assertEquals(8, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/jvm", "threadsWaiting")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "NumOpenConnections")));
        org.junit.Assert.assertEquals(4928861, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "ReceivedBytes")));
        org.junit.Assert.assertEquals(13.211112159230245, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "RpcProcessingTime_avg_time")));
        org.junit.Assert.assertEquals(25067, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "RpcProcessingTime_num_ops")));
        org.junit.Assert.assertEquals(0.19686821997924706, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "RpcQueueTime_avg_time")));
        org.junit.Assert.assertEquals(25067, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "RpcQueueTime_num_ops")));
        org.junit.Assert.assertEquals(6578899, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "SentBytes")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "callQueueLen")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "rpcAuthenticationFailures")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "rpcAuthenticationSuccesses")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "rpcAuthorizationFailures")));
        org.junit.Assert.assertEquals(12459, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "rpcAuthorizationSuccesses")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "getJournalState_num_ops")));
        org.junit.Assert.assertEquals(0.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "getJournalState_avg_time")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "newEpoch_num_ops")));
        org.junit.Assert.assertEquals(60.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "newEpoch_avg_time")));
        org.junit.Assert.assertEquals(4129, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "startLogSegment_num_ops")));
        org.junit.Assert.assertEquals(38.25951359084413, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "startLogSegment_avg_time")));
        org.junit.Assert.assertEquals(8265, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "journal_num_ops")));
        org.junit.Assert.assertEquals(2.1832618025751187, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "journal_avg_time")));
        org.junit.Assert.assertEquals(4129, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "finalizeLogSegment_num_ops")));
        org.junit.Assert.assertEquals(11.575679542203101, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "finalizeLogSegment_avg_time")));
        org.junit.Assert.assertEquals(8536, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "getEditLogManifest_num_ops")));
        org.junit.Assert.assertEquals(12.55427859318747, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "getEditLogManifest_avg_time")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "prepareRecovery_num_ops")));
        org.junit.Assert.assertEquals(10.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "prepareRecovery_avg_time")));
        org.junit.Assert.assertEquals(1, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "acceptRecovery_num_ops")));
        org.junit.Assert.assertEquals(30.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpcdetailed", "acceptRecovery_avg_time")));
        org.junit.Assert.assertEquals(0.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/ugi", "loginFailure_avg_time")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/ugi", "loginFailure_num_ops")));
        org.junit.Assert.assertEquals(0.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/ugi", "loginSuccess_avg_time")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/ugi", "loginSuccess_num_ops")));
        org.junit.Assert.assertEquals("{\"mycluster\":{\"Formatted\":\"true\"}}", resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode", "journalsStatus")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs60s_num_ops")));
        org.junit.Assert.assertEquals(988, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs60s50thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(988, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs60s75thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(988, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs60s90thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(988, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs60s95thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(988, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs60s99thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(4, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs300s_num_ops")));
        org.junit.Assert.assertEquals(1027, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs300s50thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(1037, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs300s75thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(1037, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs300s90thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(1037, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs300s95thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(1037, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs300s99thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(60, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs3600s_num_ops")));
        org.junit.Assert.assertEquals(1122, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs3600s50thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(1344, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs3600s75thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(1554, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs3600s90thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(1980, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs3600s95thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(8442, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "syncs3600s99thPercentileLatencyMicros")));
        org.junit.Assert.assertEquals(8265, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "batchesWritten")));
        org.junit.Assert.assertEquals(8265, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "txnsWritten")));
        org.junit.Assert.assertEquals(107837, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "bytesWritten")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "batchesWrittenWhileLagging")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "lastPromisedEpoch")));
        org.junit.Assert.assertEquals(2, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "lastWriterEpoch")));
        org.junit.Assert.assertEquals(0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "currentLagTxns")));
        org.junit.Assert.assertEquals(8444, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/journalnode/cluster/mycluster", "lastWrittenTxId")));
    }

    public void testPopulateResources_jmx_Storm() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c2");
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.1.1"));
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(false);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider gangliaHostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, gangliaHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "STORM_REST_API");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(28.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "tasks.total")));
        org.junit.Assert.assertEquals(8.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "slots.total")));
        org.junit.Assert.assertEquals(5.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "slots.free")));
        org.junit.Assert.assertEquals(2.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "supervisors")));
        org.junit.Assert.assertEquals(28.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "executors.total")));
        org.junit.Assert.assertEquals(3.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "slots.used")));
        org.junit.Assert.assertEquals(1.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "topologies")));
        org.junit.Assert.assertEquals(4637.0, resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/api/cluster/summary", "nimbus.uptime")));
    }

    public void testPopulateResources_NoRegionServer() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, metricsHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), null, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "HBASE_REGIONSERVER");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        int preSize = resource.getPropertiesMap().size();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(preSize, resource.getPropertiesMap().size());
    }

    public void testPopulateResources_HBaseMaster2() throws java.lang.Exception {
        org.apache.ambari.server.controller.jmx.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.jmx.TestStreamProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider hostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(false);
        org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider metricsHostProvider = new org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostProvider, metricsHostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "domu-12-31-39-0e-34-e1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "HBASE_MASTER");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet());
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> res = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, res.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> map = res.iterator().next().getPropertiesMap();
        org.junit.Assert.assertTrue(map.containsKey("metrics/hbase/master"));
        org.junit.Assert.assertTrue(map.get("metrics/hbase/master").containsKey("IsActiveMaster"));
    }

    public void testPopulateResources_params_category5() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("temporal_ganglia_data_yarn_queues.txt");
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider jmxHostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(true);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, jmxHostProvider, hostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "dev01.ambari.apache.org");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "RESOURCEMANAGER");
        java.lang.String RM_CATEGORY_1 = "metrics/yarn/Queue/root/default";
        java.lang.String RM_AVAILABLE_MEMORY_PROPERTY = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(RM_CATEGORY_1, "AvailableMB");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(RM_CATEGORY_1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(RM_CATEGORY_1), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertTrue(org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size() > 2);
        org.junit.Assert.assertNotNull(resource.getPropertyValue(RM_AVAILABLE_MEMORY_PROPERTY));
    }

    public void testPopulateResources_ganglia_JournalNode() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("journalnode_ganglia_data.txt");
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider jmxHostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(true);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, jmxHostProvider, hostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "JOURNALNODE");
        java.lang.Object[][] testData = new java.lang.Object[][]{ new java.lang.Object[]{ "metrics", "boottime", 1.378290058E9 }, new java.lang.Object[]{ "metrics/cpu", "cpu_aidle", 0.0 }, new java.lang.Object[]{ "metrics/cpu", "cpu_idle", 88.2 }, new java.lang.Object[]{ "metrics/cpu", "cpu_nice", 0.0 }, new java.lang.Object[]{ "metrics/cpu", "cpu_num", 2.0 }, new java.lang.Object[]{ "metrics/cpu", "cpu_speed", 3583.0 }, new java.lang.Object[]{ "metrics/cpu", "cpu_system", 8.4 }, new java.lang.Object[]{ "metrics/cpu", "cpu_user", 3.3 }, new java.lang.Object[]{ "metrics/cpu", "cpu_wio", 0.1 }, new java.lang.Object[]{ "metrics/disk", "disk_free", 92.428 }, new java.lang.Object[]{ "metrics/disk", "disk_total", 101.515 }, new java.lang.Object[]{ "metrics/disk", "part_max_used", 12.8 }, new java.lang.Object[]{ "metrics/load", "load_fifteen", 0.026 }, new java.lang.Object[]{ "metrics/load", "load_five", 0.114 }, new java.lang.Object[]{ "metrics/load", "load_one", 0.226 }, new java.lang.Object[]{ "metrics/memory", "mem_buffers", 129384.0 }, new java.lang.Object[]{ "metrics/memory", "mem_cached", 589576.0 }, new java.lang.Object[]{ "metrics/memory", "mem_free", 1365496.0 }, new java.lang.Object[]{ "metrics/memory", "mem_shared", 0.0 }, new java.lang.Object[]{ "metrics/memory", "mem_total", 4055144.0 }, new java.lang.Object[]{ "metrics/memory", "swap_free", 4128760.0 }, new java.lang.Object[]{ "metrics/memory", "swap_total", 4128760.0 }, new java.lang.Object[]{ "metrics/network", "bytes_in", 22547.48 }, new java.lang.Object[]{ "metrics/network", "bytes_out", 5772.33 }, new java.lang.Object[]{ "metrics/network", "pkts_in", 24.0 }, new java.lang.Object[]{ "metrics/network", "pkts_out", 35.4 }, new java.lang.Object[]{ "metrics/process", "proc_run", 4.0 }, new java.lang.Object[]{ "metrics/process", "proc_total", 657.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "batchesWritten", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "batchesWrittenWhileLagging", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "bytesWritten", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "currentLagTxns", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "lastPromisedEpoch", 5.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "lastWriterEpoch", 5.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "lastWrittenTxId", 613.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs60s50thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs60s75thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs60s90thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs60s95thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs60s99thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs60s_num_ops", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs300s50thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs300s75thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs300s90thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs300s95thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs300s99thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs300s_num_ops", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs3600s50thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs3600s75thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs3600s90thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs3600s95thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs3600s99thPercentileLatencyMicros", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "syncs3600s_num_ops", 0.0 }, new java.lang.Object[]{ "metrics/dfs/journalNode", "txnsWritten", 0.0 } };
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        java.util.Set<java.lang.String> properties = new java.util.LinkedHashSet<>();
        for (java.lang.Object[] row : testData) {
            properties.add(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(row[0].toString(), row[1].toString()));
        }
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(properties, temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.Map<java.lang.String, java.lang.Object> p = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource);
        for (java.lang.String key : p.keySet()) {
            if (!properties.contains(key)) {
                java.lang.System.out.printf(key);
            }
        }
        org.junit.Assert.assertEquals(properties.size() + 3, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        int i = 0;
        for (java.lang.String property : properties) {
            org.junit.Assert.assertEquals(testData[i++][2], resource.getPropertyValue(property));
        }
    }

    public void testPopulateResources_resourcemanager_clustermetrics() throws java.lang.Exception {
        java.lang.String[] metrics = new java.lang.String[]{ "metrics/yarn/ClusterMetrics/NumActiveNMs", "metrics/yarn/ClusterMetrics/NumDecommissionedNMs", "metrics/yarn/ClusterMetrics/NumLostNMs", "metrics/yarn/ClusterMetrics/NumUnhealthyNMs", "metrics/yarn/ClusterMetrics/NumRebootedNMs" };
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("yarn_ganglia_data.txt");
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider jmxHostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(true);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaServiceProvider();
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, jmxHostProvider, hostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        for (java.lang.String metric : metrics) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
            resource.setProperty("HostRoles/cluster_name", "c2");
            resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
            resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "RESOURCEMANAGER");
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
            temporalInfoMap.put(metric, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
            org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(metric), temporalInfoMap);
            org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
            org.junit.Assert.assertEquals(4, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
            org.junit.Assert.assertNotNull(resource.getPropertyValue(metric));
        }
    }

    public void testPopulateResourcesWithAggregateFunctionMetrics() throws java.lang.Exception {
        java.lang.String metric = "metrics/rpc/NumOpenConnections._sum";
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("ams/aggregate_component_metric.json");
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider jmxHostProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.TestJMXProvider(true);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider = new org.apache.ambari.server.controller.metrics.MetricsServiceProvider() {
            @java.lang.Override
            public org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService getMetricsServiceType() {
                return org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS;
            }
        };
        org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider propertyProvider = new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type.Component, jmxHostProvider, hostProvider, serviceProvider, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider(), new org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.EmptyPropertyProvider());
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        resource.setProperty("HostRoles/cluster_name", "c2");
        resource.setProperty("HostRoles/service_name", "HBASE");
        resource.setProperty(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID, "HBASE_REGIONSERVER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(metric, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1429824611300L, 1429825241400L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(metric), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        org.junit.Assert.assertEquals(4, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(metric));
        java.lang.Number[][] metricsArray = ((java.lang.Number[][]) (resource.getPropertyValue(metric)));
        org.junit.Assert.assertEquals(32, metricsArray.length);
    }

    private void injectCacheEntryFactoryWithStreamProvider(org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider) throws java.lang.Exception {
        java.lang.reflect.Field field = org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class.getDeclaredField("requestHelperForGets");
        field.setAccessible(true);
        field.set(org.apache.ambari.server.controller.internal.StackDefinedPropertyProviderTest.cacheEntryFactory, new org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper(streamProvider));
    }

    public static class TestJMXProvider extends org.apache.ambari.server.controller.metrics.JMXPropertyProviderTest.TestJMXHostProvider {
        public TestJMXProvider(boolean unknownPort) {
            super(unknownPort);
        }

        @java.lang.Override
        public java.lang.String getJMXRpcMetricTag(java.lang.String clusterName, java.lang.String componentName, java.lang.String port) {
            return "8020".equals(port) ? "client" : null;
        }
    }
}