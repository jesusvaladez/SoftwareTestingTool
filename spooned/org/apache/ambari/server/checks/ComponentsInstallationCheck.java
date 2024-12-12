package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.LIVELINESS, order = 2.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class ComponentsInstallationCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription COMPONENTS_INSTALLATION = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("COMPONENTS_INSTALLATION", org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE, "All service components must be installed", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following Services must be reinstalled: {{fails}}. Try to reinstall the service components in INSTALL_FAILED state.").build());

    public ComponentsInstallationCheck() {
        super(org.apache.ambari.server.checks.ComponentsInstallationCheck.COMPONENTS_INSTALLATION);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        java.util.Set<java.lang.String> failedServiceNames = new java.util.HashSet<>();
        java.util.Set<java.lang.String> installFailedHostComponents = new java.util.HashSet<>();
        java.util.Set<java.lang.String> servicesInUpgrade = checkHelperProvider.get().getServicesInUpgrade(request);
        for (java.lang.String serviceName : servicesInUpgrade) {
            final org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            if (service.getMaintenanceState() == org.apache.ambari.server.state.MaintenanceState.ON) {
                continue;
            }
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponents = service.getServiceComponents();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponent> component : serviceComponents.entrySet()) {
                org.apache.ambari.server.state.ServiceComponent serviceComponent = component.getValue();
                if (serviceComponent.isVersionAdvertised()) {
                    java.util.List<org.apache.ambari.server.orm.models.HostComponentSummary> hostComponentSummaries = org.apache.ambari.server.orm.models.HostComponentSummary.getHostComponentSummaries(service.getName(), serviceComponent.getName());
                    for (org.apache.ambari.server.orm.models.HostComponentSummary hcs : hostComponentSummaries) {
                        org.apache.ambari.server.state.Host host = clustersProvider.get().getHost(hcs.getHostName());
                        if (host.getMaintenanceState(cluster.getClusterId()) != org.apache.ambari.server.state.MaintenanceState.ON) {
                            if (hcs.getCurrentState() == org.apache.ambari.server.state.State.INSTALL_FAILED) {
                                result.getFailedDetail().add(hcs);
                                failedServiceNames.add(service.getName());
                                installFailedHostComponents.add(java.text.MessageFormat.format("[{0}:{1} on {2}]", service.getName(), serviceComponent.getName(), hcs.getHostName()));
                            }
                        }
                    }
                }
            }
        }
        if (!installFailedHostComponents.isEmpty()) {
            java.lang.String message = java.text.MessageFormat.format("Service components in INSTALL_FAILED state: {0}.", org.apache.commons.lang.StringUtils.join(installFailedHostComponents, ", "));
            result.setFailedOn(new java.util.LinkedHashSet<>(failedServiceNames));
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason("Found service components in INSTALL_FAILED state. Please re-install these components. " + message);
        }
        return result;
    }
}