package org.apache.ambari.server.metric.system.impl;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class MetricsSourceTest {
    @org.junit.Test
    public void testJvmSourceInit_PreJVM1_8() {
        org.junit.Assume.assumeThat(java.lang.System.getProperty("java.version"), new org.apache.ambari.server.metric.system.impl.MetricsSourceTest.LessThanVersionMatcher("1.8"));
        testJvmSourceInit(39);
    }

    @org.junit.Test
    public void testJvmSourceInit_JVM1_8() {
        org.junit.Assume.assumeThat(java.lang.System.getProperty("java.version"), new org.apache.ambari.server.metric.system.impl.MetricsSourceTest.VersionMatcher("1.8"));
        testJvmSourceInit(40);
    }

    private void testJvmSourceInit(int metricsSize) {
        org.apache.ambari.server.metrics.system.impl.JvmMetricsSource jvmMetricsSource = new org.apache.ambari.server.metrics.system.impl.JvmMetricsSource();
        org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration = org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.getMetricsConfiguration();
        org.apache.ambari.server.metrics.system.MetricsSink sink = new org.apache.ambari.server.metric.system.impl.TestAmbariMetricsSinkImpl();
        jvmMetricsSource.init(configuration, sink);
        org.junit.Assert.assertEquals(jvmMetricsSource.getMetrics().size(), metricsSize);
    }

    private class VersionMatcher extends org.hamcrest.BaseMatcher<java.lang.String> {
        private final float version;

        VersionMatcher(java.lang.String version) {
            this.version = java.lang.Float.parseFloat(version);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            return parseVersion(((java.lang.String) (o))) == this.version;
        }

        float parseVersion(java.lang.String versionString) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("(\\d+(?:\\.\\d+)).*");
            java.util.regex.Matcher matcher = p.matcher(versionString);
            if (matcher.matches()) {
                return java.lang.Float.parseFloat(matcher.group(1));
            } else {
                return 0.0F;
            }
        }

        @java.lang.Override
        public void describeTo(org.hamcrest.Description description) {
        }

        public float getVersion() {
            return version;
        }
    }

    private class LessThanVersionMatcher extends org.apache.ambari.server.metric.system.impl.MetricsSourceTest.VersionMatcher {
        LessThanVersionMatcher(java.lang.String version) {
            super(version);
        }

        @java.lang.Override
        public boolean matches(java.lang.Object o) {
            return parseVersion(((java.lang.String) (o))) < getVersion();
        }

        @java.lang.Override
        public void describeTo(org.hamcrest.Description description) {
        }
    }

    @org.junit.Test(timeout = 20000)
    public void testDatabaseMetricSourcePublish() throws java.lang.InterruptedException {
        java.util.Map<java.lang.String, java.lang.Long> metricsMap = new java.util.HashMap<>();
        metricsMap.put("Timer.UpdateObjectQuery.HostRoleCommandEntity", 10000L);
        metricsMap.put("Timer.UpdateObjectQuery.HostRoleCommandEntity.SqlPrepare", 5000L);
        metricsMap.put("Timer.DirectReadQuery", 6000L);
        metricsMap.put("Timer.ReadAllQuery.StackEntity.StackEntity.findByNameAndVersion.SqlPrepare", 15000L);
        metricsMap.put("Counter.UpdateObjectQuery.HostRoleCommandEntity", 10L);
        metricsMap.put("Counter.ReadObjectQuery.RequestEntity.request", 4330L);
        metricsMap.put("Counter.ReadObjectQuery.MetainfoEntity.readMetainfoEntity.CacheMisses", 15L);
        org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource source = new org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource();
        org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration = org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.getSubsetConfiguration(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.getMetricsConfiguration(), "source.database.");
        org.apache.ambari.server.metrics.system.MetricsSink sink = org.easymock.EasyMock.createMock(org.apache.ambari.server.metrics.system.impl.AmbariMetricSinkImpl.class);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.metrics.system.SingleMetric>> metricsCapture = org.easymock.EasyMock.newCapture();
        sink.publish(EasyMock.capture(metricsCapture));
        EasyMock.expectLastCall().once();
        EasyMock.replay(sink);
        source.init(configuration, sink);
        source.start();
        source.publish(metricsMap);
        java.lang.Thread.sleep(5000L);
        EasyMock.verify(sink);
        junit.framework.Assert.assertTrue(metricsCapture.getValue().size() == 6);
    }

    @org.junit.Test
    public void testDatabaseMetricsSourceAcceptMetric() {
        org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource source = new org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource();
        org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration = org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.getSubsetConfiguration(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.getMetricsConfiguration(), "source.database.");
        org.apache.ambari.server.metrics.system.MetricsSink sink = new org.apache.ambari.server.metric.system.impl.TestAmbariMetricsSinkImpl();
        source.init(configuration, sink);
        junit.framework.Assert.assertTrue(source.acceptMetric("Timer.UpdateObjectQuery.HostRoleCommandEntity.SqlPrepare"));
        junit.framework.Assert.assertFalse(source.acceptMetric("Counter.ReadObjectQuery.RequestEntity.request"));
        junit.framework.Assert.assertTrue(source.acceptMetric("Counter.ReadObjectQuery.MetainfoEntity.readMetainfoEntity.CacheMisses"));
    }

    @org.junit.Test
    public void testJmxInfoSerialization() throws java.lang.Exception {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        mapper.setAnnotationIntrospector(new com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector());
        org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo = new org.apache.ambari.server.state.alert.MetricSource.JmxInfo();
        jmxInfo.setValue("custom");
        jmxInfo.setPropertyList(java.util.Collections.singletonList("prop1"));
        org.apache.ambari.server.state.alert.MetricSource.JmxInfo deserialized = mapper.readValue(mapper.writeValueAsString(jmxInfo), org.apache.ambari.server.state.alert.MetricSource.JmxInfo.class);
        junit.framework.Assert.assertEquals("custom", deserialized.getValue().toString());
        junit.framework.Assert.assertEquals(java.util.Collections.singletonList("prop1"), deserialized.getPropertyList());
    }
}