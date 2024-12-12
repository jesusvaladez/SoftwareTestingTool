package org.apache.ambari.server.topology.tasks;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class RegisterWithConfigGroupTask extends org.apache.ambari.server.topology.tasks.TopologyHostTask {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.tasks.RegisterWithConfigGroupTask.class);

    @com.google.inject.assistedinject.AssistedInject
    public RegisterWithConfigGroupTask(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.ClusterTopology topology, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.HostRequest hostRequest) {
        super(topology, hostRequest);
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.tasks.TopologyTask.Type getType() {
        return org.apache.ambari.server.topology.tasks.TopologyTask.Type.CONFIGURE;
    }

    @java.lang.Override
    public void runTask() {
        org.apache.ambari.server.topology.tasks.RegisterWithConfigGroupTask.LOG.info("HostRequest: Executing CONFIGURE task for host: {}", hostRequest.getHostName());
        clusterTopology.getAmbariContext().registerHostWithConfigGroup(hostRequest.getHostName(), clusterTopology, hostRequest.getHostgroupName());
        org.apache.ambari.server.topology.tasks.RegisterWithConfigGroupTask.LOG.info("HostRequest: Exiting CONFIGURE task for host: {}", hostRequest.getHostName());
    }
}