package org.apache.ambari.server.audit.request;
public abstract class RequestAuditEvent extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent {
    public static abstract class RequestAuditEventBuilder<T extends org.apache.ambari.server.audit.request.RequestAuditEvent, TBuilder extends org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<T, TBuilder>> extends org.apache.ambari.server.audit.event.AbstractUserAuditEvent.AbstractUserAuditEventBuilder<T, TBuilder> {
        private org.apache.ambari.server.api.services.Request.Type requestType;

        private org.apache.ambari.server.api.services.ResultStatus resultStatus;

        private java.lang.String url;

        private java.lang.String operation;

        protected RequestAuditEventBuilder(java.lang.Class<? extends TBuilder> builderClass) {
            super(builderClass);
        }

        @java.lang.Override
        protected void buildAuditMessage(java.lang.StringBuilder builder) {
            super.buildAuditMessage(builder);
            if (operation != null) {
                builder.append(", Operation(").append(operation).append(")");
            }
            builder.append(", RequestType(").append(requestType).append("), ").append("url(").append(url).append("), ResultStatus(").append(resultStatus.getStatusCode()).append(" ").append(resultStatus.getStatus()).append(")");
            if (resultStatus.isErrorState()) {
                builder.append(", Reason(").append(resultStatus.getMessage()).append(")");
            }
        }

        public TBuilder withRequestType(org.apache.ambari.server.api.services.Request.Type requestType) {
            this.requestType = requestType;
            return self();
        }

        public TBuilder withUrl(java.lang.String url) {
            this.url = url;
            return self();
        }

        public TBuilder withResultStatus(org.apache.ambari.server.api.services.ResultStatus resultStatus) {
            this.resultStatus = resultStatus;
            return self();
        }

        public TBuilder withOperation(java.lang.String operation) {
            this.operation = operation;
            return self();
        }
    }

    protected RequestAuditEvent() {
    }

    protected RequestAuditEvent(org.apache.ambari.server.audit.request.RequestAuditEvent.RequestAuditEventBuilder<?, ?> builder) {
        super(builder);
    }
}