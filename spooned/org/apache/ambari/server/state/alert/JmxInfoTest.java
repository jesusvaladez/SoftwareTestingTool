package org.apache.ambari.server.state.alert;
public class JmxInfoTest {
    private static final java.lang.String JMX_PROP_NAME1 = "Hadoop:service=NameNode,name=FSNamesystem/CapacityUsed";

    private static final java.lang.String JMX_PROP_NAME2 = "Hadoop:service=NameNode,name=FSNamesystem/CapacityRemaining";

    @org.junit.Test
    public void testFindJmxMetricsAndCalculateSimpleValue() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo = jmxInfoWith("{1}");
        org.apache.ambari.server.controller.jmx.JMXMetricHolder metrics = metrics(12.5, 3.5);
        org.junit.Assert.assertThat(jmxInfo.eval(metrics), org.hamcrest.core.Is.is(java.util.Optional.of(3.5)));
    }

    @org.junit.Test
    public void testFindJmxMetricsAndCalculateComplexValue() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo = jmxInfoWith("2 * ({0} + {1})");
        org.apache.ambari.server.controller.jmx.JMXMetricHolder metrics = metrics(12.5, 2.5);
        org.junit.Assert.assertThat(jmxInfo.eval(metrics), org.hamcrest.core.Is.is(java.util.Optional.of(30.0)));
    }

    @org.junit.Test
    public void testReturnsEmptyWhenJmxPropertyWasNotFound() throws java.lang.Exception {
        org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo = new org.apache.ambari.server.state.alert.MetricSource.JmxInfo();
        jmxInfo.setPropertyList(java.util.Arrays.asList("notfound/notfound"));
        org.apache.ambari.server.controller.jmx.JMXMetricHolder metrics = metrics(1, 2);
        org.junit.Assert.assertThat(jmxInfo.eval(metrics), org.hamcrest.core.Is.is(java.util.Optional.empty()));
    }

    private org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfoWith(java.lang.String value) {
        org.apache.ambari.server.state.alert.MetricSource.JmxInfo jmxInfo = new org.apache.ambari.server.state.alert.MetricSource.JmxInfo();
        jmxInfo.setValue(value);
        jmxInfo.setPropertyList(java.util.Arrays.asList(org.apache.ambari.server.state.alert.JmxInfoTest.JMX_PROP_NAME1, org.apache.ambari.server.state.alert.JmxInfoTest.JMX_PROP_NAME2));
        return jmxInfo;
    }

    private org.apache.ambari.server.controller.jmx.JMXMetricHolder metrics(final double jmxValue1, final double jmxValue2) {
        org.apache.ambari.server.controller.jmx.JMXMetricHolder metrics = new org.apache.ambari.server.controller.jmx.JMXMetricHolder();
        metrics.setBeans(java.util.Arrays.asList(new java.util.HashMap<java.lang.String, java.lang.Object>() {
            {
                put("name", name(org.apache.ambari.server.state.alert.JmxInfoTest.JMX_PROP_NAME1));
                put(key(org.apache.ambari.server.state.alert.JmxInfoTest.JMX_PROP_NAME1), jmxValue1);
            }
        }, new java.util.HashMap<java.lang.String, java.lang.Object>() {
            {
                put("name", name(org.apache.ambari.server.state.alert.JmxInfoTest.JMX_PROP_NAME2));
                put(key(org.apache.ambari.server.state.alert.JmxInfoTest.JMX_PROP_NAME2), jmxValue2);
            }
        }));
        return metrics;
    }

    private java.lang.String name(java.lang.String jmxProp) {
        return jmxProp.split("/")[0];
    }

    private java.lang.String key(java.lang.String jmxProp) {
        return jmxProp.split("/")[1];
    }
}