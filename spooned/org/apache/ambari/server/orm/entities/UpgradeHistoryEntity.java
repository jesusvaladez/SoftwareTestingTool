package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.builder.EqualsBuilder;
@javax.persistence.Entity
@javax.persistence.Table(name = "upgrade_history", uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = { "upgrade_id", "component_name", "service_name" }))
@javax.persistence.TableGenerator(name = "upgrade_history_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "upgrade_history_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "UpgradeHistoryEntity.findAll", query = "SELECT upgradeHistory FROM UpgradeHistoryEntity upgradeHistory"), @javax.persistence.NamedQuery(name = "UpgradeHistoryEntity.findByUpgradeId", query = "SELECT upgradeHistory FROM UpgradeHistoryEntity upgradeHistory WHERE upgradeHistory.upgradeId = :upgradeId") })
public class UpgradeHistoryEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "upgrade_history_id_generator")
    private java.lang.Long id;

    @javax.persistence.Column(name = "upgrade_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long upgradeId;

    @javax.persistence.JoinColumn(name = "upgrade_id", nullable = false)
    private org.apache.ambari.server.orm.entities.UpgradeEntity upgrade;

    @javax.persistence.Column(name = "service_name", nullable = false, insertable = true, updatable = true)
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "component_name", nullable = false, insertable = true, updatable = true)
    private java.lang.String componentName;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "from_repo_version_id", unique = false, nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity fromRepositoryVersion = null;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "target_repo_version_id", unique = false, nullable = false, insertable = true, updatable = true)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepositoryVersion = null;

    public java.lang.Long getId() {
        return id;
    }

    public java.lang.Long getUpgradeId() {
        return upgradeId;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getFromReposistoryVersion() {
        return fromRepositoryVersion;
    }

    public void setFromRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity) {
        fromRepositoryVersion = repositoryVersionEntity;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getTargetRepositoryVersion() {
        return targetRepositoryVersion;
    }

    public java.lang.String getTargetVersion() {
        return targetRepositoryVersion.getVersion();
    }

    public void setTargetRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity) {
        targetRepositoryVersion = repositoryVersionEntity;
    }

    public void setUpgrade(org.apache.ambari.server.orm.entities.UpgradeEntity upgrade) {
        this.upgrade = upgrade;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.UpgradeHistoryEntity that = ((org.apache.ambari.server.orm.entities.UpgradeHistoryEntity) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(id, that.id).append(upgradeId, that.upgradeId).append(serviceName, that.serviceName).append(componentName, that.componentName).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(id, upgradeId, serviceName, componentName);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("id", id).add("upgradeId", upgradeId).add("serviceName", serviceName).add("componentName", componentName).add("from", fromRepositoryVersion).add("to", targetRepositoryVersion).toString();
    }
}