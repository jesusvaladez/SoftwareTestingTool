package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ExtensionLinkDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> find(org.apache.ambari.server.controller.ExtensionLinkRequest request) {
        if (request.getLinkId() != null) {
            org.apache.ambari.server.orm.entities.ExtensionLinkEntity entity = findById(java.lang.Long.parseLong(request.getLinkId()));
            java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> list = new java.util.ArrayList<>();
            list.add(entity);
            return list;
        }
        java.lang.String stackName = request.getStackName();
        java.lang.String stackVersion = request.getStackVersion();
        java.lang.String extensionName = request.getExtensionName();
        java.lang.String extensionVersion = request.getExtensionVersion();
        if ((stackName != null) && (stackVersion != null)) {
            if (extensionName != null) {
                if (extensionVersion != null) {
                    org.apache.ambari.server.orm.entities.ExtensionLinkEntity entity = findByStackAndExtension(stackName, stackVersion, extensionName, extensionVersion);
                    java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> list = new java.util.ArrayList<>();
                    list.add(entity);
                    return list;
                }
                return findByStackAndExtensionName(stackName, stackVersion, extensionName);
            }
            return findByStack(stackName, stackVersion);
        }
        if ((extensionName != null) && (extensionVersion != null)) {
            return findByExtension(extensionName, extensionVersion);
        }
        return findAll();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ExtensionLinkEntity findById(long linkId) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ExtensionLinkEntity.class, linkId);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> query = entityManagerProvider.get().createNamedQuery("ExtensionLinkEntity.findAll", org.apache.ambari.server.orm.entities.ExtensionLinkEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> findByExtension(java.lang.String extensionName, java.lang.String extensionVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> query = entityManagerProvider.get().createNamedQuery("ExtensionLinkEntity.findByExtension", org.apache.ambari.server.orm.entities.ExtensionLinkEntity.class);
        query.setParameter("extensionName", extensionName);
        query.setParameter("extensionVersion", extensionVersion);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> findByStack(java.lang.String stackName, java.lang.String stackVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> query = entityManagerProvider.get().createNamedQuery("ExtensionLinkEntity.findByStack", org.apache.ambari.server.orm.entities.ExtensionLinkEntity.class);
        query.setParameter("stackName", stackName);
        query.setParameter("stackVersion", stackVersion);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> findByStackAndExtensionName(java.lang.String stackName, java.lang.String stackVersion, java.lang.String extensionName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> query = entityManagerProvider.get().createNamedQuery("ExtensionLinkEntity.findByStackAndExtensionName", org.apache.ambari.server.orm.entities.ExtensionLinkEntity.class);
        query.setParameter("stackName", stackName);
        query.setParameter("stackVersion", stackVersion);
        query.setParameter("extensionName", extensionName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ExtensionLinkEntity findByStackAndExtension(java.lang.String stackName, java.lang.String stackVersion, java.lang.String extensionName, java.lang.String extensionVersion) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> query = entityManagerProvider.get().createNamedQuery("ExtensionLinkEntity.findByStackAndExtension", org.apache.ambari.server.orm.entities.ExtensionLinkEntity.class);
        query.setParameter("stackName", stackName);
        query.setParameter("stackVersion", stackVersion);
        query.setParameter("extensionName", extensionName);
        query.setParameter("extensionVersion", extensionVersion);
        return daoUtils.selectOne(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ExtensionLinkEntity link) throws org.apache.ambari.server.AmbariException {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(link);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ExtensionLinkEntity link) {
        entityManagerProvider.get().refresh(link);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ExtensionLinkEntity merge(org.apache.ambari.server.orm.entities.ExtensionLinkEntity link) {
        return entityManagerProvider.get().merge(link);
    }

    public void createOrUpdate(org.apache.ambari.server.orm.entities.ExtensionLinkEntity link) throws org.apache.ambari.server.AmbariException {
        if (null == link.getLinkId()) {
            create(link);
        } else {
            merge(link);
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ExtensionLinkEntity link) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        link = findById(link.getLinkId());
        if (null != link) {
            entityManager.remove(link);
        }
    }
}