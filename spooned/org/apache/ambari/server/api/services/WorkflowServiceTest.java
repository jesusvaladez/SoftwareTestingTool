package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class WorkflowServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.WorkflowService service = new org.apache.ambari.server.api.services.WorkflowServiceTest.TestWorkflowService("clusterName");
        java.lang.reflect.Method m = service.getClass().getMethod("getWorkflow", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "jobId" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.WorkflowServiceTest.TestWorkflowService("clusterName");
        m = service.getClass().getMethod("getWorkflows", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        return listInvocations;
    }

    private class TestWorkflowService extends org.apache.ambari.server.api.services.WorkflowService {
        private java.lang.String clusterName;

        public TestWorkflowService(java.lang.String clusterName) {
            super(clusterName);
            this.clusterName = clusterName;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createWorkflowResource(java.lang.String clusterName, java.lang.String workflowId) {
            org.junit.Assert.assertEquals(this.clusterName, clusterName);
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