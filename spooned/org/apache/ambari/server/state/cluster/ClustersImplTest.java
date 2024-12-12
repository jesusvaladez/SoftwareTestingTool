package org.apache.ambari.server.state.cluster;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ClustersImplTest {
    @org.junit.Test
    public void testAddSessionAttributes() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> attributes = new java.util.HashMap<>();
        attributes.put("foo", "bar");
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.cluster.ClustersImpl clusters = EasyMock.createMockBuilder(org.apache.ambari.server.state.cluster.ClustersImpl.class).addMockedMethod("findCluster", java.lang.String.class).createMock();
        EasyMock.expect(clusters.findCluster("c1")).andReturn(cluster);
        cluster.addSessionAttributes(attributes);
        EasyMock.replay(clusters, cluster);
        clusters.addSessionAttributes("c1", attributes);
        EasyMock.verify(clusters, cluster);
    }

    @org.junit.Test
    public void testGetSessionAttributes() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Object> attributes = new java.util.HashMap<>();
        attributes.put("foo", "bar");
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.cluster.ClustersImpl clusters = EasyMock.createMockBuilder(org.apache.ambari.server.state.cluster.ClustersImpl.class).addMockedMethod("findCluster", java.lang.String.class).createMock();
        EasyMock.expect(clusters.findCluster("c1")).andReturn(cluster);
        EasyMock.expect(cluster.getSessionAttributes()).andReturn(attributes);
        EasyMock.replay(clusters, cluster);
        org.junit.Assert.assertEquals(attributes, clusters.getSessionAttributes("c1"));
        EasyMock.verify(clusters, cluster);
    }
}