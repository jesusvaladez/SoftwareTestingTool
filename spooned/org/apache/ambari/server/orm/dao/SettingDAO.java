package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class SettingDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.SettingEntity findByName(java.lang.String name) {
        if (org.apache.commons.lang.StringUtils.isBlank(name)) {
            return null;
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.SettingEntity> query = entityManagerProvider.get().createNamedQuery("settingByName", org.apache.ambari.server.orm.entities.SettingEntity.class);
        query.setParameter("name", name);
        return daoUtils.selectOne(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.SettingEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.SettingEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.SettingEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.SettingEntity merge(org.apache.ambari.server.orm.entities.SettingEntity entity) {
        return entityManagerProvider.get().merge(entity);
    }

    @com.google.inject.persist.Transactional
    public void removeByName(java.lang.String name) {
        org.apache.ambari.server.orm.entities.SettingEntity entity = findByName(name);
        if (entity != null) {
            entityManagerProvider.get().remove(entity);
        }
    }
}