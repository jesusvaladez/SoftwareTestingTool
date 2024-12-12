package org.apache.ambari.server.metadata;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class RoleCommandOrderTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.metadata.RoleCommandOrderProvider roleCommandOrderProvider;

    private static final java.lang.String TEST_RCO_DATA_FILE = "test_rco_data.json";

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        roleCommandOrderProvider = injector.getInstance(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testInitializeAtGLUSTERFSCluster() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.0.6");
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        org.apache.ambari.server.state.Service service = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service.getDesiredStackId()).andReturn(stackId);
        EasyMock.expect(cluster.getClusterId()).andReturn(1L);
        EasyMock.expect(cluster.getService("GLUSTERFS")).andReturn(service);
        EasyMock.expect(cluster.getService("HDFS")).andReturn(null);
        EasyMock.expect(cluster.getService("YARN")).andReturn(null);
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("GLUSTERFS", service).build()).atLeastOnce();
        EasyMock.replay(cluster, service);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> deps = rco.getDependencies();
        junit.framework.Assert.assertTrue("Dependencies are loaded after initialization", deps.size() > 0);
        EasyMock.verify(cluster, service);
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.DATANODE));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.NAMENODE));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.SECONDARY_NAMENODE));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.JOURNALNODE));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.NAMENODE_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.HDFS_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.HDFS_CLIENT));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.SECONDARY_NAMENODE));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.JOURNALNODE));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.NAMENODE_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.HDFS_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.HDFS_CLIENT));
        junit.framework.Assert.assertTrue(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.PEERSTATUS));
    }

    @org.junit.Test
    public void testInitializeAtHDFSCluster() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        EasyMock.expect(cluster.getService("GLUSTERFS")).andReturn(null);
        EasyMock.expect(cluster.getClusterId()).andReturn(1L);
        org.apache.ambari.server.state.Service hdfsService = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(cluster.getService("HDFS")).andReturn(hdfsService).atLeastOnce();
        EasyMock.expect(cluster.getService("YARN")).andReturn(null).atLeastOnce();
        EasyMock.expect(hdfsService.getServiceComponent("JOURNALNODE")).andReturn(null);
        EasyMock.expect(hdfsService.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.0.6"));
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", hdfsService).build()).anyTimes();
        EasyMock.replay(cluster, hdfsService);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> deps = rco.getDependencies();
        junit.framework.Assert.assertTrue("Dependencies are loaded after initialization", deps.size() > 0);
        EasyMock.verify(cluster);
        EasyMock.verify(hdfsService);
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.PEERSTATUS));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.GLUSTERFS_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.GLUSTERFS_CLIENT));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.PEERSTATUS));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.GLUSTERFS_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.GLUSTERFS_CLIENT));
        junit.framework.Assert.assertTrue(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.DATANODE));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.JOURNALNODE));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.ZKFC));
    }

    @org.junit.Test
    public void testInitializeAtHaHDFSCluster() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        EasyMock.expect(cluster.getService("GLUSTERFS")).andReturn(null);
        EasyMock.expect(cluster.getClusterId()).andReturn(1L);
        org.apache.ambari.server.state.Service hdfsService = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent journalnodeSC = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(cluster.getService("HDFS")).andReturn(hdfsService).atLeastOnce();
        EasyMock.expect(cluster.getService("YARN")).andReturn(null);
        EasyMock.expect(hdfsService.getServiceComponent("JOURNALNODE")).andReturn(journalnodeSC);
        EasyMock.expect(hdfsService.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.0.6"));
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", hdfsService).build()).anyTimes();
        EasyMock.replay(cluster, hdfsService);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> deps = rco.getDependencies();
        junit.framework.Assert.assertTrue("Dependencies are loaded after initialization", deps.size() > 0);
        EasyMock.verify(cluster);
        EasyMock.verify(hdfsService);
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.PEERSTATUS));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.GLUSTERFS_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.GLUSTERFS_CLIENT));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.PEERSTATUS));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.GLUSTERFS_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.GLUSTERFS_CLIENT));
        junit.framework.Assert.assertTrue(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.DATANODE));
        junit.framework.Assert.assertTrue(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.JOURNALNODE));
        junit.framework.Assert.assertTrue(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.ZKFC));
    }

    @org.junit.Test
    public void testInitializeAtHaRMCluster() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = EasyMock.createMock(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.class);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = EasyMock.createMock(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.class);
        EasyMock.expect(cluster.getService("GLUSTERFS")).andReturn(null);
        EasyMock.expect(cluster.getClusterId()).andReturn(1L);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents = new java.util.HashMap<>();
        hostComponents.put("1", sch1);
        hostComponents.put("2", sch2);
        org.apache.ambari.server.state.Service yarnService = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent resourcemanagerSC = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(cluster.getService("YARN")).andReturn(yarnService).atLeastOnce();
        EasyMock.expect(cluster.getService("HDFS")).andReturn(null);
        EasyMock.expect(yarnService.getServiceComponent("RESOURCEMANAGER")).andReturn(resourcemanagerSC).anyTimes();
        EasyMock.expect(resourcemanagerSC.getServiceComponentHosts()).andReturn(hostComponents).anyTimes();
        EasyMock.expect(yarnService.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.0.6"));
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("YARN", yarnService).build()).anyTimes();
        EasyMock.replay(cluster, yarnService, sch1, sch2, resourcemanagerSC);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> deps = rco.getDependencies();
        junit.framework.Assert.assertTrue("Dependencies are loaded after initialization", deps.size() > 0);
        EasyMock.verify(cluster, yarnService);
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.PEERSTATUS));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.GLUSTERFS_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockedRole(deps, org.apache.ambari.server.Role.GLUSTERFS_CLIENT));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.PEERSTATUS));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.GLUSTERFS_SERVICE_CHECK));
        junit.framework.Assert.assertFalse(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.GLUSTERFS_CLIENT));
        junit.framework.Assert.assertTrue(dependenciesContainBlockerRole(deps, org.apache.ambari.server.Role.DATANODE));
        org.apache.ambari.server.metadata.RoleCommandPair rmPair = new org.apache.ambari.server.metadata.RoleCommandPair(org.apache.ambari.server.Role.RESOURCEMANAGER, org.apache.ambari.server.RoleCommand.START);
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> rmRoleCommandPairs = deps.get(rmPair);
        junit.framework.Assert.assertNotNull(rmRoleCommandPairs);
        boolean isZookeeperStartPresent = false;
        for (org.apache.ambari.server.metadata.RoleCommandPair pair : rmRoleCommandPairs) {
            if ((pair.getCmd() == org.apache.ambari.server.RoleCommand.START) && (pair.getRole() == org.apache.ambari.server.Role.ZOOKEEPER_SERVER)) {
                isZookeeperStartPresent = true;
            }
        }
        junit.framework.Assert.assertTrue(isZookeeperStartPresent);
    }

    @org.junit.Test
    public void testMissingRestartDependenciesAdded() throws java.lang.Exception {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = EasyMock.createMock(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.class);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = EasyMock.createMock(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl.class);
        EasyMock.expect(cluster.getService("GLUSTERFS")).andReturn(null);
        EasyMock.expect(cluster.getClusterId()).andReturn(1L);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hostComponents = new java.util.HashMap<>();
        hostComponents.put("1", sch1);
        hostComponents.put("2", sch2);
        org.apache.ambari.server.state.Service yarnService = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent resourcemanagerSC = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(cluster.getService("YARN")).andReturn(yarnService).atLeastOnce();
        EasyMock.expect(cluster.getService("HDFS")).andReturn(null);
        EasyMock.expect(yarnService.getServiceComponent("RESOURCEMANAGER")).andReturn(resourcemanagerSC).anyTimes();
        EasyMock.expect(yarnService.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.0.6")).anyTimes();
        EasyMock.expect(resourcemanagerSC.getServiceComponentHosts()).andReturn(hostComponents).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("YARN", yarnService).build()).anyTimes();
        EasyMock.replay(cluster, yarnService, sch1, sch2, resourcemanagerSC);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        EasyMock.verify(cluster, yarnService);
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> deps = rco.getDependencies();
        junit.framework.Assert.assertNotNull(deps);
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> startRCOs = new java.util.HashMap<>();
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> restartRCOs = new java.util.HashMap<>();
        for (java.util.Map.Entry<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> dep : deps.entrySet()) {
            if (dep.getKey().getCmd().equals(org.apache.ambari.server.RoleCommand.START)) {
                startRCOs.put(dep.getKey(), dep.getValue());
            }
            if (dep.getKey().getCmd().equals(org.apache.ambari.server.RoleCommand.RESTART)) {
                restartRCOs.put(dep.getKey(), dep.getValue());
            }
        }
        junit.framework.Assert.assertFalse(startRCOs.isEmpty());
        junit.framework.Assert.assertFalse(restartRCOs.isEmpty());
        junit.framework.Assert.assertEquals(startRCOs.size(), restartRCOs.size());
        java.util.Map.Entry<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> entry = restartRCOs.entrySet().iterator().next();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.RoleCommand.RESTART, entry.getKey().getCmd());
        for (org.apache.ambari.server.metadata.RoleCommandPair pair : entry.getValue()) {
            junit.framework.Assert.assertEquals(org.apache.ambari.server.RoleCommand.RESTART, pair.getCmd());
        }
        for (java.util.Map.Entry<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> startEntry : startRCOs.entrySet()) {
            junit.framework.Assert.assertTrue(restartRCOs.containsKey(new org.apache.ambari.server.metadata.RoleCommandPair(startEntry.getKey().getRole(), org.apache.ambari.server.RoleCommand.RESTART)));
        }
    }

    @org.junit.Test
    public void testAddDependencies() throws java.io.IOException {
        org.apache.ambari.server.metadata.RoleCommandOrder rco = injector.getInstance(org.apache.ambari.server.metadata.RoleCommandOrder.class);
        java.io.InputStream testJsonIS = getClass().getClassLoader().getResourceAsStream(org.apache.ambari.server.metadata.RoleCommandOrderTest.TEST_RCO_DATA_FILE);
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        java.util.Map<java.lang.String, java.lang.Object> testData = mapper.readValue(testJsonIS, new org.codehaus.jackson.type.TypeReference<java.util.Map<java.lang.String, java.lang.Object>>() {});
        rco.addDependencies(testData);
        mapper.setVisibility(JsonMethod.ALL, JsonAutoDetect.Visibility.ANY);
        java.lang.String dump = mapper.writeValueAsString(rco.getDependencies());
        java.util.List<java.lang.String> parts = java.util.Arrays.asList(dump.substring(1, 522).split(java.util.regex.Pattern.quote("],")));
        junit.framework.Assert.assertEquals(3, parts.size());
        junit.framework.Assert.assertTrue(parts.contains("\"RoleCommandPair{role=SECONDARY_NAMENODE, cmd=UPGRADE}\":[{\"role\":{\"name\":\"NAMENODE\"},\"cmd\":\"UPGRADE\"}"));
        junit.framework.Assert.assertTrue(parts.contains("\"RoleCommandPair{role=SECONDARY_NAMENODE, cmd=START}\":[{\"role\":{\"name\":\"NAMENODE\"},\"cmd\":\"START\"}"));
        boolean datanodeCommandExists = false;
        for (java.lang.String part : parts) {
            if (part.contains("RoleCommandPair{role=DATANODE, cmd=STOP}")) {
                datanodeCommandExists = true;
                java.lang.String[] parts2 = part.split(java.util.regex.Pattern.quote(":["));
                junit.framework.Assert.assertEquals(2, parts2.length);
                junit.framework.Assert.assertEquals("\"RoleCommandPair{role=DATANODE, cmd=STOP}\"", parts2[0]);
                java.util.List<java.lang.String> components = java.util.Arrays.asList(new java.lang.String[]{ "\"role\":{\"name\":\"HBASE_MASTER\"},\"cmd\":\"STOP\"", "\"role\":{\"name\":\"RESOURCEMANAGER\"},\"cmd\":\"STOP\"", "\"role\":{\"name\":\"TASKTRACKER\"},\"cmd\":\"STOP\"", "\"role\":{\"name\":\"NODEMANAGER\"},\"cmd\":\"STOP\"", "\"role\":{\"name\":\"HISTORYSERVER\"},\"cmd\":\"STOP\"", "\"role\":{\"name\":\"JOBTRACKER\"},\"cmd\":\"STOP\"" });
                junit.framework.Assert.assertTrue(org.apache.ambari.server.utils.CollectionPresentationUtils.isStringPermutationOfCollection(parts2[1], components, "},{", 1, 1));
            }
        }
        junit.framework.Assert.assertTrue(datanodeCommandExists);
    }

    @org.junit.Test
    public void testInitializeDefault() throws java.io.IOException {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        EasyMock.expect(cluster.getService("GLUSTERFS")).andReturn(null);
        EasyMock.expect(cluster.getClusterId()).andReturn(1L);
        org.apache.ambari.server.state.Service hdfsService = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(cluster.getService("HDFS")).andReturn(hdfsService).atLeastOnce();
        EasyMock.expect(cluster.getService("YARN")).andReturn(null);
        EasyMock.expect(hdfsService.getServiceComponent("JOURNALNODE")).andReturn(null);
        EasyMock.expect(hdfsService.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.0.5"));
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", hdfsService).build()).anyTimes();
        EasyMock.replay(cluster);
        EasyMock.replay(hdfsService);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        EasyMock.verify(cluster);
        EasyMock.verify(hdfsService);
    }

    @org.junit.Test
    public void testTransitiveServices() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        org.apache.ambari.server.state.Service hdfsService = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent namenode = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(namenode.getName()).andReturn("NAMENODE").anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> hdfsComponents = java.util.Collections.singletonMap("NAMENODE", namenode);
        EasyMock.expect(hdfsService.getServiceComponents()).andReturn(hdfsComponents).anyTimes();
        org.apache.ambari.server.state.Service hbaseService = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(cluster.getService("HBASE")).andReturn(hbaseService).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L);
        EasyMock.expect(hbaseService.getCluster()).andReturn(cluster).anyTimes();
        org.apache.ambari.server.state.ServiceComponent hbaseMaster = EasyMock.createMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(hbaseMaster.getName()).andReturn("HBASE_MASTER").anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> hbaseComponents = java.util.Collections.singletonMap("HBASE_MASTER", hbaseMaster);
        EasyMock.expect(hbaseService.getServiceComponents()).andReturn(hbaseComponents).anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> installedServices = new java.util.HashMap<>();
        installedServices.put("HDFS", hdfsService);
        installedServices.put("HBASE", hbaseService);
        EasyMock.expect(cluster.getServices()).andReturn(installedServices).atLeastOnce();
        EasyMock.expect(cluster.getService("HDFS")).andReturn(hdfsService).atLeastOnce();
        EasyMock.expect(cluster.getService("GLUSTERFS")).andReturn(null);
        EasyMock.expect(cluster.getService("YARN")).andReturn(null);
        EasyMock.expect(hdfsService.getServiceComponent("JOURNALNODE")).andReturn(null);
        EasyMock.expect(hdfsService.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.0.5"));
        EasyMock.expect(hbaseService.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.0.5"));
        EasyMock.replay(cluster, hdfsService, hbaseService, hbaseMaster, namenode);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        java.util.Set<org.apache.ambari.server.state.Service> transitiveServices = rco.getTransitiveServices(cluster.getService("HBASE"), org.apache.ambari.server.RoleCommand.START);
        junit.framework.Assert.assertNotNull(transitiveServices);
        junit.framework.Assert.assertFalse(transitiveServices.isEmpty());
        junit.framework.Assert.assertEquals(1, transitiveServices.size());
        junit.framework.Assert.assertTrue(transitiveServices.contains(hdfsService));
    }

    @org.junit.Test
    public void testOverride() throws java.lang.Exception {
        org.apache.ambari.server.state.cluster.ClusterImpl cluster = EasyMock.createMock(org.apache.ambari.server.state.cluster.ClusterImpl.class);
        EasyMock.expect(cluster.getService("GLUSTERFS")).andReturn(null).atLeastOnce();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).atLeastOnce();
        org.apache.ambari.server.state.Service hdfsService = EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(cluster.getService("HDFS")).andReturn(hdfsService).atLeastOnce();
        EasyMock.expect(cluster.getService("YARN")).andReturn(null).atLeastOnce();
        EasyMock.expect(hdfsService.getServiceComponent("JOURNALNODE")).andReturn(null);
        EasyMock.expect(hdfsService.getDesiredStackId()).andReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2.0")).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", hdfsService).build()).anyTimes();
        EasyMock.replay(cluster, hdfsService);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = roleCommandOrderProvider.getRoleCommandOrder(cluster);
        org.apache.ambari.server.metadata.RoleCommandPair startDN = new org.apache.ambari.server.metadata.RoleCommandPair(org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.metadata.RoleCommandPair startNM = new org.apache.ambari.server.metadata.RoleCommandPair(org.apache.ambari.server.Role.NODEMANAGER, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.metadata.RoleCommandPair startNN = new org.apache.ambari.server.metadata.RoleCommandPair(org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.START);
        org.apache.ambari.server.metadata.RoleCommandPair startRM = new org.apache.ambari.server.metadata.RoleCommandPair(org.apache.ambari.server.Role.RESOURCEMANAGER, org.apache.ambari.server.RoleCommand.START);
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> startDNDeps = rco.getDependencies().get(startDN);
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> startNMDeps = rco.getDependencies().get(startNM);
        junit.framework.Assert.assertNull(startDNDeps);
        junit.framework.Assert.assertTrue(startNMDeps.contains(startDN));
        rco = ((org.apache.ambari.server.metadata.RoleCommandOrder) (rco.clone()));
        java.util.LinkedHashSet<java.lang.String> keys = rco.getSectionKeys();
        keys.add("host_ordered_upgrade");
        rco.initialize(cluster, keys);
        startDNDeps = rco.getDependencies().get(startDN);
        startNMDeps = rco.getDependencies().get(startNM);
        junit.framework.Assert.assertTrue(startDNDeps.contains(startNN));
        junit.framework.Assert.assertTrue(startNMDeps.contains(startRM));
        junit.framework.Assert.assertFalse(startNMDeps.contains(startDN));
        junit.framework.Assert.assertEquals(2, startNMDeps.size());
        EasyMock.verify(cluster);
        EasyMock.verify(hdfsService);
    }

    private boolean dependenciesContainBlockedRole(java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> deps, org.apache.ambari.server.Role blocked) {
        for (org.apache.ambari.server.metadata.RoleCommandPair blockedPair : deps.keySet()) {
            if (blockedPair.getRole() == blocked) {
                return true;
            }
        }
        return false;
    }

    private boolean dependenciesContainBlockerRole(java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> deps, org.apache.ambari.server.Role blocker) {
        for (java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> blockerSet : deps.values()) {
            for (org.apache.ambari.server.metadata.RoleCommandPair roleCommandPair : blockerSet) {
                if (roleCommandPair.getRole() == blocker) {
                    return true;
                }
            }
        }
        return false;
    }
}