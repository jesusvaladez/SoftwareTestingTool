package org.apache.ambari.server.controller.metrics.timeline;
import org.apache.http.client.utils.URIBuilder;
import static org.mockito.Mockito.mock;
public class AMSReportPropertyProviderTest {
    private static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name");

    private static final java.lang.String FILE_PATH_PREFIX = "ams" + java.io.File.separator;

    private static final java.lang.String SINGLE_HOST_METRICS_FILE_PATH = org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.FILE_PATH_PREFIX + "single_host_metric.json";

    private static final java.lang.String AGGREGATE_CLUSTER_METRICS_FILE_PATH = org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.FILE_PATH_PREFIX + "aggregate_cluster_metrics.json";

    private static org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory cacheEntryFactory;

    private static org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider cacheProvider;

    @org.junit.BeforeClass
    public static void setupCache() {
        org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.cacheEntryFactory = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory(new org.apache.ambari.server.configuration.Configuration());
        org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.cacheProvider = new org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider(new org.apache.ambari.server.configuration.Configuration(), org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.cacheEntryFactory);
    }

    @org.junit.Test
    public void testPopulateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.SINGLE_HOST_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider(propertyIds, streamProvider, sslConfiguration, org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.cacheProvider, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/cpu", "User");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(propertyId, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1416445244800L, 1416448936474L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(propertyId), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "cpu_user");
        uriBuilder.addParameter("appId", "HOST");
        uriBuilder.addParameter("startTime", "1416445244800");
        uriBuilder.addParameter("endTime", "1416448936474");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue("metrics/cpu/User")));
        org.junit.Assert.assertEquals(111, val.length);
    }

    @org.junit.Test
    public void testPopulateResourceWithAggregateFunction() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider(org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.AGGREGATE_CLUSTER_METRICS_FILE_PATH);
        injectCacheEntryFactoryWithStreamProvider(streamProvider);
        org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider metricHostProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProviderTest.TestMetricHostProvider();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration sslConfiguration = Mockito.mock(org.apache.ambari.server.configuration.ComponentSSLConfiguration.class);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProvider(propertyIds, streamProvider, sslConfiguration, org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.cacheProvider, metricHostProvider, org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID);
        java.lang.String propertyId = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/cpu", "User._sum");
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(propertyId, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(1432033257812L, 1432035927922L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(propertyId), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource res = resources.iterator().next();
        java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resources.iterator().next());
        org.junit.Assert.assertNotNull(properties);
        org.apache.http.client.utils.URIBuilder uriBuilder = org.apache.ambari.server.controller.metrics.timeline.AMSPropertyProvider.getAMSUriBuilder("localhost", 6188, false);
        uriBuilder.addParameter("metricNames", "cpu_user._sum");
        uriBuilder.addParameter("appId", "HOST");
        uriBuilder.addParameter("startTime", "1432033257812");
        uriBuilder.addParameter("endTime", "1432035927922");
        org.junit.Assert.assertEquals(uriBuilder.toString(), streamProvider.getLastSpec());
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue("metrics/cpu/User._sum")));
        org.junit.Assert.assertEquals(90, val.length);
    }

    private void injectCacheEntryFactoryWithStreamProvider(org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider) throws java.lang.Exception {
        java.lang.reflect.Field field = org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheEntryFactory.class.getDeclaredField("requestHelperForGets");
        field.setAccessible(true);
        field.set(org.apache.ambari.server.controller.metrics.timeline.AMSReportPropertyProviderTest.cacheEntryFactory, new org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper(streamProvider));
    }
}