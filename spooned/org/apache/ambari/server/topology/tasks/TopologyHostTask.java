package org.apache.ambari.server.topology.tasks;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
public abstract class TopologyHostTask implements org.apache.ambari.server.topology.tasks.TopologyTask {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.tasks.TopologyHostTask.class);

    org.apache.ambari.server.topology.ClusterTopology clusterTopology;

    org.apache.ambari.server.topology.HostRequest hostRequest;

    boolean skipFailure;

    public TopologyHostTask(org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.topology.HostRequest hostRequest) {
        this.clusterTopology = topology;
        this.hostRequest = hostRequest;
    }

    public org.apache.ambari.server.topology.HostRequest getHostRequest() {
        return hostRequest;
    }

    @java.lang.Override
    public void run() {
        org.springframework.security.core.Authentication savedAuthContext = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        try {
            org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken authenticationToken = new org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken(org.apache.ambari.server.topology.TopologyManager.INTERNAL_AUTH_TOKEN);
            authenticationToken.setAuthenticated(true);
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            runTask();
        } finally {
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(savedAuthContext);
        }
    }

    public abstract void runTask();
}