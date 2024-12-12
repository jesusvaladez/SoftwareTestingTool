package org.apache.ambari.server.state.stack.upgrade;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
public class StageWrapperBuilderTest extends org.easymock.EasyMockSupport {
    private static final org.apache.ambari.server.state.StackId HDP_21 = new org.apache.ambari.server.state.StackId("HDP-2.1.1");

    @org.junit.Test
    public void testBuildOrder() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.easymock.EasyMock.expect(repoVersionEntity.getStackId()).andReturn(org.apache.ambari.server.state.stack.upgrade.StageWrapperBuilderTest.HDP_21).anyTimes();
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO = createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.easymock.EasyMock.expect(repoVersionDAO.findByStackNameAndVersion(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString())).andReturn(repoVersionEntity).anyTimes();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);
        org.easymock.EasyMock.expect(upgradeContext.getCluster()).andReturn(cluster).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.getType()).andReturn(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.getRepositoryVersion()).andReturn(repoVersionEntity).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.isComponentFailureAutoSkipped()).andReturn(false).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.isServiceCheckFailureAutoSkipped()).andReturn(false).anyTimes();
        replayAll();
        org.apache.ambari.server.state.stack.upgrade.StageWrapperBuilderTest.MockStageWrapperBuilder builder = new org.apache.ambari.server.state.stack.upgrade.StageWrapperBuilderTest.MockStageWrapperBuilder(null);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = builder.build(upgradeContext);
        java.util.List<java.lang.Integer> invocationOrder = builder.getInvocationOrder();
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(0), invocationOrder.get(0));
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(1), invocationOrder.get(1));
        org.junit.Assert.assertEquals(java.lang.Integer.valueOf(2), invocationOrder.get(2));
        org.junit.Assert.assertTrue(stageWrappers.isEmpty());
        verifyAll();
    }

    @org.junit.Test
    public void testAutoSkipCheckInserted() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersionEntity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.easymock.EasyMock.expect(repoVersionEntity.getStackId()).andReturn(org.apache.ambari.server.state.stack.upgrade.StageWrapperBuilderTest.HDP_21).anyTimes();
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO = createNiceMock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.easymock.EasyMock.expect(repoVersionDAO.findByStackNameAndVersion(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyString())).andReturn(repoVersionEntity).anyTimes();
        org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);
        org.easymock.EasyMock.expect(upgradeContext.getCluster()).andReturn(cluster).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.getType()).andReturn(org.apache.ambari.spi.upgrade.UpgradeType.ROLLING).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.getRepositoryVersion()).andReturn(repoVersionEntity).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.isComponentFailureAutoSkipped()).andReturn(true).anyTimes();
        org.easymock.EasyMock.expect(upgradeContext.isServiceCheckFailureAutoSkipped()).andReturn(true).anyTimes();
        replayAll();
        org.apache.ambari.server.stack.upgrade.Grouping grouping = new org.apache.ambari.server.stack.upgrade.Grouping();
        grouping.skippable = true;
        org.apache.ambari.server.state.stack.upgrade.StageWrapperBuilderTest.MockStageWrapperBuilder builder = new org.apache.ambari.server.state.stack.upgrade.StageWrapperBuilderTest.MockStageWrapperBuilder(grouping);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> mockStageWrappers = new java.util.ArrayList<>();
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper mockStageWrapper = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.class);
        mockStageWrappers.add(mockStageWrapper);
        builder.setMockStageWrappers(mockStageWrappers);
        java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = builder.build(upgradeContext);
        org.junit.Assert.assertEquals(2, stageWrappers.size());
        org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper skipSummaryWrapper = stageWrappers.get(1);
        org.junit.Assert.assertEquals(org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper.Type.SERVER_SIDE_ACTION, skipSummaryWrapper.getType());
        org.apache.ambari.server.stack.upgrade.ServerActionTask task = ((org.apache.ambari.server.stack.upgrade.ServerActionTask) (skipSummaryWrapper.getTasks().get(0).getTasks().get(0)));
        org.junit.Assert.assertEquals(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction.class.getName(), task.implClass);
        org.junit.Assert.assertEquals(1, task.messages.size());
        org.junit.Assert.assertTrue(task.messages.get(0).contains("There are failures that were automatically skipped"));
        verifyAll();
    }

    private final class MockStageWrapperBuilder extends org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapperBuilder {
        private java.util.List<java.lang.Integer> m_invocationOrder = new java.util.ArrayList<>();

        private java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> m_stageWrappers = java.util.Collections.emptyList();

        protected MockStageWrapperBuilder(org.apache.ambari.server.stack.upgrade.Grouping grouping) {
            super(grouping);
        }

        private void setMockStageWrappers(java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
            m_stageWrappers = stageWrappers;
        }

        private java.util.List<java.lang.Integer> getInvocationOrder() {
            return m_invocationOrder;
        }

        @java.lang.Override
        public void add(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, org.apache.ambari.server.stack.HostsType hostsType, java.lang.String service, boolean clientOnly, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent pc, java.util.Map<java.lang.String, java.lang.String> params) {
        }

        @java.lang.Override
        public java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
            m_invocationOrder.add(1);
            return m_stageWrappers;
        }

        @java.lang.Override
        protected java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> beforeBuild(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext) {
            java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers = super.beforeBuild(upgradeContext);
            m_invocationOrder.add(0);
            return stageWrappers;
        }

        @java.lang.Override
        protected java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> afterBuild(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext upgradeContext, java.util.List<org.apache.ambari.server.stack.upgrade.orchestrate.StageWrapper> stageWrappers) {
            stageWrappers = super.afterBuild(upgradeContext, stageWrappers);
            m_invocationOrder.add(2);
            return stageWrappers;
        }
    }
}