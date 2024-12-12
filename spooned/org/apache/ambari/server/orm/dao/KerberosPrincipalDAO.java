package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.CollectionUtils;
@com.google.inject.Singleton
public class KerberosPrincipalDAO {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.KerberosPrincipalDAO.class);

    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity) {
        entityManagerProvider.get().persist(kerberosPrincipalEntity);
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.KerberosPrincipalEntity create(java.lang.String principalName, boolean service) {
        org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kpe = new org.apache.ambari.server.orm.entities.KerberosPrincipalEntity(principalName, service, null);
        create(kpe);
        return kpe;
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.KerberosPrincipalEntity merge(org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity) {
        return entityManagerProvider.get().merge(kerberosPrincipalEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity) {
        if (kerberosPrincipalEntity != null) {
            javax.persistence.EntityManager entityManager = entityManagerProvider.get();
            entityManager.remove(entityManager.merge(kerberosPrincipalEntity));
        }
    }

    @com.google.inject.persist.Transactional
    public void remove(java.lang.String principalName) {
        entityManagerProvider.get().remove(find(principalName));
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity) {
        entityManagerProvider.get().refresh(kerberosPrincipalEntity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.KerberosPrincipalEntity find(java.lang.String principalName) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.KerberosPrincipalEntity.class, principalName);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosPrincipalEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosPrincipalEntity> query = entityManagerProvider.get().createNamedQuery("KerberosPrincipalEntityFindAll", org.apache.ambari.server.orm.entities.KerberosPrincipalEntity.class);
        return query.getResultList();
    }

    @org.apache.ambari.server.orm.RequiresSession
    public boolean exists(java.lang.String principalName) {
        return find(principalName) != null;
    }

    public void remove(java.util.List<org.apache.ambari.server.orm.entities.KerberosPrincipalEntity> entities) {
        if (entities != null) {
            for (org.apache.ambari.server.orm.entities.KerberosPrincipalEntity entity : entities) {
                remove(entity);
            }
        }
    }

    public boolean removeIfNotReferenced(org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity) {
        if (kerberosPrincipalEntity != null) {
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(kerberosPrincipalEntity.getKerberosKeytabPrincipalEntities())) {
                java.util.ArrayList<java.lang.String> ids = new java.util.ArrayList<>();
                for (org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity entity : kerberosPrincipalEntity.getKerberosKeytabPrincipalEntities()) {
                    java.lang.Long id = entity.getKkpId();
                    if (id != null) {
                        ids.add(java.lang.String.valueOf(id));
                    }
                }
                org.apache.ambari.server.orm.dao.KerberosPrincipalDAO.LOG.info(java.lang.String.format("principal entry for %s is still referenced by [%s]", kerberosPrincipalEntity.getPrincipalName(), java.lang.String.join(",", ids)));
            } else {
                org.apache.ambari.server.orm.dao.KerberosPrincipalDAO.LOG.info(java.lang.String.format("principal entry for %s is no longer referenced. It will be removed.", kerberosPrincipalEntity.getPrincipalName()));
                remove(kerberosPrincipalEntity);
                return true;
            }
        }
        return false;
    }
}