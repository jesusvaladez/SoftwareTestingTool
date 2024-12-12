package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
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
@javax.persistence.Entity
@javax.persistence.Table(name = "servicecomponent_version")
@javax.persistence.TableGenerator(name = "servicecomponent_version_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "servicecomponent_version_id_seq", initialValue = 0)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "ServiceComponentVersionEntity.findByComponent", query = "SELECT version FROM ServiceComponentVersionEntity version WHERE " + (("version.m_serviceComponentDesiredStateEntity.clusterId = :clusterId AND " + "version.m_serviceComponentDesiredStateEntity.serviceName = :serviceName AND ") + "version.m_serviceComponentDesiredStateEntity.componentName = :componentName")), @javax.persistence.NamedQuery(name = "ServiceComponentVersionEntity.findByComponentAndVersion", query = "SELECT version FROM ServiceComponentVersionEntity version WHERE " + ((("version.m_serviceComponentDesiredStateEntity.clusterId = :clusterId AND " + "version.m_serviceComponentDesiredStateEntity.serviceName = :serviceName AND ") + "version.m_serviceComponentDesiredStateEntity.componentName = :componentName AND ") + "version.m_repositoryVersion.version = :repoVersion")) })
public class ServiceComponentVersionEntity {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "servicecomponent_version_id_generator")
    @javax.persistence.Column(name = "id", nullable = false, updatable = false)
    private long m_id;

    @javax.persistence.ManyToOne(optional = false, cascade = { javax.persistence.CascadeType.MERGE })
    @javax.persistence.JoinColumn(name = "component_id", referencedColumnName = "id", nullable = false)
    private org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity m_serviceComponentDesiredStateEntity;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "repo_version_id", referencedColumnName = "repo_version_id", nullable = false)
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersion;

    @javax.persistence.Column(name = "state", nullable = false, insertable = true, updatable = true)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.state.RepositoryVersionState m_state = org.apache.ambari.server.state.RepositoryVersionState.CURRENT;

    @javax.persistence.Column(name = "user_name", nullable = false, insertable = true, updatable = true)
    private java.lang.String userName;

    public org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity getServiceComponentDesiredState() {
        return m_serviceComponentDesiredStateEntity;
    }

    public void setServiceComponentDesiredState(org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity) {
        m_serviceComponentDesiredStateEntity = serviceComponentDesiredStateEntity;
    }

    public void setRepositoryVersion(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) {
        m_repositoryVersion = repositoryVersion;
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity getRepositoryVersion() {
        return m_repositoryVersion;
    }

    public long getId() {
        return m_id;
    }

    public org.apache.ambari.server.state.RepositoryVersionState getState() {
        return m_state;
    }

    public void setState(org.apache.ambari.server.state.RepositoryVersionState state) {
        m_state = state;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String name) {
        userName = name;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(m_id, m_repositoryVersion, m_serviceComponentDesiredStateEntity, m_state);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity other = ((org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity) (obj));
        return ((java.util.Objects.equals(m_id, other.m_id) && java.util.Objects.equals(m_repositoryVersion, other.m_repositoryVersion)) && java.util.Objects.equals(m_serviceComponentDesiredStateEntity, other.m_serviceComponentDesiredStateEntity)) && java.util.Objects.equals(m_state, other.m_state);
    }
}