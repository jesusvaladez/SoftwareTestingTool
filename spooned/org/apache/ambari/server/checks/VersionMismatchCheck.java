package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.COMPONENT_VERSION, order = 7.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class VersionMismatchCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription VERSION_MISMATCH = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("VERSION_MISMATCH", org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST, "All components must be reporting the expected version", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "There are components which are not reporting the expected stack version: \n%s").build());

    public VersionMismatchCheck() {
        super(org.apache.ambari.server.checks.VersionMismatchCheck.VERSION_MISMATCH);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = cluster.getServices();
        java.util.List<java.lang.String> errorMessages = new java.util.ArrayList<>();
        for (org.apache.ambari.server.state.Service service : services.values()) {
            validateService(service, result, errorMessages);
        }
        if (!result.getFailedOn().isEmpty()) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING);
            java.lang.String failReason = getFailReason(result, request);
            result.setFailReason(java.lang.String.format(failReason, org.apache.commons.lang.StringUtils.join(errorMessages, "\n")));
            result.setFailReason(org.apache.commons.lang.StringUtils.join(errorMessages, "\n"));
        }
        return result;
    }

    private void validateService(org.apache.ambari.server.state.Service service, org.apache.ambari.spi.upgrade.UpgradeCheckResult result, java.util.List<java.lang.String> errorMessages) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponents = service.getServiceComponents();
        for (org.apache.ambari.server.state.ServiceComponent serviceComponent : serviceComponents.values()) {
            validateServiceComponent(serviceComponent, result, errorMessages);
        }
    }

    private void validateServiceComponent(org.apache.ambari.server.state.ServiceComponent serviceComponent, org.apache.ambari.spi.upgrade.UpgradeCheckResult result, java.util.List<java.lang.String> errorMessages) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = serviceComponent.getServiceComponentHosts();
        for (org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost : serviceComponentHosts.values()) {
            validateServiceComponentHost(serviceComponent, serviceComponentHost, result, errorMessages);
        }
    }

    private void validateServiceComponentHost(org.apache.ambari.server.state.ServiceComponent serviceComponent, org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost, org.apache.ambari.spi.upgrade.UpgradeCheckResult result, java.util.List<java.lang.String> errorMessages) {
        if (serviceComponentHost.getUpgradeState().equals(org.apache.ambari.server.state.UpgradeState.VERSION_MISMATCH)) {
            java.lang.String hostName = serviceComponentHost.getHostName();
            java.lang.String serviceComponentName = serviceComponentHost.getServiceComponentName();
            java.lang.String desiredVersion = serviceComponent.getDesiredVersion();
            java.lang.String actualVersion = serviceComponentHost.getVersion();
            java.lang.String message = (((((hostName + "/") + serviceComponentName) + " desired version: ") + desiredVersion) + ", actual version: ") + actualVersion;
            result.getFailedOn().add(hostName);
            errorMessages.add(message);
        }
    }
}