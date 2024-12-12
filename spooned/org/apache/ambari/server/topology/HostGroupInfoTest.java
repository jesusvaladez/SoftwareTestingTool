package org.apache.ambari.server.topology;
import static org.easymock.EasyMock.createNiceMock;
public class HostGroupInfoTest {
    @org.junit.Test
    public void testGetHostGroupName() {
        org.apache.ambari.server.topology.HostGroupInfo group = new org.apache.ambari.server.topology.HostGroupInfo("test-name");
        org.junit.Assert.assertEquals("test-name", group.getHostGroupName());
    }

    @org.junit.Test
    public void testHostName_isConvertedToLowercase() {
        org.apache.ambari.server.topology.HostGroupInfo group = new org.apache.ambari.server.topology.HostGroupInfo("test-name");
        group.addHost("HOST1");
        org.junit.Assert.assertEquals(1, group.getHostNames().size());
        org.junit.Assert.assertTrue(group.getHostNames().contains("host1"));
    }

    @org.junit.Test
    public void testSetGetHostNames() {
        org.apache.ambari.server.topology.HostGroupInfo group = new org.apache.ambari.server.topology.HostGroupInfo("test-name");
        group.addHost("host1");
        org.junit.Assert.assertEquals(1, group.getHostNames().size());
        org.junit.Assert.assertTrue(group.getHostNames().contains("host1"));
        group.addHosts(java.util.Arrays.asList("host2", "host3", "host1"));
        java.util.Collection<java.lang.String> hostNames = group.getHostNames();
        org.junit.Assert.assertEquals(3, hostNames.size());
        org.junit.Assert.assertTrue(hostNames.contains("host1"));
        org.junit.Assert.assertTrue(hostNames.contains("host2"));
        org.junit.Assert.assertTrue(hostNames.contains("host3"));
        hostNames.clear();
        hostNames = group.getHostNames();
        org.junit.Assert.assertEquals(3, hostNames.size());
        org.junit.Assert.assertTrue(hostNames.contains("host1"));
        org.junit.Assert.assertTrue(hostNames.contains("host2"));
        org.junit.Assert.assertTrue(hostNames.contains("host3"));
    }

    @org.junit.Test
    public void testSetGetRequestedHostCount_explicit() {
        org.apache.ambari.server.topology.HostGroupInfo group = new org.apache.ambari.server.topology.HostGroupInfo("test-name");
        org.junit.Assert.assertEquals(0, group.getRequestedHostCount());
        group.setRequestedCount(5);
        org.junit.Assert.assertEquals(5, group.getRequestedHostCount());
    }

    @org.junit.Test
    public void testSetGetRequestedHostCount_hostNamesSpecified() {
        org.apache.ambari.server.topology.HostGroupInfo group = new org.apache.ambari.server.topology.HostGroupInfo("test-name");
        org.junit.Assert.assertEquals(0, group.getRequestedHostCount());
        group.addHosts(java.util.Arrays.asList("host2", "host3", "host1"));
        org.junit.Assert.assertEquals(3, group.getRequestedHostCount());
    }

    @org.junit.Test
    public void testSetGetGetConfiguration() {
        org.apache.ambari.server.topology.Configuration configuration = EasyMock.createNiceMock(org.apache.ambari.server.topology.Configuration.class);
        org.apache.ambari.server.topology.HostGroupInfo group = new org.apache.ambari.server.topology.HostGroupInfo("test-name");
        org.junit.Assert.assertNull(group.getConfiguration());
        group.setConfiguration(configuration);
        org.junit.Assert.assertSame(configuration, group.getConfiguration());
    }

    @org.junit.Test
    public void testSetGetPredicate() throws java.lang.Exception {
        org.apache.ambari.server.topology.HostGroupInfo group = new org.apache.ambari.server.topology.HostGroupInfo("test-name");
        org.junit.Assert.assertNull(group.getPredicateString());
        org.junit.Assert.assertNull(group.getPredicate());
        group.setPredicate("Hosts/host_name=awesome.host.com");
        org.junit.Assert.assertEquals("Hosts/host_name=awesome.host.com", group.getPredicateString());
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("Hosts/host_name", "awesome.host.com"), group.getPredicate());
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.predicate.InvalidQueryException.class)
    public void testSetPredicate_invalid() throws java.lang.Exception {
        org.apache.ambari.server.topology.HostGroupInfo group = new org.apache.ambari.server.topology.HostGroupInfo("test-name");
        group.setPredicate("=thisIsNotAPredicate");
    }
}