package org.apache.ambari.server.update;
import com.google.inject.persist.PersistService;
import org.apache.commons.lang.StringUtils;
public class HostUpdateHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.update.HostUpdateHelper.class);

    private static final java.lang.String AUTHENTICATED_USER_NAME = "ambari-host-update";

    public static final java.lang.String TMP_PREFIX = "tmpvalue";

    private com.google.inject.persist.PersistService persistService;

    private org.apache.ambari.server.configuration.Configuration configuration;

    private com.google.inject.Injector injector;

    protected java.lang.String hostChangesFile;

    protected java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostChangesFileMap;

    @com.google.inject.Inject
    public HostUpdateHelper(com.google.inject.persist.PersistService persistService, org.apache.ambari.server.configuration.Configuration configuration, com.google.inject.Injector injector) {
        this.persistService = persistService;
        this.configuration = configuration;
        this.injector = injector;
    }

    public void startPersistenceService() {
        persistService.start();
    }

    public void stopPersistenceService() {
        persistService.stop();
    }

    public java.lang.String getHostChangesFile() {
        return hostChangesFile;
    }

    public void setHostChangesFile(java.lang.String hostChangesFile) {
        this.hostChangesFile = hostChangesFile;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getHostChangesFileMap() {
        return hostChangesFileMap;
    }

    public void setHostChangesFileMap(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostChangesFileMap) {
        this.hostChangesFileMap = hostChangesFileMap;
    }

    public static class CheckHelperAuditModule extends org.apache.ambari.server.audit.AuditLoggerModule {
        public CheckHelperAuditModule() throws java.lang.Exception {
        }

        @java.lang.Override
        protected void configure() {
            super.configure();
        }
    }

    public static class UpdateHelperModule extends org.apache.ambari.server.controller.ControllerModule {
        public UpdateHelperModule() throws java.lang.Exception {
        }

        @java.lang.Override
        protected void configure() {
            super.configure();
            org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(binder());
        }
    }

    void validateHostChanges() throws org.apache.ambari.server.AmbariException {
        if ((hostChangesFileMap == null) || hostChangesFileMap.isEmpty()) {
            throw new org.apache.ambari.server.AmbariException(java.lang.String.format("File with host names changes is null or empty"));
        }
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHosts : hostChangesFileMap.entrySet()) {
                java.lang.String clusterName = clusterHosts.getKey();
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
                if (cluster != null) {
                    java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
                    java.util.List<java.lang.String> invalidHostNames = new java.util.ArrayList<>();
                    java.util.List<java.lang.String> hostNames = new java.util.ArrayList<>();
                    for (org.apache.ambari.server.state.Host host : hosts) {
                        hostNames.add(host.getHostName());
                    }
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> hostPair : clusterHosts.getValue().entrySet()) {
                        if (!hostNames.contains(hostPair.getKey())) {
                            invalidHostNames.add(hostPair.getKey());
                        }
                    }
                    if (!invalidHostNames.isEmpty()) {
                        throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Hostname(s): %s was(were) not found.", org.apache.commons.lang.StringUtils.join(invalidHostNames, ", ")));
                    }
                } else {
                    throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Cluster %s was not found.", clusterName));
                }
            }
        }
    }

    void updateHostsInConfigurations() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHosts : hostChangesFileMap.entrySet()) {
                boolean hostConfigsUpdated = false;
                java.lang.String clusterName = clusterHosts.getKey();
                org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName(clusterName);
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
                java.util.Map<java.lang.String, java.lang.String> hostMapping = clusterHosts.getValue();
                java.util.List<java.lang.String> currentHostNames = new java.util.ArrayList<>();
                java.lang.String updatedPropertyValue;
                for (java.util.Map.Entry<java.lang.String, java.lang.String> hostPair : hostMapping.entrySet()) {
                    currentHostNames.add(hostPair.getKey());
                }
                if (clusterEntity != null) {
                    java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities = clusterEntity.getClusterConfigEntities();
                    boolean configUpdated;
                    org.apache.ambari.server.state.ConfigFactory configFactory = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
                    for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : clusterConfigEntities) {
                        org.apache.ambari.server.state.Config config = configFactory.createExisting(cluster, clusterConfigEntity);
                        configUpdated = false;
                        for (java.util.Map.Entry<java.lang.String, java.lang.String> property : config.getProperties().entrySet()) {
                            updatedPropertyValue = replaceHosts(property.getValue(), currentHostNames, hostMapping);
                            if (updatedPropertyValue != null) {
                                java.util.Map<java.lang.String, java.lang.String> propertiesWithUpdates = config.getProperties();
                                propertiesWithUpdates.put(property.getKey(), updatedPropertyValue);
                                config.setProperties(propertiesWithUpdates);
                                configUpdated = true;
                            }
                        }
                        if (configUpdated) {
                            hostConfigsUpdated = true;
                            config.save();
                        }
                    }
                }
                if (hostConfigsUpdated) {
                    org.apache.ambari.server.agent.stomp.AgentConfigsHolder agentConfigsHolder = injector.getInstance(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class);
                    agentConfigsHolder.updateData(cluster.getClusterId(), currentHostNames.stream().map(hm -> cluster.getHost(hm).getHostId()).collect(java.util.stream.Collectors.toList()));
                }
            }
        }
    }

    private java.lang.String replaceHosts(java.lang.String propertyValue, java.util.List<java.lang.String> currentHostNames, java.util.Map<java.lang.String, java.lang.String> hostMapping) {
        java.util.List<java.lang.String> hostListForReplace;
        java.lang.String updatedPropertyValue = null;
        hostListForReplace = getHostNamesWhichValueIncludes(currentHostNames, propertyValue);
        if ((!hostListForReplace.isEmpty()) && (hostMapping != null)) {
            java.util.Collections.sort(hostListForReplace, new org.apache.ambari.server.update.HostUpdateHelper.StringComparator());
            updatedPropertyValue = propertyValue;
            java.util.Map<java.lang.String, java.lang.String> hostNameCode = new java.util.HashMap<>();
            int counter = 0;
            for (java.lang.String hostName : hostListForReplace) {
                java.lang.String code = java.lang.String.format("{replace_code_%d}", counter++);
                hostNameCode.put(hostName, code);
                updatedPropertyValue = updatedPropertyValue.replaceAll("(?i)" + java.util.regex.Pattern.quote(hostName), code);
            }
            for (java.lang.String hostName : hostListForReplace) {
                updatedPropertyValue = updatedPropertyValue.replace(hostNameCode.get(hostName), hostMapping.get(hostName));
            }
        }
        return updatedPropertyValue;
    }

    private java.util.List<java.lang.String> getHostNamesWhichValueIncludes(java.util.List<java.lang.String> hostNames, java.lang.String value) {
        java.util.List<java.lang.String> includedHostNames = new java.util.ArrayList<>();
        if (((value != null) && (hostNames != null)) && (!value.isEmpty())) {
            for (java.lang.String host : hostNames) {
                if (org.apache.commons.lang.StringUtils.containsIgnoreCase(value, host)) {
                    includedHostNames.add(host);
                }
            }
        }
        return includedHostNames;
    }

    public class StringComparator implements java.util.Comparator<java.lang.String> {
        @java.lang.Override
        public int compare(java.lang.String s1, java.lang.String s2) {
            return s2.length() - s1.length();
        }
    }

    void checkForSecurity() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        java.util.List<java.lang.String> clustersInSecure = new java.util.ArrayList<>();
        if (clusters != null) {
            for (java.lang.String clusterName : hostChangesFileMap.keySet()) {
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
                org.apache.ambari.server.state.Config clusterEnv = cluster.getDesiredConfigByType(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV);
                if (clusterEnv != null) {
                    java.lang.String securityEnabled = clusterEnv.getProperties().get(org.apache.ambari.server.controller.KerberosHelper.SECURITY_ENABLED_PROPERTY_NAME);
                    if (securityEnabled.toLowerCase().equals("true")) {
                        clustersInSecure.add(clusterName);
                    }
                }
            }
            if (!clustersInSecure.isEmpty()) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Cluster(s) %s from file, is(are) in secure mode. Please, turn off security mode.", org.apache.commons.lang.StringUtils.join(clustersInSecure, ", ")));
            }
        }
    }

    protected void initHostChangesFileMap() throws org.apache.ambari.server.AmbariException {
        com.google.gson.JsonObject hostChangesJsonObject = configuration.getHostChangesJson(hostChangesFile);
        hostChangesFileMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, com.google.gson.JsonElement> clusterEntry : hostChangesJsonObject.entrySet()) {
            try {
                com.google.gson.Gson gson = new com.google.gson.Gson();
                hostChangesFileMap.put(clusterEntry.getKey(), gson.<java.util.Map<java.lang.String, java.lang.String>>fromJson(clusterEntry.getValue(), java.util.Map.class));
            } catch (java.lang.Exception e) {
                throw new org.apache.ambari.server.AmbariException("Error occurred during mapping Json to Map structure. Please check json structure in file.", e);
            }
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> newHostChangesFileMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHosts : hostChangesFileMap.entrySet()) {
            java.util.Map<java.lang.String, java.lang.String> newHostPairs = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> hostPair : clusterHosts.getValue().entrySet()) {
                newHostPairs.put(hostPair.getKey().toLowerCase(), hostPair.getValue().toLowerCase());
            }
            newHostChangesFileMap.put(clusterHosts.getKey(), newHostPairs);
        }
        hostChangesFileMap = newHostChangesFileMap;
    }

    void updateHostsInDB() {
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.orm.dao.HostDAO hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHosts : hostChangesFileMap.entrySet()) {
            java.lang.String clusterName = clusterHosts.getKey();
            java.util.Map<java.lang.String, java.lang.String> hostMapping = clusterHosts.getValue();
            java.util.Map<java.lang.String, java.lang.String> toTmpHostMapping = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> fromTmpHostMapping = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> hostPair : hostMapping.entrySet()) {
                toTmpHostMapping.put(hostPair.getKey(), org.apache.ambari.server.update.HostUpdateHelper.TMP_PREFIX + hostPair.getValue());
                fromTmpHostMapping.put(org.apache.ambari.server.update.HostUpdateHelper.TMP_PREFIX + hostPair.getValue(), hostPair.getValue());
            }
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName(clusterName);
            renameHostsInDB(hostDAO, toTmpHostMapping, clusterEntity);
            renameHostsInDB(hostDAO, fromTmpHostMapping, clusterEntity);
        }
    }

    private void renameHostsInDB(org.apache.ambari.server.orm.dao.HostDAO hostDAO, java.util.Map<java.lang.String, java.lang.String> hostMapping, org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        java.util.List<java.lang.String> currentHostNames = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> hostPair : hostMapping.entrySet()) {
            currentHostNames.add(hostPair.getKey());
        }
        if (clusterEntity != null) {
            java.util.Collection<org.apache.ambari.server.orm.entities.HostEntity> hostEntities = clusterEntity.getHostEntities();
            for (org.apache.ambari.server.orm.entities.HostEntity hostEntity : hostEntities) {
                if (currentHostNames.contains(hostEntity.getHostName())) {
                    hostEntity.setHostName(hostMapping.get(hostEntity.getHostName()));
                    hostDAO.merge(hostEntity);
                }
            }
        }
    }

    void updateHostsForAlertsInDB() {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.dao.AlertsDAO alertsDAO = injector.getInstance(org.apache.ambari.server.orm.dao.AlertsDAO.class);
        org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if (clusterMap != null) {
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHosts : hostChangesFileMap.entrySet()) {
                    java.util.List<java.lang.String> currentHostNames = new java.util.ArrayList<>();
                    java.util.Map<java.lang.String, java.lang.String> hostMapping = clusterHosts.getValue();
                    java.lang.Long clusterId = clusterMap.get(clusterHosts.getKey()).getClusterId();
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> hostPair : hostMapping.entrySet()) {
                        currentHostNames.add(hostPair.getKey());
                    }
                    java.util.List<org.apache.ambari.server.orm.entities.AlertCurrentEntity> currentEntities = alertsDAO.findCurrentByCluster(clusterId);
                    for (org.apache.ambari.server.orm.entities.AlertCurrentEntity alertCurrentEntity : currentEntities) {
                        org.apache.ambari.server.orm.entities.AlertHistoryEntity alertHistoryEntity = alertCurrentEntity.getAlertHistory();
                        if (currentHostNames.contains(alertHistoryEntity.getHostName())) {
                            alertHistoryEntity.setHostName(hostMapping.get(alertHistoryEntity.getHostName()));
                            alertsDAO.merge(alertHistoryEntity);
                        }
                    }
                    java.util.List<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinitionEntities = alertDefinitionDAO.findAll(clusterId);
                    for (org.apache.ambari.server.orm.entities.AlertDefinitionEntity alertDefinitionEntity : alertDefinitionEntities) {
                        alertDefinitionEntity.setHash(java.util.UUID.randomUUID().toString());
                        alertDefinitionDAO.merge(alertDefinitionEntity);
                    }
                }
            }
        }
    }

    void updateHostsForTopologyRequests() {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.dao.TopologyRequestDAO topologyRequestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.TopologyRequestDAO.class);
        org.apache.ambari.server.orm.dao.TopologyHostRequestDAO topologyHostRequestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.TopologyHostRequestDAO.class);
        org.apache.ambari.server.orm.dao.TopologyHostGroupDAO topologyHostGroupDAO = injector.getInstance(org.apache.ambari.server.orm.dao.TopologyHostGroupDAO.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if (clusterMap != null) {
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHosts : hostChangesFileMap.entrySet()) {
                    java.lang.Long clusterId = clusterMap.get(clusterHosts.getKey()).getClusterId();
                    java.util.List<org.apache.ambari.server.orm.entities.TopologyRequestEntity> topologyRequestEntities = topologyRequestDAO.findByClusterId(clusterId);
                    java.util.List<java.lang.String> currentHostNames = new java.util.ArrayList<>();
                    java.util.Map<java.lang.String, java.lang.String> hostMapping = clusterHosts.getValue();
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> hostPair : hostMapping.entrySet()) {
                        currentHostNames.add(hostPair.getKey());
                    }
                    for (org.apache.ambari.server.orm.entities.TopologyRequestEntity topologyRequestEntity : topologyRequestEntities) {
                        org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity topologyLogicalRequestEntity = topologyRequestEntity.getTopologyLogicalRequestEntity();
                        java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostGroupEntity> topologyHostGroupEntities = topologyRequestEntity.getTopologyHostGroupEntities();
                        if (topologyHostGroupEntities != null) {
                            for (org.apache.ambari.server.orm.entities.TopologyHostGroupEntity topologyHostGroupEntity : topologyHostGroupEntities) {
                                java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostInfoEntity> topologyHostInfoEntities = topologyHostGroupEntity.getTopologyHostInfoEntities();
                                boolean updatesAvailable = false;
                                for (org.apache.ambari.server.orm.entities.TopologyHostInfoEntity topologyHostInfoEntity : topologyHostInfoEntities) {
                                    if (currentHostNames.contains(topologyHostInfoEntity.getFqdn())) {
                                        topologyHostInfoEntity.setFqdn(hostMapping.get(topologyHostInfoEntity.getFqdn()));
                                        updatesAvailable = true;
                                    }
                                }
                                if (updatesAvailable) {
                                    topologyHostGroupDAO.merge(topologyHostGroupEntity);
                                }
                            }
                        }
                        if (topologyLogicalRequestEntity != null) {
                            java.util.Collection<org.apache.ambari.server.orm.entities.TopologyHostRequestEntity> topologyHostRequestEntities = topologyLogicalRequestEntity.getTopologyHostRequestEntities();
                            if (topologyHostRequestEntities != null) {
                                for (org.apache.ambari.server.orm.entities.TopologyHostRequestEntity topologyHostRequestEntity : topologyHostRequestEntities) {
                                    if (currentHostNames.contains(topologyHostRequestEntity.getHostName())) {
                                        topologyHostRequestEntity.setHostName(hostMapping.get(topologyHostRequestEntity.getHostName()));
                                        topologyHostRequestDAO.merge(topologyHostRequestEntity);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.update.HostUpdateHelper.UpdateHelperModule(), new org.apache.ambari.server.update.HostUpdateHelper.CheckHelperAuditModule(), new org.apache.ambari.server.ldap.LdapModule());
        org.apache.ambari.server.update.HostUpdateHelper hostUpdateHelper = injector.getInstance(org.apache.ambari.server.update.HostUpdateHelper.class);
        try {
            org.apache.ambari.server.update.HostUpdateHelper.LOG.info("Host names update started.");
            java.lang.String hostChangesFile = args[0];
            if ((hostChangesFile == null) || hostChangesFile.isEmpty()) {
                throw new org.apache.ambari.server.AmbariException("Path to file with host names changes is empty or null.");
            }
            hostUpdateHelper.setHostChangesFile(hostChangesFile);
            hostUpdateHelper.initHostChangesFileMap();
            hostUpdateHelper.startPersistenceService();
            hostUpdateHelper.validateHostChanges();
            hostUpdateHelper.checkForSecurity();
            hostUpdateHelper.updateHostsInDB();
            hostUpdateHelper.updateHostsForTopologyRequests();
            hostUpdateHelper.updateHostsForAlertsInDB();
            hostUpdateHelper.updateHostsInConfigurations();
            org.apache.ambari.server.update.HostUpdateHelper.LOG.info("Host names update completed successfully.");
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.update.HostUpdateHelper.LOG.error("Exception occurred during host names update, failed", e);
            throw e;
        } finally {
            hostUpdateHelper.stopPersistenceService();
        }
    }
}