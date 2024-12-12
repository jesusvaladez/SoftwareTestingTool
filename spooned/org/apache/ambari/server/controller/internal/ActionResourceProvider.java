package org.apache.ambari.server.controller.internal;
public class ActionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ActionResourceProvider.class);

    public static final java.lang.String ACTION_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Actions", "action_name");

    public static final java.lang.String ACTION_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Actions", "action_type");

    public static final java.lang.String INPUTS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Actions", "inputs");

    public static final java.lang.String TARGET_SERVICE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Actions", "target_service");

    public static final java.lang.String TARGET_COMPONENT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Actions", "target_component");

    public static final java.lang.String DESCRIPTION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Actions", "description");

    public static final java.lang.String TARGET_HOST_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Actions", "target_type");

    public static final java.lang.String DEFAULT_TIMEOUT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Actions", "default_timeout");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Action, org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ActionResourceProvider.INPUTS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_SERVICE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_COMPONENT_PROPERTY_ID, org.apache.ambari.server.controller.internal.ActionResourceProvider.DESCRIPTION_PROPERTY_ID, org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_HOST_PROPERTY_ID, org.apache.ambari.server.controller.internal.ActionResourceProvider.DEFAULT_TIMEOUT_PROPERTY_ID);

    public ActionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Action, org.apache.ambari.server.controller.internal.ActionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ActionResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not currently supported.");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not currently supported.");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ActionRequest> requests = new java.util.HashSet<>();
        if (predicate != null) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                org.apache.ambari.server.controller.ActionRequest actionReq = getRequest(propertyMap);
                org.apache.ambari.server.controller.internal.ActionResourceProvider.LOG.debug("Received a get request for Action with, actionName = {}", actionReq.getActionName());
                requests.add(actionReq);
            }
        } else {
            org.apache.ambari.server.controller.internal.ActionResourceProvider.LOG.debug("Received a get request for all Actions");
            requests.add(org.apache.ambari.server.controller.ActionRequest.getAllRequest());
        }
        java.util.Set<org.apache.ambari.server.controller.ActionResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ActionResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ActionResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getActionDefinitions(requests);
            }
        });
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ActionResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Action);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_NAME_PROPERTY_ID, response.getActionName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_TYPE_PROPERTY_ID, response.getActionType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ActionResourceProvider.INPUTS_PROPERTY_ID, response.getInputs(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_SERVICE_PROPERTY_ID, response.getTargetService(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_COMPONENT_PROPERTY_ID, response.getTargetComponent(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ActionResourceProvider.DESCRIPTION_PROPERTY_ID, response.getDescription(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_HOST_PROPERTY_ID, response.getTargetType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ActionResourceProvider.DEFAULT_TIMEOUT_PROPERTY_ID, response.getDefaultTimeout(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not currently supported.");
    }

    private org.apache.ambari.server.controller.ActionRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        org.apache.ambari.server.controller.ActionRequest ar = new org.apache.ambari.server.controller.ActionRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ActionResourceProvider.ACTION_TYPE_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ActionResourceProvider.INPUTS_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_SERVICE_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_COMPONENT_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ActionResourceProvider.DESCRIPTION_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ActionResourceProvider.TARGET_HOST_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ActionResourceProvider.DEFAULT_TIMEOUT_PROPERTY_ID))));
        return ar;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ActionResourceProvider.keyPropertyIds.values());
    }

    private org.apache.ambari.server.actionmanager.ActionManager getActionManager() {
        return getManagementController().getActionManager();
    }

    private org.apache.ambari.server.api.services.AmbariMetaInfo getAmbariMetaInfo() {
        return getManagementController().getAmbariMetaInfo();
    }

    protected synchronized java.util.Set<org.apache.ambari.server.controller.ActionResponse> getActionDefinitions(java.util.Set<org.apache.ambari.server.controller.ActionRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.ActionResponse> responses = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ActionRequest request : requests) {
            if (request.getActionName() == null) {
                java.util.List<org.apache.ambari.server.customactions.ActionDefinition> ads = getAmbariMetaInfo().getAllActionDefinition();
                for (org.apache.ambari.server.customactions.ActionDefinition ad : ads) {
                    responses.add(ad.convertToResponse());
                }
            } else {
                org.apache.ambari.server.customactions.ActionDefinition ad = getAmbariMetaInfo().getActionDefinition(request.getActionName());
                if (ad != null) {
                    responses.add(ad.convertToResponse());
                }
            }
        }
        return responses;
    }
}