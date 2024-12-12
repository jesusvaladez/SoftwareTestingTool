package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ExtensionDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ExtensionEntity findById(long extensionId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ExtensionEntity.class, extensionId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ExtensionEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ExtensionEntity> query = entityManagerProvider.get().createNamedQuery("ExtensionEntity.findAll", org.apache.ambari.server.orm.entities.ExtensionEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ExtensionEntity find(java.lang.String extensionName, java.lang.String extensionVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ExtensionEntity> query = entityManagerProvider.get().createNamedQuery("ExtensionEntity.findByNameAndVersion", org.apache.ambari.server.orm.entities.ExtensionEntity.class);
        query.setParameter("extensionName", extensionName);
        query.setParameter("extensionVersion", extensionVersion);
        return daoUtils.selectOne(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ExtensionEntity extension) throws org.apache.ambari.server.AmbariException {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(extension);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ExtensionEntity extension) {
        entityManagerProvider.get().refresh(extension);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ExtensionEntity merge(org.apache.ambari.server.orm.entities.ExtensionEntity extension) {
        return entityManagerProvider.get().merge(extension);
    }

    public void createOrUpdate(org.apache.ambari.server.orm.entities.ExtensionEntity extension) throws org.apache.ambari.server.AmbariException {
        if (null == extension.getExtensionId()) {
            create(extension);
        } else {
            merge(extension);
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ExtensionEntity extension) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        extension = findById(extension.getExtensionId());
        if (null != extension) {
            entityManager.remove(extension);
        }
    }
}