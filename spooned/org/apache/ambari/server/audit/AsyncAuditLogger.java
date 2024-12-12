package org.apache.ambari.server.audit;
@com.google.inject.Singleton
class AsyncAuditLogger implements org.apache.ambari.server.audit.AuditLogger {
    static final java.lang.String InnerLogger = "AsyncAuditLogger";

    private com.google.common.eventbus.EventBus eventBus;

    private final boolean isEnabled;

    @com.google.inject.Inject
    public AsyncAuditLogger(@com.google.inject.name.Named(org.apache.ambari.server.audit.AsyncAuditLogger.InnerLogger)
    org.apache.ambari.server.audit.AuditLogger auditLogger, org.apache.ambari.server.configuration.Configuration configuration) {
        isEnabled = configuration.isAuditLogEnabled();
        if (isEnabled) {
            eventBus = new com.google.common.eventbus.AsyncEventBus("AuditLoggerEventBus", new java.util.concurrent.ThreadPoolExecutor(0, 1, 5L, java.util.concurrent.TimeUnit.MINUTES, new java.util.concurrent.LinkedBlockingQueue<>(configuration.getAuditLoggerCapacity()), new org.apache.ambari.server.audit.AsyncAuditLogger.AuditLogThreadFactory(), new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()));
            eventBus.register(auditLogger);
        }
    }

    @java.lang.Override
    public void log(org.apache.ambari.server.audit.event.AuditEvent event) {
        if (isEnabled) {
            eventBus.post(event);
        }
    }

    @java.lang.Override
    public boolean isEnabled() {
        return isEnabled;
    }

    private static final class AuditLogThreadFactory implements java.util.concurrent.ThreadFactory {
        private static final java.util.concurrent.atomic.AtomicInteger nextId = new java.util.concurrent.atomic.AtomicInteger(1);

        @java.lang.Override
        public java.lang.Thread newThread(java.lang.Runnable runnable) {
            java.lang.Thread thread = new java.lang.Thread(runnable, "auditlog-" + org.apache.ambari.server.audit.AsyncAuditLogger.AuditLogThreadFactory.nextId.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }
}