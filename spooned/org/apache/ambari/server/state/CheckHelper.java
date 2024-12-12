package org.apache.ambari.server.state;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class CheckHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.CheckHelper.class);

    @com.google.inject.Inject
    protected com.google.inject.Provider<org.apache.ambari.server.orm.dao.RepositoryVersionDAO> repositoryVersionDaoProvider;

    @com.google.inject.Inject
    protected com.google.inject.Provider<org.apache.ambari.server.state.Clusters> clustersProvider;

    @com.google.inject.Inject
    protected com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo> metaInfoProvider;

    public java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> getApplicableChecks(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request, java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> upgradeChecks) {
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> applicableChecks = new java.util.LinkedList<>();
        for (org.apache.ambari.spi.upgrade.UpgradeCheck check : upgradeChecks) {
            java.util.List<org.apache.ambari.spi.upgrade.CheckQualification> qualifications = com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.state.CheckHelper.ServiceQualification(check), new org.apache.ambari.server.checks.OrchestrationQualification(check.getClass()), new org.apache.ambari.server.checks.UpgradeTypeQualification(check.getClass()));
            java.util.List<org.apache.ambari.spi.upgrade.CheckQualification> checkQualifications = check.getQualifications();
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(checkQualifications)) {
                qualifications.addAll(checkQualifications);
            }
            try {
                boolean checkIsApplicable = true;
                for (org.apache.ambari.spi.upgrade.CheckQualification qualification : qualifications) {
                    if (!qualification.isApplicable(request)) {
                        checkIsApplicable = false;
                        break;
                    }
                }
                if (checkIsApplicable) {
                    applicableChecks.add(check);
                }
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.state.CheckHelper.LOG.error("Unable to determine whether the pre-upgrade check {} is applicable to this upgrade", check.getCheckDescription().name(), ex);
            }
        }
        return applicableChecks;
    }

    public java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheckResult> performChecks(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request, java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> upgradeChecks, org.apache.ambari.server.configuration.Configuration config) {
        final java.lang.String clusterName = request.getClusterName();
        final java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheckResult> results = new java.util.ArrayList<>();
        final boolean canBypassPreChecks = config.isUpgradePrecheckBypass();
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> applicablePreChecks = getApplicableChecks(request, upgradeChecks);
        for (org.apache.ambari.spi.upgrade.UpgradeCheck check : applicablePreChecks) {
            org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(check);
            try {
                result = check.perform(request);
            } catch (org.apache.ambari.server.ClusterNotFoundException ex) {
                result.setFailReason(("Cluster with name " + clusterName) + " doesn't exists");
                result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            } catch (java.lang.Exception ex) {
                org.apache.ambari.server.state.CheckHelper.LOG.error(("Check " + check.getCheckDescription().name()) + " failed", ex);
                result.setFailReason("Unexpected server error happened");
                result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            }
            if ((result.getStatus() == org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL) && canBypassPreChecks) {
                org.apache.ambari.server.state.CheckHelper.LOG.error("Check {} failed but stack upgrade is allowed to bypass failures. Error to bypass: {}. Failed on: {}", check.getCheckDescription().name(), result.getFailReason(), org.apache.commons.lang.StringUtils.join(result.getFailedOn(), ", "));
                result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.BYPASS);
            }
            results.add(result);
            request.addResult(check.getCheckDescription(), result.getStatus());
        }
        return results;
    }

    public final org.apache.ambari.server.state.repository.VersionDefinitionXml getVersionDefinitionXml(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = repositoryVersionDaoProvider.get().findByPK(repositoryVersion.getId());
        try {
            org.apache.ambari.server.state.repository.VersionDefinitionXml vdf = entity.getRepositoryXml();
            return vdf;
        } catch (java.lang.Exception exception) {
            throw new org.apache.ambari.server.AmbariException("Unable to run upgrade checks because of an invalid VDF", exception);
        }
    }

    public final java.util.Set<java.lang.String> getServicesInUpgrade(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(request.getClusterName());
        try {
            org.apache.ambari.server.state.repository.VersionDefinitionXml vdf = getVersionDefinitionXml(request);
            org.apache.ambari.server.state.repository.ClusterVersionSummary clusterVersionSummary = vdf.getClusterSummary(cluster, metaInfoProvider.get());
            return clusterVersionSummary.getAvailableServiceNames();
        } catch (java.lang.Exception exception) {
            throw new org.apache.ambari.server.AmbariException("Unable to run upgrade checks because of an invalid VDF", exception);
        }
    }

    final class ServiceQualification implements org.apache.ambari.spi.upgrade.CheckQualification {
        private final org.apache.ambari.spi.upgrade.UpgradeCheck m_upgradeCheck;

        public ServiceQualification(org.apache.ambari.spi.upgrade.UpgradeCheck upgradeCheck) {
            m_upgradeCheck = upgradeCheck;
        }

        @java.lang.Override
        public boolean isApplicable(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
            java.util.Set<java.lang.String> applicableServices = m_upgradeCheck.getApplicableServices();
            if (applicableServices.isEmpty()) {
                return true;
            }
            java.util.Set<java.lang.String> servicesForUpgrade = getServicesInUpgrade(request);
            for (java.lang.String serviceInUpgrade : servicesForUpgrade) {
                if (applicableServices.contains(serviceInUpgrade)) {
                    return true;
                }
            }
            return false;
        }
    }
}