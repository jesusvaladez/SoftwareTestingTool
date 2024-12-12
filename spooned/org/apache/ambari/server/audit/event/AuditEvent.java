package org.apache.ambari.server.audit.event;
public interface AuditEvent {
    interface AuditEventBuilder<T extends org.apache.ambari.server.audit.event.AuditEvent> {
        T build();
    }

    java.lang.Long getTimestamp();

    java.lang.String getAuditMessage();
}