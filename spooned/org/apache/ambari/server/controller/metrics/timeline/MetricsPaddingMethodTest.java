package org.apache.ambari.server.controller.metrics.timeline;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
public class MetricsPaddingMethodTest {
    @org.junit.Test
    public void testPaddingWithNulls() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.MetricsPaddingMethod paddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.NULLS);
        long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("m1");
        timelineMetric.setHostName("h1");
        timelineMetric.setAppId("a1");
        timelineMetric.setStartTime(now);
        java.util.TreeMap<java.lang.Long, java.lang.Double> inputValues = new java.util.TreeMap<>();
        inputValues.put(now - 1000, 1.0);
        inputValues.put(now - 2000, 2.0);
        inputValues.put(now - 3000, 3.0);
        timelineMetric.setMetricValues(inputValues);
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = getTemporalInfo(now - 10000, now, 1L);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        java.util.TreeMap<java.lang.Long, java.lang.Double> values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(11, values.size());
        junit.framework.Assert.assertEquals(new java.lang.Long(now - 10000), values.keySet().iterator().next());
        junit.framework.Assert.assertEquals(new java.lang.Long(now), values.descendingKeySet().iterator().next());
        junit.framework.Assert.assertEquals(null, values.values().iterator().next());
    }

    @org.junit.Test
    public void testPaddingWithZeros() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.MetricsPaddingMethod paddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.ZEROS);
        long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("m1");
        timelineMetric.setHostName("h1");
        timelineMetric.setAppId("a1");
        timelineMetric.setStartTime(now);
        java.util.TreeMap<java.lang.Long, java.lang.Double> inputValues = new java.util.TreeMap<>();
        inputValues.put(now - 1000, 1.0);
        inputValues.put(now - 2000, 2.0);
        inputValues.put(now - 3000, 3.0);
        timelineMetric.setMetricValues(inputValues);
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = getTemporalInfo(now - 10000, now, 1L);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        java.util.TreeMap<java.lang.Long, java.lang.Double> values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(11, values.size());
        junit.framework.Assert.assertEquals(new java.lang.Long(now - 10000), values.keySet().iterator().next());
        junit.framework.Assert.assertEquals(new java.lang.Long(now), values.descendingKeySet().iterator().next());
        junit.framework.Assert.assertEquals(0.0, values.values().iterator().next());
    }

    @org.junit.Test
    public void testPaddingWithNoPaddingNeeded() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.MetricsPaddingMethod paddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.ZEROS);
        long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("m1");
        timelineMetric.setHostName("h1");
        timelineMetric.setAppId("a1");
        timelineMetric.setStartTime(now);
        java.util.TreeMap<java.lang.Long, java.lang.Double> inputValues = new java.util.TreeMap<>();
        inputValues.put(now, 0.0);
        inputValues.put(now - 1000, 1.0);
        inputValues.put(now - 2000, 2.0);
        inputValues.put(now - 3000, 3.0);
        timelineMetric.setMetricValues(inputValues);
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = getTemporalInfo(now - 3000, now, 1L);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        java.util.TreeMap<java.lang.Long, java.lang.Double> values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(4, values.size());
        junit.framework.Assert.assertEquals(new java.lang.Long(now - 3000), values.keySet().iterator().next());
        junit.framework.Assert.assertEquals(new java.lang.Long(now), values.descendingKeySet().iterator().next());
    }

    @org.junit.Test
    public void testPaddingWithStepProvided() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.MetricsPaddingMethod paddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.ZEROS);
        long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("m1");
        timelineMetric.setHostName("h1");
        timelineMetric.setAppId("a1");
        timelineMetric.setStartTime(now);
        java.util.TreeMap<java.lang.Long, java.lang.Double> inputValues = new java.util.TreeMap<>();
        inputValues.put(now - 1000, 1.0);
        timelineMetric.setMetricValues(inputValues);
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = getTemporalInfo(now - 10000, now, 1000L);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        java.util.TreeMap<java.lang.Long, java.lang.Double> values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(11, values.size());
        junit.framework.Assert.assertEquals(new java.lang.Long(now - 10000), values.keySet().iterator().next());
        junit.framework.Assert.assertEquals(new java.lang.Long(now), values.descendingKeySet().iterator().next());
        junit.framework.Assert.assertEquals(0.0, values.values().iterator().next());
    }

    @org.junit.Test
    public void testPaddingWithOneValue() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.MetricsPaddingMethod paddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.ZEROS);
        long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("m1");
        timelineMetric.setHostName("h1");
        timelineMetric.setAppId("a1");
        timelineMetric.setStartTime(now);
        java.util.TreeMap<java.lang.Long, java.lang.Double> inputValues = new java.util.TreeMap<>();
        inputValues.put(now - 1000, 1.0);
        timelineMetric.setMetricValues(inputValues);
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = getTemporalInfo(now - 10000, now, null);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        java.util.TreeMap<java.lang.Long, java.lang.Double> values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(1, values.size());
        junit.framework.Assert.assertEquals(new java.lang.Long(now - 1000), values.keySet().iterator().next());
        junit.framework.Assert.assertEquals(1.0, values.values().iterator().next());
    }

    @org.junit.Test
    public void testPaddingWithWithVariousPrecisionData() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.MetricsPaddingMethod paddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.ZEROS);
        long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("m1");
        timelineMetric.setHostName("h1");
        timelineMetric.setAppId("a1");
        timelineMetric.setStartTime(now);
        java.util.TreeMap<java.lang.Long, java.lang.Double> inputValues = new java.util.TreeMap<>();
        long seconds = 1000;
        long minute = 60 * seconds;
        long hour = 60 * minute;
        long day = 24 * hour;
        inputValues.clear();
        for (int i = 5; i >= 1; i--) {
            inputValues.put(now - (i * minute), i + 0.0);
        }
        timelineMetric.setMetricValues(inputValues);
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = getTemporalInfo((now - (2 * hour)) - (1 * minute), now, null);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        java.util.TreeMap<java.lang.Long, java.lang.Double> values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(122, values.size());
        junit.framework.Assert.assertEquals(new java.lang.Long((now - (2 * hour)) - (1 * minute)), values.keySet().iterator().next());
        inputValues.clear();
        for (int i = 5; i >= 1; i--) {
            inputValues.put(now - (i * hour), i + 0.0);
        }
        timelineMetric.setMetricValues(inputValues);
        temporalInfo = getTemporalInfo((now - (1 * day)) - (1 * hour), now, null);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(26, values.size());
        junit.framework.Assert.assertEquals(new java.lang.Long((now - (1 * day)) - (1 * hour)), values.keySet().iterator().next());
        inputValues.clear();
        inputValues.put(now - day, 1.0);
        timelineMetric.setMetricValues(inputValues);
        temporalInfo = getTemporalInfo(now - (40 * day), now, null);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(41, values.size());
        junit.framework.Assert.assertEquals(new java.lang.Long(now - (40 * day)), values.keySet().iterator().next());
    }

    @org.junit.Test
    public void testNoPaddingRequested() throws java.lang.Exception {
        org.apache.ambari.server.controller.metrics.MetricsPaddingMethod paddingMethod = new org.apache.ambari.server.controller.metrics.MetricsPaddingMethod(org.apache.ambari.server.controller.metrics.MetricsPaddingMethod.PADDING_STRATEGY.NONE);
        long now = java.lang.System.currentTimeMillis();
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetric timelineMetric = new org.apache.hadoop.metrics2.sink.timeline.TimelineMetric();
        timelineMetric.setMetricName("m1");
        timelineMetric.setHostName("h1");
        timelineMetric.setAppId("a1");
        timelineMetric.setStartTime(now);
        java.util.TreeMap<java.lang.Long, java.lang.Double> inputValues = new java.util.TreeMap<>();
        inputValues.put(now - 100, 1.0);
        inputValues.put(now - 200, 2.0);
        inputValues.put(now - 300, 3.0);
        timelineMetric.setMetricValues(inputValues);
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo = getTemporalInfo(now - 1000, now, 10L);
        paddingMethod.applyPaddingStrategy(timelineMetric, temporalInfo);
        java.util.TreeMap<java.lang.Long, java.lang.Double> values = ((java.util.TreeMap<java.lang.Long, java.lang.Double>) (timelineMetric.getMetricValues()));
        junit.framework.Assert.assertEquals(3, values.size());
    }

    private org.apache.ambari.server.controller.spi.TemporalInfo getTemporalInfo(final java.lang.Long startTime, final java.lang.Long endTime, final java.lang.Long step) {
        return new org.apache.ambari.server.controller.spi.TemporalInfo() {
            @java.lang.Override
            public java.lang.Long getStartTime() {
                return startTime;
            }

            @java.lang.Override
            public java.lang.Long getEndTime() {
                return endTime;
            }

            @java.lang.Override
            public java.lang.Long getStep() {
                return step;
            }

            @java.lang.Override
            public java.lang.Long getStartTimeMillis() {
                return startTime;
            }

            @java.lang.Override
            public java.lang.Long getEndTimeMillis() {
                return endTime;
            }
        };
    }
}