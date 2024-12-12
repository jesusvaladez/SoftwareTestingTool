package org.apache.ambari.server.audit.event;
import org.apache.commons.lang.StringUtils;
public abstract class AbstractUserAuditEvent extends org.apache.ambari.server.audit.event.AbstractAuditEvent {
    public static abstract class AbstractUserAuditEventBuilder<T extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent, TBuilder extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<T, TBuilder>> extends org.apache.ambari.server.audit.event.AbstractAuditEvent.AbstractAuditEventBuilder<T, TBuilder> {
        private java.lang.String userName = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName();

        private java.lang.String proxyUserName = org.apache.ambari.server.security.authorization.AuthorizationHelper.getProxyUserName();

        private java.lang.String remoteIp;

        protected AbstractUserAuditEventBuilder(java.lang.Class<? extends TBuilder> builderClass) {
            super(builderClass);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            builder.append("User(").append(this.userName).append("), RemoteIp(").append(this.remoteIp).append(")");
            if (org.apache.commons.lang.StringUtils.isNotEmpty(this.proxyUserName)) {
                builder.append(", ProxyUser(").append(this.proxyUserName).append(")");
            }
        }

        public TBuilder withUserName(java.lang.String userName) {
            this.userName = userName;
            return self();
        }

        public TBuilder withProxyUserName(java.lang.String proxyUserName) {
            this.proxyUserName = proxyUserName;
            return self();
        }

        public TBuilder withRemoteIp(java.lang.String ip) {
            this.remoteIp = ip;
            return self();
        }
    }

    protected AbstractUserAuditEvent() {
    }

    protected AbstractUserAuditEvent(org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<?, ?> builder) {
        super(builder);
    }
}