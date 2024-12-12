package org.apache.ambari.server.checks;
import org.mockito.Mockito;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ServiceCheckValidityCheckTest {
    private static final java.lang.String CLUSTER_NAME = "cluster1";

    private static final long CLUSTER_ID = 1L;

    private static final java.lang.String SERVICE_NAME = "HDFS";

    private static final long CONFIG_CREATE_TIMESTAMP = 1461518722202L;

    private static final long SERVICE_CHECK_START_TIME = org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CONFIG_CREATE_TIMESTAMP - 2000L;

    private static final java.lang.String SERVICE_COMPONENT_NAME = "service component";

    private org.apache.ambari.server.checks.ServiceCheckValidityCheck serviceCheckValidityCheck;

    private org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    private org.apache.ambari.server.state.Service service;

    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private org.apache.ambari.server.metadata.ActionMetadata actionMetadata;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        final org.apache.ambari.server.state.Clusters clusters = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        service = Mockito.mock(org.apache.ambari.server.state.Service.class);
        serviceConfigDAO = Mockito.mock(org.apache.ambari.server.orm.dao.ServiceConfigDAO.class);
        hostRoleCommandDAO = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        ambariMetaInfo = Mockito.mock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        actionMetadata = new org.apache.ambari.server.metadata.ActionMetadata();
        serviceCheckValidityCheck = new org.apache.ambari.server.checks.ServiceCheckValidityCheck();
        serviceCheckValidityCheck.hostRoleCommandDAOProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostRoleCommandDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.HostRoleCommandDAO get() {
                return hostRoleCommandDAO;
            }
        };
        serviceCheckValidityCheck.serviceConfigDAOProvider = new com.google.inject.Provider<org.apache.ambari.server.orm.dao.ServiceConfigDAO>() {
            @java.lang.Override
            public org.apache.ambari.server.orm.dao.ServiceConfigDAO get() {
                return serviceConfigDAO;
            }
        };
        serviceCheckValidityCheck.clustersProvider = new com.google.inject.Provider<org.apache.ambari.server.state.Clusters>() {
            @java.lang.Override
            public org.apache.ambari.server.state.Clusters get() {
                return clusters;
            }
        };
        serviceCheckValidityCheck.actionMetadataProvider = new com.google.inject.Provider<org.apache.ambari.server.metadata.ActionMetadata>() {
            @java.lang.Override
            public org.apache.ambari.server.metadata.ActionMetadata get() {
                return actionMetadata;
            }
        };
        org.apache.ambari.server.state.Cluster cluster = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        Mockito.when(clusters.getCluster(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_NAME)).thenReturn(cluster);
        Mockito.when(cluster.getClusterId()).thenReturn(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_ID);
        Mockito.when(cluster.getServices()).thenReturn(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME, service));
        Mockito.when(cluster.getCurrentStackVersion()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2"));
        Mockito.when(service.getName()).thenReturn(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME);
        Mockito.when(service.getDesiredStackId()).thenReturn(new org.apache.ambari.server.state.StackId("HDP", "2.2"));
        serviceCheckValidityCheck.ambariMetaInfo = new com.google.inject.Provider<org.apache.ambari.server.api.services.AmbariMetaInfo>() {
            @java.lang.Override
            public org.apache.ambari.server.api.services.AmbariMetaInfo get() {
                return ambariMetaInfo;
            }
        };
        Mockito.when(ambariMetaInfo.isServiceWithNoConfigs(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString(), org.mockito.Mockito.anyString())).thenReturn(false);
        actionMetadata.addServiceCheckAction("HDFS");
    }

    @org.junit.Test
    public void testWithNullCommandDetailAtCommand() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        Mockito.when(serviceComponent.isVersionAdvertised()).thenReturn(true);
        Mockito.when(service.getMaintenanceState()).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        Mockito.when(service.getServiceComponents()).thenReturn(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_COMPONENT_NAME, serviceComponent));
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        serviceConfigEntity.setServiceName(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME);
        serviceConfigEntity.setCreateTimestamp(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CONFIG_CREATE_TIMESTAMP);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO lastServiceCheckDTO1 = new org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO(org.apache.ambari.server.Role.ZOOKEEPER_QUORUM_SERVICE_CHECK.name(), org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_CHECK_START_TIME);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO lastServiceCheckDTO2 = new org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_CHECK_START_TIME);
        Mockito.when(serviceConfigDAO.getLastServiceConfig(Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_ID), Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME))).thenReturn(serviceConfigEntity);
        Mockito.when(hostRoleCommandDAO.getLatestServiceChecksByRole(Matchers.any(java.lang.Long.class))).thenReturn(java.util.Arrays.asList(lastServiceCheckDTO1, lastServiceCheckDTO2));
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        try {
            org.apache.ambari.spi.upgrade.UpgradeCheckResult result = serviceCheckValidityCheck.perform(request);
            org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
        } catch (java.lang.NullPointerException ex) {
            org.junit.Assert.fail("serviceCheckValidityCheck failed due to null at start_time were not handled");
        }
    }

    @org.junit.Test
    public void testFailWhenServiceWithOutdatedServiceCheckExists() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        Mockito.when(serviceComponent.isVersionAdvertised()).thenReturn(true);
        Mockito.when(service.getMaintenanceState()).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        Mockito.when(service.getServiceComponents()).thenReturn(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_COMPONENT_NAME, serviceComponent));
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        serviceConfigEntity.setServiceName(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME);
        serviceConfigEntity.setCreateTimestamp(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CONFIG_CREATE_TIMESTAMP);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO lastServiceCheckDTO = new org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_CHECK_START_TIME);
        Mockito.when(serviceConfigDAO.getLastServiceConfig(Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_ID), Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME))).thenReturn(serviceConfigEntity);
        Mockito.when(hostRoleCommandDAO.getLatestServiceChecksByRole(Matchers.any(java.lang.Long.class))).thenReturn(java.util.Collections.singletonList(lastServiceCheckDTO));
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = serviceCheckValidityCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testFailWhenServiceWithNoServiceCheckExists() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        Mockito.when(serviceComponent.isVersionAdvertised()).thenReturn(true);
        Mockito.when(service.getMaintenanceState()).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        Mockito.when(service.getServiceComponents()).thenReturn(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_COMPONENT_NAME, serviceComponent));
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        serviceConfigEntity.setServiceName(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME);
        serviceConfigEntity.setCreateTimestamp(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CONFIG_CREATE_TIMESTAMP);
        Mockito.when(serviceConfigDAO.getLastServiceConfig(Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_ID), Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME))).thenReturn(serviceConfigEntity);
        Mockito.when(hostRoleCommandDAO.getLatestServiceChecksByRole(Matchers.any(java.lang.Long.class))).thenReturn(java.util.Collections.<org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO>emptyList());
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = serviceCheckValidityCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testFailWhenServiceWithOutdatedServiceCheckExistsRepeated() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        Mockito.when(serviceComponent.isVersionAdvertised()).thenReturn(true);
        Mockito.when(service.getMaintenanceState()).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        Mockito.when(service.getServiceComponents()).thenReturn(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_COMPONENT_NAME, serviceComponent));
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        serviceConfigEntity.setServiceName(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME);
        serviceConfigEntity.setCreateTimestamp(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CONFIG_CREATE_TIMESTAMP);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO lastServiceCheckDTO1 = new org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_CHECK_START_TIME);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO lastServiceCheckDTO2 = new org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO(org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name(), org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CONFIG_CREATE_TIMESTAMP - 1L);
        Mockito.when(serviceConfigDAO.getLastServiceConfig(Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_ID), Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME))).thenReturn(serviceConfigEntity);
        Mockito.when(hostRoleCommandDAO.getLatestServiceChecksByRole(Matchers.any(java.lang.Long.class))).thenReturn(java.util.Arrays.asList(lastServiceCheckDTO1, lastServiceCheckDTO2));
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = serviceCheckValidityCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }

    @org.junit.Test
    public void testPassWhenSimilarlyNamedServiceIsOutdated() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.ServiceComponent serviceComponent = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        Mockito.when(serviceComponent.isVersionAdvertised()).thenReturn(true);
        Mockito.when(service.getMaintenanceState()).thenReturn(org.apache.ambari.server.state.MaintenanceState.OFF);
        Mockito.when(service.getServiceComponents()).thenReturn(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_COMPONENT_NAME, serviceComponent));
        org.apache.ambari.server.orm.entities.ServiceConfigEntity serviceConfigEntity = new org.apache.ambari.server.orm.entities.ServiceConfigEntity();
        serviceConfigEntity.setServiceName(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME);
        serviceConfigEntity.setCreateTimestamp(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CONFIG_CREATE_TIMESTAMP);
        java.lang.String hdfsRole = org.apache.ambari.server.Role.HDFS_SERVICE_CHECK.name();
        java.lang.String hdfs2Role = hdfsRole.replace("HDFS", "HDFS2");
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO lastServiceCheckDTO1 = new org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO(hdfsRole, org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_CHECK_START_TIME);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO lastServiceCheckDTO2 = new org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO(hdfs2Role, org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CONFIG_CREATE_TIMESTAMP - 1L);
        Mockito.when(serviceConfigDAO.getLastServiceConfig(Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_ID), Matchers.eq(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.SERVICE_NAME))).thenReturn(serviceConfigEntity);
        Mockito.when(hostRoleCommandDAO.getLatestServiceChecksByRole(Matchers.any(java.lang.Long.class))).thenReturn(java.util.Arrays.asList(lastServiceCheckDTO1, lastServiceCheckDTO2));
        org.apache.ambari.spi.ClusterInformation clusterInformation = new org.apache.ambari.spi.ClusterInformation(org.apache.ambari.server.checks.ServiceCheckValidityCheckTest.CLUSTER_NAME, false, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckRequest request = new org.apache.ambari.spi.upgrade.UpgradeCheckRequest(clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, null, null, null);
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = serviceCheckValidityCheck.perform(request);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL, result.getStatus());
    }
}