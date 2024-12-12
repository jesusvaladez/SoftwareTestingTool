package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.builder.EqualsBuilder;
@javax.persistence.Entity
@javax.persistence.Table(name = "upgrade")
@javax.persistence.TableGenerator(name = "upgrade_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "upgrade_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "UpgradeEntity.findAll", query = "SELECT u FROM UpgradeEntity u"), @javax.persistence.NamedQuery(name = "UpgradeEntity.findAllForCluster", query = "SELECT u FROM UpgradeEntity u WHERE u.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "UpgradeEntity.findUpgrade", query = "SELECT u FROM UpgradeEntity u WHERE u.upgradeId = :upgradeId"), @javax.persistence.NamedQuery(name = "UpgradeEntity.findUpgradeByRequestId", query = "SELECT u FROM UpgradeEntity u WHERE u.requestId = :requestId"), @javax.persistence.NamedQuery(name = "UpgradeEntity.findLatestForClusterInDirection", query = "SELECT u FROM UpgradeEntity u JOIN RequestEntity r ON u.requestId = r.requestId WHERE u.clusterId = :clusterId AND u.direction = :direction ORDER BY r.startTime DESC, u.upgradeId DESC"), @javax.persistence.NamedQuery(name = "UpgradeEntity.findLatestForCluster", query = "SELECT u FROM UpgradeEntity u JOIN RequestEntity r ON u.requestId = r.requestId WHERE u.clusterId = :clusterId ORDER BY r.startTime DESC"), @javax.persistence.NamedQuery(name = "UpgradeEntity.findAllRequestIds", query = "SELECT upgrade.requestId FROM UpgradeEntity upgrade"), @javax.persistence.NamedQuery(name = "UpgradeEntity.findRevertable", query = "SELECT upgrade FROM UpgradeEntity upgrade WHERE upgrade.revertAllowed = 1 AND upgrade.clusterId = :clusterId ORDER BY upgrade.upgradeId DESC", hints = { @javax.persistence.QueryHint(name = "eclipselink.query-results-cache", value = "true"), @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.ignore-null", value = "false"), @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.size", value = "1") }), @javax.persistence.NamedQuery(name = "UpgradeEntity.findRevertableUsingJPQL", query = "SELECT upgrade FROM UpgradeEntity upgrade WHERE upgrade.repoVersionId IN (SELECT upgrade.repoVersionId FROM UpgradeEntity upgrade WHERE upgrade.clusterId = :clusterId AND upgrade.orchestration IN :revertableTypes GROUP BY upgrade.repoVersionId HAVING MOD(COUNT(upgrade.repoVersionId), 2) != 0) ORDER BY upgrade.upgradeId DESC", hints = { @javax.persistence.QueryHint(name = "eclipselink.query-results-cache", value = "true"), @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.ignore-null", value = "false"), @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.size", value = "1") }) })
public class UpgradeEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "upgrade_id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "upgrade_id_generator")
    private java.lang.Long upgradeId;

    @javax.persistence.Column(name = "cluster_id", nullable = false, insertable = true, updatable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "request_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long requestId;

    @javax.persistence.OneToOne(optional = false, fetch = javax.persistence.FetchType.LAZY)
    @javax.persistence.JoinColumn(name = "request_id", nullable = false, insertable = true, updatable = false)
    private org.apache.ambari.server.orm.entities.RequestEntity requestEntity = null;

    @javax.persistence.Column(name = "direction", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.stack.upgrade.Direction direction = org.apache.ambari.server.stack.upgrade.Direction.UPGRADE;

    @javax.persistence.Column(name = "upgrade_package", nullable = false)
    private java.lang.String upgradePackage;

    @javax.persistence.Column(name = "upgrade_package_stack", nullable = false)
    private java.lang.String upgradePackStack;

    @javax.persistence.Column(name = "upgrade_type", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.spi.upgrade.UpgradeType upgradeType;

    @javax.persistence.Column(name = "repo_version_id", insertable = false, updatable = false)
    private java.lang.Long repoVersionId;

    @javax.persistence.JoinColumn(name = "repo_version_id", referencedColumnName = "repo_version_id", nullable = false)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion;

    @javax.persistence.Column(name = "skip_failures", nullable = false)
    private java.lang.Integer skipFailures = 0;

    @javax.persistence.Column(name = "skip_sc_failures", nullable = false)
    private java.lang.Integer skipServiceCheckFailures = 0;

    @javax.persistence.Column(name = "downgrade_allowed", nullable = false)
    private java.lang.Short downgradeAllowed = 1;

    @javax.persistence.Column(name = "revert_allowed", nullable = false)
    private java.lang.Short revertAllowed = 0;

    @javax.persistence.Column(name = "orchestration", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.spi.RepositoryType orchestration = org.apache.ambari.spi.RepositoryType.STANDARD;

    @javax.persistence.Column(name = "suspended", nullable = false, length = 1)
    private java.lang.Short suspended = 0;

    @javax.persistence.OneToMany(mappedBy = "upgradeEntity", cascade = { javax.persistence.CascadeType.ALL })
    private java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> upgradeGroupEntities;

    @javax.persistence.OneToMany(orphanRemoval = true, cascade = { javax.persistence.CascadeType.ALL })
    @javax.persistence.JoinColumn(name = "upgrade_id")
    private java.util.List<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> upgradeHistory;

    public java.lang.Long getId() {
        return upgradeId;
    }

    public void setId(java.lang.Long id) {
        upgradeId = id;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long id) {
        clusterId = id;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> getUpgradeGroups() {
        return upgradeGroupEntities;
    }

    public void setUpgradeGroups(java.util.List<org.apache.ambari.server.orm.entities.UpgradeGroupEntity> items) {
        for (org.apache.ambari.server.orm.entities.UpgradeGroupEntity entity : items) {
            entity.setUpgradeEntity(this);
        }
        upgradeGroupEntities = items;
    }

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestEntity(org.apache.ambari.server.orm.entities.RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
        requestId = requestEntity.getRequestId();
    }

    public org.apache.ambari.server.stack.upgrade.Direction getDirection() {
        return direction;
    }

    public void setDirection(org.apache.ambari.server.stack.upgrade.Direction direction) {
        this.direction = direction;
    }

    public org.apache.ambari.spi.upgrade.UpgradeType getUpgradeType() {
        return upgradeType;
    }

    public java.lang.Boolean isDowngradeAllowed() {
        return downgradeAllowed != null ? downgradeAllowed != 0 : null;
    }

    public void setDowngradeAllowed(boolean canDowngrade) {
        downgradeAllowed = (!canDowngrade) ? ((short) (0)) : ((short) (1));
    }

    public java.lang.Boolean isRevertAllowed() {
        return revertAllowed != null ? revertAllowed != 0 : null;
    }

    public void setRevertAllowed(boolean revertable) {
        revertAllowed = (!revertable) ? ((short) (0)) : ((short) (1));
    }

    public void setUpgradeType(org.apache.ambari.spi.upgrade.UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
    }

    public java.lang.String getUpgradePackage() {
        return upgradePackage;
    }

    public void setUpgradePackage(java.lang.String upgradePackage) {
        this.upgradePackage = upgradePackage;
    }

    public org.apache.ambari.server.state.StackId getUpgradePackStackId() {
        return null == upgradePackStack ? null : new org.apache.ambari.server.state.StackId(upgradePackStack);
    }

    public void setUpgradePackStackId(org.apache.ambari.server.state.StackId stackId) {
        upgradePackStack = stackId.toString();
    }

    public boolean isComponentFailureAutoSkipped() {
        return skipFailures != 0;
    }

    public void setAutoSkipComponentFailures(boolean autoSkipComponentFailures) {
        skipFailures = (autoSkipComponentFailures) ? 1 : 0;
    }

    public boolean isServiceCheckFailureAutoSkipped() {
        return skipServiceCheckFailures != 0;
    }

    public void setAutoSkipServiceCheckFailures(boolean autoSkipServiceCheckFailures) {
        skipServiceCheckFailures = (autoSkipServiceCheckFailures) ? 1 : 0;
    }

    public boolean isSuspended() {
        return suspended != 0;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = (suspended) ? ((short) (1)) : ((short) (0));
    }

    public void addHistory(org.apache.ambari.server.orm.entities.UpgradeHistoryEntity historicalEntry) {
        if (null == upgradeHistory) {
            upgradeHistory = new java.util.ArrayList<>();
        }
        upgradeHistory.add(historicalEntry);
    }

    public java.util.List<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> getHistory() {
        return upgradeHistory;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getRepositoryVersion() {
        return repositoryVersion;
    }

    public void setRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) {
        this.repositoryVersion = repositoryVersion;
    }

    public void setOrchestration(org.apache.ambari.spi.RepositoryType type) {
        orchestration = type;
    }

    public org.apache.ambari.spi.RepositoryType getOrchestration() {
        return orchestration;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.UpgradeEntity that = ((org.apache.ambari.server.orm.entities.UpgradeEntity) (o));
        return new org.apache.commons.lang.builder.EqualsBuilder().append(upgradeId, that.upgradeId).append(clusterId, that.clusterId).append(requestId, that.requestId).append(direction, that.direction).append(suspended, that.suspended).append(upgradeType, that.upgradeType).append(upgradePackage, that.upgradePackage).isEquals();
    }

    @java.lang.Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(upgradeId, clusterId, requestId, direction, suspended, upgradeType, upgradePackage);
    }

    public void removeHistories(java.util.Collection<org.apache.ambari.server.orm.entities.UpgradeHistoryEntity> upgradeHistoryEntity) {
        if (upgradeHistory != null) {
            upgradeHistory.removeAll(upgradeHistoryEntity);
        }
    }
}