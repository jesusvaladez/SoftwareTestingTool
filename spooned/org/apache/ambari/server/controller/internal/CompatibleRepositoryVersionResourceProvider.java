package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class CompatibleRepositoryVersionResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.class);

    public static final java.lang.String REPOSITORY_VERSION_ID_PROPERTY_ID = "CompatibleRepositoryVersions/id";

    public static final java.lang.String REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID = "CompatibleRepositoryVersions/stack_name";

    public static final java.lang.String REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID = "CompatibleRepositoryVersions/stack_version";

    public static final java.lang.String REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID = "CompatibleRepositoryVersions/repository_version";

    public static final java.lang.String REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID = "CompatibleRepositoryVersions/display_name";

    public static final java.lang.String REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID = "CompatibleRepositoryVersions/upgrade_types";

    public static final java.lang.String REPOSITORY_VERSION_SERVICES = "CompatibleRepositoryVersions/services";

    public static final java.lang.String REPOSITORY_VERSION_STACK_SERVICES = "CompatibleRepositoryVersions/stack_services";

    public static final java.lang.String SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID = new org.apache.ambari.server.api.resources.OperatingSystemResourceDefinition().getPluralName();

    private static final java.lang.String REPOSITORY_STACK_VALUE = "stack_value";

    private static final java.util.Set<java.lang.String> pkPropertyIds = java.util.Collections.singleton(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID);

    static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_STACK_VALUE, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.SUBRESOURCE_OPERATING_SYSTEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_SERVICES, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_SERVICES);

    static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new com.google.common.collect.ImmutableMap.Builder<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID).build();

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RepositoryVersionDAO s_repositoryVersionDAO;

    @com.google.inject.Inject
    private static com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> s_ambariMetaInfo;

    public CompatibleRepositoryVersionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController amc) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.keyPropertyIds, amc);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        final java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        java.lang.Long currentStackUniqueId = null;
        java.util.Map<java.lang.Long, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersion> compatibleRepositoryVersionsMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.StackId stackId = null;
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_STACK_VALUE)) {
                stackId = new org.apache.ambari.server.state.StackId(propertyMap.get(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_STACK_VALUE).toString());
                break;
            }
        }
        if (null == stackId) {
            if (propertyMaps.size() == 1) {
                java.util.Map<java.lang.String, java.lang.Object> propertyMap = propertyMaps.iterator().next();
                stackId = getStackInformationFromUrl(propertyMap);
            } else {
                org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.error("Property Maps size is NOT equal to 1. Current 'propertyMaps' size = {}", propertyMaps.size());
            }
        }
        if (null == stackId) {
            org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.error("Could not determine stack to process.  Returning empty set.");
            return resources;
        }
        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity : org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.s_repositoryVersionDAO.findByStack(stackId)) {
            currentStackUniqueId = repositoryVersionEntity.getId();
            compatibleRepositoryVersionsMap.put(repositoryVersionEntity.getId(), new org.apache.ambari.server.controller.internal.CompatibleRepositoryVersion(repositoryVersionEntity));
            if (org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.debug("Added current stack id: {} to map", repositoryVersionEntity.getId());
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> packs = org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.s_ambariMetaInfo.get().getUpgradePacks(stackId.getStackName(), stackId.getStackVersion());
        for (org.apache.ambari.server.stack.upgrade.UpgradePack up : packs.values()) {
            if (null != up.getTargetStack()) {
                org.apache.ambari.server.state.StackId targetStackId = new org.apache.ambari.server.state.StackId(up.getTargetStack());
                java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> repositoryVersionEntities = org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.s_repositoryVersionDAO.findByStack(targetStackId);
                for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity : repositoryVersionEntities) {
                    if (compatibleRepositoryVersionsMap.containsKey(repositoryVersionEntity.getId())) {
                        compatibleRepositoryVersionsMap.get(repositoryVersionEntity.getId()).addUpgradePackType(up.getType());
                        if (org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.isDebugEnabled()) {
                            org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.debug("Stack id: {} exists in map.  Appended new upgrade type {}{}", up.getType(), repositoryVersionEntity.getId());
                        }
                    } else {
                        org.apache.ambari.server.controller.internal.CompatibleRepositoryVersion compatibleRepositoryVersionEntity = new org.apache.ambari.server.controller.internal.CompatibleRepositoryVersion(repositoryVersionEntity);
                        compatibleRepositoryVersionEntity.addUpgradePackType(up.getType());
                        compatibleRepositoryVersionsMap.put(repositoryVersionEntity.getId(), compatibleRepositoryVersionEntity);
                        if (org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.isDebugEnabled()) {
                            org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.debug("Added Stack id: {} to map with upgrade type {}", repositoryVersionEntity.getId(), up.getType());
                        }
                    }
                }
            } else if (currentStackUniqueId != null) {
                compatibleRepositoryVersionsMap.get(currentStackUniqueId).addUpgradePackType(up.getType());
                if (org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.debug("Current Stack id: {} retrieved from map. Added upgrade type {}", currentStackUniqueId, up.getType());
                }
            } else {
                org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.LOG.error("Couldn't retrieve Current stack entry from Map.");
            }
        }
        for (org.apache.ambari.server.controller.internal.CompatibleRepositoryVersion entity : compatibleRepositoryVersionsMap.values()) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity = entity.getRepositoryVersionEntity();
            final org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_ID_PROPERTY_ID, repositoryVersionEntity.getId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, repositoryVersionEntity.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, repositoryVersionEntity.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, repositoryVersionEntity.getDisplayName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, repositoryVersionEntity.getVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_UPGRADES_SUPPORTED_TYPES_ID, entity.getSupportedTypes(), requestedIds);
            final org.apache.ambari.server.state.repository.VersionDefinitionXml xml;
            try {
                xml = repositoryVersionEntity.getRepositoryXml();
            } catch (java.lang.Exception e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Could not load xml for Repository %s", repositoryVersionEntity.getId()), e);
            }
            final org.apache.ambari.server.state.StackInfo stack;
            try {
                stack = org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.s_ambariMetaInfo.get().getStack(repositoryVersionEntity.getStackName(), repositoryVersionEntity.getStackVersion());
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(java.lang.String.format("Could not load stack %s for Repository %s", repositoryVersionEntity.getStackId().toString(), repositoryVersionEntity.getId()));
            }
            final java.util.List<org.apache.ambari.server.state.repository.ManifestServiceInfo> stackServices;
            if (null != xml) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_SERVICES, xml.getAvailableServices(stack), requestedIds);
                stackServices = xml.getStackServices(stack);
            } else {
                stackServices = new java.util.ArrayList<>();
                for (org.apache.ambari.server.state.ServiceInfo si : stack.getServices()) {
                    stackServices.add(new org.apache.ambari.server.state.repository.ManifestServiceInfo(si.getName(), si.getDisplayName(), si.getComment(), java.util.Collections.singleton(si.getVersion())));
                }
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_SERVICES, stackServices, requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.pkPropertyIds;
    }

    protected org.apache.ambari.server.state.StackId getStackInformationFromUrl(java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID) && propertyMap.containsKey(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID)) {
            return new org.apache.ambari.server.state.StackId(propertyMap.get(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).toString(), propertyMap.get(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).toString());
        }
        return null;
    }

    @java.lang.SuppressWarnings("rawtypes")
    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate amendPredicate(org.apache.ambari.server.controller.spi.Predicate predicate) {
        if (!org.apache.ambari.server.controller.predicate.AndPredicate.class.isInstance(predicate)) {
            return null;
        }
        org.apache.ambari.server.controller.predicate.AndPredicate ap = ((org.apache.ambari.server.controller.predicate.AndPredicate) (predicate));
        if (2 != ap.getPropertyIds().size()) {
            return null;
        }
        if ((!ap.getPropertyIds().contains(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID)) && (!ap.getPropertyIds().contains(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID))) {
            return null;
        }
        org.apache.ambari.server.controller.spi.Predicate[] predicates = ap.getPredicates();
        if ((!org.apache.ambari.server.controller.predicate.EqualsPredicate.class.isInstance(predicates[0])) || (!org.apache.ambari.server.controller.predicate.EqualsPredicate.class.isInstance(predicates[1]))) {
            return null;
        }
        org.apache.ambari.server.controller.predicate.EqualsPredicate pred1 = ((org.apache.ambari.server.controller.predicate.EqualsPredicate) (predicates[0]));
        org.apache.ambari.server.controller.predicate.EqualsPredicate pred2 = ((org.apache.ambari.server.controller.predicate.EqualsPredicate) (predicates[1]));
        org.apache.ambari.server.state.StackId stackId = null;
        if (pred1.getPropertyId().equals(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID)) {
            stackId = new org.apache.ambari.server.state.StackId(pred1.getValue().toString(), pred2.getValue().toString());
        } else {
            stackId = new org.apache.ambari.server.state.StackId(pred2.getValue().toString(), pred1.getValue().toString());
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> packs = org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.s_ambariMetaInfo.get().getUpgradePacks(stackId.getStackName(), stackId.getStackVersion());
        java.util.Set<java.lang.String> stackIds = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> entry : packs.entrySet()) {
            org.apache.ambari.server.stack.upgrade.UpgradePack pack = entry.getValue();
            java.lang.String packStack = pack.getTargetStack();
            if ((null == packStack) || (!packStack.equals(stackId.toString()))) {
                stackIds.add(packStack);
            }
        }
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> usable = new java.util.ArrayList<>();
        usable.add(predicate);
        for (java.lang.String requiredStack : stackIds) {
            org.apache.ambari.server.state.StackId targetStack = new org.apache.ambari.server.state.StackId(requiredStack);
            org.apache.ambari.server.controller.spi.Predicate p = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID).equals(targetStack.getStackName()).and().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID).equals(targetStack.getStackVersion()).toPredicate();
            usable.add(p);
        }
        org.apache.ambari.server.controller.spi.Predicate p = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.CompatibleRepositoryVersionResourceProvider.REPOSITORY_STACK_VALUE).equals(stackId.toString()).toPredicate();
        usable.add(p);
        p = new org.apache.ambari.server.controller.predicate.OrPredicate(usable.toArray(new org.apache.ambari.server.controller.spi.Predicate[usable.size()]));
        return p;
    }
}