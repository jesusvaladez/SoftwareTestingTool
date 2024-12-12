package org.apache.ambari.server.agent.stomp;
public class AgentsRegistrationQueue {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.stomp.AgentsRegistrationQueue.class);

    private final java.util.concurrent.BlockingQueue<java.lang.String> registrationQueue;

    private final java.util.concurrent.ThreadFactory threadFactoryExecutor = new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("agents-queue-%d").build();

    private final java.util.concurrent.ScheduledExecutorService scheduledExecutorService = java.util.concurrent.Executors.newScheduledThreadPool(1, threadFactoryExecutor);

    public AgentsRegistrationQueue(com.google.inject.Injector injector) {
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        registrationQueue = new java.util.concurrent.LinkedBlockingQueue<>(configuration.getAgentsRegistrationQueueSize());
    }

    public boolean offer(java.lang.String sessionId) {
        boolean offered = registrationQueue.offer(sessionId);
        scheduledExecutorService.schedule(new org.apache.ambari.server.agent.stomp.AgentsRegistrationQueue.CompleteJob(sessionId, registrationQueue), 60, java.util.concurrent.TimeUnit.SECONDS);
        return offered;
    }

    public void complete(java.lang.String sessionId) {
        registrationQueue.remove(sessionId);
    }

    private class CompleteJob implements java.lang.Runnable {
        private java.lang.String sessionId;

        private java.util.concurrent.BlockingQueue<java.lang.String> registrationQueue;

        public CompleteJob(java.lang.String sessionId, java.util.concurrent.BlockingQueue<java.lang.String> registrationQueue) {
            this.sessionId = sessionId;
            this.registrationQueue = registrationQueue;
        }

        @java.lang.Override
        public void run() {
            registrationQueue.remove(sessionId);
        }
    }
}