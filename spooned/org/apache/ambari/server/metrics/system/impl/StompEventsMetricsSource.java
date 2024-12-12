package org.apache.ambari.server.metrics.system.impl;
public class StompEventsMetricsSource extends org.apache.ambari.server.metrics.system.impl.AbstractMetricsSource {
    private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metrics.system.impl.StompEventsMetricsSource.class);

    private java.util.Map<org.apache.ambari.server.events.STOMPEvent.Type, java.lang.Long> events = new java.util.HashMap<>();

    private java.util.concurrent.ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(1);

    private final java.lang.String EVENTS_TOTAL_METRIC = "events.total";

    private final java.lang.String AVERAGE_METRIC_SUFFIX = ".avg";

    private int interval = 60;

    @java.lang.Override
    public void init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration, org.apache.ambari.server.metrics.system.MetricsSink sink) {
        super.init(configuration, sink);
        for (org.apache.ambari.server.events.STOMPEvent.Type type : org.apache.ambari.server.events.STOMPEvent.Type.values()) {
            events.put(type, 0L);
        }
    }

    @java.lang.Override
    public void start() {
        org.apache.ambari.server.metrics.system.impl.StompEventsMetricsSource.LOG.info("Starting stomp events source...");
        try {
            executor.scheduleWithFixedDelay(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> events = getEvents();
                    sink.publish(events);
                    org.apache.ambari.server.metrics.system.impl.StompEventsMetricsSource.LOG.debug("********* Published stomp events metrics to sink **********");
                }
            }, interval, interval, java.util.concurrent.TimeUnit.SECONDS);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.metrics.system.impl.StompEventsMetricsSource.LOG.info("Throwing exception when starting stomp events source", e);
        }
    }

    private java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> getEvents() {
        java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> metrics = new java.util.ArrayList<>();
        java.lang.Long totalEventsCounter = 0L;
        synchronized(events) {
            for (java.util.Map.Entry<org.apache.ambari.server.events.STOMPEvent.Type, java.lang.Long> event : events.entrySet()) {
                totalEventsCounter += event.getValue();
                metrics.add(new org.apache.ambari.server.metrics.system.SingleMetric(event.getKey().getMetricName(), event.getValue(), java.lang.System.currentTimeMillis()));
                java.lang.String averageMetricName = event.getKey().getMetricName() + AVERAGE_METRIC_SUFFIX;
                java.lang.Double eventsPerSecond = (event.getValue() == 0) ? -1 : ((double) (interval)) / ((double) (event.getValue()));
                metrics.add(new org.apache.ambari.server.metrics.system.SingleMetric(averageMetricName, eventsPerSecond, java.lang.System.currentTimeMillis()));
                events.put(event.getKey(), 0L);
            }
            metrics.add(new org.apache.ambari.server.metrics.system.SingleMetric(EVENTS_TOTAL_METRIC, totalEventsCounter, java.lang.System.currentTimeMillis()));
            java.lang.String totalAverageMetricName = EVENTS_TOTAL_METRIC + AVERAGE_METRIC_SUFFIX;
            java.lang.Double eventsPerSecond = (totalEventsCounter == 0) ? -1 : ((double) (interval)) / ((double) (totalEventsCounter));
            metrics.add(new org.apache.ambari.server.metrics.system.SingleMetric(totalAverageMetricName, eventsPerSecond, java.lang.System.currentTimeMillis()));
        }
        return metrics;
    }

    @com.google.common.eventbus.Subscribe
    public void onUpdateEvent(org.apache.ambari.server.events.STOMPEvent STOMPEvent) {
        org.apache.ambari.server.events.STOMPEvent.Type metricType = STOMPEvent.getType();
        events.put(metricType, events.get(metricType) + 1);
    }
}