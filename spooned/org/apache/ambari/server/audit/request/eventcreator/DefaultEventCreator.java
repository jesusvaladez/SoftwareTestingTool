package org.apache.ambari.server.audit.request.eventcreator;
public class DefaultEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().addAll(java.util.EnumSet.complementOf(java.util.EnumSet.of(org.apache.ambari.server.api.services.Request.Type.GET))).build();

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.services.Request.Type> getRequestTypes() {
        return requestTypes;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getResourceTypes() {
        return null;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.services.ResultStatus.STATUS> getResultStatuses() {
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.audit.event.AuditEvent createAuditEvent(final org.apache.ambari.server.api.services.Request request, final org.apache.ambari.server.api.services.Result result) {
        return org.apache.ambari.server.audit.event.request.DefaultRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRemoteIp(request.getRemoteAddress()).withRequestType(request.getRequestType()).withUrl(request.getURI()).withResultStatus(result.getStatus()).build();
    }
}