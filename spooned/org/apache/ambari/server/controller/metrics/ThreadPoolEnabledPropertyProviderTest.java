package org.apache.ambari.server.controller.metrics;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
public class ThreadPoolEnabledPropertyProviderTest {
    @org.junit.Test
    public void testGetCacheKeyForException() throws java.lang.Exception {
        org.codehaus.jackson.map.ObjectMapper jmxObjectMapper = new org.codehaus.jackson.map.ObjectMapper();
        jmxObjectMapper.configure(DeserializationConfig.Feature.USE_ANNOTATIONS, false);
        org.codehaus.jackson.map.ObjectReader jmxObjectReader = jmxObjectMapper.reader(org.apache.ambari.server.controller.jmx.JMXMetricHolder.class);
        java.util.List<java.lang.Exception> exceptions = new java.util.ArrayList<>();
        for (int i = 0; i < 2; i++) {
            try {
                jmxObjectReader.readValue("Invalid string");
            } catch (java.lang.Exception e) {
                exceptions.add(e);
            }
        }
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.getCacheKeyForException(exceptions.get(0)), org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.getCacheKeyForException(exceptions.get(1)));
    }
}