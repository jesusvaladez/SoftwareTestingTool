package org.apache.ambari.server.controller.internal;
public class TaskAttemptResourceProvider extends org.apache.ambari.server.controller.internal.AbstractJDBCResourceProvider<org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.class);

    protected static final java.lang.String TASK_ATTEMPT_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "cluster_name");

    protected static final java.lang.String TASK_ATTEMPT_WORKFLOW_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "workflow_id");

    protected static final java.lang.String TASK_ATTEMPT_JOB_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "job_id");

    protected static final java.lang.String TASK_ATTEMPT_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "task_attempt_id");

    protected static final java.lang.String TASK_ATTEMPT_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "type");

    protected static final java.lang.String TASK_ATTEMPT_START_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "start_time");

    protected static final java.lang.String TASK_ATTEMPT_FINISH_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "finish_time");

    protected static final java.lang.String TASK_ATTEMPT_MAP_FINISH_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "map_finish_time");

    protected static final java.lang.String TASK_ATTEMPT_SHUFFLE_FINISH_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "shuffle_finish_time");

    protected static final java.lang.String TASK_ATTEMPT_SORT_FINISH_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "sort_finish_fime");

    protected static final java.lang.String TASK_ATTEMPT_INPUT_BYTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "input_bytes");

    protected static final java.lang.String TASK_ATTEMPT_OUTPUT_BYTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "output_bytes");

    protected static final java.lang.String TASK_ATTEMPT_STATUS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "status");

    protected static final java.lang.String TASK_ATTEMPT_LOCALITY_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("TaskAttempt", "locality");

    protected org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFetcher taskAttemptFetcher;

    protected static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_CLUSTER_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_WORKFLOW_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Job, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_JOB_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID).build();

    protected static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_WORKFLOW_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_JOB_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_START_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_FINISH_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_MAP_FINISH_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_SHUFFLE_FINISH_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_SORT_FINISH_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_INPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_OUTPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_LOCALITY_PROPERTY_ID);

    protected TaskAttemptResourceProvider() {
        super(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.keyPropertyIds);
        taskAttemptFetcher = new org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.PostgresTaskAttemptFetcher(new org.apache.ambari.server.controller.jdbc.JobHistoryPostgresConnectionFactory());
    }

    protected TaskAttemptResourceProvider(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFetcher taskAttemptFetcher) {
        super(propertyIds, keyPropertyIds);
        this.taskAttemptFetcher = taskAttemptFetcher;
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
            java.lang.String clusterName = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_CLUSTER_NAME_PROPERTY_ID)));
            java.lang.String workflowId = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_WORKFLOW_ID_PROPERTY_ID)));
            java.lang.String jobId = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_JOB_ID_PROPERTY_ID)));
            java.lang.String taskAttemptId = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID)));
            resourceSet.addAll(taskAttemptFetcher.fetchTaskAttemptDetails(requestedIds, clusterName, workflowId, jobId, taskAttemptId));
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
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.keyPropertyIds;
    }

    public interface TaskAttemptFetcher {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> fetchTaskAttemptDetails(java.util.Set<java.lang.String> requestedIds, java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId, java.lang.String taskAttemptId);
    }

    protected class PostgresTaskAttemptFetcher implements org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFetcher {
        private static final java.lang.String TASK_ATTEMPT_TABLE_NAME = "taskattempt";

        private org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory;

        java.sql.Connection db;

        java.sql.PreparedStatement ps;

        public PostgresTaskAttemptFetcher(org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory) {
            this.connectionFactory = connectionFactory;
            this.db = null;
            this.ps = null;
        }

        protected java.sql.ResultSet getResultSet(java.util.Set<java.lang.String> requestedIds, java.lang.String workflowId, java.lang.String jobId, java.lang.String taskAttemptId) throws java.sql.SQLException {
            db = null;
            ps = null;
            db = connectionFactory.getConnection();
            if (taskAttemptId == null) {
                ps = db.prepareStatement(((((("SELECT " + getDBFieldString(requestedIds)) + " FROM ") + org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.PostgresTaskAttemptFetcher.TASK_ATTEMPT_TABLE_NAME) + " WHERE ") + org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.JOBID) + " = ? ");
                ps.setString(1, jobId);
            } else {
                ps = db.prepareStatement(((((("SELECT " + getDBFieldString(requestedIds)) + " FROM ") + org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.PostgresTaskAttemptFetcher.TASK_ATTEMPT_TABLE_NAME) + " WHERE ") + org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.TASKATTEMPTID) + " = ? ");
                ps.setString(1, taskAttemptId);
            }
            return ps.executeQuery();
        }

        protected void close() {
            if (ps != null)
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.LOG.error("Exception while closing statment", e);
                }

            if (db != null)
                try {
                    db.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.LOG.error("Exception while closing connection", e);
                }

        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> fetchTaskAttemptDetails(java.util.Set<java.lang.String> requestedIds, java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId, java.lang.String taskAttemptId) {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> taskAttempts = new java.util.HashSet<>();
            java.sql.ResultSet rs = null;
            try {
                rs = getResultSet(requestedIds, workflowId, jobId, taskAttemptId);
                while (rs.next()) {
                    org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_WORKFLOW_ID_PROPERTY_ID, workflowId, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_JOB_ID_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_TYPE_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_START_TIME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_FINISH_TIME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_MAP_FINISH_TIME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_SHUFFLE_FINISH_TIME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_SORT_FINISH_TIME_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_INPUT_BYTES_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_OUTPUT_BYTES_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_STATUS_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_LOCALITY_PROPERTY_ID, rs, requestedIds);
                    taskAttempts.add(resource);
                } 
            } catch (java.sql.SQLException e) {
                if (org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.LOG.isDebugEnabled())
                    org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.LOG.debug("Caught exception getting resource.", e);

                return java.util.Collections.emptySet();
            } finally {
                if (rs != null)
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                        org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.LOG.error("Exception while closing ResultSet", e);
                    }

                close();
            }
            return taskAttempts;
        }
    }

    enum TaskAttemptFields {

        JOBID,
        TASKATTEMPTID,
        TASKTYPE,
        STARTTIME,
        FINISHTIME,
        MAPFINISHTIME,
        SHUFFLEFINISHTIME,
        SORTFINISHTIME,
        INPUTBYTES,
        OUTPUTBYTES,
        STATUS,
        LOCALITY;}

    @java.lang.Override
    protected java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields> getDBFieldMap() {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields> dbFields = new java.util.HashMap<>();
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_JOB_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.JOBID);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.TASKATTEMPTID);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.TASKTYPE);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_START_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.STARTTIME);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_FINISH_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.FINISHTIME);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_MAP_FINISH_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.MAPFINISHTIME);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_SHUFFLE_FINISH_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.SHUFFLEFINISHTIME);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_SORT_FINISH_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.SORTFINISHTIME);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_INPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.INPUTBYTES);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_OUTPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.OUTPUTBYTES);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.STATUS);
        dbFields.put(org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TASK_ATTEMPT_LOCALITY_PROPERTY_ID, org.apache.ambari.server.controller.internal.TaskAttemptResourceProvider.TaskAttemptFields.LOCALITY);
        return dbFields;
    }
}