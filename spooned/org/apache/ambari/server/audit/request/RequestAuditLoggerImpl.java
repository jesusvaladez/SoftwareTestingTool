package org.apache.ambari.server.audit.request;
@com.google.inject.Singleton
public class RequestAuditLoggerImpl implements org.apache.ambari.server.audit.request.RequestAuditLogger {
    private static final int REQUEST_TYPE_PRIORITY = 1;

    private static final int RESULT_STATUS_PRIORITY = 2;

    private static final int RESOURCE_TYPE_PRIORITY = 4;

    private java.util.Set<org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator> creators;

    private org.apache.ambari.server.audit.AuditLogger auditLogger;

    @com.google.inject.Inject
    public RequestAuditLoggerImpl(org.apache.ambari.server.audit.AuditLogger auditLogger, java.util.Set<org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator> creatorSet) {
        this.auditLogger = auditLogger;
        this.creators = creatorSet;
    }

    @java.lang.Override
    public void log(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.services.Result result) {
        if (!auditLogger.isEnabled()) {
            return;
        }
        org.apache.ambari.server.controller.spi.Resource.Type resourceType = request.getResource().getResourceDefinition().getType();
        org.apache.ambari.server.api.services.Request.Type requestType = request.getRequestType();
        org.apache.ambari.server.api.services.ResultStatus resultStatus = result.getStatus();
        org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator creator = selectCreator(resourceType, resultStatus, requestType);
        if (creator != null) {
            org.apache.ambari.server.audit.event.AuditEvent ae = creator.createAuditEvent(request, result);
            if (ae != null) {
                auditLogger.log(ae);
            }
        }
    }

    private org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator selectCreator(org.apache.ambari.server.controller.spi.Resource.Type resourceType, org.apache.ambari.server.api.services.ResultStatus resultStatus, org.apache.ambari.server.api.services.Request.Type requestType) {
        org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator selected = null;
        java.lang.Integer priority = -1;
        for (org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator creator : creators) {
            java.lang.Integer creatorPriority = getPriority(creator, resourceType, resultStatus, requestType);
            if ((creatorPriority != null) && (priority < creatorPriority)) {
                priority = creatorPriority;
                selected = creator;
            }
        }
        return selected;
    }

    private java.lang.Integer getPriority(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator creator, org.apache.ambari.server.controller.spi.Resource.Type resourceType, org.apache.ambari.server.api.services.ResultStatus resultStatus, org.apache.ambari.server.api.services.Request.Type requestType) {
        java.lang.Integer priority = 0;
        if (isIncompatible(creator, resourceType, resultStatus, requestType)) {
            return null;
        }
        priority += ((creator.getRequestTypes() != null) && creator.getRequestTypes().contains(requestType)) ? org.apache.ambari.server.audit.request.RequestAuditLoggerImpl.REQUEST_TYPE_PRIORITY : 0;
        priority += ((creator.getResultStatuses() != null) && creator.getResultStatuses().contains(resultStatus.getStatus())) ? org.apache.ambari.server.audit.request.RequestAuditLoggerImpl.RESULT_STATUS_PRIORITY : 0;
        priority += ((creator.getResourceTypes() != null) && creator.getResourceTypes().contains(resourceType)) ? org.apache.ambari.server.audit.request.RequestAuditLoggerImpl.RESOURCE_TYPE_PRIORITY : 0;
        return priority;
    }

    private boolean isIncompatible(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator creator, org.apache.ambari.server.controller.spi.Resource.Type resourceType, org.apache.ambari.server.api.services.ResultStatus resultStatus, org.apache.ambari.server.api.services.Request.Type requestType) {
        return (((creator.getRequestTypes() != null) && (!creator.getRequestTypes().contains(requestType))) || ((creator.getResultStatuses() != null) && (!creator.getResultStatuses().contains(resultStatus.getStatus())))) || ((creator.getResourceTypes() != null) && (!creator.getResourceTypes().contains(resourceType)));
    }
}