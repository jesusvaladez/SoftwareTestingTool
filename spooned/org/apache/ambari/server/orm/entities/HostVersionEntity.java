package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@javax.persistence.Entity
@javax.persistence.Table(name = "host_version", uniqueConstraints = @javax.persistence.UniqueConstraint(name = "UQ_host_repo", columnNames = { "host_id", "repo_version_id" }))
@javax.persistence.TableGenerator(name = "host_version_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "host_version_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "hostVersionByClusterAndStackAndVersion", query = "SELECT hostVersion FROM HostVersionEntity hostVersion JOIN hostVersion.hostEntity host JOIN host.clusterEntities clusters " + "WHERE clusters.clusterName=:clusterName AND hostVersion.repositoryVersion.stack.stackName=:stackName AND hostVersion.repositoryVersion.stack.stackVersion=:stackVersion AND hostVersion.repositoryVersion.version=:version"), @javax.persistence.NamedQuery(name = "hostVersionByClusterAndHostname", query = "SELECT hostVersion FROM HostVersionEntity hostVersion JOIN hostVersion.hostEntity host JOIN host.clusterEntities clusters " + "WHERE clusters.clusterName=:clusterName AND hostVersion.hostEntity.hostName=:hostName"), @javax.persistence.NamedQuery(name = "hostVersionByHostname", query = "SELECT hostVersion FROM HostVersionEntity hostVersion JOIN hostVersion.hostEntity host " + "WHERE hostVersion.hostEntity.hostName=:hostName"), @javax.persistence.NamedQuery(name = "findByClusterAndState", query = "SELECT hostVersion FROM HostVersionEntity hostVersion JOIN hostVersion.hostEntity host JOIN host.clusterEntities clusters " + "WHERE clusters.clusterName=:clusterName AND hostVersion.state=:state"), @javax.persistence.NamedQuery(name = "findByCluster", query = "SELECT hostVersion FROM HostVersionEntity hostVersion JOIN hostVersion.hostEntity host JOIN host.clusterEntities clusters " + "WHERE clusters.clusterName=:clusterName"), @javax.persistence.NamedQuery(name = "hostVersionByClusterHostnameAndState", query = "SELECT hostVersion FROM HostVersionEntity hostVersion JOIN hostVersion.hostEntity host JOIN host.clusterEntities clusters " + "WHERE clusters.clusterName=:clusterName AND hostVersion.hostEntity.hostName=:hostName AND hostVersion.state=:state"), @javax.persistence.NamedQuery(name = "hostVersionByClusterStackVersionAndHostname", query = "SELECT hostVersion FROM HostVersionEntity hostVersion JOIN hostVersion.hostEntity host JOIN host.clusterEntities clusters " + ("WHERE clusters.clusterName=:clusterName AND hostVersion.repositoryVersion.stack.stackName=:stackName AND hostVersion.repositoryVersion.stack.stackVersion=:stackVersion AND hostVersion.repositoryVersion.version=:version AND " + "hostVersion.hostEntity.hostName=:hostName")), @javax.persistence.NamedQuery(name = "findHostVersionByClusterAndRepository", query = "SELECT hostVersion FROM HostVersionEntity hostVersion JOIN hostVersion.hostEntity host JOIN host.clusterEntities clusters " + "WHERE clusters.clusterId = :clusterId AND hostVersion.repositoryVersion = :repositoryVersion"), @javax.persistence.NamedQuery(name = "hostVersionByRepositoryAndStates", query = "SELECT hostVersion FROM HostVersionEntity hostVersion WHERE hostVersion.repositoryVersion = :repositoryVersion AND hostVersion.state IN :states"), @javax.persistence.NamedQuery(name = "findByHostAndRepository", query = "SELECT hostVersion FROM HostVersionEntity hostVersion WHERE hostVersion.hostEntity = :host AND hostVersion.repositoryVersion = :repositoryVersion") })
public class HostVersionEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false, insertable = true, updatable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "host_version_id_generator")
    private java.lang.Long id;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "repo_version_id", referencedColumnName = "repo_version_id", nullable = false)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion;

    @javax.persistence.Column(name = "host_id", nullable = false, insertable = false, updatable = false)
    private java.lang.Long hostId;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "host_id", referencedColumnName = "host_id", nullable = false)
    private org.apache.ambari.server.orm.entities.HostEntity hostEntity;

    @javax.persistence.Column(name = "state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.RepositoryVersionState state;

    public HostVersionEntity() {
    }

    public HostVersionEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion, org.apache.ambari.server.state.RepositoryVersionState state) {
        this.hostEntity = hostEntity;
        this.repositoryVersion = repositoryVersion;
        this.state = state;
    }

    public HostVersionEntity(org.apache.ambari.server.orm.entities.HostVersionEntity other) {
        hostEntity = other.hostEntity;
        repositoryVersion = other.repositoryVersion;
        state = other.state;
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getHostName() {
        return hostEntity != null ? hostEntity.getHostName() : null;
    }

    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostEntity;
    }

    public void setHostEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        this.hostEntity = hostEntity;
    }

    public org.apache.ambari.server.state.RepositoryVersionState getState() {
        return state;
    }

    public void setState(org.apache.ambari.server.state.RepositoryVersionState state) {
        this.state = state;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getRepositoryVersion() {
        return repositoryVersion;
    }

    public void setRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) {
        this.repositoryVersion = repositoryVersion;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (hostEntity == null ? 0 : hostEntity.hashCode());
        result = (prime * result) + (id == null ? 0 : id.hashCode());
        result = (prime * result) + (repositoryVersion == null ? 0 : repositoryVersion.hashCode());
        result = (prime * result) + (state == null ? 0 : state.hashCode());
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        org.apache.ambari.server.orm.entities.HostVersionEntity other = ((org.apache.ambari.server.orm.entities.HostVersionEntity) (obj));
        if (!java.util.Objects.equals(id, other.id)) {
            return false;
        }
        if (hostEntity != null ? !hostEntity.equals(other.hostEntity) : other.hostEntity != null) {
            return false;
        }
        if (repositoryVersion != null ? !repositoryVersion.equals(other.repositoryVersion) : other.repositoryVersion != null) {
            return false;
        }
        if (state != other.state) {
            return false;
        }
        return true;
    }

    public java.lang.Long getHostId() {
        return hostId;
    }

    public void setHostId(java.lang.Long hostId) {
        this.hostId = hostId;
    }
}