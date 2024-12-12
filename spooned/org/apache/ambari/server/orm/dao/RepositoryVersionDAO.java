package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class RepositoryVersionDAO extends org.apache.ambari.server.orm.dao.CrudDAO<org.apache.ambari.server.orm.entities.RepositoryVersionEntity, java.lang.Long> {
    public RepositoryVersionDAO() {
        super(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
    }

    @java.lang.Override
    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity) {
        super.create(entity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity findByDisplayName(java.lang.String displayName) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> query = entityManagerProvider.get().createNamedQuery("repositoryVersionByDisplayName", org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        query.setParameter("displayname", displayName);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity findByStackAndVersion(org.apache.ambari.server.state.StackId stackId, java.lang.String version) {
        return findByStackNameAndVersion(stackId.getStackName(), version);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity findByStackAndVersion(org.apache.ambari.server.orm.entities.StackEntity stackEntity, java.lang.String version) {
        return findByStackNameAndVersion(stackEntity.getStackName(), version);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity findByStackNameAndVersion(java.lang.String stackName, java.lang.String version) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> query = entityManagerProvider.get().createNamedQuery("repositoryVersionByStackNameAndVersion", org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        query.setParameter("stackName", stackName);
        query.setParameter("version", version);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> findByStack(org.apache.ambari.server.state.StackId stackId) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> query = entityManagerProvider.get().createNamedQuery("repositoryVersionByStack", org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        query.setParameter("stackName", stackId.getStackName());
        query.setParameter("stackVersion", stackId.getStackVersion());
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> findByStackAndType(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.spi.RepositoryType type) {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> query = entityManagerProvider.get().createNamedQuery("repositoryVersionByStackAndType", org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        query.setParameter("stackName", stackId.getStackName());
        query.setParameter("stackVersion", stackId.getStackVersion());
        query.setParameter("type", type);
        return daoUtils.selectList(query);
    }

    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity create(org.apache.ambari.server.orm.entities.StackEntity stackEntity, java.lang.String version, java.lang.String displayName, java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> repoOsEntities) throws org.apache.ambari.server.AmbariException {
        return create(stackEntity, version, displayName, repoOsEntities, org.apache.ambari.spi.RepositoryType.STANDARD);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity create(org.apache.ambari.server.orm.entities.StackEntity stackEntity, java.lang.String version, java.lang.String displayName, java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> repoOsEntities, org.apache.ambari.spi.RepositoryType type) throws org.apache.ambari.server.AmbariException {
        if (((((stackEntity == null) || (version == null)) || version.isEmpty()) || (displayName == null)) || displayName.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException("At least one of the required properties is null or empty");
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity existingByDisplayName = findByDisplayName(displayName);
        if (existingByDisplayName != null) {
            throw new org.apache.ambari.server.AmbariException(("Repository version with display name '" + displayName) + "' already exists");
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity existingVersionInStack = findByStackNameAndVersion(stackEntity.getStackName(), version);
        if (existingVersionInStack != null) {
            throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("Repository Version for version {0} already exists, in stack {1}-{2}", version, existingVersionInStack.getStack().getStackName(), existingVersionInStack.getStack().getStackVersion()));
        }
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackEntity.getStackName(), stackEntity.getStackVersion());
        if (!org.apache.ambari.server.orm.entities.RepositoryVersionEntity.isVersionInStack(stackId, version)) {
            throw new org.apache.ambari.server.AmbariException(java.text.MessageFormat.format("Version {0} needs to belong to stack {1}", version, (stackEntity.getStackName() + "-") + stackEntity.getStackVersion()));
        }
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity newEntity = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity(stackEntity, version, displayName, repoOsEntities);
        newEntity.setType(type);
        this.create(newEntity);
        return newEntity;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> findRepositoriesWithVersionDefinitions() {
        final javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> query = entityManagerProvider.get().createNamedQuery("repositoryVersionsFromDefinition", org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RepositoryVersionEntity findByVersion(java.lang.String repositoryVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> query = entityManagerProvider.get().createNamedQuery("repositoryVersionByVersion", org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        query.setParameter("version", repositoryVersion);
        return daoUtils.selectOne(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> findByServiceDesiredVersion(java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> matching) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> query = entityManagerProvider.get().createNamedQuery("findByServiceDesiredVersion", org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        return daoUtils.selectList(query, matching);
    }

    @com.google.inject.persist.Transactional
    public void removeByStack(org.apache.ambari.server.state.StackId stackId) {
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> repoVersionDeleteCandidates = findByStack(stackId);
        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersionEntity : repoVersionDeleteCandidates) {
            entityManagerProvider.get().remove(repositoryVersionEntity);
        }
    }
}