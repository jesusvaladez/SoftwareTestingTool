package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
@javax.persistence.Entity
@javax.persistence.Table(name = "alert_definition", uniqueConstraints = @javax.persistence.UniqueConstraint(columnNames = { "cluster_id", "definition_name" }))
@javax.persistence.TableGenerator(name = "alert_definition_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "alert_definition_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findAll", query = "SELECT ad FROM AlertDefinitionEntity ad"), @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findAllInCluster", query = "SELECT ad FROM AlertDefinitionEntity ad WHERE ad.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findAllEnabledInCluster", query = "SELECT ad FROM AlertDefinitionEntity ad WHERE ad.clusterId = :clusterId AND ad.enabled = 1"), @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findByName", query = "SELECT ad FROM AlertDefinitionEntity ad WHERE ad.definitionName = :definitionName AND ad.clusterId = :clusterId", hints = { @javax.persistence.QueryHint(name = "eclipselink.query-results-cache", value = "true"), @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.ignore-null", value = "true"), @javax.persistence.QueryHint(name = "eclipselink.query-results-cache.size", value = "5000") }), @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findByService", query = "SELECT ad FROM AlertDefinitionEntity ad WHERE ad.serviceName = :serviceName AND ad.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findByServiceAndComponent", query = "SELECT ad FROM AlertDefinitionEntity ad WHERE ad.serviceName = :serviceName AND ad.componentName = :componentName AND ad.clusterId = :clusterId"), @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findByServiceMaster", query = "SELECT ad FROM AlertDefinitionEntity ad WHERE ad.serviceName IN :services AND ad.scope = :scope AND ad.clusterId = :clusterId AND ad.componentName IS NULL" + " AND ad.sourceType <> org.apache.ambari.server.state.alert.SourceType.AGGREGATE"), @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findByIds", query = "SELECT ad FROM AlertDefinitionEntity ad WHERE ad.definitionId IN :definitionIds"), @javax.persistence.NamedQuery(name = "AlertDefinitionEntity.findBySourceType", query = "SELECT ad FROM AlertDefinitionEntity ad WHERE ad.clusterId = :clusterId AND ad.sourceType = :sourceType") })
public class AlertDefinitionEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "alert_definition_id_generator")
    @javax.persistence.Column(name = "definition_id", nullable = false, updatable = false)
    private java.lang.Long definitionId;

    @javax.persistence.Lob
    @javax.persistence.Basic
    @javax.persistence.Column(name = "alert_source", nullable = false, length = 32672)
    private java.lang.String source;

    @javax.persistence.Column(name = "cluster_id", nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.EAGER)
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", insertable = false, updatable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.Column(name = "component_name", length = 255)
    private java.lang.String componentName;

    @javax.persistence.Column(name = "definition_name", nullable = false, length = 255)
    private java.lang.String definitionName;

    @javax.persistence.Column(name = "label", nullable = true, length = 255)
    private java.lang.String label;

    @javax.persistence.Column(name = "help_url", nullable = true, length = 512)
    private java.lang.String helpURL;

    @javax.persistence.Lob
    @javax.persistence.Basic
    @javax.persistence.Column(name = "description", nullable = true, length = 32672)
    private java.lang.String description;

    @javax.persistence.Column(name = "scope", length = 255)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.alert.Scope scope;

    @javax.persistence.Column(nullable = false)
    private java.lang.Integer enabled = java.lang.Integer.valueOf(1);

    @javax.persistence.Column(nullable = false, length = 64)
    private java.lang.String hash;

    @javax.persistence.Column(name = "schedule_interval", nullable = false)
    private java.lang.Integer scheduleInterval;

    @javax.persistence.Column(name = "service_name", nullable = false, length = 255)
    private java.lang.String serviceName;

    @javax.persistence.Column(name = "source_type", nullable = false, length = 255)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.alert.SourceType sourceType;

    @javax.persistence.Column(name = "ignore_host", nullable = false)
    private java.lang.Integer ignoreHost = java.lang.Integer.valueOf(0);

    @javax.persistence.Column(name = "repeat_tolerance", nullable = false)
    private java.lang.Integer repeatTolerance = java.lang.Integer.valueOf(1);

    @javax.persistence.Column(name = "repeat_tolerance_enabled", nullable = false)
    private java.lang.Short repeatToleranceEnabled = java.lang.Short.valueOf(((short) (0)));

    @javax.persistence.ManyToMany(mappedBy = "alertDefinitions", cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.REFRESH })
    private java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> alertGroups;

    public AlertDefinitionEntity() {
    }

    public java.lang.Long getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(java.lang.Long definitionId) {
        this.definitionId = definitionId;
    }

    public java.lang.String getSource() {
        return source;
    }

    public void setSource(java.lang.String alertSource) {
        source = alertSource;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getCluster() {
        return clusterEntity;
    }

    public void setCluster(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public org.apache.ambari.server.state.alert.Scope getScope() {
        return scope;
    }

    public void setScope(org.apache.ambari.server.state.alert.Scope scope) {
        this.scope = scope;
    }

    public java.lang.String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(java.lang.String definitionName) {
        this.definitionName = definitionName;
    }

    public boolean getEnabled() {
        return !java.util.Objects.equals(enabled, 0);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = (enabled) ? java.lang.Integer.valueOf(1) : java.lang.Integer.valueOf(0);
    }

    public boolean isHostIgnored() {
        return !java.util.Objects.equals(ignoreHost, 0);
    }

    public void setHostIgnored(boolean ignoreHost) {
        this.ignoreHost = (ignoreHost) ? java.lang.Integer.valueOf(1) : java.lang.Integer.valueOf(0);
    }

    public java.lang.String getHash() {
        return hash;
    }

    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }

    public java.lang.Integer getScheduleInterval() {
        return scheduleInterval;
    }

    public void setScheduleInterval(java.lang.Integer scheduleInterval) {
        this.scheduleInterval = scheduleInterval;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public org.apache.ambari.server.state.alert.SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(org.apache.ambari.server.state.alert.SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> getAlertGroups() {
        return java.util.Collections.unmodifiableSet(alertGroups);
    }

    public void setAlertGroups(java.util.Set<org.apache.ambari.server.orm.entities.AlertGroupEntity> alertGroups) {
        this.alertGroups = alertGroups;
    }

    public void setLabel(java.lang.String label) {
        this.label = label;
    }

    public java.lang.String getLabel() {
        return label;
    }

    public java.lang.String getHelpURL() {
        return helpURL;
    }

    public void setHelpURL(java.lang.String helpURL) {
        this.helpURL = helpURL;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public int getRepeatTolerance() {
        return repeatTolerance;
    }

    public void setRepeatTolerance(int repeatTolerance) {
        this.repeatTolerance = repeatTolerance;
    }

    public boolean isRepeatToleranceEnabled() {
        return !java.util.Objects.equals(repeatToleranceEnabled, ((short) (0)));
    }

    public void setRepeatToleranceEnabled(boolean enabled) {
        repeatToleranceEnabled = (enabled) ? java.lang.Short.valueOf(((short) (1))) : 0;
    }

    protected void addAlertGroup(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup) {
        if (null == alertGroups) {
            alertGroups = new java.util.HashSet<>();
        }
        alertGroups.add(alertGroup);
    }

    protected void removeAlertGroup(org.apache.ambari.server.orm.entities.AlertGroupEntity alertGroup) {
        if ((null != alertGroups) && alertGroups.contains(alertGroup)) {
            alertGroups.remove(alertGroup);
        }
    }

    @javax.persistence.PreRemove
    public void preRemove() {
        if ((null == alertGroups) || (alertGroups.size() == 0)) {
            return;
        }
        java.util.Iterator<org.apache.ambari.server.orm.entities.AlertGroupEntity> iterator = alertGroups.iterator();
        while (iterator.hasNext()) {
            org.apache.ambari.server.orm.entities.AlertGroupEntity group = iterator.next();
            iterator.remove();
            group.removeAlertDefinition(this);
        } 
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.AlertDefinitionEntity that = ((org.apache.ambari.server.orm.entities.AlertDefinitionEntity) (object));
        if (null != definitionId) {
            return java.util.Objects.equals(definitionId, that.definitionId);
        }
        return (java.util.Objects.equals(definitionId, that.definitionId) && java.util.Objects.equals(clusterId, that.clusterId)) && java.util.Objects.equals(definitionName, that.definitionName);
    }

    @java.lang.Override
    public int hashCode() {
        if (null != definitionId) {
            return definitionId.hashCode();
        }
        return java.util.Objects.hash(definitionId, clusterId, definitionName);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        buffer.append(getClass().getSimpleName());
        buffer.append("{");
        buffer.append("id=").append(definitionId);
        buffer.append(", name=").append(definitionName);
        buffer.append(", serviceName=").append(serviceName);
        buffer.append(", componentName=").append(componentName);
        buffer.append(", enabled=").append(enabled);
        buffer.append(", hash=").append(hash);
        buffer.append("}");
        return buffer.toString();
    }
}