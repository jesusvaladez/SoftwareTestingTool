package org.apache.ambari.server.topology.tasks;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class InstallHostTask extends org.apache.ambari.server.topology.tasks.TopologyHostTask {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.tasks.InstallHostTask.class);

    @com.google.inject.assistedinject.AssistedInject
    public InstallHostTask(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.ClusterTopology topology, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.HostRequest hostRequest, @com.google.inject.assistedinject.Assisted
    boolean skipFailure) {
        super(topology, hostRequest);
        this.skipFailure = skipFailure;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.tasks.TopologyTask.Type getType() {
        return org.apache.ambari.server.topology.tasks.TopologyTask.Type.INSTALL;
    }

    @java.lang.Override
    public void runTask() {
        org.apache.ambari.server.topology.tasks.InstallHostTask.LOG.info("HostRequest: Executing INSTALL task for host: {}", hostRequest.getHostName());
        boolean skipInstallTaskCreate = clusterTopology.getProvisionAction().equals(org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY);
        org.apache.ambari.server.controller.RequestStatusResponse response = clusterTopology.installHost(hostRequest.getHostName(), skipInstallTaskCreate, skipFailure);
        if (response != null) {
            java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> underlyingTasks = response.getTasks();
            for (org.apache.ambari.server.controller.ShortTaskStatus task : underlyingTasks) {
                java.lang.String component = task.getRole();
                java.lang.Long logicalInstallTaskId = hostRequest.getLogicalTasksForTopologyTask(this).get(component);
                if (logicalInstallTaskId == null) {
                    org.apache.ambari.server.topology.tasks.InstallHostTask.LOG.info("Skipping physical install task registering, because component {} cannot be found", task.getRole());
                    continue;
                }
                long taskId = task.getTaskId();
                hostRequest.registerPhysicalTaskId(logicalInstallTaskId, taskId);
            }
        }
        org.apache.ambari.server.topology.tasks.InstallHostTask.LOG.info("HostRequest: Exiting INSTALL task for host: {}", hostRequest.getHostName());
    }
}