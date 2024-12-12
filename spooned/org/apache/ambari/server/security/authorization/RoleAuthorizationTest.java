package org.apache.ambari.server.security.authorization;
public class RoleAuthorizationTest {
    @org.junit.Test
    public void testTranslate() throws java.lang.Exception {
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.RoleAuthorization.VIEW_USE, org.apache.ambari.server.security.authorization.RoleAuthorization.translate("VIEW.USE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.translate("SERVICE.VIEW_METRICS"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.translate("HOST.VIEW_METRICS"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_METRICS, org.apache.ambari.server.security.authorization.RoleAuthorization.translate("CLUSTER.VIEW_METRICS"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ADD_DELETE_CLUSTERS, org.apache.ambari.server.security.authorization.RoleAuthorization.translate("AMBARI.ADD_DELETE_CLUSTERS"));
    }
}