package org.apache.ambari.server.checks;
import org.mockito.Mockito;
public class PreviousUpgradeCompletedTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);

    private org.apache.ambari.server.state.StackId sourceStackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");

    private org.apache.ambari.server.state.StackId targetStackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");

    private java.lang.String destRepositoryVersion = "2.2.8.0-5678";

    private java.lang.String clusterName = "cluster";

    private org.apache.ambari.spi.upgrade.UpgradeCheckRequest checkRequest;

    private org.apache.ambari.server.checks.PreviousUpgradeCompleted puc = new org.apache.ambari.server.checks.PreviousUpgradeCompleted();

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity toRepsitoryVersionEntity;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(cluster.getClusterName()).thenReturn(clusterName);
        org.mockito.Mockito.when(clusters.getCluster(clusterName)).thenReturn(cluster);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        org.apache.ambari.server.orm.entities.StackEntity stack = new org.apache.ambari.server.orm.entities.StackEntity();
        stack.setStackName(stackId.getStackName());
        stack.setStackVersion(stackId.getStackVersion());
        toRepsitoryVersionEntity = org.mockito.Mockito.mock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        org.mockito.Mockito.when(toRepsitoryVersionEntity.getVersion()).thenReturn(destRepositoryVersion);
        org.mockito.Mockito.when(toRepsitoryVersionEntity.getStackId()).thenReturn(targetStackId);
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(clusterName, false, null, null, null);
        checkRequest = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        puc.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        org.mockito.Mockito.when(cluster.getUpgradeInProgress()).thenReturn(null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = puc.perform(checkRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeInProgress = org.mockito.Mockito.mock(org.apache.ambari.server.orm.entities.UpgradeEntity.class);
        org.mockito.Mockito.when(upgradeInProgress.getDirection()).thenReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE);
        org.mockito.Mockito.when(upgradeInProgress.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(upgradeInProgress.getRepositoryVersion()).thenReturn(toRepsitoryVersionEntity);
        org.mockito.Mockito.when(cluster.getUpgradeInProgress()).thenReturn(upgradeInProgress);
        check = puc.perform(checkRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
    }
}