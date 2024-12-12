package org.apache.ambari.scom.utilities;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
public class SCOMMetricHelper {
    private static final java.lang.String SQLSERVER_PROPERTIES_FILE = "sqlserver_properties.json";

    private static final java.lang.String JMX_PROPERTIES_FILE = "jmx_properties.json";

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> JMX_PROPERTY_IDS = org.apache.ambari.scom.utilities.SCOMMetricHelper.readPropertyProviderIds(org.apache.ambari.scom.utilities.SCOMMetricHelper.JMX_PROPERTIES_FILE);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> SQLSERVER_PROPERTY_IDS = org.apache.ambari.scom.utilities.SCOMMetricHelper.readPropertyProviderIds(org.apache.ambari.scom.utilities.SCOMMetricHelper.SQLSERVER_PROPERTIES_FILE);

    public static java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> getSqlServerPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        return org.apache.ambari.scom.utilities.SCOMMetricHelper.SQLSERVER_PROPERTY_IDS.get(resourceType.getInternalType());
    }

    public static java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> getJMXPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        return org.apache.ambari.scom.utilities.SCOMMetricHelper.JMX_PROPERTY_IDS.get(resourceType.getInternalType());
    }

    protected static class Metric {
        private java.lang.String metric;

        private boolean pointInTime;

        private boolean temporal;

        private Metric() {
        }

        protected Metric(java.lang.String metric, boolean pointInTime, boolean temporal) {
            this.metric = metric;
            this.pointInTime = pointInTime;
            this.temporal = temporal;
        }

        public java.lang.String getMetric() {
            return metric;
        }

        public void setMetric(java.lang.String metric) {
            this.metric = metric;
        }

        public boolean isPointInTime() {
            return pointInTime;
        }

        public void setPointInTime(boolean pointInTime) {
            this.pointInTime = pointInTime;
        }

        public boolean isTemporal() {
            return temporal;
        }

        public void setTemporal(boolean temporal) {
            this.temporal = temporal;
        }
    }

    private static java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> readPropertyProviderIds(java.lang.String filename) {
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        try {
            java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.scom.utilities.SCOMMetricHelper.Metric>>> resourceMetricMap = mapper.readValue(java.lang.ClassLoader.getSystemResourceAsStream(filename), new org.codehaus.jackson.type.TypeReference<java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.scom.utilities.SCOMMetricHelper.Metric>>>>() {});
            java.util.Map<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>> resourceMetrics = new java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>>();
            for (java.util.Map.Entry<org.apache.ambari.server.controller.spi.Resource.InternalType, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.scom.utilities.SCOMMetricHelper.Metric>>> resourceEntry : resourceMetricMap.entrySet()) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>>();
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.scom.utilities.SCOMMetricHelper.Metric>> componentEntry : resourceEntry.getValue().entrySet()) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo> metrics = new java.util.HashMap<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>();
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.scom.utilities.SCOMMetricHelper.Metric> metricEntry : componentEntry.getValue().entrySet()) {
                        java.lang.String property = metricEntry.getKey();
                        org.apache.ambari.scom.utilities.SCOMMetricHelper.Metric metric = metricEntry.getValue();
                        metrics.put(property, new org.apache.ambari.server.controller.internal.PropertyInfo(metric.getMetric(), metric.isTemporal(), metric.isPointInTime()));
                    }
                    componentMetrics.put(componentEntry.getKey(), metrics);
                }
                resourceMetrics.put(resourceEntry.getKey(), componentMetrics);
            }
            return resourceMetrics;
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("Can't read properties file " + filename, e);
        }
    }
}