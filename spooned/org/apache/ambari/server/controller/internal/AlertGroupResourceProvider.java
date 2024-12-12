package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class AlertGroupResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.class);

    public static final java.lang.String ALERT_GROUP = "AlertGroup";

    public static final java.lang.String ID_PROPERTY_ID = "id";

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String NAME_PROPERTY_ID = "name";

    public static final java.lang.String DEFAULT_PROPERTY_ID = "default";

    public static final java.lang.String DEFINITIONS_PROPERTY_ID = "definitions";

    public static final java.lang.String TARGETS_PROPERTY_ID = "targets";

    public static final java.lang.String ALERT_GROUP_ID = (org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ID_PROPERTY_ID;

    public static final java.lang.String ALERT_GROUP_CLUSTER_NAME = (org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.CLUSTER_NAME_PROPERTY_ID;

    public static final java.lang.String ALERT_GROUP_NAME = (org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.NAME_PROPERTY_ID;

    public static final java.lang.String ALERT_GROUP_DEFAULT = (org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.DEFAULT_PROPERTY_ID;

    public static final java.lang.String ALERT_GROUP_DEFINITIONS = (org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.DEFINITIONS_PROPERTY_ID;

    public static final java.lang.String ALERT_GROUP_TARGETS = (org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.TARGETS_PROPERTY_ID;

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = new java.util.HashSet<>(java.util.Arrays.asList(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME));

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(ALERT_GROUP_ID);
        PROPERTY_IDS.add(ALERT_GROUP_CLUSTER_NAME);
        PROPERTY_IDS.add(ALERT_GROUP_NAME);
        PROPERTY_IDS.add(ALERT_GROUP_DEFAULT);
        PROPERTY_IDS.add(ALERT_GROUP_DEFINITIONS);
        PROPERTY_IDS.add(ALERT_GROUP_TARGETS);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, ALERT_GROUP_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, ALERT_GROUP_CLUSTER_NAME);
    }

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertDispatchDAO s_dao;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.AlertDefinitionDAO s_definitionDao;

    @com.google.inject.Inject
    private static org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    AlertGroupResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                createAlertGroups(request.getProperties());
                return null;
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> results = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestPropertyIds = getRequestPropertyIds(request, predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME)));
            if ((null == clusterName) || clusterName.isEmpty()) {
                throw new java.lang.IllegalArgumentException("The cluster name is required when retrieving alert groups");
            }
            java.lang.String id = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID)));
            if (null != id) {
                org.apache.ambari.server.orm.entities.AlertGroupEntity entity = org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.findGroupById(java.lang.Long.parseLong(id));
                if (null != entity) {
                    try {
                        org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyViewAuthorization(entity, getClusterResourceId(entity.getClusterId()));
                    } catch (org.apache.ambari.server.AmbariException e) {
                        throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
                    }
                    results.add(toResource(clusterName, entity, requestPropertyIds));
                }
            } else {
                org.apache.ambari.server.state.Cluster cluster = null;
                try {
                    cluster = getManagementController().getClusters().getCluster(clusterName);
                } catch (org.apache.ambari.server.AmbariException ae) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("Parent Cluster resource doesn't exist", ae);
                }
                java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> entities = org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.findAllGroups(cluster.getClusterId());
                for (org.apache.ambari.server.orm.entities.AlertGroupEntity entity : entities) {
                    try {
                        if (org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.hasViewAuthorization(entity, getClusterResourceId(entity.getClusterId()))) {
                            results.add(toResource(clusterName, entity, requestPropertyIds));
                        }
                    } catch (org.apache.ambari.server.AmbariException e) {
                        throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
                    }
                }
            }
        }
        return results;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                updateAlertGroups(request.getProperties());
                return null;
            }
        });
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.AlertGroupEntity> entities = new java.util.HashMap<>();
        for (final org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.Long id = ((java.lang.Long) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID)));
            if (!entities.containsKey(id)) {
                org.apache.ambari.server.orm.entities.AlertGroupEntity entity = org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.findGroupById(id);
                try {
                    org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyManageAuthorization(entity, getClusterResourceId(entity.getClusterId()));
                    entities.put(id, entity);
                } catch (org.apache.ambari.server.AmbariException e) {
                    org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.LOG.warn("The default alert group for {} cannot be removed", entity.getServiceName(), e);
                }
            }
        }
        for (final org.apache.ambari.server.orm.entities.AlertGroupEntity entity : entities.values()) {
            org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.LOG.info("Deleting alert group {}", entity.getGroupId());
            if (entity.isDefault()) {
                org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.LOG.warn("The default alert group for {} cannot be removed", entity.getServiceName());
                continue;
            }
            modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                    org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.remove(entity);
                    return null;
                }
            });
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.PK_PROPERTY_IDS;
    }

    @java.lang.SuppressWarnings("unchecked")
    private void createAlertGroups(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.List<org.apache.ambari.server.orm.entities.AlertGroupEntity> entities = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> requestMap : requestMaps) {
            org.apache.ambari.server.orm.entities.AlertGroupEntity entity = new org.apache.ambari.server.orm.entities.AlertGroupEntity();
            java.lang.String name = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME)));
            java.lang.String clusterName = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME)));
            if (org.apache.commons.lang.StringUtils.isEmpty(name)) {
                throw new java.lang.IllegalArgumentException("The name of the alert group is required.");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(clusterName)) {
                throw new java.lang.IllegalArgumentException("The name of the cluster is required when creating an alert group.");
            }
            org.apache.ambari.server.state.Cluster cluster = getManagementController().getClusters().getCluster(clusterName);
            entity.setClusterId(cluster.getClusterId());
            entity.setGroupName(name);
            entity.setDefault(false);
            if (requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS)) {
                java.util.List<java.lang.Long> targetIds = ((java.util.List<java.lang.Long>) (requestMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS)));
                java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
                targets.addAll(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.findTargetsById(targetIds));
                entity.setAlertTargets(targets);
            }
            if (requestMap.containsKey(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS)) {
                java.util.List<java.lang.Long> definitionIds = ((java.util.List<java.lang.Long>) (requestMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS)));
                java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = new java.util.HashSet<>();
                definitions.addAll(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_definitionDao.findByIds(definitionIds));
                entity.setAlertDefinitions(definitions);
            }
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyManageAuthorization(entity, cluster.getResourceId());
            entities.add(entity);
        }
        org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.createGroups(entities);
    }

    @java.lang.SuppressWarnings("unchecked")
    private void updateAlertGroups(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestMaps) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        for (java.util.Map<java.lang.String, java.lang.Object> requestMap : requestMaps) {
            java.lang.String stringId = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID)));
            if (org.apache.commons.lang.StringUtils.isEmpty(stringId)) {
                throw new java.lang.IllegalArgumentException("The ID of the alert group is required when updating an existing group");
            }
            long id = java.lang.Long.parseLong(stringId);
            org.apache.ambari.server.orm.entities.AlertGroupEntity entity = org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.findGroupById(id);
            if (null == entity) {
                java.lang.String message = java.text.MessageFormat.format("The alert group with ID {0} could not be found", id);
                throw new org.apache.ambari.server.AmbariException(message);
            }
            org.apache.ambari.server.controller.internal.AlertResourceProviderUtils.verifyManageAuthorization(entity, getClusterResourceId(entity.getClusterId()));
            java.lang.String name = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME)));
            java.util.Collection<java.lang.Long> targetIds = ((java.util.Collection<java.lang.Long>) (requestMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS)));
            java.util.Collection<java.lang.Long> definitionIds = ((java.util.Collection<java.lang.Long>) (requestMap.get(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS)));
            if (null != targetIds) {
                java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targets = new java.util.HashSet<>();
                java.util.List<java.lang.Long> ids = new java.util.ArrayList<>(targetIds.size());
                ids.addAll(targetIds);
                if (ids.size() > 0) {
                    targets.addAll(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.findTargetsById(ids));
                }
                entity.setAlertTargets(targets);
            }
            if (!entity.isDefault()) {
                if (!org.apache.commons.lang.StringUtils.isBlank(name)) {
                    entity.setGroupName(name);
                }
                java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = new java.util.HashSet<>();
                if ((null != definitionIds) && (definitionIds.size() > 0)) {
                    java.util.List<java.lang.Long> ids = new java.util.ArrayList<>(definitionIds.size());
                    ids.addAll(definitionIds);
                    definitions.addAll(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_definitionDao.findByIds(ids));
                    entity.setAlertDefinitions(definitions);
                } else {
                    entity.setAlertDefinitions(definitions);
                }
            }
            org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.s_dao.merge(entity);
            org.apache.ambari.server.events.AlertGroupsUpdateEvent alertGroupsUpdateEvent = new org.apache.ambari.server.events.AlertGroupsUpdateEvent(java.util.Collections.singletonList(new org.apache.ambari.server.agent.stomp.dto.AlertGroupUpdate(entity)), org.apache.ambari.server.events.UpdateEventType.UPDATE);
            org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.STOMPUpdatePublisher.publish(alertGroupsUpdateEvent);
        }
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(java.lang.String clusterName, org.apache.ambari.server.orm.entities.AlertGroupEntity entity, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup);
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_ID, entity.getGroupId());
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, entity.getGroupName());
        resource.setProperty(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_CLUSTER_NAME, clusterName);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFAULT, entity.isDefault(), requestedIds);
        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS, requestedIds)) {
            java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = entity.getAlertDefinitions();
            java.util.List<org.apache.ambari.server.controller.AlertDefinitionResponse> definitionList = new java.util.ArrayList<>(definitions.size());
            for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
                org.apache.ambari.server.controller.AlertDefinitionResponse response = org.apache.ambari.server.controller.AlertDefinitionResponse.coerce(definition);
                definitionList.add(response);
            }
            resource.setProperty(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS, definitionList);
        }
        if (org.apache.ambari.server.controller.internal.BaseProvider.isPropertyRequested(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS, requestedIds)) {
            java.util.Set<org.apache.ambari.server.orm.entities.AlertTargetEntity> targetEntities = entity.getAlertTargets();
            java.util.List<org.apache.ambari.server.state.alert.AlertTarget> targets = new java.util.ArrayList<>(targetEntities.size());
            for (org.apache.ambari.server.orm.entities.AlertTargetEntity targetEntity : targetEntities) {
                org.apache.ambari.server.state.alert.AlertTarget target = org.apache.ambari.server.state.alert.AlertTarget.coerce(targetEntity);
                targets.add(target);
            }
            resource.setProperty(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS, targets);
        }
        return resource;
    }
}