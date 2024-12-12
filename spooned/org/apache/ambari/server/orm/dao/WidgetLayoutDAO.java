package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class WidgetLayoutDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.WidgetLayoutEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.WidgetLayoutEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> findByCluster(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> query = entityManagerProvider.get().createNamedQuery("WidgetLayoutEntity.findByCluster", org.apache.ambari.server.orm.entities.WidgetLayoutEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> findBySectionName(java.lang.String sectionName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> query = entityManagerProvider.get().createNamedQuery("WidgetLayoutEntity.findBySectionName", org.apache.ambari.server.orm.entities.WidgetLayoutEntity.class);
        query.setParameter("sectionName", sectionName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> findByName(java.lang.Long clusterId, java.lang.String layoutName, java.lang.String userName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> query = entityManagerProvider.get().createNamedQuery("WidgetLayoutEntity.findByName", org.apache.ambari.server.orm.entities.WidgetLayoutEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("layoutName", layoutName);
        query.setParameter("userName", userName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> query = entityManagerProvider.get().createNamedQuery("WidgetLayoutEntity.findAll", org.apache.ambari.server.orm.entities.WidgetLayoutEntity.class);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity) {
        entityManagerProvider.get().persist(widgetLayoutEntity);
    }

    @com.google.inject.persist.Transactional
    public void createWithFlush(org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        entityManager.persist(widgetLayoutEntity);
        entityManager.flush();
        entityManager.refresh(widgetLayoutEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.WidgetLayoutEntity merge(org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity) {
        return entityManagerProvider.get().merge(widgetLayoutEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.WidgetLayoutEntity mergeWithFlush(org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity) {
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        widgetLayoutEntity = entityManager.merge(widgetLayoutEntity);
        entityManager.flush();
        entityManager.refresh(widgetLayoutEntity);
        return widgetLayoutEntity;
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity) {
        entityManagerProvider.get().remove(merge(widgetLayoutEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Long id) {
        entityManagerProvider.get().remove(findById(id));
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayoutEntity) {
        entityManagerProvider.get().refresh(widgetLayoutEntity);
    }
}