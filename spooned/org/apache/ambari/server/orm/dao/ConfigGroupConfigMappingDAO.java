package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class ConfigGroupConfigMappingDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity findByPK(org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntityPK configGroupConfigMappingEntityPK) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity.class, configGroupConfigMappingEntityPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> findByGroup(java.lang.Long groupId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> query = entityManagerProvider.get().createNamedQuery("configsByGroup", org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity.class);
        query.setParameter("groupId", groupId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configGroupConfigMappingEntity) {
        entityManagerProvider.get().persist(configGroupConfigMappingEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity merge(org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configGroupConfigMappingEntity) {
        return entityManagerProvider.get().merge(configGroupConfigMappingEntity);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configGroupConfigMappingEntity) {
        entityManagerProvider.get().refresh(configGroupConfigMappingEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntity configGroupConfigMappingEntity) {
        entityManagerProvider.get().remove(merge(configGroupConfigMappingEntity));
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(org.apache.ambari.server.orm.entities.ConfigGroupConfigMappingEntityPK configGroupConfigMappingEntityPK) {
        entityManagerProvider.get().remove(findByPK(configGroupConfigMappingEntityPK));
    }

    @com.google.inject.persist.Transactional
    public void removeAllByGroup(java.lang.Long groupId) {
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("DELETE FROM ConfigGroupConfigMappingEntity configs WHERE configs" + ".configGroupId = ?1", java.lang.Long.class);
        daoUtils.executeUpdate(query, groupId);
        entityManagerProvider.get().flush();
    }
}