package org.apache.ambari.server.topology.tasks;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class StartHostTask extends org.apache.ambari.server.topology.tasks.TopologyHostTask {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.tasks.StartHostTask.class);

    @com.google.inject.assistedinject.AssistedInject
    public StartHostTask(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.ClusterTopology topology, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.HostRequest hostRequest, @com.google.inject.assistedinject.Assisted
    boolean skipFailure) {
        super(topology, hostRequest);
        this.skipFailure = skipFailure;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.tasks.TopologyTask.Type getType() {
        return org.apache.ambari.server.topology.tasks.TopologyTask.Type.START;
    }

    @java.lang.Override
    public void runTask() {
        org.apache.ambari.server.topology.tasks.StartHostTask.LOG.info("HostRequest: Executing START task for host: {}", hostRequest.getHostName());
        org.apache.ambari.server.controller.RequestStatusResponse response = clusterTopology.startHost(hostRequest.getHostName(), skipFailure);
        if (response != null) {
            java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> underlyingTasks = response.getTasks();
            for (org.apache.ambari.server.controller.ShortTaskStatus task : underlyingTasks) {
                java.lang.String component = task.getRole();
                java.lang.Long logicalStartTaskId = hostRequest.getLogicalTasksForTopologyTask(this).get(component);
                if (logicalStartTaskId == null) {
                    org.apache.ambari.server.topology.tasks.StartHostTask.LOG.info("Skipping physical start task registering, because component {} cannot be found", task.getRole());
                    continue;
                }
                hostRequest.registerPhysicalTaskId(logicalStartTaskId, task.getTaskId());
            }
        }
        org.apache.ambari.server.topology.tasks.StartHostTask.LOG.info("HostRequest: Exiting START task for host: {}", hostRequest.getHostName());
    }
}