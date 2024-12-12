package org.apache.ambari.server.stack.upgrade.orchestrate;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_FAIL_ON_CHECK_WARNINGS;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES;
import static org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE;
public class UpgradeContext {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);

    public static final java.lang.String COMMAND_PARAM_CLUSTER_NAME = "clusterName";

    public static final java.lang.String COMMAND_PARAM_DIRECTION = "upgrade_direction";

    public static final java.lang.String COMMAND_PARAM_UPGRADE_PACK = "upgrade_pack";

    public static final java.lang.String COMMAND_PARAM_REQUEST_ID = "request_id";

    public static final java.lang.String COMMAND_PARAM_UPGRADE_TYPE = "upgrade_type";

    public static final java.lang.String COMMAND_PARAM_TASKS = "tasks";

    public static final java.lang.String COMMAND_PARAM_STRUCT_OUT = "structured_out";

    private final org.apache.ambari.server.state.Cluster m_cluster;

    private final org.apache.ambari.server.stack.upgrade.Direction m_direction;

    private final org.apache.ambari.spi.upgrade.UpgradeType m_type;

    private final org.apache.ambari.server.stack.upgrade.UpgradePack m_upgradePack;

    private final org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersion;

    private final org.apache.ambari.server.stack.MasterHostResolver m_resolver;

    private final java.util.List<org.apache.ambari.server.state.ServiceComponentHost> m_unhealthy = new java.util.ArrayList<>();

    private final java.util.Map<java.lang.String, java.lang.String> m_serviceNames = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, java.lang.String> m_componentNames = new java.util.HashMap<>();

    private boolean m_autoSkipComponentFailures = false;

    private boolean m_autoSkipServiceCheckFailures = false;

    private boolean m_autoSkipManualVerification = false;

    private final java.util.Set<java.lang.String> m_services = new java.util.HashSet<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> m_targetRepositoryMap = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> m_sourceRepositoryMap = new java.util.HashMap<>();

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory m_hrcFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.stageplanner.RoleGraphFactory m_roleGraphFactory;

    @com.google.inject.Inject
    private com.google.gson.Gson m_gson;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo m_metaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper m_upgradeHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO m_repoVersionDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.UpgradeDAO m_upgradeDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.KerberosHelper m_kerberosHelper;

    private final boolean m_isRevert;

    private long m_revertUpgradeId;

    private org.apache.ambari.spi.RepositoryType m_orchestration = org.apache.ambari.spi.RepositoryType.STANDARD;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration configuration;

    private org.apache.ambari.spi.upgrade.OrchestrationOptions m_orchestrationOptions;

    private org.apache.ambari.spi.upgrade.UpgradeType calculateUpgradeType(java.util.Map<java.lang.String, java.lang.Object> upgradeRequestMap, org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeType upgradeType = org.apache.ambari.spi.upgrade.UpgradeType.ROLLING;
        java.lang.String upgradeTypeProperty = ((java.lang.String) (upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE)));
        boolean upgradeTypePassed = org.apache.commons.lang.StringUtils.isNotBlank(upgradeTypeProperty);
        if (upgradeTypePassed) {
            try {
                upgradeType = org.apache.ambari.spi.upgrade.UpgradeType.valueOf(upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE).toString());
            } catch (java.lang.Exception e) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Property %s has an incorrect value of %s.", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_TYPE, upgradeTypeProperty));
            }
        } else if (upgradeEntity != null) {
            upgradeType = upgradeEntity.getUpgradeType();
        }
        return upgradeType;
    }

    @com.google.inject.assistedinject.AssistedInject
    public UpgradeContext(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted
    java.util.Map<java.lang.String, java.lang.Object> upgradeRequestMap, com.google.gson.Gson gson, org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper, org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAO, org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO, org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo) throws org.apache.ambari.server.AmbariException {
        m_gson = gson;
        m_upgradeHelper = upgradeHelper;
        m_upgradeDAO = upgradeDAO;
        m_repoVersionDAO = repoVersionDAO;
        m_cluster = cluster;
        m_isRevert = upgradeRequestMap.containsKey(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID);
        m_metaInfo = metaInfo;
        if (m_isRevert) {
            m_revertUpgradeId = java.lang.Long.parseLong(upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REVERT_UPGRADE_ID).toString());
            org.apache.ambari.server.orm.entities.UpgradeEntity revertUpgrade = m_upgradeDAO.findUpgrade(m_revertUpgradeId);
            org.apache.ambari.server.orm.entities.UpgradeEntity revertableUpgrade = m_upgradeDAO.findRevertable(cluster.getClusterId());
            if (null == revertUpgrade) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Could not find Upgrade with id %s to revert.", m_revertUpgradeId));
            }
            if (null == revertableUpgrade) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("There are no upgrades for cluster %s which are marked as revertable", cluster.getClusterName()));
            }
            if (!revertUpgrade.getOrchestration().isRevertable()) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The %s repository type is not revertable", revertUpgrade.getOrchestration()));
            }
            if (revertUpgrade.getDirection() != org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) {
                throw new org.apache.ambari.server.AmbariException("Only successfully completed upgrades can be reverted. Downgrades cannot be reverted.");
            }
            if (!revertableUpgrade.getId().equals(revertUpgrade.getId())) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The only upgrade which is currently allowed to be reverted for cluster %s is upgrade ID %s which was an upgrade to %s", cluster.getClusterName(), revertableUpgrade.getId(), revertableUpgrade.getRepositoryVersion().getVersion()));
            }
            m_type = calculateUpgradeType(upgradeRequestMap, revertUpgrade);
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> clusterServices = cluster.getServices();
            for (org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history : revertUpgrade.getHistory()) {
                java.lang.String serviceName = history.getServiceName();
                java.lang.String componentName = history.getComponentName();
                if (!clusterServices.containsKey(serviceName)) {
                    org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.LOG.warn("{}/{} will not be reverted since it is no longer installed in the cluster", serviceName, componentName);
                    continue;
                }
                m_services.add(serviceName);
                m_sourceRepositoryMap.put(serviceName, history.getTargetRepositoryVersion());
                m_targetRepositoryMap.put(serviceName, history.getFromReposistoryVersion());
            }
            m_repositoryVersion = revertUpgrade.getRepositoryVersion();
            upgradeRequestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, m_repositoryVersion.getId().toString());
            upgradeRequestMap.put(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK, revertUpgrade.getUpgradePackage());
            m_direction = org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE;
            m_orchestration = revertUpgrade.getOrchestration();
            m_upgradePack = getUpgradePack(revertUpgrade);
            m_orchestrationOptions = getOrchestrationOptions(metaInfo, m_upgradePack);
        } else {
            java.lang.String directionProperty = ((java.lang.String) (upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION)));
            if (org.apache.commons.lang.StringUtils.isEmpty(directionProperty)) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("%s is required", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_DIRECTION));
            }
            m_direction = org.apache.ambari.server.stack.upgrade.Direction.valueOf(directionProperty);
            switch (m_direction) {
                case UPGRADE :
                    {
                        java.lang.String repositoryVersionId = ((java.lang.String) (upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID)));
                        if (null == repositoryVersionId) {
                            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The property %s is required when the upgrade direction is %s", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID, m_direction));
                        }
                        m_type = calculateUpgradeType(upgradeRequestMap, null);
                        m_repositoryVersion = m_repoVersionDAO.findByPK(java.lang.Long.valueOf(repositoryVersionId));
                        m_orchestration = m_repositoryVersion.getType();
                        java.util.Set<java.lang.String> serviceNames = getServicesForUpgrade(cluster, m_repositoryVersion);
                        m_services.addAll(serviceNames);
                        java.lang.String preferredUpgradePackName = ((java.lang.String) (upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK)));
                        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "This is wrong; it assumes that any upgrade source AND target are consistent stacks")
                        org.apache.ambari.server.orm.entities.RepositoryVersionEntity upgradeFromRepositoryVersion = cluster.getService(serviceNames.iterator().next()).getDesiredRepositoryVersion();
                        m_upgradePack = m_upgradeHelper.suggestUpgradePack(m_cluster.getClusterName(), upgradeFromRepositoryVersion.getStackId(), m_repositoryVersion.getStackId(), m_direction, m_type, preferredUpgradePackName);
                        m_orchestrationOptions = getOrchestrationOptions(metaInfo, m_upgradePack);
                        break;
                    }
                case DOWNGRADE :
                    {
                        org.apache.ambari.server.orm.entities.UpgradeEntity upgrade = m_upgradeDAO.findLastUpgradeForCluster(cluster.getClusterId(), org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
                        m_repositoryVersion = upgrade.getRepositoryVersion();
                        m_orchestration = upgrade.getOrchestration();
                        m_type = calculateUpgradeType(upgradeRequestMap, upgrade);
                        for (org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history : upgrade.getHistory()) {
                            m_services.add(history.getServiceName());
                            m_sourceRepositoryMap.put(history.getServiceName(), m_repositoryVersion);
                            m_targetRepositoryMap.put(history.getServiceName(), history.getFromReposistoryVersion());
                        }
                        m_upgradePack = getUpgradePack(upgrade);
                        m_orchestrationOptions = getOrchestrationOptions(metaInfo, m_upgradePack);
                        break;
                    }
                default :
                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("%s is not a valid upgrade direction.", m_direction));
            }
        }
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator upgradeRequestValidator = buildValidator(m_type);
        upgradeRequestValidator.validate(cluster, m_direction, m_type, m_upgradePack, upgradeRequestMap);
        boolean skipComponentFailures = m_upgradePack.isComponentFailureAutoSkipped();
        boolean skipServiceCheckFailures = m_upgradePack.isServiceCheckFailureAutoSkipped();
        if (upgradeRequestMap.containsKey(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES)) {
            skipComponentFailures = java.lang.Boolean.parseBoolean(((java.lang.String) (upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES))));
        }
        if (upgradeRequestMap.containsKey(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES)) {
            skipServiceCheckFailures = java.lang.Boolean.parseBoolean(((java.lang.String) (upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_SC_FAILURES))));
        }
        boolean skipManualVerification = false;
        if (upgradeRequestMap.containsKey(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION)) {
            skipManualVerification = java.lang.Boolean.parseBoolean(((java.lang.String) (upgradeRequestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION))));
        }
        m_autoSkipComponentFailures = skipComponentFailures;
        m_autoSkipServiceCheckFailures = skipServiceCheckFailures;
        m_autoSkipManualVerification = skipManualVerification;
        m_resolver = new org.apache.ambari.server.stack.MasterHostResolver(m_cluster, configHelper, this);
    }

    @com.google.inject.assistedinject.AssistedInject
    public UpgradeContext(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.state.Cluster cluster, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.UpgradeEntity upgradeEntity, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo, org.apache.ambari.server.state.ConfigHelper configHelper) {
        m_metaInfo = ambariMetaInfo;
        m_cluster = cluster;
        m_type = upgradeEntity.getUpgradeType();
        m_direction = upgradeEntity.getDirection();
        m_repositoryVersion = upgradeEntity.getRepositoryVersion();
        m_autoSkipComponentFailures = upgradeEntity.isComponentFailureAutoSkipped();
        m_autoSkipServiceCheckFailures = upgradeEntity.isServiceCheckFailureAutoSkipped();
        @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
        org.apache.ambari.server.state.StackId stackId = null;
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> allHistory = upgradeEntity.getHistory();
        for (org.apache.ambari.server.orm.entities.UpgradeHistoryEntity history : allHistory) {
            java.lang.String serviceName = history.getServiceName();
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepositoryVersion = history.getFromReposistoryVersion();
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = history.getTargetRepositoryVersion();
            m_sourceRepositoryMap.put(serviceName, sourceRepositoryVersion);
            m_targetRepositoryMap.put(serviceName, targetRepositoryVersion);
            m_services.add(serviceName);
            if (null == stackId) {
                stackId = sourceRepositoryVersion.getStackId();
            }
        }
        m_upgradePack = getUpgradePack(upgradeEntity);
        m_resolver = new org.apache.ambari.server.stack.MasterHostResolver(m_cluster, configHelper, this);
        m_orchestration = upgradeEntity.getOrchestration();
        m_isRevert = upgradeEntity.getOrchestration().isRevertable() && (upgradeEntity.getDirection() == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE);
        m_orchestrationOptions = getOrchestrationOptions(ambariMetaInfo, m_upgradePack);
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES, comment = "This is wrong")
    public org.apache.ambari.server.state.StackId getStackIdFromVersions(java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> version) {
        return version.values().iterator().next().getStackId();
    }

    public org.apache.ambari.server.stack.upgrade.UpgradePack getUpgradePack() {
        return m_upgradePack;
    }

    public org.apache.ambari.server.state.Cluster getCluster() {
        return m_cluster;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> getSourceVersions() {
        return new java.util.HashMap<>(m_sourceRepositoryMap);
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getSourceRepositoryVersion(java.lang.String serviceName) {
        return m_sourceRepositoryMap.get(serviceName);
    }

    public java.lang.String getSourceVersion(java.lang.String serviceName) {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity serviceSourceVersion = m_sourceRepositoryMap.get(serviceName);
        return serviceSourceVersion.getVersion();
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> getTargetVersions() {
        return new java.util.HashMap<>(m_targetRepositoryMap);
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getTargetRepositoryVersion(java.lang.String serviceName) {
        return m_targetRepositoryMap.get(serviceName);
    }

    public java.lang.String getTargetVersion(java.lang.String serviceName) {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity serviceTargetVersion = m_targetRepositoryMap.get(serviceName);
        return serviceTargetVersion.getVersion();
    }

    public org.apache.ambari.server.stack.upgrade.Direction getDirection() {
        return m_direction;
    }

    public org.apache.ambari.spi.upgrade.UpgradeType getType() {
        return m_type;
    }

    public org.apache.ambari.server.stack.MasterHostResolver getResolver() {
        return m_resolver;
    }

    public org.apache.ambari.server.api.services.AmbariMetaInfo getAmbariMetaInfo() {
        return m_metaInfo;
    }

    public void addUnhealthy(java.util.List<org.apache.ambari.server.state.ServiceComponentHost> unhealthy) {
        m_unhealthy.addAll(unhealthy);
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getRepositoryVersion() {
        return m_repositoryVersion;
    }

    public java.lang.String getServiceDisplay(java.lang.String service) {
        if (m_serviceNames.containsKey(service)) {
            return m_serviceNames.get(service);
        }
        return service;
    }

    public java.lang.String getComponentDisplay(java.lang.String service, java.lang.String component) {
        java.lang.String key = (service + ":") + component;
        if (m_componentNames.containsKey(key)) {
            return m_componentNames.get(key);
        }
        return component;
    }

    public void setServiceDisplay(java.lang.String service, java.lang.String displayName) {
        m_serviceNames.put(service, displayName == null ? service : displayName);
    }

    public void setComponentDisplay(java.lang.String service, java.lang.String component, java.lang.String displayName) {
        java.lang.String key = (service + ":") + component;
        m_componentNames.put(key, displayName);
    }

    public boolean isComponentFailureAutoSkipped() {
        return m_autoSkipComponentFailures;
    }

    public boolean isServiceCheckFailureAutoSkipped() {
        return m_autoSkipServiceCheckFailures;
    }

    public boolean isManualVerificationAutoSkipped() {
        return m_autoSkipManualVerification;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public java.util.Set<java.lang.String> getSupportedServices() {
        return java.util.Collections.unmodifiableSet(m_services);
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public boolean isServiceSupported(java.lang.String serviceName) {
        return m_services.contains(serviceName);
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public boolean isScoped(org.apache.ambari.server.stack.upgrade.UpgradeScope scope) {
        if (scope == org.apache.ambari.server.stack.upgrade.UpgradeScope.ANY) {
            return true;
        }
        switch (m_orchestration) {
            case PATCH :
            case SERVICE :
            case MAINT :
                return scope == org.apache.ambari.server.stack.upgrade.UpgradeScope.PARTIAL;
            case STANDARD :
                return scope == org.apache.ambari.server.stack.upgrade.UpgradeScope.COMPLETE;
            default :
                break;
        }
        return false;
    }

    public org.apache.ambari.server.stageplanner.RoleGraphFactory getRoleGraphFactory() {
        return m_roleGraphFactory;
    }

    public org.apache.ambari.server.actionmanager.HostRoleCommandFactory getHostRoleCommandFactory() {
        return m_hrcFactory;
    }

    public org.apache.ambari.spi.RepositoryType getOrchestrationType() {
        return m_orchestration;
    }

    public java.util.Map<java.lang.String, java.lang.String> getInitializedCommandParameters() {
        java.util.Map<java.lang.String, java.lang.String> parameters = new java.util.HashMap<>();
        org.apache.ambari.server.stack.upgrade.Direction direction = getDirection();
        parameters.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.COMMAND_PARAM_CLUSTER_NAME, m_cluster.getClusterName());
        parameters.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.COMMAND_PARAM_DIRECTION, direction.name().toLowerCase());
        if (null != getType()) {
            com.google.gson.JsonElement json = m_gson.toJsonTree(getType());
            parameters.put(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.COMMAND_PARAM_UPGRADE_TYPE, json.getAsString());
        }
        return parameters;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("direction", m_direction).add("type", m_type).add("target", m_repositoryVersion).toString();
    }

    public boolean isDowngradeAllowed() {
        if (m_direction == org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE) {
            return false;
        }
        return m_upgradePack.isDowngradeAllowed();
    }

    public boolean isPatchRevert() {
        return m_isRevert;
    }

    public long getPatchRevertUpgradeId() {
        return m_revertUpgradeId;
    }

    public int getDefaultMaxDegreeOfParallelism() {
        return configuration.getDefaultMaxParallelismForUpgrades();
    }

    public org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeSummary getUpgradeSummary() {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeSummary summary = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeSummary();
        summary.direction = m_direction;
        summary.type = m_type;
        summary.orchestration = m_orchestration;
        summary.isRevert = m_isRevert;
        summary.isDowngradeAllowed = isDowngradeAllowed();
        summary.associatedRepositoryId = m_repositoryVersion.getId();
        summary.associatedStackId = m_repositoryVersion.getStackId().getStackId();
        summary.associatedVersion = m_repositoryVersion.getVersion();
        summary.isSwitchBits = m_isRevert || m_orchestration.isRevertable();
        summary.services = new java.util.HashMap<>();
        for (java.lang.String serviceName : m_services) {
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity sourceRepositoryVersion = m_sourceRepositoryMap.get(serviceName);
            org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = m_targetRepositoryMap.get(serviceName);
            if ((null == sourceRepositoryVersion) || (null == targetRepositoryVersion)) {
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.LOG.warn("Unable to get the source/target repositories for {} for the upgrade summary", serviceName);
                continue;
            }
            org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeServiceSummary serviceSummary = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeServiceSummary();
            serviceSummary.sourceRepositoryId = sourceRepositoryVersion.getId();
            serviceSummary.sourceStackId = sourceRepositoryVersion.getStackId().getStackId();
            serviceSummary.sourceVersion = sourceRepositoryVersion.getVersion();
            serviceSummary.targetRepositoryId = targetRepositoryVersion.getId();
            serviceSummary.targetStackId = targetRepositoryVersion.getStackId().getStackId();
            serviceSummary.targetVersion = targetRepositoryVersion.getVersion();
            summary.services.put(serviceName, serviceSummary);
        }
        return summary;
    }

    public org.apache.ambari.server.state.StackId getTargetStack() {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repo = m_targetRepositoryMap.values().iterator().next();
        return repo.getStackId();
    }

    public org.apache.ambari.server.state.StackId getSourceStack() {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repo = m_sourceRepositoryMap.values().iterator().next();
        return repo.getStackId();
    }

    public org.apache.ambari.server.controller.KerberosDetails getKerberosDetails() throws org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException, org.apache.ambari.server.AmbariException {
        return m_kerberosHelper.getKerberosDetails(m_cluster, null);
    }

    public org.apache.ambari.spi.upgrade.OrchestrationOptions getOrchestrationOptions() {
        return m_orchestrationOptions;
    }

    private java.util.Set<java.lang.String> getServicesForUpgrade(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> servicesForUpgrade;
        if (repositoryVersion.getType() == org.apache.ambari.spi.RepositoryType.STANDARD) {
            servicesForUpgrade = cluster.getServices().keySet();
        } else {
            try {
                org.apache.ambari.server.state.repository.VersionDefinitionXml vdf = repositoryVersion.getRepositoryXml();
                org.apache.ambari.server.state.repository.ClusterVersionSummary clusterVersionSummary = vdf.getClusterSummary(cluster, m_metaInfo);
                servicesForUpgrade = clusterVersionSummary.getAvailableServiceNames();
                if (servicesForUpgrade.isEmpty()) {
                    java.lang.String message = java.lang.String.format("When using a VDF of type %s, the available services must be defined in the VDF", repositoryVersion.getType());
                    throw new org.apache.ambari.server.AmbariException(message);
                }
            } catch (java.lang.Exception e) {
                java.lang.String msg = java.lang.String.format("Could not parse version definition for %s.  Upgrade will not proceed.", repositoryVersion.getVersion());
                throw new org.apache.ambari.server.AmbariException(msg);
            }
        }
        java.util.Iterator<java.lang.String> iterator = servicesForUpgrade.iterator();
        while (iterator.hasNext()) {
            java.lang.String serviceName = null;
            try {
                serviceName = iterator.next();
                org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
                m_sourceRepositoryMap.put(serviceName, service.getDesiredRepositoryVersion());
                m_targetRepositoryMap.put(serviceName, repositoryVersion);
            } catch (org.apache.ambari.server.ServiceNotFoundException e) {
                iterator.remove();
                org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.LOG.warn("Skipping orchestration for service {}, as it was defined to upgrade, but is not installed in cluster {}", serviceName, cluster.getClusterName());
            }
        } 
        return servicesForUpgrade;
    }

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator buildValidator(org.apache.ambari.spi.upgrade.UpgradeType upgradeType) {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator validator = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.BasicUpgradePropertiesValidator();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator preReqValidator = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.PreReqCheckValidator();
        validator.setNextValidator(preReqValidator);
        final org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator upgradeTypeValidator;
        switch (upgradeType) {
            case HOST_ORDERED :
                upgradeTypeValidator = new org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.HostOrderedUpgradeValidator();
                break;
            case NON_ROLLING :
            case ROLLING :
            default :
                upgradeTypeValidator = null;
                break;
        }
        preReqValidator.setNextValidator(upgradeTypeValidator);
        return validator;
    }

    private abstract class UpgradeRequestValidator {
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator m_nextValidator;

        void setNextValidator(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator nextValidator) {
            m_nextValidator = nextValidator;
        }

        final void validate(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack, java.util.Map<java.lang.String, java.lang.Object> requestMap) throws org.apache.ambari.server.AmbariException {
            check(cluster, direction, type, upgradePack, requestMap);
            if (null != m_nextValidator) {
                m_nextValidator.validate(cluster, direction, type, upgradePack, requestMap);
            }
        }

        abstract void check(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack, java.util.Map<java.lang.String, java.lang.Object> requestMap) throws org.apache.ambari.server.AmbariException;
    }

    private final class BasicUpgradePropertiesValidator extends org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator {
        @java.lang.Override
        public void check(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack, java.util.Map<java.lang.String, java.lang.Object> requestMap) throws org.apache.ambari.server.AmbariException {
            if (direction == org.apache.ambari.server.stack.upgrade.Direction.UPGRADE) {
                java.lang.String repositoryVersionId = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID)));
                if (org.apache.commons.lang.StringUtils.isBlank(repositoryVersionId)) {
                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("%s is required for upgrades", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID));
                }
            }
        }
    }

    private final class PreReqCheckValidator extends org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator {
        @java.lang.Override
        void check(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack, java.util.Map<java.lang.String, java.lang.Object> requestMap) throws org.apache.ambari.server.AmbariException {
            java.lang.String repositoryVersionId = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_REPO_VERSION_ID)));
            boolean skipPrereqChecks = java.lang.Boolean.parseBoolean(((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_PREREQUISITE_CHECKS))));
            boolean failOnCheckWarnings = java.lang.Boolean.parseBoolean(((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_FAIL_ON_CHECK_WARNINGS))));
            java.lang.String preferredUpgradePack = (requestMap.containsKey(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK)) ? ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_PACK))) : null;
            org.apache.ambari.server.orm.entities.UpgradeEntity existingUpgrade = cluster.getUpgradeInProgress();
            if (null != existingUpgrade) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to perform %s as another %s (request ID %s) is in progress.", direction.getText(false), existingUpgrade.getDirection().getText(false), existingUpgrade.getRequestId()));
            }
            if (direction.isDowngrade() || skipPrereqChecks) {
                return;
            }
            org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider provider = ((org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.PreUpgradeCheck)));
            org.apache.ambari.server.controller.spi.Predicate preUpgradeCheckPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_CLUSTER_NAME_PROPERTY_ID).equals(cluster.getClusterName()).and().property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_TARGET_REPOSITORY_VERSION_ID_ID).equals(repositoryVersionId).and().property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_FOR_REVERT_PROPERTY_ID).equals(m_isRevert).and().property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_TYPE_PROPERTY_ID).equals(type).and().property(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_UPGRADE_PACK_PROPERTY_ID).equals(preferredUpgradePack).toPredicate();
            org.apache.ambari.server.controller.spi.Request preUpgradeCheckRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest();
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> preUpgradeCheckResources;
            try {
                preUpgradeCheckResources = provider.getResources(preUpgradeCheckRequest, preUpgradeCheckPredicate);
            } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException | org.apache.ambari.server.controller.spi.SystemException | org.apache.ambari.server.controller.spi.UnsupportedPropertyException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to perform %s. Prerequisite checks could not be run", direction.getText(false)), e);
            }
            java.util.List<org.apache.ambari.server.controller.spi.Resource> failedResources = new java.util.LinkedList<>();
            if (preUpgradeCheckResources != null) {
                for (org.apache.ambari.server.controller.spi.Resource res : preUpgradeCheckResources) {
                    org.apache.ambari.spi.upgrade.UpgradeCheckStatus prereqCheckStatus = ((org.apache.ambari.spi.upgrade.UpgradeCheckStatus) (res.getPropertyValue(org.apache.ambari.server.controller.internal.PreUpgradeCheckResourceProvider.UPGRADE_CHECK_STATUS_PROPERTY_ID)));
                    if ((prereqCheckStatus == org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL) || (failOnCheckWarnings && (prereqCheckStatus == org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING))) {
                        failedResources.add(res);
                    }
                }
            }
            if (!failedResources.isEmpty()) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Unable to perform %s. Prerequisite checks failed %s", direction.getText(false), m_gson.toJson(failedResources)));
            }
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private final class HostOrderedUpgradeValidator extends org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.UpgradeRequestValidator {
        @java.lang.Override
        void check(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.stack.upgrade.Direction direction, org.apache.ambari.spi.upgrade.UpgradeType type, org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack, java.util.Map<java.lang.String, java.lang.Object> requestMap) throws org.apache.ambari.server.AmbariException {
            java.lang.String skipFailuresRequestProperty = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES)));
            if (java.lang.Boolean.parseBoolean(skipFailuresRequestProperty)) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The %s property is not valid when creating a %s upgrade.", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_FAILURES, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED));
            }
            java.lang.String skipManualVerification = ((java.lang.String) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION)));
            if (java.lang.Boolean.parseBoolean(skipManualVerification)) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The %s property is not valid when creating a %s upgrade.", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_SKIP_MANUAL_VERIFICATION, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED));
            }
            if (!requestMap.containsKey(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS)) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The %s property is required when creating a %s upgrade.", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED));
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.HostOrderItem> hostOrderItems = extractHostOrderItemsFromRequest(requestMap);
            java.util.List<java.lang.String> hostsFromRequest = new java.util.ArrayList<>(hostOrderItems.size());
            for (org.apache.ambari.server.stack.upgrade.HostOrderItem hostOrderItem : hostOrderItems) {
                if (hostOrderItem.getType() == org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType.HOST_UPGRADE) {
                    hostsFromRequest.addAll(hostOrderItem.getActionItems());
                }
            }
            java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
            java.util.Set<java.lang.String> clusterHostNames = new java.util.HashSet<>(hosts.size());
            for (org.apache.ambari.server.state.Host host : hosts) {
                clusterHostNames.add(host.getHostName());
            }
            java.util.Collection<java.lang.String> disjunction = org.apache.commons.collections.CollectionUtils.disjunction(hostsFromRequest, clusterHostNames);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(disjunction)) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The supplied list of hosts must match the cluster hosts in an upgrade of type %s. The following hosts are either missing or invalid: %s", org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED, org.apache.commons.lang.StringUtils.join(disjunction, ", ")));
            }
            org.apache.ambari.server.stack.upgrade.HostOrderGrouping hostOrderGrouping = null;
            java.util.List<org.apache.ambari.server.stack.upgrade.Grouping> groupings = upgradePack.getGroups(direction);
            for (org.apache.ambari.server.stack.upgrade.Grouping grouping : groupings) {
                if (grouping instanceof org.apache.ambari.server.stack.upgrade.HostOrderGrouping) {
                    hostOrderGrouping = ((org.apache.ambari.server.stack.upgrade.HostOrderGrouping) (grouping));
                    hostOrderGrouping.setHostOrderItems(hostOrderItems);
                }
            }
        }

        private java.util.List<org.apache.ambari.server.stack.upgrade.HostOrderItem> extractHostOrderItemsFromRequest(java.util.Map<java.lang.String, java.lang.Object> requestMap) throws org.apache.ambari.server.AmbariException {
            java.util.Set<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> hostsOrder = ((java.util.Set<java.util.Map<java.lang.String, java.util.List<java.lang.String>>>) (requestMap.get(org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS)));
            if (org.apache.commons.collections.CollectionUtils.isEmpty(hostsOrder)) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The %s property must be specified when using a %s upgrade type.", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED));
            }
            java.util.List<org.apache.ambari.server.stack.upgrade.HostOrderItem> hostOrderItems = new java.util.ArrayList<>();
            java.util.Iterator<java.util.Map<java.lang.String, java.util.List<java.lang.String>>> iterator = hostsOrder.iterator();
            while (iterator.hasNext()) {
                java.util.Map<java.lang.String, java.util.List<java.lang.String>> grouping = iterator.next();
                java.util.List<java.lang.String> hosts = grouping.get("hosts");
                java.util.List<java.lang.String> serviceChecks = grouping.get("service_checks");
                if (org.apache.commons.collections.CollectionUtils.isEmpty(hosts) && org.apache.commons.collections.CollectionUtils.isEmpty(serviceChecks)) {
                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("The %s property must contain at least one object with either a %s or %s key", org.apache.ambari.server.controller.internal.UpgradeResourceProvider.UPGRADE_HOST_ORDERED_HOSTS, "hosts", "service_checks"));
                }
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(hosts)) {
                    hostOrderItems.add(new org.apache.ambari.server.stack.upgrade.HostOrderItem(org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType.HOST_UPGRADE, hosts));
                }
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(serviceChecks)) {
                    hostOrderItems.add(new org.apache.ambari.server.stack.upgrade.HostOrderItem(org.apache.ambari.server.stack.upgrade.HostOrderItem.HostOrderActionType.SERVICE_CHECK, serviceChecks));
                }
            } 
            return hostOrderItems;
        }
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack getUpgradePack(org.apache.ambari.server.orm.entities.UpgradeEntity upgrade) {
        org.apache.ambari.server.state.StackId stackId = upgrade.getUpgradePackStackId();
        java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack> packs = m_metaInfo.getUpgradePacks(stackId.getStackName(), stackId.getStackVersion());
        return packs.get(upgrade.getUpgradePackage());
    }

    public org.apache.ambari.spi.upgrade.UpgradeInformation buildUpgradeInformation() {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersionEntity = m_repositoryVersion;
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> clusterServices = m_cluster.getServices();
        java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> clusterServiceVersions = new java.util.HashMap<>();
        if (null != clusterServices) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Service> serviceEntry : clusterServices.entrySet()) {
                org.apache.ambari.server.state.Service service = serviceEntry.getValue();
                org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryEntity = service.getDesiredRepositoryVersion();
                org.apache.ambari.spi.RepositoryVersion desiredRepositoryVersion = desiredRepositoryEntity.getRepositoryVersion();
                clusterServiceVersions.put(serviceEntry.getKey(), desiredRepositoryVersion);
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> sourceVersionEntites = getSourceVersions();
        java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.RepositoryVersionEntity> targetVersionEntites = getTargetVersions();
        java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> sourceVersions = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> targetVersions = new java.util.HashMap<>();
        sourceVersionEntites.forEach((service, repositoryVersion) -> sourceVersions.put(service, repositoryVersion.getRepositoryVersion()));
        targetVersionEntites.forEach((service, repositoryVersion) -> targetVersions.put(service, repositoryVersion.getRepositoryVersion()));
        org.apache.ambari.spi.upgrade.UpgradeInformation upgradeInformation = new org.apache.ambari.spi.upgrade.UpgradeInformation(getDirection().isUpgrade(), getType(), targetRepositoryVersionEntity.getRepositoryVersion(), sourceVersions, targetVersions);
        return upgradeInformation;
    }

    private org.apache.ambari.spi.upgrade.OrchestrationOptions getOrchestrationOptions(org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.stack.upgrade.UpgradePack pack) {
        if (null == pack) {
            return null;
        }
        java.lang.String className = pack.getOrchestrationOptions();
        if (null == className) {
            return null;
        }
        org.apache.ambari.server.state.StackId stackId = pack.getOwnerStackId();
        try {
            org.apache.ambari.server.state.StackInfo stack = metaInfo.getStack(stackId);
            return stack.getLibraryInstance(className);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.LOG.error(java.lang.String.format("Could not load orchestration options for stack {}: {}", stackId, e.getMessage()));
            return null;
        }
    }
}