package org.apache.ambari.server.controller.ivory;
public class ClusterTest {
    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Cluster cluster = new org.apache.ambari.server.controller.ivory.Cluster("Cluster1", "Colo", java.util.Collections.singleton(new org.apache.ambari.server.controller.ivory.Cluster.Interface("type", "endpoint", "version")), java.util.Collections.singleton(new org.apache.ambari.server.controller.ivory.Cluster.Location("name", "path")), java.util.Collections.singletonMap("P1", "V1"));
        org.junit.Assert.assertEquals("Cluster1", cluster.getName());
    }

    @org.junit.Test
    public void testGetColo() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Cluster cluster = new org.apache.ambari.server.controller.ivory.Cluster("Cluster1", "Colo", java.util.Collections.singleton(new org.apache.ambari.server.controller.ivory.Cluster.Interface("type", "endpoint", "version")), java.util.Collections.singleton(new org.apache.ambari.server.controller.ivory.Cluster.Location("name", "path")), java.util.Collections.singletonMap("P1", "V1"));
        org.junit.Assert.assertEquals("Colo", cluster.getColo());
    }

    @org.junit.Test
    public void testGetInterfaces() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Cluster.Interface interface1 = new org.apache.ambari.server.controller.ivory.Cluster.Interface("type", "endpoint", "version");
        org.apache.ambari.server.controller.ivory.Cluster cluster = new org.apache.ambari.server.controller.ivory.Cluster("Cluster1", "Colo", java.util.Collections.singleton(interface1), java.util.Collections.singleton(new org.apache.ambari.server.controller.ivory.Cluster.Location("name", "path")), java.util.Collections.singletonMap("P1", "V1"));
        org.junit.Assert.assertEquals(java.util.Collections.singleton(interface1), cluster.getInterfaces());
    }

    @org.junit.Test
    public void testGetLocations() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Cluster.Location location = new org.apache.ambari.server.controller.ivory.Cluster.Location("name", "path");
        org.apache.ambari.server.controller.ivory.Cluster cluster = new org.apache.ambari.server.controller.ivory.Cluster("Cluster1", "Colo", java.util.Collections.singleton(new org.apache.ambari.server.controller.ivory.Cluster.Interface("type", "endpoint", "version")), java.util.Collections.singleton(location), java.util.Collections.singletonMap("P1", "V1"));
        org.junit.Assert.assertEquals(java.util.Collections.singleton(location), cluster.getLocations());
    }

    @org.junit.Test
    public void testGetProperties() throws java.lang.Exception {
        org.apache.ambari.server.controller.ivory.Cluster cluster = new org.apache.ambari.server.controller.ivory.Cluster("Cluster1", "Colo", java.util.Collections.singleton(new org.apache.ambari.server.controller.ivory.Cluster.Interface("type", "endpoint", "version")), java.util.Collections.singleton(new org.apache.ambari.server.controller.ivory.Cluster.Location("name", "path")), java.util.Collections.singletonMap("P1", "V1"));
        org.junit.Assert.assertEquals(java.util.Collections.singletonMap("P1", "V1"), cluster.getProperties());
    }
}