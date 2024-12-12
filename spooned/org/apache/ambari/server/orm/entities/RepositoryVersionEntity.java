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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang.StringUtils;
@javax.persistence.Entity
@javax.persistence.Table(name = "repo_version", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "display_name" }), @javax.persistence.UniqueConstraint(columnNames = { "stack_id", "version" }) })
@javax.persistence.TableGenerator(name = "repository_version_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "repo_version_id_seq")
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "repositoryVersionByDisplayName", query = "SELECT repoversion FROM RepositoryVersionEntity repoversion WHERE repoversion.displayName=:displayname"), @javax.persistence.NamedQuery(name = "repositoryVersionByStack", query = "SELECT repoversion FROM RepositoryVersionEntity repoversion WHERE repoversion.stack.stackName=:stackName AND repoversion.stack.stackVersion=:stackVersion"), @javax.persistence.NamedQuery(name = "repositoryVersionByStackAndType", query = "SELECT repoversion FROM RepositoryVersionEntity repoversion WHERE repoversion.stack.stackName=:stackName AND repoversion.stack.stackVersion=:stackVersion AND repoversion.type=:type"), @javax.persistence.NamedQuery(name = "repositoryVersionByStackNameAndVersion", query = "SELECT repoversion FROM RepositoryVersionEntity repoversion WHERE repoversion.stack.stackName=:stackName AND repoversion.version=:version"), @javax.persistence.NamedQuery(name = "repositoryVersionsFromDefinition", query = "SELECT repoversion FROM RepositoryVersionEntity repoversion WHERE repoversion.versionXsd IS NOT NULL"), @javax.persistence.NamedQuery(name = "findRepositoryByVersion", query = "SELECT repositoryVersion FROM RepositoryVersionEntity repositoryVersion WHERE repositoryVersion.version = :version ORDER BY repositoryVersion.id DESC"), @javax.persistence.NamedQuery(name = "findByServiceDesiredVersion", query = "SELECT repositoryVersion FROM RepositoryVersionEntity repositoryVersion WHERE repositoryVersion IN (SELECT DISTINCT sd1.desiredRepositoryVersion FROM ServiceDesiredStateEntity sd1 WHERE sd1.desiredRepositoryVersion IN ?1)") })
@org.apache.ambari.server.StaticallyInject
public class RepositoryVersionEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "repo_version_id")
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "repository_version_id_generator")
    private java.lang.Long id;

    @javax.persistence.OneToOne
    @javax.persistence.JoinColumn(name = "stack_id", nullable = false)
    private org.apache.ambari.server.orm.entities.StackEntity stack;

    @javax.persistence.Column(name = "version")
    private java.lang.String version;

    @javax.persistence.Column(name = "display_name")
    private java.lang.String displayName;

    @javax.persistence.OneToMany(fetch = javax.persistence.FetchType.EAGER, cascade = javax.persistence.CascadeType.ALL, mappedBy = "repositoryVersionEntity", orphanRemoval = true)
    private java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> repoOsEntities = new java.util.ArrayList<>();

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.REMOVE, mappedBy = "repositoryVersion")
    private java.util.Set<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersionEntities;

    @javax.persistence.Column(name = "repo_type", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.spi.RepositoryType type = org.apache.ambari.spi.RepositoryType.STANDARD;

    @javax.persistence.Lob
    @javax.persistence.Column(name = "version_xml")
    private java.lang.String versionXml;

    @javax.persistence.Transient
    private org.apache.ambari.server.state.repository.VersionDefinitionXml versionDefinition = null;

    @javax.persistence.Column(name = "version_url")
    private java.lang.String versionUrl;

    @javax.persistence.Column(name = "version_xsd")
    private java.lang.String versionXsd;

    @javax.persistence.Column(name = "hidden", nullable = false)
    private short isHidden = 0;

    @javax.persistence.Column(name = "resolved", nullable = false)
    private short resolved = 0;

    @javax.persistence.Column(name = "legacy", nullable = false)
    private short isLegacy = 0;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "parent_id")
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity parent;

    @javax.persistence.OneToMany(mappedBy = "parent")
    private java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> children;

    public RepositoryVersionEntity() {
    }

    public RepositoryVersionEntity(org.apache.ambari.server.orm.entities.StackEntity stack, java.lang.String version, java.lang.String displayName, java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> repoOsEntities) {
        this.stack = stack;
        this.version = version;
        this.displayName = displayName;
        this.repoOsEntities = repoOsEntities;
        for (org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity : repoOsEntities) {
            repoOsEntity.setRepositoryVersionEntity(this);
        }
    }

    @javax.persistence.PreUpdate
    @javax.persistence.PrePersist
    public void removePrefixFromVersion() {
        java.lang.String stackName = stack.getStackName();
        if (version.startsWith(stackName)) {
            version = version.substring(stackName.length() + 1);
        }
    }

    public void updateHostVersionEntityRelation(org.apache.ambari.server.orm.entities.HostVersionEntity entity) {
        hostVersionEntities.add(entity);
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public org.apache.ambari.server.orm.entities.StackEntity getStack() {
        return stack;
    }

    public void setStack(org.apache.ambari.server.orm.entities.StackEntity stack) {
        this.stack = stack;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
        if (((null != version) && (null != stack)) && (null != stack.getStackName())) {
            removePrefixFromVersion();
        }
    }

    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    public java.lang.String getStackName() {
        return getStackId().getStackName();
    }

    public java.lang.String getStackVersion() {
        return getStackId().getStackVersion();
    }

    public org.apache.ambari.server.state.StackId getStackId() {
        if (null == stack) {
            return null;
        }
        return new org.apache.ambari.server.state.StackId(stack.getStackName(), stack.getStackVersion());
    }

    public org.apache.ambari.spi.RepositoryType getType() {
        return type;
    }

    public void setType(org.apache.ambari.spi.RepositoryType type) {
        this.type = type;
    }

    public java.lang.String getVersionXml() {
        return versionXml;
    }

    public void setVersionXml(java.lang.String xml) {
        versionXml = xml;
    }

    public java.lang.String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(java.lang.String url) {
        versionUrl = url;
    }

    public java.lang.String getVersionXsd() {
        return versionXsd;
    }

    public void setVersionXsd(java.lang.String xsdLocation) {
        versionXsd = xsdLocation;
    }

    public org.apache.ambari.server.state.repository.VersionDefinitionXml getRepositoryXml() throws java.lang.Exception {
        if (null == versionXsd) {
            return null;
        }
        if (null == versionDefinition) {
            versionDefinition = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(getVersionXml());
        }
        return versionDefinition;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(stack, version, displayName, repoOsEntities);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object object) {
        if (null == object) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity that = ((org.apache.ambari.server.orm.entities.RepositoryVersionEntity) (object));
        return ((com.google.common.base.Objects.equal(stack, that.stack) && com.google.common.base.Objects.equal(version, that.version)) && com.google.common.base.Objects.equal(displayName, that.displayName)) && com.google.common.base.Objects.equal(repoOsEntities, that.repoOsEntities);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("id", id).add("stack", stack).add("version", version).add("type", type).add("hidden", isHidden == 1).toString();
    }

    public static boolean isVersionInStack(org.apache.ambari.server.state.StackId stackId, java.lang.String version) {
        if ((null != version) && (!org.apache.commons.lang.StringUtils.isBlank(version))) {
            java.lang.String stackName = stackId.getStackName();
            if (version.startsWith(stackName + "-")) {
                version = version.substring(stackName.length() + 1);
            }
            java.lang.String leading = stackId.getStackVersion();
            java.lang.String[] leadingParts = leading.split("\\.");
            if (leadingParts.length > 2) {
                leading = (leadingParts[0] + ".") + leadingParts[1];
            }
            return version.startsWith(leading);
        }
        return false;
    }

    public void setParent(org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity) {
        parent = entity;
        parent.children.add(this);
    }

    public java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> getChildren() {
        return children;
    }

    public java.lang.Long getParentId() {
        return null == parent ? null : parent.getId();
    }

    public boolean isHidden() {
        return isHidden != 0;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = ((short) ((isHidden) ? 1 : 0));
    }

    public boolean isResolved() {
        return resolved == 1;
    }

    @java.lang.Deprecated
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public boolean isLegacy() {
        return isLegacy == 1;
    }

    @java.lang.Deprecated
    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.PATCH_UPGRADES)
    public void setLegacy(boolean isLegacy) {
        this.isLegacy = (isLegacy) ? ((short) (1)) : ((short) (0));
    }

    public void setResolved(boolean resolved) {
        this.resolved = (resolved) ? ((short) (1)) : ((short) (0));
    }

    public java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> getRepoOsEntities() {
        return repoOsEntities;
    }

    public void addRepoOsEntities(java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> repoOsEntities) {
        this.repoOsEntities = repoOsEntities;
        for (org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity : repoOsEntities) {
            repoOsEntity.setRepositoryVersionEntity(this);
        }
    }

    public org.apache.ambari.spi.RepositoryVersion getRepositoryVersion() {
        return new org.apache.ambari.spi.RepositoryVersion(getId(), getStackName(), getStackVersion(), getStackId().getStackId(), getVersion(), getType());
    }
}