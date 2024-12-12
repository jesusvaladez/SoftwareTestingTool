package org.apache.ambari.server.audit.request.eventcreator;
public class ServiceEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.Service).build();

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
        if (request.getRequestType() == org.apache.ambari.server.api.services.Request.Type.DELETE) {
            return org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withService(request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.Service)).build();
        }
        java.lang.String operation = getOperation(request);
        java.lang.Long requestId = null;
        if (containsRequestId(result)) {
            requestId = getRequestId(result);
        }
        org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder auditEventBuilder = org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.builder().withOperation(operation).withRemoteIp(request.getRemoteAddress()).withTimestamp(java.lang.System.currentTimeMillis()).withRequestId(java.lang.String.valueOf(requestId));
        if (result.getStatus().isErrorState()) {
            auditEventBuilder.withReasonOfFailure(result.getStatus().getMessage());
        }
        return auditEventBuilder.build();
    }

    private java.lang.String getOperation(org.apache.ambari.server.api.services.Request request) {
        if ((request.getBody().getRequestInfoProperties() != null) && request.getBody().getRequestInfoProperties().containsKey(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID)) {
            java.lang.String operation = "";
            if ("CLUSTER".equals(request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID))) {
                for (java.util.Map<java.lang.String, java.lang.Object> map : request.getBody().getPropertySets()) {
                    if (map.containsKey(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID)) {
                        operation = (((java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID)) + ": all services") + " (") + request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID)) + ")";
                        break;
                    }
                }
            }
            if ("SERVICE".equals(request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID))) {
                for (java.util.Map<java.lang.String, java.lang.Object> map : request.getBody().getPropertySets()) {
                    if (map.containsKey(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID)) {
                        operation = ((((java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID)) + ": ") + map.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID)) + " (") + request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID)) + ")";
                        break;
                    }
                }
            }
            return operation;
        }
        for (java.util.Map<java.lang.String, java.lang.Object> map : request.getBody().getPropertySets()) {
            if (map.containsKey(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_MAINTENANCE_STATE_PROPERTY_ID)) {
                return (("Turn " + map.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_MAINTENANCE_STATE_PROPERTY_ID)) + " Maintenance Mode for ") + map.get(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID);
            }
        }
        return null;
    }

    private java.lang.Long getRequestId(org.apache.ambari.server.api.services.Result result) {
        return ((java.lang.Long) (result.getResultTree().getChild("request").getObject().getPropertiesMap().get("Requests").get("id")));
    }

    private boolean containsRequestId(org.apache.ambari.server.api.services.Result result) {
        return (((result.getResultTree().getChild("request") != null) && (result.getResultTree().getChild("request").getObject() != null)) && (result.getResultTree().getChild("request").getObject().getPropertiesMap().get("Requests") != null)) && (result.getResultTree().getChild("request").getObject().getPropertiesMap().get("Requests").get("id") != null);
    }
}