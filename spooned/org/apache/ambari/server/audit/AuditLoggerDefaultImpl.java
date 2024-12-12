package org.apache.ambari.server.audit;
@com.google.inject.Singleton
public class AuditLoggerDefaultImpl implements org.apache.ambari.server.audit.AuditLogger {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger("audit");

    private final boolean isEnabled;

    private java.lang.ThreadLocal<java.text.DateFormat> dateFormatThreadLocal = new java.lang.ThreadLocal<java.text.DateFormat>() {
        @java.lang.Override
        protected java.text.DateFormat initialValue() {
            return new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXX");
        }
    };

    @com.google.inject.Inject
    public AuditLoggerDefaultImpl(org.apache.ambari.server.configuration.Configuration configuration) {
        isEnabled = configuration.isAuditLogEnabled();
    }

    @java.lang.Override
    @com.google.common.eventbus.Subscribe
    public void log(org.apache.ambari.server.audit.event.AuditEvent event) {
        if (!isEnabled) {
            return;
        }
        java.util.Date date = new java.util.Date(event.getTimestamp());
        org.apache.ambari.server.audit.AuditLoggerDefaultImpl.LOG.info("{}, {}", dateFormatThreadLocal.get().format(date), event.getAuditMessage());
    }

    @java.lang.Override
    public boolean isEnabled() {
        return isEnabled;
    }
}