package org.apache.ambari.server.controller.internal;
public class BlueprintResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.class);

    public static final java.lang.String BLUEPRINT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Blueprints", "blueprint_name");

    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Blueprints", "stack_name");

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Blueprints", "stack_version");

    public static final java.lang.String BLUEPRINT_SECURITY_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Blueprints", "security");

    public static final java.lang.String BLUEPRINTS_PROPERTY_ID = "Blueprints";

    public static final java.lang.String HOST_GROUP_PROPERTY_ID = "host_groups";

    public static final java.lang.String HOST_GROUP_NAME_PROPERTY_ID = "name";

    public static final java.lang.String HOST_GROUP_CARDINALITY_PROPERTY_ID = "cardinality";

    public static final java.lang.String COMPONENT_PROPERTY_ID = "components";

    public static final java.lang.String COMPONENT_NAME_PROPERTY_ID = "name";

    public static final java.lang.String COMPONENT_PROVISION_ACTION_PROPERTY_ID = "provision_action";

    public static final java.lang.String CONFIGURATION_PROPERTY_ID = "configurations";

    public static final java.lang.String SETTING_PROPERTY_ID = "settings";

    public static final java.lang.String PROPERTIES_PROPERTY_ID = "properties";

    public static final java.lang.String PROPERTIES_ATTRIBUTES_PROPERTY_ID = "properties_attributes";

    public static final java.lang.String SCHEMA_IS_NOT_SUPPORTED_MESSAGE = "Configuration format provided in Blueprint is not supported";

    public static final java.lang.String REQUEST_BODY_EMPTY_ERROR_MESSAGE = "Request body for Blueprint create request is empty";

    public static final java.lang.String CONFIGURATION_LIST_CHECK_ERROR_MESSAGE = "Configurations property must be a List of Maps";

    public static final java.lang.String CONFIGURATION_MAP_CHECK_ERROR_MESSAGE = "Configuration elements must be Maps";

    public static final java.lang.String CONFIGURATION_MAP_SIZE_CHECK_ERROR_MESSAGE = "Configuration Maps must hold a single configuration type each";

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_SECURITY_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.SETTING_PROPERTY_ID);

    private static org.apache.ambari.server.topology.BlueprintFactory blueprintFactory;

    private static org.apache.ambari.server.topology.SecurityConfigurationFactory securityConfigurationFactory;

    private static org.apache.ambari.server.orm.dao.BlueprintDAO blueprintDAO;

    private static org.apache.ambari.server.orm.dao.TopologyRequestDAO topologyRequestDAO;

    private static com.google.gson.Gson jsonSerializer;

    BlueprintResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.keyPropertyIds, controller);
    }

    public static void init(org.apache.ambari.server.topology.BlueprintFactory factory, org.apache.ambari.server.orm.dao.BlueprintDAO bpDao, org.apache.ambari.server.orm.dao.TopologyRequestDAO trDao, org.apache.ambari.server.topology.SecurityConfigurationFactory securityFactory, com.google.gson.Gson gson, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo) {
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.blueprintFactory = factory;
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.blueprintDAO = bpDao;
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.topologyRequestDAO = trDao;
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.securityConfigurationFactory = securityFactory;
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer = gson;
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.ambariMetaInfo = metaInfo;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            try {
                createResources(getCreateCommand(properties, request.getRequestInfoProperties()));
            } catch (java.lang.IllegalArgumentException e) {
                org.apache.ambari.server.controller.internal.BlueprintResourceProvider.LOG.error("Exception while creating blueprint", e);
                throw e;
            }
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.List<org.apache.ambari.server.orm.entities.BlueprintEntity> results = null;
        boolean applyPredicate = false;
        if (predicate != null) {
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> requestProps = getPropertyMaps(predicate);
            if (requestProps.size() == 1) {
                java.lang.String name = ((java.lang.String) (requestProps.iterator().next().get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID)));
                if (name != null) {
                    org.apache.ambari.server.orm.entities.BlueprintEntity entity = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.blueprintDAO.findByName(name);
                    results = (entity == null) ? java.util.Collections.emptyList() : java.util.Collections.singletonList(entity);
                }
            }
        }
        if (results == null) {
            applyPredicate = true;
            results = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.blueprintDAO.findAll();
        }
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.BlueprintEntity entity : results) {
            org.apache.ambari.server.controller.spi.Resource resource = toResource(entity, getRequestPropertyIds(request, predicate));
            if (((predicate == null) || (!applyPredicate)) || predicate.evaluate(resource)) {
                resources.add(resource);
            }
        }
        if ((predicate != null) && resources.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.NoSuchResourceException("The requested resource doesn't exist: Blueprint not found, " + predicate);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> setResources = getResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> provisionRequests = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.topologyRequestDAO.findAllProvisionRequests();
        java.util.Set<java.lang.String> provisionedBlueprints = provisionRequests.stream().map(org.apache.ambari.server.orm.entities.TopologyRequestEntity::getBlueprintName).collect(java.util.stream.Collectors.toSet());
        for (final org.apache.ambari.server.controller.spi.Resource resource : setResources) {
            final java.lang.String blueprintName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID)));
            com.google.common.base.Preconditions.checkArgument(!provisionedBlueprints.contains(blueprintName), "Blueprint %s cannot be deleted as cluster provisioning was initiated on it.", blueprintName);
            org.apache.ambari.server.controller.internal.BlueprintResourceProvider.LOG.info("Deleting Blueprint, name = " + blueprintName);
            modifyResources(() -> {
                org.apache.ambari.server.controller.internal.BlueprintResourceProvider.blueprintDAO.removeByName(blueprintName);
                return null;
            });
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, predicate);
        return getRequestStatus(null);
    }

    private static org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.BlueprintEntity entity, java.util.Set<java.lang.String> requestedIds) throws org.apache.ambari.server.controller.spi.NoSuchResourceException {
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = entity.getStack();
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_NAME_PROPERTY_ID, entity.getBlueprintName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_NAME_PROPERTY_ID, stackEntity.getStackName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.STACK_VERSION_PROPERTY_ID, stackEntity.getStackVersion(), requestedIds);
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> listGroupProps = new java.util.ArrayList<>();
        java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupEntity> hostGroups = entity.getHostGroups();
        for (org.apache.ambari.server.orm.entities.HostGroupEntity hostGroup : hostGroups) {
            java.util.Map<java.lang.String, java.lang.Object> mapGroupProps = new java.util.HashMap<>();
            mapGroupProps.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_NAME_PROPERTY_ID, hostGroup.getName());
            listGroupProps.add(mapGroupProps);
            mapGroupProps.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_CARDINALITY_PROPERTY_ID, hostGroup.getCardinality());
            java.util.List<java.util.Map<java.lang.String, java.lang.String>> listComponentProps = new java.util.ArrayList<>();
            java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupComponentEntity> components = hostGroup.getComponents();
            for (org.apache.ambari.server.orm.entities.HostGroupComponentEntity component : components) {
                java.util.Map<java.lang.String, java.lang.String> mapComponentProps = new java.util.HashMap<>();
                mapComponentProps.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_NAME_PROPERTY_ID, component.getName());
                if (component.getProvisionAction() != null) {
                    mapComponentProps.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_PROVISION_ACTION_PROPERTY_ID, component.getProvisionAction().toString());
                }
                listComponentProps.add(mapComponentProps);
            }
            mapGroupProps.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.COMPONENT_PROPERTY_ID, listComponentProps);
            mapGroupProps.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_PROPERTY_ID, populateConfigurationList(hostGroup.getConfigurations()));
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.HOST_GROUP_PROPERTY_ID, listGroupProps, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_PROPERTY_ID, populateConfigurationList(entity.getConfigurations()), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.SETTING_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.populateSettingList(entity.getSettings()), requestedIds);
        if (entity.getSecurityType() != null) {
            java.util.Map<java.lang.String, java.lang.String> securityConfigMap = new java.util.LinkedHashMap<>();
            securityConfigMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID, entity.getSecurityType().name());
            if (entity.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) {
                securityConfigMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_REFERENCE_PROPERTY_ID, entity.getSecurityDescriptorReference());
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_SECURITY_PROPERTY_ID, securityConfigMap, requestedIds);
        }
        return resource;
    }

    java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> populateConfigurationList(java.util.Collection<? extends org.apache.ambari.server.orm.entities.BlueprintConfiguration> configurations) throws org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> listConfigurations = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.BlueprintConfiguration config : configurations) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> mapConfigurations = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.Object> configTypeDefinition = new java.util.HashMap<>();
            java.lang.String type = config.getType();
            if (config instanceof org.apache.ambari.server.orm.entities.BlueprintConfigEntity) {
                java.util.Map<java.lang.String, java.lang.String> properties = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer.<java.util.Map<java.lang.String, java.lang.String>>fromJson(config.getConfigData(), java.util.Map.class);
                org.apache.ambari.server.orm.entities.StackEntity stack = ((org.apache.ambari.server.orm.entities.BlueprintConfigEntity) (config)).getBlueprintEntity().getStack();
                org.apache.ambari.server.state.StackInfo metaInfoStack;
                try {
                    metaInfoStack = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.ambariMetaInfo.getStack(stack.getStackName(), stack.getStackVersion());
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(e.getMessage());
                }
                java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes = metaInfoStack.getConfigPropertiesTypes(type);
                org.apache.ambari.server.utils.SecretReference.replacePasswordsWithReferences(propertiesTypes, properties, type, -1L);
                configTypeDefinition.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID, properties);
            } else {
                java.util.Map<java.lang.String, java.lang.Object> properties = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(config.getConfigData(), java.util.Map.class);
                configTypeDefinition.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID, properties);
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer.<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>fromJson(config.getConfigAttributes(), java.util.Map.class);
            if ((attributes != null) && (!attributes.isEmpty())) {
                configTypeDefinition.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID, attributes);
            }
            mapConfigurations.put(type, configTypeDefinition);
            listConfigurations.add(mapConfigurations);
        }
        return listConfigurations;
    }

    public static java.util.List<java.util.Map<java.lang.String, java.lang.Object>> populateSettingList(java.util.Collection<? extends org.apache.ambari.server.orm.entities.BlueprintSettingEntity> settings) throws org.apache.ambari.server.controller.spi.NoSuchResourceException {
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> listSettings = new java.util.ArrayList<>();
        if (settings != null) {
            for (org.apache.ambari.server.orm.entities.BlueprintSettingEntity setting : settings) {
                java.util.List<java.util.Map<java.lang.String, java.lang.String>> propertiesList = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer.<java.util.List<java.util.Map<java.lang.String, java.lang.String>>>fromJson(setting.getSettingData(), java.util.List.class);
                java.util.Map<java.lang.String, java.lang.Object> settingMap = new java.util.HashMap<>();
                settingMap.put(setting.getSettingName(), propertiesList);
                listSettings.add(settingMap);
            }
        }
        return listSettings;
    }

    void createBlueprintConfigEntities(java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> propertyMaps, org.apache.ambari.server.orm.entities.BlueprintEntity blueprint) {
        java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> configurations = new java.util.ArrayList<>();
        if (propertyMaps != null) {
            for (java.util.Map<java.lang.String, java.lang.String> configuration : propertyMaps) {
                org.apache.ambari.server.orm.entities.BlueprintConfigEntity configEntity = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
                configEntity.setBlueprintEntity(blueprint);
                configEntity.setBlueprintName(blueprint.getBlueprintName());
                populateConfigurationEntity(configuration, configEntity);
                configurations.add(configEntity);
            }
        }
        blueprint.setConfigurations(configurations);
    }

    void populateConfigurationEntity(java.util.Map<java.lang.String, java.lang.String> configuration, org.apache.ambari.server.orm.entities.BlueprintConfiguration configEntity) {
        org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy p = decidePopulationStrategy(configuration);
        p.applyConfiguration(configuration, configEntity);
    }

    org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy decidePopulationStrategy(java.util.Map<java.lang.String, java.lang.String> configuration) {
        if ((configuration != null) && (!configuration.isEmpty())) {
            java.lang.String keyEntry = configuration.keySet().iterator().next();
            java.lang.String[] keyNameTokens = keyEntry.split("/");
            int levels = keyNameTokens.length;
            java.lang.String propertiesType = keyNameTokens[1];
            if (levels == 2) {
                return new org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategyV1();
            } else if (((levels == 3) && org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID.equals(propertiesType)) || ((levels == 4) && org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID.equals(propertiesType))) {
                return new org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategyV2();
            } else {
                throw new java.lang.IllegalArgumentException(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.SCHEMA_IS_NOT_SUPPORTED_MESSAGE);
            }
        } else {
            return new org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategyV2();
        }
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getCreateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties, final java.util.Map<java.lang.String, java.lang.String> requestInfoProps) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.SuppressWarnings("rawtypes")
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.lang.String rawRequestBody = requestInfoProps.get(org.apache.ambari.server.controller.spi.Request.REQUEST_INFO_BODY_PROPERTY);
                com.google.common.base.Preconditions.checkArgument(!com.google.common.base.Strings.isNullOrEmpty(rawRequestBody), org.apache.ambari.server.controller.internal.BlueprintResourceProvider.REQUEST_BODY_EMPTY_ERROR_MESSAGE);
                java.util.Map<java.lang.String, java.lang.Object> rawBodyMap = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(rawRequestBody, java.util.Map.class);
                java.lang.Object configurationData = rawBodyMap.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_PROPERTY_ID);
                if (configurationData != null) {
                    com.google.common.base.Preconditions.checkArgument(configurationData instanceof java.util.List, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_LIST_CHECK_ERROR_MESSAGE);
                    for (java.lang.Object map : ((java.util.List) (configurationData))) {
                        com.google.common.base.Preconditions.checkArgument(map instanceof java.util.Map, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_MAP_CHECK_ERROR_MESSAGE);
                        com.google.common.base.Preconditions.checkArgument(((java.util.Map) (map)).size() <= 1, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.CONFIGURATION_MAP_SIZE_CHECK_ERROR_MESSAGE);
                    }
                }
                org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.securityConfigurationFactory.createSecurityConfigurationFromRequest(((java.util.Map<java.lang.String, java.lang.Object>) (rawBodyMap.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINTS_PROPERTY_ID))), true);
                org.apache.ambari.server.topology.Blueprint blueprint;
                try {
                    blueprint = org.apache.ambari.server.controller.internal.BlueprintResourceProvider.blueprintFactory.createBlueprint(properties, securityConfiguration);
                } catch (org.apache.ambari.server.stack.NoSuchStackException e) {
                    throw new java.lang.IllegalArgumentException("Specified stack doesn't exist: " + e, e);
                }
                if (org.apache.ambari.server.controller.internal.BlueprintResourceProvider.blueprintDAO.findByName(blueprint.getName()) != null) {
                    throw new org.apache.ambari.server.DuplicateResourceException("Attempted to create a Blueprint which already exists, blueprint_name=" + blueprint.getName());
                }
                try {
                    blueprint.validateRequiredProperties();
                } catch (org.apache.ambari.server.topology.InvalidTopologyException | org.apache.ambari.server.topology.GPLLicenseNotAcceptedException e) {
                    throw new java.lang.IllegalArgumentException("Blueprint configuration validation failed: " + e.getMessage(), e);
                }
                java.lang.String validateTopology = requestInfoProps.get("validate_topology");
                if ((validateTopology == null) || (!validateTopology.equalsIgnoreCase("false"))) {
                    try {
                        blueprint.validateTopology();
                    } catch (org.apache.ambari.server.topology.InvalidTopologyException e) {
                        throw new java.lang.IllegalArgumentException(e.getMessage());
                    }
                }
                org.apache.ambari.server.controller.internal.BlueprintResourceProvider.LOG.info("Creating Blueprint, name=" + blueprint.getName());
                java.lang.String blueprintSetting = (blueprint.getSetting() == null) ? "(null)" : org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer.toJson(blueprint.getSetting().getProperties());
                org.apache.ambari.server.controller.internal.BlueprintResourceProvider.LOG.info("Blueprint setting=" + blueprintSetting);
                try {
                    org.apache.ambari.server.controller.internal.BlueprintResourceProvider.blueprintDAO.create(blueprint.toEntity());
                } catch (java.lang.Exception e) {
                    throw new java.lang.RuntimeException(e);
                }
                return null;
            }
        };
    }

    protected static abstract class BlueprintConfigPopulationStrategy {
        public void applyConfiguration(java.util.Map<java.lang.String, java.lang.String> configuration, org.apache.ambari.server.orm.entities.BlueprintConfiguration blueprintConfiguration) {
            java.util.Map<java.lang.String, java.lang.String> configData = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes = new java.util.HashMap<>();
            if (configuration != null) {
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : configuration.entrySet()) {
                    java.lang.String absolutePropName = entry.getKey();
                    java.lang.String propertyValue = entry.getValue();
                    java.lang.String[] propertyNameTokens = absolutePropName.split("/");
                    if (blueprintConfiguration.getType() == null) {
                        blueprintConfiguration.setType(propertyNameTokens[0]);
                    }
                    addProperty(configData, configAttributes, propertyNameTokens, propertyValue);
                }
            }
            blueprintConfiguration.setConfigData(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer.toJson(configData));
            blueprintConfiguration.setConfigAttributes(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.jsonSerializer.toJson(configAttributes));
        }

        protected abstract void addProperty(java.util.Map<java.lang.String, java.lang.String> configData, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes, java.lang.String[] propertyNameTokens, java.lang.String propertyValue);
    }

    protected static class BlueprintConfigPopulationStrategyV1 extends org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy {
        @java.lang.Override
        protected void addProperty(java.util.Map<java.lang.String, java.lang.String> configData, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes, java.lang.String[] propertyNameTokens, java.lang.String propertyValue) {
            configData.put(propertyNameTokens[1], propertyValue);
        }
    }

    protected static class BlueprintConfigPopulationStrategyV2 extends org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BlueprintConfigPopulationStrategy {
        @java.lang.Override
        protected void addProperty(java.util.Map<java.lang.String, java.lang.String> configData, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes, java.lang.String[] propertyNameTokens, java.lang.String propertyValue) {
            if (org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID.equals(propertyNameTokens[1])) {
                configData.put(propertyNameTokens[2], propertyValue);
            } else if (org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID.equals(propertyNameTokens[1])) {
                addConfigAttribute(configAttributes, propertyNameTokens, propertyValue);
            }
        }

        private void addConfigAttribute(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configDependencyProperties, java.lang.String[] propertyNameTokens, java.lang.String value) {
            if (!configDependencyProperties.containsKey(propertyNameTokens[2])) {
                configDependencyProperties.put(propertyNameTokens[2], new java.util.HashMap<>());
            }
            java.util.Map<java.lang.String, java.lang.String> propertiesGroup = configDependencyProperties.get(propertyNameTokens[2]);
            propertiesGroup.put(propertyNameTokens[3], value);
        }
    }
}