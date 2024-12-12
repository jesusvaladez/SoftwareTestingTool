package org.apache.ambari.server.audit.request.eventcreator;
public class UnauthorizedEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.ResultStatus.STATUS> statuses = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.ResultStatus.STATUS>builder().add(org.apache.ambari.server.api.services.ResultStatus.STATUS.UNAUTHORIZED, org.apache.ambari.server.api.services.ResultStatus.STATUS.FORBIDDEN).build();

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.services.Request.Type> getRequestTypes() {
        return null;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getResourceTypes() {
        return null;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.services.ResultStatus.STATUS> getResultStatuses() {
        return statuses;
    }

    @java.lang.Override
    public org.apache.ambari.server.audit.event.AuditEvent createAuditEvent(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.services.Result result) {
        org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent ae = org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent.builder().withRemoteIp(request.getRemoteAddress()).withResourcePath(request.getURI()).withTimestamp(java.lang.System.currentTimeMillis()).build();
        return ae;
    }
}