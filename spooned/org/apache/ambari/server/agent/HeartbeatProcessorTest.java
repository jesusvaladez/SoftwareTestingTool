package org.apache.ambari.server.agent;
import org.easymock.EasyMock;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOSRelease;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOs;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyStackId;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE_MASTER;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE;
import static org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class HeartbeatProcessorTest {
    private com.google.inject.Injector injector;

    private long requestId = 23;

    private long stageId = 31;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration config;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessor;

    @com.google.inject.Inject
    private org.apache.ambari.server.agent.HeartbeatTestHelper heartbeatTestHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ActionManagerTestHelper actionManagerTestHelper;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.OrmTestHelper helper;

    public HeartbeatProcessorTest() {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = org.apache.ambari.server.agent.HeartbeatTestHelper.getTestModule();
        injector = com.google.inject.Guice.createInjector(module);
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.injectMembers(this);
        org.easymock.EasyMock.replay(injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class));
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testHeartbeatWithConfigs() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost2 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INSTALLED);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setResponseId(0);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setTaskId(1);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr.setStatus("COMPLETED");
        cr.setStdErr("");
        cr.setStdOut("");
        cr.setExitCode(215);
        cr.setRoleCommand("START");
        reports.add(cr);
        hb.setReports(reports);
        org.apache.ambari.server.orm.entities.HostEntity host1 = hostDAO.findByName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertNotNull(host1);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        });
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertNotNull(serviceComponentHost1.getActualConfigs());
        org.junit.Assert.assertEquals(serviceComponentHost1.getActualConfigs().size(), 1);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testRestartRequiredAfterInstallClient() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost.setState(org.apache.ambari.server.state.State.INSTALLED);
        serviceComponentHost.setRestartRequired(true);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setResponseId(0);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setRoleCommand("INSTALL");
        cr.setCustomCommand("EXECUTION_COMMAND");
        cr.setTaskId(1);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS_CLIENT);
        cr.setStatus("COMPLETED");
        cr.setStdErr("");
        cr.setStdOut("");
        cr.setExitCode(215);
        reports.add(cr);
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
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertNotNull(serviceComponentHost.getActualConfigs());
        org.junit.Assert.assertFalse(serviceComponentHost.isRestartRequired());
        org.junit.Assert.assertEquals(serviceComponentHost.getActualConfigs().size(), 1);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testHeartbeatCustomCommandWithConfigs() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost2 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INSTALLED);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setResponseId(0);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setRoleCommand("CUSTOM_COMMAND");
        cr.setCustomCommand("RESTART");
        cr.setTaskId(1);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr.setStatus("COMPLETED");
        cr.setStdErr("");
        cr.setStdOut("");
        cr.setExitCode(215);
        org.apache.ambari.server.agent.CommandReport crn = new org.apache.ambari.server.agent.CommandReport();
        crn.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        crn.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        crn.setRoleCommand("CUSTOM_COMMAND");
        crn.setCustomCommand("START");
        crn.setTaskId(1);
        crn.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        crn.setStatus("COMPLETED");
        crn.setStdErr("");
        crn.setStdOut("");
        crn.setExitCode(215);
        reports.add(cr);
        reports.add(crn);
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
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertNotNull(serviceComponentHost1.getActualConfigs());
        org.junit.Assert.assertEquals(serviceComponentHost1.getActualConfigs().size(), 1);
        org.junit.Assert.assertNotNull(serviceComponentHost2.getActualConfigs());
        org.junit.Assert.assertEquals(serviceComponentHost2.getActualConfigs().size(), 1);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testHeartbeatCustomStartStop() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost2 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INSTALLED);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.STARTED);
        serviceComponentHost1.setRestartRequired(true);
        serviceComponentHost2.setRestartRequired(true);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setResponseId(0);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setRoleCommand("CUSTOM_COMMAND");
        cr.setCustomCommand("START");
        cr.setTaskId(1);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr.setStatus("COMPLETED");
        cr.setStdErr("");
        cr.setStdOut("");
        cr.setExitCode(215);
        org.apache.ambari.server.agent.CommandReport crn = new org.apache.ambari.server.agent.CommandReport();
        crn.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        crn.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        crn.setRoleCommand("CUSTOM_COMMAND");
        crn.setCustomCommand("STOP");
        crn.setTaskId(1);
        crn.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        crn.setStatus("COMPLETED");
        crn.setStdErr("");
        crn.setStdOut("");
        crn.setExitCode(215);
        reports.add(cr);
        reports.add(crn);
        hb.setReports(reports);
        org.junit.Assert.assertTrue(serviceComponentHost1.isRestartRequired());
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
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, componentState1);
        org.junit.Assert.assertFalse(serviceComponentHost1.isRestartRequired());
        org.apache.ambari.server.state.State componentState2 = serviceComponentHost2.getState();
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, componentState2);
        org.junit.Assert.assertTrue(serviceComponentHost2.isRestartRequired());
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
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost2 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost3 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INSTALLED);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.INSTALLED);
        serviceComponentHost3.setState(org.apache.ambari.server.state.State.STARTING);
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
        componentStatus2.setStatus(org.apache.ambari.server.state.State.STARTED.name());
        componentStatus2.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.SECONDARY_NAMENODE);
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
        org.apache.ambari.server.state.State componentState3 = serviceComponentHost3.getState();
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTED, componentState1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.INSTALLED, componentState2);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTING, componentState3);
    }

    @org.junit.Test
    public void testCommandReport() throws org.apache.ambari.server.AmbariException {
        injector.injectMembers(this);
        clusters.addHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.StackId dummyStackId = new org.apache.ambari.server.state.StackId(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyStackId);
        clusters.addCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster, dummyStackId);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        heartbeatTestHelper.populateActionDB(db, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, requestId, stageId);
        org.apache.ambari.server.actionmanager.Stage stage = db.getAllStages(requestId).get(0);
        org.junit.Assert.assertEquals(stageId, stage.getStageId());
        stage.setHostRoleStatus(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE_MASTER, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        db.hostRoleScheduled(stage, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE_MASTER);
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setTaskId(1);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE_MASTER);
        cr.setStatus("COMPLETED");
        cr.setStdErr("");
        cr.setStdOut("");
        cr.setExitCode(215);
        reports.add(cr);
        am.processTaskResponse(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        org.junit.Assert.assertEquals(215, am.getAction(requestId, stageId).getExitCode(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE_MASTER));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, am.getAction(requestId, stageId).getHostRoleStatus(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE_MASTER));
        org.apache.ambari.server.actionmanager.Stage s = db.getAllStages(requestId).get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, s.getHostRoleStatus(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE_MASTER));
        org.junit.Assert.assertEquals(215, s.getExitCode(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.agent.DummyHeartbeatConstants.HBASE_MASTER));
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testCommandReportOnHeartbeatUpdatedState() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setTaskId(1);
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        cr.setStdErr("none");
        cr.setStdOut("dummy output");
        cr.setExitCode(777);
        cr.setRoleCommand("START");
        reports.add(cr);
        hb.setReports(reports);
        hb.setComponentStatus(new java.util.ArrayList<>());
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            {
                add(command);
            }
        }).anyTimes();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should  be " + org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, serviceComponentHost1.getState());
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(1);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        cr.setExitCode(0);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STARTED, serviceComponentHost1.getState());
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(2);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        cr.setRoleCommand("STOP");
        cr.setExitCode(777);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STARTED, serviceComponentHost1.getState());
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(3);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        cr.setExitCode(0);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, serviceComponentHost1.getState());
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.STARTING);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        cr.setExitCode(777);
        cr.setRoleCommand("START");
        hb.setResponseId(4);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.STARTING, org.apache.ambari.server.state.State.STARTING, serviceComponentHost1.getState());
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        cr.setExitCode(0);
        hb.setResponseId(5);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.STARTED, org.apache.ambari.server.state.State.STARTED, serviceComponentHost1.getState());
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.STOPPING);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        cr.setExitCode(777);
        cr.setRoleCommand("STOP");
        hb.setResponseId(6);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.STOPPING, org.apache.ambari.server.state.State.STOPPING, serviceComponentHost1.getState());
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        cr.setExitCode(0);
        hb.setResponseId(7);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, serviceComponentHost1.getState());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testUpgradeSpecificHandling() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.UPGRADING);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setTaskId(1);
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr.setRoleCommand("INSTALL");
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
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
        }).anyTimes();
        EasyMock.replay(am);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should  be " + org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, serviceComponentHost1.getState());
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(1);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        cr.setExitCode(0);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.INSTALLED, serviceComponentHost1.getState());
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.UPGRADING);
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(2);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString());
        cr.setExitCode(3);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, serviceComponentHost1.getState());
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.UPGRADING);
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(3);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING.toString());
        cr.setExitCode(55);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, serviceComponentHost1.getState());
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.UPGRADING);
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(4);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED.toString());
        cr.setExitCode(55);
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("Host state should be " + org.apache.ambari.server.state.State.UPGRADING, org.apache.ambari.server.state.State.UPGRADING, serviceComponentHost1.getState());
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testCommandStatusProcesses() throws java.lang.Exception {
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
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> procs = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> proc1info = new java.util.HashMap<>();
        proc1info.put("name", "a");
        proc1info.put("status", "RUNNING");
        procs.add(proc1info);
        java.util.Map<java.lang.String, java.lang.String> proc2info = new java.util.HashMap<>();
        proc2info.put("name", "b");
        proc2info.put("status", "NOT_RUNNING");
        procs.add(proc2info);
        java.util.Map<java.lang.String, java.lang.Object> extra = new java.util.HashMap<>();
        extra.put("processes", procs);
        java.util.ArrayList<org.apache.ambari.server.agent.ComponentStatus> componentStatuses = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.ComponentStatus componentStatus1 = new org.apache.ambari.server.agent.ComponentStatus();
        componentStatus1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        componentStatus1.setMessage(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus);
        componentStatus1.setStatus(org.apache.ambari.server.state.State.STARTED.name());
        componentStatus1.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        componentStatus1.setExtra(extra);
        componentStatuses.add(componentStatus1);
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
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        heartbeatProcessor.processHeartbeat(hb);
        org.apache.ambari.server.state.ServiceComponentHost sch = hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(2), java.lang.Integer.valueOf(sch.getProcesses().size()));
        hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(1);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setReports(new java.util.ArrayList<>());
        componentStatus1 = new org.apache.ambari.server.agent.ComponentStatus();
        componentStatus1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        componentStatus1.setMessage(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus);
        componentStatus1.setStatus(org.apache.ambari.server.state.State.STARTED.name());
        componentStatus1.setComponentName(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hb.setComponentStatus(java.util.Collections.singletonList(componentStatus1));
        heartbeatProcessor.processHeartbeat(hb);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testComponentUpgradeFailReport() throws java.lang.Exception {
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
        org.apache.ambari.server.state.StackId stack130 = new org.apache.ambari.server.state.StackId("HDP-1.3.0");
        org.apache.ambari.server.state.StackId stack120 = new org.apache.ambari.server.state.StackId("HDP-1.2.0");
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.UPGRADING);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", "cluster1", 1L, "action manager test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        s.addHostRoleExecutionCommand(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUpgradeEvent(org.apache.ambari.server.Role.DATANODE.toString(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, java.lang.System.currentTimeMillis(), "HDP-1.3.0"), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster, "HDFS", false, false);
        s.addHostRoleExecutionCommand(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(org.apache.ambari.server.Role.NAMENODE.toString(), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, java.lang.System.currentTimeMillis(), "HDP-1.3.0"), org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster, "HDFS", false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "clusterHostInfo", clusters);
        actionDBAccessor.persistActions(request);
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setTaskId(1);
        cr.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        cr.setStdErr("none");
        cr.setStdOut("dummy output");
        actionDBAccessor.updateHostRoleState(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, requestId, stageId, org.apache.ambari.server.Role.DATANODE.name(), cr);
        cr.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        cr.setTaskId(2);
        actionDBAccessor.updateHostRoleState(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, requestId, stageId, org.apache.ambari.server.Role.NAMENODE.name(), cr);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        org.apache.ambari.server.agent.CommandReport cr1 = new org.apache.ambari.server.agent.CommandReport();
        cr1.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr1.setTaskId(1);
        cr1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr1.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr1.setRoleCommand("INSTALL");
        cr1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString());
        cr1.setStdErr("none");
        cr1.setStdOut("dummy output");
        cr1.setExitCode(0);
        org.apache.ambari.server.agent.CommandReport cr2 = new org.apache.ambari.server.agent.CommandReport();
        cr2.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr2.setTaskId(2);
        cr2.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr2.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        cr2.setRoleCommand("INSTALL");
        cr2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.toString());
        cr2.setStdErr("none");
        cr2.setStdOut("dummy output");
        cr2.setExitCode(0);
        java.util.ArrayList<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        reports.add(cr1);
        reports.add(cr2);
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
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        heartbeatProcessor.processHeartbeat(hb);
        org.junit.Assert.assertEquals("State of SCH should change after fail report", org.apache.ambari.server.state.State.UPGRADING, serviceComponentHost1.getState());
        org.junit.Assert.assertEquals("State of SCH should change after fail report", org.apache.ambari.server.state.State.INSTALL_FAILED, serviceComponentHost2.getState());
        org.junit.Assert.assertEquals("Stack version of SCH should not change after fail report", org.apache.ambari.server.state.State.INSTALL_FAILED, serviceComponentHost2.getState());
    }

    @org.junit.Test
    public void testComponentUpgradeInProgressReport() throws java.lang.Exception {
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
        org.apache.ambari.server.state.StackId stack130 = new org.apache.ambari.server.state.StackId("HDP-1.3.0");
        org.apache.ambari.server.state.StackId stack120 = new org.apache.ambari.server.state.StackId("HDP-1.2.0");
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.UPGRADING);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.INSTALLING);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        hb.setTimestamp(java.lang.System.currentTimeMillis());
        hb.setResponseId(0);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        org.apache.ambari.server.agent.CommandReport cr1 = new org.apache.ambari.server.agent.CommandReport();
        cr1.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr1.setTaskId(1);
        cr1.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr1.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        cr1.setRoleCommand("INSTALL");
        cr1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        cr1.setStdErr("none");
        cr1.setStdOut("dummy output");
        cr1.setExitCode(777);
        org.apache.ambari.server.agent.CommandReport cr2 = new org.apache.ambari.server.agent.CommandReport();
        cr2.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr2.setTaskId(2);
        cr2.setServiceName(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        cr2.setRole(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        cr2.setRoleCommand("INSTALL");
        cr2.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        cr2.setStdErr("none");
        cr2.setStdOut("dummy output");
        cr2.setExitCode(777);
        java.util.ArrayList<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        reports.add(cr1);
        reports.add(cr2);
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
        handler.handleHeartBeat(hb);
        org.junit.Assert.assertEquals("State of SCH not change while operation is in progress", org.apache.ambari.server.state.State.UPGRADING, serviceComponentHost1.getState());
        org.junit.Assert.assertEquals("State of SCH not change while operation is  in progress", org.apache.ambari.server.state.State.INSTALLING, serviceComponentHost2.getState());
    }

    @org.junit.Test
    public void testHeartBeatWithAlertAndInvalidCluster() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(new java.util.ArrayList<>());
        EasyMock.replay(am);
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Clusters fsm = clusters;
        org.apache.ambari.server.state.Host hostObject = clusters.getHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hostObject.setIPv4("ipv4");
        hostObject.setIPv6("ipv6");
        hostObject.setOsType(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyOsType);
        org.apache.ambari.server.agent.HeartBeatHandler handler = new org.apache.ambari.server.agent.HeartBeatHandler(config, fsm, am, org.apache.ambari.server.security.encryption.Encryptor.NONE, injector);
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
        org.apache.ambari.server.agent.ExecutionCommand execCmd = new org.apache.ambari.server.agent.ExecutionCommand();
        execCmd.setRequestAndStage(2, 34);
        execCmd.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        org.apache.ambari.server.agent.HostStatus hs = new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus);
        hb.setResponseId(0);
        hb.setNodeStatus(hs);
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.Alert alert = new org.apache.ambari.server.state.Alert("foo", "bar", "baz", "foobar", "foobarbaz", org.apache.ambari.server.state.AlertState.OK);
        alert.setClusterId(-1L);
        java.util.List<org.apache.ambari.server.state.Alert> alerts = java.util.Collections.singletonList(alert);
        hb.setAlerts(alerts);
        handler.getHeartbeatProcessor().processHeartbeat(hb);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testInstallPackagesWithVersion() throws java.lang.Exception {
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(java.util.Collections.singletonList(command)).anyTimes();
        EasyMock.replay(am);
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        com.google.gson.JsonObject json = new com.google.gson.JsonObject();
        json.addProperty("actual_version", "2.2.1.0-2222");
        json.addProperty("package_installation_result", "SUCCESS");
        json.addProperty("installed_repository_version", "0.1-1234");
        json.addProperty("stack_id", cluster.getDesiredStackVersion().getStackId());
        org.apache.ambari.server.agent.CommandReport cmdReport = new org.apache.ambari.server.agent.CommandReport();
        cmdReport.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cmdReport.setTaskId(1);
        cmdReport.setCustomCommand("install_packages");
        cmdReport.setStructuredOut(json.toString());
        cmdReport.setRoleCommand(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE.name());
        cmdReport.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name());
        cmdReport.setRole("install_packages");
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        reports.add(cmdReport);
        hb.setReports(reports);
        hb.setTimestamp(0L);
        hb.setResponseId(0);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setComponentStatus(new java.util.ArrayList<>());
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "0.1");
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = helper.getOrCreateRepositoryVersion(cluster);
        org.junit.Assert.assertNotNull(entity);
        heartbeatProcessor.processHeartbeat(hb);
        entity = dao.findByStackAndVersion(stackId, "2.2.1.0-2222");
        org.junit.Assert.assertNull(entity);
        entity = dao.findByStackAndVersion(stackId, "0.1.1");
        org.junit.Assert.assertNotNull(entity);
    }

    @org.junit.Test
    public void testInstallPackagesWithId() throws java.lang.Exception {
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(injector);
        final org.apache.ambari.server.actionmanager.HostRoleCommand command = hostRoleCommandFactory.create(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1, org.apache.ambari.server.Role.DATANODE, null, null);
        org.apache.ambari.server.actionmanager.ActionManager am = actionManagerTestHelper.getMockActionManager();
        EasyMock.expect(am.getTasks(org.easymock.EasyMock.<java.util.List<java.lang.Long>>anyObject())).andReturn(java.util.Collections.singletonList(command)).anyTimes();
        EasyMock.replay(am);
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = helper.getOrCreateRepositoryVersion(cluster);
        org.junit.Assert.assertNotNull(entity);
        org.apache.ambari.server.agent.HeartBeatHandler handler = heartbeatTestHelper.getHeartBeatHandler(am);
        org.apache.ambari.server.agent.HeartbeatProcessor heartbeatProcessor = handler.getHeartbeatProcessor();
        org.apache.ambari.server.agent.HeartBeat hb = new org.apache.ambari.server.agent.HeartBeat();
        com.google.gson.JsonObject json = new com.google.gson.JsonObject();
        json.addProperty("actual_version", "2.2.1.0-2222");
        json.addProperty("package_installation_result", "SUCCESS");
        json.addProperty("repository_version_id", entity.getId());
        org.apache.ambari.server.agent.CommandReport cmdReport = new org.apache.ambari.server.agent.CommandReport();
        cmdReport.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cmdReport.setTaskId(1);
        cmdReport.setCustomCommand("install_packages");
        cmdReport.setStructuredOut(json.toString());
        cmdReport.setRoleCommand(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE.name());
        cmdReport.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name());
        cmdReport.setRole("install_packages");
        cmdReport.setClusterId(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyClusterId);
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        reports.add(cmdReport);
        hb.setReports(reports);
        hb.setTimestamp(0L);
        hb.setResponseId(0);
        hb.setNodeStatus(new org.apache.ambari.server.agent.HostStatus(org.apache.ambari.server.agent.HostStatus.Status.HEALTHY, org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostStatus));
        hb.setHostname(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hb.setComponentStatus(new java.util.ArrayList<>());
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "0.1");
        heartbeatProcessor.processHeartbeat(hb);
        entity = dao.findByStackAndVersion(stackId, "2.2.1.0-2222");
        org.junit.Assert.assertNotNull(entity);
        entity = dao.findByStackAndVersion(stackId, "0.1.1");
        org.junit.Assert.assertNull(entity);
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testComponentInProgressStatusSafeAfterStatusReport() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = heartbeatTestHelper.getDummyCluster();
        org.apache.ambari.server.state.Service hdfs = addService(cluster, org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        hdfs.addServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE);
        hdfs.getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).addServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost1 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.DATANODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost2 = clusters.getCluster(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyCluster).getService(org.apache.ambari.server.agent.DummyHeartbeatConstants.HDFS).getServiceComponent(org.apache.ambari.server.agent.DummyHeartbeatConstants.NAMENODE).getServiceComponentHost(org.apache.ambari.server.agent.DummyHeartbeatConstants.DummyHostname1);
        serviceComponentHost1.setState(org.apache.ambari.server.state.State.STARTING);
        serviceComponentHost2.setState(org.apache.ambari.server.state.State.STOPPING);
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
        componentStatus1.setStatus(org.apache.ambari.server.state.State.INSTALLED.name());
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
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STARTING, componentState1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.STOPPING, componentState2);
    }

    private org.apache.ambari.server.state.Service addService(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = helper.getOrCreateRepositoryVersion(cluster);
        return cluster.addService(serviceName, repositoryVersion);
    }
}