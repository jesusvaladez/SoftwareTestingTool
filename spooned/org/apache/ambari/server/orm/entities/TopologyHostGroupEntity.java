package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "topology_hostgroup")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "TopologyHostGroupEntity.findByRequestIdAndName", query = "SELECT req FROM TopologyHostGroupEntity req WHERE req.topologyRequestEntity.id = :requestId AND req.name = :name") })
@javax.persistence.TableGenerator(name = "topology_host_group_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "topology_host_group_id_seq", initialValue = 0)
public class TopologyHostGroupEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "topology_host_group_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "name", nullable = false, updatable = false)
    private java.lang.String name;

    @javax.persistence.Column(name = "group_properties")
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Lob
    private java.lang.String groupProperties;

    @javax.persistence.Column(name = "group_attributes")
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Lob
    private java.lang.String groupAttributes;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "request_id", referencedColumnName = "id", nullable = false)
    private org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity;

    @javax.persistence.OneToMany(mappedBy = "topologyHostGroupEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> topologyHostInfoEntities;

    @javax.persistence.OneToMany(mappedBy = "topologyHostGroupEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> topologyHostRequestEntities;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getGroupProperties() {
        return groupProperties;
    }

    public void setGroupProperties(java.lang.String groupProperties) {
        this.groupProperties = groupProperties;
    }

    public java.lang.String getGroupAttributes() {
        return groupAttributes;
    }

    public void setGroupAttributes(java.lang.String groupAttributes) {
        this.groupAttributes = groupAttributes;
    }

    public java.lang.Long getRequestId() {
        return topologyRequestEntity != null ? topologyRequestEntity.getId() : null;
    }

    public org.apache.ambari.server.orm.entities.TopologyRequestEntity getTopologyRequestEntity() {
        return topologyRequestEntity;
    }

    public void setTopologyRequestEntity(org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity) {
        this.topologyRequestEntity = topologyRequestEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> getTopologyHostInfoEntities() {
        return topologyHostInfoEntities;
    }

    public void setTopologyHostInfoEntities(java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> topologyHostInfoEntities) {
        this.topologyHostInfoEntities = topologyHostInfoEntities;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> getTopologyHostRequestEntities() {
        return topologyHostRequestEntities;
    }

    public void setTopologyHostRequestEntities(java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> topologyHostRequestEntities) {
        this.topologyHostRequestEntities = topologyHostRequestEntities;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.TopologyHostGroupEntity that = ((org.apache.ambari.server.orm.entities.TopologyHostGroupEntity) (o));
        if (!name.equals(that.name))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return name.hashCode();
    }
}