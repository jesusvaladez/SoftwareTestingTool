package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class WorkflowResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> expected = new java.util.HashSet<>();
        expected.add(org.apache.ambari.server.controller.internal.WorkflowResourceProviderTest.createWorkflowResponse("Cluster100", "workflow1"));
        expected.add(org.apache.ambari.server.controller.internal.WorkflowResourceProviderTest.createWorkflowResponse("Cluster100", "workflow2"));
        expected.add(org.apache.ambari.server.controller.internal.WorkflowResourceProviderTest.createWorkflowResponse("Cluster100", "workflow3"));
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.Workflow;
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CLUSTER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_USER_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_START_TIME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_LAST_UPDATE_TIME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ELAPSED_TIME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_INPUT_BYTES_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_OUTPUT_BYTES_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NUM_JOBS_TOTAL_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NUM_JOBS_COMPLETED_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_PARENT_ID_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CONTEXT_PROPERTY_ID);
        org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFetcher workflowFetcher = EasyMock.createMock(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFetcher.class);
        EasyMock.expect(workflowFetcher.fetchWorkflows(propertyIds, "Cluster100", null)).andReturn(expected).once();
        EasyMock.replay(workflowFetcher);
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.WorkflowResourceProvider(workflowFetcher);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CLUSTER_NAME_PROPERTY_ID).equals("Cluster100").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            names.add(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID))));
        }
        for (org.apache.ambari.server.controller.spi.Resource resource : expected) {
            org.junit.Assert.assertTrue(names.contains(resource.getPropertyValue(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID)));
        }
        EasyMock.verify(workflowFetcher);
    }

    @org.junit.Test
    public void testWorkflowFetcher() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = new java.util.HashSet<>();
        requestedIds.add(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.WorkflowResourceProviderTest.TestWorkflowResourceProvider();
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestedIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID).equals("workflow1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String workflowId = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals("workflow1", workflowId);
        }
    }

    private static org.apache.ambari.server.controller.spi.Resource createWorkflowResponse(java.lang.String clusterName, java.lang.String workflowId) {
        org.apache.ambari.server.controller.spi.Resource r = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Workflow);
        r.setProperty(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CLUSTER_NAME_PROPERTY_ID, clusterName);
        r.setProperty(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID, workflowId);
        return r;
    }

    private static class TestWorkflowResourceProvider extends org.apache.ambari.server.controller.internal.WorkflowResourceProvider {
        protected TestWorkflowResourceProvider() {
            super(null);
            this.workflowFetcher = new org.apache.ambari.server.controller.internal.WorkflowResourceProviderTest.TestWorkflowResourceProvider.TestWorkflowFetcher();
        }

        private class TestWorkflowFetcher extends org.apache.ambari.server.controller.internal.WorkflowResourceProvider.PostgresWorkflowFetcher {
            java.sql.ResultSet rs = null;

            public TestWorkflowFetcher() {
                super(((org.apache.ambari.server.controller.jdbc.ConnectionFactory) (null)));
            }

            @java.lang.Override
            protected java.sql.ResultSet getResultSet(java.util.Set<java.lang.String> requestedIds, java.lang.String workflowId) throws java.sql.SQLException {
                rs = EasyMock.createMock(java.sql.ResultSet.class);
                EasyMock.expect(rs.next()).andReturn(true).once();
                EasyMock.expect(rs.getString(getDBField(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID).toString())).andReturn("workflow1").once();
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