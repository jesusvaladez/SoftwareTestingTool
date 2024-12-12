package org.apache.ambari.server.api.query;
public class QueryInfoTest {
    @org.junit.Test
    public void testGetProperties() {
        java.util.Set<java.lang.String> properties = new java.util.HashSet<>();
        org.apache.ambari.server.api.query.QueryInfo info = new org.apache.ambari.server.api.query.QueryInfo(new org.apache.ambari.server.api.resources.ClusterResourceDefinition(), properties);
        org.junit.Assert.assertEquals(properties, info.getProperties());
    }

    @org.junit.Test
    public void testGetResource() {
        org.apache.ambari.server.api.resources.ResourceDefinition resource = new org.apache.ambari.server.api.resources.ClusterResourceDefinition();
        org.apache.ambari.server.api.query.QueryInfo info = new org.apache.ambari.server.api.query.QueryInfo(resource, new java.util.HashSet<>());
        org.junit.Assert.assertSame(resource, info.getResource());
    }
}