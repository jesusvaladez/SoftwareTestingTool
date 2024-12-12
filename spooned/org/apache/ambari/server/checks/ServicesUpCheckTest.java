package org.apache.ambari.server.checks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.orm.models.HostComponentSummary.class)
public class ServicesUpCheckTest {
    private final org.apache.ambari.server.state.Clusters clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo = org.mockito.Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.ClusterVersionSummary m_clusterVersionSummary;

    @org.mockito.Mock
    private org.apache.ambari.server.state.repository.VersionDefinitionXml m_vdfXml;

    @org.mockito.Mock
    private org.apache.ambari.spi.RepositoryVersion m_repositoryVersion;

    @org.mockito.Mock
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersionEntity;

    @org.mockito.Mock
    private org.apache.ambari.server.orm.dao.RepositoryVersionDAO m_repositoryVersionDAO;

    final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> m_services = new java.util.HashMap<>();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        m_services.clear();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        java.lang.String version = "2.2.0.0-1234";
        org.mockito.Mockito.when(m_repositoryVersion.getId()).thenReturn(1L);
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersion.getStackId()).thenReturn(stackId.toString());
        org.mockito.Mockito.when(m_repositoryVersion.getVersion()).thenReturn(version);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getVersion()).thenReturn(version);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getStackId()).thenReturn(stackId);
        org.mockito.Mockito.when(m_repositoryVersionEntity.getRepositoryXml()).thenReturn(m_vdfXml);
        org.mockito.Mockito.when(m_vdfXml.getClusterSummary(org.mockito.Mockito.any(org.apache.ambari.server.state.Cluster.class), org.mockito.Mockito.any(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).thenReturn(m_clusterVersionSummary);
        org.mockito.Mockito.when(m_clusterVersionSummary.getAvailableServiceNames()).thenReturn(m_services.keySet());
        org.mockito.Mockito.when(m_repositoryVersionDAO.findByPK(org.mockito.Mockito.anyLong())).thenReturn(m_repositoryVersionEntity);
    }

    @org.junit.Test
    public void testPerform() throws java.lang.Exception {
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.checks.ServicesUpCheck servicesUpCheck = new org.apache.ambari.server.checks.ServicesUpCheck();
        servicesUpCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        servicesUpCheck.ambariMetaInfo = new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return ambariMetaInfo;
            }
        };
        servicesUpCheck.checkHelperProvider = new com.google.inject.Provider<org.apache.ambari.server.state.CheckHelper>() {
            @java.lang.Override
            public org.apache.ambari.server.state.CheckHelper get() {
                org.apache.ambari.server.checks.MockCheckHelper mockCheckHelper = new org.apache.ambari.server.checks.MockCheckHelper();
                mockCheckHelper.m_clusters = clusters;
                mockCheckHelper.m_repositoryVersionDAO = m_repositoryVersionDAO;
                mockCheckHelper.setMetaInfoProvider(servicesUpCheck.ambariMetaInfo);
                return mockCheckHelper;
            }
        };
        org.apache.ambari.server.state.Host host1 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host2 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host3 = org.mockito.Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.mockito.Mockito.when(host1.getMaintenanceState(org.mockito.Mockito.anyLong())).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host2.getMaintenanceState(org.mockito.Mockito.anyLong())).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host3.getMaintenanceState(org.mockito.Mockito.anyLong())).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        final org.apache.ambari.server.state.Cluster cluster = org.mockito.Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.mockito.Mockito.when(cluster.getClusterId()).thenReturn(1L);
        org.mockito.Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2"));
        org.mockito.Mockito.when(clusters.getCluster("cluster")).thenReturn(cluster);
        org.mockito.Mockito.when(clusters.getHostById(java.lang.Long.valueOf(1))).thenReturn(host1);
        org.mockito.Mockito.when(clusters.getHostById(java.lang.Long.valueOf(2))).thenReturn(host2);
        org.mockito.Mockito.when(clusters.getHostById(java.lang.Long.valueOf(3))).thenReturn(host3);
        final org.apache.ambari.server.state.Service hdfsService = org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class);
        final org.apache.ambari.server.state.Service tezService = org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class);
        final org.apache.ambari.server.state.Service amsService = org.mockito.Mockito.mock(org.apache.ambari.server.state.Service.class);
        m_services.put("HDFS", hdfsService);
        m_services.put("TEZ", tezService);
        m_services.put("AMBARI_METRICS", amsService);
        org.mockito.Mockito.when(hdfsService.getName()).thenReturn("HDFS");
        org.mockito.Mockito.when(tezService.getName()).thenReturn("TEZ");
        org.mockito.Mockito.when(amsService.getName()).thenReturn("AMBARI_METRICS");
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.2");
        org.mockito.Mockito.when(hdfsService.getDesiredStackId()).thenReturn(stackId);
        org.mockito.Mockito.when(tezService.getDesiredStackId()).thenReturn(stackId);
        org.mockito.Mockito.when(amsService.getDesiredStackId()).thenReturn(stackId);
        org.mockito.Mockito.when(hdfsService.isClientOnlyService()).thenReturn(false);
        org.mockito.Mockito.when(tezService.isClientOnlyService()).thenReturn(true);
        org.mockito.Mockito.when(amsService.isClientOnlyService()).thenReturn(false);
        org.mockito.Mockito.when(cluster.getServices()).thenReturn(m_services);
        org.mockito.Mockito.when(cluster.getService("HDFS")).thenReturn(hdfsService);
        org.mockito.Mockito.when(cluster.getService("TEZ")).thenReturn(tezService);
        org.mockito.Mockito.when(cluster.getService("AMBARI_METRICS")).thenReturn(amsService);
        org.mockito.Mockito.when(ambariMetaInfo.getComponent(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenAnswer(new org.mockito.stubbing.Answer<org.apache.ambari.server.state.ComponentInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.state.ComponentInfo answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                org.apache.ambari.server.state.ComponentInfo anyInfo = org.mockito.Mockito.mock(org.apache.ambari.server.state.ComponentInfo.class);
                if ((invocation.getArguments().length > 3) && "DATANODE".equals(invocation.getArguments()[3])) {
                    org.mockito.Mockito.when(anyInfo.getCardinality()).thenReturn("1+");
                } else {
                    org.mockito.Mockito.when(anyInfo.getCardinality()).thenReturn(null);
                }
                return anyInfo;
            }
        });
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> hdfsComponents = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponent nameNode = org.mockito.Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.mockito.Mockito.when(nameNode.getName()).thenReturn("NAMENODE");
        org.mockito.Mockito.when(nameNode.isClientComponent()).thenReturn(false);
        org.mockito.Mockito.when(nameNode.isVersionAdvertised()).thenReturn(true);
        org.mockito.Mockito.when(nameNode.isMasterComponent()).thenReturn(true);
        org.apache.ambari.server.state.ServiceComponent dataNode = org.mockito.Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.mockito.Mockito.when(dataNode.getName()).thenReturn("DATANODE");
        org.mockito.Mockito.when(dataNode.isClientComponent()).thenReturn(false);
        org.mockito.Mockito.when(dataNode.isVersionAdvertised()).thenReturn(true);
        org.mockito.Mockito.when(dataNode.isMasterComponent()).thenReturn(false);
        org.apache.ambari.server.state.ServiceComponent zkfc = org.mockito.Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.mockito.Mockito.when(zkfc.getName()).thenReturn("ZKFC");
        org.mockito.Mockito.when(zkfc.isClientComponent()).thenReturn(false);
        org.mockito.Mockito.when(zkfc.isVersionAdvertised()).thenReturn(false);
        org.mockito.Mockito.when(zkfc.isMasterComponent()).thenReturn(false);
        hdfsComponents.put("NAMENODE", nameNode);
        hdfsComponents.put("DATANODE", dataNode);
        hdfsComponents.put("ZKFC", zkfc);
        org.mockito.Mockito.when(hdfsService.getServiceComponents()).thenReturn(hdfsComponents);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> tezComponents = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponent tezClient = org.mockito.Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.mockito.Mockito.when(tezClient.getName()).thenReturn("TEZ_CLIENT");
        org.mockito.Mockito.when(tezClient.isClientComponent()).thenReturn(true);
        org.mockito.Mockito.when(tezClient.isVersionAdvertised()).thenReturn(true);
        tezComponents.put("TEZ_CLIENT", tezClient);
        org.mockito.Mockito.when(tezService.getServiceComponents()).thenReturn(tezComponents);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponent> amsComponents = new java.util.HashMap<>();
        org.apache.ambari.server.state.ServiceComponent metricsCollector = org.mockito.Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.mockito.Mockito.when(metricsCollector.getName()).thenReturn("METRICS_COLLECTOR");
        org.mockito.Mockito.when(metricsCollector.isClientComponent()).thenReturn(false);
        org.mockito.Mockito.when(metricsCollector.isVersionAdvertised()).thenReturn(false);
        org.apache.ambari.server.state.ServiceComponent metricsMonitor = org.mockito.Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.mockito.Mockito.when(metricsMonitor.getName()).thenReturn("METRICS_MONITOR");
        org.mockito.Mockito.when(metricsMonitor.isClientComponent()).thenReturn(false);
        org.mockito.Mockito.when(metricsMonitor.isVersionAdvertised()).thenReturn(false);
        amsComponents.put("METRICS_COLLECTOR", metricsCollector);
        amsComponents.put("METRICS_MONITOR", metricsMonitor);
        org.mockito.Mockito.when(amsService.getServiceComponents()).thenReturn(amsComponents);
        final org.apache.ambari.server.orm.models.HostComponentSummary hcsNameNode = org.mockito.Mockito.mock(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.orm.models.HostComponentSummary hcsDataNode1 = org.mockito.Mockito.mock(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.orm.models.HostComponentSummary hcsDataNode2 = org.mockito.Mockito.mock(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.orm.models.HostComponentSummary hcsDataNode3 = org.mockito.Mockito.mock(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.orm.models.HostComponentSummary hcsZKFC = org.mockito.Mockito.mock(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.orm.models.HostComponentSummary hcsTezClient = org.mockito.Mockito.mock(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.orm.models.HostComponentSummary hcsMetricsCollector = org.mockito.Mockito.mock(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        final org.apache.ambari.server.orm.models.HostComponentSummary hcsMetricsMonitor = org.mockito.Mockito.mock(org.apache.ambari.server.orm.models.HostComponentSummary.class);
        org.mockito.Mockito.when(hcsNameNode.getHostId()).thenReturn(java.lang.Long.valueOf(1));
        org.mockito.Mockito.when(hcsDataNode1.getHostId()).thenReturn(java.lang.Long.valueOf(1));
        org.mockito.Mockito.when(hcsDataNode2.getHostId()).thenReturn(java.lang.Long.valueOf(2));
        org.mockito.Mockito.when(hcsDataNode3.getHostId()).thenReturn(java.lang.Long.valueOf(3));
        org.mockito.Mockito.when(hcsZKFC.getHostId()).thenReturn(java.lang.Long.valueOf(1));
        org.mockito.Mockito.when(hcsTezClient.getHostId()).thenReturn(java.lang.Long.valueOf(2));
        org.mockito.Mockito.when(hcsMetricsCollector.getHostId()).thenReturn(java.lang.Long.valueOf(1));
        org.mockito.Mockito.when(hcsMetricsMonitor.getHostId()).thenReturn(java.lang.Long.valueOf(1));
        java.util.List<org.apache.ambari.server.orm.models.HostComponentSummary> allHostComponentSummaries = new java.util.ArrayList<>();
        allHostComponentSummaries.add(hcsNameNode);
        allHostComponentSummaries.add(hcsDataNode1);
        allHostComponentSummaries.add(hcsDataNode2);
        allHostComponentSummaries.add(hcsDataNode3);
        allHostComponentSummaries.add(hcsZKFC);
        allHostComponentSummaries.add(hcsTezClient);
        allHostComponentSummaries.add(hcsMetricsCollector);
        allHostComponentSummaries.add(hcsMetricsMonitor);
        org.mockito.Mockito.when(org.apache.ambari.server.orm.models.HostComponentSummary.getHostComponentSummaries("HDFS", "NAMENODE")).thenReturn(java.util.Arrays.asList(hcsNameNode));
        org.mockito.Mockito.when(org.apache.ambari.server.orm.models.HostComponentSummary.getHostComponentSummaries("HDFS", "DATANODE")).thenReturn(java.util.Arrays.asList(hcsDataNode1, hcsDataNode2, hcsDataNode3));
        org.mockito.Mockito.when(org.apache.ambari.server.orm.models.HostComponentSummary.getHostComponentSummaries("HDFS", "ZKFC")).thenReturn(java.util.Arrays.asList(hcsZKFC));
        org.mockito.Mockito.when(org.apache.ambari.server.orm.models.HostComponentSummary.getHostComponentSummaries("TEZ", "TEZ_CLIENT")).thenReturn(java.util.Arrays.asList(hcsTezClient));
        org.mockito.Mockito.when(org.apache.ambari.server.orm.models.HostComponentSummary.getHostComponentSummaries("AMBARI_METRICS", "METRICS_COLLECTOR")).thenReturn(java.util.Arrays.asList(hcsMetricsCollector));
        org.mockito.Mockito.when(org.apache.ambari.server.orm.models.HostComponentSummary.getHostComponentSummaries("AMBARI_METRICS", "METRICS_MONITOR")).thenReturn(java.util.Arrays.asList(hcsMetricsMonitor));
        for (org.apache.ambari.server.orm.models.HostComponentSummary hcs : allHostComponentSummaries) {
            org.mockito.Mockito.when(hcs.getDesiredState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
            org.mockito.Mockito.when(hcs.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.STARTED);
        }
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("cluster", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(hcsNameNode.getDesiredState()).thenReturn(org.apache.ambari.server.state.State.STARTED);
        org.mockito.Mockito.when(hcsDataNode1.getDesiredState()).thenReturn(org.apache.ambari.server.state.State.STARTED);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(hcsZKFC.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        org.mockito.Mockito.when(hcsMetricsCollector.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        org.mockito.Mockito.when(hcsMetricsMonitor.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(hcsNameNode.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        org.mockito.Mockito.when(hcsDataNode1.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertFalse(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(hcsNameNode.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.STARTED);
        org.mockito.Mockito.when(hcsDataNode1.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(hcsNameNode.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.STARTED);
        org.mockito.Mockito.when(hcsDataNode1.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        org.mockito.Mockito.when(hcsDataNode2.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailReason().indexOf("50%") > (-1));
        org.junit.Assert.assertFalse(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(host1.getMaintenanceState(org.mockito.Mockito.anyLong())).thenReturn(org.apache.ambari.server.state.MaintenanceState.ON);
        org.mockito.Mockito.when(host3.getMaintenanceState(org.mockito.Mockito.anyLong())).thenReturn(org.apache.ambari.server.state.MaintenanceState.ON);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(hcsNameNode.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.INSTALLED);
        org.mockito.Mockito.when(hcsDataNode1.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.STARTED);
        org.mockito.Mockito.when(hcsDataNode2.getCurrentState()).thenReturn(org.apache.ambari.server.state.State.STARTED);
        org.mockito.Mockito.when(host1.getMaintenanceState(org.mockito.Mockito.anyLong())).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        org.mockito.Mockito.when(host3.getMaintenanceState(org.mockito.Mockito.anyLong())).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertFalse(check.getFailedDetail().isEmpty());
        org.mockito.Mockito.when(host1.getMaintenanceState(org.mockito.Mockito.anyLong())).thenReturn(org.apache.ambari.server.state.MaintenanceState.ON);
        check = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(null, null);
        check = servicesUpCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertFalse(check.getFailedDetail().isEmpty());
    }
}