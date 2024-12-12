package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class AutoStartDisabledCheckTest {
    private final org.apache.ambari.server.checks.AutoStartDisabledCheck m_check = new org.apache.ambari.server.checks.AutoStartDisabledCheck();

    private final org.apache.ambari.server.state.Clusters m_clusters = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);

    private java.util.Map<java.lang.String, java.lang.String> m_configMap = new java.util.HashMap<>();

    org.apache.ambari.spi.RepositoryVersion repositoryVersion;

    org.apache.ambari.spi.ClusterInformation clusterInformation;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        m_check.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return m_clusters;
            }
        };
        org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.checks.AutoStartDisabledCheck.CLUSTER_ENV_TYPE, new org.apache.ambari.server.state.DesiredConfig());
        EasyMock.expect(cluster.getDesiredConfigs()).andReturn(map).anyTimes();
        org.apache.ambari.server.state.Config config = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(config.getProperties()).andReturn(m_configMap).anyTimes();
        EasyMock.expect(cluster.getConfig(org.easymock.EasyMock.eq(org.apache.ambari.server.checks.AutoStartDisabledCheck.CLUSTER_ENV_TYPE), org.easymock.EasyMock.anyString())).andReturn(config).anyTimes();
        EasyMock.expect(m_clusters.getCluster(((java.lang.String) (EasyMock.anyObject())))).andReturn(cluster).anyTimes();
        repositoryVersion = org.easymock.EasyMock.createNiceMock(org.apache.ambari.spi.RepositoryVersion.class);
        EasyMock.expect(repositoryVersion.getRepositoryType()).andReturn(org.apache.ambari.spi.RepositoryType.STANDARD).anyTimes();
        clusterInformation = org.easymock.EasyMock.createNiceMock(org.apache.ambari.spi.ClusterInformation.class);
        EasyMock.expect(clusterInformation.getClusterName()).andReturn("cluster").anyTimes();
        EasyMock.replay(m_clusters, cluster, config, repositoryVersion, clusterInformation);
        m_configMap.clear();
    }

    @org.junit.Test
    public void testIsApplicable() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion, m_configMap, null);
        org.apache.ambari.server.state.CheckHelper checkHelper = new org.apache.ambari.server.state.CheckHelper();
        java.util.List<org.apache.ambari.spi.upgrade.UpgradeCheck> applicableChecks = checkHelper.getApplicableChecks(request, com.google.common.collect.Lists.newArrayList(m_check));
        org.junit.Assert.assertTrue(applicableChecks.size() == 1);
    }

    @org.junit.Test
    public void testNoAutoStart() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.isBlank(check.getFailReason()));
    }

    @org.junit.Test
    public void testAutoStartFalse() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion, null, null);
        m_configMap.put(org.apache.ambari.server.checks.AutoStartDisabledCheck.RECOVERY_ENABLED_KEY, "false");
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.isBlank(check.getFailReason()));
    }

    @org.junit.Test
    public void testAutoStartTrue() throws java.lang.Exception {
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, repositoryVersion, null, null);
        m_configMap.put(org.apache.ambari.server.checks.AutoStartDisabledCheck.RECOVERY_ENABLED_KEY, "true");
        m_configMap.put(org.apache.ambari.server.checks.AutoStartDisabledCheck.RECOVERY_TYPE_KEY, org.apache.ambari.server.checks.AutoStartDisabledCheck.RECOVERY_AUTO_START);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertTrue(org.apache.commons.lang.StringUtils.isNotBlank(check.getFailReason()));
        org.junit.Assert.assertEquals("Auto Start must be disabled before performing an Upgrade. To disable Auto Start, navigate to " + "Admin > Service Auto Start. Turn the toggle switch off to Disabled and hit Save.", check.getFailReason());
    }
}