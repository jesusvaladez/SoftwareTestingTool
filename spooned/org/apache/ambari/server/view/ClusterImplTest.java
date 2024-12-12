package org.apache.ambari.server.view;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ClusterImplTest {
    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster.getClusterName()).andReturn("c1").anyTimes();
        EasyMock.replay(cluster);
        org.apache.ambari.server.view.ClusterImpl clusterImpl = new org.apache.ambari.server.view.ClusterImpl(cluster);
        org.junit.Assert.assertEquals("c1", clusterImpl.getName());
        EasyMock.verify(cluster);
    }

    @org.junit.Test
    public void testGetConfigurationValue() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config config = EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put("foo", "bar");
        EasyMock.expect(cluster.getDesiredConfigByType("core-site")).andReturn(config).anyTimes();
        EasyMock.expect(config.getProperties()).andReturn(properties).anyTimes();
        EasyMock.replay(cluster, config);
        org.apache.ambari.server.view.ClusterImpl clusterImpl = new org.apache.ambari.server.view.ClusterImpl(cluster);
        org.junit.Assert.assertEquals("bar", clusterImpl.getConfigurationValue("core-site", "foo"));
        EasyMock.verify(cluster, config);
    }

    @org.junit.Test
    public void testGetHostsForServiceComponent() {
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        java.lang.String service = "SERVICE";
        java.lang.String component = "COMPONENT";
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> components = new java.util.ArrayList<>();
        org.apache.ambari.server.state.ServiceComponentHost component1 = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(component1.getHostName()).andReturn("host1");
        components.add(component1);
        org.apache.ambari.server.state.ServiceComponentHost component2 = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(component2.getHostName()).andReturn("host2");
        components.add(component2);
        EasyMock.expect(cluster.getServiceComponentHosts(service, component)).andReturn(components);
        EasyMock.replay(cluster, component1, component2);
        org.apache.ambari.server.view.ClusterImpl clusterImpl = new org.apache.ambari.server.view.ClusterImpl(cluster);
        java.util.List<java.lang.String> hosts = clusterImpl.getHostsForServiceComponent(service, component);
        org.junit.Assert.assertEquals(2, hosts.size());
        org.junit.Assert.assertEquals("host1", hosts.get(0));
        org.junit.Assert.assertEquals("host2", hosts.get(1));
        EasyMock.verify(cluster, component1, component2);
    }
}