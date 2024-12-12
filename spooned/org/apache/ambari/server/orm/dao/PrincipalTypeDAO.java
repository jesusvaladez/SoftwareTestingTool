package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
@com.google.inject.Singleton
public class PrincipalTypeDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PrincipalTypeEntity findById(java.lang.Integer id) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class, id);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PrincipalTypeEntity findByName(java.lang.String name) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrincipalTypeEntity> query = entityManagerProvider.get().createNamedQuery("PrincipalTypeEntity.findByName", org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        query.setParameter("name", name);
        return daoUtils.selectSingle(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.PrincipalTypeEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.PrincipalTypeEntity> query = entityManagerProvider.get().createQuery("SELECT principalType FROM PrincipalTypeEntity principalType", org.apache.ambari.server.orm.entities.PrincipalTypeEntity.class);
        return daoUtils.selectList(query);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.PrincipalTypeEntity entity) {
        entityManagerProvider.get().persist(entity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.PrincipalTypeEntity merge(org.apache.ambari.server.orm.entities.PrincipalTypeEntity entity) {
        return entityManagerProvider.get().merge(entity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.PrincipalTypeEntity entity) {
        entityManagerProvider.get().remove(entity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.PrincipalTypeEntity ensurePrincipalTypeCreated(int principalType) {
        org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalTypeEntity = findById(principalType);
        if (principalTypeEntity == null) {
            principalTypeEntity = new org.apache.ambari.server.orm.entities.PrincipalTypeEntity();
            principalTypeEntity.setId(principalType);
            switch (principalType) {
                case org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE :
                    principalTypeEntity.setName(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME);
                    break;
                case org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE :
                    principalTypeEntity.setName(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME);
                    break;
                case org.apache.ambari.server.orm.entities.PrincipalTypeEntity.ROLE_PRINCIPAL_TYPE :
                    principalTypeEntity.setName(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.ROLE_PRINCIPAL_TYPE_NAME);
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown principal type ID=" + principalType);
            }
            create(principalTypeEntity);
        }
        return principalTypeEntity;
    }
}