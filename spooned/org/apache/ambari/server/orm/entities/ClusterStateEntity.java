package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import static org.apache.commons.lang.StringUtils.defaultString;
@javax.persistence.Table(name = "clusterstate")
@javax.persistence.Entity
public class ClusterStateEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long clusterId;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "current_cluster_state", insertable = true, updatable = true)
    private java.lang.String currentClusterState = "";

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "current_stack_id", unique = false, nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.orm.entities.StackEntity currentStack;

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getCurrentClusterState() {
        return StringUtils.defaultString(currentClusterState);
    }

    public void setCurrentClusterState(java.lang.String currentClusterState) {
        this.currentClusterState = currentClusterState;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getCurrentStack() {
        return currentStack;
    }

    public void setCurrentStack(org.apache.ambari.server.orm.entities.StackEntity currentStack) {
        this.currentStack = currentStack;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.ClusterStateEntity that = ((org.apache.ambari.server.orm.entities.ClusterStateEntity) (o));
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null) {
            return false;
        }
        if (currentClusterState != null ? !currentClusterState.equals(that.currentClusterState) : that.currentClusterState != null) {
            return false;
        }
        if (currentStack != null ? !currentStack.equals(that.currentStack) : that.currentStack != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.intValue() : 0;
        result = (31 * result) + (currentClusterState != null ? currentClusterState.hashCode() : 0);
        return result;
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }
}