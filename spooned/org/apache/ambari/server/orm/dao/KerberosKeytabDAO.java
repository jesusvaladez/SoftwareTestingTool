package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.CollectionUtils;
@com.google.inject.Singleton
public class KerberosKeytabDAO {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.dao.KerberosKeytabDAO.class);

    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO kerberosKeytabPrincipalDAO;

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity) {
        entityManagerProvider.get().persist(kerberosKeytabEntity);
    }

    public void create(java.lang.String keytabPath) {
        create(new org.apache.ambari.server.orm.entities.KerberosKeytabEntity(keytabPath));
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.KerberosKeytabEntity merge(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity) {
        return entityManagerProvider.get().merge(kerberosKeytabEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity) {
        if (kerberosKeytabEntity != null) {
            javax.persistence.EntityManager entityManager = entityManagerProvider.get();
            entityManager.remove(entityManager.merge(kerberosKeytabEntity));
        }
    }

    public void remove(java.lang.String keytabPath) {
        org.apache.ambari.server.orm.entities.KerberosKeytabEntity kke = find(keytabPath);
        if (kke != null) {
            remove(kke);
        }
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity) {
        entityManagerProvider.get().refresh(kerberosKeytabEntity);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.KerberosKeytabEntity find(java.lang.String keytabPath) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.KerberosKeytabEntity.class, keytabPath);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.KerberosKeytabEntity findOrCreate(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKerberosKeytab) {
        org.apache.ambari.server.orm.entities.KerberosKeytabEntity result = find(resolvedKerberosKeytab.getFile());
        if (result == null) {
            result = new org.apache.ambari.server.orm.entities.KerberosKeytabEntity(resolvedKerberosKeytab);
            create(result);
        }
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> findByPrincipalAndHost(java.lang.String principalName, java.lang.Long hostId) {
        com.google.common.base.Stopwatch stopwatch = com.google.common.base.Stopwatch.createStarted();
        if (hostId == null) {
            java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> result = findByPrincipalAndNullHost(principalName);
            org.apache.ambari.server.orm.dao.KerberosKeytabDAO.LOG.debug("Loading keytabs by principal name took {}ms", stopwatch.elapsed(java.util.concurrent.TimeUnit.MILLISECONDS));
            return result;
        }
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabEntity.findByPrincipalAndHost", org.apache.ambari.server.orm.entities.KerberosKeytabEntity.class);
        query.setParameter("hostId", hostId);
        query.setParameter("principalName", principalName);
        java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> result = query.getResultList();
        if (result == null) {
            return java.util.Collections.emptyList();
        }
        org.apache.ambari.server.orm.dao.KerberosKeytabDAO.LOG.debug("Loading keytabs by principal name and host took {}ms", stopwatch.elapsed(java.util.concurrent.TimeUnit.MILLISECONDS));
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> findByPrincipalAndNullHost(java.lang.String principalName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabEntity.findByPrincipalAndNullHost", org.apache.ambari.server.orm.entities.KerberosKeytabEntity.class);
        query.setParameter("principalName", principalName);
        java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> result = query.getResultList();
        if (result == null) {
            return java.util.Collections.emptyList();
        }
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabEntity.findAll", org.apache.ambari.server.orm.entities.KerberosKeytabEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> result = query.getResultList();
        if (result == null) {
            return java.util.Collections.emptyList();
        }
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public boolean exists(java.lang.String keytabPath) {
        return find(keytabPath) != null;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public boolean exists(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity) {
        return find(kerberosKeytabEntity.getKeytabPath()) != null;
    }

    public void remove(java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabEntity> entities) {
        if (entities != null) {
            for (org.apache.ambari.server.orm.entities.KerberosKeytabEntity entity : entities) {
                remove(entity);
            }
        }
    }

    public boolean removeIfNotReferenced(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity) {
        if (kerberosKeytabEntity != null) {
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(kerberosKeytabEntity.getKerberosKeytabPrincipalEntities())) {
                java.util.ArrayList<java.lang.String> ids = new java.util.ArrayList<>();
                for (org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity entity : kerberosKeytabEntity.getKerberosKeytabPrincipalEntities()) {
                    java.lang.Long id = entity.getKkpId();
                    if (id != null) {
                        ids.add(java.lang.String.valueOf(id));
                    }
                }
                org.apache.ambari.server.orm.dao.KerberosKeytabDAO.LOG.debug(java.lang.String.format("keytab entry for %s is still referenced by [%s]", kerberosKeytabEntity.getKeytabPath(), java.lang.String.join(",", ids)));
            } else {
                org.apache.ambari.server.orm.dao.KerberosKeytabDAO.LOG.debug(java.lang.String.format("keytab entry for %s is no longer referenced. It will be removed.", kerberosKeytabEntity.getKeytabPath()));
                remove(kerberosKeytabEntity);
                return true;
            }
        }
        return false;
    }
}