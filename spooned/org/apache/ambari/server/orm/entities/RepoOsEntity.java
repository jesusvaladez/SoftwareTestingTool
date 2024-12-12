package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "repo_os")
@javax.persistence.TableGenerator(name = "repo_os_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "repo_os_id_seq")
public class RepoOsEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "repo_os_id_generator")
    private java.lang.Long id;

    @javax.persistence.Column(name = "family")
    private java.lang.String family;

    @javax.persistence.Column(name = "ambari_managed", nullable = false)
    private short ambariManaged = 1;

    @javax.persistence.OneToMany(orphanRemoval = true, fetch = javax.persistence.FetchType.EAGER, cascade = javax.persistence.CascadeType.ALL, mappedBy = "repoOs")
    private java.util.List<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> repoDefinitionEntities = new java.util.ArrayList<>();

    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.EAGER)
    @javax.persistence.JoinColumn(name = "repo_version_id", nullable = false)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity;

    public java.util.List<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> getRepoDefinitionEntities() {
        return repoDefinitionEntities;
    }

    public void addRepoDefinitionEntities(java.util.List<org.apache.ambari.server.orm.entities.RepoDefinitionEntity> repoDefinitionEntities) {
        this.repoDefinitionEntities.addAll(repoDefinitionEntities);
        for (org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity : repoDefinitionEntities) {
            repoDefinitionEntity.setRepoOs(this);
        }
    }

    public void addRepoDefinition(org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinition) {
        this.repoDefinitionEntities.add(repoDefinition);
        repoDefinition.setRepoOs(this);
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getRepositoryVersionEntity() {
        return repositoryVersionEntity;
    }

    public void setRepositoryVersionEntity(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity) {
        this.repositoryVersionEntity = repositoryVersionEntity;
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getFamily() {
        return family;
    }

    public void setFamily(java.lang.String family) {
        this.family = family;
    }

    public boolean isAmbariManaged() {
        return ambariManaged == 1;
    }

    public void setAmbariManaged(boolean ambariManaged) {
        this.ambariManaged = ((short) ((ambariManaged) ? 1 : 0));
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(family, ambariManaged, repoDefinitionEntities);
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
        org.apache.ambari.server.orm.entities.RepoOsEntity that = ((org.apache.ambari.server.orm.entities.RepoOsEntity) (object));
        return (com.google.common.base.Objects.equal(ambariManaged, that.ambariManaged) && com.google.common.base.Objects.equal(family, that.family)) && com.google.common.base.Objects.equal(repoDefinitionEntities, that.repoDefinitionEntities);
    }
}