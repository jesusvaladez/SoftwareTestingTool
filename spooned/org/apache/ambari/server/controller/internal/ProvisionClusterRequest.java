package org.apache.ambari.server.controller.internal;
@java.lang.SuppressWarnings("unchecked")
public class ProvisionClusterRequest extends org.apache.ambari.server.controller.internal.BaseClusterRequest {
    public static final java.lang.String HOSTGROUPS_PROPERTY = "host_groups";

    public static final java.lang.String HOSTGROUP_NAME_PROPERTY = "name";

    public static final java.lang.String HOSTGROUP_HOST_COUNT_PROPERTY = "host_count";

    public static final java.lang.String HOSTGROUP_HOST_PREDICATE_PROPERTY = "host_predicate";

    public static final java.lang.String HOSTGROUP_HOST_FQDN_PROPERTY = "fqdn";

    public static final java.lang.String HOSTGROUP_HOST_RACK_INFO_PROPERTY = "rack_info";

    public static final java.lang.String HOSTGROUP_HOSTS_PROPERTY = "hosts";

    public static final java.lang.String CONFIGURATIONS_PROPERTY = "configurations";

    public static final java.lang.String DEFAULT_PASSWORD_PROPERTY = "default_password";

    public static final java.lang.String CONFIG_RECOMMENDATION_STRATEGY = "config_recommendation_strategy";

    public static final java.lang.String REPO_VERSION_PROPERTY = "repository_version";

    public static final java.lang.String REPO_VERSION_ID_PROPERTY = "repository_version_id";

    public static final java.lang.String QUICKLINKS_PROFILE_FILTERS_PROPERTY = "quicklinks_profile/filters";

    public static final java.lang.String QUICKLINKS_PROFILE_SERVICES_PROPERTY = "quicklinks_profile/services";

    public static final java.lang.String ALIAS = "alias";

    public static final java.lang.String PRINCIPAL = "principal";

    public static final java.lang.String KEY = "key";

    public static final java.lang.String TYPE = "type";

    private static org.apache.ambari.server.topology.ConfigurationFactory configurationFactory = new org.apache.ambari.server.topology.ConfigurationFactory();

    private java.lang.String clusterName;

    private java.lang.String defaultPassword;

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.Credential> credentialsMap;

    private final org.apache.ambari.server.topology.ConfigRecommendationStrategy configRecommendationStrategy;

    private java.lang.String repoVersion;

    private java.lang.Long repoVersionId;

    private final java.lang.String quickLinksProfileJson;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.class);

    public ProvisionClusterRequest(java.util.Map<java.lang.String, java.lang.Object> properties, org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        setClusterName(java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID)));
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_PROPERTY)) {
            repoVersion = properties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_PROPERTY).toString();
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_ID_PROPERTY)) {
            repoVersionId = java.lang.Long.parseLong(properties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.REPO_VERSION_ID_PROPERTY).toString());
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.DEFAULT_PASSWORD_PROPERTY)) {
            defaultPassword = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.DEFAULT_PASSWORD_PROPERTY));
        }
        try {
            parseBlueprint(properties);
        } catch (org.apache.ambari.server.stack.NoSuchStackException e) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("The specified stack doesn't exist: " + e, e);
        } catch (org.apache.ambari.server.topology.NoSuchBlueprintException e) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("The specified blueprint doesn't exist: " + e, e);
        }
        this.securityConfiguration = securityConfiguration;
        org.apache.ambari.server.topology.Configuration configuration = org.apache.ambari.server.controller.internal.ProvisionClusterRequest.configurationFactory.getConfiguration(((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (properties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIGURATIONS_PROPERTY))));
        configuration.setParentConfiguration(blueprint.getConfiguration());
        setConfiguration(configuration);
        parseHostGroupInfo(properties);
        this.credentialsMap = parseCredentials(properties);
        this.configRecommendationStrategy = parseConfigRecommendationStrategy(properties);
        setProvisionAction(parseProvisionAction(properties));
        try {
            this.quickLinksProfileJson = processQuickLinksProfile(properties);
        } catch (org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException ex) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("Invalid quick links profile", ex);
        }
    }

    private java.lang.String processQuickLinksProfile(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileEvaluationException {
        java.lang.Object globalFilters = properties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.QUICKLINKS_PROFILE_FILTERS_PROPERTY);
        java.lang.Object serviceFilters = properties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.QUICKLINKS_PROFILE_SERVICES_PROPERTY);
        return (null != globalFilters) || (null != serviceFilters) ? new org.apache.ambari.server.state.quicklinksprofile.QuickLinksProfileBuilder().buildQuickLinksProfile(globalFilters, serviceFilters) : null;
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.Credential> parseCredentials(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        java.util.HashMap<java.lang.String, org.apache.ambari.server.topology.Credential> credentialHashMap = new java.util.HashMap<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> credentialsSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.String>>) (properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CREDENTIALS)));
        if (credentialsSet != null) {
            for (java.util.Map<java.lang.String, java.lang.String> credentialMap : credentialsSet) {
                java.lang.String alias = com.google.common.base.Strings.emptyToNull(credentialMap.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.ALIAS));
                if (alias == null) {
                    throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("credential.alias property is missing.");
                }
                java.lang.String principal = com.google.common.base.Strings.emptyToNull(credentialMap.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.PRINCIPAL));
                if (principal == null) {
                    throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("credential.principal property is missing.");
                }
                java.lang.String key = com.google.common.base.Strings.emptyToNull(credentialMap.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.KEY));
                if (key == null) {
                    throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("credential.key is missing.");
                }
                java.lang.String typeString = com.google.common.base.Strings.emptyToNull(credentialMap.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.TYPE));
                if (typeString == null) {
                    throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("credential.type is missing.");
                }
                org.apache.ambari.server.security.encryption.CredentialStoreType type = com.google.common.base.Enums.getIfPresent(org.apache.ambari.server.security.encryption.CredentialStoreType.class, typeString.toUpperCase()).orNull();
                if (type == null) {
                    throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("credential.type [%s] is invalid. acceptable values: %s", typeString.toUpperCase(), java.util.Arrays.toString(org.apache.ambari.server.security.encryption.CredentialStoreType.values())));
                }
                credentialHashMap.put(alias, new org.apache.ambari.server.topology.Credential(alias, principal, key, type));
            }
        }
        return credentialHashMap;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.topology.Credential> getCredentialsMap() {
        return credentialsMap;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public org.apache.ambari.server.topology.ConfigRecommendationStrategy getConfigRecommendationStrategy() {
        return configRecommendationStrategy;
    }

    @java.lang.Override
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.TopologyRequest.Type getType() {
        return org.apache.ambari.server.topology.TopologyRequest.Type.PROVISION;
    }

    @java.lang.Override
    public java.lang.String getDescription() {
        return java.lang.String.format("Provision Cluster '%s'", clusterName);
    }

    private void parseBlueprint(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.stack.NoSuchStackException, org.apache.ambari.server.topology.NoSuchBlueprintException {
        java.lang.String blueprintName = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.BLUEPRINT));
        setBlueprint(getBlueprintFactory().getBlueprint(blueprintName));
        if (blueprint == null) {
            throw new org.apache.ambari.server.topology.NoSuchBlueprintException(blueprintName);
        }
    }

    private void parseHostGroupInfo(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> hostGroups = ((java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>>) (properties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUPS_PROPERTY)));
        if ((hostGroups == null) || hostGroups.isEmpty()) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("'host_groups' element must be included in cluster create body");
        }
        for (java.util.Map<java.lang.String, java.lang.Object> hostGroupProperties : hostGroups) {
            processHostGroup(hostGroupProperties);
        }
    }

    private void processHostGroup(java.util.Map<java.lang.String, java.lang.Object> hostGroupProperties) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        java.lang.String name = java.lang.String.valueOf(hostGroupProperties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_NAME_PROPERTY));
        if (((name == null) || name.equals("null")) || name.isEmpty()) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException("All host groups must contain a 'name' element");
        }
        org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo = new org.apache.ambari.server.topology.HostGroupInfo(name);
        getHostGroupInfo().put(name, hostGroupInfo);
        processHostCountAndPredicate(hostGroupProperties, hostGroupInfo);
        processGroupHosts(name, ((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (hostGroupProperties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOSTS_PROPERTY))), hostGroupInfo);
        hostGroupInfo.setConfiguration(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.configurationFactory.getConfiguration(((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (hostGroupProperties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIGURATIONS_PROPERTY)))));
    }

    private void processHostCountAndPredicate(java.util.Map<java.lang.String, java.lang.Object> hostGroupProperties, org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        if (hostGroupProperties.containsKey(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_COUNT_PROPERTY)) {
            hostGroupInfo.setRequestedCount(java.lang.Integer.parseInt(java.lang.String.valueOf(hostGroupProperties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_COUNT_PROPERTY))));
            org.apache.ambari.server.controller.internal.ProvisionClusterRequest.LOG.info("Stored expected hosts count {} for group {}", hostGroupInfo.getRequestedHostCount(), hostGroupInfo.getHostGroupName());
        }
        if (hostGroupProperties.containsKey(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_PREDICATE_PROPERTY)) {
            if (hostGroupInfo.getRequestedHostCount() == 0) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Host group '%s' must not specify 'host_predicate' without 'host_count'", hostGroupInfo.getHostGroupName()));
            }
            java.lang.String hostPredicate = java.lang.String.valueOf(hostGroupProperties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_PREDICATE_PROPERTY));
            validateHostPredicateProperties(hostPredicate);
            try {
                hostGroupInfo.setPredicate(hostPredicate);
                org.apache.ambari.server.controller.internal.ProvisionClusterRequest.LOG.info("Compiled host predicate {} for group {}", hostPredicate, hostGroupInfo.getHostGroupName());
            } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Unable to compile host predicate '%s': %s", hostPredicate, e), e);
            }
        }
    }

    private void processGroupHosts(java.lang.String name, java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> hosts, org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        if (hosts != null) {
            if (hostGroupInfo.getRequestedHostCount() != 0) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Host group '%s' must not contain both a 'hosts' element and a 'host_count' value", name));
            }
            if (hostGroupInfo.getPredicate() != null) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Host group '%s' must not contain both a 'hosts' element and a 'host_predicate' value", name));
            }
            for (java.util.Map<java.lang.String, java.lang.String> hostProperties : hosts) {
                if (hostProperties.containsKey(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_FQDN_PROPERTY)) {
                    hostGroupInfo.addHost(hostProperties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_FQDN_PROPERTY));
                }
                if (hostProperties.containsKey(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_RACK_INFO_PROPERTY)) {
                    hostGroupInfo.addHostRackInfo(hostProperties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_FQDN_PROPERTY), hostProperties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.HOSTGROUP_HOST_RACK_INFO_PROPERTY));
                }
            }
        }
        if (hostGroupInfo.getRequestedHostCount() == 0) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Host group '%s' must contain at least one 'hosts/fqdn' or a 'host_count' value", name));
        }
    }

    private org.apache.ambari.server.topology.ConfigRecommendationStrategy parseConfigRecommendationStrategy(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        if (properties.containsKey(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIG_RECOMMENDATION_STRATEGY)) {
            java.lang.String configRecommendationStrategy = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.ProvisionClusterRequest.CONFIG_RECOMMENDATION_STRATEGY));
            com.google.common.base.Optional<org.apache.ambari.server.topology.ConfigRecommendationStrategy> configRecommendationStrategyOpt = com.google.common.base.Enums.getIfPresent(org.apache.ambari.server.topology.ConfigRecommendationStrategy.class, configRecommendationStrategy);
            if (!configRecommendationStrategyOpt.isPresent()) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Config recommendation strategy is not supported: %s", configRecommendationStrategy));
            }
            return configRecommendationStrategyOpt.get();
        } else {
            return org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY;
        }
    }

    private org.apache.ambari.server.controller.internal.ProvisionAction parseProvisionAction(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        if (properties.containsKey(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY)) {
            java.lang.String provisionActionStr = java.lang.String.valueOf(properties.get(org.apache.ambari.server.controller.internal.BaseClusterRequest.PROVISION_ACTION_PROPERTY));
            com.google.common.base.Optional<org.apache.ambari.server.controller.internal.ProvisionAction> provisionActionOptional = com.google.common.base.Enums.getIfPresent(org.apache.ambari.server.controller.internal.ProvisionAction.class, provisionActionStr);
            if (!provisionActionOptional.isPresent()) {
                throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("Invalid provision_action specified in the template: %s", provisionActionStr));
            }
            return provisionActionOptional.get();
        } else {
            return org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START;
        }
    }

    public java.lang.String getRepositoryVersion() {
        return repoVersion;
    }

    public java.lang.Long getRepositoryVersionId() {
        return repoVersionId;
    }

    public java.lang.String getQuickLinksProfileJson() {
        return quickLinksProfileJson;
    }

    public java.lang.String getDefaultPassword() {
        return defaultPassword;
    }
}