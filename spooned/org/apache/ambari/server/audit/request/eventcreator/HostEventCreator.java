package org.apache.ambari.server.audit.request.eventcreator;
public class HostEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.QUERY_POST, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.Host).build();

    private static final java.util.regex.Pattern HOSTNAME_PATTERN = java.util.regex.Pattern.compile((".*" + org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID) + "\\s*=\\s*([^&\\s]+).*");

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
            case DELETE :
                return org.apache.ambari.server.audit.event.request.DeleteHostRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withHostName(request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.Host)).build();
            case POST :
                return org.apache.ambari.server.audit.event.request.AddHostRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withHostName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getNamedProperty(request, org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID)).build();
            case QUERY_POST :
                return org.apache.ambari.server.audit.event.request.AddComponentToHostRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withHostName(getHostNameFromQuery(request)).withComponents(getHostComponents(request)).build();
            default :
                return null;
        }
    }

    private java.util.Set<java.lang.String> getHostComponents(org.apache.ambari.server.api.services.Request request) {
        java.util.Set<java.lang.String> components = new java.util.HashSet<>();
        org.apache.ambari.server.api.services.NamedPropertySet propertySet = com.google.common.collect.Iterables.getFirst(request.getBody().getNamedPropertySets(), null);
        if ((propertySet != null) && (propertySet.getProperties().get("host_components") instanceof java.util.Set)) {
            java.util.Set<java.util.Map<java.lang.String, java.lang.String>> set = ((java.util.Set<java.util.Map<java.lang.String, java.lang.String>>) (propertySet.getProperties().get("host_components")));
            if ((set != null) && (!set.isEmpty())) {
                for (java.util.Map<java.lang.String, java.lang.String> element : set) {
                    components.add(element.get(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME));
                }
            }
        }
        return components;
    }

    private java.lang.String getHostNameFromQuery(org.apache.ambari.server.api.services.Request request) {
        java.util.regex.Matcher matcher = org.apache.ambari.server.audit.request.eventcreator.HostEventCreator.HOSTNAME_PATTERN.matcher(request.getURI());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}