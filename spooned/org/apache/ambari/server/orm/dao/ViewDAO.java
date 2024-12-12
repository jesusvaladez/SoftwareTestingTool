package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ViewDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ViewEntity findByName(java.lang.String viewName) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ViewEntity.class, viewName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ViewEntity> findByCommonName(java.lang.String viewCommonName) {
        java.util.List<org.apache.ambari.server.orm.entities.ViewEntity> list = com.google.common.collect.Lists.newArrayList();
        if (viewCommonName != null) {
            for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : findAll()) {
                if (viewCommonName.equals(viewEntity.getCommonName())) {
                    list.add(viewEntity);
                }
            }
        }
        return list;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ViewEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ViewEntity> query = entityManagerProvider.get().createNamedQuery("allViews", org.apache.ambari.server.orm.entities.ViewEntity.class);
        return query.getResultList();
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ViewEntity ViewEntity) {
        entityManagerProvider.get().refresh(ViewEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ViewEntity ViewEntity) {
        entityManagerProvider.get().persist(ViewEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ViewEntity merge(org.apache.ambari.server.orm.entities.ViewEntity ViewEntity) {
        return entityManagerProvider.get().merge(ViewEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ViewEntity ViewEntity) {
        entityManagerProvider.get().remove(merge(ViewEntity));
    }
}