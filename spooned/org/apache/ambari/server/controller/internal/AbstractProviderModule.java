package org.apache.ambari.server.controller.internal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA;
import static org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS;
public abstract class AbstractProviderModule implements org.apache.ambari.server.controller.spi.ProviderModule , org.apache.ambari.server.controller.internal.ResourceProviderObserver , org.apache.ambari.server.controller.jmx.JMXHostProvider , org.apache.ambari.server.controller.metrics.MetricHostProvider , org.apache.ambari.server.controller.metrics.MetricsServiceProvider {
    private static final int PROPERTY_REQUEST_CONNECT_TIMEOUT = 5000;

    private static final int PROPERTY_REQUEST_READ_TIMEOUT = 10000;

    private static final java.lang.String GANGLIA_SERVER = "GANGLIA_SERVER";

    private static final java.lang.String METRIC_SERVER = "METRICS_COLLECTOR";

    private static final java.lang.String PROPERTIES_CATEGORY = "properties";

    private static final java.util.Map<java.lang.String, java.lang.String> serviceConfigVersions = new java.util.concurrent.ConcurrentHashMap<>();

    private static final java.util.Map<org.apache.ambari.server.state.Service.Type, java.lang.String> serviceConfigTypes = new java.util.EnumMap<>(org.apache.ambari.server.state.Service.Type.class);

    private static final java.util.Map<org.apache.ambari.server.state.Service.Type, java.util.Map<java.lang.String, java.lang.String[]>> serviceDesiredProperties = new java.util.EnumMap<>(org.apache.ambari.server.state.Service.Type.class);

    private static final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service.Type> componentServiceMap = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.List<org.apache.ambari.server.controller.internal.HttpPropertyProvider.HttpPropertyRequest>> HTTP_PROPERTY_REQUESTS = new java.util.HashMap<>();

    private static final java.lang.String PROPERTY_HDFS_HTTP_POLICY_VALUE_HTTPS_ONLY = "HTTPS_ONLY";

    private static final java.lang.String COLLECTOR_DEFAULT_PORT = "6188";

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String[]>> jmxDesiredProperties = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String[]>> jmxDesiredRpcSuffixProperties = new java.util.concurrent.ConcurrentHashMap<>();

    private volatile java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> jmxDesiredRpcSuffixes = new java.util.HashMap<>();

    private volatile java.util.Map<java.lang.String, java.lang.String> clusterHdfsSiteConfigVersionMap = new java.util.HashMap<>();

    private volatile java.util.Map<java.lang.String, java.lang.String> clusterJmxProtocolMap = new java.util.concurrent.ConcurrentHashMap<>();

    private volatile java.lang.String clusterMetricServerPort = null;

    private volatile java.lang.String clusterMetricServerVipPort = null;

    private volatile java.lang.String clusterMetricserverVipHost = null;

    static {
        serviceConfigTypes.put(org.apache.ambari.server.state.Service.Type.HDFS, "hdfs-site");
        serviceConfigTypes.put(org.apache.ambari.server.state.Service.Type.HBASE, "hbase-site");
        serviceConfigTypes.put(org.apache.ambari.server.state.Service.Type.YARN, "yarn-site");
        serviceConfigTypes.put(org.apache.ambari.server.state.Service.Type.MAPREDUCE2, "mapred-site");
        serviceConfigTypes.put(org.apache.ambari.server.state.Service.Type.AMBARI_METRICS, "ams-site");
        componentServiceMap.put("NAMENODE", org.apache.ambari.server.state.Service.Type.HDFS);
        componentServiceMap.put("DATANODE", org.apache.ambari.server.state.Service.Type.HDFS);
        componentServiceMap.put("JOURNALNODE", org.apache.ambari.server.state.Service.Type.HDFS);
        componentServiceMap.put("HBASE_MASTER", org.apache.ambari.server.state.Service.Type.HBASE);
        componentServiceMap.put("HBASE_REGIONSERVER", org.apache.ambari.server.state.Service.Type.HBASE);
        componentServiceMap.put("RESOURCEMANAGER", org.apache.ambari.server.state.Service.Type.YARN);
        componentServiceMap.put("NODEMANAGER", org.apache.ambari.server.state.Service.Type.YARN);
        componentServiceMap.put("HISTORYSERVER", org.apache.ambari.server.state.Service.Type.MAPREDUCE2);
        java.util.Map<java.lang.String, java.lang.String[]> initPropMap = new java.util.HashMap<>();
        initPropMap.put("NAMENODE", new java.lang.String[]{ "dfs.http.address", "dfs.namenode.http-address" });
        initPropMap.put("NAMENODE-HTTPS", new java.lang.String[]{ "dfs.namenode.https-address", "dfs.https.port" });
        initPropMap.put("NAMENODE-HA", new java.lang.String[]{ "dfs.namenode.http-address.%s.%s" });
        initPropMap.put("NAMENODE-HTTPS-HA", new java.lang.String[]{ "dfs.namenode.https-address.%s.%s" });
        initPropMap.put("DATANODE", new java.lang.String[]{ "dfs.datanode.http.address" });
        initPropMap.put("DATANODE-HTTPS", new java.lang.String[]{ "dfs.datanode.https.address" });
        initPropMap.put("JOURNALNODE-HTTPS", new java.lang.String[]{ "dfs.journalnode.https-address" });
        initPropMap.put("JOURNALNODE", new java.lang.String[]{ "dfs.journalnode.http-address" });
        serviceDesiredProperties.put(org.apache.ambari.server.state.Service.Type.HDFS, initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("HBASE_MASTER", new java.lang.String[]{ "hbase.master.info.port" });
        initPropMap.put("HBASE_REGIONSERVER", new java.lang.String[]{ "hbase.regionserver.info.port" });
        initPropMap.put("HBASE_MASTER-HTTPS", new java.lang.String[]{ "hbase.master.info.port" });
        initPropMap.put("HBASE_REGIONSERVER-HTTPS", new java.lang.String[]{ "hbase.regionserver.info.port" });
        serviceDesiredProperties.put(org.apache.ambari.server.state.Service.Type.HBASE, initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("RESOURCEMANAGER", new java.lang.String[]{ "yarn.resourcemanager.webapp.address" });
        initPropMap.put("RESOURCEMANAGER-HTTPS", new java.lang.String[]{ "yarn.resourcemanager.webapp.https.address" });
        initPropMap.put("NODEMANAGER", new java.lang.String[]{ "yarn.nodemanager.webapp.address" });
        initPropMap.put("NODEMANAGER-HTTPS", new java.lang.String[]{ "yarn.nodemanager.webapp.https.address" });
        serviceDesiredProperties.put(org.apache.ambari.server.state.Service.Type.YARN, initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("HISTORYSERVER", new java.lang.String[]{ "mapreduce.jobhistory.webapp.address" });
        initPropMap.put("HISTORYSERVER-HTTPS", new java.lang.String[]{ "mapreduce.jobhistory.webapp.https.address" });
        serviceDesiredProperties.put(org.apache.ambari.server.state.Service.Type.MAPREDUCE2, initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("NAMENODE", new java.lang.String[]{ "dfs.http.policy" });
        jmxDesiredProperties.put("NAMENODE", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("DATANODE", new java.lang.String[]{ "dfs.http.policy" });
        jmxDesiredProperties.put("DATANODE", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("JOURNALNODE", new java.lang.String[]{ "dfs.http.policy" });
        jmxDesiredProperties.put("JOURNALNODE", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("RESOURCEMANAGER", new java.lang.String[]{ "yarn.http.policy" });
        jmxDesiredProperties.put("RESOURCEMANAGER", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("HBASE_MASTER", new java.lang.String[]{ "hbase.ssl.enabled" });
        jmxDesiredProperties.put("HBASE_MASTER", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("HBASE_REGIONSERVER", new java.lang.String[]{ "hbase.ssl.enabled" });
        jmxDesiredProperties.put("HBASE_REGIONSERVER", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("NODEMANAGER", new java.lang.String[]{ "yarn.http.policy" });
        jmxDesiredProperties.put("NODEMANAGER", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("HISTORYSERVER", new java.lang.String[]{ "mapreduce.jobhistory.http.policy" });
        jmxDesiredProperties.put("HISTORYSERVER", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("client", new java.lang.String[]{ "dfs.namenode.rpc-address" });
        initPropMap.put("datanode", new java.lang.String[]{ "dfs.namenode.servicerpc-address" });
        initPropMap.put("healthcheck", new java.lang.String[]{ "dfs.namenode.lifeline.rpc-address" });
        jmxDesiredRpcSuffixProperties.put("NAMENODE", initPropMap);
        initPropMap = new java.util.HashMap<>();
        initPropMap.put("client", new java.lang.String[]{ "dfs.namenode.rpc-address.%s.%s" });
        initPropMap.put("datanode", new java.lang.String[]{ "dfs.namenode.servicerpc-address.%s.%s" });
        initPropMap.put("healthcheck", new java.lang.String[]{ "dfs.namenode.lifeline.rpc-address.%s.%s" });
        jmxDesiredRpcSuffixProperties.put("NAMENODE-HA", initPropMap);
        HTTP_PROPERTY_REQUESTS.put("RESOURCEMANAGER", java.util.Collections.singletonList(new org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest()));
        HTTP_PROPERTY_REQUESTS.put("ATLAS_SERVER", java.util.Collections.singletonList(new org.apache.ambari.server.controller.internal.AtlasServerHttpPropertyRequest()));
    }

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, org.apache.ambari.server.controller.spi.ResourceProvider> resourceProviders = new java.util.HashMap<>();

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider>> propertyProviders = new java.util.HashMap<>();

    @com.google.inject.Inject
    protected org.apache.ambari.server.controller.AmbariManagementController managementController;

    @com.google.inject.Inject
    org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricCacheProvider metricCacheProvider;

    @com.google.inject.Inject
    org.apache.ambari.server.controller.metrics.MetricsCollectorHAManager metricsCollectorHAManager;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.metrics.MetricPropertyProviderFactory metricPropertyProviderFactory;

    @com.google.inject.Inject
    protected org.apache.ambari.server.events.publishers.AmbariEventPublisher eventPublisher;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterHostComponentMap;

    private java.util.Map<java.lang.String, java.lang.String> clusterGangliaCollectorMap;

    private final java.util.Map<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.String>>> jmxPortMap = new java.util.concurrent.ConcurrentHashMap<>(1);

    private volatile boolean initialized = false;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AbstractProviderModule.class);

    public AbstractProviderModule() {
        if (managementController == null) {
            managementController = org.apache.ambari.server.controller.AmbariServer.getController();
        }
        if ((metricCacheProvider == null) && (managementController != null)) {
            metricCacheProvider = managementController.getTimelineMetricCacheProvider();
        }
        if ((metricPropertyProviderFactory == null) && (managementController != null)) {
            metricPropertyProviderFactory = managementController.getMetricPropertyProviderFactory();
        }
        if ((null == eventPublisher) && (null != managementController)) {
            eventPublisher = managementController.getAmbariEventPublisher();
            eventPublisher.register(this);
        }
        if ((null == metricsCollectorHAManager) && (null != managementController)) {
            metricsCollectorHAManager = managementController.getMetricsCollectorHAManager();
        }
        if ((null == clusters) && (null != managementController)) {
            clusters = managementController.getClusters();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        if (!resourceProviders.containsKey(type)) {
            registerResourceProvider(type);
        }
        return resourceProviders.get(type);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> getPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type type) {
        if (!propertyProviders.containsKey(type)) {
            createPropertyProviders(type);
        }
        return propertyProviders.get(type);
    }

    @java.lang.Override
    public void update(org.apache.ambari.server.controller.internal.ResourceProviderEvent event) {
        org.apache.ambari.server.controller.spi.Resource.Type type = event.getResourceType();
        if (((type == org.apache.ambari.server.controller.spi.Resource.Type.Cluster) || (type == org.apache.ambari.server.controller.spi.Resource.Type.Host)) || (type == org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)) {
            resetInit();
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService getMetricsServiceType() {
        try {
            checkInit();
        } catch (org.apache.ambari.server.controller.spi.SystemException e) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.error("Exception during checkInit.", e);
        }
        if (!metricsCollectorHAManager.isEmpty()) {
            return org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS;
        } else if (!clusterGangliaCollectorMap.isEmpty()) {
            return org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA;
        }
        return null;
    }

    @java.lang.Override
    public java.lang.String getCollectorHostName(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
        checkInit();
        if (service.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA)) {
            return clusterGangliaCollectorMap.get(clusterName);
        } else if (service.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
            return getMetricsCollectorHostName(clusterName);
        }
        return null;
    }

    private void checkAndAddExternalCollectorHosts(java.lang.String clusterName) throws org.apache.ambari.server.controller.spi.SystemException {
        try {
            java.lang.String configType = "cluster-env";
            java.lang.String currentConfigVersion = getDesiredConfigVersion(clusterName, configType);
            java.lang.String oldConfigVersion = org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigVersions.get(configType);
            if (!currentConfigVersion.equals(oldConfigVersion)) {
                org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigVersions.put(configType, currentConfigVersion);
                java.util.Map<java.lang.String, java.lang.String> configProperties = getDesiredConfigMap(clusterName, currentConfigVersion, configType, java.util.Collections.singletonMap("METRICS_COLLECTOR", new java.lang.String[]{ "metrics_collector_external_hosts" }));
                if (!configProperties.isEmpty()) {
                    clusterMetricserverVipHost = configProperties.get("METRICS_COLLECTOR");
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(clusterMetricserverVipHost)) {
                        for (java.lang.String collectorHost : org.apache.commons.lang.StringUtils.split(clusterMetricserverVipHost, ",")) {
                            metricsCollectorHAManager.addExternalMetricsCollectorHost(clusterName, collectorHost);
                        }
                        org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.info("Setting Metrics Collector External Host : " + clusterMetricserverVipHost);
                    }
                }
                configProperties = getDesiredConfigMap(clusterName, currentConfigVersion, configType, java.util.Collections.singletonMap("METRICS_COLLECTOR", new java.lang.String[]{ "metrics_collector_external_port" }));
                if (!configProperties.isEmpty()) {
                    clusterMetricServerVipPort = configProperties.getOrDefault("METRICS_COLLECTOR", "6188");
                }
            }
        } catch (org.apache.ambari.server.controller.spi.NoSuchParentResourceException | org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.warn("Failed to retrieve collector hostname.", e);
        }
    }

    private java.lang.String getMetricsCollectorHostName(java.lang.String clusterName) throws org.apache.ambari.server.controller.spi.SystemException {
        checkAndAddExternalCollectorHosts(clusterName);
        java.lang.String currentCollectorHost = metricsCollectorHAManager.getCollectorHost(clusterName);
        org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.debug("Cluster Metrics Vip Host : " + clusterMetricserverVipHost);
        return currentCollectorHost;
    }

    @java.lang.Override
    public boolean isCollectorHostExternal(java.lang.String clusterName) {
        return metricsCollectorHAManager.isExternalCollector();
    }

    @java.lang.Override
    public java.lang.String getCollectorPort(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
        checkInit();
        if (service.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA)) {
            return "80";
        } else if (service.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
            try {
                if (clusterMetricServerVipPort == null) {
                    java.lang.String configType = org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigTypes.get(org.apache.ambari.server.state.Service.Type.AMBARI_METRICS);
                    java.lang.String currentConfigVersion = getDesiredConfigVersion(clusterName, configType);
                    java.lang.String oldConfigVersion = org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigVersions.get(configType);
                    if (!currentConfigVersion.equals(oldConfigVersion)) {
                        org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigVersions.put(configType, currentConfigVersion);
                        java.util.Map<java.lang.String, java.lang.String> configProperties = getDesiredConfigMap(clusterName, currentConfigVersion, configType, java.util.Collections.singletonMap("METRICS_COLLECTOR", new java.lang.String[]{ "timeline.metrics.service.webapp.address" }));
                        if (!configProperties.isEmpty()) {
                            clusterMetricServerPort = getPortString(configProperties.get("METRICS_COLLECTOR"));
                        } else {
                            clusterMetricServerPort = org.apache.ambari.server.controller.internal.AbstractProviderModule.COLLECTOR_DEFAULT_PORT;
                        }
                    }
                }
            } catch (org.apache.ambari.server.controller.spi.NoSuchParentResourceException | org.apache.ambari.server.controller.spi.UnsupportedPropertyException e) {
                org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.warn("Failed to retrieve collector port.", e);
            }
        }
        return clusterMetricServerVipPort != null ? clusterMetricServerVipPort : clusterMetricServerPort;
    }

    @java.lang.Override
    public boolean isCollectorHostLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
        return metricsCollectorHAManager.isCollectorHostLive(clusterName);
    }

    @java.lang.Override
    public java.lang.String getHostName(java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException {
        checkInit();
        return clusterHostComponentMap.get(clusterName).get(componentName);
    }

    @java.lang.Override
    public java.lang.String getPublicHostName(java.lang.String clusterName, java.lang.String hostName) {
        org.apache.ambari.server.state.Host host = getHost(clusterName, hostName);
        return host == null ? hostName : host.getPublicHostName();
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getHostNames(java.lang.String clusterName, java.lang.String componentName) {
        java.util.Set<java.lang.String> hosts = null;
        try {
            org.apache.ambari.server.state.Cluster cluster = managementController.getClusters().getCluster(clusterName);
            java.lang.String serviceName = managementController.findServiceName(cluster, componentName);
            hosts = cluster.getService(serviceName).getServiceComponent(componentName).getServiceComponentHosts().keySet();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.warn("Exception in getting host names for jmx metrics: ", e);
        }
        return hosts;
    }

    private org.apache.ambari.server.state.Host getHost(java.lang.String clusterName, java.lang.String hostName) {
        org.apache.ambari.server.state.Host host = null;
        try {
            org.apache.ambari.server.state.Cluster cluster = managementController.getClusters().getCluster(clusterName);
            if (cluster != null) {
                host = cluster.getHost(hostName);
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.warn("Exception in getting host info for jmx metrics: ", e);
        }
        return host;
    }

    @java.lang.Override
    public boolean isCollectorComponentLive(java.lang.String clusterName, org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService service) throws org.apache.ambari.server.controller.spi.SystemException {
        final java.lang.String collectorHostName = getCollectorHostName(clusterName, service);
        if (service.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.GANGLIA)) {
            return org.apache.ambari.server.controller.internal.HostStatusHelper.isHostComponentLive(managementController, clusterName, collectorHostName, "GANGLIA", org.apache.ambari.server.Role.GANGLIA_SERVER.name());
        } else if (service.equals(org.apache.ambari.server.controller.metrics.MetricsServiceProvider.MetricsService.TIMELINE_METRICS)) {
            return metricsCollectorHAManager.isCollectorComponentLive(clusterName);
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String getPort(java.lang.String clusterName, java.lang.String componentName, java.lang.String hostName, boolean httpsEnabled) {
        java.util.concurrent.ConcurrentMap<java.lang.String, java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.String>> clusterJmxPorts;
        if (!jmxPortMap.containsKey(clusterName)) {
            synchronized(jmxPortMap) {
                if (!jmxPortMap.containsKey(clusterName)) {
                    clusterJmxPorts = new java.util.concurrent.ConcurrentHashMap<>();
                    jmxPortMap.put(clusterName, clusterJmxPorts);
                }
            }
        }
        clusterJmxPorts = jmxPortMap.get(clusterName);
        org.apache.ambari.server.state.Service.Type service = org.apache.ambari.server.controller.internal.AbstractProviderModule.componentServiceMap.get(componentName);
        if (service != null) {
            try {
                java.lang.String configType = org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigTypes.get(service);
                java.lang.String currVersion = getDesiredConfigVersion(clusterName, configType);
                java.lang.String oldVersion = org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigVersions.get(configType);
                if ((!org.apache.commons.lang.StringUtils.equals(currVersion, oldVersion)) || (!(clusterJmxPorts.containsKey(hostName) && clusterJmxPorts.get(hostName).containsKey(componentName)))) {
                    org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigVersions.put(configType, currVersion);
                    java.util.Map<java.lang.String, java.lang.Object> configProperties = getConfigProperties(clusterName, currVersion, org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigTypes.get(service));
                    java.lang.String publicHostName = getPublicHostName(clusterName, hostName);
                    java.util.Map<java.lang.String, java.lang.String[]> componentPortsProperties = new java.util.HashMap<>();
                    componentPortsProperties.put(componentName, getPortProperties(service, componentName, hostName, publicHostName, configProperties, httpsEnabled));
                    java.util.Map<java.lang.String, java.lang.String> portMap = getDesiredConfigMap(clusterName, currVersion, org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigTypes.get(service), componentPortsProperties, configProperties);
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : portMap.entrySet()) {
                        java.lang.String portString = getPortString(entry.getValue());
                        if (null != portString) {
                            clusterJmxPorts.putIfAbsent(hostName, new java.util.concurrent.ConcurrentHashMap<>());
                            clusterJmxPorts.get(hostName).put(entry.getKey(), portString);
                        }
                    }
                    initRpcSuffixes(clusterName, componentName, configType, currVersion, hostName, publicHostName);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.error("Exception initializing jmx port maps. ", e);
            }
        }
        org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.debug("jmxPortMap -> {}", jmxPortMap);
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.String> hostJmxPorts = clusterJmxPorts.get(hostName);
        if (hostJmxPorts == null) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.debug("Jmx ports not loaded from properties: clusterName={}, componentName={}, hostName={}, " + "clusterJmxPorts={}, jmxPortMap.get(clusterName)={}", clusterName, componentName, hostName, clusterJmxPorts, jmxPortMap.get(clusterName));
            return null;
        }
        return hostJmxPorts.get(componentName);
    }

    java.lang.String[] getPortProperties(org.apache.ambari.server.state.Service.Type service, java.lang.String componentName, java.lang.String hostName, java.lang.String publicHostName, java.util.Map<java.lang.String, java.lang.Object> properties, boolean httpsEnabled) {
        componentName = (httpsEnabled) ? componentName + "-HTTPS" : componentName;
        if (componentName.startsWith("NAMENODE") && properties.containsKey("dfs.internal.nameservices")) {
            componentName += "-HA";
            return getNamenodeHaProperty(properties, org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceDesiredProperties.get(service).get(componentName), hostName, publicHostName);
        }
        return org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceDesiredProperties.get(service).get(componentName);
    }

    private java.lang.String[] getNamenodeHaProperty(java.util.Map<java.lang.String, java.lang.Object> properties, java.lang.String[] pattern, java.lang.String hostName, java.lang.String publicHostName) {
        for (java.lang.String nameserviceId : ((java.lang.String) (properties.get("dfs.internal.nameservices"))).split(",")) {
            if (properties.containsKey("dfs.ha.namenodes." + nameserviceId)) {
                for (java.lang.String namenodeId : ((java.lang.String) (properties.get("dfs.ha.namenodes." + nameserviceId))).split(",")) {
                    java.lang.String propertyName = java.lang.String.format(pattern[0], nameserviceId, namenodeId);
                    if (properties.containsKey(propertyName)) {
                        java.lang.String propertyValue = ((java.lang.String) (properties.get(propertyName)));
                        java.lang.String propHostName = propertyValue.split(":")[0];
                        if (propHostName.equals(hostName) || propHostName.equals(publicHostName)) {
                            return new java.lang.String[]{ propertyName };
                        }
                    }
                }
            }
        }
        return pattern;
    }

    private java.lang.String postProcessPropertyValue(java.lang.String key, java.lang.String value, java.util.Map<java.lang.String, java.lang.String> properties, java.util.Set<java.lang.String> prevProps) {
        if (((value != null) && (key != null)) && value.contains("${")) {
            if (prevProps == null) {
                prevProps = new java.util.HashSet<>();
            }
            if (prevProps.contains(key)) {
                return value;
            }
            prevProps.add(key);
            java.lang.String refValueString = value;
            java.util.Map<java.lang.String, java.lang.String> refMap = new java.util.HashMap<>();
            while (refValueString.contains("${")) {
                int startValueRef = refValueString.indexOf("${") + 2;
                int endValueRef = refValueString.indexOf('}');
                java.lang.String valueRef = refValueString.substring(startValueRef, endValueRef);
                refValueString = refValueString.substring(endValueRef + 1);
                java.lang.String trueValue = postProcessPropertyValue(valueRef, properties.get(valueRef), properties, prevProps);
                if (trueValue != null) {
                    refMap.put(("${" + valueRef) + '}', trueValue);
                }
            } 
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : refMap.entrySet()) {
                refValueString = entry.getValue();
                value = value.replace(entry.getKey(), refValueString);
            }
            properties.put(key, value);
        }
        return value;
    }

    protected abstract org.apache.ambari.server.controller.spi.ResourceProvider createResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type);

    protected void registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
        org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider = createResourceProvider(type);
        if (resourceProvider instanceof org.apache.ambari.server.controller.internal.ObservableResourceProvider) {
            ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (resourceProvider)).addObserver(this);
        }
        putResourceProvider(type, resourceProvider);
    }

    protected void putResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider) {
        resourceProviders.put(type, resourceProvider);
    }

    protected void putPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> providers) {
        propertyProviders.put(type, providers);
    }

    protected void createPropertyProviders(org.apache.ambari.server.controller.spi.Resource.Type type) {
        java.util.List<org.apache.ambari.server.controller.spi.PropertyProvider> providers = new java.util.LinkedList<>();
        org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration = org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance();
        org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider = new org.apache.ambari.server.controller.internal.URLStreamProvider(org.apache.ambari.server.controller.internal.AbstractProviderModule.PROPERTY_REQUEST_CONNECT_TIMEOUT, org.apache.ambari.server.controller.internal.AbstractProviderModule.PROPERTY_REQUEST_READ_TIMEOUT, configuration);
        if (type.isInternalType()) {
            switch (type.getInternalType()) {
                case Cluster :
                    providers.add(createMetricsReportPropertyProvider(type, streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance(), this, this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Clusters", "cluster_name")));
                    providers.add(new org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider(type, "Clusters/cluster_name", null));
                    break;
                case Service :
                    providers.add(new org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider(type, "ServiceInfo/cluster_name", "ServiceInfo/service_name"));
                    break;
                case Host :
                    providers.add(createMetricsHostPropertyProvider(type, streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance(), this, this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")));
                    providers.add(new org.apache.ambari.server.controller.internal.AlertSummaryPropertyProvider(type, "Hosts/cluster_name", "Hosts/host_name"));
                    break;
                case Component :
                    {
                        org.apache.ambari.server.controller.spi.PropertyProvider jpp = createJMXPropertyProvider(type, streamProvider, this, this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "cluster_name"), null, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "state"));
                        org.apache.ambari.server.controller.spi.PropertyProvider gpp = null;
                        gpp = createMetricsComponentPropertyProvider(type, streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance(), this, this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "component_name"));
                        providers.add(new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(type, this, this, this, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "cluster_name"), null, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ServiceComponentInfo", "state"), jpp, gpp));
                        break;
                    }
                case HostComponent :
                    {
                        org.apache.ambari.server.controller.spi.PropertyProvider jpp = createJMXPropertyProvider(type, streamProvider, this, this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"));
                        org.apache.ambari.server.controller.spi.PropertyProvider gpp = null;
                        gpp = createMetricsHostComponentPropertyProvider(type, streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration.instance(), this, this, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"));
                        providers.add(new org.apache.ambari.server.controller.internal.StackDefinedPropertyProvider(type, this, this, this, streamProvider, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "state"), jpp, gpp));
                        providers.add(new org.apache.ambari.server.controller.internal.HttpPropertyProvider(streamProvider, managementController.getClusters(), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "public_host_name"), org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"), org.apache.ambari.server.controller.internal.AbstractProviderModule.HTTP_PROPERTY_REQUESTS));
                        providers.add(managementController.getLoggingSearchPropertyProvider());
                        break;
                    }
                case RootServiceComponent :
                    providers.add(new org.apache.ambari.server.controller.internal.RootServiceComponentPropertyProvider());
                    break;
                default :
                    break;
            }
        }
        putPropertyProviders(type, providers);
    }

    private void checkInit() throws org.apache.ambari.server.controller.spi.SystemException {
        if (!initialized) {
            synchronized(this) {
                if (!initialized) {
                    initProviderMaps();
                    initialized = true;
                }
            }
        }
    }

    private void resetInit() {
        if (initialized) {
            synchronized(this) {
                if (initialized) {
                    initialized = false;
                }
            }
        }
    }

    private void initProviderMaps() throws org.apache.ambari.server.controller.spi.SystemException {
        jmxPortMap.clear();
        clusterHostComponentMap = new java.util.HashMap<>();
        clusterGangliaCollectorMap = new java.util.HashMap<>();
        boolean hasMetricCollector = false;
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if (org.apache.commons.collections.MapUtils.isEmpty(clusterMap)) {
            return;
        }
        for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            hasMetricCollector = false;
            java.lang.String clusterName = cluster.getClusterName();
            java.util.Map<java.lang.String, java.lang.String> hostComponentMap = clusterHostComponentMap.get(clusterName);
            if (hostComponentMap == null) {
                hostComponentMap = new java.util.HashMap<>();
                clusterHostComponentMap.put(clusterName, hostComponentMap);
            }
            java.util.List<org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = cluster.getServiceComponentHosts();
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(serviceComponentHosts)) {
                for (org.apache.ambari.server.state.ServiceComponentHost sch : serviceComponentHosts) {
                    java.lang.String componentName = sch.getServiceComponentName();
                    java.lang.String hostName = sch.getHostName();
                    hostComponentMap.put(componentName, hostName);
                    if (componentName.equals(org.apache.ambari.server.controller.internal.AbstractProviderModule.GANGLIA_SERVER)) {
                        clusterGangliaCollectorMap.put(clusterName, hostName);
                    }
                    if (componentName.equals(org.apache.ambari.server.controller.internal.AbstractProviderModule.METRIC_SERVER)) {
                        metricsCollectorHAManager.addCollectorHost(clusterName, hostName);
                        hasMetricCollector = true;
                    }
                }
            }
            if (!hasMetricCollector) {
                checkAndAddExternalCollectorHosts(clusterName);
            }
        }
    }

    private java.lang.String getPortString(java.lang.String value) {
        return (value != null) && value.contains(":") ? value.substring(value.lastIndexOf(":") + 1, value.length()) : value;
    }

    private java.lang.String getDesiredConfigVersion(java.lang.String clusterName, java.lang.String configType) {
        java.lang.String versionTag = org.apache.ambari.server.state.ConfigHelper.FIRST_VERSION_TAG;
        try {
            org.apache.ambari.server.state.Clusters clusters = managementController.getClusters();
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
            java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
            org.apache.ambari.server.state.DesiredConfig config = desiredConfigs.get(configType);
            if (config != null) {
                versionTag = config.getTag();
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.error("Unable to lookup the desired configuration tag for {} on cluster {}, defaulting to {}", configType, clusterName, versionTag, ambariException);
        }
        return versionTag;
    }

    private java.util.Map<java.lang.String, java.lang.Object> getConfigProperties(java.lang.String clusterName, java.lang.String versionTag, java.lang.String configType) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException {
        org.apache.ambari.server.controller.spi.ResourceProvider configResourceProvider = getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.apache.ambari.server.controller.spi.Predicate configPredicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME).equals(clusterName).and().property(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE).equals(configType).and().property(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG).equals(versionTag).toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> configResources;
        try {
            configResources = configResourceProvider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE, org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG), configPredicate);
        } catch (org.apache.ambari.server.controller.spi.NoSuchResourceException e) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.info("Resource for the desired config not found.", e);
            return java.util.Collections.emptyMap();
        }
        return configResources.stream().findFirst().map(res -> res.getPropertiesMap().get(org.apache.ambari.server.controller.internal.AbstractProviderModule.PROPERTIES_CATEGORY)).orElse(java.util.Collections.emptyMap());
    }

    private java.util.Map<java.lang.String, java.lang.String> getDesiredConfigMap(java.lang.String clusterName, java.lang.String versionTag, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String[]> keys) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException {
        return getDesiredConfigMap(clusterName, versionTag, configType, keys, null);
    }

    private java.util.Map<java.lang.String, java.lang.String> getDesiredConfigMap(java.lang.String clusterName, java.lang.String versionTag, java.lang.String configType, java.util.Map<java.lang.String, java.lang.String[]> keys, java.util.Map<java.lang.String, java.lang.Object> configProperties) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.SystemException {
        if (configProperties == null) {
            configProperties = getConfigProperties(clusterName, versionTag, configType);
        }
        java.util.Map<java.lang.String, java.lang.String> mConfigs = new java.util.HashMap<>();
        if (!configProperties.isEmpty()) {
            java.util.Map<java.lang.String, java.lang.String> evaluatedProperties = null;
            for (java.util.Map.Entry<java.lang.String, java.lang.String[]> entry : keys.entrySet()) {
                java.lang.String propName = null;
                java.lang.String value = null;
                for (java.lang.String pname : entry.getValue()) {
                    propName = pname;
                    for (java.util.Map.Entry<java.lang.String, java.lang.Object> propertyEntry : configProperties.entrySet()) {
                        if (propertyEntry.getKey().startsWith(pname)) {
                            value = ((java.lang.String) (propertyEntry.getValue()));
                            break;
                        }
                    }
                    if (null != value) {
                        break;
                    }
                }
                if ((value != null) && value.contains("${")) {
                    if (evaluatedProperties == null) {
                        evaluatedProperties = new java.util.HashMap<>();
                        for (java.util.Map.Entry<java.lang.String, java.lang.Object> subentry : configProperties.entrySet()) {
                            java.lang.String keyString = subentry.getKey();
                            java.lang.Object object = subentry.getValue();
                            java.lang.String valueString;
                            if ((object != null) && (object instanceof java.lang.String)) {
                                valueString = ((java.lang.String) (object));
                                evaluatedProperties.put(keyString, valueString);
                                postProcessPropertyValue(keyString, valueString, evaluatedProperties, null);
                            }
                        }
                    }
                }
                value = postProcessPropertyValue(propName, value, evaluatedProperties, null);
                org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.debug("PROPERTY -> key: {}, value: {}", propName, value);
                mConfigs.put(entry.getKey(), value);
            }
        }
        return mConfigs;
    }

    private org.apache.ambari.server.controller.spi.PropertyProvider createJMXPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, org.apache.ambari.server.controller.jmx.JMXHostProvider jmxHostProvider, org.apache.ambari.server.controller.metrics.MetricHostProvider metricsHostProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId, java.lang.String statePropertyId) {
        return metricPropertyProviderFactory.createJMXPropertyProvider(org.apache.ambari.server.controller.utilities.PropertyHelper.getJMXPropertyIds(type), streamProvider, jmxHostProvider, metricsHostProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId, statePropertyId);
    }

    private org.apache.ambari.server.controller.spi.PropertyProvider createMetricsReportPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, java.lang.String clusterNamePropertyId) {
        return org.apache.ambari.server.controller.metrics.MetricsReportPropertyProvider.createInstance(org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(type), streamProvider, configuration, metricCacheProvider, hostProvider, serviceProvider, clusterNamePropertyId);
    }

    private org.apache.ambari.server.controller.spi.PropertyProvider createMetricsHostPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId) {
        return org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.createInstance(type, org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(type), streamProvider, configuration, metricCacheProvider, hostProvider, serviceProvider, clusterNamePropertyId, hostNamePropertyId, null);
    }

    private org.apache.ambari.server.controller.spi.PropertyProvider createMetricsComponentPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, java.lang.String clusterNamePropertyId, java.lang.String componentNamePropertyId) {
        return org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.createInstance(type, org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(type), streamProvider, configuration, metricCacheProvider, hostProvider, serviceProvider, clusterNamePropertyId, null, componentNamePropertyId);
    }

    private org.apache.ambari.server.controller.spi.PropertyProvider createMetricsHostComponentPropertyProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider, org.apache.ambari.server.configuration.ComponentSSLConfiguration configuration, org.apache.ambari.server.controller.metrics.MetricHostProvider hostProvider, org.apache.ambari.server.controller.metrics.MetricsServiceProvider serviceProvider, java.lang.String clusterNamePropertyId, java.lang.String hostNamePropertyId, java.lang.String componentNamePropertyId) {
        return org.apache.ambari.server.controller.metrics.MetricsPropertyProvider.createInstance(type, org.apache.ambari.server.controller.utilities.PropertyHelper.getMetricPropertyIds(type), streamProvider, configuration, metricCacheProvider, hostProvider, serviceProvider, clusterNamePropertyId, hostNamePropertyId, componentNamePropertyId);
    }

    @java.lang.Override
    public java.lang.String getJMXProtocol(java.lang.String clusterName, java.lang.String componentName) {
        java.lang.String mapKey = java.lang.String.format("%s-%s", clusterName, componentName);
        java.lang.String jmxProtocolString = clusterJmxProtocolMap.get(mapKey);
        if (null != jmxProtocolString) {
            return jmxProtocolString;
        }
        try {
            if (((((((componentName.equals("NAMENODE") || componentName.equals("DATANODE")) || componentName.equals("RESOURCEMANAGER")) || componentName.equals("NODEMANAGER")) || componentName.equals("JOURNALNODE")) || componentName.equals("HISTORYSERVER")) || componentName.equals("HBASE_MASTER")) || componentName.equals("HBASE_REGIONSERVER")) {
                org.apache.ambari.server.state.Service.Type service = org.apache.ambari.server.controller.internal.AbstractProviderModule.componentServiceMap.get(componentName);
                java.lang.String config = org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigTypes.get(service);
                java.lang.String newSiteConfigVersion = getDesiredConfigVersion(clusterName, config);
                java.lang.String cachedSiteConfigVersion = clusterHdfsSiteConfigVersionMap.get(clusterName);
                if (!newSiteConfigVersion.equals(cachedSiteConfigVersion)) {
                    java.util.Map<java.lang.String, java.lang.String> protocolMap = getDesiredConfigMap(clusterName, newSiteConfigVersion, config, org.apache.ambari.server.controller.internal.AbstractProviderModule.jmxDesiredProperties.get(componentName));
                    boolean isHttpsEnabled;
                    java.lang.String propetyVal = protocolMap.get(componentName);
                    if (service.equals(org.apache.ambari.server.state.Service.Type.HBASE)) {
                        isHttpsEnabled = java.lang.Boolean.valueOf(propetyVal);
                    } else {
                        isHttpsEnabled = org.apache.ambari.server.controller.internal.AbstractProviderModule.PROPERTY_HDFS_HTTP_POLICY_VALUE_HTTPS_ONLY.equals(propetyVal);
                    }
                    jmxProtocolString = getJMXProtocolStringFromBool(isHttpsEnabled);
                }
            } else {
                jmxProtocolString = "http";
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.info((("Exception while detecting JMX protocol for clusterName = " + clusterName) + ", componentName = ") + componentName, e);
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.info(((("Defaulting JMX to HTTP protocol for  for clusterName = " + clusterName) + ", componentName = ") + componentName) + componentName);
            jmxProtocolString = "http";
        }
        if (jmxProtocolString == null) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.debug("Detected JMX protocol is null for clusterName = {}, componentName = {}", clusterName, componentName);
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.debug("Defaulting JMX to HTTP protocol for  for clusterName = {}, componentName = {}", clusterName, componentName);
            jmxProtocolString = "http";
        }
        if (org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.internal.AbstractProviderModule.LOG.debug("JMXProtocol = {}, for clusterName={}, componentName = {}", jmxProtocolString, clusterName, componentName);
        }
        clusterJmxProtocolMap.put(mapKey, jmxProtocolString);
        return jmxProtocolString;
    }

    private java.lang.String getJMXProtocolStringFromBool(boolean isHttpsEnabled) {
        return isHttpsEnabled ? "https" : "http";
    }

    @java.lang.Override
    public java.lang.String getJMXRpcMetricTag(java.lang.String clusterName, java.lang.String componentName, java.lang.String port) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> componentToPortsMap = jmxDesiredRpcSuffixes.get(clusterName);
        if ((componentToPortsMap != null) && componentToPortsMap.containsKey(componentName)) {
            java.util.Map<java.lang.String, java.lang.String> portToTagMap = componentToPortsMap.get(componentName);
            if (portToTagMap != null) {
                return portToTagMap.get(port);
            }
        }
        return null;
    }

    private void initRpcSuffixes(java.lang.String clusterName, java.lang.String componentName, java.lang.String config, java.lang.String configVersion, java.lang.String hostName, java.lang.String publicHostName) throws java.lang.Exception {
        if (org.apache.ambari.server.controller.internal.AbstractProviderModule.jmxDesiredRpcSuffixProperties.containsKey(componentName)) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> componentToPortsMap;
            if (jmxDesiredRpcSuffixes.containsKey(clusterName)) {
                componentToPortsMap = jmxDesiredRpcSuffixes.get(clusterName);
            } else {
                componentToPortsMap = new java.util.HashMap<>();
                componentToPortsMap.put(componentName, new java.util.HashMap<>());
                jmxDesiredRpcSuffixes.put(clusterName, componentToPortsMap);
            }
            java.util.Map<java.lang.String, java.lang.String> portToTagMap = componentToPortsMap.get(componentName);
            portToTagMap.clear();
            java.util.Map<java.lang.String, java.lang.String[]> keys = org.apache.ambari.server.controller.internal.AbstractProviderModule.jmxDesiredRpcSuffixProperties.get(componentName);
            if ("NAMENODE".equals(componentName)) {
                java.util.Map<java.lang.String, java.lang.Object> configProperties = getConfigProperties(clusterName, configVersion, org.apache.ambari.server.controller.internal.AbstractProviderModule.serviceConfigTypes.get(org.apache.ambari.server.controller.internal.AbstractProviderModule.componentServiceMap.get(componentName)));
                if (configProperties.containsKey("dfs.internal.nameservices")) {
                    componentName += "-HA";
                    keys = org.apache.ambari.server.controller.internal.AbstractProviderModule.jmxDesiredRpcSuffixProperties.get(componentName);
                    java.util.Map<java.lang.String, java.lang.String[]> stringMap = org.apache.ambari.server.controller.internal.AbstractProviderModule.jmxDesiredRpcSuffixProperties.get(componentName);
                    for (java.lang.String tag : stringMap.keySet()) {
                        keys.put(tag, getNamenodeHaProperty(configProperties, stringMap.get(tag), hostName, publicHostName));
                    }
                }
            }
            java.util.Map<java.lang.String, java.lang.String> props = getDesiredConfigMap(clusterName, configVersion, config, keys);
            for (java.util.Map.Entry<java.lang.String, java.lang.String> prop : props.entrySet()) {
                if (prop.getValue() != null) {
                    portToTagMap.put(getPortString(prop.getValue()).trim(), prop.getKey());
                }
            }
        }
    }

    @com.google.common.eventbus.Subscribe
    public void onConfigurationChangedEvent(org.apache.ambari.server.events.ClusterConfigChangedEvent event) {
        clusterJmxProtocolMap.clear();
    }
}