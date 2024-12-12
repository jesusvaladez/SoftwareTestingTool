package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class GroupServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.groups.GroupService groupService;
        java.lang.reflect.Method m;
        java.lang.Object[] args;
        groupService = new org.apache.ambari.server.api.services.GroupServiceTest.TestGroupService();
        m = groupService.getClass().getMethod("getGroups", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, groupService, m, args, null));
        groupService = new org.apache.ambari.server.api.services.GroupServiceTest.TestGroupService();
        m = groupService.getClass().getMethod("getGroup", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "groupname" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, groupService, m, args, null));
        groupService = new org.apache.ambari.server.api.services.GroupServiceTest.TestGroupService();
        m = groupService.getClass().getMethod("createGroup", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, groupService, m, args, "body"));
        groupService = new org.apache.ambari.server.api.services.GroupServiceTest.TestGroupService();
        m = groupService.getClass().getMethod("createGroup", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "groupname" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, groupService, m, args, "body"));
        groupService = new org.apache.ambari.server.api.services.GroupServiceTest.TestGroupService();
        m = groupService.getClass().getMethod("deleteGroup", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "groupname" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, groupService, m, args, null));
        return listInvocations;
    }

    private class TestGroupService extends org.apache.ambari.server.api.services.groups.GroupService {
        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createResource(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds) {
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