package org.apache.ambari.server.checks;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.MAINTENANCE_MODE, order = 6.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class ServicesMaintenanceModeCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription SERVICES_MAINTENANCE_MODE = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("SERVICES_MAINTENANCE_MODE", org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE, "No services can be in Maintenance Mode", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following Services must not be in Maintenance Mode: {{fails}}.").build());

    public ServicesMaintenanceModeCheck() {
        super(org.apache.ambari.server.checks.ServicesMaintenanceModeCheck.SERVICES_MAINTENANCE_MODE);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(request.getClusterName());
        java.util.Set<java.lang.String> servicesInUpgrade = checkHelperProvider.get().getServicesInUpgrade(request);
        for (java.lang.String serviceName : servicesInUpgrade) {
            final org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            if ((!service.isClientOnlyService()) && (service.getMaintenanceState() == org.apache.ambari.server.state.MaintenanceState.ON)) {
                result.getFailedOn().add(service.getName());
            }
        }
        if (!result.getFailedOn().isEmpty()) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(getFailReason(result, request));
        }
        return result;
    }
}