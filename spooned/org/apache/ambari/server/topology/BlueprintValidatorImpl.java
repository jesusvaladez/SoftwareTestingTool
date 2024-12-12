package org.apache.ambari.server.topology;
@org.apache.ambari.server.StaticallyInject
public class BlueprintValidatorImpl implements org.apache.ambari.server.topology.BlueprintValidator {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.BlueprintValidatorImpl.class);

    private final org.apache.ambari.server.topology.Blueprint blueprint;

    private final org.apache.ambari.server.controller.internal.Stack stack;

    public static final java.lang.String LZO_CODEC_CLASS_PROPERTY_NAME = "io.compression.codec.lzo.class";

    public static final java.lang.String CODEC_CLASSES_PROPERTY_NAME = "io.compression.codecs";

    public static final java.lang.String LZO_CODEC_CLASS = "com.hadoop.compression.lzo.LzoCodec";

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration configuration;

    public BlueprintValidatorImpl(org.apache.ambari.server.topology.Blueprint blueprint) {
        this.blueprint = blueprint;
        this.stack = blueprint.getStack();
    }

    @java.lang.Override
    public void validateTopology() throws org.apache.ambari.server.topology.InvalidTopologyException {
        org.apache.ambari.server.topology.BlueprintValidatorImpl.LOGGER.info("Validating topology for blueprint: [{}]", blueprint.getName());
        java.util.Collection<org.apache.ambari.server.topology.HostGroup> hostGroups = blueprint.getHostGroups().values();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.DependencyInfo>>> dependenciesValidationIssues = new java.util.HashMap<>();
        for (org.apache.ambari.server.topology.HostGroup group : hostGroups) {
            java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.DependencyInfo>> groupDependenciesValidationIssues = validateHostGroup(group, "inclusive");
            groupDependenciesValidationIssues.putAll(validateHostGroup(group, "exclusive"));
            if (!groupDependenciesValidationIssues.isEmpty()) {
                dependenciesValidationIssues.put(group.getName(), groupDependenciesValidationIssues);
            }
        }
        java.util.Collection<java.lang.String> cardinalityFailures = new java.util.HashSet<>();
        java.util.Collection<java.lang.String> services = blueprint.getServices();
        for (java.lang.String service : services) {
            for (java.lang.String component : stack.getComponents(service)) {
                org.apache.ambari.server.topology.Cardinality cardinality = stack.getCardinality(component);
                org.apache.ambari.server.state.AutoDeployInfo autoDeploy = stack.getAutoDeployInfo(component);
                if (cardinality.isAll()) {
                    cardinalityFailures.addAll(verifyComponentInAllHostGroups(component, autoDeploy));
                } else {
                    cardinalityFailures.addAll(verifyComponentCardinalityCount(component, cardinality, autoDeploy));
                }
            }
        }
        if ((!dependenciesValidationIssues.isEmpty()) || (!cardinalityFailures.isEmpty())) {
            generateInvalidTopologyException(dependenciesValidationIssues, cardinalityFailures);
        }
    }

    @java.lang.Override
    public void validateRequiredProperties() throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.GPLLicenseNotAcceptedException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigurations = blueprint.getConfiguration().getProperties();
        if (clusterConfigurations != null) {
            boolean gplEnabled = org.apache.ambari.server.topology.BlueprintValidatorImpl.configuration.getGplLicenseAccepted();
            java.lang.StringBuilder errorMessage = new java.lang.StringBuilder();
            boolean containsSecretReferences = false;
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configEntry : clusterConfigurations.entrySet()) {
                java.lang.String configType = configEntry.getKey();
                if (configEntry.getValue() != null) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyEntry : configEntry.getValue().entrySet()) {
                        java.lang.String propertyName = propertyEntry.getKey();
                        java.lang.String propertyValue = propertyEntry.getValue();
                        if (propertyValue != null) {
                            if ((((!gplEnabled) && configType.equals("core-site")) && (propertyName.equals(org.apache.ambari.server.topology.BlueprintValidatorImpl.LZO_CODEC_CLASS_PROPERTY_NAME) || propertyName.equals(org.apache.ambari.server.topology.BlueprintValidatorImpl.CODEC_CLASSES_PROPERTY_NAME))) && propertyValue.contains(org.apache.ambari.server.topology.BlueprintValidatorImpl.LZO_CODEC_CLASS)) {
                                throw new org.apache.ambari.server.topology.GPLLicenseNotAcceptedException("Your Ambari server has not been configured to download LZO GPL software. " + "Please refer to documentation to configure Ambari before proceeding.");
                            }
                            if (org.apache.ambari.server.utils.SecretReference.isSecret(propertyValue)) {
                                errorMessage.append(((("  Config:" + configType) + " Property:") + propertyName) + "\n");
                                containsSecretReferences = true;
                            }
                        }
                    }
                }
            }
            if (containsSecretReferences) {
                throw new org.apache.ambari.server.topology.InvalidTopologyException(("Secret references are not allowed in blueprints, " + "replace following properties with real passwords:\n") + errorMessage);
            }
        }
        for (org.apache.ambari.server.topology.HostGroup hostGroup : blueprint.getHostGroups().values()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> operationalConfiguration = new java.util.HashMap<>(clusterConfigurations);
            operationalConfiguration.putAll(hostGroup.getConfiguration().getProperties());
            for (java.lang.String component : hostGroup.getComponentNames()) {
                if (component.equals("MYSQL_SERVER")) {
                    java.util.Map<java.lang.String, java.lang.String> hiveEnvConfig = clusterConfigurations.get("hive-env");
                    if ((((hiveEnvConfig != null) && (!hiveEnvConfig.isEmpty())) && (hiveEnvConfig.get("hive_database") != null)) && hiveEnvConfig.get("hive_database").startsWith("Existing")) {
                        throw new org.apache.ambari.server.topology.InvalidTopologyException("Incorrect configuration: MYSQL_SERVER component is available but hive" + " using existing db!");
                    }
                }
                if (org.apache.ambari.server.topology.ClusterTopologyImpl.isNameNodeHAEnabled(clusterConfigurations) && component.equals("NAMENODE")) {
                    java.util.Map<java.lang.String, java.lang.String> hadoopEnvConfig = clusterConfigurations.get("hadoop-env");
                    if ((((hadoopEnvConfig != null) && (!hadoopEnvConfig.isEmpty())) && hadoopEnvConfig.containsKey("dfs_ha_initial_namenode_active")) && hadoopEnvConfig.containsKey("dfs_ha_initial_namenode_standby")) {
                        java.util.ArrayList<org.apache.ambari.server.topology.HostGroup> hostGroupsForComponent = new java.util.ArrayList<>(blueprint.getHostGroupsForComponent(component));
                        java.util.Set<java.lang.String> givenHostGroups = new java.util.HashSet<>();
                        givenHostGroups.add(hadoopEnvConfig.get("dfs_ha_initial_namenode_active"));
                        givenHostGroups.add(hadoopEnvConfig.get("dfs_ha_initial_namenode_standby"));
                        if (givenHostGroups.size() != hostGroupsForComponent.size()) {
                            throw new java.lang.IllegalArgumentException("NAMENODE HA host groups mapped incorrectly for properties 'dfs_ha_initial_namenode_active' and 'dfs_ha_initial_namenode_standby'. Expected Host groups are :" + hostGroupsForComponent);
                        }
                        if (org.apache.ambari.server.topology.HostGroup.HOSTGROUP_REGEX.matcher(hadoopEnvConfig.get("dfs_ha_initial_namenode_active")).matches() && org.apache.ambari.server.topology.HostGroup.HOSTGROUP_REGEX.matcher(hadoopEnvConfig.get("dfs_ha_initial_namenode_standby")).matches()) {
                            for (org.apache.ambari.server.topology.HostGroup hostGroupForComponent : hostGroupsForComponent) {
                                java.util.Iterator<java.lang.String> itr = givenHostGroups.iterator();
                                while (itr.hasNext()) {
                                    if (itr.next().contains(hostGroupForComponent.getName())) {
                                        itr.remove();
                                    }
                                } 
                            }
                            if (!givenHostGroups.isEmpty()) {
                                throw new java.lang.IllegalArgumentException("NAMENODE HA host groups mapped incorrectly for properties 'dfs_ha_initial_namenode_active' and 'dfs_ha_initial_namenode_standby'. Expected Host groups are :" + hostGroupsForComponent);
                            }
                        }
                    }
                }
                if (component.equals("HIVE_METASTORE")) {
                    java.util.Map<java.lang.String, java.lang.String> hiveEnvConfig = clusterConfigurations.get("hive-env");
                    if ((((((hiveEnvConfig != null) && (!hiveEnvConfig.isEmpty())) && (hiveEnvConfig.get("hive_database") != null)) && hiveEnvConfig.get("hive_database").equals("Existing SQL Anywhere Database")) && (org.apache.ambari.server.utils.VersionUtils.compareVersions(stack.getVersion(), "2.3.0.0") < 0)) && stack.getName().equalsIgnoreCase("HDP")) {
                        throw new org.apache.ambari.server.topology.InvalidTopologyException("Incorrect configuration: SQL Anywhere db is available only for stack HDP-2.3+ " + "and repo version 2.3.2+!");
                    }
                }
                if (component.equals("OOZIE_SERVER")) {
                    java.util.Map<java.lang.String, java.lang.String> oozieEnvConfig = clusterConfigurations.get("oozie-env");
                    if ((((((oozieEnvConfig != null) && (!oozieEnvConfig.isEmpty())) && (oozieEnvConfig.get("oozie_database") != null)) && oozieEnvConfig.get("oozie_database").equals("Existing SQL Anywhere Database")) && (org.apache.ambari.server.utils.VersionUtils.compareVersions(stack.getVersion(), "2.3.0.0") < 0)) && stack.getName().equalsIgnoreCase("HDP")) {
                        throw new org.apache.ambari.server.topology.InvalidTopologyException("Incorrect configuration: SQL Anywhere db is available only for stack HDP-2.3+ " + "and repo version 2.3.2+!");
                    }
                }
            }
        }
    }

    private java.util.Collection<java.lang.String> verifyComponentInAllHostGroups(java.lang.String component, org.apache.ambari.server.state.AutoDeployInfo autoDeploy) {
        java.util.Collection<java.lang.String> cardinalityFailures = new java.util.HashSet<>();
        int actualCount = blueprint.getHostGroupsForComponent(component).size();
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroup> hostGroups = blueprint.getHostGroups();
        if (actualCount != hostGroups.size()) {
            if ((autoDeploy != null) && autoDeploy.isEnabled()) {
                for (org.apache.ambari.server.topology.HostGroup group : hostGroups.values()) {
                    group.addComponent(component);
                }
            } else {
                cardinalityFailures.add(((component + "(actual=") + actualCount) + ", required=ALL)");
            }
        }
        return cardinalityFailures;
    }

    private java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.DependencyInfo>> validateHostGroup(org.apache.ambari.server.topology.HostGroup group, java.lang.String dependencyValidationType) {
        org.apache.ambari.server.topology.BlueprintValidatorImpl.LOGGER.info("Validating hostgroup: {}", group.getName());
        java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.DependencyInfo>> dependenciesIssues = new java.util.HashMap<>();
        for (java.lang.String component : new java.util.HashSet<>(group.getComponentNames())) {
            org.apache.ambari.server.topology.BlueprintValidatorImpl.LOGGER.debug("Processing component: {}", component);
            for (org.apache.ambari.server.state.DependencyInfo dependency : stack.getDependenciesForComponent(component)) {
                org.apache.ambari.server.topology.BlueprintValidatorImpl.LOGGER.debug("Processing dependency [{}] for component [{}]", dependency.getName(), component);
                java.lang.String conditionalService = stack.getConditionalServiceForDependency(dependency);
                if ((conditionalService != null) && (!blueprint.getServices().contains(conditionalService))) {
                    org.apache.ambari.server.topology.BlueprintValidatorImpl.LOGGER.debug("Conditional service  [{}] is missing from the blueprint, skipping dependency [{}]", conditionalService, dependency.getName());
                    continue;
                }
                org.apache.ambari.server.state.ComponentInfo dependencyComponent = stack.getComponentInfo(dependency.getComponentName());
                if (dependencyComponent == null) {
                    org.apache.ambari.server.topology.BlueprintValidatorImpl.LOGGER.debug("The component [{}] is not associated with any known services, skipping dependency", dependency.getComponentName());
                    continue;
                }
                boolean isClientDependency = dependencyComponent.isClient();
                if (isClientDependency && (!blueprint.getServices().contains(dependency.getServiceName()))) {
                    org.apache.ambari.server.topology.BlueprintValidatorImpl.LOGGER.debug("The service [{}] for component [{}] is missing from the blueprint [{}], skipping dependency", dependency.getServiceName(), dependency.getComponentName(), blueprint.getName());
                    continue;
                }
                java.lang.String dependencyScope = dependency.getScope();
                java.lang.String dependencyType = dependency.getType();
                java.lang.String componentName = dependency.getComponentName();
                org.apache.ambari.server.state.AutoDeployInfo autoDeployInfo = dependency.getAutoDeploy();
                boolean resolved = false;
                if ((dependencyValidationType != null) && (!dependencyValidationType.equals(dependencyType))) {
                    continue;
                }
                if (dependency.hasDependencyConditions()) {
                    boolean conditionsSatisfied = true;
                    for (org.apache.ambari.server.state.DependencyConditionInfo dependencyCondition : dependency.getDependencyConditions()) {
                        if (!dependencyCondition.isResolved(blueprint.getConfiguration().getFullProperties())) {
                            conditionsSatisfied = false;
                            break;
                        }
                    }
                    if (!conditionsSatisfied) {
                        continue;
                    }
                }
                if (dependencyScope.equals("cluster")) {
                    java.util.Collection<java.lang.String> missingDependencyInfo = verifyComponentCardinalityCount(componentName, new org.apache.ambari.server.topology.Cardinality("1+"), autoDeployInfo);
                    resolved = missingDependencyInfo.isEmpty();
                    if (dependencyType.equals("exclusive")) {
                        resolved = !resolved;
                    }
                } else if (dependencyScope.equals("host")) {
                    if (dependencyType.equals("exclusive")) {
                        if (!group.getComponentNames().contains(componentName)) {
                            resolved = true;
                        }
                    } else if (group.getComponentNames().contains(componentName) || ((autoDeployInfo != null) && autoDeployInfo.isEnabled())) {
                        resolved = true;
                        group.addComponent(componentName);
                    }
                }
                if (!resolved) {
                    java.util.Collection<org.apache.ambari.server.state.DependencyInfo> compDependenciesIssues = dependenciesIssues.get(component);
                    if (compDependenciesIssues == null) {
                        compDependenciesIssues = new java.util.HashSet<>();
                        dependenciesIssues.put(component, compDependenciesIssues);
                    }
                    compDependenciesIssues.add(dependency);
                }
            }
        }
        return dependenciesIssues;
    }

    public java.util.Collection<java.lang.String> verifyComponentCardinalityCount(java.lang.String component, org.apache.ambari.server.topology.Cardinality cardinality, org.apache.ambari.server.state.AutoDeployInfo autoDeploy) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = blueprint.getConfiguration().getProperties();
        java.util.Collection<java.lang.String> cardinalityFailures = new java.util.HashSet<>();
        if (org.apache.ambari.server.topology.ClusterTopologyImpl.isNameNodeHAEnabled(configProperties) && component.equals("SECONDARY_NAMENODE")) {
            cardinality = new org.apache.ambari.server.topology.Cardinality("0");
        }
        int actualCount = blueprint.getHostGroupsForComponent(component).size();
        if (!cardinality.isValidCount(actualCount)) {
            boolean validated = !isDependencyManaged(stack, component, configProperties);
            if ((((!validated) && (autoDeploy != null)) && autoDeploy.isEnabled()) && cardinality.supportsAutoDeploy()) {
                java.lang.String coLocateName = autoDeploy.getCoLocate();
                if ((coLocateName != null) && (!coLocateName.isEmpty())) {
                    java.util.Collection<org.apache.ambari.server.topology.HostGroup> coLocateHostGroups = blueprint.getHostGroupsForComponent(coLocateName.split("/")[1]);
                    if (!coLocateHostGroups.isEmpty()) {
                        validated = true;
                        org.apache.ambari.server.topology.HostGroup group = coLocateHostGroups.iterator().next();
                        group.addComponent(component);
                    }
                }
            }
            if (!validated) {
                cardinalityFailures.add(((((component + "(actual=") + actualCount) + ", required=") + cardinality.getValue()) + ")");
            }
        }
        return cardinalityFailures;
    }

    protected boolean isDependencyManaged(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String component, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfig) {
        boolean isManaged = true;
        java.lang.String externalComponentConfig = stack.getExternalComponentConfig(component);
        if (externalComponentConfig != null) {
            java.lang.String[] toks = externalComponentConfig.split("/");
            java.lang.String externalComponentConfigType = toks[0];
            java.lang.String externalComponentConfigProp = toks[1];
            java.util.Map<java.lang.String, java.lang.String> properties = clusterConfig.get(externalComponentConfigType);
            if ((properties != null) && properties.containsKey(externalComponentConfigProp)) {
                if (properties.get(externalComponentConfigProp).startsWith("Existing")) {
                    isManaged = false;
                }
            }
        }
        return isManaged;
    }

    private void generateInvalidTopologyException(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.DependencyInfo>>> dependenciesIssues, java.util.Collection<java.lang.String> cardinalityFailures) throws org.apache.ambari.server.topology.InvalidTopologyException {
        java.lang.String msg = "Cluster Topology validation failed.";
        if (!cardinalityFailures.isEmpty()) {
            msg += "  Invalid service component count: " + cardinalityFailures;
        }
        if (!dependenciesIssues.isEmpty()) {
            msg += " Component dependencies issues: " + dependenciesIssues;
        }
        msg += ".  To disable topology validation and create the blueprint, " + "add the following to the end of the url: '?validate_topology=false'";
        throw new org.apache.ambari.server.topology.InvalidTopologyException(msg);
    }
}