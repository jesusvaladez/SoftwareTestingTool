package org.apache.ambari.server.controller.internal;
public class AbstractPropertyProviderTest {
    @org.junit.Test
    public void testGetComponentMetrics() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.internal.AbstractPropertyProvider provider = new org.apache.ambari.server.controller.internal.AbstractPropertyProviderTest.TestPropertyProvider(componentMetrics);
        org.junit.Assert.assertEquals(componentMetrics, provider.getComponentMetrics());
    }

    @org.junit.Test
    public void testGetPropertyInfoMap() {
        org.apache.ambari.server.controller.internal.AbstractPropertyProvider provider = new org.apache.ambari.server.controller.internal.AbstractPropertyProviderTest.TestPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent));
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = provider.getPropertyInfoMap("NAMENODE", "metrics/cpu/cpu_aidle");
        org.junit.Assert.assertEquals(1, propertyInfoMap.size());
        org.junit.Assert.assertTrue(propertyInfoMap.containsKey("metrics/cpu/cpu_aidle"));
        propertyInfoMap = provider.getPropertyInfoMap("NAMENODE", "metrics/disk");
        org.junit.Assert.assertEquals(3, propertyInfoMap.size());
        org.junit.Assert.assertTrue(propertyInfoMap.containsKey("metrics/disk/disk_free"));
        org.junit.Assert.assertTrue(propertyInfoMap.containsKey("metrics/disk/disk_total"));
        org.junit.Assert.assertTrue(propertyInfoMap.containsKey("metrics/disk/part_max_used"));
    }

    @org.junit.Test
    public void testGetJMXPropertyInfoMap() {
        org.apache.ambari.server.controller.internal.AbstractPropertyProvider provider = new org.apache.ambari.server.controller.internal.AbstractPropertyProviderTest.TestPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent));
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> propertyInfoMap = provider.getPropertyInfoMap("DATANODE", "metrics");
        org.junit.Assert.assertEquals(86, propertyInfoMap.size());
        propertyInfoMap = provider.getPropertyInfoMap("DATANODE", "metrics/rpc/RpcQueueTime_avg_time");
        org.junit.Assert.assertEquals(1, propertyInfoMap.size());
        org.junit.Assert.assertTrue(propertyInfoMap.containsKey("metrics/rpc/RpcQueueTime_avg_time"));
        propertyInfoMap = provider.getPropertyInfoMap("DATANODE", "metrics/rpc/");
        org.junit.Assert.assertEquals(12, propertyInfoMap.size());
        org.junit.Assert.assertTrue(propertyInfoMap.containsKey("metrics/rpc/RpcQueueTime_avg_time"));
    }

    @org.junit.Test
    public void testSubstituteArguments() throws java.lang.Exception {
        java.lang.String newPropertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument("category/name1/$1/name2/$2", "$1", "foo");
        org.junit.Assert.assertEquals("category/name1/foo/name2/$2", newPropertyId);
        newPropertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument("category/name1/$1/name2/$2", "$2", "bar");
        org.junit.Assert.assertEquals("category/name1/$1/name2/bar", newPropertyId);
        newPropertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument("category/name1/$1.toLowerCase()/name2/$2.toUpperCase()", "$1", "FOO");
        org.junit.Assert.assertEquals("category/name1/foo/name2/$2.toUpperCase()", newPropertyId);
        newPropertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument("category/name1/$1.toLowerCase()/name2/$2.toUpperCase()", "$2", "bar");
        org.junit.Assert.assertEquals("category/name1/$1.toLowerCase()/name2/BAR", newPropertyId);
        newPropertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument("category/name1/$1.toLowerCase().substring(1)/name2", "$1", "FOO");
        org.junit.Assert.assertEquals("category/name1/oo/name2", newPropertyId);
        newPropertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument("category/name1/$1.toLowerCase().substring(1).concat(\"_post\")/name2/$2.concat(\"_post\")", "$1", "FOO");
        newPropertyId = org.apache.ambari.server.controller.internal.AbstractPropertyProvider.substituteArgument(newPropertyId, "$2", "bar");
        org.junit.Assert.assertEquals("category/name1/oo_post/name2/bar_post", newPropertyId);
    }

    @org.junit.Test
    public void testUpdateComponentMetricMapHDP1() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        org.apache.ambari.server.controller.internal.AbstractPropertyProvider provider = new org.apache.ambari.server.controller.internal.AbstractPropertyProviderTest.TestPropertyProvider(componentMetrics);
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> flumeMetrics = provider.getComponentMetrics().get("FLUME_HANDLER");
        int metricsBefore = flumeMetrics.size();
        java.lang.String specificMetric = "metrics/flume/arg1/CHANNEL/arg2/ChannelCapacity";
        java.lang.String specificPropertyInfoId = "arg1.CHANNEL.arg2.ChannelCapacity";
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> componentMetricMap = provider.getComponentMetrics().get("FLUME_HANDLER");
        org.junit.Assert.assertNull(flumeMetrics.get(specificMetric));
        provider.updateComponentMetricMap(componentMetricMap, specificMetric);
        org.junit.Assert.assertEquals(metricsBefore + 1, flumeMetrics.size());
        org.junit.Assert.assertNotNull(flumeMetrics.get(specificMetric));
        org.junit.Assert.assertEquals(specificPropertyInfoId, flumeMetrics.get(specificMetric).getPropertyId());
    }

    private static class TestPropertyProvider extends org.apache.ambari.server.controller.internal.AbstractPropertyProvider {
        public TestPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics) {
            super(componentMetrics);
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
            return null;
        }
    }
}