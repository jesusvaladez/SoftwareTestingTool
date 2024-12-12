package org.apache.ambari.server.controller.logging;
import org.easymock.Capture;
import org.easymock.EasyMockSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
public class LoggingSearchPropertyProviderTest {
    @org.junit.Before
    @org.junit.After
    public void clearAuthentication() {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testBasicCallAsAdministrator() throws java.lang.Exception {
        testBasicCall(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), true);
    }

    @org.junit.Test
    public void testBasicCallAsClusterAdministrator() throws java.lang.Exception {
        testBasicCall(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), true);
    }

    @org.junit.Test
    public void testBasicCallAsClusterOperator() throws java.lang.Exception {
        testBasicCall(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), true);
    }

    @org.junit.Test
    public void testBasicCallAsServiceAdministrator() throws java.lang.Exception {
        testBasicCall(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), true);
    }

    @org.junit.Test
    public void testBasicCallAsServiceOperator() throws java.lang.Exception {
        testBasicCall(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator(), false);
    }

    @org.junit.Test
    public void testBasicCallAsClusterUser() throws java.lang.Exception {
        testBasicCall(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), false);
    }

    private void testBasicCall(org.springframework.security.core.Authentication authentication, boolean authorizedForLogSearch) throws java.lang.Exception {
        final java.lang.String expectedLogFilePath = "/var/log/hdfs/hdfs_namenode.log";
        final java.lang.String expectedSearchEnginePath = "/api/v1/clusters/clusterone/logging/searchEngine";
        final java.lang.String expectedAmbariURL = "http://c6401.ambari.apache.org:8080";
        final java.lang.String expectedStackName = "HDP";
        final java.lang.String expectedStackVersion = "2.4";
        final java.lang.String expectedComponentName = "NAMENODE";
        final java.lang.String expectedServiceName = "HDFS";
        final java.lang.String expectedLogSearchComponentName = "hdfs_namenode";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.spi.Resource resourceMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"))).andReturn(expectedComponentName).atLeastOnce();
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"))).andReturn("c6401.ambari.apache.org").atLeastOnce();
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"))).andReturn("clusterone").atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.controller.logging.HostComponentLoggingInfo> captureLogInfo = org.easymock.Capture.newInstance();
        if (authorizedForLogSearch) {
            resourceMock.setProperty(EasyMock.eq("logging"), EasyMock.capture(captureLogInfo));
        }
        org.apache.ambari.server.controller.logging.LogLevelQueryResponse levelQueryResponse = new org.apache.ambari.server.controller.logging.LogLevelQueryResponse();
        levelQueryResponse.setTotalCount("3");
        java.util.List<org.apache.ambari.server.controller.logging.NameValuePair> testListOfLogLevels = new java.util.LinkedList<>();
        testListOfLogLevels.add(new org.apache.ambari.server.controller.logging.NameValuePair("ERROR", "150"));
        testListOfLogLevels.add(new org.apache.ambari.server.controller.logging.NameValuePair("WARN", "500"));
        testListOfLogLevels.add(new org.apache.ambari.server.controller.logging.NameValuePair("INFO", "2200"));
        levelQueryResponse.setNameValueList(testListOfLogLevels);
        org.apache.ambari.server.controller.spi.Request requestMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.spi.Predicate predicateMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Predicate.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService dataRetrievalServiceMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.class);
        if (authorizedForLogSearch) {
            org.apache.ambari.server.controller.logging.LoggingRequestHelper loggingRequestHelperMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelper.class);
            org.apache.ambari.server.api.services.AmbariMetaInfo metaInfoMock = mockSupport.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            org.apache.ambari.server.state.StackId stackIdMock = mockSupport.createMock(org.apache.ambari.server.state.StackId.class);
            org.apache.ambari.server.state.ComponentInfo componentInfoMock = mockSupport.createMock(org.apache.ambari.server.state.ComponentInfo.class);
            org.apache.ambari.server.state.LogDefinition logDefinitionMock = mockSupport.createMock(org.apache.ambari.server.state.LogDefinition.class);
            org.apache.ambari.server.state.Service serviceMock = mockSupport.createNiceMock(org.apache.ambari.server.state.Service.class);
            EasyMock.expect(controllerMock.findServiceName(clusterMock, expectedComponentName)).andReturn(expectedServiceName).atLeastOnce();
            EasyMock.expect(clusterMock.getService(expectedServiceName)).andReturn(serviceMock).anyTimes();
            EasyMock.expect(serviceMock.getDesiredStackId()).andReturn(stackIdMock).anyTimes();
            EasyMock.expect(controllerMock.getAmbariServerURI(expectedSearchEnginePath)).andReturn(expectedAmbariURL + expectedSearchEnginePath).atLeastOnce();
            EasyMock.expect(controllerMock.getAmbariMetaInfo()).andReturn(metaInfoMock).atLeastOnce();
            EasyMock.expect(metaInfoMock.getComponent(expectedStackName, expectedStackVersion, expectedServiceName, expectedComponentName)).andReturn(componentInfoMock).atLeastOnce();
            EasyMock.expect(stackIdMock.getStackName()).andReturn(expectedStackName).atLeastOnce();
            EasyMock.expect(stackIdMock.getStackVersion()).andReturn(expectedStackVersion).atLeastOnce();
            EasyMock.expect(componentInfoMock.getLogs()).andReturn(java.util.Collections.singletonList(logDefinitionMock)).atLeastOnce();
            EasyMock.expect(logDefinitionMock.getLogId()).andReturn(expectedLogSearchComponentName).atLeastOnce();
            EasyMock.expect(dataRetrievalServiceMock.getLogFileNames(expectedLogSearchComponentName, "c6401.ambari.apache.org", "clusterone")).andReturn(java.util.Collections.singleton(expectedLogFilePath)).atLeastOnce();
            EasyMock.expect(dataRetrievalServiceMock.getLogFileTailURI(expectedAmbariURL + expectedSearchEnginePath, expectedLogSearchComponentName, "c6401.ambari.apache.org", "clusterone")).andReturn("").atLeastOnce();
            EasyMock.expect(loggingRequestHelperFactoryMock.getHelper(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class), EasyMock.anyObject(java.lang.String.class))).andReturn(loggingRequestHelperMock).atLeastOnce();
        }
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).atLeastOnce();
        EasyMock.expect(clustersMock.getCluster("clusterone")).andReturn(clusterMock).atLeastOnce();
        EasyMock.expect(clusterMock.getResourceId()).andReturn(4L).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider propertyProvider = new org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider();
        propertyProvider.setAmbariManagementController(controllerMock);
        propertyProvider.setLogSearchDataRetrievalService(dataRetrievalServiceMock);
        propertyProvider.setLoggingRequestHelperFactory(loggingRequestHelperFactoryMock);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> returnedResources = propertyProvider.populateResources(java.util.Collections.singleton(resourceMock), requestMock, predicateMock);
        org.junit.Assert.assertEquals("Returned resource set was of an incorrect size", 1, returnedResources.size());
        if (authorizedForLogSearch) {
            org.apache.ambari.server.controller.logging.HostComponentLoggingInfo returnedLogInfo = captureLogInfo.getValue();
            org.junit.Assert.assertNotNull("Returned log info should not be null", returnedLogInfo);
            org.junit.Assert.assertEquals("Returned component was not the correct name", "hdfs_namenode", returnedLogInfo.getComponentName());
            org.junit.Assert.assertEquals("Returned list of log file names for this component was incorrect", 1, returnedLogInfo.getListOfLogFileDefinitions().size());
            org.apache.ambari.server.controller.logging.LogFileDefinitionInfo definitionInfo = returnedLogInfo.getListOfLogFileDefinitions().get(0);
            org.junit.Assert.assertEquals("Incorrect log file type was found", org.apache.ambari.server.controller.logging.LogFileType.SERVICE, definitionInfo.getLogFileType());
            org.junit.Assert.assertEquals("Incorrect log file path found", expectedLogFilePath, definitionInfo.getLogFileName());
            org.junit.Assert.assertEquals("Incorrect URL path to searchEngine", expectedAmbariURL + expectedSearchEnginePath, definitionInfo.getSearchEngineURL());
            org.junit.Assert.assertNull(returnedLogInfo.getListOfLogLevels());
        } else {
            org.junit.Assert.assertFalse("Unauthorized user should not be able to retrieve log info", captureLogInfo.hasCaptured());
        }
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testBasicCallWithNullTailLogURIReturnedAsAdministrator() throws java.lang.Exception {
        testBasicCallWithNullTailLogURIReturned(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), true);
    }

    @org.junit.Test
    public void testBasicCallWithNullTailLogURIReturnedAsClusterAdministrator() throws java.lang.Exception {
        testBasicCallWithNullTailLogURIReturned(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), true);
    }

    @org.junit.Test
    public void testBasicCallWithNullTailLogURIReturnedAsClusterOperator() throws java.lang.Exception {
        testBasicCallWithNullTailLogURIReturned(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), true);
    }

    @org.junit.Test
    public void testBasicCallWithNullTailLogURIReturnedAsServiceAdministrator() throws java.lang.Exception {
        testBasicCallWithNullTailLogURIReturned(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), true);
    }

    @org.junit.Test
    public void testBasicCallWithNullTailLogURIReturnedAsServiceOperator() throws java.lang.Exception {
        testBasicCallWithNullTailLogURIReturned(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator(), false);
    }

    @org.junit.Test
    public void testBasicCallWithNullTailLogURIReturnedAsClusterUser() throws java.lang.Exception {
        testBasicCallWithNullTailLogURIReturned(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), false);
    }

    private void testBasicCallWithNullTailLogURIReturned(org.springframework.security.core.Authentication authentication, boolean authorizedForLogSearch) throws java.lang.Exception {
        final java.lang.String expectedLogFilePath = "/var/log/hdfs/hdfs_namenode.log";
        final java.lang.String expectedSearchEnginePath = "/api/v1/clusters/clusterone/logging/searchEngine";
        final java.lang.String expectedAmbariURL = "http://c6401.ambari.apache.org:8080";
        final java.lang.String expectedStackName = "HDP";
        final java.lang.String expectedStackVersion = "2.4";
        final java.lang.String expectedComponentName = "NAMENODE";
        final java.lang.String expectedServiceName = "HDFS";
        final java.lang.String expectedLogSearchComponentName = "hdfs_namenode";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.spi.Resource resourceMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"))).andReturn(expectedComponentName).atLeastOnce();
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"))).andReturn("c6401.ambari.apache.org").atLeastOnce();
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"))).andReturn("clusterone").atLeastOnce();
        org.easymock.Capture<org.apache.ambari.server.controller.logging.HostComponentLoggingInfo> captureLogInfo = org.easymock.Capture.newInstance();
        if (authorizedForLogSearch) {
            resourceMock.setProperty(EasyMock.eq("logging"), EasyMock.capture(captureLogInfo));
        }
        org.apache.ambari.server.controller.logging.LogLevelQueryResponse levelQueryResponse = new org.apache.ambari.server.controller.logging.LogLevelQueryResponse();
        levelQueryResponse.setTotalCount("3");
        java.util.List<org.apache.ambari.server.controller.logging.NameValuePair> testListOfLogLevels = new java.util.LinkedList<>();
        testListOfLogLevels.add(new org.apache.ambari.server.controller.logging.NameValuePair("ERROR", "150"));
        testListOfLogLevels.add(new org.apache.ambari.server.controller.logging.NameValuePair("WARN", "500"));
        testListOfLogLevels.add(new org.apache.ambari.server.controller.logging.NameValuePair("INFO", "2200"));
        levelQueryResponse.setNameValueList(testListOfLogLevels);
        org.apache.ambari.server.controller.spi.Request requestMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.spi.Predicate predicateMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Predicate.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.LogDefinition logDefinitionMock = mockSupport.createMock(org.apache.ambari.server.state.LogDefinition.class);
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService dataRetrievalServiceMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        if (authorizedForLogSearch) {
            org.apache.ambari.server.api.services.AmbariMetaInfo metaInfoMock = mockSupport.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            org.apache.ambari.server.state.StackId stackIdMock = mockSupport.createMock(org.apache.ambari.server.state.StackId.class);
            org.apache.ambari.server.state.ComponentInfo componentInfoMock = mockSupport.createMock(org.apache.ambari.server.state.ComponentInfo.class);
            org.apache.ambari.server.controller.logging.LoggingRequestHelper loggingRequestHelperMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelper.class);
            org.apache.ambari.server.state.Service serviceMock = mockSupport.createNiceMock(org.apache.ambari.server.state.Service.class);
            EasyMock.expect(controllerMock.findServiceName(clusterMock, expectedComponentName)).andReturn(expectedServiceName).atLeastOnce();
            EasyMock.expect(clusterMock.getService(expectedServiceName)).andReturn(serviceMock).anyTimes();
            EasyMock.expect(serviceMock.getDesiredStackId()).andReturn(stackIdMock).anyTimes();
            EasyMock.expect(dataRetrievalServiceMock.getLogFileNames(expectedLogSearchComponentName, "c6401.ambari.apache.org", "clusterone")).andReturn(java.util.Collections.singleton(expectedLogFilePath)).atLeastOnce();
            EasyMock.expect(dataRetrievalServiceMock.getLogFileTailURI(expectedAmbariURL + expectedSearchEnginePath, expectedLogSearchComponentName, "c6401.ambari.apache.org", "clusterone")).andReturn(null).atLeastOnce();
            EasyMock.expect(loggingRequestHelperFactoryMock.getHelper(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class), EasyMock.anyObject(java.lang.String.class))).andReturn(loggingRequestHelperMock).atLeastOnce();
            EasyMock.expect(controllerMock.getAmbariServerURI(expectedSearchEnginePath)).andReturn(expectedAmbariURL + expectedSearchEnginePath).atLeastOnce();
            EasyMock.expect(controllerMock.getAmbariMetaInfo()).andReturn(metaInfoMock).atLeastOnce();
            EasyMock.expect(metaInfoMock.getComponent(expectedStackName, expectedStackVersion, expectedServiceName, expectedComponentName)).andReturn(componentInfoMock).atLeastOnce();
            EasyMock.expect(componentInfoMock.getLogs()).andReturn(java.util.Collections.singletonList(logDefinitionMock)).atLeastOnce();
            EasyMock.expect(logDefinitionMock.getLogId()).andReturn(expectedLogSearchComponentName).atLeastOnce();
            EasyMock.expect(stackIdMock.getStackName()).andReturn(expectedStackName).atLeastOnce();
            EasyMock.expect(stackIdMock.getStackVersion()).andReturn(expectedStackVersion).atLeastOnce();
        }
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).atLeastOnce();
        EasyMock.expect(clustersMock.getCluster("clusterone")).andReturn(clusterMock).atLeastOnce();
        EasyMock.expect(clusterMock.getResourceId()).andReturn(4L).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider propertyProvider = new org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider();
        propertyProvider.setAmbariManagementController(controllerMock);
        propertyProvider.setLogSearchDataRetrievalService(dataRetrievalServiceMock);
        propertyProvider.setLoggingRequestHelperFactory(loggingRequestHelperFactoryMock);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> returnedResources = propertyProvider.populateResources(java.util.Collections.singleton(resourceMock), requestMock, predicateMock);
        org.junit.Assert.assertEquals("Returned resource set was of an incorrect size", 1, returnedResources.size());
        if (authorizedForLogSearch) {
            org.apache.ambari.server.controller.logging.HostComponentLoggingInfo returnedLogInfo = captureLogInfo.getValue();
            org.junit.Assert.assertNotNull("Returned log info should not be null", returnedLogInfo);
            org.junit.Assert.assertEquals("Returned component was not the correct name", "hdfs_namenode", returnedLogInfo.getComponentName());
            org.junit.Assert.assertEquals("Returned list of log file names for this component was incorrect", 0, returnedLogInfo.getListOfLogFileDefinitions().size());
            org.junit.Assert.assertNull(returnedLogInfo.getListOfLogLevels());
        } else {
            org.junit.Assert.assertFalse("Unauthorized user should not be able to retrieve log info", captureLogInfo.hasCaptured());
        }
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testCheckWhenLogSearchNotAvailableAsAdministrator() throws java.lang.Exception {
        testCheckWhenLogSearchNotAvailable(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator(), true);
    }

    @org.junit.Test
    public void testCheckWhenLogSearchNotAvailableAsClusterAdministrator() throws java.lang.Exception {
        testCheckWhenLogSearchNotAvailable(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterAdministrator(), true);
    }

    @org.junit.Test
    public void testCheckWhenLogSearchNotAvailableAsClusterOperator() throws java.lang.Exception {
        testCheckWhenLogSearchNotAvailable(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterOperator(), true);
    }

    @org.junit.Test
    public void testCheckWhenLogSearchNotAvailableAsServiceAdministrator() throws java.lang.Exception {
        testCheckWhenLogSearchNotAvailable(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceAdministrator(), true);
    }

    @org.junit.Test
    public void testCheckWhenLogSearchNotAvailableAsServiceOperator() throws java.lang.Exception {
        testCheckWhenLogSearchNotAvailable(org.apache.ambari.server.security.TestAuthenticationFactory.createServiceOperator(), false);
    }

    @org.junit.Test
    public void testCheckWhenLogSearchNotAvailableAsClusterUser() throws java.lang.Exception {
        testCheckWhenLogSearchNotAvailable(org.apache.ambari.server.security.TestAuthenticationFactory.createClusterUser(), false);
    }

    private void testCheckWhenLogSearchNotAvailable(org.springframework.security.core.Authentication authentication, boolean authorizedForLogSearch) throws java.lang.Exception {
        final java.lang.String expectedStackName = "HDP";
        final java.lang.String expectedStackVersion = "2.4";
        final java.lang.String expectedComponentName = "NAMENODE";
        final java.lang.String expectedServiceName = "HDFS";
        final java.lang.String expectedLogSearchComponentName = "hdfs_namenode";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.spi.Resource resourceMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Resource.class);
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"))).andReturn(expectedComponentName).atLeastOnce();
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"))).andReturn("c6401.ambari.apache.org").atLeastOnce();
        EasyMock.expect(resourceMock.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"))).andReturn("clusterone").atLeastOnce();
        org.apache.ambari.server.controller.spi.Request requestMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Request.class);
        org.apache.ambari.server.controller.spi.Predicate predicateMock = mockSupport.createMock(org.apache.ambari.server.controller.spi.Predicate.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clustersMock = mockSupport.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService dataRetrievalServiceMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        if (authorizedForLogSearch) {
            org.apache.ambari.server.api.services.AmbariMetaInfo metaInfoMock = mockSupport.createMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
            org.apache.ambari.server.state.StackId stackIdMock = mockSupport.createMock(org.apache.ambari.server.state.StackId.class);
            org.apache.ambari.server.state.LogDefinition logDefinitionMock = mockSupport.createMock(org.apache.ambari.server.state.LogDefinition.class);
            org.apache.ambari.server.state.ComponentInfo componentInfoMock = mockSupport.createMock(org.apache.ambari.server.state.ComponentInfo.class);
            org.apache.ambari.server.controller.logging.LoggingRequestHelper loggingRequestHelperMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelper.class);
            org.apache.ambari.server.state.Service serviceMock = mockSupport.createNiceMock(org.apache.ambari.server.state.Service.class);
            EasyMock.expect(controllerMock.findServiceName(clusterMock, expectedComponentName)).andReturn(expectedServiceName).atLeastOnce();
            EasyMock.expect(clusterMock.getService(expectedServiceName)).andReturn(serviceMock).anyTimes();
            EasyMock.expect(serviceMock.getDesiredStackId()).andReturn(stackIdMock).anyTimes();
            EasyMock.expect(controllerMock.getAmbariMetaInfo()).andReturn(metaInfoMock).atLeastOnce();
            EasyMock.expect(stackIdMock.getStackName()).andReturn(expectedStackName).atLeastOnce();
            EasyMock.expect(stackIdMock.getStackVersion()).andReturn(expectedStackVersion).atLeastOnce();
            EasyMock.expect(metaInfoMock.getComponent(expectedStackName, expectedStackVersion, expectedServiceName, expectedComponentName)).andReturn(componentInfoMock).atLeastOnce();
            EasyMock.expect(dataRetrievalServiceMock.getLogFileNames(expectedLogSearchComponentName, "c6401.ambari.apache.org", "clusterone")).andReturn(null).atLeastOnce();
            EasyMock.expect(componentInfoMock.getLogs()).andReturn(java.util.Collections.singletonList(logDefinitionMock)).atLeastOnce();
            EasyMock.expect(logDefinitionMock.getLogId()).andReturn(expectedLogSearchComponentName).atLeastOnce();
            EasyMock.expect(loggingRequestHelperFactoryMock.getHelper(EasyMock.anyObject(org.apache.ambari.server.controller.AmbariManagementController.class), EasyMock.anyObject(java.lang.String.class))).andReturn(loggingRequestHelperMock).atLeastOnce();
        }
        EasyMock.expect(controllerMock.getClusters()).andReturn(clustersMock).atLeastOnce();
        EasyMock.expect(clustersMock.getCluster("clusterone")).andReturn(clusterMock).atLeastOnce();
        EasyMock.expect(clusterMock.getResourceId()).andReturn(4L).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.security.authorization.AuthorizationHelperInitializer.viewInstanceDAOReturningNull();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
        org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider propertyProvider = new org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider();
        propertyProvider.setAmbariManagementController(controllerMock);
        propertyProvider.setLogSearchDataRetrievalService(dataRetrievalServiceMock);
        propertyProvider.setLoggingRequestHelperFactory(loggingRequestHelperFactoryMock);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> returnedResources = propertyProvider.populateResources(java.util.Collections.singleton(resourceMock), requestMock, predicateMock);
        org.junit.Assert.assertEquals("Returned resource set was of an incorrect size", 1, returnedResources.size());
        org.junit.Assert.assertSame("Returned resource was not the expected instance.", resourceMock, returnedResources.iterator().next());
        mockSupport.verifyAll();
    }
}