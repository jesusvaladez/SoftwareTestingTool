package org.apache.ambari.server.orm.entities;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Entity
@javax.persistence.Table(name = "repo_definition")
@javax.persistence.TableGenerator(name = "repo_definition_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "repo_definition_id_seq")
public class RepoDefinitionEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "id", nullable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "repo_definition_id_generator")
    private java.lang.Long id;

    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.ElementCollection(targetClass = org.apache.ambari.server.state.stack.RepoTag.class)
    @javax.persistence.CollectionTable(name = "repo_tags", joinColumns = @javax.persistence.JoinColumn(name = "repo_definition_id"))
    @javax.persistence.Column(name = "tag")
    private java.util.Set<org.apache.ambari.server.state.stack.RepoTag> repoTags = new java.util.HashSet<>();

    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.EAGER)
    @javax.persistence.JoinColumn(name = "repo_os_id", nullable = false)
    private org.apache.ambari.server.orm.entities.RepoOsEntity repoOs;

    @javax.persistence.Column(name = "repo_name", nullable = false)
    private java.lang.String repoName;

    @javax.persistence.Column(name = "repo_id", nullable = false)
    private java.lang.String repoID;

    @javax.persistence.Column(name = "base_url", nullable = false)
    private java.lang.String baseUrl;

    @javax.persistence.Column(name = "mirrors")
    private java.lang.String mirrors;

    @javax.persistence.Column(name = "distribution")
    private java.lang.String distribution;

    @javax.persistence.Column(name = "components")
    private java.lang.String components;

    @javax.persistence.Column(name = "unique_repo", nullable = false)
    private short unique = 0;

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    @javax.persistence.ElementCollection(targetClass = java.lang.String.class)
    @javax.persistence.CollectionTable(name = "repo_applicable_services", joinColumns = { @javax.persistence.JoinColumn(name = "repo_definition_id") })
    @javax.persistence.Column(name = "service_name")
    private java.util.List<java.lang.String> applicableServices = new java.util.LinkedList<>();

    public java.lang.String getDistribution() {
        return distribution;
    }

    public void setDistribution(java.lang.String distribution) {
        this.distribution = distribution;
    }

    public org.apache.ambari.server.orm.entities.RepoOsEntity getRepoOs() {
        return repoOs;
    }

    public void setRepoOs(org.apache.ambari.server.orm.entities.RepoOsEntity repoOs) {
        this.repoOs = repoOs;
    }

    public java.lang.String getRepoName() {
        return repoName;
    }

    public void setRepoName(java.lang.String repoName) {
        this.repoName = repoName;
    }

    public java.lang.String getRepoID() {
        return repoID;
    }

    public void setRepoID(java.lang.String repoID) {
        this.repoID = repoID;
    }

    public java.lang.String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(java.lang.String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public java.lang.String getMirrors() {
        return mirrors;
    }

    public void setMirrors(java.lang.String mirrors) {
        this.mirrors = mirrors;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public java.util.List<java.lang.String> getApplicableServices() {
        return applicableServices;
    }

    @org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
    public void setApplicableServices(java.util.List<java.lang.String> applicableServices) {
        this.applicableServices = applicableServices;
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getComponents() {
        return components;
    }

    public void setComponents(java.lang.String components) {
        this.components = components;
    }

    public boolean isUnique() {
        return unique == 1;
    }

    public void setUnique(boolean unique) {
        this.unique = ((short) ((unique) ? 1 : 0));
    }

    public java.util.Set<org.apache.ambari.server.state.stack.RepoTag> getTags() {
        return repoTags;
    }

    public void setTags(java.util.Set<org.apache.ambari.server.state.stack.RepoTag> repoTags) {
        this.repoTags = repoTags;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(repoTags, repoName, repoID, baseUrl, mirrors, distribution, components, unique, applicableServices);
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
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity that = ((org.apache.ambari.server.orm.entities.RepoDefinitionEntity) (object));
        return (((((((com.google.common.base.Objects.equal(unique, that.unique) && com.google.common.base.Objects.equal(repoTags, that.repoTags)) && com.google.common.base.Objects.equal(repoName, that.repoName)) && com.google.common.base.Objects.equal(repoID, that.repoID)) && com.google.common.base.Objects.equal(baseUrl, that.baseUrl)) && com.google.common.base.Objects.equal(mirrors, that.mirrors)) && com.google.common.base.Objects.equal(distribution, that.distribution)) && com.google.common.base.Objects.equal(components, that.components)) && com.google.common.base.Objects.equal(applicableServices, that.applicableServices);
    }
}