package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
@com.google.inject.Singleton
public class ConfigGroupHostMappingDAO {
    @com.google.inject.Inject
    com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.cluster.ClusterFactory clusterFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.host.HostFactory hostFactory;

    @com.google.inject.Inject
    org.apache.ambari.server.state.Clusters clusters;

    private final java.util.concurrent.locks.ReadWriteLock gl = new java.util.concurrent.locks.ReentrantReadWriteLock();

    private java.util.Map<java.lang.Long, java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping>> configGroupHostMappingByHost;

    private volatile boolean cacheLoaded;

    private void populateCache() {
        if (!cacheLoaded) {
            gl.writeLock().lock();
            try {
                if (!cacheLoaded) {
                    if (configGroupHostMappingByHost == null) {
                        configGroupHostMappingByHost = new java.util.HashMap<>();
                        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity> query = entityManagerProvider.get().createQuery("SELECT entity FROM ConfigGroupHostMappingEntity entity", org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity.class);
                        java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity> configGroupHostMappingEntities = daoUtils.selectList(query);
                        for (org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity configGroupHostMappingEntity : configGroupHostMappingEntities) {
                            java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> setByHost = configGroupHostMappingByHost.get(configGroupHostMappingEntity.getHostId());
                            if (setByHost == null) {
                                setByHost = new java.util.HashSet<>();
                                configGroupHostMappingByHost.put(configGroupHostMappingEntity.getHostId(), setByHost);
                            }
                            org.apache.ambari.server.orm.cache.ConfigGroupHostMapping configGroupHostMapping = buildConfigGroupHostMapping(configGroupHostMappingEntity);
                            setByHost.add(configGroupHostMapping);
                        }
                    }
                    cacheLoaded = true;
                }
            } finally {
                gl.writeLock().unlock();
            }
        }
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity findByPK(final org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntityPK configGroupHostMappingEntityPK) {
        return entityManagerProvider.get().find(org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity.class, configGroupHostMappingEntityPK);
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> findByHostId(java.lang.Long hostId) {
        populateCache();
        if (!configGroupHostMappingByHost.containsKey(hostId)) {
            return null;
        }
        java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> set = new java.util.HashSet<>(configGroupHostMappingByHost.get(hostId));
        return set;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> findByGroup(final java.lang.Long groupId) {
        populateCache();
        java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> result = new java.util.HashSet<>();
        for (java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> item : configGroupHostMappingByHost.values()) {
            java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> setByHost = new java.util.HashSet<>(item);
            org.apache.commons.collections.CollectionUtils.filter(setByHost, new org.apache.commons.collections.Predicate() {
                @java.lang.Override
                public boolean evaluate(java.lang.Object arg0) {
                    return ((org.apache.ambari.server.orm.cache.ConfigGroupHostMapping) (arg0)).getConfigGroupId().equals(groupId);
                }
            });
            result.addAll(setByHost);
        }
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity configGroupHostMappingEntity) {
        populateCache();
        entityManagerProvider.get().persist(configGroupHostMappingEntity);
        java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> set = configGroupHostMappingByHost.get(configGroupHostMappingEntity.getHostId());
        if (set == null) {
            set = new java.util.HashSet<>();
            configGroupHostMappingByHost.put(configGroupHostMappingEntity.getHostId(), set);
        }
        set.add(buildConfigGroupHostMapping(configGroupHostMappingEntity));
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity merge(org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity configGroupHostMappingEntity) {
        populateCache();
        java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> set = configGroupHostMappingByHost.get(configGroupHostMappingEntity.getHostId());
        if (set == null) {
            set = new java.util.HashSet<>();
            configGroupHostMappingByHost.put(configGroupHostMappingEntity.getHostId(), set);
        }
        set.remove(buildConfigGroupHostMapping(configGroupHostMappingEntity));
        set.add(buildConfigGroupHostMapping(configGroupHostMappingEntity));
        return entityManagerProvider.get().merge(configGroupHostMappingEntity);
    }

    @com.google.inject.persist.Transactional
    public void refresh(org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity configGroupHostMappingEntity) {
        cacheLoaded = false;
        populateCache();
        entityManagerProvider.get().refresh(configGroupHostMappingEntity);
    }

    @com.google.inject.persist.Transactional
    public void remove(final org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity configGroupHostMappingEntity) {
        populateCache();
        entityManagerProvider.get().remove(merge(configGroupHostMappingEntity));
        java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> setByHost = configGroupHostMappingByHost.get(configGroupHostMappingEntity.getHostId());
        if (setByHost != null) {
            org.apache.commons.collections.CollectionUtils.filter(setByHost, new org.apache.commons.collections.Predicate() {
                @java.lang.Override
                public boolean evaluate(java.lang.Object arg0) {
                    return !((org.apache.ambari.server.orm.cache.ConfigGroupHostMapping) (arg0)).getConfigGroupId().equals(configGroupHostMappingEntity.getConfigGroupId());
                }
            });
        }
    }

    @com.google.inject.persist.Transactional
    public void removeByPK(final org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntityPK configGroupHostMappingEntityPK) {
        populateCache();
        entityManagerProvider.get().remove(findByPK(configGroupHostMappingEntityPK));
        java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> setByHost = configGroupHostMappingByHost.get(configGroupHostMappingEntityPK.getHostId());
        if (setByHost != null) {
            org.apache.commons.collections.CollectionUtils.filter(setByHost, new org.apache.commons.collections.Predicate() {
                @java.lang.Override
                public boolean evaluate(java.lang.Object arg0) {
                    return !((org.apache.ambari.server.orm.cache.ConfigGroupHostMapping) (arg0)).getConfigGroupId().equals(configGroupHostMappingEntityPK.getConfigGroupId());
                }
            });
        }
    }

    @com.google.inject.persist.Transactional
    public void removeAllByGroup(final java.lang.Long groupId) {
        populateCache();
        javax.persistence.TypedQuery<java.lang.Long> query = entityManagerProvider.get().createQuery("DELETE FROM ConfigGroupHostMappingEntity confighosts WHERE " + "confighosts.configGroupId = ?1", java.lang.Long.class);
        daoUtils.executeUpdate(query, groupId);
        entityManagerProvider.get().flush();
        for (java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> setByHost : configGroupHostMappingByHost.values()) {
            org.apache.commons.collections.CollectionUtils.filter(setByHost, new org.apache.commons.collections.Predicate() {
                @java.lang.Override
                public boolean evaluate(java.lang.Object arg0) {
                    return !((org.apache.ambari.server.orm.cache.ConfigGroupHostMapping) (arg0)).getConfigGroupId().equals(groupId);
                }
            });
        }
    }

    @com.google.inject.persist.Transactional
    public void removeAllByHost(java.lang.Long hostId) {
        javax.persistence.TypedQuery<java.lang.String> query = entityManagerProvider.get().createQuery("DELETE FROM ConfigGroupHostMappingEntity confighosts WHERE " + "confighosts.hostId = ?1", java.lang.String.class);
        daoUtils.executeUpdate(query, hostId);
        java.util.Set<org.apache.ambari.server.orm.cache.ConfigGroupHostMapping> setByHost = configGroupHostMappingByHost.get(hostId);
        setByHost.clear();
    }

    private org.apache.ambari.server.orm.cache.ConfigGroupHostMapping buildConfigGroupHostMapping(org.apache.ambari.server.orm.entities.ConfigGroupHostMappingEntity configGroupHostMappingEntity) {
        org.apache.ambari.server.orm.cache.ConfigGroupHostMappingImpl configGroupHostMapping = new org.apache.ambari.server.orm.cache.ConfigGroupHostMappingImpl();
        configGroupHostMapping.setConfigGroup(buildConfigGroup(configGroupHostMappingEntity.getConfigGroupEntity()));
        configGroupHostMapping.setConfigGroupId(configGroupHostMappingEntity.getConfigGroupId());
        configGroupHostMapping.setHost(buildHost(configGroupHostMappingEntity.getHostEntity()));
        configGroupHostMapping.setHostId(configGroupHostMappingEntity.getHostId());
        return configGroupHostMapping;
    }

    private org.apache.ambari.server.state.configgroup.ConfigGroup buildConfigGroup(org.apache.ambari.server.orm.entities.ConfigGroupEntity configGroupEntity) {
        org.apache.ambari.server.state.Cluster cluster = null;
        try {
            cluster = clusters.getClusterById(configGroupEntity.getClusterId());
        } catch (org.apache.ambari.server.AmbariException e) {
        }
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createExisting(cluster, configGroupEntity);
        return configGroup;
    }

    private org.apache.ambari.server.state.Host buildHost(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        org.apache.ambari.server.state.Host host = hostFactory.create(hostEntity);
        return host;
    }
}