package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class StackDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.StackEntity findById(long stackId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.StackEntity.class, stackId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.StackEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.StackEntity> query = entityManagerProvider.get().createNamedQuery("StackEntity.findAll", org.apache.ambari.server.orm.entities.StackEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.StackEntity find(java.lang.String stackName, java.lang.String stackVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.StackEntity> query = entityManagerProvider.get().createNamedQuery("StackEntity.findByNameAndVersion", org.apache.ambari.server.orm.entities.StackEntity.class);
        query.setParameter("stackName", stackName);
        query.setParameter("stackVersion", stackVersion);
        return daoUtils.selectOne(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.StackEntity find(org.apache.ambari.server.state.StackId stackId) {
        return find(stackId.getStackName(), stackId.getStackVersion());
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.StackEntity stack) throws org.apache.ambari.server.AmbariException {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(stack);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.StackEntity stack) {
        entityManagerProvider.get().refresh(stack);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.StackEntity merge(org.apache.ambari.server.orm.entities.StackEntity stack) {
        return entityManagerProvider.get().merge(stack);
    }

    public void createOrUpdate(org.apache.ambari.server.orm.entities.StackEntity stack) throws org.apache.ambari.server.AmbariException {
        if (null == stack.getStackId()) {
            create(stack);
        } else {
            merge(stack);
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.StackEntity stack) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        stack = findById(stack.getStackId());
        if (null != stack) {
            entityManager.remove(stack);
        }
    }

    @com.google.inject.persist.Transactional
    public void removeByMpack(java.lang.Long mpackId) {
        entityManagerProvider.get().remove(findByMpack(mpackId));
    }

    public org.apache.ambari.server.orm.entities.StackEntity findByMpack(java.lang.Long mpackId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.StackEntity> query = entityManagerProvider.get().createNamedQuery("StackEntity.findByMpack", org.apache.ambari.server.orm.entities.StackEntity.class);
        query.setParameter("mpackId", mpackId);
        return daoUtils.selectOne(query);
    }
}