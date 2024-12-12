package org.apache.ambari.server.metric.system.impl;
public class MetricsServiceTest {
    @org.junit.Test
    public void testMetricsServiceStart() {
        org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl metricsService = new org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl();
        metricsService.start();
        org.apache.ambari.server.metrics.system.MetricsSource source = org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.getSource("jvm");
        junit.framework.Assert.assertNotNull(source);
        junit.framework.Assert.assertTrue(source instanceof org.apache.ambari.server.metrics.system.impl.JvmMetricsSource);
        source = org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.getSource("testsource");
        junit.framework.Assert.assertNotNull(source);
        junit.framework.Assert.assertTrue(source instanceof org.apache.ambari.server.metric.system.impl.TestMetricsSource);
    }
}