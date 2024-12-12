package org.apache.ambari.server.state;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.hasSize;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class ServiceComponentSupportTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String STACK_NAME = "HDP";

    private static final java.lang.String VERSION = "3.0";

    @org.easymock.Mock
    private org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private org.apache.ambari.server.state.ServiceComponentSupport componentSupport;

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        componentSupport = new org.apache.ambari.server.state.ServiceComponentSupport(() -> ambariMetaInfo);
    }

    @org.junit.Test
    public void testNoUnsupportedIfAllExistsInTargetStack() throws java.lang.Exception {
        targetStackWith("SERVICE1", "SERVICE2");
        java.util.Set<java.lang.String> unsupported = unsupportedServices(clusterWith("SERVICE1", "SERVICE2"));
        org.junit.Assert.assertThat(unsupported, org.hamcrest.Matchers.hasSize(0));
        verifyAll();
    }

    @org.junit.Test
    public void testUnsupportedIfDoesntExistInTargetStack() throws java.lang.Exception {
        targetStackWith("SERVICE1");
        java.util.Set<java.lang.String> unsupported = unsupportedServices(clusterWith("SERVICE1", "SERVICE2"));
        org.junit.Assert.assertThat(unsupported, org.apache.ambari.server.state.ServiceComponentSupportTest.hasOnlyItems(org.hamcrest.core.Is.is("SERVICE2")));
        verifyAll();
    }

    @org.junit.Test
    public void testUnsupportedIfDeletedFromTargetStack() throws java.lang.Exception {
        targetStackWith("SERVICE1", "SERVICE2");
        markAsDeleted("SERVICE2");
        java.util.Set<java.lang.String> unsupported = unsupportedServices(clusterWith("SERVICE1", "SERVICE2"));
        org.junit.Assert.assertThat(unsupported, org.apache.ambari.server.state.ServiceComponentSupportTest.hasOnlyItems(org.hamcrest.core.Is.is("SERVICE2")));
        verifyAll();
    }

    private void targetStackWith(java.lang.String... serviceNames) throws org.apache.ambari.server.AmbariException {
        EasyMock.expect(ambariMetaInfo.getServices(org.apache.ambari.server.state.ServiceComponentSupportTest.STACK_NAME, org.apache.ambari.server.state.ServiceComponentSupportTest.VERSION)).andReturn(serviceInfoMap(serviceNames)).anyTimes();
        EasyMock.replay(ambariMetaInfo);
    }

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap(java.lang.String... serviceNames) {
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = new java.util.HashMap<>();
        for (java.lang.String serviceName : serviceNames) {
            serviceInfoMap.put(serviceName, new org.apache.ambari.server.state.ServiceInfo());
        }
        return serviceInfoMap;
    }

    private java.util.Set<java.lang.String> unsupportedServices(org.apache.ambari.server.state.Cluster cluster) {
        return componentSupport.unsupportedServices(cluster, org.apache.ambari.server.state.ServiceComponentSupportTest.STACK_NAME, org.apache.ambari.server.state.ServiceComponentSupportTest.VERSION);
    }

    private org.apache.ambari.server.state.Cluster clusterWith(java.lang.String... installedServiceNames) {
        org.apache.ambari.server.state.Cluster cluster = mock(org.apache.ambari.server.state.Cluster.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> serviceMap = new java.util.HashMap<>();
        for (java.lang.String serviceName : installedServiceNames) {
            serviceMap.put(serviceName, null);
        }
        EasyMock.expect(cluster.getServices()).andReturn(serviceMap);
        EasyMock.replay(cluster);
        return cluster;
    }

    private void markAsDeleted(java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        ambariMetaInfo.getServices(org.apache.ambari.server.state.ServiceComponentSupportTest.STACK_NAME, org.apache.ambari.server.state.ServiceComponentSupportTest.VERSION).get(serviceName).setDeleted(true);
    }

    private static org.hamcrest.Matcher hasOnlyItems(org.hamcrest.Matcher... matchers) {
        return org.hamcrest.core.AllOf.allOf(org.hamcrest.Matchers.hasSize(matchers.length), org.junit.internal.matchers.IsCollectionContaining.hasItems(matchers));
    }
}