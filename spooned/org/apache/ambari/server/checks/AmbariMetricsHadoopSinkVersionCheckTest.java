package org.apache.ambari.server.checks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.MIN_HADOOP_SINK_VERSION_PROPERTY_NAME;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.controller.AmbariServer.class, org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.class, org.apache.ambari.server.controller.utilities.PropertyHelper.class })
public class AmbariMetricsHadoopSinkVersionCheckTest {
    private final org.apache.ambari.server.state.Clusters m_clusters = org.mockito.Mockito.mock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck m_check = new org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck();

    private final org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = org.mockito.Mockito.mock(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);

    private org.apache.ambari.server.state.repository.ClusterVersionSummary m_clusterVersionSummary;

    private org.apache.ambari.server.state.repository.VersionDefinitionXml m_vdfXml;

    @org.mockito.Mock
    private org.apache.ambari.spi.RepositoryVersion m_repositoryVersion;

    @org.mockito.Mock
    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersionEntity;

    private org.apache.ambari.server.checks.MockCheckHelper m_checkHelper = new org.apache.ambari.server.checks.MockCheckHelper();

    final java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> m_services = new java.util.HashMap<>();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        m_repositoryVersionEntity = org.mockito.Mockito.mock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        m_clusterVersionSummary = org.mockito.Mockito.mock(org.apache.ambari.server.state.repository.ClusterVersionSummary.class);
        m_vdfXml = org.mockito.Mockito.mock(org.apache.ambari.server.state.repository.VersionDefinitionXml.class);
        org.mockito.MockitoAnnotations.initMocks(this);
        m_check.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return m_clusters;
            }
        };
        org.apache.ambari.server.configuration.Configuration config = org.mockito.Mockito.mock(org.apache.ambari.server.configuration.Configuration.class);
        m_check.config = config;
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "3.0");
        java.lang.String version = "3.0.0.0-1234";
        org.mockito.Mockito.when(m_repositoryVersion.getId()).thenReturn(1L);
        org.mockito.Mockito.when(m_repositoryVersion.getRepositoryType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        org.mockito.Mockito.when(m_repositoryVersion.getStackId()).thenReturn(stackId.toString());
        org.mockito.Mockito.when(m_repositoryVersion.getVersion()).thenReturn(version);
        Mockito.when(m_repositoryVersionEntity.getVersion()).thenReturn(version);
        Mockito.when(m_repositoryVersionEntity.getStackId()).thenReturn(stackId);
        m_services.clear();
        Mockito.when(m_repositoryVersionEntity.getType()).thenReturn(org.apache.ambari.spi.RepositoryType.STANDARD);
        Mockito.when(m_repositoryVersionEntity.getRepositoryXml()).thenReturn(m_vdfXml);
        Mockito.when(m_vdfXml.getClusterSummary(org.mockito.Mockito.any(org.apache.ambari.server.state.Cluster.class), org.mockito.Mockito.any(org.apache.ambari.server.api.services.AmbariMetaInfo.class))).thenReturn(m_clusterVersionSummary);
        Mockito.when(m_clusterVersionSummary.getAvailableServiceNames()).thenReturn(m_services.keySet());
        m_checkHelper.m_clusters = m_clusters;
        org.mockito.Mockito.when(m_checkHelper.m_repositoryVersionDAO.findByPK(org.mockito.Mockito.anyLong())).thenReturn(m_repositoryVersionEntity);
        m_check.checkHelperProvider = new com.google.inject.Provider<org.apache.ambari.server.state.CheckHelper>() {
            @java.lang.Override
            public org.apache.ambari.server.state.CheckHelper get() {
                return m_checkHelper;
            }
        };
    }

    @org.junit.Test
    public void testIsApplicable() throws java.lang.Exception {
        org.junit.Assert.assertTrue(m_check.getApplicableServices().contains("HDFS"));
        org.junit.Assert.assertTrue(m_check.getApplicableServices().contains("AMBARI_METRICS"));
    }

    @org.junit.Test(timeout = 60000)
    public void testPerform() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementControllerMock = org.mockito.Mockito.mock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
        Mockito.when(org.apache.ambari.server.controller.AmbariServer.getController()).thenReturn(ambariManagementControllerMock);
        org.apache.ambari.server.controller.spi.ResourceProvider resourceProviderMock = Mockito.mock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.class);
        Mockito.when(org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(Matchers.eq(org.apache.ambari.server.controller.spi.Resource.Type.Request), Matchers.any(org.apache.ambari.server.controller.AmbariManagementController.class))).thenReturn(resourceProviderMock);
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.controller.utilities.PropertyHelper.class);
        org.apache.ambari.server.controller.spi.Request requestMock = Mockito.mock(org.apache.ambari.server.controller.spi.Request.class);
        Mockito.when(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(Matchers.any(), Matchers.any())).thenReturn(requestMock);
        Mockito.when(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id")).thenReturn("requestIdProp");
        org.apache.ambari.server.controller.spi.RequestStatus requestStatusMock = Mockito.mock(org.apache.ambari.server.controller.spi.RequestStatus.class);
        org.apache.ambari.server.controller.spi.Resource responseResourceMock = Mockito.mock(org.apache.ambari.server.controller.spi.Resource.class);
        Mockito.when(resourceProviderMock.createResources(requestMock)).thenReturn(requestStatusMock);
        Mockito.when(requestStatusMock.getRequestResource()).thenReturn(responseResourceMock);
        Mockito.when(responseResourceMock.getPropertyValue(Matchers.anyString())).thenReturn(100L);
        org.apache.ambari.server.state.Clusters clustersMock = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        Mockito.when(ambariManagementControllerMock.getClusters()).thenReturn(clustersMock);
        org.apache.ambari.server.state.Cluster clusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        Mockito.when(clustersMock.getCluster("c1")).thenReturn(clusterMock);
        Mockito.when(clusterMock.getHosts(Matchers.eq("AMBARI_METRICS"), Matchers.eq("METRICS_MONITOR"))).thenReturn(java.util.Collections.singleton("h1"));
        org.apache.ambari.server.orm.dao.RequestDAO requestDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntityMock = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(requestDAOMock.findByPks(java.util.Collections.singleton(100L), true)).thenReturn(java.util.Collections.singletonList(requestEntityMock));
        Mockito.when(requestEntityMock.getStatus()).thenReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS).thenReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        java.lang.reflect.Field requestDaoField = m_check.getClass().getDeclaredField("requestDAO");
        requestDaoField.setAccessible(true);
        requestDaoField.set(m_check, requestDAOMock);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.MIN_HADOOP_SINK_VERSION_PROPERTY_NAME, "2.7.0.0");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("c1", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS, check.getStatus());
    }

    @org.junit.Test(timeout = 60000)
    public void testPerformFail() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementControllerMock = org.mockito.Mockito.mock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.controller.AmbariServer.class);
        Mockito.when(org.apache.ambari.server.controller.AmbariServer.getController()).thenReturn(ambariManagementControllerMock);
        org.apache.ambari.server.controller.spi.ResourceProvider resourceProviderMock = Mockito.mock(org.apache.ambari.server.controller.spi.ResourceProvider.class);
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.class);
        Mockito.when(org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(Matchers.eq(org.apache.ambari.server.controller.spi.Resource.Type.Request), Matchers.any(org.apache.ambari.server.controller.AmbariManagementController.class))).thenReturn(resourceProviderMock);
        org.powermock.api.mockito.PowerMockito.mockStatic(org.apache.ambari.server.controller.utilities.PropertyHelper.class);
        org.apache.ambari.server.controller.spi.Request requestMock = Mockito.mock(org.apache.ambari.server.controller.spi.Request.class);
        Mockito.when(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(Matchers.any(), Matchers.any())).thenReturn(requestMock);
        Mockito.when(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id")).thenReturn("requestIdProp");
        org.apache.ambari.server.controller.spi.RequestStatus requestStatusMock = Mockito.mock(org.apache.ambari.server.controller.spi.RequestStatus.class);
        org.apache.ambari.server.controller.spi.Resource responseResourceMock = Mockito.mock(org.apache.ambari.server.controller.spi.Resource.class);
        Mockito.when(resourceProviderMock.createResources(requestMock)).thenReturn(requestStatusMock);
        Mockito.when(requestStatusMock.getRequestResource()).thenReturn(responseResourceMock);
        Mockito.when(responseResourceMock.getPropertyValue(Matchers.anyString())).thenReturn(101L);
        org.apache.ambari.server.state.Clusters clustersMock = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        Mockito.when(ambariManagementControllerMock.getClusters()).thenReturn(clustersMock);
        org.apache.ambari.server.state.Cluster clusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        Mockito.when(clustersMock.getCluster("c1")).thenReturn(clusterMock);
        Mockito.when(clusterMock.getHosts(Matchers.eq("AMBARI_METRICS"), Matchers.eq("METRICS_MONITOR"))).thenReturn(java.util.Collections.singleton("h1_fail"));
        org.apache.ambari.server.orm.dao.RequestDAO requestDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.RequestDAO.class);
        org.apache.ambari.server.orm.entities.RequestEntity requestEntityMock = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(requestDAOMock.findByPks(java.util.Collections.singleton(101L), true)).thenReturn(java.util.Collections.singletonList(requestEntityMock));
        Mockito.when(requestEntityMock.getStatus()).thenReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS).thenReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        java.lang.reflect.Field requestDaoField = m_check.getClass().getDeclaredField("requestDAO");
        requestDaoField.setAccessible(true);
        requestDaoField.set(m_check, requestDAOMock);
        Mockito.when(requestEntityMock.getRequestId()).thenReturn(101L);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrcEntityMock = Mockito.mock(org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
        Mockito.when(hostRoleCommandDAOMock.findByRequest(101L, true)).thenReturn(java.util.Collections.singletonList(hrcEntityMock));
        Mockito.when(hrcEntityMock.getStatus()).thenReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        Mockito.when(hrcEntityMock.getHostName()).thenReturn("h1_fail");
        java.lang.reflect.Field hrcDaoField = m_check.getClass().getDeclaredField("hostRoleCommandDAO");
        hrcDaoField.setAccessible(true);
        hrcDaoField.set(m_check, hostRoleCommandDAOMock);
        java.util.Map<java.lang.String, java.lang.String> checkProperties = new java.util.HashMap<>();
        checkProperties.put(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.MIN_HADOOP_SINK_VERSION_PROPERTY_NAME, "2.7.0.0");
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation("c1", false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, m_repositoryVersion, checkProperties, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult check = m_check.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, check.getStatus());
        org.junit.Assert.assertTrue(check.getFailReason().contains("upgrade 'ambari-metrics-hadoop-sink'"));
        org.junit.Assert.assertEquals(check.getFailedOn().size(), 1);
        org.junit.Assert.assertTrue(check.getFailedOn().iterator().next().contains("h1_fail"));
    }
}