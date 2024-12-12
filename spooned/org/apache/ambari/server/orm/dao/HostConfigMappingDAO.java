package org.apache.ambari.server.orm.dao;
import com.google.inject.persist.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
@com.google.inject.Singleton
public class HostConfigMappingDAO {
    @com.google.inject.Inject
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private java.util.concurrent.ConcurrentHashMap<java.lang.Long, java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping>> hostConfigMappingByHost;

    private volatile boolean cacheLoaded;

    private void populateCache() {
        if (!cacheLoaded) {
            if (hostConfigMappingByHost == null) {
                hostConfigMappingByHost = new java.util.concurrent.ConcurrentHashMap<>();
                javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostConfigMappingEntity> query = entityManagerProvider.get().createNamedQuery("HostConfigMappingEntity.findAll", org.apache.ambari.server.orm.entities.HostConfigMappingEntity.class);
                java.util.List<org.apache.ambari.server.orm.entities.HostConfigMappingEntity> hostConfigMappingEntities = daoUtils.selectList(query);
                for (org.apache.ambari.server.orm.entities.HostConfigMappingEntity hostConfigMappingEntity : hostConfigMappingEntities) {
                    java.lang.Long hostId = hostConfigMappingEntity.getHostId();
                    if (hostId == null) {
                        continue;
                    }
                    java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> setByHost;
                    if (hostConfigMappingByHost.containsKey(hostId)) {
                        setByHost = hostConfigMappingByHost.get(hostId);
                    } else {
                        setByHost = new java.util.HashSet<>();
                        hostConfigMappingByHost.put(hostId, setByHost);
                    }
                    org.apache.ambari.server.orm.cache.HostConfigMapping hostConfigMapping = buildHostConfigMapping(hostConfigMappingEntity);
                    setByHost.add(hostConfigMapping);
                }
            }
            cacheLoaded = true;
        }
    }

    @com.google.inject.persist.Transactional
    public void create(org.apache.ambari.server.orm.cache.HostConfigMapping hostConfigMapping) {
        populateCache();
        entityManagerProvider.get().persist(buildHostConfigMappingEntity(hostConfigMapping));
        java.lang.Long hostId = hostConfigMapping.getHostId();
        if (hostId != null) {
            java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> set;
            if (hostConfigMappingByHost.containsKey(hostId)) {
                set = hostConfigMappingByHost.get(hostId);
            } else {
                set = new java.util.HashSet<>();
                hostConfigMappingByHost.put(hostId, set);
            }
            set.add(hostConfigMapping);
        }
    }

    @com.google.inject.persist.Transactional
    public org.apache.ambari.server.orm.cache.HostConfigMapping merge(org.apache.ambari.server.orm.cache.HostConfigMapping hostConfigMapping) {
        populateCache();
        java.lang.Long hostId = hostConfigMapping.getHostId();
        if (hostId != null) {
            java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> set;
            if (hostConfigMappingByHost.containsKey(hostId)) {
                set = hostConfigMappingByHost.get(hostId);
            } else {
                set = new java.util.HashSet<>();
                hostConfigMappingByHost.put(hostId, set);
            }
            set.remove(hostConfigMapping);
            set.add(hostConfigMapping);
            entityManagerProvider.get().merge(buildHostConfigMappingEntity(hostConfigMapping));
        }
        return hostConfigMapping;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> findByType(final long clusterId, java.lang.Long hostId, final java.lang.String type) {
        populateCache();
        if (!hostConfigMappingByHost.containsKey(hostId))
            return java.util.Collections.emptySet();

        java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> set = new java.util.HashSet<>(hostConfigMappingByHost.get(hostId));
        org.apache.commons.collections.CollectionUtils.filter(set, new org.apache.commons.collections.Predicate() {
            @java.lang.Override
            public boolean evaluate(java.lang.Object arg0) {
                return ((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getClusterId().equals(clusterId) && ((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getType().equals(type);
            }
        });
        return set;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public org.apache.ambari.server.orm.cache.HostConfigMapping findSelectedByType(final long clusterId, java.lang.Long hostId, final java.lang.String type) {
        populateCache();
        if (!hostConfigMappingByHost.containsKey(hostId))
            return null;

        java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> set = new java.util.HashSet<>(hostConfigMappingByHost.get(hostId));
        org.apache.ambari.server.orm.cache.HostConfigMapping result = ((org.apache.ambari.server.orm.cache.HostConfigMapping) (org.apache.commons.collections.CollectionUtils.find(set, new org.apache.commons.collections.Predicate() {
            @java.lang.Override
            public boolean evaluate(java.lang.Object arg0) {
                return (((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getClusterId().equals(clusterId) && ((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getType().equals(type)) && (((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getSelected() > 0);
            }
        })));
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> findSelected(final long clusterId, java.lang.Long hostId) {
        populateCache();
        if (!hostConfigMappingByHost.containsKey(hostId))
            return java.util.Collections.emptySet();

        java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> set = new java.util.HashSet<>(hostConfigMappingByHost.get(hostId));
        org.apache.commons.collections.CollectionUtils.filter(set, new org.apache.commons.collections.Predicate() {
            @java.lang.Override
            public boolean evaluate(java.lang.Object arg0) {
                return ((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getClusterId().equals(clusterId) && (((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getSelected() > 0);
            }
        });
        return set;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> findSelectedByHosts(java.util.Collection<java.lang.Long> hostIds) {
        populateCache();
        if ((hostIds == null) || hostIds.isEmpty()) {
            return java.util.Collections.emptySet();
        }
        java.util.HashSet<org.apache.ambari.server.orm.cache.HostConfigMapping> result = new java.util.HashSet<>();
        for (final java.lang.Long hostId : hostIds) {
            if (!hostConfigMappingByHost.containsKey(hostId))
                continue;

            java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> set = new java.util.HashSet<>(hostConfigMappingByHost.get(hostId));
            org.apache.commons.collections.CollectionUtils.filter(set, new org.apache.commons.collections.Predicate() {
                @java.lang.Override
                public boolean evaluate(java.lang.Object arg0) {
                    return ((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getHostId().equals(hostId) && (((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getSelected() > 0);
                }
            });
            result.addAll(set);
        }
        return result;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.orm.cache.HostConfigMapping>> findSelectedHostsByTypes(final long clusterId, java.util.Collection<java.lang.String> types) {
        populateCache();
        java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.orm.cache.HostConfigMapping>> mappingsByType = new java.util.HashMap<>();
        for (java.lang.String type : types) {
            if (!mappingsByType.containsKey(type)) {
                mappingsByType.put(type, new java.util.ArrayList<>());
            }
        }
        if (!types.isEmpty()) {
            java.util.List<org.apache.ambari.server.orm.cache.HostConfigMapping> mappings = new java.util.ArrayList<>();
            for (java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> entries : hostConfigMappingByHost.values()) {
                for (org.apache.ambari.server.orm.cache.HostConfigMapping entry : entries) {
                    if (types.contains(entry.getType()) && entry.getClusterId().equals(clusterId))
                        mappings.add(new org.apache.ambari.server.orm.cache.HostConfigMappingImpl(entry));

                }
            }
            for (org.apache.ambari.server.orm.cache.HostConfigMapping mapping : mappings) {
                mappingsByType.get(mapping.getType()).add(mapping);
            }
        }
        return mappingsByType;
    }

    @org.apache.ambari.server.orm.RequiresSession
    public java.util.List<org.apache.ambari.server.orm.entities.HostConfigMappingEntity> findAll() {
        return daoUtils.selectAll(entityManagerProvider.get(), org.apache.ambari.server.orm.entities.HostConfigMappingEntity.class);
    }

    @com.google.inject.persist.Transactional
    public void removeByHostId(java.lang.Long hostId) {
        populateCache();
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findById(hostId);
        if (hostEntity != null) {
            if (hostConfigMappingByHost.containsKey(hostEntity.getHostId())) {
                javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostConfigMappingEntity> query = entityManagerProvider.get().createNamedQuery("HostConfigMappingEntity.findByHostId", org.apache.ambari.server.orm.entities.HostConfigMappingEntity.class);
                query.setParameter("hostId", hostId);
                java.util.List<org.apache.ambari.server.orm.entities.HostConfigMappingEntity> hostConfigMappingEntities = daoUtils.selectList(query);
                for (org.apache.ambari.server.orm.entities.HostConfigMappingEntity entity : hostConfigMappingEntities) {
                    entityManagerProvider.get().remove(entity);
                }
                hostConfigMappingByHost.remove(hostEntity.getHostId());
            }
        }
    }

    @com.google.inject.persist.Transactional
    public void removeByClusterAndHostName(final long clusterId, java.lang.String hostName) {
        populateCache();
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
        if (hostEntity != null) {
            if (hostConfigMappingByHost.containsKey(hostEntity.getHostId())) {
                javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.HostConfigMappingEntity> query = entityManagerProvider.get().createQuery("SELECT entity FROM HostConfigMappingEntity entity " + "WHERE entity.clusterId = ?1 AND entity.hostId=?2", org.apache.ambari.server.orm.entities.HostConfigMappingEntity.class);
                java.util.List<org.apache.ambari.server.orm.entities.HostConfigMappingEntity> list = daoUtils.selectList(query, clusterId, hostEntity.getHostId());
                for (org.apache.ambari.server.orm.entities.HostConfigMappingEntity entity : list) {
                    entityManagerProvider.get().remove(entity);
                }
                java.util.Set<org.apache.ambari.server.orm.cache.HostConfigMapping> set = hostConfigMappingByHost.get(hostEntity.getHostId());
                org.apache.commons.collections.CollectionUtils.filter(set, new org.apache.commons.collections.Predicate() {
                    @java.lang.Override
                    public boolean evaluate(java.lang.Object arg0) {
                        return !((org.apache.ambari.server.orm.cache.HostConfigMapping) (arg0)).getClusterId().equals(clusterId);
                    }
                });
            }
        }
    }

    public org.apache.ambari.server.orm.entities.HostConfigMappingEntity buildHostConfigMappingEntity(org.apache.ambari.server.orm.cache.HostConfigMapping hostConfigMapping) {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findById(hostConfigMapping.getHostId());
        org.apache.ambari.server.orm.entities.HostConfigMappingEntity hostConfigMappingEntity = new org.apache.ambari.server.orm.entities.HostConfigMappingEntity();
        hostConfigMappingEntity.setClusterId(hostConfigMapping.getClusterId());
        hostConfigMappingEntity.setCreateTimestamp(hostConfigMapping.getCreateTimestamp());
        hostConfigMappingEntity.setHostId(hostEntity.getHostId());
        hostConfigMappingEntity.setSelected(hostConfigMapping.getSelected());
        hostConfigMappingEntity.setServiceName(hostConfigMapping.getServiceName());
        hostConfigMappingEntity.setType(hostConfigMapping.getType());
        hostConfigMappingEntity.setUser(hostConfigMapping.getUser());
        hostConfigMappingEntity.setVersion(hostConfigMapping.getVersion());
        return hostConfigMappingEntity;
    }

    public org.apache.ambari.server.orm.cache.HostConfigMapping buildHostConfigMapping(org.apache.ambari.server.orm.entities.HostConfigMappingEntity hostConfigMappingEntity) {
        org.apache.ambari.server.orm.cache.HostConfigMapping hostConfigMapping = new org.apache.ambari.server.orm.cache.HostConfigMappingImpl();
        hostConfigMapping.setClusterId(hostConfigMappingEntity.getClusterId());
        hostConfigMapping.setCreateTimestamp(hostConfigMappingEntity.getCreateTimestamp());
        hostConfigMapping.setHostId(hostConfigMappingEntity.getHostId());
        hostConfigMapping.setServiceName(hostConfigMappingEntity.getServiceName());
        hostConfigMapping.setType(hostConfigMappingEntity.getType());
        hostConfigMapping.setUser(hostConfigMappingEntity.getUser());
        hostConfigMapping.setSelected(hostConfigMappingEntity.isSelected());
        hostConfigMapping.setVersion(hostConfigMappingEntity.getVersion());
        return hostConfigMapping;
    }
}