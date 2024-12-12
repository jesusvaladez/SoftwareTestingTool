package org.apache.ambari.server.audit.request.eventcreator;
public class RepositoryVersionEventCreator implements org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator {
    private java.util.Set<org.apache.ambari.server.api.services.Request.Type> requestTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.api.services.Request.Type>builder().add(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.api.services.Request.Type.DELETE).build();

    private java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> resourceTypes = com.google.common.collect.ImmutableSet.<org.apache.ambari.server.controller.spi.Resource.Type>builder().add(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion).build();

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
                return org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withStackName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID)).withStackVersion(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID)).withDisplayName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID)).withRepoVersion(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID)).withRepos(getRepos(request)).build();
            case PUT :
                return org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withStackName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID)).withStackVersion(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID)).withDisplayName(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID)).withRepoVersion(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getProperty(request, org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID)).withRepos(getRepos(request)).build();
            case DELETE :
                return org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent.builder().withTimestamp(java.lang.System.currentTimeMillis()).withRequestType(request.getRequestType()).withResultStatus(result.getStatus()).withUrl(request.getURI()).withRemoteIp(request.getRemoteAddress()).withStackName(request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.Stack)).withStackVersion(request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion)).withRepoVersion(request.getResource().getKeyValueMap().get(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion)).build();
            default :
                return null;
        }
    }

    private java.util.SortedMap<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> getRepos(org.apache.ambari.server.api.services.Request request) {
        java.util.SortedMap<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> result = new java.util.TreeMap<>();
        java.util.Map<java.lang.String, java.lang.Object> first = com.google.common.collect.Iterables.getFirst(request.getBody().getPropertySets(), null);
        if ((first != null) && (first.get("operating_systems") instanceof java.util.Set)) {
            java.util.Set<?> set = ((java.util.Set<?>) (first.get("operating_systems")));
            result = createResultForOperationSystems(set);
        }
        return result;
    }

    private java.util.SortedMap<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> createResultForOperationSystems(java.util.Set<?> set) {
        java.util.SortedMap<java.lang.String, java.util.List<java.util.Map<java.lang.String, java.lang.String>>> result = new java.util.TreeMap<>();
        for (java.lang.Object entry : set) {
            if (entry instanceof java.util.Map) {
                java.util.Map<?, ?> map = ((java.util.Map<?, ?>) (entry));
                java.lang.String osType = ((java.lang.String) (map.get(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID)));
                if (!result.containsKey(osType)) {
                    result.put(osType, new java.util.LinkedList<>());
                }
                if (map.get("repositories") instanceof java.util.Set) {
                    java.util.Set<?> repos = ((java.util.Set<?>) (map.get("repositories")));
                    for (java.lang.Object repo : repos) {
                        if (repo instanceof java.util.Map) {
                            java.util.Map<java.lang.String, java.lang.String> resultMap = buildResultRepo(((java.util.Map<java.lang.String, java.lang.String>) (repo)));
                            result.get(osType).add(resultMap);
                        }
                    }
                }
            }
        }
        return result;
    }

    private java.util.Map<java.lang.String, java.lang.String> buildResultRepo(java.util.Map<java.lang.String, java.lang.String> repo) {
        java.util.Map<java.lang.String, java.lang.String> m = repo;
        java.lang.String repoId = m.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID);
        java.lang.String repo_name = m.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID);
        java.lang.String baseUrl = m.get(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID);
        java.util.Map<java.lang.String, java.lang.String> resultMap = new java.util.HashMap<>();
        resultMap.put("repo_id", repoId);
        resultMap.put("repo_name", repo_name);
        resultMap.put("base_url", baseUrl);
        return resultMap;
    }
}