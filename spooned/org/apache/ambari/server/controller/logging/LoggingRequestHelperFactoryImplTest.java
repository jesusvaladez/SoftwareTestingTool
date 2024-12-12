package org.apache.ambari.server.controller.logging;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class LoggingRequestHelperFactoryImplTest {
    @org.junit.Test
    public void testHelperCreation() throws java.lang.Exception {
        final java.lang.String expectedClusterName = "testclusterone";
        final java.lang.String expectedHostName = "c6410.ambari.apache.org";
        final java.lang.String expectedPortNumber = "61889";
        final int expectedConnectTimeout = 3000;
        final int expectedReadTimeout = 3000;
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config logSearchEnvConfig = mockSupport.createMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHostMock = mockSupport.createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreServiceMock = mockSupport.createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        org.apache.ambari.server.configuration.Configuration serverConfigMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        java.util.Map<java.lang.String, java.lang.String> testProperties = new java.util.HashMap<>();
        testProperties.put("logsearch.http.port", expectedPortNumber);
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).atLeastOnce();
        EasyMock.expect(controllerMock.getCredentialStoreService()).andReturn(credentialStoreServiceMock).atLeastOnce();
        EasyMock.expect(clustersMock.getCluster(expectedClusterName)).andReturn(clusterMock).atLeastOnce();
        EasyMock.expect(clusterMock.getDesiredConfigByType("logsearch-properties")).andReturn(logSearchEnvConfig).atLeastOnce();
        EasyMock.expect(clusterMock.getServiceComponentHosts("LOGSEARCH", "LOGSEARCH_SERVER")).andReturn(java.util.Collections.singletonList(serviceComponentHostMock)).atLeastOnce();
        EasyMock.expect(clusterMock.getServices()).andReturn(java.util.Collections.singletonMap("LOGSEARCH", ((org.apache.ambari.server.state.Service) (null)))).atLeastOnce();
        EasyMock.expect(logSearchEnvConfig.getProperties()).andReturn(testProperties).atLeastOnce();
        EasyMock.expect(serviceComponentHostMock.getHostName()).andReturn(expectedHostName).atLeastOnce();
        EasyMock.expect(serviceComponentHostMock.getState()).andReturn(org.apache.ambari.server.state.State.STARTED).atLeastOnce();
        EasyMock.expect(serverConfigMock.getLogSearchPortalExternalAddress()).andReturn("");
        EasyMock.expect(serverConfigMock.getLogSearchPortalConnectTimeout()).andReturn(expectedConnectTimeout);
        EasyMock.expect(serverConfigMock.getLogSearchPortalReadTimeout()).andReturn(expectedReadTimeout);
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactory = new org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl();
        ((org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl) (helperFactory)).setAmbariServerConfiguration(serverConfigMock);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = helperFactory.getHelper(controllerMock, expectedClusterName);
        org.junit.Assert.assertNotNull("LoggingRequestHelper object returned by the factory was null", helper);
        org.junit.Assert.assertTrue("Helper created was not of the expected type", helper instanceof org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl);
        org.junit.Assert.assertEquals("Helper factory did not set the expected connect timeout on the helper instance", expectedConnectTimeout, ((org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl) (helper)).getLogSearchConnectTimeoutInMilliseconds());
        org.junit.Assert.assertEquals("Helper factory did not set the expected read timeout on the helper instance", expectedReadTimeout, ((org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl) (helper)).getLogSearchReadTimeoutInMilliseconds());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testHelperCreationLogSearchServerNotStarted() throws java.lang.Exception {
        final java.lang.String expectedClusterName = "testclusterone";
        final java.lang.String expectedHostName = "c6410.ambari.apache.org";
        final java.lang.String expectedPortNumber = "61889";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config logSearchEnvConfig = mockSupport.createMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHostMock = mockSupport.createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.configuration.Configuration serverConfigMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        java.util.Map<java.lang.String, java.lang.String> testProperties = new java.util.HashMap<>();
        testProperties.put("logsearch.http.port", expectedPortNumber);
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).atLeastOnce();
        EasyMock.expect(clustersMock.getCluster(expectedClusterName)).andReturn(clusterMock).atLeastOnce();
        EasyMock.expect(clusterMock.getDesiredConfigByType("logsearch-properties")).andReturn(logSearchEnvConfig).atLeastOnce();
        EasyMock.expect(clusterMock.getServiceComponentHosts("LOGSEARCH", "LOGSEARCH_SERVER")).andReturn(java.util.Collections.singletonList(serviceComponentHostMock)).atLeastOnce();
        EasyMock.expect(clusterMock.getServices()).andReturn(java.util.Collections.singletonMap("LOGSEARCH", ((org.apache.ambari.server.state.Service) (null)))).atLeastOnce();
        EasyMock.expect(serverConfigMock.getLogSearchPortalExternalAddress()).andReturn("");
        EasyMock.expect(serviceComponentHostMock.getState()).andReturn(org.apache.ambari.server.state.State.INSTALLED).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactory = new org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl();
        ((org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl) (helperFactory)).setAmbariServerConfiguration(serverConfigMock);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = helperFactory.getHelper(controllerMock, expectedClusterName);
        org.junit.Assert.assertNull("LoggingRequestHelper object returned by the factory should have been null", helper);
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testHelperCreationWithNoLogSearchServersAvailable() throws java.lang.Exception {
        final java.lang.String expectedClusterName = "testclusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config logSearchEnvConfig = mockSupport.createMock(org.apache.ambari.server.state.Config.class);
        org.apache.ambari.server.configuration.Configuration serverConfigMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).atLeastOnce();
        EasyMock.expect(clustersMock.getCluster(expectedClusterName)).andReturn(clusterMock).atLeastOnce();
        EasyMock.expect(clusterMock.getDesiredConfigByType("logsearch-properties")).andReturn(logSearchEnvConfig).atLeastOnce();
        EasyMock.expect(clusterMock.getServiceComponentHosts("LOGSEARCH", "LOGSEARCH_SERVER")).andReturn(java.util.Collections.emptyList()).atLeastOnce();
        EasyMock.expect(clusterMock.getServices()).andReturn(java.util.Collections.singletonMap("LOGSEARCH", ((org.apache.ambari.server.state.Service) (null)))).atLeastOnce();
        EasyMock.expect(serverConfigMock.getLogSearchPortalExternalAddress()).andReturn("");
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactory = new org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl();
        ((org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl) (helperFactory)).setAmbariServerConfiguration(serverConfigMock);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = helperFactory.getHelper(controllerMock, expectedClusterName);
        org.junit.Assert.assertNull("LoggingRequestHelper object returned by the factory should have been null", helper);
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testHelperCreationWithNoLogSearchServiceDeployed() throws java.lang.Exception {
        final java.lang.String expectedClusterName = "testclusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.configuration.Configuration serverConfigMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).atLeastOnce();
        EasyMock.expect(clustersMock.getCluster(expectedClusterName)).andReturn(clusterMock).atLeastOnce();
        EasyMock.expect(clusterMock.getServices()).andReturn(java.util.Collections.singletonMap("HDFS", ((org.apache.ambari.server.state.Service) (null)))).atLeastOnce();
        EasyMock.expect(serverConfigMock.getLogSearchPortalExternalAddress()).andReturn("");
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactory = new org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl();
        ((org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl) (helperFactory)).setAmbariServerConfiguration(serverConfigMock);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = helperFactory.getHelper(controllerMock, expectedClusterName);
        org.junit.Assert.assertNull("LoggingRequestHelper object returned by the factory should have been null", helper);
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testHelperCreationWithExternalLogSearchPortal() throws java.lang.Exception {
        final java.lang.String expectedClusterName = "testclusterone";
        final int expectedConnectTimeout = 3000;
        final int expectedReadTimeout = 3000;
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.configuration.Configuration serverConfigMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).atLeastOnce();
        EasyMock.expect(clustersMock.getCluster(expectedClusterName)).andReturn(clusterMock).atLeastOnce();
        EasyMock.expect(serverConfigMock.getLogSearchPortalExternalAddress()).andReturn("http://logsearch.org:61888").times(2);
        EasyMock.expect(controllerMock.getCredentialStoreService()).andReturn(null);
        EasyMock.expect(serverConfigMock.getLogSearchPortalConnectTimeout()).andReturn(expectedConnectTimeout);
        EasyMock.expect(serverConfigMock.getLogSearchPortalReadTimeout()).andReturn(expectedReadTimeout);
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactory = new org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl();
        ((org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl) (helperFactory)).setAmbariServerConfiguration(serverConfigMock);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = helperFactory.getHelper(controllerMock, expectedClusterName);
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testHelperCreationWithNoAmbariServerConfiguration() throws java.lang.Exception {
        final java.lang.String expectedClusterName = "testclusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactory = new org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl();
        ((org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl) (helperFactory)).setAmbariServerConfiguration(null);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = helperFactory.getHelper(controllerMock, expectedClusterName);
        org.junit.Assert.assertNull("LoggingRequestHelper object returned by the factory should have been null", helper);
        mockSupport.verifyAll();
    }
}