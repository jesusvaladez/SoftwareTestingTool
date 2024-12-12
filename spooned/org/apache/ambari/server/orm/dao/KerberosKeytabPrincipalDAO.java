package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.collections.CollectionUtils;
@com.google.inject.Singleton
public class KerberosKeytabPrincipalDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity) {
        entityManagerProvider.get().persist(kerberosKeytabPrincipalEntity);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity, org.apache.ambari.server.orm.entities.HostEntity hostEntity, org.apache.ambari.server.orm.entities.KerberosPrincipalEntity principalEntity) {
        entityManagerProvider.get().persist(new org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity(kerberosKeytabEntity, hostEntity, principalEntity));
    }

    public org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult findOrCreate(org.apache.ambari.server.orm.entities.KerberosKeytabEntity kerberosKeytabEntity, org.apache.ambari.server.orm.entities.HostEntity hostEntity, org.apache.ambari.server.orm.entities.KerberosPrincipalEntity kerberosPrincipalEntity, @javax.annotation.Nullable
    java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> keytabList) {
        org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult result = new org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KeytabPrincipalFindOrCreateResult();
        result.created = false;
        java.lang.Long hostId = (hostEntity == null) ? null : hostEntity.getHostId();
        org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkp = ((keytabList == null) || keytabList.isEmpty()) ? findByNaturalKey(hostId, kerberosKeytabEntity.getKeytabPath(), kerberosPrincipalEntity.getPrincipalName()) : keytabList.stream().filter(keytab -> ((((((keytab != null) && (keytab.getHostId() != null)) && (keytab.getKeytabPath() != null)) && (keytab.getPrincipalName() != null)) && keytab.getHostId().equals(hostId)) && keytab.getKeytabPath().equals(kerberosKeytabEntity.getKeytabPath())) && keytab.getPrincipalName().equals(kerberosPrincipalEntity.getPrincipalName())).findFirst().orElse(null);
        if (kkp != null) {
            result.kkp = kkp;
            return result;
        }
        kkp = new org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity(kerberosKeytabEntity, hostEntity, kerberosPrincipalEntity);
        create(kkp);
        kerberosKeytabEntity.addKerberosKeytabPrincipal(kkp);
        kerberosPrincipalEntity.addKerberosKeytabPrincipal(kkp);
        result.kkp = kkp;
        result.created = true;
        return result;
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity merge(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity) {
        return entityManagerProvider.get().merge(kerberosKeytabPrincipalEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kerberosKeytabPrincipalEntity) {
        entityManagerProvider.get().remove(merge(kerberosKeytabPrincipalEntity));
    }

    public void remove(java.util.Collection<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> kerberosKeytabPrincipalEntities) {
        for (org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity entity : kerberosKeytabPrincipalEntities) {
            remove(entity);
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> findByPrincipal(java.lang.String principal) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabPrincipalEntity.findByPrincipal", org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        query.setParameter("principalName", principal);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> findByHost(java.lang.Long hostId) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabPrincipalEntity.findByHost", org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        query.setParameter("hostId", hostId);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> findByHostAndKeytab(java.lang.Long hostId, java.lang.String keytabPath) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabPrincipalEntity.findByHostAndKeytab", org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        query.setParameter("hostId", hostId);
        query.setParameter("keytabPath", keytabPath);
        return daoUtils.selectList(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity findByHostKeytabAndPrincipal(java.lang.Long hostId, java.lang.String keytabPath, java.lang.String principalName) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabPrincipalEntity.findByHostKeytabAndPrincipal", org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        query.setParameter("hostId", hostId);
        query.setParameter("keytabPath", keytabPath);
        query.setParameter("principalName", principalName);
        return daoUtils.selectOne(query);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity findByKeytabAndPrincipalNullHost(java.lang.String keytabPath, java.lang.String principal) {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabPrincipalEntity.findByKeytabAndPrincipalNullHost", org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        query.setParameter("keytabPath", keytabPath);
        query.setParameter("principalName", principal);
        return daoUtils.selectOne(query);
    }

    public org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity findByNaturalKey(java.lang.Long hostId, java.lang.String keytabPath, java.lang.String principalName) {
        if (hostId == null) {
            return findByKeytabAndPrincipalNullHost(keytabPath, principalName);
        } else {
            return findByHostKeytabAndPrincipal(hostId, keytabPath, principalName);
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> findByFilter(org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter filter) {
        javax.persistence.criteria.CriteriaBuilder cb = entityManagerProvider.get().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> cq = cb.createQuery(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        javax.persistence.criteria.Root<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> root = cq.from(org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        java.util.ArrayList<javax.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filter.getServiceNames())) {
            javax.persistence.criteria.Join<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity, org.apache.ambari.server.orm.entities.KerberosKeytabServiceMappingEntity> mappingJoin = root.join("serviceMapping");
            predicates.add(mappingJoin.get("serviceName").in(filter.getServiceNames()));
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filter.getComponentNames())) {
                predicates.add(mappingJoin.get("componentName").in(filter.getComponentNames()));
            }
        }
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filter.getHostNames())) {
            java.util.List<java.lang.Long> hostIds = new java.util.ArrayList<>();
            boolean hasNull = false;
            for (java.lang.String hostname : filter.getHostNames()) {
                org.apache.ambari.server.orm.entities.HostEntity host = hostDAO.findByName(hostname);
                if (host == null) {
                    hasNull = true;
                } else {
                    hostIds.add(host.getHostId());
                }
            }
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(hostIds)) {
                java.util.List<javax.persistence.criteria.Predicate> hostPredicates = new java.util.ArrayList<>();
                org.apache.ambari.server.orm.helpers.SQLOperations.batch(hostIds, org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
                    hostPredicates.add(root.get("hostId").in(chunk));
                    return 0;
                });
                javax.persistence.criteria.Predicate hostCombinedPredicate = cb.or(hostPredicates.toArray(new javax.persistence.criteria.Predicate[hostPredicates.size()]));
                predicates.add(hostCombinedPredicate);
            }
            if (hasNull) {
                predicates.add(root.get("hostId").isNull());
            }
        }
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(filter.getPrincipals())) {
            java.util.ArrayList<javax.persistence.criteria.Predicate> principalPredicates = new java.util.ArrayList<>();
            org.apache.ambari.server.orm.helpers.SQLOperations.batch(filter.getPrincipals(), org.apache.ambari.server.orm.helpers.SQLConstants.IN_ARGUMENT_MAX_SIZE, (chunk, currentBatch, totalBatches, totalSize) -> {
                principalPredicates.add(root.get("principalName").in(chunk));
                return 0;
            });
            javax.persistence.criteria.Predicate principalCombinedPredicate = cb.or(principalPredicates.toArray(new javax.persistence.criteria.Predicate[0]));
            predicates.add(principalCombinedPredicate);
        }
        cq.where(cb.and(predicates.toArray(new javax.persistence.criteria.Predicate[0])));
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> query = entityManagerProvider.get().createQuery(cq);
        java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> result = query.getResultList();
        if (result == null) {
            return java.util.Collections.emptyList();
        }
        return result;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> findByFilters(java.util.Collection<org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter> filters) {
        java.util.ArrayList<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> result = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter filter : filters) {
            result.addAll(findByFilter(filter));
        }
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public boolean exists(java.lang.Long hostId, java.lang.String keytabPath, java.lang.String principalName) {
        return findByNaturalKey(hostId, keytabPath, principalName) != null;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> findAll() {
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> query = entityManagerProvider.get().createNamedQuery("KerberosKeytabPrincipalEntity.findAll", org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity.class);
        java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> result = query.getResultList();
        if (result == null) {
            return java.util.Collections.emptyList();
        }
        return result;
    }

    @com.google.inject.persist.Transactional
    public void remove(java.util.List<org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity> entities) {
        if (entities != null) {
            for (org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity entity : entities) {
                entityManagerProvider.get().remove(merge(entity));
            }
        }
    }

    public void removeByHost(java.lang.Long hostId) {
        remove(findByHost(hostId));
    }

    public static class KerberosKeytabPrincipalFilter {
        private java.util.Collection<java.lang.String> hostNames;

        private java.util.Collection<java.lang.String> serviceNames;

        private java.util.Collection<java.lang.String> componentNames;

        private java.util.Collection<java.lang.String> principals;

        private KerberosKeytabPrincipalFilter() {
            this(null, null, null, null);
        }

        private KerberosKeytabPrincipalFilter(java.util.Collection<java.lang.String> hostNames, java.util.Collection<java.lang.String> serviceNames, java.util.Collection<java.lang.String> componentNames, java.util.Collection<java.lang.String> principals) {
            this.hostNames = hostNames;
            this.serviceNames = serviceNames;
            this.componentNames = componentNames;
            this.principals = principals;
        }

        public java.util.Collection<java.lang.String> getHostNames() {
            return hostNames;
        }

        public void setHostNames(java.util.Collection<java.lang.String> hostNames) {
            this.hostNames = hostNames;
        }

        public java.util.Collection<java.lang.String> getServiceNames() {
            return serviceNames;
        }

        public void setServiceNames(java.util.Collection<java.lang.String> serviceNames) {
            this.serviceNames = serviceNames;
        }

        public java.util.Collection<java.lang.String> getComponentNames() {
            return componentNames;
        }

        public void setComponentNames(java.util.Collection<java.lang.String> componentNames) {
            this.componentNames = componentNames;
        }

        public java.util.Collection<java.lang.String> getPrincipals() {
            return principals;
        }

        public void setPrincipals(java.util.Collection<java.lang.String> principals) {
            this.principals = principals;
        }

        public static org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter createEmptyFilter() {
            return new org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter();
        }

        public static org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter createFilter(java.lang.String serviceName, java.util.Collection<java.lang.String> componentNames, java.util.Collection<java.lang.String> hostNames, java.util.Collection<java.lang.String> principalNames) {
            return new org.apache.ambari.server.orm.dao.KerberosKeytabPrincipalDAO.KerberosKeytabPrincipalFilter(hostNames, serviceName == null ? null : java.util.Collections.singleton(serviceName), componentNames, principalNames);
        }
    }

    public static class KeytabPrincipalFindOrCreateResult {
        public org.apache.ambari.server.orm.entities.KerberosKeytabPrincipalEntity kkp;

        public boolean created;
    }
}