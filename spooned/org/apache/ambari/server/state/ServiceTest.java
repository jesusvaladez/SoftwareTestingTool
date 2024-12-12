package org.apache.ambari.server.state;
import org.apache.commons.collections.MapUtils;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ServiceTest {
    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster cluster;

    private java.lang.String clusterName;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    private org.apache.ambari.server.orm.OrmTestHelper ormTestHelper;

    private org.apache.ambari.server.state.ConfigFactory configFactory;

    private final java.lang.String STACK_VERSION = "0.1";

    private final java.lang.String REPO_VERSION = "0.1-1234";

    private final org.apache.ambari.server.state.StackId STACK_ID = new org.apache.ambari.server.state.StackId("HDP", STACK_VERSION);

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        serviceComponentFactory = injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        serviceComponentHostFactory = injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        configFactory = injector.getInstance(org.apache.ambari.server.state.ConfigFactory.class);
        ormTestHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        repositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(STACK_ID, REPO_VERSION);
        clusterName = "foo";
        clusters.addCluster(clusterName, STACK_ID);
        cluster = clusters.getCluster(clusterName);
        junit.framework.Assert.assertNotNull(cluster);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testCanBeRemoved() throws java.lang.Exception {
        org.apache.ambari.server.state.Service service = cluster.addService("HDFS", repositoryVersion);
        for (org.apache.ambari.server.state.State state : org.apache.ambari.server.state.State.values()) {
            service.setDesiredState(state);
            org.junit.Assert.assertTrue(service.canBeRemoved());
        }
        org.apache.ambari.server.state.ServiceComponent component = service.addServiceComponent("NAMENODE");
        component.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        for (org.apache.ambari.server.state.State state : org.apache.ambari.server.state.State.values()) {
            service.setDesiredState(state);
            org.junit.Assert.assertTrue(service.canBeRemoved());
        }
        component.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        addHostToCluster("h1", service.getCluster().getClusterName());
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(component, "h1");
        component.addServiceComponentHost(sch);
        sch.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch.setState(org.apache.ambari.server.state.State.STARTED);
        for (org.apache.ambari.server.state.State state : org.apache.ambari.server.state.State.values()) {
            service.setDesiredState(state);
            org.junit.Assert.assertFalse(service.canBeRemoved());
        }
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
    }

    @org.junit.Test
    public void testGetAndSetServiceInfo() throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        junit.framework.Assert.assertNotNull(service);
        org.apache.ambari.server.state.StackId desiredStackId = new org.apache.ambari.server.state.StackId("HDP-1.2.0");
        java.lang.String desiredVersion = "1.2.0-1234";
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(desiredStackId, desiredVersion);
        service.setDesiredRepositoryVersion(desiredRepositoryVersion);
        junit.framework.Assert.assertEquals(desiredStackId, service.getDesiredStackId());
        service.setDesiredState(org.apache.ambari.server.state.State.INSTALLING);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLING, service.getDesiredState());
    }

    @org.junit.Test
    public void testAddGetDeleteServiceComponents() throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        junit.framework.Assert.assertNotNull(service);
        junit.framework.Assert.assertEquals(serviceName, service.getName());
        junit.framework.Assert.assertEquals(cluster.getClusterId(), service.getCluster().getClusterId());
        junit.framework.Assert.assertEquals(cluster.getClusterName(), service.getCluster().getClusterName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, service.getDesiredState());
        junit.framework.Assert.assertFalse(service.getDesiredStackId().getStackId().isEmpty());
        junit.framework.Assert.assertTrue(s.getServiceComponents().isEmpty());
        org.apache.ambari.server.state.ServiceComponent sc1 = serviceComponentFactory.createNew(s, "NAMENODE");
        org.apache.ambari.server.state.ServiceComponent sc2 = serviceComponentFactory.createNew(s, "DATANODE1");
        org.apache.ambari.server.state.ServiceComponent sc3 = serviceComponentFactory.createNew(s, "DATANODE2");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> comps = new java.util.HashMap<>();
        comps.put(sc1.getName(), sc1);
        comps.put(sc2.getName(), sc2);
        s.addServiceComponents(comps);
        junit.framework.Assert.assertEquals(2, s.getServiceComponents().size());
        junit.framework.Assert.assertNotNull(s.getServiceComponent(sc1.getName()));
        junit.framework.Assert.assertNotNull(s.getServiceComponent(sc2.getName()));
        try {
            s.getServiceComponent(sc3.getName());
            org.junit.Assert.fail("Expected error when looking for invalid component");
        } catch (java.lang.Exception e) {
        }
        s.addServiceComponent(sc3);
        org.apache.ambari.server.state.ServiceComponent sc4 = s.addServiceComponent("HDFS_CLIENT");
        junit.framework.Assert.assertNotNull(s.getServiceComponent(sc4.getName()));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, s.getServiceComponent("HDFS_CLIENT").getDesiredState());
        junit.framework.Assert.assertTrue(sc4.isClientComponent());
        junit.framework.Assert.assertEquals(4, s.getServiceComponents().size());
        junit.framework.Assert.assertNotNull(s.getServiceComponent(sc3.getName()));
        junit.framework.Assert.assertEquals(sc3.getName(), s.getServiceComponent(sc3.getName()).getName());
        junit.framework.Assert.assertEquals(s.getName(), s.getServiceComponent(sc3.getName()).getServiceName());
        junit.framework.Assert.assertEquals(cluster.getClusterName(), s.getServiceComponent(sc3.getName()).getClusterName());
        sc4.setDesiredState(org.apache.ambari.server.state.State.INSTALLING);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLING, s.getServiceComponent("HDFS_CLIENT").getDesiredState());
        s.deleteServiceComponent("NAMENODE", new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        org.junit.Assert.assertEquals(3, s.getServiceComponents().size());
    }

    @org.junit.Test
    public void testGetAndSetConfigs() {
    }

    @org.junit.Test
    public void testConvertToResponse() throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        junit.framework.Assert.assertNotNull(service);
        org.apache.ambari.server.controller.ServiceResponse r = s.convertToResponse();
        junit.framework.Assert.assertEquals(s.getName(), r.getServiceName());
        junit.framework.Assert.assertEquals(s.getCluster().getClusterName(), r.getClusterName());
        junit.framework.Assert.assertEquals(s.getDesiredStackId().getStackId(), r.getDesiredStackId());
        junit.framework.Assert.assertEquals(s.getDesiredState().toString(), r.getDesiredState());
        org.apache.ambari.server.state.StackId desiredStackId = new org.apache.ambari.server.state.StackId("HDP-1.2.0");
        java.lang.String desiredVersion = "1.2.0-1234";
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity desiredRepositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(desiredStackId, desiredVersion);
        service.setDesiredRepositoryVersion(desiredRepositoryVersion);
        service.setDesiredState(org.apache.ambari.server.state.State.INSTALLING);
        r = s.convertToResponse();
        junit.framework.Assert.assertEquals(s.getName(), r.getServiceName());
        junit.framework.Assert.assertEquals(s.getCluster().getClusterName(), r.getClusterName());
        junit.framework.Assert.assertEquals(s.getDesiredStackId().getStackId(), r.getDesiredStackId());
        junit.framework.Assert.assertEquals(s.getDesiredState().toString(), r.getDesiredState());
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        s.debugDump(sb);
        junit.framework.Assert.assertFalse(sb.toString().isEmpty());
    }

    @org.junit.Test
    public void testServiceMaintenance() throws java.lang.Exception {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        junit.framework.Assert.assertNotNull(service);
        org.apache.ambari.server.orm.dao.ClusterServiceDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterServiceDAO.class);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity entity = dao.findByClusterAndServiceNames(clusterName, serviceName);
        junit.framework.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, entity.getServiceDesiredStateEntity().getMaintenanceState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, service.getMaintenanceState());
        service.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, service.getMaintenanceState());
        entity = dao.findByClusterAndServiceNames(clusterName, serviceName);
        junit.framework.Assert.assertNotNull(entity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, entity.getServiceDesiredStateEntity().getMaintenanceState());
    }

    @org.junit.Test
    public void testServiceKerberosEnabledTest() throws java.lang.Exception {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        junit.framework.Assert.assertNotNull(service);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> map = new java.util.HashMap<>();
        junit.framework.Assert.assertFalse(service.isKerberosEnabled(null));
        junit.framework.Assert.assertFalse(service.isKerberosEnabled(map));
        map.put("core-site", java.util.Collections.singletonMap("hadoop.security.authentication", "none"));
        map.put("hdfs-site", java.util.Collections.singletonMap("hadoop.security.authentication", "none"));
        junit.framework.Assert.assertFalse(service.isKerberosEnabled(map));
        map.put("core-site", java.util.Collections.singletonMap("hadoop.security.authentication", "kerberos"));
        map.put("hdfs-site", java.util.Collections.singletonMap("hadoop.security.authentication", "none"));
        junit.framework.Assert.assertTrue(service.isKerberosEnabled(map));
        map.put("core-site", java.util.Collections.singletonMap("hadoop.security.authentication", "none"));
        map.put("hdfs-site", java.util.Collections.singletonMap("hadoop.security.authentication", "kerberos"));
        junit.framework.Assert.assertTrue(service.isKerberosEnabled(map));
        map.put("core-site", java.util.Collections.singletonMap("hadoop.security.authentication", "kerberos"));
        map.put("hdfs-site", java.util.Collections.singletonMap("hadoop.security.authentication", "kerberos"));
        junit.framework.Assert.assertTrue(service.isKerberosEnabled(map));
    }

    @org.junit.Test
    public void testClusterConfigsUnmappingOnDeleteAllServiceConfigs() throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        org.apache.ambari.server.state.Config config1 = configFactory.createNew(cluster, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        cluster.addDesiredConfig("admin", java.util.Collections.singleton(config1));
        org.apache.ambari.server.state.Config config2 = configFactory.createNew(cluster, "hdfs-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        cluster.addDesiredConfig("admin", java.util.Collections.singleton(config2));
        java.util.Collection<org.apache.ambari.server.orm.entities.ClusterConfigEntity> clusterConfigEntities = cluster.getClusterEntity().getClusterConfigEntities();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : clusterConfigEntities) {
            org.junit.Assert.assertFalse(clusterConfigEntity.isUnmapped());
        }
        ((org.apache.ambari.server.state.ServiceImpl) (service)).deleteAllServiceConfigs();
        clusterConfigEntities = cluster.getClusterEntity().getClusterConfigEntities();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : clusterConfigEntities) {
            org.junit.Assert.assertTrue(clusterConfigEntity.isUnmapped());
        }
    }

    @org.junit.Test
    public void testDeleteAllServiceConfigGroups() throws org.apache.ambari.server.AmbariException {
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup1 = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup2 = EasyMock.createNiceMock(org.apache.ambari.server.state.configgroup.ConfigGroup.class);
        EasyMock.expect(configGroup1.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(configGroup2.getId()).andReturn(2L).anyTimes();
        EasyMock.expect(configGroup1.getServiceName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(configGroup2.getServiceName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(configGroup1.getClusterName()).andReturn(cluster.getClusterName()).anyTimes();
        EasyMock.expect(configGroup2.getClusterName()).andReturn(cluster.getClusterName()).anyTimes();
        EasyMock.replay(configGroup1, configGroup2);
        cluster.addConfigGroup(configGroup1);
        cluster.addConfigGroup(configGroup2);
        ((org.apache.ambari.server.state.ServiceImpl) (service)).deleteAllServiceConfigGroups();
        org.junit.Assert.assertTrue(org.apache.commons.collections.MapUtils.isEmpty(cluster.getConfigGroupsByServiceName(serviceName)));
        EasyMock.verify(configGroup1, configGroup2);
    }

    private void addHostToCluster(java.lang.String hostname, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        clusters.addHost(hostname);
        org.apache.ambari.server.state.Host h = clusters.getHost(hostname);
        h.setIPv4(hostname + "ipv4");
        h.setIPv6(hostname + "ipv6");
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "6.3");
        h.setHostAttributes(hostAttributes);
        clusters.mapHostToCluster(hostname, clusterName);
    }
}