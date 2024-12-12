package org.apache.ambari.server.audit;
public interface AuditLogger {
    void log(final org.apache.ambari.server.audit.event.AuditEvent event);

    boolean isEnabled();
}