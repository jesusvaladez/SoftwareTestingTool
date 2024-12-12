package org.apache.ambari.server.audit.request.eventcreator;
public class ComponentEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent).build();

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
        java.lang.String operation = getOperation(request);
        java.lang.Long requestId = null;
        if (containsRequestId(result)) {
            requestId = getRequestId(result);
        }
        org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.StartOperationAuditEventBuilder auditEventBuilder = org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent.builder().withOperation(operation).withRemoteIp(request.getRemoteAddress()).withTimestamp(java.lang.System.currentTimeMillis()).withHostname(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME)).withRequestId(java.lang.String.valueOf(requestId));
        if (result.getStatus().isErrorState()) {
            auditEventBuilder.withReasonOfFailure(result.getStatus().getMessage());
        }
        return auditEventBuilder.build();
    }

    private java.lang.String getOperation(org.apache.ambari.server.api.services.Request request) {
        if (request.getRequestType() == org.apache.ambari.server.api.services.Request.Type.DELETE) {
            return "Delete component " + request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent);
        }
        if ((request.getBody().getRequestInfoProperties() != null) && request.getBody().getRequestInfoProperties().containsKey(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID)) {
            java.lang.String operation = "";
            switch (request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID)) {
                case "CLUSTER" :
                    for (java.util.Map<java.lang.String, java.lang.Object> map : request.getBody().getPropertySets()) {
                        if (map.containsKey(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME)) {
                            operation = (((((java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE)) + ": all services") + " on all hosts") + ((request.getBody().getQueryString() != null) && (request.getBody().getQueryString().length() > 0) ? " that matches " + request.getBody().getQueryString() : "")) + " (") + request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID)) + ")";
                            break;
                        }
                    }
                    break;
                case "HOST" :
                    for (java.util.Map<java.lang.String, java.lang.Object> map : request.getBody().getPropertySets()) {
                        if (map.containsKey(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME)) {
                            java.lang.String query = request.getBody().getRequestInfoProperties().get("query");
                            operation = ((((((java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE)) + ": ") + query.substring(query.indexOf("(") + 1, query.length() - 1)) + " on ") + request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOST_NAME)) + " (") + request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID)) + ")";
                            break;
                        }
                    }
                    break;
                case "HOST_COMPONENT" :
                    for (java.util.Map<java.lang.String, java.lang.Object> map : request.getBody().getPropertySets()) {
                        if (map.containsKey(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME)) {
                            operation = ((((((((java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE)) + ": ") + java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME))) + "/") + request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_SERVICE_ID)) + " on ") + request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOST_NAME)) + " (") + request.getBody().getRequestInfoProperties().get(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID)) + ")";
                            break;
                        }
                    }
                    break;
            }
            return operation;
        }
        for (java.util.Map<java.lang.String, java.lang.Object> map : request.getBody().getPropertySets()) {
            if (map.containsKey(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE)) {
                return (("Turn " + map.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE)) + " Maintenance Mode for ") + map.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME);
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