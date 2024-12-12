package org.apache.ambari.server.controller;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class MaintenanceStateHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.MaintenanceStateHelper.class);

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    public MaintenanceStateHelper(com.google.inject.Injector injector) {
        injector.injectMembers(this);
    }

    public boolean isOperationAllowed(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.internal.RequestOperationLevel levelObj, org.apache.ambari.server.controller.internal.RequestResourceFilter reqFilter, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.spi.Resource.Type level;
        if (levelObj == null) {
            level = guessOperationLevel(reqFilter);
        } else {
            level = levelObj.getLevel();
        }
        return isOperationAllowed(cluster, level, serviceName, componentName, hostname);
    }

    boolean isOperationAllowed(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.spi.Resource.Type level, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        if ((serviceName != null) && (!serviceName.isEmpty())) {
            org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            if ((componentName != null) && (!componentName.isEmpty())) {
                org.apache.ambari.server.state.ServiceComponentHost sch = service.getServiceComponent(componentName).getServiceComponentHost(hostname);
                return isOperationAllowed(level, sch);
            } else {
                return isOperationAllowed(level, service);
            }
        } else {
            org.apache.ambari.server.state.Host host = clusters.getHost(hostname);
            return isOperationAllowed(host, cluster.getClusterId(), level);
        }
    }

    public boolean isOperationAllowed(org.apache.ambari.server.controller.spi.Resource.Type level, org.apache.ambari.server.state.Service service) throws org.apache.ambari.server.AmbariException {
        if (level == org.apache.ambari.server.controller.spi.Resource.Type.Cluster) {
            return service.getMaintenanceState() == org.apache.ambari.server.state.MaintenanceState.OFF;
        } else {
            return true;
        }
    }

    public boolean isOperationAllowed(org.apache.ambari.server.state.Host host, long clusterId, org.apache.ambari.server.controller.spi.Resource.Type level) throws org.apache.ambari.server.AmbariException {
        if (level == org.apache.ambari.server.controller.spi.Resource.Type.Cluster) {
            return host.getMaintenanceState(clusterId) == org.apache.ambari.server.state.MaintenanceState.OFF;
        } else {
            return true;
        }
    }

    public org.apache.ambari.server.state.MaintenanceState getEffectiveState(org.apache.ambari.server.state.ServiceComponentHost sch, org.apache.ambari.server.state.Host host) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(sch.getClusterName());
        org.apache.ambari.server.state.Service service = cluster.getService(sch.getServiceName());
        if (null == host) {
            throw new org.apache.ambari.server.HostNotFoundException(cluster.getClusterName(), sch.getHostName());
        }
        return getEffectiveState(cluster.getClusterId(), service, host, sch);
    }

    public org.apache.ambari.server.state.MaintenanceState getEffectiveState(org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Host host = clusters.getHost(sch.getHostName());
        return getEffectiveState(sch, host);
    }

    private org.apache.ambari.server.state.MaintenanceState getEffectiveState(long clusterId, org.apache.ambari.server.state.Service service, org.apache.ambari.server.state.Host host, org.apache.ambari.server.state.ServiceComponentHost sch) {
        org.apache.ambari.server.state.MaintenanceState schState = sch.getMaintenanceState();
        if (org.apache.ambari.server.state.MaintenanceState.ON == schState) {
            return org.apache.ambari.server.state.MaintenanceState.ON;
        }
        org.apache.ambari.server.state.MaintenanceState serviceState = service.getMaintenanceState();
        org.apache.ambari.server.state.MaintenanceState hostState = host.getMaintenanceState(clusterId);
        if ((org.apache.ambari.server.state.MaintenanceState.OFF != serviceState) && (org.apache.ambari.server.state.MaintenanceState.OFF != hostState)) {
            return org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_SERVICE_AND_HOST;
        }
        if (org.apache.ambari.server.state.MaintenanceState.OFF != serviceState) {
            return org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_SERVICE;
        }
        if (org.apache.ambari.server.state.MaintenanceState.OFF != hostState) {
            return org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_HOST;
        }
        return schState;
    }

    public org.apache.ambari.server.state.MaintenanceState getEffectiveState(long clusterId, org.apache.ambari.server.state.Alert alert) throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = alert.getService();
        java.lang.String componentName = alert.getComponent();
        java.lang.String hostName = alert.getHostName();
        if ((null == serviceName) && (null == hostName)) {
            org.apache.ambari.server.controller.MaintenanceStateHelper.LOG.warn("Unable to determine maintenance state for an alert without a service or host");
            return org.apache.ambari.server.state.MaintenanceState.OFF;
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(hostName)) {
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            if (host.getMaintenanceState(clusterId) != org.apache.ambari.server.state.MaintenanceState.OFF) {
                return org.apache.ambari.server.state.MaintenanceState.ON;
            }
        }
        if (org.apache.commons.lang.StringUtils.equals(org.apache.ambari.server.controller.RootService.AMBARI.name(), serviceName)) {
            return org.apache.ambari.server.state.MaintenanceState.OFF;
        }
        org.apache.ambari.server.state.Cluster cluster = clusters.getClusterById(clusterId);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        if (service.getMaintenanceState() != org.apache.ambari.server.state.MaintenanceState.OFF) {
            return org.apache.ambari.server.state.MaintenanceState.ON;
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(componentName)) {
            org.apache.ambari.server.state.ServiceComponent serviceComponent = service.getServiceComponent(componentName);
            org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = serviceComponent.getServiceComponentHost(hostName);
            if (serviceComponentHost.getMaintenanceState() != org.apache.ambari.server.state.MaintenanceState.OFF) {
                return org.apache.ambari.server.state.MaintenanceState.ON;
            }
        }
        return org.apache.ambari.server.state.MaintenanceState.OFF;
    }

    public java.util.Set<java.util.Map<java.lang.String, java.lang.String>> getMaintenanceHostComponents(org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> set = new java.util.HashSet<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = clusters.getHostsForCluster(cluster.getClusterName());
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
                if (sc.isClientComponent()) {
                    continue;
                }
                for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                    org.apache.ambari.server.state.Host host = hosts.get(sch.getHostName());
                    if (org.apache.ambari.server.state.MaintenanceState.OFF != getEffectiveState(cluster.getClusterId(), service, host, sch)) {
                        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
                        map.put("host", sch.getHostName());
                        map.put("service", sch.getServiceName());
                        map.put("component", sch.getServiceComponentName());
                        set.add(map);
                    }
                }
            }
        }
        return set;
    }

    public boolean isOperationAllowed(org.apache.ambari.server.controller.spi.Resource.Type operationLevel, org.apache.ambari.server.state.ServiceComponentHost sch) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.MaintenanceState maintenanceState = getEffectiveState(sch);
        switch (operationLevel.getInternalType()) {
            case Cluster :
                if (maintenanceState.equals(org.apache.ambari.server.state.MaintenanceState.OFF)) {
                    return true;
                }
                break;
            case Service :
                if (maintenanceState.equals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_SERVICE) || maintenanceState.equals(org.apache.ambari.server.state.MaintenanceState.OFF)) {
                    return true;
                }
                break;
            case Host :
                if (maintenanceState.equals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_HOST) || maintenanceState.equals(org.apache.ambari.server.state.MaintenanceState.OFF)) {
                    return true;
                }
                break;
            case HostComponent :
                {
                    return true;
                }
            default :
                org.apache.ambari.server.controller.MaintenanceStateHelper.LOG.warn("Unsupported Resource type, type = " + operationLevel);
                break;
        }
        return false;
    }

    public org.apache.ambari.server.controller.spi.Resource.Type guessOperationLevel(org.apache.ambari.server.controller.internal.RequestResourceFilter filter) {
        org.apache.ambari.server.controller.spi.Resource.Type result;
        if (filter == null) {
            result = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
        } else {
            boolean serviceDefined = filter.getServiceName() != null;
            boolean componentDefined = filter.getComponentName() != null;
            boolean hostsDefined = (filter.getHostNames() != null) && (filter.getHostNames().size() > 0);
            if (hostsDefined & componentDefined) {
                result = org.apache.ambari.server.controller.spi.Resource.Type.HostComponent;
            } else if ((!serviceDefined) & hostsDefined) {
                result = org.apache.ambari.server.controller.spi.Resource.Type.Host;
            } else if (serviceDefined & (!hostsDefined)) {
                result = org.apache.ambari.server.controller.spi.Resource.Type.Service;
            } else {
                result = org.apache.ambari.server.controller.spi.Resource.Type.Cluster;
            }
        }
        return result;
    }

    public java.util.Set<java.lang.String> filterHostsInMaintenanceState(java.util.Set<java.lang.String> candidateHosts, org.apache.ambari.server.controller.MaintenanceStateHelper.HostPredicate condition) throws org.apache.ambari.server.AmbariException {
        java.util.Set<java.lang.String> removedHosts = new java.util.HashSet<>();
        for (java.lang.String hostname : candidateHosts) {
            if (condition.shouldHostBeRemoved(hostname)) {
                removedHosts.add(hostname);
            }
        }
        candidateHosts.removeAll(removedHosts);
        return removedHosts;
    }

    public interface HostPredicate {
        boolean shouldHostBeRemoved(java.lang.String hostname) throws org.apache.ambari.server.AmbariException;
    }
}