package org.apache.ambari.server.state.alert;
@com.google.inject.Singleton
public class AlertDefinitionHash {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.alert.AlertDefinitionHash.class);

    public static final java.lang.String NULL_MD5_HASH = "37a6259cc0c1dae299a7866489dff0bd";

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.AlertDefinitionDAO m_definitionDao;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.alert.AlertDefinitionFactory m_factory;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.Clusters> m_clusters;

    @com.google.inject.Inject
    private com.google.inject.Provider<org.apache.ambari.server.state.ConfigHelper> m_configHelper;

    private java.util.concurrent.locks.ReentrantLock m_actionQueueLock = new java.util.concurrent.locks.ReentrantLock();

    private java.util.concurrent.ConcurrentMap<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.String>> m_hashes = new java.util.concurrent.ConcurrentHashMap<>();

    public java.lang.String getHash(java.lang.String clusterName, java.lang.String hostName) {
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.String> clusterMapping = m_hashes.get(hostName);
        if (null == clusterMapping) {
            clusterMapping = new java.util.concurrent.ConcurrentHashMap<>();
            java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.String> temp = m_hashes.putIfAbsent(hostName, clusterMapping);
            if (temp != null) {
                clusterMapping = temp;
            }
        }
        java.lang.String hash = clusterMapping.get(hostName);
        if (null != hash) {
            return hash;
        }
        hash = hash(clusterName, hostName);
        clusterMapping.put(clusterName, hash);
        return hash;
    }

    public void invalidateAll() {
        m_hashes.clear();
    }

    public void invalidate(java.lang.String hostName) {
        m_hashes.remove(hostName);
    }

    public void invalidate(java.lang.String clusterName, java.lang.String hostName) {
        java.util.Map<java.lang.String, java.lang.String> clusterMapping = m_hashes.get(hostName);
        if (null != clusterMapping) {
            clusterMapping.remove(clusterName);
        }
    }

    public boolean isHashCached(java.lang.String clusterName, java.lang.String hostName) {
        if ((null == clusterName) || (null == hostName)) {
            return false;
        }
        java.util.Map<java.lang.String, java.lang.String> clusterMapping = m_hashes.get(hostName);
        if (null == clusterMapping) {
            return false;
        }
        return clusterMapping.containsKey(clusterName);
    }

    public java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> getAlertDefinitions(java.lang.String clusterName, java.lang.String hostName) {
        return coerce(getAlertDefinitionEntities(clusterName, hostName));
    }

    public java.util.Map<java.lang.Long, java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition>> getAlertDefinitions(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.Long, java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition>> result = new java.util.HashMap<>();
        java.lang.String hostName = m_clusters.get().getHostById(hostId).getHostName();
        for (org.apache.ambari.server.state.Cluster cluster : m_clusters.get().getClustersForHost(hostName)) {
            java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> alertDefinitions = getAlertDefinitions(cluster.getClusterName(), hostName);
            result.put(cluster.getClusterId(), org.apache.ambari.server.state.alert.AlertDefinitionHash.mapById(alertDefinitions));
        }
        return result;
    }

    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> findByServiceComponent(long clusterId, java.lang.String serviceName, java.lang.String componentName) {
        return org.apache.ambari.server.state.alert.AlertDefinitionHash.mapById(coerce(m_definitionDao.findByServiceComponent(clusterId, serviceName, componentName)));
    }

    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> findByServiceMaster(long clusterId, java.lang.String... serviceName) {
        return org.apache.ambari.server.state.alert.AlertDefinitionHash.mapById(coerce(m_definitionDao.findByServiceMaster(clusterId, com.google.common.collect.Sets.newHashSet(serviceName))));
    }

    public java.util.Set<java.lang.String> invalidateHosts(org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition) {
        return invalidateHosts(definition.getClusterId(), definition.getSourceType(), definition.getDefinitionName(), definition.getServiceName(), definition.getComponentName());
    }

    public java.util.Set<java.lang.String> invalidateHosts(org.apache.ambari.server.state.alert.AlertDefinition definition) {
        return invalidateHosts(definition.getClusterId(), definition.getSource().getType(), definition.getName(), definition.getServiceName(), definition.getComponentName());
    }

    private java.util.Set<java.lang.String> invalidateHosts(long clusterId, org.apache.ambari.server.state.alert.SourceType definitionSourceType, java.lang.String definitionName, java.lang.String definitionServiceName, java.lang.String definitionComponentName) {
        org.apache.ambari.server.state.Cluster cluster = null;
        java.lang.String clusterName = null;
        try {
            cluster = m_clusters.get().getClusterById(clusterId);
            if (null != cluster) {
                clusterName = cluster.getClusterName();
            }
            if (null == cluster) {
                org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.warn("Unable to lookup cluster with ID {}", clusterId);
            }
        } catch (java.lang.Exception exception) {
            org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.error("Unable to lookup cluster with ID {}", clusterId, exception);
        }
        if (null == cluster) {
            return java.util.Collections.emptySet();
        }
        java.util.Set<java.lang.String> affectedHosts = getAssociatedHosts(cluster, definitionSourceType, definitionName, definitionServiceName, definitionComponentName);
        for (java.lang.String hostName : affectedHosts) {
            invalidate(clusterName, hostName);
        }
        return affectedHosts;
    }

    public java.util.Set<java.lang.String> getAssociatedHosts(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.alert.SourceType definitionSourceType, java.lang.String definitionName, java.lang.String definitionServiceName, java.lang.String definitionComponentName) {
        if (definitionSourceType == org.apache.ambari.server.state.alert.SourceType.AGGREGATE) {
            return java.util.Collections.emptySet();
        }
        java.lang.String clusterName = cluster.getClusterName();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = m_clusters.get().getHostsForCluster(clusterName);
        java.util.Set<java.lang.String> affectedHosts = new java.util.HashSet<>();
        java.lang.String ambariServiceName = org.apache.ambari.server.controller.RootService.AMBARI.name();
        java.lang.String agentComponentName = org.apache.ambari.server.controller.RootComponent.AMBARI_AGENT.name();
        if (ambariServiceName.equals(definitionServiceName) && agentComponentName.equals(definitionComponentName)) {
            affectedHosts.addAll(hosts.keySet());
            return affectedHosts;
        }
        if (ambariServiceName.equalsIgnoreCase(definitionServiceName)) {
            return java.util.Collections.emptySet();
        }
        for (java.lang.String hostName : hosts.keySet()) {
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> hostComponents = cluster.getServiceComponentHosts(hostName);
            if ((null == hostComponents) || (hostComponents.size() == 0)) {
                continue;
            }
            for (org.apache.ambari.server.state.ServiceComponentHost component : hostComponents) {
                java.lang.String serviceName = component.getServiceName();
                java.lang.String componentName = component.getServiceComponentName();
                if (serviceName.equals(definitionServiceName) && componentName.equals(definitionComponentName)) {
                    affectedHosts.add(hostName);
                }
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = cluster.getServices();
        org.apache.ambari.server.state.Service service = services.get(definitionServiceName);
        if (null == service) {
            org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.warn("The alert definition {} has an unknown service of {}", definitionName, definitionServiceName);
            return affectedHosts;
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components = service.getServiceComponents();
        if (null != components) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponent> component : components.entrySet()) {
                if (component.getValue().isMasterComponent()) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> componentHosts = component.getValue().getServiceComponentHosts();
                    if (null != componentHosts) {
                        affectedHosts.addAll(componentHosts.keySet());
                    }
                }
            }
        }
        return affectedHosts;
    }

    public void enqueueAgentCommands(long clusterId) {
        java.lang.String clusterName = null;
        java.util.Collection<java.lang.String> hostNames;
        try {
            org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getClusterById(clusterId);
            clusterName = cluster.getClusterName();
            java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
            hostNames = new java.util.ArrayList<>(hosts.size());
            for (org.apache.ambari.server.state.Host host : hosts) {
                hostNames.add(host.getHostName());
            }
            enqueueAgentCommands(cluster, clusterName, hostNames);
        } catch (org.apache.ambari.server.AmbariException ae) {
            org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.error("Unable to lookup cluster for alert definition commands", ae);
        }
    }

    public void enqueueAgentCommands(long clusterId, java.util.Collection<java.lang.String> hosts) {
        java.lang.String clusterName = null;
        org.apache.ambari.server.state.Cluster cluster = null;
        try {
            cluster = m_clusters.get().getClusterById(clusterId);
            clusterName = cluster.getClusterName();
        } catch (org.apache.ambari.server.AmbariException ae) {
            org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.error("Unable to lookup cluster for alert definition commands", ae);
        }
        enqueueAgentCommands(cluster, clusterName, hosts);
    }

    private void enqueueAgentCommands(org.apache.ambari.server.state.Cluster cluster, java.lang.String clusterName, java.util.Collection<java.lang.String> hosts) {
        if (null == clusterName) {
            org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.warn("Unable to create alert definition agent commands because of a null cluster name");
            return;
        }
        if ((null == hosts) || (hosts.size() == 0)) {
            return;
        }
        try {
            m_actionQueueLock.lock();
            for (java.lang.String hostName : hosts) {
                java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> definitions = getAlertDefinitions(clusterName, hostName);
                java.lang.String hash = getHash(clusterName, hostName);
                org.apache.ambari.server.state.Host host = cluster.getHost(hostName);
                java.lang.String publicHostName = (host == null) ? hostName : host.getPublicHostName();
                org.apache.ambari.server.agent.AlertDefinitionCommand command = new org.apache.ambari.server.agent.AlertDefinitionCommand(clusterName, hostName, publicHostName, hash, definitions);
                try {
                    command.addConfigs(m_configHelper.get(), cluster);
                } catch (org.apache.ambari.server.AmbariException ae) {
                    org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.warn("Unable to add configurations to alert definition command", ae);
                }
            }
        } finally {
            m_actionQueueLock.unlock();
        }
    }

    private java.lang.String hash(java.lang.String clusterName, java.lang.String hostName) {
        java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = getAlertDefinitionEntities(clusterName, hostName);
        if (definitions.isEmpty()) {
            return org.apache.ambari.server.state.alert.AlertDefinitionHash.NULL_MD5_HASH;
        }
        java.util.Iterator<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> iterator = definitions.iterator();
        while (iterator.hasNext()) {
            if (org.apache.ambari.server.state.alert.SourceType.AGGREGATE.equals(iterator.next().getSourceType())) {
                iterator.remove();
            }
        } 
        java.util.List<java.lang.String> uuids = new java.util.ArrayList<>(definitions.size());
        for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity definition : definitions) {
            uuids.add(definition.getHash());
        }
        java.util.Collections.sort(uuids);
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            for (java.lang.String uuid : uuids) {
                digest.update(uuid.getBytes());
            }
            byte[] hashBytes = digest.digest();
            return org.apache.commons.codec.binary.Hex.encodeHexString(hashBytes);
        } catch (java.security.NoSuchAlgorithmException nsae) {
            org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.warn("Unable to calculate MD5 alert definition hash", nsae);
            return org.apache.ambari.server.state.alert.AlertDefinitionHash.NULL_MD5_HASH;
        }
    }

    private java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> getAlertDefinitionEntities(java.lang.String clusterName, java.lang.String hostName) {
        java.util.Set<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> definitions = new java.util.HashSet<>();
        try {
            org.apache.ambari.server.state.Cluster cluster = m_clusters.get().getCluster(clusterName);
            if (null == cluster) {
                return java.util.Collections.emptySet();
            }
            long clusterId = cluster.getClusterId();
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponents = cluster.getServiceComponentHosts(hostName);
            if ((null == serviceComponents) || (!serviceComponents.isEmpty())) {
                if (serviceComponents != null) {
                    for (org.apache.ambari.server.state.ServiceComponentHost serviceComponent : serviceComponents) {
                        java.lang.String serviceName = serviceComponent.getServiceName();
                        java.lang.String componentName = serviceComponent.getServiceComponentName();
                        definitions.addAll(m_definitionDao.findByServiceComponent(clusterId, serviceName, componentName));
                    }
                }
                java.util.Set<java.lang.String> services = new java.util.HashSet<>();
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Service> entry : cluster.getServices().entrySet()) {
                    org.apache.ambari.server.state.Service service = entry.getValue();
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> components = service.getServiceComponents();
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponent> component : components.entrySet()) {
                        if (component.getValue().isMasterComponent()) {
                            java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = component.getValue().getServiceComponentHosts();
                            if (hosts.containsKey(hostName)) {
                                services.add(service.getName());
                            }
                        }
                    }
                }
                if (services.size() > 0) {
                    definitions.addAll(m_definitionDao.findByServiceMaster(clusterId, services));
                }
            }
            definitions.addAll(m_definitionDao.findAgentScoped(clusterId));
        } catch (org.apache.ambari.server.ClusterNotFoundException clusterNotFound) {
            org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.warn("Unable to get alert definitions for the missing cluster {}", clusterName);
            return java.util.Collections.emptySet();
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            org.apache.ambari.server.state.alert.AlertDefinitionHash.LOG.error("Unable to get alert definitions", ambariException);
            return java.util.Collections.emptySet();
        }
        return definitions;
    }

    private java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> coerce(java.util.Collection<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> entities) {
        return entities.stream().map(m_factory::coerce).collect(java.util.stream.Collectors.toList());
    }

    private static java.util.Map<java.lang.Long, org.apache.ambari.server.state.alert.AlertDefinition> mapById(java.util.Collection<org.apache.ambari.server.state.alert.AlertDefinition> definitions) {
        return definitions.stream().collect(java.util.stream.Collectors.toMap(org.apache.ambari.server.state.alert.AlertDefinition::getDefinitionId, java.util.function.Function.identity()));
    }
}