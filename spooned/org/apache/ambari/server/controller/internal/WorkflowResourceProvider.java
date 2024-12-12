package org.apache.ambari.server.controller.internal;
public class WorkflowResourceProvider extends org.apache.ambari.server.controller.internal.AbstractJDBCResourceProvider<org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.class);

    protected static final java.lang.String WORKFLOW_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "cluster_name");

    protected static final java.lang.String WORKFLOW_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "workflow_id");

    protected static final java.lang.String WORKFLOW_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "name");

    protected static final java.lang.String WORKFLOW_USER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "user_name");

    protected static final java.lang.String WORKFLOW_START_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "start_time");

    protected static final java.lang.String WORKFLOW_LAST_UPDATE_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "last_update_time");

    protected static final java.lang.String WORKFLOW_ELAPSED_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "elapsed_time");

    protected static final java.lang.String WORKFLOW_INPUT_BYTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "input_bytes");

    protected static final java.lang.String WORKFLOW_OUTPUT_BYTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "output_bytes");

    protected static final java.lang.String WORKFLOW_NUM_JOBS_TOTAL_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "num_jobs_total");

    protected static final java.lang.String WORKFLOW_NUM_JOBS_COMPLETED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "num_jobs_completed");

    protected static final java.lang.String WORKFLOW_PARENT_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "parent_id");

    protected static final java.lang.String WORKFLOW_CONTEXT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Workflow", "context");

    protected org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFetcher workflowFetcher;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CLUSTER_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_USER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_START_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_LAST_UPDATE_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ELAPSED_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_INPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_OUTPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NUM_JOBS_TOTAL_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NUM_JOBS_COMPLETED_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_PARENT_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CONTEXT_PROPERTY_ID);

    protected WorkflowResourceProvider() {
        super(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.keyPropertyIds);
        this.workflowFetcher = new org.apache.ambari.server.controller.internal.WorkflowResourceProvider.PostgresWorkflowFetcher(new org.apache.ambari.server.controller.jdbc.JobHistoryPostgresConnectionFactory());
    }

    protected WorkflowResourceProvider(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFetcher workflowFetcher) {
        super(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.keyPropertyIds);
        this.workflowFetcher = workflowFetcher;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> predicatePropertieSet = getPropertyMaps(predicate);
        for (java.util.Map<java.lang.String, java.lang.Object> predicateProperties : predicatePropertieSet) {
            java.lang.String clusterName = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CLUSTER_NAME_PROPERTY_ID)));
            java.lang.String workflowId = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID)));
            resourceSet.addAll(workflowFetcher.fetchWorkflows(requestedIds, clusterName, workflowId));
        }
        return resourceSet;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException();
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.WorkflowResourceProvider.keyPropertyIds;
    }

    public interface WorkflowFetcher {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> fetchWorkflows(java.util.Set<java.lang.String> requestedIds, java.lang.String clusterName, java.lang.String workflowId);
    }

    protected class PostgresWorkflowFetcher implements org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFetcher {
        private static final java.lang.String WORKFLOW_TABLE_NAME = "workflow";

        private org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory;

        private java.sql.Connection db;

        private java.sql.PreparedStatement ps;

        public PostgresWorkflowFetcher(org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory) {
            this.connectionFactory = connectionFactory;
            this.db = null;
            this.ps = null;
        }

        protected java.sql.ResultSet getResultSet(java.util.Set<java.lang.String> requestedIds, java.lang.String workflowId) throws java.sql.SQLException {
            db = null;
            ps = null;
            db = connectionFactory.getConnection();
            if (workflowId == null) {
                ps = db.prepareStatement((("SELECT " + getDBFieldString(requestedIds)) + " FROM ") + org.apache.ambari.server.controller.internal.WorkflowResourceProvider.PostgresWorkflowFetcher.WORKFLOW_TABLE_NAME);
            } else {
                ps = db.prepareStatement(((((("SELECT " + getDBFieldString(requestedIds)) + " FROM ") + org.apache.ambari.server.controller.internal.WorkflowResourceProvider.PostgresWorkflowFetcher.WORKFLOW_TABLE_NAME) + " WHERE ") + org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.WORKFLOWID) + " = ?");
                ps.setString(1, workflowId);
            }
            return ps.executeQuery();
        }

        protected void close() {
            if (ps != null)
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.controller.internal.WorkflowResourceProvider.LOG.error("Exception while closing statment", e);
                }

            if (db != null)
                try {
                    db.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.controller.internal.WorkflowResourceProvider.LOG.error("Exception while closing connection", e);
                }

        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> fetchWorkflows(java.util.Set<java.lang.String> requestedIds, java.lang.String clusterName, java.lang.String workflowId) {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> workflows = new java.util.HashSet<>();
            java.sql.ResultSet rs = null;
            try {
                rs = getResultSet(requestedIds, workflowId);
                while (rs.next()) {
                    org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Workflow);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NAME_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_USER_NAME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_START_TIME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_LAST_UPDATE_TIME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ELAPSED_TIME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_INPUT_BYTES_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_OUTPUT_BYTES_PROPERTY_ID, rs, requestedIds);
                    setInt(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NUM_JOBS_TOTAL_PROPERTY_ID, rs, requestedIds);
                    setInt(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NUM_JOBS_COMPLETED_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_PARENT_ID_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CONTEXT_PROPERTY_ID, rs, requestedIds);
                    workflows.add(resource);
                } 
            } catch (java.sql.SQLException e) {
                if (org.apache.ambari.server.controller.internal.WorkflowResourceProvider.LOG.isDebugEnabled())
                    org.apache.ambari.server.controller.internal.WorkflowResourceProvider.LOG.debug("Caught exception getting resource.", e);

                return java.util.Collections.emptySet();
            } finally {
                if (rs != null)
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                        org.apache.ambari.server.controller.internal.WorkflowResourceProvider.LOG.error("Exception while closing ResultSet", e);
                    }

                close();
            }
            return workflows;
        }
    }

    enum WorkflowFields {

        WORKFLOWID,
        WORKFLOWNAME,
        USERNAME,
        STARTTIME,
        LASTUPDATETIME,
        DURATION,
        NUMJOBSTOTAL,
        NUMJOBSCOMPLETED,
        INPUTBYTES,
        OUTPUTBYTES,
        PARENTWORKFLOWID,
        WORKFLOWCONTEXT;}

    @java.lang.Override
    protected java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields> getDBFieldMap() {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields> dbFields = new java.util.HashMap<>();
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.WORKFLOWID);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.WORKFLOWNAME);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_USER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.USERNAME);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_START_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.STARTTIME);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_LAST_UPDATE_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.LASTUPDATETIME);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_ELAPSED_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.DURATION);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_INPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.INPUTBYTES);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_OUTPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.OUTPUTBYTES);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NUM_JOBS_TOTAL_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.NUMJOBSTOTAL);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_NUM_JOBS_COMPLETED_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.NUMJOBSCOMPLETED);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_PARENT_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.PARENTWORKFLOWID);
        dbFields.put(org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WORKFLOW_CONTEXT_PROPERTY_ID, org.apache.ambari.server.controller.internal.WorkflowResourceProvider.WorkflowFields.WORKFLOWCONTEXT);
        return dbFields;
    }
}