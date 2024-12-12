package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.LIVELINESS, order = 2.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class ServicesUpCheck extends org.apache.ambari.server.checks.ClusterCheck {
    private static final float SLAVE_THRESHOLD = 0.5F;

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription SERVICES_UP = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("SERVICES_UP", org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE, "All services must be started", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following Services must be started: {{fails}}. Try to do a Stop & Start in case they were started outside of Ambari.").build());

    public ServicesUpCheck() {
        super(org.apache.ambari.server.checks.ServicesUpCheck.SERVICES_UP);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        java.util.List<java.lang.String> errorMessages = new java.util.ArrayList<>();
        java.util.LinkedHashSet<org.apache.ambari.server.checks.ClusterCheck.ServiceDetail> failedServices = new java.util.LinkedHashSet<>();
        java.util.Set<java.lang.String> servicesInUpgrade = checkHelperProvider.get().getServicesInUpgrade(request);
        for (java.lang.String serviceName : servicesInUpgrade) {
            final org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            if (service.isClientOnlyService()) {
                continue;
            }
            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> serviceComponents = service.getServiceComponents();
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponent> component : serviceComponents.entrySet()) {
                org.apache.ambari.server.state.ServiceComponent serviceComponent = component.getValue();
                if (serviceComponent.isClientComponent()) {
                    continue;
                }
                if (!serviceComponent.isVersionAdvertised()) {
                    continue;
                }
                java.util.List<org.apache.ambari.server.orm.models.HostComponentSummary> hostComponentSummaries = org.apache.ambari.server.orm.models.HostComponentSummary.getHostComponentSummaries(service.getName(), serviceComponent.getName());
                if (hostComponentSummaries.isEmpty()) {
                    continue;
                }
                boolean checkThreshold = false;
                if (!serviceComponent.isMasterComponent()) {
                    org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
                    org.apache.ambari.server.state.ComponentInfo componentInfo = ambariMetaInfo.get().getComponent(stackId.getStackName(), stackId.getStackVersion(), serviceComponent.getServiceName(), serviceComponent.getName());
                    java.lang.String cardinality = componentInfo.getCardinality();
                    if ((null != cardinality) && (cardinality.equals("ALL") || cardinality.matches("[1-9].*"))) {
                        checkThreshold = true;
                    }
                }
                if (checkThreshold) {
                    int total = hostComponentSummaries.size();
                    int up = 0;
                    int down = 0;
                    for (org.apache.ambari.server.orm.models.HostComponentSummary summary : hostComponentSummaries) {
                        if (isConsideredDown(cluster, serviceComponent, summary)) {
                            down++;
                        } else {
                            up++;
                        }
                    }
                    if ((((float) (down)) / total) > org.apache.ambari.server.checks.ServicesUpCheck.SLAVE_THRESHOLD) {
                        failedServices.add(new org.apache.ambari.server.checks.ClusterCheck.ServiceDetail(serviceName));
                        java.lang.String message = java.text.MessageFormat.format("{0}: {1} out of {2} {3} are started; there should be {4,number,percent} started before upgrading.", service.getName(), up, total, serviceComponent.getName(), org.apache.ambari.server.checks.ServicesUpCheck.SLAVE_THRESHOLD);
                        errorMessages.add(message);
                    }
                } else {
                    for (org.apache.ambari.server.orm.models.HostComponentSummary summary : hostComponentSummaries) {
                        if (isConsideredDown(cluster, serviceComponent, summary)) {
                            failedServices.add(new org.apache.ambari.server.checks.ClusterCheck.ServiceDetail(serviceName));
                            java.lang.String message = java.text.MessageFormat.format("{0}: {1} (in {2} on host {3})", service.getName(), serviceComponent.getName(), summary.getCurrentState(), summary.getHostName());
                            errorMessages.add(message);
                            break;
                        }
                    }
                }
            }
        }
        if (!errorMessages.isEmpty()) {
            result.setFailedOn(failedServices.stream().map(failedService -> failedService.serviceName).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new)));
            result.getFailedDetail().addAll(failedServices);
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason("The following Service Components should be in a started state.  Please invoke a service Stop and full Start and try again. " + org.apache.commons.lang.StringUtils.join(errorMessages, ", "));
        }
        return result;
    }

    private boolean isConsideredDown(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponent serviceComponent, org.apache.ambari.server.orm.models.HostComponentSummary summary) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host host = clustersProvider.get().getHostById(summary.getHostId());
        org.apache.ambari.server.state.MaintenanceState maintenanceState = host.getMaintenanceState(cluster.getClusterId());
        if ((maintenanceState == org.apache.ambari.server.state.MaintenanceState.ON) && (!serviceComponent.isMasterComponent())) {
            return false;
        }
        org.apache.ambari.server.state.State desiredState = summary.getDesiredState();
        org.apache.ambari.server.state.State currentState = summary.getCurrentState();
        switch (desiredState) {
            case INSTALLED :
            case STARTED :
                return currentState != org.apache.ambari.server.state.State.STARTED;
            default :
                return false;
        }
    }
}