package org.apache.ambari.server.checks;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class ComponentExistsInRepoCheckTest extends org.easymock.EasyMockSupport {
    public static final java.lang.String STACK_NAME = "HDP";

    public static final java.lang.String STACK_VERSION = "2.2.0";

    private org.apache.ambari.server.checks.ComponentsExistInRepoCheck check = new org.apache.ambari.server.checks.ComponentsExistInRepoCheck();

    @org.easymock.Mock
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock
    private org.apache.ambari.server.state.ServiceComponentSupport serviceComponentSupport;

    @org.easymock.Mock
    private org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper;

    private org.apache.ambari.spi.upgrade.UpgradeCheckRequest request;

    private org.apache.ambari.server.state.StackId sourceStackId = new org.apache.ambari.server.state.StackId(org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_NAME, "1.0");

    @org.junit.Before
    public void before() throws java.lang.Exception {
        check.clustersProvider = () -> clusters;
        check.serviceComponentSupport = serviceComponentSupport;
        check.upgradeHelper = upgradeHelper;
        EasyMock.expect(clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getCurrentStackVersion()).andReturn(sourceStackId).anyTimes();
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repoVersion(), null, null);
    }

    @org.junit.Test
    public void testPassesWhenNoUnsupportedInTargetStack() throws java.lang.Exception {
        EasyMock.expect(serviceComponentSupport.allUnsupported(cluster, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_NAME, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_VERSION)).andReturn(java.util.Collections.emptyList()).anyTimes();
        replayAll();
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, result.getStatus());
    }

    @org.junit.Test
    public void testFailsWhenUnsupportedFoundInTargetStack() throws java.lang.Exception {
        EasyMock.expect(serviceComponentSupport.allUnsupported(cluster, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_NAME, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_VERSION)).andReturn(java.util.Collections.singletonList("ANY_SERVICE")).anyTimes();
        suggestedUpgradePackIs(new org.apache.ambari.server.stack.upgrade.UpgradePack());
        replayAll();
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testWarnsWhenUnsupportedFoundInTargetStackAndUpgradePackHasAutoDeleteTask() throws java.lang.Exception {
        EasyMock.expect(serviceComponentSupport.allUnsupported(cluster, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_NAME, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_VERSION)).andReturn(java.util.Collections.singletonList("ANY_SERVICE")).anyTimes();
        suggestedUpgradePackIs(upgradePackWithDeleteUnsupportedTask());
        replayAll();
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING, result.getStatus());
    }

    private org.apache.ambari.spi.RepositoryVersion repoVersion() {
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = new org.apache.ambari.spi.RepositoryVersion(1, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_NAME, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_VERSION, new org.apache.ambari.server.state.StackId(org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_NAME, org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_VERSION).getStackId(), org.apache.ambari.server.checks.ComponentExistsInRepoCheckTest.STACK_VERSION, org.apache.ambari.spi.RepositoryType.STANDARD);
        return repositoryVersion;
    }

    private void suggestedUpgradePackIs(org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack) throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(upgradeHelper.suggestUpgradePack("cluster", sourceStackId, new org.apache.ambari.server.state.StackId(request.getTargetRepositoryVersion().getStackId()), org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, request.getUpgradeType(), null)).andReturn(upgradePack).anyTimes();
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack upgradePackWithDeleteUnsupportedTask() {
        org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = new org.apache.ambari.server.stack.upgrade.UpgradePack();
        org.apache.ambari.server.stack.upgrade.ClusterGrouping group = new org.apache.ambari.server.stack.upgrade.ClusterGrouping();
        org.apache.ambari.server.stack.upgrade.ExecuteStage stage = new org.apache.ambari.server.stack.upgrade.ExecuteStage();
        org.apache.ambari.server.stack.upgrade.ServerActionTask task = new org.apache.ambari.server.stack.upgrade.ServerActionTask();
        task.setImplClass(org.apache.ambari.server.serveraction.upgrades.DeleteUnsupportedServicesAndComponents.class.getName());
        stage.task = task;
        group.executionStages = java.util.Collections.singletonList(stage);
        upgradePack.getAllGroups().add(group);
        return upgradePack;
    }
}