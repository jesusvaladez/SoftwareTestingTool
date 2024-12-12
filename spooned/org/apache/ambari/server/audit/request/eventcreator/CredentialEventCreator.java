package org.apache.ambari.server.audit.request.eventcreator;
public class CredentialEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.POST).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.Credential).build();

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
        return org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withClusterName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID)).withType(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID)).withAlias(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID)).withPrincipal(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID)).build();
    }
}