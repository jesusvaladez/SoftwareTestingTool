package org.apache.ambari.server.controller.metrics.timeline;
import org.apache.http.client.utils.URIBuilder;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.mockito.Mockito.mock;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.class, org.apache.ambari.server.controller.AmbariServer.class })
@org.powermock.core.classloader.annotations.PowerMockIgnore({ "javax.xml.parsers.*", "org.xml.sax.*", "org.ehcache.*", "org.apache.log4j.*" })
public class AMSPropertyProviderTest {
    private static final java.lang.String PROPERTY_ID1 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/cpu", "cpu_user");

    private static final java.lang.String PROPERTY_ID2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/memory", "mem_free");

    private static final java.lang.String PROPERTY_ID3 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/datanode", "blocks_replicated");

    private static final java.lang.String PROPERTY_ID4 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/dfs/datanode", "blocks_removed");

    private static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name");

    private static final java.lang.String HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name");

    private static final java.lang.String COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name");

    private static final java.lang.String FILE_PATH_PREFIX = "ams" + java.io.File.separator;

    private static final java.lang.String SINGLE_HOST_METRICS_FILE_PATH = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "single_host_metric.json";

    private static final java.lang.String MULTIPLE_HOST_METRICS_FILE_PATH = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "multiple_host_metrics.json";

    private static final java.lang.String SINGLE_COMPONENT_METRICS_FILE_PATH = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "single_component_metrics.json";

    private static final java.lang.String MULTIPLE_COMPONENT_REGEXP_METRICS_FILE_PATH = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "multiple_component_regexp_metrics.json";

    private static final java.lang.String EMBEDDED_METRICS_FILE_PATH = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "embedded_host_metric.json";

    private static final java.lang.String AGGREGATE_METRICS_FILE_PATH = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "aggregate_component_metric.json";

    private static org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory;

    @org.junit.Before
    public void setupCache() {
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.cacheEntryFactory = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory(new org.apache.ambari.server.configuration.Configuration());
        org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken authenticationToken = new org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken("admin");
        authenticationToken.setAuthenticated(true);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @org.junit.Test
    public void testRbacForAMSPropertyProvider() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("ClusterAdmin", 2L));
        org.springframework.security.core.context.SecurityContextHolder.getContext();
        testPopulateResourcesForSingleHostMetric();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator("ServiceAdmin", 2L));
        org.springframework.security.core.context.SecurityContextHolder.getContext();
        testPopulateResourcesForSingleHostMetricPointInTime();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        org.springframework.security.core.context.SecurityContextHolder.getContext();
        try {
            testPopulateResourcesForMultipleHostMetricscPointInTime();
        } catch (java.lang.Exception e) {
            org.junit.Assert.assertTrue(e instanceof org.apache.ambari.server.security.authorization.AuthorizationException);
        }
    }

    @org.junit.Test
    public void testAMSPropertyProviderAsViewUser() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser("ViewUser", 2L));
        try {
            testPopulateResourcesForSingleHostMetric();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
        try {
            testPopulateResourcesForSingleHostMetric();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
        try {
            testPopulateResourcesForMultipleHostMetrics();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
        try {
            testPopulateResourcesForSingleComponentMetric();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
        try {
            testAggregateFunctionForComponentMetrics();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
        try {
            testPopulateResourcesForHostComponentHostMetrics();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
        try {
            testPopulateResourcesForHostComponentMetricsForMultipleHosts();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
        try {
            testPopulateResourcesHostBatches();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
        try {
            testPopulateResourcesForMultipleComponentsMetric();
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.security.authorization.AuthorizationException ignored) {
        }
    }

    @org.junit.Test
    public void testPopulateResourcesForSingleHostMetric() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.SINGLE_HOST_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244800L, 1416448936474L, 15L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "cpu_user");
        uriBuilder.addParameter("hostname", "h1");
        uriBuilder.addParameter("appId", "HOST");
        uriBuilder.addParameter("startTime", "1416445244800");
        uriBuilder.addParameter("endTime", "1416448936474");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1)));
        org.junit.Assert.assertNotNull("No value for property " + org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1, val);
        org.junit.Assert.assertEquals(111, val.length);
    }

    @org.junit.Test
    public void testPopulateResourcesForSingleHostMetricPointInTime() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.SINGLE_HOST_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = java.util.Collections.emptyMap();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(res);
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "cpu_user");
        uriBuilder.addParameter("hostname", "h1");
        uriBuilder.addParameter("appId", "HOST");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Double val = ((java.lang.Double) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1)));
        org.junit.Assert.assertEquals(41.088, val, 0.001);
    }

    @org.junit.Test
    public void testPopulateResourcesForMultipleHostMetricscPointInTime() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.MULTIPLE_HOST_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = java.util.Collections.emptyMap();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<java.lang.String>() {
            {
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1);
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2);
            }
        }, temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "cpu_user,mem_free");
        uriBuilder.addParameter("hostname", "h1");
        uriBuilder.addParameter("appId", "HOST");
        org.apache.http.client.utils.URIBuilder uriBuilder2 = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder2.addParameter("metricNames", "mem_free,cpu_user");
        uriBuilder2.addParameter("hostname", "h1");
        uriBuilder2.addParameter("appId", "HOST");
        org.junit.Assert.assertTrue(uriBuilder.toString().equals(streamProvider.getLastSpec()) || uriBuilder2.toString().equals(streamProvider.getLastSpec()));
        java.lang.Double val1 = ((java.lang.Double) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1)));
        org.junit.Assert.assertNotNull("No value for property " + org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1, val1);
        org.junit.Assert.assertEquals(41.088, val1, 0.001);
        java.lang.Double val2 = ((java.lang.Double) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2)));
        org.junit.Assert.assertNotNull("No value for property " + org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2, val2);
        org.junit.Assert.assertEquals(2.47025664E8, val2, 0.1);
    }

    @org.junit.Test
    public void testPopulateResourcesForMultipleHostMetrics() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.MULTIPLE_HOST_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244701L, 1416448936564L, 15L));
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244701L, 1416448936564L, 15L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<java.lang.String>() {
            {
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1);
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2);
                add("params/padding/NONE");
            }
        }, temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder1 = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder1.addParameter("metricNames", "cpu_user,mem_free");
        uriBuilder1.addParameter("hostname", "h1");
        uriBuilder1.addParameter("appId", "HOST");
        uriBuilder1.addParameter("startTime", "1416445244701");
        uriBuilder1.addParameter("endTime", "1416448936564");
        org.apache.http.client.utils.URIBuilder uriBuilder2 = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder2.addParameter("metricNames", "mem_free,cpu_user");
        uriBuilder2.addParameter("hostname", "h1");
        uriBuilder2.addParameter("appId", "HOST");
        uriBuilder2.addParameter("startTime", "1416445244701");
        uriBuilder2.addParameter("endTime", "1416448936564");
        java.util.List<java.lang.String> allSpecs = new java.util.ArrayList<>(streamProvider.getAllSpecs());
        org.junit.Assert.assertEquals(1, allSpecs.size());
        org.junit.Assert.assertTrue(uriBuilder1.toString().equals(allSpecs.get(0)) || uriBuilder2.toString().equals(allSpecs.get(0)));
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1)));
        org.junit.Assert.assertEquals(111, val.length);
        val = ((java.lang.Number[][]) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2)));
        org.junit.Assert.assertEquals(86, val.length);
    }

    @org.junit.Test
    public void testPopulateResourcesForRegexpMetrics() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.MULTIPLE_COMPONENT_REGEXP_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>() {
            {
                put("RESOURCEMANAGER", new java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>() {
                    {
                        put("metrics/yarn/Queue/$1.replaceAll(\"([.])\",\"/\")/AvailableMB", new org.apache.ambari.server.controller.internal.PropertyInfo("yarn.QueueMetrics.Queue=(.+).AvailableMB", true, false));
                    }
                });
            }
        };
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        java.lang.String propertyId1 = "metrics/yarn/Queue/root/AvailableMB";
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "RESOURCEMANAGER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(propertyId1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416528759233L, 1416531129231L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(propertyId1), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "yarn.QueueMetrics.Queue=root.AvailableMB");
        uriBuilder.addParameter("appId", "RESOURCEMANAGER");
        uriBuilder.addParameter("startTime", "1416528759233");
        uriBuilder.addParameter("endTime", "1416531129231");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue("metrics/yarn/Queue/root/AvailableMB")));
        org.junit.Assert.assertNotNull("No value for property metrics/yarn/Queue/root/AvailableMB", val);
        org.junit.Assert.assertEquals(238, val.length);
    }

    @org.junit.Test
    public void testPopulateResourcesForSingleComponentMetric() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.SINGLE_COMPONENT_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "RpcQueueTime_avg_time");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(propertyId, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416528759233L, 1416531129231L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(propertyId), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "rpc.rpc.RpcQueueTimeAvgTime");
        uriBuilder.addParameter("appId", "NAMENODE");
        uriBuilder.addParameter("startTime", "1416528759233");
        uriBuilder.addParameter("endTime", "1416531129231");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue(propertyId)));
        org.junit.Assert.assertNotNull("No value for property " + propertyId, val);
        org.junit.Assert.assertEquals(238, val.length);
    }

    @org.junit.Test
    public void testPopulateResourcesForMultipleComponentsMetric() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.SINGLE_COMPONENT_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        java.util.Set<java.lang.String> requestedPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList("metrics/hbase/master", "metrics/cpu/cpu_wio"));
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "METRICS_COLLECTOR");
        org.apache.ambari.server.controller.spi.Resource namenodeResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        namenodeResource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        namenodeResource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        namenodeResource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "NAMENODE");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        for (java.lang.String propertyId : requestedPropertyIds) {
            temporalInfoMap.put(propertyId, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416528759233L, 1416531129231L, 1L));
        }
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestedPropertyIds, temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(new java.util.HashSet<>(java.util.Arrays.asList(resource, namenodeResource)), request, null);
        org.junit.Assert.assertEquals(2, resources.size());
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "cpu_wio");
        uriBuilder.addParameter("appId", "NAMENODE");
        uriBuilder.addParameter("startTime", "1416528759233");
        uriBuilder.addParameter("endTime", "1416531129231");
        org.junit.Assert.assertTrue(streamProvider.getAllSpecs().contains(uriBuilder.toString()));
        java.util.List<java.lang.String> allSpecs = new java.util.ArrayList<>(streamProvider.getAllSpecs());
        org.junit.Assert.assertEquals(2, allSpecs.size());
    }

    @org.junit.Test
    public void testPopulateMetricsForEmbeddedHBase() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
        EasyMock.expect(org.apache.ambari.server.controller.AmbariServer.getController()).andReturn(amc).anyTimes();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("HostRoles/cluster_name")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(2L).anyTimes();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        try {
            EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster).anyTimes();
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
        }
        org.apache.ambari.server.state.Service amsService = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(amsService.getDesiredStackId()).andReturn(stackId);
        EasyMock.expect(amsService.getName()).andReturn("AMS");
        EasyMock.expect(cluster.getServiceByComponentName("METRICS_COLLECTOR")).andReturn(amsService);
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackId).anyTimes();
        EasyMock.expect(amc.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponentToService("HDP", "2.2", "METRICS_COLLECTOR")).andReturn("AMS").anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent("HDP", "2.2", "AMS", "METRICS_COLLECTOR")).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getTimelineAppid()).andReturn("AMS-HBASE");
        EasyMock.replay(amc, clusters, cluster, amsService, ambariMetaInfo, componentInfo);
        org.powermock.api.easymock.PowerMock.replayAll();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.EMBEDDED_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/hbase/regionserver", "requests");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "METRICS_COLLECTOR");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(propertyId, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1421694000L, 1421697600L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(propertyId), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "regionserver.Server.totalRequestCount");
        uriBuilder.addParameter("appId", "AMS-HBASE");
        uriBuilder.addParameter("startTime", "1421694000");
        uriBuilder.addParameter("endTime", "1421697600");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue(propertyId)));
        org.junit.Assert.assertEquals(189, val.length);
    }

    @org.junit.Test
    public void testAggregateFunctionForComponentMetrics() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
        EasyMock.expect(org.apache.ambari.server.controller.AmbariServer.getController()).andReturn(amc).anyTimes();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        EasyMock.expect(amc.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("HostRoles/cluster_name")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(2L).anyTimes();
        try {
            EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster).anyTimes();
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
        }
        org.apache.ambari.server.state.Service hbaseService = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(hbaseService.getDesiredStackId()).andReturn(stackId);
        EasyMock.expect(hbaseService.getName()).andReturn("HBASE");
        EasyMock.expect(cluster.getServiceByComponentName("HBASE_REGIONSERVER")).andReturn(hbaseService);
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackId).anyTimes();
        EasyMock.expect(amc.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponentToService("HDP", "2.2", "HBASE_REGIONSERVER")).andReturn("HBASE").anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent("HDP", "2.2", "HBASE", "HBASE_REGIONSERVER")).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getTimelineAppid()).andReturn("HBASE");
        EasyMock.replay(amc, clusters, cluster, hbaseService, ambariMetaInfo, componentInfo);
        org.powermock.api.easymock.PowerMock.replayAll();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.AGGREGATE_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        org.apache.ambari.server.controller.utilities.PropertyHelper.updateMetricsWithAggregateFunctionSupport(propertyIds);
        org.apache.ambari.server.controller.metrics.timeline.AMSComponentPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/rpc", "NumOpenConnections._sum");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Component);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "HBASE_REGIONSERVER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(propertyId, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1429824611300L, 1429825241400L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(propertyId), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "rpc.rpc.NumOpenConnections._sum");
        uriBuilder.addParameter("appId", "HBASE");
        uriBuilder.addParameter("startTime", "1429824611300");
        uriBuilder.addParameter("endTime", "1429825241400");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue(propertyId)));
        org.junit.Assert.assertEquals(32, val.length);
    }

    @org.junit.Test
    public void testFilterOutOfBandMetricData() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.SINGLE_HOST_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416446744801L, 1416447224801L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "cpu_user");
        uriBuilder.addParameter("hostname", "h1");
        uriBuilder.addParameter("appId", "HOST");
        uriBuilder.addParameter("startTime", "1416446744801");
        uriBuilder.addParameter("endTime", "1416447224801");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1)));
        org.junit.Assert.assertNotNull("No value for property " + org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1, val);
        org.junit.Assert.assertEquals(25, val.length);
    }

    static class TestStreamProviderForHostComponentHostMetricsTest extends org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider {
        java.lang.String hostMetricFilePath = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "single_host_metric.json";

        java.lang.String hostComponentMetricFilePath = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "single_host_component_metrics.json";

        public TestStreamProviderForHostComponentHostMetricsTest(java.lang.String fileName) {
            super(fileName);
        }

        @java.lang.Override
        public java.io.InputStream readFrom(java.lang.String spec) throws java.io.IOException {
            if (spec.contains("HOST")) {
                this.fileName = hostMetricFilePath;
            } else {
                this.fileName = hostComponentMetricFilePath;
            }
            specs.add(spec);
            return super.readFrom(spec);
        }
    }

    @org.junit.Test
    public void testPopulateResourcesForHostComponentHostMetrics() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestStreamProviderForHostComponentHostMetricsTest streamProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestStreamProviderForHostComponentHostMetricsTest(null);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244801L, 1416448936464L, 1L));
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID3, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244801L, 1416448936464L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<java.lang.String>() {
            {
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1);
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID3);
                add("params/padding/NONE");
            }
        }, temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        java.util.Set<java.lang.String> specs = streamProvider.getAllSpecs();
        org.junit.Assert.assertEquals(2, specs.size());
        java.lang.String hostMetricSpec = null;
        java.lang.String hostComponentMetricsSpec = null;
        for (java.lang.String spec : specs) {
            if (spec.contains("HOST")) {
                hostMetricSpec = spec;
            } else {
                hostComponentMetricsSpec = spec;
            }
        }
        org.junit.Assert.assertNotNull(hostMetricSpec);
        org.junit.Assert.assertNotNull(hostComponentMetricsSpec);
        org.apache.http.client.utils.URIBuilder uriBuilder1 = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder1.addParameter("metricNames", "dfs.datanode.BlocksReplicated");
        uriBuilder1.addParameter("hostname", "h1");
        uriBuilder1.addParameter("appId", "DATANODE");
        uriBuilder1.addParameter("startTime", "1416445244801");
        uriBuilder1.addParameter("endTime", "1416448936464");
        org.junit.Assert.assertEquals(uriBuilder1.toString(), hostComponentMetricsSpec);
        org.apache.http.client.utils.URIBuilder uriBuilder2 = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder2.addParameter("metricNames", "cpu_user");
        uriBuilder2.addParameter("hostname", "h1");
        uriBuilder2.addParameter("appId", "HOST");
        uriBuilder2.addParameter("startTime", "1416445244801");
        uriBuilder2.addParameter("endTime", "1416448936464");
        org.junit.Assert.assertEquals(uriBuilder2.toString(), hostMetricSpec);
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1)));
        org.junit.Assert.assertEquals(111, val.length);
        val = ((java.lang.Number[][]) (res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID3)));
        org.junit.Assert.assertNotNull("No value for property " + org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID3, val);
        org.junit.Assert.assertEquals(8, val.length);
    }

    static class TestStreamProviderForHostComponentMultipleHostsMetricsTest extends org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider {
        java.lang.String hostComponentMetricFilePath_h1 = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "single_host_component_metrics_h1.json";

        java.lang.String hostComponentMetricFilePath_h2 = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.FILE_PATH_PREFIX + "single_host_component_metrics_h2.json";

        public TestStreamProviderForHostComponentMultipleHostsMetricsTest(java.lang.String fileName) {
            super(fileName);
        }

        @java.lang.Override
        public java.io.InputStream readFrom(java.lang.String spec) throws java.io.IOException {
            if (spec.contains("h1")) {
                this.fileName = hostComponentMetricFilePath_h1;
            } else {
                this.fileName = hostComponentMetricFilePath_h2;
            }
            specs.add(spec);
            return super.readFrom(spec);
        }
    }

    @org.junit.Test
    public void testPopulateResourcesHostBatches() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestStreamProviderForHostComponentMultipleHostsMetricsTest streamProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestStreamProviderForHostComponentMultipleHostsMetricsTest(null);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider("h1");
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (int i = 0; i < (100 + 1); i++) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
            resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
            resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h" + i);
            resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
            resources.add(resource);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244801L, 1416448936464L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<java.lang.String>() {
            {
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4);
                add("params/padding/NONE");
            }
        }, temporalInfoMap);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources1 = propertyProvider.populateResources(resources, request, null);
        java.util.List<java.lang.String> allSpecs = new java.util.ArrayList<>(streamProvider.getAllSpecs());
        org.junit.Assert.assertEquals(2, allSpecs.size());
    }

    @org.junit.Test
    public void testPopulateResourcesForHostComponentMetricsForMultipleHosts() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestStreamProviderForHostComponentMultipleHostsMetricsTest streamProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestStreamProviderForHostComponentMultipleHostsMetricsTest(null);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider("h1");
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244801L, 1416448936464L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<java.lang.String>() {
            {
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4);
                add("params/padding/NONE");
            }
        }, temporalInfoMap);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources1 = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources1.size());
        org.apache.ambari.server.controller.spi.Resource res1 = resources1.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources1.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider("h2");
        cacheProviderMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider.class);
        cacheMock = org.easymock.EasyMock.createMock(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCache.class);
        EasyMock.expect(cacheProviderMock.getTimelineMetricsCache()).andReturn(cacheMock).anyTimes();
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h2");
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244801L, 1416448936464L, 1L));
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<java.lang.String>() {
            {
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4);
                add("params/padding/NONE");
            }
        }, temporalInfoMap);
        propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostComponentPropertyProvider(propertyIds, streamProvider, sslConfiguration, cacheProviderMock, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h2");
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources2 = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources2.size());
        org.apache.ambari.server.controller.spi.Resource res2 = resources2.iterator().next();
        properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources2.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        java.util.Set<java.lang.String> specs = streamProvider.getAllSpecs();
        org.junit.Assert.assertEquals(2, specs.size());
        org.apache.http.client.utils.URIBuilder uriBuilder1 = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        java.lang.Number[][] val;
        for (java.lang.String spec : specs) {
            org.junit.Assert.assertNotNull(spec);
            if (spec.contains("h2")) {
                uriBuilder1.setParameter("metricNames", "dfs.datanode.blocks_removed");
                uriBuilder1.setParameter("hostname", "h2");
                uriBuilder1.setParameter("appId", "DATANODE");
                uriBuilder1.setParameter("startTime", "1416445244801");
                uriBuilder1.setParameter("endTime", "1416448936464");
                org.junit.Assert.assertEquals(uriBuilder1.toString(), spec);
                val = ((java.lang.Number[][]) (res2.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4)));
                org.junit.Assert.assertNotNull("No value for property " + org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4, val);
                org.junit.Assert.assertEquals(9, val.length);
            } else {
                uriBuilder1.setParameter("metricNames", "dfs.datanode.blocks_removed");
                uriBuilder1.setParameter("hostname", "h1");
                uriBuilder1.setParameter("appId", "DATANODE");
                uriBuilder1.setParameter("startTime", "1416445244801");
                uriBuilder1.setParameter("endTime", "1416448936464");
                org.junit.Assert.assertEquals(uriBuilder1.toString(), spec);
                val = ((java.lang.Number[][]) (res1.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4)));
                org.junit.Assert.assertNotNull("No value for property " + org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID4, val);
                org.junit.Assert.assertEquals(8, val.length);
            }
        }
    }

    @org.junit.Test
    public void testSocketTimeoutExceptionBehavior() throws java.lang.Exception {
        setUpCommonMocks();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("ClusterAdmin", 2L));
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = EasyMock.createNiceMock(org.apache.ambari.server.controller.internal.URLStreamProvider.class);
        java.net.HttpURLConnection connection = EasyMock.createNiceMock(java.net.HttpURLConnection.class);
        EasyMock.expect(streamProvider.processURL(((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.lang.String) (EasyMock.anyObject())), ((java.util.Map<java.lang.String, java.util.List<java.lang.String>>) (EasyMock.anyObject())))).andReturn(connection);
        EasyMock.expect(connection.getInputStream()).andThrow(new java.net.SocketTimeoutException("Unit test raising Exception")).once();
        EasyMock.replay(streamProvider, connection);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSHostPropertyProvider(propertyIds, streamProvider, sslConfiguration, new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider(new org.apache.ambari.server.configuration.Configuration(), org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.cacheEntryFactory), metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        final org.apache.ambari.server.controller.spi.Resource resource1 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource1.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource1.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h1");
        final org.apache.ambari.server.controller.spi.Resource resource2 = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource2.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource2.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.HOST_NAME_PROPERTY_ID, "h2");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244801L, 1416448936464L, 1L));
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445344901L, 1416448946564L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(new java.util.HashSet<java.lang.String>() {
            {
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1);
                add(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2);
            }
        }, temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(new java.util.HashSet<org.apache.ambari.server.controller.spi.Resource>() {
            {
                add(resource1);
                add(resource2);
            }
        }, request, null);
        EasyMock.verify(streamProvider, connection);
        org.junit.Assert.assertEquals(2, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.junit.Assert.assertNull(res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID1));
        org.junit.Assert.assertNull(res.getPropertyValue(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.PROPERTY_ID2));
    }

    public static class TestMetricHostProvider implements org.apache.ambari.server.controller.metrics.MetricHostProvider {
        private java.lang.String hostName;

        public TestMetricHostProvider() {
        }

        public TestMetricHostProvider(java.lang.String hostName) {
            this.hostName = hostName;
        }

        @java.lang.Override
        public java.lang.String getCollectorHostName(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return "localhost";
        }

        @java.lang.Override
        public java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException {
            return hostName != null ? hostName : "h1";
        }

        @java.lang.Override
        public java.lang.String getCollectorPort(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return "6188";
        }

        @java.lang.Override
        public boolean isCollectorHostLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return true;
        }

        @java.lang.Override
        public boolean isCollectorComponentLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return true;
        }

        @java.lang.Override
        public boolean isCollectorHostExternal(java.lang.String clusterName) {
            return false;
        }
    }

    private void setUpCommonMocks() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ams = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
        EasyMock.expect(org.apache.ambari.server.controller.AmbariServer.getController()).andReturn(ams).anyTimes();
        org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.ComponentInfo componentInfo = EasyMock.createNiceMock(org.apache.ambari.server.state.ComponentInfo.class);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        EasyMock.expect(ams.getClusters()).andReturn(clusters).anyTimes();
        try {
            EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster).anyTimes();
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
        }
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(stackId).anyTimes();
        EasyMock.expect(ams.getAmbariMetaInfo()).andReturn(ambariMetaInfo).anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponentToService(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andReturn("HDFS").anyTimes();
        EasyMock.expect(ambariMetaInfo.getComponent(EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andReturn(componentInfo).anyTimes();
        EasyMock.expect(componentInfo.getTimelineAppid()).andReturn(null).anyTimes();
        EasyMock.replay(ams, clusters, cluster, ambariMetaInfo, componentInfo);
        org.powermock.api.easymock.PowerMock.replayAll();
    }

    private void injectCacheEntryFactoryWithStreamProvider(org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider) throws java.lang.Exception {
        java.lang.reflect.Field field = org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class.getDeclaredField("requestHelperForGets");
        field.setAccessible(true);
        field.set(org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.cacheEntryFactory, new org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper(streamProvider));
    }
}