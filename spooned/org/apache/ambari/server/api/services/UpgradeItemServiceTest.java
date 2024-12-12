package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class UpgradeItemServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.UpgradeItemService service = new org.apache.ambari.server.api.services.UpgradeItemServiceTest.TestUpgradeItemService("clusterName", "upgradeId", "upgradeGroupId", 99L);
        java.lang.reflect.Method m = service.getClass().getMethod("updateUpgradeItem", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.Long.class);
        java.lang.Object[] args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), 99L };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, service, m, args, "body"));
        return listInvocations;
    }

    private class TestUpgradeItemService extends org.apache.ambari.server.api.services.UpgradeItemService {
        private java.lang.Long m_upgradeItemId;

        private TestUpgradeItemService(java.lang.String clusterName, java.lang.String upgradeId, java.lang.String upgradeGroupId, java.lang.Long upgradeItemId) {
            super(clusterName, upgradeId, upgradeGroupId);
            m_upgradeItemId = upgradeItemId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.Long upgradeItemId) {
            org.junit.Assert.assertEquals(m_upgradeItemId, upgradeItemId);
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.services.RequestFactory getRequestFactory() {
            return getTestRequestFactory();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.parsers.RequestBodyParser getBodyParser() {
            return getTestBodyParser();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.serializers.ResultSerializer getResultSerializer() {
            return getTestResultSerializer();
        }
    }
}