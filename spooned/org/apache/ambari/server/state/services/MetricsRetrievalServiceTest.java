package org.apache.ambari.server.state.services;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
public class MetricsRetrievalServiceTest extends org.easymock.EasyMockSupport {
    private com.google.inject.Injector m_injector;

    private static final java.lang.String JMX_URL = "http://jmx-endpoint";

    private static final java.lang.String REST_URL = "http://rest-endpoint";

    private static final int METRICS_SERVICE_TIMEOUT = 10;

    org.apache.ambari.server.state.services.MetricsRetrievalService m_service = new org.apache.ambari.server.state.services.MetricsRetrievalService();

    @org.junit.Before
    public void before() {
        m_injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.MockModule());
        m_injector.injectMembers(m_service);
    }

    @org.junit.After
    public void after() throws java.util.concurrent.TimeoutException {
        if ((m_service != null) && m_service.isRunning()) {
            m_service.stopAsync();
            m_service.awaitTerminated(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    @org.junit.Test
    public void testCachedValueRetrievalDoesNotRequest() throws java.lang.Exception {
        m_service.startAsync();
        m_service.awaitRunning(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        org.apache.ambari.server.controller.jmx.JMXMetricHolder jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNull(jmxMetricHolder);
        java.util.Map<java.lang.String, java.lang.String> restMetrics = m_service.getCachedRESTMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        junit.framework.Assert.assertNull(restMetrics);
    }

    @org.junit.Test
    public void testRetrievalOfMetrics() throws java.lang.Exception {
        java.io.InputStream jmxInputStream = org.apache.commons.io.IOUtils.toInputStream("{ \"beans\": [] }");
        java.io.InputStream restInputStream = org.apache.commons.io.IOUtils.toInputStream("{}");
        org.apache.ambari.server.controller.utilities.StreamProvider streamProvider = createNiceMock(org.apache.ambari.server.controller.utilities.StreamProvider.class);
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL)).andReturn(jmxInputStream).once();
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL)).andReturn(restInputStream).once();
        replayAll();
        m_service.startAsync();
        m_service.awaitRunning(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        m_service.setThreadPoolExecutor(new org.apache.ambari.server.utils.SynchronousThreadPoolExecutor());
        org.apache.ambari.server.controller.jmx.JMXMetricHolder jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNull(jmxMetricHolder);
        java.util.Map<java.lang.String, java.lang.String> restMetrics = m_service.getCachedRESTMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        junit.framework.Assert.assertNull(restMetrics);
        m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNotNull(jmxMetricHolder);
        m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.REST, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        restMetrics = m_service.getCachedRESTMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        junit.framework.Assert.assertNotNull(restMetrics);
        verifyAll();
    }

    @org.junit.Test
    public void testRemovingValuesFromCacheOnFail() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = m_injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_REQUEST_TTL.getKey(), "1");
        java.io.InputStream jmxInputStream = org.apache.commons.io.IOUtils.toInputStream("{ \"beans\": [] }");
        java.io.InputStream restInputStream = org.apache.commons.io.IOUtils.toInputStream("{}");
        org.apache.ambari.server.controller.utilities.StreamProvider streamProvider = createNiceMock(org.apache.ambari.server.controller.utilities.StreamProvider.class);
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL)).andReturn(jmxInputStream).once();
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL)).andReturn(restInputStream).once();
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL)).andThrow(new java.io.IOException()).once();
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL)).andThrow(new java.io.IOException()).once();
        replayAll();
        m_service.startAsync();
        m_service.awaitRunning(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        m_service.setThreadPoolExecutor(new org.apache.ambari.server.utils.SynchronousThreadPoolExecutor());
        org.apache.ambari.server.controller.jmx.JMXMetricHolder jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNull(jmxMetricHolder);
        java.util.Map<java.lang.String, java.lang.String> restMetrics = m_service.getCachedRESTMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        junit.framework.Assert.assertNull(restMetrics);
        m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNotNull(jmxMetricHolder);
        m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.REST, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        restMetrics = m_service.getCachedRESTMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        junit.framework.Assert.assertNotNull(restMetrics);
        jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNotNull(jmxMetricHolder);
        restMetrics = m_service.getCachedRESTMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        junit.framework.Assert.assertNotNull(restMetrics);
        java.lang.Thread.sleep(1000);
        m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNull(jmxMetricHolder);
        m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.REST, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        restMetrics = m_service.getCachedRESTMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.REST_URL);
        junit.framework.Assert.assertNull(restMetrics);
        verifyAll();
    }

    @org.junit.Test
    public void testJsonNaN() throws java.lang.Exception {
        java.io.InputStream jmxInputStream = org.apache.commons.io.IOUtils.toInputStream("{ \"beans\": [ " + (((((((" {\n" + "    \"name\" : \"Hadoop:service=HBase,name=RegionServer,sub=Server\",\n") + "    \"modelerType\" : \"RegionServer,sub=Server\",  \"l1CacheMissCount\" : 0,\n") + "    \"l1CacheHitRatio\" : NaN,\n") + "    \"l1CacheMissRatio\" : NaN,\n") + "    \"l2CacheHitCount\" : 0") + " }] ") + "}"));
        org.apache.ambari.server.controller.utilities.StreamProvider streamProvider = createNiceMock(org.apache.ambari.server.controller.utilities.StreamProvider.class);
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL)).andReturn(jmxInputStream).once();
        replayAll();
        m_service.startAsync();
        m_service.awaitRunning(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        m_service.setThreadPoolExecutor(new org.apache.ambari.server.utils.SynchronousThreadPoolExecutor());
        org.apache.ambari.server.controller.jmx.JMXMetricHolder jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNull(jmxMetricHolder);
        m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        jmxMetricHolder = m_service.getCachedJMXMetric(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        junit.framework.Assert.assertNotNull(jmxMetricHolder);
    }

    @org.junit.Test
    public void testRequestTTL() throws java.lang.Exception {
        java.io.InputStream jmxInputStream = org.apache.commons.io.IOUtils.toInputStream("{ \"beans\": [] }");
        org.apache.ambari.server.controller.utilities.StreamProvider streamProvider = createStrictMock(org.apache.ambari.server.controller.utilities.StreamProvider.class);
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL)).andReturn(jmxInputStream).once();
        replayAll();
        m_service.startAsync();
        m_service.awaitRunning(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        m_service.setThreadPoolExecutor(new org.apache.ambari.server.utils.SynchronousThreadPoolExecutor());
        for (int i = 0; i < 100; i++) {
            m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        }
        verifyAll();
    }

    @org.junit.Test
    public void testRequestTTLDisabled() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = m_injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.METRIC_RETRIEVAL_SERVICE_REQUEST_TTL_ENABLED.getKey(), "false");
        java.io.InputStream jmxInputStream = org.apache.commons.io.IOUtils.toInputStream("{ \"beans\": [] }");
        org.apache.ambari.server.controller.utilities.StreamProvider streamProvider = createStrictMock(org.apache.ambari.server.controller.utilities.StreamProvider.class);
        org.easymock.EasyMock.expect(streamProvider.readFrom(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL)).andReturn(jmxInputStream).times(100);
        replayAll();
        m_service.startAsync();
        m_service.awaitRunning(org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.METRICS_SERVICE_TIMEOUT, java.util.concurrent.TimeUnit.SECONDS);
        m_service.setThreadPoolExecutor(new org.apache.ambari.server.utils.SynchronousThreadPoolExecutor());
        for (int i = 0; i < 100; i++) {
            m_service.submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType.JMX, streamProvider, org.apache.ambari.server.state.services.MetricsRetrievalServiceTest.JMX_URL);
        }
        verifyAll();
    }

    private class MockModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
            binder.bind(org.apache.ambari.server.state.Clusters.class).toInstance(createNiceMock(org.apache.ambari.server.state.Clusters.class));
            binder.bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            binder.bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(createNiceMock(org.apache.ambari.server.orm.DBAccessor.class));
            binder.bind(org.apache.ambari.server.state.Cluster.class).toInstance(cluster);
            binder.bind(javax.persistence.EntityManager.class).toInstance(createNiceMock(javax.persistence.EntityManager.class));
        }
    }
}