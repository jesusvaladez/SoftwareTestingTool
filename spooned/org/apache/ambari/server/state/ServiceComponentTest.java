package org.apache.ambari.server.state;
public class ServiceComponentTest {
    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.state.Service service;

    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    private org.apache.ambari.server.orm.OrmTestHelper helper;

    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        serviceFactory = injector.getInstance(org.apache.ambari.server.state.ServiceFactory.class);
        serviceComponentFactory = injector.getInstance(org.apache.ambari.server.state.ServiceComponentFactory.class);
        serviceComponentHostFactory = injector.getInstance(org.apache.ambari.server.state.ServiceComponentHostFactory.class);
        helper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        hostDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class);
        clusterName = "foo";
        serviceName = "HDFS";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        helper.createStack(stackId);
        clusters.addCluster(clusterName, stackId);
        cluster = clusters.getCluster(clusterName);
        cluster.setDesiredStackVersion(stackId);
        junit.framework.Assert.assertNotNull(cluster);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.state.Service s = serviceFactory.createNew(cluster, serviceName, repositoryVersion);
        cluster.addService(s);
        service = cluster.getService(serviceName);
        junit.framework.Assert.assertNotNull(service);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testCreateServiceComponent() throws org.apache.ambari.server.AmbariException {
        java.lang.String componentName = "DATANODE2";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        service.addServiceComponent(component);
        org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponent(componentName);
        junit.framework.Assert.assertNotNull(sc);
        junit.framework.Assert.assertEquals(componentName, sc.getName());
        junit.framework.Assert.assertEquals(serviceName, sc.getServiceName());
        junit.framework.Assert.assertEquals(cluster.getClusterId(), sc.getClusterId());
        junit.framework.Assert.assertEquals(cluster.getClusterName(), sc.getClusterName());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, sc.getDesiredState());
        junit.framework.Assert.assertFalse(sc.getDesiredStackId().getStackId().isEmpty());
    }

    @org.junit.Test
    public void testGetAndSetServiceComponentInfo() throws org.apache.ambari.server.AmbariException {
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        service.addServiceComponent(component);
        org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponent(componentName);
        junit.framework.Assert.assertNotNull(sc);
        sc.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
        org.apache.ambari.server.state.StackId newStackId = new org.apache.ambari.server.state.StackId("HDP-1.2.0");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(newStackId, newStackId.getStackVersion());
        sc.setDesiredRepositoryVersion(repositoryVersion);
        junit.framework.Assert.assertEquals(newStackId.toString(), sc.getDesiredStackId().getStackId());
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.apache.ambari.server.state.ServiceComponent sc1 = serviceComponentFactory.createExisting(service, serviceComponentDesiredStateEntity);
        junit.framework.Assert.assertNotNull(sc1);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc1.getDesiredState());
        junit.framework.Assert.assertEquals("HDP-1.2.0", sc1.getDesiredStackId().getStackId());
    }

    @org.junit.Test
    public void testGetAndSetConfigs() {
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

    @org.junit.Test
    public void testAddAndGetServiceComponentHosts() throws org.apache.ambari.server.AmbariException {
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        service.addServiceComponent(component);
        org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponent(componentName);
        junit.framework.Assert.assertNotNull(sc);
        junit.framework.Assert.assertTrue(sc.getServiceComponentHosts().isEmpty());
        try {
            serviceComponentHostFactory.createNew(sc, "h1");
            org.junit.Assert.fail("Expected error for invalid host");
        } catch (java.lang.Exception e) {
        }
        addHostToCluster("h1", service.getCluster().getClusterName());
        addHostToCluster("h2", service.getCluster().getClusterName());
        addHostToCluster("h3", service.getCluster().getClusterName());
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = hostDAO.findByName("h1");
        org.junit.Assert.assertNotNull(hostEntity1);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc.addServiceComponentHost("h1");
        org.apache.ambari.server.state.ServiceComponentHost sch2 = sc.addServiceComponentHost("h2");
        org.junit.Assert.assertNotNull(sch1);
        org.junit.Assert.assertNotNull(sch2);
        try {
            sc.addServiceComponentHost("h2");
            org.junit.Assert.fail("Expected error for dups");
        } catch (java.lang.Exception e) {
        }
        junit.framework.Assert.assertEquals(2, sc.getServiceComponentHosts().size());
        org.apache.ambari.server.state.ServiceComponentHost schCheck = sc.getServiceComponentHost("h2");
        junit.framework.Assert.assertNotNull(schCheck);
        junit.framework.Assert.assertEquals("h2", schCheck.getHostName());
        sc.addServiceComponentHost("h3");
        junit.framework.Assert.assertNotNull(sc.getServiceComponentHost("h3"));
        sch1.setState(org.apache.ambari.server.state.State.STARTING);
        sch1.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO desiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO.class);
        org.apache.ambari.server.orm.dao.HostComponentStateDAO liveStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentStateDAO.class);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity desiredStateEntity = desiredStateDAO.findByIndex(cluster.getClusterId(), serviceName, componentName, hostEntity1.getHostId());
        org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = liveStateDAO.findByIndex(cluster.getClusterId(), serviceName, componentName, hostEntity1.getHostId());
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createExisting(sc, stateEntity, desiredStateEntity);
        junit.framework.Assert.assertNotNull(sch);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.STARTING, sch.getState());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sch.getDesiredState());
        junit.framework.Assert.assertEquals(service.getDesiredRepositoryVersion().getVersion(), sch.getServiceComponent().getDesiredVersion());
    }

    @org.junit.Test
    public void testConvertToResponse() throws org.apache.ambari.server.AmbariException {
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        service.addServiceComponent(component);
        addHostToCluster("h1", service.getCluster().getClusterName());
        addHostToCluster("h2", service.getCluster().getClusterName());
        addHostToCluster("h3", service.getCluster().getClusterName());
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(component, "h1");
        org.apache.ambari.server.state.ServiceComponentHost sch2 = serviceComponentHostFactory.createNew(component, "h2");
        org.apache.ambari.server.state.ServiceComponentHost sch3 = serviceComponentHostFactory.createNew(component, "h3");
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLED);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> compHosts = new java.util.HashMap<>();
        compHosts.put("h1", sch);
        compHosts.put("h2", sch2);
        compHosts.put("h3", sch3);
        component.addServiceComponentHosts(compHosts);
        junit.framework.Assert.assertEquals(3, component.getServiceComponentHosts().size());
        component.getServiceComponentHost("h2").setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        sch3.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponent(componentName);
        junit.framework.Assert.assertNotNull(sc);
        sc.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.controller.ServiceComponentResponse r = sc.convertToResponse();
        junit.framework.Assert.assertEquals(sc.getClusterName(), r.getClusterName());
        junit.framework.Assert.assertEquals(sc.getClusterId(), r.getClusterId().longValue());
        junit.framework.Assert.assertEquals(sc.getName(), r.getComponentName());
        junit.framework.Assert.assertEquals(sc.getServiceName(), r.getServiceName());
        junit.framework.Assert.assertEquals(sc.getDesiredStackId().getStackId(), r.getDesiredStackId());
        junit.framework.Assert.assertEquals(sc.getDesiredState().toString(), r.getDesiredState());
        int totalCount = r.getServiceComponentStateCount().get("totalCount");
        int startedCount = r.getServiceComponentStateCount().get("startedCount");
        int installedCount = r.getServiceComponentStateCount().get("installedCount");
        int installedAndMaintenanceOffCount = r.getServiceComponentStateCount().get("installedAndMaintenanceOffCount");
        junit.framework.Assert.assertEquals(3, totalCount);
        junit.framework.Assert.assertEquals(0, startedCount);
        junit.framework.Assert.assertEquals(3, installedCount);
        junit.framework.Assert.assertEquals(1, installedAndMaintenanceOffCount);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sc.debugDump(sb);
        junit.framework.Assert.assertFalse(sb.toString().isEmpty());
    }

    @org.junit.Test
    public void testCanBeRemoved() throws java.lang.Exception {
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        addHostToCluster("h1", service.getCluster().getClusterName());
        org.apache.ambari.server.state.ServiceComponentHost sch = serviceComponentHostFactory.createNew(component, "h1");
        component.addServiceComponentHost(sch);
        for (org.apache.ambari.server.state.State state : org.apache.ambari.server.state.State.values()) {
            component.setDesiredState(state);
            for (org.apache.ambari.server.state.State hcState : org.apache.ambari.server.state.State.values()) {
                sch.setDesiredState(hcState);
                sch.setState(hcState);
                if (hcState.isRemovableState()) {
                    org.junit.Assert.assertTrue(component.canBeRemoved());
                } else {
                    org.junit.Assert.assertFalse(component.canBeRemoved());
                }
            }
        }
    }

    @org.junit.Test
    public void testServiceComponentRemove() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        service.addServiceComponent(component);
        org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponent(componentName);
        junit.framework.Assert.assertNotNull(sc);
        sc.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, sc.getDesiredState());
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        junit.framework.Assert.assertNotNull(serviceComponentDesiredStateEntity);
        junit.framework.Assert.assertTrue(sc.getServiceComponentHosts().isEmpty());
        addHostToCluster("h1", service.getCluster().getClusterName());
        addHostToCluster("h2", service.getCluster().getClusterName());
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = hostDAO.findByName("h1");
        org.junit.Assert.assertNotNull(hostEntity1);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = serviceComponentHostFactory.createNew(sc, "h1");
        org.apache.ambari.server.state.ServiceComponentHost sch2 = serviceComponentHostFactory.createNew(sc, "h2");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> compHosts = new java.util.HashMap<>();
        compHosts.put("h1", sch1);
        compHosts.put("h2", sch2);
        sc.addServiceComponentHosts(compHosts);
        sch1.setState(org.apache.ambari.server.state.State.STARTED);
        sch2.setState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData deleteMetaData = new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData();
        sc.delete(deleteMetaData);
        junit.framework.Assert.assertNotNull("Delete must fail as some SCH are in STARTED state", deleteMetaData.getAmbariException());
        sch1.setState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALL_FAILED);
        sc.delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        junit.framework.Assert.assertNull(serviceComponentDesiredStateEntity);
    }

    @org.junit.Test
    public void testVersionCreation() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        service.addServiceComponent(component);
        org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponent(componentName);
        junit.framework.Assert.assertNotNull(sc);
        sc.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.2.0");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity rve = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity(stackEntity, "HDP-2.2.0", "2.2.0.1-1111", new java.util.ArrayList<>());
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        repositoryDAO.create(rve);
        sc.setDesiredRepositoryVersion(rve);
        junit.framework.Assert.assertEquals(rve, sc.getDesiredRepositoryVersion());
        junit.framework.Assert.assertEquals(new org.apache.ambari.server.state.StackId("HDP", "2.2.0"), sc.getDesiredStackId());
        junit.framework.Assert.assertEquals("HDP-2.2.0", sc.getDesiredStackId().getStackId());
        junit.framework.Assert.assertNotNull(serviceComponentDesiredStateEntity);
        org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity version = new org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity();
        version.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        version.setRepositoryVersion(rve);
        version.setUserName("user");
        serviceComponentDesiredStateEntity.addVersion(version);
        serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.merge(serviceComponentDesiredStateEntity);
        serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.junit.Assert.assertEquals(1, serviceComponentDesiredStateEntity.getVersions().size());
        org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity persistedVersion = serviceComponentDesiredStateEntity.getVersions().iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, persistedVersion.getState());
    }

    @org.junit.Test
    public void testVersionRemoval() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        service.addServiceComponent(component);
        org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponent(componentName);
        junit.framework.Assert.assertNotNull(sc);
        sc.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find("HDP", "2.2.0");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity rve = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity(stackEntity, "HDP-2.2.0", "2.2.0.1-1111", new java.util.ArrayList<>());
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        repositoryDAO.create(rve);
        sc.setDesiredRepositoryVersion(rve);
        org.apache.ambari.server.state.StackId stackId = sc.getDesiredStackId();
        junit.framework.Assert.assertEquals(new org.apache.ambari.server.state.StackId("HDP", "2.2.0"), stackId);
        junit.framework.Assert.assertEquals("HDP-2.2.0", sc.getDesiredStackId().getStackId());
        junit.framework.Assert.assertNotNull(serviceComponentDesiredStateEntity);
        org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity version = new org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity();
        version.setState(org.apache.ambari.server.state.RepositoryVersionState.CURRENT);
        version.setRepositoryVersion(rve);
        version.setUserName("user");
        serviceComponentDesiredStateEntity.addVersion(version);
        serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.merge(serviceComponentDesiredStateEntity);
        serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.junit.Assert.assertEquals(1, serviceComponentDesiredStateEntity.getVersions().size());
        org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity persistedVersion = serviceComponentDesiredStateEntity.getVersions().iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, persistedVersion.getState());
        sc.delete(new org.apache.ambari.server.controller.internal.DeleteHostComponentStatusMetaData());
        serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        junit.framework.Assert.assertNull(serviceComponentDesiredStateEntity);
        java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity> list = serviceComponentDesiredStateDAO.findVersions(cluster.getClusterId(), serviceName, componentName);
        org.junit.Assert.assertEquals(0, list.size());
    }

    @org.junit.Test
    public void testUpdateStates() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        java.lang.String componentName = "NAMENODE";
        org.apache.ambari.server.state.ServiceComponent component = serviceComponentFactory.createNew(service, componentName);
        org.apache.ambari.server.state.StackId newStackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(newStackId, newStackId.getStackVersion());
        component.setDesiredRepositoryVersion(repositoryVersion);
        service.addServiceComponent(component);
        org.apache.ambari.server.state.ServiceComponent sc = service.getServiceComponent(componentName);
        junit.framework.Assert.assertNotNull(sc);
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity entity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion2201 = helper.getOrCreateRepositoryVersion(component.getDesiredStackId(), "2.2.0.1");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion2202 = helper.getOrCreateRepositoryVersion(component.getDesiredStackId(), "2.2.0.2");
        addHostToCluster("h1", clusterName);
        addHostToCluster("h2", clusterName);
        sc.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sc.getDesiredState());
        org.apache.ambari.server.state.ServiceComponentHost sch1 = sc.addServiceComponentHost("h1");
        org.apache.ambari.server.state.ServiceComponentHost sch2 = sc.addServiceComponentHost("h2");
        sc.setDesiredRepositoryVersion(repositoryVersion);
        sch1.setVersion("2.2.0.1");
        sch2.setVersion("2.2.0.2");
        sc.updateRepositoryState("2.2.0.2");
        entity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, entity.getRepositoryState());
        sc.setDesiredRepositoryVersion(repositoryVersion);
        sch1.setVersion("2.2.0.1");
        sch2.setVersion("2.2.0.1");
        sc.updateRepositoryState("2.2.0.1");
        entity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, entity.getRepositoryState());
        sc.setDesiredRepositoryVersion(repoVersion2201);
        sch1.setVersion("2.2.0.1");
        sch2.setVersion("2.2.0.2");
        sc.updateRepositoryState("2.2.0.2");
        entity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, entity.getRepositoryState());
        sc.setDesiredRepositoryVersion(repoVersion2201);
        sch1.setVersion("2.2.0.1");
        sch2.setVersion("2.2.0.2");
        sc.updateRepositoryState("2.2.0.1");
        entity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, entity.getRepositoryState());
        sc.setDesiredRepositoryVersion(repoVersion2201);
        sch1.setVersion("2.2.0.1");
        sch2.setVersion("2.2.0.1");
        sc.updateRepositoryState("2.2.0.1");
        entity = serviceComponentDesiredStateDAO.findByName(cluster.getClusterId(), serviceName, componentName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, entity.getRepositoryState());
    }
}