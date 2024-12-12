package org.apache.ambari.server.audit.request.eventcreator;
public class MemberEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.Member).build();

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
                return org.apache.ambari.server.audit.event.request.AddUserToGroupRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withAffectedUserName(getUserName(request)).withGroupName(getGroupName(request)).build();
            case DELETE :
                return org.apache.ambari.server.audit.event.request.RemoveUserFromGroupRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withAffectedUserName(getUserName(request)).withGroupName(getGroupName(request)).build();
            case PUT :
                return org.apache.ambari.server.audit.event.request.MembershipChangeRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withGroupName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_GROUP_NAME_PROPERTY_ID)).withUserNameList(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getPropertyList(request, org.apache.ambari.server.controller.internal.MemberResourceProvider.MEMBER_USER_NAME_PROPERTY_ID)).build();
            default :
                return null;
        }
    }

    private java.lang.String getUserName(org.apache.ambari.server.api.services.Request request) {
        return request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.Member);
    }

    private java.lang.String getGroupName(org.apache.ambari.server.api.services.Request request) {
        return request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.Group);
    }
}