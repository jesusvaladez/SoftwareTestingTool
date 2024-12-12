package org.apache.ambari.server.audit.request.eventcreator;
public class ConfigurationChangeEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.Cluster).build();

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
        if (!request.getBody().getPropertySets().isEmpty()) {
            java.util.Map<java.lang.String, java.lang.Object> map = com.google.common.collect.Iterables.getFirst(request.getBody().getPropertySets(), null);
            if (((map != null) && (map.size() == 1)) && map.containsKey(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID)) {
                java.lang.String newName = java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID));
                java.lang.String oldName = request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
                return org.apache.ambari.server.audit.event.request.ClusterNameChangeRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withOldName(oldName).withNewName(newName).build();
            }
        }
        return org.apache.ambari.server.audit.event.request.ConfigurationChangeRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withVersionNote(getServiceConfigVersionNote(result)).withVersionNumber(getServiceConfigVersion(result)).build();
    }

    private java.lang.String getServiceConfigVersion(org.apache.ambari.server.api.services.Result result) {
        java.util.Map<java.lang.String, java.lang.Object> map = getServiceConfigMap(result);
        return map == null ? null : java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_PROPERTY_ID));
    }

    private java.lang.String getServiceConfigVersionNote(org.apache.ambari.server.api.services.Result result) {
        java.util.Map<java.lang.String, java.lang.Object> map = getServiceConfigMap(result);
        return map == null ? null : java.lang.String.valueOf(map.get(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID));
    }

    private java.util.Map<java.lang.String, java.lang.Object> getServiceConfigMap(org.apache.ambari.server.api.services.Result result) {
        if (result.getResultTree().getChild("resources") != null) {
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> first = com.google.common.collect.Iterables.getFirst(result.getResultTree().getChild("resources").getChildren(), null);
            if ((first != null) && (first.getObject() != null)) {
                return first.getObject().getPropertiesMap().get("");
            }
        }
        return null;
    }
}