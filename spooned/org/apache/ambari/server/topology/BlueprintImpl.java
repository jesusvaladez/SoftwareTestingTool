package org.apache.ambari.server.topology;
import org.apache.commons.lang.StringUtils;
public class BlueprintImpl implements org.apache.ambari.server.topology.Blueprint {
    private java.lang.String name;

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroups = new java.util.HashMap<>();

    private org.apache.ambari.server.controller.internal.Stack stack;

    private org.apache.ambari.server.topology.Configuration configuration;

    private org.apache.ambari.server.topology.BlueprintValidator validator;

    private org.apache.ambari.server.topology.SecurityConfiguration security;

    private org.apache.ambari.server.topology.Setting setting;

    private java.util.List<org.apache.ambari.server.topology.RepositorySetting> repoSettings;

    public BlueprintImpl(org.apache.ambari.server.orm.entities.BlueprintEntity entity) throws org.apache.ambari.server.stack.NoSuchStackException {
        this.name = entity.getBlueprintName();
        if (entity.getSecurityType() != null) {
            this.security = org.apache.ambari.server.topology.SecurityConfiguration.of(entity.getSecurityType(), entity.getSecurityDescriptorReference(), null);
        }
        parseStack(entity.getStack());
        processConfiguration(entity.getConfigurations());
        parseBlueprintHostGroups(entity);
        configuration.setParentConfiguration(stack.getConfiguration(getServices()));
        validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(this);
        processSetting(entity.getSettings());
        processRepoSettings();
    }

    public BlueprintImpl(java.lang.String name, java.util.Collection<org.apache.ambari.server.topology.HostGroup> groups, org.apache.ambari.server.controller.internal.Stack stack, org.apache.ambari.server.topology.Configuration configuration, org.apache.ambari.server.topology.SecurityConfiguration security) {
        this(name, groups, stack, configuration, security, null);
    }

    public BlueprintImpl(java.lang.String name, java.util.Collection<org.apache.ambari.server.topology.HostGroup> groups, org.apache.ambari.server.controller.internal.Stack stack, org.apache.ambari.server.topology.Configuration configuration, org.apache.ambari.server.topology.SecurityConfiguration security, org.apache.ambari.server.topology.Setting setting) {
        this.name = name;
        this.stack = stack;
        this.security = security;
        for (org.apache.ambari.server.topology.HostGroup hostGroup : groups) {
            hostGroups.put(hostGroup.getName(), hostGroup);
        }
        this.configuration = configuration;
        if (configuration.getParentConfiguration() == null) {
            configuration.setParentConfiguration(stack.getConfiguration(getServices()));
        }
        validator = new org.apache.ambari.server.topology.BlueprintValidatorImpl(this);
        this.setting = setting;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getStackName() {
        return stack.getName();
    }

    public java.lang.String getStackVersion() {
        return stack.getVersion();
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.SecurityConfiguration getSecurity() {
        return security;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> getHostGroups() {
        return hostGroups;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.HostGroup getHostGroup(java.lang.String name) {
        return hostGroups.get(name);
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.Configuration getConfiguration() {
        return configuration;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.Setting getSetting() {
        return setting;
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getServices() {
        java.util.Collection<java.lang.String> services = new java.util.HashSet<>();
        for (org.apache.ambari.server.topology.HostGroup group : getHostGroups().values()) {
            services.addAll(group.getServices());
        }
        return services;
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.server.state.ServiceInfo> getServiceInfos() {
        return getServices().stream().map(stack::getServiceInfo).filter(java.util.Optional::isPresent).map(java.util.Optional::get).collect(java.util.stream.Collectors.toList());
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getComponents(java.lang.String service) {
        java.util.Collection<java.lang.String> components = new java.util.HashSet<>();
        for (org.apache.ambari.server.topology.HostGroup group : getHostGroupsForService(service)) {
            components.addAll(group.getComponents(service));
        }
        return components;
    }

    @java.lang.Override
    public java.lang.String getRecoveryEnabled(java.lang.String serviceName, java.lang.String componentName) {
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> settingValue;
        if (setting == null)
            return null;

        settingValue = setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_COMPONENT_SETTINGS);
        for (java.util.Map<java.lang.String, java.lang.String> setting : settingValue) {
            java.lang.String name = setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_NAME);
            if (org.apache.commons.lang.StringUtils.equals(name, componentName)) {
                if (!org.apache.commons.lang.StringUtils.isEmpty(setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED))) {
                    return setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED);
                }
            }
        }
        settingValue = this.setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_SERVICE_SETTINGS);
        for (java.util.Map<java.lang.String, java.lang.String> setting : settingValue) {
            java.lang.String name = setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_NAME);
            if (org.apache.commons.lang.StringUtils.equals(name, serviceName)) {
                if (!org.apache.commons.lang.StringUtils.isEmpty(setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED))) {
                    return setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED);
                }
            }
        }
        settingValue = this.setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_SETTINGS);
        for (java.util.Map<java.lang.String, java.lang.String> setting : settingValue) {
            if (!org.apache.commons.lang.StringUtils.isEmpty(setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED))) {
                return setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_RECOVERY_ENABLED);
            }
        }
        return null;
    }

    @java.lang.Override
    public java.lang.String getCredentialStoreEnabled(java.lang.String serviceName) {
        if (setting == null)
            return null;

        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> settingValue = setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_SERVICE_SETTINGS);
        for (java.util.Map<java.lang.String, java.lang.String> setting : settingValue) {
            java.lang.String name = setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_NAME);
            if (org.apache.commons.lang.StringUtils.equals(name, serviceName)) {
                if (!org.apache.commons.lang.StringUtils.isEmpty(setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_CREDENTIAL_STORE_ENABLED))) {
                    return setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_CREDENTIAL_STORE_ENABLED);
                }
                break;
            }
        }
        return null;
    }

    @java.lang.Override
    public boolean shouldSkipFailure() {
        if (setting == null) {
            return false;
        }
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> settingValue = setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_DEPLOYMENT_SETTINGS);
        for (java.util.Map<java.lang.String, java.lang.String> setting : settingValue) {
            if (setting.containsKey(org.apache.ambari.server.topology.Setting.SETTING_NAME_SKIP_FAILURE)) {
                return setting.get(org.apache.ambari.server.topology.Setting.SETTING_NAME_SKIP_FAILURE).equalsIgnoreCase("true");
            }
        }
        return false;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.Stack getStack() {
        return stack;
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.server.topology.HostGroup> getHostGroupsForComponent(java.lang.String component) {
        java.util.Collection<org.apache.ambari.server.topology.HostGroup> resultGroups = new java.util.HashSet<>();
        for (org.apache.ambari.server.topology.HostGroup group : hostGroups.values()) {
            if (group.getComponentNames().contains(component)) {
                resultGroups.add(group);
            }
        }
        return resultGroups;
    }

    @java.lang.Override
    public java.util.Collection<org.apache.ambari.server.topology.HostGroup> getHostGroupsForService(java.lang.String service) {
        java.util.Collection<org.apache.ambari.server.topology.HostGroup> resultGroups = new java.util.HashSet<>();
        for (org.apache.ambari.server.topology.HostGroup group : hostGroups.values()) {
            if (group.getServices().contains(service)) {
                resultGroups.add(group);
            }
        }
        return resultGroups;
    }

    @java.lang.Override
    public void validateTopology() throws org.apache.ambari.server.topology.InvalidTopologyException {
        validator.validateTopology();
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.entities.BlueprintEntity toEntity() {
        org.apache.ambari.server.orm.entities.BlueprintEntity entity = new org.apache.ambari.server.orm.entities.BlueprintEntity();
        entity.setBlueprintName(name);
        if (security != null) {
            if (security.getType() != null) {
                entity.setSecurityType(security.getType());
            }
            if (security.getDescriptorReference() != null) {
                entity.setSecurityDescriptorReference(security.getDescriptorReference());
            }
        }
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
        stackEntity.setStackName(stack.getName());
        stackEntity.setStackVersion(stack.getVersion());
        entity.setStack(stackEntity);
        createHostGroupEntities(entity);
        createBlueprintConfigEntities(entity);
        createBlueprintSettingEntities(entity);
        return entity;
    }

    @java.lang.Override
    public void validateRequiredProperties() throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.GPLLicenseNotAcceptedException {
        validator.validateRequiredProperties();
    }

    private void parseStack(org.apache.ambari.server.orm.entities.StackEntity stackEntity) throws org.apache.ambari.server.stack.NoSuchStackException {
        try {
            stack = new org.apache.ambari.server.controller.internal.Stack(stackEntity.getStackName(), stackEntity.getStackVersion(), org.apache.ambari.server.controller.AmbariServer.getController());
        } catch (org.apache.ambari.server.StackAccessException e) {
            throw new org.apache.ambari.server.stack.NoSuchStackException(stackEntity.getStackName(), stackEntity.getStackVersion());
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new java.lang.RuntimeException("An error occurred parsing the stack information.", e);
        }
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> parseBlueprintHostGroups(org.apache.ambari.server.orm.entities.BlueprintEntity entity) {
        for (org.apache.ambari.server.orm.entities.HostGroupEntity hostGroupEntity : entity.getHostGroups()) {
            org.apache.ambari.server.topology.HostGroupImpl hostGroup = new org.apache.ambari.server.topology.HostGroupImpl(hostGroupEntity, getName(), stack);
            hostGroup.getConfiguration().setParentConfiguration(configuration);
            hostGroups.put(hostGroupEntity.getName(), hostGroup);
        }
        return hostGroups;
    }

    private void processConfiguration(java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> configs) {
        configuration = new org.apache.ambari.server.topology.Configuration(parseConfigurations(configs), parseAttributes(configs), null);
    }

    private void processSetting(java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintSettingEntity> blueprintSetting) {
        if (blueprintSetting != null) {
            setting = new org.apache.ambari.server.topology.Setting(parseSetting(blueprintSetting));
        }
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parseConfigurations(java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> configs) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        for (org.apache.ambari.server.orm.entities.BlueprintConfiguration config : configs) {
            java.lang.String type = config.getType();
            java.util.Map<java.lang.String, java.lang.String> typeProperties = gson.<java.util.Map<java.lang.String, java.lang.String>>fromJson(config.getConfigData(), java.util.Map.class);
            properties.put(type, typeProperties);
        }
        return properties;
    }

    private java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> parseSetting(java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintSettingEntity> blueprintSetting) {
        java.util.Map<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> properties = new java.util.HashMap<>();
        com.google.gson.Gson gson = new com.google.gson.Gson();
        for (org.apache.ambari.server.orm.entities.BlueprintSettingEntity setting : blueprintSetting) {
            java.lang.String settingName = setting.getSettingName();
            java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> settingProperties = gson.<java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>>fromJson(setting.getSettingData(), java.util.Set.class);
            properties.put(settingName, settingProperties);
        }
        return properties;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> parseAttributes(java.util.Collection<org.apache.ambari.server.orm.entities.BlueprintConfigEntity> configs) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> mapAttributes = new java.util.HashMap<>();
        if (configs != null) {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            for (org.apache.ambari.server.orm.entities.BlueprintConfigEntity config : configs) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttrs = gson.<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>fromJson(config.getConfigAttributes(), java.util.Map.class);
                if ((typeAttrs != null) && (!typeAttrs.isEmpty())) {
                    mapAttributes.put(config.getType(), typeAttrs);
                }
            }
        }
        return mapAttributes;
    }

    @java.lang.SuppressWarnings("unchecked")
    private void createHostGroupEntities(org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity) {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupEntity> entities = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.HostGroup group : getHostGroups().values()) {
            org.apache.ambari.server.orm.entities.HostGroupEntity hostGroupEntity = new org.apache.ambari.server.orm.entities.HostGroupEntity();
            entities.add(hostGroupEntity);
            hostGroupEntity.setName(group.getName());
            hostGroupEntity.setBlueprintEntity(blueprintEntity);
            hostGroupEntity.setBlueprintName(getName());
            hostGroupEntity.setCardinality(group.getCardinality());
            createHostGroupConfigEntities(hostGroupEntity, group.getConfiguration());
            createComponentEntities(hostGroupEntity, group.getComponents());
        }
        blueprintEntity.setHostGroups(entities);
    }

    private void createHostGroupConfigEntities(org.apache.ambari.server.orm.entities.HostGroupEntity hostGroup, org.apache.ambari.server.topology.Configuration groupConfiguration) {
        com.google.gson.Gson jsonSerializer = new com.google.gson.Gson();
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.HostGroupConfigEntity> configEntityMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propEntry : groupConfiguration.getProperties().entrySet()) {
            java.lang.String type = propEntry.getKey();
            java.util.Map<java.lang.String, java.lang.String> properties = propEntry.getValue();
            org.apache.ambari.server.orm.entities.HostGroupConfigEntity configEntity = new org.apache.ambari.server.orm.entities.HostGroupConfigEntity();
            configEntityMap.put(type, configEntity);
            configEntity.setBlueprintName(getName());
            configEntity.setHostGroupEntity(hostGroup);
            configEntity.setHostGroupName(hostGroup.getName());
            configEntity.setType(type);
            configEntity.setConfigData(jsonSerializer.toJson(properties));
        }
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributesEntry : groupConfiguration.getAttributes().entrySet()) {
            java.lang.String type = attributesEntry.getKey();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = attributesEntry.getValue();
            org.apache.ambari.server.orm.entities.HostGroupConfigEntity entity = configEntityMap.get(type);
            if (entity == null) {
                entity = new org.apache.ambari.server.orm.entities.HostGroupConfigEntity();
                configEntityMap.put(type, entity);
                entity.setBlueprintName(getName());
                entity.setHostGroupEntity(hostGroup);
                entity.setHostGroupName(hostGroup.getName());
                entity.setType(type);
            }
            entity.setConfigAttributes(jsonSerializer.toJson(attributes));
        }
        hostGroup.setConfigurations(configEntityMap.values());
    }

    @java.lang.SuppressWarnings("unchecked")
    private void createComponentEntities(org.apache.ambari.server.orm.entities.HostGroupEntity group, java.util.Collection<org.apache.ambari.server.topology.Component> components) {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostGroupComponentEntity> componentEntities = new java.util.HashSet<>();
        group.setComponents(componentEntities);
        for (org.apache.ambari.server.topology.Component component : components) {
            org.apache.ambari.server.orm.entities.HostGroupComponentEntity componentEntity = new org.apache.ambari.server.orm.entities.HostGroupComponentEntity();
            componentEntities.add(componentEntity);
            componentEntity.setName(component.getName());
            componentEntity.setBlueprintName(group.getBlueprintName());
            componentEntity.setHostGroupEntity(group);
            componentEntity.setHostGroupName(group.getName());
            if (component.getProvisionAction() != null) {
                componentEntity.setProvisionAction(component.getProvisionAction().toString());
            }
        }
        group.setComponents(componentEntities);
    }

    private void createBlueprintConfigEntities(org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity) {
        com.google.gson.Gson jsonSerializer = new com.google.gson.Gson();
        org.apache.ambari.server.topology.Configuration config = getConfiguration();
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.BlueprintConfigEntity> configEntityMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propEntry : config.getProperties().entrySet()) {
            java.lang.String type = propEntry.getKey();
            java.util.Map<java.lang.String, java.lang.String> properties = propEntry.getValue();
            org.apache.ambari.server.orm.entities.BlueprintConfigEntity configEntity = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
            configEntityMap.put(type, configEntity);
            configEntity.setBlueprintName(getName());
            configEntity.setBlueprintEntity(blueprintEntity);
            configEntity.setType(type);
            configEntity.setConfigData(jsonSerializer.toJson(properties));
        }
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributesEntry : config.getAttributes().entrySet()) {
            java.lang.String type = attributesEntry.getKey();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = attributesEntry.getValue();
            org.apache.ambari.server.orm.entities.BlueprintConfigEntity entity = configEntityMap.get(type);
            if (entity == null) {
                entity = new org.apache.ambari.server.orm.entities.BlueprintConfigEntity();
                configEntityMap.put(type, entity);
                entity.setBlueprintName(getName());
                entity.setBlueprintEntity(blueprintEntity);
                entity.setType(type);
            }
            entity.setConfigAttributes(jsonSerializer.toJson(attributes));
        }
        blueprintEntity.setConfigurations(configEntityMap.values());
    }

    private void createBlueprintSettingEntities(org.apache.ambari.server.orm.entities.BlueprintEntity blueprintEntity) {
        com.google.gson.Gson jsonSerializer = new com.google.gson.Gson();
        org.apache.ambari.server.topology.Setting blueprintSetting = getSetting();
        if (blueprintSetting != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.BlueprintSettingEntity> settingEntityMap = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>>> propEntry : blueprintSetting.getProperties().entrySet()) {
                java.lang.String settingName = propEntry.getKey();
                java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> properties = propEntry.getValue();
                org.apache.ambari.server.orm.entities.BlueprintSettingEntity settingEntity = new org.apache.ambari.server.orm.entities.BlueprintSettingEntity();
                settingEntityMap.put(settingName, settingEntity);
                settingEntity.setBlueprintName(getName());
                settingEntity.setBlueprintEntity(blueprintEntity);
                settingEntity.setSettingName(settingName);
                settingEntity.setSettingData(jsonSerializer.toJson(properties));
            }
            blueprintEntity.setSettings(settingEntityMap.values());
        }
    }

    @java.lang.Override
    public boolean isValidConfigType(java.lang.String configType) {
        if (org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV.equals(configType) || "global".equals(configType)) {
            return true;
        }
        return getStack().getServicesForConfigType(configType).stream().anyMatch(getServices()::contains);
    }

    private void processRepoSettings() {
        repoSettings = new java.util.ArrayList<>();
        if (setting != null) {
            java.util.Set<java.util.HashMap<java.lang.String, java.lang.String>> settingValue = setting.getSettingValue(org.apache.ambari.server.topology.Setting.SETTING_NAME_REPOSITORY_SETTINGS);
            for (java.util.Map<java.lang.String, java.lang.String> setting : settingValue) {
                org.apache.ambari.server.topology.RepositorySetting rs = parseRepositorySetting(setting);
                repoSettings.add(rs);
            }
        }
    }

    private org.apache.ambari.server.topology.RepositorySetting parseRepositorySetting(java.util.Map<java.lang.String, java.lang.String> setting) {
        org.apache.ambari.server.topology.RepositorySetting result = new org.apache.ambari.server.topology.RepositorySetting();
        result.setOperatingSystem(setting.get(org.apache.ambari.server.topology.RepositorySetting.OPERATING_SYSTEM));
        result.setOverrideStrategy(setting.get(org.apache.ambari.server.topology.RepositorySetting.OVERRIDE_STRATEGY));
        result.setRepoId(setting.get(org.apache.ambari.server.topology.RepositorySetting.REPO_ID));
        result.setBaseUrl(setting.get(org.apache.ambari.server.topology.RepositorySetting.BASE_URL));
        return result;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.topology.RepositorySetting> getRepositorySettings() {
        return repoSettings;
    }
}