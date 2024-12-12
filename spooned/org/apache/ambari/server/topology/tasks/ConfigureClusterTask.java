package org.apache.ambari.server.topology.tasks;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class ConfigureClusterTask implements java.util.concurrent.Callable<java.lang.Boolean> {
    private static final long DEFAULT_TIMEOUT = java.util.concurrent.TimeUnit.MINUTES.toMillis(30);

    private static final long REPEAT_DELAY = java.util.concurrent.TimeUnit.SECONDS.toMillis(1);

    private static final java.lang.String TIMEOUT_PROPERTY_NAME = "cluster_configure_task_timeout";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.tasks.ConfigureClusterTask.class);

    private final org.apache.ambari.server.topology.ClusterConfigurationRequest configRequest;

    private final org.apache.ambari.server.topology.ClusterTopology topology;

    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    private final java.util.Map<java.lang.String, java.lang.Integer> previousHostCounts = com.google.common.collect.Maps.newHashMap();

    private final java.util.Set<java.lang.String> missingHostGroups = com.google.common.collect.Sets.newHashSet();

    @com.google.inject.assistedinject.AssistedInject
    public ConfigureClusterTask(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.ClusterTopology topology, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.ClusterConfigurationRequest configRequest, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher) {
        this.configRequest = configRequest;
        this.topology = topology;
        this.ambariEventPublisher = ambariEventPublisher;
    }

    @java.lang.Override
    @org.apache.ambari.server.security.authorization.internal.RunWithInternalSecurityContext(token = org.apache.ambari.server.topology.TopologyManager.INTERNAL_AUTH_TOKEN)
    public java.lang.Boolean call() throws java.lang.Exception {
        org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.debug("Entering");
        java.util.Collection<java.lang.String> requiredHostGroups = getTopologyRequiredHostGroups();
        if (!areHostGroupsResolved(requiredHostGroups)) {
            java.lang.String msg = "Some host groups require more hosts, cluster configuration cannot begin";
            org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.info(msg);
            throw new org.apache.ambari.server.topology.AsyncCallableService.RetryTaskSilently(msg);
        }
        org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.info("All required host groups are complete, cluster configuration can now begin");
        configRequest.process();
        org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.info("Cluster configuration finished successfully");
        notifyListeners();
        org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.debug("Exiting");
        return true;
    }

    public long getTimeout() {
        long timeout = org.apache.ambari.server.topology.tasks.ConfigureClusterTask.DEFAULT_TIMEOUT;
        java.lang.String timeoutStr = topology.getConfiguration().getPropertyValue(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, org.apache.ambari.server.topology.tasks.ConfigureClusterTask.TIMEOUT_PROPERTY_NAME);
        if (timeoutStr != null) {
            try {
                timeout = java.lang.Long.parseLong(timeoutStr);
                org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.info("Using custom timeout: {} ms", timeout);
            } catch (java.lang.NumberFormatException e) {
            }
        }
        return timeout;
    }

    public long getRepeatDelay() {
        return org.apache.ambari.server.topology.tasks.ConfigureClusterTask.REPEAT_DELAY;
    }

    private java.util.Collection<java.lang.String> getTopologyRequiredHostGroups() {
        try {
            return configRequest.getRequiredHostGroups();
        } catch (java.lang.RuntimeException e) {
            org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.error("Could not determine required host groups", e);
            return java.util.Collections.emptyList();
        }
    }

    private boolean areHostGroupsResolved(java.util.Collection<java.lang.String> requiredHostGroups) {
        boolean allHostGroupsResolved = true;
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = topology.getHostGroupInfo();
        for (java.lang.String hostGroup : requiredHostGroups) {
            org.apache.ambari.server.topology.HostGroupInfo groupInfo = hostGroupInfo.get(hostGroup);
            if (groupInfo == null) {
                allHostGroupsResolved = false;
                if (missingHostGroups.add(hostGroup)) {
                    org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.warn("Host group '{}' is missing from cluster creation request", hostGroup);
                }
            } else {
                int actualHostCount = groupInfo.getHostNames().size();
                int requestedHostCount = groupInfo.getRequestedHostCount();
                boolean hostGroupReady = actualHostCount >= requestedHostCount;
                allHostGroupsResolved &= hostGroupReady;
                java.lang.Integer previousHostCount = previousHostCounts.put(hostGroup, actualHostCount);
                if ((previousHostCount == null) || (previousHostCount != actualHostCount)) {
                    if (hostGroupReady) {
                        org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.info("Host group '{}' resolved, requires {} hosts and {} are available", groupInfo.getHostGroupName(), requestedHostCount, actualHostCount);
                    } else {
                        org.apache.ambari.server.topology.tasks.ConfigureClusterTask.LOG.info("Host group '{}' pending, requires {} hosts, but only {} are available", groupInfo.getHostGroupName(), requestedHostCount, actualHostCount);
                    }
                }
            }
        }
        return allHostGroupsResolved;
    }

    private void notifyListeners() throws org.apache.ambari.server.AmbariException {
        long clusterId = topology.getClusterId();
        java.lang.String clusterName = topology.getAmbariContext().getClusterName(clusterId);
        ambariEventPublisher.publish(new org.apache.ambari.server.events.ClusterConfigFinishedEvent(clusterId, clusterName));
    }
}