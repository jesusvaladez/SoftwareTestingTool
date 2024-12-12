package org.apache.ambari.server.events.publishers;
@com.google.inject.Singleton
public final class AlertEventPublisher {
    private final com.google.common.eventbus.EventBus m_eventBus;

    @com.google.inject.Inject
    public AlertEventPublisher(org.apache.ambari.server.configuration.Configuration config) {
        int corePoolSize = config.getAlertEventPublisherCorePoolSize();
        int maxPoolSize = config.getAlertEventPublisherMaxPoolSize();
        int workerQueueSize = config.getAlertEventPublisherWorkerQueueSize();
        java.util.concurrent.ThreadPoolExecutor executor = new org.apache.ambari.server.controller.utilities.ScalingThreadPoolExecutor(corePoolSize, maxPoolSize, 0L, java.util.concurrent.TimeUnit.SECONDS, workerQueueSize);
        executor.allowCoreThreadTimeOut(false);
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy());
        executor.setThreadFactory(new org.apache.ambari.server.events.publishers.AlertEventPublisher.AlertEventBusThreadFactory());
        m_eventBus = new com.google.common.eventbus.AsyncEventBus(executor);
    }

    public void publish(org.apache.ambari.server.events.AlertEvent event) {
        m_eventBus.post(event);
    }

    public void register(java.lang.Object object) {
        m_eventBus.register(object);
    }

    private static final class AlertEventBusThreadFactory implements java.util.concurrent.ThreadFactory {
        private static final java.util.concurrent.atomic.AtomicInteger s_threadIdPool = new java.util.concurrent.atomic.AtomicInteger(1);

        @java.lang.Override
        public java.lang.Thread newThread(java.lang.Runnable r) {
            java.lang.Thread thread = new java.lang.Thread(r, "alert-event-bus-" + org.apache.ambari.server.events.publishers.AlertEventPublisher.AlertEventBusThreadFactory.s_threadIdPool.getAndIncrement());
            thread.setDaemon(false);
            thread.setPriority(java.lang.Thread.NORM_PRIORITY - 1);
            return thread;
        }
    }
}