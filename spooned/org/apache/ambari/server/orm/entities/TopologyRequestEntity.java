package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "topology_request")
@javax.persistence.TableGenerator(name = "topology_request_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "topology_request_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "TopologyRequestEntity.findByClusterId", query = "SELECT req FROM TopologyRequestEntity req WHERE req.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "TopologyRequestEntity.findProvisionRequests", query = "SELECT req FROM TopologyRequestEntity req WHERE req.action = 'PROVISION'") })
public class TopologyRequestEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "topology_request_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private java.lang.Long id;

    @javax.persistence.Column(name = "action", length = 255, nullable = false)
    private java.lang.String action;

    @javax.persistence.Column(name = "cluster_id", nullable = true)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "bp_name", length = 100, nullable = false)
    private java.lang.String blueprintName;

    @javax.persistence.Column(name = "cluster_properties")
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Lob
    private java.lang.String clusterProperties;

    @javax.persistence.Column(name = "cluster_attributes")
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.Lob
    private java.lang.String clusterAttributes;

    @javax.persistence.Column(name = "description", length = 1024, nullable = false)
    private java.lang.String description;

    @javax.persistence.OneToMany(mappedBy = "topologyRequestEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> topologyHostGroupEntities;

    @javax.persistence.OneToOne(mappedBy = "topologyRequestEntity", cascade = javax.persistence.CascadeType.ALL)
    private org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity topologyLogicalRequestEntity;

    @javax.persistence.Column(name = "provision_action", length = 255, nullable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.controller.internal.ProvisionAction provisionAction;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getAction() {
        return action;
    }

    public void setAction(java.lang.String action) {
        this.action = action;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getBlueprintName() {
        return blueprintName;
    }

    public void setBlueprintName(java.lang.String blueprintName) {
        this.blueprintName = blueprintName;
    }

    public java.lang.String getClusterProperties() {
        return clusterProperties;
    }

    public void setClusterProperties(java.lang.String clusterProperties) {
        this.clusterProperties = clusterProperties;
    }

    public java.lang.String getClusterAttributes() {
        return clusterAttributes;
    }

    public void setClusterAttributes(java.lang.String clusterAttributes) {
        this.clusterAttributes = clusterAttributes;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> getTopologyHostGroupEntities() {
        return topologyHostGroupEntities;
    }

    public void setTopologyHostGroupEntities(java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> topologyHostGroupEntities) {
        this.topologyHostGroupEntities = topologyHostGroupEntities;
    }

    public org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity getTopologyLogicalRequestEntity() {
        return topologyLogicalRequestEntity;
    }

    public void setTopologyLogicalRequestEntity(org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity topologyLogicalRequestEntity) {
        this.topologyLogicalRequestEntity = topologyLogicalRequestEntity;
    }

    public org.apache.ambari.server.controller.internal.ProvisionAction getProvisionAction() {
        return provisionAction;
    }

    public void setProvisionAction(org.apache.ambari.server.controller.internal.ProvisionAction provisionAction) {
        this.provisionAction = provisionAction;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.TopologyRequestEntity that = ((org.apache.ambari.server.orm.entities.TopologyRequestEntity) (o));
        if (!id.equals(that.id))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }
}