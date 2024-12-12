package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class WidgetDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.WidgetEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.WidgetEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> findByCluster(long clusterId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetEntity> query = entityManagerProvider.get().createNamedQuery("WidgetEntity.findByCluster", org.apache.ambari.server.orm.entities.WidgetEntity.class);
        query.setParameter("clusterId", clusterId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> findBySectionName(java.lang.String sectionName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetEntity> query = entityManagerProvider.get().createNamedQuery("WidgetEntity.findBySectionName", org.apache.ambari.server.orm.entities.WidgetEntity.class);
        query.setParameter("sectionName", sectionName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> findByName(java.lang.Long clusterId, java.lang.String widgetName, java.lang.String author, java.lang.String defaultSectionName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetEntity> query = entityManagerProvider.get().createNamedQuery("WidgetEntity.findByName", org.apache.ambari.server.orm.entities.WidgetEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("widgetName", widgetName);
        query.setParameter("author", author);
        query.setParameter("defaultSectionName", defaultSectionName);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> findByScopeOrAuthor(java.lang.String author, java.lang.String scope) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetEntity> query = entityManagerProvider.get().createNamedQuery("WidgetEntity.findByScopeOrAuthor", org.apache.ambari.server.orm.entities.WidgetEntity.class);
        query.setParameter("author", author);
        query.setParameter("scope", scope);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.WidgetEntity> query = entityManagerProvider.get().createNamedQuery("WidgetEntity.findAll", org.apache.ambari.server.orm.entities.WidgetEntity.class);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity) {
        entityManagerProvider.get().persist(widgetEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.WidgetEntity merge(org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity) {
        return entityManagerProvider.get().merge(widgetEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity) {
        entityManagerProvider.get().remove(merge(widgetEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(java.lang.Long id) {
        entityManagerProvider.get().remove(findById(id));
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.WidgetEntity widgetEntity) {
        entityManagerProvider.get().refresh(widgetEntity);
    }
}