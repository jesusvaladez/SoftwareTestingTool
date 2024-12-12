package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class BlueprintServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.BlueprintService BlueprintService = new org.apache.ambari.server.api.services.BlueprintServiceTest.TestBlueprintService("blueprintName");
        java.lang.reflect.Method m = BlueprintService.getClass().getMethod("getBlueprint", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "blueprintName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, BlueprintService, m, args, null));
        BlueprintService = new org.apache.ambari.server.api.services.BlueprintServiceTest.TestBlueprintService(null);
        m = BlueprintService.getClass().getMethod("getBlueprints", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, BlueprintService, m, args, null));
        BlueprintService = new org.apache.ambari.server.api.services.BlueprintServiceTest.TestBlueprintService("blueprintName");
        m = BlueprintService.getClass().getMethod("createBlueprint", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "blueprintName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, BlueprintService, m, args, "body"));
        BlueprintService = new org.apache.ambari.server.api.services.BlueprintServiceTest.TestBlueprintService("blueprintName");
        m = BlueprintService.getClass().getMethod("deleteBlueprint", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "blueprintName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, BlueprintService, m, args, null));
        BlueprintService = new org.apache.ambari.server.api.services.BlueprintServiceTest.TestBlueprintService(null);
        m = BlueprintService.getClass().getMethod("deleteBlueprints", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, BlueprintService, m, args, null));
        return listInvocations;
    }

    private class TestBlueprintService extends org.apache.ambari.server.api.services.BlueprintService {
        private java.lang.String m_blueprintId;

        private TestBlueprintService(java.lang.String blueprintId) {
            m_blueprintId = blueprintId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createBlueprintResource(java.lang.String blueprintName) {
            org.junit.Assert.assertEquals(m_blueprintId, blueprintName);
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