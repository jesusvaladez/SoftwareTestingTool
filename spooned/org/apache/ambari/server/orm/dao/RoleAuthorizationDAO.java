package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class RoleAuthorizationDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorizationEntity) {
        entityManagerProvider.get().persist(roleAuthorizationEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.RoleAuthorizationEntity merge(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity roleAuthorizationEntity) {
        return entityManagerProvider.get().merge(roleAuthorizationEntity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.RoleAuthorizationEntity findById(java.lang.String id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> query = entityManagerProvider.get().createNamedQuery("findAll", org.apache.ambari.server.orm.entities.RoleAuthorizationEntity.class);
        return daoUtils.selectList(query);
    }
}