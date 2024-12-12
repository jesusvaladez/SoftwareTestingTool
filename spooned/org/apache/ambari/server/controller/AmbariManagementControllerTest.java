package org.apache.ambari.server.controller;
import javax.persistence.EntityManager;
import org.apache.commons.collections.CollectionUtils;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AmbariManagementControllerTest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.AmbariManagementControllerTest.class);

    private static final java.lang.String STACK_NAME = "HDP";

    private static final java.lang.String SERVICE_NAME_YARN = "YARN";

    private static final java.lang.String COMPONENT_NAME_NODEMANAGER = "NODEMANAGER";

    private static final java.lang.String SERVICE_NAME_HBASE = "HBASE";

    private static final java.lang.String COMPONENT_NAME_REGIONSERVER = "HBASE_REGIONSERVER";

    private static final java.lang.String COMPONENT_NAME_DATANODE = "DATANODE";

    private static final java.lang.String SERVICE_NAME_HIVE = "HIVE";

    private static final java.lang.String COMPONENT_NAME_HIVE_METASTORE = "HIVE_METASTORE";

    private static final java.lang.String COMPONENT_NAME_HIVE_SERVER = "HIVE_SERVER";

    private static final java.lang.String STACK_VERSION = "0.2";

    private static final java.lang.String NEW_STACK_VERSION = "2.0.6";

    private static final java.lang.String OS_TYPE = "centos5";

    private static final java.lang.String REPO_ID = "HDP-1.1.1.16";

    private static final java.lang.String REPO_NAME = "HDP";

    private static final java.lang.String PROPERTY_NAME = "hbase.regionserver.msginterval";

    private static final java.lang.String SERVICE_NAME = "HDFS";

    private static final java.lang.String FAKE_SERVICE_NAME = "FAKENAGIOS";

    private static final int STACK_VERSIONS_CNT = 17;

    private static final int REPOS_CNT = 3;

    private static final int STACK_PROPERTIES_CNT = 103;

    private static final int STACK_COMPONENTS_CNT = 5;

    private static final int OS_CNT = 2;

    private static final java.lang.String NON_EXT_VALUE = "XXX";

    private static final java.lang.String INCORRECT_BASE_URL = "http://incorrect.url";

    private static final java.lang.String COMPONENT_NAME = "NAMENODE";

    private static final java.lang.String REQUEST_CONTEXT_PROPERTY = "context";

    private static org.apache.ambari.server.controller.AmbariManagementController controller;

    private static org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.actionmanager.ActionDBAccessor actionDB;

    private static com.google.inject.Injector injector;

    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    private static org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private javax.persistence.EntityManager entityManager;

    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private org.apache.ambari.server.orm.dao.TopologyHostInfoDAO topologyHostInfoDAO;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    private org.apache.ambari.server.stack.StackManagerMock stackManagerMock;

    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion01;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion02;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion120;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion201;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion206;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion207;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion208;

    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion220;

    @org.junit.Rule
    public org.junit.rules.ExpectedException expectedException = org.junit.rules.ExpectedException.none();

    @org.junit.BeforeClass
    public static void beforeClass() throws java.lang.Exception {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.injector = com.google.inject.Guice.createInjector(module);
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(org.apache.ambari.server.controller.AmbariManagementControllerTest.injector);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.init();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.topology.TopologyManager topologyManager = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class);
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(topologyManager);
        org.apache.ambari.server.configuration.Configuration configuration = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.utils.StageUtils.setConfiguration(configuration);
        org.apache.ambari.server.actionmanager.ActionManager.setTopologyManager(topologyManager);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(org.apache.ambari.server.controller.AmbariManagementControllerTest.injector);
        entityManager = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getProvider(javax.persistence.EntityManager.class).get();
        actionDB = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        serviceFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        serviceComponentFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        serviceComponentHostFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        configHelper = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
        configGroupFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.configgroup.ConfigGroupFactory.class);
        helper = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        stageFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.actionmanager.StageFactory.class);
        hostDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        topologyHostInfoDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.TopologyHostInfoDAO.class);
        hostRoleCommandDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        stackManagerMock = ((org.apache.ambari.server.stack.StackManagerMock) (org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.getStackManager()));
        org.easymock.EasyMock.replay(org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class));
        repositoryVersion01 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"), "0.1-1234");
        repositoryVersion02 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-0.2"), "0.2-1234");
        repositoryVersion120 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-1.2.0"), "1.2.0-1234");
        repositoryVersion201 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.1"), "2.0.1-1234");
        repositoryVersion206 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"), "2.0.6-1234");
        repositoryVersion207 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"), "2.0.7-1234");
        repositoryVersion208 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.8"), "2.0.8-1234");
        repositoryVersion220 = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("HDP-2.2.0"), "2.2.0-1234");
        repositoryVersionDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        for (org.apache.ambari.server.state.Host host : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHosts()) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.updateHostMappings(host);
        }
    }

    @org.junit.After
    public void teardown() {
        actionDB = null;
        org.easymock.EasyMock.reset(org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class));
    }

    @org.junit.AfterClass
    public static void afterClass() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.controller.AmbariManagementControllerTest.injector);
    }

    private static java.lang.String getUniqueName() {
        return java.util.UUID.randomUUID().toString();
    }

    private void setOsFamily(org.apache.ambari.server.state.Host host, java.lang.String osFamily, java.lang.String osVersion) {
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", osFamily);
        hostAttributes.put("os_release_version", osVersion);
        host.setHostAttributes(hostAttributes);
    }

    private void addHost(java.lang.String hostname) throws java.lang.Exception {
        addHostToCluster(hostname, null);
    }

    private void addHostToCluster(java.lang.String hostname, java.lang.String clusterName) throws java.lang.Exception {
        if (!org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.hostExists(hostname)) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(hostname);
            setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(hostname), "redhat", "6.3");
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(hostname).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        }
        if (null != clusterName) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.mapHostToCluster(hostname, clusterName);
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.updateHostMappings(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(hostname));
    }

    private void deleteHost(java.lang.String hostname) throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.deleteHost(hostname);
    }

    private void createCluster(java.lang.String clusterName) throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, org.apache.ambari.server.state.State.INSTALLED.name(), org.apache.ambari.server.state.SecurityType.NONE, "HDP-0.1", null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
    }

    private void createService(java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.state.State desiredState) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        createService(clusterName, serviceName, repositoryVersion02, desiredState);
    }

    private void createService(java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion, org.apache.ambari.server.state.State desiredState) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceRequest r1 = new org.apache.ambari.server.controller.ServiceRequest(clusterName, serviceName, repositoryVersion.getId(), dStateStr, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r1);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, requests);
    }

    private void createServiceComponent(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.State desiredState) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentRequest r = new org.apache.ambari.server.controller.ServiceComponentRequest(clusterName, serviceName, componentName, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests);
    }

    private void createServiceComponentHost(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, org.apache.ambari.server.state.State desiredState) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName, hostname, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(requests);
    }

    private void deleteServiceComponentHost(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, org.apache.ambari.server.state.State desiredState) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName, hostname, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.deleteHostComponents(requests);
    }

    private java.lang.Long createConfigGroup(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName, java.lang.String name, java.lang.String tag, java.util.List<java.lang.String> hosts, java.util.List<org.apache.ambari.server.state.Config> configs) throws java.lang.Exception {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hostMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configMap = new java.util.HashMap<>();
        for (java.lang.String hostname : hosts) {
            org.apache.ambari.server.state.Host host = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(hostname);
            org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostname);
            hostMap.put(hostEntity.getHostId(), host);
        }
        for (org.apache.ambari.server.state.Config config : configs) {
            configMap.put(config.getType(), config);
        }
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(cluster, serviceName, name, tag, "", configMap, hostMap);
        configGroup.setServiceName(serviceName);
        cluster.addConfigGroup(configGroup);
        return configGroup.getId();
    }

    private long stopService(java.lang.String clusterName, java.lang.String serviceName, boolean runSmokeTests, boolean reconfigureClients) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(clusterName, serviceName, null, org.apache.ambari.server.state.State.INSTALLED.toString(), null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.RequestStatusResponse resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, runSmokeTests, reconfigureClients);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            }
        }
        return resp.getRequestId();
    }

    private long stopServiceComponentHosts(java.lang.String clusterName, java.lang.String serviceName) throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster c = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName);
        org.apache.ambari.server.state.Service s = c.getService(serviceName);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.ServiceComponent sc : s.getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.apache.ambari.server.controller.ServiceComponentHostRequest schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, sc.getName(), sch.getHostName(), org.apache.ambari.server.state.State.INSTALLED.name());
                requests.add(schr);
            }
        }
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.RequestStatusResponse resp = org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.updateHostComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, org.apache.ambari.server.controller.AmbariManagementControllerTest.injector, requests, mapRequestProps, false);
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            }
        }
        return resp.getRequestId();
    }

    private long startService(java.lang.String clusterName, java.lang.String serviceName, boolean runSmokeTests, boolean reconfigureClients) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        return startService(clusterName, serviceName, runSmokeTests, reconfigureClients, null);
    }

    private long startService(java.lang.String clusterName, java.lang.String serviceName, boolean runSmokeTests, boolean reconfigureClients, org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(clusterName, serviceName, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.RequestStatusResponse resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, runSmokeTests, reconfigureClients, maintenanceStateHelper);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getDesiredState());
        if (resp != null) {
            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = actionDB.getRequestTasks(resp.getRequestId());
            for (org.apache.ambari.server.actionmanager.HostRoleCommand cmd : commands) {
                java.lang.String scName = cmd.getRole().toString();
                if (!scName.endsWith("CHECK")) {
                    org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName);
                    java.lang.String hostname = cmd.getHostName();
                    for (org.apache.ambari.server.state.Service s : cluster.getServices().values()) {
                        if (s.getServiceComponents().containsKey(scName) && (!s.getServiceComponent(scName).isClientComponent())) {
                            s.getServiceComponent(scName).getServiceComponentHost(hostname).setState(org.apache.ambari.server.state.State.STARTED);
                            break;
                        }
                    }
                }
            }
            return resp.getRequestId();
        } else {
            return -1;
        }
    }

    private long installService(java.lang.String clusterName, java.lang.String serviceName, boolean runSmokeTests, boolean reconfigureClients) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        return installService(clusterName, serviceName, runSmokeTests, reconfigureClients, null, null);
    }

    private long installService(java.lang.String clusterName, java.lang.String serviceName, boolean runSmokeTests, boolean reconfigureClients, org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper, java.util.Map<java.lang.String, java.lang.String> mapRequestPropsInput) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(clusterName, serviceName, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        if (mapRequestPropsInput != null) {
            mapRequestProps.putAll(mapRequestPropsInput);
        }
        org.apache.ambari.server.controller.RequestStatusResponse resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, runSmokeTests, reconfigureClients, maintenanceStateHelper);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getDesiredState());
        if (resp != null) {
            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = actionDB.getRequestTasks(resp.getRequestId());
            for (org.apache.ambari.server.actionmanager.HostRoleCommand cmd : commands) {
                org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponent(cmd.getRole().name()).getServiceComponentHost(cmd.getHostName()).setState(org.apache.ambari.server.state.State.INSTALLED);
            }
            return resp.getRequestId();
        } else {
            return -1;
        }
    }

    private boolean checkExceptionType(java.lang.Throwable e, java.lang.Class<? extends java.lang.Exception> exceptionClass) {
        return (e != null) && (exceptionClass.isAssignableFrom(e.getClass()) || checkExceptionType(e.getCause(), exceptionClass));
    }

    @org.junit.Test
    public void testCreateClusterSimple() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> r = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getClusters(java.util.Collections.singleton(new org.apache.ambari.server.controller.ClusterRequest(null, cluster1, null, null)));
        org.junit.Assert.assertEquals(1, r.size());
        org.apache.ambari.server.controller.ClusterResponse c = r.iterator().next();
        org.junit.Assert.assertEquals(cluster1, c.getClusterName());
        try {
            createCluster(cluster1);
            org.junit.Assert.fail("Duplicate cluster creation should fail");
        } catch (java.lang.Exception e) {
        }
    }

    @org.junit.Test
    public void testCreateClusterWithHostMapping() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        hostNames.add(host1);
        hostNames.add(host2);
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, cluster1, "HDP-0.1", hostNames);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
            org.junit.Assert.fail("Expected create cluster to fail for invalid hosts");
        } catch (java.lang.Exception e) {
        }
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
            org.junit.Assert.fail("Expected to fail for non created cluster");
        } catch (org.apache.ambari.server.ClusterNotFoundException e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host2);
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1), "redhat", "6.3");
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2), "redhat", "6.3");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1));
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateClusterWithInvalidRequest1() throws java.lang.Exception {
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, null, null, null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateClusterWithInvalidRequest2() throws java.lang.Exception {
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(1L, null, null, null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateClusterWithInvalidRequest3() throws java.lang.Exception {
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName(), null, null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testCreateClusterWithInvalidRequest4() throws java.lang.Exception {
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, null, org.apache.ambari.server.state.State.INSTALLING.name(), null, "HDP-1.2.0", null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(r), null);
    }

    @org.junit.Test
    public void testCreateServicesSimple() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, repositoryVersion02, org.apache.ambari.server.state.State.INIT);
        org.apache.ambari.server.state.Service s = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName);
        org.junit.Assert.assertNotNull(s);
        org.junit.Assert.assertEquals(serviceName, s.getName());
        org.junit.Assert.assertEquals(cluster1, s.getCluster().getClusterName());
        org.apache.ambari.server.controller.ServiceRequest req = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> r = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(req));
        org.junit.Assert.assertEquals(1, r.size());
        org.apache.ambari.server.controller.ServiceResponse resp = r.iterator().next();
        org.junit.Assert.assertEquals(serviceName, resp.getServiceName());
        org.junit.Assert.assertEquals(cluster1, resp.getClusterName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT.toString(), resp.getDesiredState());
        org.junit.Assert.assertEquals("HDP-0.2", resp.getDesiredStackId());
    }

    @org.junit.Test
    public void testCreateServicesWithInvalidRequest() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> set1 = new java.util.HashSet<>();
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceRequest rInvalid = new org.apache.ambari.server.controller.ServiceRequest(null, null, null, null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceRequest rInvalid = new org.apache.ambari.server.controller.ServiceRequest("foo", null, null, null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceRequest rInvalid = new org.apache.ambari.server.controller.ServiceRequest("foo", "bar", null, null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
            org.junit.Assert.fail("Expected failure for invalid cluster");
        } catch (java.lang.Exception e) {
            org.junit.Assert.assertTrue(checkExceptionType(e, org.apache.ambari.server.ClusterNotFoundException.class));
        }
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster2, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceRequest valid1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", null, null, null);
            org.apache.ambari.server.controller.ServiceRequest valid2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", null, null, null);
            set1.add(valid1);
            set1.add(valid2);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceRequest valid1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "bar", repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
            set1.add(valid1);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
            org.junit.Assert.fail("Expected failure for invalid service");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceRequest valid1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
            org.apache.ambari.server.controller.ServiceRequest valid2 = new org.apache.ambari.server.controller.ServiceRequest(cluster2, "HDFS", repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
            set1.add(valid1);
            set1.add(valid2);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
            org.junit.Assert.fail("Expected failure for multiple clusters");
        } catch (java.lang.Exception e) {
        }
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1));
        org.junit.Assert.assertEquals(0, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServices().size());
        set1.clear();
        org.apache.ambari.server.controller.ServiceRequest valid = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), null, null);
        set1.add(valid);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceRequest valid1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
            org.apache.ambari.server.controller.ServiceRequest valid2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
            set1.add(valid1);
            set1.add(valid2);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
            org.junit.Assert.fail("Expected failure for existing service");
        } catch (java.lang.Exception e) {
        }
        org.junit.Assert.assertEquals(1, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServices().size());
    }

    @org.junit.Test
    public void testCreateServiceWithInvalidInfo() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName = "HDFS";
        try {
            createService(cluster1, serviceName, org.apache.ambari.server.state.State.INSTALLING);
            org.junit.Assert.fail("Service creation should fail for invalid state");
        } catch (java.lang.Exception e) {
        }
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName);
            org.junit.Assert.fail("Service creation should have failed");
        } catch (java.lang.Exception e) {
        }
        try {
            createService(cluster1, serviceName, org.apache.ambari.server.state.State.INSTALLED);
            org.junit.Assert.fail("Service creation should fail for invalid initial state");
        } catch (java.lang.Exception e) {
        }
        createService(cluster1, serviceName, null);
        java.lang.String serviceName2 = "MAPREDUCE";
        createService(cluster1, serviceName2, org.apache.ambari.server.state.State.INIT);
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> response = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, response.size());
        for (org.apache.ambari.server.controller.ServiceResponse svc : response) {
            org.junit.Assert.assertTrue(svc.getServiceName().equals(serviceName) || svc.getServiceName().equals(serviceName2));
            org.junit.Assert.assertEquals("HDP-0.2", svc.getDesiredStackId());
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT.toString(), svc.getDesiredState());
        }
    }

    @org.junit.Test
    public void testCreateServicesMultiple() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> set1 = new java.util.HashSet<>();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.controller.ServiceRequest valid1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion01.getId(), null, null);
        org.apache.ambari.server.controller.ServiceRequest valid2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "MAPREDUCE", repositoryVersion01.getId(), null, null);
        set1.add(valid1);
        set1.add(valid2);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
        try {
            valid1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "PIG", repositoryVersion01.getId(), null, null);
            valid2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, "MAPREDUCE", 4L, null, null);
            set1.add(valid1);
            set1.add(valid2);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, repositoryVersionDAO, set1);
            org.junit.Assert.fail("Expected failure for invalid services");
        } catch (java.lang.Exception e) {
            org.junit.Assert.assertTrue(checkExceptionType(e, org.apache.ambari.server.DuplicateResourceException.class));
        }
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1));
        org.junit.Assert.assertEquals(2, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServices().size());
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService("HDFS"));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService("MAPREDUCE"));
    }

    @org.junit.Test
    public void testCreateServiceComponentSimple() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName = "NAMENODE";
        try {
            createServiceComponent(cluster1, serviceName, componentName, org.apache.ambari.server.state.State.INSTALLING);
            org.junit.Assert.fail("ServiceComponent creation should fail for invalid state");
        } catch (java.lang.Exception e) {
        }
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName);
            org.junit.Assert.fail("ServiceComponent creation should have failed");
        } catch (java.lang.Exception e) {
        }
        createServiceComponent(cluster1, serviceName, componentName, org.apache.ambari.server.state.State.INIT);
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName));
        org.apache.ambari.server.controller.ServiceComponentRequest r = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> response = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, response.size());
        org.apache.ambari.server.controller.ServiceComponentResponse sc = response.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT.toString(), sc.getDesiredState());
        org.junit.Assert.assertEquals(componentName, sc.getComponentName());
        org.junit.Assert.assertEquals(cluster1, sc.getClusterName());
        org.junit.Assert.assertEquals(serviceName, sc.getServiceName());
    }

    @org.junit.Test
    public void testCreateServiceComponentWithInvalidRequest() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> set1 = new java.util.HashSet<>();
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentRequest(null, null, null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, null, null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "s1", null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "s1", "sc1", null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for invalid cluster");
        } catch (org.apache.ambari.server.ParentObjectNotFoundException e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster2, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "NAMENODE", null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for invalid service");
        } catch (org.apache.ambari.server.ParentObjectNotFoundException e) {
        }
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        c1.setDesiredStackVersion(stackId);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service s2 = serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        c1.addService(s1);
        c1.addService(s2);
        set1.clear();
        org.apache.ambari.server.controller.ServiceComponentRequest valid1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "NAMENODE", null);
        org.apache.ambari.server.controller.ServiceComponentRequest valid2 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "MAPREDUCE", "JOBTRACKER", null);
        org.apache.ambari.server.controller.ServiceComponentRequest valid3 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "MAPREDUCE", "TASKTRACKER", null);
        set1.add(valid1);
        set1.add(valid2);
        set1.add(valid3);
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "HDFS_CLIENT", null);
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid2 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "HDFS_CLIENT", null);
            set1.add(rInvalid1);
            set1.add(rInvalid2);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for dups in requests");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "HDFS_CLIENT", null);
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid2 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster2, "HDFS", "HDFS_CLIENT", null);
            set1.add(rInvalid1);
            set1.add(rInvalid2);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for multiple clusters");
        } catch (java.lang.Exception e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "NAMENODE", null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for already existing component");
        } catch (java.lang.Exception e) {
        }
        org.junit.Assert.assertEquals(1, s1.getServiceComponents().size());
        org.junit.Assert.assertNotNull(s1.getServiceComponent("NAMENODE"));
        org.junit.Assert.assertEquals(2, s2.getServiceComponents().size());
        org.junit.Assert.assertNotNull(s2.getServiceComponent("JOBTRACKER"));
        org.junit.Assert.assertNotNull(s2.getServiceComponent("TASKTRACKER"));
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testGetExecutionCommandWithClusterEnvForRetry() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        configs.put("command_retry_enabled", "true");
        configs.put("command_retry_max_time_in_sec", "5");
        configs.put("commands_to_retry", "INSTALL");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "cluster-env", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        org.apache.ambari.server.controller.ServiceComponentHostRequest schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, "HDFS", "DATANODE", host2, "INSTALLED");
        java.util.Map<java.lang.String, java.lang.String> requestProps = new java.util.HashMap<>();
        requestProps.put("phase", "INITIAL_INSTALL");
        org.apache.ambari.server.controller.RequestStatusResponse rsr = updateHostComponents(java.util.Collections.singleton(schr), requestProps, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(rsr.getRequestId());
        org.junit.Assert.assertEquals(1, stages.size());
        org.apache.ambari.server.actionmanager.Stage stage = stages.iterator().next();
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> execWrappers = stage.getExecutionCommands(host2);
        org.junit.Assert.assertEquals(1, execWrappers.size());
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execWrapper = execWrappers.iterator().next();
        org.apache.ambari.server.agent.ExecutionCommand ec = execWrapper.getExecutionCommand();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = ec.getConfigurations();
        org.junit.Assert.assertNotNull(configurations);
        org.junit.Assert.assertEquals(1, configurations.size());
        org.junit.Assert.assertTrue(configurations.containsKey("cluster-env"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("max_duration_for_retries"));
        org.junit.Assert.assertEquals("5", ec.getCommandParams().get("max_duration_for_retries"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("command_retry_enabled"));
        org.junit.Assert.assertEquals("true", ec.getCommandParams().get("command_retry_enabled"));
        for (org.apache.ambari.server.state.ServiceComponentHost sch : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServiceComponentHosts(host2)) {
            sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        }
        schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, "HDFS", "DATANODE", host2, "STARTED");
        rsr = updateHostComponents(java.util.Collections.singleton(schr), requestProps, false);
        stages = actionDB.getAllStages(rsr.getRequestId());
        org.junit.Assert.assertEquals(1, stages.size());
        stage = stages.iterator().next();
        execWrappers = stage.getExecutionCommands(host2);
        org.junit.Assert.assertEquals(1, execWrappers.size());
        execWrapper = execWrappers.iterator().next();
        ec = execWrapper.getExecutionCommand();
        configurations = ec.getConfigurations();
        org.junit.Assert.assertNotNull(configurations);
        org.junit.Assert.assertEquals(1, configurations.size());
        org.junit.Assert.assertTrue(configurations.containsKey("cluster-env"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("max_duration_for_retries"));
        org.junit.Assert.assertEquals("5", ec.getCommandParams().get("max_duration_for_retries"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("command_retry_enabled"));
        org.junit.Assert.assertEquals("false", ec.getCommandParams().get("command_retry_enabled"));
        configs.put("command_retry_enabled", "true");
        configs.put("command_retry_max_time_in_sec", "12");
        configs.put("commands_to_retry", "START");
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "cluster-env", "version2", configs, null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        requestProps.put("phase", "INITIAL_START");
        schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, "HDFS", "DATANODE", host2, "STARTED");
        rsr = updateHostComponents(java.util.Collections.singleton(schr), requestProps, false);
        stages = actionDB.getAllStages(rsr.getRequestId());
        org.junit.Assert.assertEquals(1, stages.size());
        stage = stages.iterator().next();
        execWrappers = stage.getExecutionCommands(host2);
        org.junit.Assert.assertEquals(1, execWrappers.size());
        execWrapper = execWrappers.iterator().next();
        ec = execWrapper.getExecutionCommand();
        configurations = ec.getConfigurations();
        org.junit.Assert.assertNotNull(configurations);
        org.junit.Assert.assertEquals(1, configurations.size());
        org.junit.Assert.assertTrue(configurations.containsKey("cluster-env"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("max_duration_for_retries"));
        org.junit.Assert.assertEquals("12", ec.getCommandParams().get("max_duration_for_retries"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("command_retry_enabled"));
        org.junit.Assert.assertEquals("true", ec.getCommandParams().get("command_retry_enabled"));
        configs.put("command_retry_enabled", "asdf");
        configs.put("command_retry_max_time_in_sec", "-5");
        configs.put("commands_to_retry2", "START");
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "cluster-env", "version3", configs, null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        requestProps.put("phase", "INITIAL_START");
        schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, "HDFS", "DATANODE", host2, "STARTED");
        rsr = updateHostComponents(java.util.Collections.singleton(schr), requestProps, false);
        stages = actionDB.getAllStages(rsr.getRequestId());
        org.junit.Assert.assertEquals(1, stages.size());
        stage = stages.iterator().next();
        execWrappers = stage.getExecutionCommands(host2);
        org.junit.Assert.assertEquals(1, execWrappers.size());
        execWrapper = execWrappers.iterator().next();
        ec = execWrapper.getExecutionCommand();
        configurations = ec.getConfigurations();
        org.junit.Assert.assertNotNull(configurations);
        org.junit.Assert.assertEquals(1, configurations.size());
        org.junit.Assert.assertTrue(configurations.containsKey("cluster-env"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("max_duration_for_retries"));
        org.junit.Assert.assertEquals("0", ec.getCommandParams().get("max_duration_for_retries"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("command_retry_enabled"));
        org.junit.Assert.assertEquals("false", ec.getCommandParams().get("command_retry_enabled"));
    }

    @org.junit.Test
    public void testGetExecutionCommand() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createServiceComponentHostSimple(cluster1, host1, org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName());
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s1 = cluster.getService(serviceName);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        java.util.Map<java.lang.String, java.lang.String> hadoopEnvConfigs = new java.util.HashMap<>();
        hadoopEnvConfigs.put("hdfs_user", "myhdfsuser");
        hadoopEnvConfigs.put("hdfs_group", "myhdfsgroup");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        org.apache.ambari.server.controller.ConfigurationRequest cr3;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        cr3 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hadoop-env", "version1", hadoopEnvConfigs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr2));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr3));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        installService(cluster1, serviceName, false, false);
        org.apache.ambari.server.agent.ExecutionCommand ec = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getExecutionCommand(cluster, s1.getServiceComponent("NAMENODE").getServiceComponentHost(host1), org.apache.ambari.server.RoleCommand.START);
        org.junit.Assert.assertEquals("1-0", ec.getCommandId());
        org.junit.Assert.assertEquals(cluster1, ec.getClusterName());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = ec.getConfigurations();
        org.junit.Assert.assertNotNull(configurations);
        org.junit.Assert.assertEquals(0, configurations.size());
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("max_duration_for_retries"));
        org.junit.Assert.assertEquals("0", ec.getCommandParams().get("max_duration_for_retries"));
        org.junit.Assert.assertTrue(ec.getCommandParams().containsKey("command_retry_enabled"));
        org.junit.Assert.assertEquals("false", ec.getCommandParams().get("command_retry_enabled"));
        org.junit.Assert.assertFalse(ec.getCommandParams().containsKey("custom_folder"));
        ec = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getExecutionCommand(cluster, s1.getServiceComponent("DATANODE").getServiceComponentHost(host1), org.apache.ambari.server.RoleCommand.START);
        org.junit.Assert.assertEquals(cluster1, ec.getClusterName());
        org.junit.Assert.assertNotNull(ec.getCommandParams());
        org.junit.Assert.assertNotNull(ec.getHostLevelParams());
        org.junit.Assert.assertTrue(ec.getHostLevelParams().containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_LIST));
        org.junit.Assert.assertEquals("[\"myhdfsuser\"]", ec.getHostLevelParams().get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_LIST));
        org.junit.Assert.assertTrue(ec.getHostLevelParams().containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GROUP_LIST));
        org.junit.Assert.assertEquals("[\"myhdfsgroup\"]", ec.getHostLevelParams().get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.GROUP_LIST));
        org.junit.Assert.assertTrue(ec.getHostLevelParams().containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS));
        org.junit.Assert.assertEquals("{\"myhdfsuser\":[\"myhdfsgroup\"]}", ec.getHostLevelParams().get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS));
    }

    @org.junit.Test
    public void testCreateServiceComponentMultiple() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.2"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster2, new org.apache.ambari.server.state.StackId("HDP-0.2"));
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.2");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service s2 = serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        c1.addService(s1);
        c1.addService(s2);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> set1 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceComponentRequest valid1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "NAMENODE", null);
        org.apache.ambari.server.controller.ServiceComponentRequest valid2 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "MAPREDUCE", "JOBTRACKER", null);
        org.apache.ambari.server.controller.ServiceComponentRequest valid3 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "MAPREDUCE", "TASKTRACKER", null);
        set1.add(valid1);
        set1.add(valid2);
        set1.add(valid3);
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
        org.junit.Assert.assertEquals(1, c1.getService("HDFS").getServiceComponents().size());
        org.junit.Assert.assertEquals(2, c1.getService("MAPREDUCE").getServiceComponents().size());
        org.junit.Assert.assertNotNull(c1.getService("HDFS").getServiceComponent("NAMENODE"));
        org.junit.Assert.assertNotNull(c1.getService("MAPREDUCE").getServiceComponent("JOBTRACKER"));
        org.junit.Assert.assertNotNull(c1.getService("MAPREDUCE").getServiceComponent("TASKTRACKER"));
    }

    @org.junit.Test
    public void testCreateServiceComponentHostSimple1() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createServiceComponentHostSimple(cluster1, host1, host2);
    }

    private void createServiceComponentHostSimple(java.lang.String clusterName, java.lang.String host1, java.lang.String host2) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        createCluster(clusterName);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(clusterName, serviceName, repositoryVersion01, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(clusterName, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        try {
            createServiceComponentHost(clusterName, serviceName, componentName1, host1, org.apache.ambari.server.state.State.INIT);
            org.junit.Assert.fail("ServiceComponentHost creation should fail for invalid host" + " as host not mapped to cluster");
        } catch (java.lang.Exception e) {
        }
        addHostToCluster(host1, clusterName);
        addHostToCluster(host2, clusterName);
        try {
            createServiceComponentHost(clusterName, serviceName, componentName1, host1, org.apache.ambari.server.state.State.INSTALLING);
            org.junit.Assert.fail("ServiceComponentHost creation should fail for invalid state");
        } catch (java.lang.Exception e) {
        }
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponent(componentName1).getServiceComponentHost(host1);
            org.junit.Assert.fail("ServiceComponentHost creation should have failed earlier");
        } catch (java.lang.Exception e) {
        }
        createServiceComponentHost(clusterName, null, componentName1, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName2, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName2, host2, null);
        createServiceComponentHost(clusterName, serviceName, componentName3, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName3, host2, null);
        try {
            createServiceComponentHost(clusterName, serviceName, componentName1, host1, null);
            org.junit.Assert.fail("ServiceComponentHost creation should fail as duplicate");
        } catch (java.lang.Exception e) {
        }
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponent(componentName1).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host2));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host2));
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName2, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, response.size());
    }

    @org.junit.Test
    public void testCreateServiceComponentHostMultiple() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> set1 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceComponentHostRequest r1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName2, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host2, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r4 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName2, host2, org.apache.ambari.server.state.State.INIT.toString());
        set1.add(r1);
        set1.add(r2);
        set1.add(r3);
        set1.add(r4);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
        org.junit.Assert.assertEquals(2, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServiceComponentHosts(host1).size());
        org.junit.Assert.assertEquals(2, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServiceComponentHosts(host2).size());
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName1).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName1).getServiceComponentHost(host2));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host2));
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testCreateServiceComponentHostExclusiveAmbariException() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "EXCLUSIVE_DEPENDENCY_COMPONENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> set1 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceComponentHostRequest r1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName3, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName2, host1, org.apache.ambari.server.state.State.INIT.toString());
        set1.add(r1);
        set1.add(r2);
        set1.add(r3);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
    }

    @org.junit.Test
    public void testCreateServiceComponentHostWithInvalidRequest() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> set1 = new java.util.HashSet<>();
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest(null, null, null, null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest("foo", null, null, null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest("foo", "HDFS", null, null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest("foo", "HDFS", "NAMENODE", null, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid requests");
        } catch (java.lang.IllegalArgumentException e) {
        }
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String clusterFoo = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host1, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid cluster");
        } catch (org.apache.ambari.server.ParentObjectNotFoundException e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(clusterFoo, new org.apache.ambari.server.state.StackId("HDP-0.2"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.2"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster2, new org.apache.ambari.server.state.StackId("HDP-0.2"));
        org.apache.ambari.server.state.Cluster foo = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterFoo);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Cluster c2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster2);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.2");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        foo.setDesiredStackVersion(stackId);
        foo.setCurrentStackVersion(stackId);
        stackId = new org.apache.ambari.server.state.StackId("HDP-0.2");
        c1.setDesiredStackVersion(stackId);
        c1.setCurrentStackVersion(stackId);
        stackId = new org.apache.ambari.server.state.StackId("HDP-0.2");
        c2.setDesiredStackVersion(stackId);
        c2.setCurrentStackVersion(stackId);
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host1, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid service");
        } catch (java.lang.IllegalArgumentException e) {
        }
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(foo, "HDFS", repositoryVersion);
        foo.addService(s1);
        org.apache.ambari.server.state.Service s2 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(s2);
        org.apache.ambari.server.state.Service s3 = serviceFactory.createNew(c2, "HDFS", repositoryVersion);
        c2.addService(s3);
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host1, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid service");
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.state.ServiceComponent sc1 = serviceComponentFactory.createNew(s1, "NAMENODE");
        s1.addServiceComponent(sc1);
        org.apache.ambari.server.state.ServiceComponent sc2 = serviceComponentFactory.createNew(s2, "NAMENODE");
        s2.addServiceComponent(sc2);
        org.apache.ambari.server.state.ServiceComponent sc3 = serviceComponentFactory.createNew(s3, "NAMENODE");
        s3.addServiceComponent(sc3);
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host1, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid host");
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host1);
        org.apache.ambari.server.state.Host h1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1);
        h1.setIPv4("ipv41");
        h1.setIPv6("ipv61");
        setOsFamily(h1, "redhat", "6.3");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host2);
        org.apache.ambari.server.state.Host h2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2);
        h2.setIPv4("ipv42");
        h2.setIPv6("ipv62");
        setOsFamily(h2, "redhat", "6.3");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host3);
        org.apache.ambari.server.state.Host h3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3);
        h3.setIPv4("ipv43");
        h3.setIPv6("ipv63");
        setOsFamily(h3, "redhat", "6.3");
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host1, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for invalid host cluster mapping");
        } catch (java.lang.Exception e) {
        }
        java.util.Set<java.lang.String> hostnames = new java.util.HashSet<>();
        hostnames.add(host1);
        hostnames.add(host2);
        hostnames.add(host3);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.mapAndPublishHostsToCluster(hostnames, clusterFoo);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.mapAndPublishHostsToCluster(hostnames, cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.mapAndPublishHostsToCluster(hostnames, cluster2);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.updateHostMappings(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.updateHostMappings(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.updateHostMappings(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3));
        set1.clear();
        org.apache.ambari.server.controller.ServiceComponentHostRequest valid = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host1, null);
        set1.add(valid);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host2, null);
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host2, null);
            set1.add(rInvalid1);
            set1.add(rInvalid2);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for dup requests");
        } catch (org.apache.ambari.server.DuplicateResourceException e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, "HDFS", "NAMENODE", host2, null);
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster2, "HDFS", "NAMENODE", host3, null);
            set1.add(rInvalid1);
            set1.add(rInvalid2);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for multiple clusters");
        } catch (java.lang.IllegalArgumentException e) {
        }
        try {
            set1.clear();
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host1, null);
            org.apache.ambari.server.controller.ServiceComponentHostRequest rInvalid2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterFoo, "HDFS", "NAMENODE", host2, null);
            set1.add(rInvalid1);
            set1.add(rInvalid2);
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
            org.junit.Assert.fail("Expected failure for already existing");
        } catch (org.apache.ambari.server.DuplicateResourceException e) {
        }
        org.junit.Assert.assertEquals(1, foo.getServiceComponentHosts(host1).size());
        org.junit.Assert.assertEquals(0, foo.getServiceComponentHosts(host2).size());
        org.junit.Assert.assertEquals(0, foo.getServiceComponentHosts(host3).size());
        set1.clear();
        org.apache.ambari.server.controller.ServiceComponentHostRequest valid1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, "HDFS", "NAMENODE", host1, null);
        set1.add(valid1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
        set1.clear();
        org.apache.ambari.server.controller.ServiceComponentHostRequest valid2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster2, "HDFS", "NAMENODE", host1, null);
        set1.add(valid2);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
        org.junit.Assert.assertEquals(1, foo.getServiceComponentHosts(host1).size());
        org.junit.Assert.assertEquals(1, c1.getServiceComponentHosts(host1).size());
        org.junit.Assert.assertEquals(1, c2.getServiceComponentHosts(host1).size());
    }

    @org.junit.Test
    public void testCreateHostSimple() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.HostRequest r1 = new org.apache.ambari.server.controller.HostRequest(host1, null);
        r1.toString();
        java.util.Set<org.apache.ambari.server.controller.HostRequest> requests = new java.util.HashSet<>();
        requests.add(r1);
        try {
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests);
            org.junit.Assert.fail("Create host should fail for non-bootstrapped host");
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host2);
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1), "redhat", "5.9");
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2), "redhat", "5.9");
        org.apache.ambari.server.controller.HostRequest request = new org.apache.ambari.server.controller.HostRequest(host2, "foo");
        requests.add(request);
        try {
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests);
            org.junit.Assert.fail("Create host should fail for invalid clusters");
        } catch (java.lang.Exception e) {
        }
        request.setClusterName(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.state.Cluster c = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        c.setDesiredStackVersion(stackId);
        c.setCurrentStackVersion(stackId);
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests);
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2));
        org.junit.Assert.assertEquals(0, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getClustersForHost(host1).size());
        org.junit.Assert.assertEquals(1, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getClustersForHost(host2).size());
    }

    @org.junit.Test
    public void testCreateHostMultiple() throws java.lang.Exception {
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host2);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host3);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.state.Cluster c = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.StackId stackID = new org.apache.ambari.server.state.StackId("HDP-0.1");
        c.setDesiredStackVersion(stackID);
        c.setCurrentStackVersion(stackID);
        helper.getOrCreateRepositoryVersion(stackID, stackID.getStackVersion());
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1), "redhat", "5.9");
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2), "redhat", "5.9");
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3), "redhat", "5.9");
        org.apache.ambari.server.controller.HostRequest r1 = new org.apache.ambari.server.controller.HostRequest(host1, cluster1);
        org.apache.ambari.server.controller.HostRequest r2 = new org.apache.ambari.server.controller.HostRequest(host2, cluster1);
        org.apache.ambari.server.controller.HostRequest r3 = new org.apache.ambari.server.controller.HostRequest(host3, null);
        java.util.Set<org.apache.ambari.server.controller.HostRequest> set1 = new java.util.HashSet<>();
        set1.add(r1);
        set1.add(r2);
        set1.add(r3);
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
        org.junit.Assert.assertEquals(1, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getClustersForHost(host1).size());
        org.junit.Assert.assertEquals(1, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getClustersForHost(host2).size());
        org.junit.Assert.assertEquals(0, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getClustersForHost(host3).size());
    }

    @org.junit.Test
    public void testCreateHostWithInvalidRequests() throws java.lang.Exception {
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.util.Set<org.apache.ambari.server.controller.HostRequest> set1 = new java.util.HashSet<>();
        try {
            set1.clear();
            org.apache.ambari.server.controller.HostRequest rInvalid = new org.apache.ambari.server.controller.HostRequest(host1, null);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for invalid host");
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host1);
        try {
            set1.clear();
            org.apache.ambari.server.controller.HostRequest rInvalid = new org.apache.ambari.server.controller.HostRequest(host1, cluster1);
            set1.add(rInvalid);
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for invalid cluster");
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        try {
            set1.clear();
            org.apache.ambari.server.controller.HostRequest rInvalid1 = new org.apache.ambari.server.controller.HostRequest(host1, cluster1);
            rInvalid1.setRackInfo(java.util.UUID.randomUUID().toString());
            org.apache.ambari.server.controller.HostRequest rInvalid2 = new org.apache.ambari.server.controller.HostRequest(host1, cluster1);
            set1.add(rInvalid1);
            set1.add(rInvalid2);
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set1);
            org.junit.Assert.fail("Expected failure for dup requests");
        } catch (java.lang.Exception e) {
        }
    }

    @org.junit.Test
    public void testRequestStatusLogs() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createServiceComponentHostSimple(cluster1, org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName());
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        for (org.apache.ambari.server.state.Host h : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHosts()) {
            h.setPrefix(org.apache.ambari.server.configuration.Configuration.PREFIX_DIR);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        org.apache.ambari.server.state.ConfigFactory configFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config c1 = configFactory.createNew(cluster, "hdfs-site", "v1", properties, propertiesAttributes);
        configs.put(c1.getType(), c1);
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.RequestStatusResponse trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> taskStatuses = trackAction.getTasks();
        org.junit.Assert.assertFalse(taskStatuses.isEmpty());
        for (org.apache.ambari.server.controller.ShortTaskStatus task : taskStatuses) {
            org.junit.Assert.assertEquals("Task output logs don't match", ((org.apache.ambari.server.configuration.Configuration.PREFIX_DIR + "/output-") + task.getTaskId()) + ".txt", task.getOutputLog());
            org.junit.Assert.assertEquals("Task error logs don't match", ((org.apache.ambari.server.configuration.Configuration.PREFIX_DIR + "/errors-") + task.getTaskId()) + ".txt", task.getErrorLog());
        }
    }

    @org.junit.Test
    public void testInstallAndStartService() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createServiceComponentHostSimple(cluster1, host1, host2);
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Config> configs = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesAttributes = new java.util.HashMap<>();
        properties.put("a", "a1");
        properties.put("b", "b1");
        org.apache.ambari.server.state.ConfigFactory configFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config c1 = configFactory.createNew(cluster, "hdfs-site", "v1", properties, propertiesAttributes);
        properties.put("c", cluster1);
        properties.put("d", "d1");
        org.apache.ambari.server.state.Config c2 = configFactory.createNew(cluster, "core-site", "v1", properties, propertiesAttributes);
        configFactory.createNew(cluster, "foo-site", "v1", properties, propertiesAttributes);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        configs.put(c1.getType(), c1);
        configs.put(c2.getType(), c2);
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.RequestStatusResponse trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, sch.getState());
            }
        }
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> taskStatuses = trackAction.getTasks();
        org.junit.Assert.assertEquals(5, taskStatuses.size());
        boolean foundH1NN = false;
        boolean foundH1DN = false;
        boolean foundH2DN = false;
        boolean foundH1CLT = false;
        boolean foundH2CLT = false;
        for (org.apache.ambari.server.controller.ShortTaskStatus taskStatus : taskStatuses) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.debug("Task dump :{}", taskStatus);
            org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.INSTALL.toString(), taskStatus.getCommand());
            org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING.toString(), taskStatus.getStatus());
            if (taskStatus.getHostName().equals(host1)) {
                if (org.apache.ambari.server.Role.NAMENODE.toString().equals(taskStatus.getRole())) {
                    foundH1NN = true;
                } else if (org.apache.ambari.server.Role.DATANODE.toString().equals(taskStatus.getRole())) {
                    foundH1DN = true;
                } else if (org.apache.ambari.server.Role.HDFS_CLIENT.toString().equals(taskStatus.getRole())) {
                    foundH1CLT = true;
                } else {
                    org.junit.Assert.fail("Found invalid role for host h1");
                }
            } else if (taskStatus.getHostName().equals(host2)) {
                if (org.apache.ambari.server.Role.DATANODE.toString().equals(taskStatus.getRole())) {
                    foundH2DN = true;
                } else if (org.apache.ambari.server.Role.HDFS_CLIENT.toString().equals(taskStatus.getRole())) {
                    foundH2CLT = true;
                } else {
                    org.junit.Assert.fail("Found invalid role for host h2");
                }
            } else {
                org.junit.Assert.fail("Found invalid host in task list");
            }
        }
        org.junit.Assert.assertTrue((((foundH1DN && foundH1NN) && foundH2DN) && foundH1CLT) && foundH2CLT);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(trackAction.getRequestId());
        org.junit.Assert.assertEquals(1, stages.size());
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info(((("Stage Details for Install Service" + ", stageId=") + stage.getStageId()) + ", actionId=") + stage.getActionId());
            for (java.lang.String host : stage.getHosts()) {
                for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper ecw : stage.getExecutionCommands(host)) {
                    org.junit.Assert.assertNotNull(ecw.getExecutionCommand().getRepositoryFile());
                }
            }
        }
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STAGE_ID_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals(trackAction.getRequestId()).toPredicate();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = hostRoleCommandDAO.findAll(request, predicate);
        org.junit.Assert.assertEquals(5, entities.size());
        java.lang.Long taskId = entities.get(0).getTaskId();
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals(trackAction.getRequestId()).and().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).equals(taskId).toPredicate();
        entities = hostRoleCommandDAO.findAll(request, predicate);
        org.junit.Assert.assertEquals(1, entities.size());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            }
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
        requests.clear();
        requests.add(r);
        trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            if (sc.getName().equals("HDFS_CLIENT")) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sc.getDesiredState());
            }
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                if (sch.getServiceComponentName().equals("HDFS_CLIENT")) {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
                } else {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch.getDesiredState());
                }
            }
        }
        stages = actionDB.getAllStages(trackAction.getRequestId());
        org.junit.Assert.assertEquals(2, stages.size());
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.debugDump(sb);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info("Cluster Dump: " + sb);
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                if (sc.isClientComponent()) {
                    sch.setState(org.apache.ambari.server.state.State.INSTALLED);
                } else {
                    sch.setState(org.apache.ambari.server.state.State.INSTALL_FAILED);
                }
            }
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
        requests.clear();
        requests.add(r);
        trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
            }
        }
        stages = actionDB.getAllStages(trackAction.getRequestId());
        org.junit.Assert.assertEquals(1, stages.size());
    }

    @org.junit.Test
    public void testGetClusters() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        c1.setDesiredStackVersion(stackId);
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> resp = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getClusters(java.util.Collections.singleton(r));
        org.junit.Assert.assertFalse(resp.isEmpty());
        boolean found = false;
        for (org.apache.ambari.server.controller.ClusterResponse cr : resp) {
            if (cr.getClusterName().equals(cluster1)) {
                org.junit.Assert.assertEquals(c1.getClusterId(), cr.getClusterId());
                org.junit.Assert.assertEquals(c1.getDesiredStackVersion().getStackId(), cr.getDesiredStackVersion());
                found = true;
                break;
            }
        }
        org.junit.Assert.assertTrue(found);
    }

    @org.junit.Test
    public void testGetClustersWithFilters() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster4 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster2, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster3, new org.apache.ambari.server.state.StackId("HDP-1.2.0"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster4, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ClusterResponse> resp = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getClusters(java.util.Collections.singleton(r));
        org.junit.Assert.assertTrue(resp.size() >= 4);
        r = new org.apache.ambari.server.controller.ClusterRequest(null, cluster1, null, null);
        resp = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getClusters(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resp.size());
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.junit.Assert.assertEquals(c1.getClusterId(), resp.iterator().next().getClusterId());
        r = new org.apache.ambari.server.controller.ClusterRequest(null, null, "HDP-0.1", null);
        resp = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getClusters(java.util.Collections.singleton(r));
        org.junit.Assert.assertTrue(resp.size() >= 3);
        r = new org.apache.ambari.server.controller.ClusterRequest(null, null, null, null);
        resp = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getClusters(java.util.Collections.singleton(r));
        org.junit.Assert.assertTrue("Stack ID request is invalid and expect them all", resp.size() > 3);
    }

    @org.junit.Test
    public void testGetServices() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, stackId);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(s1);
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.apache.ambari.server.controller.ServiceResponse resp1 = resp.iterator().next();
        org.junit.Assert.assertEquals(s1.getClusterId(), resp1.getClusterId().longValue());
        org.junit.Assert.assertEquals(s1.getCluster().getClusterName(), resp1.getClusterName());
        org.junit.Assert.assertEquals(s1.getName(), resp1.getServiceName());
        org.junit.Assert.assertEquals("HDP-0.1", s1.getDesiredStackId().getStackId());
        org.junit.Assert.assertEquals(s1.getDesiredStackId().getStackId(), resp1.getDesiredStackId());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED.toString(), resp1.getDesiredState());
    }

    @org.junit.Test
    public void testGetServicesWithFilters() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.2");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, stackId);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster2, stackId);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Cluster c2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster2);
        c1.setDesiredStackVersion(stackId);
        c2.setDesiredStackVersion(stackId);
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service s2 = serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        org.apache.ambari.server.state.Service s3 = serviceFactory.createNew(c1, "HBASE", repositoryVersion);
        org.apache.ambari.server.state.Service s4 = serviceFactory.createNew(c2, "HIVE", repositoryVersion);
        org.apache.ambari.server.state.Service s5 = serviceFactory.createNew(c2, "ZOOKEEPER", repositoryVersion);
        c1.addService(s1);
        c1.addService(s2);
        c1.addService(s3);
        c2.addService(s4);
        c2.addService(s5);
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        s2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        s4.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(null, null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> resp;
        try {
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
            org.junit.Assert.fail("Expected failure for invalid request");
        } catch (java.lang.Exception e) {
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(c1.getClusterName(), null, null, null, null);
        resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(3, resp.size());
        r = new org.apache.ambari.server.controller.ServiceRequest(c1.getClusterName(), s2.getName(), null, null, null);
        resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resp.size());
        org.junit.Assert.assertEquals(s2.getName(), resp.iterator().next().getServiceName());
        try {
            r = new org.apache.ambari.server.controller.ServiceRequest(c2.getClusterName(), s1.getName(), null, null, null);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
            org.junit.Assert.fail("Expected failure for invalid service");
        } catch (java.lang.Exception e) {
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(c1.getClusterName(), null, null, "INSTALLED", null);
        resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, resp.size());
        r = new org.apache.ambari.server.controller.ServiceRequest(c2.getClusterName(), null, null, "INIT", null);
        resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resp.size());
        org.apache.ambari.server.controller.ServiceRequest r1;
        org.apache.ambari.server.controller.ServiceRequest r2;
        org.apache.ambari.server.controller.ServiceRequest r3;
        r1 = new org.apache.ambari.server.controller.ServiceRequest(c1.getClusterName(), null, null, "INSTALLED", null);
        r2 = new org.apache.ambari.server.controller.ServiceRequest(c2.getClusterName(), null, null, "INIT", null);
        r3 = new org.apache.ambari.server.controller.ServiceRequest(c2.getClusterName(), null, null, "INIT", null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> reqs = new java.util.HashSet<>();
        reqs.addAll(java.util.Arrays.asList(r1, r2, r3));
        resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs);
        org.junit.Assert.assertEquals(3, resp.size());
    }

    @org.junit.Test
    public void testGetServiceComponents() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.2");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, stackId);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        c1.setDesiredStackVersion(stackId);
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(s1);
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponent sc1 = serviceComponentFactory.createNew(s1, "DATANODE");
        s1.addServiceComponent(sc1);
        sc1.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        org.apache.ambari.server.controller.ServiceComponentRequest r = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, s1.getName(), sc1.getName(), null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> resps = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        org.apache.ambari.server.controller.ServiceComponentResponse resp = resps.iterator().next();
        org.junit.Assert.assertEquals(c1.getClusterName(), resp.getClusterName());
        org.junit.Assert.assertEquals(sc1.getName(), resp.getComponentName());
        org.junit.Assert.assertEquals(s1.getName(), resp.getServiceName());
        org.junit.Assert.assertEquals("HDP-0.2", resp.getDesiredStackId());
        org.junit.Assert.assertEquals(sc1.getDesiredState().toString(), resp.getDesiredState());
        org.junit.Assert.assertEquals(c1.getClusterId(), resp.getClusterId().longValue());
    }

    @org.junit.Test
    public void testGetServiceComponentsWithFilters() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.2");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, stackId);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster2, stackId);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Cluster c2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster2);
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service s2 = serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        org.apache.ambari.server.state.Service s3 = serviceFactory.createNew(c1, "HBASE", repositoryVersion);
        org.apache.ambari.server.state.Service s4 = serviceFactory.createNew(c2, "HIVE", repositoryVersion);
        org.apache.ambari.server.state.Service s5 = serviceFactory.createNew(c2, "ZOOKEEPER", repositoryVersion);
        c1.addService(s1);
        c1.addService(s2);
        c1.addService(s3);
        c2.addService(s4);
        c2.addService(s5);
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        s2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        s4.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponent sc1 = serviceComponentFactory.createNew(s1, "DATANODE");
        org.apache.ambari.server.state.ServiceComponent sc2 = serviceComponentFactory.createNew(s1, "NAMENODE");
        org.apache.ambari.server.state.ServiceComponent sc3 = serviceComponentFactory.createNew(s3, "HBASE_REGIONSERVER");
        org.apache.ambari.server.state.ServiceComponent sc4 = serviceComponentFactory.createNew(s4, "HIVE_SERVER");
        org.apache.ambari.server.state.ServiceComponent sc5 = serviceComponentFactory.createNew(s4, "HIVE_CLIENT");
        org.apache.ambari.server.state.ServiceComponent sc6 = serviceComponentFactory.createNew(s4, "MYSQL_SERVER");
        org.apache.ambari.server.state.ServiceComponent sc7 = serviceComponentFactory.createNew(s5, "ZOOKEEPER_SERVER");
        org.apache.ambari.server.state.ServiceComponent sc8 = serviceComponentFactory.createNew(s5, "ZOOKEEPER_CLIENT");
        s1.addServiceComponent(sc1);
        s1.addServiceComponent(sc2);
        s3.addServiceComponent(sc3);
        s4.addServiceComponent(sc4);
        s4.addServiceComponent(sc5);
        s4.addServiceComponent(sc6);
        s5.addServiceComponent(sc7);
        s5.addServiceComponent(sc8);
        sc1.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        sc3.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        sc5.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        sc6.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        sc7.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        sc8.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        org.apache.ambari.server.controller.ServiceComponentRequest r = new org.apache.ambari.server.controller.ServiceComponentRequest(null, null, null, null);
        try {
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
            org.junit.Assert.fail("Expected failure for invalid cluster");
        } catch (java.lang.Exception e) {
        }
        r = new org.apache.ambari.server.controller.ServiceComponentRequest(c1.getClusterName(), null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentResponse> resps = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(3, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentRequest(c2.getClusterName(), null, null, org.apache.ambari.server.state.State.UNINSTALLED.toString());
        resps = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(4, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentRequest(c2.getClusterName(), s5.getName(), null, null);
        resps = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentRequest(c2.getClusterName(), s4.getName(), null, org.apache.ambari.server.state.State.INIT.toString());
        resps = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        org.junit.Assert.assertEquals(sc4.getName(), resps.iterator().next().getComponentName());
        r = new org.apache.ambari.server.controller.ServiceComponentRequest(c2.getClusterName(), null, sc5.getName(), org.apache.ambari.server.state.State.INIT.toString());
        resps = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        org.junit.Assert.assertEquals(sc5.getName(), resps.iterator().next().getComponentName());
        r = new org.apache.ambari.server.controller.ServiceComponentRequest(c2.getClusterName(), s4.getName(), sc5.getName(), org.apache.ambari.server.state.State.INIT.toString());
        resps = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        org.junit.Assert.assertEquals(sc5.getName(), resps.iterator().next().getComponentName());
        org.apache.ambari.server.controller.ServiceComponentRequest r1;
        org.apache.ambari.server.controller.ServiceComponentRequest r2;
        org.apache.ambari.server.controller.ServiceComponentRequest r3;
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> reqs = new java.util.HashSet<>();
        r1 = new org.apache.ambari.server.controller.ServiceComponentRequest(c2.getClusterName(), null, null, org.apache.ambari.server.state.State.UNINSTALLED.toString());
        r2 = new org.apache.ambari.server.controller.ServiceComponentRequest(c1.getClusterName(), null, null, null);
        r3 = new org.apache.ambari.server.controller.ServiceComponentRequest(c1.getClusterName(), null, null, org.apache.ambari.server.state.State.INIT.toString());
        reqs.addAll(java.util.Arrays.asList(r1, r2, r3));
        resps = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.getComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs);
        org.junit.Assert.assertEquals(7, resps.size());
    }

    @org.junit.Test
    public void testGetServiceComponentHosts() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.Cluster c1 = setupClusterWithHosts(cluster1, "HDP-0.1", com.google.common.collect.Lists.newArrayList(host1), "centos5");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion01;
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        c1.addService(s1);
        org.apache.ambari.server.state.ServiceComponent sc1 = serviceComponentFactory.createNew(s1, "DATANODE");
        s1.addServiceComponent(sc1);
        sc1.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = serviceComponentHostFactory.createNew(sc1, host1);
        sc1.addServiceComponentHost(sch1);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        org.apache.ambari.server.controller.ServiceComponentHostResponse resp = resps.iterator().next();
        org.junit.Assert.assertEquals(c1.getClusterName(), resp.getClusterName());
        org.junit.Assert.assertEquals(sc1.getName(), resp.getComponentName());
        org.junit.Assert.assertEquals(s1.getName(), resp.getServiceName());
        org.junit.Assert.assertEquals(sch1.getHostName(), resp.getHostname());
        org.junit.Assert.assertEquals(sch1.getDesiredState().toString(), resp.getDesiredState());
        org.junit.Assert.assertEquals(sch1.getState().toString(), resp.getLiveState());
        org.junit.Assert.assertEquals(repositoryVersion.getStackId(), sch1.getServiceComponent().getDesiredStackId());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testGetServiceComponentHostsWithStaleConfigFilter() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.Cluster c = setupClusterWithHosts(cluster1, "HDP-2.0.5", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        }, "centos5");
        java.lang.Long clusterId = c.getClusterId();
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        installService(cluster1, serviceName, false, false);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(clusterId, cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        startService(cluster1, serviceName, false, false);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> actualConfig = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version1");
                    }
                });
            }
        };
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> actualConfigOld = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version0");
                    }
                });
            }
        };
        org.apache.ambari.server.state.Service s1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName);
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(5, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        r.setStaleConfig("true");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        r.setStaleConfig("false");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(3, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, host1, null);
        r.setStaleConfig("false");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, host2, null);
        r.setStaleConfig("true");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
    }

    @org.junit.Test
    public void testServiceComponentHostsWithDecommissioned() throws java.lang.Exception {
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        setupClusterWithHosts(cluster1, "HDP-2.0.7", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        }, "centos5");
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        installService(cluster1, serviceName, false, false);
        startService(cluster1, serviceName, false, false);
        org.apache.ambari.server.state.Service s1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName);
        s1.getServiceComponent(componentName2).getServiceComponentHost(host1).setComponentAdminState(org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED);
        s1.getServiceComponent(componentName2).getServiceComponentHost(host2).setComponentAdminState(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE);
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(5, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        r.setAdminState("DECOMMISSIONED");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        r.setAdminState("INSERVICE");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        r.setAdminState("INSTALLED");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(0, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, "HDFS", "DATANODE", host2, null);
        r.setAdminState("DECOMMISSIONED");
        try {
            updateHostComponents(java.util.Collections.singleton(r), new java.util.HashMap<>(), false);
            org.junit.Assert.fail("Must throw exception when decommission attribute is updated.");
        } catch (java.lang.IllegalArgumentException ex) {
            org.junit.Assert.assertTrue(ex.getMessage().contains("Property adminState cannot be modified through update"));
        }
    }

    @org.junit.Test
    public void testHbaseDecommission() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"));
        java.lang.String serviceName = "HBASE";
        createService(cluster1, serviceName, repositoryVersion207, null);
        java.lang.String componentName1 = "HBASE_MASTER";
        java.lang.String componentName2 = "HBASE_REGIONSERVER";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName1, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        org.apache.ambari.server.controller.internal.RequestOperationLevel level = new org.apache.ambari.server.controller.internal.RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, cluster1, null, null, null);
        installService(cluster1, serviceName, false, false);
        startService(cluster1, serviceName, false, false);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, s.getDesiredState());
        org.apache.ambari.server.state.ServiceComponentHost scHost = s.getServiceComponent("HBASE_REGIONSERVER").getServiceComponentHost(host2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE, scHost.getComponentAdminState());
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("excluded_hosts", host2);
                put("align_maintenance_state", "true");
            }
        };
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HBASE", "HBASE_MASTER", null);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<>();
        resourceFilters.add(resourceFilter);
        org.apache.ambari.server.controller.ExecuteActionRequest request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, level, params, false);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.apache.ambari.server.agent.ExecutionCommand execCmd = storedTasks.get(0).getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertNotNull(storedTasks);
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED, scHost.getComponentAdminState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, scHost.getMaintenanceState());
        org.apache.ambari.server.actionmanager.HostRoleCommand command = storedTasks.get(0);
        org.junit.Assert.assertTrue("DECOMMISSION".equals(command.getCustomCommandName()));
        org.junit.Assert.assertTrue(("DECOMMISSION, Excluded: " + host2).equals(command.getCommandDetail()));
        java.util.Map<java.lang.String, java.lang.String> cmdParams = command.getExecutionCommandWrapper().getExecutionCommand().getCommandParams();
        org.junit.Assert.assertTrue(cmdParams.containsKey("mark_draining_only"));
        org.junit.Assert.assertEquals("false", cmdParams.get("mark_draining_only"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.Role.HBASE_MASTER, command.getRole());
        org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, command.getRoleCommand());
        org.junit.Assert.assertEquals("DECOMMISSION", execCmd.getCommandParams().get("custom_command"));
        org.junit.Assert.assertEquals(host2, execCmd.getCommandParams().get("all_decommissioned_hosts"));
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        s.getServiceComponent("HBASE_REGIONSERVER").getServiceComponentHost(host2).setState(org.apache.ambari.server.state.State.INSTALLED);
        params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("excluded_hosts", host2);
                put("mark_draining_only", "true");
                put("slave_type", "HBASE_REGIONSERVER");
                put("align_maintenance_state", "true");
            }
        };
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HBASE", "HBASE_MASTER", null);
        java.util.ArrayList<org.apache.ambari.server.controller.internal.RequestResourceFilter> filters = new java.util.ArrayList<>();
        filters.add(resourceFilter);
        request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, filters, level, params, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        storedTasks = actionDB.getRequestTasks(response.getRequestId());
        execCmd = storedTasks.get(0).getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertNotNull(storedTasks);
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED, scHost.getComponentAdminState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, scHost.getMaintenanceState());
        command = storedTasks.get(0);
        org.junit.Assert.assertEquals("DECOMMISSION", execCmd.getCommandParams().get("custom_command"));
        org.junit.Assert.assertEquals(host2, execCmd.getCommandParams().get("all_decommissioned_hosts"));
        org.junit.Assert.assertTrue("DECOMMISSION".equals(command.getCustomCommandName()));
        org.junit.Assert.assertTrue(("DECOMMISSION, Excluded: " + host2).equals(command.getCommandDetail()));
        cmdParams = command.getExecutionCommandWrapper().getExecutionCommand().getCommandParams();
        org.junit.Assert.assertTrue(cmdParams.containsKey("mark_draining_only"));
        org.junit.Assert.assertEquals("true", cmdParams.get("mark_draining_only"));
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("included_hosts", host2);
            }
        };
        request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, level, params, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        storedTasks = actionDB.getRequestTasks(response.getRequestId());
        execCmd = storedTasks.get(0).getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertNotNull(storedTasks);
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE, scHost.getComponentAdminState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, scHost.getMaintenanceState());
        command = storedTasks.get(0);
        org.junit.Assert.assertTrue("DECOMMISSION".equals(command.getCustomCommandName()));
        org.junit.Assert.assertTrue(("DECOMMISSION, Included: " + host2).equals(command.getCommandDetail()));
        cmdParams = command.getExecutionCommandWrapper().getExecutionCommand().getCommandParams();
        org.junit.Assert.assertTrue(cmdParams.containsKey("mark_draining_only"));
        org.junit.Assert.assertEquals("false", cmdParams.get("mark_draining_only"));
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        org.junit.Assert.assertTrue(cmdParams.containsKey("excluded_hosts"));
        org.junit.Assert.assertEquals("", cmdParams.get("excluded_hosts"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.Role.HBASE_MASTER, command.getRole());
        org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, command.getRoleCommand());
        org.junit.Assert.assertEquals("DECOMMISSION", execCmd.getCommandParams().get("custom_command"));
    }

    private org.apache.ambari.server.state.Cluster setupClusterWithHosts(java.lang.String clusterName, java.lang.String stackId, java.util.List<java.lang.String> hosts, java.lang.String osType) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, stackId, null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(clusterName);
        for (java.lang.String host : hosts) {
            addHostToCluster(host, clusterName);
        }
        for (org.apache.ambari.server.state.Host host : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHosts()) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.updateHostMappings(host);
        }
        return c1;
    }

    @org.junit.Test
    public void testGetServiceComponentHostsWithFilters() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        final java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.Cluster c1 = setupClusterWithHosts(cluster1, "HDP-0.2", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
                add(host3);
            }
        }, "centos5");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion02;
        org.apache.ambari.server.state.Service s1 = serviceFactory.createNew(c1, "HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service s2 = serviceFactory.createNew(c1, "MAPREDUCE", repositoryVersion);
        org.apache.ambari.server.state.Service s3 = serviceFactory.createNew(c1, "HBASE", repositoryVersion);
        c1.addService(s1);
        c1.addService(s2);
        c1.addService(s3);
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        s2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponent sc1 = serviceComponentFactory.createNew(s1, "DATANODE");
        org.apache.ambari.server.state.ServiceComponent sc2 = serviceComponentFactory.createNew(s1, "NAMENODE");
        org.apache.ambari.server.state.ServiceComponent sc3 = serviceComponentFactory.createNew(s3, "HBASE_REGIONSERVER");
        s1.addServiceComponent(sc1);
        s1.addServiceComponent(sc2);
        s3.addServiceComponent(sc3);
        sc1.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        sc3.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = serviceComponentHostFactory.createNew(sc1, host1);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = serviceComponentHostFactory.createNew(sc1, host2);
        org.apache.ambari.server.state.ServiceComponentHost sch3 = serviceComponentHostFactory.createNew(sc1, host3);
        org.apache.ambari.server.state.ServiceComponentHost sch4 = serviceComponentHostFactory.createNew(sc2, host1);
        org.apache.ambari.server.state.ServiceComponentHost sch5 = serviceComponentHostFactory.createNew(sc2, host2);
        org.apache.ambari.server.state.ServiceComponentHost sch6 = serviceComponentHostFactory.createNew(sc3, host3);
        sc1.addServiceComponentHost(sch1);
        sc1.addServiceComponentHost(sch2);
        sc1.addServiceComponentHost(sch3);
        sc2.addServiceComponentHost(sch4);
        sc2.addServiceComponentHost(sch5);
        sc3.addServiceComponentHost(sch6);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch4.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setDesiredState(org.apache.ambari.server.state.State.UNINSTALLED);
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(null, null, null, null, null);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
            org.junit.Assert.fail("Expected failure for invalid cluster");
        } catch (java.lang.Exception e) {
        }
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(6, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), s1.getName(), null, null, null);
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(5, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, sc3.getName(), null, null);
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, null, host2, null);
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, null, null, org.apache.ambari.server.state.State.UNINSTALLED.toString());
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), s1.getName(), null, null, org.apache.ambari.server.state.State.INIT.toString());
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, sc3.getName(), null, org.apache.ambari.server.state.State.INSTALLED.toString());
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(0, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, null, host2, org.apache.ambari.server.state.State.INIT.toString());
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), s3.getName(), null, host1, null);
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(0, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), s3.getName(), sc3.getName(), host3, org.apache.ambari.server.state.State.INSTALLED.toString());
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(0, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), s3.getName(), sc3.getName(), host3, null);
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r1;
        org.apache.ambari.server.controller.ServiceComponentHostRequest r2;
        org.apache.ambari.server.controller.ServiceComponentHostRequest r3;
        r1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, null, host3, null);
        r2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), s3.getName(), sc3.getName(), host2, null);
        r3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(c1.getClusterName(), null, null, host2, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> reqs = new java.util.HashSet<>();
        reqs.addAll(java.util.Arrays.asList(r1, r2, r3));
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(reqs);
        org.junit.Assert.assertEquals(4, resps.size());
    }

    @org.junit.Test
    public void testGetHosts() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        final java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host4 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        setupClusterWithHosts(cluster1, "HDP-0.2", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        }, "centos5");
        setupClusterWithHosts(cluster2, "HDP-0.2", new java.util.ArrayList<java.lang.String>() {
            {
                add(host3);
            }
        }, "centos5");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(host4);
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host4), "redhat", "5.9");
        java.util.Map<java.lang.String, java.lang.String> attrs = new java.util.HashMap<>();
        attrs.put("a1", "b1");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setHostAttributes(attrs);
        attrs.put("a2", "b2");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host4).setHostAttributes(attrs);
        org.apache.ambari.server.controller.HostRequest r = new org.apache.ambari.server.controller.HostRequest(null, null);
        java.util.Set<org.apache.ambari.server.controller.HostResponse> resps = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        java.util.Set<java.lang.String> foundHosts = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.HostResponse resp : resps) {
            if (resp.getHostname().equals(host1)) {
                org.junit.Assert.assertEquals(cluster1, resp.getClusterName());
                org.junit.Assert.assertEquals(2, resp.getHostAttributes().size());
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, resp.getMaintenanceState());
                foundHosts.add(resp.getHostname());
            } else if (resp.getHostname().equals(host2)) {
                org.junit.Assert.assertEquals(cluster1, resp.getClusterName());
                org.junit.Assert.assertEquals(2, resp.getHostAttributes().size());
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, resp.getMaintenanceState());
                foundHosts.add(resp.getHostname());
            } else if (resp.getHostname().equals(host3)) {
                org.junit.Assert.assertEquals(cluster2, resp.getClusterName());
                org.junit.Assert.assertEquals(3, resp.getHostAttributes().size());
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, resp.getMaintenanceState());
                foundHosts.add(resp.getHostname());
            } else if (resp.getHostname().equals(host4)) {
                org.junit.Assert.assertEquals("", resp.getClusterName());
                org.junit.Assert.assertEquals(4, resp.getHostAttributes().size());
                org.junit.Assert.assertEquals(null, resp.getMaintenanceState());
                foundHosts.add(resp.getHostname());
            }
        }
        org.junit.Assert.assertEquals(4, foundHosts.size());
        r = new org.apache.ambari.server.controller.HostRequest(host1, null);
        resps = org.apache.ambari.server.controller.internal.HostResourceProviderTest.getHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
        org.apache.ambari.server.controller.HostResponse resp = resps.iterator().next();
        org.junit.Assert.assertEquals(host1, resp.getHostname());
        org.junit.Assert.assertEquals(cluster1, resp.getClusterName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, resp.getMaintenanceState());
        org.junit.Assert.assertEquals(2, resp.getHostAttributes().size());
    }

    @org.junit.Test
    public void testServiceUpdateBasic() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName = "HDFS";
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.2"));
        createService(cluster1, serviceName, org.apache.ambari.server.state.State.INIT);
        org.apache.ambari.server.state.Service s = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName);
        org.junit.Assert.assertNotNull(s);
        org.junit.Assert.assertEquals(serviceName, s.getName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, s.getDesiredState());
        org.junit.Assert.assertEquals(cluster1, s.getCluster().getClusterName());
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> reqs = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceRequest r;
        try {
            r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLING.toString(), null);
            reqs.clear();
            reqs.add(r);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
            org.junit.Assert.fail("Expected fail for invalid state transition");
        } catch (java.lang.Exception e) {
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
        reqs.clear();
        reqs.add(r);
        org.apache.ambari.server.controller.RequestStatusResponse trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
        org.junit.Assert.assertNull(trackAction);
    }

    @org.junit.Test
    public void testServiceUpdateInvalidRequest() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String cluster2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster2);
        java.lang.String serviceName1 = "HDFS";
        createService(cluster1, serviceName1, null);
        java.lang.String serviceName2 = "HBASE";
        java.lang.String serviceName3 = "HBASE";
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        try {
            createService(cluster2, serviceName3, repositoryVersion01, null);
            org.junit.Assert.fail("Expected fail for invalid service for stack 0.1");
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.2"));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster2).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.2"));
        createService(cluster1, serviceName2, null);
        createService(cluster2, serviceName3, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> reqs = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceRequest req1;
        org.apache.ambari.server.controller.ServiceRequest req2;
        try {
            reqs.clear();
            req1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
            req2 = new org.apache.ambari.server.controller.ServiceRequest(cluster2, serviceName2, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
            reqs.add(req1);
            reqs.add(req2);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
            org.junit.Assert.fail("Expected failure for multi cluster update");
        } catch (java.lang.Exception e) {
        }
        try {
            reqs.clear();
            req1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
            req2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
            reqs.add(req1);
            reqs.add(req2);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
            org.junit.Assert.fail("Expected failure for dups services");
        } catch (java.lang.Exception e) {
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName2).setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        try {
            reqs.clear();
            req1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
            req2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName2, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
            reqs.add(req1);
            reqs.add(req2);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
            org.junit.Assert.fail("Expected failure for different states");
        } catch (java.lang.Exception e) {
        }
    }

    @org.junit.Ignore("Something fishy with the stacks here that's causing the RCO to be loaded incorrectly")
    public void testServiceUpdateRecursive() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.2"));
        java.lang.String serviceName1 = "HDFS";
        createService(cluster1, serviceName1, repositoryVersion02, null);
        java.lang.String serviceName2 = "HBASE";
        createService(cluster1, serviceName2, repositoryVersion02, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HBASE_MASTER";
        java.lang.String componentName4 = "HDFS_CLIENT";
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponent(cluster1, serviceName1, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName2, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName4, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> set1 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceComponentHostRequest r1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host2, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r4 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host2, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r5 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName2, componentName3, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r6 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName4, host2, org.apache.ambari.server.state.State.INIT.toString());
        set1.add(r1);
        set1.add(r2);
        set1.add(r3);
        set1.add(r4);
        set1.add(r5);
        set1.add(r6);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s1 = c1.getService(serviceName1);
        org.apache.ambari.server.state.Service s2 = c1.getService(serviceName2);
        org.apache.ambari.server.state.ServiceComponent sc1 = s1.getServiceComponent(componentName1);
        org.apache.ambari.server.state.ServiceComponent sc2 = s1.getServiceComponent(componentName2);
        org.apache.ambari.server.state.ServiceComponent sc3 = s2.getServiceComponent(componentName3);
        org.apache.ambari.server.state.ServiceComponent sc4 = s1.getServiceComponent(componentName4);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc1.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = sc2.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch3 = sc1.getServiceComponentHost(host2);
        org.apache.ambari.server.state.ServiceComponentHost sch4 = sc2.getServiceComponentHost(host2);
        org.apache.ambari.server.state.ServiceComponentHost sch5 = sc3.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch6 = sc4.getServiceComponentHost(host2);
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        s2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc1.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sc2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sc3.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sc4.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch4.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch6.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch4.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch6.setState(org.apache.ambari.server.state.State.INSTALLED);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> reqs = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceRequest req1;
        org.apache.ambari.server.controller.ServiceRequest req2;
        try {
            req1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
            reqs.add(req1);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
            org.junit.Assert.fail("Expected failure for invalid state update");
        } catch (java.lang.Exception e) {
        }
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        s2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc1.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sc2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc3.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch4.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setState(org.apache.ambari.server.state.State.INIT);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INIT);
        sch4.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setState(org.apache.ambari.server.state.State.INSTALLED);
        try {
            reqs.clear();
            req1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
            reqs.add(req1);
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
            org.junit.Assert.fail("Expected failure for invalid state update");
        } catch (java.lang.Exception e) {
        }
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        s2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc1.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sc2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc3.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch1.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch2.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch3.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch4.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch5.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch1.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch4.setState(org.apache.ambari.server.state.State.STARTED);
        sch5.setState(org.apache.ambari.server.state.State.INSTALLED);
        reqs.clear();
        req1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
        req2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName2, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
        reqs.add(req1);
        reqs.add(req2);
        org.apache.ambari.server.controller.RequestStatusResponse trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, s1.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, s2.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sc1.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sc2.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sc3.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc4.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch1.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch2.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch3.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch4.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch5.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch6.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch1.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch2.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch3.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch4.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch5.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch6.getState());
        long requestId = trackAction.getRequestId();
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(requestId);
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.debug("Stage dump: {}", stage);
        }
        org.junit.Assert.assertTrue(!stages.isEmpty());
        org.junit.Assert.assertEquals(3, stages.size());
        org.apache.ambari.server.actionmanager.Stage stage1 = null;
        org.apache.ambari.server.actionmanager.Stage stage2 = null;
        org.apache.ambari.server.actionmanager.Stage stage3 = null;
        for (org.apache.ambari.server.actionmanager.Stage s : stages) {
            if (s.getStageId() == 0) {
                stage1 = s;
            }
            if (s.getStageId() == 1) {
                stage2 = s;
            }
            if (s.getStageId() == 2) {
                stage3 = s;
            }
        }
        org.junit.Assert.assertEquals(2, stage1.getExecutionCommands(host1).size());
        org.junit.Assert.assertEquals(1, stage1.getExecutionCommands(host2).size());
        org.junit.Assert.assertEquals(1, stage2.getExecutionCommands(host1).size());
        org.junit.Assert.assertNotNull(stage1.getExecutionCommandWrapper(host1, "NAMENODE"));
        org.junit.Assert.assertNotNull(stage1.getExecutionCommandWrapper(host1, "DATANODE"));
        org.junit.Assert.assertNotNull(stage1.getExecutionCommandWrapper(host2, "NAMENODE"));
        org.junit.Assert.assertNotNull(stage2.getExecutionCommandWrapper(host1, "HBASE_MASTER"));
        org.junit.Assert.assertNull(stage1.getExecutionCommandWrapper(host2, "DATANODE"));
        org.junit.Assert.assertNotNull(stage3.getExecutionCommandWrapper(host1, "HBASE_SERVICE_CHECK"));
        org.junit.Assert.assertNotNull(stage2.getExecutionCommandWrapper(host2, "HDFS_SERVICE_CHECK"));
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        for (org.apache.ambari.server.actionmanager.Stage s : stages) {
            for (java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> list : s.getExecutionCommands().values()) {
                for (org.apache.ambari.server.actionmanager.ExecutionCommandWrapper ecw : list) {
                    if (ecw.getExecutionCommand().getRole().contains("SERVICE_CHECK")) {
                        java.util.Map<java.lang.String, java.lang.String> hostParams = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(s.getHostParamsStage(), type);
                        org.junit.Assert.assertNotNull(hostParams);
                        org.junit.Assert.assertTrue(hostParams.size() > 0);
                        org.junit.Assert.assertTrue(hostParams.containsKey("stack_version"));
                        org.junit.Assert.assertEquals(hostParams.get("stack_version"), c1.getDesiredStackVersion().getStackVersion());
                    }
                }
            }
        }
        sch1.setState(org.apache.ambari.server.state.State.STARTED);
        sch2.setState(org.apache.ambari.server.state.State.STARTED);
        sch3.setState(org.apache.ambari.server.state.State.STARTED);
        sch4.setState(org.apache.ambari.server.state.State.STARTED);
        sch5.setState(org.apache.ambari.server.state.State.STARTED);
        reqs.clear();
        req1 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
        req2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName2, repositoryVersion02.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
        reqs.add(req1);
        reqs.add(req2);
        trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, mapRequestProps, true, false);
        org.junit.Assert.assertNull(trackAction);
    }

    @org.junit.Test
    public void testServiceComponentUpdateRecursive() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName1 = "HDFS";
        createService(cluster1, serviceName1, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName1, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> set1 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceComponentHostRequest r1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host2, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r4 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host2, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r5 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName3, host1, org.apache.ambari.server.state.State.INIT.toString());
        set1.add(r1);
        set1.add(r2);
        set1.add(r3);
        set1.add(r4);
        set1.add(r5);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s1 = c1.getService(serviceName1);
        org.apache.ambari.server.state.ServiceComponent sc1 = s1.getServiceComponent(componentName1);
        org.apache.ambari.server.state.ServiceComponent sc2 = s1.getServiceComponent(componentName2);
        org.apache.ambari.server.state.ServiceComponent sc3 = s1.getServiceComponent(componentName3);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc1.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = sc2.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch3 = sc1.getServiceComponentHost(host2);
        org.apache.ambari.server.state.ServiceComponentHost sch4 = sc2.getServiceComponentHost(host2);
        org.apache.ambari.server.state.ServiceComponentHost sch5 = sc3.getServiceComponentHost(host1);
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc1.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sc2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sc3.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch4.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.STARTED);
        sch4.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setState(org.apache.ambari.server.state.State.UNKNOWN);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> reqs = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceComponentRequest req1;
        org.apache.ambari.server.controller.ServiceComponentRequest req2;
        org.apache.ambari.server.controller.ServiceComponentRequest req3;
        req1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName1, sc3.getName(), org.apache.ambari.server.state.State.INSTALLED.toString());
        reqs.add(req1);
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, java.util.Collections.emptyMap(), true);
        try {
            reqs.clear();
            req1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName1, sc1.getName(), org.apache.ambari.server.state.State.INIT.toString());
            reqs.add(req1);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, java.util.Collections.emptyMap(), true);
            org.junit.Assert.fail("Expected failure for invalid state update");
        } catch (java.lang.Exception e) {
        }
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc1.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sc2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc3.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch3.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch4.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch5.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch1.setState(org.apache.ambari.server.state.State.INIT);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INIT);
        sch4.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setState(org.apache.ambari.server.state.State.INSTALLED);
        try {
            reqs.clear();
            req1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName1, sc1.getName(), org.apache.ambari.server.state.State.STARTED.toString());
            reqs.add(req1);
            org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, java.util.Collections.emptyMap(), true);
            org.junit.Assert.fail("Expected failure for invalid state update");
        } catch (java.lang.Exception e) {
        }
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc1.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sc2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sc3.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch3.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch4.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch5.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch1.setState(org.apache.ambari.server.state.State.STARTED);
        sch2.setState(org.apache.ambari.server.state.State.INIT);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch4.setState(org.apache.ambari.server.state.State.STARTED);
        sch5.setState(org.apache.ambari.server.state.State.INIT);
        reqs.clear();
        req1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName1, sc1.getName(), org.apache.ambari.server.state.State.INSTALLED.toString());
        req2 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName1, sc2.getName(), org.apache.ambari.server.state.State.INSTALLED.toString());
        req3 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName1, sc3.getName(), org.apache.ambari.server.state.State.INSTALLED.toString());
        reqs.add(req1);
        reqs.add(req2);
        reqs.add(req3);
        org.apache.ambari.server.controller.RequestStatusResponse trackAction = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, java.util.Collections.emptyMap(), true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, s1.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc1.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc2.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc3.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch1.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch2.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch3.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch4.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch5.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch1.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, sch2.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch3.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch4.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, sch5.getState());
        long requestId = trackAction.getRequestId();
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(requestId);
        org.junit.Assert.assertTrue(!stages.isEmpty());
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.debug("Stage dump: {}", stage);
        }
        sch1.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch4.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setState(org.apache.ambari.server.state.State.INSTALLED);
        reqs.clear();
        req1 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName1, sc1.getName(), org.apache.ambari.server.state.State.INSTALLED.toString());
        req2 = new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName1, sc2.getName(), org.apache.ambari.server.state.State.INSTALLED.toString());
        reqs.add(req1);
        reqs.add(req2);
        trackAction = org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, reqs, java.util.Collections.emptyMap(), true);
        org.junit.Assert.assertNull(trackAction);
    }

    @org.junit.Test
    public void testServiceComponentHostUpdateRecursive() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName1 = "HDFS";
        createService(cluster1, serviceName1, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName1, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> set1 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceComponentHostRequest r1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host2, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r4 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host2, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r5 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName3, host1, org.apache.ambari.server.state.State.INIT.toString());
        set1.add(r1);
        set1.add(r2);
        set1.add(r3);
        set1.add(r4);
        set1.add(r5);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s1 = c1.getService(serviceName1);
        org.apache.ambari.server.state.ServiceComponent sc1 = s1.getServiceComponent(componentName1);
        org.apache.ambari.server.state.ServiceComponent sc2 = s1.getServiceComponent(componentName2);
        org.apache.ambari.server.state.ServiceComponent sc3 = s1.getServiceComponent(componentName3);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc1.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = sc2.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch3 = sc1.getServiceComponentHost(host2);
        org.apache.ambari.server.state.ServiceComponentHost sch4 = sc2.getServiceComponentHost(host2);
        org.apache.ambari.server.state.ServiceComponentHost sch5 = sc3.getServiceComponentHost(host1);
        s1.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sc1.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sc2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sc3.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch3.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sch4.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setState(org.apache.ambari.server.state.State.INIT);
        sch2.setState(org.apache.ambari.server.state.State.INSTALL_FAILED);
        sch3.setState(org.apache.ambari.server.state.State.INIT);
        sch4.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.controller.ServiceComponentHostRequest req1;
        org.apache.ambari.server.controller.ServiceComponentHostRequest req2;
        org.apache.ambari.server.controller.ServiceComponentHostRequest req3;
        org.apache.ambari.server.controller.ServiceComponentHostRequest req4;
        org.apache.ambari.server.controller.ServiceComponentHostRequest req5;
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> reqs = new java.util.HashSet<>();
        try {
            reqs.clear();
            req1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host1, org.apache.ambari.server.state.State.INSTALLED.toString());
            req2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host2, org.apache.ambari.server.state.State.INSTALLED.toString());
            req3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host1, org.apache.ambari.server.state.State.INSTALLED.toString());
            req4 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host2, org.apache.ambari.server.state.State.INSTALLED.toString());
            req5 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName3, host1, org.apache.ambari.server.state.State.STARTED.toString());
            reqs.add(req1);
            reqs.add(req2);
            reqs.add(req3);
            reqs.add(req4);
            reqs.add(req5);
            updateHostComponents(reqs, java.util.Collections.emptyMap(), true);
        } catch (java.lang.Exception e) {
            org.junit.Assert.fail("Failure for invalid states");
        }
        reqs.clear();
        req1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, componentName1, host1, org.apache.ambari.server.state.State.INSTALLED.toString());
        req2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host2, org.apache.ambari.server.state.State.INSTALLED.toString());
        req3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, componentName2, host1, org.apache.ambari.server.state.State.INSTALLED.toString());
        req4 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host2, org.apache.ambari.server.state.State.INSTALLED.toString());
        req5 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName3, host1, org.apache.ambari.server.state.State.INSTALLED.toString());
        reqs.add(req1);
        reqs.add(req2);
        reqs.add(req3);
        reqs.add(req4);
        reqs.add(req5);
        org.apache.ambari.server.controller.RequestStatusResponse trackAction = updateHostComponents(reqs, java.util.Collections.emptyMap(), true);
        org.junit.Assert.assertNotNull(trackAction);
        long requestId = trackAction.getRequestId();
        org.junit.Assert.assertFalse(actionDB.getAllStages(requestId).isEmpty());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(requestId);
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.debug("Stage dump: {}", stage);
        }
        sch1.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch4.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch5.setState(org.apache.ambari.server.state.State.INSTALLED);
        reqs.clear();
        req1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host1, org.apache.ambari.server.state.State.INSTALLED.toString());
        req2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host2, org.apache.ambari.server.state.State.INSTALLED.toString());
        reqs.add(req1);
        reqs.add(req2);
        trackAction = updateHostComponents(reqs, java.util.Collections.emptyMap(), true);
        org.junit.Assert.assertNull(trackAction);
    }

    @org.junit.Test
    public void testCreateCustomActions() throws java.lang.Exception {
        final java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = "a" + org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = "b" + org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host3 = "c" + org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        setupClusterWithHosts(cluster1, "HDP-2.0.6", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
                add(host3);
            }
        }, "centos6");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        cluster.setCurrentStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.state.ConfigFactory cf = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config1 = cf.createNew(cluster, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = cf.createNew(cluster, "core-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config3 = cf.createNew(cluster, "yarn-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test.password", "supersecret");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion206;
        org.apache.ambari.server.state.Service hdfs = cluster.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service mapred = cluster.addService("YARN", repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        mapred.addServiceComponent(org.apache.ambari.server.Role.RESOURCEMANAGER.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(host2);
        java.lang.String actionDef1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String actionDef2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.customactions.ActionDefinition a1 = new org.apache.ambari.server.customactions.ActionDefinition(actionDef1, org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "test,[optional1]", "", "", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.SPECIFIC, java.lang.Integer.valueOf("100"), null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getAmbariMetaInfo().addActionDefinition(a1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getAmbariMetaInfo().addActionDefinition(new org.apache.ambari.server.customactions.ActionDefinition(actionDef2, org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "", "HDFS", "DATANODE", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ALL, java.lang.Integer.valueOf("1000"), null));
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
                put("pwd", "SECRET:yarn-site:1:test.password");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        requestProperties.put("datanode", "abc");
        java.util.ArrayList<java.lang.String> hosts = new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
            }
        };
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "DATANODE", hosts);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<>();
        resourceFilters.add(resourceFilter);
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, resourceFilters, null, params, false);
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(1, response.getTasks().size());
        org.apache.ambari.server.controller.ShortTaskStatus taskStatus = response.getTasks().get(0);
        org.junit.Assert.assertEquals(host1, taskStatus.getHostName());
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.apache.ambari.server.actionmanager.Stage stage = actionDB.getAllStages(response.getRequestId()).get(0);
        org.junit.Assert.assertNotNull(stage);
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.apache.ambari.server.actionmanager.HostRoleCommand task = storedTasks.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, task.getRoleCommand());
        org.junit.Assert.assertEquals(actionDef1, task.getRole().name());
        org.junit.Assert.assertEquals(host1, task.getHostName());
        org.apache.ambari.server.agent.ExecutionCommand cmd = task.getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertEquals(host1, cmd.getHostname());
        org.junit.Assert.assertFalse(cmd.getLocalComponents().isEmpty());
        org.junit.Assert.assertTrue(cmd.getLocalComponents().contains(org.apache.ambari.server.Role.DATANODE.name()));
        org.junit.Assert.assertTrue(cmd.getLocalComponents().contains(org.apache.ambari.server.Role.NAMENODE.name()));
        org.junit.Assert.assertTrue(cmd.getLocalComponents().contains(org.apache.ambari.server.Role.HDFS_CLIENT.name()));
        org.junit.Assert.assertFalse(cmd.getLocalComponents().contains(org.apache.ambari.server.Role.RESOURCEMANAGER.name()));
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        java.util.Map<java.lang.String, java.lang.String> hostParametersStage = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getHostParamsStage(), type);
        java.util.Map<java.lang.String, java.lang.String> commandParametersStage = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getCommandParamsStage(), type);
        org.junit.Assert.assertTrue(commandParametersStage.containsKey("test"));
        org.junit.Assert.assertTrue(commandParametersStage.containsKey("pwd"));
        org.junit.Assert.assertEquals(commandParametersStage.get("pwd"), "supersecret");
        org.junit.Assert.assertEquals("HDFS", cmd.getServiceName());
        org.junit.Assert.assertEquals("DATANODE", cmd.getComponentName());
        org.junit.Assert.assertNotNull(hostParametersStage.get("jdk_location"));
        org.junit.Assert.assertEquals("900", cmd.getCommandParams().get("command_timeout"));
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        a1.setDefaultTimeout(1800);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, resourceFilters, null, params, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks1 = actionDB.getRequestTasks(response.getRequestId());
        cmd = storedTasks1.get(0).getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertEquals("1800", cmd.getCommandParams().get("command_timeout"));
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef2, resourceFilters, null, params, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(2, response.getTasks().size());
        final java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks2 = actionDB.getRequestTasks(response.getRequestId());
        task = storedTasks2.get(1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, task.getRoleCommand());
        org.junit.Assert.assertEquals(actionDef2, task.getRole().name());
        java.util.HashSet<java.lang.String> expectedHosts = new java.util.HashSet<java.lang.String>() {
            {
                add(host2);
                add(host1);
            }
        };
        java.util.HashSet<java.lang.String> actualHosts = new java.util.HashSet<java.lang.String>() {
            {
                add(storedTasks2.get(1).getHostName());
                add(storedTasks2.get(0).getHostName());
            }
        };
        org.junit.Assert.assertEquals(expectedHosts, actualHosts);
        cmd = task.getExecutionCommandWrapper().getExecutionCommand();
        commandParametersStage = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getCommandParamsStage(), type);
        org.junit.Assert.assertTrue(commandParametersStage.containsKey("test"));
        org.junit.Assert.assertTrue(commandParametersStage.containsKey("pwd"));
        org.junit.Assert.assertEquals(commandParametersStage.get("pwd"), "supersecret");
        org.junit.Assert.assertEquals("HDFS", cmd.getServiceName());
        org.junit.Assert.assertEquals("DATANODE", cmd.getComponentName());
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        org.junit.Assert.assertEquals(host2, cmd.getHostname());
        org.junit.Assert.assertFalse(cmd.getLocalComponents().isEmpty());
        org.junit.Assert.assertTrue(cmd.getLocalComponents().contains(org.apache.ambari.server.Role.DATANODE.name()));
        org.junit.Assert.assertFalse(cmd.getLocalComponents().contains(org.apache.ambari.server.Role.NAMENODE.name()));
        org.junit.Assert.assertFalse(cmd.getLocalComponents().contains(org.apache.ambari.server.Role.HDFS_CLIENT.name()));
        org.junit.Assert.assertFalse(cmd.getLocalComponents().contains(org.apache.ambari.server.Role.RESOURCEMANAGER.name()));
        hosts = new java.util.ArrayList<java.lang.String>() {
            {
                add(host3);
            }
        };
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "", hosts);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, resourceFilters, null, params, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(1, response.getTasks().size());
        taskStatus = response.getTasks().get(0);
        org.junit.Assert.assertEquals(host3, taskStatus.getHostName());
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
    }

    @org.junit.Test
    public void testComponentCategorySentWithRestart() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        final java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        setupClusterWithHosts(cluster1, "HDP-2.0.7", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
            }
        }, "centos5");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"));
        cluster.setCurrentStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"));
        org.apache.ambari.server.state.ConfigFactory cf = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config1 = cf.createNew(cluster, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = cf.createNew(cluster, "core-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion207;
        org.apache.ambari.server.state.Service hdfs = cluster.addService("HDFS", repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(host1);
        installService(cluster1, "HDFS", false, false);
        startService(cluster1, "HDFS", false, false);
        org.apache.ambari.server.state.Cluster c = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = c.getService("HDFS");
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, s.getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : s.getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                if (sc.isClientComponent()) {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
                } else {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch.getDesiredState());
                }
            }
        }
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
            }
        };
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "HDFS_CLIENT", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
            }
        });
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "RESTART", params, false);
        actionRequest.getResourceFilters().add(resourceFilter);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        requestProperties.put("hdfs_client", "abc");
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(response.getRequestId());
        org.junit.Assert.assertNotNull(stages);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = null;
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand cmd : stage.getOrderedHostRoleCommands()) {
                if (cmd.getRole().equals(org.apache.ambari.server.Role.HDFS_CLIENT)) {
                    hrc = cmd;
                }
                java.util.Map<java.lang.String, java.lang.String> hostParamStage = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getHostParamsStage(), type);
                org.junit.Assert.assertTrue(hostParamStage.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.DB_DRIVER_FILENAME));
                org.junit.Assert.assertTrue(hostParamStage.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.MYSQL_JDBC_URL));
                org.junit.Assert.assertTrue(hostParamStage.containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.ORACLE_JDBC_URL));
            }
        }
        org.junit.Assert.assertNotNull(hrc);
        org.junit.Assert.assertEquals("RESTART HDFS/HDFS_CLIENT", hrc.getCommandDetail());
        java.util.Map<java.lang.String, java.lang.String> roleParams = hrc.getExecutionCommandWrapper().getExecutionCommand().getRoleParams();
        org.junit.Assert.assertNotNull(roleParams);
        org.junit.Assert.assertEquals("CLIENT", roleParams.get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMPONENT_CATEGORY));
        org.junit.Assert.assertTrue(hrc.getExecutionCommandWrapper().getExecutionCommand().getCommandParams().containsKey("hdfs_client"));
        org.junit.Assert.assertEquals("abc", hrc.getExecutionCommandWrapper().getExecutionCommand().getCommandParams().get("hdfs_client"));
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
    }

    @java.lang.SuppressWarnings("serial")
    @org.junit.Test
    public void testCreateActionsFailures() throws java.lang.Exception {
        final java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        setupClusterWithHosts(cluster1, "HDP-2.0.7", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
            }
        }, "centos5");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"));
        cluster.setCurrentStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"));
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion207;
        org.apache.ambari.server.state.ConfigFactory cf = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config1 = cf.createNew(cluster, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = cf.createNew(cluster, "core-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        cluster.addConfig(config1);
        cluster.addConfig(config2);
        cluster.addDesiredConfig("_test", java.util.Collections.singleton(config1));
        cluster.addDesiredConfig("_test", java.util.Collections.singleton(config2));
        org.apache.ambari.server.state.Service hdfs = cluster.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service hive = cluster.addService("HIVE", repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        hive.addServiceComponent(org.apache.ambari.server.Role.HIVE_SERVER.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(host1);
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
            }
        };
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", null, null);
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "NON_EXISTENT_CHECK", params, false);
        actionRequest.getResourceFilters().add(resourceFilter);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "Unsupported action");
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION_DATANODE", params, false);
        actionRequest.getResourceFilters().add(resourceFilter);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "Unsupported action DECOMMISSION_DATANODE for Service: HDFS and Component: null");
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "HDFS_CLIENT", null);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<>();
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "Unsupported action DECOMMISSION for Service: HDFS and Component: HDFS_CLIENT");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", null, null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, "DECOMMISSION_DATANODE", resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "Action DECOMMISSION_DATANODE does not exist");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("YARN", "RESOURCEMANAGER", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Service not found, clusterName=" + cluster1) + ", serviceName=YARN");
        java.util.Map<java.lang.String, java.lang.String> params2 = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("included_hosts", "h1,h2");
                put("excluded_hosts", "h1,h3");
            }
        };
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, null, params2, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "Same host cannot be specified for inclusion as well as exclusion. Hosts: [h1]");
        params2 = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("included_hosts", " h1,h2");
                put("excluded_hosts", "h4, h3");
                put("slave_type", "HDFS_CLIENT");
            }
        };
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, null, params2, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "Component HDFS_CLIENT is not supported for decommissioning.");
        java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();
        hosts.add("h6");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", hosts);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, null, params2, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "Decommission command cannot be issued with target host(s) specified.");
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).getServiceComponentHost(host1).setState(org.apache.ambari.server.state.State.INSTALLED);
        params2 = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("excluded_hosts", host1);
            }
        };
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, null, params2, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Component DATANODE on host " + host1) + " cannot be decommissioned as its not in STARTED state");
        params2 = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("excluded_hosts", "h1 ");
                put("mark_draining_only", "true");
            }
        };
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, resourceFilters, null, params2, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "mark_draining_only is not a valid parameter for NAMENODE");
        java.lang.String actionDef1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String actionDef2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String actionDef3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String actionDef4 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getAmbariMetaInfo().addActionDefinition(new org.apache.ambari.server.customactions.ActionDefinition(actionDef1, org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "test,dirName", "", "", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.SPECIFIC, java.lang.Integer.valueOf("100"), null));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getAmbariMetaInfo().addActionDefinition(new org.apache.ambari.server.customactions.ActionDefinition(actionDef2, org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "", "HDFS", "DATANODE", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("100"), null));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getAmbariMetaInfo().addActionDefinition(new org.apache.ambari.server.customactions.ActionDefinition("update_repo", org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "", "HDFS", "DATANODE", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("100"), null));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getAmbariMetaInfo().addActionDefinition(new org.apache.ambari.server.customactions.ActionDefinition(actionDef3, org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "", "MAPREDUCE", "MAPREDUCE_CLIENT", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("100"), null));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getAmbariMetaInfo().addActionDefinition(new org.apache.ambari.server.customactions.ActionDefinition(actionDef4, org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "", "HIVE", "", "Does file exist", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("100"), null));
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, null, null, null, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Action " + actionDef1) + " requires input 'test' that is not provided");
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, null, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Action " + actionDef1) + " requires input 'dirName' that is not provided");
        params.put("dirName", "dirName");
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, null, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Action " + actionDef1) + " requires explicit target host(s)");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HIVE", null, null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef2, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Action " + actionDef2) + " targets service HIVE that does not match with expected HDFS");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "HDFS_CLIENT", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef2, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Action " + actionDef2) + " targets component HDFS_CLIENT that does not match with expected DATANODE");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS2", "HDFS_CLIENT", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Service not found, clusterName=" + cluster1) + ", serviceName=HDFS2");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "HDFS_CLIENT2", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("ServiceComponent not found, clusterName=" + cluster1) + ", serviceName=HDFS, serviceComponentName=HDFS_CLIENT2");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("", "HDFS_CLIENT2", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef1, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Action " + actionDef1) + " targets component HDFS_CLIENT2 without specifying the target service");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("", "", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef3, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, ("Service not found, clusterName=" + cluster1) + ", serviceName=MAPREDUCE");
        hosts = new java.util.ArrayList<>();
        hosts.add("h6");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "", hosts);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef2, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, "Request specifies host h6 but it is not a valid host based on the target service=HDFS and component=DATANODE");
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HIVE", "", null);
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, actionDef4, resourceFilters, null, params, false);
        expectActionCreationErrorWithMessage(actionRequest, requestProperties, (("Suitable hosts not found, component=, service=HIVE, cluster=" + cluster1) + ", actionName=") + actionDef4);
    }

    private void expectActionCreationErrorWithMessage(org.apache.ambari.server.controller.ExecuteActionRequest actionRequest, java.util.Map<java.lang.String, java.lang.String> requestProperties, java.lang.String message) {
        try {
            org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
            org.junit.Assert.fail("createAction should fail");
        } catch (java.lang.Exception ex) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info(ex.getMessage());
            if (!ex.getMessage().contains(message)) {
                org.junit.Assert.fail(java.lang.String.format("Expected '%s' to contain '%s'", ex.getMessage(), message));
            }
        }
    }

    @java.lang.SuppressWarnings("serial")
    @org.junit.Test
    public void testCreateServiceCheckActions() throws java.lang.Exception {
        final java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        setupClusterWithHosts(cluster1, "HDP-0.1", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        }, "centos5");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        cluster.setCurrentStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion01;
        org.apache.ambari.server.state.ConfigFactory cf = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config1 = cf.createNew(cluster, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        config1.setPropertiesAttributes(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("attr1", new java.util.HashMap<>());
            }
        });
        org.apache.ambari.server.state.Config config2 = cf.createNew(cluster, "core-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        config2.setPropertiesAttributes(new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("attr2", new java.util.HashMap<>());
            }
        });
        cluster.addDesiredConfig("_test", java.util.Collections.singleton(config1));
        cluster.addDesiredConfig("_test", java.util.Collections.singleton(config2));
        org.apache.ambari.server.state.Service hdfs = cluster.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service mapReduce = cluster.addService("MAPREDUCE", repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name());
        mapReduce.addServiceComponent(org.apache.ambari.server.Role.MAPREDUCE_CLIENT.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(host1);
        mapReduce.getServiceComponent(org.apache.ambari.server.Role.MAPREDUCE_CLIENT.name()).addServiceComponentHost(host2);
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), params, false);
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", null, null);
        actionRequest.getResourceFilters().add(resourceFilter);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(1, response.getTasks().size());
        org.apache.ambari.server.controller.ShortTaskStatus task = response.getTasks().get(0);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.apache.ambari.server.actionmanager.Stage stage = actionDB.getAllStages(response.getRequestId()).get(0);
        org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.ExecutionCommandDAO.class);
        org.apache.ambari.server.orm.entities.ExecutionCommandEntity commandEntity = executionCommandDAO.findByPK(task.getTaskId());
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = gson.fromJson(new java.io.StringReader(new java.lang.String(commandEntity.getCommand())), org.apache.ambari.server.agent.ExecutionCommand.class);
        org.junit.Assert.assertTrue((executionCommand.getConfigurations() == null) || executionCommand.getConfigurations().isEmpty());
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = storedTasks.get(0);
        org.junit.Assert.assertEquals("SERVICE_CHECK HDFS", hostRoleCommand.getCommandDetail());
        org.junit.Assert.assertNull(hostRoleCommand.getCustomCommandName());
        org.junit.Assert.assertEquals(task.getTaskId(), hostRoleCommand.getTaskId());
        org.junit.Assert.assertNotNull(actionRequest.getResourceFilters());
        org.apache.ambari.server.controller.internal.RequestResourceFilter requestResourceFilter = actionRequest.getResourceFilters().get(0);
        org.junit.Assert.assertEquals(resourceFilter.getServiceName(), hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getServiceName());
        org.junit.Assert.assertEquals(actionRequest.getClusterName(), hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getClusterName());
        org.junit.Assert.assertEquals(actionRequest.getCommandName(), hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getRole());
        org.junit.Assert.assertEquals(org.apache.ambari.server.Role.HDFS_CLIENT.name(), hostRoleCommand.getEvent().getEvent().getServiceComponentName());
        org.junit.Assert.assertEquals(actionRequest.getParameters(), hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getRoleParams());
        org.junit.Assert.assertNotNull(hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getConfigurations());
        org.junit.Assert.assertEquals(0, hostRoleCommand.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().size());
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), stage.getRequestContext());
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, org.apache.ambari.server.Role.MAPREDUCE_SERVICE_CHECK.name(), null, false);
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("MAPREDUCE", null, null);
        actionRequest.getResourceFilters().add(resourceFilter);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.metadata.ActionMetadata.class).addServiceCheckAction("MAPREDUCE");
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(1, response.getTasks().size());
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> tasks = actionDB.getRequestTasks(response.getRequestId());
        org.junit.Assert.assertEquals(1, tasks.size());
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, null);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(1, response.getTasks().size());
        org.junit.Assert.assertEquals("", response.getRequestContext());
    }

    @org.junit.Test
    public void testUpdateConfigForRunningService() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName1).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host2));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host2));
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.toString(), null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            }
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.STARTED.toString(), null);
        requests.clear();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                sch.setState(org.apache.ambari.server.state.State.STARTED);
            }
        }
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            if (sc.getName().equals("HDFS_CLIENT")) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sc.getDesiredState());
            }
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                if (sch.getServiceComponentName().equals("HDFS_CLIENT")) {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
                } else {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch.getDesiredState());
                }
            }
        }
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        org.apache.ambari.server.controller.ConfigurationRequest cr3;
        org.apache.ambari.server.controller.ConfigurationRequest cr4;
        org.apache.ambari.server.controller.ConfigurationRequest cr5;
        org.apache.ambari.server.controller.ConfigurationRequest cr6;
        org.apache.ambari.server.controller.ConfigurationRequest cr7;
        org.apache.ambari.server.controller.ConfigurationRequest cr8;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeB", "v1", configs, null);
        cr3 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeC", "v1", configs, null);
        cr4 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeD", "v1", configs, null);
        cr5 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v2", configs, null);
        cr6 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeB", "v2", configs, null);
        cr7 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeC", "v2", configs, null);
        cr8 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeE", "v1", configs, null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr2);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr3);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr4);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr5);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr6);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr7);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr8);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent sc1 = s.getServiceComponent(componentName1);
        org.apache.ambari.server.state.ServiceComponent sc2 = s.getServiceComponent(componentName2);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc1.getServiceComponentHost(host1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> schReqs = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> scReqs = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> sReqs = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> configVersions = new java.util.HashMap<>();
        configVersions.clear();
        configVersions.put("typeA", "v1");
        configVersions.put("typeB", "v1");
        configVersions.put("typeC", "v1");
        schReqs.clear();
        schReqs.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, null));
        org.junit.Assert.assertNull(updateHostComponents(schReqs, java.util.Collections.emptyMap(), true));
        configVersions.clear();
        configVersions.put("typeC", "v1");
        configVersions.put("typeD", "v1");
        scReqs.clear();
        scReqs.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, componentName2, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, scReqs, java.util.Collections.emptyMap(), true));
        configVersions.clear();
        configVersions.put("typeA", "v2");
        configVersions.put("typeC", "v2");
        configVersions.put("typeE", "v1");
        sReqs.clear();
        sReqs.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), null, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, sReqs, mapRequestProps, true, false));
        configVersions.clear();
        configVersions.put("typeA", "v1");
        configVersions.put("typeB", "v1");
        configVersions.put("typeC", "v1");
        schReqs.clear();
        schReqs.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, null));
        org.junit.Assert.assertNull(updateHostComponents(schReqs, java.util.Collections.emptyMap(), true));
        configVersions.clear();
        configVersions.put("typeC", "v2");
        configVersions.put("typeD", "v1");
        scReqs.clear();
        scReqs.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, componentName1, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, scReqs, java.util.Collections.emptyMap(), true));
    }

    @org.junit.Test
    public void testConfigUpdates() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName1).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host2));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host2));
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes = new java.util.HashMap<>();
        configAttributes.put("final", new java.util.HashMap<>());
        configAttributes.get("final").put("a", "true");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        org.apache.ambari.server.controller.ConfigurationRequest cr3;
        org.apache.ambari.server.controller.ConfigurationRequest cr4;
        org.apache.ambari.server.controller.ConfigurationRequest cr5;
        org.apache.ambari.server.controller.ConfigurationRequest cr6;
        org.apache.ambari.server.controller.ConfigurationRequest cr7;
        org.apache.ambari.server.controller.ConfigurationRequest cr8;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v1", configs, configAttributes);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeB", "v1", configs, configAttributes);
        cr3 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeC", "v1", configs, configAttributes);
        cr4 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeD", "v1", configs, configAttributes);
        cr5 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v2", configs, configAttributes);
        cr6 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeB", "v2", configs, configAttributes);
        cr7 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeC", "v2", configs, configAttributes);
        cr8 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeE", "v1", configs, configAttributes);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr2);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr3);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr4);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr5);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr6);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr7);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr8);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent sc1 = s.getServiceComponent(componentName1);
        org.apache.ambari.server.state.ServiceComponent sc2 = s.getServiceComponent(componentName2);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc1.getServiceComponentHost(host1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> schReqs = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> scReqs = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> sReqs = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> configVersions = new java.util.HashMap<>();
        configVersions.clear();
        configVersions.put("typeA", "v1");
        configVersions.put("typeB", "v1");
        configVersions.put("typeC", "v1");
        schReqs.clear();
        schReqs.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, null));
        org.junit.Assert.assertNull(updateHostComponents(schReqs, java.util.Collections.emptyMap(), true));
        configVersions.clear();
        configVersions.put("typeC", "v1");
        configVersions.put("typeD", "v1");
        scReqs.clear();
        scReqs.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, componentName2, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, scReqs, java.util.Collections.emptyMap(), true));
        configVersions.clear();
        configVersions.put("typeA", "v2");
        configVersions.put("typeC", "v2");
        configVersions.put("typeE", "v1");
        sReqs.clear();
        sReqs.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), null, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, sReqs, mapRequestProps, true, false));
        configVersions.clear();
        configVersions.put("typeA", "v1");
        configVersions.put("typeB", "v1");
        configVersions.put("typeC", "v1");
        schReqs.clear();
        schReqs.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, null));
        org.junit.Assert.assertNull(updateHostComponents(schReqs, java.util.Collections.emptyMap(), true));
        configVersions.clear();
        configVersions.put("typeC", "v2");
        configVersions.put("typeD", "v1");
        scReqs.clear();
        scReqs.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, componentName1, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, scReqs, java.util.Collections.emptyMap(), true));
    }

    @org.junit.Test
    public void testReConfigureService() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.toString());
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            }
        }
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        org.apache.ambari.server.controller.ConfigurationRequest cr3;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        cr3 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version122", configs, null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr2);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr3);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent sc1 = s.getServiceComponent(componentName1);
        org.apache.ambari.server.state.ServiceComponent sc2 = s.getServiceComponent(componentName2);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc1.getServiceComponentHost(host1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> schReqs = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> scReqs = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> sReqs = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> configVersions = new java.util.HashMap<>();
        configVersions.clear();
        configVersions.put("core-site", "version1");
        configVersions.put("hdfs-site", "version1");
        schReqs.clear();
        schReqs.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, null));
        org.junit.Assert.assertNull(updateHostComponents(schReqs, java.util.Collections.emptyMap(), true));
        configVersions.clear();
        configVersions.put("core-site", "version122");
        schReqs.clear();
        schReqs.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, null));
        org.junit.Assert.assertNull(updateHostComponents(schReqs, java.util.Collections.emptyMap(), true));
        entityManager.clear();
        configVersions.clear();
        configVersions.put("core-site", "version1");
        configVersions.put("hdfs-site", "version1");
        scReqs.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, componentName2, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, scReqs, java.util.Collections.emptyMap(), true));
        scReqs.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, componentName1, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, scReqs, java.util.Collections.emptyMap(), true));
        configVersions.clear();
        configVersions.put("core-site", "version122");
        scReqs.clear();
        scReqs.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, componentName2, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, scReqs, java.util.Collections.emptyMap(), true));
        scReqs.clear();
        scReqs.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, serviceName, componentName1, null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.updateComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, scReqs, java.util.Collections.emptyMap(), true));
        entityManager.clear();
        configVersions.clear();
        configVersions.put("core-site", "version1");
        configVersions.put("hdfs-site", "version1");
        sReqs.clear();
        sReqs.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, sReqs, mapRequestProps, true, false));
        configVersions.clear();
        configVersions.put("core-site", "version122");
        sReqs.clear();
        sReqs.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), null));
        org.junit.Assert.assertNull(org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, sReqs, mapRequestProps, true, false));
        entityManager.clear();
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testReConfigureServiceClient() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName1 = "HDFS";
        java.lang.String serviceName2 = "MAPREDUCE";
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        java.lang.String componentName4 = "JOBTRACKER";
        java.lang.String componentName5 = "TASKTRACKER";
        java.lang.String componentName6 = "MAPREDUCE_CLIENT";
        createService(cluster1, serviceName1, repositoryVersion01, null);
        createService(cluster1, serviceName2, repositoryVersion01, null);
        createServiceComponent(cluster1, serviceName1, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName2, componentName4, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName2, componentName5, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName2, componentName6, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        addHostToCluster(host3, cluster1);
        createServiceComponentHost(cluster1, serviceName1, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName1, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName2, componentName4, host1, null);
        createServiceComponentHost(cluster1, serviceName2, componentName5, host1, null);
        createServiceComponentHost(cluster1, serviceName1, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName1, componentName3, host2, null);
        createServiceComponentHost(cluster1, serviceName2, componentName6, host2, null);
        createServiceComponentHost(cluster1, serviceName1, componentName3, host3, null);
        createServiceComponentHost(cluster1, serviceName2, componentName6, host3, null);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        java.util.Map<java.lang.String, java.lang.String> configs2 = new java.util.HashMap<>();
        configs2.put("c", "d");
        java.util.Map<java.lang.String, java.lang.String> configs3 = new java.util.HashMap<>();
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        org.apache.ambari.server.controller.ConfigurationRequest cr3;
        org.apache.ambari.server.controller.ConfigurationRequest cr4;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        cr4 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "kerberos-env", "version1", configs3, null);
        org.apache.ambari.server.state.ConfigFactory cf = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config1 = cf.createNew(cluster, "kerberos-env", "version1", new java.util.HashMap<>(), new java.util.HashMap<>());
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr2));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr4));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        long requestId1 = installService(cluster1, serviceName1, true, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(requestId1);
        installService(cluster1, serviceName2, false, false);
        startService(cluster1, serviceName1, true, false);
        startService(cluster1, serviceName2, true, false);
        cr3 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version122", configs2, null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr3));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        stopService(cluster1, serviceName1, false, false);
        stopService(cluster1, serviceName2, false, false);
        long requestId2 = startService(cluster1, serviceName1, true, true);
        long requestId3 = startService(cluster1, serviceName2, true, true);
        stages = new java.util.ArrayList<>();
        stages.addAll(actionDB.getAllStages(requestId2));
        stages.addAll(actionDB.getAllStages(requestId3));
        org.apache.ambari.server.actionmanager.HostRoleCommand hdfsCmdHost3 = null;
        org.apache.ambari.server.actionmanager.HostRoleCommand hdfsCmdHost2 = null;
        org.apache.ambari.server.actionmanager.HostRoleCommand mapRedCmdHost2 = null;
        org.apache.ambari.server.actionmanager.HostRoleCommand mapRedCmdHost3 = null;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hrcs = stage.getOrderedHostRoleCommands();
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : hrcs) {
                org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.debug("role: {}", hrc.getRole());
                if (hrc.getRole().toString().equals("HDFS_CLIENT")) {
                    if (hrc.getHostName().equals(host3)) {
                        hdfsCmdHost3 = hrc;
                    } else if (hrc.getHostName().equals(host2)) {
                        hdfsCmdHost2 = hrc;
                    }
                }
                if (hrc.getRole().toString().equals("MAPREDUCE_CLIENT")) {
                    if (hrc.getHostName().equals(host2)) {
                        mapRedCmdHost2 = hrc;
                    } else if (hrc.getHostName().equals(host3)) {
                        mapRedCmdHost3 = hrc;
                    }
                }
            }
        }
        org.junit.Assert.assertNotNull(hdfsCmdHost3);
        org.junit.Assert.assertNotNull(hdfsCmdHost2);
        org.apache.ambari.server.agent.ExecutionCommand execCmd = hdfsCmdHost3.getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertNotNull(mapRedCmdHost2);
        org.junit.Assert.assertNotNull(mapRedCmdHost3);
        stopService(cluster1, serviceName1, false, false);
        stopService(cluster1, serviceName2, false, false);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        requestId3 = startService(cluster1, serviceName2, true, true);
        stages = actionDB.getAllStages(requestId3);
        org.apache.ambari.server.actionmanager.HostRoleCommand clientWithHostDown = null;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getRole().toString().equals("MAPREDUCE_CLIENT") && hrc.getHostName().equals(host2)) {
                    clientWithHostDown = hrc;
                }
            }
        }
        org.junit.Assert.assertNull(clientWithHostDown);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService("MAPREDUCE").getServiceComponent("TASKTRACKER").getServiceComponentHost(host1).getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService("HDFS").getServiceComponent("NAMENODE").getServiceComponentHost(host1).getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService("HDFS").getServiceComponent("DATANODE").getServiceComponentHost(host1).getState());
    }

    @org.junit.Test
    public void testReconfigureClientWithServiceStarted() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        java.util.Map<java.lang.String, java.lang.String> configs2 = new java.util.HashMap<>();
        configs2.put("c", "d");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        org.apache.ambari.server.controller.ConfigurationRequest cr3;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr2));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        installService(cluster1, serviceName, false, false);
        startService(cluster1, serviceName, false, false);
        org.apache.ambari.server.state.Cluster c = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = c.getService(serviceName);
        stopServiceComponentHosts(cluster1, serviceName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, s.getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : s.getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
            }
        }
        cr3 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version122", configs2, null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr3));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        long id = startService(cluster1, serviceName, false, true);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(id);
        org.apache.ambari.server.actionmanager.HostRoleCommand clientHrc = null;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getHostName().equals(host2) && hrc.getRole().toString().equals("HDFS_CLIENT")) {
                    clientHrc = hrc;
                }
            }
        }
        org.junit.Assert.assertNotNull(clientHrc);
    }

    @org.junit.Test
    public void testClientServiceSmokeTests() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName = "PIG";
        createService(cluster1, serviceName, repositoryVersion01, null);
        java.lang.String componentName1 = "PIG";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, null, componentName1, host2, null);
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.toString());
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.RequestStatusResponse trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
            org.junit.Assert.assertFalse(sc.isRecoveryEnabled());
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, sch.getState());
            }
        }
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> taskStatuses = trackAction.getTasks();
        org.junit.Assert.assertEquals(2, taskStatuses.size());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(trackAction.getRequestId());
        org.junit.Assert.assertEquals(1, stages.size());
        org.junit.Assert.assertEquals("Called from a test", stages.get(0).getRequestContext());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            sc.setRecoveryEnabled(true);
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            }
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.STARTED.toString());
        requests.clear();
        requests.add(r);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.metadata.ActionMetadata.class).addServiceCheckAction("PIG");
        trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertNotNull(trackAction);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
            org.junit.Assert.assertTrue(sc.isRecoveryEnabled());
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getState());
            }
        }
        stages = actionDB.getAllStages(trackAction.getRequestId());
        for (org.apache.ambari.server.actionmanager.Stage s : stages) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info("Stage dump : " + s);
        }
        org.junit.Assert.assertEquals(1, stages.size());
        taskStatuses = trackAction.getTasks();
        org.junit.Assert.assertEquals(1, taskStatuses.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.Role.PIG_SERVICE_CHECK.toString(), taskStatuses.get(0).getRole());
    }

    @org.junit.Test
    public void testSkipTaskOnUnhealthyHosts() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        addHostToCluster(host3, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host3, null);
        installService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        long requestId = startService(cluster1, serviceName, true, false);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = actionDB.getRequestTasks(requestId);
        org.junit.Assert.assertEquals(3, commands.size());
        int commandCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if (command.getRoleCommand() == org.apache.ambari.server.RoleCommand.START) {
                org.junit.Assert.assertTrue(command.getHostName().equals(host1) || command.getHostName().equals(host2));
                commandCount++;
            }
        }
        org.junit.Assert.assertEquals("Expect only two task.", 2, commandCount);
        stopService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1).setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        requestId = startService(cluster1, serviceName, true, false);
        commands = actionDB.getRequestTasks(requestId);
        commandCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if (command.getRoleCommand() == org.apache.ambari.server.RoleCommand.START) {
                org.junit.Assert.assertTrue(command.getHostName().equals(host3));
                commandCount++;
            }
        }
        org.junit.Assert.assertEquals("Expect only one task.", 1, commandCount);
        stopService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        requestId = startService(cluster1, serviceName, true, false);
        commands = actionDB.getRequestTasks(requestId);
        commandCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if (command.getRoleCommand() == org.apache.ambari.server.RoleCommand.START) {
                org.junit.Assert.assertTrue((command.getHostName().equals(host3) || command.getHostName().equals(host2)) || command.getHostName().equals(host1));
                commandCount++;
            }
        }
        org.junit.Assert.assertEquals("Expect all three task.", 3, commandCount);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        requestId = stopService(cluster1, serviceName, false, false);
        commands = actionDB.getRequestTasks(requestId);
        org.junit.Assert.assertEquals(2, commands.size());
        commandCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if (command.getRoleCommand() == org.apache.ambari.server.RoleCommand.STOP) {
                org.junit.Assert.assertTrue(command.getHostName().equals(host3) || command.getHostName().equals(host1));
                commandCount++;
            }
        }
        org.junit.Assert.assertEquals("Expect only two task.", 2, commandCount);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent sc3 = s.getServiceComponent(componentName2);
        for (org.apache.ambari.server.state.ServiceComponentHost sch : sc3.getServiceComponentHosts().values()) {
            if (sch.getHostName().equals(host3)) {
                sch.setState(org.apache.ambari.server.state.State.INSTALL_FAILED);
            }
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        requestId = installService(cluster1, serviceName, false, false);
        org.junit.Assert.assertEquals(-1, requestId);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        requestId = installService(cluster1, serviceName, false, false);
        commands = actionDB.getRequestTasks(requestId);
        org.junit.Assert.assertEquals(1, commands.size());
        commandCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if (command.getRoleCommand() == org.apache.ambari.server.RoleCommand.INSTALL) {
                org.junit.Assert.assertTrue(command.getHostName().equals(host3));
                commandCount++;
            }
        }
        org.junit.Assert.assertEquals("Expect only one task.", 1, commandCount);
    }

    @org.junit.Test
    public void testServiceCheckWhenHostIsUnhealthy() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        addHostToCluster(host3, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host3, null);
        installService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setState(org.apache.ambari.server.state.HostState.UNHEALTHY);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        long requestId = startService(cluster1, serviceName, true, false);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = actionDB.getRequestTasks(requestId);
        int commandCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if ((command.getRoleCommand() == org.apache.ambari.server.RoleCommand.SERVICE_CHECK) && (command.getRole() == org.apache.ambari.server.Role.HDFS_SERVICE_CHECK)) {
                org.junit.Assert.assertTrue(command.getHostName().equals(host2));
                commandCount++;
            }
        }
        org.junit.Assert.assertEquals("Expect only one service check.", 1, commandCount);
        stopService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        requestId = startService(cluster1, serviceName, true, false);
        commands = actionDB.getRequestTasks(requestId);
        commandCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if ((command.getRoleCommand() == org.apache.ambari.server.RoleCommand.SERVICE_CHECK) && (command.getRole() == org.apache.ambari.server.Role.HDFS_SERVICE_CHECK)) {
                org.junit.Assert.assertTrue(command.getHostName().equals(host3));
                commandCount++;
            }
        }
        org.junit.Assert.assertEquals("Expect only one service check.", 1, commandCount);
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", null, null);
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), null, false);
        actionRequest.getResourceFilters().add(resourceFilter);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        commands = actionDB.getRequestTasks(response.getRequestId());
        commandCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if ((command.getRoleCommand() == org.apache.ambari.server.RoleCommand.SERVICE_CHECK) && (command.getRole() == org.apache.ambari.server.Role.HDFS_SERVICE_CHECK)) {
                org.junit.Assert.assertTrue(command.getHostName().equals(host3));
                commandCount++;
            }
        }
        org.junit.Assert.assertEquals("Expect only one service check.", 1, commandCount);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setState(org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host2).setState(org.apache.ambari.server.state.HostState.INIT);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
            org.junit.Assert.assertTrue("Exception should have been raised.", false);
        } catch (java.lang.Exception e) {
            org.junit.Assert.assertTrue(e.getMessage().contains("there were no healthy eligible hosts"));
        }
    }

    @org.junit.Test
    public void testReInstallForInstallFailedClient() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        addHostToCluster(host3, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host3, null);
        installService(cluster1, serviceName, false, false);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.apache.ambari.server.state.ServiceComponent sc3 = s.getServiceComponent(componentName3);
        for (org.apache.ambari.server.state.ServiceComponentHost sch : sc3.getServiceComponentHosts().values()) {
            if (sch.getHostName().equals(host3)) {
                sch.setState(org.apache.ambari.server.state.State.INSTALL_FAILED);
            }
        }
        long requestId = startService(cluster1, serviceName, false, true);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(requestId);
        org.apache.ambari.server.actionmanager.HostRoleCommand clientReinstallCmd = null;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getHostName().equals(host3) && hrc.getRole().toString().equals("HDFS_CLIENT")) {
                    clientReinstallCmd = hrc;
                    break;
                }
            }
        }
        org.junit.Assert.assertNotNull(clientReinstallCmd);
    }

    @org.junit.Test
    public void testReInstallClientComponent() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        addHostToCluster(host3, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host3, null);
        installService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.ServiceComponentHostRequest schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName3, host3, org.apache.ambari.server.state.State.INSTALLED.name());
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> setReqs = new java.util.HashSet<>();
        setReqs.add(schr);
        org.apache.ambari.server.controller.RequestStatusResponse resp = updateHostComponents(setReqs, java.util.Collections.emptyMap(), false);
        org.junit.Assert.assertNotNull(resp);
        org.junit.Assert.assertTrue(resp.getRequestId() > 0);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(resp.getRequestId());
        org.apache.ambari.server.actionmanager.HostRoleCommand clientReinstallCmd = null;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getHostName().equals(host3) && hrc.getRole().toString().equals("HDFS_CLIENT")) {
                    clientReinstallCmd = hrc;
                    break;
                }
            }
        }
        org.junit.Assert.assertNotNull(clientReinstallCmd);
    }

    @org.junit.Test
    public void testReInstallClientComponentFromServiceChange() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName, host2, null);
        installService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.ServiceRequest sr = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion206.getId(), org.apache.ambari.server.state.State.STARTED.name());
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> setReqs = new java.util.HashSet<>();
        setReqs.add(sr);
        org.apache.ambari.server.controller.RequestStatusResponse resp = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, setReqs, java.util.Collections.emptyMap(), false, true);
        org.junit.Assert.assertNotNull(resp);
        org.junit.Assert.assertTrue(resp.getRequestId() > 0);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(resp.getRequestId());
        java.util.Map<java.lang.String, org.apache.ambari.server.Role> hostsToRoles = new java.util.HashMap<>();
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                hostsToRoles.put(hrc.getHostName(), hrc.getRole());
            }
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.Role> expectedHostsToRoles = new java.util.HashMap<>();
        expectedHostsToRoles.put(host1, org.apache.ambari.server.Role.HDFS_CLIENT);
        expectedHostsToRoles.put(host2, org.apache.ambari.server.Role.HDFS_CLIENT);
        org.junit.Assert.assertEquals(expectedHostsToRoles, hostsToRoles);
    }

    @org.junit.Test
    public void testDecommissonDatanodeAction() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        final java.lang.String host1 = "d" + org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = "e" + org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        org.apache.ambari.server.controller.internal.RequestOperationLevel level = new org.apache.ambari.server.controller.internal.RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, cluster1, null, null, null);
        installService(cluster1, serviceName, false, false);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        startService(cluster1, serviceName, false, false);
        cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, s.getDesiredState());
        org.apache.ambari.server.state.ServiceComponentHost scHost = s.getServiceComponent("DATANODE").getServiceComponentHost(host2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE, scHost.getComponentAdminState());
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
                put("excluded_hosts", host2);
                put("align_maintenance_state", "true");
            }
        };
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", null);
        java.util.ArrayList<org.apache.ambari.server.controller.internal.RequestResourceFilter> filters = new java.util.ArrayList<>();
        filters.add(resourceFilter);
        org.apache.ambari.server.controller.ExecuteActionRequest request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, filters, level, params, false);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.apache.ambari.server.agent.ExecutionCommand execCmd = storedTasks.get(0).getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertNotNull(storedTasks);
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED, scHost.getComponentAdminState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, scHost.getMaintenanceState());
        org.apache.ambari.server.actionmanager.HostRoleCommand command = storedTasks.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.Role.NAMENODE, command.getRole());
        org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, command.getRoleCommand());
        org.junit.Assert.assertEquals("DECOMMISSION", execCmd.getCommandParams().get("custom_command"));
        org.junit.Assert.assertEquals(host2, execCmd.getCommandParams().get("all_decommissioned_hosts"));
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
                put("excluded_hosts", host1);
                put("align_maintenance_state", "true");
            }
        };
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", null);
        filters = new java.util.ArrayList<>();
        filters.add(resourceFilter);
        request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, filters, level, params, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        storedTasks = actionDB.getRequestTasks(response.getRequestId());
        execCmd = storedTasks.get(0).getExecutionCommandWrapper().getExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> cmdParams = execCmd.getCommandParams();
        org.junit.Assert.assertTrue(cmdParams.containsKey("update_files_only"));
        org.junit.Assert.assertTrue(cmdParams.get("update_files_only").equals("false"));
        org.junit.Assert.assertNotNull(storedTasks);
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.DECOMMISSIONED, scHost.getComponentAdminState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, scHost.getMaintenanceState());
        org.junit.Assert.assertEquals("DECOMMISSION", execCmd.getCommandParams().get("custom_command"));
        org.junit.Assert.assertTrue(execCmd.getCommandParams().get("all_decommissioned_hosts").contains(host1));
        org.junit.Assert.assertTrue(execCmd.getCommandParams().get("all_decommissioned_hosts").contains(host2));
        org.junit.Assert.assertTrue(execCmd.getCommandParams().get("all_decommissioned_hosts").equals((host1 + ",") + host2) || execCmd.getCommandParams().get("all_decommissioned_hosts").equals((host2 + ",") + host1));
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        createServiceComponentHost(cluster1, serviceName, componentName1, host2, null);
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host2, org.apache.ambari.server.state.State.INSTALLED.toString());
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        updateHostComponents(requests, java.util.Collections.emptyMap(), true);
        s.getServiceComponent(componentName1).getServiceComponentHost(host2).setState(org.apache.ambari.server.state.State.INSTALLED);
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host2, org.apache.ambari.server.state.State.STARTED.toString());
        requests.clear();
        requests.add(r);
        updateHostComponents(requests, java.util.Collections.emptyMap(), true);
        s.getServiceComponent(componentName1).getServiceComponentHost(host2).setState(org.apache.ambari.server.state.State.STARTED);
        params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
                put("included_hosts", (host1 + " , ") + host2);
                put("align_maintenance_state", "true");
            }
        };
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", null);
        filters = new java.util.ArrayList<>();
        filters.add(resourceFilter);
        request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", null, filters, level, params, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.junit.Assert.assertNotNull(storedTasks);
        scHost = s.getServiceComponent("DATANODE").getServiceComponentHost(host2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE, scHost.getComponentAdminState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, scHost.getMaintenanceState());
        execCmd = storedTasks.get(0).getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertNotNull(storedTasks);
        org.junit.Assert.assertEquals(2, storedTasks.size());
        int countRefresh = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : storedTasks) {
            org.junit.Assert.assertEquals("DECOMMISSION", hrc.getCustomCommandName());
            org.junit.Assert.assertTrue(hrc.getCommandDetail().contains("DECOMMISSION, Included: "));
            org.junit.Assert.assertTrue(hrc.getCommandDetail().contains(host1));
            org.junit.Assert.assertTrue(hrc.getCommandDetail().contains(host2));
            cmdParams = hrc.getExecutionCommandWrapper().getExecutionCommand().getCommandParams();
            if ((!cmdParams.containsKey("update_files_only")) || (!cmdParams.get("update_files_only").equals("true"))) {
                countRefresh++;
            }
            org.junit.Assert.assertEquals("", cmdParams.get("all_decommissioned_hosts"));
        }
        org.junit.Assert.assertEquals(2, countRefresh);
        scHost.setComponentAdminState(null);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostComponentAdminState.INSERVICE, scHost.getComponentAdminState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, scHost.getMaintenanceState());
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
    }

    @org.junit.Test
    public void testResourceFiltersWithCustomActions() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        setupClusterWithHosts(cluster1, "HDP-2.0.6", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
                add(host3);
            }
        }, "centos6");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        cluster.setCurrentStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion206;
        org.apache.ambari.server.state.ConfigFactory cf = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        cf.createNew(cluster, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        cf.createNew(cluster, "core-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Service hdfs = cluster.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service mapred = cluster.addService("YARN", repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        mapred.addServiceComponent(org.apache.ambari.server.Role.RESOURCEMANAGER.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(host2);
        java.lang.String action1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getAmbariMetaInfo().addActionDefinition(new org.apache.ambari.server.customactions.ActionDefinition(action1, org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "", "HDFS", "", "Some custom action.", org.apache.ambari.server.actionmanager.TargetHostType.ALL, java.lang.Integer.valueOf("10010"), null));
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<>();
        java.util.ArrayList<java.lang.String> hosts = new java.util.ArrayList<java.lang.String>() {
            {
                add(host2);
            }
        };
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter1 = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "DATANODE", hosts);
        hosts = new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
            }
        };
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter2 = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", hosts);
        resourceFilters.add(resourceFilter1);
        resourceFilters.add(resourceFilter2);
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, action1, resourceFilters, null, params, false);
        org.apache.ambari.server.controller.RequestStatusResponse response = null;
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        } catch (java.lang.Exception ae) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info("Expected exception.", ae);
            org.junit.Assert.assertTrue(ae.getMessage().contains("Custom action definition only " + "allows one resource filter to be specified"));
        }
        resourceFilters.remove(resourceFilter1);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, null, action1, resourceFilters, null, params, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(1, response.getTasks().size());
        org.apache.ambari.server.actionmanager.HostRoleCommand nnCommand = null;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : actionDB.getRequestTasks(response.getRequestId())) {
            if (hrc.getHostName().equals(host1)) {
                nnCommand = hrc;
            }
        }
        org.junit.Assert.assertNotNull(nnCommand);
        org.apache.ambari.server.agent.ExecutionCommand cmd = nnCommand.getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertEquals(action1, cmd.getRole());
        org.junit.Assert.assertEquals("10010", cmd.getCommandParams().get("command_timeout"));
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        for (org.apache.ambari.server.actionmanager.Stage stage : actionDB.getAllStages(response.getRequestId())) {
            java.util.Map<java.lang.String, java.lang.String> commandParamsStage = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getCommandParamsStage(), type);
            org.junit.Assert.assertTrue(commandParamsStage.containsKey("test"));
        }
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
    }

    @org.junit.Test
    public void testResourceFiltersWithCustomCommands() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        setupClusterWithHosts(cluster1, "HDP-2.0.6", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
                add(host3);
            }
        }, "centos6");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        cluster.setCurrentStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion206;
        org.apache.ambari.server.state.ConfigFactory cf = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        org.apache.ambari.server.state.Config config1 = cf.createNew(cluster, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config2 = cf.createNew(cluster, "core-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("key1", "value1");
            }
        }, new java.util.HashMap<>());
        org.apache.ambari.server.state.Service hdfs = cluster.addService("HDFS", repositoryVersion);
        org.apache.ambari.server.state.Service mapred = cluster.addService("YARN", repositoryVersion);
        hdfs.addServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.NAMENODE.name());
        hdfs.addServiceComponent(org.apache.ambari.server.Role.DATANODE.name());
        mapred.addServiceComponent(org.apache.ambari.server.Role.RESOURCEMANAGER.name());
        hdfs.getServiceComponent(org.apache.ambari.server.Role.HDFS_CLIENT.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.NAMENODE.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(host1);
        hdfs.getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).addServiceComponentHost(host2);
        mapred.getServiceComponent(org.apache.ambari.server.Role.RESOURCEMANAGER.name()).addServiceComponentHost(host2);
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        requestProperties.put("command_retry_enabled", "true");
        requestProperties.put("log_output", "false");
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<>();
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", org.apache.ambari.server.Role.DATANODE.name(), new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        });
        resourceFilters.add(resourceFilter);
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("YARN", org.apache.ambari.server.Role.RESOURCEMANAGER.name(), new java.util.ArrayList<java.lang.String>() {
            {
                add(host2);
            }
        });
        resourceFilters.add(resourceFilter);
        org.apache.ambari.server.controller.ExecuteActionRequest request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "RESTART", null, resourceFilters, null, params, false);
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        org.junit.Assert.assertEquals(3, response.getTasks().size());
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.junit.Assert.assertNotNull(storedTasks);
        int expectedRestartCount = 0;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : storedTasks) {
            org.junit.Assert.assertEquals("RESTART", hrc.getCustomCommandName());
            java.util.Map<java.lang.String, java.lang.String> cParams = hrc.getExecutionCommandWrapper().getExecutionCommand().getCommandParams();
            org.junit.Assert.assertEquals("Expect retry to be set", true, cParams.containsKey("command_retry_enabled"));
            org.junit.Assert.assertEquals("Expect max duration to be set", true, cParams.containsKey("max_duration_for_retries"));
            org.junit.Assert.assertEquals("Expect max duration to be 600", "600", cParams.get("max_duration_for_retries"));
            org.junit.Assert.assertEquals("Expect retry to be true", "true", cParams.get("command_retry_enabled"));
            org.junit.Assert.assertEquals("Expect log_output to be set", true, cParams.containsKey("log_output"));
            org.junit.Assert.assertEquals("Expect log_output to be false", "false", cParams.get("log_output"));
            if (hrc.getHostName().equals(host1) && hrc.getRole().equals(org.apache.ambari.server.Role.DATANODE)) {
                expectedRestartCount++;
            } else if (hrc.getHostName().equals(host2)) {
                if (hrc.getRole().equals(org.apache.ambari.server.Role.DATANODE)) {
                    expectedRestartCount++;
                } else if (hrc.getRole().equals(org.apache.ambari.server.Role.RESOURCEMANAGER)) {
                    expectedRestartCount++;
                }
            }
        }
        org.junit.Assert.assertEquals("Restart 2 datanodes and 1 Resourcemanager.", 3, expectedRestartCount);
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        requestProperties.put("max_duration_for_retries", "423");
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        org.junit.Assert.assertEquals(3, response.getTasks().size());
        storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.junit.Assert.assertNotNull(storedTasks);
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : storedTasks) {
            org.junit.Assert.assertEquals("RESTART", hrc.getCustomCommandName());
            java.util.Map<java.lang.String, java.lang.String> cParams = hrc.getExecutionCommandWrapper().getExecutionCommand().getCommandParams();
            org.junit.Assert.assertEquals("Expect retry to be set", true, cParams.containsKey("command_retry_enabled"));
            org.junit.Assert.assertEquals("Expect max duration to be set", true, cParams.containsKey("max_duration_for_retries"));
            org.junit.Assert.assertEquals("Expect max duration to be 423", "423", cParams.get("max_duration_for_retries"));
            org.junit.Assert.assertEquals("Expect retry to be true", "true", cParams.get("command_retry_enabled"));
        }
        requestProperties.remove("max_duration_for_retries");
        requestProperties.remove("command_retry_enabled");
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        org.junit.Assert.assertEquals(3, response.getTasks().size());
        storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.junit.Assert.assertNotNull(storedTasks);
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : storedTasks) {
            org.junit.Assert.assertEquals("RESTART", hrc.getCustomCommandName());
            java.util.Map<java.lang.String, java.lang.String> cParams = hrc.getExecutionCommandWrapper().getExecutionCommand().getCommandParams();
            org.junit.Assert.assertEquals("Expect retry to be set", false, cParams.containsKey("command_retry_enabled"));
            org.junit.Assert.assertEquals("Expect max duration to be set", false, cParams.containsKey("max_duration_for_retries"));
        }
        resourceFilters.clear();
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", null, new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
            }
        });
        resourceFilters.add(resourceFilter);
        request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), null, resourceFilters, null, null, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        org.junit.Assert.assertEquals(1, response.getTasks().size());
        storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.junit.Assert.assertNotNull(storedTasks);
        org.junit.Assert.assertEquals(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), storedTasks.get(0).getRole().name());
        org.junit.Assert.assertEquals(host1, storedTasks.get(0).getHostName());
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, cluster.getService("HDFS").getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).getServiceComponentHost(host1).getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, cluster.getService("HDFS").getServiceComponent(org.apache.ambari.server.Role.DATANODE.name()).getServiceComponentHost(host2).getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, cluster.getService("YARN").getServiceComponent(org.apache.ambari.server.Role.RESOURCEMANAGER.name()).getServiceComponentHost(host2).getDesiredState());
    }

    @org.junit.Test
    public void testConfigsAttachedToServiceChecks() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr2));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        installService(cluster1, serviceName, false, false);
        long requestId = startService(cluster1, serviceName, true, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(requestId);
        boolean serviceCheckFound = false;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getRole().equals(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK)) {
                    serviceCheckFound = true;
                }
            }
        }
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        for (org.apache.ambari.server.actionmanager.Stage stage : actionDB.getAllStages(requestId)) {
            java.util.Map<java.lang.String, java.lang.String> hostParamsStage = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getHostParamsStage(), type);
            org.junit.Assert.assertNotNull(hostParamsStage.get("jdk_location"));
        }
        org.junit.Assert.assertEquals(true, serviceCheckFound);
    }

    @org.junit.Test
    @org.junit.Ignore("Unsuported feature !")
    public void testConfigsAttachedToServiceNotCluster() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createConfiguration(cr2);
        java.util.Map<java.lang.String, java.lang.String> configVersions = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("core-site", "version1");
                put("hdfs-site", "version1");
            }
        };
        org.apache.ambari.server.controller.ServiceRequest sr = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), null);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(sr), new java.util.HashMap<>(), false, false);
        installService(cluster1, serviceName, false, false);
        long requestId = startService(cluster1, serviceName, true, false);
        org.junit.Assert.assertEquals(0, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getDesiredConfigs().size());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(requestId);
        boolean serviceCheckFound = false;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getRole().equals(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK)) {
                    serviceCheckFound = true;
                }
            }
        }
        org.junit.Assert.assertEquals(true, serviceCheckFound);
    }

    @org.junit.Test
    public void testHostLevelParamsSentWithCommands() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        java.lang.String serviceName = "PIG";
        createService(cluster1, serviceName, repositoryVersion01, null);
        java.lang.String componentName1 = "PIG";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, null, componentName1, host2, null);
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.toString());
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.RequestStatusResponse trackAction = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(trackAction.getRequestId());
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            java.util.Map<java.lang.String, java.lang.String> params = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getHostParamsStage(), type);
            org.junit.Assert.assertEquals("0.1", params.get("stack_version"));
            org.junit.Assert.assertNotNull(params.get("jdk_location"));
            org.junit.Assert.assertNotNull(params.get("db_name"));
            org.junit.Assert.assertNotNull(params.get("mysql_jdbc_url"));
            org.junit.Assert.assertNotNull(params.get("oracle_jdbc_url"));
        }
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = stages.get(0).getOrderedHostRoleCommands().get(0).getExecutionCommandWrapper().getExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> paramsCmd = executionCommand.getHostLevelParams();
        org.junit.Assert.assertNotNull(executionCommand.getRepositoryFile());
        org.junit.Assert.assertNotNull(paramsCmd.get("clientsToUpdateConfigs"));
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testConfigGroupOverridesWithHostActions() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        java.lang.String serviceName1 = "HDFS";
        java.lang.String serviceName2 = "MAPREDUCE2";
        createService(cluster1, serviceName1, repositoryVersion206, null);
        createService(cluster1, serviceName2, repositoryVersion206, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        java.lang.String componentName4 = "HISTORYSERVER";
        createServiceComponent(cluster1, serviceName1, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName2, componentName4, org.apache.ambari.server.state.State.INIT);
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        addHostToCluster(host3, cluster1);
        createServiceComponentHost(cluster1, serviceName1, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName1, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName1, componentName3, host2, null);
        createServiceComponentHost(cluster1, serviceName1, componentName3, host3, null);
        createServiceComponentHost(cluster1, serviceName2, componentName4, host3, null);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        org.apache.ambari.server.controller.ConfigurationRequest cr3;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        cr3 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "mapred-site", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr2));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr3));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        java.lang.String group1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String tag1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String group2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String tag2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.ConfigFactory configFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        configs = new java.util.HashMap<>();
        configs.put("a", "c");
        cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        final org.apache.ambari.server.state.Config config = configFactory.createReadOnly("core-site", "version122", configs, null);
        java.lang.Long groupId = createConfigGroup(cluster, serviceName1, group1, tag1, new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
            }
        }, new java.util.ArrayList<org.apache.ambari.server.state.Config>() {
            {
                add(config);
            }
        });
        org.junit.Assert.assertNotNull(groupId);
        configs = new java.util.HashMap<>();
        configs.put("a", "c");
        final org.apache.ambari.server.state.Config config2 = configFactory.createReadOnly("mapred-site", "version122", configs, null);
        groupId = createConfigGroup(cluster, serviceName2, group2, tag2, new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
            }
        }, new java.util.ArrayList<org.apache.ambari.server.state.Config>() {
            {
                add(config2);
            }
        });
        org.junit.Assert.assertNotNull(groupId);
        java.lang.Long requestId = installService(cluster1, serviceName1, false, false);
        org.apache.ambari.server.actionmanager.HostRoleCommand namenodeInstall = null;
        org.apache.ambari.server.actionmanager.HostRoleCommand clientInstall = null;
        org.apache.ambari.server.actionmanager.HostRoleCommand slaveInstall = null;
        for (org.apache.ambari.server.actionmanager.Stage stage : actionDB.getAllStages(requestId)) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getRole().equals(org.apache.ambari.server.Role.NAMENODE) && hrc.getHostName().equals(host1)) {
                    namenodeInstall = hrc;
                } else if (hrc.getRole().equals(org.apache.ambari.server.Role.HDFS_CLIENT) && hrc.getHostName().equals(host3)) {
                    clientInstall = hrc;
                } else if (hrc.getRole().equals(org.apache.ambari.server.Role.DATANODE) && hrc.getHostName().equals(host2)) {
                    slaveInstall = hrc;
                }
            }
        }
        org.junit.Assert.assertNotNull(namenodeInstall);
        org.junit.Assert.assertNotNull(clientInstall);
        org.junit.Assert.assertNotNull(slaveInstall);
        org.junit.Assert.assertTrue(namenodeInstall.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("core-site").containsKey("a"));
        org.junit.Assert.assertEquals("c", namenodeInstall.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("core-site").get("a"));
        org.junit.Assert.assertTrue(clientInstall.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("core-site").containsKey("a"));
        org.junit.Assert.assertEquals("b", clientInstall.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("core-site").get("a"));
        org.junit.Assert.assertTrue(slaveInstall.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("core-site").containsKey("a"));
        org.junit.Assert.assertEquals("b", slaveInstall.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("core-site").get("a"));
        startService(cluster1, serviceName1, false, false);
        requestId = installService(cluster1, serviceName2, false, false);
        org.apache.ambari.server.actionmanager.HostRoleCommand mapredInstall = null;
        for (org.apache.ambari.server.actionmanager.Stage stage : actionDB.getAllStages(requestId)) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getRole().equals(org.apache.ambari.server.Role.HISTORYSERVER) && hrc.getHostName().equals(host3)) {
                    mapredInstall = hrc;
                }
            }
        }
        org.junit.Assert.assertNotNull(mapredInstall);
        org.junit.Assert.assertEquals("b", mapredInstall.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("mapred-site").get("a"));
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = cluster.getConfigGroups().get(groupId);
        configGroup.setHosts(new java.util.HashMap<java.lang.Long, org.apache.ambari.server.state.Host>() {
            {
                put(3L, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3));
            }
        });
        requestId = startService(cluster1, serviceName2, false, false);
        mapredInstall = null;
        for (org.apache.ambari.server.actionmanager.Stage stage : actionDB.getAllStages(requestId)) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getRole().equals(org.apache.ambari.server.Role.HISTORYSERVER) && hrc.getHostName().equals(host3)) {
                    mapredInstall = hrc;
                }
            }
        }
        org.junit.Assert.assertNotNull(mapredInstall);
        org.junit.Assert.assertEquals("c", mapredInstall.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("mapred-site").get("a"));
    }

    @org.junit.Test
    public void testConfigGroupOverridesWithDecommissionDatanode() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.7"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        installService(cluster1, serviceName, false, false);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        startService(cluster1, serviceName, false, false);
        configs = new java.util.HashMap<>();
        configs.put("a", "c");
        java.lang.String group1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String tag1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.ConfigFactory configFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        final org.apache.ambari.server.state.Config config = configFactory.createReadOnly("hdfs-site", "version122", configs, null);
        java.lang.Long groupId = createConfigGroup(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1), serviceName, group1, tag1, new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        }, new java.util.ArrayList<org.apache.ambari.server.state.Config>() {
            {
                add(config);
            }
        });
        org.junit.Assert.assertNotNull(groupId);
        cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, s.getDesiredState());
        java.util.Map<java.lang.String, java.lang.String> params = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test", "test");
                put("excluded_hosts", host1);
            }
        };
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", null);
        org.apache.ambari.server.controller.ExecuteActionRequest request = new org.apache.ambari.server.controller.ExecuteActionRequest(cluster1, "DECOMMISSION", params, false);
        request.getResourceFilters().add(resourceFilter);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(request, requestProperties);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.apache.ambari.server.agent.ExecutionCommand execCmd = storedTasks.get(0).getExecutionCommandWrapper().getExecutionCommand();
        org.junit.Assert.assertNotNull(storedTasks);
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.apache.ambari.server.actionmanager.HostRoleCommand command = storedTasks.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.Role.NAMENODE, command.getRole());
        org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, command.getRoleCommand());
        org.junit.Assert.assertEquals("DECOMMISSION", execCmd.getCommandParams().get("custom_command"));
        org.junit.Assert.assertEquals(host1, execCmd.getCommandParams().get("all_decommissioned_hosts"));
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testConfigGroupOverridesWithServiceCheckActions() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("a", "b");
        org.apache.ambari.server.controller.ConfigurationRequest cr1;
        org.apache.ambari.server.controller.ConfigurationRequest cr2;
        cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", configs, null);
        cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr2));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        installService(cluster1, serviceName, false, false);
        java.lang.String group1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String tag1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        configs = new java.util.HashMap<>();
        configs.put("a", "c");
        org.apache.ambari.server.state.ConfigFactory configFactory = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        final org.apache.ambari.server.state.Config config = configFactory.createReadOnly("hdfs-site", "version122", configs, null);
        java.lang.Long groupId = createConfigGroup(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1), serviceName, group1, tag1, new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        }, new java.util.ArrayList<org.apache.ambari.server.state.Config>() {
            {
                add(config);
            }
        });
        org.junit.Assert.assertNotNull(groupId);
        long requestId = startService(cluster1, serviceName, true, false);
        org.apache.ambari.server.actionmanager.HostRoleCommand smokeTestCmd = null;
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(requestId);
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : stage.getOrderedHostRoleCommands()) {
                if (hrc.getRole().equals(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK)) {
                    smokeTestCmd = hrc;
                }
            }
        }
        org.junit.Assert.assertNotNull(smokeTestCmd);
        org.junit.Assert.assertEquals("c", smokeTestCmd.getExecutionCommandWrapper().getExecutionCommand().getConfigurations().get("hdfs-site").get("a"));
    }

    @org.junit.Test
    public void testGetStacks() throws java.lang.Exception {
        java.util.HashSet<java.lang.String> availableStacks = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.StackInfo stackInfo : org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.getStacks()) {
            availableStacks.add(stackInfo.getName());
        }
        org.apache.ambari.server.controller.StackRequest request = new org.apache.ambari.server.controller.StackRequest(null);
        java.util.Set<org.apache.ambari.server.controller.StackResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStacks(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(availableStacks.size(), responses.size());
        org.apache.ambari.server.controller.StackRequest requestWithParams = new org.apache.ambari.server.controller.StackRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME);
        java.util.Set<org.apache.ambari.server.controller.StackResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStacks(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getStackName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME);
        }
        org.apache.ambari.server.controller.StackRequest invalidRequest = new org.apache.ambari.server.controller.StackRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStacks(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetStackVersions() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackVersionRequest request = new org.apache.ambari.server.controller.StackVersionRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, null);
        java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackVersions(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSIONS_CNT, responses.size());
        org.apache.ambari.server.controller.StackVersionRequest requestWithParams = new org.apache.ambari.server.controller.StackVersionRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION);
        java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackVersions(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackVersionResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getStackVersion(), org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION);
        }
        org.apache.ambari.server.controller.StackVersionRequest invalidRequest = new org.apache.ambari.server.controller.StackVersionRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackVersions(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
        requestWithParams = new org.apache.ambari.server.controller.StackVersionRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.1.1");
        responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackVersions(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        org.apache.ambari.server.controller.StackVersionResponse resp = responsesWithParams.iterator().next();
        org.junit.Assert.assertNotNull(resp.getUpgradePacks());
        org.junit.Assert.assertTrue(resp.getUpgradePacks().size() > 0);
        org.junit.Assert.assertTrue(resp.getUpgradePacks().contains("upgrade_test"));
    }

    @org.junit.Test
    public void testGetStackVersionActiveAttr() throws java.lang.Exception {
        for (org.apache.ambari.server.state.StackInfo stackInfo : org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.getStacks(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME)) {
            if (stackInfo.getVersion().equalsIgnoreCase(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION)) {
                stackInfo.setActive(true);
            }
        }
        org.apache.ambari.server.controller.StackVersionRequest requestWithParams = new org.apache.ambari.server.controller.StackVersionRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION);
        java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackVersions(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackVersionResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertTrue(responseWithParams.isActive());
        }
    }

    @org.junit.Test
    public void testGetRepositories() throws java.lang.Exception {
        org.apache.ambari.server.controller.RepositoryRequest request = new org.apache.ambari.server.controller.RepositoryRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.OS_TYPE, null, org.apache.ambari.server.controller.AmbariManagementControllerTest.REPO_NAME);
        java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRepositories(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.AmbariManagementControllerTest.REPOS_CNT, responses.size());
        org.apache.ambari.server.controller.RepositoryRequest requestWithParams = new org.apache.ambari.server.controller.RepositoryRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.OS_TYPE, org.apache.ambari.server.controller.AmbariManagementControllerTest.REPO_ID, org.apache.ambari.server.controller.AmbariManagementControllerTest.REPO_NAME);
        requestWithParams.setClusterVersionId(525L);
        java.util.Set<org.apache.ambari.server.controller.RepositoryResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRepositories(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.RepositoryResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getRepoId(), org.apache.ambari.server.controller.AmbariManagementControllerTest.REPO_ID);
            org.junit.Assert.assertEquals(525L, responseWithParams.getClusterVersionId().longValue());
        }
        org.apache.ambari.server.controller.RepositoryRequest invalidRequest = new org.apache.ambari.server.controller.RepositoryRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.OS_TYPE, org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE, org.apache.ambari.server.controller.AmbariManagementControllerTest.REPO_NAME);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRepositories(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetStackServices() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceRequest request = new org.apache.ambari.server.controller.StackServiceRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, null);
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackServices(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(12, responses.size());
        org.apache.ambari.server.controller.StackServiceRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME);
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackServices(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackServiceResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getServiceName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME);
            org.junit.Assert.assertTrue(responseWithParams.getConfigTypes().size() > 0);
        }
        org.apache.ambari.server.controller.StackServiceRequest invalidRequest = new org.apache.ambari.server.controller.StackServiceRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackServices(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testConfigInComponent() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.0.6", "YARN");
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackServices(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackServiceResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getServiceName(), "YARN");
            org.junit.Assert.assertTrue(responseWithParams.getConfigTypes().containsKey("capacity-scheduler"));
        }
    }

    @org.junit.Test
    public void testGetStackConfigurations() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackConfigurationRequest request = new org.apache.ambari.server.controller.StackConfigurationRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, null);
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackConfigurations(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_PROPERTIES_CNT, responses.size());
        org.apache.ambari.server.controller.StackConfigurationRequest requestWithParams = new org.apache.ambari.server.controller.StackConfigurationRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.PROPERTY_NAME);
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackConfigurations(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackConfigurationResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getPropertyName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.PROPERTY_NAME);
        }
        org.apache.ambari.server.controller.StackConfigurationRequest invalidRequest = new org.apache.ambari.server.controller.StackConfigurationRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackConfigurations(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetStackComponents() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest request = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, null);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_COMPONENTS_CNT, responses.size());
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME);
        }
        org.apache.ambari.server.controller.StackServiceComponentRequest invalidRequest = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetStackOperatingSystems() throws java.lang.Exception {
        org.apache.ambari.server.controller.OperatingSystemRequest request = new org.apache.ambari.server.controller.OperatingSystemRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, null);
        java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getOperatingSystems(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.AmbariManagementControllerTest.OS_CNT, responses.size());
        org.apache.ambari.server.controller.OperatingSystemRequest requestWithParams = new org.apache.ambari.server.controller.OperatingSystemRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.OS_TYPE);
        java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getOperatingSystems(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.OperatingSystemResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getOsType(), org.apache.ambari.server.controller.AmbariManagementControllerTest.OS_TYPE);
        }
        org.apache.ambari.server.controller.OperatingSystemRequest invalidRequest = new org.apache.ambari.server.controller.OperatingSystemRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getOperatingSystems(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.StackAccessException e) {
        }
    }

    @org.junit.Test
    public void testGetStackOperatingSystemsWithRepository() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION);
        org.junit.Assert.assertNotNull(stackEntity);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity versionEntity = dao.create(stackEntity, "0.2.2", "HDP-0.2", org.apache.ambari.server.controller.internal.ClusterStackVersionResourceProviderTest.REPO_OS_ENTITIES);
        org.apache.ambari.server.controller.OperatingSystemRequest request = new org.apache.ambari.server.controller.OperatingSystemRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, null);
        java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getOperatingSystems(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.AmbariManagementControllerTest.OS_CNT, responses.size());
        org.apache.ambari.server.controller.OperatingSystemRequest requestWithParams = new org.apache.ambari.server.controller.OperatingSystemRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.OS_TYPE);
        requestWithParams.setVersionDefinitionId(versionEntity.getId().toString());
        java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getOperatingSystems(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
    }

    @org.junit.Test
    public void testStackServiceCheckSupported() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceRequest hdfsServiceRequest = new org.apache.ambari.server.controller.StackServiceRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.0.8", org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME);
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackServices(java.util.Collections.singleton(hdfsServiceRequest));
        org.junit.Assert.assertEquals(1, responses.size());
        org.apache.ambari.server.controller.StackServiceResponse response = responses.iterator().next();
        org.junit.Assert.assertTrue(response.isServiceCheckSupported());
        org.apache.ambari.server.controller.StackServiceRequest fakeServiceRequest = new org.apache.ambari.server.controller.StackServiceRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.0.8", org.apache.ambari.server.controller.AmbariManagementControllerTest.FAKE_SERVICE_NAME);
        responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackServices(java.util.Collections.singleton(fakeServiceRequest));
        org.junit.Assert.assertEquals(1, responses.size());
        response = responses.iterator().next();
        org.junit.Assert.assertFalse(response.isServiceCheckSupported());
    }

    @org.junit.Test
    public void testStackServiceComponentCustomCommands() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest namenodeRequest = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(namenodeRequest));
        org.junit.Assert.assertEquals(1, responses.size());
        org.apache.ambari.server.controller.StackServiceComponentResponse response = responses.iterator().next();
        org.junit.Assert.assertNotNull(response.getCustomCommands());
        org.junit.Assert.assertEquals(2, response.getCustomCommands().size());
        org.junit.Assert.assertEquals("DECOMMISSION", response.getCustomCommands().get(0));
        org.junit.Assert.assertEquals("REBALANCEHDFS", response.getCustomCommands().get(1));
        org.apache.ambari.server.controller.StackServiceComponentRequest journalNodeRequest = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, "JOURNALNODE");
        responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(journalNodeRequest));
        org.junit.Assert.assertEquals(1, responses.size());
        response = responses.iterator().next();
        org.junit.Assert.assertNotNull(response.getCustomCommands());
        org.junit.Assert.assertEquals(0, response.getCustomCommands().size());
    }

    @org.junit.Test
    public void testDecommissionAllowed() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME_HBASE, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_REGIONSERVER);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_REGIONSERVER);
            org.junit.Assert.assertTrue(responseWithParams.isDecommissionAlllowed());
        }
    }

    @org.junit.Test
    public void testDecommissionAllowedInheritance() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_DATANODE);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_DATANODE);
            org.junit.Assert.assertTrue(responseWithParams.isDecommissionAlllowed());
        }
    }

    @org.junit.Test
    public void testDecommissionAllowedOverwrite() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.0.5", org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME_YARN, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_NODEMANAGER);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_NODEMANAGER);
            org.junit.Assert.assertFalse(responseWithParams.isDecommissionAlllowed());
        }
        requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME_YARN, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_NODEMANAGER);
        responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_NODEMANAGER);
            org.junit.Assert.assertTrue(responseWithParams.isDecommissionAlllowed());
        }
    }

    @org.junit.Test
    public void testRassignAllowed() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.0.5", org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME);
            org.junit.Assert.assertTrue(responseWithParams.isReassignAlllowed());
        }
        requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.0.5", org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_DATANODE);
        responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_DATANODE);
            org.junit.Assert.assertFalse(responseWithParams.isReassignAlllowed());
        }
    }

    @org.junit.Test
    public void testReassignAllowedInheritance() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME_HIVE, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_HIVE_METASTORE);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_HIVE_METASTORE);
            org.junit.Assert.assertTrue(responseWithParams.isReassignAlllowed());
        }
    }

    @org.junit.Test
    public void testReassignAllowedOverwrite() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.0.5", org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME_HIVE, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_HIVE_SERVER);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_HIVE_SERVER);
            org.junit.Assert.assertTrue(responseWithParams.isReassignAlllowed());
        }
        requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME_HIVE, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_HIVE_SERVER);
        responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_HIVE_SERVER);
            org.junit.Assert.assertFalse(responseWithParams.isReassignAlllowed());
        }
    }

    @org.junit.Test
    public void testBulkCommandsInheritence() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_DATANODE);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_DATANODE);
            org.junit.Assert.assertEquals(responseWithParams.getBulkCommandsDisplayName(), "DataNodes");
            org.junit.Assert.assertEquals(responseWithParams.getBulkCommandsMasterComponentName(), "NAMENODE");
        }
    }

    @org.junit.Test
    public void testBulkCommandsChildStackOverride() throws java.lang.Exception {
        org.apache.ambari.server.controller.StackServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, "2.0.5", org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME_HBASE, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_REGIONSERVER);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getBulkCommandsDisplayName(), "Region Servers");
            org.junit.Assert.assertEquals(responseWithParams.getBulkCommandsMasterComponentName(), "HBASE_MASTER");
        }
        requestWithParams = new org.apache.ambari.server.controller.StackServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.NEW_STACK_VERSION, org.apache.ambari.server.controller.AmbariManagementControllerTest.SERVICE_NAME_HBASE, org.apache.ambari.server.controller.AmbariManagementControllerTest.COMPONENT_NAME_REGIONSERVER);
        responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getStackComponents(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.StackServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getBulkCommandsDisplayName(), "HBase Region Servers");
            org.junit.Assert.assertEquals(responseWithParams.getBulkCommandsMasterComponentName(), "HBASE_MASTER");
        }
    }

    @org.junit.Test
    public void testUpdateClusterUpgradabilityCheck() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.StackId currentStackId = new org.apache.ambari.server.state.StackId("HDP-0.2");
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster c = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        c.setDesiredStackVersion(currentStackId);
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(c.getClusterId(), cluster1, "HDP-0.3", null);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(r), mapRequestProps);
        } catch (java.lang.Exception e) {
            org.junit.Assert.assertTrue(e.getMessage().contains("Illegal request to upgrade to"));
        }
        org.apache.ambari.server.state.StackId unsupportedStackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        c.setDesiredStackVersion(unsupportedStackId);
        c.setCurrentStackVersion(unsupportedStackId);
        c.refresh();
        r = new org.apache.ambari.server.controller.ClusterRequest(c.getClusterId(), cluster1, "HDP-0.2", null);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(r), mapRequestProps);
        } catch (java.lang.Exception e) {
            org.junit.Assert.assertTrue(e.getMessage().contains("Upgrade is not allowed from"));
        }
    }

    private void validateGeneratedStages(java.util.List<org.apache.ambari.server.actionmanager.Stage> stages, int expectedStageCount, org.apache.ambari.server.controller.AmbariManagementControllerTest.ExpectedUpgradeTasks expectedTasks) {
        org.junit.Assert.assertEquals(expectedStageCount, stages.size());
        int prevRoleOrder = -1;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            int currRoleOrder = -1;
            for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                if (command.getRole() == org.apache.ambari.server.Role.AMBARI_SERVER_ACTION) {
                    org.junit.Assert.assertTrue(command.toString(), expectedTasks.isTaskExpected(command.getRole()));
                    currRoleOrder = expectedTasks.getRoleOrder(command.getRole());
                    org.apache.ambari.server.agent.ExecutionCommand execCommand = command.getExecutionCommandWrapper().getExecutionCommand();
                    org.junit.Assert.assertTrue(execCommand.getRoleParams().containsKey(org.apache.ambari.server.serveraction.ServerAction.ACTION_NAME));
                    org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.EXECUTE, execCommand.getRoleCommand());
                } else {
                    org.junit.Assert.assertTrue(command.toString(), expectedTasks.isTaskExpected(command.getRole(), command.getHostName()));
                    currRoleOrder = expectedTasks.getRoleOrder(command.getRole());
                    org.apache.ambari.server.agent.ExecutionCommand execCommand = command.getExecutionCommandWrapper().getExecutionCommand();
                    org.junit.Assert.assertTrue(execCommand.getCommandParams().containsKey("source_stack_version"));
                    org.junit.Assert.assertTrue(execCommand.getCommandParams().containsKey("target_stack_version"));
                    org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.UPGRADE, execCommand.getRoleCommand());
                }
            }
            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = stage.getOrderedHostRoleCommands();
            org.junit.Assert.assertTrue(commands.size() > 0);
            org.apache.ambari.server.Role role = commands.get(0).getRole();
            for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
                org.junit.Assert.assertTrue("All commands must be for the same role", role.equals(command.getRole()));
            }
            org.junit.Assert.assertTrue("Roles must be in order", currRoleOrder > prevRoleOrder);
            prevRoleOrder = currRoleOrder;
        }
    }

    class ExpectedUpgradeTasks {
        private static final int ROLE_COUNT = 25;

        private static final java.lang.String DEFAULT_HOST = "default_host";

        private java.util.ArrayList<java.util.Map<java.lang.String, java.lang.Boolean>> expectedList;

        private java.util.Map<org.apache.ambari.server.Role, java.lang.Integer> roleToIndex;

        public ExpectedUpgradeTasks(java.util.List<java.lang.String> hosts) {
            roleToIndex = new java.util.HashMap<>();
            expectedList = new java.util.ArrayList<>(org.apache.ambari.server.controller.AmbariManagementControllerTest.ExpectedUpgradeTasks.ROLE_COUNT);
            fillRoleToIndex();
            fillExpectedHosts(hosts);
        }

        public void expectTask(org.apache.ambari.server.Role role, java.lang.String host) {
            expectedList.get(roleToIndex.get(role)).put(host, true);
        }

        public void expectTask(org.apache.ambari.server.Role role) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, role);
            expectTask(role, org.apache.ambari.server.controller.AmbariManagementControllerTest.ExpectedUpgradeTasks.DEFAULT_HOST);
        }

        public boolean isTaskExpected(org.apache.ambari.server.Role role, java.lang.String host) {
            return expectedList.get(roleToIndex.get(role)).get(host);
        }

        public boolean isTaskExpected(org.apache.ambari.server.Role role) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, role);
            return isTaskExpected(role, org.apache.ambari.server.controller.AmbariManagementControllerTest.ExpectedUpgradeTasks.DEFAULT_HOST);
        }

        public int getRoleOrder(org.apache.ambari.server.Role role) {
            return roleToIndex.get(role);
        }

        public void resetAll() {
            for (org.apache.ambari.server.Role role : roleToIndex.keySet()) {
                java.util.Map<java.lang.String, java.lang.Boolean> hostState = expectedList.get(roleToIndex.get(role));
                for (java.lang.String host : hostState.keySet()) {
                    hostState.put(host, false);
                }
            }
        }

        private void fillExpectedHosts(java.util.List<java.lang.String> hosts) {
            for (int index = 0; index < org.apache.ambari.server.controller.AmbariManagementControllerTest.ExpectedUpgradeTasks.ROLE_COUNT; index++) {
                java.util.Map<java.lang.String, java.lang.Boolean> hostState = new java.util.HashMap<>();
                for (java.lang.String host : hosts) {
                    hostState.put(host, false);
                }
                expectedList.add(hostState);
            }
        }

        private void fillRoleToIndex() {
            roleToIndex.put(org.apache.ambari.server.Role.NAMENODE, 0);
            roleToIndex.put(org.apache.ambari.server.Role.SECONDARY_NAMENODE, 1);
            roleToIndex.put(org.apache.ambari.server.Role.DATANODE, 2);
            roleToIndex.put(org.apache.ambari.server.Role.HDFS_CLIENT, 3);
            roleToIndex.put(org.apache.ambari.server.Role.JOBTRACKER, 4);
            roleToIndex.put(org.apache.ambari.server.Role.TASKTRACKER, 5);
            roleToIndex.put(org.apache.ambari.server.Role.MAPREDUCE_CLIENT, 6);
            roleToIndex.put(org.apache.ambari.server.Role.ZOOKEEPER_SERVER, 7);
            roleToIndex.put(org.apache.ambari.server.Role.ZOOKEEPER_CLIENT, 8);
            roleToIndex.put(org.apache.ambari.server.Role.HBASE_MASTER, 9);
            roleToIndex.put(org.apache.ambari.server.Role.HBASE_REGIONSERVER, 10);
            roleToIndex.put(org.apache.ambari.server.Role.HBASE_CLIENT, 11);
            roleToIndex.put(org.apache.ambari.server.Role.HIVE_SERVER, 12);
            roleToIndex.put(org.apache.ambari.server.Role.HIVE_METASTORE, 13);
            roleToIndex.put(org.apache.ambari.server.Role.HIVE_CLIENT, 14);
            roleToIndex.put(org.apache.ambari.server.Role.HCAT, 15);
            roleToIndex.put(org.apache.ambari.server.Role.OOZIE_SERVER, 16);
            roleToIndex.put(org.apache.ambari.server.Role.OOZIE_CLIENT, 17);
            roleToIndex.put(org.apache.ambari.server.Role.WEBHCAT_SERVER, 18);
            roleToIndex.put(org.apache.ambari.server.Role.PIG, 19);
            roleToIndex.put(org.apache.ambari.server.Role.SQOOP, 20);
            roleToIndex.put(org.apache.ambari.server.Role.GANGLIA_SERVER, 21);
            roleToIndex.put(org.apache.ambari.server.Role.GANGLIA_MONITOR, 22);
            roleToIndex.put(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, 23);
        }
    }

    @org.junit.Test
    public void testServiceStopWhileStopping() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName1).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host2));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host2));
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.toString());
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                sch.setState(org.apache.ambari.server.state.State.INSTALLED);
            }
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.STARTED.toString());
        requests.clear();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                if (!sch.getServiceComponentName().equals("HDFS_CLIENT")) {
                    sch.setState(org.apache.ambari.server.state.State.STARTED);
                }
            }
        }
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).setDesiredState(org.apache.ambari.server.state.State.STOPPING);
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                if (!sch.getServiceComponentName().equals("HDFS_CLIENT")) {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch.getDesiredState());
                    sch.setState(org.apache.ambari.server.state.State.STOPPING);
                } else if (sch.getServiceComponentName().equals("DATANODE")) {
                    org.apache.ambari.server.controller.ServiceComponentHostRequest r1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, sch.getServiceComponentName(), sch.getHostName(), org.apache.ambari.server.state.State.INSTALLED.name());
                    java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> reqs1 = new java.util.HashSet<>();
                    reqs1.add(r1);
                    updateHostComponents(reqs1, java.util.Collections.emptyMap(), true);
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
                }
            }
        }
        r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.toString());
        requests.clear();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        for (org.apache.ambari.server.state.ServiceComponent sc : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                if (!sch.getServiceComponentName().equals("HDFS_CLIENT")) {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
                }
            }
        }
    }

    @org.junit.Test
    public void testGetTasksByRequestId() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.ActionManager am = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        final long requestId1 = am.getNextRequestId();
        final long requestId2 = am.getNextRequestId();
        final long requestId3 = am.getNextRequestId();
        final java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String hostName1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String context = "Test invocation";
        org.apache.ambari.server.state.StackId stackID = new org.apache.ambari.server.state.StackId("HDP-0.1");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addCluster(cluster1, stackID);
        org.apache.ambari.server.state.Cluster c = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        java.lang.Long clusterId = c.getClusterId();
        helper.getOrCreateRepositoryVersion(stackID, stackID.getStackVersion());
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.addHost(hostName1);
        setOsFamily(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(hostName1), "redhat", "5.9");
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.mapAndPublishHostsToCluster(new java.util.HashSet<java.lang.String>() {
            {
                add(hostName1);
            }
        }, cluster1);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(stageFactory.createNew(requestId1, "/a1", cluster1, clusterId, context, "", ""));
        stages.get(0).setStageId(1);
        stages.get(0).addHostRoleExecutionCommand(hostName1, org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_MASTER.toString(), hostName1, java.lang.System.currentTimeMillis()), cluster1, "HBASE", false, false);
        stages.add(stageFactory.createNew(requestId1, "/a2", cluster1, clusterId, context, "", ""));
        stages.get(1).setStageId(2);
        stages.get(1).addHostRoleExecutionCommand(hostName1, org.apache.ambari.server.Role.HBASE_CLIENT, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_CLIENT.toString(), hostName1, java.lang.System.currentTimeMillis()), cluster1, "HBASE", false, false);
        stages.add(stageFactory.createNew(requestId1, "/a3", cluster1, clusterId, context, "", ""));
        stages.get(2).setStageId(3);
        stages.get(2).addHostRoleExecutionCommand(hostName1, org.apache.ambari.server.Role.HBASE_CLIENT, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_CLIENT.toString(), hostName1, java.lang.System.currentTimeMillis()), cluster1, "HBASE", false, false);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters);
        actionDB.persistActions(request);
        stages.clear();
        stages.add(stageFactory.createNew(requestId2, "/a4", cluster1, clusterId, context, "", ""));
        stages.get(0).setStageId(4);
        stages.get(0).addHostRoleExecutionCommand(hostName1, org.apache.ambari.server.Role.HBASE_CLIENT, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_CLIENT.toString(), hostName1, java.lang.System.currentTimeMillis()), cluster1, "HBASE", false, false);
        stages.add(stageFactory.createNew(requestId2, "/a5", cluster1, clusterId, context, "", ""));
        stages.get(1).setStageId(5);
        stages.get(1).addHostRoleExecutionCommand(hostName1, org.apache.ambari.server.Role.HBASE_CLIENT, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_CLIENT.toString(), hostName1, java.lang.System.currentTimeMillis()), cluster1, "HBASE", false, false);
        request = new org.apache.ambari.server.actionmanager.Request(stages, "", org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters);
        actionDB.persistActions(request);
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent serviceComponentHostServerActionEvent = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString(), null, java.lang.System.currentTimeMillis());
        stages.clear();
        stages.add(stageFactory.createNew(requestId3, "/a6", cluster1, clusterId, context, "", ""));
        stages.get(0).setStageId(6);
        stages.get(0).addServerActionCommand("some.action.class.name", null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, cluster1, serviceComponentHostServerActionEvent, null, null, null, null, false, false);
        org.junit.Assert.assertEquals("_internal_ambari", stages.get(0).getOrderedHostRoleCommands().get(0).getHostName());
        request = new org.apache.ambari.server.actionmanager.Request(stages, "", org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters);
        actionDB.persistActions(request);
        org.apache.ambari.server.controller.spi.Request spiRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_STAGE_ID_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals(requestId1).toPredicate();
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = hostRoleCommandDAO.findAll(spiRequest, predicate);
        org.junit.Assert.assertEquals(3, entities.size());
        java.lang.Long taskId = entities.get(0).getTaskId();
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals(requestId1).and().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).equals(taskId).toPredicate();
        entities = hostRoleCommandDAO.findAll(spiRequest, predicate);
        org.junit.Assert.assertEquals(1, entities.size());
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals(requestId2).toPredicate();
        entities = hostRoleCommandDAO.findAll(spiRequest, predicate);
        org.junit.Assert.assertEquals(2, entities.size());
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals(requestId1).and().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_ID_PROPERTY_ID).equals(taskId).or().property(org.apache.ambari.server.controller.internal.TaskResourceProvider.TASK_REQUEST_ID_PROPERTY_ID).equals(requestId2).toPredicate();
        entities = hostRoleCommandDAO.findAll(spiRequest, predicate);
        org.junit.Assert.assertEquals(3, entities.size());
    }

    @org.junit.Test
    public void testUpdateHostComponentsBadState() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName1).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host2));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host1));
        org.junit.Assert.assertNotNull(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getServiceComponent(componentName3).getServiceComponentHost(host2));
        org.apache.ambari.server.controller.ServiceRequest r = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.toString());
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName).getDesiredState());
        for (org.apache.ambari.server.state.ServiceComponentHost sch : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServiceComponentHosts(host1)) {
            sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        }
        for (org.apache.ambari.server.state.ServiceComponentHost sch : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServiceComponentHosts(host2)) {
            sch.setState(org.apache.ambari.server.state.State.UNKNOWN);
        }
        org.apache.ambari.server.controller.ServiceComponentHostRequest schr = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, "HDFS", "DATANODE", host2, "INSTALLED");
        java.util.Map<java.lang.String, java.lang.String> requestProps = new java.util.HashMap<>();
        requestProps.put("datanode", "dn_value");
        requestProps.put("namenode", "nn_value");
        org.apache.ambari.server.controller.RequestStatusResponse rsr = updateHostComponents(java.util.Collections.singleton(schr), requestProps, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(rsr.getRequestId());
        org.junit.Assert.assertEquals(1, stages.size());
        org.apache.ambari.server.actionmanager.Stage stage = stages.iterator().next();
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> execWrappers = stage.getExecutionCommands(host2);
        org.junit.Assert.assertEquals(1, execWrappers.size());
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execWrapper = execWrappers.iterator().next();
        org.junit.Assert.assertTrue(execWrapper.getExecutionCommand().getCommandParams().containsKey("datanode"));
        org.junit.Assert.assertFalse(execWrapper.getExecutionCommand().getCommandParams().containsKey("namendode"));
        for (org.apache.ambari.server.state.ServiceComponentHost sch : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServiceComponentHosts(host2)) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.UNKNOWN, sch.getState());
        }
    }

    @org.junit.Test
    public void testServiceUpdateRecursiveBadHostComponent() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.2"));
        java.lang.String serviceName1 = "HDFS";
        createService(cluster1, serviceName1, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName1, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> set1 = new java.util.HashSet<>();
        org.apache.ambari.server.controller.ServiceComponentHostRequest r1 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName1, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r2 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName2, host1, org.apache.ambari.server.state.State.INIT.toString());
        org.apache.ambari.server.controller.ServiceComponentHostRequest r3 = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName1, componentName3, host1, org.apache.ambari.server.state.State.INIT.toString());
        set1.add(r1);
        set1.add(r2);
        set1.add(r3);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createHostComponents(set1);
        org.apache.ambari.server.state.Cluster c1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s1 = c1.getService(serviceName1);
        org.apache.ambari.server.state.ServiceComponent sc1 = s1.getServiceComponent(componentName1);
        org.apache.ambari.server.state.ServiceComponent sc2 = s1.getServiceComponent(componentName2);
        org.apache.ambari.server.state.ServiceComponent sc3 = s1.getServiceComponent(componentName3);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc1.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = sc2.getServiceComponentHost(host1);
        org.apache.ambari.server.state.ServiceComponentHost sch3 = sc3.getServiceComponentHost(host1);
        s1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc1.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sc2.setDesiredState(org.apache.ambari.server.state.State.INIT);
        sc3.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.UNKNOWN);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.controller.ServiceRequest req = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion02.getId(), org.apache.ambari.server.state.State.INSTALLED.toString());
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(req), java.util.Collections.emptyMap(), true, false);
    }

    @org.junit.Test
    public void testUpdateStacks() throws java.lang.Exception {
        org.apache.ambari.server.state.StackInfo stackInfo = org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.getStack(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION);
        for (org.apache.ambari.server.state.RepositoryInfo repositoryInfo : stackInfo.getRepositories()) {
            org.junit.Assert.assertFalse(org.apache.ambari.server.controller.AmbariManagementControllerTest.INCORRECT_BASE_URL.equals(repositoryInfo.getBaseUrl()));
            repositoryInfo.setBaseUrl(org.apache.ambari.server.controller.AmbariManagementControllerTest.INCORRECT_BASE_URL);
            org.junit.Assert.assertTrue(org.apache.ambari.server.controller.AmbariManagementControllerTest.INCORRECT_BASE_URL.equals(repositoryInfo.getBaseUrl()));
        }
        stackManagerMock.invalidateCurrentPaths();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateStacks();
        stackInfo = org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.getStack(org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_NAME, org.apache.ambari.server.controller.AmbariManagementControllerTest.STACK_VERSION);
        for (org.apache.ambari.server.state.RepositoryInfo repositoryInfo : stackInfo.getRepositories()) {
            org.junit.Assert.assertFalse(org.apache.ambari.server.controller.AmbariManagementControllerTest.INCORRECT_BASE_URL.equals(repositoryInfo.getBaseUrl()));
        }
    }

    @org.junit.Test
    public void testDeleteHostComponentInVariousStates() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-1.3.1"));
        java.lang.String hdfs = "HDFS";
        java.lang.String mapred = "MAPREDUCE";
        createService(cluster1, hdfs, null);
        createService(cluster1, mapred, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        java.lang.String componentName4 = "JOBTRACKER";
        java.lang.String componentName5 = "TASKTRACKER";
        java.lang.String componentName6 = "MAPREDUCE_CLIENT";
        createServiceComponent(cluster1, hdfs, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, hdfs, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, hdfs, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, mapred, componentName4, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, mapred, componentName5, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, mapred, componentName6, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        createServiceComponentHost(cluster1, hdfs, componentName1, host1, null);
        createServiceComponentHost(cluster1, hdfs, componentName2, host1, null);
        createServiceComponentHost(cluster1, hdfs, componentName3, host1, null);
        createServiceComponentHost(cluster1, mapred, componentName4, host1, null);
        createServiceComponentHost(cluster1, mapred, componentName5, host1, null);
        createServiceComponentHost(cluster1, mapred, componentName6, host1, null);
        installService(cluster1, hdfs, false, false);
        installService(cluster1, mapred, false, false);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service s1 = cluster.getService(hdfs);
        org.apache.ambari.server.state.Service s2 = cluster.getService(mapred);
        org.apache.ambari.server.state.ServiceComponent sc1 = s1.getServiceComponent(componentName1);
        sc1.getServiceComponentHosts().values().iterator().next().setState(org.apache.ambari.server.state.State.STARTED);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> schRequests = new java.util.HashSet<>();
        schRequests.clear();
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, hdfs, componentName1, host1, null));
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.deleteHostComponents(schRequests);
            org.junit.Assert.fail("Expect failure while deleting.");
        } catch (java.lang.Exception ex) {
            org.junit.Assert.assertTrue(ex.getMessage().contains("Current host component state prohibiting component removal"));
        }
        sc1.getServiceComponentHosts().values().iterator().next().setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sc1.getServiceComponentHosts().values().iterator().next().setState(org.apache.ambari.server.state.State.UNKNOWN);
        org.apache.ambari.server.state.ServiceComponent sc2 = s1.getServiceComponent(componentName2);
        sc2.getServiceComponentHosts().values().iterator().next().setState(org.apache.ambari.server.state.State.INIT);
        org.apache.ambari.server.state.ServiceComponent sc3 = s1.getServiceComponent(componentName3);
        sc3.getServiceComponentHosts().values().iterator().next().setState(org.apache.ambari.server.state.State.INSTALL_FAILED);
        org.apache.ambari.server.state.ServiceComponent sc4 = s2.getServiceComponent(componentName4);
        sc4.getServiceComponentHosts().values().iterator().next().setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sc4.getServiceComponentHosts().values().iterator().next().setState(org.apache.ambari.server.state.State.DISABLED);
        org.apache.ambari.server.state.ServiceComponent sc5 = s2.getServiceComponent(componentName5);
        sc5.getServiceComponentHosts().values().iterator().next().setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponent sc6 = s2.getServiceComponent(componentName6);
        sc6.getServiceComponentHosts().values().iterator().next().setState(org.apache.ambari.server.state.State.INIT);
        schRequests.clear();
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, hdfs, componentName1, host1, null));
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, hdfs, componentName2, host1, null));
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, hdfs, componentName3, host1, null));
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, mapred, componentName4, host1, null));
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, mapred, componentName5, host1, null));
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, mapred, componentName6, host1, null));
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteStatusMetaData = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.deleteHostComponents(schRequests);
        org.junit.Assert.assertEquals(0, deleteStatusMetaData.getExceptionForKeys().size());
    }

    @org.junit.Test
    public void testDeleteHostWithComponent() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        installService(cluster1, serviceName, false, false);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents = cluster.getService(serviceName).getServiceComponent(componentName1).getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), cluster.getDesiredStackVersion().getStackId()));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        hostComponents = cluster.getService(serviceName).getServiceComponent(componentName2).getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), cluster.getDesiredStackVersion().getStackId()));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        java.util.Set<org.apache.ambari.server.controller.HostRequest> requests = new java.util.HashSet<>();
        requests.clear();
        requests.add(new org.apache.ambari.server.controller.HostRequest(host1, cluster1));
        org.apache.ambari.server.state.Service s = cluster.getService(serviceName);
        s.getServiceComponent("DATANODE").getServiceComponentHost(host1).setState(org.apache.ambari.server.state.State.STARTED);
        try {
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.deleteHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, false);
            org.junit.Assert.fail("Expect failure deleting hosts when components exist and have not been stopped.");
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info("Exception is - " + e.getMessage());
            org.junit.Assert.assertTrue(e.getMessage().contains("these components are not in the removable state:"));
        }
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData data = null;
        org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info("Test dry run of delete with all host components");
        s.getServiceComponent("DATANODE").getServiceComponentHost(host1).setState(org.apache.ambari.server.state.State.INSTALLED);
        try {
            data = org.apache.ambari.server.controller.internal.HostResourceProviderTest.deleteHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, true);
            org.junit.Assert.assertTrue(data.getDeletedKeys().size() == 1);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info("Exception is - " + e.getMessage());
            org.junit.Assert.fail("Do not expect failure deleting hosts when components exist and are stopped.");
        }
        org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info("Test successful delete with all host components");
        s.getServiceComponent("DATANODE").getServiceComponentHost(host1).setState(org.apache.ambari.server.state.State.INSTALLED);
        try {
            data = org.apache.ambari.server.controller.internal.HostResourceProviderTest.deleteHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, false);
            org.junit.Assert.assertNotNull(data);
            org.junit.Assert.assertTrue(4 == data.getDeletedKeys().size());
            org.junit.Assert.assertTrue(0 == data.getExceptionForKeys().size());
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.LOG.info("Exception is - " + e.getMessage());
            org.junit.Assert.fail("Do not expect failure deleting hosts when components exist and are stopped.");
        }
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1);
            org.junit.Assert.fail("Expected a HostNotFoundException.");
        } catch (org.apache.ambari.server.HostNotFoundException e) {
        }
    }

    @org.junit.Test
    public void testDeleteHost() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        installService(cluster1, serviceName, false, false);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents = cluster.getService(serviceName).getServiceComponent(componentName1).getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), cluster.getDesiredStackVersion().getStackId()));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        hostComponents = cluster.getService(serviceName).getServiceComponent(componentName2).getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), cluster.getDesiredStackVersion().getStackId()));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        java.util.Set<org.apache.ambari.server.controller.HostRequest> requests = new java.util.HashSet<>();
        requests.clear();
        requests.add(new org.apache.ambari.server.controller.HostRequest(host1, cluster1));
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> schRequests = new java.util.HashSet<>();
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, "DISABLED"));
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName2, host1, "DISABLED"));
        updateHostComponents(schRequests, new java.util.HashMap<>(), false);
        schRequests.clear();
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, null));
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName2, host1, null));
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName3, host1, null));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.deleteHostComponents(schRequests);
        org.junit.Assert.assertEquals(0, cluster.getServiceComponentHosts(host1).size());
        org.junit.Assert.assertNull(topologyHostInfoDAO.findByHostname(host1));
        java.lang.Long firstHostId = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1).getHostId();
        requests.clear();
        requests.add(new org.apache.ambari.server.controller.HostRequest(host1, null));
        try {
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.deleteHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests);
        } catch (java.lang.Exception e) {
            org.junit.Assert.fail("Did not expect an error deleting the host from the cluster. Error: " + e.getMessage());
        }
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHostsForCluster(cluster1).containsKey(host1));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getClustersForHost(host1).contains(cluster));
        org.junit.Assert.assertNull(topologyHostInfoDAO.findByHostname(host1));
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> tasks = hostRoleCommandDAO.findByHostId(firstHostId);
        org.junit.Assert.assertEquals(0, tasks.size());
        requests.clear();
        requests.add(new org.apache.ambari.server.controller.HostRequest(host2, cluster1));
        try {
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.deleteHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests);
        } catch (java.lang.Exception e) {
            org.junit.Assert.fail("Did not expect an error deleting the host from the cluster. Error: " + e.getMessage());
        }
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHostsForCluster(cluster1).containsKey(host2));
        org.junit.Assert.assertFalse(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getClustersForHost(host2).contains(cluster));
        org.junit.Assert.assertNull(topologyHostInfoDAO.findByHostname(host2));
        requests.clear();
        requests.add(new org.apache.ambari.server.controller.HostRequest(host1, null));
        try {
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.deleteHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests);
            org.junit.Assert.fail("Expected a HostNotFoundException trying to remove a host that was already deleted.");
        } catch (org.apache.ambari.server.HostNotFoundException e) {
        }
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1);
            org.junit.Assert.fail("Expected a HostNotFoundException.");
        } catch (org.apache.ambari.server.HostNotFoundException e) {
        }
        requests.clear();
        requests.add(new org.apache.ambari.server.controller.HostRequest(host3, null));
        try {
            org.apache.ambari.server.controller.internal.HostResourceProviderTest.deleteHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests);
            org.junit.Assert.fail("Expected a HostNotFoundException trying to remove a host that was never added.");
        } catch (org.apache.ambari.server.HostNotFoundException e) {
        }
    }

    @org.junit.Test
    public void testGetRootServices() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceRequest request = new org.apache.ambari.server.controller.RootServiceRequest(null);
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRootServices(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.RootService.values().length, responses.size());
        org.apache.ambari.server.controller.RootServiceRequest requestWithParams = new org.apache.ambari.server.controller.RootServiceRequest(org.apache.ambari.server.controller.RootService.AMBARI.toString());
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRootServices(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.RootServiceResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getServiceName(), org.apache.ambari.server.controller.RootService.AMBARI.toString());
        }
        org.apache.ambari.server.controller.RootServiceRequest invalidRequest = new org.apache.ambari.server.controller.RootServiceRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRootServices(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
        }
    }

    @org.junit.Test
    public void testGetRootServiceComponents() throws java.lang.Exception {
        org.apache.ambari.server.controller.RootServiceComponentRequest request = new org.apache.ambari.server.controller.RootServiceComponentRequest(org.apache.ambari.server.controller.RootService.AMBARI.toString(), null);
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> responses = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRootServiceComponents(java.util.Collections.singleton(request));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.RootService.AMBARI.getComponents().length, responses.size());
        org.apache.ambari.server.controller.RootServiceComponentRequest requestWithParams = new org.apache.ambari.server.controller.RootServiceComponentRequest(org.apache.ambari.server.controller.RootService.AMBARI.toString(), org.apache.ambari.server.controller.RootService.AMBARI.getComponents()[0].toString());
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> responsesWithParams = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRootServiceComponents(java.util.Collections.singleton(requestWithParams));
        org.junit.Assert.assertEquals(1, responsesWithParams.size());
        for (org.apache.ambari.server.controller.RootServiceComponentResponse responseWithParams : responsesWithParams) {
            org.junit.Assert.assertEquals(responseWithParams.getComponentName(), org.apache.ambari.server.controller.RootService.AMBARI.getComponents()[0].toString());
        }
        org.apache.ambari.server.controller.RootServiceComponentRequest invalidRequest = new org.apache.ambari.server.controller.RootServiceComponentRequest(org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE, org.apache.ambari.server.controller.AmbariManagementControllerTest.NON_EXT_VALUE);
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getRootServiceComponents(java.util.Collections.singleton(invalidRequest));
        } catch (org.apache.ambari.server.ObjectNotFoundException e) {
        }
    }

    @org.junit.Test
    public void testDeleteComponentsOnHost() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        createServiceComponentHost(cluster1, null, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        installService(cluster1, serviceName, false, false);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents = cluster.getService(serviceName).getServiceComponent(componentName1).getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), cluster.getDesiredStackVersion().getStackId()));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        hostComponents = cluster.getService(serviceName).getServiceComponent(componentName2).getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), cluster.getDesiredStackVersion().getStackId()));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        org.apache.ambari.server.state.ServiceComponentHost sch = cluster.getService(serviceName).getServiceComponent(componentName2).getServiceComponentHost(host1);
        org.junit.Assert.assertNotNull(sch);
        sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis()));
        sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartedEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis()));
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> schRequests = new java.util.HashSet<>();
        schRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, host1, null));
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData deleteStatusMetaData = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.deleteHostComponents(schRequests);
        org.junit.Assert.assertEquals(1, deleteStatusMetaData.getExceptionForKeys().size());
        org.junit.Assert.assertEquals(1, cluster.getServiceComponentHosts(host1).size());
        sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStopEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis()));
        sch.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStoppedEvent(sch.getServiceComponentName(), sch.getHostName(), java.lang.System.currentTimeMillis()));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.deleteHostComponents(schRequests);
        org.junit.Assert.assertEquals(0, cluster.getServiceComponentHosts(host1).size());
    }

    @org.junit.Test
    public void testExecutionCommandConfiguration() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> config = new java.util.HashMap<>();
        config.put("type1", new java.util.HashMap<>());
        config.put("type3", new java.util.HashMap<>());
        config.get("type3").put("name1", "neverchange");
        configHelper.applyCustomConfig(config, "type1", "name1", "value11", false);
        org.junit.Assert.assertEquals("value11", config.get("type1").get("name1"));
        config.put("type1", new java.util.HashMap<>());
        configHelper.applyCustomConfig(config, "type1", "name1", "value12", false);
        org.junit.Assert.assertEquals("value12", config.get("type1").get("name1"));
        configHelper.applyCustomConfig(config, "type2", "name2", "value21", false);
        org.junit.Assert.assertEquals("value21", config.get("type2").get("name2"));
        configHelper.applyCustomConfig(config, "type2", "name2", "", true);
        org.junit.Assert.assertEquals("", config.get("type2").get("DELETED_name2"));
        org.junit.Assert.assertEquals("neverchange", config.get("type3").get("name1"));
        java.util.Map<java.lang.String, java.lang.String> persistedClusterConfig = new java.util.HashMap<>();
        persistedClusterConfig.put("name1", "value11");
        persistedClusterConfig.put("name3", "value31");
        persistedClusterConfig.put("name4", "value41");
        java.util.Map<java.lang.String, java.lang.String> override = new java.util.HashMap<>();
        override.put("name1", "value12");
        override.put("name2", "value21");
        override.put("DELETED_name3", "value31");
        java.util.Map<java.lang.String, java.lang.String> mergedConfig = configHelper.getMergedConfig(persistedClusterConfig, override);
        org.junit.Assert.assertEquals(3, mergedConfig.size());
        org.junit.Assert.assertFalse(mergedConfig.containsKey("name3"));
        org.junit.Assert.assertEquals("value12", mergedConfig.get("name1"));
        org.junit.Assert.assertEquals("value21", mergedConfig.get("name2"));
        org.junit.Assert.assertEquals("value41", mergedConfig.get("name4"));
    }

    @org.junit.Test
    public void testApplyConfigurationWithTheSameTag() throws org.apache.ambari.server.security.authorization.AuthorizationException {
        final java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String tag = "version1";
        java.lang.String type = "core-site";
        java.lang.Exception exception = null;
        try {
            org.apache.ambari.server.controller.AmbariManagementController amc = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
            org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
            com.google.gson.Gson gson = new com.google.gson.Gson();
            clusters.addHost("host1");
            clusters.addHost("host2");
            clusters.addHost("host3");
            org.apache.ambari.server.state.Host host = clusters.getHost("host1");
            setOsFamily(host, "redhat", "6.3");
            host = clusters.getHost("host2");
            setOsFamily(host, "redhat", "6.3");
            host = clusters.getHost("host3");
            setOsFamily(host, "redhat", "6.3");
            org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(null, cluster1, "HDP-1.2.0", null);
            amc.createCluster(clusterRequest);
            java.util.Set<org.apache.ambari.server.controller.ServiceRequest> serviceRequests = new java.util.HashSet<>();
            serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), null));
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(amc, repositoryVersionDAO, serviceRequests);
            java.lang.reflect.Type confType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
            org.apache.ambari.server.controller.ConfigurationRequest configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, type, tag, gson.fromJson("{ \"fs.default.name\" : \"localhost:8020\"}", confType), null);
            amc.createConfiguration(configurationRequest);
            amc.createConfiguration(configurationRequest);
        } catch (java.lang.Exception e) {
            exception = e;
        }
        org.junit.Assert.assertNotNull(exception);
        java.lang.String exceptionMessage = java.text.MessageFormat.format("Configuration with tag ''{0}'' exists for ''{1}''", tag, type);
        org.junit.Assert.assertEquals(exceptionMessage, exception.getMessage());
    }

    @org.junit.Test
    public void testDeleteClusterCreateHost() throws java.lang.Exception {
        java.lang.String STACK_ID = "HDP-2.0.1";
        java.lang.String CLUSTER_NAME = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String HOST1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String HOST2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addHost(HOST1);
        org.apache.ambari.server.state.Host host = clusters.getHost(HOST1);
        setOsFamily(host, "redhat", "6.3");
        clusters.getHost(HOST1).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        clusters.updateHostMappings(host);
        clusters.addHost(HOST2);
        host = clusters.getHost(HOST2);
        clusters.updateHostMappings(host);
        setOsFamily(host, "redhat", "6.3");
        org.apache.ambari.server.controller.AmbariManagementController amc = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.ClusterRequest cr = new org.apache.ambari.server.controller.ClusterRequest(null, CLUSTER_NAME, STACK_ID, null);
        amc.createCluster(cr);
        long clusterId = clusters.getCluster(CLUSTER_NAME).getClusterId();
        org.apache.ambari.server.controller.ConfigurationRequest configRequest = new org.apache.ambari.server.controller.ConfigurationRequest(CLUSTER_NAME, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, null);
        org.apache.ambari.server.controller.ClusterRequest ur = new org.apache.ambari.server.controller.ClusterRequest(clusterId, CLUSTER_NAME, STACK_ID, null);
        ur.setDesiredConfig(java.util.Collections.singletonList(configRequest));
        amc.updateClusters(java.util.Collections.singleton(ur), new java.util.HashMap<>());
        java.util.Set<org.apache.ambari.server.controller.HostRequest> hrs = new java.util.HashSet<>();
        hrs.add(new org.apache.ambari.server.controller.HostRequest(HOST1, CLUSTER_NAME));
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(amc, hrs);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> serviceRequests = new java.util.HashSet<>();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "HDFS", repositoryVersion201.getId(), null));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "MAPREDUCE2", repositoryVersion201.getId(), null));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "YARN", repositoryVersion201.getId(), null));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(amc, repositoryVersionDAO, serviceRequests);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> serviceComponentRequests = new java.util.HashSet<>();
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "HDFS", "NAMENODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "HDFS", "SECONDARY_NAMENODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "HDFS", "DATANODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "MAPREDUCE2", "HISTORYSERVER", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "YARN", "RESOURCEMANAGER", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "YARN", "NODEMANAGER", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "HDFS", "HDFS_CLIENT", null));
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(amc, serviceComponentRequests);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> componentHostRequests = new java.util.HashSet<>();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, "HDFS", "DATANODE", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, "HDFS", "NAMENODE", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, "HDFS", "SECONDARY_NAMENODE", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, "MAPREDUCE2", "HISTORYSERVER", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, "YARN", "RESOURCEMANAGER", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, "YARN", "NODEMANAGER", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, "HDFS", "HDFS_CLIENT", HOST1, null));
        amc.createHostComponents(componentHostRequests);
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", null, null);
        org.apache.ambari.server.controller.ExecuteActionRequest ar = new org.apache.ambari.server.controller.ExecuteActionRequest(CLUSTER_NAME, org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), null, false);
        ar.getResourceFilters().add(resourceFilter);
        amc.createAction(ar, null);
        amc.deleteCluster(cr);
        org.junit.Assert.assertNotNull(clusters.getHost(HOST1));
        org.junit.Assert.assertNotNull(clusters.getHost(HOST2));
        org.apache.ambari.server.orm.dao.HostDAO dao = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        org.junit.Assert.assertNotNull(dao.findByName(HOST1));
        org.junit.Assert.assertNotNull(dao.findByName(HOST2));
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testDisableAndDeleteStates() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementController amc = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        clusters.addHost(host1);
        clusters.addHost(host2);
        clusters.addHost(host3);
        org.apache.ambari.server.state.Host host = clusters.getHost("host1");
        setOsFamily(host, "redhat", "5.9");
        clusters.updateHostMappings(host);
        host = clusters.getHost("host2");
        setOsFamily(host, "redhat", "5.9");
        clusters.updateHostMappings(host);
        host = clusters.getHost("host3");
        setOsFamily(host, "redhat", "5.9");
        clusters.updateHostMappings(host);
        org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(null, cluster1, "HDP-1.2.0", null);
        amc.createCluster(clusterRequest);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> serviceRequests = new java.util.HashSet<>();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), null));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HIVE", repositoryVersion120.getId(), null));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(amc, repositoryVersionDAO, serviceRequests);
        java.lang.reflect.Type confType = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        org.apache.ambari.server.controller.ConfigurationRequest configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version1", gson.fromJson("{ \"fs.default.name\" : \"localhost:8020\"}", confType), null);
        amc.createConfiguration(configurationRequest);
        configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", gson.fromJson("{ \"dfs.datanode.data.dir.perm\" : \"750\"}", confType), null);
        amc.createConfiguration(configurationRequest);
        configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "global", "version1", gson.fromJson("{ \"hive.server2.enable.doAs\" : \"true\"}", confType), null);
        amc.createConfiguration(configurationRequest);
        org.junit.Assert.assertTrue(clusters.getCluster(cluster1).getDesiredConfigs().containsKey("hive-site"));
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), null));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> serviceComponentRequests = new java.util.HashSet<>();
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "NAMENODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "SECONDARY_NAMENODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "DATANODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "HDFS_CLIENT", null));
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(amc, serviceComponentRequests);
        java.util.Set<org.apache.ambari.server.controller.HostRequest> hostRequests = new java.util.HashSet<>();
        hostRequests.add(new org.apache.ambari.server.controller.HostRequest(host1, cluster1));
        hostRequests.add(new org.apache.ambari.server.controller.HostRequest(host2, cluster1));
        hostRequests.add(new org.apache.ambari.server.controller.HostRequest(host3, cluster1));
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(amc, hostRequests);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> componentHostRequests = new java.util.HashSet<>();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "DATANODE", host1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "SECONDARY_NAMENODE", host1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "DATANODE", host2, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "DATANODE", host3, null));
        amc.createHostComponents(componentHostRequests);
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), "INSTALLED"));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(cluster1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> namenodes = cluster.getService("HDFS").getServiceComponent("NAMENODE").getServiceComponentHosts();
        org.junit.Assert.assertEquals(1, namenodes.size());
        org.apache.ambari.server.state.ServiceComponentHost componentHost = namenodes.get(host1);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents = cluster.getService("HDFS").getServiceComponent("DATANODE").getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), "HDP-1.2.0"));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        hostComponents = cluster.getService("HDFS").getServiceComponent("NAMENODE").getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), "HDP-1.2.0"));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        hostComponents = cluster.getService("HDFS").getServiceComponent("SECONDARY_NAMENODE").getServiceComponentHosts();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : hostComponents.entrySet()) {
            org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), "HDP-1.2.0"));
            cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
        }
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host1, "DISABLED"));
        updateHostComponents(amc, componentHostRequests, mapRequestProps, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, componentHost.getState());
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host1, "INSTALLED"));
        updateHostComponents(amc, componentHostRequests, mapRequestProps, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, componentHost.getState());
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host1, "DISABLED"));
        updateHostComponents(amc, componentHostRequests, mapRequestProps, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, componentHost.getState());
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host2, null));
        amc.createHostComponents(componentHostRequests);
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host2, "INSTALLED"));
        updateHostComponents(amc, componentHostRequests, mapRequestProps, true);
        namenodes = cluster.getService("HDFS").getServiceComponent("NAMENODE").getServiceComponentHosts();
        org.junit.Assert.assertEquals(2, namenodes.size());
        componentHost = namenodes.get(host2);
        componentHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(componentHost.getServiceComponentName(), componentHost.getHostName(), java.lang.System.currentTimeMillis(), "HDP-1.2.0"));
        componentHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(componentHost.getServiceComponentName(), componentHost.getHostName(), java.lang.System.currentTimeMillis()));
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), "STARTED"));
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        for (org.apache.ambari.server.controller.ShortTaskStatus shortTaskStatus : response.getTasks()) {
            org.junit.Assert.assertFalse(host1.equals(shortTaskStatus.getHostName()) && "NAMENODE".equals(shortTaskStatus.getRole()));
        }
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host1, null));
        amc.deleteHostComponents(componentHostRequests);
        namenodes = cluster.getService("HDFS").getServiceComponent("NAMENODE").getServiceComponentHosts();
        org.junit.Assert.assertEquals(1, namenodes.size());
        testRunSmokeTestFlag(mapRequestProps, amc, serviceRequests);
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host1, null));
        amc.createHostComponents(componentHostRequests);
        namenodes = cluster.getService("HDFS").getServiceComponent("NAMENODE").getServiceComponentHosts();
        org.junit.Assert.assertEquals(2, namenodes.size());
        componentHost = namenodes.get(host1);
        componentHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(componentHost.getServiceComponentName(), componentHost.getHostName(), java.lang.System.currentTimeMillis(), "HDP-1.2.0"));
        componentHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(componentHost.getServiceComponentName(), componentHost.getHostName(), java.lang.System.currentTimeMillis()));
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", host1, "INSTALLED"));
        updateHostComponents(amc, componentHostRequests, mapRequestProps, true);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, namenodes.get(host1).getState());
        org.apache.ambari.server.state.ServiceComponentHost sch = null;
        for (org.apache.ambari.server.state.ServiceComponentHost tmp : cluster.getServiceComponentHosts(host2)) {
            if (tmp.getServiceComponentName().equals("DATANODE")) {
                tmp.setState(org.apache.ambari.server.state.State.UNKNOWN);
                sch = tmp;
            }
        }
        org.junit.Assert.assertNotNull(sch);
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "DATANODE", host2, "DISABLED"));
        updateHostComponents(amc, componentHostRequests, mapRequestProps, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, sch.getState());
        componentHostRequests.clear();
        mapRequestProps.put(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, cluster1);
        updateHostComponents(amc, componentHostRequests, mapRequestProps, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, sch.getState());
        mapRequestProps.clear();
        org.junit.Assert.assertEquals(sch.getServiceComponentName(), "DATANODE");
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), "INSTALLED"));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, sch.getState());
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), "STARTED"));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, sch.getState());
        componentHostRequests.clear();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "DATANODE", host2, null));
        amc.deleteHostComponents(componentHostRequests);
        sch = null;
        for (org.apache.ambari.server.state.ServiceComponentHost tmp : cluster.getServiceComponentHosts(host2)) {
            if (tmp.getServiceComponentName().equals("DATANODE")) {
                sch = tmp;
            }
        }
        org.junit.Assert.assertNull(sch);
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), "INSTALLED"));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, null, null, null, null));
        org.junit.Assert.assertEquals(2, org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(amc, serviceRequests).size());
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), null));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HIVE", repositoryVersion120.getId(), null));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.deleteServices(amc, serviceRequests);
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, null, null, null, null));
        org.junit.Assert.assertEquals(0, org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(amc, serviceRequests).size());
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), null));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(amc, repositoryVersionDAO, serviceRequests);
        org.junit.Assert.assertEquals(1, org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(amc, serviceRequests).size());
        configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "core-site", "version2", gson.fromJson("{ \"fs.default.name\" : \"localhost:8020\"}", confType), null);
        amc.createConfiguration(configurationRequest);
        configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version2", gson.fromJson("{ \"dfs.datanode.data.dir.perm\" : \"750\"}", confType), null);
        amc.createConfiguration(configurationRequest);
        configurationRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "global", "version2", gson.fromJson("{ \"hbase_hdfs_root_dir\" : \"/apps/hbase/\"}", confType), null);
        amc.createConfiguration(configurationRequest);
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion120.getId(), null));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        serviceComponentRequests = new java.util.HashSet<>();
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "NAMENODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "SECONDARY_NAMENODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "DATANODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(cluster1, "HDFS", "HDFS_CLIENT", null));
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(amc, serviceComponentRequests);
        componentHostRequests = new java.util.HashSet<>();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "DATANODE", "host1", null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "NAMENODE", "host1", null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "SECONDARY_NAMENODE", host1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "DATANODE", "host2", null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, "DATANODE", "host3", null));
        amc.createHostComponents(componentHostRequests);
        namenodes = cluster.getService("HDFS").getServiceComponent("NAMENODE").getServiceComponentHosts();
        org.junit.Assert.assertEquals(1, namenodes.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> datanodes = cluster.getService("HDFS").getServiceComponent("DATANODE").getServiceComponentHosts();
        org.junit.Assert.assertEquals(3, datanodes.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> namenodes2 = cluster.getService("HDFS").getServiceComponent("SECONDARY_NAMENODE").getServiceComponentHosts();
        org.junit.Assert.assertEquals(1, namenodes2.size());
    }

    @org.junit.Test
    public void testScheduleSmokeTest() throws java.lang.Exception {
        final java.lang.String HOST1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String OS_TYPE = "centos5";
        final java.lang.String STACK_ID = "HDP-2.0.1";
        final java.lang.String CLUSTER_NAME = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String HDFS_SERVICE_CHECK_ROLE = "HDFS_SERVICE_CHECK";
        final java.lang.String MAPREDUCE2_SERVICE_CHECK_ROLE = "MAPREDUCE2_SERVICE_CHECK";
        final java.lang.String YARN_SERVICE_CHECK_ROLE = "YARN_SERVICE_CHECK";
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = java.util.Collections.emptyMap();
        org.apache.ambari.server.controller.AmbariManagementController amc = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        clusters.addHost(HOST1);
        org.apache.ambari.server.state.Host host = clusters.getHost(HOST1);
        setOsFamily(host, "redhat", "5.9");
        clusters.getHost(HOST1).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        clusters.updateHostMappings(host);
        org.apache.ambari.server.controller.ClusterRequest clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(null, CLUSTER_NAME, STACK_ID, null);
        amc.createCluster(clusterRequest);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> serviceRequests = new java.util.HashSet<>();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "HDFS", repositoryVersion201.getId(), null));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "MAPREDUCE2", repositoryVersion201.getId(), null));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "YARN", repositoryVersion201.getId(), null));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(amc, repositoryVersionDAO, serviceRequests);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> serviceComponentRequests = new java.util.HashSet<>();
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "HDFS", "NAMENODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "HDFS", "SECONDARY_NAMENODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "HDFS", "DATANODE", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "MAPREDUCE2", "HISTORYSERVER", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "YARN", "RESOURCEMANAGER", null));
        serviceComponentRequests.add(new org.apache.ambari.server.controller.ServiceComponentRequest(CLUSTER_NAME, "YARN", "NODEMANAGER", null));
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(amc, serviceComponentRequests);
        java.util.Set<org.apache.ambari.server.controller.HostRequest> hostRequests = new java.util.HashSet<>();
        hostRequests.add(new org.apache.ambari.server.controller.HostRequest(HOST1, CLUSTER_NAME));
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.createHosts(amc, hostRequests);
        for (org.apache.ambari.server.state.Host clusterHost : clusters.getHosts()) {
            clusters.updateHostMappings(clusterHost);
        }
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> componentHostRequests = new java.util.HashSet<>();
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, null, "DATANODE", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, null, "NAMENODE", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, null, "SECONDARY_NAMENODE", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, null, "HISTORYSERVER", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, null, "RESOURCEMANAGER", HOST1, null));
        componentHostRequests.add(new org.apache.ambari.server.controller.ServiceComponentHostRequest(CLUSTER_NAME, null, "NODEMANAGER", HOST1, null));
        amc.createHostComponents(componentHostRequests);
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "HDFS", repositoryVersion201.getId(), org.apache.ambari.server.state.State.INSTALLED.name()));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "MAPREDUCE2", repositoryVersion201.getId(), org.apache.ambari.server.state.State.INSTALLED.name()));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "YARN", repositoryVersion201.getId(), org.apache.ambari.server.state.State.INSTALLED.name()));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(CLUSTER_NAME);
        for (java.lang.String serviceName : cluster.getServices().keySet()) {
            for (java.lang.String componentName : cluster.getService(serviceName).getServiceComponents().keySet()) {
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> serviceComponentHosts = cluster.getService(serviceName).getServiceComponent(componentName).getServiceComponentHosts();
                for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> entry : serviceComponentHosts.entrySet()) {
                    org.apache.ambari.server.state.ServiceComponentHost cHost = entry.getValue();
                    cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis(), STACK_ID));
                    cHost.handleEvent(new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(cHost.getServiceComponentName(), cHost.getHostName(), java.lang.System.currentTimeMillis()));
                }
            }
        }
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "HDFS", repositoryVersion201.getId(), org.apache.ambari.server.state.State.STARTED.name()));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "MAPREDUCE2", repositoryVersion201.getId(), org.apache.ambari.server.state.State.STARTED.name()));
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(CLUSTER_NAME, "YARN", repositoryVersion201.getId(), org.apache.ambari.server.state.State.STARTED.name()));
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, true, false);
        java.util.Collection<?> hdfsSmokeTasks = org.apache.commons.collections.CollectionUtils.select(response.getTasks(), new org.apache.ambari.server.controller.AmbariManagementControllerTest.RolePredicate(HDFS_SERVICE_CHECK_ROLE));
        org.junit.Assert.assertEquals(1, hdfsSmokeTasks.size());
        java.util.Collection<?> mapreduce2SmokeTasks = org.apache.commons.collections.CollectionUtils.select(response.getTasks(), new org.apache.ambari.server.controller.AmbariManagementControllerTest.RolePredicate(MAPREDUCE2_SERVICE_CHECK_ROLE));
        org.junit.Assert.assertEquals(1, mapreduce2SmokeTasks.size());
        java.util.Collection<?> yarnSmokeTasks = org.apache.commons.collections.CollectionUtils.select(response.getTasks(), new org.apache.ambari.server.controller.AmbariManagementControllerTest.RolePredicate(YARN_SERVICE_CHECK_ROLE));
        org.junit.Assert.assertEquals(1, yarnSmokeTasks.size());
    }

    @org.junit.Test
    public void testGetServices2() throws java.lang.Exception {
        com.google.inject.Injector injector = EasyMock.createStrictMock(com.google.inject.Injector.class);
        org.easymock.Capture<org.apache.ambari.server.controller.AmbariManagementController> controllerCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.controller.ServiceResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.controller.ServiceRequest request1 = new org.apache.ambari.server.controller.ServiceRequest("cluster1", "service1", null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> setRequests = new java.util.HashSet<>();
        setRequests.add(request1);
        org.apache.ambari.server.controller.AmbariManagementControllerImplTest.constructorInit(injector, controllerCapture, null, maintHelper, EasyMock.createStrictMock(org.apache.ambari.server.controller.KerberosHelper.class), null, null);
        EasyMock.expect(clusters.getCluster("cluster1")).andReturn(cluster);
        EasyMock.expect(cluster.getService("service1")).andReturn(service);
        EasyMock.expect(service.convertToResponse()).andReturn(response);
        EasyMock.replay(maintHelper, injector, clusters, cluster, service, response);
        org.apache.ambari.server.controller.AmbariManagementController controller = new org.apache.ambari.server.controller.AmbariManagementControllerImpl(null, clusters, injector);
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> setResponses = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(controller, setRequests);
        org.junit.Assert.assertSame(controller, controllerCapture.getValue());
        org.junit.Assert.assertEquals(1, setResponses.size());
        org.junit.Assert.assertTrue(setResponses.contains(response));
        EasyMock.verify(injector, clusters, cluster, service, response);
    }

    @org.junit.Test
    public void testGetServices___ServiceNotFoundException() throws java.lang.Exception {
        com.google.inject.Injector injector = EasyMock.createStrictMock(com.google.inject.Injector.class);
        org.easymock.Capture<org.apache.ambari.server.controller.AmbariManagementController> controllerCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.ServiceRequest request1 = new org.apache.ambari.server.controller.ServiceRequest("cluster1", "service1", null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> setRequests = new java.util.HashSet<>();
        setRequests.add(request1);
        org.apache.ambari.server.controller.AmbariManagementControllerImplTest.constructorInit(injector, controllerCapture, null, maintHelper, EasyMock.createStrictMock(org.apache.ambari.server.controller.KerberosHelper.class), null, null);
        EasyMock.expect(clusters.getCluster("cluster1")).andReturn(cluster);
        EasyMock.expect(cluster.getService("service1")).andThrow(new org.apache.ambari.server.ServiceNotFoundException("custer1", "service1"));
        EasyMock.replay(maintHelper, injector, clusters, cluster);
        org.apache.ambari.server.controller.AmbariManagementController controller = new org.apache.ambari.server.controller.AmbariManagementControllerImpl(null, clusters, injector);
        try {
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(controller, setRequests);
            org.junit.Assert.fail("expected ServiceNotFoundException");
        } catch (org.apache.ambari.server.ServiceNotFoundException e) {
        }
        org.junit.Assert.assertSame(controller, controllerCapture.getValue());
        EasyMock.verify(injector, clusters, cluster);
    }

    @org.junit.Test
    public void testGetServices___OR_Predicate_ServiceNotFoundException() throws java.lang.Exception {
        com.google.inject.Injector injector = EasyMock.createStrictMock(com.google.inject.Injector.class);
        org.easymock.Capture<org.apache.ambari.server.controller.AmbariManagementController> controllerCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service1 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.Service service2 = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.controller.ServiceResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.controller.ServiceResponse response2 = EasyMock.createNiceMock(org.apache.ambari.server.controller.ServiceResponse.class);
        org.apache.ambari.server.controller.ServiceRequest request1 = new org.apache.ambari.server.controller.ServiceRequest("cluster1", "service1", null, null, null);
        org.apache.ambari.server.controller.ServiceRequest request2 = new org.apache.ambari.server.controller.ServiceRequest("cluster1", "service2", null, null, null);
        org.apache.ambari.server.controller.ServiceRequest request3 = new org.apache.ambari.server.controller.ServiceRequest("cluster1", "service3", null, null, null);
        org.apache.ambari.server.controller.ServiceRequest request4 = new org.apache.ambari.server.controller.ServiceRequest("cluster1", "service4", null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> setRequests = new java.util.HashSet<>();
        setRequests.add(request1);
        setRequests.add(request2);
        setRequests.add(request3);
        setRequests.add(request4);
        org.apache.ambari.server.controller.AmbariManagementControllerImplTest.constructorInit(injector, controllerCapture, null, maintHelper, EasyMock.createStrictMock(org.apache.ambari.server.controller.KerberosHelper.class), null, null);
        EasyMock.expect(clusters.getCluster("cluster1")).andReturn(cluster).times(4);
        EasyMock.expect(cluster.getService("service1")).andReturn(service1);
        EasyMock.expect(cluster.getService("service2")).andThrow(new org.apache.ambari.server.ServiceNotFoundException("cluster1", "service2"));
        EasyMock.expect(cluster.getService("service3")).andThrow(new org.apache.ambari.server.ServiceNotFoundException("cluster1", "service3"));
        EasyMock.expect(cluster.getService("service4")).andReturn(service2);
        EasyMock.expect(service1.convertToResponse()).andReturn(response);
        EasyMock.expect(service2.convertToResponse()).andReturn(response2);
        EasyMock.replay(maintHelper, injector, clusters, cluster, service1, service2, response, response2);
        org.apache.ambari.server.controller.AmbariManagementController controller = new org.apache.ambari.server.controller.AmbariManagementControllerImpl(null, clusters, injector);
        java.util.Set<org.apache.ambari.server.controller.ServiceResponse> setResponses = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.getServices(controller, setRequests);
        org.junit.Assert.assertSame(controller, controllerCapture.getValue());
        org.junit.Assert.assertEquals(2, setResponses.size());
        org.junit.Assert.assertTrue(setResponses.contains(response));
        org.junit.Assert.assertTrue(setResponses.contains(response2));
        EasyMock.verify(injector, clusters, cluster, service1, service2, response, response2);
    }

    private void testRunSmokeTestFlag(java.util.Map<java.lang.String, java.lang.String> mapRequestProps, org.apache.ambari.server.controller.AmbariManagementController amc, java.util.Set<org.apache.ambari.server.controller.ServiceRequest> serviceRequests) throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.RequestStatusResponse response;
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), "INSTALLED"));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, false, false);
        boolean runSmokeTest = false;
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), "STARTED"));
        response = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, runSmokeTest, false);
        java.util.List<org.apache.ambari.server.controller.ShortTaskStatus> taskStatuses = response.getTasks();
        boolean smokeTestRequired = false;
        for (org.apache.ambari.server.controller.ShortTaskStatus shortTaskStatus : taskStatuses) {
            if (shortTaskStatus.getRole().equals(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.toString())) {
                smokeTestRequired = true;
            }
        }
        org.junit.Assert.assertFalse(smokeTestRequired);
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), "INSTALLED"));
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, false, false);
        runSmokeTest = true;
        serviceRequests.clear();
        serviceRequests.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, "HDFS", repositoryVersion02.getId(), "STARTED"));
        response = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(amc, serviceRequests, mapRequestProps, runSmokeTest, false);
        taskStatuses = response.getTasks();
        smokeTestRequired = false;
        for (org.apache.ambari.server.controller.ShortTaskStatus shortTaskStatus : taskStatuses) {
            if (shortTaskStatus.getRole().equals(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.toString())) {
                smokeTestRequired = true;
            }
        }
        org.junit.Assert.assertTrue(smokeTestRequired);
    }

    private class RolePredicate implements org.apache.commons.collections.Predicate {
        private java.lang.String role;

        public RolePredicate(java.lang.String role) {
            this.role = role;
        }

        @java.lang.Override
        public boolean evaluate(java.lang.Object obj) {
            org.apache.ambari.server.controller.ShortTaskStatus task = ((org.apache.ambari.server.controller.ShortTaskStatus) (obj));
            return task.getRole().equals(role);
        }
    }

    @org.junit.Test
    public void testReinstallClientSchSkippedInMaintenance() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host3 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.Cluster c1 = setupClusterWithHosts(cluster1, "HDP-1.2.0", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
                add(host3);
            }
        }, "centos5");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = repositoryVersion120;
        org.apache.ambari.server.state.Service hdfs = c1.addService("HDFS", repositoryVersion);
        createServiceComponent(cluster1, "HDFS", "NAMENODE", org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, "HDFS", "DATANODE", org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, "HDFS", "HDFS_CLIENT", org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, "HDFS", "NAMENODE", host1, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, "HDFS", "DATANODE", host1, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, "HDFS", "HDFS_CLIENT", host1, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, "HDFS", "HDFS_CLIENT", host2, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, "HDFS", "HDFS_CLIENT", host3, org.apache.ambari.server.state.State.INIT);
        installService(cluster1, "HDFS", false, false);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host3).setMaintenanceState(c1.getClusterId(), org.apache.ambari.server.state.MaintenanceState.ON);
        java.lang.Long id = startService(cluster1, "HDFS", false, true);
        org.junit.Assert.assertNotNull(id);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = actionDB.getAllStages(id);
        org.junit.Assert.assertNotNull(stages);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc1 = null;
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc2 = null;
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc3 = null;
        for (org.apache.ambari.server.actionmanager.Stage s : stages) {
            for (org.apache.ambari.server.actionmanager.HostRoleCommand hrc : s.getOrderedHostRoleCommands()) {
                if (hrc.getRole().equals(org.apache.ambari.server.Role.HDFS_CLIENT) && hrc.getHostName().equals(host1)) {
                    hrc1 = hrc;
                } else if (hrc.getRole().equals(org.apache.ambari.server.Role.HDFS_CLIENT) && hrc.getHostName().equals(host2)) {
                    hrc2 = hrc;
                } else if (hrc.getRole().equals(org.apache.ambari.server.Role.HDFS_CLIENT) && hrc.getHostName().equals(host3)) {
                    hrc3 = hrc;
                }
            }
        }
        org.junit.Assert.assertNotNull(hrc1);
        org.junit.Assert.assertNotNull(hrc2);
        org.junit.Assert.assertNull(hrc3);
    }

    @org.junit.Test
    public void setMonitoringServicesRestartRequired() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.8");
        cluster.setDesiredStackVersion(stackId);
        cluster.setCurrentStackVersion(stackId);
        java.lang.String hdfsService = "HDFS";
        java.lang.String fakeMonitoringService = "FAKENAGIOS";
        createService(cluster1, hdfsService, repositoryVersion208, null);
        createService(cluster1, fakeMonitoringService, repositoryVersion208, null);
        java.lang.String namenode = "NAMENODE";
        java.lang.String datanode = "DATANODE";
        java.lang.String hdfsClient = "HDFS_CLIENT";
        java.lang.String fakeServer = "FAKE_MONITORING_SERVER";
        createServiceComponent(cluster1, hdfsService, namenode, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, hdfsService, datanode, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, fakeMonitoringService, fakeServer, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        createServiceComponentHost(cluster1, hdfsService, namenode, host1, null);
        createServiceComponentHost(cluster1, hdfsService, datanode, host1, null);
        createServiceComponentHost(cluster1, fakeMonitoringService, fakeServer, host1, null);
        org.apache.ambari.server.state.ServiceComponentHost monitoringServiceComponentHost = null;
        for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(host1)) {
            if (sch.getServiceComponentName().equals(fakeServer)) {
                monitoringServiceComponentHost = sch;
            }
        }
        org.junit.Assert.assertFalse(monitoringServiceComponentHost.isRestartRequired());
        createServiceComponent(cluster1, hdfsService, hdfsClient, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, hdfsService, hdfsClient, host1, null);
        org.junit.Assert.assertTrue(monitoringServiceComponentHost.isRestartRequired());
    }

    @org.junit.Test
    public void setRestartRequiredAfterChangeService() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.7");
        cluster.setDesiredStackVersion(stackId);
        cluster.setCurrentStackVersion(stackId);
        java.lang.String hdfsService = "HDFS";
        java.lang.String zookeeperService = "ZOOKEEPER";
        createService(cluster1, hdfsService, repositoryVersion207, null);
        createService(cluster1, zookeeperService, repositoryVersion207, null);
        java.lang.String namenode = "NAMENODE";
        java.lang.String datanode = "DATANODE";
        java.lang.String hdfsClient = "HDFS_CLIENT";
        java.lang.String zookeeperServer = "ZOOKEEPER_SERVER";
        java.lang.String zookeeperClient = "ZOOKEEPER_CLIENT";
        createServiceComponent(cluster1, hdfsService, namenode, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, hdfsService, datanode, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, zookeeperService, zookeeperServer, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, zookeeperService, zookeeperClient, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        createServiceComponentHost(cluster1, hdfsService, namenode, host1, null);
        createServiceComponentHost(cluster1, hdfsService, datanode, host1, null);
        createServiceComponentHost(cluster1, zookeeperService, zookeeperServer, host1, null);
        createServiceComponentHost(cluster1, zookeeperService, zookeeperClient, host1, null);
        org.apache.ambari.server.state.ServiceComponentHost zookeeperSch = null;
        for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(host1)) {
            if (sch.getServiceComponentName().equals(zookeeperServer)) {
                zookeeperSch = sch;
            }
        }
        org.junit.Assert.assertFalse(zookeeperSch.isRestartRequired());
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, zookeeperService, zookeeperClient, host2, null);
        org.junit.Assert.assertFalse(zookeeperSch.isRestartRequired());
        createServiceComponentHost(cluster1, zookeeperService, zookeeperServer, host2, null);
        org.junit.Assert.assertTrue(zookeeperSch.isRestartRequired());
        deleteServiceComponentHost(cluster1, zookeeperService, zookeeperServer, host2, null);
        deleteServiceComponentHost(cluster1, zookeeperService, zookeeperClient, host2, null);
        deleteHost(host2);
        org.junit.Assert.assertTrue(zookeeperSch.isRestartRequired());
    }

    @org.junit.Test
    public void testRestartIndicatorsAndSlaveFilesUpdateAtComponentsDelete() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.7");
        cluster.setDesiredStackVersion(stackId);
        cluster.setCurrentStackVersion(stackId);
        java.lang.String hdfsService = "HDFS";
        java.lang.String zookeeperService = "ZOOKEEPER";
        createService(cluster1, hdfsService, null);
        createService(cluster1, zookeeperService, null);
        java.lang.String namenode = "NAMENODE";
        java.lang.String datanode = "DATANODE";
        java.lang.String zookeeperServer = "ZOOKEEPER_SERVER";
        java.lang.String zookeeperClient = "ZOOKEEPER_CLIENT";
        createServiceComponent(cluster1, hdfsService, namenode, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, hdfsService, datanode, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, zookeeperService, zookeeperServer, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, zookeeperService, zookeeperClient, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        createServiceComponentHost(cluster1, hdfsService, namenode, host1, null);
        createServiceComponentHost(cluster1, hdfsService, datanode, host1, null);
        createServiceComponentHost(cluster1, zookeeperService, zookeeperServer, host1, null);
        createServiceComponentHost(cluster1, zookeeperService, zookeeperClient, host1, null);
        org.apache.ambari.server.state.ServiceComponentHost nameNodeSch = null;
        for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(host1)) {
            if (sch.getServiceComponentName().equals(namenode)) {
                nameNodeSch = sch;
            }
        }
        org.junit.Assert.assertFalse(nameNodeSch.isRestartRequired());
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, hdfsService, datanode, host2, null);
        org.junit.Assert.assertFalse(nameNodeSch.isRestartRequired());
        deleteServiceComponentHost(cluster1, hdfsService, datanode, host2, null);
        deleteHost(host2);
        org.junit.Assert.assertFalse(nameNodeSch.isRestartRequired());
        java.util.List<java.lang.Long> requestIDs = actionDB.getRequestsByStatus(null, 1, false);
        org.apache.ambari.server.actionmanager.Request request = actionDB.getRequest(requestIDs.get(0));
        org.junit.Assert.assertEquals("Update Include/Exclude Files for [HDFS]", request.getRequestContext());
        org.junit.Assert.assertEquals(false, request.isExclusive());
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        java.util.Map<java.lang.String, java.lang.String> requestParams = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(request.getInputs(), type);
        org.junit.Assert.assertEquals(2, requestParams.size());
        org.junit.Assert.assertEquals("true", requestParams.get("is_add_or_delete_slave_request"));
        org.junit.Assert.assertEquals("true", requestParams.get("update_files_only"));
        org.junit.Assert.assertEquals(1, request.getResourceFilters().size());
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = request.getResourceFilters().get(0);
        org.junit.Assert.assertEquals(resourceFilter.getServiceName(), hdfsService);
        org.junit.Assert.assertEquals(resourceFilter.getComponentName(), namenode);
        org.junit.Assert.assertEquals(resourceFilter.getHostNames(), new java.util.ArrayList<java.lang.String>());
    }

    @org.junit.Test
    public void testMaintenanceState() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-1.2.0"));
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put("context", "Called from a test");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> hosts = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHostsForCluster(cluster1);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = org.apache.ambari.server.controller.MaintenanceStateHelperTest.getMaintenanceStateHelperInstance(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters);
        org.apache.ambari.server.controller.ServiceRequest sr = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName, repositoryVersion120.getId(), null);
        sr.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON.name());
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(sr), requestProperties, false, false, maintenanceStateHelper);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, service.getMaintenanceState());
        for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_SERVICE, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(sch));
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, sch.getMaintenanceState());
            }
        }
        sr.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF.name());
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(sr), requestProperties, false, false, maintenanceStateHelper);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, service.getMaintenanceState());
        for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(sch));
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, sch.getMaintenanceState());
            }
        }
        org.apache.ambari.server.controller.HostRequest hr = new org.apache.ambari.server.controller.HostRequest(host1, cluster1);
        hr.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON.name());
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.updateHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(hr));
        org.apache.ambari.server.state.Host host = hosts.get(host1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, host.getMaintenanceState(cluster.getClusterId()));
        for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.apache.ambari.server.state.MaintenanceState implied = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(sch);
                if (sch.getHostName().equals(host1)) {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_HOST, implied);
                } else {
                    org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, implied);
                }
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, sch.getMaintenanceState());
            }
        }
        hr.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF.name());
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.updateHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(hr));
        host = hosts.get(host1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, host.getMaintenanceState(cluster.getClusterId()));
        for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(sch));
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, sch.getMaintenanceState());
            }
        }
        org.apache.ambari.server.controller.HostRequest hr1 = new org.apache.ambari.server.controller.HostRequest(host1, cluster1);
        hr1.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON.name());
        org.apache.ambari.server.controller.HostRequest hr2 = new org.apache.ambari.server.controller.HostRequest(host2, cluster1);
        hr2.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON.name());
        java.util.Set<org.apache.ambari.server.controller.HostRequest> set = new java.util.HashSet<>();
        set.add(hr1);
        set.add(hr2);
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.updateHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set);
        host = hosts.get(host1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, host.getMaintenanceState(cluster.getClusterId()));
        host = hosts.get(host2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, host.getMaintenanceState(cluster.getClusterId()));
        hr1 = new org.apache.ambari.server.controller.HostRequest(host1, cluster1);
        hr1.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF.name());
        hr2 = new org.apache.ambari.server.controller.HostRequest(host2, cluster1);
        hr2.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF.name());
        set = new java.util.HashSet<>();
        set.add(hr1);
        set.add(hr2);
        org.apache.ambari.server.controller.internal.HostResourceProviderTest.updateHosts(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, set);
        host = hosts.get(host1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, host.getMaintenanceState(cluster.getClusterId()));
        host = hosts.get(host2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, host.getMaintenanceState(cluster.getClusterId()));
        org.apache.ambari.server.state.ServiceComponentHost targetSch = service.getServiceComponent(componentName2).getServiceComponentHosts().get(host2);
        org.junit.Assert.assertNotNull(targetSch);
        targetSch.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(targetSch));
        service.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(targetSch));
        targetSch.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_SERVICE, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(targetSch));
        service.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(targetSch));
        host = hosts.get(host2);
        host.setMaintenanceState(cluster.getClusterId(), org.apache.ambari.server.state.MaintenanceState.ON);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.IMPLIED_FROM_HOST, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(targetSch));
        targetSch.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getEffectiveMaintenanceState(targetSch));
        for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, sch.getState());
            }
        }
        long id1 = installService(cluster1, serviceName, false, false, maintenanceStateHelper, null);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hdfsCmds = actionDB.getRequestTasks(id1);
        org.junit.Assert.assertNotNull(hdfsCmds);
        org.apache.ambari.server.actionmanager.HostRoleCommand datanodeCmd = null;
        for (org.apache.ambari.server.actionmanager.HostRoleCommand cmd : hdfsCmds) {
            if (cmd.getRole().equals(org.apache.ambari.server.Role.DATANODE)) {
                datanodeCmd = cmd;
            }
        }
        org.junit.Assert.assertNotNull(datanodeCmd);
        for (org.apache.ambari.server.state.ServiceComponent sc : service.getServiceComponents().values()) {
            if (!sc.getName().equals(componentName2)) {
                continue;
            }
            for (org.apache.ambari.server.state.ServiceComponentHost sch : sc.getServiceComponentHosts().values()) {
                org.junit.Assert.assertEquals(sch == targetSch ? org.apache.ambari.server.state.State.INIT : org.apache.ambari.server.state.State.INSTALLED, sch.getState());
            }
        }
    }

    @org.junit.Test
    public void testCredentialStoreRelatedAPICallsToUpdateSettings() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.2.0"));
        java.lang.String service1Name = "HDFS";
        java.lang.String service2Name = "STORM";
        java.lang.String service3Name = "ZOOKEEPER";
        createService(cluster1, service1Name, repositoryVersion220, null);
        createService(cluster1, service2Name, repositoryVersion220, null);
        createService(cluster1, service3Name, repositoryVersion220, null);
        java.lang.String component1Name = "NAMENODE";
        java.lang.String component2Name = "DRPC_SERVER";
        java.lang.String component3Name = "ZOOKEEPER_SERVER";
        createServiceComponent(cluster1, service1Name, component1Name, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, service2Name, component2Name, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, service3Name, component3Name, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        createServiceComponentHost(cluster1, service1Name, component1Name, host1, null);
        createServiceComponentHost(cluster1, service2Name, component2Name, host1, null);
        createServiceComponentHost(cluster1, service3Name, component3Name, host1, null);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put("context", "Called from a test");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        org.apache.ambari.server.state.Service service1 = cluster.getService(service1Name);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = org.apache.ambari.server.controller.MaintenanceStateHelperTest.getMaintenanceStateHelperInstance(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters);
        org.apache.ambari.server.controller.ServiceRequest sr = new org.apache.ambari.server.controller.ServiceRequest(cluster1, service1Name, repositoryVersion220.getId(), null);
        sr.setCredentialStoreEnabled("true");
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(sr), requestProperties, false, false, maintenanceStateHelper);
        org.junit.Assert.assertTrue(service1.isCredentialStoreEnabled());
        org.junit.Assert.assertTrue(service1.isCredentialStoreSupported());
        org.junit.Assert.assertFalse(service1.isCredentialStoreRequired());
        org.apache.ambari.server.controller.ServiceRequest sr2 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, service2Name, repositoryVersion220.getId(), null);
        sr2.setCredentialStoreEnabled("true");
        try {
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(sr2), requestProperties, false, false, maintenanceStateHelper);
            org.junit.Assert.assertTrue("Expected exception not thrown - service does not support cred store", true);
        } catch (java.lang.IllegalArgumentException iaex) {
            org.junit.Assert.assertTrue(iaex.getMessage(), iaex.getMessage().contains("Invalid arguments, cannot enable credential store as it is not supported by the service. Service=STORM"));
        }
        org.apache.ambari.server.controller.ServiceRequest sr3 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, service3Name, repositoryVersion220.getId(), null);
        sr3.setCredentialStoreEnabled("false");
        try {
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(sr3), requestProperties, false, false, maintenanceStateHelper);
            org.junit.Assert.assertTrue("Expected exception not thrown - service does not support disabling of cred store", true);
        } catch (java.lang.IllegalArgumentException iaex) {
            org.junit.Assert.assertTrue(iaex.getMessage(), iaex.getMessage().contains("Invalid arguments, cannot disable credential store as it is required by the service. Service=ZOOKEEPER"));
        }
        org.apache.ambari.server.controller.ServiceRequest sr4 = new org.apache.ambari.server.controller.ServiceRequest(cluster1, service3Name, repositoryVersion220.getId(), null);
        sr4.setCredentialStoreSupported("true");
        try {
            org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(sr4), requestProperties, false, false, maintenanceStateHelper);
            org.junit.Assert.assertTrue("Expected exception not thrown - service does not support updating cred store support", true);
        } catch (java.lang.IllegalArgumentException iaex) {
            org.junit.Assert.assertTrue(iaex.getMessage(), iaex.getMessage().contains("Invalid arguments, cannot update credential_store_supported as it is set only via service definition. Service=ZOOKEEPER"));
        }
    }

    @org.junit.Test
    public void testPassiveSkipServices() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName1 = "HDFS";
        java.lang.String serviceName2 = "MAPREDUCE";
        createService(cluster1, serviceName1, null);
        createService(cluster1, serviceName2, null);
        java.lang.String componentName1_1 = "NAMENODE";
        java.lang.String componentName1_2 = "DATANODE";
        java.lang.String componentName1_3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName1, componentName1_1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName1_2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName1, componentName1_3, org.apache.ambari.server.state.State.INIT);
        java.lang.String componentName2_1 = "JOBTRACKER";
        java.lang.String componentName2_2 = "TASKTRACKER";
        createServiceComponent(cluster1, serviceName2, componentName2_1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName2, componentName2_2, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHostToCluster(host1, cluster1);
        addHostToCluster(host2, cluster1);
        createServiceComponentHost(cluster1, serviceName1, componentName1_1, host1, null);
        createServiceComponentHost(cluster1, serviceName1, componentName1_2, host1, null);
        createServiceComponentHost(cluster1, serviceName1, componentName1_2, host2, null);
        createServiceComponentHost(cluster1, serviceName2, componentName2_1, host1, null);
        createServiceComponentHost(cluster1, serviceName2, componentName2_2, host2, null);
        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = org.apache.ambari.server.controller.MaintenanceStateHelperTest.getMaintenanceStateHelperInstance(org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters);
        installService(cluster1, serviceName1, false, false, maintenanceStateHelper, null);
        installService(cluster1, serviceName2, false, false, maintenanceStateHelper, null);
        startService(cluster1, serviceName1, false, false, maintenanceStateHelper);
        startService(cluster1, serviceName2, false, false, maintenanceStateHelper);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put("context", "Called from a test");
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, service.getDesiredState());
        }
        org.apache.ambari.server.state.Service service2 = cluster.getService(serviceName2);
        service2.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> srs = new java.util.HashSet<>();
        srs.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.name()));
        srs.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName2, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.name()));
        org.apache.ambari.server.controller.RequestStatusResponse rsr = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, srs, requestProperties, false, false, maintenanceStateHelper);
        for (org.apache.ambari.server.controller.ShortTaskStatus sts : rsr.getTasks()) {
            java.lang.String role = sts.getRole();
            org.junit.Assert.assertFalse(role.equals(componentName2_1));
            org.junit.Assert.assertFalse(role.equals(componentName2_2));
        }
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            if (service.getName().equals(serviceName2)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, service.getDesiredState());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, service.getDesiredState());
            }
        }
        service2.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, srs, requestProperties, false, false, maintenanceStateHelper);
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, service.getDesiredState());
        }
        startService(cluster1, serviceName1, false, false, maintenanceStateHelper);
        startService(cluster1, serviceName2, false, false, maintenanceStateHelper);
        org.apache.ambari.server.state.Host h1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getHost(host1);
        h1.setMaintenanceState(cluster.getClusterId(), org.apache.ambari.server.state.MaintenanceState.ON);
        srs = new java.util.HashSet<>();
        srs.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName1, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.name()));
        srs.add(new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName2, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.name()));
        rsr = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, srs, requestProperties, false, false, maintenanceStateHelper);
        for (org.apache.ambari.server.controller.ShortTaskStatus sts : rsr.getTasks()) {
            org.junit.Assert.assertFalse(sts.getHostName().equals(host1));
        }
        h1.setMaintenanceState(cluster.getClusterId(), org.apache.ambari.server.state.MaintenanceState.OFF);
        startService(cluster1, serviceName2, false, false, maintenanceStateHelper);
        service2.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.apache.ambari.server.controller.ServiceRequest sr = new org.apache.ambari.server.controller.ServiceRequest(cluster1, serviceName2, repositoryVersion01.getId(), org.apache.ambari.server.state.State.INSTALLED.name());
        rsr = org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.updateServices(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, java.util.Collections.singleton(sr), requestProperties, false, false, maintenanceStateHelper);
        org.junit.Assert.assertTrue("Service start request defaults to Cluster operation level," + "command does not create tasks", (rsr == null) || (rsr.getTasks().size() == 0));
    }

    @org.junit.Test
    public void testIsAttributeMapsEqual() {
        org.apache.ambari.server.controller.AmbariManagementControllerImpl controllerImpl = null;
        if (org.apache.ambari.server.controller.AmbariManagementControllerTest.controller instanceof org.apache.ambari.server.controller.AmbariManagementControllerImpl) {
            controllerImpl = ((org.apache.ambari.server.controller.AmbariManagementControllerImpl) (org.apache.ambari.server.controller.AmbariManagementControllerTest.controller));
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> requestConfigAttributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterConfigAttributes = new java.util.HashMap<>();
        org.junit.Assert.assertTrue(controllerImpl.isAttributeMapsEqual(requestConfigAttributes, clusterConfigAttributes));
        requestConfigAttributes.put("final", new java.util.HashMap<>());
        requestConfigAttributes.get("final").put("c", "true");
        clusterConfigAttributes.put("final", new java.util.HashMap<>());
        clusterConfigAttributes.get("final").put("c", "true");
        org.junit.Assert.assertTrue(controllerImpl.isAttributeMapsEqual(requestConfigAttributes, clusterConfigAttributes));
        clusterConfigAttributes.put("final2", new java.util.HashMap<>());
        clusterConfigAttributes.get("final2").put("a", "true");
        org.junit.Assert.assertFalse(controllerImpl.isAttributeMapsEqual(requestConfigAttributes, clusterConfigAttributes));
        requestConfigAttributes.put("final2", new java.util.HashMap<>());
        requestConfigAttributes.get("final2").put("a", "false");
        org.junit.Assert.assertFalse(controllerImpl.isAttributeMapsEqual(requestConfigAttributes, clusterConfigAttributes));
    }

    @org.junit.Test
    public void testEmptyConfigs() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        createCluster(cluster1);
        org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.controller.ClusterRequest cr = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster.getClusterName(), null, null);
        cr.setDesiredConfig(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v1", null, null)));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(cr), new java.util.HashMap<>());
        org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType("typeA");
        org.junit.Assert.assertNull(config);
        cr.setDesiredConfig(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v1", new java.util.HashMap<>(), new java.util.HashMap<>())));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(cr), new java.util.HashMap<>());
        config = cluster.getDesiredConfigByType("typeA");
        org.junit.Assert.assertNotNull(config);
        cr.setDesiredConfig(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v2", new java.util.HashMap<>(), new java.util.HashMap<>())));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(cr), new java.util.HashMap<>());
        config = cluster.getDesiredConfigByType("typeA");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(0), java.lang.Integer.valueOf(config.getProperties().size()));
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.clear();
        map.put("c", "d");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributesMap = new java.util.HashMap<>();
        attributesMap.put("final", new java.util.HashMap<>());
        attributesMap.get("final").put("c", "true");
        cr.setDesiredConfig(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v3", map, attributesMap)));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(cr), new java.util.HashMap<>());
        config = cluster.getDesiredConfigByType("typeA");
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertTrue(config.getProperties().containsKey("c"));
        cr.setDesiredConfig(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v2", new java.util.HashMap<>(), new java.util.HashMap<>())));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(cr), new java.util.HashMap<>());
        config = cluster.getDesiredConfigByType("typeA");
        org.junit.Assert.assertEquals("v2", config.getTag());
        org.junit.Assert.assertNotNull(config);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(0), java.lang.Integer.valueOf(config.getProperties().size()));
        cr.setDesiredConfig(java.util.Collections.singletonList(new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "typeA", "v2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("final", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("a", "true");
                    }
                });
            }
        })));
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(cr), new java.util.HashMap<>());
            org.junit.Assert.fail("Expect failure when creating a config that exists");
        } catch (java.lang.Exception e) {
        }
    }

    @org.junit.Test
    public void testCreateCustomActionNoCluster() throws java.lang.Exception {
        java.lang.String hostname1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String hostname2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        addHost(hostname1);
        addHost(hostname2);
        java.lang.String action1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.addActionDefinition(new org.apache.ambari.server.customactions.ActionDefinition(action1, org.apache.ambari.server.actionmanager.ActionType.SYSTEM, "", "", "", "action def description", org.apache.ambari.server.actionmanager.TargetHostType.ANY, java.lang.Integer.valueOf("60"), null));
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY, "Called from a test");
        java.util.Map<java.lang.String, java.lang.String> requestParams = new java.util.HashMap<>();
        requestParams.put("some_custom_param", "abc");
        java.util.List<java.lang.String> hosts = java.util.Arrays.asList(hostname1);
        org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter(null, null, hosts);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<>();
        resourceFilters.add(resourceFilter);
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(null, null, action1, resourceFilters, null, requestParams, false);
        org.apache.ambari.server.controller.RequestStatusResponse response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(1, response.getTasks().size());
        org.apache.ambari.server.controller.ShortTaskStatus taskStatus = response.getTasks().get(0);
        org.junit.Assert.assertEquals(hostname1, taskStatus.getHostName());
        org.apache.ambari.server.actionmanager.Stage stage = actionDB.getAllStages(response.getRequestId()).get(0);
        org.junit.Assert.assertNotNull(stage);
        org.junit.Assert.assertEquals(-1L, stage.getClusterId());
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.junit.Assert.assertEquals(1, storedTasks.size());
        org.apache.ambari.server.actionmanager.HostRoleCommand task = storedTasks.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, task.getRoleCommand());
        org.junit.Assert.assertEquals(action1, task.getRole().name());
        org.junit.Assert.assertEquals(hostname1, task.getHostName());
        org.apache.ambari.server.agent.ExecutionCommand cmd = task.getExecutionCommandWrapper().getExecutionCommand();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.lang.String>>() {}.getType();
        java.util.Map<java.lang.String, java.lang.String> commandParamsStage = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getCommandParamsStage(), type);
        org.junit.Assert.assertTrue(commandParamsStage.containsKey("some_custom_param"));
        org.junit.Assert.assertEquals(null, cmd.getServiceName());
        org.junit.Assert.assertEquals(null, cmd.getComponentName());
        org.junit.Assert.assertTrue(cmd.getLocalComponents().isEmpty());
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
        hosts = java.util.Arrays.asList(hostname1, hostname2);
        resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter(null, null, hosts);
        resourceFilters = new java.util.ArrayList<>();
        resourceFilters.add(resourceFilter);
        actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest(null, null, action1, resourceFilters, null, requestParams, false);
        response = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createAction(actionRequest, requestProperties);
        org.junit.Assert.assertEquals(2, response.getTasks().size());
        boolean host1Found = false;
        boolean host2Found = false;
        for (org.apache.ambari.server.controller.ShortTaskStatus sts : response.getTasks()) {
            if (sts.getHostName().equals(hostname1)) {
                host1Found = true;
            } else if (sts.getHostName().equals(hostname2)) {
                host2Found = true;
            }
        }
        org.junit.Assert.assertTrue(host1Found);
        org.junit.Assert.assertTrue(host2Found);
        stage = actionDB.getAllStages(response.getRequestId()).get(0);
        org.junit.Assert.assertNotNull(stage);
        org.junit.Assert.assertEquals(-1L, stage.getClusterId());
        storedTasks = actionDB.getRequestTasks(response.getRequestId());
        org.junit.Assert.assertEquals(2, storedTasks.size());
        task = storedTasks.get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, task.getRoleCommand());
        org.junit.Assert.assertEquals(action1, task.getRole().name());
        org.junit.Assert.assertTrue(hostname1.equals(task.getHostName()) || hostname2.equals(task.getHostName()));
        cmd = task.getExecutionCommandWrapper().getExecutionCommand();
        commandParamsStage = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(stage.getCommandParamsStage(), type);
        org.junit.Assert.assertTrue(commandParamsStage.containsKey("some_custom_param"));
        org.junit.Assert.assertEquals(null, cmd.getServiceName());
        org.junit.Assert.assertEquals(null, cmd.getComponentName());
        org.junit.Assert.assertTrue(cmd.getLocalComponents().isEmpty());
        org.junit.Assert.assertEquals(requestProperties.get(org.apache.ambari.server.controller.AmbariManagementControllerTest.REQUEST_CONTEXT_PROPERTY), response.getRequestContext());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testConfigAttributesStaleConfigFilter() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.Cluster c = setupClusterWithHosts(cluster1, "HDP-2.0.5", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        }, "centos5");
        java.lang.Long clusterId = c.getClusterId();
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        installService(cluster1, serviceName, false, false);
        java.util.Map<java.lang.String, java.lang.String> hdfsConfigs = new java.util.HashMap<>();
        hdfsConfigs.put("a", "b");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hdfsConfigAttributes = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("final", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("a", "true");
                    }
                });
            }
        };
        org.apache.ambari.server.controller.ConfigurationRequest cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", hdfsConfigs, hdfsConfigAttributes);
        org.apache.ambari.server.controller.ClusterRequest crReq1 = new org.apache.ambari.server.controller.ClusterRequest(clusterId, cluster1, null, null);
        crReq1.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq1), null);
        startService(cluster1, serviceName, false, false);
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> actualConfig = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version1");
                    }
                });
            }
        };
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> actualConfigOld = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version0");
                    }
                });
            }
        };
        org.apache.ambari.server.state.Service s1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName);
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostResponse> resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(5, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        r.setStaleConfig("true");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, null, null);
        r.setStaleConfig("false");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(3, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, host1, null);
        r.setStaleConfig("false");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(2, resps.size());
        r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, null, null, host2, null);
        r.setStaleConfig("true");
        resps = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getHostComponents(java.util.Collections.singleton(r));
        org.junit.Assert.assertEquals(1, resps.size());
    }

    @org.junit.Test
    public void testSecretReferences() throws java.lang.Exception, org.apache.ambari.server.security.authorization.AuthorizationException {
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        final java.lang.String host2 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.Cluster cl = setupClusterWithHosts(cluster1, "HDP-2.0.5", new java.util.ArrayList<java.lang.String>() {
            {
                add(host1);
                add(host2);
            }
        }, "centos5");
        java.lang.Long clusterId = cl.getClusterId();
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(cluster1, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host1, null);
        createServiceComponentHost(cluster1, serviceName, componentName2, host2, null);
        createServiceComponentHost(cluster1, serviceName, componentName3, host2, null);
        installService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.ClusterRequest crReq;
        org.apache.ambari.server.controller.ConfigurationRequest cr;
        cr = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test.password", "first");
                put("test.password.empty", "");
            }
        }, new java.util.HashMap<>());
        crReq = new org.apache.ambari.server.controller.ClusterRequest(clusterId, cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        cr = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test.password", "SECRET:hdfs-site:1:test.password");
                put("new", "new");
            }
        }, new java.util.HashMap<>());
        crReq = new org.apache.ambari.server.controller.ClusterRequest(clusterId, cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        cr = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version3", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test.password", "brandNewPassword");
            }
        }, new java.util.HashMap<>());
        crReq = new org.apache.ambari.server.controller.ClusterRequest(clusterId, cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        cr = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version3", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test.password", "SECRET:hdfs-site:666:test.password");
            }
        }, new java.util.HashMap<>());
        crReq = new org.apache.ambari.server.controller.ClusterRequest(clusterId, cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr));
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
            org.junit.Assert.fail("Request need to be failed with wrong secret reference");
        } catch (java.lang.Exception e) {
        }
        cr = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version4", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("foo", "bar");
            }
        }, new java.util.HashMap<>());
        crReq = new org.apache.ambari.server.controller.ClusterRequest(clusterId, cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        cr = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version5", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("test.password", "SECRET:hdfs-site:4:test.password");
                put("new", "new");
            }
        }, new java.util.HashMap<>());
        crReq = new org.apache.ambari.server.controller.ClusterRequest(clusterId, cluster1, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr));
        try {
            org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
            org.junit.Assert.fail("Request need to be failed with wrong secret reference");
        } catch (java.lang.Exception e) {
            org.junit.Assert.assertEquals(("Error when parsing secret reference. Cluster: " + cluster1) + " ConfigType: hdfs-site ConfigVersion: 4 does not contain property 'test.password'", e.getMessage());
        }
        cl.getAllConfigs();
        org.junit.Assert.assertEquals(cl.getAllConfigs().size(), 4);
        org.apache.ambari.server.state.Config v1 = cl.getConfigByVersion("hdfs-site", 1L);
        org.apache.ambari.server.state.Config v2 = cl.getConfigByVersion("hdfs-site", 2L);
        org.apache.ambari.server.state.Config v3 = cl.getConfigByVersion("hdfs-site", 3L);
        org.apache.ambari.server.state.Config v4 = cl.getConfigByVersion("hdfs-site", 4L);
        org.junit.Assert.assertEquals(v1.getProperties().get("test.password"), "first");
        org.junit.Assert.assertEquals(v2.getProperties().get("test.password"), "first");
        org.junit.Assert.assertEquals(v3.getProperties().get("test.password"), "brandNewPassword");
        org.junit.Assert.assertFalse(v4.getProperties().containsKey("test.password"));
        final org.apache.ambari.server.controller.ConfigurationRequest configRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", null, null, null);
        configRequest.setIncludeProperties(true);
        java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> requestedConfigs = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getConfigurations(new java.util.HashSet<org.apache.ambari.server.controller.ConfigurationRequest>() {
            {
                add(configRequest);
            }
        });
        for (org.apache.ambari.server.controller.ConfigurationResponse resp : requestedConfigs) {
            java.lang.String secretName = ("SECRET:hdfs-site:" + resp.getVersion()) + ":test.password";
            if (resp.getConfigs().containsKey("test.password")) {
                org.junit.Assert.assertEquals(resp.getConfigs().get("test.password"), secretName);
            }
            if (resp.getConfigs().containsKey("test.password.empty")) {
                org.junit.Assert.assertEquals(resp.getConfigs().get("test.password.empty"), "");
            }
        }
    }

    @org.junit.Test
    public void testTargetedProcessCommand() throws java.lang.Exception {
        final java.lang.String host1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.state.Cluster cluster = setupClusterWithHosts(cluster1, "HDP-2.0.5", java.util.Arrays.asList(host1), "centos5");
        java.lang.String serviceName = "HDFS";
        createService(cluster1, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        createServiceComponent(cluster1, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(cluster1, serviceName, componentName1, host1, null);
        installService(cluster1, serviceName, false, false);
        java.util.Map<java.lang.String, java.lang.String> hdfsConfigs = new java.util.HashMap<>();
        hdfsConfigs.put("a", "b");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hdfsConfigAttributes = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("final", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("a", "true");
                    }
                });
            }
        };
        org.apache.ambari.server.controller.ConfigurationRequest cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(cluster1, "hdfs-site", "version1", hdfsConfigs, hdfsConfigAttributes);
        org.apache.ambari.server.controller.ClusterRequest crReq1 = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), cluster1, null, null);
        crReq1.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.updateClusters(java.util.Collections.singleton(crReq1), null);
        startService(cluster1, serviceName, false, false);
        org.apache.ambari.server.controller.ServiceComponentHostRequest req = new org.apache.ambari.server.controller.ServiceComponentHostRequest(cluster1, serviceName, componentName1, host1, "INSTALLED");
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<>();
        requestProperties.put("namenode", "p1");
        org.apache.ambari.server.controller.RequestStatusResponse resp = updateHostComponents(java.util.Collections.singleton(req), requestProperties, false);
        org.junit.Assert.assertNotNull(resp);
        for (org.apache.ambari.server.state.ServiceComponentHost sch : org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getServiceComponentHosts(host1)) {
            sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        }
        resp = updateHostComponents(java.util.Collections.singleton(req), new java.util.HashMap<>(), false);
        org.junit.Assert.assertNull(resp);
        resp = updateHostComponents(java.util.Collections.singleton(req), requestProperties, false);
        org.junit.Assert.assertNotNull(resp);
    }

    @org.junit.Test
    public void testGetPackagesForServiceHost() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo service = org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.getStack("HDP", "2.0.1").getService("HIVE");
        java.util.HashMap<java.lang.String, java.lang.String> hostParams = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceOsSpecific.Package> packages = new java.util.HashMap<>();
        java.lang.String[] packageNames = new java.lang.String[]{ "hive", "mysql-connector-java", "mysql", "mysql-server", "mysql-client" };
        for (java.lang.String packageName : packageNames) {
            org.apache.ambari.server.state.ServiceOsSpecific.Package pkg = new org.apache.ambari.server.state.ServiceOsSpecific.Package();
            pkg.setName(packageName);
            packages.put(packageName, pkg);
        }
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> rhel5Packages = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getPackagesForServiceHost(service, hostParams, "redhat5");
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> expectedRhel5 = java.util.Arrays.asList(packages.get("hive"), packages.get("mysql-connector-java"), packages.get("mysql"), packages.get("mysql-server"));
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> sles11Packages = org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.getPackagesForServiceHost(service, hostParams, "suse11");
        java.util.List<org.apache.ambari.server.state.ServiceOsSpecific.Package> expectedSles11 = java.util.Arrays.asList(packages.get("hive"), packages.get("mysql-connector-java"), packages.get("mysql"), packages.get("mysql-client"));
        org.junit.Assert.assertThat(rhel5Packages, org.hamcrest.CoreMatchers.is(expectedRhel5));
        org.junit.Assert.assertThat(sles11Packages, org.hamcrest.CoreMatchers.is(expectedSles11));
    }

    @org.junit.Test
    public void testServiceWidgetCreationOnServiceCreate() throws java.lang.Exception {
        java.lang.String cluster1 = org.apache.ambari.server.controller.AmbariManagementControllerTest.getUniqueName();
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, cluster1, org.apache.ambari.server.state.State.INSTALLED.name(), org.apache.ambari.server.state.SecurityType.NONE, "OTHER-2.0", null);
        org.apache.ambari.server.controller.AmbariManagementControllerTest.controller.createCluster(r);
        java.lang.String serviceName = "HBASE";
        org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).setDesiredStackVersion(new org.apache.ambari.server.state.StackId("OTHER-2.0"));
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(new org.apache.ambari.server.state.StackId("OTHER-2.0"), "2.0-1234");
        createService(cluster1, serviceName, repositoryVersion, org.apache.ambari.server.state.State.INIT);
        org.apache.ambari.server.state.Service s = org.apache.ambari.server.controller.AmbariManagementControllerTest.clusters.getCluster(cluster1).getService(serviceName);
        org.junit.Assert.assertNotNull(s);
        org.junit.Assert.assertEquals(serviceName, s.getName());
        org.junit.Assert.assertEquals(cluster1, s.getCluster().getClusterName());
        org.apache.ambari.server.orm.dao.WidgetDAO widgetDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.WidgetDAO.class);
        org.apache.ambari.server.orm.dao.WidgetLayoutDAO widgetLayoutDAO = org.apache.ambari.server.controller.AmbariManagementControllerTest.injector.getInstance(org.apache.ambari.server.orm.dao.WidgetLayoutDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.WidgetEntity> widgetEntities = widgetDAO.findAll();
        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutEntity> layoutEntities = widgetLayoutDAO.findAll();
        org.junit.Assert.assertNotNull(widgetEntities);
        org.junit.Assert.assertFalse(widgetEntities.isEmpty());
        org.junit.Assert.assertNotNull(layoutEntities);
        org.junit.Assert.assertFalse(layoutEntities.isEmpty());
        org.apache.ambari.server.orm.entities.WidgetEntity candidateVisibleEntity = null;
        for (org.apache.ambari.server.orm.entities.WidgetEntity entity : widgetEntities) {
            if (entity.getWidgetName().equals("OPEN_CONNECTIONS")) {
                candidateVisibleEntity = entity;
            }
        }
        org.junit.Assert.assertNotNull(candidateVisibleEntity);
        org.junit.Assert.assertEquals("GRAPH", candidateVisibleEntity.getWidgetType());
        org.junit.Assert.assertEquals("ambari", candidateVisibleEntity.getAuthor());
        org.junit.Assert.assertEquals("CLUSTER", candidateVisibleEntity.getScope());
        org.junit.Assert.assertNotNull(candidateVisibleEntity.getMetrics());
        org.junit.Assert.assertNotNull(candidateVisibleEntity.getProperties());
        org.junit.Assert.assertNotNull(candidateVisibleEntity.getWidgetValues());
        org.apache.ambari.server.orm.entities.WidgetLayoutEntity candidateLayoutEntity = null;
        for (org.apache.ambari.server.orm.entities.WidgetLayoutEntity entity : layoutEntities) {
            if (entity.getLayoutName().equals("default_hbase_layout")) {
                candidateLayoutEntity = entity;
            }
        }
        org.junit.Assert.assertNotNull(candidateLayoutEntity);
        java.util.List<org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity> layoutUserWidgetEntities = candidateLayoutEntity.getListWidgetLayoutUserWidgetEntity();
        org.junit.Assert.assertNotNull(layoutUserWidgetEntities);
        org.junit.Assert.assertEquals(4, layoutUserWidgetEntities.size());
        org.junit.Assert.assertEquals("RS_READS_WRITES", layoutUserWidgetEntities.get(0).getWidget().getWidgetName());
        org.junit.Assert.assertEquals("OPEN_CONNECTIONS", layoutUserWidgetEntities.get(1).getWidget().getWidgetName());
        org.junit.Assert.assertEquals("FILES_LOCAL", layoutUserWidgetEntities.get(2).getWidget().getWidgetName());
        org.junit.Assert.assertEquals("UPDATED_BLOCKED_TIME", layoutUserWidgetEntities.get(3).getWidget().getWidgetName());
        org.junit.Assert.assertEquals("HBASE_SUMMARY", layoutUserWidgetEntities.get(0).getWidget().getDefaultSectionName());
        java.io.File widgetsFile = org.apache.ambari.server.controller.AmbariManagementControllerTest.ambariMetaInfo.getCommonWidgetsDescriptorFile();
        org.junit.Assert.assertNotNull(widgetsFile);
        org.junit.Assert.assertEquals("src/test/resources/widgets.json", widgetsFile.getPath());
        org.junit.Assert.assertTrue(widgetsFile.exists());
        candidateLayoutEntity = null;
        for (org.apache.ambari.server.orm.entities.WidgetLayoutEntity entity : layoutEntities) {
            if (entity.getLayoutName().equals("default_system_heatmap")) {
                candidateLayoutEntity = entity;
                break;
            }
        }
        org.junit.Assert.assertNotNull(candidateLayoutEntity);
        org.junit.Assert.assertEquals("ambari", candidateVisibleEntity.getAuthor());
        org.junit.Assert.assertEquals("CLUSTER", candidateVisibleEntity.getScope());
    }

    private org.apache.ambari.server.controller.RequestStatusResponse updateHostComponents(java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest) throws java.lang.Exception {
        return updateHostComponents(org.apache.ambari.server.controller.AmbariManagementControllerTest.controller, requests, requestProperties, runSmokeTest);
    }

    private org.apache.ambari.server.controller.RequestStatusResponse updateHostComponents(org.apache.ambari.server.controller.AmbariManagementController controller, java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests, java.util.Map<java.lang.String, java.lang.String> requestProperties, boolean runSmokeTest) throws java.lang.Exception {
        return org.apache.ambari.server.controller.internal.HostComponentResourceProviderTest.updateHostComponents(controller, org.apache.ambari.server.controller.AmbariManagementControllerTest.injector, requests, requestProperties, runSmokeTest);
    }
}