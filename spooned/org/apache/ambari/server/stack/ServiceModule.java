package org.apache.ambari.server.stack;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
public class ServiceModule extends org.apache.ambari.server.stack.BaseModule<org.apache.ambari.server.stack.ServiceModule, org.apache.ambari.server.state.ServiceInfo> implements org.apache.ambari.server.stack.Validable {
    private org.apache.ambari.server.state.ServiceInfo serviceInfo;

    private org.apache.ambari.server.stack.StackContext stackContext;

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ConfigurationModule> configurationModules = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ComponentModule> componentModules = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.ThemeModule> themeModules = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.stack.QuickLinksConfigurationModule> quickLinksConfigurationModules = new java.util.HashMap<>();

    private org.apache.ambari.server.stack.ServiceDirectory serviceDirectory;

    private boolean isCommonService;

    protected boolean valid = true;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.ServiceModule.class);

    private boolean hasConfigs = true;

    public ServiceModule(org.apache.ambari.server.stack.StackContext stackContext, org.apache.ambari.server.state.ServiceInfo serviceInfo, org.apache.ambari.server.stack.ServiceDirectory serviceDirectory) {
        this(stackContext, serviceInfo, serviceDirectory, false);
    }

    public ServiceModule(org.apache.ambari.server.stack.StackContext stackContext, org.apache.ambari.server.state.ServiceInfo serviceInfo, org.apache.ambari.server.stack.ServiceDirectory serviceDirectory, boolean isCommonService) {
        this.serviceInfo = serviceInfo;
        this.stackContext = stackContext;
        this.serviceDirectory = serviceDirectory;
        this.isCommonService = isCommonService;
        serviceInfo.setMetricsFile(serviceDirectory.getMetricsFile(serviceInfo.getName()));
        serviceInfo.setAlertsFile(serviceDirectory.getAlertsFile());
        serviceInfo.setKerberosDescriptorFile(serviceDirectory.getKerberosDescriptorFile());
        serviceInfo.setWidgetsDescriptorFile(serviceDirectory.getWidgetsDescriptorFile(serviceInfo.getName()));
        serviceInfo.setRoleCommandOrder(serviceDirectory.getRoleCommandOrder());
        serviceInfo.setSchemaVersion(org.apache.ambari.server.api.services.AmbariMetaInfo.SCHEMA_VERSION_2);
        serviceInfo.setServicePackageFolder(serviceDirectory.getPackageDir());
        serviceInfo.setServiceUpgradesFolder(serviceDirectory.getUpgradesDir());
        serviceInfo.setChecksFolder(serviceDirectory.getChecksDir());
        serviceInfo.setServerActionsFolder(serviceDirectory.getServerActionsDir());
        serviceInfo.setAdvisorFile(serviceDirectory.getAdvisorFile());
        serviceInfo.setAdvisorName(serviceDirectory.getAdvisorName(serviceInfo.getName()));
        populateComponentModules();
        populateConfigurationModules();
        populateThemeModules();
        populateQuickLinksConfigurationModules();
        validateServiceInfo();
    }

    @java.lang.Override
    public org.apache.ambari.server.state.ServiceInfo getModuleInfo() {
        return serviceInfo;
    }

    @java.lang.Override
    public void resolve(org.apache.ambari.server.stack.ServiceModule parentModule, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        resolveInternal(parentModule, allStacks, commonServices, extensions, false);
    }

    public void resolveExplicit(org.apache.ambari.server.stack.ServiceModule parentModule, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        resolveInternal(parentModule, allStacks, commonServices, extensions, true);
    }

    public void resolveInternal(org.apache.ambari.server.stack.ServiceModule parentModule, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions, boolean resolveExplicit) throws org.apache.ambari.server.AmbariException {
        if ((!serviceInfo.isValid()) || (!parentModule.isValid())) {
            return;
        }
        org.apache.ambari.server.stack.ServiceModule.LOG.debug("Resolve service");
        if ((!org.apache.commons.lang.StringUtils.isBlank(serviceInfo.getParent())) && (!resolveExplicit)) {
            return;
        }
        org.apache.ambari.server.state.ServiceInfo parent = parentModule.getModuleInfo();
        if (serviceInfo.getComment() == null) {
            serviceInfo.setComment(parent.getComment());
        }
        org.apache.ambari.server.stack.ServiceModule.LOG.info(java.lang.String.format("Display name service/parent: %s/%s", serviceInfo.getDisplayName(), parent.getDisplayName()));
        if (serviceInfo.getDisplayName() == null) {
            serviceInfo.setDisplayName(parent.getDisplayName());
        }
        if (serviceInfo.getServiceType() == null) {
            serviceInfo.setServiceType(parent.getServiceType());
        }
        if (serviceInfo.getServiceAdvisorType() == null) {
            org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType = parent.getServiceAdvisorType();
            serviceInfo.setServiceAdvisorType(serviceAdvisorType == null ? org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON : serviceAdvisorType);
        }
        if (serviceInfo.getVersion() == null) {
            serviceInfo.setVersion(parent.getVersion());
        }
        if ((serviceInfo.getRequiredServices() == null) || (serviceInfo.getRequiredServices().size() == 0)) {
            serviceInfo.setRequiredServices(parent.getRequiredServices() != null ? parent.getRequiredServices() : java.util.Collections.emptyList());
        }
        if (serviceInfo.isRestartRequiredAfterChange() == null) {
            serviceInfo.setRestartRequiredAfterChange(parent.isRestartRequiredAfterChange());
        }
        if (serviceInfo.isRestartRequiredAfterRackChange() == null) {
            serviceInfo.setRestartRequiredAfterRackChange(parent.isRestartRequiredAfterRackChange());
        }
        if (serviceInfo.isMonitoringService() == null) {
            serviceInfo.setMonitoringService(parent.isMonitoringService());
        }
        if (serviceInfo.getOsSpecifics().isEmpty()) {
            serviceInfo.setOsSpecifics(parent.getOsSpecifics());
        }
        if (serviceInfo.getCommandScript() == null) {
            serviceInfo.setCommandScript(parent.getCommandScript());
        }
        if (serviceInfo.getServicePackageFolder() == null) {
            serviceInfo.setServicePackageFolder(parent.getServicePackageFolder());
        }
        if (serviceInfo.getServiceUpgradesFolder() == null) {
            serviceInfo.setServiceUpgradesFolder(parent.getServiceUpgradesFolder());
        }
        if (serviceInfo.getMetricsFile() == null) {
            serviceInfo.setMetricsFile(parent.getMetricsFile());
        }
        if (serviceInfo.getAlertsFile() == null) {
            serviceInfo.setAlertsFile(parent.getAlertsFile());
        }
        if (serviceInfo.getKerberosDescriptorFile() == null) {
            serviceInfo.setKerberosDescriptorFile(parent.getKerberosDescriptorFile());
        }
        if (serviceInfo.getThemesMap().isEmpty()) {
            serviceInfo.setThemesMap(parent.getThemesMap());
        }
        if (serviceInfo.getWidgetsDescriptorFile() == null) {
            serviceInfo.setWidgetsDescriptorFile(parent.getWidgetsDescriptorFile());
        }
        if (serviceInfo.getAdvisorFile() == null) {
            serviceInfo.setAdvisorFile(parent.getAdvisorFile());
        }
        if (serviceInfo.getAdvisorName() == null) {
            serviceInfo.setAdvisorName(parent.getAdvisorName());
        }
        if (serviceInfo.getRoleCommandOrder() == null) {
            serviceInfo.setRoleCommandOrder(parent.getRoleCommandOrder());
        }
        if (serviceInfo.getChecksFolder() == null) {
            serviceInfo.setChecksFolder(parent.getChecksFolder());
        }
        if (serviceInfo.getServerActionsFolder() == null) {
            serviceInfo.setServerActionsFolder(parent.getServerActionsFolder());
        }
        if (serviceInfo.getCredentialStoreInfo() == null) {
            serviceInfo.setCredentialStoreInfo(parent.getCredentialStoreInfo());
        }
        if (serviceInfo.getSingleSignOnInfo() == null) {
            serviceInfo.setSingleSignOnInfo(parent.getSingleSignOnInfo());
        }
        if (serviceInfo.isSelectionEmpty()) {
            serviceInfo.setSelection(parent.getSelection());
        }
        if (serviceInfo.isMaintainerEmpty()) {
            serviceInfo.setMaintainer(parent.getMaintainer());
        }
        if (null == serviceInfo.getSupportDeleteViaUIField()) {
            serviceInfo.setSupportDeleteViaUI(parent.isSupportDeleteViaUI());
        }
        mergeCustomCommands(parent.getCustomCommands(), serviceInfo.getCustomCommands());
        mergeConfigDependencies(parent);
        mergeComponents(parentModule, allStacks, commonServices, extensions);
        mergeConfigurations(parentModule, allStacks, commonServices, extensions);
        mergeThemes(parentModule, allStacks, commonServices, extensions);
        mergeQuickLinksConfigurations(parentModule, allStacks, commonServices, extensions);
        mergeExcludedConfigTypes(parent);
        mergeServiceProperties(parent.getServicePropertyList());
    }

    private void mergeServiceProperties(java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> other) {
        if (!other.isEmpty()) {
            java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> servicePropertyList = serviceInfo.getServicePropertyList();
            java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> servicePropertiesToAdd = com.google.common.collect.Lists.newArrayList();
            java.util.Set<java.lang.String> servicePropertyNames = com.google.common.collect.Sets.newTreeSet(com.google.common.collect.Iterables.transform(servicePropertyList, new com.google.common.base.Function<org.apache.ambari.server.state.ServicePropertyInfo, java.lang.String>() {
                @javax.annotation.Nullable
                @java.lang.Override
                public java.lang.String apply(org.apache.ambari.server.state.ServicePropertyInfo serviceProperty) {
                    return serviceProperty.getName();
                }
            }));
            for (org.apache.ambari.server.state.ServicePropertyInfo otherServiceProperty : other) {
                if (!servicePropertyNames.contains(otherServiceProperty.getName()))
                    servicePropertiesToAdd.add(otherServiceProperty);

            }
            java.util.List<org.apache.ambari.server.state.ServicePropertyInfo> mergedServicePropertyList = com.google.common.collect.ImmutableList.<org.apache.ambari.server.state.ServicePropertyInfo>builder().addAll(servicePropertyList).addAll(servicePropertiesToAdd).build();
            serviceInfo.setServicePropertyList(mergedServicePropertyList);
            validateServiceInfo();
        }
    }

    public void resolveCommonService(java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        if (!isCommonService) {
            throw new org.apache.ambari.server.AmbariException("Not a common service");
        }
        moduleState = org.apache.ambari.server.stack.ModuleState.VISITED;
        java.lang.String parentString = serviceInfo.getParent();
        if (parentString != null) {
            java.lang.String[] parentToks = parentString.split(org.apache.ambari.server.stack.StackManager.PATH_DELIMITER);
            if (parentToks.length != 3) {
                throw new org.apache.ambari.server.AmbariException((((("The common service '" + serviceInfo.getName()) + serviceInfo.getVersion()) + "' extends an invalid parent: '") + parentString) + "'");
            }
            if (parentToks[0].equalsIgnoreCase(org.apache.ambari.server.stack.StackManager.COMMON_SERVICES)) {
                java.lang.String baseServiceKey = (parentToks[1] + org.apache.ambari.server.stack.StackManager.PATH_DELIMITER) + parentToks[2];
                org.apache.ambari.server.stack.ServiceModule baseService = commonServices.get(baseServiceKey);
                org.apache.ambari.server.stack.ModuleState baseModuleState = baseService.getModuleState();
                if (baseModuleState == org.apache.ambari.server.stack.ModuleState.INIT) {
                    baseService.resolveCommonService(allStacks, commonServices, extensions);
                } else if (baseModuleState == org.apache.ambari.server.stack.ModuleState.VISITED) {
                    throw new org.apache.ambari.server.AmbariException("Cycle detected while parsing common service");
                }
                resolveExplicit(baseService, allStacks, commonServices, extensions);
            } else {
                throw new org.apache.ambari.server.AmbariException("Common service cannot inherit from a non common service");
            }
        }
        moduleState = org.apache.ambari.server.stack.ModuleState.RESOLVED;
    }

    @java.lang.Override
    public boolean isDeleted() {
        return serviceInfo.isDeleted();
    }

    @java.lang.Override
    public java.lang.String getId() {
        return serviceInfo.getName();
    }

    @java.lang.Override
    public void finalizeModule() {
        finalizeChildModules(configurationModules.values());
        finalizeChildModules(componentModules.values());
        finalizeConfiguration();
        if ((serviceInfo.getCommandScript() != null) && (!isDeleted())) {
            stackContext.registerServiceCheck(getId());
        }
    }

    private void populateComponentModules() {
        for (org.apache.ambari.server.state.ComponentInfo component : serviceInfo.getComponents()) {
            componentModules.put(component.getName(), new org.apache.ambari.server.stack.ComponentModule(component));
        }
    }

    private void populateConfigurationModules() {
        org.apache.ambari.server.stack.ConfigurationDirectory configDirectory = serviceDirectory.getConfigurationDirectory(serviceInfo.getConfigDir(), org.apache.ambari.server.stack.StackDirectory.SERVICE_PROPERTIES_FOLDER_NAME);
        if (configDirectory != null) {
            for (org.apache.ambari.server.stack.ConfigurationModule config : configDirectory.getConfigurationModules()) {
                org.apache.ambari.server.stack.ConfigurationInfo info = config.getModuleInfo();
                if (isValid()) {
                    setValid(config.isValid() && info.isValid());
                    if (!isValid()) {
                        addErrors(config.getErrors());
                        addErrors(info.getErrors());
                    }
                }
                serviceInfo.getProperties().addAll(info.getProperties());
                serviceInfo.setTypeAttributes(config.getConfigType(), info.getAttributes());
                configurationModules.put(config.getConfigType(), config);
            }
            for (java.lang.String excludedType : serviceInfo.getExcludedConfigTypes()) {
                if (!configurationModules.containsKey(excludedType)) {
                    org.apache.ambari.server.stack.ConfigurationInfo configInfo = new org.apache.ambari.server.stack.ConfigurationInfo(java.util.Collections.emptyList(), java.util.Collections.emptyMap());
                    org.apache.ambari.server.stack.ConfigurationModule config = new org.apache.ambari.server.stack.ConfigurationModule(excludedType, configInfo);
                    config.setDeleted(true);
                    configurationModules.put(excludedType, config);
                }
            }
        }
    }

    private void populateThemeModules() {
        if (serviceInfo.getThemesDir() == null) {
            serviceInfo.setThemesDir(org.apache.ambari.server.stack.StackDirectory.SERVICE_THEMES_FOLDER_NAME);
        }
        java.lang.String themesDir = (serviceDirectory.getAbsolutePath() + java.io.File.separator) + serviceInfo.getThemesDir();
        if (serviceInfo.getThemes() != null) {
            java.util.List<org.apache.ambari.server.state.ThemeInfo> themes = new java.util.ArrayList<>(serviceInfo.getThemes().size());
            for (org.apache.ambari.server.state.ThemeInfo themeInfo : serviceInfo.getThemes()) {
                java.io.File themeFile = new java.io.File((themesDir + java.io.File.separator) + themeInfo.getFileName());
                org.apache.ambari.server.stack.ThemeModule module = new org.apache.ambari.server.stack.ThemeModule(themeFile, themeInfo);
                if (module.isValid()) {
                    themeModules.put(module.getId(), module);
                    themes.add(themeInfo);
                } else {
                    org.apache.ambari.server.stack.ServiceModule.LOG.error("Invalid theme {} for service {}", themeInfo.getFileName(), serviceInfo.getName());
                }
            }
            serviceInfo.setThemes(themes);
        }
    }

    private void mergeThemes(org.apache.ambari.server.stack.ServiceModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<org.apache.ambari.server.stack.ThemeModule> mergedModules = mergeChildModules(allStacks, commonServices, extensions, themeModules, parent.themeModules);
        for (org.apache.ambari.server.stack.ThemeModule mergedModule : mergedModules) {
            themeModules.put(mergedModule.getId(), mergedModule);
            org.apache.ambari.server.state.ThemeInfo moduleInfo = mergedModule.getModuleInfo();
            if (!moduleInfo.isDeleted()) {
                serviceInfo.getThemesMap().put(moduleInfo.getFileName(), moduleInfo);
            } else {
                serviceInfo.getThemesMap().remove(moduleInfo.getFileName());
            }
        }
    }

    private void populateQuickLinksConfigurationModules() {
        if (serviceInfo.getQuickLinksConfigurationsDir() == null) {
            serviceInfo.setQuickLinksConfigurationsDir(org.apache.ambari.server.stack.StackDirectory.SERVICE_QUICKLINKS_CONFIGURATIONS_FOLDER_NAME);
        }
        java.lang.String quickLinksConfigurationsDir = (serviceDirectory.getAbsolutePath() + java.io.File.separator) + serviceInfo.getQuickLinksConfigurationsDir();
        if (serviceInfo.getQuickLinksConfigurations() != null) {
            for (org.apache.ambari.server.state.QuickLinksConfigurationInfo quickLinksConfigurationInfo : serviceInfo.getQuickLinksConfigurations()) {
                java.io.File file = new java.io.File((quickLinksConfigurationsDir + java.io.File.separator) + quickLinksConfigurationInfo.getFileName());
                org.apache.ambari.server.stack.QuickLinksConfigurationModule module = new org.apache.ambari.server.stack.QuickLinksConfigurationModule(file, quickLinksConfigurationInfo);
                quickLinksConfigurationModules.put(module.getId(), module);
            }
        }
    }

    private void mergeQuickLinksConfigurations(org.apache.ambari.server.stack.ServiceModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<org.apache.ambari.server.stack.QuickLinksConfigurationModule> mergedModules = mergeChildModules(allStacks, commonServices, extensions, quickLinksConfigurationModules, parent.quickLinksConfigurationModules);
        for (org.apache.ambari.server.stack.QuickLinksConfigurationModule mergedModule : mergedModules) {
            quickLinksConfigurationModules.put(mergedModule.getId(), mergedModule);
            org.apache.ambari.server.state.QuickLinksConfigurationInfo moduleInfo = mergedModule.getModuleInfo();
            if (!moduleInfo.isDeleted()) {
                serviceInfo.getQuickLinksConfigurationsMap().put(moduleInfo.getFileName(), moduleInfo);
            } else {
                serviceInfo.getQuickLinksConfigurationsMap().remove(moduleInfo.getFileName());
            }
        }
    }

    private void mergeExcludedConfigTypes(org.apache.ambari.server.state.ServiceInfo parent) {
        if (serviceInfo.getExcludedConfigTypes() == null) {
            serviceInfo.setExcludedConfigTypes(parent.getExcludedConfigTypes());
        } else if (parent.getExcludedConfigTypes() != null) {
            java.util.Set<java.lang.String> resultExcludedConfigTypes = serviceInfo.getExcludedConfigTypes();
            for (java.lang.String excludedType : parent.getExcludedConfigTypes()) {
                if (!resultExcludedConfigTypes.contains(excludedType)) {
                    resultExcludedConfigTypes.add(excludedType);
                }
            }
            serviceInfo.setExcludedConfigTypes(resultExcludedConfigTypes);
        }
    }

    private void mergeConfigDependencies(org.apache.ambari.server.state.ServiceInfo parent) {
        java.util.List<java.lang.String> configDependencies = serviceInfo.getConfigDependencies();
        java.util.List<java.lang.String> parentConfigDependencies = (parent.getConfigDependencies() != null) ? parent.getConfigDependencies() : java.util.Collections.emptyList();
        if (configDependencies == null) {
            serviceInfo.setConfigDependencies(parentConfigDependencies);
        } else {
            for (java.lang.String parentDependency : parentConfigDependencies) {
                if (!configDependencies.contains(parentDependency)) {
                    configDependencies.add(parentDependency);
                }
            }
        }
    }

    private void mergeConfigurations(org.apache.ambari.server.stack.ServiceModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        serviceInfo.getProperties().clear();
        serviceInfo.setAllConfigAttributes(new java.util.HashMap<>());
        java.util.Collection<org.apache.ambari.server.stack.ConfigurationModule> mergedModules = mergeChildModules(allStacks, commonServices, extensions, configurationModules, parent.configurationModules);
        for (org.apache.ambari.server.stack.ConfigurationModule module : mergedModules) {
            configurationModules.put(module.getId(), module);
            if (!module.isDeleted()) {
                serviceInfo.getProperties().addAll(module.getModuleInfo().getProperties());
                serviceInfo.setTypeAttributes(module.getConfigType(), module.getModuleInfo().getAttributes());
            }
        }
    }

    private void mergeComponents(org.apache.ambari.server.stack.ServiceModule parent, java.util.Map<java.lang.String, org.apache.ambari.server.stack.StackModule> allStacks, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ServiceModule> commonServices, java.util.Map<java.lang.String, org.apache.ambari.server.stack.ExtensionModule> extensions) throws org.apache.ambari.server.AmbariException {
        serviceInfo.getComponents().clear();
        java.util.Collection<org.apache.ambari.server.stack.ComponentModule> mergedModules = mergeChildModules(allStacks, commonServices, extensions, componentModules, parent.componentModules);
        componentModules.clear();
        for (org.apache.ambari.server.stack.ComponentModule module : mergedModules) {
            if (!module.isDeleted()) {
                componentModules.put(module.getId(), module);
                serviceInfo.getComponents().add(module.getModuleInfo());
            }
        }
    }

    private void mergeCustomCommands(java.util.Collection<org.apache.ambari.server.state.CustomCommandDefinition> parentCmds, java.util.Collection<org.apache.ambari.server.state.CustomCommandDefinition> childCmds) {
        java.util.Collection<java.lang.String> existingNames = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.CustomCommandDefinition childCmd : childCmds) {
            existingNames.add(childCmd.getName());
        }
        for (org.apache.ambari.server.state.CustomCommandDefinition parentCmd : parentCmds) {
            if (!existingNames.contains(parentCmd.getName())) {
                childCmds.add(parentCmd);
            }
        }
    }

    private void finalizeConfiguration() {
        org.apache.ambari.server.stack.ServiceModule.LOG.debug("Finalize config, number of configuration modules {}", configurationModules.size());
        hasConfigs = !configurationModules.isEmpty();
        org.apache.ambari.server.stack.ServiceModule.LOG.debug("Finalize config, hasConfigs {}", hasConfigs);
        for (org.apache.ambari.server.stack.ConfigurationModule config : configurationModules.values()) {
            org.apache.ambari.server.stack.ConfigurationInfo configInfo = config.getModuleInfo();
            configInfo.ensureDefaultAttributes();
            serviceInfo.setTypeAttributes(config.getConfigType(), configInfo.getAttributes());
        }
    }

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    public org.apache.ambari.server.stack.ServiceDirectory getServiceDirectory() {
        return serviceDirectory;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }

    private void validateServiceInfo() {
        if (!serviceInfo.isValid()) {
            setValid(false);
            addErrors(serviceInfo.getErrors());
        }
    }

    public boolean hasConfigs() {
        return hasConfigs;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}