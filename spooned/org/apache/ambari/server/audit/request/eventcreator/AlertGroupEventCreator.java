package org.apache.ambari.server.audit.request.eventcreator;
public class AlertGroupEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup).build();

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.services.Request.Type> getRequestTypes() {
        return requestTypes;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> getResourceTypes() {
        return resourceTypes;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.api.services.ResultStatus.STATUS> getResultStatuses() {
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.audit.event.AuditEvent createAuditEvent(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.services.Result result) {
        switch (request.getRequestType()) {
            case POST :
                return org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedProperty(request, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME)).withDefinitionIds(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS)).withNotificationIds(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS)).build();
            case PUT :
                return org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedProperty(request, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME)).withDefinitionIds(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS)).withNotificationIds(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS)).build();
            case DELETE :
                return org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withId(request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup)).build();
            default :
                return null;
        }
    }
}