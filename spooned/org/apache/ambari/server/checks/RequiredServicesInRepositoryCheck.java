package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.REPOSITORY_VERSION, order = 1.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED }, orchestration = { org.apache.ambari.spi.RepositoryType.PATCH, org.apache.ambari.spi.RepositoryType.MAINT, org.apache.ambari.spi.RepositoryType.SERVICE })
public class RequiredServicesInRepositoryCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription VALID_SERVICES_INCLUDED_IN_REPOSITORY = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("VALID_SERVICES_INCLUDED_IN_REPOSITORY", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "The repository is missing services which are required", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following services are included in the upgrade but the repository is missing their dependencies:\n%s").build());

    public RequiredServicesInRepositoryCheck() {
        super(org.apache.ambari.server.checks.RequiredServicesInRepositoryCheck.VALID_SERVICES_INCLUDED_IN_REPOSITORY);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        java.lang.String clusterName = request.getClusterName();
        org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = checkHelperProvider.get().getVersionDefinitionXml(request);
        java.util.Set<java.lang.String> missingDependencies = xml.getMissingDependencies(cluster, ambariMetaInfo.get());
        if (!missingDependencies.isEmpty()) {
            java.lang.String failReasonTemplate = getFailReason(result, request);
            java.lang.String message = java.lang.String.format("The following services are also required to be included in this upgrade: %s", org.apache.commons.lang.StringUtils.join(missingDependencies, ", "));
            result.setFailedOn(new java.util.LinkedHashSet<>(missingDependencies));
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(java.lang.String.format(failReasonTemplate, message));
            java.util.Set<org.apache.ambari.server.checks.ClusterCheck.ServiceDetail> missingServiceDetails = missingDependencies.stream().map(missingService -> new org.apache.ambari.server.checks.ClusterCheck.ServiceDetail(missingService)).collect(java.util.stream.Collectors.toCollection(java.util.TreeSet::new));
            result.getFailedDetail().addAll(missingServiceDetails);
            return result;
        }
        result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
        return result;
    }
}