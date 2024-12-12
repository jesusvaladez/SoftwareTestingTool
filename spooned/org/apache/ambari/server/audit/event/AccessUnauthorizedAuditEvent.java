package org.apache.ambari.server.audit.event;
import javax.annotation.concurrent.Immutable;
@javax.annotation.concurrent.Immutable
public class AccessUnauthorizedAuditEvent extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent {
    public static class AccessUnauthorizedAuditEventBuilder extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent, org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.AccessUnauthorizedAuditEventBuilder> {
        private java.lang.String httpMethodName;

        private java.lang.String resourcePath;

        private AccessUnauthorizedAuditEventBuilder() {
            super(org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.AccessUnauthorizedAuditEventBuilder.class);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            builder.append(", Operation(").append(httpMethodName).append("), ResourcePath(").append(resourcePath).append("), Status(Failed), Reason(Access not authorized)");
        }

        public org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.AccessUnauthorizedAuditEventBuilder withHttpMethodName(java.lang.String httpMethodName) {
            this.httpMethodName = httpMethodName;
            return this;
        }

        public org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.AccessUnauthorizedAuditEventBuilder withResourcePath(java.lang.String resourcePath) {
            this.resourcePath = resourcePath;
            return this;
        }

        @java.lang.Override
        protected org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent newAuditEvent() {
            return new org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent(this);
        }
    }

    private AccessUnauthorizedAuditEvent() {
    }

    private AccessUnauthorizedAuditEvent(org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.AccessUnauthorizedAuditEventBuilder builder) {
        super(builder);
    }

    public static org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.AccessUnauthorizedAuditEventBuilder builder() {
        return new org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.AccessUnauthorizedAuditEventBuilder();
    }
}