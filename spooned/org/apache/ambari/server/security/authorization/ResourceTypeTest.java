package org.apache.ambari.server.security.authorization;
public class ResourceTypeTest {
    @org.junit.Test
    public void testGetId() throws java.lang.Exception {
        org.junit.Assert.assertEquals(1, org.apache.ambari.server.security.authorization.ResourceType.AMBARI.getId());
        org.junit.Assert.assertEquals(2, org.apache.ambari.server.security.authorization.ResourceType.CLUSTER.getId());
        org.junit.Assert.assertEquals(3, org.apache.ambari.server.security.authorization.ResourceType.VIEW.getId());
    }

    @org.junit.Test
    public void testTranslate() throws java.lang.Exception {
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, org.apache.ambari.server.security.authorization.ResourceType.translate("ambari"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, org.apache.ambari.server.security.authorization.ResourceType.translate("Ambari"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, org.apache.ambari.server.security.authorization.ResourceType.translate("AMBARI"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, org.apache.ambari.server.security.authorization.ResourceType.translate(" AMBARI "));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, org.apache.ambari.server.security.authorization.ResourceType.translate("cluster"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, org.apache.ambari.server.security.authorization.ResourceType.translate("Cluster"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, org.apache.ambari.server.security.authorization.ResourceType.translate("CLUSTER"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, org.apache.ambari.server.security.authorization.ResourceType.translate(" CLUSTER "));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("view"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("View"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("VIEW"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate(" VIEW "));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("CAPACITY-SCHEDULER{1.0.0}"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("ADMIN_VIEW{2.1.2}"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("FILES{1.0.0}"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("PIG{1.0.0}"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("CAPACITY-SCHEDULER{1.0.0}"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("TEZ{0.7.0.2.3.2.0-377}"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("SLIDER{2.0.0}"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.ResourceType.VIEW, org.apache.ambari.server.security.authorization.ResourceType.translate("HIVE{1.0.0}"));
    }
}