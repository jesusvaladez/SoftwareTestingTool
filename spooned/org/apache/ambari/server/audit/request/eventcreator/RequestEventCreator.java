package org.apache.ambari.server.audit.request.eventcreator;
public class RequestEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.POST).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.Request).build();

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
                return org.apache.ambari.server.audit.event.request.AddRequestRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withCommand(request.getBody().getRequestInfoProperties().get("command")).withClusterName(getClusterName(request, org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID)).build();
            default :
                return null;
        }
    }

    private java.lang.String getClusterName(org.apache.ambari.server.api.services.Request request, java.lang.String propertyName) {
        java.util.Map<java.lang.String, java.lang.String> requestInfoProps = request.getBody().getRequestInfoProperties();
        return requestInfoProps.containsKey(propertyName) ? requestInfoProps.get(propertyName) : getProperty(request, org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID);
    }

    private java.lang.String getProperty(org.apache.ambari.server.api.services.Request request, java.lang.String propertyName) {
        if (!request.getBody().getPropertySets().isEmpty()) {
            return java.lang.String.valueOf(request.getBody().getPropertySets().iterator().next().get(propertyName));
        }
        return null;
    }
}