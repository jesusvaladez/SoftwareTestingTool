package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.agent.ExecutionCommand.KeyNames.PACKAGE_LIST;
public class ClientConfigResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    protected static final java.lang.String COMPONENT_CLUSTER_NAME_PROPERTY_ID = "ServiceComponentInfo/cluster_name";

    protected static final java.lang.String COMPONENT_SERVICE_NAME_PROPERTY_ID = "ServiceComponentInfo/service_name";

    protected static final java.lang.String COMPONENT_COMPONENT_NAME_PROPERTY_ID = "ServiceComponentInfo/component_name";

    protected static final java.lang.String HOST_COMPONENT_HOST_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name");

    private final com.google.gson.Gson gson;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Service, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Component, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.HOST_COMPONENT_HOST_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.HOST_COMPONENT_HOST_NAME_PROPERTY_ID);

    private org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.class);

    @com.google.inject.assistedinject.AssistedInject
    ClientConfigResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.keyPropertyIds, managementController);
        gson = new com.google.gson.Gson();
        setRequiredGetAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS));
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("The request is not supported");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> responses = null;
        try {
            responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse>>() {
                @java.lang.Override
                public java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> invoke() throws org.apache.ambari.server.AmbariException {
                    return getManagementController().getHostComponents(requests);
                }
            });
        } catch (java.lang.Exception e) {
            throw new org.apache.ambari.server.controller.spi.SystemException("Failed to get components ", e);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.ServiceComponentHostResponse> componentMap = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.ServiceComponentHostResponse resp : responses) {
            java.lang.String componentName = resp.getComponentName();
            if (!componentMap.containsKey(componentName)) {
                componentMap.put(resp.getComponentName(), resp);
            }
        }
        org.apache.ambari.server.controller.ServiceComponentHostRequest schRequest = requests.iterator().next();
        java.lang.String requestComponentName = schRequest.getComponentName();
        java.lang.String requestServiceName = schRequest.getServiceName();
        java.lang.String requestHostName = schRequest.getHostname();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.controller.ServiceComponentHostResponse>> serviceToComponentMap = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.controller.ServiceComponentHostResponse> schWithConfigFiles = new java.util.ArrayList<>();
        org.apache.ambari.server.configuration.Configuration configs = new org.apache.ambari.server.configuration.Configuration();
        java.util.Map<java.lang.String, java.lang.String> configMap = configs.getConfigsMap();
        java.lang.String TMP_PATH = configMap.get(org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR.getKey());
        java.lang.String pythonCmd = configMap.get(org.apache.ambari.server.configuration.Configuration.AMBARI_PYTHON_WRAP.getKey());
        java.util.List<java.lang.String> pythonCompressFilesCmds = new java.util.ArrayList<>();
        java.util.List<java.io.File> commandFiles = new java.util.ArrayList<>();
        for (org.apache.ambari.server.controller.ServiceComponentHostResponse response : componentMap.values()) {
            org.apache.ambari.server.controller.AmbariManagementController managementController = getManagementController();
            org.apache.ambari.server.state.ConfigHelper configHelper = managementController.getConfigHelper();
            org.apache.ambari.server.state.Cluster cluster = null;
            org.apache.ambari.server.state.Clusters clusters = managementController.getClusters();
            try {
                cluster = clusters.getCluster(response.getClusterName());
                java.lang.String serviceName = response.getServiceName();
                java.lang.String componentName = response.getComponentName();
                java.lang.String hostName = response.getHostname();
                java.lang.String publicHostName = response.getPublicHostname();
                org.apache.ambari.server.state.ComponentInfo componentInfo = null;
                java.lang.String packageFolder = null;
                org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                org.apache.ambari.server.state.ServiceComponent component = service.getServiceComponent(componentName);
                org.apache.ambari.server.state.StackId stackId = component.getDesiredStackId();
                componentInfo = managementController.getAmbariMetaInfo().getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceName, componentName);
                packageFolder = managementController.getAmbariMetaInfo().getService(stackId.getStackName(), stackId.getStackVersion(), serviceName).getServicePackageFolder();
                java.lang.String commandScript = componentInfo.getCommandScript().getScript();
                java.util.List<org.apache.ambari.server.state.ClientConfigFileDefinition> clientConfigFiles = componentInfo.getClientConfigFiles();
                if (clientConfigFiles == null) {
                    if (componentMap.size() == 1) {
                        throw new org.apache.ambari.server.controller.spi.SystemException("No configuration files defined for the component " + componentInfo.getName());
                    } else {
                        org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LOG.debug("No configuration files defined for the component {}", componentInfo.getName());
                        continue;
                    }
                }
                schWithConfigFiles.add(response);
                if (serviceToComponentMap.containsKey(response.getServiceName())) {
                    java.util.List<org.apache.ambari.server.controller.ServiceComponentHostResponse> schResponseList = serviceToComponentMap.get(serviceName);
                    schResponseList.add(response);
                } else {
                    java.util.List<org.apache.ambari.server.controller.ServiceComponentHostResponse> schResponseList = new java.util.ArrayList<>();
                    schResponseList.add(response);
                    serviceToComponentMap.put(serviceName, schResponseList);
                }
                java.lang.String resourceDirPath = configs.getResourceDirPath();
                java.lang.String packageFolderAbsolute = (resourceDirPath + java.io.File.separator) + packageFolder;
                java.lang.String commandScriptAbsolute = (packageFolderAbsolute + java.io.File.separator) + commandScript;
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.TreeMap<>();
                java.util.Map<java.lang.String, java.lang.Long> configVersions = new java.util.TreeMap<>();
                java.util.Map<java.lang.String, java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>>> configPropertiesTypes = new java.util.TreeMap<>();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes = new java.util.TreeMap<>();
                java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredClusterConfigs = cluster.getDesiredConfigs();
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigEntry : desiredClusterConfigs.entrySet()) {
                    java.lang.String configType = desiredConfigEntry.getKey();
                    org.apache.ambari.server.state.DesiredConfig desiredConfig = desiredConfigEntry.getValue();
                    org.apache.ambari.server.state.Config clusterConfig = cluster.getConfig(configType, desiredConfig.getTag());
                    if (clusterConfig != null) {
                        java.util.Map<java.lang.String, java.lang.String> props = new java.util.HashMap<>(clusterConfig.getProperties());
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> allConfigTags = null;
                        allConfigTags = configHelper.getEffectiveDesiredTags(cluster, schRequest.getHostname());
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags = new java.util.HashMap<>();
                        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : allConfigTags.entrySet()) {
                            if (entry.getKey().equals(clusterConfig.getType())) {
                                configTags.put(clusterConfig.getType(), entry.getValue());
                            }
                        }
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configHelper.getEffectiveConfigProperties(cluster, configTags);
                        if (!properties.isEmpty()) {
                            for (java.util.Map<java.lang.String, java.lang.String> propertyMap : properties.values()) {
                                props.putAll(propertyMap);
                            }
                        }
                        configurations.put(clusterConfig.getType(), props);
                        configVersions.put(clusterConfig.getType(), clusterConfig.getVersion());
                        configPropertiesTypes.put(clusterConfig.getType(), clusterConfig.getPropertiesTypes());
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attrs = new java.util.TreeMap<>();
                        configHelper.cloneAttributesMap(clusterConfig.getPropertiesAttributes(), attrs);
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = configHelper.getEffectiveConfigAttributes(cluster, configTags);
                        for (java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributesMap : attributes.values()) {
                            configHelper.cloneAttributesMap(attributesMap, attrs);
                        }
                        configurationAttributes.put(clusterConfig.getType(), attrs);
                    }
                }
                org.apache.ambari.server.state.ConfigHelper.processHiddenAttribute(configurations, configurationAttributes, componentName, true);
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributesEntry : configurationAttributes.entrySet()) {
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attrs = configurationAttributesEntry.getValue();
                    attrs.remove("hidden");
                }
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configEntry : configurations.entrySet()) {
                    java.lang.String configType = configEntry.getKey();
                    java.util.Map<java.lang.String, java.lang.String> configProperties = configEntry.getValue();
                    java.lang.Long configVersion = configVersions.get(configType);
                    java.util.Map<org.apache.ambari.server.state.PropertyInfo.PropertyType, java.util.Set<java.lang.String>> propertiesTypes = configPropertiesTypes.get(configType);
                    org.apache.ambari.server.utils.SecretReference.replacePasswordsWithReferences(propertiesTypes, configProperties, configType, configVersion);
                }
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = null;
                org.apache.ambari.server.state.ServiceInfo serviceInfo = null;
                java.lang.String osFamily = null;
                clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getClusterHostInfo(cluster);
                serviceInfo = managementController.getAmbariMetaInfo().getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
                try {
                    clusterHostInfo = org.apache.ambari.server.utils.StageUtils.substituteHostIndexes(clusterHostInfo);
                } catch (org.apache.ambari.server.AmbariException e) {
                    throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
                }
                osFamily = clusters.getHost(hostName).getOsFamily();
                org.apache.ambari.server.state.ServiceOsSpecific anyOs = null;
                if (serviceInfo.getOsSpecifics().containsKey(org.apache.ambari.server.api.services.AmbariMetaInfo.ANY_OS)) {
                    anyOs = serviceInfo.getOsSpecifics().get(org.apache.ambari.server.api.services.AmbariMetaInfo.ANY_OS);
                }
                org.apache.ambari.server.state.ServiceOsSpecific hostOs = populateServicePackagesInfo(serviceInfo, osFamily);
                java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = new java.util.ArrayList<>();
                if (anyOs != null) {
                    packages.addAll(anyOs.getPackages());
                }
                if (hostOs != null) {
                    packages.addAll(hostOs.getPackages());
                }
                java.lang.String packageList = gson.toJson(packages);
                java.lang.String jsonConfigurations = null;
                java.util.Map<java.lang.String, java.lang.Object> commandParams = new java.util.HashMap<>();
                java.util.List<java.util.Map<java.lang.String, java.lang.String>> xmlConfigs = new java.util.LinkedList<>();
                java.util.List<java.util.Map<java.lang.String, java.lang.String>> envConfigs = new java.util.LinkedList<>();
                java.util.List<java.util.Map<java.lang.String, java.lang.String>> propertiesConfigs = new java.util.LinkedList<>();
                for (org.apache.ambari.server.state.ClientConfigFileDefinition clientConfigFile : clientConfigFiles) {
                    java.util.Map<java.lang.String, java.lang.String> fileDict = new java.util.HashMap<>();
                    fileDict.put(clientConfigFile.getFileName(), clientConfigFile.getDictionaryName());
                    if (clientConfigFile.getType().equals("xml")) {
                        xmlConfigs.add(fileDict);
                    } else if (clientConfigFile.getType().equals("env")) {
                        envConfigs.add(fileDict);
                    } else if (clientConfigFile.getType().equals("properties")) {
                        propertiesConfigs.add(fileDict);
                    }
                }
                java.util.TreeMap<java.lang.String, java.lang.String> clusterLevelParams = null;
                java.util.TreeMap<java.lang.String, java.lang.String> ambariLevelParams = null;
                java.util.TreeMap<java.lang.String, java.lang.String> topologyCommandParams = new java.util.TreeMap<>();
                if (getManagementController() instanceof org.apache.ambari.server.controller.AmbariManagementControllerImpl) {
                    org.apache.ambari.server.controller.AmbariManagementControllerImpl controller = ((org.apache.ambari.server.controller.AmbariManagementControllerImpl) (getManagementController()));
                    clusterLevelParams = controller.getMetadataClusterLevelParams(cluster, stackId);
                    ambariLevelParams = controller.getMetadataAmbariLevelParams();
                    org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
                    org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent(componentName);
                    org.apache.ambari.server.state.ServiceComponentHost sch = sc.getServiceComponentHost(response.getHostname());
                    topologyCommandParams = controller.getTopologyCommandParams(cluster.getClusterId(), serviceName, componentName, sch);
                }
                java.util.TreeMap<java.lang.String, java.lang.String> agentLevelParams = new java.util.TreeMap<>();
                agentLevelParams.put("hostname", hostName);
                agentLevelParams.put("public_hostname", publicHostName);
                commandParams.put(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.PACKAGE_LIST, packageList);
                commandParams.put("xml_configs_list", xmlConfigs);
                commandParams.put("env_configs_list", envConfigs);
                commandParams.put("properties_configs_list", propertiesConfigs);
                commandParams.put("output_file", (componentName + "-configs") + org.apache.ambari.server.configuration.Configuration.DEF_ARCHIVE_EXTENSION);
                commandParams.putAll(topologyCommandParams);
                java.util.Map<java.lang.String, java.lang.Object> jsonContent = new java.util.TreeMap<>();
                jsonContent.put("configurations", configurations);
                jsonContent.put("configurationAttributes", configurationAttributes);
                jsonContent.put("commandParams", commandParams);
                jsonContent.put("clusterHostInfo", clusterHostInfo);
                jsonContent.put("ambariLevelParams", ambariLevelParams);
                jsonContent.put("clusterLevelParams", clusterLevelParams);
                jsonContent.put("agentLevelParams", agentLevelParams);
                jsonContent.put("hostname", hostName);
                jsonContent.put("public_hostname", publicHostName);
                jsonContent.put("clusterName", cluster.getClusterName());
                jsonContent.put("serviceName", serviceName);
                jsonContent.put("role", componentName);
                jsonContent.put("componentVersionMap", cluster.getComponentVersionMap());
                jsonConfigurations = gson.toJson(jsonContent);
                java.io.File tmpDirectory = new java.io.File(TMP_PATH);
                if (!tmpDirectory.exists()) {
                    try {
                        tmpDirectory.mkdirs();
                        tmpDirectory.setWritable(true, true);
                        tmpDirectory.setReadable(true, true);
                    } catch (java.lang.SecurityException se) {
                        throw new org.apache.ambari.server.controller.spi.SystemException("Failed to get temporary directory to store configurations", se);
                    }
                }
                java.io.File jsonFile = java.io.File.createTempFile(componentName, "-configuration.json", tmpDirectory);
                try {
                    jsonFile.setWritable(true, true);
                    jsonFile.setReadable(true, true);
                } catch (java.lang.SecurityException e) {
                    throw new org.apache.ambari.server.controller.spi.SystemException("Failed to set permission", e);
                }
                java.io.PrintWriter printWriter = null;
                try {
                    printWriter = new java.io.PrintWriter(jsonFile.getAbsolutePath());
                    printWriter.print(jsonConfigurations);
                    printWriter.close();
                } catch (java.io.FileNotFoundException e) {
                    throw new org.apache.ambari.server.controller.spi.SystemException("Failed to write configurations to json file ", e);
                }
                java.lang.String cmd = (((((((((((pythonCmd + " ") + commandScriptAbsolute) + " generate_configs ") + jsonFile.getAbsolutePath()) + " ") + packageFolderAbsolute) + " ") + TMP_PATH) + java.io.File.separator) + "structured-out.json") + " INFO ") + TMP_PATH;
                commandFiles.add(jsonFile);
                pythonCompressFilesCmds.add(cmd);
            } catch (java.io.IOException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException("Controller error ", e);
            }
        }
        if (schWithConfigFiles.isEmpty()) {
            throw new org.apache.ambari.server.controller.spi.SystemException("No configuration files defined for any component");
        }
        java.lang.Integer totalCommands = pythonCompressFilesCmds.size() * 2;
        java.lang.Integer threadPoolSize = java.lang.Math.min(totalCommands, configs.getExternalScriptThreadPoolSize());
        java.util.concurrent.ExecutorService processExecutor = java.util.concurrent.Executors.newFixedThreadPool(threadPoolSize);
        try {
            java.util.List<org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThreadWrapper> pythonCmdThreads = executeCommands(processExecutor, pythonCompressFilesCmds);
            java.lang.Integer timeout = configs.getExternalScriptTimeout();
            waitForAllThreadsToJoin(processExecutor, pythonCmdThreads, timeout);
        } finally {
            for (java.io.File each : commandFiles) {
                each.delete();
            }
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(requestComponentName)) {
            org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.TarUtils tarUtils;
            java.lang.String fileName;
            java.util.List<org.apache.ambari.server.controller.ServiceComponentHostResponse> schToTarConfigFiles = schWithConfigFiles;
            if (org.apache.commons.lang.StringUtils.isNotEmpty(requestHostName)) {
                fileName = ((requestHostName + "(") + org.apache.ambari.server.controller.spi.Resource.InternalType.Host.toString().toUpperCase()) + ")";
            } else if (org.apache.commons.lang.StringUtils.isNotEmpty(requestServiceName)) {
                fileName = ((requestServiceName + "(") + org.apache.ambari.server.controller.spi.Resource.InternalType.Service.toString().toUpperCase()) + ")";
                schToTarConfigFiles = serviceToComponentMap.get(requestServiceName);
            } else {
                fileName = ((schRequest.getClusterName() + "(") + org.apache.ambari.server.controller.spi.Resource.InternalType.Cluster.toString().toUpperCase()) + ")";
            }
            tarUtils = new org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.TarUtils(TMP_PATH, fileName, schToTarConfigFiles);
            tarUtils.tarConfigFiles();
        }
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig);
        resources.add(resource);
        return resources;
    }

    private java.util.List<org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThreadWrapper> executeCommands(final java.util.concurrent.ExecutorService processExecutor, java.util.List<java.lang.String> commandLines) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.List<org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThreadWrapper> commandLineThreadWrappers = new java.util.ArrayList<>();
        try {
            for (java.lang.String commandLine : commandLines) {
                org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThreadWrapper commandLineThreadWrapper = executeCommand(processExecutor, commandLine);
                commandLineThreadWrappers.add(commandLineThreadWrapper);
            }
        } catch (java.io.IOException e) {
            org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LOG.error("Failed to run generate client configs script for components");
            processExecutor.shutdownNow();
            throw new org.apache.ambari.server.controller.spi.SystemException("Failed to run generate client configs script for components");
        }
        return commandLineThreadWrappers;
    }

    private org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThreadWrapper executeCommand(final java.util.concurrent.ExecutorService processExecutor, final java.lang.String commandLine) throws java.io.IOException {
        java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(java.util.Arrays.asList(commandLine.split("\\s+")));
        builder.redirectErrorStream(true);
        java.lang.Process process = builder.start();
        org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThread commandLineThread = new org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThread(process);
        org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LogStreamReader logStream = new org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LogStreamReader(process.getInputStream());
        java.lang.Thread logStreamThread = new java.lang.Thread(logStream, "LogStreamReader");
        processExecutor.execute(logStreamThread);
        processExecutor.execute(commandLineThread);
        return new org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThreadWrapper(commandLine, commandLineThread, logStreamThread, logStream, process);
    }

    private void waitForAllThreadsToJoin(java.util.concurrent.ExecutorService processExecutor, java.util.List<org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThreadWrapper> pythonCmdThreads, java.lang.Integer timeout) throws org.apache.ambari.server.controller.spi.SystemException {
        processExecutor.shutdown();
        try {
            processExecutor.awaitTermination(timeout, java.util.concurrent.TimeUnit.MILLISECONDS);
            processExecutor.shutdownNow();
            for (org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThreadWrapper commandLineThreadWrapper : pythonCmdThreads) {
                org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThread commandLineThread = commandLineThreadWrapper.getCommandLineThread();
                try {
                    java.lang.Integer returnCode = commandLineThread.getReturnCode();
                    if (returnCode == null) {
                        throw new java.util.concurrent.TimeoutException();
                    } else if (returnCode != 0) {
                        throw new java.util.concurrent.ExecutionException(java.lang.String.format("Execution of \"%s\" returned %d.", commandLineThreadWrapper.getCommandLine(), returnCode), new java.lang.Throwable(commandLineThreadWrapper.getLogStream().getOutput()));
                    }
                } catch (java.util.concurrent.TimeoutException e) {
                    org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LOG.error("Generate client configs script was killed due to timeout ", e);
                    throw new org.apache.ambari.server.controller.spi.SystemException("Generate client configs script was killed due to timeout ", e);
                } catch (java.util.concurrent.ExecutionException e) {
                    org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LOG.error(e.getMessage(), e);
                    throw new org.apache.ambari.server.controller.spi.SystemException((e.getMessage() + " ") + e.getCause());
                } finally {
                    commandLineThreadWrapper.getProcess().destroy();
                }
            }
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            processExecutor.shutdownNow();
            org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LOG.error("Failed to run generate client configs script for components");
            throw new org.apache.ambari.server.controller.spi.SystemException("Failed to run generate client configs script for components");
        }
    }

    private static class CommandLineThreadWrapper {
        private java.lang.String commandLine;

        private org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThread commandLineThread;

        private java.lang.Thread logStreamThread;

        private org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LogStreamReader logStream;

        private java.lang.Process process;

        private CommandLineThreadWrapper(java.lang.String commandLine, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThread commandLineThread, java.lang.Thread logStreamThread, org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LogStreamReader logStream, java.lang.Process process) {
            this.commandLine = commandLine;
            this.commandLineThread = commandLineThread;
            this.logStreamThread = logStreamThread;
            this.logStream = logStream;
            this.process = process;
        }

        private java.lang.String getCommandLine() {
            return commandLine;
        }

        private void setCommandLine(java.lang.String commandLine) {
            this.commandLine = commandLine;
        }

        private org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThread getCommandLineThread() {
            return commandLineThread;
        }

        private void setCommandLineThread(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.CommandLineThread commandLineThread) {
            this.commandLineThread = commandLineThread;
        }

        private java.lang.Thread getLogStreamThread() {
            return logStreamThread;
        }

        private void setLogStreamThread(java.lang.Thread logStreamThread) {
            this.logStreamThread = logStreamThread;
        }

        private org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LogStreamReader getLogStream() {
            return logStream;
        }

        private void setLogStream(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LogStreamReader logStream) {
            this.logStream = logStream;
        }

        private java.lang.Process getProcess() {
            return process;
        }

        private void setProcess(java.lang.Process process) {
            this.process = process;
        }
    }

    private static class CommandLineThread extends java.lang.Thread {
        private final java.lang.Process process;

        private java.lang.Integer returnCode;

        private void setReturnCode(java.lang.Integer exit) {
            returnCode = exit;
        }

        private java.lang.Integer getReturnCode() {
            return returnCode;
        }

        private CommandLineThread(java.lang.Process process) {
            this.process = process;
        }

        @java.lang.Override
        public void run() {
            try {
                setReturnCode(process.waitFor());
            } catch (java.lang.InterruptedException ignore) {
                return;
            }
        }
    }

    private class LogStreamReader implements java.lang.Runnable {
        private java.io.BufferedReader reader;

        private java.lang.StringBuilder output;

        public LogStreamReader(java.io.InputStream is) {
            reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
            output = new java.lang.StringBuilder("");
        }

        public java.lang.String getOutput() {
            return output.toString();
        }

        @java.lang.Override
        public void run() {
            try {
                java.lang.String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    output.append("\n");
                    line = reader.readLine();
                } 
                reader.close();
            } catch (java.io.IOException e) {
                org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.LOG.warn(e.getMessage());
            }
        }
    }

    protected static class TarUtils {
        private java.lang.String tmpDir;

        private java.lang.String fileName;

        private java.util.List<org.apache.ambari.server.controller.ServiceComponentHostResponse> serviceComponentHostResponses;

        TarUtils(java.lang.String tmpDir, java.lang.String fileName, java.util.List<org.apache.ambari.server.controller.ServiceComponentHostResponse> serviceComponentHostResponses) {
            this.tmpDir = tmpDir;
            this.fileName = fileName;
            this.serviceComponentHostResponses = serviceComponentHostResponses;
        }

        protected void tarConfigFiles() throws org.apache.ambari.server.controller.spi.SystemException {
            try {
                java.io.File compressedOutputFile = new java.io.File(tmpDir, (fileName + "-configs") + org.apache.ambari.server.configuration.Configuration.DEF_ARCHIVE_EXTENSION);
                java.io.FileOutputStream fOut = new java.io.FileOutputStream(compressedOutputFile);
                java.io.BufferedOutputStream bOut = new java.io.BufferedOutputStream(fOut);
                org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream gzOut = new org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream(bOut);
                org.apache.commons.compress.archivers.tar.TarArchiveOutputStream tOut = new org.apache.commons.compress.archivers.tar.TarArchiveOutputStream(gzOut);
                tOut.setLongFileMode(org.apache.commons.compress.archivers.tar.TarArchiveOutputStream.LONGFILE_POSIX);
                tOut.setBigNumberMode(org.apache.commons.compress.archivers.tar.TarArchiveOutputStream.BIGNUMBER_POSIX);
                try {
                    for (org.apache.ambari.server.controller.ServiceComponentHostResponse schResponse : serviceComponentHostResponses) {
                        java.lang.String componentName = schResponse.getComponentName();
                        java.io.File compressedInputFile = new java.io.File(tmpDir, (componentName + "-configs") + org.apache.ambari.server.configuration.Configuration.DEF_ARCHIVE_EXTENSION);
                        java.io.FileInputStream fin = new java.io.FileInputStream(compressedInputFile);
                        java.io.BufferedInputStream bIn = new java.io.BufferedInputStream(fin);
                        org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream gzIn = new org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream(bIn);
                        org.apache.commons.compress.archivers.tar.TarArchiveInputStream tarIn = new org.apache.commons.compress.archivers.tar.TarArchiveInputStream(gzIn);
                        org.apache.commons.compress.archivers.tar.TarArchiveEntry entry = null;
                        try {
                            while ((entry = tarIn.getNextTarEntry()) != null) {
                                entry.setName((componentName + java.io.File.separator) + entry.getName());
                                tOut.putArchiveEntry(entry);
                                if (entry.isFile()) {
                                    org.apache.commons.compress.utils.IOUtils.copy(tarIn, tOut);
                                }
                                tOut.closeArchiveEntry();
                            } 
                        } catch (java.lang.Exception e) {
                            throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
                        } finally {
                            tarIn.close();
                            gzIn.close();
                            bIn.close();
                            fin.close();
                        }
                    }
                } finally {
                    tOut.finish();
                    tOut.close();
                }
            } catch (java.lang.Exception e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("The request is not supported");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException("The request is not supported");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.keyPropertyIds.values());
    }

    private org.apache.ambari.server.controller.ServiceComponentHostRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.ServiceComponentHostRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_CLUSTER_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_SERVICE_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.COMPONENT_COMPONENT_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClientConfigResourceProvider.HOST_COMPONENT_HOST_NAME_PROPERTY_ID))), null);
    }

    protected org.apache.ambari.server.state.ServiceOsSpecific populateServicePackagesInfo(org.apache.ambari.server.state.ServiceInfo serviceInfo, java.lang.String osFamily) {
        org.apache.ambari.server.state.ServiceOsSpecific hostOs = new org.apache.ambari.server.state.ServiceOsSpecific(osFamily);
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific> foundedOSSpecifics = getOSSpecificsByFamily(serviceInfo.getOsSpecifics(), osFamily);
        if (!foundedOSSpecifics.isEmpty()) {
            for (org.apache.ambari.server.state.ServiceOsSpecific osSpecific : foundedOSSpecifics) {
                hostOs.addPackages(osSpecific.getPackages());
            }
        }
        return hostOs;
    }

    private java.util.List<org.apache.ambari.server.state.ServiceOsSpecific> getOSSpecificsByFamily(java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> osSpecifics, java.lang.String osFamily) {
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific> foundedOSSpecifics = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific> osSpecific : osSpecifics.entrySet()) {
            if (osSpecific.getKey().indexOf(osFamily) != (-1)) {
                foundedOSSpecifics.add(osSpecific.getValue());
            }
        }
        return foundedOSSpecifics;
    }
}