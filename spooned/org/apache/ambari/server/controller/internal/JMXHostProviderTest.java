package org.apache.ambari.server.controller.internal;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class JMXHostProviderTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    static org.apache.ambari.server.controller.AmbariManagementController controller;

    private static final java.lang.String NAMENODE_PORT_V1 = "dfs.http.address";

    private static final java.lang.String NAMENODE_PORT_V2 = "dfs.namenode.http-address";

    private static final java.lang.String DATANODE_PORT = "dfs.datanode.http.address";

    private static final java.lang.String DATANODE_HTTPS_PORT = "dfs.datanode.https.address";

    private static final java.lang.String RESOURCEMANAGER_PORT = "yarn.resourcemanager.webapp.address";

    private static final java.lang.String RESOURCEMANAGER_HTTPS_PORT = "yarn.resourcemanager.webapp.https.address";

    private static final java.lang.String YARN_HTTPS_POLICY = "yarn.http.policy";

    private static final java.lang.String NODEMANAGER_PORT = "yarn.nodemanager.webapp.address";

    private static final java.lang.String NODEMANAGER_HTTPS_PORT = "yarn.nodemanager.webapp.https.address";

    private static final java.lang.String JOURNALNODE_HTTPS_PORT = "dfs.journalnode.https-address";

    private static final java.lang.String HDFS_HTTPS_POLICY = "dfs.http.policy";

    private static final java.lang.String MAPREDUCE_HTTPS_POLICY = "mapreduce.jobhistory.http.policy";

    private static final java.lang.String MAPREDUCE_HTTPS_PORT = "mapreduce.jobhistory.webapp.https.address";

    private final java.lang.String STACK_VERSION = "2.0.6";

    private final java.lang.String REPO_VERSION = "2.0.6-1234";

    private final org.apache.ambari.server.state.StackId STACK_ID = new org.apache.ambari.server.state.StackId("HDP", STACK_VERSION);

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersion;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.orm.OrmTestHelper ormTestHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        m_repositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(STACK_ID, REPO_VERSION);
        org.junit.Assert.assertNotNull(m_repositoryVersion);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    private void createService(java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceRequest r1 = new org.apache.ambari.server.controller.ServiceRequest(clusterName, serviceName, m_repositoryVersion.getId(), dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r1);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller, injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class), requests);
    }

    private void createServiceComponent(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentRequest r = new org.apache.ambari.server.controller.ServiceComponentRequest(clusterName, serviceName, componentName, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller, requests);
    }

    private void createServiceComponentHost(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName, hostname, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.createHostComponents(requests);
    }

    private void createHDFSServiceConfigs(boolean version1) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.lang.String clusterName = "c1";
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, "HDP-0.1", null);
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.createCluster(r);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-0.1"));
        java.lang.String serviceName = "HDFS";
        createService(clusterName, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(clusterName, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = "h1";
        clusters.addHost(host1);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "5.9");
        clusters.getHost("h1").setHostAttributes(hostAttributes);
        java.lang.String host2 = "h2";
        clusters.addHost(host2);
        hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        clusters.getHost("h2").setHostAttributes(hostAttributes);
        clusters.mapHostToCluster(host1, clusterName);
        clusters.mapHostToCluster(host2, clusterName);
        clusters.updateHostMappings(clusters.getHost(host1));
        clusters.updateHostMappings(clusters.getHost(host2));
        createServiceComponentHost(clusterName, null, componentName1, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName2, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName2, host2, null);
        createServiceComponentHost(clusterName, serviceName, componentName3, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName3, host2, null);
        if (version1) {
            java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
            configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.NAMENODE_PORT_V1, "localhost:${ambari.dfs.datanode.http.port}");
            configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.DATANODE_PORT, "localhost:70075");
            configs.put("ambari.dfs.datanode.http.port", "70070");
            org.apache.ambari.server.controller.ConfigurationRequest cr = new org.apache.ambari.server.controller.ConfigurationRequest(clusterName, "hdfs-site", "version1", configs, null);
            org.apache.ambari.server.controller.ClusterRequest crequest = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), clusterName, null, null);
            crequest.setDesiredConfig(java.util.Collections.singletonList(cr));
            org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.updateClusters(java.util.Collections.singleton(crequest), new java.util.HashMap<>());
        } else {
            java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
            configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.NAMENODE_PORT_V2, "localhost:70071");
            configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.DATANODE_PORT, "localhost:70075");
            org.apache.ambari.server.controller.ConfigurationRequest cr = new org.apache.ambari.server.controller.ConfigurationRequest(clusterName, "hdfs-site", "version2", configs, null);
            org.apache.ambari.server.controller.ClusterRequest crequest = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), clusterName, null, null);
            crequest.setDesiredConfig(java.util.Collections.singletonList(cr));
            org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.updateClusters(java.util.Collections.singleton(crequest), new java.util.HashMap<>());
        }
    }

    private void createConfigs() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.lang.String clusterName = "c1";
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, "HDP-2.0.6", null);
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.createCluster(r);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        java.lang.String serviceName = "HDFS";
        java.lang.String serviceName2 = "YARN";
        java.lang.String serviceName3 = "MAPREDUCE2";
        java.lang.String serviceName4 = "HBASE";
        createService(clusterName, serviceName, null);
        createService(clusterName, serviceName2, null);
        createService(clusterName, serviceName3, null);
        createService(clusterName, serviceName4, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        java.lang.String componentName4 = "RESOURCEMANAGER";
        java.lang.String componentName5 = "JOURNALNODE";
        java.lang.String componentName6 = "HISTORYSERVER";
        java.lang.String componentName7 = "NODEMANAGER";
        java.lang.String componentName8 = "HBASE_MASTER";
        java.lang.String componentName9 = "HBASE_REGIONSERVER";
        createServiceComponent(clusterName, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName2, componentName4, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName5, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName3, componentName6, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName2, componentName7, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName4, componentName8, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName4, componentName9, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = "h1";
        clusters.addHost(host1);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "5.9");
        clusters.getHost("h1").setHostAttributes(hostAttributes);
        java.lang.String host2 = "h2";
        clusters.addHost(host2);
        hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        clusters.getHost("h2").setHostAttributes(hostAttributes);
        clusters.mapHostToCluster(host1, clusterName);
        clusters.mapHostToCluster(host2, clusterName);
        clusters.updateHostMappings(clusters.getHost(host1));
        clusters.updateHostMappings(clusters.getHost(host2));
        createServiceComponentHost(clusterName, null, componentName1, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName2, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName2, host2, null);
        createServiceComponentHost(clusterName, serviceName, componentName3, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName5, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName5, host2, null);
        createServiceComponentHost(clusterName, serviceName, componentName3, host2, null);
        createServiceComponentHost(clusterName, serviceName2, componentName4, host2, null);
        createServiceComponentHost(clusterName, serviceName3, componentName6, host2, null);
        createServiceComponentHost(clusterName, serviceName2, componentName7, host2, null);
        createServiceComponentHost(clusterName, serviceName4, componentName8, host1, null);
        createServiceComponentHost(clusterName, serviceName4, componentName9, host2, null);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.NAMENODE_PORT_V1, "localhost:${ambari.dfs.datanode.http.port}");
        configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.DATANODE_PORT, "localhost:70075");
        configs.put("ambari.dfs.datanode.http.port", "70070");
        configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.JOURNALNODE_HTTPS_PORT, "localhost:8481");
        configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.DATANODE_HTTPS_PORT, "50475");
        configs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.HDFS_HTTPS_POLICY, "HTTPS_ONLY");
        java.util.Map<java.lang.String, java.lang.String> yarnConfigs = new java.util.HashMap<>();
        yarnConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.RESOURCEMANAGER_PORT, "8088");
        yarnConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.NODEMANAGER_PORT, "8042");
        yarnConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.RESOURCEMANAGER_HTTPS_PORT, "8090");
        yarnConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.NODEMANAGER_HTTPS_PORT, "8044");
        yarnConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.YARN_HTTPS_POLICY, "HTTPS_ONLY");
        java.util.Map<java.lang.String, java.lang.String> mapreduceConfigs = new java.util.HashMap<>();
        mapreduceConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.MAPREDUCE_HTTPS_PORT, "19889");
        mapreduceConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.MAPREDUCE_HTTPS_POLICY, "HTTPS_ONLY");
        java.util.Map<java.lang.String, java.lang.String> hbaseConfigs = new java.util.HashMap<>();
        hbaseConfigs.put("hbase.ssl.enabled", "true");
        org.apache.ambari.server.controller.ConfigurationRequest cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(clusterName, "hdfs-site", "versionN", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), clusterName, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        cluster = clusters.getCluster(clusterName);
        org.junit.Assert.assertEquals("versionN", cluster.getDesiredConfigByType("hdfs-site").getTag());
        org.apache.ambari.server.controller.ConfigurationRequest cr2 = new org.apache.ambari.server.controller.ConfigurationRequest(clusterName, "yarn-site", "versionN", yarnConfigs, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr2));
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        org.apache.ambari.server.controller.ConfigurationRequest cr3 = new org.apache.ambari.server.controller.ConfigurationRequest(clusterName, "mapred-site", "versionN", mapreduceConfigs, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr3));
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        org.apache.ambari.server.controller.ConfigurationRequest cr4 = new org.apache.ambari.server.controller.ConfigurationRequest(clusterName, "hbase-site", "versionN", hbaseConfigs, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr4));
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        org.junit.Assert.assertEquals("versionN", cluster.getDesiredConfigByType("yarn-site").getTag());
        org.junit.Assert.assertEquals("localhost:${ambari.dfs.datanode.http.port}", cluster.getDesiredConfigByType("hdfs-site").getProperties().get(org.apache.ambari.server.controller.internal.JMXHostProviderTest.NAMENODE_PORT_V1));
    }

    private void createConfigsNameNodeHa() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.lang.String clusterName = "nnha";
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, "HDP-2.0.6", null);
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.createCluster(r);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        cluster.setDesiredStackVersion(new org.apache.ambari.server.state.StackId("HDP-2.0.6"));
        java.lang.String serviceName = "HDFS";
        createService(clusterName, serviceName, null);
        java.lang.String componentName1 = "NAMENODE";
        java.lang.String componentName2 = "DATANODE";
        java.lang.String componentName3 = "HDFS_CLIENT";
        createServiceComponent(clusterName, serviceName, componentName1, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName2, org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, serviceName, componentName3, org.apache.ambari.server.state.State.INIT);
        java.lang.String host1 = "h1";
        clusters.addHost(host1);
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "5.9");
        clusters.getHost("h1").setHostAttributes(hostAttributes);
        java.lang.String host2 = "h2";
        clusters.addHost(host2);
        hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        clusters.getHost("h2").setHostAttributes(hostAttributes);
        clusters.mapHostToCluster(host1, clusterName);
        clusters.mapHostToCluster(host2, clusterName);
        clusters.updateHostMappings(clusters.getHost(host1));
        clusters.updateHostMappings(clusters.getHost(host2));
        createServiceComponentHost(clusterName, serviceName, componentName1, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName1, host2, null);
        createServiceComponentHost(clusterName, serviceName, componentName2, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName2, host2, null);
        createServiceComponentHost(clusterName, serviceName, componentName3, host1, null);
        createServiceComponentHost(clusterName, serviceName, componentName3, host2, null);
        java.util.Map<java.lang.String, java.lang.String> configs = new java.util.HashMap<>();
        configs.put("dfs.internal.nameservices", "ns");
        configs.put("dfs.namenode.http-address", "h1:50070");
        configs.put("dfs.namenode.http-address.ns.nn1", "h1:50071");
        configs.put("dfs.namenode.http-address.ns.nn2", "h2:50072");
        configs.put("dfs.namenode.https-address", "h1:50470");
        configs.put("dfs.namenode.https-address.ns.nn1", "h1:50473");
        configs.put("dfs.namenode.https-address.ns.nn2", "h2:50474");
        configs.put("dfs.ha.namenodes.ns", "nn1,nn2");
        org.apache.ambari.server.controller.ConfigurationRequest cr1 = new org.apache.ambari.server.controller.ConfigurationRequest(clusterName, "hdfs-site", "version1", configs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(cluster.getClusterId(), clusterName, null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr1));
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
    }

    @org.junit.Test
    public void testJMXPortMapInitAtServiceLevelVersion1() throws java.lang.Exception {
        createHDFSServiceConfigs(true);
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("70070", providerModule.getPort("c1", "NAMENODE", "localhost", false));
        org.junit.Assert.assertEquals("70075", providerModule.getPort("c1", "DATANODE", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "JOBTRACKER", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "TASKTRACKER", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "HBASE_MASTER", "localhost", false));
    }

    @org.junit.Test
    public void testJMXPortMapInitAtServiceLevelVersion2() throws java.lang.Exception {
        createHDFSServiceConfigs(false);
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("70071", providerModule.getPort("c1", "NAMENODE", "localhost", false));
        org.junit.Assert.assertEquals("70075", providerModule.getPort("c1", "DATANODE", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "JOBTRACKER", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "TASKTRACKER", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "HBASE_MASTER", "localhost", false));
    }

    @org.junit.Test
    public void testJMXPortMapNameNodeHa() throws java.lang.Exception {
        createConfigsNameNodeHa();
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Service);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("50071", providerModule.getPort("nnha", "NAMENODE", "h1", false));
        org.junit.Assert.assertEquals("50072", providerModule.getPort("nnha", "NAMENODE", "h2", false));
    }

    @org.junit.Test
    public void testJMXPortMapInitAtClusterLevel() throws java.lang.Exception {
        createConfigs();
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("70070", providerModule.getPort("c1", "NAMENODE", "localhost", false));
        org.junit.Assert.assertEquals("70075", providerModule.getPort("c1", "DATANODE", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "JOBTRACKER", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "TASKTRACKER", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "HBASE_MASTER", "localhost", false));
    }

    @org.junit.Test
    public void testGetHostNames() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController managementControllerMock = EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(managementControllerMock);
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent serviceComponentMock = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents = new java.util.HashMap<>();
        hostComponents.put("host1", null);
        EasyMock.expect(managementControllerMock.getClusters()).andReturn(clustersMock).anyTimes();
        EasyMock.expect(managementControllerMock.findServiceName(clusterMock, "DATANODE")).andReturn("HDFS");
        EasyMock.expect(clustersMock.getCluster("c1")).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getService("HDFS")).andReturn(serviceMock).anyTimes();
        EasyMock.expect(serviceMock.getServiceComponent("DATANODE")).andReturn(serviceComponentMock).anyTimes();
        EasyMock.expect(serviceComponentMock.getServiceComponentHosts()).andReturn(hostComponents).anyTimes();
        EasyMock.replay(managementControllerMock, clustersMock, clusterMock, serviceMock, serviceComponentMock);
        java.util.Set<java.lang.String> result = providerModule.getHostNames("c1", "DATANODE");
        org.junit.Assert.assertTrue(result.iterator().next().equals("host1"));
    }

    @org.junit.Test
    public void testJMXHttpsPort() throws java.lang.Exception {
        createConfigs();
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("https", providerModule.getJMXProtocol("c1", "RESOURCEMANAGER"));
        org.junit.Assert.assertEquals("8090", providerModule.getPort("c1", "RESOURCEMANAGER", "localhost", true));
        org.junit.Assert.assertEquals("https", providerModule.getJMXProtocol("c1", "NODEMANAGER"));
        org.junit.Assert.assertEquals("8044", providerModule.getPort("c1", "NODEMANAGER", "localhost", true));
    }

    @org.junit.Test
    public void testJMXHistoryServerHttpsPort() throws java.lang.Exception {
        createConfigs();
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("https", providerModule.getJMXProtocol("c1", "HISTORYSERVER"));
        org.junit.Assert.assertEquals("19889", providerModule.getPort("c1", "HISTORYSERVER", "localhost", true));
    }

    @org.junit.Test
    public void testJMXJournalNodeHttpsPort() throws java.lang.Exception {
        createConfigs();
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("https", providerModule.getJMXProtocol("c1", "JOURNALNODE"));
        org.junit.Assert.assertEquals("8481", providerModule.getPort("c1", "JOURNALNODE", "localhost", true));
    }

    @org.junit.Test
    public void testJMXDataNodeHttpsPort() throws java.lang.Exception {
        createConfigs();
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("https", providerModule.getJMXProtocol("c1", "DATANODE"));
        org.junit.Assert.assertEquals("50475", providerModule.getPort("c1", "DATANODE", "localhost", true));
    }

    @org.junit.Test
    public void testJMXHbaseMasterHttps() throws java.lang.Exception {
        createConfigs();
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("https", providerModule.getJMXProtocol("c1", "HBASE_MASTER"));
        org.junit.Assert.assertEquals("https", providerModule.getJMXProtocol("c1", "HBASE_REGIONSERVER"));
    }

    @org.junit.Test
    public void testJMXPortMapUpdate() throws java.lang.Exception {
        createConfigs();
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule providerModule = new org.apache.ambari.server.controller.internal.JMXHostProviderTest.JMXHostProviderModule(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        providerModule.registerResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
        org.junit.Assert.assertEquals("8088", providerModule.getPort("c1", "RESOURCEMANAGER", "localhost", false));
        java.util.Map<java.lang.String, java.lang.String> yarnConfigs = new java.util.HashMap<>();
        yarnConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.RESOURCEMANAGER_PORT, "localhost:50030");
        yarnConfigs.put(org.apache.ambari.server.controller.internal.JMXHostProviderTest.NODEMANAGER_PORT, "localhost:11111");
        org.apache.ambari.server.controller.ConfigurationRequest cr2 = new org.apache.ambari.server.controller.ConfigurationRequest("c1", "yarn-site", "versionN+1", yarnConfigs, null);
        org.apache.ambari.server.controller.ClusterRequest crReq = new org.apache.ambari.server.controller.ClusterRequest(1L, "c1", null, null);
        crReq.setDesiredConfig(java.util.Collections.singletonList(cr2));
        org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller.updateClusters(java.util.Collections.singleton(crReq), null);
        org.junit.Assert.assertEquals("50030", providerModule.getPort("c1", "RESOURCEMANAGER", "localhost", false));
        org.junit.Assert.assertEquals("11111", providerModule.getPort("c1", "NODEMANAGER", "localhost", false));
        org.junit.Assert.assertEquals("70070", providerModule.getPort("c1", "NAMENODE", "localhost", false));
        org.junit.Assert.assertEquals(null, providerModule.getPort("c1", "JOBTRACKER", "localhost", false));
        org.junit.Assert.assertNull(providerModule.getPort("c1", "HBASE_REGIONSERVER", "remotehost1", false));
    }

    private static class JMXHostProviderModule extends org.apache.ambari.server.controller.internal.AbstractProviderModule {
        org.apache.ambari.server.controller.spi.ResourceProvider clusterResourceProvider = new org.apache.ambari.server.controller.internal.ClusterResourceProvider(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);

        com.google.inject.Injector injector = EasyMock.createNiceMock(com.google.inject.Injector.class);

        org.apache.ambari.server.controller.MaintenanceStateHelper maintenanceStateHelper = EasyMock.createNiceMock(org.apache.ambari.server.controller.MaintenanceStateHelper.class);

        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);

        {
            EasyMock.expect(injector.getInstance(org.apache.ambari.server.state.Clusters.class)).andReturn(null);
            EasyMock.replay(maintenanceStateHelper, injector);
        }

        org.apache.ambari.server.controller.spi.ResourceProvider serviceResourceProvider = new org.apache.ambari.server.controller.internal.ServiceResourceProvider(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller, maintenanceStateHelper, repositoryVersionDAO);

        org.apache.ambari.server.controller.spi.ResourceProvider hostCompResourceProvider = new org.apache.ambari.server.controller.internal.HostComponentResourceProvider(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);

        org.apache.ambari.server.controller.spi.ResourceProvider configResourceProvider = new org.apache.ambari.server.controller.internal.ConfigurationResourceProvider(org.apache.ambari.server.controller.internal.JMXHostProviderTest.controller);

        JMXHostProviderModule(org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) {
            super();
            managementController = ambariManagementController;
        }

        @java.lang.Override
        protected org.apache.ambari.server.controller.spi.ResourceProvider createResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
            if (type == org.apache.ambari.server.controller.spi.Resource.Type.Cluster) {
                return clusterResourceProvider;
            }
            if (type == org.apache.ambari.server.controller.spi.Resource.Type.Service) {
                return serviceResourceProvider;
            } else if (type == org.apache.ambari.server.controller.spi.Resource.Type.HostComponent) {
                return hostCompResourceProvider;
            } else if (type == org.apache.ambari.server.controller.spi.Resource.Type.Configuration) {
                return configResourceProvider;
            }
            return null;
        }
    }
}