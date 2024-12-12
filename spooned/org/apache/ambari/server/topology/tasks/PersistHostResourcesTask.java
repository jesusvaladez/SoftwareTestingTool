package org.apache.ambari.server.topology.tasks;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class PersistHostResourcesTask extends org.apache.ambari.server.topology.tasks.TopologyHostTask {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.tasks.PersistHostResourcesTask.class);

    @com.google.inject.assistedinject.AssistedInject
    public PersistHostResourcesTask(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.ClusterTopology topology, @com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.topology.HostRequest hostRequest) {
        super(topology, hostRequest);
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.tasks.TopologyTask.Type getType() {
        return org.apache.ambari.server.topology.tasks.TopologyTask.Type.RESOURCE_CREATION;
    }

    @java.lang.Override
    public void runTask() {
        org.apache.ambari.server.topology.tasks.PersistHostResourcesTask.LOG.info("HostRequest: Executing RESOURCE_CREATION task for host: {}", hostRequest.getHostName());
        org.apache.ambari.server.topology.HostGroup group = hostRequest.getHostGroup();
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponents = new java.util.HashMap<>();
        for (java.lang.String service : group.getServices()) {
            serviceComponents.put(service, new java.util.HashSet<>(group.getComponents(service)));
        }
        clusterTopology.getAmbariContext().createAmbariHostResources(hostRequest.getClusterId(), hostRequest.getHostName(), serviceComponents);
        org.apache.ambari.server.topology.tasks.PersistHostResourcesTask.LOG.info("HostRequest: Exiting RESOURCE_CREATION task for host: {}", hostRequest.getHostName());
    }
}