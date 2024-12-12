package org.apache.ambari.server.serveraction.upgrades;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.class)
public class UpgradeUserKerberosDescriptorTest {
    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster cluster;

    private org.apache.ambari.server.orm.entities.UpgradeEntity upgrade;

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory;

    private org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO;

    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory upgradeContextFactory;

    private java.util.TreeMap<java.lang.String, java.lang.reflect.Field> fields = new java.util.TreeMap<>();

    private org.apache.ambari.server.state.StackId HDP_24 = new org.apache.ambari.server.state.StackId("HDP", "2.4");

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        clusters = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        cluster = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        upgrade = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        kerberosDescriptorFactory = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory.class);
        ambariMetaInfo = org.easymock.EasyMock.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        artifactDAO = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.ArtifactDAO.class);
        upgradeContextFactory = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class);
        upgradeContext = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).atLeastOnce();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(HDP_24).atLeastOnce();
        EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(upgrade).atLeastOnce();
        EasyMock.expect(upgradeContextFactory.create(cluster, upgrade)).andReturn(upgradeContext).atLeastOnce();
        EasyMock.replay(clusters, cluster, upgradeContextFactory, upgrade);
        prepareFields();
    }

    @org.junit.Test
    public void testUpgrade() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.5");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersion.getStackId()).andReturn(stackId).atLeastOnce();
        EasyMock.expect(upgradeContext.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).atLeastOnce();
        EasyMock.expect(upgradeContext.getRepositoryVersion()).andReturn(repositoryVersion).atLeastOnce();
        EasyMock.replay(repositoryVersion, upgradeContext);
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
        org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor action = new org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor();
        injectFields(action);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hrc);
        org.apache.ambari.server.orm.entities.ArtifactEntity entity = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ArtifactEntity.class);
        EasyMock.expect(entity.getArtifactData()).andReturn(null).anyTimes();
        EasyMock.expect(entity.getForeignKeys()).andReturn(null).anyTimes();
        EasyMock.expect(artifactDAO.findByNameAndForeignKeys(EasyMock.anyString(), ((java.util.TreeMap<java.lang.String, java.lang.String>) (EasyMock.anyObject())))).andReturn(entity).atLeastOnce();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor newDescriptor = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor previousDescriptor = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor updatedKerberosDescriptor = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.class);
        org.powermock.api.mockito.PowerMockito.when(org.apache.ambari.server.state.kerberos.KerberosDescriptorUpdateHelper.updateUserKerberosDescriptor(previousDescriptor, newDescriptor, userDescriptor)).thenReturn(updatedKerberosDescriptor);
        EasyMock.expect(kerberosDescriptorFactory.createInstance(((java.util.Map) (null)))).andReturn(userDescriptor).atLeastOnce();
        EasyMock.expect(ambariMetaInfo.getKerberosDescriptor("HDP", "2.5", false)).andReturn(newDescriptor).atLeastOnce();
        EasyMock.expect(ambariMetaInfo.getKerberosDescriptor("HDP", "2.4", false)).andReturn(previousDescriptor).atLeastOnce();
        EasyMock.expect(updatedKerberosDescriptor.toMap()).andReturn(null).once();
        EasyMock.expect(artifactDAO.merge(entity)).andReturn(entity).once();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ArtifactEntity> createCapture = org.easymock.Capture.newInstance();
        artifactDAO.create(EasyMock.capture(createCapture));
        org.easymock.EasyMock.expectLastCall().once();
        EasyMock.replay(artifactDAO, entity, ambariMetaInfo, kerberosDescriptorFactory, updatedKerberosDescriptor);
        action.execute(null);
        EasyMock.verify(artifactDAO, updatedKerberosDescriptor);
        org.junit.Assert.assertEquals(createCapture.getValue().getArtifactName(), "kerberos_descriptor_backup");
    }

    @org.junit.Test
    public void testDowngrade() throws java.lang.Exception {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.5");
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersion.getStackId()).andReturn(stackId).atLeastOnce();
        EasyMock.expect(upgradeContext.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.DOWNGRADE).atLeastOnce();
        EasyMock.expect(upgradeContext.getRepositoryVersion()).andReturn(repositoryVersion).atLeastOnce();
        EasyMock.replay(repositoryVersion, upgradeContext);
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
        org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor action = new org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor();
        injectFields(action);
        action.setExecutionCommand(executionCommand);
        action.setHostRoleCommand(hrc);
        org.apache.ambari.server.orm.entities.ArtifactEntity entity = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.orm.entities.ArtifactEntity.class);
        EasyMock.expect(entity.getArtifactData()).andReturn(null).anyTimes();
        EasyMock.expect(entity.getForeignKeys()).andReturn(null).anyTimes();
        EasyMock.expect(artifactDAO.findByNameAndForeignKeys(EasyMock.anyString(), ((java.util.TreeMap<java.lang.String, java.lang.String>) (EasyMock.anyObject())))).andReturn(entity).atLeastOnce();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor userDescriptor = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.kerberos.KerberosDescriptor.class);
        EasyMock.expect(kerberosDescriptorFactory.createInstance(((java.util.Map) (null)))).andReturn(userDescriptor).atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.orm.entities.ArtifactEntity> createCapture = org.easymock.Capture.newInstance();
        artifactDAO.create(EasyMock.capture(createCapture));
        org.easymock.EasyMock.expectLastCall().once();
        artifactDAO.remove(entity);
        org.easymock.EasyMock.expectLastCall().atLeastOnce();
        EasyMock.replay(artifactDAO, entity, ambariMetaInfo, kerberosDescriptorFactory);
        action.execute(null);
        EasyMock.verify(artifactDAO);
        org.junit.Assert.assertEquals(createCapture.getValue().getArtifactName(), "kerberos_descriptor");
    }

    private void prepareFields() throws java.lang.NoSuchFieldException {
        java.lang.String[] fieldsNames = new java.lang.String[]{ "artifactDAO", "m_clusters", "ambariMetaInfo", "kerberosDescriptorFactory", "m_upgradeContextFactory" };
        for (java.lang.String fieldName : fieldsNames) {
            try {
                java.lang.reflect.Field clustersField = org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.class.getDeclaredField(fieldName);
                clustersField.setAccessible(true);
                fields.put(fieldName, clustersField);
            } catch (java.lang.NoSuchFieldException noSuchFieldException) {
                java.lang.reflect.Field clustersField = org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor.class.getSuperclass().getDeclaredField(fieldName);
                clustersField.setAccessible(true);
                fields.put(fieldName, clustersField);
            }
        }
    }

    private void injectFields(org.apache.ambari.server.serveraction.upgrades.UpgradeUserKerberosDescriptor action) throws java.lang.IllegalAccessException {
        fields.get("artifactDAO").set(action, artifactDAO);
        fields.get("m_clusters").set(action, clusters);
        fields.get("ambariMetaInfo").set(action, ambariMetaInfo);
        fields.get("kerberosDescriptorFactory").set(action, kerberosDescriptorFactory);
        fields.get("m_upgradeContextFactory").set(action, upgradeContextFactory);
    }
}