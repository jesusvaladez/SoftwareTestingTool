package org.apache.ambari.server.controller.metrics;
import com.google.inject.assistedinject.Assisted;
import javax.annotation.Nullable;
public interface MetricPropertyProviderFactory {
    org.apache.ambari.server.controller.jmx.JMXPropertyProvider createJMXPropertyProvider(@com.google.inject.assistedinject.Assisted("componentMetrics")
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics, @com.google.inject.assistedinject.Assisted("streamProvider")
    org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, @com.google.inject.assistedinject.Assisted("jmxHostProvider")
    org.apache.ambari.server.controller.jmx.JMXHostProvider jmxHostProvider, @com.google.inject.assistedinject.Assisted("metricHostProvider")
    org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider, @com.google.inject.assistedinject.Assisted("clusterNamePropertyId")
    java.lang.String clusterNamePropertyId, @com.google.inject.assistedinject.Assisted("hostNamePropertyId")
    @javax.annotation.Nullable
    java.lang.String hostNamePropertyId, @com.google.inject.assistedinject.Assisted("componentNamePropertyId")
    java.lang.String componentNamePropertyId, @com.google.inject.assistedinject.Assisted("statePropertyId")
    @javax.annotation.Nullable
    java.lang.String statePropertyId);

    org.apache.ambari.server.controller.metrics.RestMetricsPropertyProvider createRESTMetricsPropertyProvider(@com.google.inject.assistedinject.Assisted("metricsProperties")
    java.util.Map<java.lang.String, java.lang.String> metricsProperties, @com.google.inject.assistedinject.Assisted("componentMetrics")
    java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics, @com.google.inject.assistedinject.Assisted("streamProvider")
    org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, @com.google.inject.assistedinject.Assisted("metricHostProvider")
    org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider, @com.google.inject.assistedinject.Assisted("clusterNamePropertyId")
    java.lang.String clusterNamePropertyId, @com.google.inject.assistedinject.Assisted("hostNamePropertyId")
    @javax.annotation.Nullable
    java.lang.String hostNamePropertyId, @com.google.inject.assistedinject.Assisted("componentNamePropertyId")
    java.lang.String componentNamePropertyId, @com.google.inject.assistedinject.Assisted("statePropertyId")
    @javax.annotation.Nullable
    java.lang.String statePropertyId, @com.google.inject.assistedinject.Assisted("componentName")
    @javax.annotation.Nullable
    java.lang.String componentName);
}