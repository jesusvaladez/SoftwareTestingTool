package org.apache.ambari.server.controller.logging;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
public class LogSearchDataRetrievalServiceTest {
    @org.junit.Test
    public void testGetTailFileWhenHelperIsAvailable() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        final java.lang.String expectedResultURI = "http://localhost/test/result";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helperMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelper.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(helperFactoryMock.getHelper(null, expectedClusterName)).andReturn(helperMock);
        EasyMock.expect(helperMock.createLogFileTailURI("http://localhost", expectedComponentName, expectedHostName)).andReturn(expectedResultURI);
        EasyMock.expect(configurationMock.getLogSearchMetadataCacheExpireTimeout()).andReturn(1).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService retrievalService = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService();
        retrievalService.setLoggingRequestHelperFactory(helperFactoryMock);
        retrievalService.setConfiguration(configurationMock);
        retrievalService.doStart();
        java.lang.String resultTailFileURI = retrievalService.getLogFileTailURI("http://localhost", expectedComponentName, expectedHostName, expectedClusterName);
        org.junit.Assert.assertEquals("TailFileURI was not returned as expected", expectedResultURI, resultTailFileURI);
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testGetTailFileWhenRequestHelperIsNull() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(helperFactoryMock.getHelper(null, expectedClusterName)).andReturn(null);
        EasyMock.expect(configurationMock.getLogSearchMetadataCacheExpireTimeout()).andReturn(1).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService retrievalService = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService();
        retrievalService.setLoggingRequestHelperFactory(helperFactoryMock);
        retrievalService.setConfiguration(configurationMock);
        retrievalService.doStart();
        java.lang.String resultTailFileURI = retrievalService.getLogFileTailURI("http://localhost", expectedComponentName, expectedHostName, expectedClusterName);
        org.junit.Assert.assertNull("TailFileURI should be null in this case", resultTailFileURI);
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testGetLogFileNamesDefault() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        java.util.concurrent.Executor executorMock = mockSupport.createMock(java.util.concurrent.Executor.class);
        com.google.inject.Injector injectorMock = mockSupport.createMock(com.google.inject.Injector.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        executorMock.execute(EasyMock.isA(org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable.class));
        EasyMock.expectLastCall().once();
        EasyMock.expect(injectorMock.getInstance(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class)).andReturn(helperFactoryMock);
        EasyMock.expect(configurationMock.getLogSearchMetadataCacheExpireTimeout()).andReturn(1).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService retrievalService = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService();
        retrievalService.setLoggingRequestHelperFactory(helperFactoryMock);
        retrievalService.setInjector(injectorMock);
        retrievalService.setConfiguration(configurationMock);
        retrievalService.doStart();
        retrievalService.setExecutor(executorMock);
        org.junit.Assert.assertEquals("Default request set should be empty", 0, retrievalService.getCurrentRequests().size());
        java.util.Set<java.lang.String> resultSet = retrievalService.getLogFileNames(expectedComponentName, expectedHostName, expectedClusterName);
        org.junit.Assert.assertNull("Inital query on the retrieval service should be null, since cache is empty by default", resultSet);
        org.junit.Assert.assertEquals("Incorrect number of entries in the current request set", 1, retrievalService.getCurrentRequests().size());
        org.junit.Assert.assertTrue("Incorrect HostComponent set on request set", retrievalService.getCurrentRequests().contains((expectedComponentName + "+") + expectedHostName));
        org.junit.Assert.assertEquals("Incorrect size for failure counts for components, should be 0", 0, retrievalService.getComponentRequestFailureCounts().size());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testGetLogFileNamesExistingFailuresLessThanThreshold() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        java.util.concurrent.Executor executorMock = mockSupport.createMock(java.util.concurrent.Executor.class);
        com.google.inject.Injector injectorMock = mockSupport.createMock(com.google.inject.Injector.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        executorMock.execute(EasyMock.isA(org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable.class));
        EasyMock.expectLastCall().once();
        EasyMock.expect(injectorMock.getInstance(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class)).andReturn(helperFactoryMock);
        EasyMock.expect(configurationMock.getLogSearchMetadataCacheExpireTimeout()).andReturn(1).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService retrievalService = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService();
        retrievalService.setLoggingRequestHelperFactory(helperFactoryMock);
        retrievalService.setInjector(injectorMock);
        retrievalService.setConfiguration(configurationMock);
        retrievalService.doStart();
        retrievalService.setExecutor(executorMock);
        retrievalService.getComponentRequestFailureCounts().put(expectedComponentName, new java.util.concurrent.atomic.AtomicInteger(5));
        org.junit.Assert.assertEquals("Default request set should be empty", 0, retrievalService.getCurrentRequests().size());
        java.util.Set<java.lang.String> resultSet = retrievalService.getLogFileNames(expectedComponentName, expectedHostName, expectedClusterName);
        org.junit.Assert.assertNull("Inital query on the retrieval service should be null, since cache is empty by default", resultSet);
        org.junit.Assert.assertEquals("Incorrect number of entries in the current request set", 1, retrievalService.getCurrentRequests().size());
        org.junit.Assert.assertTrue("Incorrect HostComponent set on request set", retrievalService.getCurrentRequests().contains((expectedComponentName + "+") + expectedHostName));
        org.junit.Assert.assertEquals("Incorrect size for failure counts for components, should be 0", 1, retrievalService.getComponentRequestFailureCounts().size());
        org.junit.Assert.assertEquals("Incorrect failure count for component", 5, retrievalService.getComponentRequestFailureCounts().get(expectedComponentName).get());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testGetLogFileNamesExistingFailuresAtThreshold() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        java.util.concurrent.Executor executorMock = mockSupport.createMock(java.util.concurrent.Executor.class);
        com.google.inject.Injector injectorMock = mockSupport.createMock(com.google.inject.Injector.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configurationMock.getLogSearchMetadataCacheExpireTimeout()).andReturn(1).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService retrievalService = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService();
        retrievalService.setLoggingRequestHelperFactory(helperFactoryMock);
        retrievalService.setInjector(injectorMock);
        retrievalService.setConfiguration(configurationMock);
        retrievalService.doStart();
        retrievalService.setExecutor(executorMock);
        retrievalService.getComponentRequestFailureCounts().put(expectedComponentName, new java.util.concurrent.atomic.AtomicInteger(10));
        org.junit.Assert.assertEquals("Default request set should be empty", 0, retrievalService.getCurrentRequests().size());
        java.util.Set<java.lang.String> resultSet = retrievalService.getLogFileNames(expectedComponentName, expectedHostName, expectedClusterName);
        org.junit.Assert.assertNull("Inital query on the retrieval service should be null, since cache is empty by default", resultSet);
        org.junit.Assert.assertEquals("Incorrect number of entries in the current request set", 0, retrievalService.getCurrentRequests().size());
        org.junit.Assert.assertEquals("Incorrect size for failure counts for components, should be 0", 1, retrievalService.getComponentRequestFailureCounts().size());
        org.junit.Assert.assertEquals("Incorrect failure count for component", 10, retrievalService.getComponentRequestFailureCounts().get(expectedComponentName).get());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testGetLogFileNamesExistingFailuresOverThreshold() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        java.util.concurrent.Executor executorMock = mockSupport.createMock(java.util.concurrent.Executor.class);
        com.google.inject.Injector injectorMock = mockSupport.createMock(com.google.inject.Injector.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configurationMock.getLogSearchMetadataCacheExpireTimeout()).andReturn(1).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService retrievalService = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService();
        retrievalService.setLoggingRequestHelperFactory(helperFactoryMock);
        retrievalService.setInjector(injectorMock);
        retrievalService.setConfiguration(configurationMock);
        retrievalService.doStart();
        retrievalService.setExecutor(executorMock);
        retrievalService.getComponentRequestFailureCounts().put(expectedComponentName, new java.util.concurrent.atomic.AtomicInteger(20));
        org.junit.Assert.assertEquals("Default request set should be empty", 0, retrievalService.getCurrentRequests().size());
        java.util.Set<java.lang.String> resultSet = retrievalService.getLogFileNames(expectedComponentName, expectedHostName, expectedClusterName);
        org.junit.Assert.assertNull("Inital query on the retrieval service should be null, since cache is empty by default", resultSet);
        org.junit.Assert.assertEquals("Incorrect number of entries in the current request set", 0, retrievalService.getCurrentRequests().size());
        org.junit.Assert.assertEquals("Incorrect size for failure counts for components, should be 0", 1, retrievalService.getComponentRequestFailureCounts().size());
        org.junit.Assert.assertEquals("Incorrect failure count for component", 20, retrievalService.getComponentRequestFailureCounts().get(expectedComponentName).get());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testGetLogFileNamesIgnoreMultipleRequestsForSameHostComponent() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        java.util.concurrent.Executor executorMock = mockSupport.createMock(java.util.concurrent.Executor.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = mockSupport.createMock(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configurationMock.getLogSearchMetadataCacheExpireTimeout()).andReturn(1).atLeastOnce();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService retrievalService = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService();
        retrievalService.setLoggingRequestHelperFactory(helperFactoryMock);
        retrievalService.setConfiguration(configurationMock);
        retrievalService.doStart();
        retrievalService.setExecutor(executorMock);
        retrievalService.getCurrentRequests().add((expectedComponentName + "+") + expectedHostName);
        java.util.Set<java.lang.String> resultSet = retrievalService.getLogFileNames(expectedComponentName, expectedHostName, expectedClusterName);
        org.junit.Assert.assertNull("Inital query on the retrieval service should be null, since cache is empty by default", resultSet);
        mockSupport.verifyAll();
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testRunnableWithSuccessfulCall() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        final java.lang.String expectedComponentAndHostName = (expectedComponentName + "+") + expectedHostName;
        final org.apache.ambari.server.controller.logging.HostLogFilesResponse resp = new org.apache.ambari.server.controller.logging.HostLogFilesResponse();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> componentMap = new java.util.HashMap<>();
        java.util.List<java.lang.String> expectedList = new java.util.ArrayList<>();
        expectedList.add("/this/is/just/a/test/directory");
        componentMap.put(expectedComponentName, expectedList);
        resp.setHostLogFiles(componentMap);
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helperMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelper.class);
        com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> cacheMock = mockSupport.createMock(com.google.common.cache.Cache.class);
        java.util.Set<java.lang.String> currentRequestsMock = mockSupport.createMock(java.util.Set.class);
        java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentFailureCounts = mockSupport.createMock(java.util.Map.class);
        EasyMock.expect(helperFactoryMock.getHelper(controllerMock, expectedClusterName)).andReturn(helperMock);
        EasyMock.expect(helperMock.sendGetLogFileNamesRequest(expectedHostName)).andReturn(resp);
        cacheMock.put(expectedComponentAndHostName, java.util.Collections.singleton("/this/is/just/a/test/directory"));
        EasyMock.expect(currentRequestsMock.remove(expectedComponentAndHostName)).andReturn(true).once();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable loggingRunnable = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable(expectedHostName, expectedComponentName, expectedClusterName, cacheMock, currentRequestsMock, helperFactoryMock, componentFailureCounts, controllerMock);
        loggingRunnable.run();
        mockSupport.verifyAll();
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testRunnableWithFailedCallNullHelper() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        final java.lang.String expectedComponentAndHostName = (expectedComponentName + "+") + expectedHostName;
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> cacheMock = mockSupport.createMock(com.google.common.cache.Cache.class);
        java.util.Set<java.lang.String> currentRequestsMock = mockSupport.createMock(java.util.Set.class);
        java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentFailureCounts = mockSupport.createMock(java.util.Map.class);
        EasyMock.expect(helperFactoryMock.getHelper(controllerMock, expectedClusterName)).andReturn(null);
        EasyMock.expect(currentRequestsMock.remove(expectedComponentAndHostName)).andReturn(true).once();
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable loggingRunnable = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable(expectedHostName, expectedComponentName, expectedClusterName, cacheMock, currentRequestsMock, helperFactoryMock, componentFailureCounts, controllerMock);
        loggingRunnable.run();
        mockSupport.verifyAll();
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testRunnableWithFailedCallNullResult() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        final java.lang.String expectedComponentAndHostName = (expectedComponentName + "+") + expectedHostName;
        final java.util.concurrent.atomic.AtomicInteger testInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helperMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelper.class);
        com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> cacheMock = mockSupport.createMock(com.google.common.cache.Cache.class);
        java.util.Set<java.lang.String> currentRequestsMock = mockSupport.createMock(java.util.Set.class);
        java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentFailureCounts = mockSupport.createMock(java.util.Map.class);
        org.easymock.Capture<java.util.concurrent.atomic.AtomicInteger> captureFailureCount = org.easymock.EasyMock.newCapture();
        EasyMock.expect(helperFactoryMock.getHelper(controllerMock, expectedClusterName)).andReturn(helperMock);
        EasyMock.expect(helperMock.sendGetLogFileNamesRequest(expectedHostName)).andReturn(null);
        EasyMock.expect(currentRequestsMock.remove(expectedComponentAndHostName)).andReturn(true).once();
        EasyMock.expect(componentFailureCounts.containsKey(expectedComponentName)).andReturn(false);
        EasyMock.expect(componentFailureCounts.put(EasyMock.eq(expectedComponentName), EasyMock.capture(captureFailureCount))).andReturn(new java.util.concurrent.atomic.AtomicInteger(0));
        EasyMock.expect(componentFailureCounts.get(expectedComponentName)).andReturn(testInteger);
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable loggingRunnable = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable(expectedHostName, expectedComponentName, expectedClusterName, cacheMock, currentRequestsMock, helperFactoryMock, componentFailureCounts, controllerMock);
        loggingRunnable.run();
        org.junit.Assert.assertEquals("Initial count set by Runnable should be 0", 0, captureFailureCount.getValue().get());
        org.junit.Assert.assertEquals("Failure count should have been incremented", 1, testInteger.get());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testRunnableWithFailedCallNullResultExistingFailureCount() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        final java.lang.String expectedComponentAndHostName = (expectedComponentName + "+") + expectedHostName;
        final java.util.concurrent.atomic.AtomicInteger testFailureCount = new java.util.concurrent.atomic.AtomicInteger(2);
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helperMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelper.class);
        com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> cacheMock = mockSupport.createMock(com.google.common.cache.Cache.class);
        java.util.Set<java.lang.String> currentRequestsMock = mockSupport.createMock(java.util.Set.class);
        java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentFailureCounts = mockSupport.createMock(java.util.Map.class);
        EasyMock.expect(helperFactoryMock.getHelper(controllerMock, expectedClusterName)).andReturn(helperMock);
        EasyMock.expect(helperMock.sendGetLogFileNamesRequest(expectedHostName)).andReturn(null);
        EasyMock.expect(currentRequestsMock.remove(expectedComponentAndHostName)).andReturn(true).once();
        EasyMock.expect(componentFailureCounts.containsKey(expectedComponentName)).andReturn(true);
        EasyMock.expect(componentFailureCounts.get(expectedComponentName)).andReturn(testFailureCount);
        mockSupport.replayAll();
        org.junit.Assert.assertEquals("Initial count should be 2", 2, testFailureCount.get());
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable loggingRunnable = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable(expectedHostName, expectedComponentName, expectedClusterName, cacheMock, currentRequestsMock, helperFactoryMock, componentFailureCounts, controllerMock);
        loggingRunnable.run();
        org.junit.Assert.assertEquals("Failure count should have been incremented", 3, testFailureCount.get());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    @java.lang.SuppressWarnings("unchecked")
    public void testRunnableWithFailedCallEmptyResult() throws java.lang.Exception {
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedComponentName = "DATANODE";
        final java.lang.String expectedClusterName = "clusterone";
        final java.lang.String expectedComponentAndHostName = (expectedComponentName + "+") + expectedHostName;
        final java.util.concurrent.atomic.AtomicInteger testInteger = new java.util.concurrent.atomic.AtomicInteger(0);
        final org.apache.ambari.server.controller.logging.HostLogFilesResponse resp = new org.apache.ambari.server.controller.logging.HostLogFilesResponse();
        resp.setHostLogFiles(new java.util.HashMap<>());
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory helperFactoryMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class);
        org.apache.ambari.server.controller.AmbariManagementController controllerMock = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helperMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelper.class);
        com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> cacheMock = mockSupport.createMock(com.google.common.cache.Cache.class);
        java.util.Set<java.lang.String> currentRequestsMock = mockSupport.createMock(java.util.Set.class);
        java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentFailureCounts = mockSupport.createMock(java.util.Map.class);
        org.easymock.Capture<java.util.concurrent.atomic.AtomicInteger> captureFailureCount = org.easymock.EasyMock.newCapture();
        EasyMock.expect(helperFactoryMock.getHelper(controllerMock, expectedClusterName)).andReturn(helperMock);
        EasyMock.expect(helperMock.sendGetLogFileNamesRequest(expectedHostName)).andReturn(resp);
        EasyMock.expect(currentRequestsMock.remove(expectedComponentAndHostName)).andReturn(true).once();
        EasyMock.expect(componentFailureCounts.containsKey(expectedComponentName)).andReturn(false);
        EasyMock.expect(componentFailureCounts.put(EasyMock.eq(expectedComponentName), EasyMock.capture(captureFailureCount))).andReturn(new java.util.concurrent.atomic.AtomicInteger(0));
        EasyMock.expect(componentFailureCounts.get(expectedComponentName)).andReturn(testInteger);
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable loggingRunnable = new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable(expectedHostName, expectedComponentName, expectedClusterName, cacheMock, currentRequestsMock, helperFactoryMock, componentFailureCounts, controllerMock);
        loggingRunnable.run();
        org.junit.Assert.assertEquals("Initial count set by Runnable should be 0", 0, captureFailureCount.getValue().get());
        org.junit.Assert.assertEquals("Failure count should have been incremented", 1, testInteger.get());
        mockSupport.verifyAll();
    }
}