package org.apache.ambari.server.state.svccomphost;
public class ServiceComponentHostTest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.svccomphost.ServiceComponentHostTest.class);

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceFactory serviceFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentFactory serviceComponentFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ServiceComponentHostFactory serviceComponentHostFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.ConfigFactory configFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.configgroup.ConfigGroupFactory configGroupFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO;

    private java.lang.String clusterName = "c1";

    private java.lang.String hostName1 = "h1";

    private java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.0.6");
        createCluster(stackId, clusterName);
        repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        hostAttributes.put("os_family", "redhat");
        hostAttributes.put("os_release_version", "5.9");
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        hostNames.add(hostName1);
        addHostsToCluster(clusterName, hostAttributes, hostNames);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    private org.apache.ambari.server.orm.entities.ClusterEntity createCluster(org.apache.ambari.server.state.StackId stackId, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        helper.createStack(stackId);
        clusters.addCluster(clusterName, stackId);
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName(clusterName);
        org.junit.Assert.assertNotNull(clusterEntity);
        return clusterEntity;
    }

    private void addHostsToCluster(java.lang.String clusterName, java.util.Map<java.lang.String, java.lang.String> hostAttributes, java.util.Set<java.lang.String> hostNames) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterDAO.findByName(clusterName);
        java.util.List<org.apache.ambari.server.orm.entities.HostEntity> hostEntities = new java.util.ArrayList<>();
        for (java.lang.String hostName : hostNames) {
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host = clusters.getHost(hostName);
            host.setHostAttributes(hostAttributes);
        }
        clusterEntity.setHostEntities(hostEntities);
        clusterDAO.merge(clusterEntity);
        clusters.mapAndPublishHostsToCluster(hostNames, clusterName);
    }

    private org.apache.ambari.server.state.ServiceComponentHost createNewServiceComponentHost(java.lang.String clusterName, java.lang.String svc, java.lang.String svcComponent, java.lang.String hostName, boolean isClient) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        org.junit.Assert.assertNotNull(c.getConfigGroups());
        return createNewServiceComponentHost(c, svc, svcComponent, hostName);
    }

    private org.apache.ambari.server.state.ServiceComponentHost createNewServiceComponentHost(org.apache.ambari.server.state.Cluster c, java.lang.String svc, java.lang.String svcComponent, java.lang.String hostName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Service s = null;
        try {
            s = c.getService(svc);
        } catch (org.apache.ambari.server.ServiceNotFoundException e) {
            org.apache.ambari.server.state.svccomphost.ServiceComponentHostTest.LOG.debug("Calling service create, serviceName={}", svc);
            s = serviceFactory.createNew(c, svc, repositoryVersion);
            c.addService(s);
        }
        org.apache.ambari.server.state.ServiceComponent sc = null;
        try {
            sc = s.getServiceComponent(svcComponent);
        } catch (org.apache.ambari.server.ServiceComponentNotFoundException e) {
            sc = serviceComponentFactory.createNew(s, svcComponent);
            s.addServiceComponent(sc);
        }
        org.apache.ambari.server.state.ServiceComponentHost impl = serviceComponentHostFactory.createNew(sc, hostName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, impl.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INIT, impl.getDesiredState());
        org.junit.Assert.assertEquals(c.getClusterName(), impl.getClusterName());
        org.junit.Assert.assertEquals(c.getClusterId(), impl.getClusterId());
        org.junit.Assert.assertEquals(s.getName(), impl.getServiceName());
        org.junit.Assert.assertEquals(sc.getName(), impl.getServiceComponentName());
        org.junit.Assert.assertEquals(hostName, impl.getHostName());
        org.junit.Assert.assertNotNull(c.getServiceComponentHosts(hostName));
        org.junit.Assert.assertNotNull(sc.getDesiredRepositoryVersion());
        return impl;
    }

    private org.apache.ambari.server.state.ServiceComponentHostEvent createEvent(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl, long timestamp, org.apache.ambari.server.state.ServiceComponentHostEventType eventType) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster c = clusters.getCluster(clusterName);
        if (c.getConfig("time", java.lang.String.valueOf(timestamp)) == null) {
            org.apache.ambari.server.state.Config config = configFactory.createNew(c, "time", java.lang.String.valueOf(timestamp), new java.util.HashMap<>(), new java.util.HashMap<>());
        }
        switch (eventType) {
            case HOST_SVCCOMP_INSTALL :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp, impl.getServiceComponent().getDesiredStackId().toString());
            case HOST_SVCCOMP_START :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            case HOST_SVCCOMP_STOP :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStopEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            case HOST_SVCCOMP_UNINSTALL :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUninstallEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            case HOST_SVCCOMP_OP_FAILED :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            case HOST_SVCCOMP_OP_SUCCEEDED :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            case HOST_SVCCOMP_OP_IN_PROGRESS :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            case HOST_SVCCOMP_OP_RESTART :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpRestartedEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            case HOST_SVCCOMP_DISABLE :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostDisableEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            case HOST_SVCCOMP_WIPEOUT :
                return new org.apache.ambari.server.state.svccomphost.ServiceComponentHostWipeoutEvent(impl.getServiceComponentName(), impl.getHostName(), timestamp);
            default :
                return null;
        }
    }

    private void runStateChanges(org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl, org.apache.ambari.server.state.ServiceComponentHostEventType startEventType, org.apache.ambari.server.state.State startState, org.apache.ambari.server.state.State inProgressState, org.apache.ambari.server.state.State failedState, org.apache.ambari.server.state.State completedState) throws java.lang.Exception {
        long timestamp = 0;
        boolean checkStack = false;
        if (startEventType == org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL) {
            checkStack = true;
        }
        org.junit.Assert.assertEquals(startState, impl.getState());
        org.apache.ambari.server.state.ServiceComponentHostEvent startEvent = createEvent(impl, ++timestamp, startEventType);
        long startTime = timestamp;
        impl.handleEvent(startEvent);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(inProgressState, impl.getState());
        if (checkStack) {
            org.junit.Assert.assertNotNull(impl.getServiceComponent().getDesiredStackId());
        }
        org.apache.ambari.server.state.ServiceComponentHostEvent installEvent2 = createEvent(impl, ++timestamp, startEventType);
        boolean exceptionThrown = false;
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostTest.LOG.info((("Transitioning from " + impl.getState()) + " ") + installEvent2.getType());
        try {
            impl.handleEvent(installEvent2);
        } catch (java.lang.Exception e) {
            exceptionThrown = true;
        }
        if (((((impl.getState() == org.apache.ambari.server.state.State.INSTALLING) || (impl.getState() == org.apache.ambari.server.state.State.STARTING)) || (impl.getState() == org.apache.ambari.server.state.State.UNINSTALLING)) || (impl.getState() == org.apache.ambari.server.state.State.WIPING_OUT)) || (impl.getState() == org.apache.ambari.server.state.State.STARTED)) {
            startTime = timestamp;
            org.junit.Assert.assertTrue("Exception not thrown on invalid event", !exceptionThrown);
        } else {
            org.junit.Assert.assertTrue("Exception not thrown on invalid event", exceptionThrown);
        }
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(inProgressState, impl.getState());
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent inProgressEvent1 = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        impl.handleEvent(inProgressEvent1);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(timestamp, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(inProgressState, impl.getState());
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent inProgressEvent2 = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        impl.handleEvent(inProgressEvent2);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(timestamp, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(inProgressState, impl.getState());
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent failEvent = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        long endTime = timestamp;
        impl.handleEvent(failEvent);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(timestamp, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(endTime, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(failedState, impl.getState());
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpRestartedEvent restartEvent = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpRestartedEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        startTime = timestamp;
        impl.handleEvent(restartEvent);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(inProgressState, impl.getState());
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent inProgressEvent3 = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        impl.handleEvent(inProgressEvent3);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(timestamp, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(inProgressState, impl.getState());
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent failEvent2 = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        endTime = timestamp;
        impl.handleEvent(failEvent2);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(timestamp, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(endTime, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(failedState, impl.getState());
        org.apache.ambari.server.state.ServiceComponentHostEvent startEvent2 = createEvent(impl, ++timestamp, startEventType);
        startTime = timestamp;
        impl.handleEvent(startEvent2);
        org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(inProgressState, impl.getState());
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent inProgressEvent4 = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        impl.handleEvent(inProgressEvent4);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(timestamp, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(inProgressState, impl.getState());
        org.apache.ambari.server.state.ServiceComponentHostEvent succeededEvent;
        if (startEventType == org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_START) {
            succeededEvent = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartedEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        } else if (startEventType == org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOP) {
            succeededEvent = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStoppedEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        } else {
            succeededEvent = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpSucceededEvent(impl.getServiceComponentName(), impl.getHostName(), ++timestamp);
        }
        endTime = timestamp;
        impl.handleEvent(succeededEvent);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(timestamp, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(endTime, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(completedState, impl.getState());
    }

    @org.junit.Test
    public void testClientStateFlow() throws java.lang.Exception {
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl = ((org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl) (createNewServiceComponentHost(clusterName, "HDFS", "HDFS_CLIENT", hostName1, true)));
        runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, org.apache.ambari.server.state.State.INIT, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALLED);
        boolean exceptionThrown = false;
        try {
            runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_START, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTED);
        } catch (java.lang.Exception e) {
            exceptionThrown = true;
        }
        org.junit.Assert.assertTrue("Exception not thrown on invalid event", exceptionThrown);
        runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UNINSTALL, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLED);
        runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_WIPEOUT, org.apache.ambari.server.state.State.UNINSTALLED, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.INIT);
        for (org.apache.ambari.server.state.State state : org.apache.ambari.server.state.State.values()) {
            impl.setState(state);
            if (state.isRemovableState()) {
                org.junit.Assert.assertTrue(impl.canBeRemoved());
            } else {
                org.junit.Assert.assertFalse(impl.canBeRemoved());
            }
        }
    }

    @org.junit.Test
    public void testDaemonStateFlow() throws java.lang.Exception {
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl = ((org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl) (createNewServiceComponentHost(clusterName, "HDFS", "DATANODE", hostName1, false)));
        runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_INSTALL, org.apache.ambari.server.state.State.INIT, org.apache.ambari.server.state.State.INSTALLING, org.apache.ambari.server.state.State.INSTALL_FAILED, org.apache.ambari.server.state.State.INSTALLED);
        runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_START, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTED);
        runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOP, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.INSTALLED);
        runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_UNINSTALL, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLING, org.apache.ambari.server.state.State.UNINSTALLED);
        runStateChanges(impl, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_WIPEOUT, org.apache.ambari.server.state.State.UNINSTALLED, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.WIPING_OUT, org.apache.ambari.server.state.State.INIT);
    }

    @org.junit.Ignore
    @org.junit.Test
    public void testJobHandling() {
    }

    @org.junit.Ignore
    @org.junit.Test
    public void testGetAndSetConfigs() {
    }

    @org.junit.Test
    public void testGetAndSetBasicInfo() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponentHost sch = createNewServiceComponentHost(clusterName, "HDFS", "NAMENODE", hostName1, false);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLING, sch.getState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, sch.getDesiredState());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testActualConfigs() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceComponentHost sch = createNewServiceComponentHost(clusterName, "HDFS", "NAMENODE", hostName1, false);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        final org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(cluster, "HDFS", "cg1", "t1", "", new java.util.HashMap<>(), new java.util.HashMap<>());
        cluster.addConfigGroup(configGroup);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> actual = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("global", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version1");
                    }
                });
                put("core-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version1");
                        put(configGroup.getId().toString(), "version2");
                    }
                });
            }
        };
        java.util.Map<java.lang.String, org.apache.ambari.server.state.HostConfig> confirm = sch.getActualConfigs();
        org.junit.Assert.assertEquals(2, confirm.size());
        org.junit.Assert.assertTrue(confirm.containsKey("global"));
        org.junit.Assert.assertTrue(confirm.containsKey("core-site"));
        org.junit.Assert.assertEquals(1, confirm.get("core-site").getConfigGroupOverrides().size());
    }

    @org.junit.Test
    public void testConvertToResponse() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponentHost sch = createNewServiceComponentHost(clusterName, "HDFS", "DATANODE", hostName1, false);
        sch.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.apache.ambari.server.controller.ServiceComponentHostResponse r = sch.convertToResponse(null);
        org.junit.Assert.assertEquals("HDFS", r.getServiceName());
        org.junit.Assert.assertEquals("DATANODE", r.getComponentName());
        org.junit.Assert.assertEquals(hostName1, r.getHostname());
        org.junit.Assert.assertEquals(clusterName, r.getClusterName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED.toString(), r.getDesiredState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLING.toString(), r.getLiveState());
        org.junit.Assert.assertEquals(repositoryVersion.getStackId().toString(), r.getDesiredStackVersion());
        org.junit.Assert.assertFalse(r.isStaleConfig());
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sch.debugDump(sb);
        org.junit.Assert.assertFalse(sb.toString().isEmpty());
    }

    @org.junit.Test
    public void testStopInVariousStates() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.ServiceComponentHost sch = createNewServiceComponentHost(clusterName, "HDFS", "DATANODE", hostName1, false);
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl = ((org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl) (sch));
        sch.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        long timestamp = 0;
        org.apache.ambari.server.state.ServiceComponentHostEvent stopEvent = createEvent(impl, ++timestamp, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOP);
        long startTime = timestamp;
        impl.handleEvent(stopEvent);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STOPPING, impl.getState());
        sch.setState(org.apache.ambari.server.state.State.INSTALL_FAILED);
        boolean exceptionThrown = false;
        try {
            impl.handleEvent(stopEvent);
        } catch (java.lang.Exception e) {
            exceptionThrown = true;
        }
        org.junit.Assert.assertTrue("Exception not thrown on invalid event", exceptionThrown);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        sch.setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.state.ServiceComponentHostEvent stopEvent2 = createEvent(impl, ++timestamp, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_STOP);
        startTime = timestamp;
        impl.handleEvent(stopEvent2);
        org.junit.Assert.assertEquals(startTime, impl.getLastOpStartTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
        org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STOPPING, impl.getState());
    }

    @org.junit.Test
    public void testDisableInVariousStates() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.ServiceComponentHost sch = createNewServiceComponentHost(clusterName, "HDFS", "DATANODE", hostName1, false);
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl impl = ((org.apache.ambari.server.state.svccomphost.ServiceComponentHostImpl) (sch));
        long timestamp = 0;
        java.util.HashSet<org.apache.ambari.server.state.State> validStates = new java.util.HashSet<>();
        validStates.add(org.apache.ambari.server.state.State.INSTALLED);
        validStates.add(org.apache.ambari.server.state.State.INSTALL_FAILED);
        validStates.add(org.apache.ambari.server.state.State.UNKNOWN);
        validStates.add(org.apache.ambari.server.state.State.DISABLED);
        for (org.apache.ambari.server.state.State state : validStates) {
            sch.setState(state);
            org.apache.ambari.server.state.ServiceComponentHostEvent disableEvent = createEvent(impl, ++timestamp, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_DISABLE);
            impl.handleEvent(disableEvent);
            org.junit.Assert.assertEquals(-1, impl.getLastOpStartTime());
            org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
            org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
            org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.DISABLED, impl.getState());
        }
        java.util.HashSet<org.apache.ambari.server.state.State> invalidStates = new java.util.HashSet<>();
        invalidStates.add(org.apache.ambari.server.state.State.INIT);
        invalidStates.add(org.apache.ambari.server.state.State.INSTALLING);
        invalidStates.add(org.apache.ambari.server.state.State.STARTING);
        invalidStates.add(org.apache.ambari.server.state.State.STARTED);
        invalidStates.add(org.apache.ambari.server.state.State.STOPPING);
        invalidStates.add(org.apache.ambari.server.state.State.UNINSTALLING);
        invalidStates.add(org.apache.ambari.server.state.State.UNINSTALLED);
        invalidStates.add(org.apache.ambari.server.state.State.UPGRADING);
        for (org.apache.ambari.server.state.State state : invalidStates) {
            sch.setState(state);
            org.apache.ambari.server.state.ServiceComponentHostEvent disableEvent = createEvent(impl, ++timestamp, org.apache.ambari.server.state.ServiceComponentHostEventType.HOST_SVCCOMP_DISABLE);
            boolean exceptionThrown = false;
            try {
                impl.handleEvent(disableEvent);
            } catch (java.lang.Exception e) {
                exceptionThrown = true;
            }
            org.junit.Assert.assertTrue("Exception not thrown on invalid event", exceptionThrown);
            org.junit.Assert.assertEquals(-1, impl.getLastOpStartTime());
            org.junit.Assert.assertEquals(-1, impl.getLastOpLastUpdateTime());
            org.junit.Assert.assertEquals(-1, impl.getLastOpEndTime());
        }
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testStaleConfigs() throws java.lang.Exception {
        java.lang.String stackVersion = "HDP-2.0.6";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackVersion);
        java.lang.String clusterName = "c2";
        createCluster(stackId, clusterName);
        final java.lang.String hostName = "h3";
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        hostNames.add(hostName);
        addHostsToCluster(clusterName, hostAttributes, hostNames);
        final org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
        org.junit.Assert.assertNotNull(hostEntity.getHostId());
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.junit.Assert.assertNotNull(cluster);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.state.ServiceComponentHost sch1 = createNewServiceComponentHost(cluster, "HDFS", "NAMENODE", hostName);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = createNewServiceComponentHost(cluster, "HDFS", "DATANODE", hostName);
        org.apache.ambari.server.state.ServiceComponentHost sch3 = createNewServiceComponentHost(cluster, "MAPREDUCE2", "HISTORYSERVER", hostName);
        sch1.getServiceComponent().setDesiredRepositoryVersion(repositoryVersion);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setState(org.apache.ambari.server.state.State.INSTALLING);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLING);
        sch3.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        makeConfig(cluster, "hdfs-site", "version0", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> actual = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version0");
                    }
                });
            }
        };
        makeConfig(cluster, "foo", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "c");
            }
        }, new java.util.HashMap<>());
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        makeConfig(cluster, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a1", "b1");
            }
        }, new java.util.HashMap<>());
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        actual.put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("tag", "version1");
            }
        });
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        makeConfig(cluster, "hdfs-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("dfs.journalnode.http-address", "http://foo");
            }
        }, new java.util.HashMap<>());
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        actual.get("hdfs-site").put("tag", "version2");
        sch1.setRestartRequired(false);
        sch2.setRestartRequired(false);
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        final org.apache.ambari.server.state.Host host = clusters.getHostsForCluster(clusterName).get(hostName);
        org.junit.Assert.assertNotNull(host);
        final org.apache.ambari.server.state.Config c = configFactory.createNew(cluster, "hdfs-site", "version3", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("dfs.journalnode.http-address", "http://goo");
            }
        }, new java.util.HashMap<>());
        host.addDesiredConfig(cluster.getClusterId(), true, "user", c);
        org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupFactory.createNew(cluster, "HDFS", "g1", "t1", "", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Config>() {
            {
                put("hdfs-site", c);
            }
        }, new java.util.HashMap<java.lang.Long, org.apache.ambari.server.state.Host>() {
            {
                put(hostEntity.getHostId(), host);
            }
        });
        cluster.addConfigGroup(configGroup);
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        actual.get("hdfs-site").put(configGroup.getId().toString(), "version3");
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        makeConfig(cluster, "mapred-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
            }
        }, new java.util.HashMap<>());
        actual.put("mapred-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("tag", "version1");
            }
        });
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch3.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch3.convertToResponse(null).isStaleConfig());
        makeConfig(cluster, "core-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
                put("fs.trash.interval", "360");
            }
        }, new java.util.HashMap<>());
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch3.convertToResponse(null).isStaleConfig());
        actual.put("core-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("tag", "version1");
            }
        });
        final org.apache.ambari.server.state.Config c1 = configFactory.createNew(cluster, "core-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("fs.trash.interval", "400");
            }
        }, new java.util.HashMap<>());
        configGroup = configGroupFactory.createNew(cluster, "HDFS", "g2", "t2", "", new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Config>() {
            {
                put("core-site", c1);
            }
        }, new java.util.HashMap<java.lang.Long, org.apache.ambari.server.state.Host>() {
            {
                put(hostEntity.getHostId(), host);
            }
        });
        cluster.addConfigGroup(configGroup);
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch3.convertToResponse(null).isStaleConfig());
        java.lang.Long id = configGroup.getId();
        java.util.HashMap<java.lang.String, java.lang.String> tags = new java.util.HashMap<>(2);
        tags.put("tag", "version1");
        tags.put(id.toString(), "version2");
        actual.put("core-site", tags);
        org.junit.Assert.assertFalse(sch3.convertToResponse(null).isStaleConfig());
        cluster.deleteConfigGroup(id);
        org.junit.Assert.assertNull(cluster.getConfigGroups().get(id));
        org.junit.Assert.assertTrue(sch3.convertToResponse(null).isStaleConfig());
        tags.remove(id.toString());
        org.junit.Assert.assertFalse(sch3.convertToResponse(null).isStaleConfig());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testStaleConfigsAttributes() throws java.lang.Exception {
        java.lang.String stackVersion = "HDP-2.0.6";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackVersion);
        java.lang.String clusterName = "c2";
        createCluster(stackId, clusterName);
        final java.lang.String hostName = "h3";
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        hostNames.add(hostName);
        addHostsToCluster(clusterName, hostAttributes, hostNames);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.state.ServiceComponentHost sch1 = createNewServiceComponentHost(cluster, "HDFS", "NAMENODE", hostName);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = createNewServiceComponentHost(cluster, "HDFS", "DATANODE", hostName);
        org.apache.ambari.server.state.ServiceComponentHost sch3 = createNewServiceComponentHost(cluster, "MAPREDUCE2", "HISTORYSERVER", hostName);
        sch1.getServiceComponent().setDesiredRepositoryVersion(repositoryVersion);
        sch1.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch1.setState(org.apache.ambari.server.state.State.INSTALLING);
        sch2.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch2.setState(org.apache.ambari.server.state.State.INSTALLING);
        sch3.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        sch3.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        makeConfig(cluster, "global", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "b");
                put("dfs_namenode_name_dir", "/foo1");
                put("mapred_log_dir_prefix", "/foo2");
            }
        }, new java.util.HashMap<>());
        makeConfig(cluster, "hdfs-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("hdfs1", "hdfs1value1");
            }
        }, new java.util.HashMap<>());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> actual = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("global", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version1");
                    }
                });
                put("hdfs-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version1");
                    }
                });
            }
        };
        makeConfig(cluster, "mapred-site", "version1", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("a", "c");
            }
        }, new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("final", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("a", "true");
                    }
                });
            }
        });
        org.junit.Assert.assertFalse(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch3.convertToResponse(null).isStaleConfig());
        actual = new java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>() {
            {
                put("global", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version1");
                    }
                });
                put("mapred-site", new java.util.HashMap<java.lang.String, java.lang.String>() {
                    {
                        put("tag", "version1");
                    }
                });
            }
        };
        sch3.setRestartRequired(false);
        org.junit.Assert.assertFalse(sch3.convertToResponse(null).isStaleConfig());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> c1PropAttributes = new java.util.HashMap<>();
        c1PropAttributes.put("final", new java.util.HashMap<>());
        c1PropAttributes.get("final").put("hdfs1", "true");
        makeConfig(cluster, "hdfs-site", "version2", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("hdfs1", "hdfs1value1");
            }
        }, c1PropAttributes);
        sch1.setRestartRequired(false);
        sch2.setRestartRequired(false);
        sch3.setRestartRequired(false);
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch3.convertToResponse(null).isStaleConfig());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> c2PropAttributes = new java.util.HashMap<>();
        c2PropAttributes.put("final", new java.util.HashMap<>());
        c2PropAttributes.get("final").put("hdfs1", "false");
        makeConfig(cluster, "hdfs-site", "version3", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("hdfs1", "hdfs1value1");
            }
        }, c2PropAttributes);
        sch1.setRestartRequired(false);
        sch2.setRestartRequired(false);
        sch3.setRestartRequired(false);
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch3.convertToResponse(null).isStaleConfig());
        makeConfig(cluster, "hdfs-site", "version4", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("hdfs1", "hdfs1value1");
            }
        }, new java.util.HashMap<>());
        sch1.setRestartRequired(false);
        sch2.setRestartRequired(false);
        sch3.setRestartRequired(false);
        org.junit.Assert.assertTrue(sch1.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertTrue(sch2.convertToResponse(null).isStaleConfig());
        org.junit.Assert.assertFalse(sch3.convertToResponse(null).isStaleConfig());
    }

    private void makeConfig(org.apache.ambari.server.state.Cluster cluster, java.lang.String type, java.lang.String tag, java.util.Map<java.lang.String, java.lang.String> values, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config config = configFactory.createNew(cluster, type, tag, values, attributes);
        cluster.addDesiredConfig("user", java.util.Collections.singleton(config));
    }

    @org.junit.Test
    public void testMaintenance() throws java.lang.Exception {
        java.lang.String stackVersion = "HDP-2.0.6";
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackVersion);
        java.lang.String clusterName = "c2";
        createCluster(stackId, clusterName);
        final java.lang.String hostName = "h3";
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        hostNames.add(hostName);
        addHostsToCluster(clusterName, hostAttributes, hostNames);
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        helper.getOrCreateRepositoryVersion(stackId, stackId.getStackVersion());
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName);
        org.junit.Assert.assertNotNull(hostEntity);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = createNewServiceComponentHost(cluster, "HDFS", "NAMENODE", hostName);
        org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity entity = hostComponentDesiredStateDAO.findByIndex(cluster.getClusterId(), sch1.getServiceName(), sch1.getServiceComponentName(), hostEntity.getHostId());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, entity.getMaintenanceState());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.OFF, sch1.getMaintenanceState());
        sch1.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, sch1.getMaintenanceState());
        entity = hostComponentDesiredStateDAO.findByIndex(cluster.getClusterId(), sch1.getServiceName(), sch1.getServiceComponentName(), hostEntity.getHostId());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.MaintenanceState.ON, entity.getMaintenanceState());
    }

    @org.junit.Test
    public void testHostVersionTransitionIsScopedByRepository() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findByName(hostName1);
        java.util.Collection<org.apache.ambari.server.orm.entities.HostVersionEntity> hostVersions = hostEntity.getHostVersionEntities();
        org.junit.Assert.assertEquals(1, hostVersions.size());
        hostVersions.iterator().next().setState(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC);
        hostDAO.merge(hostEntity);
        org.apache.ambari.server.state.ServiceComponentHost namenode = createNewServiceComponentHost(clusterName, "HDFS", "NAMENODE", hostName1, false);
        namenode.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        namenode.setState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.state.ServiceComponentHost datanode = createNewServiceComponentHost(clusterName, "HDFS", "DATANODE", hostName1, false);
        datanode.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        datanode.setState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.state.ServiceComponentHost zkServer = createNewServiceComponentHost(clusterName, "ZOOKEEPER", "ZOOKEEPER_SERVER", hostName1, false);
        zkServer.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        zkServer.setState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.state.ServiceComponentHost zkClient = createNewServiceComponentHost(clusterName, "ZOOKEEPER", "ZOOKEEPER_CLIENT", hostName1, true);
        zkClient.setDesiredState(org.apache.ambari.server.state.State.STARTED);
        zkClient.setState(org.apache.ambari.server.state.State.STARTED);
        hostEntity = hostDAO.findByName(hostName1);
        java.util.Collection<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostEntity.getHostComponentStateEntities();
        for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentState : hostComponentStates) {
            if (org.apache.commons.lang3.StringUtils.equals("HDFS", hostComponentState.getServiceName())) {
                hostComponentState.setVersion(org.apache.ambari.server.state.State.UNKNOWN.name());
                hostComponentStateDAO.merge(hostComponentState);
            }
        }
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-2.2.0");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity patchRepositoryVersion = helper.getOrCreateRepositoryVersion(stackId, "2.2.0.0-1");
        zkServer.getServiceComponent().setDesiredRepositoryVersion(patchRepositoryVersion);
        zkClient.getServiceComponent().setDesiredRepositoryVersion(patchRepositoryVersion);
        helper.createHostVersion(hostName1, patchRepositoryVersion, org.apache.ambari.server.state.RepositoryVersionState.INSTALLED);
        hostEntity = hostDAO.findByName(hostName1);
        hostComponentStates = hostEntity.getHostComponentStateEntities();
        for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentState : hostComponentStates) {
            if (org.apache.commons.lang3.StringUtils.equals("ZOOKEEPER", hostComponentState.getServiceName())) {
                hostComponentState.setVersion(patchRepositoryVersion.getVersion());
                hostComponentState.setUpgradeState(org.apache.ambari.server.state.UpgradeState.COMPLETE);
                hostComponentStateDAO.merge(hostComponentState);
            }
        }
        hostEntity = hostDAO.merge(hostEntity);
        zkServer.recalculateHostVersionState();
        hostVersions = hostEntity.getHostVersionEntities();
        org.junit.Assert.assertEquals(2, hostVersions.size());
        for (org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion : hostVersions) {
            if (hostVersion.getRepositoryVersion().equals(repositoryVersion)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.OUT_OF_SYNC, hostVersion.getState());
            } else if (hostVersion.getRepositoryVersion().equals(patchRepositoryVersion)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.state.RepositoryVersionState.CURRENT, hostVersion.getState());
            } else {
                org.junit.Assert.fail("Unexpected repository version");
            }
        }
    }
}