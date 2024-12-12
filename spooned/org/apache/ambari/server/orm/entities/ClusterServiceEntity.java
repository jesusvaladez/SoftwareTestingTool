package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.ClusterServiceEntityPK.class)
@javax.persistence.Table(name = "clusterservices")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "clusterServiceByClusterAndServiceNames", query = "SELECT clusterService " + (("FROM ClusterServiceEntity clusterService " + "JOIN clusterService.clusterEntity cluster ") + "WHERE clusterService.serviceName=:serviceName AND cluster.clusterName=:clusterName")) })
@javax.persistence.Entity
public class ClusterServiceEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = false, updatable = false, length = 10)
    private java.lang.Long clusterId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "service_name", nullable = false, insertable = true, updatable = true)
    private java.lang.String serviceName;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "service_enabled", nullable = false, insertable = true, updatable = true, length = 10)
    private java.lang.Integer serviceEnabled = 0;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.OneToOne(mappedBy = "clusterServiceEntity", cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE })
    private org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity;

    @javax.persistence.OneToMany(mappedBy = "clusterServiceEntity")
    private java.util.Collection<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> serviceComponentDesiredStateEntities;

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

    public int getServiceEnabled() {
        return serviceEnabled;
    }

    public void setServiceEnabled(int serviceEnabled) {
        this.serviceEnabled = serviceEnabled;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.ClusterServiceEntity that = ((org.apache.ambari.server.orm.entities.ClusterServiceEntity) (o));
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null)
            return false;

        if (serviceEnabled != null ? !serviceEnabled.equals(that.serviceEnabled) : that.serviceEnabled != null)
            return false;

        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.intValue() : 0;
        result = (31 * result) + (serviceName != null ? serviceName.hashCode() : 0);
        result = (31 * result) + serviceEnabled;
        return result;
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity getServiceDesiredStateEntity() {
        return serviceDesiredStateEntity;
    }

    public void setServiceDesiredStateEntity(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity) {
        this.serviceDesiredStateEntity = serviceDesiredStateEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> getServiceComponentDesiredStateEntities() {
        return serviceComponentDesiredStateEntities;
    }

    public void setServiceComponentDesiredStateEntities(java.util.Collection<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> serviceComponentDesiredStateEntities) {
        this.serviceComponentDesiredStateEntities = serviceComponentDesiredStateEntities;
    }
}