package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class JobResourceProviderTest {
    @org.junit.Test
    public void testGetResources() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> expected = new java.util.HashSet<>();
        expected.add(org.apache.ambari.server.controller.internal.JobResourceProviderTest.createJobResponse("Cluster100", "workflow1", "job1"));
        expected.add(org.apache.ambari.server.controller.internal.JobResourceProviderTest.createJobResponse("Cluster100", "workflow2", "job2"));
        expected.add(org.apache.ambari.server.controller.internal.JobResourceProviderTest.createJobResponse("Cluster100", "workflow2", "job3"));
        java.util.Set<java.lang.String> propertyIds = org.apache.ambari.server.controller.internal.JobResourceProvider.propertyIds;
        org.apache.ambari.server.controller.internal.JobResourceProvider.JobFetcher jobFetcher = EasyMock.createMock(org.apache.ambari.server.controller.internal.JobResourceProvider.JobFetcher.class);
        EasyMock.expect(jobFetcher.fetchJobDetails(propertyIds, null, "workflow2", null)).andReturn(expected).once();
        EasyMock.replay(jobFetcher);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.JobResourceProvider(jobFetcher);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ID_PROPERTY_ID).equals("workflow2").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(3, resources.size());
        java.util.Set<java.lang.String> names = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CLUSTER_NAME_PROPERTY_ID)));
            org.junit.Assert.assertEquals("Cluster100", clusterName);
            names.add(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID))));
        }
        for (org.apache.ambari.server.controller.spi.Resource resource : expected) {
            org.junit.Assert.assertTrue(names.contains(resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID)));
        }
        EasyMock.verify(jobFetcher);
    }

    @org.junit.Test
    public void testJobFetcher1() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = new java.util.HashSet<>();
        requestedIds.add(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.JobResourceProviderTest.TestJobResourceProvider(1);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestedIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID).equals("job1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String workflowId = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals("job1", workflowId);
        }
    }

    @org.junit.Test
    public void testJobFetcher2() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = new java.util.HashSet<>();
        requestedIds.add(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID);
        requestedIds.add(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.JobResourceProviderTest.TestJobResourceProvider(2);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestedIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID).equals("job1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String workflowId = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals("job1", workflowId);
            org.junit.Assert.assertEquals(42L, resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID));
        }
    }

    @org.junit.Test
    public void testJobFetcher3() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = new java.util.HashSet<>();
        requestedIds.add(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID);
        requestedIds.add(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.JobResourceProviderTest.TestJobResourceProvider(3);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestedIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID).equals("job1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String workflowId = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals("job1", workflowId);
            org.junit.Assert.assertEquals(1L, resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID));
        }
    }

    @org.junit.Test
    public void testJobFetcher4() throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = new java.util.HashSet<>();
        requestedIds.add(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID);
        requestedIds.add(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID);
        requestedIds.add(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID);
        org.apache.ambari.server.controller.spi.ResourceProvider provider = new org.apache.ambari.server.controller.internal.JobResourceProviderTest.TestJobResourceProvider(4);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(requestedIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID).equals("job1").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(request, predicate);
        org.junit.Assert.assertEquals(1, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            java.lang.String workflowId = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID)));
            org.junit.Assert.assertEquals("job1", workflowId);
            org.junit.Assert.assertEquals(42L, resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID));
            org.junit.Assert.assertEquals(0L, resource.getPropertyValue(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID));
        }
    }

    private static org.apache.ambari.server.controller.spi.Resource createJobResponse(java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId) {
        org.apache.ambari.server.controller.spi.Resource r = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Job);
        r.setProperty(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CLUSTER_NAME_PROPERTY_ID, clusterName);
        r.setProperty(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ID_PROPERTY_ID, workflowId);
        r.setProperty(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID, jobId);
        return r;
    }

    private static class TestJobResourceProvider extends org.apache.ambari.server.controller.internal.JobResourceProvider {
        protected TestJobResourceProvider(int type) {
            super();
            this.jobFetcher = new org.apache.ambari.server.controller.internal.JobResourceProviderTest.TestJobResourceProvider.TestJobFetcher(type);
        }

        private class TestJobFetcher extends org.apache.ambari.server.controller.internal.JobResourceProvider.PostgresJobFetcher {
            java.sql.ResultSet rs = null;

            int type;

            public TestJobFetcher(int type) {
                super(((org.apache.ambari.server.controller.jdbc.ConnectionFactory) (null)));
                this.type = type;
            }

            @java.lang.Override
            protected java.sql.ResultSet getResultSet(java.util.Set<java.lang.String> requestedIds, java.lang.String workflowId, java.lang.String jobId) throws java.sql.SQLException {
                rs = EasyMock.createMock(java.sql.ResultSet.class);
                EasyMock.expect(rs.next()).andReturn(true).once();
                EasyMock.expect(rs.getString(getDBField(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID).toString())).andReturn("job1").once();
                if (type > 1)
                    EasyMock.expect(rs.getLong(getDBField(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID).toString())).andReturn(42L).once();

                if (type == 3)
                    EasyMock.expect(rs.getLong(org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.FINISHTIME.toString())).andReturn(43L).once();

                if (type == 4)
                    EasyMock.expect(rs.getLong(org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.FINISHTIME.toString())).andReturn(41L).once();

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