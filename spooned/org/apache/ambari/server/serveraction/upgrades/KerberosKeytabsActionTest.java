package org.apache.ambari.server.serveraction.upgrades;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
public class KerberosKeytabsActionTest {
    private com.google.inject.Injector m_injector;

    private org.apache.ambari.server.state.Clusters m_clusters;

    private org.apache.ambari.server.controller.KerberosHelper m_kerberosHelper;

    private org.apache.ambari.server.state.Config m_kerberosConfig;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_clusters = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        com.google.inject.persist.UnitOfWork unitOfWork = org.easymock.EasyMock.createMock(com.google.inject.persist.UnitOfWork.class);
        java.util.Map<java.lang.String, java.lang.String> mockProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("kerberos-env", "");
            }
        };
        m_kerberosConfig = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(m_kerberosConfig.getType()).andReturn("kerberos-env").anyTimes();
        EasyMock.expect(m_kerberosConfig.getProperties()).andReturn(mockProperties).anyTimes();
        org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getDesiredConfigByType("kerberos-env")).andReturn(m_kerberosConfig).atLeastOnce();
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.KERBEROS).anyTimes();
        EasyMock.expect(m_clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).anyTimes();
        EasyMock.replay(m_clusters, cluster, m_kerberosConfig);
        m_injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addClustersBinding().addLdapBindings().build().configure(binder());
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(m_clusters);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                bind(org.apache.ambari.server.orm.dao.StackDAO.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.StackDAO.class));
                bind(javax.persistence.EntityManager.class).toInstance(org.easymock.EasyMock.createNiceMock(javax.persistence.EntityManager.class));
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
            }
        });
        m_kerberosHelper = m_injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
    }

    @org.junit.Test
    public void testAction_NotKerberized() throws java.lang.Exception {
        EasyMock.reset(m_kerberosHelper);
        EasyMock.expect(m_kerberosHelper.isClusterKerberosEnabled(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class))).andReturn(java.lang.Boolean.FALSE).atLeastOnce();
        EasyMock.replay(m_kerberosHelper);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName("c1");
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = org.easymock.EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        EasyMock.expect(hrc.getRequestId()).andReturn(1L).anyTimes();
        EasyMock.expect(hrc.getStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(hrc.getExecutionCommandWrapper()).andReturn(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand)).anyTimes();
        EasyMock.replay(hrc);
        org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction action = m_injector.getInstance(org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction.class);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hrc);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.contains(report.getStdOut(), "Cluster c1 is not secured by Kerberos"));
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.contains(report.getStdOut(), "No action required."));
    }

    @org.junit.Test
    public void testAction_NoKdcType() throws java.lang.Exception {
        EasyMock.reset(m_kerberosHelper);
        EasyMock.expect(m_kerberosHelper.isClusterKerberosEnabled(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class))).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        EasyMock.replay(m_kerberosHelper);
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName("c1");
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = org.easymock.EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        EasyMock.expect(hrc.getRequestId()).andReturn(1L).anyTimes();
        EasyMock.expect(hrc.getStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(hrc.getExecutionCommandWrapper()).andReturn(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand)).anyTimes();
        EasyMock.replay(hrc);
        org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction action = m_injector.getInstance(org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction.class);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hrc);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name(), report.getStatus());
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.contains(report.getStdOut(), "KDC Type is NONE"));
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.contains(report.getStdOut(), "No action required."));
    }

    @org.junit.Test
    public void testAction_Kerberized() throws java.lang.Exception {
        EasyMock.reset(m_kerberosHelper);
        EasyMock.expect(m_kerberosHelper.isClusterKerberosEnabled(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class))).andReturn(java.lang.Boolean.TRUE).atLeastOnce();
        EasyMock.replay(m_kerberosHelper);
        m_kerberosConfig.getProperties().put("kdc_type", "mit-kdc");
        java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();
        commandParams.put("clusterName", "c1");
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setCommandParams(commandParams);
        executionCommand.setClusterName("c1");
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc = org.easymock.EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        EasyMock.expect(hrc.getRequestId()).andReturn(1L).anyTimes();
        EasyMock.expect(hrc.getStageId()).andReturn(2L).anyTimes();
        EasyMock.expect(hrc.getExecutionCommandWrapper()).andReturn(new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand)).anyTimes();
        EasyMock.replay(hrc);
        org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction action = m_injector.getInstance(org.apache.ambari.server.serveraction.upgrades.KerberosKeytabsAction.class);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hrc);
        org.apache.ambari.server.agent.CommandReport report = action.execute(null);
        org.junit.Assert.assertNotNull(report);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING.name(), report.getStatus());
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.contains(report.getStdOut(), "Checking KDC type... MIT_KDC"));
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.contains(report.getStdOut(), "Regenerate keytabs after upgrade is complete."));
    }
}