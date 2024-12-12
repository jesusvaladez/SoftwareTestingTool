package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "remoteambariclusterservice")
@javax.persistence.TableGenerator(name = "remote_cluster_service_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "remote_cluster_service_id_seq", initialValue = 1)
@javax.persistence.Entity
public class RemoteAmbariClusterServiceEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "remote_cluster_service_id_generator")
    private java.lang.Long id;

    @javax.persistence.Column(name = "service_name", nullable = false, insertable = true, updatable = false)
    private java.lang.String serviceName;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false)
    private org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity cluster;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity getCluster() {
        return cluster;
    }

    public void setCluster(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity cluster) {
        this.cluster = cluster;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }
}