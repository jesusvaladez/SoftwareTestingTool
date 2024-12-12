package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class ConfigsUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    private java.lang.Long serviceConfigId;

    private java.lang.Long clusterId;

    private java.lang.String serviceName;

    private java.lang.Long groupId;

    private java.lang.Long version;

    private java.lang.String user;

    private java.lang.String note;

    private java.util.List<java.lang.String> hostNames;

    private java.lang.Long createTime;

    private java.lang.String groupName;

    private java.util.List<org.apache.ambari.server.events.ConfigsUpdateEvent.ClusterConfig> configs = new java.util.ArrayList<>();

    private java.util.Set<java.lang.String> changedConfigTypes = new java.util.HashSet<>();

    public ConfigsUpdateEvent(org.apache.ambari.server.orm.entities.ServiceConfigEntity configs, java.lang.String configGroupName, java.util.List<java.lang.String> hostNames, java.util.Set<java.lang.String> changedConfigTypes) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.CONFIGS);
        this.serviceConfigId = configs.getServiceConfigId();
        this.clusterId = configs.getClusterEntity().getClusterId();
        this.serviceName = configs.getServiceName();
        this.groupId = configs.getGroupId();
        this.version = configs.getVersion();
        this.user = configs.getUser();
        this.note = configs.getNote();
        this.hostNames = (hostNames == null) ? null : new java.util.ArrayList<>(hostNames);
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : configs.getClusterConfigEntities()) {
            this.configs.add(new org.apache.ambari.server.events.ConfigsUpdateEvent.ClusterConfig(clusterConfigEntity.getClusterId(), clusterConfigEntity.getType(), clusterConfigEntity.getTag(), clusterConfigEntity.getVersion()));
        }
        this.createTime = configs.getCreateTimestamp();
        this.groupName = configGroupName;
        this.changedConfigTypes = changedConfigTypes;
    }

    public ConfigsUpdateEvent(org.apache.ambari.server.state.Cluster cluster, java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> configs) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.CONFIGS);
        this.clusterId = cluster.getClusterId();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : configs) {
            this.configs.add(new org.apache.ambari.server.events.ConfigsUpdateEvent.ClusterConfig(clusterConfigEntity.getClusterId(), clusterConfigEntity.getType(), clusterConfigEntity.getTag(), clusterConfigEntity.getVersion()));
        }
    }

    public java.lang.Long getServiceConfigId() {
        return serviceConfigId;
    }

    public void setServiceConfigId(java.lang.Long serviceConfigId) {
        this.serviceConfigId = serviceConfigId;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.Long getGroupId() {
        return groupId;
    }

    public void setGroupId(java.lang.Long groupId) {
        this.groupId = groupId;
    }

    public java.lang.Long getVersion() {
        return version;
    }

    public void setVersion(java.lang.Long version) {
        this.version = version;
    }

    public java.lang.String getUser() {
        return user;
    }

    public void setUser(java.lang.String user) {
        this.user = user;
    }

    public java.lang.String getNote() {
        return note;
    }

    public void setNote(java.lang.String note) {
        this.note = note;
    }

    public java.util.List<java.lang.String> getHostNames() {
        return hostNames;
    }

    public void setHostNames(java.util.List<java.lang.String> hostNames) {
        this.hostNames = hostNames;
    }

    public java.util.List<org.apache.ambari.server.events.ConfigsUpdateEvent.ClusterConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(java.util.List<org.apache.ambari.server.events.ConfigsUpdateEvent.ClusterConfig> configs) {
        this.configs = configs;
    }

    public java.lang.Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.lang.Long createTime) {
        this.createTime = createTime;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    public java.util.Set<java.lang.String> getChangedConfigTypes() {
        return changedConfigTypes;
    }

    public void setChangedConfigTypes(java.util.Set<java.lang.String> changedConfigTypes) {
        this.changedConfigTypes = changedConfigTypes;
    }

    public class ClusterConfig {
        private java.lang.Long clusterId;

        private java.lang.String type;

        private java.lang.String tag;

        private java.lang.Long version;

        public ClusterConfig(java.lang.Long clusterId, java.lang.String type, java.lang.String tag, java.lang.Long version) {
            this.clusterId = clusterId;
            this.type = type;
            this.tag = tag;
            this.version = version;
        }

        public java.lang.Long getClusterId() {
            return clusterId;
        }

        public void setClusterId(java.lang.Long clusterId) {
            this.clusterId = clusterId;
        }

        public java.lang.String getTag() {
            return tag;
        }

        public void setTag(java.lang.String tag) {
            this.tag = tag;
        }

        public java.lang.Long getVersion() {
            return version;
        }

        public void setVersion(java.lang.Long version) {
            this.version = version;
        }

        public java.lang.String getType() {
            return type;
        }

        public void setType(java.lang.String type) {
            this.type = type;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.events.ConfigsUpdateEvent.ClusterConfig that = ((org.apache.ambari.server.events.ConfigsUpdateEvent.ClusterConfig) (o));
            if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null)
                return false;

            if (type != null ? !type.equals(that.type) : that.type != null)
                return false;

            if (tag != null ? !tag.equals(that.tag) : that.tag != null)
                return false;

            return version != null ? version.equals(that.version) : that.version == null;
        }

        @java.lang.Override
        public int hashCode() {
            int result = (clusterId != null) ? clusterId.hashCode() : 0;
            result = (31 * result) + (type != null ? type.hashCode() : 0);
            result = (31 * result) + (tag != null ? tag.hashCode() : 0);
            result = (31 * result) + (version != null ? version.hashCode() : 0);
            return result;
        }
    }
}