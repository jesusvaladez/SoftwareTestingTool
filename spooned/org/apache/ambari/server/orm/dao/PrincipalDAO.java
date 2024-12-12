package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class PrincipalDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PrincipalEntity findById(java.lang.Long id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.PrincipalEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrincipalEntity> query = entityManagerProvider.get().createQuery("SELECT principal FROM PrincipalEntity principal", org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> findByPermissionId(java.lang.Integer id) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrincipalEntity> query = entityManagerProvider.get().createNamedQuery("principalByPrivilegeId", org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        query.setParameter("permission_id", id);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> findByPrincipalType(java.lang.String name) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrincipalEntity> query = entityManagerProvider.get().createNamedQuery("principalByPrincipalType", org.apache.ambari.server.orm.entities.PrincipalEntity.class);
        query.setParameter("principal_type", name);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.PrincipalEntity entity) {
        create(java.util.Arrays.asList(entity));
    }

    @com.google.inject.persist.Transactional
    public void create(java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> entities) {
        for (org.apache.ambari.server.orm.entities.PrincipalEntity entity : entities) {
            entityManagerProvider.get().persist(entity);
        }
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.PrincipalEntity merge(org.apache.ambari.server.orm.entities.PrincipalEntity entity) {
        return entityManagerProvider.get().merge(entity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.PrincipalEntity entity) {
        entityManagerProvider.get().remove(entity);
    }
}