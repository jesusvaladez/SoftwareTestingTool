package org.apache.ambari.server.audit.request.eventcreator;
public class UserEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.User).build();

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
                return org.apache.ambari.server.audit.event.request.CreateUserRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withCreatedUsername(getUsername(request)).withActive(isActive(request)).withAdmin(isAdmin(request)).build();
            case DELETE :
                return org.apache.ambari.server.audit.event.request.DeleteUserRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withDeletedUsername(request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.User)).build();
            case PUT :
                if (hasActive(request)) {
                    return org.apache.ambari.server.audit.event.request.ActivateUserRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withAffectedUsername(getUsername(request)).withActive(isActive(request)).build();
                }
                if (hasAdmin(request)) {
                    return org.apache.ambari.server.audit.event.request.AdminUserRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withAffectedUsername(getUsername(request)).withAdmin(isAdmin(request)).build();
                }
                if (hasOldPassword(request)) {
                    return org.apache.ambari.server.audit.event.request.UserPasswordChangeRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withAffectedUsername(getUsername(request)).build();
                }
                break;
            default :
                break;
        }
        return null;
    }

    private boolean isAdmin(org.apache.ambari.server.api.services.Request request) {
        return hasAdmin(request) && "true".equals(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID));
    }

    private boolean isActive(org.apache.ambari.server.api.services.Request request) {
        return hasActive(request) && "true".equals(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID));
    }

    private boolean hasAdmin(org.apache.ambari.server.api.services.Request request) {
        java.util.Map<java.lang.String, java.lang.Object> first = com.google.common.collect.Iterables.getFirst(request.getBody().getPropertySets(), null);
        return (first != null) && first.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID);
    }

    private boolean hasActive(org.apache.ambari.server.api.services.Request request) {
        java.util.Map<java.lang.String, java.lang.Object> first = com.google.common.collect.Iterables.getFirst(request.getBody().getPropertySets(), null);
        return (first != null) && first.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID);
    }

    private boolean hasOldPassword(org.apache.ambari.server.api.services.Request request) {
        java.util.Map<java.lang.String, java.lang.Object> first = com.google.common.collect.Iterables.getFirst(request.getBody().getPropertySets(), null);
        return (first != null) && first.containsKey(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_OLD_PASSWORD_PROPERTY_ID);
    }

    private java.lang.String getUsername(org.apache.ambari.server.api.services.Request request) {
        java.util.Map<java.lang.String, java.lang.Object> first = com.google.common.collect.Iterables.getFirst(request.getBody().getPropertySets(), null);
        if (first != null) {
            return java.lang.String.valueOf(first.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID));
        }
        return null;
    }
}