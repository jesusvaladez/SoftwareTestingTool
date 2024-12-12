package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
@java.lang.SuppressWarnings("serial")
public class ClusterServiceEntityPK implements java.io.Serializable {
    private java.lang.Long clusterId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = true, updatable = true, length = 10)
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    private java.lang.String serviceName;

    @javax.persistence.Id
    @javax.persistence.Column(name = "service_name", nullable = false, insertable = true, updatable = true)
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ClusterServiceEntityPK that = ((org.apache.ambari.server.orm.entities.ClusterServiceEntityPK) (o));
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null)
            return false;

        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.intValue() : 0;
        result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        return result;
    }
}