package org.apache.ambari.server.agent;
import javax.xml.bind.JAXBException;
import org.codehaus.jackson.JsonGenerationException;
import org.easymock.EasyMock;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCurrentPingPort;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOSRelease;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOs;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyStackId;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE;
import static org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class TestHeartbeatHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.agent.TestHeartbeatHandler.class);

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    long requestId = 23;

    long stageId = 31;

    @com.google.inject.Inject
    org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration config;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessor;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    org.apache.ambari.server.agent.HeartbeatTestHelper heartbeatTestHelper;

    @com.google.inject.Inject
    org.apache.ambari.server.actionmanager.ActionManagerTestHelper actionManagerTestHelper;

    @com.google.inject.Inject
    org.apache.ambari.server.audit.AuditLogger auditLogger;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    @com.google.inject.Inject
    private org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher;

    @org.junit.Rule
    public org.junit.rules.TemporaryFolder temporaryFolder = new org.junit.rules.TemporaryFolder();

    private org.apache.ambari.server.orm.InMemoryDefaultTestModule module;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        module = org.apache.ambari.server.agent.HeartbeatTestHelper.getTestModule();
        injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        injector.injectMembers(this);
        org.easymock.EasyMock.replay(auditLogger, injector.getInstance(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class));
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        org.easymock.EasyMock.reset(auditLogger);
    }

    @org.junit.Test
    public void testHeartbeat() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<>());
        EasyMock.replay(am);
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        org.junit.Assert.assertEquals(hosts.size(), 1);
        org.apache.ambari.server.state.Host hostObject = hosts.iterator().next();
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        hostObject.setOsType(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        java.lang.String hostname = hostObject.getHostName();
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(hostname);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOs);
        hi.setOSRelease(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOSRelease);
        reg.setHostname(hostname);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        handler.handleRegistration(reg);
        hostObject.setState(org.apache.ambari.server.state.HostState.UNHEALTHY);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setResponseId(0);
        org.apache.ambari.server.agent.HostStatus hs = new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus);
        java.util.List<org.apache.ambari.server.state.Alert> al = new java.util.ArrayList<>();
        al.add(new org.apache.ambari.server.state.Alert());
        hb.setNodeStatus(hs);
        hb.setHostname(hostname);
        handler.handleHeartBeat(hb);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostState.HEALTHY, hostObject.getState());
    }

    @org.junit.Test
    public void testStatusHeartbeatWithAnnotation() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setReports(new java.util.ArrayList<>());
        java.util.ArrayList<org.apache.ambari.server.agent.ComponentStatus> componentStatuses = new java.util.ArrayList<>();
        hb.setComponentStatus(componentStatuses);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        }).anyTimes();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.apache.ambari.server.agent.HeartBeatResponse resp = handler.handleHeartBeat(hb);
        junit.framework.Assert.assertFalse(resp.hasMappedComponents());
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INIT);
        hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(1);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setReports(new java.util.ArrayList<>());
        hb.setComponentStatus(componentStatuses);
        resp = handler.handleHeartBeat(hb);
        junit.framework.Assert.assertTrue(resp.hasMappedComponents());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testLiveStatusUpdateAfterStopFailed() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost2 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.STARTED);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setReports(new java.util.ArrayList<>());
        java.util.ArrayList<org.apache.ambari.server.agent.ComponentStatus> componentStatuses = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.ComponentStatus componentStatus1 = new org.apache.ambari.server.agent.ComponentStatus();
        componentStatus1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        componentStatus1.setMessage(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus);
        componentStatus1.setStatus(org.apache.ambari.server.state.State.STARTED.name());
        componentStatus1.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        componentStatuses.add(componentStatus1);
        org.apache.ambari.server.agent.ComponentStatus componentStatus2 = new org.apache.ambari.server.agent.ComponentStatus();
        componentStatus2.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        componentStatus2.setMessage(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus);
        componentStatus2.setStatus(org.apache.ambari.server.state.State.INSTALLED.name());
        componentStatus2.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        componentStatuses.add(componentStatus2);
        hb.setComponentStatus(componentStatuses);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
                add(command);
            }
        });
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        heartbeatProcessor.processHeartbeat(hb);
        org.apache.ambari.server.state.State componentState1 = serviceComponentHost1.getState();
        org.apache.ambari.server.state.State componentState2 = serviceComponentHost2.getState();
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, componentState1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, componentState2);
    }

    @org.junit.Test
    public void testRegistration() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        clusters.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setCurrentPingPort(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCurrentPingPort);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        reg.setPrefix(org.apache.ambari.server.configuration.Configuration.PREFIX_DIR);
        handler.handleRegistration(reg);
        org.junit.Assert.assertEquals(hostObject.getState(), org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES);
        org.junit.Assert.assertEquals(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType, hostObject.getOsType());
        org.junit.Assert.assertEquals(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCurrentPingPort, hostObject.getCurrentPingPort());
        org.junit.Assert.assertTrue(hostObject.getLastRegistrationTime() != 0);
        org.junit.Assert.assertEquals(hostObject.getLastHeartbeatTime(), hostObject.getLastRegistrationTime());
    }

    private org.apache.ambari.server.agent.HeartBeatHandler createHeartBeatHandler() {
        return new org.apache.ambari.server.agent.HeartBeatHandler(config, clusters, actionManagerTestHelper.getMockActionManager(), org.apache.ambari.server.security.encryption.Encryptor.NONE, injector);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testRegistrationRecoveryConfig() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        handler.start();
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setCurrentPingPort(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCurrentPingPort);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        reg.setPrefix(org.apache.ambari.server.configuration.Configuration.PREFIX_DIR);
        org.apache.ambari.server.agent.RegistrationResponse rr = handler.handleRegistration(reg);
        org.apache.ambari.server.agent.RecoveryConfig rc = rr.getRecoveryConfig();
        org.junit.Assert.assertEquals(rc.getEnabledComponents(), "DATANODE,NAMENODE");
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        org.apache.ambari.server.agent.HeartBeatResponse hbr = handler.handleHeartBeat(hb);
        org.junit.Assert.assertNull(hbr.getRecoveryConfig());
        handler.stop();
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testRegistrationRecoveryConfigMaintenanceMode() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT).setRecoveryEnabled(true);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.state.ServiceComponentHost schHdfsClient = hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        schHdfsClient.setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setCurrentPingPort(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCurrentPingPort);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        reg.setPrefix(org.apache.ambari.server.configuration.Configuration.PREFIX_DIR);
        org.apache.ambari.server.agent.RegistrationResponse rr = handler.handleRegistration(reg);
        org.apache.ambari.server.agent.RecoveryConfig rc = rr.getRecoveryConfig();
        org.junit.Assert.assertEquals(rc.getEnabledComponents(), "DATANODE,NAMENODE");
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testRegistrationAgentConfig() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        clusters.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setCurrentPingPort(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCurrentPingPort);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        reg.setPrefix(org.apache.ambari.server.configuration.Configuration.PREFIX_DIR);
        org.apache.ambari.server.agent.RegistrationResponse rr = handler.handleRegistration(reg);
        java.util.Map<java.lang.String, java.lang.String> config = rr.getAgentConfig();
        org.junit.Assert.assertFalse(config.isEmpty());
        org.junit.Assert.assertTrue(config.containsKey(org.apache.ambari.server.configuration.Configuration.CHECK_REMOTE_MOUNTS.getKey()));
        org.junit.Assert.assertTrue("false".equals(config.get(org.apache.ambari.server.configuration.Configuration.CHECK_REMOTE_MOUNTS.getKey())));
        org.junit.Assert.assertTrue(config.containsKey(org.apache.ambari.server.configuration.Configuration.CHECK_MOUNTS_TIMEOUT.getKey()));
        org.junit.Assert.assertTrue("0".equals(config.get(org.apache.ambari.server.configuration.Configuration.CHECK_MOUNTS_TIMEOUT.getKey())));
        org.junit.Assert.assertTrue("true".equals(config.get(org.apache.ambari.server.configuration.Configuration.ENABLE_AUTO_AGENT_CACHE_UPDATE.getKey())));
    }

    @org.junit.Test
    public void testRegistrationWithBadVersion() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        clusters.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion("");
        reg.setPrefix(org.apache.ambari.server.configuration.Configuration.PREFIX_DIR);
        try {
            handler.handleRegistration(reg);
            org.junit.Assert.fail("Expected failure for non compatible agent version");
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.agent.TestHeartbeatHandler.log.debug("Error:{}", e.getMessage());
            junit.framework.Assert.assertTrue(e.getMessage().contains("Cannot register host with non compatible agent version"));
        }
        reg.setAgentVersion(null);
        try {
            handler.handleRegistration(reg);
            org.junit.Assert.fail("Expected failure for non compatible agent version");
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.agent.TestHeartbeatHandler.log.debug("Error:{}", e.getMessage());
            junit.framework.Assert.assertTrue(e.getMessage().contains("Cannot register host with non compatible agent version"));
        }
    }

    @org.junit.Test
    public void testRegistrationPublicHostname() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        clusters.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setHardwareProfile(hi);
        reg.setPublicHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1 + "-public");
        reg.setAgentVersion(metaInfo.getServerVersion());
        handler.handleRegistration(reg);
        org.junit.Assert.assertEquals(hostObject.getState(), org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES);
        org.junit.Assert.assertEquals(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType, hostObject.getOsType());
        org.junit.Assert.assertTrue(hostObject.getLastRegistrationTime() != 0);
        org.junit.Assert.assertEquals(hostObject.getLastHeartbeatTime(), hostObject.getLastRegistrationTime());
        org.apache.ambari.server.state.Host verifyHost = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(verifyHost.getPublicHostName(), reg.getPublicHostname());
    }

    @org.junit.Test
    public void testInvalidOSRegistration() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        clusters.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS("MegaOperatingSystem");
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        try {
            handler.handleRegistration(reg);
            org.junit.Assert.fail("Expected failure for non matching os type");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
    }

    @org.junit.Test
    public void testIncompatibleAgentRegistration() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        clusters.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion("0.0.0");
        try {
            handler.handleRegistration(reg);
            org.junit.Assert.fail("Expected failure for non compatible agent version");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
    }

    @org.junit.Test
    public void testRegisterNewNode() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.state.Clusters fsm = clusters;
        fsm.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS("redhat5");
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        reg.setPrefix(org.apache.ambari.server.configuration.Configuration.PREFIX_DIR);
        org.apache.ambari.server.agent.RegistrationResponse response = handler.handleRegistration(reg);
        org.junit.Assert.assertEquals(hostObject.getState(), org.apache.ambari.server.state.HostState.WAITING_FOR_HOST_STATUS_UPDATES);
        org.junit.Assert.assertEquals("redhat5", hostObject.getOsType());
        org.junit.Assert.assertEquals(0, response.getResponseId());
        org.junit.Assert.assertEquals(reg.getPrefix(), hostObject.getPrefix());
    }

    @org.junit.Test
    public void testRequestId() throws java.io.IOException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException, org.codehaus.jackson.JsonGenerationException, javax.xml.bind.JAXBException {
        org.apache.ambari.server.agent.HeartBeatHandler heartBeatHandler = injector.getInstance(org.apache.ambari.server.agent.HeartBeatHandler.class);
        org.apache.ambari.server.agent.Register register = new org.apache.ambari.server.agent.Register();
        register.setHostname("newHost");
        register.setTimestamp(new java.util.Date().getTime());
        register.setResponseId(123);
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS("redhat5");
        register.setHardwareProfile(hi);
        register.setAgentVersion(metaInfo.getServerVersion());
        org.apache.ambari.server.agent.RegistrationResponse registrationResponse = heartBeatHandler.handleRegistration(register);
        org.junit.Assert.assertEquals("ResponseId should start from zero", 0L, registrationResponse.getResponseId());
        org.apache.ambari.server.agent.HeartBeat heartBeat = constructHeartBeat("newHost", registrationResponse.getResponseId(), org.apache.ambari.server.agent.HostStatus.Status.HEALTHY);
        org.apache.ambari.server.agent.HeartBeatResponse hbResponse = heartBeatHandler.handleHeartBeat(heartBeat);
        org.junit.Assert.assertEquals("responseId was not incremented", 1L, hbResponse.getResponseId());
        org.junit.Assert.assertTrue("Not cached response returned", hbResponse == heartBeatHandler.handleHeartBeat(heartBeat));
        heartBeat.setResponseId(1L);
        hbResponse = heartBeatHandler.handleHeartBeat(heartBeat);
        org.junit.Assert.assertEquals("responseId was not incremented", 2L, hbResponse.getResponseId());
        org.junit.Assert.assertTrue("Agent is flagged for restart", hbResponse.isRestartAgent() == null);
        org.apache.ambari.server.agent.TestHeartbeatHandler.log.debug(org.apache.ambari.server.utils.StageUtils.jaxbToString(hbResponse));
        heartBeat.setResponseId(20L);
        hbResponse = heartBeatHandler.handleHeartBeat(heartBeat);
        org.junit.Assert.assertTrue("Agent is not flagged for restart", hbResponse.isRestartAgent());
        org.apache.ambari.server.agent.TestHeartbeatHandler.log.debug(org.apache.ambari.server.utils.StageUtils.jaxbToString(hbResponse));
    }

    private org.apache.ambari.server.agent.HeartBeat constructHeartBeat(java.lang.String hostName, long responseId, org.apache.ambari.server.agent.HostStatus.Status status) {
        org.apache.ambari.server.agent.HeartBeat heartBeat = new org.apache.ambari.server.agent.HeartBeat();
        heartBeat.setHostname(hostName);
        heartBeat.setTimestamp(new java.util.Date().getTime());
        heartBeat.setResponseId(responseId);
        org.apache.ambari.server.agent.HostStatus hs = new org.apache.ambari.server.agent.HostStatus();
        hs.setCause("");
        hs.setStatus(status);
        heartBeat.setNodeStatus(hs);
        heartBeat.setReports(java.util.Collections.emptyList());
        return heartBeat;
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testStateCommandsAtRegistration() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        java.util.List<org.apache.ambari.server.agent.StatusCommand> dummyCmds = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.StatusCommand statusCmd1 = new org.apache.ambari.server.agent.StatusCommand();
        statusCmd1.setClusterName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster);
        statusCmd1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        dummyCmds.add(statusCmd1);
        org.apache.ambari.server.agent.HeartbeatMonitor hm = Mockito.mock(org.apache.ambari.server.agent.HeartbeatMonitor.class);
        Mockito.when(hm.generateStatusCommands(Matchers.anyString())).thenReturn(dummyCmds);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        handler.setHeartbeatMonitor(hm);
        clusters.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        org.apache.ambari.server.agent.RegistrationResponse registrationResponse = handler.handleRegistration(reg);
        registrationResponse.getStatusCommands();
        org.junit.Assert.assertTrue(registrationResponse.getStatusCommands().size() == 1);
        org.junit.Assert.assertTrue(registrationResponse.getStatusCommands().get(0).equals(statusCmd1));
    }

    @org.junit.Test
    public void testTaskInProgressHandling() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setTaskId(1);
        cr.setClusterId(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId);
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr.setRoleCommand("INSTALL");
        cr.setStatus("IN_PROGRESS");
        cr.setStdErr("none");
        cr.setStdOut("dummy output");
        cr.setExitCode(777);
        reports.add(cr);
        hb.setReports(reports);
        hb.setComponentStatus(new java.util.ArrayList<>());
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, org.apache.ambari.server.RoleCommand.INSTALL);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        });
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        handler.handleHeartBeat(hb);
        handler.getHeartbeatProcessor().processHeartbeat(hb);
        org.apache.ambari.server.state.State componentState1 = serviceComponentHost1.getState();
        org.junit.Assert.assertEquals("Host state should still be installing", org.apache.ambari.server.state.State.INSTALLING, componentState1);
    }

    @org.junit.Test
    public void testOPFailedEventForAbortedTask() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(1, "/a/b", "cluster1", 1L, "action manager test", "commandParamsStage", "hostParamsStage");
        s.setStageId(1);
        s.addHostRoleExecutionCommand(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(org.apache.ambari.server.Role.DATANODE.toString(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, java.lang.System.currentTimeMillis(), "HDP-1.3.0"), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster, "HDFS", false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "clusterHostInfo", clusters);
        actionDBAccessor.persistActions(request);
        actionDBAccessor.abortHostRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, 1, 1, org.apache.ambari.server.Role.DATANODE.name());
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(1, 1));
        cr.setTaskId(1);
        cr.setClusterId(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId);
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr.setRoleCommand("INSTALL");
        cr.setStatus("FAILED");
        cr.setStdErr("none");
        cr.setStdOut("dummy output");
        cr.setExitCode(777);
        reports.add(cr);
        hb.setReports(reports);
        hb.setComponentStatus(new java.util.ArrayList<>());
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        });
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        handler.handleHeartBeat(hb);
        handler.getHeartbeatProcessor().processHeartbeat(hb);
        org.apache.ambari.server.state.State componentState1 = serviceComponentHost1.getState();
        org.junit.Assert.assertEquals("Host state should still be installing", org.apache.ambari.server.state.State.INSTALLING, componentState1);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testStatusHeartbeat() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost2 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost3 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INSTALLED);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.STARTED);
        serviceComponentHost3.setState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setReports(new java.util.ArrayList<>());
        hb.setAgentEnv(new org.apache.ambari.server.agent.AgentEnv());
        hb.setMounts(new java.util.ArrayList<>());
        java.util.ArrayList<org.apache.ambari.server.agent.ComponentStatus> componentStatuses = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.ComponentStatus componentStatus1 = createComponentStatus(new java.lang.Long(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId), org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE, "{\"stackName\":\"HDP\",\"stackVersion\":\"1.3.0\"}");
        org.apache.ambari.server.agent.ComponentStatus componentStatus2 = createComponentStatus(new java.lang.Long(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId), org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus, org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE, "");
        org.apache.ambari.server.agent.ComponentStatus componentStatus3 = createComponentStatus(new java.lang.Long(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId), org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT, "{\"stackName\":\"HDP\",\"stackVersion\":\"1.3.0\"}");
        componentStatuses.add(componentStatus1);
        componentStatuses.add(componentStatus2);
        componentStatuses.add(componentStatus3);
        hb.setComponentStatus(componentStatuses);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<>());
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        handler.handleHeartBeat(hb);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertTrue(hb.getAgentEnv().getHostHealth().getServerTimeStampAtReporting() >= hb.getTimestamp());
    }

    @org.junit.Test
    public void testRecoveryStatusReports() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setState(org.apache.ambari.server.state.State.STARTED);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setState(org.apache.ambari.server.state.State.STARTED);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
                add(command);
            }
        }).anyTimes();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOs);
        hi.setOSRelease(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOSRelease);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        handler.handleRegistration(reg);
        hostObject.setState(org.apache.ambari.server.state.HostState.UNHEALTHY);
        org.apache.ambari.server.agent.HeartBeat hb1 = new org.apache.ambari.server.agent.HeartBeat();
        hb1.setResponseId(0);
        hb1.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb1.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.agent.RecoveryReport rr = new org.apache.ambari.server.agent.RecoveryReport();
        rr.setSummary("RECOVERABLE");
        java.util.List<org.apache.ambari.server.agent.ComponentRecoveryReport> compRecReports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.ComponentRecoveryReport compRecReport = new org.apache.ambari.server.agent.ComponentRecoveryReport();
        compRecReport.setLimitReached(java.lang.Boolean.FALSE);
        compRecReport.setName("DATANODE");
        compRecReport.setNumAttempts(2);
        compRecReports.add(compRecReport);
        rr.setComponentReports(compRecReports);
        hb1.setRecoveryReport(rr);
        handler.handleHeartBeat(hb1);
        org.junit.Assert.assertEquals("RECOVERABLE", hostObject.getRecoveryReport().getSummary());
        org.junit.Assert.assertEquals(1, hostObject.getRecoveryReport().getComponentReports().size());
        org.junit.Assert.assertEquals(2, hostObject.getRecoveryReport().getComponentReports().get(0).getNumAttempts());
        org.apache.ambari.server.agent.HeartBeat hb2 = new org.apache.ambari.server.agent.HeartBeat();
        hb2.setResponseId(1);
        hb2.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb2.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        rr = new org.apache.ambari.server.agent.RecoveryReport();
        rr.setSummary("UNRECOVERABLE");
        compRecReports = new java.util.ArrayList<>();
        compRecReport = new org.apache.ambari.server.agent.ComponentRecoveryReport();
        compRecReport.setLimitReached(java.lang.Boolean.TRUE);
        compRecReport.setName("DATANODE");
        compRecReport.setNumAttempts(5);
        compRecReports.add(compRecReport);
        rr.setComponentReports(compRecReports);
        hb2.setRecoveryReport(rr);
        handler.handleHeartBeat(hb2);
        org.junit.Assert.assertEquals("UNRECOVERABLE", hostObject.getRecoveryReport().getSummary());
        org.junit.Assert.assertEquals(1, hostObject.getRecoveryReport().getComponentReports().size());
        org.junit.Assert.assertEquals(5, hostObject.getRecoveryReport().getComponentReports().get(0).getNumAttempts());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testProcessStatusReports() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = clusters;
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setState(org.apache.ambari.server.state.State.STARTED);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setState(org.apache.ambari.server.state.State.STARTED);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
                add(command);
            }
        }).anyTimes();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = createHeartBeatHandler();
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        org.apache.ambari.server.agent.Register reg = new org.apache.ambari.server.agent.Register();
        org.apache.ambari.server.agent.HostInfo hi = new org.apache.ambari.server.agent.HostInfo();
        hi.setHostName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hi.setOS(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOs);
        hi.setOSRelease(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOSRelease);
        reg.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        reg.setHardwareProfile(hi);
        reg.setAgentVersion(metaInfo.getServerVersion());
        handler.handleRegistration(reg);
        hostObject.setState(org.apache.ambari.server.state.HostState.UNHEALTHY);
        org.apache.ambari.server.agent.HeartBeat hb1 = new org.apache.ambari.server.agent.HeartBeat();
        hb1.setResponseId(0);
        hb1.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb1.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        java.util.List<org.apache.ambari.server.agent.ComponentStatus> componentStatus = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.ComponentStatus dataNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        dataNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        dataNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        dataNodeStatus.setStatus("STARTED");
        componentStatus.add(dataNodeStatus);
        org.apache.ambari.server.agent.ComponentStatus nameNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        nameNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        nameNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        nameNodeStatus.setStatus("STARTED");
        componentStatus.add(nameNodeStatus);
        hb1.setComponentStatus(componentStatus);
        handler.handleHeartBeat(hb1);
        heartbeatProcessor.processHeartbeat(hb1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY.name(), hostObject.getStatus());
        org.apache.ambari.server.agent.HeartBeat hb2 = new org.apache.ambari.server.agent.HeartBeat();
        hb2.setResponseId(1);
        hb2.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb2.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        componentStatus = new java.util.ArrayList<>();
        dataNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        dataNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        dataNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        dataNodeStatus.setStatus("INSTALLED");
        componentStatus.add(dataNodeStatus);
        nameNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        nameNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        nameNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        nameNodeStatus.setStatus("STARTED");
        componentStatus.add(nameNodeStatus);
        hb2.setComponentStatus(componentStatus);
        handler.handleHeartBeat(hb2);
        heartbeatProcessor.processHeartbeat(hb2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.ALERT.name(), hostObject.getStatus());
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.ON);
        org.apache.ambari.server.agent.HeartBeat hb2a = new org.apache.ambari.server.agent.HeartBeat();
        hb2a.setResponseId(2);
        hb2a.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb2a.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        componentStatus = new java.util.ArrayList<>();
        dataNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        dataNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        dataNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        dataNodeStatus.setStatus("INSTALLED");
        componentStatus.add(dataNodeStatus);
        nameNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        nameNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        nameNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        nameNodeStatus.setStatus("STARTED");
        componentStatus.add(nameNodeStatus);
        hb2a.setComponentStatus(componentStatus);
        handler.handleHeartBeat(hb2a);
        heartbeatProcessor.processHeartbeat(hb2a);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY.name(), hostObject.getStatus());
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setMaintenanceState(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.apache.ambari.server.agent.HeartBeat hb3 = new org.apache.ambari.server.agent.HeartBeat();
        hb3.setResponseId(3);
        hb3.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb3.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        componentStatus = new java.util.ArrayList<>();
        dataNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        dataNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        dataNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        dataNodeStatus.setStatus("INSTALLED");
        componentStatus.add(dataNodeStatus);
        nameNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        nameNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        nameNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        nameNodeStatus.setStatus("INSTALLED");
        componentStatus.add(nameNodeStatus);
        hb3.setComponentStatus(componentStatus);
        handler.handleHeartBeat(hb3);
        heartbeatProcessor.processHeartbeat(hb3);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNHEALTHY.name(), hostObject.getStatus());
        hb1.setResponseId(4);
        handler.handleHeartBeat(hb1);
        heartbeatProcessor.processHeartbeat(hb1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY.name(), hostObject.getStatus());
        EasyMock.reset(am);
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        }).anyTimes();
        EasyMock.replay(am);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.agent.HeartBeat hb4 = new org.apache.ambari.server.agent.HeartBeat();
        hb4.setResponseId(5);
        hb4.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb4.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        componentStatus = new java.util.ArrayList<>();
        dataNodeStatus = new org.apache.ambari.server.agent.ComponentStatus();
        dataNodeStatus.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        dataNodeStatus.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        dataNodeStatus.setStatus("STARTED");
        componentStatus.add(dataNodeStatus);
        hb4.setComponentStatus(componentStatus);
        handler.handleHeartBeat(hb4);
        heartbeatProcessor.processHeartbeat(hb4);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.UNHEALTHY.name(), hostObject.getStatus());
        hb1.setResponseId(6);
        handler.handleHeartBeat(hb1);
        heartbeatProcessor.processHeartbeat(hb1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.HEALTHY.name(), hostObject.getStatus());
        org.apache.ambari.server.agent.HeartBeat hb5 = new org.apache.ambari.server.agent.HeartBeat();
        hb5.setResponseId(7);
        hb5.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb5.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.agent.CommandReport cr1 = new org.apache.ambari.server.agent.CommandReport();
        cr1.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr1.setTaskId(1);
        cr1.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr1.setStatus("COMPLETED");
        cr1.setStdErr("");
        cr1.setStdOut("");
        cr1.setExitCode(215);
        cr1.setRoleCommand("STOP");
        java.util.ArrayList<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        reports.add(cr1);
        hb5.setReports(reports);
        handler.handleHeartBeat(hb5);
        heartbeatProcessor.processHeartbeat(hb5);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.HostHealthStatus.HealthStatus.ALERT.name(), hostObject.getStatus());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testIgnoreCustomActionReport() throws java.lang.Exception, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        org.apache.ambari.server.agent.CommandReport cr1 = new org.apache.ambari.server.agent.CommandReport();
        cr1.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr1.setTaskId(1);
        cr1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr1.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        cr1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString());
        cr1.setRoleCommand("CUSTOM_COMMAND");
        cr1.setStdErr("none");
        cr1.setStdOut("dummy output");
        cr1.setExitCode(0);
        org.apache.ambari.server.agent.CommandReport cr2 = new org.apache.ambari.server.agent.CommandReport();
        cr2.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr2.setTaskId(2);
        cr2.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr2.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        cr2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString());
        cr2.setRoleCommand("ACTIONEXECUTE");
        cr2.setStdErr("none");
        cr2.setStdOut("dummy output");
        cr2.setExitCode(0);
        java.util.ArrayList<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        reports.add(cr1);
        reports.add(cr2);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setReports(reports);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
                add(command);
            }
        });
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        try {
            handler.handleHeartBeat(hb);
            handler.getHeartbeatProcessor().processHeartbeat(hb);
        } catch (java.lang.Exception e) {
            org.junit.Assert.fail();
        }
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testComponents() throws java.lang.Exception {
        org.apache.ambari.server.agent.ComponentsResponse expected = new org.apache.ambari.server.agent.ComponentsResponse();
        org.apache.ambari.server.state.StackId dummyStackId = new org.apache.ambari.server.state.StackId(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyStackId);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> dummyComponents = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> dummyCategoryMap = new java.util.HashMap<>();
        dummyCategoryMap.put("NAMENODE", "MASTER");
        dummyComponents.put("HDFS", dummyCategoryMap);
        expected.setClusterName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster);
        expected.setStackName(dummyStackId.getStackName());
        expected.setStackVersion(dummyStackId.getStackVersion());
        expected.setComponents(dummyComponents);
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service service = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(service.getName()).andReturn("HDFS").atLeastOnce();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> componentMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponent nnComponent = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(nnComponent.getName()).andReturn("NAMENODE").atLeastOnce();
        EasyMock.expect(nnComponent.getDesiredStackId()).andReturn(dummyStackId).atLeastOnce();
        componentMap.put("NAMENODE", nnComponent);
        EasyMock.expect(service.getServiceComponents()).andReturn(componentMap).atLeastOnce();
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.replay(service, nnComponent, am);
        cluster.addService(service);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.junit.Assert.assertNotNull(handler);
        org.apache.ambari.server.agent.ComponentsResponse actual = handler.handleComponents(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster);
        if (org.apache.ambari.server.agent.TestHeartbeatHandler.log.isDebugEnabled()) {
            org.apache.ambari.server.agent.TestHeartbeatHandler.log.debug(actual.toString());
        }
        org.junit.Assert.assertEquals(expected.getClusterName(), actual.getClusterName());
        org.junit.Assert.assertEquals(expected.getComponents(), actual.getComponents());
    }

    private org.apache.ambari.server.agent.ComponentStatus createComponentStatus(java.lang.Long clusterId, java.lang.String serviceName, java.lang.String message, org.apache.ambari.server.state.State state, java.lang.String componentName, java.lang.String stackVersion) {
        org.apache.ambari.server.agent.ComponentStatus componentStatus1 = new org.apache.ambari.server.agent.ComponentStatus();
        componentStatus1.setClusterId(clusterId);
        componentStatus1.setServiceName(serviceName);
        componentStatus1.setMessage(message);
        componentStatus1.setStatus(state.name());
        componentStatus1.setComponentName(componentName);
        componentStatus1.setStackVersion(stackVersion);
        return componentStatus1;
    }

    @org.junit.Test
    public void testCommandStatusProcesses_empty() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1).setState(org.apache.ambari.server.state.State.STARTED);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setReports(new java.util.ArrayList<>());
        java.util.ArrayList<org.apache.ambari.server.agent.ComponentStatus> componentStatuses = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.ComponentStatus componentStatus1 = new org.apache.ambari.server.agent.ComponentStatus();
        componentStatus1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        componentStatus1.setMessage(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus);
        componentStatus1.setStatus(org.apache.ambari.server.state.State.STARTED.name());
        componentStatus1.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        componentStatuses.add(componentStatus1);
        hb.setComponentStatus(componentStatuses);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        });
        EasyMock.replay(am);
        org.apache.ambari.server.state.ServiceComponentHost sch = hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        junit.framework.Assert.assertEquals(java.lang.Integer.valueOf(0), java.lang.Integer.valueOf(sch.getProcesses().size()));
    }

    @org.junit.Test
    public void testInjectKeytabApplicableHost() throws java.lang.Exception {
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> kcp;
        java.util.Map<java.lang.String, java.lang.String> properties;
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        kcp = testInjectKeytabSetKeytab("c6403.ambari.apache.org");
        junit.framework.Assert.assertNotNull(kcp);
        junit.framework.Assert.assertEquals(1, kcp.size());
        properties = kcp.get(0);
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals("c6403.ambari.apache.org", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.HOSTNAME));
        junit.framework.Assert.assertEquals("dn/_HOST@_REALM", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.PRINCIPAL));
        junit.framework.Assert.assertEquals("/etc/security/keytabs/dn.service.keytab", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_PATH));
        junit.framework.Assert.assertEquals("hdfs", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_OWNER_NAME));
        junit.framework.Assert.assertEquals("r", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_OWNER_ACCESS));
        junit.framework.Assert.assertEquals("hadoop", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_GROUP_NAME));
        junit.framework.Assert.assertEquals("", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_GROUP_ACCESS));
        junit.framework.Assert.assertEquals(org.apache.commons.codec.binary.Base64.encodeBase64String("hello".getBytes()), kcp.get(0).get(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KEYTAB_CONTENT_BASE64));
        kcp = testInjectKeytabRemoveKeytab("c6403.ambari.apache.org");
        junit.framework.Assert.assertNotNull(kcp);
        junit.framework.Assert.assertEquals(1, kcp.size());
        properties = kcp.get(0);
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals("c6403.ambari.apache.org", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.HOSTNAME));
        junit.framework.Assert.assertEquals("dn/_HOST@_REALM", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.PRINCIPAL));
        junit.framework.Assert.assertEquals("/etc/security/keytabs/dn.service.keytab", properties.get(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_PATH));
        junit.framework.Assert.assertFalse(properties.containsKey(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_OWNER_NAME));
        junit.framework.Assert.assertFalse(properties.containsKey(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_OWNER_ACCESS));
        junit.framework.Assert.assertFalse(properties.containsKey(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_GROUP_NAME));
        junit.framework.Assert.assertFalse(properties.containsKey(org.apache.ambari.server.serveraction.kerberos.KerberosIdentityDataFileWriter.KEYTAB_FILE_GROUP_ACCESS));
        junit.framework.Assert.assertFalse(properties.containsKey(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.KEYTAB_CONTENT_BASE64));
    }

    @org.junit.Test
    public void testInjectKeytabNotApplicableHost() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> kcp;
        kcp = testInjectKeytabSetKeytab("c6401.ambari.apache.org");
        junit.framework.Assert.assertNotNull(kcp);
        junit.framework.Assert.assertTrue(kcp.isEmpty());
        kcp = testInjectKeytabRemoveKeytab("c6401.ambari.apache.org");
        junit.framework.Assert.assertNotNull(kcp);
        junit.framework.Assert.assertTrue(kcp.isEmpty());
    }

    private java.util.List<java.util.Map<java.lang.String, java.lang.String>> testInjectKeytabSetKeytab(java.lang.String targetHost) throws java.lang.Exception {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> hlp = new java.util.HashMap<>();
        hlp.put("custom_command", org.apache.ambari.server.controller.KerberosHelperImpl.SET_KEYTAB);
        executionCommand.setHostLevelParams(hlp);
        java.util.Map<java.lang.String, java.lang.String> commandparams = new java.util.HashMap<>();
        commandparams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME, "admin");
        executionCommand.setCommandParams(commandparams);
        executionCommand.setClusterName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        });
        EasyMock.replay(am);
        java.lang.reflect.Method injectKeytabMethod = agentCommandsPublisher.getClass().getDeclaredMethod("injectKeytab", org.apache.ambari.server.agent.ExecutionCommand.class, java.lang.String.class, java.lang.String.class, java.util.Map.class);
        injectKeytabMethod.setAccessible(true);
        commandparams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, createTestKeytabData(agentCommandsPublisher, false).getAbsolutePath());
        injectKeytabMethod.invoke(agentCommandsPublisher, executionCommand, "SET_KEYTAB", targetHost, null);
        return executionCommand.getKerberosCommandParams();
    }

    private java.util.List<java.util.Map<java.lang.String, java.lang.String>> testInjectKeytabRemoveKeytab(java.lang.String targetHost) throws java.lang.Exception {
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        java.util.Map<java.lang.String, java.lang.String> hlp = new java.util.HashMap<>();
        hlp.put("custom_command", "REMOVE_KEYTAB");
        executionCommand.setHostLevelParams(hlp);
        java.util.Map<java.lang.String, java.lang.String> commandparams = new java.util.HashMap<>();
        commandparams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.AUTHENTICATED_USER_NAME, "admin");
        executionCommand.setCommandParams(commandparams);
        executionCommand.setClusterName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        });
        EasyMock.replay(am);
        java.lang.reflect.Method injectKeytabMethod = agentCommandsPublisher.getClass().getDeclaredMethod("injectKeytab", org.apache.ambari.server.agent.ExecutionCommand.class, java.lang.String.class, java.lang.String.class, java.util.Map.class);
        injectKeytabMethod.setAccessible(true);
        commandparams.put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DATA_DIRECTORY, createTestKeytabData(agentCommandsPublisher, true).getAbsolutePath());
        injectKeytabMethod.invoke(agentCommandsPublisher, executionCommand, "REMOVE_KEYTAB", targetHost, null);
        return executionCommand.getKerberosCommandParams();
    }

    private java.io.File createTestKeytabData(org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher, boolean removeKeytabs) throws java.lang.Exception {
        org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController kerberosKeytabControllerMock = EasyMock.createMock(org.apache.ambari.server.serveraction.kerberos.stageutils.KerberosKeytabController.class);
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> filter;
        if (removeKeytabs) {
            filter = null;
            com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceMapping = com.google.common.collect.ArrayListMultimap.create();
            serviceMapping.put("HDFS", "DATANODE");
            org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal resolvedKerberosPrincipal = EasyMock.createMock(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal.class);
            EasyMock.expect(resolvedKerberosPrincipal.getHostName()).andReturn("c6403.ambari.apache.org");
            EasyMock.expect(resolvedKerberosPrincipal.getPrincipal()).andReturn("dn/_HOST@_REALM");
            EasyMock.expect(resolvedKerberosPrincipal.getServiceMapping()).andReturn(serviceMapping);
            EasyMock.replay(resolvedKerberosPrincipal);
            org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab resolvedKerberosKeytab = EasyMock.createMock(org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab.class);
            EasyMock.expect(resolvedKerberosKeytab.getPrincipals()).andReturn(java.util.Collections.singleton(resolvedKerberosPrincipal));
            EasyMock.replay(resolvedKerberosKeytab);
            EasyMock.expect(kerberosKeytabControllerMock.getKeytabByFile("/etc/security/keytabs/dn.service.keytab")).andReturn(resolvedKerberosKeytab).once();
        } else {
            filter = java.util.Collections.singletonMap("HDFS", java.util.Collections.singletonList("*"));
        }
        EasyMock.expect(kerberosKeytabControllerMock.adjustServiceComponentFilter(EasyMock.anyObject(), EasyMock.eq(false), EasyMock.anyObject())).andReturn(filter).once();
        EasyMock.expect(kerberosKeytabControllerMock.getFilteredKeytabs(((java.util.Collection<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor>) (org.easymock.EasyMock.anyObject())), org.easymock.EasyMock.eq(null), org.easymock.EasyMock.eq(null))).andReturn(com.google.common.collect.Sets.newHashSet(new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosKeytab("/etc/security/keytabs/dn.service.keytab", "hdfs", "r", "hadoop", "", com.google.common.collect.Sets.newHashSet(new org.apache.ambari.server.serveraction.kerberos.stageutils.ResolvedKerberosPrincipal(1L, "c6403.ambari.apache.org", "dn/_HOST@_REALM", false, "/tmp", "HDFS", "DATANODE", "/etc/security/keytabs/dn.service.keytab")), false, false))).once();
        EasyMock.expect(kerberosKeytabControllerMock.getServiceIdentities(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyObject(), org.easymock.EasyMock.anyObject())).andReturn(java.util.Collections.emptySet()).anyTimes();
        EasyMock.replay(kerberosKeytabControllerMock);
        java.lang.reflect.Field controllerField = agentCommandsPublisher.getClass().getDeclaredField("kerberosKeytabController");
        controllerField.setAccessible(true);
        controllerField.set(agentCommandsPublisher, kerberosKeytabControllerMock);
        java.io.File dataDirectory = temporaryFolder.newFolder();
        java.io.File hostDirectory = new java.io.File(dataDirectory, "c6403.ambari.apache.org");
        java.io.File keytabFile;
        if (hostDirectory.mkdirs()) {
            keytabFile = new java.io.File(hostDirectory, org.apache.commons.codec.digest.DigestUtils.sha256Hex("/etc/security/keytabs/dn.service.keytab"));
            java.io.FileWriter fw = new java.io.FileWriter(keytabFile);
            java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
            bw.write("hello");
            bw.close();
        } else {
            throw new java.lang.Exception("Failed to create " + hostDirectory.getAbsolutePath());
        }
        return dataDirectory;
    }

    private org.apache.ambari.server.state.Service addService(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(cluster);
        return cluster.addService(serviceName, repositoryVersion);
    }
}