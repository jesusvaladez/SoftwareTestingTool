package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "remoteambaricluster")
@javax.persistence.TableGenerator(name = "remote_cluster_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "remote_cluster_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "allRemoteAmbariClusters", query = "SELECT remoteAmbariCluster FROM RemoteAmbariClusterEntity remoteambaricluster"), @javax.persistence.NamedQuery(name = "remoteAmbariClusterByName", query = "SELECT remoteAmbariCluster " + ("FROM RemoteAmbariClusterEntity remoteAmbariCluster " + "WHERE remoteAmbariCluster.name=:clusterName")), @javax.persistence.NamedQuery(name = "remoteAmbariClusterById", query = "SELECT remoteAmbariCluster " + ("FROM RemoteAmbariClusterEntity remoteAmbariCluster " + "WHERE remoteAmbariCluster.id=:clusterId")) })
@javax.persistence.Entity
public class RemoteAmbariClusterEntity {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.class);

    @javax.persistence.Id
    @javax.persistence.Column(name = "cluster_id", nullable = false)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "remote_cluster_id_generator")
    private java.lang.Long id;

    @javax.persistence.Column(name = "name", nullable = false, insertable = true, updatable = true)
    private java.lang.String name;

    @javax.persistence.Column(name = "url", nullable = false, insertable = true, updatable = true)
    private java.lang.String url;

    @javax.persistence.Column(name = "username", nullable = false, insertable = true, updatable = true)
    private java.lang.String username;

    @javax.persistence.Column(name = "password", nullable = false, insertable = true, updatable = true)
    private java.lang.String password;

    @javax.persistence.OneToMany(cascade = javax.persistence.CascadeType.ALL, mappedBy = "cluster")
    private java.util.Collection<org.apache.ambari.server.orm.entities.RemoteAmbariClusterServiceEntity> services;

    private static org.apache.ambari.view.Masker masker = new org.apache.ambari.server.view.DefaultMasker();

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

    public java.lang.String getUrl() {
        return url;
    }

    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    public java.lang.String getUsername() {
        return username;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public java.lang.String getPassword() {
        try {
            return org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.masker.unmask(password);
        } catch (org.apache.ambari.view.MaskException e) {
            org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.LOG.error("Unable to unmask password for Remote Cluster : " + name, e);
        }
        return null;
    }

    public void setPassword(java.lang.String password) throws org.apache.ambari.view.MaskException {
        this.password = org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity.masker.mask(password);
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.RemoteAmbariClusterServiceEntity> getServices() {
        return services;
    }

    public void setServices(java.util.Collection<org.apache.ambari.server.orm.entities.RemoteAmbariClusterServiceEntity> services) {
        this.services = services;
    }
}