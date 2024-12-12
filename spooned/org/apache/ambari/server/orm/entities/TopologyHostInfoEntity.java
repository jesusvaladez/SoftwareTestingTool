package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "topology_host_info")
@javax.persistence.TableGenerator(name = "topology_host_info_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "topology_host_info_id_seq", initialValue = 0)
public class TopologyHostInfoEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "topology_host_info_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "fqdn", length = 255)
    private java.lang.String fqdn;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "host_id")
    private org.apache.ambari.server.orm.entities.HostEntity hostEntity;

    @javax.persistence.Column(name = "host_count", length = 10)
    private java.lang.Integer hostCount;

    @javax.persistence.Column(name = "predicate", length = 2048)
    private java.lang.String predicate;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private org.apache.ambari.server.orm.entities.TopologyHostGroupEntity topologyHostGroupEntity;

    @javax.persistence.Column(name = "rack_info", length = 255)
    private java.lang.String rackInfo;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getGroupId() {
        return topologyHostGroupEntity.getId();
    }

    public java.lang.String getFqdn() {
        return fqdn;
    }

    public void setFqdn(java.lang.String fqdn) {
        this.fqdn = fqdn;
    }

    public java.lang.Integer getHostCount() {
        return hostCount;
    }

    public void setHostCount(java.lang.Integer hostCount) {
        this.hostCount = hostCount;
    }

    public java.lang.String getPredicate() {
        return predicate;
    }

    public void setPredicate(java.lang.String predicate) {
        this.predicate = predicate;
    }

    public org.apache.ambari.server.orm.entities.TopologyHostGroupEntity getTopologyHostGroupEntity() {
        return topologyHostGroupEntity;
    }

    public void setTopologyHostGroupEntity(org.apache.ambari.server.orm.entities.TopologyHostGroupEntity topologyHostGroupEntity) {
        this.topologyHostGroupEntity = topologyHostGroupEntity;
    }

    public java.lang.String getRackInfo() {
        return rackInfo;
    }

    public void setRackInfo(java.lang.String rackInfo) {
        this.rackInfo = rackInfo;
    }

    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostEntity;
    }

    public void setHostEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        this.hostEntity = hostEntity;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.TopologyHostInfoEntity that = ((org.apache.ambari.server.orm.entities.TopologyHostInfoEntity) (o));
        if (!id.equals(that.id))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }
}