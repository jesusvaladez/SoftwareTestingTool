package org.apache.ambari.server.controller.internal;
public class JobResourceProvider extends org.apache.ambari.server.controller.internal.AbstractJDBCResourceProvider<org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.JobResourceProvider.class);

    protected static final java.lang.String JOB_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "cluster_name");

    protected static final java.lang.String JOB_WORKFLOW_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "workflow_id");

    protected static final java.lang.String JOB_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "job_id");

    protected static final java.lang.String JOB_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "name");

    protected static final java.lang.String JOB_STATUS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "status");

    protected static final java.lang.String JOB_USER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "user_name");

    protected static final java.lang.String JOB_SUBMIT_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "submit_time");

    protected static final java.lang.String JOB_ELAPSED_TIME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "elapsed_time");

    protected static final java.lang.String JOB_MAPS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "maps");

    protected static final java.lang.String JOB_REDUCES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "reduces");

    protected static final java.lang.String JOB_INPUT_BYTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "input_bytes");

    protected static final java.lang.String JOB_OUTPUT_BYTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "output_bytes");

    protected static final java.lang.String JOB_CONF_PATH_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "conf_path");

    protected static final java.lang.String JOB_WORKFLOW_ENTITY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Job", "workflow_entity_name");

    protected org.apache.ambari.server.controller.internal.JobResourceProvider.JobFetcher jobFetcher;

    protected static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CLUSTER_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Job, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID).build();

    protected static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_USER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_MAPS_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_REDUCES_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_INPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_OUTPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CONF_PATH_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ENTITY_NAME_PROPERTY_ID);

    protected JobResourceProvider() {
        super(org.apache.ambari.server.controller.internal.JobResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.JobResourceProvider.keyPropertyIds);
        jobFetcher = new org.apache.ambari.server.controller.internal.JobResourceProvider.PostgresJobFetcher(new org.apache.ambari.server.controller.jdbc.JobHistoryPostgresConnectionFactory());
    }

    protected JobResourceProvider(org.apache.ambari.server.controller.internal.JobResourceProvider.JobFetcher jobFetcher) {
        super(org.apache.ambari.server.controller.internal.JobResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.JobResourceProvider.keyPropertyIds);
        this.jobFetcher = jobFetcher;
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
            java.lang.String clusterName = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CLUSTER_NAME_PROPERTY_ID)));
            java.lang.String workflowId = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ID_PROPERTY_ID)));
            java.lang.String jobId = ((java.lang.String) (predicateProperties.get(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID)));
            resourceSet.addAll(jobFetcher.fetchJobDetails(requestedIds, clusterName, workflowId, jobId));
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
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.JobResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.JobResourceProvider.keyPropertyIds;
    }

    public interface JobFetcher {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> fetchJobDetails(java.util.Set<java.lang.String> requestedIds, java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId);
    }

    protected class PostgresJobFetcher implements org.apache.ambari.server.controller.internal.JobResourceProvider.JobFetcher {
        private static final java.lang.String JOB_TABLE_NAME = "job";

        private org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory;

        java.sql.Connection db;

        java.sql.PreparedStatement ps;

        public PostgresJobFetcher(org.apache.ambari.server.controller.jdbc.ConnectionFactory connectionFactory) {
            this.connectionFactory = connectionFactory;
            this.db = null;
            this.ps = null;
        }

        protected java.sql.ResultSet getResultSet(java.util.Set<java.lang.String> requestedIds, java.lang.String workflowId, java.lang.String jobId) throws java.sql.SQLException {
            db = null;
            ps = null;
            db = connectionFactory.getConnection();
            java.lang.String fields = getDBFieldString(requestedIds);
            if (requestedIds.contains(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID) && (!requestedIds.contains(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID)))
                fields += "," + getDBField(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID);

            if (jobId == null) {
                ps = db.prepareStatement(((((("SELECT " + fields) + " FROM ") + org.apache.ambari.server.controller.internal.JobResourceProvider.PostgresJobFetcher.JOB_TABLE_NAME) + " WHERE ") + org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.WORKFLOWID) + " = ?");
                ps.setString(1, workflowId);
            } else {
                ps = db.prepareStatement(((((("SELECT " + fields) + " FROM ") + org.apache.ambari.server.controller.internal.JobResourceProvider.PostgresJobFetcher.JOB_TABLE_NAME) + " WHERE ") + org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.JOBID) + " = ?");
                ps.setString(1, jobId);
            }
            return ps.executeQuery();
        }

        protected void close() {
            if (ps != null)
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.controller.internal.JobResourceProvider.LOG.error("Exception while closing statment", e);
                }

            if (db != null)
                try {
                    db.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.controller.internal.JobResourceProvider.LOG.error("Exception while closing connection", e);
                }

        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> fetchJobDetails(java.util.Set<java.lang.String> requestedIds, java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId) {
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> jobs = new java.util.HashSet<>();
            java.sql.ResultSet rs = null;
            try {
                rs = getResultSet(requestedIds, workflowId, jobId);
                while (rs.next()) {
                    org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Job);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CLUSTER_NAME_PROPERTY_ID, clusterName, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_NAME_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_STATUS_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_USER_NAME_PROPERTY_ID, rs, requestedIds);
                    if (requestedIds.contains(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID) || requestedIds.contains(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID)) {
                        long submitTime = rs.getLong(org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.SUBMITTIME.toString());
                        if (requestedIds.contains(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID))
                            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID, submitTime, requestedIds);

                        if (requestedIds.contains(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID)) {
                            long finishTime = rs.getLong(org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.FINISHTIME.toString());
                            if (finishTime > submitTime)
                                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID, finishTime - submitTime, requestedIds);
                            else
                                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID, 0L, requestedIds);

                        }
                    }
                    setInt(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_MAPS_PROPERTY_ID, rs, requestedIds);
                    setInt(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_REDUCES_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_INPUT_BYTES_PROPERTY_ID, rs, requestedIds);
                    setLong(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_OUTPUT_BYTES_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CONF_PATH_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ID_PROPERTY_ID, rs, requestedIds);
                    setString(resource, org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ENTITY_NAME_PROPERTY_ID, rs, requestedIds);
                    jobs.add(resource);
                } 
            } catch (java.sql.SQLException e) {
                if (org.apache.ambari.server.controller.internal.JobResourceProvider.LOG.isDebugEnabled())
                    org.apache.ambari.server.controller.internal.JobResourceProvider.LOG.debug("Caught exception getting resource.", e);

                return java.util.Collections.emptySet();
            } finally {
                if (rs != null)
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                        org.apache.ambari.server.controller.internal.JobResourceProvider.LOG.error("Exception while closing ResultSet", e);
                    }

                close();
            }
            return jobs;
        }
    }

    enum JobFields {

        JOBID,
        JOBNAME,
        STATUS,
        USERNAME,
        SUBMITTIME,
        FINISHTIME,
        MAPS,
        REDUCES,
        INPUTBYTES,
        OUTPUTBYTES,
        CONFPATH,
        WORKFLOWID,
        WORKFLOWENTITYNAME;}

    @java.lang.Override
    protected java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields> getDBFieldMap() {
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields> dbFields = new java.util.HashMap<>();
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.WORKFLOWID);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.JOBID);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.JOBNAME);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_STATUS_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.STATUS);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_USER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.USERNAME);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_SUBMIT_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.SUBMITTIME);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_ELAPSED_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.FINISHTIME);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_MAPS_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.MAPS);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_REDUCES_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.REDUCES);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_INPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.INPUTBYTES);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_OUTPUT_BYTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.OUTPUTBYTES);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_CONF_PATH_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.CONFPATH);
        dbFields.put(org.apache.ambari.server.controller.internal.JobResourceProvider.JOB_WORKFLOW_ENTITY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.JobResourceProvider.JobFields.WORKFLOWENTITYNAME);
        return dbFields;
    }
}