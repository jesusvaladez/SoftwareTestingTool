package org.apache.ambari.server.events;
public class HostComponentVersionAdvertisedEvent extends org.apache.ambari.server.events.ClusterEvent {
    protected org.apache.ambari.server.state.Cluster cluster;

    protected org.apache.ambari.server.state.ServiceComponentHost sch;

    protected java.lang.String version;

    protected java.lang.Long repoVersionId;

    public HostComponentVersionAdvertisedEvent(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponentHost sch, java.lang.String version, java.lang.Long repoVersionId) {
        this(cluster, sch, version);
        this.repoVersionId = repoVersionId;
    }

    public HostComponentVersionAdvertisedEvent(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.ServiceComponentHost sch, java.lang.String version) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.HOST_COMPONENT_VERSION_ADVERTISED, cluster.getClusterId());
        this.cluster = cluster;
        this.sch = sch;
        this.version = version;
    }

    public org.apache.ambari.server.state.ServiceComponentHost getServiceComponentHost() {
        return sch;
    }

    public org.apache.ambari.server.state.Cluster getCluster() {
        return cluster;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public java.lang.Long getRepositoryVersionId() {
        return repoVersionId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("HostComponentVersionAdvertisedEvent{");
        buffer.append("cluserId=").append(m_clusterId);
        buffer.append(", serviceName=").append(sch.getServiceName());
        buffer.append(", componentName=").append(sch.getServiceComponentName());
        buffer.append(", hostName=").append(sch.getHostName());
        buffer.append(", version=").append(version);
        buffer.append(", repo_version_id=").append(repoVersionId);
        buffer.append("}");
        return buffer.toString();
    }
}