package org.apache.ambari.server.audit.request.eventcreator;
public class ViewPrivilegeEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.ViewPrivilege).build();

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
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> users = getEntities(request, org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME);
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> groups = getEntities(request, org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME);
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> roles = getEntities(request, org.apache.ambari.server.orm.entities.PrincipalTypeEntity.ROLE_PRINCIPAL_TYPE_NAME);
        return org.apache.ambari.server.audit.event.request.ViewPrivilegeChangeRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withType(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME)).withVersion(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION)).withName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME)).withUsers(users).withGroups(groups).withRoles(roles).build();
    }

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> getEntities(final org.apache.ambari.server.api.services.Request request, final java.lang.String type) {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> entities = new java.util.HashMap<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getBody().getPropertySets()) {
            java.lang.String ptype = java.lang.String.valueOf(propertyMap.get(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.PRINCIPAL_TYPE));
            if (type.equals(ptype)) {
                java.lang.String role = java.lang.String.valueOf(propertyMap.get(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.PERMISSION_NAME));
                java.lang.String name = java.lang.String.valueOf(propertyMap.get(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.PRINCIPAL_NAME));
                if (!entities.containsKey(role)) {
                    entities.put(role, new java.util.LinkedList<>());
                }
                entities.get(role).add(name);
            }
        }
        return entities;
    }
}