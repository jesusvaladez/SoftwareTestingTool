package org.apache.ambari.server.controller.internal;
public class RecommendationResourceProvider extends org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.class);

    protected static final java.lang.String RECOMMENDATION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Recommendation", "id");

    protected static final java.lang.String CLUSTER_ID_PROPERTY_ID = "clusterId";

    protected static final java.lang.String SERVICE_NAME_PROPERTY_ID = "serviceName";

    protected static final java.lang.String AUTO_COMPLETE_PROPERTY_ID = "autoComplete";

    protected static final java.lang.String CONFIGS_RESPONSE_PROPERTY_ID = "configsResponse";

    protected static final java.lang.String HOSTS_PROPERTY_ID = "hosts";

    protected static final java.lang.String SERVICES_PROPERTY_ID = "services";

    protected static final java.lang.String RECOMMEND_PROPERTY_ID = "recommend";

    protected static final java.lang.String RECOMMENDATIONS_PROPERTY_ID = "recommendations";

    protected static final java.lang.String CONFIG_GROUPS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("recommendations", "config-groups");

    protected static final java.lang.String BLUEPRINT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("recommendations", "blueprint");

    protected static final java.lang.String BLUEPRINT_CONFIGURATIONS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("recommendations/blueprint", "configurations");

    protected static final java.lang.String BLUEPRINT_HOST_GROUPS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("recommendations/blueprint", "host_groups");

    protected static final java.lang.String BLUEPRINT_HOST_GROUPS_NAME_PROPERTY_ID = "name";

    protected static final java.lang.String BLUEPRINT_HOST_GROUPS_COMPONENTS_PROPERTY_ID = "components";

    protected static final java.lang.String BINDING_HOST_GROUPS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("recommendations/blueprint_cluster_binding", "host_groups");

    protected static final java.lang.String BINDING_HOST_GROUPS_NAME_PROPERTY_ID = "name";

    protected static final java.lang.String BINDING_HOST_GROUPS_HOSTS_PROPERTY_ID = "hosts";

    protected static final java.lang.String CHANGED_CONFIGURATIONS_PROPERTY_ID = "changed_configurations";

    protected static final java.lang.String BINDING_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("recommendations", "blueprint_cluster_binding");

    protected static final java.lang.String USER_CONTEXT_PROPERTY_ID = "user_context";

    protected static final java.lang.String USER_CONTEXT_OPERATION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.USER_CONTEXT_PROPERTY_ID, "operation");

    protected static final java.lang.String USER_CONTEXT_OPERATION_DETAILS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.USER_CONTEXT_PROPERTY_ID, "operation_details");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Recommendation, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.RECOMMENDATION_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_VERSION_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.RECOMMENDATION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.RECOMMEND_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.CLUSTER_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.SERVICE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.AUTO_COMPLETE_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.CONFIGS_RESPONSE_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.HOSTS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.SERVICES_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.CONFIG_GROUPS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.CHANGED_CONFIGURATIONS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.USER_CONTEXT_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.USER_CONTEXT_OPERATION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.USER_CONTEXT_OPERATION_DETAILS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.RECOMMENDATIONS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_CONFIGURATIONS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_HOST_GROUPS_PROPERTY_ID, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_HOST_GROUPS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_HOST_GROUPS_NAME_PROPERTY_ID), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_HOST_GROUPS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_HOST_GROUPS_COMPONENTS_PROPERTY_ID), org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_PROPERTY_ID, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_NAME_PROPERTY_ID), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_HOSTS_PROPERTY_ID), org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_HOSTS_PROPERTY_ID);

    protected RecommendationResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Recommendation, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    protected java.lang.String getRequestTypePropertyId() {
        return org.apache.ambari.server.controller.internal.RecommendationResourceProvider.RECOMMEND_PROPERTY_ID;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest recommendationRequest = prepareStackAdvisorRequest(request);
        final org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response;
        try {
            response = org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.saHelper.recommend(recommendationRequest);
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequestException e) {
            org.apache.ambari.server.controller.internal.RecommendationResourceProvider.LOG.warn("Error occured during recommendation", e);
            throw new java.lang.IllegalArgumentException(e.getMessage(), e);
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException | org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.internal.RecommendationResourceProvider.LOG.warn("Error occured during recommendation", e);
            throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
        }
        org.apache.ambari.server.controller.spi.Resource recommendation = createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.spi.Resource>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.spi.Resource invoke() throws org.apache.ambari.server.AmbariException {
                org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Recommendation);
                if (!recommendationRequest.getConfigsResponse()) {
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.RECOMMENDATION_ID_PROPERTY_ID, response.getId(), getPropertyIds());
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_NAME_PROPERTY_ID, response.getVersion().getStackName(), getPropertyIds());
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_VERSION_PROPERTY_ID, response.getVersion().getStackVersion(), getPropertyIds());
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.HOSTS_PROPERTY_ID, response.getHosts(), getPropertyIds());
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.SERVICES_PROPERTY_ID, response.getServices(), getPropertyIds());
                }
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.CONFIG_GROUPS_PROPERTY_ID, response.getRecommendations().getConfigGroups(), getPropertyIds());
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_CONFIGURATIONS_PROPERTY_ID, response.getRecommendations().getBlueprint().getConfigurations(), getPropertyIds());
                if (!recommendationRequest.getConfigsResponse()) {
                    java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> hostGroups = response.getRecommendations().getBlueprint().getHostGroups();
                    java.util.List<java.util.Map<java.lang.String, java.lang.Object>> listGroupProps = new java.util.ArrayList<>();
                    for (org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup : hostGroups) {
                        java.util.Map<java.lang.String, java.lang.Object> mapGroupProps = new java.util.HashMap<>();
                        mapGroupProps.put(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_HOST_GROUPS_NAME_PROPERTY_ID, hostGroup.getName());
                        mapGroupProps.put(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_HOST_GROUPS_COMPONENTS_PROPERTY_ID, hostGroup.getComponents());
                        listGroupProps.add(mapGroupProps);
                    }
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BLUEPRINT_HOST_GROUPS_PROPERTY_ID, listGroupProps, getPropertyIds());
                    java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> bindingHostGroups = response.getRecommendations().getBlueprintClusterBinding().getHostGroups();
                    java.util.List<java.util.Map<java.lang.String, java.lang.Object>> listBindingGroupProps = new java.util.ArrayList<>();
                    for (org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup hostGroup : bindingHostGroups) {
                        java.util.Map<java.lang.String, java.lang.Object> mapGroupProps = new java.util.HashMap<>();
                        mapGroupProps.put(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_NAME_PROPERTY_ID, hostGroup.getName());
                        mapGroupProps.put(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_HOSTS_PROPERTY_ID, hostGroup.getHosts());
                        listBindingGroupProps.add(mapGroupProps);
                    }
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RecommendationResourceProvider.BINDING_HOST_GROUPS_PROPERTY_ID, listBindingGroupProps, getPropertyIds());
                }
                return resource;
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Recommendation, request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>(java.util.Arrays.asList(recommendation));
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null, resources);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.RecommendationResourceProvider.keyPropertyIds.values());
    }
}