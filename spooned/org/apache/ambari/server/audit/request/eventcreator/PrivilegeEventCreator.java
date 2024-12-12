package org.apache.ambari.server.audit.request.eventcreator;
public class PrivilegeEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege).build();

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
        switch (request.getRequestType()) {
            case PUT :
                return org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withUsers(users).withGroups(groups).withRoles(roles).build();
            case POST :
                java.lang.String role = (users.isEmpty()) ? com.google.common.collect.Iterables.getFirst(groups.keySet(), null) : com.google.common.collect.Iterables.getFirst(users.keySet(), null);
                return org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withRole(role).withGroup(groups.get(role) == null ? null : groups.get(role).get(0)).withUser(users.get(role) == null ? null : users.get(role).get(0)).withOperation((users.isEmpty() ? groups.isEmpty() ? "" : "Group " : "User ") + "role change").build();
            default :
                return null;
        }
    }

    private java.util.Map<java.lang.String, java.util.List<java.lang.String>> getEntities(final org.apache.ambari.server.api.services.Request request, final java.lang.String type) {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> entities = new java.util.HashMap<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getBody().getPropertySets()) {
            java.lang.String ptype = java.lang.String.valueOf(propertyMap.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE));
            if (type.equals(ptype)) {
                java.lang.String role = java.lang.String.valueOf(propertyMap.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME));
                java.lang.String name = java.lang.String.valueOf(propertyMap.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME));
                if (!entities.containsKey(role)) {
                    entities.put(role, new java.util.LinkedList<>());
                }
                entities.get(role).add(name);
            }
        }
        return entities;
    }
}