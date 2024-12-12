package org.apache.ambari.server.controller.metrics.ganglia;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.controller.metrics.MetricHostProvider.class })
public class GangliaPropertyProviderTest {
    private static final java.lang.String PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/process", "proc_total");

    private static final java.lang.String PROPERTY_ID2 = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/cpu", "cpu_wio");

    private static final java.lang.String FLUME_CHANNEL_CAPACITY_PROPERTY = "metrics/flume/flume/CHANNEL/c1/ChannelCapacity";

    private static final java.lang.String FLUME_CATEGORY = "metrics/flume";

    private static final java.lang.String FLUME_CATEGORY2 = "metrics/flume/flume";

    private static final java.lang.String FLUME_CATEGORY3 = "metrics/flume/flume/CHANNEL";

    private static final java.lang.String FLUME_CATEGORY4 = "metrics/flume/flume/CHANNEL/c1";

    private static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name");

    private static final java.lang.String HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name");

    private static final java.lang.String COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name");

    private org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration;

    @org.junit.runners.Parameterized.Parameters
    public static java.util.Collection<java.lang.Object[]> configs() {
        org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration1 = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", false);
        org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration2 = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", true);
        org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration3 = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", false);
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ configuration1 }, new java.lang.Object[]{ configuration2 }, new java.lang.Object[]{ configuration3 } });
    }

    public GangliaPropertyProviderTest(org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration) {
        this.configuration = configuration;
    }

    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testGangliaPropertyProviderAsClusterAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator("ClusterAdmin", 2L));
        testPopulateResources();
        testPopulateResources_checkHostComponent();
        testPopulateResources_checkHost();
        testPopulateManyResources();
        testPopulateResources__LargeNumberOfHostResources();
        testPopulateResources_params();
        testPopulateResources_paramsMixed();
        testPopulateResources_paramsAll();
        testPopulateResources_params_category1();
        testPopulateResources_params_category2();
        testPopulateResources_params_category3();
        testPopulateResources_params_category4();
    }

    @org.junit.Test
    public void testGangliaPropertyProviderAsAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("Admin"));
        testPopulateResources();
        testPopulateResources_checkHostComponent();
        testPopulateResources_checkHost();
        testPopulateManyResources();
        testPopulateResources__LargeNumberOfHostResources();
        testPopulateResources_params();
        testPopulateResources_paramsMixed();
        testPopulateResources_paramsAll();
        testPopulateResources_params_category1();
        testPopulateResources_params_category2();
        testPopulateResources_params_category3();
        testPopulateResources_params_category4();
    }

    @org.junit.Test
    public void testGangliaPropertyProviderAsServiceAdministrator() throws java.lang.Exception {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator("ServiceAdmin", 2L));
        testPopulateResources();
        testPopulateResources_checkHostComponent();
        testPopulateResources_checkHost();
        testPopulateManyResources();
        testPopulateResources__LargeNumberOfHostResources();
        testPopulateResources_params();
        testPopulateResources_paramsMixed();
        testPopulateResources_paramsAll();
        testPopulateResources_params_category1();
        testPopulateResources_params_category2();
        testPopulateResources_params_category3();
        testPopulateResources_params_category4();
    }

    @org.junit.Test(expected = org.apache.ambari.server.security.authorization.AuthorizationException.class)
    public void testGangliaPropertyProviderAsViewUser() throws java.lang.Exception {
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createViewUser("ViewUser", 2L));
        testPopulateResources();
        testPopulateResources_checkHostComponent();
        testPopulateResources_checkHost();
        testPopulateManyResources();
        testPopulateResources__LargeNumberOfHostResources();
        testPopulateResources_params();
        testPopulateResources_paramsMixed();
        testPopulateResources_paramsAll();
        testPopulateResources_params_category1();
        testPopulateResources_params_category2();
        testPopulateResources_params_category3();
        testPopulateResources_params_category4();
    }

    public void testPopulateResources() throws java.lang.Exception {
        setUpCommonMocks();
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("temporal_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(gangliaPropertyIds, streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.lang.String expected = (configuration.isHttpsEnabled() ? "https" : "http") + "://domU-12-31-39-0E-34-E1.compute-1.internal/cgi-bin/rrd.py?c=HDPDataNode%2CHDPSlaves&h=domU-12-31-39-0E-34-E1.compute-1.internal&m=proc_total&s=10&e=20&r=1";
        org.junit.Assert.assertEquals(expected, streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(4, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID));
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "TASKTRACKER");
        temporalInfoMap = new java.util.HashMap<>();
        java.util.Set<java.lang.String> properties = new java.util.HashSet<>();
        java.lang.String shuffle_exceptions_caught = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/mapred/shuffleOutput", "shuffle_exceptions_caught");
        java.lang.String shuffle_failed_outputs = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/mapred/shuffleOutput", "shuffle_failed_outputs");
        java.lang.String shuffle_output_bytes = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/mapred/shuffleOutput", "shuffle_output_bytes");
        java.lang.String shuffle_success_outputs = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/mapred/shuffleOutput", "shuffle_success_outputs");
        properties.add(shuffle_exceptions_caught);
        properties.add(shuffle_failed_outputs);
        properties.add(shuffle_output_bytes);
        properties.add(shuffle_success_outputs);
        request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(properties, temporalInfoMap);
        temporalInfoMap.put(shuffle_exceptions_caught, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        temporalInfoMap.put(shuffle_failed_outputs, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        temporalInfoMap.put(shuffle_output_bytes, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        temporalInfoMap.put(shuffle_success_outputs, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> metricsRegexes = new java.util.ArrayList<>();
        metricsRegexes.add("metrics/mapred/shuffleOutput/shuffle_exceptions_caught");
        metricsRegexes.add("metrics/mapred/shuffleOutput/shuffle_failed_outputs");
        metricsRegexes.add("metrics/mapred/shuffleOutput/shuffle_output_bytes");
        metricsRegexes.add("metrics/mapred/shuffleOutput/shuffle_success_outputs");
        java.lang.String metricsList = getMetricsRegexes(metricsRegexes, gangliaPropertyIds, "TASKTRACKER");
        org.apache.http.client.utils.URIBuilder expectedUri = new org.apache.http.client.utils.URIBuilder();
        expectedUri.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        expectedUri.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setPath("/cgi-bin/rrd.py");
        expectedUri.setParameter("c", "HDPTaskTracker,HDPSlaves");
        expectedUri.setParameter("h", "domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setParameter("m", metricsList);
        expectedUri.setParameter("s", "10");
        expectedUri.setParameter("e", "20");
        expectedUri.setParameter("r", "1");
        org.apache.http.client.utils.URIBuilder actualUri = new org.apache.http.client.utils.URIBuilder(streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(expectedUri.getScheme(), actualUri.getScheme());
        org.junit.Assert.assertEquals(expectedUri.getHost(), actualUri.getHost());
        org.junit.Assert.assertEquals(expectedUri.getPath(), actualUri.getPath());
        org.junit.Assert.assertTrue(isUrlParamsEquals(actualUri, expectedUri));
        org.junit.Assert.assertEquals(7, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(shuffle_exceptions_caught));
        java.lang.Number[][] dataPoints = ((java.lang.Number[][]) (resource.getPropertyValue(shuffle_exceptions_caught)));
        org.junit.Assert.assertEquals(106, dataPoints.length);
        for (int i = 0; i < dataPoints.length; ++i) {
            org.junit.Assert.assertEquals((i >= 10) && (i < 20) ? 7 : 0.0, dataPoints[i][0]);
            org.junit.Assert.assertEquals((360 * i) + 1358434800, dataPoints[i][1]);
        }
        org.junit.Assert.assertNotNull(resource.getPropertyValue(shuffle_failed_outputs));
        org.junit.Assert.assertNotNull(resource.getPropertyValue(shuffle_output_bytes));
        org.junit.Assert.assertNotNull(resource.getPropertyValue(shuffle_success_outputs));
    }

    public void testPopulateResources_checkHostComponent() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("temporal_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider = org.powermock.api.easymock.PowerMock.createPartialMock(org.apache.ambari.server.controller.metrics.MetricHostProvider.class, "isCollectorHostLive", "isCollectorComponentLive");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "DATANODE");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID), temporalInfoMap);
        EasyMock.expect(hostProvider.getCollectorHostName(EasyMock.anyObject(java.lang.String.class), EasyMock.eq(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA))).andReturn("ganglia-host");
        EasyMock.expect(hostProvider.isCollectorComponentLive(EasyMock.anyObject(java.lang.String.class), EasyMock.eq(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA))).andReturn(true).once();
        EasyMock.expect(hostProvider.isCollectorHostLive(EasyMock.anyObject(java.lang.String.class), EasyMock.eq(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA))).andReturn(true).once();
        org.powermock.api.easymock.PowerMock.replay(hostProvider);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.powermock.api.easymock.PowerMock.verify(hostProvider);
        org.junit.Assert.assertEquals(1, populateResources.size());
    }

    public void testPopulateResources_checkHost() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("host_temporal_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host), streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "corp-hadoopda05.client.ext");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put("metrics/process/proc_total", new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton("metrics/process/proc_total"), temporalInfoMap);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources = propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null);
        org.junit.Assert.assertEquals(1, populateResources.size());
        org.apache.ambari.server.controller.spi.Resource res = populateResources.iterator().next();
        java.lang.Number[][] val = ((java.lang.Number[][]) (res.getPropertyValue("metrics/process/proc_total")));
        org.junit.Assert.assertEquals(226, val.length);
    }

    public void testPopulateManyResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("temporal_ganglia_data_1.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host), streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E1.compute-1.internal");
        resources.add(resource);
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E2.compute-1.internal");
        resources.add(resource);
        resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "domU-12-31-39-0E-34-E3.compute-1.internal");
        resources.add(resource);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID), temporalInfoMap);
        org.junit.Assert.assertEquals(3, propertyProvider.populateResources(resources, request, null).size());
        org.apache.http.client.utils.URIBuilder uriBuilder = new org.apache.http.client.utils.URIBuilder();
        uriBuilder.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        uriBuilder.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        uriBuilder.setPath("/cgi-bin/rrd.py");
        uriBuilder.setParameter("c", "HDPJobTracker,HDPHBaseMaster,HDPResourceManager,HDPFlumeServer,HDPSlaves,HDPHistoryServer,HDPJournalNode,HDPTaskTracker,HDPHBaseRegionServer,HDPNameNode");
        uriBuilder.setParameter("h", "domU-12-31-39-0E-34-E3.compute-1.internal,domU-12-31-39-0E-34-E1.compute-1.internal,domU-12-31-39-0E-34-E2.compute-1.internal");
        uriBuilder.setParameter("m", "proc_total");
        uriBuilder.setParameter("s", "10");
        uriBuilder.setParameter("e", "20");
        uriBuilder.setParameter("r", "1");
        java.lang.String expected = uriBuilder.toString();
        java.util.List<java.lang.String> components = java.util.Arrays.asList(new java.lang.String[]{ "HDPJobTracker", "HDPHBaseMaster", "HDPResourceManager", "HDPFlumeServer", "HDPSlaves", "HDPHistoryServer", "HDPJournalNode", "HDPTaskTracker", "HDPHBaseRegionServer", "HDPNameNode" });
        java.util.List<java.lang.String> hosts = java.util.Arrays.asList(new java.lang.String[]{ "domU-12-31-39-0E-34-E3.compute-1.internal", "domU-12-31-39-0E-34-E1.compute-1.internal", "domU-12-31-39-0E-34-E2.compute-1.internal" });
        int httpsVariation = (configuration.isHttpsEnabled()) ? 1 : 0;
        org.junit.Assert.assertEquals(expected.substring(0, 66 + httpsVariation), streamProvider.getLastSpec().substring(0, 66 + httpsVariation));
        org.junit.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isStringPermutationOfCollection(streamProvider.getLastSpec().substring(66 + httpsVariation, 236 + httpsVariation), components, "%2C", 0, 0));
        org.junit.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isStringPermutationOfCollection(streamProvider.getLastSpec().substring(239 + httpsVariation, 368 + httpsVariation), hosts, "%2C", 0, 0));
        org.junit.Assert.assertEquals(expected.substring(369 + httpsVariation), streamProvider.getLastSpec().substring(369 + httpsVariation));
        for (org.apache.ambari.server.controller.spi.Resource res : resources) {
            org.junit.Assert.assertEquals(3, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(res).size());
            org.junit.Assert.assertNotNull(res.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID));
        }
    }

    public void testPopulateResources__LargeNumberOfHostResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("temporal_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Host), streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.lang.StringBuilder hostsList = new java.lang.StringBuilder();
        for (int i = 0; i < 150; ++i) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Host);
            resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
            resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "host" + i);
            resources.add(resource);
            if (hostsList.length() != 0)
                hostsList.append(",host").append(i);
            else
                hostsList.append("host").append(i);

        }
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID), temporalInfoMap);
        org.junit.Assert.assertEquals(150, propertyProvider.populateResources(resources, request, null).size());
        org.apache.http.client.utils.URIBuilder expectedUri = new org.apache.http.client.utils.URIBuilder();
        expectedUri.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        expectedUri.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setPath("/cgi-bin/rrd.py");
        expectedUri.setParameter("c", "HDPJobTracker,HDPHBaseMaster,HDPResourceManager,HDPFlumeServer,HDPSlaves,HDPHistoryServer,HDPJournalNode,HDPTaskTracker,HDPHBaseRegionServer,HDPNameNode");
        expectedUri.setParameter("h", hostsList.toString());
        expectedUri.setParameter("m", "proc_total");
        expectedUri.setParameter("s", "10");
        expectedUri.setParameter("e", "20");
        expectedUri.setParameter("r", "1");
        org.apache.http.client.utils.URIBuilder actualUri = new org.apache.http.client.utils.URIBuilder(streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(expectedUri.getScheme(), actualUri.getScheme());
        org.junit.Assert.assertEquals(expectedUri.getHost(), actualUri.getHost());
        org.junit.Assert.assertEquals(expectedUri.getPath(), actualUri.getPath());
        org.junit.Assert.assertTrue(isUrlParamsEquals(actualUri, expectedUri));
    }

    public void testPopulateResources_params() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("flume_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(gangliaPropertyIds, streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "FLUME_HANDLER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> metricsRegexes = new java.util.ArrayList<>();
        metricsRegexes.add(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY);
        java.lang.String metricsList = getMetricsRegexes(metricsRegexes, gangliaPropertyIds, "FLUME_HANDLER");
        org.apache.http.client.utils.URIBuilder expectedUri = new org.apache.http.client.utils.URIBuilder();
        expectedUri.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        expectedUri.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setPath("/cgi-bin/rrd.py");
        expectedUri.setParameter("c", "HDPFlumeServer,HDPSlaves");
        expectedUri.setParameter("h", "ip-10-39-113-33.ec2.internal");
        expectedUri.setParameter("m", metricsList);
        expectedUri.setParameter("s", "10");
        expectedUri.setParameter("e", "20");
        expectedUri.setParameter("r", "1");
        org.apache.http.client.utils.URIBuilder actualUri = new org.apache.http.client.utils.URIBuilder(streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(expectedUri.getScheme(), actualUri.getScheme());
        org.junit.Assert.assertEquals(expectedUri.getHost(), actualUri.getHost());
        org.junit.Assert.assertEquals(expectedUri.getPath(), actualUri.getPath());
        org.junit.Assert.assertTrue(isUrlParamsEquals(actualUri, expectedUri));
        org.junit.Assert.assertEquals(4, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY));
    }

    public void testPopulateResources_paramsMixed() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("flume_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(gangliaPropertyIds, streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "FLUME_HANDLER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        java.util.Set<java.lang.String> ids = new java.util.HashSet<>();
        ids.add(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY2);
        ids.add(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID2);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(ids, temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> metricsRegexes = new java.util.ArrayList<>();
        metricsRegexes.add("metrics/flume");
        metricsRegexes.add("metrics/cpu/cpu_wio");
        java.lang.String metricsList = getMetricsRegexes(metricsRegexes, gangliaPropertyIds, "FLUME_HANDLER");
        org.apache.http.client.utils.URIBuilder expectedUri = new org.apache.http.client.utils.URIBuilder();
        expectedUri.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        expectedUri.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setPath("/cgi-bin/rrd.py");
        expectedUri.setParameter("c", "HDPFlumeServer,HDPSlaves");
        expectedUri.setParameter("h", "ip-10-39-113-33.ec2.internal");
        expectedUri.setParameter("m", metricsList);
        expectedUri.setParameter("e", "now");
        expectedUri.setParameter("pt", "true");
        org.apache.http.client.utils.URIBuilder actualUri = new org.apache.http.client.utils.URIBuilder(streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(expectedUri.getScheme(), actualUri.getScheme());
        org.junit.Assert.assertEquals(expectedUri.getHost(), actualUri.getHost());
        org.junit.Assert.assertEquals(expectedUri.getPath(), actualUri.getPath());
        org.junit.Assert.assertTrue(isUrlParamsEquals(actualUri, expectedUri));
        org.junit.Assert.assertEquals(23, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.PROPERTY_ID2));
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY));
    }

    public void testPopulateResources_paramsAll() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("flume_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent), streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "FLUME_HANDLER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.emptySet(), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.lang.String expected = (configuration.isHttpsEnabled() ? "https" : "http") + "://domU-12-31-39-0E-34-E1.compute-1.internal/cgi-bin/rrd.py?c=HDPFlumeServer%2CHDPSlaves&h=ip-10-39-113-33.ec2.internal&m=";
        java.util.List<java.lang.String> components = java.util.Arrays.asList(new java.lang.String[]{ "HDPFlumeServer", "HDPSlaves" });
        int httpsVariation = (configuration.isHttpsEnabled()) ? 1 : 0;
        org.junit.Assert.assertEquals(expected.substring(0, 66 + httpsVariation), streamProvider.getLastSpec().substring(0, 66 + httpsVariation));
        org.junit.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isStringPermutationOfCollection(streamProvider.getLastSpec().substring(66 + httpsVariation, 92 + httpsVariation), components, "%2C", 0, 0));
        org.junit.Assert.assertTrue(streamProvider.getLastSpec().substring(92 + httpsVariation).startsWith(expected.substring(92 + httpsVariation)));
        org.junit.Assert.assertEquals(34, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY));
    }

    public void testPopulateResources_params_category1() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("flume_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(gangliaPropertyIds, streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "FLUME_HANDLER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> metricsRegexes = new java.util.ArrayList<>();
        metricsRegexes.add("metrics/flume");
        java.lang.String metricsList = getMetricsRegexes(metricsRegexes, gangliaPropertyIds, "FLUME_HANDLER");
        org.apache.http.client.utils.URIBuilder expectedUri = new org.apache.http.client.utils.URIBuilder();
        expectedUri.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        expectedUri.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setPath("/cgi-bin/rrd.py");
        expectedUri.setParameter("c", "HDPFlumeServer,HDPSlaves");
        expectedUri.setParameter("h", "ip-10-39-113-33.ec2.internal");
        expectedUri.setParameter("m", metricsList);
        expectedUri.setParameter("s", "10");
        expectedUri.setParameter("e", "20");
        expectedUri.setParameter("r", "1");
        org.apache.http.client.utils.URIBuilder actualUri = new org.apache.http.client.utils.URIBuilder(streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(expectedUri.getScheme(), actualUri.getScheme());
        org.junit.Assert.assertEquals(expectedUri.getHost(), actualUri.getHost());
        org.junit.Assert.assertEquals(expectedUri.getPath(), actualUri.getPath());
        org.junit.Assert.assertTrue(isUrlParamsEquals(actualUri, expectedUri));
        org.junit.Assert.assertEquals(22, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY));
    }

    public void testPopulateResources_params_category2() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("flume_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(gangliaPropertyIds, streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "FLUME_HANDLER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY2, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY2), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> metricsRegexes = new java.util.ArrayList<>();
        metricsRegexes.add("metrics/flume/");
        java.lang.String metricsList = getMetricsRegexes(metricsRegexes, gangliaPropertyIds, "FLUME_HANDLER");
        org.apache.http.client.utils.URIBuilder expectedUri = new org.apache.http.client.utils.URIBuilder();
        expectedUri.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        expectedUri.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setPath("/cgi-bin/rrd.py");
        expectedUri.setParameter("c", "HDPFlumeServer,HDPSlaves");
        expectedUri.setParameter("h", "ip-10-39-113-33.ec2.internal");
        expectedUri.setParameter("m", metricsList);
        expectedUri.setParameter("s", "10");
        expectedUri.setParameter("e", "20");
        expectedUri.setParameter("r", "1");
        org.apache.http.client.utils.URIBuilder actualUri = new org.apache.http.client.utils.URIBuilder(streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(expectedUri.getScheme(), actualUri.getScheme());
        org.junit.Assert.assertEquals(expectedUri.getHost(), actualUri.getHost());
        org.junit.Assert.assertEquals(expectedUri.getPath(), actualUri.getPath());
        org.junit.Assert.assertTrue(isUrlParamsEquals(actualUri, expectedUri));
        org.junit.Assert.assertEquals(22, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY));
    }

    public void testPopulateResources_params_category3() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("flume_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(gangliaPropertyIds, streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "FLUME_HANDLER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY3, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY3), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> metricsRegexes = new java.util.ArrayList<>();
        metricsRegexes.add("metrics/flume/$1/CHANNEL/$2/");
        metricsRegexes.add(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY);
        java.lang.String metricsList = getMetricsRegexes(metricsRegexes, gangliaPropertyIds, "FLUME_HANDLER");
        org.apache.http.client.utils.URIBuilder expectedUri = new org.apache.http.client.utils.URIBuilder();
        expectedUri.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        expectedUri.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setPath("/cgi-bin/rrd.py");
        expectedUri.setParameter("c", "HDPFlumeServer,HDPSlaves");
        expectedUri.setParameter("h", "ip-10-39-113-33.ec2.internal");
        expectedUri.setParameter("m", metricsList);
        expectedUri.setParameter("s", "10");
        expectedUri.setParameter("e", "20");
        expectedUri.setParameter("r", "1");
        org.apache.http.client.utils.URIBuilder actualUri = new org.apache.http.client.utils.URIBuilder(streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(expectedUri.getScheme(), actualUri.getScheme());
        org.junit.Assert.assertEquals(expectedUri.getHost(), actualUri.getHost());
        org.junit.Assert.assertEquals(expectedUri.getPath(), actualUri.getPath());
        org.junit.Assert.assertTrue(isUrlParamsEquals(actualUri, expectedUri));
        org.junit.Assert.assertEquals(12, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY));
    }

    public void testPopulateResources_params_category4() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("flume_ganglia_data.txt");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.TestGangliaHostProvider();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaHostComponentPropertyProvider(gangliaPropertyIds, streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.HOST_NAME_PROPERTY_ID, "ip-10-39-113-33.ec2.internal");
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.COMPONENT_NAME_PROPERTY_ID, "FLUME_HANDLER");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY4, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CATEGORY4), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.util.List<java.lang.String> metricsRegexes = new java.util.ArrayList<>();
        metricsRegexes.add("metrics/flume/$1/CHANNEL/$2");
        metricsRegexes.add(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY);
        java.lang.String metricsList = getMetricsRegexes(metricsRegexes, gangliaPropertyIds, "FLUME_HANDLER");
        org.apache.http.client.utils.URIBuilder expectedUri = new org.apache.http.client.utils.URIBuilder();
        expectedUri.setScheme(configuration.isHttpsEnabled() ? "https" : "http");
        expectedUri.setHost("domU-12-31-39-0E-34-E1.compute-1.internal");
        expectedUri.setPath("/cgi-bin/rrd.py");
        expectedUri.setParameter("c", "HDPFlumeServer,HDPSlaves");
        expectedUri.setParameter("h", "ip-10-39-113-33.ec2.internal");
        expectedUri.setParameter("m", metricsList);
        expectedUri.setParameter("s", "10");
        expectedUri.setParameter("e", "20");
        expectedUri.setParameter("r", "1");
        org.apache.http.client.utils.URIBuilder actualUri = new org.apache.http.client.utils.URIBuilder(streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(expectedUri.getScheme(), actualUri.getScheme());
        org.junit.Assert.assertEquals(expectedUri.getHost(), actualUri.getHost());
        org.junit.Assert.assertEquals(expectedUri.getPath(), actualUri.getPath());
        org.junit.Assert.assertTrue(isUrlParamsEquals(actualUri, expectedUri));
        org.junit.Assert.assertEquals(12, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.FLUME_CHANNEL_CAPACITY_PROPERTY));
    }

    private boolean isUrlParamsEquals(org.apache.http.client.utils.URIBuilder actualUri, org.apache.http.client.utils.URIBuilder expectedUri) {
        for (final org.apache.http.NameValuePair expectedParam : expectedUri.getQueryParams()) {
            org.apache.http.NameValuePair actualParam = ((org.apache.http.NameValuePair) (org.apache.commons.collections.CollectionUtils.find(actualUri.getQueryParams(), new org.apache.commons.collections.Predicate() {
                @java.lang.Override
                public boolean evaluate(java.lang.Object arg0) {
                    if (!(arg0 instanceof org.apache.http.NameValuePair))
                        return false;

                    org.apache.http.NameValuePair otherObj = ((org.apache.http.NameValuePair) (arg0));
                    return otherObj.getName().equals(expectedParam.getName());
                }
            })));
            if (actualParam == null) {
                return false;
            }
            java.util.List<java.lang.String> actualParamList = new java.util.ArrayList<>(java.util.Arrays.asList(actualParam.getValue().split(",")));
            java.util.List<java.lang.String> expectedParamList = new java.util.ArrayList<>(java.util.Arrays.asList(expectedParam.getValue().split(",")));
            java.util.Collections.sort(actualParamList);
            java.util.Collections.sort(expectedParamList);
            if (!actualParamList.equals(expectedParamList))
                return false;

        }
        return true;
    }

    private java.lang.String getMetricsRegexes(java.util.List<java.lang.String> metricsRegexes, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> gangliaPropertyIds, java.lang.String componentName) {
        java.lang.StringBuilder metricsBuilder = new java.lang.StringBuilder();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> entry : gangliaPropertyIds.get(componentName).entrySet()) {
            for (java.lang.String metricRegex : metricsRegexes) {
                if (entry.getKey().startsWith(metricRegex)) {
                    metricsBuilder.append(entry.getValue().getPropertyId()).append(",");
                }
            }
        }
        return metricsBuilder.toString();
    }

    private void setUpCommonMocks() throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.AmbariManagementController amc = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        java.lang.reflect.Field field = org.apache.ambari.server.controller.AmbariServer.class.getDeclaredField("clusterController");
        field.setAccessible(true);
        field.set(null, amc);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(amc.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster(org.apache.ambari.server.controller.metrics.ganglia.GangliaPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getResourceId()).andReturn(2L).anyTimes();
        try {
            EasyMock.expect(clusters.getCluster(EasyMock.anyObject(java.lang.String.class))).andReturn(cluster).anyTimes();
        } catch (org.apache.ambari.server.AmbariException e) {
            e.printStackTrace();
        }
        EasyMock.replay(amc, clusters, cluster);
        org.powermock.api.easymock.PowerMock.replayAll();
    }

    public static class TestGangliaServiceProvider implements org.apache.ambari.server.controller.metrics.MetricsServiceProvider {
        @java.lang.Override
        public org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService getMetricsServiceType() {
            return org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA;
        }
    }

    public static class TestGangliaHostProvider implements org.apache.ambari.server.controller.metrics.MetricHostProvider {
        private boolean isHostLive;

        private boolean isComponentLive;

        public TestGangliaHostProvider() {
            this(true, true);
        }

        public TestGangliaHostProvider(boolean isHostLive, boolean isComponentLive) {
            this.isHostLive = isHostLive;
            this.isComponentLive = isComponentLive;
        }

        @java.lang.Override
        public java.lang.String getCollectorHostName(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) {
            return "domU-12-31-39-0E-34-E1.compute-1.internal";
        }

        @java.lang.Override
        public java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException {
            return null;
        }

        @java.lang.Override
        public java.lang.String getCollectorPort(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return null;
        }

        @java.lang.Override
        public boolean isCollectorHostLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return isHostLive;
        }

        @java.lang.Override
        public boolean isCollectorComponentLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
            return isComponentLive;
        }

        @java.lang.Override
        public boolean isCollectorHostExternal(java.lang.String clusterName) {
            return false;
        }
    }
}