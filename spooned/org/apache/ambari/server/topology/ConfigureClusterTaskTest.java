package org.apache.ambari.server.topology;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
public class ConfigureClusterTaskTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.topology.ClusterConfigurationRequest clusterConfigurationRequest;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.topology.ClusterTopology clusterTopology;

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private org.apache.ambari.server.topology.AmbariContext ambariContext;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher;

    private org.apache.ambari.server.topology.tasks.ConfigureClusterTask testSubject;

    @org.junit.Before
    public void before() {
        resetAll();
        testSubject = new org.apache.ambari.server.topology.tasks.ConfigureClusterTask(clusterTopology, clusterConfigurationRequest, ambariEventPublisher);
    }

    @org.junit.Test
    public void taskShouldBeExecutedIfRequiredHostgroupsAreResolved() throws java.lang.Exception {
        EasyMock.expect(clusterConfigurationRequest.getRequiredHostGroups()).andReturn(java.util.Collections.emptyList());
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(java.util.Collections.emptyMap());
        EasyMock.expect(clusterTopology.getClusterId()).andReturn(1L).anyTimes();
        EasyMock.expect(clusterTopology.getAmbariContext()).andReturn(ambariContext);
        EasyMock.expect(ambariContext.getClusterName(1L)).andReturn("testCluster");
        clusterConfigurationRequest.process();
        ambariEventPublisher.publish(EasyMock.anyObject(org.apache.ambari.server.events.AmbariEvent.class));
        replayAll();
        java.lang.Boolean result = testSubject.call();
        verifyAll();
        org.junit.Assert.assertTrue(result);
    }

    @org.junit.Test
    public void testsShouldConfigureClusterTaskExecuteWhenCalledFromAsyncCallableService() throws java.lang.Exception {
        EasyMock.expect(clusterConfigurationRequest.getRequiredHostGroups()).andReturn(java.util.Collections.emptyList());
        EasyMock.expect(clusterTopology.getHostGroupInfo()).andReturn(java.util.Collections.emptyMap());
        clusterConfigurationRequest.process();
        replayAll();
        org.apache.ambari.server.topology.AsyncCallableService<java.lang.Boolean> asyncService = new org.apache.ambari.server.topology.AsyncCallableService<>(testSubject, 5000, 500, "test", t -> {
        });
        asyncService.call();
        verifyAll();
    }
}