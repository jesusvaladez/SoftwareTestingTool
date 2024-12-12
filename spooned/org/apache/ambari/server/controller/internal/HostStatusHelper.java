package org.apache.ambari.server.controller.internal;
public class HostStatusHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.HostStatusHelper.class);

    public static boolean isHostComponentLive(org.apache.ambari.server.controller.AmbariManagementController managementController, java.lang.String clusterName, java.lang.String hostName, java.lang.String serviceName, java.lang.String componentName) {
        if (clusterName == null) {
            return false;
        }
        org.apache.ambari.server.controller.ServiceComponentHostResponse componentHostResponse;
        try {
            org.apache.ambari.server.controller.ServiceComponentHostRequest componentRequest = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName, hostName, null);
            java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> hostComponents = managementController.getHostComponents(java.util.Collections.singleton(componentRequest));
            componentHostResponse = (hostComponents.size() == 1) ? hostComponents.iterator().next() : null;
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.internal.HostStatusHelper.LOG.debug("Error checking {} server host component state: ", componentName, e);
            return false;
        }
        return (componentHostResponse != null) && componentHostResponse.getLiveState().equals(org.apache.ambari.server.state.State.STARTED.name());
    }

    public static boolean isHostLive(org.apache.ambari.server.controller.AmbariManagementController managementController, java.lang.String clusterName, java.lang.String hostName) {
        if (clusterName == null) {
            return false;
        }
        org.apache.ambari.server.controller.HostResponse hostResponse;
        try {
            org.apache.ambari.server.controller.HostRequest hostRequest = new org.apache.ambari.server.controller.HostRequest(hostName, clusterName);
            java.util.Set<org.apache.ambari.server.controller.HostResponse> hosts = org.apache.ambari.server.controller.internal.HostResourceProvider.getHosts(managementController, hostRequest, null);
            hostResponse = (hosts.size() == 1) ? hosts.iterator().next() : null;
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.internal.HostStatusHelper.LOG.debug("Error while checking host live status: ", e);
            return false;
        }
        return (hostResponse != null) && (!hostResponse.getHostState().equals(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST));
    }
}