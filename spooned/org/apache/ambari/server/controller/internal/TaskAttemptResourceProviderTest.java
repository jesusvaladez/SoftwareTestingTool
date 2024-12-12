package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class TaskAttemptResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> expected = new java.util.HashSet<>();
        expected.add(org.apache.ambari.server.controller.internal.TaskAttemptResourceProviderTest.createTaskAttemptResponse("Cluster100", "workflow1", "job1", "taskAttempt1"));
        expected.add(org.apache.ambari.server.controller.internal.TaskAttemptResourceProviderTest.createTaskAttemptResponse("Cluster100", "workflow2", "job2", "taskAttempt2"));
        expected.add(org.apache.ambari.server.controller.internal.TaskAttemptResourceProviderTest.createTaskAttemptResponse("Cluster100", "workflow2", "job2", "taskAttempt3"));
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt;
        java.util.Set<java.lang.String> propertyIds = org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.propertyIds;
        org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFetcher taskAttemptFetcher = EasyMock.createMock(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFetcher.class);
        EasyMock.expect(taskAttemptFetcher.fetchTaskAttemptDetails(propertyIds, null, null, "job2", null)).andReturn(expected).once();
        EasyMock.replay(taskAttemptFetcher);
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider(propertyIds, keyPropertyIds, taskAttemptFetcher);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_JOB_ID_PROPERTY_ID).equals("job2").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            names.add(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID))));
        }
        for (org.apache.ambari.server.controller.spi.Resource resource : expected) {
            org.junit.Assert.assertTrue(names.contains(resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID)));
        }
        EasyMock.verify(taskAttemptFetcher);
    }

    @org.junit.Test
    public void testTaskAttemptFetcher() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = new java.util.HashSet<>();
        requestedIds.add(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID);
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.TaskAttemptResourceProviderTest.TestTaskAttemptResourceProvider(requestedIds, keyPropertyIds);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestedIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID).equals("taskattempt1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String workflowId = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals("taskattempt1", workflowId);
        }
    }

    private static org.apache.ambari.server.controller.spi.Resource createTaskAttemptResponse(java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId, java.lang.String taskAttemptId) {
        org.apache.ambari.server.controller.spi.Resource r = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt);
        r.setProperty(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_CLUSTER_NAME_PROPERTY_ID, clusterName);
        r.setProperty(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_WORKFLOW_ID_PROPERTY_ID, workflowId);
        r.setProperty(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_JOB_ID_PROPERTY_ID, jobId);
        r.setProperty(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID, taskAttemptId);
        return r;
    }

    private static class TestTaskAttemptResourceProvider extends org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider {
        protected TestTaskAttemptResourceProvider(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds) {
            super(propertyIds, keyPropertyIds, null);
            this.taskAttemptFetcher = new org.apache.ambari.server.controller.internal.TaskAttemptResourceProviderTest.TestTaskAttemptResourceProvider.TestTaskAttemptFetcher();
        }

        private class TestTaskAttemptFetcher extends org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.PostgresTaskAttemptFetcher {
            java.sql.ResultSet rs = null;

            public TestTaskAttemptFetcher() {
                super(((org.apache.ambari.server.controller.jdbc.ConnectionFactory) (null)));
            }

            @java.lang.Override
            protected java.sql.ResultSet getResultSet(java.util.Set<java.lang.String> requestedIds, java.lang.String workflowId, java.lang.String jobId, java.lang.String taskAttemptId) throws java.sql.SQLException {
                rs = EasyMock.createMock(java.sql.ResultSet.class);
                EasyMock.expect(rs.next()).andReturn(true).once();
                EasyMock.expect(rs.getString(getDBField(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID).toString())).andReturn("taskattempt1").once();
                EasyMock.expect(rs.next()).andReturn(false).once();
                rs.close();
                EasyMock.expectLastCall().once();
                EasyMock.replay(rs);
                return rs;
            }

            @java.lang.Override
            protected void close() {
                EasyMock.verify(rs);
            }
        }
    }
}