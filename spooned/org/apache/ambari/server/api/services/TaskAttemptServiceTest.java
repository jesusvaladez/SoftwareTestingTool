package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class TaskAttemptServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.TaskAttemptServiceTest.TestTaskAttemptService service = new org.apache.ambari.server.api.services.TaskAttemptServiceTest.TestTaskAttemptService("clusterName", "workflowId", "jobId");
        java.lang.reflect.Method m = service.getClass().getMethod("getTaskAttempt", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "jobId" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        service = new org.apache.ambari.server.api.services.TaskAttemptServiceTest.TestTaskAttemptService("clusterName", "workflowId", "jobId");
        m = service.getClass().getMethod("getTaskAttempts", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, service, m, args, null));
        return listInvocations;
    }

    private class TestTaskAttemptService extends org.apache.ambari.server.api.services.TaskAttemptService {
        private java.lang.String clusterName;

        private java.lang.String workflowId;

        private java.lang.String jobId;

        public TestTaskAttemptService(java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId) {
            super(clusterName, workflowId, jobId);
            this.clusterName = clusterName;
            this.workflowId = workflowId;
            this.jobId = jobId;
        }

        @java.lang.Override
        org.apache.ambari.server.api.resources.ResourceInstance createTaskAttemptResource(java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId, java.lang.String taskAttemptId) {
            org.junit.Assert.assertEquals(this.clusterName, clusterName);
            org.junit.Assert.assertEquals(this.workflowId, workflowId);
            org.junit.Assert.assertEquals(this.jobId, jobId);
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