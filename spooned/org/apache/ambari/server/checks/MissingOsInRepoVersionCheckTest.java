package org.apache.ambari.server.checks;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.apache.ambari.server.state.MaintenanceState.OFF;
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.expect;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class MissingOsInRepoVersionCheckTest extends org.easymock.EasyMockSupport {
    public static final java.lang.String CLUSTER_NAME = "cluster";

    public static final org.apache.ambari.server.state.StackId SOURCE_STACK = new org.apache.ambari.server.state.StackId("HDP-2.6");

    public static final java.lang.String OS_FAMILY_IN_CLUSTER = "centos7";

    private org.apache.ambari.server.checks.MissingOsInRepoVersionCheck prerequisite;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Host host;

    @org.easymock.Mock
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    @org.easymock.Mock
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO;

    private org.apache.ambari.server.checks.MockCheckHelper m_checkHelper = new org.apache.ambari.server.checks.MockCheckHelper();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        prerequisite = new org.apache.ambari.server.checks.MissingOsInRepoVersionCheck();
        prerequisite.clustersProvider = () -> clusters;
        prerequisite.ambariMetaInfo = () -> ambariMetaInfo;
        prerequisite.checkHelperProvider = () -> m_checkHelper;
        prerequisite.repositoryVersionDaoProvider = () -> repositoryVersionDAO;
        EasyMock.expect(clusters.getCluster(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.CLUSTER_NAME)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getHosts()).andReturn(java.util.Collections.singleton(host)).anyTimes();
        EasyMock.expect(cluster.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(host.getOsFamily()).andReturn(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.OS_FAMILY_IN_CLUSTER).anyTimes();
        EasyMock.expect(host.getMaintenanceState(EasyMock.anyInt())).andReturn(org.apache.ambari.server.state.MaintenanceState.OFF).anyTimes();
        m_checkHelper.m_clusters = clusters;
    }

    @org.junit.Test
    public void testSuccessWhenOsExistsBothInTargetAndSource() throws java.lang.Exception {
        sourceStackRepoIs(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.OS_FAMILY_IN_CLUSTER);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = request(targetRepo(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.OS_FAMILY_IN_CLUSTER));
        replayAll();
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = performPrerequisite(request);
        verifyAll();
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
    }

    @org.junit.Test
    public void testFailsWhenOsDoesntExistInSource() throws java.lang.Exception {
        sourceStackRepoIs("different-os");
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = request(targetRepo(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.OS_FAMILY_IN_CLUSTER));
        replayAll();
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = performPrerequisite(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        verifyAll();
    }

    @org.junit.Test
    public void testFailsWhenOsDoesntExistInTarget() throws java.lang.Exception {
        sourceStackRepoIs(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.OS_FAMILY_IN_CLUSTER);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = request(targetRepo("different-os"));
        replayAll();
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = performPrerequisite(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        verifyAll();
    }

    private void sourceStackRepoIs(java.lang.String osFamily) throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(ambariMetaInfo.getStack(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.SOURCE_STACK)).andReturn(stackInfo(repoInfo(osFamily))).anyTimes();
    }

    private org.apache.ambari.server.state.StackInfo stackInfo(org.apache.ambari.server.state.RepositoryInfo repositoryInfo) {
        org.apache.ambari.server.state.StackInfo stackInfo = new org.apache.ambari.server.state.StackInfo();
        stackInfo.getRepositories().add(repositoryInfo);
        return stackInfo;
    }

    private org.apache.ambari.server.state.RepositoryInfo repoInfo(java.lang.String osType) {
        org.apache.ambari.server.state.RepositoryInfo repo = new org.apache.ambari.server.state.RepositoryInfo();
        repo.setOsType(osType);
        return repo;
    }

    private org.apache.ambari.spi.upgrade.UpgradeCheckRequest request(org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepo) {
        m_checkHelper.m_repositoryVersionDAO = repositoryVersionDAO;
        EasyMock.expect(repositoryVersionDAO.findByPK(1L)).andReturn(targetRepo).anyTimes();
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = createNiceMock(org.apache.ambari.spi.RepositoryVersion.class);
        EasyMock.expect(repositoryVersion.getId()).andReturn(1L).anyTimes();
        EasyMock.expect(repositoryVersion.getRepositoryType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        EasyMock.expect(repositoryVersion.getStackId()).andReturn(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.SOURCE_STACK.getStackId()).anyTimes();
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.MissingOsInRepoVersionCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion, null, null);
        return request;
    }

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepo(java.lang.String osFamilyInCluster) {
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity targetRepo = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity();
        org.apache.ambari.server.orm.entities.RepoOsEntity osEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        osEntity.setFamily(osFamilyInCluster);
        targetRepo.addRepoOsEntities(java.util.Collections.singletonList(osEntity));
        return targetRepo;
    }

    private org.apache.ambari.spi.upgrade.UpgradeCheckResult performPrerequisite(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        return prerequisite.perform(request);
    }
}