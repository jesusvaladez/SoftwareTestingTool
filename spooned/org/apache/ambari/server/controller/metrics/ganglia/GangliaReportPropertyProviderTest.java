package org.apache.ambari.server.controller.metrics.ganglia;
@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
public class GangliaReportPropertyProviderTest {
    private static final java.lang.String PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("metrics/load", "Procs");

    private static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name");

    private org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration;

    @org.junit.runners.Parameterized.Parameters
    public static java.util.Collection<java.lang.Object[]> configs() {
        org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration1 = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", false);
        org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration2 = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", true);
        org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration3 = org.apache.ambari.server.configuration.ComponentSSLConfigurationTest.getConfiguration("tspath", "tspass", "tstype", false);
        return java.util.Arrays.asList(new java.lang.Object[][]{ new java.lang.Object[]{ configuration1 }, new java.lang.Object[]{ configuration2 }, new java.lang.Object[]{ configuration3 } });
    }

    public GangliaReportPropertyProviderTest(org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration) {
        this.configuration = configuration;
    }

    @org.junit.Test
    public void testPopulateResources() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider streamProvider = new org.apache.ambari.server.controller.metrics.ganglia.TestStreamProvider("temporal_ganglia_report_data.json");
        org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProviderTest.TestGangliaHostProvider hostProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProviderTest.TestGangliaHostProvider();
        org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProvider propertyProvider = new org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.Cluster), streamProvider, configuration, hostProvider, org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        resource.setProperty(org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProviderTest.CLUSTER_NAME_PROPERTY_ID, "c1");
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> temporalInfoMap = new java.util.HashMap<>();
        temporalInfoMap.put(org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProviderTest.PROPERTY_ID, new org.apache.ambari.server.controller.internal.TemporalInfoImpl(10L, 20L, 1L));
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(java.util.Collections.singleton(org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProviderTest.PROPERTY_ID), temporalInfoMap);
        org.junit.Assert.assertEquals(1, propertyProvider.populateResources(java.util.Collections.singleton(resource), request, null).size());
        java.lang.String expected = (configuration.isHttpsEnabled() ? "https" : "http") + "://domU-12-31-39-0E-34-E1.compute-1.internal/ganglia/graph.php?g=load_report&json=1";
        org.junit.Assert.assertEquals(expected, streamProvider.getLastSpec());
        org.junit.Assert.assertEquals(2, org.apache.ambari.server.controller.utilities.PropertyHelper.getProperties(resource).size());
        org.junit.Assert.assertNotNull(resource.getPropertyValue(org.apache.ambari.server.controller.metrics.ganglia.GangliaReportPropertyProviderTest.PROPERTY_ID));
    }

    private static class TestGangliaHostProvider implements org.apache.ambari.server.controller.metrics.MetricHostProvider {
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
}