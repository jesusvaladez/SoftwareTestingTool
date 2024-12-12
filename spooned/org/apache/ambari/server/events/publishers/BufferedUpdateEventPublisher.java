package org.apache.ambari.server.events.publishers;
public abstract class BufferedUpdateEventPublisher<T> {
    private static final long TIMEOUT = 1000L;

    private final java.util.concurrent.ConcurrentLinkedQueue<T> buffer = new java.util.concurrent.ConcurrentLinkedQueue<>();

    public abstract org.apache.ambari.server.events.STOMPEvent.Type getType();

    private java.util.concurrent.ScheduledExecutorService scheduledExecutorService;

    public BufferedUpdateEventPublisher(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher stompUpdatePublisher) {
        stompUpdatePublisher.registerPublisher(this);
    }

    public void publish(T event, com.google.common.eventbus.EventBus m_eventBus) {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = java.util.concurrent.Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleWithFixedDelay(getScheduledPublisher(m_eventBus), org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher.TIMEOUT, org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher.TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
        }
        buffer.add(event);
    }

    protected org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher<T>.MergingRunnable getScheduledPublisher(com.google.common.eventbus.EventBus m_eventBus) {
        return new MergingRunnable(m_eventBus);
    }

    protected java.util.List<T> retrieveBuffer() {
        java.util.List<T> bufferContent = new java.util.ArrayList<>();
        while (!buffer.isEmpty()) {
            bufferContent.add(buffer.poll());
        } 
        return bufferContent;
    }

    public abstract void mergeBufferAndPost(java.util.List<T> events, com.google.common.eventbus.EventBus m_eventBus);

    private class MergingRunnable implements java.lang.Runnable {
        private final com.google.common.eventbus.EventBus m_eventBus;

        public MergingRunnable(com.google.common.eventbus.EventBus m_eventBus) {
            this.m_eventBus = m_eventBus;
        }

        @java.lang.Override
        public final void run() {
            java.util.List<T> events = retrieveBuffer();
            if (events.isEmpty()) {
                return;
            }
            mergeBufferAndPost(events, m_eventBus);
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher<?> that = ((org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher<?>) (o));
        return java.util.Objects.equals(getType(), that.getType());
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(getType());
    }
}