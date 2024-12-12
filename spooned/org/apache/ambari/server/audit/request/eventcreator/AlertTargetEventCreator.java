package org.apache.ambari.server.audit.request.eventcreator;
public class AlertTargetEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget).build();

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
                return org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME)).withDescription(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION)).withAlertStates(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES)).withGroupIds(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS)).withNotificationType(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE)).withEmailFrom(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/") + org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.JAVAMAIL_FROM_PROPERTY)).withEmailRecipients(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/") + org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_RECIPIENTS)).build();
            case PUT :
                return org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME)).withDescription(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION)).withAlertStates(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES)).withGroupIds(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS)).withNotificationType(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE)).withEmailFrom(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/") + org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.JAVAMAIL_FROM_PROPERTY)).withEmailRecipients(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedPropertyList(request, (org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/") + org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_RECIPIENTS)).build();
            case DELETE :
                return org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withId(request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget)).build();
            default :
                return null;
        }
    }
}