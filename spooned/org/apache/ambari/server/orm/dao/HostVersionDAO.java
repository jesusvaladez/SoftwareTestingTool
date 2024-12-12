package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class HostVersionDAO extends org.apache.ambari.server.orm.dao.CrudDAO<org.apache.ambari.server.orm.entities.HostVersionEntity, java.lang.Long> {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    public HostVersionDAO() {
        super(org.apache.ambari.server.orm.entities.HostVersionEntity.class);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.HostVersionEntity entity) throws java.lang.IllegalArgumentException {
        if (entity.getRepositoryVersion() == null) {
            throw new java.lang.IllegalArgumentException("RepositoryVersion argument is not set for the entity");
        }
        super.create(entity);
        entity.getRepositoryVersion().updateHostVersionEntityRelation(entity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> findByClusterStackAndVersion(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId, java.lang.String version) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("hostVersionByClusterAndStackAndVersion", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("clusterName", clusterName);
        query.setParameter("stackName", stackId.getStackName());
        query.setParameter("stackVersion", stackId.getStackVersion());
        query.setParameter("version", version);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> findByHost(java.lang.String hostName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("hostVersionByHostname", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("hostName", hostName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> findByClusterAndHost(java.lang.String clusterName, java.lang.String hostName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("hostVersionByClusterAndHostname", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("clusterName", clusterName);
        query.setParameter("hostName", hostName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> findByCluster(java.lang.String clusterName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("findByCluster", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("clusterName", clusterName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> findByClusterAndState(java.lang.String clusterName, org.apache.ambari.server.state.RepositoryVersionState state) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("findByClusterAndState", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("clusterName", clusterName);
        query.setParameter("state", state);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> findByClusterHostAndState(java.lang.String clusterName, java.lang.String hostName, org.apache.ambari.server.state.RepositoryVersionState state) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("hostVersionByClusterHostnameAndState", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("clusterName", clusterName);
        query.setParameter("hostName", hostName);
        query.setParameter("state", state);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostVersionEntity findByClusterStackVersionAndHost(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId, java.lang.String version, java.lang.String hostName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("hostVersionByClusterStackVersionAndHostname", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("clusterName", clusterName);
        query.setParameter("stackName", stackId.getStackName());
        query.setParameter("stackVersion", stackId.getStackVersion());
        query.setParameter("version", version);
        query.setParameter("hostName", hostName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> findHostVersionByClusterAndRepository(long clusterId, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("findHostVersionByClusterAndRepository", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("repositoryVersion", repositoryVersion);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostVersionEntity> findByRepositoryAndStates(org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion, java.util.Collection<org.apache.ambari.server.state.RepositoryVersionState> states) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("hostVersionByRepositoryAndStates", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("repositoryVersion", repositoryVersion);
        query.setParameter("states", states);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.HostVersionEntity findHostVersionByHostAndRepository(org.apache.ambari.server.orm.entities.HostEntity host, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostVersionEntity> query = entityManagerProvider.get().createNamedQuery("findByHostAndRepository", org.apache.ambari.server.orm.entities.HostVersionEntity.class);
        query.setParameter("host", host);
        query.setParameter("repositoryVersion", repositoryVersion);
        return daoUtils.selectOne(query);
    }

    @com.google.inject.persist.Transactional
    public void removeByHostName(java.lang.String hostName) {
        java.util.Collection<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = findByHost(hostName);
        this.remove(hostVersions);
    }
}