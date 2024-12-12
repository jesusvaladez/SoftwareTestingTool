package org.apache.ambari.server.audit.event;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public abstract class AbstractAuditEvent implements org.apache.ambari.server.audit.event.AuditEvent {
    private final java.lang.Long timestamp;

    private final java.lang.String auditMessage;

    protected static abstract class AbstractAuditEventBuilder<T extends org.apache.ambari.server.audit.event.AbstractAuditEvent, TBuilder extends org.apache.ambari.server.audit.event.AbstractAuditEvent.AbstractAuditEventBuilder<T, TBuilder>> implements org.apache.ambari.server.audit.event.AuditEvent.AuditEventBuilder<T> {
        private java.lang.Long timestamp;

        private java.lang.String auditMessage;

        private final java.lang.Class<? extends TBuilder> builderClass;

        protected abstract T newAuditEvent();

        protected abstract void buildAuditMessage(java.lang.StringBuilder builder);

        protected AbstractAuditEventBuilder(java.lang.Class<? extends TBuilder> builderClass) {
            this.builderClass = builderClass;
        }

        public TBuilder withTimestamp(java.lang.Long timestamp) {
            this.timestamp = timestamp;
            return self();
        }

        @java.lang.Override
        public final T build() {
            final java.lang.StringBuilder auditMessageBuilder = new java.lang.StringBuilder();
            buildAuditMessage(auditMessageBuilder);
            auditMessage = auditMessageBuilder.toString();
            return newAuditEvent();
        }

        protected TBuilder self() {
            return builderClass.cast(this);
        }
    }

    protected AbstractAuditEvent() {
        this.timestamp = null;
        this.auditMessage = null;
    }

    protected AbstractAuditEvent(org.apache.ambari.server.audit.event.AbstractAuditEvent.AbstractAuditEventBuilder<?, ?> builder) {
        timestamp = builder.timestamp;
        auditMessage = builder.auditMessage;
    }

    @java.lang.Override
    public java.lang.Long getTimestamp() {
        return timestamp;
    }

    @java.lang.Override
    public java.lang.String getAuditMessage() {
        return auditMessage;
    }

    @java.lang.Override
    public final boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.server.audit.event.AbstractAuditEvent))
            return false;

        org.apache.ambari.server.audit.event.AbstractAuditEvent that = ((org.apache.ambari.server.audit.event.AbstractAuditEvent) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(getTimestamp(), that.getTimestamp()).append(getAuditMessage(), that.getAuditMessage()).isEquals();
    }

    @java.lang.Override
    public final int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(getTimestamp()).append(getAuditMessage()).toHashCode();
    }
}